/**
 * 模块下默认的引导文件，该文件中可能会根据模块的复杂程度加载多个视图控制器，如：在一个列表模块中，可能需要加载，查询条件模块、列表模块
 * 、分页模块等等一系列模块在一起后组成整个页面
 */
steal('can', 'modules/common/control/menu.js','./assets/js/core/bootstrap.min.js','./assets/js/core/jquery.slimscroll.min.js',
'./assets/js/core/jquery.scrollLock.min.js','./assets/js/core/jquery.placeholder.min.js','./js/app.js',
		function(can, Menu) {
			$("header").append(can.view("templates/white/ejs/head.ejs"));
			$("#logout").click(function(){
				$.System.logout(function(){
					window.location.href=APP_CONFIG.domain;
				});
			});
			$.VF.build("beitou.default");
			var menu=new Menu("#menu-container",{finished:function(){
				$("#current-user").html();
				App.init();
			}});
			
		}).then(function(){
			
		});