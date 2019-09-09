var typeval = 1;
$(function() {

	shouruzzlandmubiaodcl(1);

	// 2.2.4服务中心收入排名(登录人所在城市所有服务中心)
	// type   类型 1.城市 2.服务中心
	//  orderBy    排序字段 4.累计 3.本年 2.本季度 1.本月
	//  orderByType     排序顺序 1.顺序 2.倒序
	shourupaihang(1, 2, 1, 2);
	// 2.2.6服务中心快件量排名(登录人所在城市所有服务中心)
	// type    类型 1.城市 2.服务中心
	//  orderBy    排序字段 4.累计 3.本年 2.本季度 1.本月
	//  orderByType  排序顺序 1.顺序 2.倒序
	kuaijianliangpaihang(1, 2, 1, 2);
	// 2.2.5本月目标达成率
	percentGoalMetBySc(1);
})

function shouruzzlandmubiaodcl(pn) {
	$("#main1syzzl").showLoading();
	$.post(contentURL + "/keshihua/shouruzzlandmubiaodclByCity/", function(data) {
		$("#main1syzzl").hideLoading();
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
					xdata.push(data.data[i][0]);
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
			}
		}
	})
}

function shetChartShouruzzlandmubiaodcl(xdata, srzzl, mbdcl) {
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main1syzzl'));

	// 指定图表的配置项和数据
	var option = {
		title: {
			text: '',
			textStyle: {
				color: '#fff',
				fontSize: 15
			},
			x: 'left'
		},
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c}%"
		},
		color: ['#FCCE10', '#E87C25', '#27727B',
			'#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
			'#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'
		],
		legend: {
			data: ['收入增长率', '目标达成率', ],
			x: 'right',
			textStyle: {
				color: '#fff'
			}
		},
		grid: {
			left: '3%',
			right: '6%',
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
			data: xdata,
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
		series: [{
				name: '收入增长率',
				type: 'line',
				data: srzzl
			},
			{
				name: '目标达成率',
				type: 'line',
				data: mbdcl
			},

		]
	};

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}

function shourupaihang(pn, type, orderBy, orderByType) {

	$("#srphTableBycityDiv").showLoading();
	$.post(contentURL + "/keshihua/shourupaimingByCity/1", {
		'type': type,
		'orderBy': orderBy,
		'orderByType': orderByType
	}, function(data) {
		$("#srphTableBycityDiv").hideLoading();
		if(data.code == "200") {
			if(typeof data.data == "string") {
				data.data = $.parseJSON(data.data);
			}
			if(data.data.length > 0) {
				$("#srphTableBycity").empty("");
				for(var i = 0; i < data.data.length; i++) {
					var a = data.data[i];
					var a = data.data[i][2];
					$("#srphTableBycity").append('<tr><td width="10%">' + (i + 1) + '</td><td width="30%">' + data.data[i][2] + '</td><td width="15%">' + data.data[i][6] + '</td><td width="15%">' + data.data[i][5] + '</td><td width="15%">' + data.data[i][4] + '</td><td width="15%">' + data.data[i][3] + '</td></tr>');

				}
			}
			startScorll($("#marquee9"));
		}
	})
}

function kuaijianliangpaihang(pn, type, orderBy, orderByType) {

	$("#kuaijiangliangpaimingTableDiv").showLoading();
	$.post(contentURL + "/keshihua/kuaijianpaimingByCity/", {
		'type': type,
		'orderBy': orderBy,
		'orderByType': orderByType
	}, function(data) {
		$("#kuaijiangliangpaimingTableDiv").hideLoading();
		if(data.code == "200") {
			if(typeof data.data == "string") {
				data.data = $.parseJSON(data.data);
			}
			if(data.data.length > 0) {
				$("#kuaijiangliangpaimingTable").empty("");
				var kuaijiangliangpaimingTable = $("#kuaijiangliangpaimingTable");
				for(var i = 0; i < data.data.length; i++) {
					var a = data.data[i];
					var a = data.data[i][2];
					kuaijiangliangpaimingTable.append('<tr><td width="10%">' + (i + 1) + '</td><td width="30%">' + data.data[i][2] + '</td><td width="15%">' + data.data[i][6] + '</td><td width="15%">' + data.data[i][5] + '</td><td width="15%">' + data.data[i][4] + '</td><td width="15%">' + data.data[i][3] + '</td></tr>');

				}
				startScorll($("#marquee6"));
			}
		}
	})
}

