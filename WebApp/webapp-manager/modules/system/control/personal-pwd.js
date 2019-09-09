/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal('can', '../ejs/personal-modify-pwd.ejs', '../models/user.js','js/modal-show.js',
		'plugins/My97DatePicker/WdatePicker.js','plugins/jquery-validation/jv.js',
		function(can, personalPwdEjs,User,Modal) {
			return can.Control(
	/** @Prototype */
	{
		/**
		 * Create a recipe list, render it, and make a request for finding all recipes.
		 */
		
		init : function(el, option) {
			var obj = this;
			//监听数组集合
			this.linstner=[
				{el:"#qurenButton click",func:this.qurenButton},
				{el:"#personalModifyPwd input change",func:this.personalModifyPwd},
			]
			//整个监听的初始化参数
			obj.initEvent();
			User.findUserInfo($.session.currentUser.id,function(data){
				if(typeof(data) !="object"){
					obj.currentUser = $.parseJSON(data);
				}else{
					obj.currentUser = data;
				}
				
				
				obj.element.html(personalPwdEjs(obj.currentUser));
				
				$(el).find("#personalModifyPwd").data("obj",obj);
				//整个监听的初始化参数
				obj.initEvent();
				
				obj.currentUser.password = "";
				obj.currentUser.newPassword = "";
				obj.currentUser.newPassword1 = "";
				obj.initValidate($(el).find("#personalModifyPwd"));
			});
			
			
			//初始化参数只有新增时初始化，修改则为原始值该信息不允许被修改
			
			
		},
		/**
		 * 初始化表单的验证，通过循环form中的input根据name来对应相应的验证规则
		 * el-表单
		 * option-配置数据，暂时未使用
		 */
		initValidate : function(el,option) {
			var rules = [ {
				"name" : "password",
				"validate" : "validate[required,minSize[6],maxSize[24]]"
			}, {
				"name" : "newPassword",
				"validate" : "validate[required,minSize[6],maxSize[24]]"
			}, {
				"name" : "newPassword1",
				"validate" : "validate[required,minSize[6],maxSize[24]]"
			}
			]
			var $form=$(el);
			for(var i in rules){
				$form.find("[name="+rules[i].name+"]").addClass(rules[i].validate);
			}
			var re = $("[class*='required']");
			$(re).css("border-color","rgb(221, 183, 76)");
			$form.validationEngine();
		},
		qurenButton:function(el){
			
			var obj = $(this).parents("#personalModifyPwd").data("obj");
				Modal.showLoading($(obj.element).find("#personalModifyPwd"),'正在修改密码...');
				if ($(obj.element).find("#personalModifyPwd").validationEngine('validate')&&User.validate(obj.currentUser)) {
					if(obj.currentUser.newPassword != obj.currentUser.newPassword1){
						alert("新密码确认不一致，请修改!");
						Modal.hideLoading($(obj.element).find("#personalModifyPwd"));
					}else{
						User.savePwd(obj.currentUser,function(data){
							Modal.hideLoading($(obj.element).find("#personalModifyPwd"));
						});
					}
				}else{
					Modal.hideLoading($(obj.element).find("#personalModifyPwd"));
					return;
				}
		},personalModifyPwd:function(el){
			var el = this;
			var obj = $(this).parents("#personalModifyPwd").data("obj");
			if(typeof(obj.currentUser)!="undefined"){
				$.each(obj.currentUser,function(a,b){
					if($(el).attr("name")==a){
						eval("obj.currentUser."+a+"='"+$(el).val()+"'");
						console.log("set a new val:obj.currentUser."+a+"-->"+$(el).val());
						var $form=$(el).parents("form");
						var submitBtn=$form.find("[type=submit]");
						submitBtn.attr("disabled",false);
					}
				});
			};
			
			
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
