/**
 * 一个基本模块的实现，此模块实现了基础的list列表显示功能
 */
steal("can", function(can) {
	var Control = can.Control.extend({
		init: function(el, options) {
			var self=this;
			if(!options.para.forlist){
				console.error("page can't load when List wasn't loaded or specified");
			}else{
				$(options.para.forlist).bind("finish",function(){
					self.listControl=$(self.options.para.forlist).data("control");
					if(HLoaderConfig.listSetting.resultKey){
						self.dataModel=self.listControl.data[HLoaderConfig.listSetting.resultKey];
					}else{
						self.dataModel=self.listControl.data;
					}
					self.dataModel.totalSize=self.dataModel[HLoaderConfig.pageSetting.totalSizeName];
					self.dataModel.perPageSize=self.dataModel[HLoaderConfig.pageSetting.perPageSizeName];
					self.dataModel.currentPage=self.dataModel[HLoaderConfig.pageSetting.currentPageName];
					$(el).html(self._getPageBar());
					if(true!=self.finished){
						el.trigger("finish");
						self.finished=true;
					}
				});
			}
		},
		initPage:function(){
			var self=this;
			self.listControl=$(self.options.para.forlist).data("control");
			if(window['LIST_RESULT_KEY']){
				self.dataModel=self.listControl.data[LIST_RESULT_KEY];
			}else{
				self.dataModel=self.listControl.data;
			}
			if(window['PAGE_FIELD']){
				self.dataModel.totalSize=self.dataModel[PAGE_FIELD.totalSize];
				self.dataModel.perPageSize=self.dataModel[PAGE_FIELD.perPageSize];
				self.dataModel.currentPage=self.dataModel[PAGE_FIELD.currentPage];
			}
			$(self.element).html(self._getPageBar());
		},
		setPage:function(page){
			var self=this;
			this.dataModel.currentPage=page;
			this.element.find("ul li").removeClass("active");
			this.element.find("ul .page-module-"+page).addClass("active");
			this.listControl.setQuery(page,undefined,function(){
				self.initPage();
				self.element.trigger("change",self.dataModel.currentPage);
			});
		},
		hasNext:function(){
			if((this.dataModel.currentPage+1)<=this.dataModel.pageTotal){
				return true;
			}else{
				return false;
			}
		},
		hasPrevious:function(){
			if((this.dataModel.currentPage-1)>=1){
				return true;	
			}else{
				return false;
			}
		},
		next:function(){
			if((this.dataModel.currentPage+1)<=this.dataModel.pageTotal){
				this.dataModel.currentPage+=1;
				this.setPage(this.dataModel.currentPage);
				this.element.trigger("change",this.dataModel.currentPage);
			}
		},
		previous:function(){
			if((this.dataModel.currentPage-1)>=1){
				this.dataModel.currentPage-=1;
				this.setPage(this.dataModel.currentPage);
				this.element.trigger("change",this.dataModel.currentPage);
			}
		},
		_getPageBar: function() {
			var self = this;
			var maxPage = 6;
			var $pageBar = $("<ul class='page-module'></ul>");
			if(self.options.para.classes){
				$pageBar.addClass(self.options.para.classes);
			}
			this.dataModel.pageTotal = parseInt(this.dataModel.totalSize / this.dataModel.perPageSize) + (this.dataModel.totalSize % this.dataModel.perPageSize > 0 ? 1 : 0);
			if(this.dataModel.pageTotal > maxPage && this.dataModel.currentPage > maxPage) {
				var $pbLi = $("<li></li>");
				$pbLi.html("<a><<</a>");
				$pbLi.data("cp", 1);
				$pageBar.append($pbLi);
			}
			var start = this.dataModel.currentPage - maxPage / 2
			for(count = start <= 0 ? 1 : start; count <= this.dataModel.pageTotal; count++) {
				var tli = $("<li></li>");
				tli.addClass("page-module-"+count);
				tli.html("<a>" + count + "</a>");
				tli.data("cp", count);
				if(this.dataModel.currentPage == count) {
					tli.addClass("active");
				}
				$pageBar.append(tli);
				if(count > this.dataModel.currentPage + maxPage)
					break;
			}
			if(this.dataModel.currentPage < this.dataModel.pageTotal - maxPage) {
				var tli = $("<li></li>");
				tli.html("<a>>></a>");
				tli.data("cp", this.dataModel.pageTotal);
				$pageBar.append(tli);
			}
			$pageBar.find("li").click(function() {
				self.setPage($(this).data("cp"));
				self.element.trigger("change",$(this).data("cp"));
			});
			return $pageBar;
		}
	});
	//---------将引用注入到内存中，在ModuleControl加载时执行，开始-------
	if(!$.Modules) {
		$.Modules = new Object();
	}
	$.Modules['page'] = Control;
	//---------将引用注入到内存中，在ModuleControl加载时执行，结束-------
	return Control;
})