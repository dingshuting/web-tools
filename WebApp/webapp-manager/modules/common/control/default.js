steal(
	'can', '../ejs/default.ejs',
	function(can, DefaultEJS) {
		return can.Control(
			/** @Static */
			{},
			/** @Prototype */
			{
				init: function(el, options) {
					var self = this;
					$.get($.contextPath+"/mdyn/statistics_preview",function(data){
						$.get($.contextPath+"/mdyn/statistics_preview_list",function(data1){
							data.list=data1;
							el.html(DefaultEJS(new can.Observe(data)));
						});
					})
				},
				refresh:function(){
					this.table.refresh();
				}
			});
	});