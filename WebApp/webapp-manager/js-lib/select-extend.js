/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
'can',
'plugins/zTree/js/jquery.ztree.core.js',
'plugins/zTree/css/zTreeStyle/zTreeStyle.css',
'plugins/chosen/chosen.jquery.min.js','plugins/chosen/chosen.min.css',
function (can) {
	return can.Control( 
	/** @Prototype */
	{
		//当前扩展值的url地址
		url:undefined,
		//初始化扩展插件
		/**
		 * 
		 * @param {Object} inputEl input元素
		 * @param {Object} option url:获取地址的url
		 * 						  idAlias:id的别名
		 * 						  nameAlias:name的别名
		 * 						  defaultValOfid:默认的父节点值及跟节点的值,默认值为0
		 * 						  defaultText:默认的显示的初始化选择的内容
		 */
		init: function (el,option) {
			if(!option)option={};
			this.opt=option;
			var obj = this;
			if(!option.defaultValOfid){
				option.defaultValOfid=0;
			}
			$.getJSON($.contextPath+this.opt.url+"?id="+option.defaultValOfid,function(data){
				if($(el).is('input')){
					if(!obj.extContiner){
						for(var i in data){
							data[i].isParent=true;
						}
						obj.extContiner=obj.getContiner(el);
						$.fn.zTree.init(obj.extContiner.find("ul"), obj.getOrgSetting(el), data);
						$(el).after(obj.extContiner);
						$(el).click(function(){
							obj.extContiner.toggle();
						})
					}
				}else if($(el).is('select')){
					var defaultOpt=$("<option value=''>-请选择-</option>");
					if(obj.opt.defaultText){
						defaultOpt.html(defaultText);
					}
					$(el).append(defaultOpt);
					for(var i in data){
						if(!data[i].id){
							data[i].id=data[i][obj.opt.idAlias];
						}
						if(!data[i].name){
							data[i].name=data[i][obj.opt.nameAlias];
						}
						var option=$("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
						if(data[i].name==obj.opt.val||data[i].id==obj.opt.val){
							option.attr("selected","selected");
						}
						$(el).append(option);
					}
					$(el).chosen();
				}
			});
		},
		//获取扩展项的容器，并初始化提交的隐藏域，其作用用于提交最终选择的值
		getContiner:function(inputEl){
			var continer=$("<div class='select-extend'></div>");
			var ctop=$(inputEl).offset().top;
			var cleft=$(inputEl).offset().left;
			continer.css("position","absolute");
			//continer.attr("top",ctop);
			continer.attr("left",cleft);
			continer.css("z-index","9999");
			continer.css("width",$(inputEl).css("width"));
			var hideVal=$("<input type='hidden'/>")
			hideVal.attr("name",$(inputEl).attr("name"));
			$(inputEl).attr("name","");
			continer.append(hideVal);
			continer.attr("id",'sel-ext-'+hideVal.name)
			continer.append("<ul id='sel_ext_ul_"+hideVal.name+"' class='ztree'></ul>")
			return continer;
		},
		//获取初始化的ztree配置
		getOrgSetting: function(el) {
					var obj = this;
					var setting = {
						check: {
							enable: false
						},
						async: {
							enable: true,
							type: "get",
							url: $.contextPath + obj.opt.url,
							autoParam: ["id"],
							//对数据进行筛选处理，标记其是否包含子项
							dataFilter: function(treeId, parentNode, responseData) {
									for(var i = 0; i < responseData.length; i++) {
										responseData[i].isParent = true;
									}
									return responseData;
								
							}
						},
						callback: {
							//点击方法，当点击角色后将自动填充其详细信息，含相关的功能列表
							onClick: function(event, treeId, treeNode) {
								obj.currentData=treeNode;
								$(el).val(treeNode.name);
							},
							onDblClick:function(event, treeId, treeNode) {
								obj.currentData=treeNode;
								$(el).val(treeNode.name);
								obj.extContiner.find("input[type='hidden']").val(treeNode.id);
								obj.extContiner.hide();
							}
						}
					}
					return setting;
				}
	});
	
});

