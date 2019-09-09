var DefaultConfig={
		//当ModuleControl实例化没有指定容器时的默认容器
		container:"body",
		/**
		 * 是否加载动态模块，不加载代表存静态页面用于演示或者ui调整等
		 * 默认为true，如果为false情况下，页面将不会再加载包含data-type标签元素的各项动态数据，只显示原始的静态页面。
		 * 当然data-include仍然会被渲染执行
		 */
		isLoadModule:true,
		/**
		 * model默认执行的目标请求对象,该值再请求是如果不指定data-target时将使用的model[t]
		 */
		modelDefaultTarget:"api",
		/**
		 * 扩展自定义的url地址，通过页面标签元素data-target[data-act]代表modelUrl[key]对应的url，获取到url后系统自动发起请求，来加载相应的模块
		 */
		modelUrl:{
			
		},
		//上传组件的配置
		uploadSetting:{
			//上传服务器的上传文件地址
			uri:"/file/upload",
			//返回数据对应的图片或文件的url的key
			resultUrlKey:"url"
		},
		//list组件配置
		listSetting:{
			resultKey:undefined,
		},
		//分页组件属性的配置
		pageSetting:{
			totalSizeName:"totalSize",
			perPageSizeName:"perPageSize",
			currentPageName:"currentPage"
		},
		//默认事件
		DefaultEvent:{
			empty:function(self,el){el.append("<div class='col-lg-12 text-center nodata-hit'>暂无数据</div>")}
		},
		//详情组件默认的格式化数据方法
		detailDataFormat:{
			money: function(obj) {
				if(obj.replace)
                obj=obj.replace(/\-/g, '/');
				return obj.toFixed(2);
			},
			date: function(obj) {
				if(obj.replace)
                obj=obj.replace(/\-/g, '/');
               var dt= new Date(obj);
                if(typeof obj=="number"){
                	dt.setTime(obj);
                }
				return dt.Format("yyyy-MM-dd");
			},
			time: function(obj) {
				if(obj.replace)
                obj=obj.replace(/\-/g, '/');
				return new Date(obj).Format("hh:mm");
			},
			dateTime: function(obj) {
				if(obj.replace)
                obj=obj.replace(/\-/g, '/');
				return new Date(obj).Format("yyyy-MM-dd hh:mm:ss");
			},
			year: function(obj) {
				return new Date(obj).Format("yyyy");
			},
			wuliuTime: function(obj) {
				if(obj.replace)
                obj=obj.replace(/\-/g, '/');
				return new Date(obj).Format("MM-dd hh:mm:ss");
			},
            yuanToWanyuan: function (obj) {
				return (obj/10000).toFixed(2);
            },
            moneyWithSign:function(obj){
				return "￥"+obj.toFixed(2);
            }
		},
		//在data-action 标签中绑定的点击事件函数，参数为function(el,val),el-当前点击的ui元素，当前点击的对象数据
		dataItemActions:{
			
		},
		defaultEventHolder:{
			//当异步处理出错时的处理函数
			ajaxErrorHolder:function(event,xhr,options,exc){
				if(xhr.status==401){
					console.log("尚未登录，正常现象");
					if($.isLogin == "yes") {
						window.location.href=LOGIN_PAGE;
					}
				}else{
					console.error(xhr.responseText);
					alert("请求时发生异常。");
				}
			},
			//数据过滤器，在一条数据完成渲染时调用，通过此函数可以实现个性化得需求，如在数据行或详情中添加动效、数据判断及格式化等等,其中key为数据表code，当当前列表数据为该code时则直接调用过滤器
			//函数包括2个参数 this-代表当前的detailControl 
			//			el-代表当前的加载的页面元素
			//			data-当前渲染到el标签中的数据
			dataFilter:{
				//所有均会触发
				"All":function(el,data){
				},
				"User":function(el,data){
					
				}
			}
		}
	}
if(window['HLoaderConfig']){
	$.extend(true, DefaultConfig, HLoaderConfig);
}
window['HLoaderConfig']=DefaultConfig;
