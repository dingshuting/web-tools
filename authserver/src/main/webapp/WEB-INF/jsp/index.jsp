<%@page import="com.ijs.core.base.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="static/css/style.css" type="text/css" media="all" />
	<!-- Style-CSS -->
<link rel="stylesheet" href="static/css/fontawesome-all.css">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="static/jquery.cookie.js"></script>
</head>
<body>
	<div id="auto_login">
		<center id="message"></center>
		<form action="login_auth" id="loginform" method="post" id="wx_form">
			<input type="hidden" name="username" />
			
		</form>
	</div>
	<div id="unify_login" style="display: none">
		<!-- bg effect -->
		<div id="bg">
			<canvas></canvas>
			<canvas></canvas>
			<canvas></canvas>
		</div>
		<!-- //bg effect -->
		<!-- title -->
		<h1>亿达控股</h1>
		<!-- //title -->
		<!-- content -->
		<div class="sub-main-w3">
			<form action="login_auth" method="post">
				<h2>认证中心
					<i class="fas fa-level-down-alt"></i>
				</h2>
				<div class="form-style-agile">
					<label>
						<i class="fas fa-user"></i>
						用户名
					</label>
					<input placeholder="Username" name="username" type="text" required="">
					<input type="hidden" name="ajax" value="1" />
				</div>
				<div class="form-style-agile">
					<label>
						<i class="fas fa-unlock-alt"></i>
						密&nbsp;&nbsp;码
					</label>
					<input placeholder="Password" name="password" type="password">
				</div>
				<!-- checkbox -->
				<div class="wthree-text">
					<ul>
						<!--<li>
							<label class="anim">
								<input type="checkbox" class="checkbox" required="">
								<span>Stay Signed In</span>
							</label>
						</li>
					 	<li>
							<a href="#">Forgot Password?</a>
						</li> -->
					</ul>
				</div>
				<!-- //checkbox -->
				<input type="submit" value="登&nbsp;&nbsp;&nbsp;&nbsp;录">
			</form>
		</div>
		<!-- //content -->
	
		<!-- copyright -->
		<div class="footer">
			<p>Copyright &copy; 2018.亿达控股  All rights reserved.</a></p>
		</div>
	</div>
	<script>
		if(window.location.href.indexOf("error")>-1){
			document.getElementById("message").innerHTML="认证异常";
			$("#unify_login").show();
		}else{
			var openid=$.cookie('YIDAOPENID');
			if(openid&&openid!=""){
				$("input[ name='username']").val("wx_"+openid);
				document.getElementById("loginform").submit();
			}else{
				$("#unify_login").show();
				//document.getElementById("message").innerHTML="获取授权失败，请联系管理员";
			}
		}
	</script>
	<script src="static/js/canva_moving_effect.js"></script>
</body>
</html>
