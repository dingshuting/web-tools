APP_CONFIG = {
	domain: "http://127.0.0.1:9001",
	contextPath:"",
	logoutUrl:"/boda_security_logout",
	authUrl:"auth.html",
	template:"cy_xms"
}
/**
 * 自定义表格过滤的js,
 */
/**
 * 预警时长超过3天的，在预警时长字段的内容标红显示
 */
function updateColor(headElement, $val, $col,rowdata) {
    if (headElement.colCode == 'deliyDays'){
        var day = $val.split('天')[0];
        if (day >= 3){
            $col.css('color','red');
        }
        return new $.TableControl()._defaultDataFilter(headElement, $val, $col,rowdata);
    }else{
        return $val;
    }
}
/**
 * 格式化项目日报的日期显示,返回yyyy-MM-dd，本月达成比率拼接百分号
 */
function formatDay(headElement, $val, $col,rowdata) {
    if (headElement.colCode == 'createTime'){
        return $val.split(' ')[0];
    }else if (headElement.colCode == 'completeRatioMonth' && '' != $val){
        return $val+'%';
    }else{
        return $val;
    }
}
function formatAddress(headElement,$val,$col,rowdata){
	 if (headElement.colCode == 'address'){
	 	$val=rowdata.province+rowdata.city+rowdata.regionId+$val;
	 	return $val;
	 }else{
	 	return $val;
	 }
}
/**
 * 设置查询条件中的服务中心默认值为当前登录用户的服务中心
 */
function bindServiceCenter(){
    var user = $.session.currentUser;
    if (user.owner != '100000'){
        $('#'+$.session.currFun.id).find('div[name="scId"]').find('input[type="text"]').val(user.identity.name)
    }
}

/**
 * 如果当前用户不是管理员，则删除服务中心的点击事件
 * @param $input
 * @param input
 */
function refHander($input, input) {
    var user = $.session.currentUser;
    if ((!user.owner||user.owner==''||user.owner == '100000')&& (input.colCode == 'scId'||input.colCode == 'serviceCenterId')){
    	$.FormControl.defaultRefHander($input, input);
    }else if(input.colCode == 'scId'||input.colCode == 'serviceCenterId'){
        //$.FormControl.defaultRefHander($input, input);
        alert('无权使用');
    }else{
    	$.FormControl.defaultRefHander($input, input);
    }
}