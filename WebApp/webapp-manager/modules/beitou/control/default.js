steal(
	'can', '../ejs/default.ejs','plugins/webuploader/upload.js',
	function(can, defaultEJS,Upload) {
		return can.Control(
			/** @Static */
			{},
			/** @Prototype */
			{
				init: function(el, options) {
					el.html(defaultEJS());
					var uploadCon=new Upload(el.find("#upload"),{limit:1,serverUrl:"/common/importalldata",uploadSuccess:function(file,response){
						uploadCon.element.find(".webuploader-pick i").attr("class","fa fa-plus");
						$("#uploadResult").empty();
						 uploadCon.uploader.removeFile(file);
						for(res in response.data){
							$("#uploadResult").append("<li>"+response.data[res]+"</li>")
						}
					}});
				},
			});
	});