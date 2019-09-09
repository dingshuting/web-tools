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
	cb = countBy;
	var data = {};
	data['countBy'] = countBy;
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
				benyueChart(countBy, data.list[0][8], data.list[0][9], data.list[0][10], data.list[0][11], data.list[0][12]);
			}
            var liLen = $("#marquee4 li").length;
            if (1 < liLen){
                $("#marquee4 li:last-child").remove();
            }
                $("#marquee8").kxbdMarquee({
                    direction: "up",
                    isEqual: false
                });
                $("#marquee4").kxbdMarquee({
                    direction: "up",
                    loop: 2,
                    isEqual: false
                });
                $("#marquee5").kxbdMarquee({
                    direction: "up",
                    isEqual: false
                });
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
	var myChart = echarts.init(document.getElementById('main5'));

	// 指定图表的配置项和数据
	var option = {
		title: {

			text: '本月派送及时率分析' + (countBy == 1 ? '（城市）' : '（服务中心）'),
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
					name: '1天以上'
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
    // 指定图表的配置项和数据
    var option = {
        title: {
            x: 'left',
            textStyle: {
                color: '#9BCA63',
                fontSize: 40,
            },
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)",
            textStyle: {
                color: '#afa9ff',
                fontSize: 30,
            },
        },
        color: ['#ffc65b', '#31d6c5', '#fe8081', '#FE8463', '#9BCA63', '#FAD860', '#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'
        ],
        legend: {
            orient: 'vertical',
            left: 'right',
            data: ['1小时内', '2小时内', '4小时内', '1天内', '1天以上'],
            textStyle: {
                color: '#c3c1cc',
                fontSize: 26,

            },
        },
        series: [{
            name: '派送及时率',
            label: { //饼图图形上的文本标签
                normal: {
                    show: true,
                    textStyle: {
                        fontWeight: 300,
                        fontSize: 36 //文字的字体大小
                    },
                    formatter: '{d}%'

                }
            },
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
                    name: '1天以上'
                }
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',

                },

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