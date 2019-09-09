steal(
    'can',
    function(can) {
        return can.Control(
            /** @Static */
            {},
            /** @Prototype */
            {
                init: function(el, options) {
                    var self = this;
                    var path=options.path;
                    //var url = can.view('htmls/'+path+'.html');
                    self.showPage(path);
                    el.append("<span>此视图展示方式为</span>")
                    var b=$("<a>全屏展示</a>");
                    el.append(b);
                    b.click(function(){
                    	self.showPage(path);
                    })
                },
                showPage:function(uri){
                	var dialog=$.Modal.prompt("");
                    dialog.fullscreen();
                    setTimeout(function(){
                    	dialog.content("<iframe width='100%' height='"+(document.body.clientHeight-100)+"' frameborder='0' src='htmls/"+uri+".html' ></iframe>");
                    },500)
                },
                refresh:function(){

                }
            });
    });