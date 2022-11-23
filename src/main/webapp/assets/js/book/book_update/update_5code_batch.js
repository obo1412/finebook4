const selectCodeType = document.querySelector('.select__code__type');

const divSameValueControl = document.querySelector('.div__same__value__control');
const divVolumeCodeControl = document.querySelector('.div__volume__code__control');


window.addEventListener('load', function() {
	
})


selectCodeType.addEventListener('change', () => {
	if(event.target.value == "volumeCode") {
		divVolumeCodeControl.classList.remove('div__hidden');
		divSameValueControl.classList.add('div__hidden');
	} else {
		divVolumeCodeControl.classList.add('div__hidden');
		divSameValueControl.classList.remove('div__hidden');
	}
});

