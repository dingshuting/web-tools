
function Pop(title,act,intro){
	this.title=title;
	this.act=act;
	this.intro=intro;
	this.apearTime=1000;
	this.hideTime=500;
	this.delay=10000;
	//添加信息
	this.addInfo();
	//显示
	this.showDiv();
}


Pop.prototype={
  addInfo:function(){
    $("#popTitle a").html(this.title).click(this.act);
    $("#popIntro").html(this.intro);
    var obj=this;
    $("#popClose").click(obj.closeDiv);
    $("#popMore a").click(function(){
    	obj.act();
    	obj.closeDiv();
    });
  },
  showDiv:function(time){
      $('#pop').show();
      $("#pop").animate({right:'10px'},'slow');
      $("#pop").oneTime("5s",this.closeDiv);
  },
  closeDiv:function(){
  	$("#pop").animate({right:'-260px'},'fast',function(){
  		$('#pop').hide();
  	 });
  }
}
