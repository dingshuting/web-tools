/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
'can','../ejs/user-info.ejs','../models/user.js',
function (can,userInfoEjs,User) {
	return can.Control(
	/** @Prototype */
	{
		/**
		 * Create a recipe list, render it, and make a request for finding all recipes.
		 */
			init: function (el,option) {
				var obj = this;
				this.element.html(userInfoEjs(new can.Observe(option.data)));
				this.linstner=[
						 {el:"button[name='cancel'] click",func:this.calcel},
				]
				$(el).data("obj",obj);
				this.initEvent();
			},
			calcel:function(button){
				ViewFactory.build("system.user-list");
			},
				initEvent:function(){
					for(var i=0;i<this.linstner.length;i++){
						var el=this.linstner[i].el;
						var event=el.substr(el.lastIndexOf(" ")+1);
						var selecter=el.substr(0,el.lastIndexOf(" "));
						$(this.element).find(selecter).bind(event,this.linstner[i].func);
					}
				}
		});
	
});
