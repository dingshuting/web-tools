steal(
	'can',
	function(can){
		var BDMapControl = can.Control.extend({
			/**
			 * 初始化
			 * @param {Object} el 
			 */
			init:function(el,options){
				//地图主体div
				if(!options.isStreetNeed){
					options.isStreetNeed=false;
				}
				var $iframe = '<iframe src="plugins/map.html" frameborder="0" scrolling="auto" width="630px" height="430px" id="map" name="map"></iframe>';
				$(el).append($iframe);
			},
			getValue:function(div,data){
                $(div).find("input").each(function() {
                    if($(this).attr("name") == 'address') {
                        data[$(div).attr("name")] = $(this).val();
                    }
                    if($(this).attr("name") == 'lng') {
                        data['lng'] = $(this).val();
                    }
                    if($(this).attr("name") == 'lat') {
                        data['lat'] = $(this).val();
                    }
                    if($(this).attr("name") == 'province') {
                        data['province'] = $(this).val();
                    }
                    if($(this).attr("name") == 'city') {
                        data['city'] = $(this).val();
                    }
                    if($(this).attr("name") == 'district') {
                        data['district'] = $(this).val();
                    }
                });
				return data;
			},
            getAddress:function(){
                //var result = window.frames["map"].document.getElementById("resultOfAddress").value;//方法一
                //var result = this.element.find("#map")[0].contentWindow.getValue();//方法二
				var result = $("#map")[0].contentWindow.getValue();//方法二
                if(this.options.isStreetNeed&&(result.street||result.street=="")){
                    return undefined;
                }
                return result;
            },setLocation:function (lng,lat) {
				$('#map').load(function () {
                    $("#map")[0].contentWindow.theLocation(lng,lat);
                })
            },validate:function () {
                if(this.options.div.attr("class").indexOf("required")>0&&'' == $(this.options.div).find("input[name='address']").val()){
                    $(this.options.div).validationEngine('showPrompt', '*此处不可空白', 'red')
                    return false;
                }else{
                	return true;
                }
            }
		});
		$.BDMapControl = BDMapControl;
		return BDMapControl;
	}
)
