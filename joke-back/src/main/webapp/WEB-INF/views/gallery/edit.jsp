<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
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
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	<script src="ui/js/jquery.oupeng.upload.js"></script>
	<link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
<jsp:include page="../common/topbar.jsp" />
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
		<li><a href="gallery/list">图片管理</a></li>
	</ul>
</div>
<div class="row">
<div class="box col-md-12">
	<div class="box-content">
		<form:form modelAttribute ="galleryVo" action="gallery/save?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="application/x-www-form-urlencoded"> 
			<form:hidden path="id"/>
			<div class="form-group">
				<form:label path="title" style="color:red">标题</form:label>
				<form:input path ="title" id="title" cssStyle="width: 200px;"/>
				<form:errors path ="title" cssClass ="label label-warning"></form:errors> 
			</div>
			<div class="form-group">
				<form:label path="fontCount" style="color:red">点赞数</form:label>
				<form:input path ="fontCount" id="fontCount" cssStyle="width: 200px;"/>
				<form:errors path ="fontCount" cssClass ="label label-warning"></form:errors> 
			</div>
			<input type="submit" class="btn btn-default" value="保存">
			<input type="button" class="btn btn-default" onclick="clearInput()" value="取消" />
		</form:form>
	</div>


<script type="text/javascript">
	function clearInput(){
		if(confirm("是否放弃本次编辑？")){
			location.href = '<%=basePath%>gallery/list';
		}
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
