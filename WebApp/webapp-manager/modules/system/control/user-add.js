/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal('can', '../ejs/user-add.ejs', '../models/user.js','js/modal-show.js','plugins/webuploader/upload.js','js-lib/lib-form.js',
		'plugins/jquery-validation/jv.js',function(can, userAddEjs, User,Modal,Upload) {
			return can.Control(
			/** @Prototype */
			{
				/**
				 * Create a recipe list, render it, and make a request for
				 * finding all recipes.
				 */
				init : function(el, option) {
					var obj = this;
					//整个监听的初始化参数
					this.linstner=[
					 {el:"#userFrom submit",func:this.formsubmit},
					  {el:"button[name='cancel'] click",func:this.calcel},
					  {el:"#changeDep change",func:this.depchange},
					]
					//list 页面data 属性存入this
					$(el).data("obj",obj);

					var obj = this;
					obj.rolelist=option.data.deps;
					
					obj.element.html(userAddEjs(new can.Observe(option.data)));
					obj.initValidate(el);
					obj.initEvent();
					$(obj.element).find("#userFrom").data("obj",obj);
					/*if (option.act == 'u') {
						User.get(option.uid, function(data) {
							obj.currentUser=data;
							obj.element.html(userAddEjs(data));
							obj.initValidate(el,option.act)
						});
					} else {
						obj.element.html(userAddEjs());
						obj.initValidate(el)
					}*/
				},
				/**
				 * 表单提交方法，不使用表单之间提交，所以返回永远是false，通过model的save保存，首先保存用户信息，后再上传图片信息
				 */
				formsubmit: function(el) {
					var obj = $(this).data('obj');
					el = $(this);
					var checkboxlength=$(el).find("input[type='checkbox']:checked");
					var str="";
					for(var i=0;i<checkboxlength.length;i++){
						str+=$(checkboxlength[i]).val()+",";
					}
					str=str.substring(0, str.length-1);
					$("#roleStr").val(str);
					var data=obj.currentUser?obj.currentUser:el.formParams();
					if(str==""){
						if($("#changeDep").val()==""){
							Modal.alert("请先选择部门！");	
						}else{
							Modal.alert("请选择角色信息！");	
						}
						Modal.hideLoading(el);
						Modal.hide();
						return;
					}
					if ($(el).validationEngine('validate')&&User.validate(data)) {
						User.save(data, function(data) {
							/*data=$.parseJSON(data);*/
							if (data.code == 200) {
								Modal.showLoading(el,'正在保存用户信息...');
								Modal.hideLoading(el);
								//Modal.confirm("用户添加提醒","用户添加成功，是否跳转至用户列表？",function(){
									Modal.alert("添加用户成功");
									Modal.hide();
                                	$.VF.back();
									/*ViewFactory.build("system.user-list");*/
								/*},function(){
									//添加完成后如果留在当前页面则表示为修改当前信息，并将提交按钮和账户设置为无法修改，当值有改变时激活提交按钮
									obj.currentUser=data.data;
									$(el).find("[type=submit]").attr("disabled","disabled");
									$(el).find("[name=accountNo]").attr("disabled","disabled");
								});*/
								/*Modal.changeLoadingContent(el,'用户信息保存成功,正在保存头像');
								obj.uploader.upload({"uid":data.data.id,"act":"userServ"},function(){
									Modal.changeLoadingContent(el,'保存头像完成');
									
								});*/
							}else{
								/*  Modal.confirm("用户","该部门下的登录账户已被使用，请修改登录账户！",function(){
									  	Modal.hide();
									},function(){
										Modal.hide();
										//ViewFactory.build("role-list");
								  });*/
								 alert(data.desc);
							}
							//Modal.hideLoading(el);

						});
					}
					return false;
				},
				/**
				 * 监听input的改变事件，当改变时判断当前是否为修改，如果值有修改则更新到当前的user对象中
				 */
				"form input change":function(el){
					var obj=this;
					if(this.currentUser){
						$.each(this.currentUser,function(a,b){
							if(el.attr("name")==a){
								eval("obj.currentUser."+a+"='"+el.val+"'")
								console.log("set a new val:obj.currentUser."+a+"-->"+el.val());
								var $form=$(el).parents("form");
								var submitBtn=$form.find("[type=submit]");
								submitBtn.attr("disabled",false);
							}
						})
					}
				},
				"#goback click":function(){
					ViewFactory.build("system.user-list");
				},
				"#serviceCenter click":function(el){
                        $.FormControl.defaultRefHander(el,{referenceExtralDataId:"85C396FE-DC3A-4C01-B4B2-EC8AD879B9FA"});
				},
                "#delServiceCenter click":function(el){
                    $('input[name="centerId"]').val('');
                    $('input[name="owner"]').val('100000');
                },
				showrole:function(children,box,obj,level){
					if(typeof(children)!= 'undefined'){
						if(children.length>0){
							for(var j = 0;j<children.length;j++){
								var $cb=$("<div><input value='"+children[j].id+"' type='checkbox' /> "+children[j].name+"</div>");
								$cb.css("margin-left",(level*20)+"px");
								$(box).append($cb);
								if(children[j].children.length>0){
									$cb.find("input").remove();
									obj.showrole(children[j].children,box,obj,level+1);
								}
							}
						}
					}
				},
				depchange:function(el){
					var obj = $(this).parents("#userFrom").data("obj");
					var dep=$(obj.element).find("#changeDep");
					var box = $(obj.element).find("#show_Roles");
					$(box).html("");
					if(dep.find("option:selected").attr("data-status")=="2"){
						$("#serviceCenterContainer").children().slideUp();
						$("#serviceCenterContainer input").val("");
					}else{
						$("#serviceCenterContainer").children().slideDown();
					}
					for(var i = 0;i<obj.rolelist.length;i++){
						if(obj.rolelist[i].id == $(dep).val()){
							obj.showrole(obj.rolelist[i].children,box,obj,1);
						}
					}
					if($(box).html() == ""){
						$("#show_Roles").html("该部门下暂时还没有角色！");
					}
				},
				/**
				 * 初始化表单的验证，通过循环form中的input根据name来对应相应的验证规则
				 * el-表单
				 * option-配置数据，暂时未使用
				 */
				initValidate : function(el,option) {
					var rules = [ {
						"name" : "accountNo",
						"validate" : "validate[required,minSize[6],maxSize[24]]"
					}, {
						"name" : "password",
						"validate" : "validate[required,minSize[6],maxSize[15]]"
					}, {
						"name" : "name",
						"validate" : "validate[required,maxSize[64]]"
					}, {
						"name" : "email",
						"validate" : "validate[required,custom[email]]"
					},{
						"name" : "confirm_pwd",
						"validate" : "validate[required,equals[password]]"
					},
					{
						"name" : "mobilePhone",
						"validate" : "validate[required,custom[phone]]"
					},
					{
						"name" : "officePhone",
						"validate" : "validate[required,custom[officePhone]]"
					},
					{
						"name" : "depId",
						"validate" : "validate[required]"
					},
			
					]
					var $form=$(el).find("form");
					for(var i in rules){
						$form.find("[name="+rules[i].name+"]").addClass(rules[i].validate);
					}
					var re = $("[class*='required']");
					$(re).css("border-color","rgb(221, 183, 76)");
				},
				calcel:function(button){
					$.VF.back();
				},
				initEvent:function(){
					for(var i=0;i<this.linstner.length;i++){
						var el=this.linstner[i].el;
						var event=el.substr(el.lastIndexOf(" ")+1);
						var selecter=el.substr(0,el.lastIndexOf(" "));
						$(this.element).find(selecter).bind(event,this.linstner[i].func);
					}
				}
			});

		});
