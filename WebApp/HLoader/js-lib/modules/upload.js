/**
 *form表单的组件，其也支持内嵌form方式，来支持复合对象的添加保存
 * form有2个事件，success--》数据提交成功后的触发函数，failed---》数据保存失败后的触发函数，返回值为标准的result结果，参考后台的返回结果说明
 * options.para--->data-tag的对应属性的封装
 */
steal('plugins/webuploader/upload.js', function(UploadControl) {

	//---------将引用注入到内存中，在ModuleControl加载时执行，开始-------
	if(!$.Modules) {
		$.Modules = new Object();
	}
	$.Modules['upload'] = UploadControl;
	//---------将引用注入到内存中，在ModuleControl加载时执行，结束-------
	return UploadControl;
})