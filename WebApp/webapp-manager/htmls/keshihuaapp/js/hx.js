var typeval = 1;
var kjphtypeval = 1;

$(function() {
	/**
	 *2.1.3收入增长率-目标达成率分析/2.2.3收入增长率-目标达成率分析
	 * */
	shouruzzlandmubiaodcl(1);

	// 2.1.4收入排名
	// type   类型 1.城市 2.服务中心
	//  orderBy    排序字段 4.累计 3.本年 2.本季度 1.本月
	//  orderByType     排序顺序 1.顺序 2.倒序
	shourupaihang(1, 2, 1, 2);
	// 2.1.4收入排名
	// type    类型 1.城市 2.服务中心
	//  orderBy    排序字段 4.累计 3.本年 2.本季度 1.本月
	//  orderByType  排序顺序 1.顺序 2.倒序
	//kuaijianliangpaihang(1, 1, 1, 2);
})

function shouruzzlandmubiaodcl(pn) {
	$("#main1").showLoading();
	$.post(contentURL + "/keshihua/shouruzzlandmubiaodcl/1", function(data) {
		$("#main1").hideLoading();
		if(data.code == "200") {
			if(typeof data.data == "string") {
				data.data = $.parseJSON(data.data);
			}
			if(data.data.length > 0) {
				var xdata = new Array(); //x轴
				var srzzl = new Array(); //收入增长率
				var benqishoury = new Array(); //本期收入
				var shangqishoury = new Array(); //上期收入
				var mbdcl = new Array(); //目标达成率
				for(var i = 1; i < data.data.length; i++) {
					var mm = data.data[i][0].substring(data.data[i][0].length - 2);
					if(mm >= 10) {
						xdata.push(mm + "月");
					} else {
						xdata.push(mm.substring(mm.length - 1) + "月");
					}
					benqishoury.push(((data.data[i][1] * 1).toFixed(2)) + '');
					shangqishoury.push(((data.data[i - 1][1] * 1).toFixed(2)) + '');
					var benqival = ((data.data[i][1] * 1).toFixed(2));
					var shangqival = ((data.data[i - 1][1] * 1).toFixed(2));
					if(shangqival == 0) {
						srzzl.push(0.00);
					} else {
						srzzl.push(((parseFloat(benqival) - parseFloat(shangqival)) / parseFloat(shangqival) * 100).toFixed(2));
					}
					mbdcl.push(((data.data[i][2] * 1).toFixed(2)) + '');

				}
				shetChartShouruzzlandmubiaodcl(xdata, srzzl, mbdcl);
				shetChartShouruzzlandmubiaodclTable(data);
				jzbysrdcl(mbdcl);
				srdcl(xdata, benqishoury, mbdcl);
			}
		}
	})
}

function shetChartShouruzzlandmubiaodcl(xdata, srzzl, mbdcl) {
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main1'));
	// 指定图表的配置项和数据
	var option = {
		title: {
			text: '',
			textStyle: {
				color: '#c3c1cc',
				fontSize: 36
			},
		},
		grid: {
			top: '1%', //距上边距

			left: '5%', //距离左边距

			right: '5%', //距离右边距

		},
		tooltip: {
			trigger: 'axis',
			textStyle: {
				color: '#c3c1cc',
				fontSize: 30,

			},
			formatter: "{a} <br/>{b} : {c}%"
		},
		color: ['#31d6c5', '#31d6c5', '#fe8081', '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
			'#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'
		],
		legend: {
			data: ['收入增长率'],
			textStyle: {
				color: '#c3c1cc',
				fontSize: 30,

			},
		},
		grid: {
			left: '3%',
			right: '4%',
			top: '5%',
			containLabel: true,

		},
		toolbox: {
			feature: {
				saveAsImage: {}
			},

		},
		xAxis: {
			show: true,
			type: 'category',
			boundaryGap: false,
			data: xdata,
			axisLine: {

				lineStyle: {
					color: '#c3c1cc',
					width: 3,
				}
			},
			axisLabel: {
				show: true,
				textStyle: {
					color: '#c3c1cc',
					fontSize: 32
				},
			},
		},
		yAxis: {
			type: 'value',
			axisLabel: {
				show: true,
				interval: 'auto',
				textStyle: {
					color: '#c3c1cc',
					fontSize: 32
				},
				formatter: '{value} %'
			},
			
			axisLine: {
				show: true,
				lineStyle: {
					color: '#c3c1cc',
					width: 3,
				}
			},
		},
		series: [{
				name:'',
				type: 'line',
				data: srzzl,
				itemStyle: {
					normal: {
						lineStyle: {
							width: 5, //折线宽度
						}
					}
				},
			},

		]
	};
	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);

}

