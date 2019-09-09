/*! jQuery extension for the HLoader, jQuery must be loaded when use this js*/
$.fn.extend({
	control: function(obj) {
		if(obj) {
			this.data("control", obj)
		} else {
			return this.data("control")
		}
	},
	/**
	 * 将元素内超出宽高的字符串替换为...
	 * A function for processing a long text in a element of html.
	 * @param {Number} maxLine the maximum of rows,if it's undifined, the value 'height/lineHeight' will be set by default.
	 */
	subStr:function(maxLine){
		this.each(function(){
			var width=$(this).innerWidth();
			var height=$(this).innerHeight();
			var letterSpace=0;
			if($(this).css("letter-spacing")){
				letterSpace=parseInt($(this).css("letter-spacing"));
			}
			var fontSize=parseInt($(this).css("font-size"))+letterSpace;
			var lineHeight=parseInt($(this).css("line-height"));
			var tmaxLine=maxLine;
			if(!maxLine){
				tmaxLine=parseInt(height/lineHeight);
			}
			maxLength=parseInt(width/fontSize)*tmaxLine;
			var content=$(this).text();
			if(content.length>=maxLength){
				$(this).text(content.substr(0,maxLength-1)+"...");
			}
		});
	},
	/**
	 * this method only worked in single task, you can not use it for mulite task such as load more than two html of div in the meantime.
	 */
	loadHtml:function(url,para,whenFinished){
		var el=$(this);
		if($.isFunction(para)){
			whenFinished=para;
		}
		$.ajax({
			type: "get",
			url: url,
			success: function(data) {
				el.html(data);
				whenFinished?whenFinished.call(el):'';
			}
		});
	}
});
/**
 * Extending the steal in JavaScriptMVC. it will insert the name of current domain to the first location of every argument, because the root path of the 'steal' function in JavaScriptMVC
 * base on the domain of HLoader .
 */
$.steal=function(){
	var domain=window.location.protocol+"//"+window.location.host;
	var relatePath=window.location.pathname.substr(0,window.location.pathname.lastIndexOf("/")+1);
	var call="steal.call(this,";
	for(var i in arguments){
		if($.isString(arguments[i])){
			if(arguments[i].indexOf("/")==0){
				arguments[i]=domain+arguments[i];
			}else if(arguments[i].indexOf("..")==0){
				arguments[i]=domain+relatePath.sub(0,relatePath.lastIndexOf("/")+1)+arguments[i];
			}else{
				arguments[i]=domain+relatePath+arguments[i];
			}
		}
		call+="arguments["+i+"],"
	}
	call=call.substr(0,call.length-1);
	call+=");"
	eval(call);
	//steal.call(this,arguments);
}

/**
 * Rendering a string tpl to complete string, there are one or more varibles in the tpl string with the format '${VARIBLE}',
 * The value of VARIBLE must be existed in the objt or whole runtime environment.
 * @param {String} val a string with name of varibles.
 * @param {Object} objt A object of javascript for replacing the ${VARIBLE} in the val;
 */
$.renderStringTpl = function(val, objt) {
	if(!val){
		return val;
	}
	var result = val.replace(/\$\{[^\}]*\}/g, function(varval) {
		var para = varval.substring(2, varval.length - 1);
		var vart = para.substring(0, para.indexOf("."));
		if(vart &&vart!="$"&& objt && !vart.indexOf("$") > -1) {
			eval("var " + vart + "={};");
			eval("$.extend(true," + vart + ", objt);");
			return eval(varval.substring(2, varval.length - 1));
		} else if(vart.startsWith("$")) {
			return eval(varval.substring(2, varval.length - 1));
		}
		return varval;
	});
	return result;
}
//判断是否为字符串
$.isString = function(str) {
	return(typeof str == 'string') && str.constructor == String;
}
/**
 * 根据给定的条件查询在对象数组中匹配到的对象。
 * @param {Object} array 数组对象
 * @param {Object} condition 查询条件格式为：字段名 = 字段值 and 字段名=字段值 ...
 * @param {Object} start 默认为0，无需传递
 */
$.objArrayQuery = function(array, condition, start) {
	if(condition == undefined || $.trim(condition) == "") {
		return array;
	}
	var result = new Array();
	conditions = condition.split(" ");
	var con = $.arraySearch(conditions, "and");

	function executeCondition(obj, filed, comSign, variable) {
		if(comSign == "eq") {
			return(obj[filed] == variable);
		} else if(comSign == "gt") {
			return(obj[filed] > variable);
		} else if(comSign == "lt") {
			return(obj[filed] < variable);
		} else {
			console.error("comSign must be one in eq,gt,lt")
		}
	}
	if(!start) {
		start = 0;
	}
	for(var i = start; i < con.length; i++) {
		for(var j in array) {
			if(i == con.length - 1 && conditions.length > 3) {
				if(executeCondition(array[j], conditions[con[i] - 3], conditions[con[i] - 2], conditions[con[i] - 1]) && executeCondition(array[j], conditions[con[i] + 1], conditions[con[i] + 2], conditions[con[i] + 3])) {
					result.push(array[j])
				}
			} else {
				if(executeCondition(array[j], conditions[con[i] - 3], conditions[con[i] - 2], conditions[con[i] - 1])) {
					result.push(array[j])
				}
			}
		}
		if(i != con.length - 1) {
			result = $.objArrayQuery(result, condition, i + 1);
		}
		return result;
	}
}
/**
 * search multi results in the array.
 * @param {Object} array source array.
 * @param {Object} val a key for searching
 */
$.arraySearch = function(array, val) {
	var result = new Array();
	for(var i in array) {
		if(array[i] == val) {
			result.push(parseInt(i));
		}
	}
	if(result.length < 1) {
		result.push(3);
	}
	return result;
}
//将字符串模块进行渲染成字符串
//功能权限按钮的功能
$.urlToObject = function(url) {
	var strToObj = function(obj, so, val) {
		if(so.indexOf(".") > 0) {
			var item = so.substr(0, so.indexOf("."));
			if(!obj[item]) {
				obj[item] = {};
			};
			strToObj(obj[item], so.substr(so.indexOf(".") + 1), val);
		} else {
			obj[so] = val;
		}
	}
	var urlObject = {};
	if(/\?/.test(url)) {
		var urlString = decodeURI(url).substring(url.indexOf("?") + 1);
		var urlArray = urlString.split("&");
		for(var i = 0, len = urlArray.length; i < len; i++) {
			var urlItem = urlArray[i];
			var item = urlItem.split("=");
			if(item.length > 2) {
				item[1] = urlItem.substr(urlItem.indexOf("=") + 1);
			}
			strToObj(urlObject, item[0], item[1]);
		}

	}
	return urlObject;
};

/**judging if the environment is wechat.*/
$.isWeiXin=function() {
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger') {
			return true;
		} else {
			return false;
		}
}
//return true if obj is not a undefined or empty string or null, else the false will be returned
$.isNull=function(obj){
	return obj==undefined||obj==''||obj==null||obj=='undefined';
}
