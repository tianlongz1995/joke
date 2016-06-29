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
		<li><a href="source/list">数据源管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 数据源编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
		<thead>
			<tr>
				<th>数据源ID</th>
				<th>数据源名字</th>
				<th>数据源URL</th>
				<th>数据源频道</th>
				<th>数据源标签</th>
				<th>数据源状态</th>
			</tr>
			<tr>
				<td><input id="sourceId" type="text" class="form-control" placeholder="数据源ID"  disabled="disabled" value="${source.id}"/></td>
				<td><input id="name" type="text" class="form-control" placeholder="数据源名字"  value="${source.name}"/></td>
				<td><input id="url" type="text" class="form-control" placeholder="数据源URL"  value="${source.url}"/></td>
				<td>
					<select id="cid" class="form-control" placeholder="数据源频道">
						<option value="">---</option>
		  				<c:forEach items="${channelList}" var="channel">
			  				<option value="${channel.id}" <c:if test="${source.channelId == channel.id}">selected</c:if>>${channel.name}</option>
		  				</c:forEach>
		  			</select>
				</td>
				<td>
					<select id="lid" class="form-control" placeholder="数据源标签">
						<option value="">---</option>
		  				<c:forEach items="${labelList}" var="label">
			  				<option value="${label.id}" <c:if test="${source.labelId == label.id}">selected</c:if>>${label.name}</option>
		  				</c:forEach>
		  			</select>
				</td>
			  	<td>
				  	<select id="status">
				  		<c:choose>
				  			<c:when test="${source.status == 0}">
				  				<option value ="0" selected="selected">可用</option>
	  							<option value ="1">不可用</option>
				  			</c:when>
				  			<c:otherwise>
				  				<option value ="0">可用</option>
	  							<option value ="1" selected="selected">不可用</option>
				  			</c:otherwise>
				  		</c:choose>
					</select>
			  	</td>
			</tr>
		</thead>

		<tbody>
		</tbody>
		</table>
		<button id="updateSource" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$('#updateSource').click(function(event) {
	post('source/update',
			'name='+$("#name").val()+'&id='+$("#sourceId").val()+'&status='+$("#status").val()
			+'&url='+encodeURIComponent($("#url").val())+'&cid='+$("#cid").val()+'&lid='+$("#lid").val(),
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
