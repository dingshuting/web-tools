/**
 * the boot js that will include when the file 'index.html' loaded. the js also
 * used to decide which view should be loaded.
 */

steal('js/template.js', 'js/system.js',
	'js/jquerypp.formparpm.js',"js/jquery.json.js",'js/modal-show.js',
	function(Template, System) {
		if(!$.support.opacity) {
			window.location.href = "htmls/404.html"
			return;
		}
		System.load(function(user) {
			if(user) {
				$.templateName = APP_CONFIG.template;
				$("body").html("");
				Template.load($.templateName);
				$(document).ajaxError(APP_CONFIG.callBack.defaultAjaxError)
			}
			APP_CONFIG.callBack.userInited(user);
			if(window['initFinished'] && $.isFunction(window['initFinished'])) {
				//initFinished();
			}
		});

	})