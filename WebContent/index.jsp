<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.lang.Math" %>
<%@ page import="src.SQLBase" %>


<!DOCTYPE html>
<html>
  <head>
    <title>Twitter Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta http-equiv="Content-Type" content="text/html" charset="utf-8" />
    <meta charset="utf-8">
    
    <link rel="stylesheet" href="js/style.css">
	<script type="text/javascript" src="js/effects.js"></script> 
	<script type="text/javascript" src="js/jquery.js"></script> 
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&language=en"></script>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true&libraries=visualization"></script>
    <script type="text/javascript" src="js/map.js"></script>

  </head>
  
  <body id="init">
  
  <div class="panel">
  	<span id="word">Twitter Map</span>
  	<span id="author">Copyright: Jihan Li</span>
		<ul class="drop" id="drop">
			<li id="par" onmouseover="subMenuDown(this)" onmouseout="subMenuUp(this)">
				<a href="#">
					<span>
						Selecting Topics &nbsp;
					</span>
				</a>	
				<ul>
					<li id="all" title="All of Twits" onclick="changeTopic(this)"><a href="#">All</a></li>
					<li id="common" title="Common Words" onclick="changeTopic(this)"><a href="#">Common</a></li>
					<li id="play" title="Sth. about entertainment: party, music, fun..." onclick="changeTopic(this)"><a href="#">Entertainment</a></li>
					<li id="sport" title="Sth. about sport: football, tennis, run..." onclick="changeTopic(this)"><a href="#">Sport</a></li>
					<li id="food" title="Sth. about food: apple, fish, rice..." onclick="changeTopic(this)"><a href="#">Food</a></li>
					<li id="tech" title="Sth. about techniques: java, SQL, computer..." onclick="changeTopic(this)"><a href="#">Technology</a></li>
				</ul>
			</li>
		</ul>
		<div class="earth"></div>
	</div>
	
    
    <div class="buttons" id="buttons">
		<div class="btn1" id="btn1" onclick="changeMarker(this)">
  		Show Markers
		</div>
	
		<div class="btn2" id="btn2" onclick="changeCircle(this)">
  			Show Circles
		</div>
	
		<div class="btn3" id="btn3" onclick="changeHeat(this)">
  			Show Heatmap
		</div>
	
		<div class="mapbtn" id="mapbtn" onclick="changeMap(this)">
  			Show Terrain
		</div>
	</div>
		
    
    <div id="map-canvas"></div> 
    
    <div id="chart"></div>
		
	<script src="http://d3js.org/d3.v3.min.js"></script>
	<script type="text/javascript" src="js/svg.js"></script>
	<script type="text/javascript" src="js/chart.js"></script>
	<script type="text/javascript" src="js/disp.js"></script>
		
  </body>
</html>
