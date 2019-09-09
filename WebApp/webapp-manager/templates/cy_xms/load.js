/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal('can', 'modules/common/control/menu.js', './js/bootstrap.min.js','./css/bootstrap.min.css','./css/animate.css','./css/style.css',
	'./font-awesome/css/font-awesome.css','./font/iconfont.css','./css/custom.css','./css/plugins/iCheck/custom.css','./js/plugins/metisMenu/jquery.metisMenu.js',
		function(can, Menu,NavBar) {
			$(".top-bar").html(can.view("templates/cy_xms/includes/head.html"));
			$.Menu= new Menu("#nav-left-bar",{finished:function(){
				$('#side-menu').metisMenu();
			}});
			$.VF.build("beitou.default");
			//ViewFactory.build("nav_bar",".head-bar");
			$("footer").html(can.view("templates/cy_xms/includes/foot.html"));
			
		}).then(function(){
			
		});