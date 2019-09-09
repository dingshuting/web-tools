/**
 * the boot js that will include when the file 'index.html' loaded. the js also
 * used to decide which view should be loaded.
 */

steal('can', 'js/decorater.js', 'js/template.js', 'js/system.js',
	'css/showloading.css',
	function(can, Decorater, Template, System) {
		steal('can', './plugins/jquery.showLoading.min.js',
			 'js/jquerypp.formparpm.js',
			"js/jquery.json.js", 'js/jquery.timer.js',
			'js/modal-show.js', './plugins/pop.js',
			function() {
				if(!$.support.opacity) {
					window.location.href = "htmls/404.html"
					return;
				}
				System.load(function(user) {
					//var template = Decorater.decorate(user);
					if(user){
						$.templateName=APP_CONFIG.template;
						/*if(user.owner == "100000"){
							$.templateName = "white";
						}else{
							$.templateName = "white";
						}*/
						$("body").html("");
						Template.load(null, $.templateName);
						$(document).ajaxError(function(event,xhr,options,exc){
							if(xhr.responseText&&xhr.status=="401"){
								$.System.toLogin();
							}
						})
					}					
					if(window['initFinished']&&$.isFunction(window['initFinished'])){
						initFinished();
					}
				});

			});

	})