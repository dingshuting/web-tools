steal(
    'can', './uploader.ejs', "./webuploader.min.js",
    "./webuploader.css",
    function(can, uploaderEJS) {
        var Upload = can.Control({
            /**
             * options.fileNumLimit int 文件数量的限制
             * options.value Array 当前值，array的url数组
             * options.isShowRemove boolean 是否显示remove操作
             */
            init: function(el, options) {

                var obj = this;
                if(options.value) {
                    if(Array.isArray(options.value)) {
                        this.vals = options.value;
                    } else {
                        this.vals = new Array();
                        this.vals.push(options.value);
                    }
                } else {
                    this.vals = new Array();
                }
                var removeEvent = function() {
                    for(var i in obj.vals) {
                        if($(this).parent().find("img").attr("src").indexOf(obj.vals[i]) > -1 || $(this).parent().find("img").attr("src").indexOf('plugins/webuploader/image/excel.png') > -1 || $(this).parent().find("img").attr("src").indexOf('plugins/webuploader/image/pdf.jpg') > -1 || $(this).parent().find("img").attr("src").indexOf('plugins/webuploader/image/word.png') > -1) {
                            obj.vals.splice(i, 1);
                        }
                    }
                    if($(this).data("file")) {
                        obj.uploader.removeFile($(this).data("file"));
                    }
                    $(this).parent().remove();

                }
                el.html(uploaderEJS(new can.Model.List(this.vals)));
                if(!options.isShowRemove) {
                    this.options.isShowRemove = true;
                }
                if(!options.fileNumLimit){
                    options.fileNumLimit=1;
                }
                if(options.isShowRemove) {
                    this.element.find(".remove").click(removeEvent);
                }
                this.mimetypes = {
                    video: "video/mp4,video/mpeg,video/mpeg4,video/mpeg",
                    image: "image/jpg,image/jpeg,image/bmp,image/png",
                    file: "image/jpg,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/doc,application/docx,application/msword,application/x-gzip,application/x-zip-compressed,application/pdf",
                    excel:"application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                }
				var serverUrl=$.contextPath + '/file/upload';
				if(options.serverUrl){
					serverUrl=$.contextPath+options.serverUrl;
				}
                this.uploader = WebUploader.create({
                    // swf文件路径
                    auto: true,
                    swf: 'jsLibs/plugins/webuploader/Uploader.swf',

                    // 文件接收服务端。
                    server:serverUrl ,

                    // 选择文件的按钮。可选。
                    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                    pick: el.find(".picker"),

                    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
                    resize: false,
                    // [可选] [默认值：undefined] 验证文件总数量, 超出则不允许加入队列。
                    fileNumLimit: options.fileNumLimit || 1,
                    formData: {
                        act: "resource"
                    },
                    accept: {
                        title: 'Images',
                        extensions: 'gif,jpg,jpeg,bmp,png,xlsx,pdf,docx,doc,PDF,DOCX,DOC',
                        //						mimeTypes: 'image/*'
                        mimeTypes: obj.mimetypes
                    }
                });
                if(options.uploadSuccess){
                	this.uploader.on('uploadSuccess',options.uploadSuccess);
                }else{
                	this.uploader.on('uploadSuccess', function(file, response) {
                    if('jpg' == file.ext || 'jpeg' == file.ext || 'bmp' == file.ext || 'png' == file.ext){
                        var responseJson = $.parseJSON(response)
                        obj.element.find(".webuploader-pick i").attr("class","fa fa-plus");
                        if(obj.options.fileNumLimit < 2 || !obj.options.fileNumLimit) {
                            obj.element.find(".uploader-list .upload-list-item").remove();
                        }
                        var uli = $("<div class='upload-list-item'></div>");
                        var remove = $("<i class='fa fa-remove remove'></i>");
                        if(obj.options.isShowRemove) {
                            remove.data("file",file);
                            uli.append(remove);
                        }
                        uli.append("<img src='" + responseJson.url + "' value='"+responseJson.url+"' style='width:80px;height:95px'/>");
                        obj.element.find(".uploader-list ").prepend(uli);
                        obj.vals.push(responseJson.url);
                        remove.click(removeEvent);
                    }else if ('xlsx' == file.ext){
                        var responseJson = $.parseJSON(response)
                        obj.element.find(".webuploader-pick i").attr("class","fa fa-plus");
                        if(obj.options.fileNumLimit < 2 || !obj.options.fileNumLimit) {
                            obj.element.find(".uploader-list .upload-list-item").remove();
                        }
                        var uli = $("<div class='upload-list-item'></div>");
                        var remove = $("<i class='fa fa-remove remove'></i>");
                        if(obj.options.isShowRemove) {
                            remove.data("file",file);
                            uli.append(remove);
                        }
                        uli.append("<img src='plugins/webuploader/image/excel.png' value='"+responseJson.url+"' style='width:80px;height:75px'/>");
                        uli.append("<span style='width:80px;height: 25px;color:#434343;'>"+file.name+"</span>");
                        obj.element.find(".uploader-list ").prepend(uli);
                        obj.vals.push(responseJson.url);
                        remove.click(removeEvent);
                        console.log(responseJson.url);
                    }
                    else if ('doc' == file.ext){
                        var responseJson = $.parseJSON(response)
                        obj.element.find(".webuploader-pick i").attr("class","fa fa-plus");
                        if(obj.options.fileNumLimit < 2 || !obj.options.fileNumLimit) {
                            obj.element.find(".uploader-list .upload-list-item").remove();
                        }
                        var uli = $("<div class='upload-list-item'></div>");
                        var remove = $("<i class='fa fa-remove remove'></i>");
                        if(obj.options.isShowRemove) {
                            remove.data("file",file);
                            uli.append(remove);
                        }
                        uli.append("<img src='plugins/webuploader/image/word.png' value='"+responseJson.url+"' style='width:80px;height:75px'/>");
                        uli.append("<span style='width:80px;height: 25px;color:#434343;'>"+file.name+"</span>");
                        obj.element.find(".uploader-list ").prepend(uli);
                        obj.vals.push(responseJson.url);
                        remove.click(removeEvent);
                        console.log(responseJson.url);
                    }
                    else if ('docx' == file.ext){
                        var responseJson = $.parseJSON(response)
                        obj.element.find(".webuploader-pick i").attr("class","fa fa-plus");
                        if(obj.options.fileNumLimit < 2 || !obj.options.fileNumLimit) {
                            obj.element.find(".uploader-list .upload-list-item").remove();
                        }
                        var uli = $("<div class='upload-list-item'></div>");
                        var remove = $("<i class='fa fa-remove remove'></i>");
                        if(obj.options.isShowRemove) {
                            remove.data("file",file);
                            uli.append(remove);
                        }
                        uli.append("<img src='plugins/webuploader/image/word.png' value='"+responseJson.url+"' style='width:80px;height:75px'/>");
                        uli.append("<span style='width:80px;height: 25px;color:#434343;'>"+file.name+"</span>");
                        obj.element.find(".uploader-list ").prepend(uli);
                        obj.vals.push(responseJson.url);
                        remove.click(removeEvent);
                        console.log(responseJson.url);
                    }
                    else if ('pdf' == file.ext){
                        var responseJson = $.parseJSON(response)
                        obj.element.find(".webuploader-pick i").attr("class","fa fa-plus");
                        if(obj.options.fileNumLimit < 2 || !obj.options.fileNumLimit) {
                            obj.element.find(".uploader-list .upload-list-item").remove();
                        }
                        var uli = $("<div class='upload-list-item'></div>");
                        var remove = $("<i class='fa fa-remove remove'></i>");
                        if(obj.options.isShowRemove) {
                            remove.data("file",file);
                            uli.append(remove);
                        }
                        uli.append("<img src='plugins/webuploader/image/pdf.jpg' value='"+responseJson.url+"' style='width:80px;height:75px'/>");
                        uli.append("<span style='width:80px;height: 25px;color:#434343;'>"+file.name+"</span>");
                        obj.element.find(".uploader-list ").prepend(uli);
                        obj.vals.push(responseJson.url);
                        remove.click(removeEvent);
                        console.log(responseJson.url);
                    }
                	});
                }
                
                this.uploader.on("error",function(ex){
                    if(ex=='Q_EXCEED_NUM_LIMIT'){
                        // alert("最大支持上传"+options.fileNumLimit+"张图片");
                        alert("最大支持上传"+options.fileNumLimit+"个附件");
                    }
                });
                this.uploader.on("uploadProgress", function(file, percentage) {
                    obj.element.find(".webuploader-pick i").attr("class","fa fa-spin fa-circle-o-notch");
                });
            },

            getValue: function() {
                if(this.vals.length == 1) {
                    return this.vals[0];
                } else if(this.vals.length > 1) {
                    return this.vals;
                } else {
                    return "";
                }
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
                this.uploader.removeFile(id, remove || undefined);
            }
        });
        return Upload;
    })