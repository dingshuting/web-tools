/**
 *form表单的组件，其也支持内嵌form方式，来支持复合对象的添加保存
 * form有2个事件，success--》数据提交成功后的触发函数，failed---》数据保存失败后的触发函数，返回值为标准的result结果，参考后台的返回结果说明
 * options.para.action-->整个所有模块的通用属性，用于构建model的动作
 * options.para.model -->与action配合使用，model代表着目标model的类型。通过model.action找到对应的url后执行相应的操作
 */
steal("can", 'js/jquerypp.formparpm.js', function() {
	steal('plugins/jquery-validation/css/validationEngine.jquery.css');
	steal('plugins/jquery-validation/css/template.css');
	steal('plugins/jquery-validation/js/languages/jquery.validationEngine-zh_CN.js');
	steal('plugins/jquery-validation/js/jquery.validationEngine.js');
	var FormControl = can.Control.extend({
		init: function(el, options) {
			var self = this;
			$(el).control(this);
			if(!options.para) {
				options.para = {};
			}
			var form;
			if(el[0].tagName == "FORM") {
				form = el;
			} else {
				form = el.find("form");
			}
			form[0].onsubmit=function(){return false};
			self.formEl=form;
			if(undefined==options.para.isedit){
				options.para.isedit=true;
			}
			//是否为自定义action
			if(options.para.action) {
				options.para.act=options.para.action;
			}
			if(options.para.act) {
				this.Modle = $.Model(options.para.act, options.para.target);
			} else {
				this.Modle = $.Model(options.para.model, options.para.target);
			}
			this.initRequestParaToform();
			this.data = options.para.data;
			if(!this.data){
				this.data=options.data;
			}
			if(this.data) {
				this.loadData(el, this.data);
				$(self.element).trigger("finish");
			} else if(options.para.id&&options.para.isedit==true) {
				$.Modal.showLoading(el, "加载中");
				this.Modle.get(options.para.id, function(result) {
					if(self.options.beforeDataLoaderFilter) {
						result = self.options.beforeDataLoaderFilter(result);
					}
					self.loadData(el, result);
					form.validationEngine({
						promptPosition: "centerCenter"
					});
					self.data = result;
					$(self.element).trigger("finish");
					$.Modal.hideLoading(el);
				});
			} else {
				$(self.element).trigger("finish");
			}
			form.validationEngine({
				promptPosition: "centerCenter"
			});
			el.find(".submit").control(this);
			el.find(".submit").bind("click", self.toSubmit);
		},
		reset:function(){
			var self=this;
			self.formEl[0].reset();
			delete self.data;
		},
		//将表单里默认需要request的参数给予填充
		initRequestParaToform: function() {
			var requestPara = $.request.para;
			this.element.find("[type='hidden']").each(function() {
				$(this).val($.renderStringTpl($(this).val(), requestPara));
			});
		},
		isInScope:function(current,el){
			return (($(current).parents("[data-type]:eq(0)").is(el)&&$(current).parents("[name]").length<1)||($(current).parents("[name]:eq(0)").attr("name")==el.attr("name")));
		},
		//加载数据到元素内容
		loadData: function(el, data) {
			var obj = data;
			var self=this;
			el.find("[data-inner-model]").each(function(){
				if(self.isInScope($(this),el)){
					new $.ModuleControl($(this),{"data":data[$(this).attr("name")]});
				}
			})
			for(key in data) {
				var ce = el.find("[name='" + key + "']");
				if(ce.length > 1) {
					ce.each(function() {
						if($(this).parents("[name]").length < 1) {
							if($(this).attr("value")==data[key]){
								if($(this).is("[type='checkBox'],[type='radio']")){
									$(this).prop("checked","checked");
								}
							}
						}
					});
				}else
				if(ce[0]) {
					var type = ce[0].tagName;
					if(typeof data[key] == 'object') {
						var options = {}
						$.extend(true, options, this.options);
						options.para.data = data[key];
						var control = new FormControl(ce, options);
						ce.data("control", control);
					} else {
						var attrs = $.ModuleControl.getDataTag(ce);
						for(var k in attrs) {
							ce.attr(k, $.renderStringTpl(attrs[k], obj));
						}
						if($.ModuleControl.tags[type]) {
							ce.attr($.ModuleControl.tags[type], data[key]);
						} else {
							if($.isFunction(ce.val)) {
								ce.val(data[key]);
							} else {
								ce.html(data[key]);
							}

						}
						if(this.options.field_filter) {
							options.field_filter(key, arrData, ce, $(this), el);
						}
					}
				}
			}

		},
		/**
		 * Getting the value of current form, it can serialize the params automatically to a object of javascript, 
		 * 	if the form has one more child(a div that has a name attr tag and the data-control not null),the child will be serialized to a object too as a child of object
		 */
		getValue: function() {
			var form;
			var self = this;
			if(this.element[0].tagName == "FORM") {
				form = this.element;
			} else {
				form = $(this.element).find("form");
			}
			if(form.length > 0) {
				if(this.validate(form)) {
					var validate=true;
					var obj = form.formParams();
					if(this.data) {
						$.extend(true, this.data, obj);
						obj = this.data;
					}
					form.find("div[name]").each(function() {
						var control = $(this).data("control");
						if(control){
							obj[$(this).attr("name")] = control.getValue();
							if(control.element.hasClass("required")&&(!obj[$(this).attr("name")]||obj[$(this).attr("name")]=="")){
								control.element.validationEngine('showPrompt', '这里必填');
								validate=false;
								return false;
							}
						}else{
							var ikey=$(this).attr("name");
							obj[ikey]={};
							$(this).find("[name]").each(function(){
								if($(this).is("[type='checkBox'],[type='radio']")){
									if(!$(this).is(":checked")){
										return true;
									}
								}
								obj[ikey][$(this).attr("name")]=obj[$(this).attr("name")];
								delete obj[$(this).attr("name")];
							})
						}
							
					});
					if(validate){
						return obj;
					}
					
				}
			}
		},
		validate: function(form) {
			var self=this;
			form.validationEngine({
				promptPosition: "centerRight"
			});
			if(form.validationEngine("validate")) {
				if(self.element.attr("data-custom-validate")){
					var _validate=window[self.element.attr("data-custom-validate")];
					if($.isFunction(_validate)){
						return _validate(self.data);
					}else{
						console.error("the value of data-custom-validate must be a function of window");
					}
				}
				return true;
			} else {
				form.submit();
				return false;
			}
		},
		submitCallBack: function(self, data, el) {
			self.result = data;
			if(data.code == "200") {
				if(self.element.attr("data-event-success")) {
					self.element.trigger("success", data);
				} else {
					$.Modal.showMessage("操作成功");
				}
			} else {
				if(self.element.attr("data-event-failed")) {
					self.element.trigger("failed", data);
				} else {
					$.Modal.showError(data.desc||data.message);	
				}
				el.css("background-color", el.data("old-bc"));
				el.bind("click", self.toSubmit);
			}
		},
		/**
		 * to submit the object got from form to the server by method-model.save,
		 * or you can get the object of form by getValue(), then take the data by yourself.
		 */
		toSubmit: function(event, obj, el) {
			var self, $this
			if(event) {
				self = $(this).control();
				if(!self){
					self=$(this).parents("[data-type='form']").control();
				}
				$this = $(this);
			} else {
				self = this;
				$this = el;
			}
			if(!obj) {
				obj = self.getValue();
			}
			if(self && obj && $this) {
				$this.trigger("beforesubmit",obj);
				$this.unbind("click");
				//判断是否执行默认的保存还是自定义的post请求
				self.data=obj;
				if(self.options.para.model) {
					self.Modle.save(obj, function(data) {
						self.submitCallBack(self, data, $this);
					});
				} else if(self.options.para.act) {
					if(self.options.para.method == undefined || self.options.para.method == "rest") {
						self.Modle.execute(obj, function(data) {
							self.submitCallBack(self, data, $this);
						});
					} else if(self.options.para.method == "form") {
						self.Modle.executeForm(obj, function(data) {
							self.submitCallBack(self, data, $this);
						});
					} else {
						console.error("Only 'rest' or 'form' as value can be supported on tag data-method");
					}
				} else {
					console.error("one of data-action and data-model must be specified on tag 'form'");
				}

			}else{
				console.error("self && obj && $this one of them is undefined");
			}
		}
	});
	//---------将引用注入到内存中，在ModuleControl加载时执行，开始-------
	if(!$.Modules) {
		$.Modules = new Object();
	}
	$.Modules['form'] = FormControl;
	//---------将引用注入到内存中，在ModuleControl加载时执行，结束-------
	return FormControl;
})