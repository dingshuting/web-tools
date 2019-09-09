/**
 * 动态的生成表单
 */
steal(
	'can',
	'js/modal-show.js',
	function(can, ModelShow) {
		steal('plugins/zTree/css/zTreeStyle/zTreeStyle.css');
		steal('plugins/zTree/js/jquery.ztree.core.js', function() {
			steal('plugins/zTree/js/jquery.ztree.excheck.js');
		});
		steal('plugins/zTree/js/jquery.ztree.exedit.js');
		var ZTree = can.Control.extend({
			/**
			 * 
			 * @param {Object} el 
			 * @param {Object} options 
			 * 
			 * //zTree数据
			 * options.zNodes=[{
			 * 			id:编号，
			 * 			pid:父编号
			 * 			name:显示名称
			 * }]
			 * 
			 * options.model="";//model对象
			 * 
			 * options.isShowActs :true/false,是否创建添加、修改、删除按钮。
			 * 
			 * options.isCheck = true|false 节点上是否显示 checkbox / radio
			 * 
			 * options.isMultiselect = true|false 是否为多选
			 * 
			 * options.isOnlyLessChild=true|false 是否只能是最子业才能选择，只有isCheck=true时生效
			 * 
			 */
			init: function(el, options) {
				var self = this;
				if(options.isShowActs == undefined) {
					options.isShowActs = false;
				}
				if(options.isCheck == undefined) {
					options.isCheck = false;
				}
				if(options.isMultiselect == undefined) {
					options.isMultiselect = false;
				}
				if(options.isOnlyLessChild == undefined) {
					options.isOnlyLessChild = false;
				}
				var height = $(window).height() / 2;
				var treeEl = $("<ul id='showTree' class='ztree'></ul>");
				treeEl.attr("style", "background-color:ghostwhite;overflow-y:scroll;height:" + height + "px;");
				$(el).append(treeEl);
				this.setting=this.getSetting();
				if(options.isShowActs) {
					var obj = new Object();
					obj.id = -1;
					obj.pId = 0;
					obj.name = "添加";
					options.zNodes.push(obj);
					self.setting.view.addHoverDom = self._addHoverDom;
					self.setting.view.removeHoverDom = self._removeHoverDom;
				} else {
					self.setting.view.addHoverDom = null;
				}
				$.fn.zTree.init($(treeEl), self.setting, options.zNodes);
			},
			//初始化配置参数
			getSetting: function() {
				var self=this;
				var setting={
					view: {
						showIcon: false
					},
					edit: {
						enable: true,
						editNameSelectAll: true,
						showRemoveBtn: null,
						showRenameBtn: null
					},
					data: {
						simpleData: {
							enable: true
						}
					},
					callback: {
						beforeDrag: null,
						beforeEditName: null,
						beforeRemove: null,
						onClick: null,
						beforeCheck: function(treeId, treeNode) {
							if(self.options.isOnlyLessChild==true){
								if(treeNode.isParent){
									$.Modal.alert('只能选择最子集');
									return false;
								}else{
									return true;
								}
							}
						}
					},
					check: {
						enable: false,
					}
				}
				if(self.options.isCheck) {
					setting.check.enable = true;
					if(self.options.isMultiselect) {
						setting.check.chkStyle = "checkbox";
					} else {
						setting.check.chkStyle = "radio";
						setting.check.radioType = "all";
					}
				}
				return setting;
			},
			//用于当鼠标移动到节点上时,显示隐藏状态同 zTree 内部的编辑、删除按钮
			_addHoverDom: function(treeId, treeNode) {
				if(treeNode.level == 2) return;
				var sObj = $("#" + treeNode.tId + "_span");
				if(treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
				var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
					"' title='添加子分类' onfocus='this.blur();'></span>";
				sObj.after(addStr);
				var btn = $("#addBtn_" + treeNode.tId);
				if(btn) btn.bind("click", function() {
					var zTree = $.fn.zTree.getZTreeObj("treeDemo");
					//添加子分类
					if(treeNode.level == 0) {
						if(treeNode.isLastNode) { //添加一级分类
							add(1, treeNode.id, treeNode.tId); //级别、分类Id
						} else { //添加子类即添加二级
							add(2, treeNode.id, treeNode.tId); //级别、分类Id
						}
					} else if(treeNode.level == 1) {
						add(3, treeNode.id, treeNode.tId); //添加三级
					}
					return false;
				});
			},
			//用于当鼠标移出节点上时,显示隐藏状态同 zTree 内部的编辑、删除按钮
			_removeHoverDom: function(treeId, treeNode) {
				$("#addBtn_" + treeNode.tId).unbind().remove();
			},
			getValue() {
				var treeObj = $.fn.zTree.getZTreeObj("showTree");
				return treeObj.getCheckedNodes(true);
			}

		});
		$.ZTreeControl = ZTree;
		return ZTree;
	});