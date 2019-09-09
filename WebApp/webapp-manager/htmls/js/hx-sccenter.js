var typeval = 1;
var scid = '';
var xtype = 0;
$(function () {
    $.requestPara = $.urlToObject(window.location.href);
    if($.requestPara['scId']!=undefined&&$.requestPara['scId']!=null&&$.requestPara['scId']!=''){
        scid=$.requestPara['scId'];
    }


    //2.3.3目标达成率分析
//   type  3.本年 2.本季度 1.本月
    mbdclfx(1);
    //2.3.4服务费数据分析
//   type  3.本年 2.本季度 1.本月,4 时间
    fwffenx(1);
    //2.3.5快件量数据分析(按服务中心)
//   type  3.本年 2.本季度 1.本月,4 时间
    kjfenx(1);
    // 2.3.6退回件原因分析(所在服务中心)
    //   type 类型 1.所在服务中心
    thyyfx(1);
})


function mbdclfx(type) {
    switch (type) {
        case 1:
            $("#mbdclbym").css('color', "#1ab394");
            $("#mbdclbyj").css('color', "#fff");
            $("#mbdclbyy").css('color', "#fff");
            break;
        case 2:
            $("#mbdclbym").css('color', "#fff");
            $("#mbdclbyj").css('color', "#1ab394");
            $("#mbdclbyy").css('color', "#fff");
            break;
        case 3:
            $("#mbdclbym").css('color', "#fff");
            $("#mbdclbyj").css('color', "#fff");
            $("#mbdclbyy").css('color', "#1ab394");
            break;
    }
    $("#mbdclfx").showLoading();
    var data = {};
    data['type'] = type;
    // data['scId'] = scid;
    $.get(contentURL + "/keshihua/mubiaodachenglv/?scId=" + scid + "", data, function (data) {
        $("#mbdclfx").hideLoading();
        if (data.code == "200") {
            if (typeof data.data == "string") {
                data.data = $.parseJSON(data.data);
            }
            if (data.data.length > 0) {
                var xdata = new Array();//x轴
                var mbfwf = new Array();//目标服务费
                var sjfwf = new Array();//实际服务费
                var mbdcl = new Array();//目标达成率
                for (var i = 0; i < data.data.length; i++) {
                    xdata.push(data.data[i][0]);
                    mbfwf.push(((data.data[i][1] * 1).toFixed(2)) + '');
                    sjfwf.push(((data.data[i][2] * 1).toFixed(2)) + '');
                    mbdcl.push(((data.data[i][3] * 1).toFixed(2)) + '');
                }
                shetChartmbdclfx(xdata, mbfwf, sjfwf, mbdcl);
            }
        }
    })
}

var timequerenType = '';

function beforfwffenx() {
    $("#timeDiv").show();
}

function beforkjfenx() {
    timequerenType = 1;
    $("#timeDiv1").show();
}


function fwffenx(type) {
    if (type == 4) {
        if (!checkTime()) {
            return;
        }
        if (!checkTimeCha()) {
            return;
        }
    } else {
        $("#timeDiv").hide();
        $('input[name="startTime"]').val('');
        $('input[name="endTime"]').val('');
    }

    switch (type) {
        case 1:
            $("#fwfbym").css('color', "#1ab394");
            $("#fwfbyj").css('color', "#fff");
            $("#fwfbyy").css('color', "#fff");
            $("#fwfbyd").css('color', "#fff");
            break;
        case 2:
            $("#fwfbym").css('color', "#fff");
            $("#fwfbyj").css('color', "#1ab394");
            $("#fwfbyy").css('color', "#fff");
            $("#fwfbyd").css('color', "#fff");
            break;
        case 3:
            $("#fwfbym").css('color', "#fff");
            $("#fwfbyj").css('color', "#fff");
            $("#fwfbyy").css('color', "#1ab394");
            $("#fwfbyd").css('color', "#fff");
            break;
        case 4:
            $("#fwfbym").css('color', "#fff");
            $("#fwfbyj").css('color', "#fff");
            $("#fwfbyy").css('color', "#fff");
            $("#fwfbyd").css('color', "#1ab394");
            break;
    }
    $("#fwffenx").showLoading();
    var data = {};
    data['type'] = type;
    data['xtype'] = xtype;
    data['startTime'] = $('input[name="startTime"]').val() + " 00:00:00";
    data['endTime'] = $('input[name="endTime"]').val() + " 23:59:59";
    // data['scId'] = scid;
    $.post(contentURL + "/keshihua/fuwufeiByscId/?scId=" + scid + "", data, function (data) {
        $("#fwffenx").hideLoading();
        $("#timeDiv").hide();
        $('input[name="startTime"]').val('');
        $('input[name="endTime"]').val('');
        if (data.code == "200") {
            var xval='';
            if (typeof data.data == "string") {
                data.data = $.parseJSON(data.data);
            }
            // if (data.data.length > 0) {
            var xdata = new Array();//x轴
            var sjfwf = new Array();//实际服务费

            var mbdcl = new Array();//环比浮动率

            if(xtype==2){
                xval="周";
            }else{
                xval='';
            }

            for (var i = 0; i < data.data.length; i++) {
                xdata.push(data.data[i][0]+xval);
                sjfwf.push(((data.data[i][1] * 1).toFixed(2)) + '');
                var benqival = ((data.data[i][1] * 1).toFixed(2));
                var shangqival = 0.00;
                if (i > 0) {
                    shangqival = ((data.data[i - 1][1] * 1).toFixed(2));
                }
                if (shangqival == 0) {
                    mbdcl.push(0.00);
                } else {
                    mbdcl.push(((parseFloat(benqival) - parseFloat(shangqival)) / parseFloat(shangqival) * 100).toFixed(2));
                }

            }
            shetChartfwffenx(xdata, sjfwf, mbdcl);
            xtype=0;
            //}
        }
    })
}

