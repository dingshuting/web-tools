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
			ddl:function(pid,callback){
				return $.get($.contextPath + "/common/dd/list?id="+pid, callback);
			},
			
			dic:function(pid,callback){
				return $.get($.contextPath + "/common/dd/list/"+pid, callback);
			},
		},

		/* @Prototype */
		{

		});
});