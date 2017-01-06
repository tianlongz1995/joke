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
	<title>缓存数据管理</title>
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
<div class="row">
	<div class="box col-md-12">
		<div class="box-inner">
			<div class="box-header well" data-original-title="">
				<h2><i class="glyphicon glyphicon-user"></i> 缓存数据管理</h2>
			</div>
			<div class="row" >
				<div class="bs-example" data-example-id="block-btns">
					<div class="well center-block" style="max-width: 400px;">
						<button data-toggle="modal" data-target="#distributorAdCacheModal" type="button" class="btn btn-primary btn-lg btn-block">渠道广告配置缓存更新</button>
					</div>
				</div>
			</div>
		</div>
	</div><!-- box col-md-12 end -->
</div><!-- row end -->
<div class="modal fade bs-example-modal-sm" id="distributorAdCacheModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h2 class="modal-title" style="color: #ff0906;"><i class="glyphicon glyphicon-exclamation-sign"></i>严重警告</h2>
			</div>
			<div class="modal-body">
				即将刷新渠道广告配置缓存，可能导致前端接口数据异常，请谨慎操作！
				<input id="managerId" type="text" class="form-control" placeholder="数据管理中心密码"/>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-primary btn-sm" id="dataManager">更新缓存!</button>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$('#dataManager').click(function(event) {
	var id = $('#channelIds').val();
	post('distributor/updateDistributorAdConfigCache', 'managerKey='+$("#managerId").val(),
		function (data) {
			if(data.status == 1) {
				alert('更新成功:'+data.info);
				location.href = '<%=basePath%>distributor/list';
			} else {
				alert('更新失败:'+data.info);
			}
		}, function () {
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