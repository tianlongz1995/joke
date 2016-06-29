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
		<table class="table table-hover">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >渠道名称</span> 
					<c:if test="${distributorName == null }">
						<input id="distributorName" type="text" aria-controls="DataTables_Table_0" />
					</c:if>
					<c:if test="${distributorName != null }">
						<input id="distributorName" type="text" aria-controls="DataTables_Table_0" value="${distributorName}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<input id="selectDistributor" type="button" class="btn btn-submit" data-dismiss="modal" value="查询"/>
				</label>
			</div>
		
			<thead>
				<div class="alert alert-info">需要添加新的渠道点击: <a href="#" data-toggle="modal" data-target="#newDistributor">新增渠道</a>
				</div>
				<tr>
					<th>ID</th>
					<th>渠道名称</th>
					<th>渠道状态</th>
					<th>广告位ID</th>
					<th>最小图集size限制</th>
					<th>发布周期</th>
					<th>最少图集size</th>
					<th>广告插入间隔</th>
					<th>广告滑动</th>
					<th>创建时间</th>
					<th>更新时间</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="distributor">
				<tr>
					<td>${distributor.id}</td>
					<td>${distributor.name}</td>
					<td>
						<c:if test="${distributor.status == 0}">可用</c:if>
						<c:if test="${distributor.status != 0}">不可用</c:if>
					</td>
					<td>${distributor.slotId}</td>
					<td>${distributor.limitMinPictures}</td>
					<td>
						<c:if test="${distributor.releaseCycle == '0 0 1/1 * * ?'}">1小时一次</c:if>
						<c:if test="${distributor.releaseCycle == '0 0 2/5 * * ?'}">5小时一次</c:if>
						<c:if test="${distributor.releaseCycle == '0 0 3/12 * * ?'}">12小时一次</c:if>
						<c:if test="${distributor.releaseCycle == '0 0 * 1/1 * ?'}">一天两次</c:if>
						<c:if test="${distributor.releaseCycle == '0 0 * 2/2 * ?'}">两天一次</c:if>
					</td>
					<td>${distributor.releaseLimitMinGallerys}</td>
					<td>${distributor.adInsertInterval}</td>
					<td>
						<c:if test="${distributor.adSlide == '0'}">支持</c:if>
						<c:if test="${distributor.adSlide == '1'}">不支持</c:if>
					</td>
					<td>
						<fmt:formatDate value="${distributor.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${distributor.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<a class="btn btn-danger" href="#" data-toggle="modal" data-target="#deleteDistributor" onclick="selectId(${distributor.id})">
				        	 <i class="glyphicon glyphicon-trash icon-white"></i>删除
				        </a>
				        <a class="btn btn-info" href="distributor/edit?id=${distributor.id}">
				        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
				        </a>
				        <c:if test="${distributor.status == 0}">
				        	<a class="btn btn-success" href="#" onclick="publishDistributor('${distributor.id}')">
				        	<i class="glyphicon glyphicon-zoom-in icon-white"></i>手动发布数据
				        </a>
				        </c:if>
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
					<th>渠道名字</th>
					<td><input id="name" type="text" class="form-control" placeholder="渠道名字"/></td>
				</tr>
				<tr>
					<th>广告位Id</th>
					<td><input id="slotId" type="text" class="form-control" placeholder="广告位Id"/></td>
				</tr>
					<th>最小图集size</th>
					<td><input id="limitMinPictures" type="text" class="form-control" placeholder="最小图集size" value="5"/></td>
				</tr>
				<tr>
					<th>发布周期</th>
					<td>
			  			<select id="releaseCycle" class="form-control" placeholder="发布周期">
							<option value ="0 0 1/1 * * ?" <c:if test="${distributor.releaseCycle == '0 0 1/1 * * ?'}">selected</c:if>>1小时一次</option>
							<option value ="0 0 2/5 * * ?" <c:if test="${distributor.releaseCycle == '0 0 2/5 * * ?'}">selected</c:if>>5小时一次</option>
							<option value ="0 0 3/12 * * ?" <c:if test="${distributor.releaseCycle =='0 0 3/12 * * ?'}">selected</c:if>>12小时一次</option>
							<option value ="0 0 * 1/1 * ?" <c:if test="${distributor.releaseCycle == '0 0 * 1/1 * ?'}">selected</c:if>>1天一次</option>
							<option value ="0 0 * 2/2 * ?" <c:if test="${distributor.releaseCycle == '0 0 * 2/2 * ?'}">selected</c:if>>2天一次</option>
						</select>
			  		</td>
				</tr>
				<tr>
					<th>最少图集size</th>
					<td><input id="releaseLimitMinGallerys" type="text" class="form-control" placeholder="最少图集size" value="10"/></td>
				</tr>
				<tr>
					<th>广告插入间隔</th>
					<td><input id="adInsertInterval" type="text" class="form-control" placeholder="广告插入间隔" value="0"/></td>
				</tr>
				<tr>
					<th>广告滑动</th>
					<td>
			  			<select id="adSlide" class="form-control" placeholder="广告滑动">
							<option value ="0" <c:if test="${distributor.adSlide == '0'}">selected</c:if>>支持</option>
							<option value ="1" <c:if test="${distributor.adSlide == '1'}">selected</c:if>>不支持</option>
						</select>
			  		</td>
				</tr>
				<tr>
					<th>渠道状态</th>
					<td>
			  			<select id="status"  class="form-control" placeholder="渠道状态">
			  				<option value="1">不可用</option>
			  				<option value="0">可用</option>
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

<div class="modal fade" id="deleteDistributor" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">确认删除</h4>
		</div>
		<div class="modal-body">
      		<button id="delDistributorConfirm" class="btn btn-primary" type="button">确定</button>
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

$('#addNewDistributor').click(function(event) {
	post('distributor/add',
		'name='+$("#name").val()+'&status='+$('#status').val()+'&slotId='+$('#slotId').val()+'&limitMinPictures='
		+$('#limitMinPictures').val()+'&releaseCycle='+$("#releaseCycle").val()+'&releaseLimitMinGallerys='+$("#releaseLimitMinGallerys").val()
		+'&adInsertInterval='+$("#adInsertInterval").val()+'&adSlide='+$("#adSlide").val(), 
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>distributor/list';
			}
			else {
				alert('添加渠道失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delDistributorConfirm').click(function(event) {
	post('distributor/del',
		'id='+selectedId,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>distributor/list';
			}
			else {
				alert('删除渠道. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#selectDistributor').click(function(event) {
	location.href = '<%=basePath%>distributor/list?dname='+$("#distributorName").val();
});

function publishDistributor(id){
	post('distributor/publish','id='+id,
		function (data) {
			if(data['status']) {
				alert('发布成功.');
				location.href = '<%=basePath%>distributor/list';
			}else {
				alert('发布失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
}

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
