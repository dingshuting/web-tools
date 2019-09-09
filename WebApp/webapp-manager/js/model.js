/**
 * 这个model用于所有后台的公共查询，适用于查询mysql和mongodb的基本的CURD操作。
 * 对于个性的功能可以在自己的model中引用此实例，实现js层的继承动作
 * 执行完成后，Model对象将会赋值在$对象中，可以通过$.Model(参数)使用，
 * $.Model(extraData) 描述
 * 	extraData-为数据表描述对象，参考后台的ExtraData的model对象
 * 			  同时该对应也可以作为自定义方法的对象，其中：
 * 				extraData.edType，代表自定义方法的对象名字
 * 				extraData.action,代表自定义方法对象的属性，该属性记录了url值
 * 
 */
steal('can', function(can) {
	steal('js/jquery.json.js');
	$.Model = function(extraData) {
		//var server = "http://jsny.ijushang.com/";

		var server = $.contextPath;
		var t;
		if(!extraData) {
			extraData = {};
			extraData.edType = 3;
		}
		if(!isNaN(extraData)) {
			var tn = extraData;
			extraData = {};
			extraData.edType = tn;
		}
		if(extraData.edType == 1000401) {
			t = "sql";
		} else if(extraData.edType == 1000402) {
			t = "nosql"
		} else if(extraData.edType == 3) {
			t = "dataDict";
		} else {
			//使用自定义的方法
			t = extraData.edType;
		}
		var urls = {
			sql: {
				get: "/mdyn/" +extraData.nameCodeAlias + "/info/",
				delete: "/mdyn/" +extraData.nameCodeAlias + "/remove/",
				save: "/mdyn/" +extraData.nameCodeAlias + "/save",
				list: "/mdyn/" +extraData.nameCodeAlias + "/list/",
/*
				all: "/mdyn/" +extraData.nameCodeAlias + "/all",

*/
                all: "/mdyn/" +extraData.nameCodeAlias + "/list/",
				changeStatus: "/mdyn/" +extraData.nameCodeAlias + "/cs/"
			},
			nosql: {
				get: "/dyn/" + extraData.nameCodeAlias + "/info/",
				save: "/dyn/" + extraData.nameCodeAlias + "/save",
				delete: "/dyn/" + extraData.nameCodeAlias + "/remove/",
				list: "/dyn/" + extraData.nameCodeAlias + "/list/",
				changeStatus: "/dyn/" + extraData.nameCodeAlias + "/cs/",
				all: "/dyn/" + extraData.nameCodeAlias + "/all/",
				count: "/dyn/" + extraData.nameCodeAlias + "/count/",
				dataBind: "/dyn/" + extraData.nameCodeAlias + "/databind/"

			},
			dataDict: {
				get: "/common/dd/info/",
				delete: "/common/dd/rm/",
				list: "/common/dd/list/",
			}
		};
		var Model = can.Model(
			/* @static */
			{
				//此参数里面内容需为函数，当查询条件data值在此有定义时将会调用此属性里定义的函数来获取最终的提交值
				options:{
					//获取当前登录者组织id
					
				},
				target: t,
				head: extraData,
				URL: urls,
				getHead: function(id, callback) {
					if(id) {
						$.getJSON(server + "/mdyn/extra_data/info/" + id, callback);
					} else {
						return extraData;
					}
				},
				execute: function(data, callback) {
					Model.post(server + urls[this.target][this.head.action], data, callback);
				},
				//以表单的方式进行提交
				executeForm: function(data, callback) {
					jQuery.post(server + urls[this.target][this.head.action], data, callback);
				},
				post: function(url, data, callback) {
					if(typeof data == "object") {
						data = $.toJSON(data);
					}else if(typeof data=="function"){
						data=data(this);
					}else if(Model.options[data]){
						data=Model.options[data]();
					}
					var requestJsonObj = $.toJSON(data);
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
                                $.Modal.hideLoading();
                            }
						}
					});
				},
				/**
				 * @param {Object} pn 当前页数
				 * @param {Object} query 查询对象，基本的对象形式，查询区间段时，查询对象字段为对象形式,如：{普通字段:普通字段值,区间字段名：{start:'val',end:'val'}}
				 * @param {Object} callback 查询完毕后的回调方法
				 */
				list: function(pn, query, callback) {
					$.Modal.showLoading("body", "加载中...");
					Model.post(server + this.URL[this.target].list + pn,query, function(data) {
						if(!data.list) {
							data.list = new Array();
						}
						callback(data);
						$.Modal.hideLoading("body");
					})
				},
				get: function(key, callback) {
					var self = this;
					$.get(server + this.URL[this.target].get + key, function(data) {
						if(typeof data === 'object') {
							callback(data);
						} else {
							try{
								callback($.parseJSON(data));
							}catch(e){
								console.error("there is a error occured when getting the detail-info from '"+self.URL[self.target].get+key+"'");
							}
							
						}

					});
				},
				delete: function(key, callback) {
					$.get(server + this.URL[this.target].delete + key, callback);
				},
				save: function(model, callback) {
					Model.post(server + this.URL[this.target].save,model,callback);
				},
				cs: function(key, status, callback) {
					$.get(server + this.URL[this.target].changeStatus + key, {
						"s": status
					}, callback);
				},
				dataBind: function(target, did, tid, callback) {
					$.get(server + this.URL[this.target].dataBind + target + "/" + did + "/" + tid, callback);
				},
				all: function(pn, query, callback) {
					var self = this;
					var url = this.URL[this.target].all;
					if(this.target == "nosql") {
						server = server.substring(0, server.length - 1);
					}
					if($.isFunction(pn)) {
						callback = pn;
						pn = 1;
					}

                    url = url + pn + "?count=999";
					/*if(this.target != "sql") {
						url = url + pn + "?count=999";
					}*/
					$.Modal.showLoading("body", "加载中...");
					Model.post(server + url,query,function(data) {
							callback(data);
							$.Modal.hideLoading("body");
						})
				},
				/**
				 * @param {Object} pn 当前页数
				 * @param {Object} query 查询对象，基本的对象形式，查询区间段时，查询对象字段为对象形式,如：{普通字段:普通字段值,区间字段名：{start:'val',end:'val'}}
				 * @param {Object} callback 查询完毕后的回调方法
				 */
				count: function(query, callback) {
					$.Modal.showLoading("body", "加载中...");
					var requestJsonObj = $.toJSON(query);
					if(requestJsonObj == "{}") {
						requestJsonObj = undefined;
					}
					var self = this;

					jQuery.ajax({
						url: server + this.URL[this.target].count,
						data: requestJsonObj,
						type: "post",
						dataType: "json",
						contentType: "text/json",
						success: function(data) {
							callback(data);
							$.Modal.hideLoading("body");
						},
						error: function(data) {
							alert("查询出错" + data);
						}
					});
				}
			},
			/** 添加项目 **/
			/* @Prototype */
			{
				target: t,
				URL: urls,
				save: function(callback) {
					var requestJsonObj = $.toJSON(this._data);
					jQuery.ajax({
						url: server + this.URL[this.target].save,
						data: requestJsonObj,
						type: "post",
						dataType: "json",
						contentType: "application/json",
						success: callback,
						error: function(data) {
							alert("信息保存出错", data);
						}
					});
				},
				update: function(callback) {
					this.save(callback);
				}
			});
		return Model;
	};
});