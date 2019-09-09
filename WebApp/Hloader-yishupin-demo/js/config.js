/**应用所要请求的地址配置，MODEL.[target].[act]分别对应在模块中的data-target.data-act,组成请求地址
 * /opt/docker/apps/mall-nginx/website
**/
MODEL = {
	yishupin: {
		
	},
	
}
//上传组件的配置
UploadSetting={
	//上传服务器的地址
	uri:"/aliyun/oss/uploadPicture",
	//返回数据对应的图片或文件的key
	resultUrlKey:"data"
}
//分页字段属性的配置
PAGE_FIELD={
	totalSize:"total",
	perPageSize:"pageSize",
	currentPage:"pageNum",
	pageTotal:"totalPage"
}
VAL_IMG_MAPPING={
	"正方形":"img/zhengfangxing.png",
	"横向长方形":"img/hengchangfangxing.png",
	"竖向长方形":"img/shuchangfangxing.png",
	"圆形":"img/yuan.png",
	"多边形":"img/duobainxing.png",
	"无框":"img/wukuang.jpg",
	"黑色细框":"img/hese.jpg",
	"金色古典":"img/yinse.jpg",
	"原木细框":"img/yuanmu.png",
	"金色简约":"img/jinse.jpg",
	"黑金古典":"img/heijinggudian.png",
	"白色细框":"img/baisexikuang.jpg",
	"金色现代":"img/jinsexiandai.png",
	"碳木简约":"img/tanmujianyue.png"
}

HLoaderConfig={
	modelDefaultTarget:"yishupin",
	//分页组件属性的配置
	pageSetting:{
		totalSizeName:"total",
		perPageSizeName:"pageSize",
		currentPageName:"pageNum"
	},
	modelUrl:{
		yishupin:{
			packageList:"package/list",
			login:"sso/login",
			register:"sso/register",
			productAttribute:"productAttribute/list/1",
			productSellAttribute:"productAttribute/list/0",
			productList:"product/list",
			newProduct:"mainpage/newproducts",
			deriverecommend:"mainpage/deriverecommend",
			artistrecommend:"mainpage/artistrecommend",
			shoppingCarList:"cart/list",
			shoppingCarAdd:"cart/add",
			memberAddressList:"member/address/list",
			memberAddressAdd:"member/address/add",
			memberAddressUpdate:"member/address/update",
			generateConfirmOrder:"order/generateConfirmOrder",
			generateOrder:"order/generateOrder",
			getOrderInfo:"order/getOrderInfo",
			deleteOrder:"order/deleteOrder",
			cancelOrder:"order/userCancelOrder",
			refundOrder:"order/applyPay",
			complateOrder:"order/userCompleteOrder",
			payOrder:"order/testPaySuccess",
			productDetail:"product/",
			orderList:"order/getOrderListInfo",
			artistDetail:"artist/detail",
			artistApply:"artist/apply",
			updateLike:"product/updateLike/",
			updateUserInfo:"sso/savedetail",
			forsearch:"productAttribute/list/forsearch",
			updatePassword:"sso/updatePassword",
			artistList:"artist/list",
			artNews:"mainpage/artnews",
			superrecommend:"mainpage/superrecommend",
			newslist:"news/newslist",
			newsdetail:"news/newsdetail",
			newsRead:"news/updatereadcount/",
			generateToacanOrder:"package/generateOrder",
			getPackageOrderInfo:"package/getPackageOrderInfo"
		}
	},
	listSetting:{
		resultKey:"data"
	}
}
//后台服务地址
SERVER_DOMAIN="http://artyida.isite8.com";
CONTENT_PATH="";
PAGE_ROOT_PATH="http://127.0.0.1:8020/yishupin-mobile";
var loginPage="/login.html"
var ua = window.navigator.userAgent.toLowerCase();
if(ua.match(/MicroMessenger/i) == 'micromessenger') {
	loginPage="/login-wx.html"
} 
LOGIN_PAGE=PAGE_ROOT_PATH+loginPage;
CURRENT_USER_URL="/sso/getCurrentMember";
DefaultEvent={
	empty:function(self,el){
		el.append("<div class='col-lg-12 text-center'>暂无数据</div>")
	}
}
//list模块中返回数据的list的所作对象或字段的key，取值如：result.LIST_RESULT_KEY.list 无则为 result.list
LIST_RESULT_KEY="data";


