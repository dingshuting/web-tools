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
	$.Model = function(url) {
		var server = "http://101.53.102.23:810/";
		var t;

		if (url == 'classroom') {
			t = "dataClass";
		}
		var urls = {
			dataClass: {
				get: url + "/info/",
				delete: url + "/remove/",
				save: url + "/save",
				list: url + "/classroomlist.show"
			}
		}
		return can.Model(
			/* @static */
			{
				target: t,
				head: url,
				URL: urls,
				getHead: function(id, callback) {
					if (id) {
						$.get(server + "/extra_data/info/" + id, callback);
					} else {
						return url;
					}
				},
				/**
				 * @param {Object} pn 当前页数
				 * @param {Object} query 查询对象，基本的对象形式，查询区间段时，查询对象字段为对象形式,如：{普通字段:普通字段值,区间字段名：{start:'val',end:'val'}}
				 * @param {Object} callback 查询完毕后的回调方法
				 */
				list: function(pn, query, callback) {
					//$.Modal.showLoading("body","加载中...");
					var requestJsonObj = $.toJSON(query);
					if (requestJsonObj == "{}") {
						requestJsonObj = undefined;
					}
					var self = this;
					jQuery.ajax({
						url: server + this.URL[this.target].list+"?"+pn,
						data: requestJsonObj,
						type: "post",
						dataType: "json",
						contentType: "text/json",
						success: function(data) {
							callback(data);
							//$.Modal.hideLoading("body");
						},
						error: function(data) {
							alert("查询出错", data);
						}
					});
				},
				get: function(key, callback) {
					var self = this;
					$.get(server + this.URL[this.target].get + key, function(data) {
						if (typeof data === 'object') {
							callback(data);
						} else {
							callback($.parseJSON(data));
						}

					});
				},
				delete: function(key, callback) {
					$.get(server + this.URL[this.target].remove + key, callback);
				},
				save: function(model, callback) {
					var requestJsonObj = $.toJSON(model);
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
	};
});