function subMenuDown(li) 
{ 
	var subMenu = li.getElementsByTagName("ul")[0]; 
	subMenu.style.display = "block"; 
} 
function subMenuUp(li) 
{ 
	var subMenu = li.getElementsByTagName("ul")[0]; 
	subMenu.style.display = "none"; 
} 
