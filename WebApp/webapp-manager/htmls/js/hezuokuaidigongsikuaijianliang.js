function hezuokuaidigongsikuaijianliang(type,el) {
    var paixu = $(el).hasClass("arrow3");
    tableHandler(el,function () {
        var isByServiceCenter=false;
        if($.requestPara['scId']){
            isByServiceCenter=true;
        }
        if ('yue' == type){
            Gethezuokuaidigongsikuaijianliang(isByServiceCenter==false?1:3,2,paixu==false?1:2);
        }else if ('ji' == type){
            Gethezuokuaidigongsikuaijianliang(isByServiceCenter==false?1:3,3,paixu==false?1:2);
        }else if ('nian' == type){
            Gethezuokuaidigongsikuaijianliang(isByServiceCenter==false?1:3,4,paixu==false?1:2);
        }else if ('leiji' == type){
            Gethezuokuaidigongsikuaijianliang(isByServiceCenter==false?1:3,1,paixu==false?1:2);
        }
    })
}
/**
 * 合作快递公司快件量排名
 * @param type 类型
 * 			   1.按所有城市所有合作的快递公司快件统计
 *             2.按登录人所在城市所有合作的快递公司快件统计
 *             3.按登录人所在服务中心所有合作的快递公司快件
 * @param orderBy 排序字段
 *                1.累计
 *                2.本月
 *                3.本季度
 *                4.本年
 * @param orderByType 排序顺序
 *                    1.顺序
 *                    2.倒序
 */
function Gethezuokuaidigongsikuaijianliang(type,orderBy,orderByType) {
    var el=$('#hezuokuaidigongsikuaijianliangDiv');
    $(el).showLoading();
    var data = {};
    data['type'] = type;
    data['orderBy'] = orderBy;
    data['orderByType'] = orderByType;
    if($.requestPara['scId']){
        data['scId'] = $.requestPara['scId'];
    }
    $.post(contentURL + "/keshihua/hezuokuaidigongsikuaijianliang/1",data, function (data) {
        var hezuokuaidigongsikuaijianliangHTML = '';
        if (data.list.length > 0){
            for (var i = 0; i < data.list.length; i++){
                hezuokuaidigongsikuaijianliangHTML+= '<tr><td width="10%">'+(i+1)+'</td><td width="30%">'+data.list[i][1]+'</td><td width="15%">'+data.list[i][2]+'</td><td width="15%">'+data.list[i][3]+'</td><td width="15%">'+data.list[i][4]+'</td><td width="15%">'+data.list[i][5]+'</td></tr>';
            }
            $('#hezuokuaidigongsikuaijianliang').html(hezuokuaidigongsikuaijianliangHTML);
        }else {
            $('#hezuokuaidigongsikuaijianliang').html('');
        }
        $(el).hideLoading();
		startScorll($("#marquee10"))
    })
}
function benyuepaisongtuotoulvOption() {
    var opt={yAxis: {
            name:""
        }}
    return opt;
    
}


		


