/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal(
'can','../ejs/user-modify.ejs','../models/user.js','plugins/My97DatePicker/WdatePicker.js',
function (can,userModifyEjs,User) {
	return can.Control(
	/** @Prototype */
	{
		/**
		 * Create a recipe list, render it, and make a request for finding all recipes.
		 */
			init: function (el,option) {
				console.log(el)
				var obj = this;
				
				this.linstner=[
					 {el:"#user_modify submit",func:this.modifysubmit},
					 {el:"#goback click",func:this.calcel},
					 {el:"button[name='cancel'] click",func:this.calcel},
					{el:"#changeDep change",func:this.depchange},
				]
				$(el).data("obj",obj);
				obj.o=option.data;
				obj.rolelist=option.data.deps;
				obj.element.html(userModifyEjs(new can.Observe(option.data)));
				obj.initValidate(el);
				obj.initEvent();
				obj.currentUser = option.data;
				var box = $(obj.element).find("#show_Roles");
				for(var i = 0;i<obj.rolelist.length;i++){
					if(obj.rolelist[i].id == obj.options.data.deps[i].id&&obj.options.data.deps[i].id==option.data.sysDepId){
						obj.showrole(obj.rolelist[i].children,box,obj,obj.options.data.roles,1);
						break;
						//部门下拉框选中
                        //$("#changeDep").find("option[value='"+obj.rolelist[i].id+"']").attr("selected",true);
					}
				}
			},
			showrole:function(children,box,obj,option,level){
				if(typeof(children)!= 'undefined'){
					if(children.length>0){
						for(var j = 0;j<children.length;j++){
							var $cb=$("<div></div>");
							var html = "<input value='"+children[j].id+"' ";
							if(typeof(option)!='undefined'){
								for(var f =0;f<option.length;f++){
									if(option[f].id == children[j].id){
										html+=" checked='checked' ";
									}
								}
							}
							html+=" type='checkbox' />"
							html+=children[j].name;
							$cb.css("margin-left",(level*20)+"px");
							$cb.html(html);
							$(box).append($cb);
							obj.showrole(children[j].children,box,obj,option,level+1);
						}
					}
				}
			},
			"#serviceCenter click":function(el){
				$.FormControl.defaultRefHander(el,{referenceExtralDataId:"85C396FE-DC3A-4C01-B4B2-EC8AD879B9FA"});
			},
			"#delServiceCenter click":function(el){
				$('input[name="centerId"]').val('');
                $('input[name="owner"]').val('100000');
			},
			modifysubmit:function(el){
				var obj = $(this).parents("#f8b3187d-9d54-4baf-ac2c-749cae440222").data("obj");
				el  = $(this);
				var checkboxlength=$(el).find("input[type='checkbox']:checked");
				var str="";
				for(var i=0;i<checkboxlength.length;i++){
					str+=$(checkboxlength[i]).val()+",";
				}
				str=str.substring(0, str.length-1);
				$("#roleStr").val(str);
				var data=obj.currentUser?obj.currentUser:el.formParams();
				var show_role_str=$("#show_Roles").html();
				if(show_role_str=="该部门下暂时还没有角色！"){
					Modal.alert("该部门下暂时还没有角色，请先添加角色！");	
					Modal.hideLoading(el);
					Modal.hide();
					return;
				}
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
					obj.setData(el.formParams(),obj);
					Modal.showLoading(el,'正在修改用户信息...');
					User.save(data, function(result) {
						if (result.code == 200) {
							Modal.hideLoading(el);
                            $.VF.back();
							alert("用户修改成功!");
							/*Modal.changeLoadingContent(el,'用户信息保存成功,正在保存头像');
							obj.uploader.upload({"uid":data.data.id,"act":"userServ"},function(){
								Modal.changeLoadingContent(el,'保存头像完成');
								
							});*/
							Modal.hide();
						}
						if(data.code==999){
							alert("该部门下的登录账户已被使用，请修改登录账户！");
							Modal.hide();
						}
						//Modal.hideLoading(el);

					});
				}
				return false;
			},
			/**
			 * 监听input的改变事件，当改变时判断当前是否为修改，如果值有修改则更新到当前的user对象中
			 */
			setData:function(formData,obj){
				$.each(formData,function(elName,elValue){
					if(!obj.currentUser){
						obj.currentUser={};
					}
					eval("obj.currentUser."+elName+"='"+elValue+"'");
					
				});
			},
			depchange:function(el){
				var obj = $(this).parents("#f8b3187d-9d54-4baf-ac2c-749cae440222").data("obj");
				var dep=$(obj.element).find("#changeDep");
				var box = $(obj.element).find("#show_Roles");
				$(box).html("");
				for(var i = 0;i<obj.rolelist.length;i++){
					if(obj.rolelist[i].id == $(dep).val()){
						obj.showrole(obj.rolelist[i].children,box,obj,undefined,1);
					}
				}
				if($(box).html() == ""){
					$("#show_Roles").html("该部门下暂时还没有角色！");
				}
				if(dep.find("option:selected").attr("data-status")=="2"){
					$("#serviceCenterContainer").children().slideUp();
				}else{
					$("#serviceCenterContainer").children().slideDown();
				}
			},
			calcel:function(button){
				ViewFactory.build("user-list");
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
			
			},calcel:function(button){
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
