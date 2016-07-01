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
	<title>内容编辑</title>
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
		<li><a href="distributor/list">内容编辑</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-content">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>标题</th>
					<td>
						<c:if test="${empty joke.title}">
							<input id="title" type="text" class="form-control" value=""/>
						</c:if>
						<c:if test="${!empty joke.title}">
							<input id="title" type="text" class="form-control" value="${joke.title}"/>
						</c:if>
					</td>
				</tr>
				<tr>
					<th>图片 /动图</th>
					<td>
						<c:if test="${joke.type == 2}">
							<img id="img" src="${joke.img}" onfocus="this.src=${joke.gif}" onblur="this.src=${joke.img}"/>
						</c:if>
						<c:if test="${joke.type == 1}">
							<img id="img" src="${joke.img}"/>
						</c:if>
					</td>
				</tr>
				<tr>
		  			<th>内容</th>
			  		<td>
			  			<c:if test="${empty joke.content}">
							<input id="content2" type="text" class="form-control" value=""/>
						</c:if>
						<c:if test="${!empty joke.content}">
							<input id="content2" type="text" class="form-control" value="${joke.content}"/>
						</c:if>
			  		</td>
			  	</tr>
			</thead>
		</table>
		<input id="id" type="hidden" value="${joke.id}"/>
		<input id="imgpath" type="hidden" value=""/>
		<input id="gifpath" type="hidden" value=""/>
		<button id="updateJoke" type="button" class="btn btn-default" data-dismiss="modal">通过</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$('#updateJoke').click(function(event) {
	post('joke/update',
			'id='+$("#id").val()+'&title='+$("#title").val()+'&imgpath='+$("#imgpath").val()
			+'&gifpath='+$("#gifpath").val()+'&content='+$("#content2").val(),
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>joke/list';
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