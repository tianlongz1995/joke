<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>

<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>段子</title>
	<base href="<%=basePath%>">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link id="bs-css" href="ui/charisma/css/bootstrap-cerulean.min.css" rel="stylesheet">
	<link href="ui/charisma/css/charisma-app.css" rel="stylesheet">
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	
	<!-- The fav icon -->
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">

</head>

<body>
<div class="ch-container">
	<div class="row">

	<div class="row">
		<div class="col-md-12 center login-header">
			<h2>欢迎使用段子后台</h2>
		</div>
		<!--/span-->
	</div><!--/row-->

	<div class="row">
		<div class="well col-md-5 center login-box">
			<div class="alert alert-info">
				请输入用户名密码
			</div>
			<c:url value="/login" var="loginUrl"/>
			<form class="form-horizontal" action="${loginUrl}" method="post">
				<fieldset>
					<div class="input-group input-group-lg">
						<span class="input-group-addon"><i class="glyphicon glyphicon-user red"></i></span>
						<input name="username" type="text" class="form-control" placeholder="用户名">
					</div>
					<div class="clearfix"></div><br>

					<div class="input-group input-group-lg">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock red"></i></span>
						<input name="password" type="password" class="form-control" placeholder="密码">
					</div>
					<div class="clearfix"></div>

					<div class="input-prepend">
						<label class="remember" for="remember">
						<input type="checkbox" id="remember" name="remember-me"> Remember me</label>
					</div>
					<div class="clearfix"></div>

					<p class="center col-md-5">
						<button name="submit" type="submit" class="btn btn-primary" value="Login">登陆</button>
					</p>
				</fieldset>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			</form>
		</div>
		<!--/span-->
	</div><!--/row-->
</div><!--/fluid-row-->

</div><!--/.fluid-container-->

<script src="ui/charisma/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- library for cookie management -->
<script src="ui/charisma/js/jquery.cookie.js"></script>
</body>
</html>