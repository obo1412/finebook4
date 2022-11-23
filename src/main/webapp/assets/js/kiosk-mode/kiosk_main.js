


window.addEventListener('load', function() {
	/*document.querySelector('.btn__kiosk').click();*/
})



function startFS(element) {
	if(element.requestFullScreen) {
		element.requestFullScreen();
	} else if(element.webkitRequestFullScreen ) {
		element.webkitRequestFullScreen();
	} else if(element.mozRequestFullScreen) {
		element.mozRequestFullScreen();
	} else if (element.msRequestFullscreen) {
		element.msRequestFullscreen(); // IE
	}
}


function btnclicked() {
	startFS(document.body);
}