steal(
	'can', './uploader64.ejs', "./webuploader.min.js",
	"./webuploader.css",
	function(can, uploaderEJS) {
		return can.Control({
			init: function(el, option) {
				var obj = this;
				if(!option.html)
					el.html(uploaderEJS());
				else
					el.html(option.html);
				this.uploader = WebUploader.create({
					// swf文件路径

					swf: 'jsLibs/plugins/webuploader/Uploader.swf',

					// 文件接收服务端。
					server: option.server || $.contextPath + '/file/upload',

					// 选择文件的按钮。可选。
					// 内部根据当前运行是创建，可能是input元素，也可能是flash.
					pick: option.pick || "#picker",

					// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
					resize: false,
					// [可选] [默认值：undefined] 验证文件总数量, 超出则不允许加入队列。
					fileNumLimit : option.fileNumLimit||4,
					accept: {
						title: 'Images',
						extensions: 'gif,jpg,jpeg,bmp,png',
//						mimeTypes: 'image/*'
						mimeTypes: 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
					}
				});
				if(!option.fileQueued) {
					this.uploader.on('fileQueued', function(file) {
						
						obj.uploader.makeThumb(file, function(error, ret) {
							if(error) {
								$f.find("li").text('预览错误');
							} else {
								file.src=ret;
								obj.showView(el,file,obj.removeFile);
							}
						});
						
					});
				} else {
					this.uploader.on('fileQueued', function(file) {
						obj.uploader.makeThumb(file, function(error, ret) {
							if(!option.parameter) {
								option.fileQueued(file, ret, option.id);
							} else {
								option.fileQueued(file, ret, option.id, option.parameter);
							}
						});

					});
				}
				this.uploader.on('fileDequeued', function(file) {
					if(!option.fileDequeued) {
						$list = el.find(".uploader-list ");
						$list.find("#" + file.id).remove();
					} else {
						option.fileDequeued(file);
					}
				});
				this.uploader.on('uploadFinished', function() {
					if(option.uploadFinished) {
						option.uploadFinished(option.parameter||undefined);
					}
				});
				
			},
			showView: function(el,img,removeBack) {
				if(img.url){
					img.src=img.url;
				}
				$list = el.find(".uploader-list");
				$f = $('<div id="' + img.id + '" class="col-sm-2 item-image">' +
					'<img style="height:140px;width:130px" class="img-thumbnail" src="'+img.src+'"/>' +
					'</div>');
				$rb = $('<a href="##" name="deleteUp" class="remove"></a>');
				$rb.click(function() {
					if(removeBack) {
						removeBack(img);
					} else {
						obj.removeFile(file.id);
					}
				});
				$f.append($rb);
				$("#picker").before($f);
			},
			//文件上传完成的回掉函数
			/**
			 * formdata-提交的表单数据，标识文件上传的标识或关联的资源id
			 * successCallback-成功的回掉函数，回掉函数中有一个文件的参数，上传成功后会自动赋值
			 * errorCallback-上传错误是回掉函数
			 */
			upload: function(formdata, successCallback, errorCallback) {
				if(this.uploader.getFiles().length > 0) {
					this.uploader.on('uploadSuccess', successCallback);
					this.uploader.on('uploadError', errorCallback);
					this.uploader.option("formData", formdata);
					this.uploader.upload();
				} else {
					successCallback();
				}
			},
			hasFile: function() {
				return this.uploader.getFiles().length > 0;
			},
			getFile: function(id) {
				return this.uploader.getFile(id);

			},
			removeFile: function(id, remove) {
				this.uploader.removeFile(id, remove||undefined);
			}
		});
	})