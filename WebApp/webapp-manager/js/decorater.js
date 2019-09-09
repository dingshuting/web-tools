steal(
	'can',
	function(can) {
		Decorater=can.Construct.extend(
				{
					decorateConfig:{
							"proscenium":["addbill.html*","register.html*","apierror.html*"],
							"cy_xms":["/*"]
					},
					decorate:function(){
						var uri = steal.URI(document.location.href);
						for(k in this.decorateConfig){
							for(r in this.decorateConfig[k]){
								if(uri.path.match(this.decorateConfig[k][r])){
									return k;
								}
							}
						}
						return "default";
					},
				},{
					
				}
			)
		return Decorater;
	});
	
