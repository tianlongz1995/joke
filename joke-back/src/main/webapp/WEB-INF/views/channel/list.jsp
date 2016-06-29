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
	<title>频道管理</title>
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
		<li><a href="channel/list">频道管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 频道列表</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
		<thead>
			<div class="alert alert-info">需要添加新的频道点击: <a href="#" data-toggle="modal" data-target="#newChannel">新增频道</a>
			</div>
			<tr>
				<th>ID</th>
				<th>频道名称</th>
				<th>频道状态</th>
				<th>创建时间</th>
				<th>更新时间</th>
			</tr>
		</thead>

		<tbody>
			<c:forEach items="${list}" var="channel">
			<tr>
				<td>${channel.id}</td>
				<td>${channel.name}</td>
				<td>
					<c:if test="${channel.status == 0}">可用</c:if>
					<c:if test="${channel.status != 0}">不可用</c:if>
				</td>
				<td>
					<fmt:formatDate value="${channel.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${channel.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<a class="btn btn-danger" href="#" data-toggle="modal" data-target="#deleteChannel" onclick="selectId(${channel.id})">
			        	 <i class="glyphicon glyphicon-trash icon-white"></i>删除
			        </a>
			        <a class="btn btn-info" href="channel/edit?id=${channel.id}">
			        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
			        </a>
			    </td>
			</tr>
			</c:forEach>
		</tbody>
		</table>
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<div class="modal fade" id="newChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建频道</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>频道配置</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
			  		<td><input id="name" type="text" class="form-control" placeholder="频道名字"/></td>
			  		<td>
			  			<select id="status"  class="form-control" placeholder="频道状态">
			  				<option value="0">可用</option>
			  				<option value="1">不可用</option>
			  			</select>
			  		</td>
				</tr>
			</tbody>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewChannel" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<div class="modal fade" id="deleteChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">确认删除</h4>
		</div>
		<div class="modal-body">
      		<button id="delChannelConfirm" class="btn btn-primary" type="button">确定</button>
      		<button data-dismiss="modal" class="btn btn-primary" type="button">取消</button>
		</div>
	</div>
	</div>
</div>

<script type="text/javascript">
var selectedId = '';

function selectId(id) {
	selectedId = id;
}

$('#addNewChannel').click(function(event) {
	post('channel/add',
		'name='+$("#name").val()+'&status='+$('#status').val(), 
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>channel/list';
			}
			else {
				alert('添加频道失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delChannelConfirm').click(function(event) {
	post('channel/del',
		'id='+selectedId,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>channel/list';
			}
			else {
				alert('删除频道. info:'+data['info']);
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