function percentGoalMetBySc(type) {

	$("#bymbdclvTable").showLoading();
	$.post(contentURL + "/keshihua/percentGoalMetBySc/" + type + "",
		function(data) {
			$("#bymbdclvTable").hideLoading();
			if(data.code == "200") {
				if(typeof data.data == "string") {
					data.data = $.parseJSON(data.data);
				}
				var ztmbdclavg = 0;
				if(data.data.length > 0) {
					$("#bymbdclvTable").empty("");

					var bymbdclvTable = $("#bymbdclvTable");
					for(var i = 0; i < data.data.length; i++) {
						ztmbdclavg = ztmbdclavg + data.data[i][3];
						bymbdclvTable.append('<tr><td width="10%" >' + (i + 1) + '</td><td width="30%">' + data.data[i][2] + '</td><td width="25%"><span class="xiaozi">实际:' + data.data[i][0] + '/目标:' + data.data[i][1] + '</span></td> <td><div class="bj1"><div class="bj2" style="width:' + (data.data[i][3]).toFixed(2) + '%;color:#000">' + (data.data[i][3]).toFixed(2) + '%</div></div></td></tr>');
					}
				} else {
					$("#nonemubiao").show();
				}
				if(data.data.length > 0) {
					$("#ztmbdclavg").html((parseFloat(ztmbdclavg) / parseFloat(data.data.length)).toFixed(2));
				} else {
					$("#ztmbdclavg").html(0.00);
				}
				startScorll($("#marquee11"));
			}
		})
}

$("span[name='aaa']").click(function() {
	var id = this.id;
	$(this).toggleClass("arrow3");
	var arrow = this.className;
	var orderBy = 0;
	if(id == 'mmmm') {
		$("#jjjj").removeClass("arrow3");
		$("#yyyy").removeClass("arrow3");
		$("#sums").removeClass("arrow3");
		orderBy = 1;
	} else if(id == 'jjjj') {
		$("#mmmm").removeClass("arrow3");
		$("#yyyy").removeClass("arrow3");
		$("#sums").removeClass("arrow3");
		orderBy = 2;
	} else if(id == 'yyyy') {
		$("#mmmm").removeClass("arrow3");
		$("#jjjj").removeClass("arrow3");
		$("#sums").removeClass("arrow3");
		orderBy = 3;
	} else {
		$("#mmmm").removeClass("arrow3");
		$("#jjjj").removeClass("arrow3");
		$("#yyyy").removeClass("arrow3");
		orderBy = 4;
	}

	if(arrow != "arrow2 arrow3") {
		shourupaihang(1, 2, orderBy, 1);
	} else if(arrow == "arrow2 arrow3") {
		shourupaihang(1, 2, orderBy, 2);
	}

});

$("span[name='bbb']").click(function() {
	var id = this.id;
	$(this).toggleClass("arrow3");
	var arrow = this.className;
	var orderBy = 0;
	if(id == 'mmmmkj') {
		$("#jjjjkj").removeClass("arrow3");
		$("#yyyykj").removeClass("arrow3");
		$("#sumskj").removeClass("arrow3");
		orderBy = 1;
	} else if(id == 'jjjjkj') {
		$("#mmmmkj").removeClass("arrow3");
		$("#yyyykj").removeClass("arrow3");
		$("#sumskj").removeClass("arrow3");
		orderBy = 2;
	} else if(id == 'yyyykj') {
		$("#mmmmkj").removeClass("arrow3");
		$("#jjjjkj").removeClass("arrow3");
		$("#sumskj").removeClass("arrow3");
		orderBy = 3;
	} else {
		$("#mmmmkj").removeClass("arrow3");
		$("#jjjjkj").removeClass("arrow3");
		$("#yyyykj").removeClass("arrow3");
		orderBy = 4;
	}

	if(arrow != "arrow2 arrow3") {
		kuaijianliangpaihang(1, 2, orderBy, 1);
	} else if(arrow == "arrow2 arrow3") {
		kuaijianliangpaihang(1, 2, orderBy, 2);
	}

});