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
		<li><a href="distributor/list">渠道-频道管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 渠道-标签编辑</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
		<thead>
			<tr>
				<th>渠道名字</th>
				<td><input id="distributorName" type="text" class="form-control" disabled="disabled" value="${distributorChannel.distributorName}"/></td>
			</tr>
			<tr>
				<th>频道名字</th>
				<td><input id="channelName" type="text" class="form-control" disabled="disabled" value="${distributorChannel.channelName}"/></td>
			</tr>
			<tr>
	  			<th>开始时间</th>
		  		<td>
		  			<select id="start" class="form-control" >
			  			<option value="0" <c:if test="${0 == distributorChannel.start}">selected</c:if>>0点</option>
			  			<option value="1" <c:if test="${1 == distributorChannel.start}">selected</c:if>>1点</option>
			  			<option value="2" <c:if test="${2 == distributorChannel.start}">selected</c:if>>2点</option>
			  			<option value="3" <c:if test="${3 == distributorChannel.start}">selected</c:if>>3点</option>
			  			<option value="4" <c:if test="${4 == distributorChannel.start}">selected</c:if>>4点</option>
			  			<option value="5" <c:if test="${5 == distributorChannel.start}">selected</c:if>>5点</option>
			  			<option value="6" <c:if test="${6 == distributorChannel.start}">selected</c:if>>6点</option>
			  			<option value="7" <c:if test="${7 == distributorChannel.start}">selected</c:if>>7点</option>
			  			<option value="8" <c:if test="${8 == distributorChannel.start}">selected</c:if>>8点</option>
			  			<option value="9" <c:if test="${9 == distributorChannel.start}">selected</c:if>>9点</option>
			  			<option value="10" <c:if test="${10 == distributorChannel.start}">selected</c:if>>10点</option>
			  			<option value="11" <c:if test="${11 == distributorChannel.start}">selected</c:if>>11点</option>
			  			<option value="12" <c:if test="${12 == distributorChannel.start}">selected</c:if>>12点</option>
			  			<option value="13" <c:if test="${13 == distributorChannel.start}">selected</c:if>>13点</option>
			  			<option value="14" <c:if test="${14 == distributorChannel.start}">selected</c:if>>14点</option>
			  			<option value="15" <c:if test="${15 == distributorChannel.start}">selected</c:if>>15点</option>
			  			<option value="16" <c:if test="${16 == distributorChannel.start}">selected</c:if>>16点</option>
			  			<option value="17" <c:if test="${17 == distributorChannel.start}">selected</c:if>>17点</option>
			  			<option value="18" <c:if test="${18 == distributorChannel.start}">selected</c:if>>18点</option>
			  			<option value="19" <c:if test="${19 == distributorChannel.start}">selected</c:if>>19点</option>
			  			<option value="20" <c:if test="${20 == distributorChannel.start}">selected</c:if>>20点</option>
			  			<option value="21" <c:if test="${21 == distributorChannel.start}">selected</c:if>>21点</option>
			  			<option value="22" <c:if test="${22 == distributorChannel.start}">selected</c:if>>22点</option>
			  			<option value="23" <c:if test="${23 == distributorChannel.start}">selected</c:if>>23点</option>
		  			</select>
		  		</td>
		  	</tr>
		  	<tr>
	  			<th>结束时间</th>
		  		<td>
		  			<select id="end" class="form-control" >
			  			<option value="23" <c:if test="${23 == distributorChannel.end}">selected</c:if>>23点</option>
			  			<option value="22" <c:if test="${22 == distributorChannel.end}">selected</c:if>>22点</option>
			  			<option value="21" <c:if test="${21 == distributorChannel.end}">selected</c:if>>21点</option>
			  			<option value="20" <c:if test="${20 == distributorChannel.end}">selected</c:if>>20点</option>
			  			<option value="19" <c:if test="${19 == distributorChannel.end}">selected</c:if>>19点</option>
			  			<option value="18" <c:if test="${18 == distributorChannel.end}">selected</c:if>>18点</option>
			  			<option value="17" <c:if test="${17 == distributorChannel.end}">selected</c:if>>17点</option>
			  			<option value="16" <c:if test="${16 == distributorChannel.end}">selected</c:if>>16点</option>
			  			<option value="15" <c:if test="${15 == distributorChannel.end}">selected</c:if>>15点</option>
			  			<option value="14" <c:if test="${14 == distributorChannel.end}">selected</c:if>>14点</option>
			  			<option value="13" <c:if test="${13 == distributorChannel.end}">selected</c:if>>13点</option>
			  			<option value="12" <c:if test="${12 == distributorChannel.end}">selected</c:if>>12点</option>
			  			<option value="11" <c:if test="${11 == distributorChannel.end}">selected</c:if>>11点</option>
			  			<option value="10" <c:if test="${10 == distributorChannel.end}">selected</c:if>>10点</option>
			  			<option value="9" <c:if test="${9 == distributorChannel.end}">selected</c:if>>9点</option>
			  			<option value="8" <c:if test="${8 == distributorChannel.end}">selected</c:if>>8点</option>
			  			<option value="7" <c:if test="${7 == distributorChannel.end}">selected</c:if>>7点</option>
			  			<option value="6" <c:if test="${6 == distributorChannel.end}">selected</c:if>>6点</option>
			  			<option value="5" <c:if test="${5 == distributorChannel.end}">selected</c:if>>5点</option>
			  			<option value="4" <c:if test="${4 == distributorChannel.end}">selected</c:if>>4点</option>
			  			<option value="3" <c:if test="${3 == distributorChannel.end}">selected</c:if>>3点</option>
			  			<option value="2" <c:if test="${2 == distributorChannel.end}">selected</c:if>>2点</option>
			  			<option value="1" <c:if test="${1 == distributorChannel.end}">selected</c:if>>1点</option>
			  			<option value="0" <c:if test="${0 == distributorChannel.end}">selected</c:if>>0点</option>
		  			</select>
		  		</td>
		  	</tr>
		  	<tr>
		  		<th>排序值</th>
		  		<td>
		  			<c:if test="${empty  distributorChannel.sort}">
			  			<input id="sort" type="text" class="form-control" placeholder="从小到大" />
		  			</c:if>
		  			<c:if test="${!empty  distributorChannel.sort}">
			  			<input id="sort" type="text" class="form-control" placeholder="从小到大" value="${distributorChannel.sort}"/>
		  			</c:if>
		  		</td>
			</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
		<button id="updateDistributorChannel" type="button" class="btn btn-default" data-dismiss="modal"
			onclick="selectId(${distributorChannel.distributorId},${distributorChannel.channelId})">提交</button>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<script type="text/javascript">
var distributorId = '',channelId = '';

function selectId(id1,id2) {
	distributorId = id1;
	channelId = id2;
}

$('#updateDistributorChannel').click(function(event) {
	post('distributor/channelUpdate',
			'did='+distributorId+'&cid='+channelId+'&start='+$('#start').val()+'&end='+$('#end').val()+'&sort='+$('#sort').val(),
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>distributor/channelList';
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
