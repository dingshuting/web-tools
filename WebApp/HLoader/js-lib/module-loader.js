/**
 * 前台动态生成
 * author:fx
 * 
 *1.  tages 数组作用是给指定标签的对应属性赋值，可以给一个标签的多个属性赋值
 *2.  data-type 属性是标志查询类型 通过该类型可以找到js中对应的方法 比如列表list    详情 get
 * ModuleControl是公共的基本Control，默认情况下它的el元素为body，然后再通过渲染将内部的各个子元素显示出来
 * 
 */
steal(
	'can',
	function(can) {
		$.ModuleControl = can.Control.extend({
			//将元素中的data标签，序列化成参数传入到组件Control中，以在逻辑中使用，data标签的值可以是函数,通过函数（）来确认是否是函数，但函数必须返回相应的参数，否则可能导致程序出错
			getParameters: function(el,selfObj) {
				var obj = $.urlToObject(document.location.href);
				if(!obj) {
					obj = {};
				}
				jQuery.extend(true, obj, $.ModuleControl.getDataTag(el,undefined,selfObj));
				return obj;
			},
			tags: {
				'IMG': "src",
				'VIDEO': "src"
			},
			/**
			 * 获取data标签的参数，如data-type,执行完成后将获取到{type：val}类似的对象
			 * @param {Object} el 要获取参数的元素
			 * @param {Object} para 有2级data值，如，data-model-type,最终结果只保留type作为key
			 */
			getDataTag: function(el, para,obj) {
				var attrs = el[0].attributes;
				var attr = new Object();
				for(var i in attrs) {
					if(!isNaN(parseInt(i)) && attrs[i]) {
						var dataName = attrs[i].name;
						var tagPrefix = "data-";
						if(para) {
							tagPrefix += para;
						} else {
							var tattrs=dataName.split("-");
							if(tattrs.length > 2&&tattrs[1]!="data") {
								continue;
							}
						}
						if(dataName.indexOf(tagPrefix) > -1) {
							if(typeof attrs[i].nodeValue != "undefined" && typeof window[attrs[i].nodeValue] === 'function' && tagPrefix.indexOf("event") < 1 && dataName.indexOf("action") < 1) {
								attr[dataName.substr(tagPrefix.length)] = window[attrs[i].nodeValue](el);
							} else {
								var val = attrs[i].nodeValue;
								try {
									if(val.indexOf("$")==0&&val.lastIndexOf("}")==val.length-1){
										eval("val="+val.substring(2,val.length-1));
									}else{
										val = $.renderStringTpl(val, obj);
									}
								} catch(e) {}
								attr[dataName.substr(tagPrefix.length)] = val;
							}
						}
					}
				}
				return attr;
			},
			bindEvent:function(v){
				var attrs = $.ModuleControl.getDataTag($(v), "event-");
				for(var k in attrs) {
					if(window[attrs[k]]&&$.isFunction(window[attrs[k]])){
						$(v).unbind(k).bind(k,window[attrs[k]]);
						$(v).children().unbind(k);
					}
				}
			}
			//所有的自定义参数标签的存储
		}, {
			init: function(el,options) {
				if(!el.length||el.length<1){
					el=$(HLoaderConfig.container);
				}
				this.pageFinishedInvoked=false;
				this.moduleLoadedNum=0;
				var self = this;
				if(HLoaderConfig.isLoadModule==false){
					this.pageFinishedInvoked=true;
					$(document).trigger("pageFinished");
					if(window["pageFinished"]){
						window["pageFinished"]();
					}
					return;
				}
				if(el.attr("data-type")) {
					self.loadModule(el, $(el).attr("data-type"));
					self.moduleCount=1;
				} else {
					self.moduleCount=$(el).find("[data-type]:not([data-event-finish])").length;
					if(self.moduleCount<1){
						this.pageFinishedInvoked=true;
						$(document).trigger("pageFinished");
						if(window["pageFinished"]){
							window["pageFinished"].call(el);
						}
						if($(el).find("[data-type]").length<1)
							return;
					}
					$(el).find("[data-type]").each(function(k, v) {
						if($(this).is("[data-inner-model]")){
							return true;
						}
						var moduleName = $(this).attr("data-type");
						self.loadModule(v, moduleName);
					});
				}
			},
			loadModule: function(v, moduleName) {
				//加载绑定事件
				if($(v).attr("data-lazy") == "yes") {
					this.moduleFinish(this);
					return;
				}
				var self = this;
				
				//加载js实现模块
				var Control = $.Modules[moduleName];
				if(!Control) {
					console.error("Module cann't be found in $.Modules[" + moduleName + "], please adding the " + moduleName + ".js into the meta-tag in the head of whole html.");
					return;
				}
				//此处为将整个URL参数与当前EL配置的参数进行叠加传递到Control模块中进行实例化渲染
				self.options.para = $.ModuleControl.getParameters($(v),self.options.obj);
				$.ModuleControl.bindEvent(v);
				$(v).bind("finish",function(){
					self.moduleFinish(this);
				});
				$(v).data("loaderControl",self);
				$(v).control(new Control($(v), self.options));
			},
			moduleFinish:function(el){
				this.moduleLoadedNum++;
				console.log(this.moduleCount+"<-->"+this.moduleLoadedNum);
				if(this.moduleLoadedNum>=this.moduleCount){
					if(this.pageFinishedInvoked==false){
						$(document).trigger("pageFinished");
						if(window["pageFinished"])
							window["pageFinished"].call(el);
						this.pageFinishedInvoked=true;
					}
				}
			}
		});
		return $.ModuleControl;
	});