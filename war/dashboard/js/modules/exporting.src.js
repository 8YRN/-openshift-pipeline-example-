
/** 
 * @license Highcharts JS v2.0.5 (2010-09-17)
 * Exporting module
 * 
 * (c) 2010 Torstein Hønsi
 * 
 * License: www.highcharts.com/license
 */

// JSLint options:
/*global Highcharts, document, window, Math, setTimeout */

(function() { // encapsulate

// create shortcuts
var HC = Highcharts,
	Chart = HC.Chart,
	addEvent = HC.addEvent,
	defaultOptions = HC.defaultOptions,
	createElement = HC.createElement,
	discardElement = HC.discardElement,
	css = HC.css,
	merge = HC.merge,
	each = HC.each,
	extend = HC.extend,
	math = Math,
	mathMax = math.max,
	doc = document,
	win = window,
	M = 'M',
	L = 'L',
	DIV = 'div',
	HIDDEN = 'hidden',
	NONE = 'none',
	PREFIX = 'highcharts-',
	ABSOLUTE = 'absolute',
	PX = 'px',



	// Add language and get the defaultOptions
	defaultOptions = HC.setOptions({
		lang: {
			downloadPNG: 'Download PNG image',
			downloadJPEG: 'Download JPEG image',
			downloadPDF: 'Download PDF document',
			downloadSVG: 'Download SVG vector image',
			exportButtonTitle: 'Export to raster or vector image',
			printButtonTitle: 'Print the chart'
		}
	});

// Buttons and menus are collected in a separate config option set called 'navigation'.
// This can be extended later to add control buttons like zoom and pan right click menus.
defaultOptions.navigation = {
	menuStyle: {
		border: '1px solid #A0A0A0',
		background: '#FFFFFF'
	},
	menuItemStyle: {
		padding: '0 5px',
		background: NONE,
		color: '#303030'
	},
	menuItemHoverStyle: {
		background: '#4572A5',
		color: '#FFFFFF'
	},
	
	buttonOptions: {
		align: 'right',
		backgroundColor: {
			linearGradient: [0, 0, 0, 20],
			stops: [
				[0.4, '#F7F7F7'],
				[0.6, '#E3E3E3']
			]
		},
		borderColor: '#B0B0B0',
		borderRadius: 3,
		borderWidth: 1,
		//enabled: true,
		height: 20,
		hoverBorderColor: '#909090',
		hoverSymbolFill: '#81A7CF',
		hoverSymbolStroke: '#4572A5',
		symbolFill: '#E0E0E0',
		//symbolSize: 12,
		symbolStroke: '#A0A0A0',
		//symbolStrokeWidth: 1,
		symbolX: 11.5,
		symbolY: 10.5,
		verticalAlign: 'top',
		width: 24,
		y: 10		
	}
};



// Add the export related options
defaultOptions.exporting = {
	//enabled: true,
	//filename: 'chart',
	type: 'image/png',
	url: 'http://export.highcharts.com/',
	width: 800,
	buttons: {
		exportButton: {
			//enabled: true,
			symbol: 'exportIcon',
			x: -10,
			symbolFill: '#A8BF77',
			hoverSymbolFill: '#768F3E',
			_titleKey: 'exportButtonTitle',
			menuItems: [{
				textKey: 'downloadPNG',
				onclick: function() {
					this.exportChart();
				}
			}, {
				textKey: 'downloadJPEG',
				onclick: function() {
					this.exportChart({
						type: 'image/jpeg'
					});
				}
			}, {
				textKey: 'downloadPDF',
				onclick: function() {
					this.exportChart({
						type: 'application/pdf'
					});
				}
			}, {
				textKey: 'downloadSVG',
				onclick: function() {
					this.exportChart({
						type: 'image/svg+xml'
					});
				}
			}/*, {
				text: 'View SVG',
				onclick: function() {
					var svg = this.getSVG()
						.replace(/</g, '\n&lt;')
						.replace(/>/g, '&gt;');
						
					doc.body.innerHTML = '<pre>'+ svg +'</pre>';
				}
			}*/]
			
		},
		printButton: {
			//enabled: true,
			symbol: 'printIcon',
			x: -36,
			symbolFill: '#B5C9DF',
			hoverSymbolFill: '#779ABF',
			_titleKey: 'printButtonTitle',
			onclick: function() {
				this.print();
			}
		}
	}
};



extend (Chart.prototype, {
	/**
	 * Return an SVG representation of the chart
	 * 
	 * @param additionalOptions {Object} Additional chart options for the generated SVG representation
	 */	
	 getSVG: function(additionalOptions) {
		var chart = this,
			chartCopy,
			sandbox,
			svg,
			seriesOptions,
			pointOptions,
			pointMarker,
			options = merge(chart.options, additionalOptions); // copy the options and add extra options
		
		// IE compatibility hack for generating SVG content that it doesn't really understand
		if (!doc.createElementNS) {
			doc.createElementNS = function(ns, tagName) {
				var elem = doc.createElement(tagName);
				elem.getBBox = function() {
					return chart.renderer.Element.prototype.getBBox.apply({ element: elem });
				};
				return elem;
			};
		}
		
		// create a sandbox where a new chart will be generated
		sandbox = createElement(DIV, null, {
			position: ABSOLUTE,
			top: '-9999em',
			width: chart.chartWidth + PX,
			height: chart.chartHeight + PX
		}, doc.body);
		
		// override some options
		extend(options.chart, {
			renderTo: sandbox,
			renderer: 'SVG'
		});
		options.exporting.enabled = false; // hide buttons in print
		options.chart.plotBackgroundImage = null; // the converter doesn't handle images
		
		// prepare for replicating the chart
		options.series = [];
		each (chart.series, function(serie) {
			seriesOptions = serie.options;			
			
			seriesOptions.animation = false; // turn off animation
			seriesOptions.showCheckbox = false;
			
			seriesOptions.data = [];
			each(serie.data, function(point) {
				pointOptions = point.config == null || typeof point.config == 'number' ?
					{ y: point.y } :
					point.config;
				pointOptions.x = point.x;
				seriesOptions.data.push(pointOptions); // copy fresh updated data
								
				// remove image markers
				pointMarker = point.config && point.config.marker;
				if (pointMarker && /^url\(/.test(pointMarker.symbol)) { 
					delete pointMarker.symbol;
				}
			});	
			
			options.series.push(seriesOptions);
		});
		
		// generate the chart copy
		chartCopy = new Highcharts.Chart(options);
		
		// get the SVG from the container's innerHTML
		svg = chartCopy.container.innerHTML;
		
		// free up memory
		options = null;
		chartCopy.destroy();
		discardElement(sandbox);
		
		// sanitize
		svg = svg.
			replace(/zIndex="[^"]+"/g, ''). 
			replace(/isShadow="[^"]+"/g, '').
			replace(/symbolName="[^"]+"/g, '').
			replace(/jQuery[0-9]+="[^"]+"/g, '').
			replace(/isTracker="[^"]+"/g, '').
			replace(/url\([^#]+#/g, 'url(#').
			
			// IE specific
			replace(/id=([^" >]+)/g, 'id="$1"'). 
			replace(/class=([^" ]+)/g, 'class="$1"').
			replace(/ transform /g, ' ').
			replace(/:path/g, 'path').
			replace(/style="([^"]+)"/g, function(s) {
				return s.toLowerCase();
			});
			
		// IE9 beta bugs with innerHTML. Test again with final IE9.
		svg = svg.replace(/(url\(#highcharts-[0-9]+)&quot;/g, '$1')
			.replace(/&quot;/g, "'");
		if (svg.match(/ xmlns="/g).length == 2) {
			svg = svg.replace(/xmlns="[^"]+"/, '');
		}
			
		return svg;
	},
	
	/**
	 * Submit the SVG representation of the chart to the server
	 * @param {Object} options Exporting options. Possible members are url, type and width.
	 * @param {Object} chartOptions Additional chart options for the SVG representation of the chart
	 */
	exportChart: function(options, chartOptions) {
		var form,
			chart = this,
			svg = chart.getSVG(chartOptions);
			
		// merge the options
		options = merge(chart.options.exporting, options);
		
		// create the form
		form = createElement('form', {
			method: 'post',
			action: options.url
		}, {
			display: NONE
		}, doc.body);
		
		// add the values
		each(['filename', 'type', 'width', 'svg'], function(name) {
			createElement('input', {
				type: HIDDEN,
				name: name,
				value: { 
					filename: options.filename || 'chart', 
					type: options.type, 
					width: options.width, 
					svg: svg 
				}[name]
			}, null, form);
		});
		
		// submit
		form.submit();
		
		// clean up
		discardElement(form);
	},
	
	/**
	 * Print the chart
	 */
	print: function() {
		
		var chart = this,
			container = chart.container,
			i,
			origDisplay = [],
			origParent = container.parentNode,
			body = doc.body,
			childNodes = body.childNodes;
			
		if (chart.isPrinting) { // block the button while in printing mode
			return;
		}
		
		chart.isPrinting = true;
		
		// hide all body content	
		each(childNodes, function(node, i) {
			if (node.nodeType == 1) {
				origDisplay[i] = node.style.display;
				node.style.display = NONE;
			}
		});
			
		// pull out the chart
		body.appendChild(container);
		 
		// print
		win.print();		
		
		// allow the browser to prepare before reverting
		setTimeout(function() {

			// put the chart back in
			origParent.appendChild(container);
			
			// restore all body content
			each (childNodes, function(node, i) {
				if (node.nodeType == 1) {
					node.style.display = origDisplay[i];
				}
			});
			
			chart.isPrinting = false;
			
		}, 1000);

	},
	
	/**
	 * Display a popup menu for choosing the export type 
	 * 
	 * @param {String} name An identifier for the menu
	 * @param {Array} items A collection with text and onclicks for the items
	 * @param {Number} x The x position of the opener button
	 * @param {Number} y The y position of the opener button
	 * @param {Number} width The width of the opener button
	 * @param {Number} height The height of the opener button
	 */
	contextMenu: function(name, items, x, y, width, height) {
		var chart = this,
			navOptions = chart.options.navigation,
			menuItemStyle = navOptions.menuItemStyle,
			chartWidth = chart.chartWidth,
			chartHeight = chart.chartHeight,
			cacheName = 'cache-'+ name,