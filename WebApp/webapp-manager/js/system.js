steal(
	'can',"modules/system/models/system.js","js/jquery.own.js","js/util.js",'js-lib/table.js','js/model.js','js-lib/lib-form.js',
	'js/viewFactory.js', 'js-lib/lib-map.js','js-lib/lib-contentInfo.js','app-conf/beitou.js',
	function(can,SystemModel) {
		steal("js-lib/query-form.js");
		var System = can.Construct.extend({
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
			/**
			 *注销当前登录的用户 
			 * @param {Object} callback 注销成功后的回调地址
			 */
			logout:function(callback){
				$.post($.request.domain+window['APP_CONFIG'].logoutUrl,callback);
			},
			/**
			 *跳转至登录页面 
			 */
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
					SystemModel.currentUser(function(data) {
						whenFinish(data);
						if(data) {
							$.session.currentUser = obj.userExtend(data);;
						} else {
							obj.toLogin();
							//window.location.href = $.request.domain+"/index?redirectUrl="+window.location.href;
						}
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
		}, {

		})
		$.System=System;
		return System;
	});