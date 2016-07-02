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
	<title>快捷查找</title>
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
	<style>
		.table-item{
			overflow: hidden;
		}
	</style>
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
		<li><a href="distributor/list">快捷查找</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 内容列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >ID</span>
					<c:if test="${empty jokeid}">
						<input id="jokeid" type="text" />
					</c:if>
					<c:if test="${!empty jokeid}">
						<input id="jokeid" type="text" value="${jokeid}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<span >关键字</span>
					<c:if test="${empty content}">
						<input id="content2" type="text" style="width:500px;" maxlength="30" />
					</c:if>
					<c:if test="${!empty content}">
						<input id="content2" type="text" style="width:500px;" maxlength="30"   value="${content}"/>
					</c:if>
				</label>
				<label style="padding-right:30px;">
					<a class="btn btn-info" href="#" id="searchJoke">
						<span class="glyphicon glyphicon-search icon-white" >搜索</span>
					</a>
				</label>
				<label style="padding-right:30px;">
			        <a class="btn btn-danger" href="#" onclick="deleteJoke('batch')">
			        	 <i class="glyphicon glyphicon-remove icon-white"></i>批量删除
			        </a>
				</label>
			</div>
		
			<thead>
				<tr>
					<th>全选<input type="checkbox" id="allcheck" /></th>
					<th>ID</th>
					<th>内容</th>
					<th>格式</th>
					<th>操作</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="joke">
				<tr>
					<td><input type="checkbox" name="jokeid" value="${joke.id}"/></td>
					<td><c:out value="${joke.id}"/></td>
					<td>
						<div class="table-item" style="height:60px">
							<c:if test="${!empty joke.title}">
								<p><h5>${joke.title}</h5></p>
							</c:if>
							<c:if test="${!empty joke.content}">
								<p><small>${joke.content}</<small>></p>
							</c:if>
							<c:if test="${joke.type == 2}">
								<p><img src="${joke.img}" data-origin="${joke.img}" data-src="${joke.gif}" /></p>
							</c:if>
							<c:if test="${joke.type == 1}">
								<p><img src="${joke.img}"/></p>
							</c:if>
						</div>
					</td>
					<td>
						<c:if test="${joke.type == 0}">文字</c:if>
						<c:if test="${joke.type == 1}">图片</c:if>
						<c:if test="${joke.type == 2}">动图</c:if>
					</td>
					<td>
						<a class="btn btn-danger" href="#" onclick="deleteJoke(${joke.id})">
				        	 <i class="glyphicon glyphicon-remove icon-white"></i>删除
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

<script type="text/javascript">
$('#table_list img').hover(function(){
	var gif = $(this).attr('data-src');
	if(gif){
		this.src = gif;
	}
}, function(){
	var origin = $(this).attr('data-origin');
	if(origin){
		this.src = origin;
	}
});

$('.table-item').hover(function(){
	$(this).removeAttr("style");
}, function(){
	$(this).attr("style","height:60px");
});

$('#allcheck').on('click', function(){
	if ($(this).prop("checked")) {
        $(":checkbox").prop("checked", true);
    }else {
        $(":checkbox").prop("checked", false);
    }
});

function deleteJoke(id) {
	if("batch" == id){
		var ids = [];
		$('input[name="jokeid"]:checked').each(function(){
			ids.push($(this).val());
			});
		if(ids.length == 0){
			alert("未选中任何内容");
			return false;
		}
		id = ids.toString();
	}
	
	post('joke/verify',
			'ids='+id+'&status=2', 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>joke/search?jokeid='+$("#jokeid").val()+'&content='+$("#content2").val();
				}
				else {
					alert('审核失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
}

$('#searchJoke').click(function(event) {
	location.href = '<%=basePath%>joke/search?jokeid='+$("#jokeid").val()+'&content='+$("#content2").val();
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