function shetChartShouruzzlandmubiaodclTable(data) {
	if(data.code == "200") {
		if(typeof data.data == "string") {
			data.data = $.parseJSON(data.data);
		}
		if(data.data.length > 0) {
			var xdata = new Array(); //月份
			var benqishoury = new Array(); //本期收入
			var huanbifudu = new Array(); //环比幅度
			var shangqishoury = new Array(); //上期收入
			var jiantouArr = new Array(); //箭头
			var shouruzzlandmubiaodcl = $("#shouruzzlandmubiaodclTable");
			for(var i =  data.data.length-1; i > 1; i--) {
				var mm = data.data[i][0].substring(data.data[i][0].length - 2);
				if(mm >= 10) {
					xdata.push(mm + "月");
				} else {
					xdata.push(mm.substring(mm.length - 1) + "月");
				}
				benqishoury.push(((data.data[i][1] * 1).toFixed(2)) + '');
				shangqishoury.push(((data.data[i - 1][1] * 1).toFixed(2)) + '');
				var benqival = ((data.data[i][1] * 1).toFixed(2));
				var shangqival = ((data.data[i - 1][1] * 1).toFixed(2));
				if(shangqival == 0) {
					huanbifudu.push(0.00);
				} else {
					huanbifudu.push(((parseFloat(benqival) - parseFloat(shangqival)) / parseFloat(shangqival) * 100).toFixed(2));
				}
				if((parseFloat(benqival) - parseFloat(shangqival)) > 0) {
					jiantouArr.push(1);
				} else if((parseFloat(benqival) - parseFloat(shangqival)) == 0) {
					jiantouArr.push(0);
				} else {
					jiantouArr.push(-1);
				}
			}
			for(var j = 0; j < xdata.length; j++) {
				var jiantou = "";
				if(jiantouArr[j] == 0) {
					jiantou = ''
				} else if(jiantouArr[j] == 1) {
					jiantou = '<img src="img/shangjiantou.png" height="30">'
				} else {
					jiantou = '<img src="img/xiajiantou.png" height="30">'
				}

				shouruzzlandmubiaodcl.append(' <tr><td width="50%" height="120" style="padding-left:20px;">' + xdata[j] + '收入:' + benqishoury[j] + '</td><td width="35%">环比幅度:' + huanbifudu[j] + '%</td> <td width="15%">' + jiantou + '</td></tr>');
			}
		}
	}
}

function jzbysrdcl(mbdcl) {
	var myChart = echarts.init(document.getElementById('myChart'));
	var option = {

		tooltip: {
			formatter: "{a} <br/>{b} : {c}%"
		},
		toolbox: {
			feature: {
				restore: {},
				saveAsImage: {}
			}
		},
		axisLabel: {
			show: false,
			textStyle: {
				color: '#fff',
				fontSize: 37,
			}
		},
		series: [{

			type: 'gauge',
			detail: {
				formatter: '{value}%',
				backgroundColor: 'rgba(30,144,255,0.8)',
				textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
					fontWeight: 'bolder',
					color: '#fff',
					fontSize: 56,
				},
			},
			axisLine: { // 坐标轴线
				lineStyle: { // 属性lineStyle控制线条样式
					color: [
						[0.2, '#ffc65b'],
						[0.8, '#31d6c5'],
						[1, '#fe8081']
					]
				}
			},
			axisLabel: { // 刻度标签。
				show: true, // 是否显示标签,默认 true。
				distance: 5, // 标签与刻度线的距离,默认 5。
				color: "#fff", // 文字的颜色,默认 #fff。
				fontSize: 28, // 文字的字体大小,默认 5。
				formatter: "{value}", // 刻度标签的内容格式器，支持字符串模板和回调函数两种形式。 示例:// 使用字符串模板，模板变量为刻度默认标签 {value},如:formatter: '{value} kg'; // 使用函数模板，函数参数分别为刻度数值,如formatter: function (value) {return value + 'km/h';}
			},

			data: [{
				value: mbdcl[mbdcl.length - 1],
			}],

		}]
	};

	setInterval(function() {
		myChart.setOption(option, true);
	}, 2000);
}

