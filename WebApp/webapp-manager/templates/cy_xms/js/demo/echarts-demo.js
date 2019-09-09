$(function() {
	/*首页-经销商分布*/
	function randomData() {
		return Math.round(Math.random() * 1000);
	}
	var indexMapChart = echarts.init(document.getElementById("index-map-chart"));
	var indexMapOps = {

		tooltip: {
			trigger: 'item'
		},

		visualMap: {
			min: 0,
			max: 2500,
			left: 'left',
			top: 'top',
			text: ['高', '低'], // 文本，默认为数值文本
			calculable: true
		},

		series: [{
				name: '经销商数量',
				type: 'map',
				mapType: 'china',
				roam: false,
				label: {
					normal: {
						show: true
					},
					emphasis: {
						show: true
					}
				},
				data: [{
					name: '北京',
					value: randomData()
				}, {
					name: '天津',
					value: randomData()
				}, {
					name: '上海',
					value: randomData()
				}, {
					name: '重庆',
					value: randomData()
				}, {
					name: '河北',
					value: randomData()
				}, {
					name: '河南',
					value: randomData()
				}, {
					name: '云南',
					value: randomData()
				}, {
					name: '辽宁',
					value: randomData()
				}, {
					name: '黑龙江',
					value: randomData()
				}, {
					name: '湖南',
					value: randomData()
				}, {
					name: '安徽',
					value: randomData()
				}, {
					name: '山东',
					value: randomData()
				}, {
					name: '新疆',
					value: randomData()
				}, {
					name: '江苏',
					value: randomData()
				}, {
					name: '浙江',
					value: randomData()
				}, {
					name: '江西',
					value: randomData()
				}, {
					name: '湖北',
					value: randomData()
				}, {
					name: '广西',
					value: randomData()
				}, {
					name: '甘肃',
					value: randomData()
				}, {
					name: '山西',
					value: randomData()
				}, {
					name: '内蒙古',
					value: randomData()
				}, {
					name: '陕西',
					value: randomData()
				}, {
					name: '吉林',
					value: randomData()
				}, {
					name: '福建',
					value: randomData()
				}, {
					name: '贵州',
					value: randomData()
				}, {
					name: '广东',
					value: randomData()
				}, {
					name: '青海',
					value: randomData()
				}, {
					name: '西藏',
					value: randomData()
				}, {
					name: '四川',
					value: randomData()
				}, {
					name: '宁夏',
					value: randomData()
				}, {
					name: '海南',
					value: randomData()
				}, {
					name: '台湾',
					value: randomData()
				}, {
					name: '香港',
					value: randomData()
				}, {
					name: '澳门',
					value: randomData()
				}]
			},

		]
	};
	indexMapChart.setOption(indexMapOps);

	/*首页-业务简报*/
	var indexLineChart = echarts.init(document.getElementById("index-line-chart"));
	var indexLineOps = {
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: ['邮件营销', '联盟广告', '视频广告', '直接访问', '搜索引擎']
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
			data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
		},
		yAxis: {
			type: 'value'
		},
		series: [{
			name: '邮件营销',
			type: 'line',
			stack: '总量',
			data: [120, 132, 101, 134, 90, 230, 210]
		}, {
			name: '联盟广告',
			type: 'line',
			stack: '总量',
			data: [220, 182, 191, 234, 290, 330, 310]
		}, {
			name: '视频广告',
			type: 'line',
			stack: '总量',
			data: [150, 232, 201, 154, 190, 330, 410]
		}, {
			name: '直接访问',
			type: 'line',
			stack: '总量',
			data: [320, 332, 301, 334, 390, 330, 320]
		}, {
			name: '搜索引擎',
			type: 'line',
			stack: '总量',
			data: [820, 932, 901, 934, 1290, 1330, 1320]
		}]
	};
	indexLineChart.setOption(indexLineOps);
});