/**
 * 详情信息的实现
 */
steal("can", function() {
	var GetControl = can.Control.extend({
		loadAction: function(di) {
			var actionEls = di.find("[data-action]");
			if(actionEls.length < 1 && di.is("[data-action]")) {
				actionEls = di;
			}
			actionEls.each(function() {
				var parent = $(this).closest($(this).parents("[data-item]"));
				if(!di.control()) {
					return true;
				}
				//防止事件穿透，只加载最近的元素
				if(parent.length > 0 && !di.is(parent)) {
					return true;
				}
				var cel = $(this);

			});
		},
		//数据标签功能
		dataTagFunction: {
			"data-jstl": function(el, obj) {
				var jstl = el.attr("data-jstl");
				var val;
				try {
					eval("val=" + jstl);
				} catch(e) {
					console.error(e);
				}
				el.html(GetControl.dataFormat(el, val));
				el.removeAttr("data-jstl");
			},
			"data-if": function(el, obj) {
				var isEnable;
				var ifEx = $(el).attr("data-if");
				try {
					if(ifEx) {
						eval("isEnable=" + ifEx);
						if(isEnable == false) {
							$(el).remove();
							return false;
						} else {
							$(el).removeAttr("data-if");
							$(el).fadeIn();
							return true;
						}
					} else {
						return true;
					}
				} catch(e) {}
			},
			"data-action": function(el, obj) {
				el.data("value", obj);
				el.control(this);
				var action = $.renderStringTpl($(el).attr("data-action"), obj);
				if($.isNull(action)) {
					console.warn("no action was defind tag has deleted automatically");
					$(el).remove();
					return true;
				}
				var beforAct = $.renderStringTpl($(el).attr("data-before-action"), obj);
				var _ba = window[beforAct];
				if($.isFunction(_ba)) {
					if(_ba(el.data("value"), el) == false) {
						el.remove();
						return true;
					}
				}
				var _a = window[action];
				if($.isFunction(_a)) {
					$(el).unbind("click");
					$(el).bind("click", _a);
				} else if(HLoaderConfig.dataItemActions[action] != undefined) {
					$(el).unbind("click");
					$(el).bind("click", HLoaderConfig.dataItemActions[action]);
				} else {
					console.error("the func[" + action + "] of  data-action was not defined,please defined it in your page.")
				}
			},
			/**
			 * 元素包含data-tag标签时，此标签的属性以data-开头的均会渲染成去掉data-后的属性，如data-name=${obj.name},渲染后为name='obj.name的值'
			 */
			"data-tag": function(el, obj) {
				var attrs = $.ModuleControl.getDataTag(el);
				for(var k in attrs) {
					if(obj && k != "tag" && el.attr("data-" + k)) {
						if(k.startWith("data-")) {
							//当为标签时其它过滤器可以判断是否是本次渲染，false代表仅仅为标签不做数据渲染
							el.data("isRender", false);
						} else {
							el.data("isRender", true);
						}
						if($.isString(obj)) {
							el.attr(k, $.renderStringTpl(attrs[k], {
								"val": obj
							}));
						} else {
							el.attr(k, $.renderStringTpl(attrs[k], obj));
						}
						el.removeAttr("data-" + k);
					}
				}
			},
			"data-datadict-pid": function(el, obj) {
				if(el.attr('name')) {
					var names=el.attr('name').split(".");
					for(var i in GetControl.dataDict[el.attr("data-datadict-pid")]) {
						if(names.length>1){
							if(GetControl.dataDict[el.attr("data-datadict-pid")][i].id == obj[names[0]][names[1]]) {
								el.html(GetControl.dataDict[el.attr("data-datadict-pid")][i].name);
								break;
							}
						}else{
							if(GetControl.dataDict[el.attr("data-datadict-pid")][i].id == obj[names[0]]) {
								el.html(GetControl.dataDict[el.attr("data-datadict-pid")][i].name);
								break;
							}
						}
						
					}
				} else {
					console.error("Both data-datadict-pid and name must be exist!!!");
				}
			},

			"data-inner-model": function(el, obj) {
				new $.ModuleControl(el, {
					"obj": obj
				});
				el.removeAttr("data-inner-model");
			},

		},
		/**
		 * 判断当前的元素是否在当前作用域下，作用域为当前对象有效的范围，不包括，当前对象的属性对象的元素和父对象的元素，为保证数据正确渲染尽量避免data-item和name属性出现在统一个标签之上
		 * 逻辑为，条件1：当前元素的最近拥有data-type元素与当前加载的元素相同，并且当前元素标签的父不在name组件内，一个name代表一个作用域
		 * 		条件2：当前加载的标签父标签name属性等于当前正在加载元素的name
		 */
		isInScope: function(current, el) {
			return($(current).is(el) || $(current).parents("[data-item]:eq(0)").is(el) || //当前标签就是父标签或者当前标签的父data-item为当前传入的参数元素el
				(el.is("[data-item]") && $(current).parents("[data-item]:eq(0)").is(el) && !$(current).is("[data-item]")) //当前元素与el父元素为同一元素
				||
				($(current).parents("[data-type]:eq(0)").is(el) && $(current).parents("[name]").length < 1) //属于同一个模块中，并且确保模块中不包括一个name标签，name标签也可以代办一个模块
				||
				($(current).parents("[name]:eq(0)").attr("name") == el.attr("name") //当前标签父name和传入参数el的name相同，但可能中间存在跨作用域，所以还需确认下面几个条件
					&&
					!$(current).is("[data-item]") //当前标签不为data-item,如果为data-item则说明其自己为独立作用域，不能进行渲染
					&&
					($(current).parents("[data-item]:eq(0)").is(el.parents("[data-item]:eq(0)")) //属于同一个data-item的作用域
						||
						$(current).parents("[data-item]:eq(0)").is(el) //或者当前标签属于el的作用域
						||
						$(current).parents("[data-type]:eq(0)").is(el) //或者当前标签属于el的作用域另一种情况，如详情中并没有data-item标签，因此通过data-type判断
						||
						$(current).parents("[data-type]:eq(0)").is(el.parents("[data-type]:eq(0)"))))); //属于同一个data-type的作用域详情下生效
		},
		//对数据值进行格式化的操作,此功能为整个框架的公共格式化参数，如需个性化格式化通过data-filter来完成
		dataFormat: function(ce, value) {
			var format = ce.attr("data-event-format");
			var _format = window[format];
			var val;
			if(!_format) {
				_format = HLoaderConfig.detailDataFormat[format];
			}
			if($.isFunction(_format)) {
				val = _format(value, ce);
			} else {
				val = value;
			}
			return val;
		},
		//加载数据字段数据
		loadDataDict: function(el, finished) {
			var length = el.find("[data-datadict-pid]").length;
			el.find("[data-datadict-pid]").each(function(i, v) {
				var pid = $(v).attr("data-datadict-pid");
				//当父没有缓存数据时则查询数据字典数据
				if(!GetControl.dataDict[pid]) {
					$.Model(undefined, "dataDict").list(pid, 999, undefined, function(data) {
						GetControl.dataDict[data[0].pid] = data;
						if(i == length - 1) {
							finished();
						}
					});
				} else {
					if(i == length - 1) {
						finished();
					}
				}

			});
			if(length < 1) {
				finished();
			}
		},
		//数据字典的缓存，所有元素共用一个静态数据
		dataDict: []
	}, {
		//初始化构造方法
		init: function(el, options) {
			var self = this;
			$.ModuleControl.bindEvent(el);
			if(!options.para) options.para = {};
			this.data = options.para.data;
			if(!this.data) {
				this.data = el.data("data");
			}
			if(!this.data) {
				this.data = options.data;
			}
			GetControl.loadDataDict(el, function() {
				if(self.data) {
					self.loadData(self.element, self.data);
					if(el.is("[data-type]"))
						el.trigger("finish");
				} else {
					//当执行完毕查询后调用的回调函数
					self.loadByAjax(el);
				}
			});
			el.control(this);
		},
		loadByAjax: function(el) {
			var self=this;
			var options=self.options;
			var queryCallback = function(result) {
				$.Modal.hideLoading(el);
				if(options.para.resultkey && options.para.resultkey != "null") {
					self.data = result[options.para.resultkey];
					self.loadData(el, result[options.para.resultkey]);
				} else {
					self.data = result;
					self.loadData(el, result);
				}
				el.trigger("finish");
			}
			if($.isString(options.para.query)) {
				options.para.query = $.parseJSON(options.para.query);
			}
			self.Modle = $.Model(options.para.model ? options.para.model : options.para.act, options.para.target);
			if(options.para && !options.para.id) {
				if(options.para.query && options.para.query.id) {
					options.para.id = options.para.query.id
				} else {
					console.warn("no condition was found, make sure it's a link without any parameter");
				}
			} else {
				if(!options.para.query) {
					options.para.query = {}
				}
				options.para.query.id = options.para.id;
			}
			$.Modal.showLoading(el, "加载中")
			if(options.para.querytype == "get") {
				pathPara = undefined;
				if(options.para.pathpara) {
					pathPara = options.para.query.id;
				}
				self.Modle.executeGet(options.para.query, queryCallback, pathPara);
			} else if(options.para.querytype == "form") {
				self.Modle.executeForm(options.para.query, queryCallback);
			} else if(options.para.querytype == "rest") {
				self.Modle.executeRest(options.para.query, queryCallback, options.para.query.id);
			} else {
				self.Modle.get(options.para.id, queryCallback);
			}
		},
		//根据查询条件刷新当前的模块内容
		setQuery: function(query) {
			this.options.para.query = query;
			this.loadByAjax(this.element);
		},
		//加载数据到元素内容
		loadData: function(el, data) {
			var self = this;
			var obj = data;
			el.data("value", data);
			/**
			 * iterating all of the element,which have the 'name' attribute
			 * 迭代所有包含name属性的标签
			 */
			var nameEl = el.find("[name]:not([data-datadict-pid])");
			if(nameEl.length < 1 && el.is("[name]")) {
				nameEl = el;
			}
			//加载数据标签函数，用于扩展功能逻辑
			for(var k in GetControl.dataTagFunction) {
				var dataTags = el.find("[" + k + "]");
				if(dataTags.length < 1 && el.is("[" + k + "]")) {
					dataTags = el;
				}
				dataTags.each(function() {
					if(GetControl.isInScope($(this), el)) {
						GetControl.dataTagFunction[k].call(self, $(this), obj);
					}
				})
			}

			//if the obj is type of string,it will be a element of array
			if($.isString(obj)) {
				var $valCon;
				if(el.is("[data-array-value]")) {
					$valCon = el;
				} else {
					$valCon = el.find("[data-array-value]");
				}
				if($valCon.length > 0 && $.ModuleControl.tags[$valCon[0].tagName]) {
					$valCon.attr($.ModuleControl.tags[$valCon[0].tagName], GetControl.dataFormat($valCon, data));
				} else {
					$valCon.html(GetControl.dataFormat($valCon, data));
				}
				return;
			}

			nameEl.each(function() {
				//judging if in scope,means only the module itself without the inside ones.判断是否在作用域内

				if(GetControl.isInScope(this, el)) {
					var ce = $(this);
					var type = ce[0].tagName;
					var keys = $(this).attr("name").split(".");
					var key = keys[0];
					// A new module of list will be loaded here when the type of value of 'data[key]' is array or string-list
					if(($.type(data[key]) === 'array' && keys.length == 1) || (ce.is("[data-value-type='string-list']") && data[key] != "") || ce.is("[data-value-type='list']")) {
						var Control = $.Modules['list'];
						var options = {};
						$.extend(true, options, this.options);
						options.parent = obj;
						options.data = data[key];
						new Control(ce, options);
						//A new module of detail will be loaded here when type is object and the key doesn't equal to 'parent', because the 'parent' is a variable of sub-mudule.
					} else if(typeof data[key] == 'object' && key != "parent" && keys.length == 1) {
						var options = {
							para: {}
						};
						$.extend(true, options, this.options);
						options.para.data = data[key];
						options.parent = obj;

						var control = new GetControl(ce, options);
						// else the value is a normal string.
					} else {
						// the value will set to the  pre specified attribute of tags if it's not a normal tag like the img,video.
						if($.ModuleControl.tags[type]) {
							if(data[key] && data[key] != "") {
								ce.attr($.ModuleControl.tags[type], GetControl.dataFormat(ce, data[key]));
							}
						} else {
							var val;
							//取数据字段
							//判断是否为层级对象复制通过{objName.attrName}方式进行命名的name，最多3层，2层情况下可以直接使用{attr[index].attr1}方式，前提data[attr]必须是数组
							if(keys.length == 2) {
								var ai = keys[0].search(/\[\d+\]/);
								if(ai > -1) {
									var index = keys[0].substring(ai + 1, keys[0].length - 1);
									var realKey = keys[0].substr(0, ai);
									if(data[realKey] && data[realKey][index])
										val = GetControl.dataFormat(ce, data[realKey][index][keys[1]]);
								} else {
									if(data[key])
										val = GetControl.dataFormat(ce, data[keys[0]][keys[1]]);
								}
							} else if(keys.length == 3) {
								if(data[key]!=undefined)
									val = GetControl.dataFormat(ce, data[keys[0]][keys[1]][keys[2]]);
							} else {
								if(data[key]!=undefined)
									val = GetControl.dataFormat(ce, data[key]);
							}
							if(ce.is("input")) {
								ce.val(val);
							} else {
								ce.html(val);
							}
						}
						if(self.options.field_filter) {
							self.options.field_filter(key, arrData, ce, $(this), el);
						}
					}
				}
			});
			if($.session.currFun.extraData && HLoaderConfig.defaultEventHolder.dataFilter[$.session.currFun.extraData.nameCode]) {
				HLoaderConfig.defaultEventHolder.dataFilter[$.session.currFun.extraData.nameCode].call(el, data);
			}
			HLoaderConfig.defaultEventHolder.dataFilter["All"].call(self, el, data);
			el.removeAttr("data-tpl");
			$.Modules['detail'].loadAction(el);
		}
	});
	//---------将引用注入到内存中，在ModuleControl加载时执行，开始-------
	if(!$.Modules) {
		$.Modules = new Object();
	}
	$.Modules['detail'] = GetControl;
	//---------将引用注入到内存中，在ModuleControl加载时执行，结束-------
	return GetControl;
})