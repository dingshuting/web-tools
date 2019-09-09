/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal('can', '../ejs/personal-details.ejs', '../models/user.js','js/modal-show.js','../ejs/personal-modify.ejs',
		'plugins/My97DatePicker/WdatePicker.js','plugins/jquery-validation/jv.js',
		function(can, personalDetailsEjs,User,Modal,personalModifyEjs) {
			return can.Control(
	/** @Prototype */
	{
		/**
		 * Create a recipe list, render it, and make a request for finding all recipes.
		 */
		currentUser:null,
		strubgJJ:null,
		init : function(el, option) {
			var obj = this;
			obj.inel = el;
			obj.inoption = el;
			this.linstner=[
				{el:"#submit_button click",func:this.submitUserUp},
			]
			
			User.findUserInfo($.session.currentUser.id,function(data){
				strubgJJ = data;
				if(typeof(data) !="object"){
					obj.currentUser = $.parseJSON(data);
				}else{
					obj.currentUser = data;
				}
				obj.element.html(personalDetailsEjs(obj.currentUser));
				
				$(el).find("#submit_button").data("obj",obj);
				//整个监听的初始化参数
				obj.initEvent();
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
				"name" : "mobilePhone",
				"validate" : "validate[required,custom[phone],maxSize[32]]"
			}, {
				"name" : "officePhone",
				"validate" : "validate[required,custom[officephone],maxSize[32]]"
			}, {
				"name" : "email",
				"validate" : "validate[required,custom[email],maxSize[32]]"
			}
			]
			var $form=$(el);
			for(var i in rules){
				$form.find("[name="+rules[i].name+"]").addClass(rules[i].validate);
			}
			$form.validationEngine();
		},
		submitUserUp:function(){
			var obj = $(this).data('obj');
			var _thisObj = this;
			Modal.confirm("修改信息",personalModifyEjs(obj.currentUser),function(){
				if ($("#personalModify").validationEngine('validate')&&User.validate(obj.currentUser)) {
					User.saveUserInfo(obj.currentUser,function(data){
						// data = $.parseJSON(data);
						if (data.code == 200) {
							//保存提示
							obj.init(obj.inel,obj.inoption);
							 $(".flavr-container").remove();
                            Modal.hideLoading($("#personalModify"));
                            Modal.hide();
                            Modal.alert("修改成功！");
						}else{
							Modal.hideLoading($("#personalModify"));
							Modal.hide();
							Modal.alert("修改失败！");
						}
					});
				}else{
					return false;
				}
				return false;
			});
			
			obj.initValidate($("#personalModify"));
			
			
			$("#personalModify").find("input").change(function(el){
				if(typeof(obj.currentUser)!="undefined"){
					$.each(obj.currentUser,function(a,b){
						if($(el.target).attr("name")==a){
							eval("obj.currentUser."+a+"='"+$(el.target).val()+"'");
							console.log("set a new val:obj.currentUser."+a+"-->"+$(el.target).val());
							var $form=$(el.target).parents("form");
							var submitBtn=$form.find("[type=submit]");
							submitBtn.attr("disabled",false);
						}
					});
				};
				
				
			});
				
		},inputChange:function(el){
			
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
