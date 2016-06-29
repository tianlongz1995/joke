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
	<title>数据统计</title>
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
	<!-- <ul class="breadcrumb">
		<li><a href="statistics/dayTotal">总日报</a></li>
	</ul> -->
</div>
<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 总周报</h2>
	</div>
	<div class="box-content">
		<table  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-12">
					<div class="dataTables_filter" id="DataTables_Table_0_filter">
						<label style="padding-right:30px;">
							<span >开始周</span>
							<c:if test="${empty startWeek}">
								<input id="startWeek" type="text" aria-controls="DataTables_Table_0" value=""/>
							</c:if>
							<c:if test="${!empty startWeek}">
								<input id="startWeek" type="text" aria-controls="DataTables_Table_0" value="${startWeek}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >结束周</span>
							<c:if test="${empty endWeek}">
								<input id="endWeek" type="text" aria-controls="DataTables_Table_0" value=""/>
							</c:if>
							<c:if test="${!empty endWeek}">
								<input id="endWeek" type="text" aria-controls="DataTables_Table_0" value="${endWeek}"/>
							</c:if>
						</label>
					
						<label style="padding-right:30px;">
							<input type="button" id="btnClick" class="btn btn-submit" onclick="turnPage()" value="查询"/>
						</label>
					</div>
				</div>
			</div>
			<thead>
				<tr>
					<th>周</th>
					<th>总PV</th>
					<th>总UV</th>
					<th>人均PV</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="timeTotal">
				<tr>
					<td>${timeTotal.time}</td>
					<td>${timeTotal.pv}</td>
					<td>${timeTotal.uv}</td>
					<td>
						<c:if test="${timeTotal.uv == 0 }">0</c:if>
						<c:if test="${timeTotal.uv != 0 }">
							<fmt:formatNumber type="number" value="${timeTotal.pv / timeTotal.uv }" pattern="0.00" maxFractionDigits="2"/>
						</c:if>
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
		location.href = '<%=basePath%>statistics/weekTotal?startWeek='+$("#startWeek").val()+'&endWeek='+$("#endWeek").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
	}
	
	$("body").keyup(function () {  
        if (event.which == 13){  
            $("#btnClick").trigger("click");  
        }  
    });
</script>

</div><!-- content end -->
</div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>
