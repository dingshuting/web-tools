/**
 * 模态窗口，用于弹出框的相关提示，模态框在单个访问页面下，为单例模式
 */

steal(
	'can',
	'../plugins/flavr/index_files/flavr.min.js',
   '../plugins/flavr/index_files/animate.css',
    "../plugins/flavr/index_files/flavr.css",
	function(can,ViewFactory) {
		
		Modal=can.Construct.extend(
				{
					_getModalEl:function(classtype,head,body){
						var $continer=$("<div class='am-modal' id='am-modal-container' tabindex='-1'></div>");
						$continer.addClass(classtype);
						var $dialog=$(" <div class='am-modal-dialog'></div>");
						$dialog.append("<div class='am-modal-hd'>"+head+"</div>");
						var $db=$("<div class='am-modal-bd'></div>");
						$db.append(body);
						$dialog.append($db);
						
						var footer=$("<div class='am-modal-footer'></div>");
						
						$dialog.append(footer);
						$continer.append($dialog);
						return $continer;
					},
					_getPopupEl:function(head){
						var $continer=$("<div class='am-popup' id='am-popup-container' tabindex='-1'></div>");
						var $dialog=$(" <div class='am-popup-inner'></div>");
						
						$dialog.append("<div class='am-popup-hd'> <h4 class='am-popup-title'>"+head+"</h4> <span data-am-modal-close class='am-close'>&times;</span></div>");
						$dialog.append("<div class='am-popup-bd' id='am-popup-container-bd'></div>");
						
						$continer.append($dialog);
						return $continer;
					},
					/**
					 * 确认的窗口，可显示指定的内容供用户确认，其中包涵2个按钮，确认和取消
					 */
					confirm:function(title,body,onconfirm,oncancle){
							var html=body;
                            new $.flavr({dialog : 'confirm', content: html, onConfirm : onconfirm });
					},
					
					confirmList:function(title,body,data,onconfirm,oncancle){
							var html = $("<div id='confirmList'></div>");
							$.VF.build(body,html,data);
                             new $.flavr({  content: html ,  position : 'top-mid', dialog : 'confirm', onConfirm:onconfirm });
                            
					},
					confirmListNoData:function(title,body,onconfirm,oncancle){
							var html = $("<div id='confirmList'></div>");
							ViewFactory.build(body,html);
                             new $.flavr({  content: html ,  position : 'top-mid', dialog : 'confirm', onConfirm:onconfirm });
                            
					},
					/**
					 * 
					 * 信息提示框，代替window的alert
					 */
					alert:function(body){
						new $.flavr({modal : false,position : 'top-mid', autoclose : true, timeout : 3000 , animateEntrance
                            : "slideInDown", animateClosing : "slideOutUp", content : body })
					},
					/**
					 * 隐藏窗口，无论是哪种类型的模态框，均使用此方法隐藏
					 */
					hide:function(){
						if(this.el)
						this.el.modal('close');
					},
					/**
					 * 显示loading框，给予元素，实现局部loading，通过调用hideLoading隐藏loading
					 */
					showLoading:function(el,content){
						$(el).showLoading();
						if(content){
							$(".loading-indicator").append("<lable>"+content+"</lable>");
						}	
//						if($.AMUI)
//						$.AMUI.progress.start();
					},
					/**
					 * 弹出的整体框，其内容为整个全屏，用于信息量大的详细信息的展示
					 */
					popup:function(title,rid,htmlContent){
						$("#am-modal-container").remove();
						this.el=this._getPopupEl(title);
						$("body").append(this.el);
						if(rid){
							ViewFactory.build(rid,"#am-popup-container-bd");
						}else{
							this.el.find("#am-popup-container-bd").html(htmlContent);
						}
						this.el.modal();
					},
					/**
					 * 有输入框的用户确认模态框，用户可输入相关信息后确认关联信息
					 */
					prompt:function(body,onConfirm,onCancel){
						return new $.flavr({ content : body, position : 'top-mid', 
						animateEntrance:"slideDown", animateClosing : "slideOutUp",fullscreen:true,
                            buttons :{ primary :{action:onConfirm,text:"确认"}, danger : {style:"danger",action:onCancel,text:"取消"}}}); 
					},
					hideLoading:function(el){
						$(el).hideLoading();
						//if($.AMUI)
						//$.AMUI.progress.done();
					},
					changeLoadingContent:function(el,content){
						$(".loading-indicator lable").html(content);
					},
					formdata:function(){
						return $("#am-modal-container form").formParams();
					},
						view:function(vid){
						if(vid){
							return $("#am-modal-container").find(vid)
						}
						return $("#am-modal-container");
					}
					
				},{});
		window.alert=Modal.alert;
		$.Modal=Modal;
		return Modal;
				
	});