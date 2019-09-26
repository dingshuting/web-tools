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
             * 当前初始化表格的容器
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
             * //表格是否可以进行其内容的编辑，true时点击行及弹出内容编辑框
             * options.isEdit=true|false

             * //ol设置，主要用于对象列表中的对象直接在编辑模式中进行数据库写操作，并关联主对象
             * options.parentObj={id:'必带字段'}//在表格处于编辑状态时，对应的父对象数据，此项一般都是由表单中的ol类型触发，该参数代表object，l代表表格本身的列表数据
             * options.olSetting={
             * 	childrenField:'name',//对应列表对象关联的主对应字段
             * }
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
            /**
             * 刷新当前表格内容，刷新时将重新发起请求来获取最新的数据，同时刷新时的条件也为当前表格的查询条件。
             * 刷新后自动重新加载表格。
             */
            refresh: function() {
                this._loadDataByAjax(self.options.currentPage);
            },
            //初始化页面视图，model不为空时则从后台获取数据，否则数据和列头需要传入否则表格将无法显示
            init_view: function() {
                self = this;
                if(this.options.model) {
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
                        	if(self.options.list){
	                            var table = self._getTable(self.options.head.cols, self.options.list);
	                            if(self.options.head.width >(parseInt($(document.body).width())-parseInt($(document.body).width())*0.12)) {
	                            	table.css("width", self.options.head.width + "px");
	                            }
	                            self.element.html(table);
                           }else{
                           		console.error("options.list must be specified when head was existed");
                           }
                        });
                    }else{
                    	console.error("options.head must be specified,the head is ExtraData");
                    }
                }
                if(window["lodingCols"]) {
                    window["lodingCols"](self);
                }
            },
            /**
             * 根据cols配置的length属性，来计算宽度百分比（当前列定义长度/总长度）
             */
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
            /**
             * 初始化配置参数，其中初始化的参数有：
             * dataAdded：当前新增的数据项
             * dataRemoved：当前删除的数据项
             * dataChanged：当前修改后的数据项
             * dataChecked：当前选中的数据项
             */
            init_args: function() {
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
				this.dataList=this.options.list;
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
            //默认提供的操作项，在可编辑的情况下使用
            defaultActions: [],
            /**
             * 根据传入的list配置参数获取table的head
             * @param {Object} head
             */
            _getThead: function(head,table) {
                $head = $("<tr></tr>");
                var self = this;
                if(this.options.isShowIndex) {
                    $head.append("<th style='text-align:center;width:"+(70/table.width()*100)+"%'>序号</th>");
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
                    if(isShowInList == 1) {
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
                if(this.options.isShowActs&&this.options.isEdit==false) {
                    $head.append("<th style='text-align:center;width:70px'>操作</th>");
                }
                return $head;

            },

            //添加新数据的操作，调用自动提示出相关的输入项，保存后数据存入js缓存后续一并提交数据
            addNewData: function(saveFunc) {
            	var self=this;
            	var formContainer=$.Modal.getRightModal();
        		var fcontrol=new $.FormControl(formContainer,{input:self.options.head,groupNum:1});
        		formContainer.prepend("<h3>信息添加</h3>")
        		formContainer.append('<div class="col-lg-12 text-center" style="padding:5px"><button type="button" class="btn btn-primary  btn-save">确认</button><button type="button" class="btn btn-warning btn-close">关闭</button></div>');
        		formContainer.find(".btn-close").click(function(){
        			formContainer.animate({right:formContainer.width()*-1.1},500);
        		});
        		var addNewElRow=function(result){
        			var newRow=self._getNewRow(self.options.head.cols,result);
					if(self.options.isShowIndex){
						newRow.prepend("<td>"+self.element.find("table tr").length+"</td>")
					}
					self.element.find("table").append(newRow);
        		}
        		formContainer.find(".btn-save").click(function(){
        			var result=fcontrol.getValue();
        			//当有父对象时，则直接进行子对象新增，否则则直接添加到列表中，在父对象保存时统一保存
        			if(result){
	        			if(self.options.parentObj){
		        			result[self.options.olSetting.childrenField]=self.options.parentObj.id;
		        			if(saveFunc){
		        				saveFunc.call(formContainer,result);
		        				return;
		        			}else{
		        				var Model = $.Model(self.options.head);
			        			formContainer.showLoading();
			        			Model.save(result,function(data){
		        				formContainer.hideLoading();
			        				if(data.code == "200") {
			        					formContainer.find(".btn-close").trigger("click");
			        					$.Modal.alert("保存成功");
			        					addNewElRow(result);
			        				}
		        				});
		        			}
	        			}else{
	        				formContainer.find(".btn-close").trigger("click");
	        				addNewElRow(result);
	    					self.dataList.push(result);
	        			}
	        			
        			}
        		});
            },
            /**
             * 获取当前表格插件的数据对象列表，其有2种情况：
             * 1、当options.checked.display为true的时候代表当前插件为选择行的状态，返回选中的行数据对象
             * 2、正常状态下返回表格的整个数据对象列表，主要用于列表数据的新增
             */
            getValue: function() {
                if(this.options.checked.display) {
                    return this.dataChecked;
                }
                return this.dataList;
            },
            /**
             * table插件进行查询操作，查询后将会自动刷新table的内容
             * @param {Object} query
             */
            setQuery: function(query) {
                this.options.query = query;
                this._loadDataByAjax(1);
            },
            //重新刷新行内容
            refreshRow: function(row, rowData) {
            	var firstTd;
            	if(self.options.isShowIndex==true){
            		firstTd=row.find("td:first").clone();
            	}
                this._getNewRow(self.options.head.cols, rowData, row);
                if(firstTd)
                	row.find("td:first").before(firstTd);
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
            /**
             * 根据给定的列说明列表，并生成一个指定数据列表的表格实例
             * @param {Array} headCols 数据描述列参考数据库extra_data_cols字段
             * @param {Array} dataList 数据实例列表
             */
            _getTable: function(headCols, dataList) {
            	var self=this;
                var $table = $("<table></table>");
                //当列宽度大于内容区域则开启横向滚动条
                if(self.options.head.width > $(window).width()*0.88) {
                    $table.css("width", self.options.head.width + "px");
                }else{
                	$table.css("width",self.element.parent().width()-20);
                }
                self = this;
                if(self.options.listClasses){
                	$table.addClass(self.options.listClasses);
                }else{
                	$table.addClass(TPL_TABLE_OPTION.listClasses);
                }
                $table.append(self._getThead(headCols,$table));
                for(var i in dataList) {
                    var row = this._getNewRow(headCols, dataList[i]);
                    if(self.options.isShowIndex) {
                        if(self.options.pageSetting.isShow) {
                            i = (self.dataModel.currentPage - 1) * self.dataModel.perPageSize + parseInt(i) ;
                        }
                        //如果当前为选择插件，那么序号将由单选框代替
                        row.find("td:first").before("<td style='text-align:center'>" + (i*1+1) + "</td>");
                    }
                    if(this.options.checked.display) {
                        var $checkbox;
                        if(this.options.checked.isSingle) {
                            $checkbox = $("<input type='radio' name='choseData' />");
                            $checkbox.click(function() {
                                self.dataChecked[0] = $(this).data('val');
                            });
                        } else {
                            $checkbox = $("<input type='checkbox' name='choseData' />");
                            for(var j in self.dataChecked) {
                            	if(dataList[i].id==self.dataChecked[j].id){
                            		$checkbox.prop("checked","checked");
                            	}
                            }
                            $checkbox.change(function() {
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
                }
               
                self.table = $table;
                return self.table;
            },
            /**
             * 根据指定的头信息，生成一条新行数据，当options.isShowActs为true时则显示操作项，
             * 该操作项来源于数据库功能配置，并非人为指定，需要受到权限控制
             * @param {Array} headCols 头的定义参考数据表Extra_data_cols
             * @param {Object} rowData 行对象信息，对应数据库的单条对象
             * @param {jQuery} oldRow 在旧行的基础上进行渲染行,用于表格行的更新
             */
            _getNewRow: function(headCols, rowData, oldRow) {
                var $row = oldRow ? oldRow : $("<tr></tr>");
                $row.empty();
                var self = this;
                $row.data("val",rowData);
                $(headCols).each(function() {
                	var newCol=self._getNewCol(this, rowData, false)
                	if(newCol)
                    	$row.append(newCol);
                });
                if(rowData) {
                	//当前表格设置为可编辑状态时，isShowActs将不再生效
                	if(self.options.isEdit) {
                    	$row.addClass("rowAct");
                    	$row.on("click",self._showEditDialog);
                    }else if(this.options.isShowActs) {
                        var org_action = this._loadActions(rowData);
                        $row.append(org_action);
                    }
                    rowData.el = $row;
                } else {
                	//表格本身将不做新增操作，由右侧表单来完成
                	return $row;
                }
                return $row;
            },
            /**
             *
             * @param {Object} headElement 根据头部配置获取列元素
             * @param {Object} rowdata 行数据
             */
            _getNewCol: function(headElement, rowdata) {
            	var self=this;
            	if(headElement.colType==2&&self.options.isEdit==true){
                	return;
                }
                if(headElement.isShowInList == 1) {
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
                    	//当为系统输入并且不是编辑状态时显示值
                        if(headElement.colType!=2||self.options.isEdit==false) {
                        	if($val)
                            	$col.html($val);
                        }else{
                        	return;
                        }
                    }
                }
                return $col;
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
            /**
             * 异步加载表格内容
             * @param {Object} pn 分页数据
             */
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
            /**
             * 带有“rowAct”类样式的则为可操作的行，点击显示右侧模态框，用于编辑当前点击行数据信息。
             * 点击后将通过FormControl生成编辑改数据的form，同时可以对改数据进行修改和删除动作。
             * 删除时自动提交到后台的删除
             */
            ".rowAct click":function(el){
            	var self=this;
            	var $row=el;
        		$row.parent().find("tr").removeClass("active");
        		$row.addClass("active")
        		var formContainer=$.Modal.getRightModal();
        		var fcontrol=new $.FormControl(formContainer,{input:self.options.head,data:$row.data('val'),groupNum:1});
        		formContainer.prepend("<h3>信息编辑</h3>")
        		formContainer.append('<div class="col-lg-12 text-center" style="padding:5px"><button type="button" class="btn btn-primary  btn-save">确认</button><button type="button" class="btn btn-danger  btn-remove">删除</button><button type="button" class="btn btn-warning btn-close">关闭</button></div>');
        		formContainer.find(".btn-close").click(function(){
        			formContainer.animate({right:formContainer.width()*-1.1},500);
        		});
        		formContainer.find(".btn-save").click(function(){
        			var Model = $.Model(self.options.head);
        			var result=fcontrol.getValue();
        			self.dataList[$row[0].rowIndex]=result;
        			if(result.id){
        				formContainer.showLoading();
	        			Model.save(result,function(data){
	        				formContainer.hideLoading();
	        				if(data.code == "200") {
	        					formContainer.find(".btn-close").trigger("click");
	        					self.refreshRow($row,result);
	        					$.Modal.alert("保存成功");
	        				}
	        			});
        			}
        		});
        		formContainer.find(".btn-remove").click(function(){
        			var Model = $.Model($.session.currFun.extraData);
        			var rowData=$row.data('val');
        			self.dataList.slice($row[0].rowIndex,1);
        			if(rowData.id){
	        			Model.delete(rowData.id,function(data){
	        				if(data.code=="200"){
	        					$row.hide(500);
	        				}
	        			});
        			}else{
        				$row.hide(500);
        			}
        			
        		});
            }
        });
        $.TableControl = Control;
        return Control;
    });