function kjfenx(type) {
    if (type == 4) {
        if (!checkTime()) {
            return;
        }
        if (!checkTimeCha()) {
            return;
        }

    } else {
        $("#timeDiv1").hide();
        $('input[name="startTime1"]').val('');
        $('input[name="endTime1"]').val('');
    }
    switch (type) {
        case 1:
            $("#kjslbym").css('color', "#1ab394");
            $("#kjslbyj").css('color', "#fff");
            $("#kjslbyy").css('color', "#fff");
            $("#kjslbyd").css('color', "#fff");
            break;
        case 2:
            $("#kjslbym").css('color', "#fff");
            $("#kjslbyj").css('color', "#1ab394");
            $("#kjslbyy").css('color', "#fff");
            $("#kjslbyd").css('color', "#fff");
            break;
        case 3:
            $("#kjslbym").css('color', "#fff");
            $("#kjslbyj").css('color', "#fff");
            $("#kjslbyy").css('color', "#1ab394");
            $("#kjslbyd").css('color', "#fff");
            break;
        case 4:
            $("#kjslbym").css('color', "#fff");
            $("#kjslbyj").css('color', "#fff");
            $("#kjslbyy").css('color', "#fff");
            $("#kjslbyd").css('color', "#1ab394");
            break;
    }
    $("#kjfenx").showLoading();
    var data = {};
    data['type'] = type;
    data['xtype'] = xtype;
    data['startTime'] = $('input[name="startTime1"]').val() + " 00:00:00";
    data['endTime'] = $('input[name="endTime1"]').val() + " 23:59:59";
    // data['scId'] = scid;
    $.post(contentURL + "/keshihua/kuaijianshuliangByscId/?scId=" + scid + "", data, function (data) {
        $("#kjfenx").hideLoading();
        $("#timeDiv1").hide();
        if (data.code == "200") {
            if (typeof data.data == "string") {
                data.data = $.parseJSON(data.data);
            }
            var xval="";
            if(xtype==2){
                xval="周";
            }else{
                xval='';
            }
            if (data.data.length > 0) {
                var xdata = new Array();//x轴
                var rucnum = new Array();//入仓量
                var tuotounum = new Array();//妥投量
                var huanbilv = new Array();//环比浮动率
                for (var i = 0; i < data.data.length; i++) {
                    xdata.push(data.data[i][0]+xval);
                    rucnum.push(((data.data[i][1] * 1).toFixed(2)) + '');
                    tuotounum.push(((data.data[i][2] * 1).toFixed(2)) + '');
                    huanbilv.push(((data.data[i][4] * 1).toFixed(2)) + '');
                }
                shetChartkjfenx(xdata, rucnum, tuotounum, huanbilv);
                xtype=0;
            }
        }
    })
}


function thyyfx(type) {
    $("#thyyfx").showLoading();
    var data = {};
    data['type'] = type;
    // data['scId'] = scid;
    $.post(contentURL + "/keshihua/tuijianyuanyinfenxi/?scId=" + scid + "", data, function (data) {
        $("#thyyfx").hideLoading();
        if (data.code == "200") {
            if (typeof data.data == "string") {
                data.data = $.parseJSON(data.data);
            }
            var dataArr = new Array();

            for (var i = 0; i < data.data.length; i++) {
                var newdata = new Object();
                newdata.name = data.data[i][1];
                newdata.value = data.data[i][2];
                dataArr.push(newdata);
            }
            shetChartthyyfx(dataArr);
        }
    })
}