var datayue=null;
var datasum=null;
function shourupaihang(pn, type, orderBy, orderByType) {
	if(orderBy == 1) {
		$("#yue").addClass('baise lvse');
		$("#sum").removeClass('lvse')
        if(datayue!=null){
            shourupaihangSetData(datayue,type, orderBy, orderByType);
		    return;
        }
	} else {
		$("#yue").removeClass('lvse');
		$("#sum").addClass('baise lvse')
        if(datasum!=null){
            shourupaihangSetData(datasum,type, orderBy, orderByType);
            return;
        }
	}
	$("#srphTableDiv").showLoading();
	$.post(contentURL + "/keshihua/shourupaiming/1", {
		'type': 2,
		'orderBy': orderBy,
		'orderByType': orderByType
	}, function(data) {
		$("#srphTableDiv").hideLoading();
		if(data.code == "200") {
			if(typeof data.data == "string") {
				data.data = $.parseJSON(data.data);
			}
			if(data.data.length > 0) {
                if(orderBy == 1) { //月
                    datayue=data;
                } else {
                    datasum=data;
                }
                shourupaihangSetData(data,type, orderBy, orderByType);
			}
		}
	})
}
function shourupaihangSetData(data,type, orderBy, orderByType) {
    $("#srphTable").empty("");
    var srphTable = $("#srphTable");
    var fristData = 0;
    var number = 0;
    for(var i = 0; i < data.data.length; i++) {
        if(i == 10) {
            return;
        }
        var obj = data.data[i];
        if(orderBy == 1) { //月
            number = obj[6];

        } else {
            number = obj[3];

        }
        var tr = '';
        if(i == 0) {
            fristData = number;
            tr = ' <tr><td width="40%" style="padding-left:20px;">' + obj[2] + '</td>' +
                '   <td width="40%">' +
                '   <div class="bj1">' +
                '    <div class="bj2" style="width:100%;color:#fff;"></div>' +
                '    </div>' +
                '  </td>' +
                '  <td width="20%" style="padding-left:20px;">' + number + '</td>' +
                ' </tr>';
        } else {
            tr = ' <tr><td width="40%" style="padding-left:20px;">' + obj[2] + '</td>' +
                '   <td width="40%">' +
                '   <div class="bj1">' +
                '    <div class="bj2" style="width:' + (number / fristData).toFixed(2) * 100 + '%;color:#fff;"></div>' +
                '    </div>' +
                '  </td>' +
                '  <td width="20%" style="padding-left:20px;">' + number + '</td>' +
                ' </tr>';
        }
        srphTable.append(tr);

    }
}
function srdcl(xdata, benqishoury, mbdcl) {
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main4'));

	// 指定图表的配置项和数据
	var option = {
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'cross',
				crossStyle: {
					color: '#999'
				}
			},
			textStyle: {
				color: '#afa9ff',
				fontSize: 40,
			},
            formatter: function (param) {
                if(param.length==1) {
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='收入达成率'?'%':"") ;

                }else if(param.length==2){

                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='收入达成率'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='收入达成率'?'%':"") ;

                }else if(param.length==3){
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='收入达成率'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='收入达成率'?'%':"") +
                        "<br>" + param[2].seriesName + ":" + param[2].data + (param[2].seriesName=='收入达成率'?'%':"");
                }
            }
		},
		legend: {
			data: ['实际服务费', '收入达成率'],
			textStyle: {
				color: '#c3c1cc',
				fontSize: 30,

			},
		},
		grid: {
			left: '3%',
			right: '3%',
			containLabel: true
		},
		color: ['#ffc65b', '#31d6c5', '#fe8081', '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
			'#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'
		],
		xAxis: [{
			type: 'category',
			data: xdata,
			axisPointer: {
				type: 'shadow'
			},
			axisLabel: {
				show: true,
				textStyle: {
					color: '#c3c1cc',
					fontSize: 32
				},
			},
			axisLine: {

				lineStyle: {
					color: '#c3c1cc',
					width: 3,
				}
			},
		}],
		yAxis: [{
				type: 'value',

				axisLabel: {
					formatter: '{value} ml'
				},
				axisLabel: {
					show: false,
					interval: 'auto',
					textStyle: {
						color: '#c3c1cc',
						fontSize: 32
					},
				},
				splitLine: {
					show: false,
					lineStyle: {
						//设置刻度线粗度(粗的宽度)
						width: 3,
						//颜色数组，数组数量要比刻度线数量大才能不循环使用
						color: ['rgba(0, 0, 0, 0)', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9']
					}
				},
				axisLine: {

					lineStyle: {
						color: '#c3c1cc',
						width: 3,
					}
				},
			},

			{
				type: 'value',
				position:'left',
				axisLabel: {
					show: true,
					interval: 'auto',
					textStyle: {
						color: '#c3c1cc',
						fontSize: 32
					},
					formatter: '{value} %'
				},
				splitLine: {
					show: true,
					lineStyle: {
						//设置刻度线粗度(粗的宽度)
						width: 3,
						//颜色数组，数组数量要比刻度线数量大才能不循环使用
						color: ['rgba(0, 0, 0, 0)', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9', '#EDF9F9']
					}
				},
				axisLine: {

					lineStyle: {
						color: '#c3c1cc',
						width: 3,
					}
				},
			},

		],
		series: [
			{
				name: '收入达成率',
				type: 'line',
				yAxisIndex: 1,
				data: mbdcl,
				itemStyle: {
					normal: {
						lineStyle: {
							width: 5, //折线宽度
						}
					}
				},
				axisLabel: {
					show: true,
					textStyle: {
						color: '#c3c1cc',
						fontSize: 32
					},
				},
			},
			{
				name: '实际服务费',
				type: 'bar',
				barWidth: 50,
				data: benqishoury,
				itemStyle: {
					normal: {
						lineStyle: {
							width: 3, //折线宽度
						}
					}
				},
			},

		]
	};

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
	var srdclTable = $("#srdclTable");
	for(var j = xdata.length - 1; j > 0; j--) {

		srdclTable.append(' <tr><td width="50%" height="120" style="padding-left:20px;">' + xdata[j] + '实际服务费:' + benqishoury[j] + '</td><td width="15%"></td><td width="40%" style="text-align: left">达成率:' + mbdcl[j] + '%</td></tr>');
	}
}