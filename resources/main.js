$(document).ready(function(){
	window.setInterval(function(){
		$('h1').each(function(){
			if ($(this).hasClass('rose')) {
				$(this).removeClass('rose').addClass('vert');
			} else if ($(this).hasClass('vert')) {
				$(this).removeClass('vert').addClass('rose');
			}
		});
	}, 100);
	vCentrer();
	$(window).resize(function(e){
		e.preventDefault();
		vCentrer();
	});
	$('.rotate').each(function(){
		vRotate(this);
	})
	//addEvent(window, "resize", vCentrer());
	//window.onresize = vCentrer();
});

function vCentrer(e)
{
	
	//alert('resize');
	var nImgSize = $('#image_404').width();
	console.log(nImgSize);
	var nWinSize = $(window).width();
	console.log(nWinSize);
	var nPadding = (nWinSize - nImgSize)/2;
	console.log(nPadding);
	$('#image_404').css('padding-left', nPadding);
}

function vRotate(oObject)
{
	var i = 0;
	var n = 0;
	console.log('n : '+n);
	var cpt = 0;
	var sens = 'droite';
	var incrementAngle = Math.sin(Math.PI * $(oObject).width()/100);
	var increment = ($(window).width() - $(oObject).width())/100;
	console.log('incrément : '+increment);
	window.setInterval(function(){
		oObject.style.transform = "rotate("+i+"rad)";
		$(oObject).css('left', n+'px');
		if (sens == 'droite') {
			cpt++;
			n+= increment;
			i+= incrementAngle;
			if (cpt >= 100) {
				sens = 'gauche';
			}
		} else {
			cpt--;
			n-= increment;
			i-= incrementAngle;
			if (cpt <= 0) {
				sens = 'droite';
			}
		}
		
	}, 41)
}