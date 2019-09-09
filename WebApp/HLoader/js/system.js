steal(
	'can',
	function(can) {
		System = can.Construct.extend({
			/**
			 * 在系统登录后的初始化动作，初始化动作完成了：
			 * 1、$.request,$.session,$session.currentUser的创建
			 * 2、将uri的参数以对象形式封装到$.request.para
			 * 3、定义了功能函数$.funcButtons,$.funcOptions
			 * 		$.funcButtons(parps,container)
			 * 			parps-数组类型，传入功能菜单的id数组
			 * 			container-按钮要添加到的容器
			 * 		$.funcOptions-同$.funcButtons功能一致但添加的元素不一样
			 * @param {obj} 当系统初始化完成后的回调函数
			 */
			load: function(obj) {
				//初始化模块
				var self = this;
				var jsLib = "js-lib/modules/detail.js";
				var plib = $("meta[name='js-libs']").attr("content");
				if(plib && plib != "") {
					jsLib += "," + plib;
					var jsLib = jsLib.replace(/,/g, "','");
				}
				var libs = "'" + jsLib + "'";
				eval("steal(" + libs + ",function(){self.initOwn$Extend();self.initConfig();self.checkUserStatus(obj);});");
				self.loaded = true;
			},
			//初始化系统配置,主要包括一些常量如服务器域名、当前路径以及默认功能等等。
			initConfig: function() {
				var uri = steal.URI(document.location.href);
				if(!$.session) {
					$.session = {};
				}
				if(!$.request) {
					$.request = {};
				}
				if(!$.view) {
					$.view = {};
				}
				$.contextPath = "";
				if('SERVER_DOMAIN' in window) {
					$.request.domain = SERVER_DOMAIN+CONTENT_PATH
				} else {
					console.error("The SERVER_DOMAIN is not setting,please define a SERVER_DOMAIN variable and give a value in the config.js that can be localed in apps/{appName}/config.js");
				}
				$.request.uri = uri;
				$.request.para = {};
				$.session.currFun = {}
				$.session.currFun.id = "0101";
				var url = document.location.href;
				if(url.indexOf("#") > -1) {
					url = url.substring(0, url.lastIndexOf("#"))
				}
				$.request.para = $.urlToObject(url);
				//是否启用tab页的模式访问页面
			},
			//初始化系统所用的组件函数和工具函数
			initOwn$Extend: function() {
			},
			//检查用户的登录状态，非登录跳转登陆页面
			checkUserStatus: function(whenFinish) {
				var obj = this;
				console.log("start check the user")
				if((!$.session.currentUser && $.isLogin)||("yes"==$.needsUser)) {
					$.Model().currentUser(function(data) {
						if(data) {
							$.session.currentUser = obj.userExtend(data);;
							whenFinish(data);
						} else {
							whenFinish(undefined);
							$.session.currentUser = undefined;
						}
					});
				} else {
					whenFinish($.session.currentUser);
				}
			},
			userExtend: function(user) {
				return user;
			},
			//为用户增加获取功能及是否包含某个角色的相关功能
			urlToObject: $.urlToObject
		}, {})
		$.System = System;
		$.System.version = "1.0.0"
		return System;
	});