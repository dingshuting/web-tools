框架概述
		整体框架依托于原生的静态页面，因此不用像其它框架类型需要有模板的支撑，即使脱离框架，静态页面也仍可独立的进行样式调整，便于前端ul和逻辑实现分工。
	整个数据填充完全依靠具体的数据对象，整个页面元素的结构根据name名字与对象中	的属性名字进行绑定，其支持对象的嵌套，每个对象有其自己的元素作用域，也可以
	通过事件驱动、进行数据的格式化以及事件绑定等操作，以满足不同的业务需求。
		框架底层使用JavaScriptMVC（http://www.javascriptmvc.com）进行封装，除了使用以及封装好的静态页面功能外，也可独立使用此框架的EJS
	模板等功能，具体可参考官网文档。
框架文件夹架构
|-根目录
	apps---存放应用程序的目录
		|--应用1名称
			|-其它应用所需的文件夹或文件
			|-config.js 配置文件
		|--应用2....
	can---JavaScriptMVC的基础工具包，保存can.view、util、route等等
	css---框架公共的css，比如弹出框，showLoading等，应用级别的应放置在应用文件夹内
	docs--文件文档
	img---公共图片
	js----框架自身所需要的必要js，如jquery等相关组件
    js-lib整体自定义组件的文件夹所在。
    	|-model.js 			公共的请求组件，封装基本的请求后台操作
    	|-module-loader.js	模块的加载器，通过此组件加载页面上一个或多个的module组件
    	|-modules			自定义的组件列表
    		|-detail.js		加载详情信息，即将详情内容渲染到期元素作用域内
    		|-list.js		渲染列表类信息
    		|-form.js		加载一个表单类型组件，组件提供基本表单验证，数据封装及表单的数据回显等
    		|-page.js		分页组件与list组件配合使用
    		|-upload.js		上传文件组件
    plugins公共插件文件夹，如表单验证，上传等
    steal--JavaScriptMVC的依赖包管理工具
    stealconfig.js--JavaScriptMVC的基础配置文件
框架加载及工作流程
	1、静态页面加载，并在结尾处导入loader引导js
	2、loader加载器，将会首先执行基本的js导入包含'js-lib/model.js', 'js/system.js'
	3、然后调用system.init初始化（判断用户及加载基本依赖组件和页面所需的组件库）工作。
	4、初始化完成后，执行data-include的页面导入,将静态页面导入补充完全
	5、判断页面是否需要登录，如果需要用户登录但没有登录则自动跳转登录页面，流程结束，否则则继续流程加载页面
	6、新建module-loader的实例，渲染整个页面
		6.1 搜索组件（拥有data-type）的元素,开始渲染组件
		6.2 加载标签（data-*)参数,并将参数封装到组件的options中
		6.3 实例化组件，开始组件的数据渲染
	7、渲染结束，流程结束
标签定义
前台加载静态页面数据的框架，整个框架基本采用标签方式进行相应的数据填充和识别，下面是标签的具体描述
	inner-list:标记为子list模块，不进行异步加载动作
	data-type ： 对于的模块名称，即在文件夹js-lib/modules下的文件名称,
		list: list底层使用detail组件来进行详情显示，因此在data-item中依然可以使用detail的相关标签
			data-query: 列表（list）模块在查询时的附加条件
			data-tpl ： 列表（list）模块的专属标签，此标签在list模块中必须而且只有1个元素，用于执行列表每个元素的模版
			data-item ： 属于data-tpl的子标签，可以有1个或多个，最少有一个，来标记一次循环几条对象
			data-page:是否需要分页，yes or no
			data-act：自定义url的key
			data-querytype:查询类型，form--postForm提交，get--getUrl提交，json--requestBody为json方式
			data-resultkey：获取在结果对象中列表处于字段的键值，默认为list。
			data-value-type：目前就string-list（1,2,3,4,5）格式的字符串形式数组或者不写代表对象数组形式
			data-action:列表（list）模块在模块中有data-action的标签时，会将此标签的值自动绑定点击事件，因此此参数为函数，同时提供默认点击，changeStatus和delete事件，
						剩下需要自己定义来完成相关函数,其中默认函数如下：
				changeStatus：变更当前数据的状态
				 	data-source-status:当前状态，只有数据的当前状态为此值时才显示
					data-target-status ：改变的目标状态
					data-before-action：加载action标签前执行的函数，返回值为true或false，false则该元素不进行加载，用于状态的判端
				delete： 无参数，自动删除当前行数据
				update: 在list中快速更新对象，如执行更新是否默认，类型等少的字段更新
					data-change： 要更新的字段信息，可以为函数，或者有效的json字符，函数参数为当前的对象，可以在函数中对当前对象进行引用变更。
		detail:
			data-event-format:数据格式化标签，在加载数据前进行数据格式化操作，默认提供如下几个操作值，不满足可使用自定义函数进行扩展
				money：格式化成保留2位小数的样子
				date：日期格式化-yyyy-MM-dd
				time:时间格式化HH:mm:ss
				dateTime:日期时间格式化yyyy-MM-dd HH:mm
				moneyWithSign:带钱标识符的数字保留2位小数
			data-array-value:代表值为string数组形式，并无key，与data-value-type配合使用
			data-jstl:表达式标签，此标签的值，会通过执行获取到结果显示到标签html中
			data-if:值为表达式返回true和false，当为true时渲染标签，否则不渲染，obj为当前变量名
			data-pathPara:是否使用路徑參數yes，querytype为get时有效
		page:
			data-forlist： 分页（page）模块专属标签，用于和list模块的列表关联，只需指定list对应的selector即可自动关联
			data-classes: page容器的类样式.
		upload:
			直接调用webuploader上传插件进行图片上传动作，其参数如下：
			
		form:
			data-act: 用于自定义的提交地址对象
			data-target: 提交的对象类型，如dyn、mysqlDym、api等
			data-model:	用于基本rest类型的提交，默认调用此model的save方法，此标签与data-action排斥，当有action时，将调用自定义，不再调用公共的
	data-lazy：	模块在整体页面加载时，先不执行，后续等待某个元素执行完成后再手动去除该属性，添加相关的依赖值或查询条件，然后再在逻辑处通过实例化$.ModuleControl进行加载
	data-model：  模块加载时对应的后台动态的数据源名称，即为表的别名，通过model.js统一调用后台获取数据
	data-event-beforeDataLoader: 在异步获取数据后，对此数据进行过滤的功能函数，如对数据的清洗，去从等功能可通过此标签函数实现
	data-event-finish：模块加载完成后的触发函数，可以在此处填充扩展业务逻辑
	data-method:表单（form）模块在提交数据时的提交方式，有form和rest，由于框架采用异步方式，因此所有的提交均已post方式进行提交。
	data-target:确认数据的请求方式，值有api和dyn2种选项，默认为api
	data-tag: 标识该标签为数据标签，数据标签不对其HTML进行任何变更，只会操作相应的data标签；
	
	data-before-action：data-action加载前执行的函数，该函数返回bool类型，true则继续，false则不会再加载data-action的动作，同时该动作的元素也会消失
	
	data-datadict：数据字典标签，当获取的数据中存在数据字典值时则加注该标签，自动获取数据字典key所对应的值
		data-datadict-pid:数据字典标签必备的参数
		
	
其它：
	$.request.para -- 当前页码请求的 参数信息，可以直接再data标签中使用${变量名使用}；
	chlid_form -- 类样式，用于区分表单嵌套，在表单序列化时，不对拥有此类样式的下属元素进行序列化操作