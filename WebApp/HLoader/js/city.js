steal("js/city-data.js",function(){
	_CityUtil_={
		loadCity:function(_CityObj_){
			this.data=_REGION_;
			this._CityObj_=_CityObj_;
			if($("[name='province']").length>0&&$("[name='province']")[0].tagName=="SELECT"){
				var $province=$("[name='province']").filter("select");
				var $city=$("[name='city']").filter("select");
				$province.find("option:gt(0)").remove();
				for(var i in this.data){
					var $option=$("<option value='"+this.data[i].name+"'>"+this.data[i].name+"</option>");
					if(_CityObj_&&_CityObj_.province==this.data[i].name){
						$option.prop("selected","selected");
					}
					$option.data("childRegs",this.data[i].childRegs);
					$province.append($option);
				}
				$province.change(this.provinceChange);
				$city.change(this.cityChange);
				if(_CityObj_){
					this.provinceChange();
					this.cityChange();
				}
			}
		},
		provinceChange:function(){
			var $city=$("[name='city']").filter("select");
			var $region=$("[name='region']").filter("select");
			var $province=$("[name='province']").filter("select");
			$city.find("option:gt(0)").remove();
			$region.find("option:gt(0)").remove();
			var tempData=$province.find("option:checked").data("childRegs");
			for(var i in tempData){
				var $option=$("<option value='"+tempData[i].name+"'>"+tempData[i].name+"</option>");
				if(this._CityObj_&&this._CityObj_.city==tempData[i].name){
					$option.prop("selected","selected");
				}
				$option.data("childRegs",tempData[i].childRegs);
				$city.append($option);
			}
		},
		cityChange:function(){
			var $region=$("[name='region']").filter("select");
			var $city=$("[name='city']").filter("select");
			$region.find("option:gt(0)").remove();
			var tempData=$city.find("option:checked").data("childRegs");
			for(var i in tempData){
				var $option=$("<option value='"+tempData[i].name+"'>"+tempData[i].name+"</option>");
				if(this._CityObj_&&this._CityObj_.region==tempData[i].name){
					$option.prop("selected","selected");
				}
				$option.data("childRegs",tempData[i].childRegs);
				$region.append($option);
			}
		}
	}
})

