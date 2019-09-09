$(function(){
	//初始化综合数据
	//从元素标签中获取type类型获取到数据
	$("#zonghe").showLoading({convas:true});
	var url=contentURL+"/keshihua/wuliuzonghe/10";
	$.getJSON(url,function(data){
    	if(data.code=="200"){
    		if(typeof data.data=="string"){
    			data.data=$.parseJSON(data.data);
    		}
    		$("#amount").html(data.data[0][0]==null?0:data.data[0][0]);
    		$("#total").html(data.data[0][1]==null?0:data.data[0][1]);
    		$("#zitiADT").html(data.data[0][2]==null?0:data.data[0][2].toFixed(2));
    		$("#paijianADT").html(data.data[0][3]==null?0.00:data.data[0][3].toFixed(2));
    		$("#zonghe").hideLoading();
    	}
    });
})
function loadLineChart(el,type){
	var url=contentURL+contentPath+"/"+$(el).attr("data-url")+"/"+type;
	if($.requestPara['scId']){
		url+="?scId="+$.requestPara['scId'];
	}
	(function(el){
		$(el).showLoading();
		$.getJSON(url,function(result){
			$(el).hideLoading();
			if(typeof result.data=="string"){
	    			result.data=$.parseJSON(result.data);
	    		}
        	if(result.code=="200"){
        		var chartData=rowToColsForChart(result.data,parseInt(el.attr("data-legend")),parseInt(el.attr("data-xAxis")));
        		var option=undefined;
        		if(el.attr("data-option")&&window[el.attr("data-option")]){
        			option=window[el.attr("data-option")]();
        		}
        		drawLineChart(el,option,chartData,parseInt(el.attr("data-val")));
        	}else{
        		console.error(result);
        	}
		});
	}(el));
}
function baoheduOption(){
	return {grid: {
            left: '3%',
            right: '7%',
            bottom: '3%',
            containLabel: true
        },};
}
