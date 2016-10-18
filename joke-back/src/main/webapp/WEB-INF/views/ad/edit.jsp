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
	<title>广告编辑</title>
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
		<li><a href="ad/list">广告管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 广告编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>
					<td><input id="adid" type="text" class="form-control" disabled="disabled" value="${ad.id}"/></td>
				</tr>
				<tr>
					<th>广告位ID</th>
					<td><input id="slotId" type="text" class="form-control" value="${ad.slotId}"/></td>
				</tr>
				<tr>
					<th>广告状态</th>
					<td>
						<select id="status" class="form-control" >
							<option value="0" <c:if test="${!empty ad && !empty ad.status && ad.status == 0}">selected</c:if> >下线</option>
							<option value="1" <c:if test="${!empty ad && !empty ad.status && ad.status == 1}">selected</c:if> >上线</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>发布渠道</th>
					<td>
						<input type="text" class="form-control" disabled="disabled" value="${ad.dName}"/>
						<input type="hidden" id="distributors" value="${ad.did}"/>
					</td>
					<%--<td>
						&lt;%&ndash;<input id="dName" type="text" class="form-control" value="${ad.dName}"/>&ndash;%&gt;
						<select id="distributors" class="form-control" style="font-size: 16px;width: 300px;margin: 3px;" >
							<c:forEach items="${dList}" var="distributor" varStatus="status">
								<option value='<c:out value="${distributor.id}"/>' <c:if test="${!empty ad.did && ad.did == distributor.id}">selected</c:if> ><c:out value="${distributor.name}"/></option>
							</c:forEach>
						</select>
					</td>--%>
				</tr>
				<tr id="adStatusTr">
					<th>广告位位置</th>
					<td>
						<select id="pos" class="form-control" disabled="disabled">
							<option value="1" <c:if test="${!empty ad && !empty ad.pos && ad.pos == 1}">selected</c:if> >列表页中间</option>
							<option value="2" <c:if test="${!empty ad && !empty ad.pos && ad.pos == 2}">selected</c:if> >详情页插屏</option>
							<option value="3" <c:if test="${!empty ad && !empty ad.pos && ad.pos == 3}">selected</c:if> >详情页上方</option>
							<option value="4" <c:if test="${!empty ad && !empty ad.pos && ad.pos == 4}">selected</c:if> >详情页中部</option>
							<option value="5" <c:if test="${!empty ad && !empty ad.pos && ad.pos == 5}">selected</c:if> >详情页底部</option>
						</select>
					</td>
				</tr>
				<c:if test="${!empty ad.pos && ad.pos == 1}">
					<tr>
						<th>广告频率</th>
						<td><input id="slide" type="text" class="form-control" value="${ad.slide}"/></td>
					</tr>
				</c:if>
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
	$('#updateAd').attr("disabled","disabled");
	var slide = $('#slide').val();
	if(slide == null || slide == undefined){
		slide = '';
	}
	var slotId = $('#slotId').val();

	if(!isInteger(slotId)){
		alert("广告位置ID只能是整数");
		return false;
	}
	var pos = $('#pos').val();
	if(pos == 1){
		if(!isInteger(slide) || slide < 1 || slide > 99){
			alert("投放频率必须是大于0或者小于100的正整数");
			return false;
		}
	}
	post('ad/update',
			'id='+$("#adid").val()+'&slotId='+slotId+'&pos='+$('#pos').val()+'&slide='+slide+'&did='+$('#distributors').val()+'&status='+$('#status').val(),
			function (data) {
				if(data.status == 1) {
					location.href = '<%=basePath%>ad/list';
				}
				else {
					alert('更新失败:'+data.info);
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
$('#pos').change(function (event) {
	var pos = $("#pos").val();
	if(pos == 1){
		$("#adStatusTr").next().remove();
		$("#adStatusTr").after('<tr><th>投放频率</th><td><input id="slide" type="text" class="input-sm" style="width:50px;" />（内容）+ 1（广告）</td></tr>');
	}else{
		$("#adStatusTr").next().remove();
	}
});
function isInteger(x) {
	if(x == null || x.trim().length < 1){
        return false;
     }
     var n = Number(x);
     return n >= -2147483648 && n <= 2147483647;
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