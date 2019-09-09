/**
 * 模态窗口，用于弹出框的相关提示，模态框在单个访问页面下，为单例模式
 */

steal(
	'can',
	function(can,ViewFactory) {
		steal('js/dialog/dialog.js','css/dialog/dialog.css','js/jquery.showLoading.js');
		Modal=can.Construct.extend(
				{
					confirm:function(title,body,onconfirm,oncancle,okText,cancleText){
						this.type="confirm";
						if(!okText){
							okText="确认";
						}
						if(!cancleText){
							cancleText="取消";
						}
                        if(body.indexOf(".html")>-1){
                        	$.get(body,function(content){
                        		Modal.para=$.urlToObject(body);
                        		$_dialog({
                        			showTitle : false,
									type : 'confirm',
									autoClose:false,
									onClickOk : onconfirm,
									onClickCancel : oncancle,
									contentHtml : content,
									buttonText:{
										ok:okText,
										cancel:cancleText
									}
								});
                        	})
                        }else{
                        	$_dialog({
                        			titleText:title,
									type : 'confirm',
									onClickOk : onconfirm,
									onClickCancel : oncancle,
									buttonText:{
										ok:okText,
										cancel:cancleText
									},
									contentHtml : body
								});
                        }
					},
					showMessage:function(context){
						this.alert(context);
					},
					showError:function(context){
						this.alert(context);
					},
					open:function(url){
						window.location.href=url;
					},
					/**
					 * 信息提示框，代替window的alert
					 */
					alert:function(showHtml){
                        $_dialog({
                            showTitle : false,
                          	autoClose : 2000,
                            contentHtml : showHtml
                        });
					},
					/**
					 * 隐藏窗口，无论是哪种类型的模态框，均使用此方法隐藏
					 */
					hide:function(){
						if(this.el)
							this.el.modal('close');
						$_dialog.close();
					},
					/**
					 * 显示loading框，给予元素，实现局部loading，通过调用hideLoading隐藏loading
					 */
					showLoading:function(el,content){
						//$(el).showLoading();
					},
					hideLoading:function(el){
						//$(el).hideLoading();
					}
				},{});
		window.alert=Modal.alert;
		$.Modal=Modal;
		return Modal;

	});
