/**some utils, all of extended functions that extended native function or object should be here*/

String.prototype.endWith = function(str) {
	if(str == null || str == "" || this.length == 0 || str.length > this.length)   return false;
	if(this.substring(this.length - str.length) == str)   return true;
	else   return false;
	return true;
}
String.prototype.startWith=function(str){
	if(str == null || str == "" || this.length == 0 || str.length > this.length)   return false;
	if(this.substring(0,str.length) == str)   return true;
	else return false;
}
/**
 *  当字符变量超长时则自动以...代替
 * @param {Number} length- 显示的字符串长度,不包含...
 * @param {String} postion- ...显示的位置,默认right及最后,left/center/right
 */
String.prototype.subStrWithDotted=function(length,postion){
	if(!postion){
		postion="right";
	}
	if(length<3||this.length<3){
		return this;
	}else{
		if(this.length<length){
			return this;
		}
		if(postion=="left"){
			return "..."+this.substring(this.length-length,this.length);	
		}else if(postion=="center"){
			return this.substring(0,length/2)+"..."+this.substring(this.length-length/2,this.length);
		}else if(postion=="right"){
			return this.substring(0,length)+"...";
		}
	}
	
}
/**
 * 时间格式化扩展功能,年份默认为fullyear
 * @param {Object} fmt 格式字符串默认为yyyy-MM-dd hh:mm:ss
 */
Date.prototype.Format = function(fmt) {
	if(!fmt){
		fmt="yyyy-MM-dd hh:mm:ss"
	}
	var o = {
		"M+": this.getMonth() + 1, //月份 
		"d+": this.getDate(), //日 
		"h+": this.getHours(), //小时 
		"m+": this.getMinutes(), //分 
		"s+": this.getSeconds(), //秒 
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度 
		"S": this.getMilliseconds() //毫秒 
	};
	if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
};