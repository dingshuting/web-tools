steal(
	'can',
	'../ejs/data-dictionary.ejs',
	'../models/system.js',
	'js/viewFactory.js', 'js/modal-show.js',
	'plugins/zTree/js/jquery.ztree.core.js',
	'plugins/zTree/css/zTreeStyle/zTreeStyle.css',
	function(can, DDEjs, System, ViewFactory, Modal) {
		steal('plugins/zTree/js/jquery.ztree.exedit.js');
		/**
		 * @constructor cookbook/recipe/list
		 * @alias RecipeList
		 * @parent cookbook
		 * @inherits can.Control
		 * Lists recipes and lets you destroy them.
		 */
		return can.Control(
			/** @Static */
			{
				/**
				 * adding default options
				 */
				defaults: {

				}
			},
			/** @Prototype */
			{
				/**
				 * Create a recipe list, render it, and make a request for finding all recipes.
				 */
				init: function() {
					var obj = this;
					this.element.html(DDEjs());
					System.ddl("0", function(dd) {
							for(var i = 0; i < dd.length; i++) {
									dd[i].open = false;
									dd[i].isParent = true;
									dd[i].icon=$.basePath+"/jsLibs/img/dd.png";
								}
						$.fn.zTree.init($("#dd_tree"), obj.getOrgSetting(), dd);
					})
				},
				/**
				 * 获取组织架构的ztree的配置
				 */
				getOrgSetting: function() {
					var obj = this;
					var setting = {
						async: {
							enable: true,
							type: "get",
							url: $.contextPath + "/common/dd/list",
							autoParam: ["id"],
							dataFilter: function(treeId, parentNode, responseData) {
								for(var i = 0; i < responseData.length; i++) {
									responseData[i].open = false;
									responseData[i].isParent = true;
									responseData[i].icon=$.basePath+"/jsLibs/img/dd.png";
								}
								return responseData;
							}
						},
						callback: {
							//点击方法，当点击角色后将自动填充其详细信息，含相关的功能列表
							onClick: function(event, treeId, treeNode) {
								var rinfo = $(obj.element).find("#show_dd");
								obj.currentRole = treeNode;
								var ct = $("<h3></h3>");
								ct.html(treeNode.id + "------>" + treeNode.name);
								rinfo.html(ct);
							}
						},
						view:{
							addHoverDom: function(treeId, treeNode) {
								var sObj = $("#" + treeNode.tId + "_span");
								if(treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
								var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
									"' title='add node' onfocus='this.blur();'></span>";
								sObj.after(addStr);
								var btn = $("#addBtn_" + treeNode.tId);
								if(btn) btn.bind("click", function() {
									var dname=prompt("请输入字段名字，索引将自动生成");
									if(dname.length<2){
										alert("太短了");
										return ;
									}
									var did;
									if(!treeNode.children||treeNode.children.length<1){
										did=treeNode.id+"_1";
									}else{
										var ndid=treeNode.children[treeNode.children.length-1].id;
										var did=did.substring(0,ndid.lastIndexOf("_"))+(parseInt(ndid.substring(ndid.lastIndexOf("_")+1))+1)
									}
									
									System.dds({id:did,name:dname,pId:treeNode.id},function(data){
										data.data.icon=$.basePath+"/jsLibs/img/dd.png";
										$.fn.zTree.getZTreeObj("dd_tree").addNodes(treeNode,data.data);
									});
									return false;
								});
							},
							removeHoverDom: function(treeId, treeNode) {
								$("#addBtn_" + treeNode.tId).unbind().remove();
							},
							selectedMulti: false
						}
					
					}
					return setting;
				}

			});
	});