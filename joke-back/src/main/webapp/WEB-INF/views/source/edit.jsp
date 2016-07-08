<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>内容源编辑</title>
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
		<li><a href="source/list">内容源管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 内容源编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>
					<td><input id="sourceid" type="text" class="form-control" disabled="disabled" value="${source.id}"/></td>
				</tr>
				<tr>
					<th>名称</th>
					<td><input id="name" type="text" class="form-control" value="${source.name}"/></td>
				</tr>
				<tr>
					<th>URL</th>
					<td><input id="url" type="text" class="form-control" value="${source.url}"/></td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
						<select id="status" class="form-control" >
							<option value="0" <c:if test="${!empty source && !empty source.status && source.status == 0}">selected</c:if> >下线</option>
							<option value="1" <c:if test="${!empty source && !empty source.status && source.status == 1}">selected</c:if> >上线</option>
						</select>
					</td>
				</tr>
			</thead>
		</table>
		<div style="text-align: center;">
			<button id="updateAd" type="button" class="btn btn-info" data-dismiss="modal">提交</button>
		</div>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$('#updateAd').click(function(event) {
	post('source/update',
			'id='+$("#sourceid").val()+'&name='+$("#name").val()+'&url='+$('#url').val()+'&status='+$('#status').val(),
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>source/list';
				}
				else {
					alert('更新失败. info:'+data['info']);
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