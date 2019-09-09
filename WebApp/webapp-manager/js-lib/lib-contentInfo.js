/**
 * 动态的生成表格，并提供表格内的的基本操作进行
 */
steal(
    'can',
    function(can) {
        var Control = $.FormControl.extend({
            init: function(el, options) {
                el.html("");
                if(EL_OPTIONS.detailContainer){
                	el.append(EL_OPTIONS.detailContainer);
                }else{
                	var title = $("<div class='ibox-title'><h5>详情信息</h5></div>");
                	el.append(title);
                	el.append("<div class='ibox-content '><div class='row form-detail'></div></div>");
                }
               
                var self = this;
                //this.initParameter(this); //初始化实例对象参数
                //this.filterType(this); //是否过滤各类型参数
                //$.Modal.confirm(contentId,this.html, null, null);

                $.TableControl._loadDataDict(options.colsTitle, function() {
                    var inputGroup = self._getInputGroup(options.colsTitle.cols);
                    for(var i in inputGroup) {
                        if(inputGroup[i].length < 1) {
                            continue;
                        }
                        var DivGroup = self._getInputGroupContainer(i, $.UiSetting.inputGroupNum);
                        var user = $.session.currentUser;
                        for(var j in inputGroup[i]) {
                            if(user.owner != '100000'&&user.dep) {
                                var depName = user.dep.name;//（角色）名称
                                var status=inputGroup[i][j].status;
                                var colsAuthorizations=inputGroup[i][j].colsAuthorizations;
                                if (colsAuthorizations!=undefined&&colsAuthorizations.length != 0) {//对该列设置了读写权限
                                    for (var K = 0; K < colsAuthorizations.length; K++) {
                                        var colsAuthorization = colsAuthorizations[K];
                                        if (colsAuthorization.roleId == depName) {
                                            if (colsAuthorization.isShowread == 1) {
                                                if(inputGroup[i][j].status == '1') {
                                                    DivGroup.append(self.getContent(inputGroup[i][j], options.colsVal));
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    if(inputGroup[i][j].status == '1') {
                                        DivGroup.append(self.getContent(inputGroup[i][j], options.colsVal));
                                    }
                                }

                            }else {
                                if(inputGroup[i][j].status == '1') {
                                    DivGroup.append(self.getContent(inputGroup[i][j], options.colsVal));
                                }
                            }
                        }
                        el.find(".form-detail").append(DivGroup);

                    }
                });
            },
            _defaultDataFilter: function(head, val, el,obj) {
                if(val != undefined&&val!="") {
                    if(head.colValType == "s") {
                        return val;
                    } else if(head.colValType == "b") {
                        if(val == 2) {
                            return "待审核";
                        }
                        return val == 1 ? "是" : "否";
                    } else if(head.colValType == "f") {
                        el.html("<img src='" + val + "' width='120' height='120'/>");
                        el.click(function(){
                            window.open($(this).find("img").attr("src"));
                        })
                        return undefined;
                    } else if(head.colValType == 'fv') {
                        return head.DV[val];
                    } else if(head.colValType == 'ref') {
                        if(val.name) {
                            return val.name;
                        } else if(obj[head.colCode.substr(0,head.colCode.indexOf("Id"))]){
                            return obj[head.colCode.substr(0,head.colCode.indexOf("Id"))].name;
                        }else{
                            return val;
                        }
                    }  else if(head.colValType == 'refl') {
                         if(!val||val==""){
                            return "无";
                        }else{
                        	var res="";
                        	for(i in val){
                        		if(val[i].name)
                        			res+=val[i].name+","
                        		else
                        			console.warn("no name was found")
                        	}
                            return res.substr(0,res.length-1);
                        }
                    } else if(head.colValType == 'ol') {
                        $.Model().getHead(head.referenceExtralDataId, function(refCols) {
                            new $.TableControl(el, {
                                isShowActs: false,
                                head: refCols,
                                list: val,
                                listClasses:"form-detail-table"
                            });
                        });
                        return undefined;
                    }else if(head.colValType == 'refl') {
                        $.Model().getHead(head.referenceExtralDataId, function(refCols) {
                            new $.TableControl(el, {
                                isShowActs: false,
                                head: refCols,
                                list: val,
                                listClasses:"form-detail-table"
                            });
                        });
                        return undefined;
                    } else if(head.colValType == 'i'){
                        if(!val||val==""){
                            return 0;
                        }else{
                            return val;
                        }
                    }else{
                        return val;
                    }
                } else {
                    return "--";
                }
            },
            getContent: function(cols, colsVal) {
                var el = $("<dl class='dl-horizontal'></dl>");
                var $title = $("<dt class='name'></dt>");
                var $value = $("<dd class='value'></dd>");
                if(cols.colOrder>900){
                	return;
                }
                if(this.options.alias&&this.options.alias[cols.colCode]){
                    $title.html(this.options.alias[cols.colCode]);
                }else{
                    $title.html(cols.colName);
                }
                var val;
                if(this.options.dataFilter&&$.isFunction(this.options.dataFilter)) {
                    val = this.options.dataFilter();
                }else if(window[this.options.dataFilter]&&$.isFunction(window[this.options.dataFilter])){
                	val=window[this.options.dataFilter](cols, $(colsVal).attr($(cols).attr("colCode")), $value,colsVal);
                }else{
                	val=$(colsVal).attr($(cols).attr("colCode"));
                }
                val = this._defaultDataFilter(cols, val, $value,colsVal);
                if(val) {
                    $value.html(val);
                }
                el.append($title);
                el.append($value);
                return el;
            }
        });
        $.ContentInfoControl = Control;
        return Control;
    });