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
	<title>数据源管理</title>
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
		<h2><i class="glyphicon glyphicon-user"></i> 数据源列表</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >名称</span> 
					<c:if test="${name == null }">
						<input id="sname" type="text" aria-controls="DataTables_Table_0" />
					</c:if>
					<c:if test="${name != null }">
						<input id="sname" type="text" aria-controls="DataTables_Table_0" value="${name}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<span >URL</span> 
					<c:if test="${url == null }">
						<input id="surl" type="text" aria-controls="DataTables_Table_0" />
					</c:if>
					<c:if test="${url != null }">
						<input id="surl" type="text" aria-controls="DataTables_Table_0" value="${url}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<span >频道名称</span>
					<select id="schannelId">
						<option value="">全部</option>
						<c:forEach items="${channelList}" var="channel">
							<option value="${channel.id}" <c:if test="${channelId == channel.id}">selected</c:if> >${channel.name}</option>
						</c:forEach>
					</select>
				</label>
				<label style="padding-right:30px;">
					<span >标签名称</span>
					<select id="slabelId">
						<option value="">全部</option>
						<c:forEach items="${labelList}" var="label">
							<option value="${label.id}" <c:if test="${labelId == label.id}">selected</c:if> >${label.name}</option>
						</c:forEach>
					</select>
				</label>
				<label style="padding-right:30px;">
					<span >状态</span>
					<select id="sstatus">
				  		<c:choose>
				  			<c:when test="${status == 0}">
				  				<option value ="">全部</option>
				  				<option value ="0" selected="selected">可用</option>
	  							<option value ="1">不可用</option>
				  			</c:when>
				  			<c:when test="${status == 1}">
				  				<option value ="">全部</option>
				  				<option value ="0">可用</option>
	  							<option value ="1"  selected="selected">不可用</option>
				  			</c:when>
				  			<c:otherwise>
				  				<option value ="" selected="selected">全部</option>
				  				<option value ="0">可用</option>
	  							<option value ="1">不可用</option>
				  			</c:otherwise>
				  		</c:choose>
					</select>
				</label>
				<label style="padding-right:30px;">
					<input id="selectDataSource" type="button" class="btn btn-submit" data-dismiss="modal" value="查询"/>
				</label>
			</div>
		
			<thead>
				<div class="alert alert-info">需要添加新的数据源点击: <a href="#" data-toggle="modal" data-target="#newSource">新增数据源</a>
				</div>
				<tr>
					<th>ID</th>
					<th>名称</th>
					<th>URL</th>
					<th>频道</th>
					<th>标签</th>
					<th>状态</th>
					<th>创建时间</th>
					<th>更新时间</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="source">
				<tr>
					<td>${source.id}</td>
					<td>${source.name}</td>
					<td>${source.url}</td>
					<td>
						<c:forEach items="${channelList}" var="channel">
				  			<c:if test="${source.channelId == channel.id}">${channel.name}</c:if>
			  			</c:forEach>
					</td>
					<td>
						<c:forEach items="${labelList}" var="label">
				  			<c:if test="${source.labelId == label.id}">${label.name}</c:if>
			  			</c:forEach>
					</td>
					<td>
						<c:if test="${source.status == 0}">可用</c:if>
						<c:if test="${source.status != 0}">不可用</c:if>
					</td>
					<td>
						<fmt:formatDate value="${source.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${source.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<a class="btn btn-danger" href="#" data-toggle="modal" data-target="#deleteSource" onclick="selectId(${source.id})">
				        	 <i class="glyphicon glyphicon-trash icon-white"></i>删除
				        </a>
				        <a class="btn btn-info" href="source/edit?id=${source.id}">
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

<div class="modal fade" id="newSource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建数据源</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>数据源配置</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
			  		<td><span>数据源名字</span><input id="name" type="text" class="form-control" /></td>
			  		<td><span>数据源URL</span><input id="url" type="text" class="form-control" /></td>
			  		<td>
			  			<span>数据源频道</span>
						<select id="cid" class="form-control" >
							<option value="">---</option>
			  				<c:forEach items="${channelList}" var="channel">
				  				<option value="${channel.id}">${channel.name}</option>
			  				</c:forEach>
			  			</select>
					</td>
					<td>
						<span>数据源标签</span>
						<select id="lid" class="form-control" >
							<option value="">---</option>
			  				<c:forEach items="${labelList}" var="label">
				  				<option value="${label.id}">${label.name}</option>
			  				</c:forEach>
			  			</select>
					</td>
			  		<td>
			  			<span>数据源状态</span>
			  			<select id="status"  class="form-control" >
			  				<option value="0">可用</option>
			  				<option value="1">不可用</option>
			  			</select>
			  		</td>
				</tr>
			</tbody>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewSource" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<div class="modal fade" id="deleteSource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">确认删除</h4>
		</div>
		<div class="modal-body">
      		<button id="delSourceConfirm" class="btn btn-primary" type="button">确定</button>
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

$('#addNewSource').click(function(event) {
	post('source/add',
		'name='+$("#name").val()+'&status='+$('#status').val()
		+'&url='+encodeURIComponent($("#url").val())+'&cid='+$("#cid").val()+'&lid='+$("#lid").val(), 
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>source/list';
			}
			else {
				alert('添加数据源失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delSourceConfirm').click(function(event) {
	post('source/del',
		'id='+selectedId,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>source/list';
			}
			else {
				alert('删除数据源. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#selectDataSource').click(function(event) {
	location.href = '<%=basePath%>source/list?name='+$("#sname").val()+'&cid='+$('#schannelId').val()
		+'&lid='+$('#slabelId').val()+'&status='+$('#sstatus').val()+'&url='+$('#surl').val();
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
