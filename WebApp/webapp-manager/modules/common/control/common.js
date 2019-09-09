steal(
	'can', 'templates/' + $.templateName + '/ejs/common.ejs','templates/' + $.templateName + '/js/config.js',
	function(can, CommonEJS) {
		return can.Control(
			/** @Static */
			{},
			/** @Prototype */
			{
				init: function(el, options) {
					var self = this;
					el.html(CommonEJS());
					var query=options.query;
					var actions=$.MenuControl.funcOptions();
					self.query=new $.QueryFormControl(el.find("#query_bar"),$.extend(true,{model:$.Model($.session.currFun.extraData),form:{classes:"form-inline col-lg-12"},isQuery:true}, options));
					var $btnContainer=$("<div class='form-group'></div>")
					$btnContainer.append("<button type='button' id='query_button' class='btn btn-sm btn-primary'> 查询</button>");
					$btnContainer.append("<button type='button' id='empty_button' class='btn btn-sm btn-primary'> 清空</button>");
					self.query.element.find("form .col-lg-12").append($btnContainer);
					self.query.element.find("#query_button").click(function(){
						self.table.setQuery($.extend(true,self.query.getValue(), query));
					});
                    self.query.element.find("#empty_button").click(function(){
                       self.query.element.find("form").get(0).reset();
                        self.query.element.find("form").find("input[type='hidden']").not('[name="t_type"]').val("");
                    });
					var tableOptions={
						"model": $.Model($.session.currFun.extraData),
						"pageSetting": {
							isShow: true,
							classes: "pagination",
							lableClasses: "paginate_button"
						},
						dataActions: actions
					};
					if(window['TPL_TABLE_OPTION']){
						tableOptions=$.extend(true, tableOptions, window['TPL_TABLE_OPTION']);
					}
					var listEl=el.find("#data_content");
					if(window.APP_CONFIG&&APP_CONFIG.callBack&&APP_CONFIG.callBack.listFinish){
                   		listEl.bind("finish",APP_CONFIG.callBack.listFinish);
                   }
					self.table = new $.TableControl(listEl, $.extend(true,tableOptions, options));
                    $(document).keydown(function () {
                        if (event.keyCode == "13") {//keyCode=13是回车键
                            $("#" + $.session.currFun.id).find("#btn_sub").trigger('click');
                            $("#" + $.session.currFun.id).find("#query_button").trigger('click');
                        }
                    });
				},
				refresh:function(){
					this.table.refresh();
				}
			});
	});