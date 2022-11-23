
let a4Class = document.querySelectorAll('.a4_size');
let membershipSpaceClass = document.querySelectorAll('.membershipSingleSpace');


window.addEventListener('load', function() {
	console.log('멤버십인쇄 페이지: a4 개수 '+a4Class.length);
	console.log('멤버십 표현 개수 '+membershipSpaceClass.length);
	
	for(var i=0; i<membershipSpaceClass.length; i++){
		a4Class[parseInt(i/20)].appendChild(membershipSpaceClass[i]);
	}
})

