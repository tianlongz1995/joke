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
	<title>渠道-频道管理</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	<script src="ui/js/jquery.form.js"></script>

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
		<li><a href="distributor/channelList">渠道-频道管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 渠道-频道列表</h2>
	</div>
	<div class="box-content">
		<table class="table table-hover">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >渠道</span>
					<c:if test="${empty distributorName}">
						<input id="distributorName" type="text" aria-controls="DataTables_Table_0" />
					</c:if>
					<c:if test="${!empty distributorName}">
						<input id="distributorName" type="text" aria-controls="DataTables_Table_0" value="${distributorName}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<span >频道</span>
					<select id="channelId">
						<option value="">全部</option>
						<c:forEach items="${channelList}" var="channel">
							<option value="${channel.id}" <c:if test="${channelId == channel.id}">selected</c:if> >${channel.name}</option>
						</c:forEach>
					</select>
				</label>
				<label style="padding-right:30px;">
					<input id="selectDistributorChannel" type="button" class="btn btn-submit" data-dismiss="modal" value="查询"/>
				</label>
			</div>
			
			<thead>
				<div class="alert alert-info">
					需要添加新的渠道-频道: 
					<a href="#" data-toggle="modal" data-target="#newDistributorChannel">
						<span >单个</span>
					</a>
					<a href="#" data-toggle="modal" data-target="#newBatchDistributorChannel">
						<span >批量</span>
					</a>
				</div>
				<tr>
					<th>渠道名称</th>
					<th>频道名称</th>
					<th>展示周期</th>
					<th>排序值</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="distributorChanel">
				<tr>
					<td>${distributorChanel.distributorName}</td>
					<td>${distributorChanel.channelName}</td>
					<td>
						<c:if test="${distributorChanel.start==0 && distributorChanel.end ==23 }">全天</c:if>
						<c:if test="${distributorChanel.start!=0 || distributorChanel.end !=23 }">
							${distributorChanel.start}点-${distributorChanel.end}点
						</c:if>
					</td>
					<td>${distributorChanel.sort}</td>
					<td>
						<a class="btn btn-danger" href="#" data-toggle="modal" data-target="#deleteDistributorChannel" 
							onclick="selectId(${distributorChanel.distributorId},${distributorChanel.channelId})">
				        	 <i class="glyphicon glyphicon-trash icon-white"></i>删除
				        </a>
				        <a class="btn btn-info" href="distributor/channelEdit?did=${distributorChanel.distributorId}&cid=${distributorChanel.channelId}">
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

