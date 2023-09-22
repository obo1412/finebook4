const selectCodeType = document.querySelector('.select__code__type');

const divSameValueControl = document.querySelector('.div__same__value__control');
const divVolumeCodeControl = document.querySelector('.div__volume__code__control');

const spanCommonLabel = document.querySelector('.common__label');
const spanPurOrDonLabel = document.querySelector('.purOrDon__label');


window.addEventListener('load', function() {
	
})


selectCodeType.addEventListener('change', (event) => {
	if(event.target.value == "volumeCode") {
		divVolumeCodeControl.classList.remove('div__hidden');
		divSameValueControl.classList.add('div__hidden');
	} else {
		divVolumeCodeControl.classList.add('div__hidden');
		divSameValueControl.classList.remove('div__hidden');
	}
	
	if(event.target.value == "purOrDon") {
		spanCommonLabel.classList.add('div__hidden');
		spanPurOrDonLabel.classList.remove('div__hidden');
	} else {
		spanCommonLabel.classList.remove('div__hidden');
		spanPurOrDonLabel.classList.add('div__hidden');
	}
});

