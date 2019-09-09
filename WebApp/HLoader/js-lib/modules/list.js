/**
 * 一个基本模块的实现，此模块实现了基础的list列表显示功能
 */
steal("can", function(can) {
	var ListControl = can.Control.extend({
		/**
		 * this:{
		 * 	instanced:代表是否已经实例化
		 *  Model:代表与当前实例绑定的Model，用于ajax请求
		 * 	options:{
		 *		para: 当前标签的data参数	
		 * 	}
		 * }
		 */
		init: function(el, options) {
			var self = this;
			$.ModuleControl.bindEvent(el);
			el.control(this);
			if(!options.para){
				options.para={};
			}
			if(options.data||options.para.data) {
				self.loadData(el, options.data?options.data:options.para.data);
				if(el.is("[data-event-finish]"))
					el.trigger("finish");
				return;
			}
			if(options.para.act&&options.para.target){
				this.Model = $.Model(options.para.act, options.para.target);
			}else if(options.para.model&&options.para.target){
				this.Model = $.Model(options.para.model, options.para.target);
			}else if(options.para.act){
				this.Model = $.Model(options.para.act, HLoaderConfig.modelDefaultTarget);
			}else{
				return;
			}
			
			if(!options.para.count) {
				options.para.count = 12;
			}
			if(!options.para.query) {
				console.warn("the query is null, you should use data-query as a condition of query when useing the List Module")
			}
			$.Modules['detail'].loadDataDict(el,function(){
				self.setQuery(1, options.para.query);
			})
		},
		getValue:function(){
			return this.options.data;
		},
		setQuery: function(pn, query, callback) {
			var self = this;
			if(self.inProcess){
				return;
			}
			this.inProcess=true;
			if(query) {
				if($.isString(query)){
					self.query=$.parseJSON(query);
				}else{
					self.query = query;
				}
				
			}
			if(!self.query){
				self.query={};
			}
			$.Modal.showLoading(self.element, "加载中");
			if((self.element).attr("data-page")=="no"){
				pn=undefined;
			}
			queryAndLoadData=function(result){
				var tempResult = $(self.element).trigger("dataloaded", result);
				self.data = result;
				var resultKey="list";
				if(self.options.para.resultkey&&self.options.para.resultkey!="null"){
					resultKey==self.options.para.resultkey;
				}else if(self.options.para.resultkey=="null"){
					resultKey=undefined;
				}
				if(HLoaderConfig.listSetting.resultKey){
					self.loadData(self.element,resultKey==undefined?result[HLoaderConfig.listSetting.resultKey]:result[HLoaderConfig.listSetting.resultKey][resultKey]);
				}else{
					self.loadData(self.element, resultKey==undefined?result:result[resultKey]);
				}
				
				$.Modal.hideLoading(self.element);
				// make sure the finish function executes once only
				if(true!=self.instanced){
					$(self.element).trigger("finish");
					self.instanced=true;
				}
					
				if(callback) {
					if(HLoaderConfig.listSetting.resultKey){
						callback(resultKey==undefined?result[HLoaderConfig.listSetting.resultKey]:result[HLoaderConfig.listSetting.resultKey][resultKey]);
					}else{
						callback(resultKey==undefined?result:result[resultKey]);
					}
				}
				self.inProcess=false;
			}
			self.query[HLoaderConfig.pageSetting.currentPageName]=pn;
			if(self.options.para.querytype=="form"){
				this.Model.executeForm(self.query,queryAndLoadData);
			}else if(self.options.para.querytype=="get"){
				this.Model.executeGet(self.query,queryAndLoadData);
			}else if(self.options.para.querytype=="rest"){
				this.Model.executeRest(self.query, queryAndLoadData);
			}else{
				this.Model.list(pn, self.options.para.count, self.query,queryAndLoadData);
			}
		},
		//清除当前模块下的所有data-标签，此函数使用场景可以在渲染一个模板数据后，再次重新渲染，防止标签混乱以及重复渲染
		cleanTag:function(){
			this.element.find("[data-item]").removeAttr("data-item");
			this.element.find("[data-tpl]").removeAttr("data-tpl");
			this.element.find("[name]").removeAttr("name");
			this.element.removeAttr("data-type");
		},
		//刷新当前Control的内容，包含已有的查询条件
		refresh: function(func) {
			this.element.html("");
			this.setQuery(1,undefined, func);
		},
		//清空整个数据从第一页开始，并不带任何查询条件
		restart: function(func,query) {
			this.element.html("");
			if(!query)query={};
			this.setQuery(1, query, func);
		},
		dataIfFunc:function(el,obj){
			var isEnable;
			var ifEx=$(el).attr("data-if");
			try{
				if(ifEx){
					eval("isEnable="+ifEx);
					if(isEnable==false){
						$(el).remove();
						return false;
					}else{
						$(el).fadeIn();
						$(el).removeAttr("data-if");
						return true;
					}
				}else{
					return true;
				}
			}catch(e){}
		},
		
		loadData: function(el, data) {
			var self = this;
			if(!data){
				return;
			}
			if(el.attr("data-value-type")=="string-list"){
				data=data.split(",");
			}
			if(!self.tpl) {
				if(el.find("[data-tpl]:first").length < 1) {
					console.error("no data-tpl was found in data-type tag. tips: tpl means the template of a item for whole List-Module, so please to add a tag that has attr data-tpl and conver a tag that has attr data-item");
					return;
				}
				var tpl = el.find("[data-tpl]:first");
				self.tpl=tpl.clone();
				self.tpl.removeAttr("data-tpl");
				self.tpl.addClass('e93a9c-k2kcvu-88993')
			}
			el.find(".nodata-hit").remove();
			if(!self.options.para.noclean)
				el.find(".e93a9c-k2kcvu-88993").remove();
			if(data.length<1){
				if(!el.is("[data-event-empty]")){
					HLoaderConfig.DefaultEvent.empty(self,el);
				}
				el.trigger("empty");
				return;
			}
			if(data.length > 0) {
				var start=0;
				var end=data.length;
				if(self.options.para.start){
					start=self.options.para.start;
				}
				if(self.options.para.end&&self.options.para.end<data.length){
					end=self.options.para.end;
				}
				var previousTpl=el.find("[data-tpl]:first");
				if(previousTpl.length<1){
					if(true==self.isMuiltyItem){
						previousTpl=el.find(">[data-item]:last").parent();
					}else{
						previousTpl=el.find(">[data-item]:last");
					}
				}
				for(var i = start; i < end;) {
					if(data[i] == null||!data[i]) {
						i++;
						continue;
					}
					var tpl = self.tpl.clone();
					var di;
					if(tpl.attr("data-item") == undefined) {
						di = tpl.children("[data-item]");
						self.isMuiltyItem=true;
					} else {
						di = tpl;
					}
					di.find("[data-index]").html((self.data?(self.data.currentPage-1)*self.data.perPageSize:0)+i+1);
					var noIf=true;
					di.each(function(index) {
						if(i>=data.length){
							$(this).hide();
							return true;
						}
						var obj = data[i];
						if(self.dataIfFunc($(this),obj)==false){
							noIf=false;
							i++;
							return true;
						}else{
							noIf=true;
						}
						var parent = $(this).closest($(this).parents("[data-type]"));
						//防止事件穿透，只加载最近的元素
						if(parent.length > 0 && !el.is(parent)) {
							return true;
						}
						$(this).data("index", i);
						$(this).data("value", obj);
						var detailControl = $.Modules['detail'];
						var option = {}
						$.extend(true, option, self.options);
						option.para.data = obj;
						var dcontrol = new detailControl($(this), option);
						$(this).data("detailControl",dcontrol);
						dcontrol.parent=self;
						$(this).control(self);
						$.Modules['detail'].loadAction($(this));
						i++;
					});
					/**
					 * 当不是if标签，并且没有找到data-item标签时标识没有data-item标签程序无法继续循环，直接退出,当当前的data-item为data-if判断时，则根据判断结果如果为false则不添加该元素
					 * the program will return directly,when no data-item was found.
					 * **/
					if(i == 0&&noIf==true) {
						console.error("there is no data-item tag wasn't defind or no data was valuable on tag #" + el.attr("id") + "(the id need to add by yourself), the list-module can not load any data");
						return;
					}else if(noIf==false){
						continue;
					}
					if(previousTpl.length<1){
						el.append(tpl);
					}else{
						previousTpl.after(tpl);
					}
					previousTpl=tpl;
				}
			} else {
				var tel = self.tpl.clone();
				if(typeof self.data != 'undefined' && self.data.currentPage==1){
					$(self.element).trigger("nodata",self);
				}
			}
			el.find("[data-tpl]:first").remove(); //删除模板
		}

	});
	//---------将引用注入到内存中，在ModuleControl加载时执行，开始-------
	if(!$.Modules) {
		$.Modules = new Object();
	}
	$.Modules['list'] = ListControl;
	//---------将引用注入到内存中，在ModuleControl加载时执行，结束-------
	return ListControl;
})