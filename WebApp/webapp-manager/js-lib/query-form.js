/**
 * 动态的生成表单
 */
steal('can',
    function(can) {
        var Form = $.FormControl.extend({
            _getInputGroupContainer: function(current, countNum) {
                var $groupDiv = $("<div></div>");
                $groupDiv.addClass("col-lg-12");
                return $groupDiv;
            },
            _getInputGroup: function(input) {
                var gp = new Array();
                gp[0] = input
                return gp;
            },
            _loadInput: function(input, valData) {
                var self = this;
                var user = $.session.currentUser;
                if(user.owner != '100000'&&user.dep) {
                    var depName = user.dep.name;
                    var colsAuthorizations=input.colsAuthorizations;
                    if (colsAuthorizations!=undefined&&colsAuthorizations.length != 0) {//对该列设置了查询条件显示权限
                        for (var j = 0; j< colsAuthorizations.length; j++) {
                            var colsAuthorization = colsAuthorizations[j];
                            if (colsAuthorization.roleId == depName) {
                                input.isSeach=colsAuthorization.isSeach;
                            }
                        }
                    }
                }

                if(input.status == '1' && input.isSeach == 1) {
                    if(input.colValType == 't' ||input.colValType == 'ref' || input.colValType == 's' || input.colValType == 'd' || input.colValType == 'b' || input.colValType == 'fv') {
                        var $div = $("<div class='form-group'></div>");
                        var $label = $("<label></label>");
                        $label.html(input.colName + "：");
                        var $input = this._getInput(input, valData);
                        if(input.formClasses) {
                            $input.addClass(input.formClasses);
                        }
                        if(input.style) {
                            $input.attr("style", input.style);
                        }
                        $div.append($label);
                        $div.append($input);
                    }
                }
                return $div;
            },
            //根据类型获取相应的查询元素
            _getInput: function(input, valData) {
                var self = this;
                var $input = $("<div></div>");
                if(input.colValType == "t") {
                    $input.addClass("chlid_form");
                    $input.attr("name", input.colCode)
                    if("undefined" != typeof WdatePicker) {
                        var data_options='yyyy-MM-dd HH:mm',inputType = 2;
                        if(input.options && input.options != "") {
                            try {
                                if(typeof input.options=="string"){
                                    input.options = $.parseJSON(input.options);
                                }
                                data_options = input.options.queryForm.format;
                                if (typeof input.options.queryForm.inputType != 'undefined'){
                                    inputType = input.options.queryForm.inputType;
                                }
                            } catch(e) {
                                console.error("the options of <" + input.colCode + "> must be a valid JSON varchar.");
                            }
                        }
                        var idS = "ids" + new Date().getTime();
                        var idE = "ide" + new Date().getTime();
                        if(inputType == 2){
                            $input.append("<input id='" + idS + "'  type='text' name='start' class='Wdate'  onclick=\"WdatePicker({dateFmt:'"+data_options+"'})\"/>");
                            $input.append("&nbsp;至&nbsp;");
                            $input.append("<input id='" + idE + "' type='text' name='end'  class='Wdate'  onclick=\"WdatePicker({dateFmt:'"+data_options+"'})\"/>");
                        }else{
                            $input.append("<input id='" + idS + "'  type='text' name='start' class='Wdate'  onclick=\"WdatePicker({dateFmt:'"+data_options+"'})\"/>");
                        }
                         $input.find("input").css("width",data_options.length*12+"px");
                    } else {
                        $input.append("<input  type='datetime-local' name='start' />");
                        $input.append("&nbsp;至&nbsp;");
                        $input.append("<input type='datetime-local' name='end' />");
                    }
                   
                    $input.append("<input type='hidden' name='t_type' value='"+inputType+"'/>");
                    $input.data("t",true);
                } else if(input.colValType == 's') {
                    $input.append("<input type='text' name='" + input.colCode + "' />");
                } else if(input.colValType == 'd' || input.colValType == 'f' || input.colValType == 'i') {
                    $input.append("<input type='number' name='" + input.colCode + "' />");
                } else if(input.colValType == 'b') {
                    $input.append("<select name='" + input.colCode + "'></select>");
                    $input.find("select").append("<option value>--全部--</option>");
                    $input.find("select").append("<option value='1'>是</option>");
                    $input.find("select").append("<option value='0'>否</option>");
                } else if(input.colValType == 'fv') {
                    var minput = $("<select></select>");
                    minput.attr("name", input.colCode);
                    minput.append("<option value>--全部--</option>")
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
                            minput.append(opt);
                        }
                    });
                    $input.append(minput);
                }else if(input.colValType=='ref'){

                    $input.append(this.inputType.ref(input,valData,self));

                }
                if(valData) {
                    $input.val(valData[input.colCode]);
                }
                $input.change(function() {
                    if(valData) {
                        self.options.data[input.colCode] = $input.val();
                    }

                });
                if(input.colValType!="ref"){
                    $input.find("input").addClass("form-control");
                }
                $input.find("select").addClass("form-control");
                return $input;
            },
            /**
             * 获取当前表单对象的值，值通过表单序列化后得到，返回一个Js对象，该对象的结构遵循在元素中配置的结构，此方法与lif-form工作流程一致，
             * 不同的地方是查询条件qp.sections，该条件包含了复合查询方式，如时间段、区间段等，可根据后台业务逻辑用于不同的场景
             * 获取最终对象分2步
             * 		1、将基本的表单进行序列化得到最基本的对象
             * 		2、将复合对象以及自定义插件的元素，封装到原始对象中
             */
            getValue: function() {
                var self = this;
                var data = undefined;
                if(self.options.data) {
                    data = self.options.data;
                } else {
                    data = self.element.find("form").formParams();
                }

                self.element.find("div[name]").each(function() {
                    var div = this;
                    if($(this).data("control")) {
                        var control = $(this).data("control");
                        data[$(this).attr("name")] = control.getValue();
                        if($(this).attr("class").indexOf("required")>-1&&!data[$(this).attr("name")]){
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
                    }else if($(this).data("t")){
                        if(!data.qp){
                            data.qp={}
                            data.qp.sections=new Array();
                        }
                        data.qp.sections.push({'colName':$(this).attr("name"),'type':$(this).find("[name='t_type']").val(),'start':$(this).find("[name='start']").val(),'end':$(this).find("[name='end']").val()});
                    }
                });
                return data;
            },
        });
        $.QueryFormControl = Form;
        return Form;
    })