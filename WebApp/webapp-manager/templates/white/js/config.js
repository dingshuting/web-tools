/**
 * 渲染整体页面的样式及个组件相关配置
 */
//模板下Table模块的options选项
TPL_TABLE_OPTION={
	listClasses:"table table-striped table-borderless table-header-bg"
}
//页面元素或容器配置选项，最终以包含container类样式的节点为最终填充节点
EL_OPTIONS={
	//form模块的容器
	form_container:"<div class='card' ><div class='card-block card-block-full'><div class='row el-container'></div></div></div>",
	//form模块的操作按钮容器
	formOptionContainer:"<div class='col-lg-12 func-buttons'></div>",
	//table組件的默認容器
	choiseTableContainer:"<div class='card' style='overflow-y:auto'><div class='card-header' style='padding:5px'><input style='margin:5px'/><button class='btn btn-sm btn-primary'>查询</button></div><div class='icard-block table-content'></div></div>",
	//详情的容器
	detailContainer:"<div class='card'><div class='card-block card-block-full'><div class='row el-container form-detail'></div><div class='row'><div class='col-lg-12 func-buttons'></div></div></div></div>"
}
