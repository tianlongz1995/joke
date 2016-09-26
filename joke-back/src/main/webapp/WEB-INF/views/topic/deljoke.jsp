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
	<title>添加专题内容</title>
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
		<h2><i class="glyphicon glyphicon-user"></i> 添加专题内容</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			<a href="<%=basePath%>topic/list?status=${status}&topicCoverId=${topicCoverId}&pageSize=${pSize}&pageNumber=${pNumber}">
				<i class="glyphicon glyphicon-arrow-left"></i> 返回专题列表
			</a>
		</div>
		<table id="table_list"  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
			    <label style="padding-right:30px;">
			        <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke('batch')">
			        	 <i class="glyphicon glyphicon-remove icon-white"></i>批量删除
			        </a>
				</label>
			</div>
			<tr>
				<th>全选<input type="checkbox" id="allcheck" /></th>
				<th>内容</th>
				<th>类型</th>
				<th>审核通过时间</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="joke">
				<tr>
					<td><input type="checkbox" name="jokeid" value="${joke.id}"/></td>
					<td>
						<div class="table-item"  style="margin: 0px;padding: 0px;width: 100%;height: 100%;top:0px;bottom:0px;min-width: 100%;min-height: 50px;" <c:if test="${joke.type == 1}">data-origin="${joke.img}"</c:if> <c:if test="${joke.type == 2}">data-src="${joke.gif}"</c:if> >
							<c:if test="${!empty joke.title}">
								<p><h5>${joke.title}</h5></p>
							</c:if>
							<c:if test="${!empty joke.content}">
								<p><small>${joke.content}</small></p>
							</c:if>
						</div>
					</td>
					<td>
						<c:if test="${joke.type == 0}">文字</c:if>
						<c:if test="${joke.type == 1}">图片</c:if>
						<c:if test="${joke.type == 2}">动图</c:if>
					</td>
					<td>
						<fmt:formatDate value="${joke.verifyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(${joke.id})">
							 <i class="glyphicon glyphicon-remove icon-white"></i>删除
						</a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<div class="row">
			<div class="col-md-12 center-block">
				<div class="dataTables_paginate paging_bootstrap pagination">
					<jsp:include page="../common/page.jsp" />
				</div>
			</div>
		</div>
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
	var gif = $(this).attr('data-src');
	var origin = $(this).attr('data-origin');
	//console.log(gif + " ori:" + origin)
	if(gif){
		$("#pic").attr("src",gif);
		$("#showPic").css('display','block');
	} else 	if(origin){
		$("#pic").attr("src",origin);
		$("#showPic").css('display','block');
	}
}, function(){
	$("#showPic").css('display','none');
});

$('#allcheck').on('click', function(){
	if ($(this).prop("checked")) {
        $(":checkbox").prop("checked", true);
    }else {
        $(":checkbox").prop("checked", false);
    }
});

function verifyJoke(id) {
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
	
	post('topic/delBatchJoke',
			'topicId=${topicId}&ids='+id, 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>topic/deljoke?status=${status}&topicCoverId=${topicCoverId}&pSize=${pSize}&pNumber=${pNumber}&topicId=${topicId}&pageNumber='+$("#pageNumber").val()+'&pageSize='+$("#pageSize").val();
				} else {
					alert('删除失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
};
function post(url, data, success, error) {
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");
	var csrfToken = $("meta[name='_csrf']").attr("content");
	$.ajax({
		type: 'POST', url: url, data: data, success: success, error: error,
		headers: {'X-CSRF-TOKEN': csrfToken}
	});
};
/**	分页方法	*/
function turnPage(){
	location.href = '<%=basePath%>topic/deljoke?status=${status}&topicCoverId=${topicCoverId}&pSize=${pSize}&pNumber=${pNumber}&topicId=${topicId}&pageNumber='+$("#pageNumber").val()+'&pageSize='+$("#pageSize").val();
};
</script>

</div><!-- content end -->
</div><!-- row end -->
	<!--  图片展示页面	-->
	<div id="showPic" style="display: none;text-align: center;position: fixed; _position:absolute;left:50%;top:50%;margin: -141px 0 0 -201px;border:0px;">
		<img id="pic" src=""/>
	</div>
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>