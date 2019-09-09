steal(
	'can',
	function(can) {
		/**
		 *  此Control用于跳转单个表单的入口，也涵盖了单张的用户配置表，主键id为用户id的数据表，通过参数isSingle来判断。
		 * 
		 */
		return can.Control(
			/** @Static */
			{},
			/** @Prototype */
			{
				init: function(el, options) {
					var self = this;
					var actions=$.MenuControl.funcOptions();
					var opts={model:$.Model($.session.currFun.extraData),form:{isCreate:true}};
					if(options.// data['scId'] = scid;=1){
						opts.id=$.session.currentUser.id;
					}
					$.VF.build($.FormControl, undefined, opts,false);
				},
			});
	});