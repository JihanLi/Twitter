var mapInit = false;
var data = null;
var last = null;
var jWord = ["Common", "Entertainment", "Sports", "Food", "Technique"];
var jCount;
var curType;
window.setInterval(function(){submitRequest(last);},1000); 

function changeTopic(tp) 
{
	last = tp;
	mapOptions.zoom = map.getZoom();
	mapOptions.center = map.getCenter();
	mapOptions.mapTypeId = map.getMapTypeId();
	map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
	submitRequest(tp);
}

  function submitRequest(current) 
  {
  	var message=-1;
  	var topic = current != null ? current.id : "all";
  	if(topic=="common")
  		message=0;
  	else if(topic=="play")
  		message=1;
  	else if(topic=="sport")
  		message=2;
  	else if(topic=="food")
  		message=3;
  	else if(topic=="tech")
  		message=4;
   	$.ajax({
          type: "POST",
          url: "server.jsp",
          data: { 'Topic': message },
          success: function (result) 
      	{
      		if (result.success == 1)
      		{
      			data = result.data;
      			if (!mapInit)
      			{
      				mapInit = true;
      				reloadMap(data, 3);
      				curType = 3;
      			}
      			else
      			{
      				reloadMap(data, curType);
      				jCount = [1, 1, 1, 1, 1];
      				for(var i = 0; i < data.length; i++)
    				{
      					jCount[data[i].topic]++;
      				}
      				last = current;
      			}
      		}
          }
      }); 
      
  }
  
  	var map, heatmap, pointArray;
  	var heatData = [];
  	var circles = [];
  	var markers = [];
  	var colors = ['#FF0000', '#0000FF', '#424242', '#00FF00', "#BF00FF"];
	var mapOptions;
	
    function initialize()
    {
      	mapOptions = {
	        zoom: 2,
	        center: new google.maps.LatLng(0, 0),
	      	mapTypeId: google.maps.MapTypeId.TERRAIN
	      };
	      
		map = new google.maps.Map(document.getElementById('map-canvas'),
		      mapOptions);
    }
		
	function changeMarker(div) 
	{
		mapOptions.zoom = map.getZoom();
		mapOptions.center = map.getCenter();
		mapOptions.mapTypeId = map.getMapTypeId();
		map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);
		curType = 1;
		reloadMap(data, curType);
	}
	
	function changeCircle(div) 
	{
		mapOptions.zoom = map.getZoom();
		mapOptions.center = map.getCenter();
		mapOptions.mapTypeId = map.getMapTypeId();
		map = new google.maps.Map(document.getElementById('map-canvas'),
	      mapOptions);
		curType = 2;
		reloadMap(data, curType);
	}
	
	function changeHeat(div) 
	{
		curType = 3;
		reloadMap(data, curType);
	}
	
	function changeMap(div)
	{
		var mapBtn = div;
		if(mapBtn.innerHTML.trim() == 'Show Terrain')
		{
			mapBtn.innerHTML = 'Show Map';
			mapOptions.zoom = map.getZoom();
			mapOptions.center = map.getCenter();
			mapOptions.mapTypeId = google.maps.MapTypeId.SATELLITE;
			map = new google.maps.Map(document.getElementById('map-canvas'),
		      mapOptions);
		}
		else
		{
			mapBtn.innerHTML = 'Show Terrain';
			mapOptions.zoom = map.getZoom();
			mapOptions.center = map.getCenter();
			mapOptions.mapTypeId = google.maps.MapTypeId.TERRAIN;
			map = new google.maps.Map(document.getElementById('map-canvas'),
		      mapOptions);
		}
	}
	
	function reloadMap(data, drawType)
	{
		if (data == null)
		{
			return;
		}
		
		for (var i = 0; i < circles.length; ++i)
		{
			circles[i].setMap(null);
		}
		
		for (var i = 0; i < markers.length; ++i)
		{
			markers[i].setMap(null);
		}
				
		if(drawType == 1)
		{
			for(var i = 0; i < data.length; i++)
    		{
			  	var marker = new google.maps.Marker({
				      position: new google.maps.LatLng(data[i].lattitude, data[i].longitude),
				      map: map,
				      title: data[i].display
			  	});
			  	markers.push(marker);
			}
		}		
		else if (drawType == 2)
		{
			for(var i = 0; i < data.length; i++)
    		{
				var circleOptions = {
			      strokeColor: colors[data[i].topic],
			      strokeOpacity: 0.8,
			      strokeWeight: 2,
			      fillColor: colors[data[i].topic],
			      fillOpacity: 0.35,
			      map: map,
			      center: new google.maps.LatLng(data[i].lattitude, data[i].longitude),
			      radius: Math.random()*100000+100000
			    };
			    circle = new google.maps.Circle(circleOptions);
			    circles.push(circle);
			}
		}
		else if (drawType == 3)
		{
			heatData = [];
     
			for(var i = 0; i < data.length; i++)
    		{
				heatData.push(new google.maps.LatLng(data[i].lattitude, data[i].longitude));
			}

			pointArray = new google.maps.MVCArray(heatData);

			heatmap = new google.maps.visualization.HeatmapLayer({
				data: pointArray
			});

			heatmap.setMap(map);
		}
	}

	submitRequest(null);
	google.maps.event.addDomListener(window, 'load', initialize);