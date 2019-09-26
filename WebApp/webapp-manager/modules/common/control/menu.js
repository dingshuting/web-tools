steal(
	'can',
	'templates/' + $.templateName + '/ejs/menu.ejs',
	'modules/common/model/system.js',
	function(can, menuEJS, System) {
		/**
		 * @constructor cookbook/recipe/list
		 * @alias RecipeList
		 * @parent cookbook
		 * @inherits can.Control
		 * Lists recipes and lets you destroy them.
		 */
		var Control = can.Control(
			/** @Static */
			{
				//加载子项操作按钮
				loadFuncBtns: function() {
					$("#"+$.session.currFun.id+" .func-buttons").html("");
					$("#"+$.session.currFun.id+" .func-buttons").append($.MenuControl.funcButtons());
					$("#"+$.session.currFun.id+" .func-title").html($.Menu.currFun.name);
					$("#"+$.session.currFun.id+" .func-child-title").html($.session.currFun.name);
				},
				//获取操作按钮的元素
				funcButtons: function() {
					var self = this;
					if($.session.currFun && $.session.currFun.children.length > 0) {
						var $btnAction = $("<div class='title-action'></div>");
						for(var i in $.session.currFun.children) {
							if($.session.currFun.children[i].displayType == 2) {
								$btn = $("<button type='button'></button>");
								$btn.html($.session.currFun.children[i].name);
								$btn.data("func", $.session.currFun.children[i]);
								$btn.attr("id","fn-"+$.session.currFun.children[i].id);
								$btn.addClass($.session.currFun.children[i].icon);
								$btn.bind("click", function(){
									var currFun=$(this).data("func");
									var btn=this;
									if(currFun.extraDataId&&$.session.currFun.extraDataId!=currFun.extraDataId){
										$.Model().getHead(currFun.extraDataId, function(data) {
											currFun.extraData=data;
											$(btn).data("func",currFun);
											self.togoActions[currFun.togo].call(btn);
										});
									}else{
										self.togoActions[currFun.togo].call(btn);
									}
									
								});
								$btnAction.append($btn);
							}
						}
						return $btnAction;
					}
				},
				funcOptions: function() {
					var $btnAction = new Array();
					if($.session.currFun && $.session.currFun.children.length > 0) {
						for(var i in $.session.currFun.children) {
							if($.session.currFun.children[i].displayType == 3) {
								var togo = this.togoActions[$.session.currFun.children[i].togo];
								if(togo) {
									togo.name = $.session.currFun.children[i].name;
									if(!togo.args) {
										togo.args = {};
									}
									togo.args["func"] = $.session.currFun.children[i];
									$btnAction.push(togo);
								}

							}
						}
						
					}
					return $btnAction;
				},
				addBackBtn: function(func) {
					var hasBack = false;
					//添加按钮
					if(!func.children) {
						func.children = new Array();
					}
					for(var i in func.children) {
						if(func.children[i].id == "back") {
							hasBack = true;
							break;
						}
					}
					if(!hasBack) {
						if(func.children)
							func.children.push({
								id: "back",
								name: "返回",
								togo: "goBackToList",
								displayType: 2,
								icon: "btn btn-danger"
							});
					}
				},
				//所有操作按钮的事件集合
				togoActions: {
                    toUserAdd: function() {
                        var Model = $.Model($.session.currFun.extraData);
                        $.session.currFun = $(this).data("func");
                        $.MenuControl.addBackBtn($.session.currFun);
                        $.MenuControl.loadFuncBtns();

						steal("modules/common/model/role.js",'modules/system/control/user-add.js',function (Role,userAddControl) {
                            Role.getlists(function(deps){
                                var data = {};
                                data.deps = deps;
                                /*new userAddControl("#content-body",data);*/
                                $.VF.build(userAddControl, undefined, {
                                    model: Model,
                                    data: $.extend(true, data, $.session.pageData)
                                }, false);
                            },0);
                        })
                    },
                    toUserUpdate: {
                        func: function() {
                            var val = $(this).data("val");
                            $.session.currFun = $(this).data("func");
                            /*form = $.VF.build($.FormControl, undefined, {
                                "model": $.Model($.Menu.currFun.extraData),
                                "id": val.id
                            }, false);*/
                            steal("modules/common/model/role.js","modules/common/model/user.js",'modules/system/control/user-modify.js',function (Role,User,userModifyControl) {
                                User.findUserInfo(val.id,function(data){
                                    Role.getlists(function(deps){
                                        data.deps = deps;
                                        /*new userAddControl("#content-body",data);*/
                                        $.VF.build(userModifyControl, undefined, {
                                            model: $.Model($.Menu.currFun.extraData),
                                            data: $.extend(true, data, $.session.pageData)
                                        }, false);
                                    });
                                });
                            })
                            $.MenuControl.loadFuncBtns();
                        },
                        beforActionLoad: function(rowdata, row) {

                            return true;
                        }
                    },
                    showUserDetail: {
                        func: function() {
                            var val = $(this).data("val");
                            $.session.currFun = $(this).data("func");
                            steal("modules/common/model/role.js","modules/common/model/user.js",'modules/system/control/user-info.js',
								function (Role,User,UserInfoControl) {
                                User.findUserInfo(val.id,function(data){
									$.VF.build(UserInfoControl, undefined, {
										model: $.Model($.Menu.currFun.extraData),
										data: $.extend(true, data, $.session.pageData)
									}, false);
                                });
                            })
                            $.MenuControl.loadFuncBtns();
                        }
                    },
					toAdd: function() {
						$.session.currFun = $(this).data("func");
						var Model = $.Model($.session.currFun.extraData);
						$.MenuControl.addBackBtn($.session.currFun);
						$.VF.build($.FormControl, undefined, {
							finished:function(){
								$.MenuControl.loadFuncBtns();
							},
							model: Model,
							data: $.extend(true, {}, $.session.pageData)
						}, false);

					},
					saveOrUpdate: function(val,callback) {
						var Model = $.Model($.Menu.currFun.extraData);
						if($.session.currFun.extraData){
							var Model = $.Model($.session.currFun.extraData);
						}
						if(!val)
							val = $.VF.getValue();
						if(val) {
							Modal.showLoading("body", "请稍后...");
							Model.save(val, function(data) {
								Modal.hideLoading("body");
								if(data.code == "200") {
									alert("保存成功");
									if(!callback)
										$.MenuControl.togoActions.goBackToList();
									else{
										callback.call();
									}
								} else {
									alert("保存失败：" + data.desc);
								}

							});
						}
					},
					saveOrUpdateOuther: function() {
						var url = null;
						var func = $.session.currFun.children;
						var Model = $.Model($.Menu.currFun.extraData);
						for(var key in func) {
							if(func[key].name == "保存") {
								url = func[key].url;
							}
						}
						Model.saveOuther($.VF.getValue(), url, function(data) {
							if(data.code == "200") {
								alert("保存成功");
								$.MenuControl.togoActions.goBackToList();
							}
						});
					},
					toUpdate: {
						func: function() {
							var val = $(this).data("val");
							$.session.currFun = $(this).data("func");
							form = $.VF.build($.FormControl, undefined, {
								finished:function(){
									$.MenuControl.loadFuncBtns();
								},
								"model": $.Model($.Menu.currFun.extraData),
								"id": val.id
							}, false);
							$.MenuControl.loadFuncBtns();
						},
						beforActionLoad: function(rowdata, row) {
                            if(rowdata.status == "3") {
                                return false;
                            }
							return true;
						}
					},
					showDetail: {
						func: function() {
							var val = $(this).data("val");
							$.session.currFun = $(this).data("func");
							$.Model($.session.currFun.extraData).get(val.id, function(data1) {
								$.VF.build($.ContentInfoControl, undefined, $.extend(true, {
									colsVal: data1,
									colsTitle: $.session.currFun.extraData
								}, $.session.pageData), false);
								$.MenuControl.loadFuncBtns();
							});
							
						}
					},
					confirmChangeStatus: {
						func: function() {
							var val = $(this).data("val");
							if(confirm("确认该条信息已经处理？")) {
								$.Model($.session.currFun.extraData).cs(val.id, "1",
									function(data1) {
										if(data1== "200") {
											alert("修改成功！");
                                            $.VF.contentControl.table.refresh();
										}else{
                                            alert("修改失败！");
                                            $.VF.contentControl.table.refresh();
										}
									});
							} else {
								// $.Model($.session.currFun.extraData).cs(val.id, "0",
								// 	function(data1) {
								// 		if(data1.code == "200") {
								// 			alert("审核成功");
								// 			$.MenuControl.togoActions.goBackToList();
								// 		}
								// 	});
							}
						},
						beforActionLoad: function(rowdata, row) {
							//判断是否显示审核按钮
							if(rowdata.status == "2") { //审核通过
								return false;
							} else { //未审核、审核不通过
								return true;
							}

						},
					},
					//启用
					enable: {
						func: function() {
							var val = $(this).data("val");
							if(confirm("是否要启用当前数据？")) {
								$.Model($.session.currFun.extraData).cs(val.id, "1",
									function(data) {
										if(data.code == "200") {
											alert("操作成功");
											$.MenuControl.togoActions.goBackToList();
										} else {
											alert("操作失败");
											$.MenuControl.togoActions.goBackToList();
										}
									});
							}
						},
						beforActionLoad: function(rowdata, row) {
							if(rowdata.status == "0") {
								return true;
							} else {
								return false;
							}
						},
					},
					//停用
					disable: {
						func: function() {
							var val = $(this).data("val");
							if(confirm("是否要停用当前数据？")) {
								$.Model($.session.currFun.extraData).cs(val.id, "0",
									function(data) {
										if(data.code == "200") {
											alert("操作成功");
											$.VF.back();
										} else {
											alert("操作失败");
											$.VF.back();
										}
									});
							}
						},
						beforActionLoad: function(rowdata, row) {
							if(rowdata.status == "1") {
								return true;
							} else {
								return false;
							}
						},
					},
					remove: {
						func: function() {
							var val = $(this).data("val");
							if(confirm("确认要删除该信息么？"))
								$.Model($.session.currFun.extraData).delete(val.id, function(data) {
									if(data.code == "200") {
										alert("操作成功");
									} else {
										alert("操作失败");
									}
									$.VF.contentControl.table.refresh();
								});
						},
						beforActionLoad: function(rowdata, row) {

							return true;
						}
					},
					export_jiaojie:function(){
						var obj=$.VF.contentControl.query.getValue()
						obj.startTime=$($.VF.contentControl.query.element).find("[name='start']").val();
						obj.endTime=$($.VF.contentControl.query.element).find("[name='end']").val();
						if(!obj.endTime){
							delete obj.endTime;
						}
						var str = $(this).data("func").url;
                        var index = str .lastIndexOf("\/");
                        var str  = str .substring(index + 1, str .length);
                        console.log(str);
                        //判断入仓交接的导出
                        if (5==str||"4_1"==str){
                            // if(obj&&obj.ecId&&obj.ecId!=""){
                            //     obj=$.objToUrlPara(obj);
                            //     window.open($.request.domain+$(this).data("func").url+"?"+obj);
                            // }else{
                            //     $.Modal.alert("请选择快递公司后再导出");
                            // }

							//判断是不是服务中心人员，如果是，则直接导出当前服务中心数据，否则必须选择服务中心后再导出数据。
							var user = $.session.currentUser;
							if (user.owner != '100000'){
                                obj=$.objToUrlPara(obj);
                                window.open($.request.domain+$(this).data("func").url+"?"+obj);
							} else {
                                if(obj&&obj.scId&&obj.scId!=""){
                                    obj=$.objToUrlPara(obj);
                                    window.open($.request.domain+$(this).data("func").url+"?"+obj);
                                }else{
                                    $.Modal.alert("请选择服务中心后再导出");
                                }
							}
						}else{
                            obj=$.objToUrlPara(obj);
                            window.open($.request.domain+$(this).data("func").url+"?"+obj);
						}
					},
					export_price_mingxi:function(){
						var obj=$.VF.contentControl.query.getValue()
						var startTime=$($.VF.contentControl.query.element).find("[name='start']").val();
                        var endTime=$($.VF.contentControl.query.element).find("[name='end']").val();
						obj.startTime=startTime;
						obj.endTime=endTime;
                        if(obj&&obj.scId&&obj.scId!=""){

                        }else{
                            $.Modal.alert("请选择服务中心后再导出");
                            return;
                        }
                        if(startTime==''||endTime==''){
                            $.Modal.alert("请选择入仓时间");
                            return;
                        }else{
                            var startTime_ms = Date.parse(startTime.replace(/-/g, "/"));
                            var endTime_ms = Date.parse(endTime.replace(/-/g, "/"));
                            var date3 = new Date(endTime_ms).getTime() - new Date(startTime_ms).getTime();
                            //计算出相差天数
                            var days = Math.floor(date3 / (24 * 3600 * 1000))

                            var leave1 = date3 % (24 * 3600 * 1000)
                            var hours = Math.floor(leave1 / (3600 * 1000))

                            var leave2 = leave1 % (3600 * 1000)
                            var minutes = Math.floor(leave2 / (60 * 1000))

                            var leave3 = leave2 % (60 * 1000)      //计算分钟数后剩余的毫秒数
                            var seconds = Math.round(leave3 / 1000)
                             if (days > 31) {//1月以上
                                 $.Modal.alert("时间跨度不可大于31天");
                                 return;
                            }
						}
                        obj=$.objToUrlPara(obj);
                        window.open($.request.domain+$(this).data("func").url+"?"+obj);
					},
					goBackToList: function() {
						$.session.currFun = $.Menu.currFun;
						$.VF.back();
						$.MenuControl.loadFuncBtns();
					}
				}
			},
			/** @Prototype */
			{
				/**
				 * 初始化菜单操作
				 */
				init: function(el,options) {
					var obj = this;
					$.Menu = this;
					System.funcs(function(data) {
						data = JSON.parse(data)
						obj.list = new can.Observe.List(data);
						var html = menuEJS(obj.list);
						obj.element.html(html);
						$(obj.element).hideLoading();
						if(options.finished&&$.isFunction(options.finished)){
							options.finished(obj);
						}
						//$("#page-wrapper").height(document.body.clientHeight)
						
					}, function(xhr) {
						// called if an error
						console.info(xhr);
					});
				},

				"li ul li click": function(li, e) {
					var self = this;
					e.preventDefault();
					if(li.hasClass("active")){
						return;
					}
					var url = $(li).attr("id")
					if(typeof(url) != 'undefined') {
						$.VF.build(url);
						return;
					}

					if(!$.session) {
						$.session = {};
					}
					$.session.currFun = li.data("menu")._data;
					var url = li.data("menu").togo;
					var pageData;
					if(url.indexOf("?") > -1) {
						var obj = $.urlToObject(url);
						$.session.pageData = obj;
						url = url.substr(0, url.indexOf("?"));
					} else {
						$.session.pageData = undefined;
					}
					if($.session.currFun.extraDataId && $.session.currFun.extraDataId != "") {
						$.Model().getHead($.session.currFun.extraDataId, function(data) {
							$.session.currFun.extraData = data;
							//子功能与父功能共享数据模型，不必重复查询，当不一致时再查询覆盖
							for(var i in $.session.currFun.children) {
								$.session.currFun.children[i].extraData = data;
								$.MenuControl.addBackBtn($.session.currFun.children[i]);
							}
							self.currFun = $.session.currFun;
							$.VF.build(url, undefined, $.extend(true, {finished:function(){
								$.MenuControl.loadFuncBtns();
							}}, $.session.pageData));
							
						});
					} else {
						self.currFun = $.session.currFun;
						$.VF.build(url, undefined, $.extend(true, {finished:function(){
								$.MenuControl.loadFuncBtns();
							}}, $.session.pageData));
					}

					$(".nav li ul li").removeClass("active");
					li.addClass("active");
					return false;
				}
			});
		$.MenuControl = Control;
		if(APP_CONFIG.togoActions){
			$.extend(true, $.MenuControl.togoActions, APP_CONFIG.togoActions);
			console.log($.MenuControl.togoActions);
		}
		return Control;
	});