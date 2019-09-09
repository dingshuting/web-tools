var _SkuSelect = {};
var selectedSKU;
//transfering the str to a url of image.
function valTransToImg(val) {
	return VAL_IMG_MAPPING[val];
}

function choiseSku() {
	var el = $(this);
	var obj = $(this).data("value");
	if(el.parent().hasClass("disabled")) {
		return;
	}
	var itemIndex = el.parents("[data-item]:last").data("index");
	//当已经点击时，则取消点击状态，并重新查询sku列表，并加装
	var selectAttr = {};
	if(el.parent().hasClass("active")) {
		el.parent().removeClass("active");
		delete _SkuSelect["sp" + (itemIndex + 1)];
	} else {
		el.parents("[name='inputList']").find(".sellAttr").removeClass("active");
		el.parent().addClass("active");
		_SkuSelect["sp" + (itemIndex + 1)] = obj;
		selectAttr["sp" + (itemIndex + 1)] = obj;
	}

	loadExsitSku(undefined, selectAttr);
}

function loadExsitSku(event, obj) {
	$("[name='albumPics'] [data-item]").click(function() {
		$("[name='pic']").attr("src", $(this).data("detailControl").data);
		$("[name='albumPics'] [data-item]").removeClass("active");
		$(this).addClass("active");
	});
	var skus = querySku(_SkuSelect);
	//the length eq 1 means the sku has been selected.
	if(skus.length == 1) {
		selectedSKU = skus[0];
		console.log("the SKU has bean selected [" + selectedSKU.id + "]");
		skuDecided(selectedSKU);
		//return;
	} else {
		skuDecided(undefined);
	}
	if(obj)
		skus = querySku(obj);
	$('.productAttribute').each(function() {
		var key = "sp" + ($(this).data("index") + 1);
		if(obj && obj[key]) {
			return true;
		}
		$(this).find("[data-item]").each(function() {
			var hasAttr = false;
			for(var sku in skus) {
				if(skus[sku][key] == $(this).data("value") && skus[sku].stock > 0) {
					hasAttr = true;
					break;
				}
			}
			if(hasAttr) {
				$(this).find(".sellAttr").removeClass("disabled");
			} else {
				if(event) {
					$(this).find(".sellAttr").remove();
				} else {
					$(this).find(".sellAttr").addClass("disabled");
				}
			}
		});
		if($(this).find(".sellAttr").length == 1 && !$(this).find(".sellAttr").hasClass("disabled")) {
			$(this).find(".sellAttr").addClass("active");
		}
	});
}

function skuDecided(sku) {
	var product = $("#detail").control().data;
	if(sku) {
		$("[name='price']").html("￥ " + sku.price);
		if(sku.pic && sku.pic != "")
			$("[name='pic']").attr("src", sku.pic);
		else
			$("[name='pic']").attr("src", product.pic);
	} else {
		$("[name='price']").html("￥ " + product.price);
		$("[name='pic']").attr("src", product.pic);
		selectedSKU = undefined;
	}

}
//searching the skus that contain attrs that you has bean selected;
function querySku(queryObj) {
	var skus = $("#detail").control().data.skuStocks;
	var con = "";
	for(var key in queryObj) {
		con += key + " eq " + queryObj[key] + " and "
	}
	con = con.substr(0, con.length - 5);
	return $.objArrayQuery(skus, con);
}
var buyNow=false;
function pageFinished(){
	console.log(this)
	$(".skuChoiser .am-icon-close").click(function(){
		var parent=$(this).parents(".skuChoiser");
		parent.animate({"margin-bottom":(parent.height()+20)*-1},300);
	});
	$(".addToCar").click(addProToCar);
	$(".buyNow").click(function(){
		addProToCar(undefined,function(){
			window.location.href="shoppingcar.html"
		})
		buyNow=true;
	});
	$('.am-slider-a1').flexslider({"controlNav":false});
	$(".sureSku").click(addProToCar);
}
function addProToCar(e,callback){
	if(showChoiser()!=false) {
		if(!$.session.currentUser) {
			window.location = LOGIN_PAGE+"?redirect=" + encodeURI(window.location.href);
		} else {
			var product = $("#detail").control().data;
			var car = {
				price: selectedSKU.price,
				quantity: 1,
				productSkuId: selectedSKU.id,
				productSkuCode: selectedSKU.skuCode,
				productId: selectedSKU.productId,
				productName: product.name,
				isRent:product.isRent,
				sp1: selectedSKU.sp1,
				sp2: selectedSKU.sp2,
				sp3: selectedSKU.sp3
			}
			if(selectedSKU.pic){
				car.productPic=selectedSKU.pic;
			}else{
				car.productPic=product.pic;
			}
			$.Model("shoppingCarAdd", "yishupin").execute(car, function(res) {
				if(res.code == "200"&&callback==undefined) {
					alert('添加成功');
					$(".skuChoiser .am-icon-close").trigger("click");
					if(buyNow==true){
						window.location.href="shoppingcar.html";
					}
				}else{
					callback.call();
				}
			})
		}
	} else {
		//alert("请选择相关属性");
	}
}
function showChoiser(){
	if(selectedSKU){
		return selectedSKU;
	}else{
		$(".skuChoiser").animate({"margin-bottom":0},300);
		return false;
	}
}
