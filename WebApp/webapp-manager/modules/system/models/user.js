/**
 * this js is for getting current user who has alread logined, the func of the user and the configuration of system.
 */

steal('can', function(can) {
	/**
	 * @constructor models/system
	 * @alias System
	 * @inherits can.Model
	 *
	 * Wraps backend recipe services.
	 */
	
	return can.Model(
		/* @static */
		{
			//查询用户分页集合数据
			findListSesh: function(pn,user,callback){
				$.post($.contextPath+"/user/list/"+pn,user,callback);
			},
			/** 项目查询项目经理列表 **/
			findProUserList : function(callback){
				$.post("/user/proList",callback);
			},
			//查询用户详情
			findUserInfo: function(id,callback){
				$.post($.contextPath+"/user/info/"+id,callback);
				
			},
			findUserSysDep: function(callback){
				$.post("/user/findusersysdep",callback);
			},
			removeUser: function(id,callback){
				$.post("/user/removeuser?id="+id,callback);
			},
			savePwd:function(user,callback){
				$.post($.request.domain+"/profile/savepwd",{"oldpwd":user.password,"password":user.newPassword},function(data){
					if(data.code=="200"){
						Modal.alert("密码修改成功");
					}else{
						Modal.alert(data.desc);
					}
					callback(data);
				})
			},
            saveUserInfo:function(user,callback){
				
                $.post($.request.domain+"/profile/save",{"officePhone":user.officePhone,"mobilePhone":user.mobilePhone,"email":user.email},function(data){
                    if(data.code=="200"){
                        Modal.alert("用户信息修改成功");
                    }else{
                        Modal.alert(data.desc);
                    }
                    callback(data);
                })
            },
			save:function(user,callback){
				
				var requestJsonObj = $.toJSON(user);
				jQuery.ajax({
					url:$.contextPath+"/user/save",
					data:requestJsonObj,
					type:"post",
					dataType:"json",
					contentType:"application/json",
					success:callback,
					error:function(data){
						Modal.alert("用户添加出错",data);
					}
				});
				//$.post("/user/save1",{"accountNo":"sa"},callback);
			},
			findDepUser : function(depType,callback){
				$.post("/user/findDepUser?depType="+depType,callback);
			},
			validate:function(){
				return true;
			}
		
		},
		
		/* @Prototype */
		{
			
		});
});