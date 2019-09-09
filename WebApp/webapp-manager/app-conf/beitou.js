//应用基本配置
APP_CONFIG = {
	domain: "http://127.0.0.1:8090",
	contextPath:"/beitou",
	logoutUrl:"/logout",
	template:"white",
	togoActions:{
		testAdd:function(){
			
		}
	},//自定义togo函数集合
	callBack:{
		listFinish:function(){
			
		}
	},
	RefHander:{
		showCols:["companyId","parentCompanyId"]
	}
}
APP_CONFIG.authUrl=APP_CONFIG.domain+"/index?redirectUrl="+window.location.href;
CallBack={
	
}
function addShareLink(headElement, $val, $col,rowdata) {
    if (headElement.colCode == 'parentCompanyId'||headElement.colCode=='companyId'||headElement.colCode=='name'){
    	$col.click(function(){
    		$(this).parent().find(".lt-opt li a").each(function(){
    			if($(this).text()=="详情"){
    				$(this).trigger("click");
    			}
    		});
    	});
        $col.css('cursor','pointer');
        return new $.TableControl()._defaultDataFilter(headElement, $val, $col,rowdata);
    }else{
        return $val;
    }
}
function percentageFormat(headElement, $val, $col,rowdata){
	var formatCols=['liabilitiesRate','assetBenefitRate','totalAssetRwardRate','sellNetProfitRate','sellIncreasRate'
	,'netBenefitIncreaseRate','benefitReportRate','resotreBenefitIncreasReate','resotreBenefitIncreasTotalReate'];
	if(formatCols.indexOf(headElement.colCode)>-1){
		return (parseFloat($val)*100).toFixed(2);
	}else{
		return $val;
	}
}
function ownDataFilter(headElement, $val, $col,rowdata){
	addShareLink(headElement, $val, $col,rowdata);
	return percentageFormat(headElement, $val, $col,rowdata);
}
