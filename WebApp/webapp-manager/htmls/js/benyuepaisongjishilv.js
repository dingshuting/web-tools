//全局的分组类型，用于点击城市名称或者服务中心名称时图表联动的标识。
var cb = 1,kxbdMarqueeCount=0;

function benyuepaisongjishilv(type, el) {
	var paixu = $(el).hasClass("arrow3");
	tableHandler(el, function() {
		if('yue' == type) {
			Gethezuokuaidigongsikuaijianliang(1, 2, paixu == false ? 1 : 2);
		} else if('ji' == type) {
			Gethezuokuaidigongsikuaijianliang(1, 3, paixu == false ? 1 : 2);
		} else if('nian' == type) {
			Gethezuokuaidigongsikuaijianliang(1, 4, paixu == false ? 1 : 2);
		} else if('leiji' == type) {
			Gethezuokuaidigongsikuaijianliang(1, 1, paixu == false ? 1 : 2);
		}
	})
}
/**
 * 本月派送及时率分析
 * @param type 类型
 * 			   1.按城市查看派送件派件的及时率统计
 *             2.按登录人所在城市的派送及时率统计
 *             3.按登录人所在服务中心的派送及时率统计
 * @param countBy 分组类型
 *                1.按城市
 *                2.按服务中心
 */
