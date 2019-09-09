/**
 * 控制表格中排序用的上下箭头
 * @param el 箭头的元素
 * @param callback 回调
 */
function tableHandler(el,callback) {
    $(el).toggleClass ("arrow3");
    callback();
}
var contain=function(array,val){
	for(var i in array){
		if(array[i]==val){
			return true
		}
	}
	return false;
}
/**
 * 將数组按照图表的格式对应的数组，转换过程中会将无值的数据点进行零填充
 * @param {Object} objArray 数据查询结果
 * @param {Object} legend 数据总维度的索引
 * @param {Object} xAxis x轴维度索引
 * @return {Array} 返回[[],[],[],...],第一个为数据总维度的数据，第2个为x轴标识，后面按照索引顺序进行排列
 */
function rowToColsForChart(objArray,legend,xAxis){
	if(objArray.length<1)return;
	var result={};
	var keys1=[]
	var keys2=[]
	//初始化legend和xAxis
	for(var i in objArray){
		for(var j in objArray[i]){
			if(j==legend&&!contain(keys1,objArray[i][j])){
				keys1.push(objArray[i][j])
			}else if(j==xAxis&&!contain(keys2,objArray[i][j])){
				keys2.push(objArray[i][j])
			}
		}
	}
	//初始化结果对象
	for(var k1 in keys1){
		if(!result[keys1[k1]]){
			result[keys1[k1]]={}
		}
		for(var k2 in keys2){
			for(var j in objArray[0]){
				if(j!=legend&&j!=xAxis){
					result[keys1[k1]][j]=[]
				}
			}
		}
	}
	for(var k1 in keys1){
		for(var k2 in keys2){
			hasVal=false;
			for(var i in objArray){
				if(objArray[i][legend]==keys1[k1]&&objArray[i][xAxis]==keys2[k2]){
					$.each(result[keys1[k1]],function(k,v){
						v.push(objArray[i][k]);
						hasVal=true;
					});
				}
			}
			if(!hasVal){
				$.each(result[keys1[k1]],function(k,v){
					v.push(0);
				});
			}
		}
	}
	result.legend=keys1;
	result.xAxis=keys2;
	return result;
}
function drawLineChart(el,option,colData,colValIndex){
	if(!colData){
		el.append('<div style="color:#fff;font-size:18px;text-align:center;width:100%;line-height:200px;">暂无数据!</div>');
		return;
	}else{
	}
	(function(el,colData){
		var myChart=el.data("chart")
		if(!myChart){
			myChart = echarts.init(el[0]);
			el.data("chart",myChart)
			// 指定图表的配置项和数据
			// 使用刚指定的配置项和数据显示图表。
		}else{
			myChart.clear();
		}
		
		var foption = {
				title: {
					text: el.attr("title"),
					textStyle: {
						color: '#fff',
						fontSize: 15,
					},
					x: 'left'
				},
				tooltip: {
					trigger: 'item'
				},
				color:['#FCCE10','#E87C25','#27727B',
                           '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'],

				legend: {
					data: colData.legend,
					x: 'right',
					textStyle: {
							color: '#fff'
						}
				},
				grid: {
					left: '3%',
					right: '4%',
					bottom: '3%',
					containLabel: true
				},
				
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
				xAxis: {
					type: 'category',
					boundaryGap: false,
					data: colData.xAxis,
					axisLabel: {
						show: true,
						textStyle: {
							color: '#fff'
						}
					},
					axisLine: {
						lineStyle: {
							color: '#fff',
							width: 1,
						}
					}
				},
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        show: true,
                        interval: 'auto',
                        textStyle: {
                            color: '#fff'
                        },
                        formatter: '{value} %'
                    },
                    axisLine: {

                        lineStyle: {
                            color: '#fff',
                            width: 1,
                        }
                    },
                    show: true
                },
				series: []
			};
		$.each(colData,function(k,v){
				if(k!="legend"&&k!="xAxis"){
					serie={}
					serie.name=k;
					serie.type="line";
					serie.data=v[colValIndex];
					serie.tooltip={ formatter: function (param) {
							var tip=param.seriesName+"  "+param.name+"<br/>"
							if(LINE_CHART_EXTEND_DATA[el.attr("data-extend-name")]){
								$.each(LINE_CHART_EXTEND_DATA[el.attr("data-extend-name")], function(k,v) {
									if(!isNaN(parseInt(k))){
										tip+=v.name+":"+colData[param.seriesName][k][param.dataIndex];
										if(v.unit){
											tip+=v.unit;
										}
										tip+="<br/>";
									}else if($.isFunction(v)){
										if(v(el,colData,param)){
											tip+=v(el,colData,param);
										}
									}
								});
							}
							return tip;
						}
					}
					foption.series.push(serie);
				}
			})
			$.extend(true,foption,option);
		myChart.setOption(foption);
	}(el,colData));
}
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
						var urlString = decodeURIComponent(url.substring(url.indexOf("?") + 1));
						var urlArray = urlString.split("&");
						for(var i = 0, len = urlArray.length; i < len; i++) {
							var urlItem = urlArray[i];
							var item = urlItem.split("=");
							strToObj(urlObject, item[0], item[1]);
						}

					}
					return urlObject;
				};
