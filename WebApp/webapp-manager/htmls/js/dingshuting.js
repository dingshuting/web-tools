$(function(){
	//初始化综合数据
	$.requestPara=$.urlToObject(window.location.href)
	var el=$("#zonghe");
	if(el.length>0){
		//从元素标签中获取type类型获取到数据
		$(el).showLoading({convas:true});
		var url=contentURL+"/keshihua/wuliuzonghe/"+el.attr("data-type");
		if($.requestPara['scId']){
			url+="?scId="+$.requestPara['scId'];
		}
		$.getJSON(url,function(data){
	    	if(data.code=="200"){
	    		if(typeof data.data=="string"){
	    			data.data=$.parseJSON(data.data);
	    		}
	    		$("#zonghe [name='amount']").html(data.data[0][0]==null?0:data.data[0][0]);
	    		$("#zonghe [name='total']").html(data.data[0][1]==null?0:data.data[0][1]);
	    		$("#zonghe [name='zitiADT']").html(data.data[0][2]==null?0:data.data[0][2].toFixed(2));
	    		$("#zonghe [name='paijianADT']").html(data.data[0][3]==null?0.00:data.data[0][3].toFixed(2));
	    		$(el).hideLoading();
	    	}
	    });
    }
	//加载所有折线图
    $(".lineChart").each(function(){
    	loadLineChart($(this),$(this).attr("data-default-type"));
    })
    
    $(".chart-act").click(function(){
    	var el=$($(this).attr("data-container"));
    	$(".chart-act").css("color","#fff");
    	$(this).css("color","#1ab394")
    	loadLineChart(el,$(this).attr("data-type"));
    	el.attr("data-legend",$(this).attr("data-legend"))
    })
    if($("#sc_citys").length>0){
	    $.post(contentURL+contentPath+"/scList",function(list){
	    	for(i in list){
	    		$("#sc_citys").append("<option value='"+list[i].id+"'>"+list[i].name+"</option>");
	    	}
	    	$("#sc_citys").change(function(){
	    		window.location.href="index_servercenter1.html?scId="+$(this).val();
	    	});
	    });
    }
    if($("#sc_title").length>0&&$.requestPara['scId']){
    	$.post(contentURL+"/mdyn/service_center/info/"+$.requestPara['scId'],function(data){
    		$("#sc_title").html(data.name);
    	})
    }
    if($.requestPara['scId']){
    	$("#fanhuiCity").show();
	    $("#fanhuiCity").click(function(){
	    	window.location.href="../htmls/index_city.html"
	    })
    }
   
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