<div class="modal fade" id="newDistributorChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">新建渠道-频道</h4>
		</div>
		<div class="modal-body">
			<table id="orders-table" class="table table-hover">
			<thead>
				<tr>
			  		<th>渠道-频道配置</th>
				</tr>
		  	</thead>
			<tbody>
				<tr>
			  		<th>渠道名称</th>
			  		<td>
			  			<select id="distributor" class="form-control" >
			  				<c:forEach items="${distributorList}" var="distributor">
				  				<option value="${distributor.id}">${distributor.name}</option>
			  				</c:forEach>
			  			</select>
			  		</td>
			  	</tr>
			  	<tr>
			  		<th>频道名称</th>
			  		<td>
			  			<select id="channel" class="form-control" >
			  				<c:forEach items="${channelList}" var="channel">
				  				<option value="${channel.id}">${channel.name}</option>
			  				</c:forEach>
			  			</select>
			  		</td>
			  	</tr>
			  	<tr>
			  		<th>开始时间</th>
			  		<td>
			  			<select id="start" class="form-control" >
				  			<option value="0">0点</option>
				  			<option value="1">1点</option>
				  			<option value="2">2点</option>
				  			<option value="3">3点</option>
				  			<option value="4">4点</option>
				  			<option value="5">5点</option>
				  			<option value="6">6点</option>
				  			<option value="7">7点</option>
				  			<option value="8">8点</option>
				  			<option value="9">9点</option>
				  			<option value="10">10点</option>
				  			<option value="11">11点</option>
				  			<option value="12">12点</option>
				  			<option value="13">13点</option>
				  			<option value="14">14点</option>
				  			<option value="15">15点</option>
				  			<option value="16">16点</option>
				  			<option value="17">17点</option>
				  			<option value="18">18点</option>
				  			<option value="19">19点</option>
				  			<option value="20">20点</option>
				  			<option value="21">21点</option>
				  			<option value="22">22点</option>
				  			<option value="23">23点</option>
			  			</select>
			  		</td>
			  	</tr>
			  	<tr>
			  		<th>结束时间</th>
			  		<td>
			  			<select id="end" class="form-control" >
				  			<option value="23">23点</option>
				  			<option value="22">22点</option>
				  			<option value="21">21点</option>
				  			<option value="20">20点</option>
				  			<option value="19">19点</option>
				  			<option value="18">18点</option>
				  			<option value="17">17点</option>
				  			<option value="16">16点</option>
				  			<option value="15">15点</option>
				  			<option value="14">14点</option>
				  			<option value="13">13点</option>
				  			<option value="12">12点</option>
				  			<option value="11">11点</option>
				  			<option value="10">10点</option>
				  			<option value="9">9点</option>
				  			<option value="8">8点</option>
				  			<option value="7">7点</option>
				  			<option value="6">6点</option>
				  			<option value="5">5点</option>
				  			<option value="4">4点</option>
				  			<option value="3">3点</option>
				  			<option value="2">2点</option>
				  			<option value="1">1点</option>
				  			<option value="0">0点</option>
			  			</select>
			  		</td>
			  	</tr>
			  	<tr>
			  		<th>排序值</th>
			  		<td>
			  			<input id="sort" type="text" class="form-control" placeholder="从小到大"/>
			  		</td>
				</tr>
			</tbody>
	  		</table>
		</div>
		<div class="modal-footer">
			<button id="addNewDistributorChannel" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
		</div>
		</div>
	</div>
</div>

<div class="modal fade" id="newBatchDistributorChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<form id="batchAddForm" action="<%=basePath%>distributor/channelAddBatch?${_csrf.parameterName}=${_csrf.token}" method="post" enctype="application/x-www-form-urlencoded"> 
		<div class="modal-dialog">
			<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title" id="myModalLabel">新建渠道-频道</h4>
			</div>
			<div class="modal-body">
				<table id="orders-table" class="table table-hover">
				<thead>
					<tr>
				  		<th>渠道-频道配置</th>
					</tr>
			  	</thead>
				<tbody>
					<tr>
				  		<th>渠道名称</th>
				  		<td>
				  			<select name="distributorBatch" class="form-control" >
				  				<c:forEach items="${distributorList}" var="distributor">
					  				<option value="${distributor.id}">${distributor.name}</option>
				  				</c:forEach>
				  			</select>
				  		</td>
				  	</tr>
				  	<tr>
				  		<table>
				  			<thead>
				  				<tr>
									<th>选中</th>
									<th>频道名称</th>
									<th>开始时间</th>
									<th>结束时间</th>
									<th>移动</th>
								</tr>
				  			</thead>
				  			<tbody>
				  				<c:forEach items="${channelList}" var="channel">
				  					<tr>
				  						<td>
				  							<input type="checkbox" name="channelIdBatch" value="${channel.id}" />
				  						</td>
				  						<td><c:out value="${channel.name}"/></td>
				  						<td>
				  							<select name="startBatch" class="form-control" >
									  			<option value="0">0点</option>
									  			<option value="1">1点</option>
									  			<option value="2">2点</option>
									  			<option value="3">3点</option>
									  			<option value="4">4点</option>
									  			<option value="5">5点</option>
									  			<option value="6">6点</option>
									  			<option value="7">7点</option>
									  			<option value="8">8点</option>
									  			<option value="9">9点</option>
									  			<option value="10">10点</option>
									  			<option value="11">11点</option>
									  			<option value="12">12点</option>
									  			<option value="13">13点</option>
									  			<option value="14">14点</option>
									  			<option value="15">15点</option>
									  			<option value="16">16点</option>
									  			<option value="17">17点</option>
									  			<option value="18">18点</option>
									  			<option value="19">19点</option>
									  			<option value="20">20点</option>
									  			<option value="21">21点</option>
									  			<option value="22">22点</option>
									  			<option value="23">23点</option>
								  			</select>
				  						</td>
				  						<td>
				  							<select name="endBatch" class="form-control" >
									  			<option value="23">23点</option>
									  			<option value="22">22点</option>
									  			<option value="21">21点</option>
									  			<option value="20">20点</option>
									  			<option value="19">19点</option>
									  			<option value="18">18点</option>
									  			<option value="17">17点</option>
									  			<option value="16">16点</option>
									  			<option value="15">15点</option>
									  			<option value="14">14点</option>
									  			<option value="13">13点</option>
									  			<option value="12">12点</option>
									  			<option value="11">11点</option>
									  			<option value="10">10点</option>
									  			<option value="9">9点</option>
									  			<option value="8">8点</option>
									  			<option value="7">7点</option>
									  			<option value="6">6点</option>
									  			<option value="5">5点</option>
									  			<option value="4">4点</option>
									  			<option value="3">3点</option>
									  			<option value="2">2点</option>
									  			<option value="1">1点</option>
									  			<option value="0">0点</option>
								  			</select>
				  						</td>
				  						<td>
				  							<input type="button" class="btn btn-default" onclick="up(this)" value="上移" /> 
											<input type="button" class="btn btn-default" onclick="down(this)" value="下移" />
				  						</td>
				  					</tr>
					  			</c:forEach>
				  			</tbody>
				  		</table>
				  	</tr>
				</tbody>
		  		</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="submitBatch()">提交</button>
			</div>
			</div>
		</div>
	</form>