function shetChartmbdclfx(xdata, mbfwf, sjfwf, mbdcl) {
    var myChart = echarts.init(document.getElementById('mbdclfx'));

    // 指定图表的配置项和数据
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#fff'
                }
            },
            formatter: function (param) {

                if(param.length==1) {
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='目标达成率'?'%':"") ;

                }else if(param.length==2){

                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='目标达成率'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='目标达成率'?'%':"") ;

                }else if(param.length==3){
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='目标达成率'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='目标达成率'?'%':"") +
                        "<br>" + param[2].seriesName + ":" + param[2].data + (param[2].seriesName=='目标达成率'?'%':"");
                }
            }
            },

        toolbox: {
            feature: {
                dataView: {
                    show: true,
                    readOnly: false
                },
                magicType: {
                    show: true,
                    type: ['line', 'bar']
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        color: ['#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'],
        legend: {
            data: ['目标服务费', '实际服务费', '目标达成率'],
            textStyle: {
                color: '#fff'
            }
        },
        xAxis: [{
            type: 'category',
            data: xdata,
            axisPointer: {
                type: 'shadow'
            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true

        }],
        yAxis: [{
            type: 'value',
            name: '服务费（元）',

            axisLabel: {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                },

            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true
        },

            {
                type: 'value',
                name: '目标达成率（%）',

                axisLabel: {
                    formatter: '{value} %',
                    textStyle: {
                        color: '#fff'
                    },
                },
                axisLine: {

                    lineStyle: {
                        color: '#fff',
                        width: 1,
                    }
                },
                show: false
            }
        ],
        series: [{
            name: '目标服务费',
            type: 'bar',
            data: mbfwf
        },
            {
                name: '实际服务费',
                type: 'bar',
                data: sjfwf
            },
            {
                name: '目标达成率',
                type: 'line',
                yAxisIndex: 1,
                data: mbdcl
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}

function shetChartfwffenx(xdata, sjfwf, mbdcl) {
    var myChart = echarts.init(document.getElementById('fwffenx'));

    // 指定图表的配置项和数据
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#fff'
                }
            },
            formatter: function (param) {
                if(param.length==1) {
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") ;

                }else if(param.length==2){

                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='环比浮动'?'%':"") ;

                }else if(param.length==3){
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[2].seriesName + ":" + param[2].data + (param[2].seriesName=='环比浮动'?'%':"");
                }

            }
        },
        toolbox: {
            feature: {
                dataView: {
                    show: true,
                    readOnly: false
                },
                magicType: {
                    show: true,
                    type: ['line', 'bar']
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        color: ['#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'],
        legend: {
            data: ['实际服务费', '环比浮动'],
            textStyle: {
                color: '#fff'
            }
        },
        xAxis: [{
            type: 'category',
            data: xdata,
            axisPointer: {
                type: 'shadow'
            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true

        }],
        yAxis: [{
            type: 'value',
            name: '服务费（元）',

            axisLabel: {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                },

            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true
        },

            {
                type: 'value',
                name: '环比浮动（%）',

                axisLabel: {
                    formatter: '{value} %',
                    textStyle: {
                        color: '#fff'
                    },
                },
                axisLine: {

                    lineStyle: {
                        color: '#fff',
                        width: 1,
                    }
                },
                show: false
            }
        ],
        series: [{
            name: '实际服务费',
            type: 'bar',
            barWidth: 30,
            data: sjfwf
        },
            {
                name: '环比浮动',
                type: 'line',
                yAxisIndex: 1,
                barWidth: 30,
                data: mbdcl
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option, true);
}

function shetChartkjfenx(xdata, rucnum, tuotounum, huanbilv) {
    var myChart = echarts.init(document.getElementById('kjfenx'));

    // 指定图表的配置项和数据
    var option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#fff'
                }
            },
            formatter: function (param) {
                if(param.length==1) {
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") ;

                }else if(param.length==2){

                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='环比浮动'?'%':"") ;

                }else if(param.length==3){
                    return param[0].name + '<br/>' + param[0].seriesName + ":" + param[0].data + (param[0].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[1].seriesName + ":" + param[1].data + (param[1].seriesName=='环比浮动'?'%':"") +
                        "<br>" + param[2].seriesName + ":" + param[2].data + (param[2].seriesName=='环比浮动'?'%':"");
                }
            }
        },
        toolbox: {
            feature: {
                dataView: {
                    show: true,
                    readOnly: false
                },
                magicType: {
                    show: true,
                    type: ['line', 'bar']
                },
                restore: {
                    show: true
                },
                saveAsImage: {
                    show: true
                }
            }
        },
        color: ['#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'],
        legend: {
            data: ['入仓量', '妥投量', '环比浮动'],
            textStyle: {
                color: '#fff'
            }
        },
        xAxis: [{
            type: 'category',
            data: xdata,
            axisPointer: {
                type: 'shadow'
            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true

        }],
        yAxis: [{
            type: 'value',
            name: '快件量（件）',

            axisLabel: {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                },

            },
            axisLine: {

                lineStyle: {
                    color: '#fff',
                    width: 1,
                }
            },
            show: true
        },

            {
                type: 'value',
                name: '环比浮动（%）',

                axisLabel: {
                    formatter: '{value} %',
                    textStyle: {
                        color: '#fff'
                    },
                },
                axisLine: {

                    lineStyle: {
                        color: '#fff',
                        width: 1,
                    }
                },
                show: false
            }
        ],
        series: [{
            name: '入仓量',
            type: 'bar',
            data: rucnum,
        },
            {
                name: '妥投量',
                type: 'bar',
                data: tuotounum,
            },
            {
                name: '环比浮动',
                type: 'line',
                yAxisIndex: 1,
                data: huanbilv
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option, true);
}

function shetChartthyyfx(data) {

    var myChart = echarts.init(document.getElementById('thyyfx'));

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
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        color: ['#F3A43B', '#60C0DD',
            '#D7504B', '#C6E579', '#F4E001', '#F0805A', '#26C0C0'],

        series: [{
            name: '退件原因',
            type: 'pie',
            radius: '55%',

            data: data,
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
    myChart.setOption(option, true);
}

function checkTime() {
    var begintime = $('input[name="startTime' + timequerenType + '"]').val();
    var endtime = $('input[name="endTime' + timequerenType + '"]').val();

    if (!begintime || begintime == '') {
        alert("开始时间不能为空");
        return false;
    }
    if (!endtime || endtime == '') {
        alert("结束时间不能为空");
        return false;
    }

    var time1 = new Date(begintime).getTime();
    var time2 = new Date(endtime).getTime();

    if (time1 > time2) {
        alert("开始时间不能大于结束时间");
        return false;
    }

    //判断时间跨度是否大于3个月
    var arr1 = begintime.split('-');
    var arr2 = endtime.split('-');
    arr1[1] = parseInt(arr1[1]);
    arr1[2] = parseInt(arr1[2]);
    arr2[1] = parseInt(arr2[1]);
    arr2[2] = parseInt(arr2[2]);
    var flag = true;
    if (arr1[0] == arr2[0]) {//同年
        if (arr2[1] - arr1[1] > 3) { //月间隔超过3个月
            flag = false;
        } else if (arr2[1] - arr1[1] == 3) { //月相隔3个月，比较日
            if (arr2[2] > arr1[2]) { //结束日期的日大于开始日期的日
                flag = false;
            }
        }
    } else { //不同年
        if (arr2[0] - arr1[0] > 1) {
            flag = false;
        } else if (arr2[0] - arr1[0] == 1) {
            if (arr1[1] < 10) { //开始年的月份小于10时，不需要跨年
                flag = false;
            } else if (arr1[1] + 3 - arr2[1] < 12) { //月相隔大于3个月
                flag = false;
            } else if (arr1[1] + 3 - arr2[1] == 12) { //月相隔3个月，比较日
                if (arr2[2] > arr1[2]) { //结束日期的日大于开始日期的日
                    flag = false;
                }
            }
        }
    }
    if (!flag) {
        alert("时间跨度不得超过3个月！");
        return false;
    }
    return true;
}

function checkTimeCha() {

    var startTime_ms = Date.parse(new Date($('input[name="startTime' + timequerenType + '"]').val().replace(/-/g, "/")));
    var endTime_ms = Date.parse(new Date($('input[name="endTime' + timequerenType + '"]').val().replace(/-/g, "/")));
    var date3 = new Date(endTime_ms).getTime() - new Date(startTime_ms).getTime();
    //计算出相差天数
    var days = Math.floor(date3 / (24 * 3600 * 1000))

    var leave1 = date3 % (24 * 3600 * 1000)
    var hours = Math.floor(leave1 / (3600 * 1000))

    var leave2 = leave1 % (3600 * 1000)
    var minutes = Math.floor(leave2 / (60 * 1000))

    var leave3 = leave2 % (60 * 1000)      //计算分钟数后剩余的毫秒数
    var seconds = Math.round(leave3 / 1000)
    if (days == 0) {
        alert("时间跨度最小24小时！");
        return false;
    }


    if (days > 31) {//1月以上  月为x轴
        xtype = 1;

    } else if (days >7 && days<=31) {//1周以上 周为x轴
        xtype = 2;
    } else { //7天以下 天为x轴
        xtype = 3;
    }
    return true;

    // alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒")

}