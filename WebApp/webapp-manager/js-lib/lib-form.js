/**
 * 动态的生成表单
 */
steal(
    'can',
    'js/modal-show.js',
    'plugins/webuploader/upload.js',
    function(can, ModelShow, Upload) {
        steal('plugins/summernote/css/summernote.css');
        steal('plugins/summernote/css/summernote-bs3.css');
        steal('plugins/jquery-validation/css/validationEngine.jquery.css');
        steal('plugins/jquery-validation/css/template.css');
        steal('plugins/summernote/summernote.min.js', function() {
            steal('plugins/summernote/summernote-zh-CN.js');
        });
        steal('plugins/jquery-validation/js/languages/jquery.validationEngine-zh_CN.js');
        steal('plugins/jquery-validation/js/jquery.validationEngine.js');

        var Form = can.Control.extend({
            timeWhere: {
                //小于今年
                "lt-now-year": {
                    maxDate: "%y"
                },
                //小于现在
                "lt-now-time": {
                    maxDate: '%y-%M-%d %H:%m:%s'
                },
                //大于今年
                "gt-now-year": {
                    minDate: "%y"
                },
                //大于现在
                "gt-now-time": {
                    minDate: '%y-%M-%d %H:%m:%s'
                }
            },
            id: {
                "id":null
            },
            //默认的引用字段处理方法
            defaultRefHander: function(divEL, headElement) {
                var container=$(EL_OPTIONS.choiseTableContainer);
                var body=container.find(".table-content");
                var searchInput=container.find("input");
                var searchButton=container.find("button");
                container.css("max-height",$(window).height()-110);
                $.Model().getHead(headElement.referenceExtralDataId, function(extraData) {
                	for(i in extraData.cols){
                		if(extraData.cols[i].colCode.match(".*name.*|.*city.*")||(APP_CONFIG.RefHander&&APP_CONFIG.RefHander.showCols&&APP_CONFIG.RefHander.showCols.indexOf(extraData.cols[i].colCode)>-1)){
                			extraData.cols[i].isShowInList=1;
                			if(!searchInput.attr("name")){
                				searchInput.attr("name",extraData.cols[i].colCode);
                				searchInput.attr("placeholder",extraData.cols[i].colName);
                			}
                		}else{
                			extraData.cols[i].isShowInList=0;
                		}
                	}
                    var modelObject = new $.Model(extraData);
                    var choseList=new $.TableControl(body,{query:headElement.options,model:modelObject,isShowActs:false,checked:{display:true,isSingle:true},pageSetting:{isShow:true,classes:"pagination"}});
                    searchButton.click(function(){
                    	var query={};
                    	query[searchInput.attr("name")]=searchInput.val();
                    	choseList.setQuery(query);
                    });
                    ModelShow.prompt(container, function(){
                    	result=choseList.getValue();
                    	if(result.length>0){
                    		checkedData=result;
                		var showVal="";
        		 		var ttotal=0;
        		 		for(i in extraData.cols){
        		 			if(extraData.cols[i].isShowInList==1){
        		 				ttotal++;
        		 				if(ttotal>3){
        		 					break;
        		 				}else if(ttotal>1){
        		 					var lastIndex=extraData.cols[i].colCode.lastIndexOf("Id");
        		 					if(lastIndex==extraData.cols[i].colCode.length-2){
        		 						showVal+=" 【"+checkedData[0][extraData.cols[i].colCode.substr(0,lastIndex)].name+"】";
        		 					}else{
        		 						showVal+=" 【"+checkedData[0][extraData.cols[i].colCode]+"】";
        		 					}
        		 				}else{
        		 					var lastIndex=extraData.cols[i].colCode.lastIndexOf("Id");
        		 					if(lastIndex==extraData.cols[i].colCode.length-2){
        		 						showVal+=checkedData[0][extraData.cols[i].colCode.substr(0,lastIndex)].name;
        		 					}else{
        		 						showVal+=checkedData[0][extraData.cols[i].colCode];
        		 					}
        		 				}
        		 			}
        		 		}
                		 if(divEL[0].tagName == "INPUT") {
                		 		$(divEL).val(showVal);
                		 		var hiddenEl=$("<input type='hidden' name='"+$(divEL).attr("name")+"'/>");
                		 		hiddenEl.val(checkedData[0].id);
                		 		$(divEL).after(hiddenEl);
                		 		$(divEL).removeAttr("name");
                		 		hiddenEl.data("value",checkedData[0]);
                            } else {
                                divEL.find("input").each(function() {
                                    if($(this).attr("class")) {
                                        $(this).val(showVal);
                                    } else {
                                        $(this).val(checkedData[0].id);
                                    }
                                });
                            }
                    	}else {
                                alert("请选择一条数据");
                                return false;
                            }
                    }, undefined);
                });
            }
        }, {
            /**
             *
             * @param {Object} el
             * @param {Object} options
             *
             * //表单信息
             * options.form=[{
			 * 				id:"";//表单id
			 * 				classes:"";//表单样式
			 * 				action:"";//提交地址
			 * 				method:"";提交方式
			 * 				isCreate:"";是否创建form
			 * }]
             *
             * //输入元素信息
             * options.input=[{
			 * 					code:"code",//元素的编码，文本框的name，用于编码
			 * 					valType:"valType",//元素的值的类型 t、时间    s、字符值   d、浮点  i、整数  b、boolean
			 * 									  f、文件    o、object类型   h、html类型，对应表单为html可视化编辑器类型text、大文本的，对应到表单则为textarea
			 * 									  bl、basic-list类型,在list中存储基本数据的类型，对应到表单则为多选框形式，值根据options中的关联数据字典中获取
			 * 									  ol、object-list类型，在list中存储为对象(model)数据类型，此类型具体应用参考reference_extral_data_id的引用
			 * 									  fv、fixed-value 固定的值类型，类似于性别，其值为固定，对应到表单中为单选按钮，可选值根据options字典关联数据字典
			 * 					name:"name",//元素的名字
			 * 					validate:"validate",//验证规则
			 * 					status:"",//是否在表单中显示 1、显示  0、不显示
			 * 					classes："",//元素样式
			 * 					style:"",//行内样式
			 * 					options:"",//当元素类型为bl或者fv时，参考此值
			 * 					referenceExtralDataId:"",
			 *                  },...]
             * //表单相应值信息
             * options.data=[{......}]
             *
             * //表单提交的类对象
             * options.nameCode="",//表或类的英文名，用于编码中
             *
             * //options.id="",//对象id
             *
             * //基本信息
             * options.extraData=[{....}]
             *
             * options.model="";//model对象
             *
             */
            init: function(el, options) {
                var self = this;
                options.extraData = options.model.head;
                options.input = options.model.head.cols;
                self.cols={}
                if(!options.form) {
                    options.form = {};
                    options.form.isCreate = true;
                }
                if(!options.isQuery){
                	options.isQuery=false;
                }
                if(options.data && options.data.query) {
                    options.data = options.data.query;
                }
                if(options.id) {
                    Form.id["id"]=options.id;
                    options.model.get(options.id, function(valData) {
                        options.data = valData;
                        $(el).html(self._loadForm(options.form, options.input, options.data, options.model));
                        if(typeof self.options != 'undefined' && typeof self.options.finished != 'undefined' && $.isFunction(self.options.finished)){
                            self.options.finished(self);
                        }else if(typeof self.options != 'undefined' && typeof self.options.finished != 'undefined' && self.options.finished in window){
                            window[self.options.finished](self);
                        }
                    });
                } else {
                    $(el).html(self._loadForm(options.form, options.input, options.data, options.model));
                    if($.isFunction(self.options.finished)){
                        self.options.finished(self);
                    }else if(typeof self.options.finished != 'undefined' && self.options.finished in window){
                        window[self.options.finished](self);
                    }
                }
            },
            /**
             * 将列进行分组处理
             * @param {Object} cols 列集合
             * @param {Object} num  分组数，默认为分组数+1，额外的+1代表宽度80%的,此参数需可以整除12
             */
            _getInputGroup: function(cols, num) {
                if(!num) {
                    num =$.UiSetting.inputGroupNum;
                }
                var group = new Array();
                group[num] = new Array();
                var length = 0;
                for(c in cols) {
                    if(cols[c].length < 128) {
                        length++;
                    }
                }
                var numPerGroup = length % num > 0 ? parseInt(length / num + 1) : length / num;
                numPerGroup = numPerGroup == 0 ? 1 : numPerGroup;
                var t = 0;
                for(var i = 0; i < num; i++) {
                    group[i] = new Array();
                    while(t < cols.length && group[i].length < numPerGroup) {
                        if(cols[t].length < 128) {
                            group[i].push(cols[t]);
                        }
                        t++;
                    }
                }
                t = 0;
                while(t < cols.length) {
                    if(cols[t].length >= 128) {
                        group[num].push(cols[t]);
                    }
                    t++
                }

                return group;
            },
            //获取组的Dom元素,current：为当前组，countNum：总组数
            _getInputGroupContainer: function(current, countNum) {
                var size = $.UiSetting.colTotalSize / countNum;
                if(current == countNum) {
                    size = $.UiSetting.colTotalSize;
                }
                var $groupDiv = $("<div></div>");
                $groupDiv.addClass($.UiSetting.colClass + size);
                return $groupDiv;
            },

            //获取一个表单
            _loadForm: function(form, input, valData, model) {
                var $form = undefined;

                //目前基本没有form支撑无法执行表单的序列化操作，所有此处代码判断无意义，默认执行表单即可。
                if(form) {
                	var container;
					if(window['EL_OPTIONS']&&window['EL_OPTIONS'].form_container){
						container=$(window['EL_OPTIONS'].form_container)
					}
                    $form = $("<form class='form' onsubmit='return false'></form>");
                    if(form.id) {
                        $form.attr("id", form.id);
                    }
                    $form.attr("action", form.action);
                    if(form.method) {
                        $form.attr("method", form.method);
                    } else {
                        $form.attr("method", "post");
                    }
                    if(form.classes) {
                        $form.addClass(form.classes);
                    }
                    //此处修改，将循环迁移，方便加载样式和整体字段的显示和控制
                    var inputGroup = this._getInputGroup(input);
                    for(var i in inputGroup) {
                        if(inputGroup[i].length < 1) {
                            continue;
                        }
                        var DivGroup = this._getInputGroupContainer(i, $.UiSetting.inputGroupNum);
                        for(var j in inputGroup[i]) {
                            if(inputGroup[i][j].status == '1') {
                                DivGroup.append(this._loadInput(inputGroup[i][j], valData, model));
                            }
                        }
                        $form.append(DivGroup);
                    }
                    this.formEl = $form;
                    if(container){
                    	container.find(".el-container").append($form);
                    	if(window['EL_OPTIONS'].formOptionContainer&&this.options.isQuery==false){
                    		$form.append(window['EL_OPTIONS'].formOptionContainer);
                    	}
                    	return container;
                    }
                    return $form;
                }
            },
            /**
             * 加载表单输入元素，此处为单独加载单个表单元素好加载其样式
             * @param {input} 为单个列描述的数据
             */

            _loadInput: function(input, valData, model) {

                var user = $.session.currentUser;
                var self=this;

                if(input.status == '1') {
                    var $div = $("<div class='"+$.UiSetting.formContainerDefaultClass+"'></div>");
                    var $label = $("<label></label>");
                    $label.html(input.colName + "：");

                    var $input = this._getInput(input, valData, model);
                    if(input.colType != 1) {
                        $div.css("display", "none");
                        //$input.attr("readonly","readonly");
                    }
                    if(input.formClasses) {
                        $input.addClass(input.formClasses);
                    }
                    if(input.style) {
                        $input.attr("style", input.style);
                    }
                    if(input.isEdit == 0) {
                        if(user.owner == '100000'){

                            $input.attr("readonly","readonly");
                        }else {
                            $input.attr("readonly","readonly");
                            // $input.css("display", "none");
                        }
                    }
                    $div.append($label);
                    $div.append($input);
                    input.el=$div;
                    self.cols[input.colCode]=input;
                    if(input.event!="") {
                        if(window[input.event]){
                            window[input.event](valData,input,self);
                        }
                    }
                    return $div;
                }

            },
            inputType:{
                //时间类型输入
                t:function(input,valData,self){
                    if("undefined" != typeof WdatePicker) {
                        var time_format = 'yyyy-MM-dd HH:mm:ss';
                        if(input.options && input.options != "") {
                            try {
                                if(typeof input.options=="string"){
                                    input.options = $.parseJSON(input.options);
                                }
                                time_format = input.options.addForm.format;
                            } catch(e) {
                                console.error("the options of <" + input.colCode + "> must be a valid JSON varchar.");
                            }
                        }
                        var $input = $("<input type='text' name='" + input.colCode + "' onFocus=\"\"/>");
                        var WPoptions = {
                            onpicked: function() {
                                $(this).change();
                            },
                            dateFmt: time_format
                        };
                        if(input.options && input.options.where) {
                            try {
                                var whereOpt = Form.timeWhere[input.options.where];
                                $.extend(true, WPoptions, whereOpt);
                            } catch(e) {
                                log.warn("the where of options of '" + input.colCode + "' is missing, please check your value in timeWhere of FormControl is defined.");
                            }
                        }
                        $input.focus(function() {
                            WdatePicker(WPoptions)
                        });
                    } else {
                        $input = $("<input type='datetime-local' name='" + input.colCode + "' />");
                    }
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                //字符串输入
                s:function(input, valData,self){
                    var $input = $("<input type='text' name='" + input.colCode + "' />");
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                //double输入
                d:function(input,valData,self){
                    var $input = $("<input type='number' name='" + input.colCode + "' />");
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                //integer
                i:function(input,valData,self){
                    var $input = $("<input type='number' name='" + input.colCode + "' />");
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                //boolean输入
                b:function(input, valData,self){
                    var $input = $("<select name='" + input.colCode + "'></select>");
                    $input.append("<option value=''>--全部--</option>")
                    $input.append("<option value='1'>是</option>")
                    $input.append("<option value='0'>否</option>")
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    if(valData) {
                        $input.attr("selected", "selected");
                    }
                    return $input;
                },
                //文件输入
                f:function(input, valData,self){
                    var $input = $("<div name='" + input.colCode + "' class='ibox-content'></div>");
                    if(valData) {
                        var upload = undefined;
                        if(input.options) {
                            upload = new Upload($input, {
                                value: valData[input.colCode],
                                fileNumLimit: input.options
                            });
                        } else {
                            upload = new Upload($input, {
                                value: valData[input.colCode]
                            });
                        }
                        $input.data("control", upload);
                    } else {
                        var upload = new Upload($input, {
                            value: null
                        });
                        $input.data("control", upload);
                    }
                    return $input;
                },
                m:function(input, valData,self){
                    var $input = $("<div name='"+input.colCode+"'  class='map'></div>");
                    var $innerInput=$("<input type='text' class='form-control'/>");
                    var $address = $("<input type='hidden' name='address' />");
                    var $lng = $("<input type='hidden' name='lng' />");
                    var $lat = $("<input type='hidden' name='lat' />");
                    var $province = $("<input type='hidden' name='province' />");
                    var $city = $("<input type='hidden' name='city' />");
                    var $district = $("<input type='hidden' name='district' />");
                    var mapContiner=$("<div></div>");
                    var bdMapControl = new $.BDMapControl(mapContiner,{'div':$input});
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    $innerInput.attr("readonly", "readonly");
                    $innerInput.click(function() {
                        ModelShow.prompt(mapContiner, function(){
                            var result = bdMapControl.getAddress();
                            if('undefined' != result.address){
                                $innerInput.val(result.address);
                                $address.val(result.address);
                                $lng.val(result.location.lng);
                                $lat.val(result.location.lat);
                                //根据城市查询省份
                                $.getJSON("js-lib/city.json", function (data){
                                    var province = '';
                                    for (var i in data){
                                        var provinces = data[i];
                                        if ('' == province){
                                            for (var k in provinces.childRegs){
                                                var cities = provinces.childRegs[k];
                                                if (cities.name == result.city){
                                                    province = provinces.name;
                                                    break;
                                                }
                                            }
                                        }else{
                                            break;
                                        }
                                    }
                                    $province.val(province);
                                    $city.val(result.city);
                                    $district.val(result.district);
                                })
                                /*$.post($.request.domain + "/common/session", function (id) {

                                })*/
                                // $input.removeClass("validate" + input.validate)
                            }
                            return true;
                        });
                        //根据经纬度回显大头针
                        if($lng.val()!=''&&$lat.val()!=''){
                            bdMapControl.setLocation($lng.val(),$lat.val());
                        }
                    });
                    $input.append($innerInput);
                    $input.append($address);
                    $input.append($lng);
                    $input.append($lat);
                    $input.append($province);
                    $input.append($city);
                    $input.append($district);
                    $input.append();
                    $input.data("control",bdMapControl);
                    if(valData && valData[input.colCode]) {
                        if(self.options.model.target == 'sql') {
                            $innerInput.val(valData[input.colCode]);
                            $address.val(valData[input.colCode]);
                            $lng.val(valData['lng']);
                            $lat.val(valData['lat']);
                            $province.val(valData['province']);
                            $city.val(valData['city']);
                            $district.val(valData['district']);
                        }
                    }
                    return $input;
                },
                //大文本
                text:function(input, valData,self){
                    var $input = $("<textarea name='" + input.colCode + "'></textarea>");
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                //html输入
                h:function(input, valData,self){
                    var $input = $("<div name='" + input.colCode + "' class='h ibox-content'></div>");
                    $iboxDiv = $("<div class='ibox-content no-padding'></div>");
                    $summernoteDiv = ("<div class='summernote'></div>");
                    $iboxDiv.append($summernoteDiv);
                    $input.append($iboxDiv);
                    $input.data("h", input.colValType);

                    $input.find('div.summernote').summernote({
                        lang: 'zh-CN'
                    });
                    if(valData) {
                        $input.find("div.note-editable").html(valData[input.colCode]);
                    }
                    return $input;
                },
                //对象输入
                o:function(input, valData,self){
                    var $input = $("<div name='" + input.colCode + "' class='chlid_form'></div>");
                    $.Model().getHead(input.referenceExtralDataId, function(extraData) {
                        var modelObject = new $.Model(extraData);
                        if(valData) {
                            new Form($input, {
                                data: valData[input.colCode],
                                model: modelObject
                            });
                        } else {
                            new Form($input, {
                                model: modelObject
                            });
                        }
                    });
                    return $input;
                },
                ol:function(input, valData,self){
                    var $input = $("<div name='" + input.colCode + "' class='chlid_form ibox'></div>");
                    var $div_show_button = $("<div class='ibox-title'></div>");
                    var $div_show_table = $("<div style='overflow-x: auto;' class='ibox-content'></div>");
					$.ajaxSettings.async=false;
                    $.Model().getHead(input.referenceExtralDataId, function(extraData) {
                        var tab = undefined;
                        if( valData.cols==undefined){
                            valData.cols=valData[input.colCode];
                        }
                        if(valData) {
                            tab = new $.TableControl($div_show_table, {
                                isShowActs: true,
                                isEdit:true,
                                head: extraData,
                                list: valData[input.colCode]
                            });
                        } else {
                            tab = new $.TableControl($div_show_table, {
                                isShowActs: true,
                                isEdit:true,
                                head: extraData
                            });

                            tab.addNewData();
                        }
                        $input.data("control", tab);
                        var but = $("<button type='button' class='btn'>添加</button>");
                        but.click(function() {
                            tab.addNewData();
                        });

                        $div_show_button.append(but);

                        $input.append($div_show_button);
                        $input.append($div_show_table);
                        $.ajaxSettings.async=true;
                    });
                     if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    return $input;
                },
                bl:function(input, valData,self){
                    var $input = $("<div class='bl'></div>");
                    $.ajaxSettings.async=false;
                    $.Model().list(input.options, undefined, function(data) {
                        for(var i in data) {
                            var $in = $("<input type='checkbox' name='" + input.colCode + "'/>");
                            $in.val(data[i].id);
                            if(valData) {
                                for(var j in valData[input.colCode]) {
                                    if(valData[input.colCode][j] == data[i].id) {
                                        $in.attr("checked", "checked");
                                    }
                                }
                            }
                            $input.append($in);
                            var $label = $("<label></label>");
                            $label.html(data[i].name);
                            $input.append($label);

                            $in.change(function() {
                                if(valData) {
                                    var temArray = new Array();
                                    $input.find("input:checked").each(function() {
                                        temArray.push($(this).val());
                                    });
                                    self.options.data[input.colCode] = temArray;
                                }
                            });
                            $.ajaxSettings.async=true;
                        }
                    });
                    return $input;
                },
                fv:function(input, valData,self){
                    var $input = $("<select></select>");
                    $input.attr("name", input.colCode);
                    if(input.validate) {
                        $input.addClass("validate" + input.validate);
                    }
                    $input.append("<option value=''>--全部--</option>")
                    $.ajaxSettings.async=false;
                    $.Model().list(input.options, undefined, function(data) {
                        for(var i in data) {
                            var opt = $("<option></option>");
                            opt.val(data[i].id);
                            opt.text(data[i].name);
                            if(valData) {
                                if(valData[input.colCode] == data[i].id) {
                                    opt.attr("selected", "selected");
                                }
                            }
                            if(data[i].id)
                                $input.append(opt);
                        }
                        $.ajaxSettings.async=true;
                    });
                    return $input;
                },
                ref:function(input, valData,self){
                    var $input = $("<div name='" + input.colCode + "'  class='ref' style='position: relative;'></div>");
                    var $in = $("<input type='text' class='form-control'/>");
                    if(input.validate) {
                        $in.addClass("validate" + input.validate);
                    }
                    $in.attr("readonly", "readonly");
                    $in.click(function() {
                        if(self.options.refHander || window['refHander']) {
                            if (window['refHander']) {
                                window['refHander']($input, input);
                            }else{
                                self.options.refHander($input, input);
                            }
                        } else {
                            $.FormControl.defaultRefHander($input, input);
                        }
                    });
                    $input.append($in);
                    var $id = $("<input type='hidden' />");
                    $input.append($id);
                    $input.data("ref", self.options.model.target);
                    var closeDiv=$("<div style='width:20px;height:100%;display:none;position: absolute;right:0px;line-height: 35px;top:0px;' id='close'>x</div>")
                    $input.append(closeDiv);
                    closeDiv.click(function(){
                        $in.val("");
                        $id.val("");
                    })
                    $input.hover(function(){
                        closeDiv.show();
                    },function(){
                        closeDiv.hide();
                    })
                    if(valData && valData[input.colCode]) {
                        if(self.options.model.target == 'sql') {
                            if(valData[input.colCode] != 0) { //当前为一级分类
                                $.Model().getHead(input.referenceExtralDataId, function(head) {
                                    $.Model(head).get(valData[input.colCode], function(data) {
                                        $in.val(data.name);
                                    });
                                });
                            } else {
                                $in.val("默认");
                            }
                            $id.val(valData[input.colCode]);
                        } else {
                            $in.val(valData[input.colCode].name);
                            $id.val(valData[input.colCode].id);
                        }
                    }
                    return $input;
                },
                refl:function(input, valData,self){
                    var querystr='';
                    if(input.colCode=='enterpriseCustomLines'){
                        var object={}
                        object.enterpriseCustomId=valData.id==undefined?'':valData.id;
                          var querystr=  JSON.stringify(object);
                    }
                    var $input = $("<div name='" + input.colCode + "' class='refl ibox-content'></div>");
                    $.ajaxSettings.async = false;
                    $.Model().getHead(input.referenceExtralDataId, function(extraData) {
                        var modelObject = new $.Model(extraData);
                        var refTab = new $.TableControl($input, {
                            query:querystr,
                            model: modelObject,
                            checked: {
                                display: true,
                                isSingle: false
                            },
                            pageSetting: {
                                classes: "pagination",
                                lableClasses: "paginate_button"
                            }
                        });
                        $input.data("control", refTab);
                         $.ajaxSettings.async = true;
                    });
                    return $input;
                },
                role:function(input, valData,self){
                    var $input = $("<div name='"+input.colCode+"'  class='role'></div>");
                    Role.getlists(function(deps){
                        var data = {};
                        data.deps = deps;

                        var innerInput=$("<input type='text' class='form-control'/>");
                        $input.append(innerInput);
                        $input.append();
                        var roleContiner=$("<div></div>");
                        var bdRoleControl = new $.BDRoleControl(roleContiner,data);
                        if(input.validate) {
                            $input.addClass("validate" + input.validate);
                        }
                        innerInput.attr("readonly", "readonly");
                        innerInput.click(function() {
                            ModelShow.prompt(roleContiner, function(){
                                innerInput.val(bdRoleControl.getValue().address);
                                return true;
                            });
                        });
                        $input.data("control",bdRoleControl);

                    });
                    return $input;
                }
            },
            //根据数值类型创建一个表单元素
            _getInput: function(input, valData) {
                var self = this;
                if(!self.inputType[input.colValType]){
                    console.error("the input-type("+input.colValType+") is unknown ");
                    return;
                }
                var $input = self.inputType[input.colValType](input,valData,self);
                if(valData) {
                    $input.val(valData[input.colCode]);
                }
                $input.bind("change", function() {
                    if(valData && input.colValType != 'bl') {
                        self.options.data[input.colCode] = $input.val();
                    }
                });
                if($input[0].tagName != "DIV")
                    $input.addClass("form-control");
                return $input;
            },
            //获取所有表单元素的值
            getValue: function() {
                var self = this;
                var data = undefined,isReturn = true;
                if(!self.formEl.validationEngine("validate")) {
                    return false
                }
                if(self.options.data) {
                    data = self.options.data;
                } else {
                    data = self.element.find("form").formParams();
                }
                self.element.find("div[name]").each(function() {
                    var div = this;
                    if($(this).data("control")) {
                        var control = $(this).data("control");
                        if (typeof control.validate != 'undefined' && control.validate() == false){
                            isReturn = false;
                            return false;
                        }
                        //如果是地图，需要将data传过去添加经纬度。
                        if(true == $(this).hasClass('map')){
                            data = control.getValue(div,data);
                        }else{
                        	var tval=control.getValue(div);
                            data[$(this).attr("name")] =tval; 
                        }
                        if($(this).attr("class").indexOf("required")>-1&&!data[$(this).attr("name")]){
                            isReturn = false;
                            return false;
                        }
                    } else if($(this).data("h")) { //编辑器
                        data[$(this).attr("name")] = $(this).find("div.note-editable").code();
                    } else if($(this).data("ref")) { //引用
                        data[$(div).attr("name")] = {};
                        $(this).find("input").each(function() {
                            if($(div).data("ref") == 'sql') { //mysql表
                                if(!$(this).attr("class")) {
                                    data[$(div).attr("name")] = $(this).val();
                                }
                            } else { //mongo表
                                if($(this).attr("class")) {
                                    data[$(div).attr("name")]['name'] = $(this).val();
                                } else {
                                    data[$(div).attr("name")]['id'] = $(this).val();
                                }
                            }
                        });
                    }
                });
                return isReturn==true?data:isReturn;
            },

        });
        $.FormControl = Form;
        return Form;
    });