</div>

<div class="modal fade" id="deleteDistributorChannel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			<h4 class="modal-title" id="myModalLabel">确认删除</h4>
		</div>
		<div class="modal-body">
      		<button id="delDistributorChannelConfirm" class="btn btn-primary" type="button">确定</button>
      		<button data-dismiss="modal" class="btn btn-primary" type="button">取消</button>
		</div>
	</div>
	</div>
</div>

<script type="text/javascript">
var distributorId = '',channelId = '';

function selectId(id1,id2) {
	distributorId = id1;
	channelId = id2;
}

function up(obj) {
	var objParentTR = $(obj).parent().parent();
	var prevTR = objParentTR.prev();
	if (prevTR.length > 0) {
		prevTR.insertAfter(objParentTR);
	}
}
function down(obj) {
	var objParentTR = $(obj).parent().parent();
	var nextTR = objParentTR.next();
	if (nextTR.length > 0) {
		nextTR.insertBefore(objParentTR);
	}
} 

function submitBatch() {
	$(":checkbox").each(function(){
	    if(this.checked == false){
	    	$(this).parent().parent().remove();
	    }
	});
	$("#batchAddForm").ajaxSubmit(function(){
		window.location.href = '<%=basePath%>distributor/channelList';
	});
} 

$('#addNewDistributorChannel').click(function(event) {
	post('distributor/channelAdd',
		'did='+$("#distributor").val()+'&cid='+$('#channel').val()+'&start='+$('#start').val()+'&end='+$('#end').val()+'&sort='+$('#sort').val(), 
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>distributor/channelList';
			}
			else {
				alert('添加渠道-频道失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#delDistributorChannelConfirm').click(function(event) {
	post('distributor/channelDel',
		'did='+distributorId+'&cid='+channelId,
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>distributor/channelList?dname='+$("#distributorName").val()+'&cid='+$('#channelId').val();
			}
			else {
				alert('删除渠道-频道. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

$('#selectDistributorChannel').click(function(event) {
	location.href = '<%=basePath%>distributor/channelList?dname='+$("#distributorName").val()+'&cid='+$('#channelId').val();
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
