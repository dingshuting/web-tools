/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
'can',
function (can) {
	var Control= can.Control( 
	/** @Prototype */
	{
		/**
		 * Create a recipe list, render it, and make a request for finding all recipes.
		 */
		region:undefined,
		viel:undefined,
		init: function (el,option) {
			var obj = this;
			viel = el;
			var ddc=$("<select class='form-control m-b validate[required]'></select>");
			ddc.attr("name",option.name);
			ddc.attr("id",option.id);
			ddc.append("<option value=''>--全部--</option>")
			$.Model().list(option.id,undefined,function(data){
				for(var i in data){
					var opt=$("<option></option>");
					opt.val(data[i].id);
					opt.text(data[i].name);
					if(option.val==data[i].id){
						opt.attr("selected","selected");
					}
					ddc.append(opt);
				}
				obj.element.html(ddc);
				$(ddc).css("border-color", "rgb(221, 183, 76)");
			});
		}

	});
	$.libDD=Control;
	return Control;
});

