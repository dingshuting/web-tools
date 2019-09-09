function currentUser() {
	return $.session.currentUser;
}

function loadInclude(whenFinished) {
	if($("body").find("[data-include]").length < 1) {
		whenFinished();
	}
	var asyncComplated=0;
	var includeSize = $("body").find("[data-include]").length;
	$("body").find("[data-include]").each(function(i) {
		var el = $(this);
		var url = $(this).attr("data-include");
		var after;
		var before;
		try {
			before = eval($(this).attr("data-include-before"));
			after = eval($(this).attr("data-include-after"));
		} catch(e) {
			console.log(" the EL has tag 'data-include-before' but the value is not a function or undefined");
		}
		if($.isFunction(before)) {
			before();
		}
		
		$.ajax({
			type: "get",
			url: url,
			success: function(data) {
				el.html(data);
				asyncComplated++;
				if(asyncComplated == includeSize) {
					if(whenFinished) {
						whenFinished();
					}
				}
				if($.isFunction(after)) {
					after();
				}
			}
		});
	});
}

function loadModule() {
	steal('js-lib/module-loader.js', function(ModuleControl) {
		if(!ModuleControl){
			ModuleControl=$.ModuleControl;
		}
		new ModuleControl("body");
	})
}
//to load a jQuery plugin, if $ or jQuery has been exist, it will not produce a network request to get the js-lib;
function loadJquery(jqueryLoaded){
	function extendOwnJqueryFunc(){
		//the public error handler of jQuery 
		steal("js/config.js","js/jquery.own.js",'js/util.js',function(){
			jqueryLoaded.call();
			//execute a including css libs ,when you add a data-include tag on head-tag 
			if($("head").is("[data-include]")){
				$.ajax({
					type: "get",
					url: $("head").attr("data-include"),
					async:false,
					success: function(data) {
						$("head").prepend($.renderStringTpl(data,window));
					}
				});
			}
			$(window).ajaxError(HLoaderConfig.defaultEventHolder.ajaxErrorHolder);
		});
		
	}
	if(window["jQuery"]){
		extendOwnJqueryFunc();
	}else{
		steal('jquery', function() {
			extendOwnJqueryFunc();
		});
	}
	
}
/**
 * the mian function of the framework,it will be executed when loader.js is loaded.
 * @param {Object} event A callback function that will be called when basic js-libs were loaded such as jQuery.js,system.js,dialog plugs.
 * 						 The modules isn't loaded at the time,so you can't call some functions of modules, because modules may not loaded at the time.
 */
function init(event) {
	$.isLogin = $('meta[name="is-login"]').attr("content");
	$.needsUser = $('meta[name="needs-user"]').attr("content");
	steal('js/modal-show.js', 'js-lib/model.js', 'js/system.js', function() {
		$.System.load(function(user) {
			loadInclude(function() {
				$("body>*:not(script):not(.module)").show();
				$("body").css("background-image","none");
				if(user) {
					console.log("alread logined keeping on this page")
				} else {
					if($.isLogin == "yes") {
						console.log("not login forword to login page");
						if(window['LOGIN_PAGE']){
							window.location.href=LOGIN_PAGE;
						}else{
							window.location.href=SERVER_DOMAIN+"/index?redirectUrl="+window.location.href;
						}
					}
				}
				$(".body").css("display", "block");
				$(".boay_loading").remove();
				loadModule();
				$.Modal.hideLoading();
				if(event) {
					event(user);
				}
			});
		});
	});
}

//the frameword depands Jquery plugin, so that load jQuery must be loaded at first.
loadJquery(function(){
	init(window['initFinished']);
})
