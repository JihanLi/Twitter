		var svg = d3.select("#chart")
			.append("svg")
			.append("g");
		
		svg.append("g")
			.attr("class", "slices");
		svg.append("g")
			.attr("class", "labels");
		svg.append("g")
			.attr("class", "lines");
		
		var width = 320,
		    height = 180,
			radius = Math.min(width, height) / 2;
		
		var pie = d3.layout.pie()
			.sort(null)
			.value(function(d) {
				return d.value;
			});
		
		var arc = d3.svg.arc()
			.outerRadius(radius * 0.8)
			.innerRadius(radius * 0.4);
		
		var outerArc = d3.svg.arc()
			.innerRadius(radius * 0.9)
			.outerRadius(radius * 0.9);
		
		svg.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
		
		var key = function(d){ return d.data.label; };
		
		var color = d3.scale.ordinal()
			.range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56"]);