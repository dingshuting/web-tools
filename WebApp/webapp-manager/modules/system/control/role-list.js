/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
	'can', '../models/role.js', 'js/modal-show.js', '../ejs/role-list.ejs', '../ejs/role-add-modify.ejs', 'plugins/zTree/js/jquery.ztree.core.js',
	'plugins/zTree/css/zTreeStyle/zTreeStyle.css','plugins/jquery-validation/jv.js',
	function(can, Role, Modal, roleListEjs, roleAddAndModifyEjs) {
		steal('plugins/zTree/js/jquery.ztree.exedit.js');
		steal('plugins/zTree/js/jquery.ztree.excheck.js');
		var roleControl = can.Control(
			/** @Prototype */
			{
				org_tree: undefined,
				linstner: undefined,
				//获取当前角色功能的ztree对象
				role_func_tree: undefined,
				//是否开启编辑功能
				is_edit: true,
				//是否开启点击查看角色详情功能
				is_open_detail: true,
				//添加角色的父节点,在添加tree的时候赋值
				parentRole: undefined,
				//当前的角色，点击树形菜单是赋值
				currentRole: undefined,
				//是否开启多重选择，此用于选择部门或者角色
				is_multiple:false,
				/**
				 * Create a recipe list, render it, and make a request for finding all recipes.
				 */
				init: function(el, parp) {
					var obj = this;
					this.initPara(parp);
					this.linstner = [{
						el: "form .submit_button click",
						func: this.roleSave
					}, {
						el: ".add_dep click",
						func: this.openSaveD
					}];

					obj.element.html(roleListEjs());
					if(obj.is_edit) {
						obj.element.find(".add_dep").show();
					}

					$.fn.zTree.init($("#org_tree"), obj.getOrgSetting(), null);

					obj.org_tree = $.fn.zTree.getZTreeObj("org_tree");
					obj.initEvent(obj);
					//加载zTree数据
				},
				//初始化配置参数
				initPara: function(para) {
					if(para.is_edit) {
						this.is_edit = true;
					}else{
						this.is_edit = false;
					}
					if(para.is_open_detail) {
						this.is_open_detail = true;
					}else{
						this.is_open_detail = false;
					}
					if(para.is_multiple){
						this.is_multiple = true;
					}else{
						this.is_multiple = false;
					}
				},
				//获取功能树的配置，主要是CheckBox的相关配置
				getFuncsSetting: function() {
					var obj = this;
					var setting = {
						check: {
							enable: true,
							chkStyle: "checkbox",
							chkboxType: {
								"Y": "ps",
								"N": "ps"
							}
						},
						callback: {
							//当点击后将对当前选中角色的func进行相关的变更操作，未选中则从funcs中移除，否则则添加进去
							onCheck: function(event, treeId, treeNode) {
								if(obj.currentRole) {
									for(var i in obj.currentRole.funcs) {
										if(obj.currentRole.funcs[i].id == treeNode.id) {
											if(!treeNode.check) {
												obj.currentRole.funcs[i] = {};
												return;
											}
										}
									}
									if(treeNode.check) {
										obj.currentRole.funcs.push(treeNode);
									}
								}

							}
						}
					}
					return setting;
				},
				/**
				 * 获取组织架构的ztree的配置
				 */
				getOrgSetting: function() {
					var obj = this;
					var setting = {
						check: {
							enable: false
						},
						async: {
							enable: true,
							type: "get",
							url: $.contextPath + "/org/rlist",
							autoParam: ["id"],
							//对数据进行筛选处理，标记其是否包含子项
							dataFilter: function(treeId, parentNode, responseData) {
								for(var i = 0; i < responseData.length; i++) {
									responseData[i].open=true;
									if(!responseData[i].funcs){
										responseData[i].nocheck=true;
									}
									responseData[i].pId = responseData[i].parentId;
									for(var j in responseData[i].children) {
										responseData[i].children[j].pId = responseData[i].children[j].parentId;
										responseData[i].children[j].isParent = true;
									}
									responseData[i].isParent = true;
								}
								return responseData;
							}
						},
						callback: {
							//点击方法，当点击角色后将自动填充其详细信息，含相关的功能列表
							onClick: function(event, treeId, treeNode) {
								var rinfo = $(obj.element).find("#showRoleInfo");
								obj.currentRole=treeNode;
								if(treeNode.funcs) {
									obj.showRoleFunc(obj, treeNode);
								} else {
									rinfo.html("<h2>请选择部门下的角色查看具体信息！</h2>");
								}

							}
						}

					};
					if(obj.is_edit&&!obj.is_multiple) {
						setting.view = {
							addHoverDom: function(treeId, treeNode) {
								var sObj = $("#" + treeNode.tId + "_span");
								if(treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
								var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
									"' title='add node' onfocus='this.blur();'></span>";
								sObj.after(addStr);
								var btn = $("#addBtn_" + treeNode.tId);
								if(btn) btn.bind("click", function() {
									obj.org_tree.selectNode(treeNode, false, false);
									obj.parentRole = treeNode;
									obj.currentRole=undefined;
									obj.openSaveR(obj);
									return false;
								});
							},
							removeHoverDom: function(treeId, treeNode) {
								$("#addBtn_" + treeNode.tId).unbind().remove();
							},
							selectedMulti: false
						};
						setting.edit = {
							enable: true,
							showRemoveBtn: function(treeId, treeNode) {
								return treeNode.children?treeNode.children.length < 1:true;
							},
							showRenameBtn: false
						};
						setting.callback.onRemove = function(event, treeId, treeNode) {
							if(treeNode.funcs){
								Role.remover(treeNode.id,function(data){
									if(data.code=='200'){
										alert('刪除完成')
									}else{
										alert(data.desc);
									}
								});
							}else{
								Role.removed(treeNode.id);
								alert("删除部门成功");
							}
							
						};
					}
					if(obj.is_multiple){
						setting.check= {
							enable:  true
						}
					}
					return setting;
				},
				//关闭角色详情页面
				closeRoleFunc:function(obj){
					var rinfo = $(obj.element).find("#showRoleInfo");
					rinfo.html("<h2>请选择相应的角色信息</h2>");
				},
				//显示角色功能的详情
				showRoleFunc: function(obj, role) {
					if(!obj.is_open_detail) {
						return;
					}
					var rinfo = $(obj.element).find("#showRoleInfo");
					obj.crrentRole = role;
					rinfo.html(roleAddAndModifyEjs(role));
					if(role) {
						$.fn.zTree.init($("#role_func_tree"), obj.getFuncsSetting(), obj.filterFunc(obj, $.session.currentUser.funcs, role.funcs));
					} else {
						$.fn.zTree.init($("#role_func_tree"), obj.getFuncsSetting(), obj.filterFunc(obj, $.session.currentUser.funcs, []));
					}
					obj.role_func_tree = $.fn.zTree.getZTreeObj("role_func_tree");
					obj.initEvent(obj);
					if(obj.is_edit) {
						rinfo.find(".submit_button").show();
					}
					obj.initValidate(obj.element);
				},
				/**
				 * 填充功能项并判断给定的菜单项是否选中
				 * obj- control 的实体对象
				 * data- func 的功能列表及当前用户已经拥有的function功能列表
				 * hadFunc- 角色的功能列表用于判断角色的功能回显
				 */
				filterFunc: function(obj, data, hadFuncs) {
					var tdata = new Array;
					a:
						for(i in data) {
							for(j in hadFuncs) {
								if(hadFuncs[j].id == data[i].id) {
									tdata.push({
										id: data[i].id,
										name: data[i].name,
										pId: data[i].parentId,
										isParent:true,
										children: obj.filterFunc(obj, data[i].children, hadFuncs),
										checked: true
									});
									continue a;
								}
							}
							tdata.push({
								id: data[i].id,
								name: data[i].name,
								isParent:true,
								children: obj.filterFunc(obj, data[i].children, hadFuncs),
								checked: false
							});

						}
					return tdata;
				},

				roleSave: function() {
					var obj = $(this).data("obj");
					if(!$(obj.element).find("form").validationEngine("validate")){
						return;
					}
					if(obj.role_func_tree.getCheckedNodes(true).length<1){
						alert('功能没选')
						return;
					}
					if(obj.currentRole){
						obj.currentRole.name=$(obj.element).find("form input[name='name']").val();
						obj.currentRole.remark=$(obj.element).find("form textarea[name='remark']").val();
					}else{
						obj.currentRole = $(obj.element).find("form").formParams();
						obj.currentRole.parentId=obj.parentRole.id;
					}
					var nodes = obj.role_func_tree.getCheckedNodes(true);
					var funcs = new Array();
					for(var i in nodes) {
						funcs.push({
							"id": nodes[i].id
						});
					}
					obj.currentRole.funcs = funcs;
					Role.saver(obj.currentRole,function(data){
						if(data.code=="200"){
							if(obj.currentRole.id==null||!obj.currentRole.id){
								data.data.pId=data.data.parentId;
								data.data.children=[];
								obj.org_tree.addNodes(obj.parentRole,data.data);
								obj.org_tree.selectNode(data.data);
								obj.currentRole=data.data;
								alert('角色新增成功')
							}else{
								obj.org_tree.updateNode(obj.currentRole);
								alert('角色修改成功')
							}
						}else{
							alert(data.desc);
						}
					});
				},
				//打开保存角色的窗口
				openSaveR: function(obj) {
					if(!obj) {
						obj = $(this).data("obj");
					}
					obj.showRoleFunc(obj);
				},
				openSaveD: function() {
					var obj = $(this).data("obj");
					var dname = prompt("请输入部门名称");
					if(dname && dname.length >= 3) {
						Role.saved({
							name: dname
						}, function(data) {
							obj.org_tree.addNodes(null, {
								id: data.data.id,
								name: data.data.name,
								children:[]
							}, false);
						});
					} else if(typeof(dname) == "string") {
						alert("名字太短了");
					}else{
						
					}
					return false;
				},
				//可外部调用，获取选择的部门或角色对象
				selected:function(){
					return this.org_tree.getCheckedNodes();
				},
				initValidate: function(el, option) {
					var rules = [{
						"name": "name",
						"validate": "validate[required,maxSize[20]]"
					}]
					var $form = $(el).find("form");
					for(var i in rules) {
						$form.find("[name=" + rules[i].name + "]").addClass(rules[i].validate);
					}
					$($form).validationEngine();
				},
				initEvent: function(obj) {
					for(var i = 0; i < obj.linstner.length; i++) {
						var el = obj.linstner[i].el;
						var event = el.substr(el.lastIndexOf(" ") + 1);
						var selecter = el.substr(0, el.lastIndexOf(" "));
						$(obj.element).find(selecter).data("obj", obj);
						$(obj.element).find(selecter).unbind(event, this.linstner[i].func);
						$(obj.element).find(selecter).bind(event, this.linstner[i].func);
					}
				}
			});
		return roleControl;
	});