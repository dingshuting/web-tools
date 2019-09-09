/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
'can','../control/personal-details.js','plugins/webuploader/upload.js',
function (can,personalDetailsControl,Upload) {

	return can.Construct.extend(
			{
				init:function(contentId){
					this.content=new personalDetailsControl(contentId);
				},
				destroy:function(){
					this.content.destroy();
				}
			});
});