function Getbenyuepaisongjishilv(type, countBy) {
	if (3 == type){
        cb = type;
	} else {
        cb = countBy;
	}
	var el = $('#benyuepaisongjishilvDiv');
	$(el).showLoading();
	var data = {};
	// data['tjtype'] = type;
	data['countBy'] = countBy;
	if($.requestPara['scId']) {
		data['scId'] = $.requestPara['scId'];
	}
	$.ajax({
		type: "GET",
		url: contentURL + "/keshihua/benyuepaisongjishilv/1/" + type,
		data: data,
		dataType: "json",
		ContenType: "application/json",
		success: function(data) {
			var benyuepaisongjishilvHTML = '';
			if(data.list.length > 0) {
				// benyueChart(countBy,yixiaoshi,liangxiaoshi,sixiaoshi,yitiannei,yitianwai);
				benyueChart(cb, data.list[0][8], data.list[0][9], data.list[0][10], data.list[0][11], data.list[0][12]);
				for(var i = 0; i < data.list.length; i++) {
					var yixiaoshi = data.list[i][8],
						liangxiaoshi = data.list[i][9],
						sixiaoshi = data.list[i][10],
						yitiannei = data.list[i][11],
						yitianwai = data.list[i][12]
					var totalCount = (yixiaoshi - 0) + (liangxiaoshi - 0) + (sixiaoshi - 0) + (yitiannei - 0) + (yitianwai - 0);
					var jsonData = $.toJSON(data.list[i]);
					jsonData = jsonData.replace(/\"/g, "'");
					if(type == 2) {
						// benyuepaisongjishilvHTML+= '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">'+(0==i?data.list[i][1]:data.list[i][2])+'<input type="hidden" value="'+jsonData+'"/> </a></td><td width="15%">'+(data.list[i][3]!=''?data.list[i][3]:0)+'%</td><td width="15%">'+(data.list[i][4]!=''?data.list[i][4]:0)+'%</td><td width="15%">'+(data.list[i][5]!=''?data.list[i][5]:0)+'%</td><td width="15%">'+(data.list[i][6]!=''?data.list[i][6]:0)+'%</td><td width="15%">'+(data.list[i][7]!=''?data.list[i][7]:0)+'%</td></tr>';
						benyuepaisongjishilvHTML += '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">' + (0 == i ? data.list[i][1] : data.list[i][2]) + '<input type="hidden" value="' + jsonData + '"/> </a></td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((liangxiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((sixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitiannei / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitianwai / totalCount * 100))) + '%</td></tr>';
					} else if(type == 3) {
						// benyuepaisongjishilvHTML+= '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">'+(0==i?data.list[i][2]:data.list[i][13])+'<input type="hidden" value="'+jsonData+'"/> </a></td><td width="15%">'+(data.list[i][3]!=''?data.list[i][3]:0)+'%</td><td width="15%">'+(data.list[i][4]!=''?data.list[i][4]:0)+'%</td><td width="15%">'+(data.list[i][5]!=''?data.list[i][5]:0)+'%</td><td width="15%">'+(data.list[i][6]!=''?data.list[i][6]:0)+'%</td><td width="15%">'+(data.list[i][7]!=''?data.list[i][7]:0)+'%</td></tr>';
						benyuepaisongjishilvHTML += '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">' + (0 == i ? data.list[i][2] : data.list[i][13]) + '<input type="hidden" value="' + jsonData + '"/> </a></td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((liangxiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((sixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitiannei / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitianwai / totalCount * 100))) + '%</td></tr>';
					} else {
						// benyuepaisongjishilvHTML+= '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">'+(countBy==1?data.list[i][1]:data.list[i][2])+'<input type="hidden" value="'+jsonData+'"/> </a></td><td width="15%">'+(data.list[i][3]!=''?data.list[i][3]:0)+'%</td><td width="15%">'+(data.list[i][4]!=''?data.list[i][4]:0)+'%</td><td width="15%">'+(data.list[i][5]!=''?data.list[i][5]:0)+'%</td><td width="15%">'+(data.list[i][6]!=''?data.list[i][6]:0)+'%</td><td width="15%">'+(data.list[i][7]!=''?data.list[i][7]:0)+'%</td></tr>';
						benyuepaisongjishilvHTML += '<tr><td width="25%"><a href="javascript:void(0)" onclick="tobenyueChart(this)" style="color:#fff">' + (countBy == 1 ? data.list[i][1] : data.list[i][2]) + '<input type="hidden" value="' + jsonData + '"/> </a></td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((liangxiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((sixiaoshi / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitiannei / totalCount * 100))) + '%</td><td width="15%">' + (totalCount == 0 ? 0 : toDecimal2((yitianwai / totalCount * 100))) + '%</td></tr>';
					}
				}
				$('#benyuepaisongjishilv').html(benyuepaisongjishilvHTML);
			}
			$(el).hideLoading();
            var liLen = $("#marquee4 li").length;
            if (1 < liLen){
                $("#marquee4 li:last-child").remove();
            }
            startScorll($("#marquee8"))
            startScorll($("#marquee4"))
            startScorll($("#marquee5"))
            kxbdMarqueeCount++;
		}
	});
}
//图表联动的方法
function tobenyueChart(el) {
	var data = $(el).find('input[type="hidden"]').val();
	data = $.parseJSON(data.replace(/'/g, '"'));
	benyueChart(cb, data[8], data[9], data[10], data[11], data[12]);
}

function benyueChart(countBy, yixiaoshi, liangxiaoshi, sixiaoshi, yitiannei, yitianwai) {
	var text = '';
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main5')),txt='';
    if (countBy == 1){
        txt = '（城市）';
    } else if (countBy == 2){
        txt = '（服务中心）';
    } else if (countBy == 3){
        txt = '';
    }
    // 指定图表的配置项和数据
	var option = {
		title: {

			text: '本月派送及时率分析' + txt,
			textStyle: {
				color: '#fff',
				fontSize: 15
			},
			x: 'left'
		},
		tooltip: {
			trigger: 'item',
			formatter: "{a} <br/>{b} : {c} ({d}%)"
		},
		color: ['#FCCE10', '#E87C25', '#27727B',
			'#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
			'#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'
		],

		series: [{
			name: '派送及时率',
			type: 'pie',
			radius: '55%',
			center: ['50%', '60%'],
			data: [{
					value: yixiaoshi,
					name: '1小时内'
				},
				{
					value: liangxiaoshi,
					name: '2小时内'
				},
				{
					value: sixiaoshi,
					name: '4小时内'
				},
				{
					value: yitiannei,
					name: '1天内'
				},
				{
					value: yitianwai,
					name: '>1天'
				}
			],
			itemStyle: {
				emphasis: {
					shadowBlur: 10,
					shadowOffsetX: 0,
					shadowColor: 'rgba(0, 0, 0, 0.5)'
				}
			}
		}]
	};

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}
$(".chart-act-jsl").click(function() {
	$(".chart-act-jsl").css("color", "#fff");
	$(this).css("color", "#1ab394")
})

function toDecimal2(x) {
	var f = parseFloat(x);
	if(isNaN(f)) {
		return;
	}
	f = Math.round(x * 100) / 100;
	return f;
}