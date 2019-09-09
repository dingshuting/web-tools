/**
 * 这个model用于所有后台的公共查询，适用于查询mysql和mongodb的基本的CURD操作。
 * 对于个性的功能可以在自己的model中引用此实例，实现js层的继承动作
 * 执行完成后，Model对象将会赋值在$对象中，可以通过$.Model(参数)使用，
 * $.Model(extraData) 描述
 * 	extraData-为数据表描述对象，参考后台的ExtraData的model对象
 * 			  同时可以只为数字，但只能为数字3，代表公共的组件查询，不能调用实例的方法否则会引发程序异常
 * 
 */
steal('can', function(can) {
	steal('js/jquery.json.js');
	/**
	 * 
	 * @param {Object} a  action动作名称，自定义情况下为t.a来获取请求链接，其它情况线下则a为表名
	 * @param {Object} t  目标可空，默认为api
	 */
	$.Model = function(a,t) {
		var server = $.request.domain+"/";
		var urls = {
			api: {
				get: "api/" + a + "/info/",
				list: "api/" + a + "/list/",
			},
			dyn:{
				get: "dyn/" + a + "/info/",
				save: "dyn/" + a + "/save",
				del: "dyn/" + a + "/remove/",
				list: "dyn/" + a + "/list/",
				changeStatus: "dyn/" + a + "/cs/"
			},
			mysqlDyn:{
				get: "mdyn/"+a + "/info/",
				save: "mdyn/"+a + "/save",
				del:"mdyn/"+a + "/remove/",
				list:  "mdyn/"+a + "/list/",
				changeStatus: "mdyn/"+a + "/cs/"
			},
			dataDict: {
				get: "common/dd/info/",
				del: "common/dd/rm/",
				list: "common/dd/list/",
			},
			util:{
				sendSmsCode:"common/sendsms",
				verifySmsCode:"common/verifysmscode",
				keepAlive1:"common/ka"
			},
			user:{
				savepwd:"profile/savepwd",
				resetPwd:"profile/resetpwd",
				saveProfile:"profile/bind_sc",
				savephone:"wx_user/savephone"
			},
		}
		$.extend(true,urls,HLoaderConfig.modelUrl)
		Model = can.Model(
			/* @static */
			{
				target: typeof t=="undefined"?HLoaderConfig.modelDefaultTarget:t,
				URL: urls,
				//默认的rest提交方式
				execute:function(data,callback){
					if(data){
						var url=server + this.URL[this.target][a];
						if(data["verifySmsCode"]){
							url +="?verifySmsCode="+data["verifySmsCode"];
						}else if(data["verifyCode"]){
							url +="?verifyCode="+data["verifyCode"];
						}
						Model.post(url,data,callback);
					}
					
				},
				//以表单的方式进行提交
				executeForm:function(data,callback){
					var url=server + this.URL[this.target][a];
					if(data){
						if($.isString(data)){
							data=$.parseJSON(data);
						}
						if(data["verifySmsCode"]){
							url +="?verifySmsCode="+data["verifySmsCode"];
						}else if(data["verifyCode"]){
							url +="?verifyCode="+data["verifyCode"];
						}
					}
					jQuery.post(url,data,callback);
				},
				executeGet:function(data,callback,pathPara){
					var url=server + this.URL[this.target][a];
					if(pathPara){
						url+=pathPara;
					}
					jQuery.get(url,data,function(data){
						if($.isString(data)){
							data=$.parseJSON(data);
						}
						callback(data);
					});
				},
				executeRest: function(query, callback,pathPara) {
					var url;
					if(this.URL[this.target][a]){
						url=server + this.URL[this.target][a];
					}else{
						url=server + this.URL[this.target].list;
					}
					if(pathPara){
						url+=pathPara;
					}
					Model.post(url,query,callback);
				},
				post:function(url,data,callback){
					if(typeof data=="object"){
						data= $.toJSON(data);
					}
					if(data)
					data=data.replace("undefined","null");
					jQuery.ajax({
						url: url,
						data: data,
						type: "post",
						dataType: "json",
						/*contentType: "text/json",*/
						contentType: "application/json",
						success: function(data) {
							callback(data);
						},
						error: function(data) {
							console.error(data);
                            if (data.status == 401){
                                $.Modal.alert('暂无权限，请联系管理员！');
                            }
						}
					});
				},
				
				/**
				 * @param {Object} pn 当前页数
				 * @param {Object} query 查询对象，基本的对象形式，查询区间段时，查询对象字段为对象形式,如：{普通字段:普通字段值,区间字段名：{start:'val',end:'val'}}
				 * @param {Object} callback 查询完毕后的回调方法
				 */
				list: function(pn, count, query, callback) {
					var url;
					if(this.URL[this.target][a]){
						url=server + this.URL[this.target][a];
					}else{
						url=server + this.URL[this.target].list;
					}
					if(!query){
						query={};
					}
					if(!query.qp){
						query.qp={"count":count};
					}else if(!query.qp.count){
						query.qp.count=count;
					}
					if(!pn){
						Model.post(url,query,callback);
					}else{
						if((""+pn).indexOf("?")>-1){
							Model.post(url + pn + "&count=" + count,query,callback);
						}else{
							Model.post(url + pn + "?count=" + count,query,callback);
						}
					}
					
				},
				get: function(key, callback) {
					if(!key){
						callback(undefined);
						console.error("the key can not be null when invoke the get-method of Model");
						return;
					}
					var self = this;
					$.get(server + this.URL[this.target].get + key, function(data) {
						if(typeof data === 'object') {
							callback(data);
						} else {
							try{
								var obj=$.parseJSON(data);
								callback(obj);
							}catch(e){
								console.error("the data are not complated ,please check the data in database")
								callback(undefined);
							}
						}

					});
				},
				del: function(key, callback) {
					$.get(server + this.URL[this.target].del + key, callback);
				},
				save: function(model, callback) {
					if(model){
						var url=server + this.URL[this.target].save;
						if(model["verifySmsCode"]){
							url +="?verifySmsCode="+model["verifySmsCode"];
						}
						Model.post(url,model,callback);
					}else{
						Modal.alert('保存数据不能为空');
					}
					
				},
				cs: function(key, status, callback) {
					$.get(server + this.URL[this.target].changeStatus + key, {
						"s": status
					}, callback);
				},
				currentUser: function(callback) {
					var obj = this;
					if(obj.CURR_USER) {
						return obj.CURR_USER;
					}
					var userUrl= $.request.domain + "/user/cu?isDetail=0";
					if(window['CURRENT_USER_URL']){
						userUrl=$.request.domain+CURRENT_USER_URL;
					}
					$.ajax({
						url:userUrl,
						success: function(data) {
							try {
								if(data instanceof Object) {
									if(data) {
										obj.CURR_USER = data;
										callback(data);
									}
								} else {
									callback(undefined);
								}
							} catch(e) {
								callback(undefined);
							}
						},
						error: function(data) {
							callback(undefined);
							return;
						}
					});
					return
				}

			},
			/** 添加项目 **/
			/* @Prototype */
			{
				target: "nosql",
				URL: urls,
				save: function(callback) {
					var requestJsonObj = $.toJSON(this._data);
					Model.post(server + this.URL[this.target].save,callback);
				},
				update: function(callback) {
					this.save(callback);
				}
			});
		return Model;
	};
	var kaId;
	$.keepAliveStart=function(){
		if(!kaId){
			kaId=setInterval(function(){
				$.Model("keepAlive1","util").executeForm();
			},60000)
		}
	}
	$.keepAliveStop=function(){
		if(kaId){
			clearInterval(kaId);
		}
	}
});