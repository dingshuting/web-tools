var total='';
/**
 * 未妥投/预警快件分析
 * @param type 类型
 * 			   1.按登录人所在城市所有服务中心预警快件量、占比，未妥投未预警快件量及占比统计
 *             2.按登录人所在服务中心所有退回件退回原因、数量及占比统计
 */
function Getweituotouyujingfenxi(type) {
    var data = {};
    data['wttyjtjtype'] = type;
    if($.requestPara['scId']){
        data['scId'] = $.requestPara['scId'];
    }
    $.post(contentURL + "/keshihua/weituotouyujingfenxi/1",data, function (data) {
        var weituotouyujingfenxiHTML = '';
        if (data.list.length > 0){
            if (1 == type){
                for (var i = 0; i < data.list.length; i++){
                    var jsonData = $.toJSON(data.list[i]);
                    jsonData = jsonData.replace(/\"/g,"'");
                    weituotouyujingfenxiHTML+= '<tr><td width="40%"><a href="javascript:void(0)" onclick="toweituotouChart(this)" style="color:#fff">'+(data.list[i][0])+'<input type="hidden" value="'+jsonData+'"/> </a></td><td width="20%">'+data.list[i][2]+'</td><td width="20%">'+data.list[i][3]+'</td><td width="20%">'+data.list[i][4]+'</td></tr>';
                }
                $('#weituotouyujingfenxi').html(weituotouyujingfenxiHTML);
            }else {
                //合计
                var heji = parseInt(data.list[0][3])+parseInt(data.list[0][4]);
                total = heji;
            }
            weituotouChart(data.list[0][3],data.list[0][4]);
        }
     	startScorll($("#marquee17"));
    })
}
//图表联动的方法
function toweituotouChart(el) {
    var data = $(el).find('input[type="hidden"]').val();
    data = $.parseJSON(data.replace(/'/g, '"'));
    weituotouChart(data[3],data[4]);
}
//未妥投/预警快件分析
function weituotouChart(yujing,weituotouweiyujing) {
    var text = '';
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main22'));
    // 指定图表的配置项和数据
    var option = {
        title: {
            text: total==''?'':'合计：'+total,
            textStyle: {
                color: '#fff',
                fontSize: 15
            },
            x: 'right'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        color:['#FCCE10','#E87C25','#27727B',
                           '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'],

        series: [{
            name: '本月派送件妥投率分析',
            type: 'pie',
            radius: '55%',
            center: ['50%', '60%'],
            data: [
                {
                    value: yujing,
                    name: '预警快件'
                },
                {
                    value: weituotouweiyujing,
                    name: '未妥投未预警快件'
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