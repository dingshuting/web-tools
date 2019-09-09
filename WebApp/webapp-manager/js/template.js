steal(
	'can',
	function(can) {
		Template=can.Construct.extend(
				{
					current:'default',
					init:function(){
						
					},
					/**
					 */
					load:function(module,tname){
						if(!tname){
							tname=this.current;
						}
						this.current=tname;
						$("body").append(can.view("templates/"+tname+"/index.html"));
						steal("templates/"+tname+"/load.js");
					}
					
				},{
					
				}
			)
		return Template;
	});
	
