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
	<script src="/ui/js/date/WdatePicker.js"></script>
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
		<h2><i class="glyphicon glyphicon-user"></i> 下拉刷新日报</h2>
	</div>
	<div class="box-content">
		<table  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-12">
					<div class="dataTables_filter" id="DataTables_Table_0_filter">
						<label style="padding-right:30px;">
							<span >开始日期</span>
							<c:if test="${empty startDay}">
								<input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value=""/>
							</c:if>
							<c:if test="${!empty startDay}">
								<input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="${startDay}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >结束日期</span>
							<c:if test="${empty endDay}">
								<input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value=""/>
							</c:if>
							<c:if test="${!empty endDay}">
								<input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="${endDay}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;" id="flushTypeLabel">
							<select id="flushType" style="font-size: 16px;width: 100px;margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty flushType || flushType == 0}">selected</c:if> >下拉刷新</option>
								<option value="1" <c:if test="${!empty flushType && flushType == 1}">selected</c:if> >上拉刷新</option>
							</select>
						</label>

						<label style="padding-right:30px;" id="dateTypeLabel">
							<select id="dateType" style="font-size: 16px;width: 100px;margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty dateType || dateType == 0}">selected</c:if> >日报</option>
								<option value="1" <c:if test="${!empty dateType && dateType == 1}">selected</c:if> >周报</option>
								<option value="2" <c:if test="${!empty dateType && dateType == 2}">selected</c:if> >月报</option>
							</select>
						</label>
					
						<label style="padding-right:30px;">
							<input type="button" id="btnClick" class="btn btn-primary btn-sm" onclick="turnPage()" value="查询"/>
						</label>
					</div>
				</div>
			</div>
			<thead>
				<tr>
					<th>日期</th>
					<th>总下拉刷新PV</th>
					<th>总下拉刷新UV</th>
					<th>人均下拉刷新PV</th>
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
		location.href = '<%=basePath%>statistics/dropTotal?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()+'&flushType='+$("#flushType").val()+'&dateType='+$("#dateType").val();
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
