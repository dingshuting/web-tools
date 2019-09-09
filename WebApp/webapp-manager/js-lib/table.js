/**
 * 动态的生成表格，并提供表格内的的基本操作进行
 */
steal(
    'can',
    'js/modal-show.js',
    'plugins/webuploader/upload.js',
    function(can, ModelShow, Upload) {
        steal('plugins/jquery-validation/css/validationEngine.jquery.css');
        steal('plugins/jquery-validation/css/template.css');
        var Control = can.Control.extend({
            /**
             * 加载数据字典信息，并将其绑定到指定的列上
             * @param {Object} head
             */
            _loadDataDict: function(head, callback) {
                //判断是否是最后一个获取的数据字典选项
                var isLastFv = function(cols, i) {
                    if(i >= cols.length - 1)
                        return true;
                    for(var ti = (parseInt(i) + 1); ti < cols.length; ti++) {
                        if(cols[ti].colValType == "fv") {
                            return false;
                        }
                    }
                    return true;
                };
                var hasFv = 0;
                for(var i in head.cols) {
                    if(head.cols[i].colValType == "fv") {
                        hasFv++;
                        $.Model().list(head.cols[i].options, undefined, function(data) {
                            for(var j in head.cols) {
                                if(head.cols[j].options == data[0].pid) {
                                    head.cols[j].DD = data;
                                    head.cols[j].DV = {};
                                    for(var k in data) {
                                        head.cols[j].DV[data[k].id] = data[k].name;
                                    }
                                    if(isLastFv(head.cols, j)) {
                                        callback();
                                    }
                                }
                            }

                        });

                    }
                }
                if(hasFv < 1) {
                    callback();
                }
            }
        }, {
            /**
             *
             * @param {Object} el
             * @param {Object} options
             * //表格的头部配置信息
             * options.head={cols：[{
			 * 				colName:'name',//列显示的名称
			 * 				colCode:'code',//列的name编码即数据model的列名
			 * 				colValType:'',//值的类型
			 * 				style:'width:20%',//列的宽度
			 * 				validate:"[required,max[12],...]"//验证字符串,参考验证框架，有新增时则会用到
			 * 				options:<array>,//如有则是关联的数据字典id
			 * 				},...],classes:<string>}
             * //表格显示的动态数据，其数据的列名，与参数head的code一一对应,其中数据扩展2个字段（canEdit，canDelete）用于标识数据是否可以被修改或者删除
             * options.list=[{canDelete:true,canEdit:true,...},...]
             * options.value=[{data}];表格的当前值，用于多选或单选的回显操作
             * //加载数据的可操作项，默认包含了改和删的功能
             * options.dataActions=[{
			 * 				name:'name',//操作显示的名称
			 * 				func:function(rowdata),//点击操作触发的功能函数，其中触发时会将单击的数据作为参数
			 * 				beforActionLoad:function(rowdata)//在数据动作加载前执行函数，用于判断是否进行功能函数的显示，返回true或false，可为空，默认返回true
			 * 				},...]
             * //加载表格的行内样式
             * //是否显示操作列
             * options.isShowActs=true|false
             * //是否显示表格序号
             * options.isShowIndex=true|false
             * //当model不为空时，则为异步获取表格数据，此model为标准的Model对象，直接调用list等方法获取动态数据
             * options.model
             * //当前的查询条件对象，遵循mongoDB的查询规则
             * options.query
             * //是否显示分页
             * options.pageSetting=
             * 		{
			 * 			//是否显示分页
			 * 			isShow:<boolean>,
			 * 			//每页显示数据量
			 * 			//dom元素,此配置可空，如有值则会将label添加加到此元素中
			 * 			theme:<DomElement>,
			 * 			//整体样式，整个分页标签的父div容器的类样式
			 * 			classes：<string>,
			 * 			//dom元素标签，不写则默认为<li/>格式
			 * 			label:<DomElement>,
			 * 			lableClasses:<string>
			 * 		}
             * //是否开启可选择选项
             * options.checked=
             * 		{
			 * 		//是否开启表格可选择项,默认为false
			 * 			display:<boolean>,
			 * 			//是否单选,否则代表多选
			 * 			isSingle:<boolean>,
			 * 		}
             *
             */
            init: function(el, options) {
                var self = this;
                this.init_args();

                this.init_view();


            },
            refresh: function() {
                this._loadDataByAjax(self.options.currentPage);
            },
            //初始化页面视图，model不为空时则从后台获取数据，否则数据和列头需要传入否则表格将无法显示
            init_view: function() {
                self = this;
                if(this.options.model) {
                    //计算列的宽度
                    this.options.head = this.options.model.head;
                    if(this.options.head) {
                        this.init_td_width();
                        $.TableControl._loadDataDict(self.options.head, function() {
                            self._loadDataByAjax(1);
                        });
                    }
                } else {
                    if(this.options.head) {
                        this.init_td_width();
                        $.TableControl._loadDataDict(this.options.head, function() {
                            var table = self._getTable(self.options.head.cols, self.options.list);
                            if(self.options.head.width >(parseInt($(document.body).width())-parseInt($(document.body).width())*0.12)) {
                            	table.css("width", self.options.head.width + "px");
                            }
                            self.element.html(table);
                            
                        });

                    }
                }
                if(window["lodingCols"]) {
                    window["lodingCols"](self);
                }
            },
            init_td_width: function() {
                //计算列的宽度
                var count = 0;
                for(var i in this.options.head.cols) {
                    if(this.options.head.cols[i].isShowInList == 1||(this.options.isEdit==true&&this.options.head.cols[i].isEdit==1)) {
                        count += this.options.head.cols[i].length;
                    }

                }
                for(var i in this.options.head.cols) {
                    if(this.options.head.cols[i].isShowInList == 1||(this.options.isEdit==true&&this.options.head.cols[i].isEdit==1)) {
                        var tw = this.options.head.cols[i].length / count;
                        if(this.options.head.cols[i].length > 120) {
                            tw = this.options.head.cols[i].length / 2 / count;
                        }
                        this.options.head.cols[i].width = (tw * 90) + "%";
                    }
                }
                this.options.head.width = count * 8;
            },
            //初始化配置参数
            init_args: function() {
                this.dataAdded = new Array();
                this.dataRemoved = new Array();
                this.dataChanged = new Array();
                this.dataChecked = new Array();
                 if(this.options.isEdit == undefined) {
                 	this.options.isEdit=false;
                 }
                if(this.options.isShowIndex == undefined) {
                    this.options.isShowIndex = true;
                }
                if(this.options.isShowActs == undefined) {
                    this.options.isShowActs = true;

                }
                if(!this.options.pageSetting) {
                    this.options.pageSetting = {
                        isShow: false
                    }
                }
                if(!this.options.list) {
                    this.options.list = [];
                }

                if(!this.options.checked) {
                    this.options.checked = {};
                    this.options.checked.display = false;
                    this.options.checked.isSingle = true;
                } else {
                    this.options.isShowActs = false;
                }
                if(this.options.checked.display) {
                    this.options.isShowIndex = false;
                    if(this.options.checked.isSingle == undefined) {
                        this.options.checked.isSingle = true;
                    }
                }
                if(!this.options.query) {
                    this.options.query = {};
                }
                if(this.options.dataActions && this.options.model) {
                    this.defaultActions = this.options.dataActions;
                }

            },
            //当数据改变时调用此方法来记录改变的数据
            pushRowData: function(rowData) {
                if(rowData._id) {
                    for(var i in this.dataChanged) {
                        if(this.dataChanged[i]._id == rowData._id) {
                            this.dataChanged[i] = rowData;
                            return;
                        }
                    }
                    this.dataChanged.push(rowData);
                    if(this.options.list) {
                        for(var i in this.options.list) {
                            if(this.options.list[i]._id == rowData._id) {
                                this.options.list[i] = rowData;
                            }
                        }
                    }
                }
            },
            //默认提供的操作项，在可编辑的情况下使用
            defaultActions: [{
                name: "编辑",
                func: function() {
                    var rowData = $(this).data("val");
                    var self = $(this).data("self");
                    self._getNewRow(self.options.head.cols, rowData, rowData.el, true);
                },
                beforActionLoad: function(rowdata) {
                    if(rowdata.canEdit) {
                        return rowdata.canEdit;
                    } else {
                        return true;
                    }
                }
            }, {
                name: "删除",
                func: function() {
                    var self = $(this).data("self");
                    var rowdata = $(this).data("val");
                    rowdata.el.remove();
                    if(rowdata.id) {
                        self.dataRemoved.push(rowdata);
                        for(var i in self.dataChanged) {
                            if(self.dataChanged[i].id == rowdata.id) {
                                self.dataChanged[i] = undefined;
                                self.dataChanged.splice(i, 1);
                            }
                        }
                        if(self.options.list) {
                            for(var i in self.options.list) {
                                if(self.options.list[i].id == rowdata.id) {
                                    self.options.list[i] = undefined;
                                    self.options.list.splice(i, 1);
                                }
                            }
                        }
                    }

                },
                beforActionLoad: function(rowdata) {
                    if(rowdata.canDelete) {
                        return rowdata.canDelete;
                    } else {
                        return true;
                    }
                }
            }],
            /**
             * 根据传入的list配置参数获取table的head
             * @param {Object} head
             */
            getThead: function(head) {
                $head = $("<tr></tr>");
                var self = this;
                if(this.options.isShowIndex) {
                    $head.append("<th style='width:5%;text-align:center;'>序号</th>");
                }
                if(this.options.checked.display) {
                    var $headEl = $("<th style='width:10%;text-align:center;'></th>");
                    if(!this.options.checked.isSingle) {
                        var $checkAll = $("<input type='checkbox' title='全选'/>");
                        $checkAll.click(function() {
                            if(this.checked || this.checked) {
                                self.element.find("[name=choseData]").prop("checked", true);
                            } else {
                                self.element.find("[name=choseData]").prop("checked", false);
                            }
                            self.element.find("[name=choseData]").change();
                        });
                        $headEl.append($checkAll);
                    }
                    $head.append($headEl);
                }
                for(var i in head) {
                	if(head[i].colType==2&&self.options.isEdit==true){
                		continue;
                	}
                    var isShowInList=head[i].isShowInList;
                    var user = $.session.currentUser;
                    if(isShowInList == 1||(self.options.isEdit==true&&head[i].isEdit==1)) {
                        var $th = $("<th style='text-align:center;'></th>");
                        $th.attr("style", head[i].style);
                        $th.attr("width", head[i].width);
                        if(self.options.alias&&self.options.alias[head[i].colCode]){
                            $th.html(self.options.alias[head[i].colCode]);
                        }else{
                            $th.html(head[i].colName);
                        }

                        $head.append($th);
                    }
                    head[i].isShowInList=isShowInList;
                }
                if(this.options.isShowActs&&this.options.isShowActs==true) {
                    $head.append("<th style='text-align:center;width:5%'>操作</th>");
                }
                return $head;

            },

            //添加新数据的操作，调用自动提示出相关的输入项，保存后数据存入js缓存后续一并提交数据
            addNewData: function() {
                this.element.find("table").append(this._getNewRow(this.options.head.cols));
            },
            getValue: function() {
            	//$(".temp-opt .confirm").trigger("click");
                if(this.options.checked.display) {
                    return this.getDataList().checked;
                }
                return this.getDataList().dataList;
            },
            //获取当前table需要进行保存或者修改的动态数据
            getDataList: function() {
                var dataAdded = new Array();
                var datalist = new Array();
                var obj = this;
                this.element.find("table tr:gt(0)").each(function() {
                    if($(this).data("val")) {
                        if(rowdata != undefined) {
                            obj.options.list.push(rowdata);
                        }

                        var rowdata = obj._filterData(obj.options.head.cols, $(this).data("val"));
                        dataAdded.push(rowdata);
                        datalist.push(rowdata);
                        $(this).data("val", undefined);
                    }
                });
                for(var i in this.options.list) {
                    datalist.push(obj._filterData(obj.options.head.cols, this.options.list[i]));
                }
                return {
                    "changed": this.dataChanged,
                    "added": dataAdded,
                    "removed": this.dataRemoved,
                    "dataList": datalist,
                    "checked": this.dataChecked
                }
            },
            setQuery: function(query) {
                this.options.query = query;
                this._loadDataByAjax(1);
            },
            //过滤对象，将对象中除去头部定义之外的字段清除
            _filterData: function(head, data) {
                var data1 = {};
                var isHas = false;
                for(var i in head) {
                    data1[head[i].colCode] = $(data).attr(head[i].colCode);
                }
				if(data['id']&&!data1['id']){
					data1['id']=data['id'];
				}
                return data1;
            },
            //重新刷新行内容
            _refreshRow: function(row, rowData) {
                var self = row.data("self");
                self._getNewRow(self.options.head.cols, rowData, row);
            },
            //加载行的操作
            _loadActions: function(row) {
                var $optTd = $("<td class='lt-opt'><div class='org-opt btn-group'><button data-toggle='dropdown' class='btn btn-warning btn-sm dropdown-toggle'>操作 <span class='caret'></span></button></div></td>");
                var $optOptOrg = $("<ul class='dropdown-menu'></ul>");
                var self=this;
                for(var i in this.defaultActions) {
                    if(!this.defaultActions[i].beforActionLoad) {
                        this.defaultActions[i].beforActionLoad = function() {
                            return true
                        }
                    }
                    if(this.defaultActions[i].beforActionLoad(row)) {
                        var $optdiv = $("<li></li>")
                        var $optEl = $("<a href='javascript:void(0)'>" + this.defaultActions[i].name + "</a>");
                        $optEl.data("val", row);
                        $optEl.data("self", this);
                        if(this.defaultActions[i].args){
                            $optEl.data("func", this.defaultActions[i].args.func);
                            $optEl.data("opt", this.defaultActions[i].args.opt);
                        }
                        $optEl.bind("click",self.defaultActions[i].func);
                        $optdiv.append($optEl);
                        $optOptOrg.append($optdiv);
                    }
                }
                $optTd.find('.dropdown-toggle').dropdown();
                $optTd.find(".btn-group").append($optOptOrg);
                return $optTd;

            },
            //获取表格的实例
            _getTable: function(head, dataList) {
                var $table = $("<table width='100%'></table>");
                self = this;
                if(self.options.listClasses){
                	$table.addClass(self.options.listClasses);
                }else{
                	$table.addClass(TPL_TABLE_OPTION.listClasses);
                }
                $table.append(self.getThead(head));
                for(var i in dataList) {
                    var row = this._getNewRow(head, dataList[i], undefined, undefined, true);
                    if(self.options.isShowIndex) {
                        if(self.options.pageSetting.isShow) {
                            i = (self.dataModel.currentPage - 1) * self.dataModel.perPageSize + parseInt(i) ;
                        }
                        //如果当前为选择插件，那么序号将由单选框代替

                        row.find("td:first").before("<td>" + (i*1+1) + "</td>");

                    }
                    if(this.options.checked.display) {
                        var $checkbox;
                        if(this.options.checked.isSingle) {
                            $checkbox = $("<input type='radio' name='choseData' />");
                            $checkbox.data("self",self);
                            $checkbox.click(function() {
                            	self=$(this).data("self");
                                self.dataChecked[0] = $(this).data('val');
                            });
                        } else {
                            $checkbox = $("<input type='checkbox' name='choseData' />");
                            $checkbox.data("self",self);
                            for(var j in self.dataChecked) {
                            	if(dataList[i].id==self.dataChecked[j].id){
                            		$checkbox.prop("checked","checked");
                            	}
                            }
                            $checkbox.change(function() {
                            	self=$(this).data("self");
                                if($(this)[0].checked) {
                                	for(var j in self.dataChecked) {
                                        if(self.dataChecked[j].id == $(this).data("val").id) {
                                        	return;
                                        }
                                    }
                                    self.dataChecked.push($(this).data('val'));
                                } else {
                                    for(var j in self.dataChecked) {
                                        if(self.dataChecked[j].id == $(this).data("val").id) {
                                            self.dataChecked.splice(j, 1);
                                        }
                                    }
                                }
                            });
                        }
                        if(self.options.value) {
                            for(var i in self.options.value) {
                                if(self.options.value[i]._id == dataList[i]._id) {
                                    $checkbox.prop("checked", true);
                                    break;
                                }
                            }
                        }
                        var $td = $("<td></td>");
                        $td.append($checkbox);
                        row.find("td:first").before($td);
                        $checkbox.data("val", dataList[i]);

                    }
                    $table.append(row);
                    $table.data("self", self);

                }
                self.table = $table;
                return self.table;
            },
            /**
             * 获取一条新行
             * @param {Object} head 头的定义
             * @param {Object} rowData 行数据
             */
            _getNewRow: function(head, rowData, oldRow, showEdit, isShow) {
                var $row = oldRow ? oldRow : $("<tr></tr>");
                $row.empty();
                var self = this;
                $row.data("self", self);
                if(self.options.isEdit){
                	$row.addClass("validationEngineContainer");
                }
                if(self.options.isShowIndex && !isShow) {
                    $row.append("<td>--</td>");
                }
                $(head).each(function() {
                	var newCol=self._getNewCol(this, rowData, rowData ? showEdit : true)
                	if(newCol)
                    	$row.append(newCol);
                    
                });
                if(rowData) {
                    if(this.options.isShowActs&&this.options.isShowActs==true) {
                        var org_action = this._loadActions(rowData);
                        if(showEdit) {
                            org_action.find(".org-opt").hide();
                            org_action.append(self._loadConfirmActionInRow($row, rowData));
                        }
                        $row.append(org_action);
                    }
                    rowData.el = $row;
                } else {
                    var org_action = $("<td class='temp-opt'></td>")
                    org_action.find(".org-opt").hide();
                    org_action.append(self._loadConfirmActionInRow($row, rowData));
                    $row.append(org_action);
                }

                return $row;
            },
            //加载对表格数据的修改和确认消息的动作，此时表格数据也成对应的可修改项
            _loadConfirmActionInRow: function(row, rowData) {
                var $temp_opt = $("<div class='temp-opt'></div>");
                var $confirm = $("<a class='confirm' href='javascript:void(0)'>确认  </a>");
                var $cancel = $("<a class='cancle' href='javascript:void(0)'>  取消</a>");
                var self = $(row).data("self");
                $cancel.click(function() {
                    if(rowData) {
                        self._getNewRow(self.options.head.cols, rowData, row, false);
                    } else {
                        row.remove();
                    }
                    $(row).find(".temp-opt").fadeOut();
                    $(row).find(".org-opt").fadeIn();
                });
                $confirm.click(function() {
                    var $tr = $(row);
                    var isChanged = false;
                    var isAdd = false;
                     var isPass=true;
                    if($tr.validationEngine('validate')==false){
                    	return;
                    }
                    if(!rowData) {
                        rowData = {};
                        isAdd = true;
                    }
                    $tr.find("td").each(function(k1, v1) {
                        if(!$(v1).hasClass("lt-opt")) {
                           	var te = $(v1).find("[name]:not([name='file'])");
                           	var tv;
                            if(te.length>0){
                                 if(te.is("div")){
                                 	tv=te.data("control").getValue();
                                 }else{
                             		tv=te.val();
                                 }
                            }else{
                            	return true;
                            }
                            var tk=te.attr("name");
                            if(te.data("value")&&tk.lastIndexOf("Id")>-1){
                            	rowData[tk.substr(0,tk.lastIndexOf("Id"))]=te.data("value");
                            }
                            if(rowData[tk] != tv) {
                                rowData[tk] = tv;
                                isChanged = true;
                            } else if(!rowData[tk]) {
                                rowData[tk] = tv;
                                isChanged = true;
                            }
                        }
                    });
                    if(!isPass){
                    	return;	
                    }
                    rowData.canEdit = true;
                    rowData.canDelete = true;
                    self._refreshRow($tr, rowData);
                    //如果是新增数据，则将数据绑定在行之上，保存是直接取出
                    if(isAdd) {
                        $(row).data("val", rowData);
                        return;
                    }
                    //如果是修改，则将修改的数据保存到待提交的保存列表中，提交时取出
                    if(isChanged) {
                        self.pushRowData(rowData);
                    }
                })
                $temp_opt.append($confirm);
                $temp_opt.append($cancel);
                return $temp_opt;

            },
            /**
             *
             * @param {Object} headElement 根据头部配置获取列元素
             * @param {Object} rowdata 行数据
             */
            _getNewCol: function(headElement, rowdata, showEdit) {
            	var self=this;
            	if(headElement.colType==2&&self.options.isEdit==true){
                	return;
                }
                if(headElement.isShowInList == 1||(self.options.isEdit==true&&headElement.isEdit==1)) {
                    var $col = $("<td style='word-wrap:break-word;word-break:break-all;'></td>");
                    if(rowdata) {
                        var $val = rowdata[headElement.colCode];
                        if(this.options.dataFilter) {
                        	$val = this._defaultDataFilter(headElement, $val, $col,rowdata);
                            if($.isFunction(this.options.dataFilter)){
                                $val = this.options.dataFilter(headElement, $val, $col,rowdata);
                            }else if(this.options.dataFilter in window){
                                $val = window[this.options.dataFilter](headElement, $val, $col,rowdata);
                            }
                        } else {
                            $val = this._defaultDataFilter(headElement, $val, $col,rowdata);
                        }
                        if(showEdit) {
                        	var inputel=this._loadInputForm(headElement, rowdata[headElement.colCode]);
                        	if(inputel)
                           		$col.html(inputel);
                            else{
                            	return;
                            }
                        } else {
                        	//当为系统输入并且不是编辑状态时显示值
                            if(headElement.colType!=2||self.options.isEdit==false) {
                            	if($val)
                                	$col.html($val);
                            }else{
                            	return;
                            }

                        }

                    } else {
                    	var inputel=this._loadInputForm(headElement);
                    	if(inputel)
                       		$col.html(inputel);
                        else{
                        	return;
                        }
                    }
                }
                return $col;
            },
            //根据表格头的定义加载相应的输入元素
            _loadInputForm: function(headElement, val) {
                var $in = undefined;
                var valData={};
                if(headElement.colType==2){
                	return;
                }
                if(headElement.colValType == 's') {
                    $in = $("<input type='text' name='" + headElement.colCode + "' />");
                } else if(headElement.colValType == 'b') {
                    $in = $("<select  name='" + headElement.colCode + "' />");
                    var yes = $("<option value='0'>否</option>");
                    var no = $("<option value='1'>是</option>");
                    if(val == 1) {
                        yes.attr("checked", "checked");
                    }
                    $in.append(yes);
                    $in.append(no);
                } else if(headElement.colValType == 't') {
                    if("undefined" != typeof WdatePicker) {
                        $in = $("<input type='text' readonly='readonly' name='" + headElement.colCode + "' onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\"/>");

                    } else {
                        $in = $("<input type='datetime-local' name='" + headElement.colCode + "' />");
                    }

                } else if(headElement.colValType == 'd') {
                    $in = $("<input type='text' name='" + headElement.colCode + "' />");

                }else if(headElement.colValType == 'f'){
                    var $input = $("<div name='" + headElement.colCode + "' class='ibox-content'></div>");
                    if(valData) {
                        var upload = undefined;
                        if(headElement.options) {
                            upload = new Upload($input, {
                                value: valData[headElement.colCode],
                                fileNumLimit: headElement.options
                            });
                        } else {
                            upload = new Upload($input, {
                                value: valData[headElement.colCode]
                            });
                        }
                        $input.data("control", upload);
                    } else {
                        var upload = new Upload($input, {
                            value: null
                        });
                        $input.data("control", upload);
                    }
                    $in=$input;

                } else if(headElement.colValType == 'fv') {
                    $in = $("<select name='" + headElement.colCode + "'/></select>");
                    $in.append("<option value>--请选择--</option>");
                    for(var i in headElement.DD) {
                        var op = $("<option ></option>");
                        op.attr("value", headElement.DD[i].id);
                        op.text(headElement.DD[i].name);
                        if(headElement.DD[i].id)
                            $in.append(op);
                    }
                } else if(headElement.colValType == 'ref') {
                    $in = $("<input type='text' readonly='readonly' name='" + headElement.colCode + "' />");
                    if(val) {
                        $in.val(val.name);
                    }
                    var self = this;
                    $in.click(function() {
                        $.FormControl.defaultRefHander($in, headElement);
                    })
                } else {
                    $in = $("<input type='text' name='" + headElement.colCode + "' />")
                }
                if(val) {
                    $in.val(val);
                }
                $in.addClass("validate"+headElement.validate);
                if(headElement.colType == 2) {
                    $in.attr("readonly", true);
                }
                return $in;
            },
            _getPageBar: function() {
                var self = this;
                if(this.options.pageSetting && this.options.pageSetting.isShow) {
                    var maxPage = 6;
                    var $pageBar = $("<ul></ul>");
                    if(this.options.pageSetting.classes) {
                        $pageBar.addClass(this.options.pageSetting.classes);
                    }

                    this.dataModel.pageTotal = parseInt(this.dataModel.totalSize / this.dataModel.perPageSize) + (this.dataModel.totalSize % this.dataModel.perPageSize > 0 ? 1 : 0);
                    if(this.dataModel.pageTotal > maxPage && this.dataModel.currentPage > maxPage) {
                        var $pbLi = $("<li></li>");
                        $pbLi.html("<a><<</a>");
                        $pbLi.data("cp", 1);
                        $pageBar.append($pbLi);
                    }
                    var start = this.dataModel.currentPage - maxPage / 2
                    for(count = start <= 0 ? 1 : start; count <= this.dataModel.pageTotal; count++) {
                        var tli = $("<li></li>");
                        tli.html("<a>" + count + "</a>");
                        tli.data("cp", count);
                        if(this.dataModel.currentPage == count) {
                            tli.addClass("active");
                        }
                        $pageBar.append(tli);
                        if(count > this.dataModel.currentPage + maxPage)
                            break;
                    }
                    if(this.dataModel.currentPage < this.dataModel.pageTotal - maxPage) {
                        var tli = $("<li></li>");
                        tli.html("<a>>></a>");
                        tli.data("cp", this.dataModel.pageTotal);
                        $pageBar.append(tli);
                    }
                    if(this.options.pageSetting.lableClasses) {
                        $pageBar.find("li").addClass(this.options.pageSetting.lableClasses);
                    }
                    $pageBar.find("li").click(function() {
                        self.dataModel.attr("currentPage", $(this).data("cp"));
                        console.log("currentPage" + self.dataModel.attr("currentPage"))
                    });
                    $pageBar.append("<li class=\"paginate_button\">  &nbsp;<a>总数:"+this.dataModel.totalSize+"</li>");
                    return $pageBar;
                }
            },
            _loadDataByAjax: function(pn) {
                var self = this;
                var list = self.options.isAll ? "all" : "list";
                if(pn){
                    self.options.currentPage = pn;
                }else{
                    pn=self.options.currentPage;
                }
                this.options.model[list](pn, this.options.query, function(data) {
                    self.options.list = data.list;
                    if(data.totalSize > data.list.length) {
                        self.options.pageSetting.isShow = true
                    }
                    self.dataModel = new can.Model(data);
                    self.dataModel.bind("currentPage", function(ev, data) {
                        self._loadDataByAjax(data);
                    });
                    var table = self._getTable(self.options.head.cols, self.options.list);
                    if(self.options.head.width > (parseInt($(document.body).width())-parseInt($(document.body).width())*0.12)) {
                        table.css("width", self.options.head.width + "px");
                    }
                    self.element.html(table);
                    table.after(self._getPageBar());
                    if(window[self.options.finished]){
                    	window[self.options.finished](self);
                    }else if($.isFunction(self.options.finished)){
                    	self.options.finished(self);
                    }
                    self.element.trigger("finish");
                });
            },
            _defaultDataFilter: function(head, val, el,obj) {
                if(val != undefined) {
                    if(head.colValType == "s") {
                        if(!val||val==""){
                            return "--"
                        }else{
                            return val;
                        }
                    } else if(head.colValType == "b") {
                        if(val == 2) {
                            return "待审核";
                        }
                        return val == 1 ? "是" : "否";
                    } else if(head.colValType == "f") {
                       // el.html("<img src='" + val + "' width='20' height='20'/>");
                        el.html("<a  href='" + val + "' width='20' height='20' title='" + val + "'/>" + val + "</a>");
                        return undefined;
                    } else if(head.colValType == 'fv') {
                        if(head.DV){
                            return head.DV[val];
                        }else{
                            return "--"
                        }
                    } else if(head.colValType == 'ref') {
                        if(val.name) {
                            return val.name;
                        } else if(head.colCode.lastIndexOf("Id")>0&&obj[head.colCode.substring(0,head.colCode.lastIndexOf("Id"))]){
                            return obj[head.colCode.substring(0,head.colCode.lastIndexOf("Id"))].name;
                        }else if(obj.identity){
                            return obj.identity.name;
                        }else{
                            return val;
                        }
                    }else if(head.colValType == 'refl'){
                    	
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
                    } else if(head.colValType == 'i'){
                        if(!val||val==""){
                            return "0";
                        }else{
                            return val;
                        }
                    }else if(head.colValType == 'd'){
                        if(!val||val==""){
                            return "0";
                        }else{
                            return parseFloat(val).toFixed(2);
                        }
                    }else{

                        return val;
                    }
                } else {
                    return "--";
                }
            },
        });
        $.TableControl = Control;
        return Control;
    });