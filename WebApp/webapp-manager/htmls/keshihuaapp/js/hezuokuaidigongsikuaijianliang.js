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
    var express=null;
    if(window.localStorage){
        var storage = window.localStorage;
        if(storage.getItem("expressInfo")==null){
            $.post(contentURL+'/mdyn/express_company/list/1?count=99',{},function (data) {
                storage.setItem("expressInfo",JSON.stringify(data));
                express = data;
            })
        }else {
            express = JSON.parse(storage.getItem("expressInfo"));
        }
    }else{
        $.post(contentURL+'/mdyn/express_company/list/1?count=99',{},function (data) {
            express = data;
        })
    }
    var data = {};
    data['type'] = type;
    data['orderBy'] = orderBy;
    data['orderByType'] = orderByType;
    $.post(contentURL + "/keshihua/hezuokuaidigongsikuaijianliang/1",data, function (data) {
        var hezuokuaidigongsikuaijianliangHTML = '';
        if (data.list.length > 0){
            for (var i = 0; i < data.list.length; i++){
                var logo='';
                for (var j = 0; j < express.list.length; j++){
                    if(data.list[i][1] == express.list[j].name){
                        logo = express.list[j].logo;
                    }
                }
                hezuokuaidigongsikuaijianliangHTML+= '<div class="swiper-slide"style="height:341px;"><div class="reflection1" style="text-align: center"><img src="'+logo+'"width="120"height="120" style="border-radius: 50%"></div><div class="reflection">第'+(i+1)+'名<br>本月入仓量：'+data.list[i][2]+'<br>累计入仓量：'+data.list[i][5]+'</div></div>';
            }
            $('#kuaijianliang').html(hezuokuaidigongsikuaijianliangHTML);
        }else {
            $('#kuaijianliang').html('');
        }
        var mySwiper = new Swiper('.swiper-container', {
            slidesPerView: 3,
            autoplay: 3000,
            loop: true,
            //Enable 3D Flow
            tdFlow: {
                modifier: 1,
                shadows: true
            }
        })
    })
}
function benyuepaisongtuotoulvOption() {
    var opt={yAxis: {
            name:""
        }}
    return opt;
    
}


		


