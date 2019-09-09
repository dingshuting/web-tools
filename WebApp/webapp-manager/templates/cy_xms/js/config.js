/**
 * 渲染整体页面的样式及个组件相关配置
 */
//模板下Table模块的options选项
TPL_TABLE_OPTION={
	listClasses:""
}
//页面元素或容器配置选项，最终以包含container类样式的节点为最终填充节点
EL_OPTIONS={
	//form模块的容器
	formContainer:"",
	//form模块的操作按钮容器
	formOptionContainer:"",
	//弹出框选择的列表容器
	choiseTableContainer:"<div class='ibox' style='color:black;overflow-y:auto'><div class='ibox-heading' style='padding:5px'><input style='margin:5px'/><button class='btn btn-sm btn-primary'>查询</button></div><div style=' overflow-x: auto;min-width:350px' class='ibox-content table-content'></div></div>"
}
