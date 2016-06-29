<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>美图</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

	<!-- The fav icon -->
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
<jsp:include page="../common/topbar.jsp" />
<div class="ch-container">
<div class="row">
<jsp:include page="../common/leftmenu.jsp" />
<noscript>
	<div class="alert alert-block col-md-12">
		<h4 class="alert-heading">Warning!</h4>
		<p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled to use this site.
		</p>
	</div>
</noscript>

<!-- content start -->
<div id="content" class="col-lg-10 col-sm-10">
<div>
	<ul class="breadcrumb">
		<li><a href="#">后台管理</a></li>
		<li><a href="#">用户管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 用户列表</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
		<thead>
			<div class="alert alert-info">需要添加新用户点击: <a href="#" data-toggle="modal" data-target="#newUser">新增用户</a>
			</div>
			<tr>
				<th>用户名</th>
				<th>用户权限</th>
				<th>创建时间</th>
				<th>更新密码</th>
  				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<th>删除该用户</th>
				</sec:authorize>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${users}" var="user">
			<tr>
				<td>${user.username}</td>
				<td>${user.authority}</td>
				<td>
					<fmt:formatDate value="${user.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<td>
						<a href="#" data-toggle="modal" data-target="#changePwd" onclick="selectUser('${user.username}')">更新密码</a>
					</td>
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_USER')">
					<c:choose>
						<c:when test="${user.username == username}">
				  			<td>
				  				<a href="#" data-toggle="modal" data-target="#changePwd" onclick="selectUser('${user.username}')">更新密码</a>
				  			</td>
						</c:when>
						<c:otherwise>
							<td></td>
						</c:otherwise>
					</c:choose>
				</sec:authorize>
				<c:choose>
					<c:when test="${user.username != username}">
						<sec:authorize access="hasRole('ROLE_ADMIN')">
							<td>
								<a href="#" data-toggle="modal" data-target="#deleteUser" onclick="selectUser('${user.username}')">删&nbsp;&nbsp;&nbsp;&nbsp;除</a>
							</td>
				  		</sec:authorize>
						<sec:authorize access="hasRole('ROLE_ROLE')">
							<td></td>
						</sec:authorize>
					</c:when>
					<c:otherwise>
						<td></td>
					</c:otherwise>
				</c:choose>
			</tr>
			</c:forEach>
		</tbody>
		</table>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<div class="modal fade" id="newUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建用户</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>用户名</th>
			  		<th>密码</th>
			  		<th>权限</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
			  		<td><input id="username" type="text" class="form-control" placeholder="输入用户名"/></td>
			  		<td><input id="password" type="password" class="form-control" placeholder="输入密码"/></td>
			  		<td>
						<select id="authority" class="form-control">
			  				<option value="ROLE_USER">用户</option>
			  				<option value="ROLE_ADMIN">管理员</option>
			  			</select>
			  		</td>
				</tr>
			</tbody>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewUser" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<div class="modal fade" id="changePwd" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">修改密码</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>新密码</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
					<td><input id="c_password" type="password" class="form-control" placeholder="输入新密码"/></td>
				</tr>
			</tbody>
			</table>
		</div>
		<div class="modal-footer">
			<button id="updatePwd" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
	</div>
	</div>
</div>

<div class="modal fade" id="deleteUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">删除用户</h4>
		</div>
		<div class="modal-body">
      		<button id="delUserConfirm" class="btn btn-primary" type="button">确定</button>
      		<button data-dismiss="modal" class="btn btn-primary" type="button">取消</button>
		</div>
	</div>
	</div>
</div>

<script type="text/javascript">
var selectedUsername = '';

function selectUser(username) {
	selectedUsername = username;
}

$('#addNewUser').click(function(event) {
	post('user/add',
		'username='+$("#username").val()+'&password='+$("#password").val()+'&authority='+$("#authority").val(),
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>user';
			}
			else {
				alert('添加用户失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delUserConfirm').click(function(event) {
	post('user/delete',
		'username='+selectedUsername,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>user';
			}
			else {
				alert('删除用户失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#updatePwd').click(function(event) {
	var url = 'user/changePassword';
	post('user/changePassword',
			'username='+selectedUsername+'&password='+$("#c_password").val(),
			function (data) {
				if(data['status']) {
					alert('密码更新成功');
				}
				else {
					alert('密码更新失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
});

function post(url, data, success, error) {
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: 'POST', url: url, data: data, success: success, error: error,
		headers: {'X-CSRF-TOKEN': csrfToken}
	});
}
</script>

</div><!-- content end -->
</div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>
