steal(
	'can',
	function(can) {
		Template = can.Construct.extend({
			/**
			 * 加载模板内容
			 */
			load: function(tname) {
				if(!tname) {
					tname = this.current;
				}
				this.current = tname;
				$("body").append(can.view("templates/" + tname + "/index.html"));
				steal("templates/" + tname + "/load.js");
			}
		},{})
		return Template;
	});