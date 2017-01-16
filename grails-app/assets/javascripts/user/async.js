
// IMPORTANT : Ne pas changer ca, si bascule dans document.ready, ca ne marche plus
(function($) {
	// Gestion d'un spinner à chaque appel Ajax
	// Affiche / Masque un élément d'id = ajaxSpinner
	$(document).ajaxStart(function() {
		$('#ajaxSpinner').show()
	}).ajaxStop(function() {
		$('#ajaxSpinner').fadeOut(200);
		formatDataTable();
		ajaxPagination();
	});
	
})(jQuery);


/**
 * Code à exécuter dès qu"une page est chargée
 */
$( document ).ready(function() {
	// ne pas afficher de popup en cas d'erreurs mais log error
	$.fn.dataTable.ext.errMode = 'throw';
	
	formatDataTable();
	ajaxPagination();
})



/**
 * Recherche des paginations en mode Ajax.
 * 
 * Transforme les liens GET en appel Ajax
 */
function ajaxPagination() {
	var divParent = $("div[ajax='true']");
	
	if (divParent) {
		divParent.on('click', '.pagination a', function(event) {
			var link = $(event.target);
			var urlLink = link.attr('href');
			
			jQuery.ajax({
				type: 'POST',
				data: jQuery(this).parents('form:first').serialize(),
				url: urlLink,
				success: function(data, textStatus){
					divParent.html(data);
				}
			});
			
			return false;
		});
	}
}


/**
 * Chargement des DIV asynchrones
 */
$(window).on('load', function() {
	$("div[async-url]").each(function() {
		// récupère l'url dans le composant
		var div = $(this);
		var url = div.attr('async-url');
		var asyncComplete = div.attr('on-async-complete');
		
		jQuery.ajax({
			url: url,
			success: function(data, textStatus){
				div.html(data);
				if (asyncComplete) {
					eval(asyncComplete);
				}
			}
		});
	});
});


function ajaxGet(eltSrcId, urlAttr, datas, divDstId, onSuccess) {
	var urlAction = $(eltSrcId).attr(urlAttr);
	var global = !($(eltSrcId).attr('data-immediate') == 'true');
	
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: urlAction,
		global: global,
		success: function(data, textStatus) {
			$(divDstId).html(data);
			if (onSuccess) {
				onSuccess(data);
			}
		},
		error: errorAjaxFunction
	});
}


/**
 * Chargement des formulaires asynchrone.
 * 
 */
$(window).on('load', function() {
	$('form[id^="deferedForm"]').submit();
});


var errorAjaxFunction = function(jqXHR, textStatus, errorThrown) {
	if (jqXHR.responseText) {
		$('#ajaxError').html(jqXHR.responseText);
	} else {
		$('#ajaxError').html(errorThrown);
	}
	$('#ajaxError').show();
	$('#ajaxError').fadeOut(3000);
};


function ajaxSubmitForm(eltSrcId, urlAttr, formId, divDstId, onSuccess) {
	var urlAction = $(eltSrcId).attr(urlAttr);
	var datas = $(formId).serializeArray();
	
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: urlAction,
		success: function(data, textStatus) {
			$(divDstId).html(data);
			if (onSuccess) {
				onSuccess();
			}
		},
		error: errorAjaxFunction
	});
}


function ajaxRerender(url, datas, divDstId) {
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: url,
		success: function(data, textStatus) {
			$(divDstId).html(data);
		}
	});
}


function favoriteStar(starId, star) {
	if (star) {
		$(starId).removeClass("aui-iconfont-unstar").addClass("aui-iconfont-star").addClass('star')
	} else {
		$(starId).removeClass("aui-iconfont-star").removeClass("star").addClass('aui-iconfont-unstar')
	}
}