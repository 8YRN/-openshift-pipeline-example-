
// ==ClosureCompiler==
// @compilation_level SIMPLE_OPTIMIZATIONS

/** 
 * @license Highcharts JS v2.0.5 (2010-09-17)
 * 
 * (c) 2009-2010 Torstein Hønsi
 * 
 * License: www.highcharts.com/license
 */

// JSLint options:
/*jslint forin: true */
/*global document, window, navigator, setInterval, clearInterval, location, jQuery, $, $each, $merge, Events, Event, Fx, Request */
	
(function() {

// encapsulated variables
var doc = document,
	win = window,
	math = Math,
	mathRound = math.round,
	mathFloor = math.floor,
	mathCeil = math.ceil,
	mathMax = math.max,
	mathMin = math.min,
	mathAbs = math.abs,
	mathCos = math.cos,
	mathSin = math.sin,	
	
	
	// some variables
	userAgent = navigator.userAgent,
	isIE = /msie/i.test(userAgent) && !win.opera,
	isWebKit = /AppleWebKit/.test(userAgent),
	hasSVG = win.SVGAngle || doc.implementation.hasFeature("http://www.w3.org/TR/SVG11/feature#BasicStructure", "1.1"),
	colorCounter,
	symbolCounter,
	symbolSizes = {},
	idCounter = 0,
	timeFactor = 1, // 1 = JavaScript time, 1000 = Unix time
	garbageBin,
	defaultOptions,
	dateFormat, // function
	
	
	// some constants for frequently used strings
	UNDEFINED,
	DIV = 'div',
	ABSOLUTE = 'absolute',
	RELATIVE = 'relative',
	HIDDEN = 'hidden',
	PREFIX = 'highcharts-',
	VISIBLE = 'visible',
	PX = 'px',
	NONE = 'none',
	M = 'M',
	L = 'L',
	/*
	 * Empirical lowest possible opacities for TRACKER_FILL
	 * IE6: 0.002
	 * IE7: 0.002
	 * IE8: 0.002
	 * IE9: 0.00000000001 (unlimited)
	 * FF: 0.00000000001 (unlimited)
	 * Chrome: 0.000001
	 * Safari: 0.000001
	 * Opera: 0.00000000001 (unlimited)
	 */
	TRACKER_FILL = 'rgba(192,192,192,'+ (hasSVG ? 0.000001 : 0.002) +')', // invisible but clickable
	NORMAL_STATE = '',
	HOVER_STATE = 'hover',
	SELECT_STATE = 'select',
	
	// time methods, changed based on whether or not UTC is used
	makeTime,
	getMinutes,
	getHours,
	getDay,
	getDate,
	getMonth,
	getFullYear,
	setMinutes,
	setHours,
	setDate,
	setMonth,
	setFullYear,
	
	// check for a custom HighchartsAdapter defined prior to this file
	globalAdapter = win.HighchartsAdapter,
	adapter = globalAdapter || {}, 
	
	// Utility functions. If the HighchartsAdapter is not defined, adapter is an empty object
	// and all the utility functions will be null. In that case they are populated by the 
	// default adapters below.
	each = adapter.each,
	grep = adapter.grep,
	map = adapter.map,
	merge = adapter.merge,
	hyphenate = adapter.hyphenate,
	addEvent = adapter.addEvent,
	removeEvent = adapter.removeEvent,
	fireEvent = adapter.fireEvent,
	animate = adapter.animate,
	stop = adapter.stop,
	getAjax = adapter.getAjax,
	
	// lookup over the types and the associated classes
	seriesTypes = {};
	
/**
 * Extend an object with the members of another
 * @param {Object} a The object to be extended
 * @param {Object} b The object to add to the first one
 */
function extend(a, b) {
	if (!a) {
		a = {};
	}
	for (var n in b) {
		a[n] = b[n];
	}
	return a;
}

/**
 * Returns true if the object is not null or undefined. Like MooTools' $.defined.
 * @param {Object} obj
 */
function defined (obj) {
	return obj !== UNDEFINED && obj !== null;
}

/**
 * Set or get an attribute or an object of attributes. Can't use jQuery attr because
 * it attempts to set expando properties on the SVG element, which is not allowed.
 * 
 * @param {Object} elem The DOM element to receive the attribute(s)
 * @param {String|Object} prop The property or an abject of key-value pairs
 * @param {String} value The value if a single property is set
 */
function attr(elem, prop, value) {
	var key,
		setAttribute = 'setAttribute',
		ret;
	
	// if the prop is a string
	if (typeof prop == 'string') {
		// set the value
		if (defined(value)) {
			elem[setAttribute](prop, value);
		
		// get the value
		} else if (elem && elem.getAttribute) { // elem not defined when printing pie demo...
			ret = elem.getAttribute(prop);
		}
	
	// else if prop is defined, it is a hash of key/value pairs
	} else if (defined(prop) && typeof prop == 'object') {
		for (key in prop) {
			elem[setAttribute](key, prop[key]);
		}
	}

	return ret;
}
/**
 * Check if an element is an array, and if not, make it into an array. Like
 * MooTools' $.splat.
 */
function splat(obj) {
	if (!obj || obj.constructor != Array) {
		obj = [obj];
	}
	return obj; 
}



/**
 * Return the first value that is defined. Like MooTools' $.pick.
 */
function pick() {
	var args = arguments,
		i,
		arg;
	for (i = 0; i < args.length; i++) {
		arg = args[i];
		if (defined(arg)) {
			return arg;
		}
	}