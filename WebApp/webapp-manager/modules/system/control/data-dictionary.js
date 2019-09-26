steal(
	'can',
	'../ejs/data-dictionary.ejs',
	'../models/system.js',
	'js-lib/table.js',
	function(can, DDEjs, System, TablesControl) {
		/**
		 */
		return can.Control(
			/** @Static */
			{
			},
			/** @Prototype */
			{
				/**
				 * Create a recipe list, render it, and make a request for finding all recipes.
				 */
				init: function(el, para) {
					var self = this;
					System.ddl("0", function(dd) {
						el.html(DDEjs(dd));
						self.loadList(dd[0]);
						el.find(".list-group-item:first").addClass("active");
						el.find("#funcButtons").append($.MenuControl.funcButtons());
						el.find("#fn-fc3fd105-7896-4de2-9ffc-f3c7f3c9abef").unbind("click");
					});
					
				},
				loadList: function(parentDD) {
					var self=this;
					System.ddl(parentDD.id, function(dd) {
						self.tableControl=new TablesControl("#table_container", {
							list: dd,
							head:{
								cols: [{
									colCode: "id",
									colName: "字典标识",
									colValType: "s",
									isEdit: 1,
									status: 1,
									isShowInList:1,
									length:20
								},{
									colCode: "name",
									colName: "字典值",
									colValType: "s",
									isEdit: 1,
									status: 1,
									isShowInList:1,
									length:24
								},{
									colCode: "remark",
									colName: "备注",
									colValType: "s",
									isEdit: 1,
									status: 1,
									isShowInList:0,
									length:0
								}]
							},
							listClasses: "table",
							isEdit:true
						});
					})
				},
				".list-group-item click":function(el,event){
					//console.log(el.data("val"));
					this.options.dd=el.data("val");
					this.loadList(this.options.dd);
					console.log(this.options.dd);
					this.element.find(".list-group-item").removeClass("active");
					el.addClass("active");
					
				},
				"#fn-fc3fd105-7896-4de2-9ffc-f3c7f3c9abef click":function(el,event){
					var self=this;
					this.tableControl.addNewData(function(result){
						var inself=$(this);
						var Model = $.Model($.session.currFun.extraData);
	        			inself.showLoading();
	        			result.pid=self.element.find(".list-group-item.active").data('val').id;
	        			result.locked=0;
	        			result.status=1;
	        			Model.save(result,function(data){
        					inself.hideLoading();
	        				if(data.code == "200") {
	        					inself.find(".btn-close").trigger("click");
	        					self.element.find(".list-group-item.active").trigger("click");
	        					$.Modal.alert("保存成功");
	        				}else{
	        					$.Modal.error("保存失败"+data.desc);
	        				}
        				});
					});
				}
			});
	});