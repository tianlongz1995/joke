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
	<title>渠道管理</title>
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
		<h2><i class="glyphicon glyphicon-user"></i> 渠道列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			需要添加新的渠道点击: 
					<a href="#" data-toggle="modal" data-target="#newDistributor">新增渠道</a>
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >状态</span>
					<select id="status">
						<option value="">全部</option>
						<option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >下线</option>
						<option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >上线</option>
					</select>
				</label>
				<label style="padding-right:30px;">
					<a class="btn btn-primary" href="#" id="selectDistributorList">
						<span class="glyphicon glyphicon-search icon-white" >查询</span>
					</a>
				</label>
			</div>
		
			<thead>
				<tr>
					<th>ID</th>
					<th>渠道名称</th>
					<th>渠道状态</th>
					<th>创建时间</th>
					<th>更新时间</th>
					<th>操作</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="distributor">
					<tr>
						<td><c:out value="${distributor.id}"/> </td>
						<td><c:out value="${distributor.name}"/></td>
						<td>
							<c:if test="${distributor.status == 0}">
								下线
							</c:if>
							<c:if test="${distributor.status == 1}">
								上线
							</c:if>
						</td>
						<td>
							<fmt:formatDate value="${distributor.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${distributor.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<c:if test="${distributor.status == 0}">
								<a class="btn btn-success" href="#" onclick="modifyStatus(1,${distributor.id})">
									<i class="glyphicon glyphicon-ok icon-white"></i>上线
								</a>
							</c:if>
							<c:if test="${distributor.status == 1}">
								<a class="btn btn-danger" href="#" onclick="modifyStatus(0,${distributor.id})">
									<i class="glyphicon glyphicon-remove icon-white"></i>下线
								</a>
							</c:if>
							<a class="btn btn-info" href="distributor/edit?id=${distributor.id}">
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

<div class="modal fade" id="newDistributor" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建渠道</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
				<tr>
					<th>名称</th>
					<td><input id="addname" type="text" class="form-control" placeholder="渠道名称"/></td>
				</tr>
				<tr>
					<th>状态</th>
					<td>
			  			<select id="addstatus"  class="form-control">
			  				<option value="0">下线</option>
			  				<option value="1">上线</option>
			  			</select>
			  		</td>
				</tr>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewDistributor" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$('#addNewDistributor').click(function(event) {
	post('distributor/add',
		'name='+$("#addname").val()+'&status='+$('#addstatus').val(),
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>distributor/list?status='+$("#status").val();
			}else {
				alert('添加失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

function modifyStatus(status,id) {
	post('distributor/modifyStatus',
			'id='+id+'&status='+status, 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>distributor/list?status='+$("#status").val();
				}
				else {
					alert('操作失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
}

$('#selectDistributorList').click(function(event) {
	location.href = '<%=basePath%>distributor/list?status='+$("#status").val();
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
