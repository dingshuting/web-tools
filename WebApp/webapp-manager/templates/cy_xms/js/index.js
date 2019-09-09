function showParentToastr(){
	toastr.options = {
	  "closeButton": true,
	  "debug": false,
	  "progressBar": true,
	  "positionClass": "toast-bottom-right",
	  "showDuration": "10000",
	  "hideDuration": "100",
	  "timeOut": "20000",
	  "extendedTimeOut": "20000",
	  "showEasing": "swing",
	  "hideEasing": "linear",
	  "showMethod": "fadeIn",
	  "hideMethod": "fadeOut",
	  "onclick":function(){
	  	$("#chat")[0].contentWindow.showIm_ChatListener();
	  }
	}
	toastr.info("有人发起聊天，请点击查看！","在线咨询");
}