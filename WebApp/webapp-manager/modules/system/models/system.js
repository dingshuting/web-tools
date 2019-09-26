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
			/**
			 * Find funcs of the user has 
			 */
			funcs: function(callback) {
				if($.showdemo) {
					$.get($.basePath + "/jsLibs/simulations/menu.json", callback);
				} else {
					$.get($.contextPath + "/profile/funcs", callback);
				}

				//return $.get($.contextPath+"/sys/funcs",callback);  
			},
			ddl: function(pid, callback) {
				return $.get($.contextPath + "/common/dd/list/" + pid, callback);
			},
			ddr: function(did, callback) {
				return $.get($.contextPath + "/common/dd/rm/" + did, callback);
			},
			dds: function(dd, callback) {
				$.ajax({
					url: $.contextPath + "/common/dd/save",
					data: $.toJSON(dd),
					type: "post",
					contentType: "application/json",
					success: callback,
					dataType: "json",
				});
			},
			currentUser: function(callback) {
				var obj = this;
				if(obj.CURR_USER) {
					return obj.CURR_USER;
				}
				$.ajax({
					url: $.contextPath + "/user/cu?isDetail=2",
					success: function(data) {
						try {
							if(data instanceof Object) {
								if(data) {
									obj.CURR_USER = data;
									$.ajax({
										type: "get",
										url: $.contextPath + "/profile/funcs",
										async: true,
										success: function(data) {
											obj.CURR_USER.funcs = $.parseJSON(data);
										}
									});
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

		/* @Prototype */
		{

		});
});