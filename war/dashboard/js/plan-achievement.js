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
			
			//alert(serverDataPlan.allSph