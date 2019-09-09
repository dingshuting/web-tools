var contentURL = "http://wuliu-bms.yidaqifu.com";
var contentPath = "";
$.ajax({
	url: contentURL+contentPath+ "/user/cu",
	success: function(data) {
		
		try {
			if(data instanceof Object) {
				
			}else{
				window.location.href="login.html"
			}
		}catch(e) {
			window.location.href="login.html"
		}
	},
	error: function(data) {
		window.location.href="login.html";
	}
});
var LINE_CHART_EXTEND_DATA = {
	"bhdfx": {
		1: {
			name: "目标工作量",
			unit: "件"
		},
		2: {
			name: "实际工作量",
			unit: "件"
		},
		3: {
			name: "饱和度",
			unit: "%"
		},
		ex1: function(el, colData, series) {
			var opt = "环比上月:";
			if(series.dataIndex > 0) {
				var val = (colData[series.seriesName][3][series.dataIndex] - colData[series.seriesName][3][series.dataIndex - 1]).toFixed(2);
				if(val > 0) {
					opt += val + "% <font style='color:red'>↑</font>"
				} else {
					opt += val + "% <font style='color:green'>↓</font>"
				}
			} else {
				opt += 0;
			}
			return opt;
		}
	},
	"bypsjttlfx": {
		4: {
			name: "本日入仓"
		},
		3: {
			name: "本日入仓已妥投"
		},
		5: {
			name: "妥投率",
            unit: "%"
		}
	}
}