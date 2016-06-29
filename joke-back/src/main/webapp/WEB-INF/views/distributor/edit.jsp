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
		<li><a href="distributor/list">渠道管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 渠道编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
		<thead>
			<tr>
				<th>渠道ID</th>
				<th>渠道名字</th>
				<th>广告位Id</th>
				<th>最小图集size</th>
				<th>发布周期</th>
				<th>最少图集size</th>
				<th>广告插入间隔</th>
				<th>广告滑动</th>
				<th>渠道状态</th>
			</tr>
			<tr>
				<td><input id="distributorId" type="text" class="form-control" placeholder="渠道ID"  disabled="disabled" value="${distributor.id}"/></td>
				<td><input id="name" type="text" class="form-control" placeholder="渠道名字"  value="${distributor.name}"/></td>
				<td><input id="slotId" type="text" class="form-control" placeholder="广告位Id"  value="${distributor.slotId}"/></td>
				<td><input id="limitMinPictures" type="text" class="form-control" placeholder="最小图集size"  value="${distributor.limitMinPictures}"/></td>
				<td>
					<select id="releaseCycle">
						<option value ="0 0 1/1 * * ?" <c:if test="${distributor.releaseCycle == '0 0 1/1 * * ?'}">selected</c:if>>1小时一次</option>
						<option value ="0 0 2/5 * * ?" <c:if test="${distributor.releaseCycle == '0 0 2/5 * * ?'}">selected</c:if>>5小时一次</option>
						<option value ="0 0 3/12 * * ?" <c:if test="${distributor.releaseCycle =='0 0 3/12 * * ?'}">selected</c:if>>12小时一次</option>
						<option value ="0 0 * 1/1 * ?" <c:if test="${distributor.releaseCycle == '0 0 * 1/1 * ?'}">selected</c:if>>1天一次</option>
						<option value ="0 0 * 2/2 * ?" <c:if test="${distributor.releaseCycle == '0 0 * 2/2 * ?'}">selected</c:if>>2天一次</option>
					</select>
				</td>
				<td><input id="releaseLimitMinGallerys" type="text" class="form-control" placeholder="最少图集size"  value="${distributor.releaseLimitMinGallerys}"/></td>
				<td><input id="adInsertInterval" type="text" class="form-control" placeholder="广告插入间隔"  value="${distributor.adInsertInterval}"/></td>
			  	<td>
				  	<select id="adSlide">
				  		<c:choose>
				  			<c:when test="${distributor.adSlide == 0}">
				  				<option value ="0" selected="selected">支持</option>
	  							<option value ="1">不支持</option>
				  			</c:when>
				  			<c:otherwise>
				  				<option value ="0">支持</option>
	  							<option value ="1" selected="selected">不支持</option>
				  			</c:otherwise>
				  		</c:choose>
					</select>
			  	</td>
			  	<td>
				  	<select id="status">
				  		<c:choose>
				  			<c:when test="${distributor.status == 0}">
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
		<button id="updateDistributor" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
$('#updateDistributor').click(function(event) {
	post('distributor/update',
			'name='+$("#name").val()+'&id='+$("#distributorId").val()+'&status='+$("#status").val()+'&slotId='+$("#slotId").val()+'&limitMinPictures='
			+$("#limitMinPictures").val()+'&releaseCycle='+$("#releaseCycle").val()+'&releaseLimitMinGallerys='+$("#releaseLimitMinGallerys").val()
			+'&adInsertInterval='+$("#adInsertInterval").val()+'&adSlide='+$("#adSlide").val(),
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>distributor/list';
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
