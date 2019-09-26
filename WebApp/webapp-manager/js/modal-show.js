/**
 * 模态窗口，用于弹出框的相关提示，模态框在单个访问页面下，为单例模式
 * 所有组件的提示调用都应通过$.Modal.alert();$.Modal.confirm();来使用
 */

steal(
	'can',
	'../plugins/flavr/index_files/flavr.min.js',
	'../plugins/flavr/index_files/animate.css',
	"../plugins/flavr/index_files/flavr.css",
	'css/modal-show.css', 'plugins/jquery.showLoading.min.js',
	function(can, ViewFactory) {
		Modal = can.Construct.extend({
			getRightModal:function(){
				if($(".right-modal").length>0){
					$(".right-modal").html("");
					$(".right-modal").animate({"right":0},500);
					return $(".right-modal");
				}
				var $rightModal=$("<div class='right-modal col-lg-3 col-md-3'></div>");
				$rightModal.height($(window).height());
				$rightModal.width($(window).width()*0.25);
				$("body").append($rightModal);
				$rightModal.css("right",$rightModal.width()*-1);
				$rightModal.animate({"right":0},500);
				return $rightModal;
			},
			/**
			 * 确认的窗口，可显示指定的内容供用户确认，其中包涵2个按钮，确认和取消
			 */
			confirm: function(title, body, onconfirm, oncancle) {
				var html = body;
				new $.flavr({
					dialog: 'confirm',
					content: html,
					onConfirm: onconfirm
				});
			},
			error:function(body){
				alert(body);
			},
			/**
			 * 
			 * 信息提示框，代替window的alert
			 */
			alert: function(body) {
				$.Dialog=new $.flavr({
					modal: false,
					position: 'top-mid',
					autoclose: true,
					timeout: 3000,
					animateEntrance: "slideInDown",
					animateClosing: "slideOutUp",
					content: body
				})
			},
			/**
			 * 隐藏窗口，无论是哪种类型的模态框，均使用此方法隐藏
			 */
			hide: function() {
				if($.Dialog)
					$.Dialog.close();
			},
			/**
			 * 显示loading框，给予元素，实现局部loading，通过调用hideLoading隐藏loading
			 */
			showLoading: function(el, content) {
				$(el).showLoading();
				if(content) {
					$(".loading-indicator").append("<lable>" + content + "</lable>");
				}
				//						if($.AMUI)
				//						$.AMUI.progress.start();
			},
			/**
			 * 有输入框的用户确认模态框，用户可输入相关信息后确认关联信息
			 */
			prompt: function(body, onConfirm, onCancel) {
				$.Dialog=new $.flavr({
					content: body,
					position: 'top-mid',
					animateEntrance: "slideDown",
					animateClosing: "slideOutUp",
					fullscreen: true,
					buttons: {
						primary: {
							action: onConfirm,
							text: "确认"
						},
						danger: {
							style: "danger",
							action: onCancel,
							text: "取消"
						}
					}
				});
			},
			hideLoading: function(el) {
				$(el).hideLoading();
			},
			changeLoadingContent: function(el, content) {
				$(".loading-indicator lable").html(content);
			}
		}, {});
		window.alert = Modal.alert;
		$.Modal = Modal;
		return Modal;
	});