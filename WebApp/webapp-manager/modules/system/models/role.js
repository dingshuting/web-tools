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
			//删除角色
			remover:function(id,callback){
				$.get($.contextPath+"/org/remover/"+id,callback);
			},
			//删除部门
			removed:function(id,callback){
				$.get($.contextPath+"/org/removed/"+id,callback);
			},
			//得到所有的权限进行zTree展示
			getlists:function(callback,hasFunc){
				if(!hasFunc){
					hasFunc=1;
				}
				$.get($.contextPath+"/org/dlist?hasFunc="+hasFunc,callback);
			},
			
			saver:function(role,callback){
				var tr={id:role.id,name:role.name,parentId:role.parentId,funcs:role.funcs,remark:role.remark};
				jQuery.ajax({
					url:$.contextPath+"/org/saver",
					data:$.toJSON(tr),
					type:"post",
					dataType:"json",
					contentType:"application/json",
					success:callback,
					error:function(data){
						Modal.alert("角色添加出错",data);
					}
				});
				//$.post("/user/save1",{"accountNo":"sa"},callback);
			},
			saved:function(dep,callback){
				jQuery.ajax({
					url:$.contextPath+"/org/saved",
					data:$.toJSON(dep),
					type:"post",
					success:callback,
					dataType:"json",
					contentType:"application/json",
					error:function(data){
						Modal.alert("角色添加出错",data);
					}
				});
				//$.post("/user/save1",{"accountNo":"sa"},callback);
			},
			validate:function(){
				return true;
			}
		
		},
		
		/* @Prototype */
		{
			
		});
});