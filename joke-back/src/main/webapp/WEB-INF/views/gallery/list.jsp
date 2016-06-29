<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<title>图片管理</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

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
		<li><a href="gallery/list">图片管理</a></li>
	</ul>
</div>
<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 图集列表</h2>
	</div>
	<div class="box-content">
		<table  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-12">
					<div class="dataTables_filter" id="DataTables_Table_0_filter">
						<label style="padding-right:30px;">
							<span >ID</span> 
							<c:if test="${galleryId == null }">
								<input id="galleryId" type="text" aria-controls="DataTables_Table_0" />
							</c:if>
							<c:if test="${galleryId != null }">
								<input id="galleryId" type="text" aria-controls="DataTables_Table_0" value="${galleryId}"/>
							</c:if>
						</label>
					
						<label style="padding-right:30px;">
							<span >标题</span> 
							<c:if test="${title == null }">
								<input id="title" type="text" aria-controls="DataTables_Table_0" />
							</c:if>
							<c:if test="${title != null }">
								<input id="title" type="text" aria-controls="DataTables_Table_0" value="${title}"/>
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
							<span >标签</span>
							<select id="labelId">
								<option value="">全部</option>
								<c:forEach items="${labelList}" var="label">
									<option value="${label.id}" <c:if test="${labelId == label.id}">selected</c:if> >${label.name}</option>
								</c:forEach>
							</select>
						</label>
						
						<label style="padding-right:30px;">
							<span >数据源</span>
							<select id="sourceId">
								<option value="">全部</option>
								<c:forEach items="${sourceList}" var="source">
									<option value="${source.id}" <c:if test="${sourceId == source.id}">selected</c:if> >${source.name}</option>
								</c:forEach>
							</select>
						</label>
						
						<label style="padding-right:30px;">
							<input type="button" class="btn btn-submit" onclick="turnPage()" value="查询"/>
						</label>
					</div>
				</div>
			</div>
			<thead>
				<tr>
					<th>ID</th>
					<th>标题</th>
					<th>图片</th>
					<th>图集数量</th>
					<th>图片列表</th>
					<th>点赞数</th>
					<th>频道</th>
					<th>标签</th>
					<th>数据来源</th>
					<th>创建时间</th>
					<th>更新时间</th>
					<th>操作</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="gallery">
				<tr>
					<td>${gallery.id}</td>
					<td>${gallery.title}</td>
					<td>
						<img width="150px" alt="${gallery.title}" src="${gallery.url}" />
					</td>
					<td>${gallery.pictureCount}</td>
					<td>
						<c:if test="${! empty gallery.list}">
							<ul class="thumbnails">
								<c:forEach items="${gallery.list}" var="picture" varStatus="status">
									<li id="image-${status.count}" class="thumbnail">
										<a style="background:url(${picture.url})" title="${gallery.title}" href="${picture.url}">
											<img width="150px" src="${picture.url}" alt="${gallery.title}" />
<%-- 											<img width="150px" class="grayscale" src="${picture.url}" alt="${gallery.title}" /> --%>
										</a>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</td>
					<td>${gallery.fontCount}</td>
					<td>
						<c:if test="${!empty gallery.channelId}">
							<c:forEach items="${channelList}" var="channel">
								<c:if test="${channel.id == gallery.channelId }">${channel.name}</c:if>
							</c:forEach>
						</c:if>
					</td>
					<td>
						<c:if test="${!empty gallery.labelId}">
							<c:forEach items="${labelList}" var="label">
								<c:if test="${label.id == gallery.labelId }">${label.name}</c:if>
							</c:forEach>
						</c:if>
					</td>
					<td>
						<c:if test="${!empty gallery.dataSourceId}">
							<c:forEach items="${sourceList}" var="source">
								<c:if test="${source.id == gallery.dataSourceId }">${source.name}</c:if>
							</c:forEach>
						</c:if>
					</td>
					<td>
						<fmt:formatDate value="${gallery.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<fmt:formatDate value="${gallery.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
			            <a class="btn btn-danger" href="#" onclick="delGallery('${gallery.id}')">
			                <i class="glyphicon glyphicon-trash icon-white"></i>删除
			            </a>
			            <a class="btn btn-info" href="gallery/edit?id=${gallery.id}">
			                <i class="glyphicon glyphicon-edit icon-white"></i>编辑
			            </a>
					</td>
				</tr>
				</c:forEach>
			</tbody>
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
	function turnPage(){
		location.href = '<%=basePath%>gallery/list?sourceId='+$("#sourceId").val()+'&galleryId='+$("#galleryId").val()+'&title='+$("#title").val()
			+'&cid='+$("#channelId").val()+'&lid='+$("#labelId").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
	}
	function delGallery(id){
		if(confirm("确认删除吗？")){
			post('gallery/del','id='+id,
				function (data) {
					if(data['status']) {
						location.href = '<%=basePath%>gallery/list?sourceId='+$("#sourceId").val()+'&galleryId='+$("#galleryId").val()+'&title='+$("#title").val()
						+'&cid='+$("#channelId").val()+'&lid='+$("#labelId").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
					}else {
						alert('删除失败. info:'+data['info']);
					}
				},
				function () {
					alert('请求失败，请检查网络环境');
				});
		}
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
