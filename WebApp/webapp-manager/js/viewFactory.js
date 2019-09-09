/**
 * version 0.1.0
 */
steal(
	'can',
	function(can) {

		ViewFactory = can.Construct.extend({
			current: 'default',
			uri: steal.URI(document.location.href),
			d: document,
			contentControl: undefined,
			init: function() {
				this.contentId = '#content-body';
				//缓存当前的访问历史元素
				this.controlls = new Array();
				this.currentPage = 0
				this.maxStrackSize = 0;
				this.vfHistory = new Array();
			},
			back: function() {
				var existCon = "";
				for(var i in this.controlls) {
					existCon += "#" + this.controlls[i].attr("id");
					if(i != this.controlls.length - 1) {
						existCon += ",";
					}
				}
				$(this.contentId + " .content_page").not(existCon).remove();
				var currControlEL;
				if(this.vfHistory.length > 1) {
					var i = this.vfHistory.pop();
					//999代表整个等钱显示的页面并不在controlls中缓存，并且在上面的remove元素中已经被元素，因此不需要再进行隐藏，直接显示最后一个元素即可
					if(i != 999) {
						this.controlls[i].hide();
					}
					currControlEL = this.controlls[this.vfHistory[this.vfHistory.length - 1]];
					currControlEL.show();
					if(currControlEL.data("control").refresh) {
						currControlEL.data("control").refresh();
					}
				}
			},
			/**
			 * 将页面保存在历史存储器中，用于返回等操作
			 * @param {Object} el
			 */
			pushPageIntoStack: function(el) {
				if(this.vfHistory.length > 0 && this.vfHistory[this.vfHistory.length - 1] != 999)
					this.controlls[this.vfHistory[this.vfHistory.length - 1]].hide();
				for(var i in this.controlls) {
					if(this.controlls[i].attr("id") == $.session.currFun.id) {
						this.controlls[i].show();
						this.contentControl=this.controlls[i].data("control");
						this.vfHistory.push(i);
						return -1;
					}
				}
				if(this.controlls.length > this.maxStrackSize) {
					this.controlls.shift().remove();
					this.vfHistory.shift();
				}
				var i = this.controlls.push(el);
				this.vfHistory.push(--i);
			},
			/**
			 * 通过调用此方法来构建指定的元素，
			 * @param {Object} rid 可以是资源的路径，也可以是一个Control的js对象，均可以进行实例化
			 * @param {Object} contentId 当前容器的选择器或者其el元素.
			 * @param {Object} pageData 传入即将实例化Control的options参数
			 * @param {Boolean} isFlash 是否将此构建添加到内存历史中可供进行返回的操作
			 */
			build: function(rid, contentId, pageData, isFlash) {
				if(isFlash == undefined) {
					isFlash = true;
				}
				var contentId = contentId ? contentId : ViewFactory.contentId;
				if(contentId == this.contentId) {
					var self = this;
					$(contentId).find(".content_page").each(function() {
						var isEx = false;
						for(var ci in self.controlls) {
							if($(this).attr("id") == self.controlls[ci].attr("id")) {
								isEx = true;
								break;
							}
						}
						if(!isEx){
							$(this).remove();
						}
					});

					var pageContiner = $("<div class='content_page'></div>");
					pageContiner.attr("id", $.session.currFun.id);
					$(contentId).append(pageContiner);
					contentId = pageContiner;
					if(isFlash) {
						var cp = this.pushPageIntoStack(contentId);
					} else {
						this.controlls[this.vfHistory[this.vfHistory.length - 1]].hide();
						this.vfHistory.push(999);
					}

					if(cp < 0) {
						pageContiner.remove();
						return;
					}

				}
				if($.isFunction(rid)) {
					var contentControl = new rid(contentId, pageData);
					$(contentId).data("control", contentControl)
					return;
				}
				if(!rid) {
					rid = this.current;
				} else {
					this.current = rid;
				}
				var rid = this.current.replace(".", "/");

				var orid = {
					path: rid.substr(0, rid.lastIndexOf("/")),
					rid: rid.substr(rid.lastIndexOf("/") + 1)
				}
				steal("modules/" + orid.path + "/control/" + orid.rid + ".js", function(ContentControl) {
					self.contentControl = new ContentControl(contentId, pageData);
					$(contentId).data("control", self.contentControl)
				});
			
			},
			/**
			 * 获取当前Control的值，此Control为表单类值，所以不再内存中进行驻留
			 */
			getValue: function() {
				return $("#" + $.session.currFun.id).data("control").getValue();
			}
		}, {

		})
		$.VF = ViewFactory;
		return ViewFactory;
	});