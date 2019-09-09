steal(
	'can', 'js/model.js', 'js-lib/table.js', 'js-lib/lib-form.js',
	'js/viewFactory.js', 'js-lib/lib-map.js','app-conf/beitou.js',
	function(can) {
		steal('js-lib/query-form.js', 'js-lib/lib-contentInfo.js');
		steal('plugins/My97DatePicker/WdatePicker.js', 'plugins/My97DatePicker/skin/WdatePicker.css');
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
				this.initConfig();
				//初始化组件
				if(!$.showdemo) {
					this.checkUserStatus(obj);
					this.initOwn$Extend();
				}

			},
			//初始化系统配置
			initConfig: function() {
				if(!$.session) {
					$.session = {};
				}
				if(!$.request) {
					$.request = {};
				}
				if(!$.view) {
					$.view = {};
				}
				if(window['APP_CONFIG']&&window['APP_CONFIG'].domain){
					$.request.domain = APP_CONFIG.domain;
				}else{
					console.error('please set App-Conf in app-conf')
					return;
				}
				//$.contextPath = $.request.domain+"/beitou";
				if(window['APP_CONFIG'].contextPath==''||(window['APP_CONFIG'].contextPath.indexOf("/")==0&&window['APP_CONFIG'].contextPath.length>1)){
					$.contextPath = $.request.domain+window['APP_CONFIG'].contextPath;
				}else{
					console.error('contextPath must start with / or empty string')
					return;
				}
				var uri = steal.URI(document.location.href);
				$.request.uri = uri;
				$.session.currFun = {}
				$.session.currFun.id = "0101";
				//$.request.para = $.urlToObject(document.location.href);
				//是否启用tab页的模式访问页面
				$.view.isOpenTab = false;

				$.UiSetting = {
					//设置当前表单的列数量
					inputGroupNum: 2,
					//设置表单的列样式前缀，后面自动附加计算好的数值1~12，如col-lg-3
					colClass:"col-lg-",
					//列的总宽
					colTotalSize:12,
					
					formContainerDefaultClass:"form-group"
				};
				
			},
			logout:function(callback){
				$.post($.request.domain+window['APP_CONFIG'].logoutUrl,callback);
			},
			//初始化系统所用的组件函数
			initOwn$Extend: function() {
				//数据字典的插件功能
				$.SelectExtend = function(el, option) {
					steal("modules/common/control/select-extend.js", function(SEControl) {
						new SEControl(el, option);
					});
				};
				$.DataDictionary = function(el, option) {
					steal("modules/common/control/data-dictionary.js", function(DDControl) {
						new DDControl(el, option);
					});
				};
				//功能权限按钮的功能
				$.funcButtons = function(fids, appendTo, data) {
					for(var i in fids) {
						var fun = $.session.currentUser.getFunc(fids[i]);
						if(fun != null) {
							var $btn = $("<button type='button' class='am-btn am-btn-primary am-btn-sm fbtn_" + fun.id + "'></button>");
							$btn.data("func", fun);
							$btn.html(fun.name);
							$btn.click(function() {
								$.session.currFun = fun;
								var fun = $(this).data("func");
								if(fun.togo) {
									$.VF.build(fun.togo, undefined, data);
								}
							});
							appendTo.append($btn);
						}
					}
				};
				//功能权限选项的功能
				$.funcOptions = function(fids, appendTo) {
					for(var i in fids) {
						var fun = $.session.currentUser.getFunc(fids[i]);
						if(fun != null) {
							var $optHtml = $("<li><a href='##'></a></li>");
							$optHtml.find("a").html(fun.name);
							$optHtml.find("li").click(fun.togo);
							appendTo.append($optHtml);
						}
					}
				};
				$.objToUrlPara=function(data){
					var para="";
					for(var k in data) {
		                para +=  k + "=" + encodeURI(data[k])+"&";
		            }
            		return para;
				}
				$.urlToObject = function(url) {
					var strToObj = function(obj, so, val) {
						if(so.indexOf(".") > 0) {
							var item = so.substr(0, so.indexOf("."));
							if(!obj[item]) {
								obj[item] = {};
							};
							strToObj(obj[item], so.substr(so.indexOf(".") + 1), val);
						} else {
							obj[so] = val;
						}
					}
					var urlObject = {};
					if(/\?/.test(url)) {
						var urlString = decodeURIComponent(url.substring(url.indexOf("?") + 1));
						var urlArray = urlString.split("&");
						for(var i = 0, len = urlArray.length; i < len; i++) {
							var urlItem = urlArray[i];
							var item = urlItem.split("=");
							strToObj(urlObject, item[0], item[1]);
						}

					}
					return urlObject;
				};
				$.getUUID = function(len, radix) {
					var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
					var uuid = [],
						i;
					radix = radix || chars.length;

					if(len) {
						for(i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
					} else {
						var r;
						uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
						uuid[14] = '4';
						for(i = 0; i < 36; i++) {
							if(!uuid[i]) {
								r = 0 | Math.random() * 16;
								uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
							}
						}
					}
					return uuid.join('');
				};
				//时间格式化
				Date.prototype.Format = function(fmt) { //author: meizz 
					var o = {
						"M+": this.getMonth() + 1, //月份 
						"d+": this.getDate(), //日 
						"h+": this.getHours(), //小时 
						"m+": this.getMinutes(), //分 
						"s+": this.getSeconds(), //秒 
						"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
						"S": this.getMilliseconds() //毫秒 
					};
					if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
					for(var k in o)
						if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
					return fmt;
				};
			},
			toLogin:function(){
				if(window.location.href.indexOf(window['APP_CONFIG'].authUrl)<1){
					window.location.href = window['APP_CONFIG'].authUrl;
				}
				//window.location.href= $.request.domain+"/index?redirectUrl="+window.location.href;;
			},
			//检查用户的登录状态，非登录跳转登陆页面
			checkUserStatus: function(whenFinish) {
				var obj = this;
				if(!$.session.currentUser) {
					steal("modules/system/models/system.js", function(System) {
						System.currentUser(function(data) {
							whenFinish(data);
							if(data) {
								$.session.currentUser = obj.userExtend(data);;
							} else {
								obj.toLogin();
								//window.location.href = $.request.domain+"/index?redirectUrl="+window.location.href;
							}
						});
					});
				}
			},
			//为用户增加获取功能及是否包含某个角色的相关功能
			userExtend: function(user) {
					if(user) {
						user.getFunc = function(fid) {
							for(var i in user.roles) {
								for(var j in user.roles[i].funcs) {
									if(user.roles[i].funcs[j].id == fid) {
										return user.roles[i].funcs[j];
									}
								}
							}
							return null;
						}
						user.containRole = function(roleCode) {
							for(var i in user.roles) {
								for(var j in user.roles[i].funcs) {
									if(user.roles[i].code == roleCode) {
										return true
									}
								}
							}
							return false;
						}
						return user;
					}
				}
				//url参数转换为对象形式的方法
				,
		}, {

		})
		$.System=System;
		return System;
	});