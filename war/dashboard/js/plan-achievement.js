var serverDataPlan = {
			allSpheres: []
			// other data might be placed in this container
			
		};
		
		var planAchievementGraph;
		var planAchievementOptions;
		
								
		// gets data from the server
		$.getJSON("plan-achievement", parametersForServlet, function(data){
		
			if('nullData'==data.error)
			{
				alert('Weekly Profiles for user ' + parametersForServlet.userName + ' not found.');
			}
			
			 // Store data from server for future (when another sphere is chosen, no get method is required)
			serverDataPlan.allSpheres = data.spheres;
			
			//alert(serverDataPlan.allSpheres[0].series[0].name),
					
			// Prepare settings of a chart
			planAchievementOptions = {
				chart: {
					renderTo: 'plan-achievement', 
					defaultSeriesType: 'line'
				},
				title: {
					text: 'Planned vs. achieved sphere realisation'
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					type: 'datetime'
				},
				yAxis: {
					title: null,
					labels: {
						formatter: function() {
							return Highcharts.numberFormat(this.value*100, 0) +' %'; 
						}
					}
				},
				tooltip: {
					formatter: function() {
						return '<b>'+ this.series.name  +'</b><br/>' +
							'Year: ' + Highcharts.dateFormat('%Y', this.x) + ', week: ' + getWeek(this.x, 4) + ':<br/>'+
							''+ Highcharts.numberFormat(this.y*100, 0) +' %';
					}
				},
				plotOptions: {
					line: {
						lineWidth: 3,
						marker: {
							enabled: false,
					