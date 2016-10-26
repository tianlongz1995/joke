<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
	<meta charset="utf-8">
	<title>使用统计</title>
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
		<h2><i class="glyphicon glyphicon-user"></i> 使用统计</h2>
	</div>
	<div class="box-content">
		<table  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-15">
					<div class="dataTables_filter" id="DataTables_Table_0_filter" style="padding-left: 20px;">
						<label style="padding-right:30px;">
							<span >开始日期</span>
							<c:if test="${empty startDay}">
								<input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="" style="max-width: 100px;"/>
							</c:if>
							<c:if test="${!empty startDay}">
								<input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="${startDay}" style="max-width: 100px;"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >结束日期</span>
							<c:if test="${empty endDay}">
								<input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="" style="max-width: 100px;"/>
							</c:if>
							<c:if test="${!empty endDay}">
								<input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyyMMdd'})" class="Wdate" value="${endDay}" style="max-width: 100px;"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span>日志类型</span>
							<select id="logType" class="form-control input-sm" style="margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty logType || logType == 0}">selected</c:if> >长图展开日志</option>
								<option value="1" <c:if test="${!empty logType && logType == 1}">selected</c:if> >上拉刷新日志</option>
								<option value="2" <c:if test="${!empty logType &&  logType == 2}">selected</c:if> >下拉刷新日志</option>
							</select>
						</label>
						<label style="padding-right:30px;">
							<span>渠道</span>
							<select id="distributorId" class="form-control input-sm" style="margin: 1px;padding: 5px;">
								<option value="" >全选</option>
								<c:if test="${!empty distributorList}">
									<c:forEach items="${distributorList}" var="distributor">
										<option value="${distributor.id}" <c:if test="${!empty distributorId && distributorId == distributor.id}">selected</c:if> >${distributor.name}</option>
									</c:forEach>
								</c:if>
							</select>
						</label>
						<label style="padding-right:30px;">
							<span >频道</span>
							<select id="channelId" class="form-control input-sm" style="margin: 1px;padding: 5px;">
								<option value="" >全选</option>
								<c:if test="${!empty channelList}">
									<c:forEach items="${channelList}" var="channel">
										<option value="${channel.id}" <c:if test="${!empty channelId && channelId == channel.id}">selected</c:if> >${channel.name}</option>
									</c:forEach>
								</c:if>
							</select>
						</label>
						<label style="padding-right:30px;" id="dateTypeLabel" >
							<select id="dateType" class="form-control input-sm" style="margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty dateType || dateType == 0}">selected</c:if> >日报</option>
								<option value="1" <c:if test="${!empty dateType && dateType == 1}">selected</c:if> >周报</option>
								<option value="2" <c:if test="${!empty dateType &&  dateType == 2}">selected</c:if> >月报</option>
							</select>
						</label>
						<label style="padding-right:30px;" id="typeLabel" >
							<select id="reportType" class="form-control input-sm" style="margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty reportType || reportType == 0}">selected</c:if> >总报</option>
								<option value="1" <c:if test="${!empty reportType && reportType == 1}">selected</c:if> >渠道</option>
								<option value="2" <c:if test="${!empty reportType && reportType == 2}">selected</c:if> >频道</option>
								<option value="3" <c:if test="${!empty reportType && reportType == 3}">selected</c:if> >明细</option>
							</select>
						</label>

						<label style="padding-right:30px;">
							<input type="button" id="btnClick" class="btn btn-primary btn-sm" onclick="query()" value="查询"/>
						</label>
					</div>
				</div>
			</div>
			<tr>
				<th>日期</th>
				<!--  渠道	-->
				<c:if test="${!empty reportType && reportType == 1}">
					<th>渠道</th>
				</c:if>
				<!--  频道	-->
				<c:if test="${!empty reportType && reportType == 2}">
					<th>频道</th>
				</c:if>
				<!--  明细	-->
				<c:if test="${!empty reportType && reportType == 3}">
					<th>渠道</th>
					<th>频道</th>
				</c:if>
				<th>PV</th>
				<th>UV</th>
				<th>人均PV</th>
			</tr>

			<tbody>
				<c:forEach items="${list}" var="timeDetail">
				<tr>
					<td>${timeDetail.time}</td>
					<!--  渠道	-->
					<c:if test="${!empty reportType && reportType == 1}">
						<td>${timeDetail.dName}</td>
					</c:if>
					<!--  频道	-->
					<c:if test="${!empty reportType && reportType == 2}">
						<td>${timeDetail.cName}</td>
					</c:if>
					<!--  明细	-->
					<c:if test="${!empty reportType && reportType == 3}">
						<td>${timeDetail.dName}</td>
						<td>${timeDetail.cName}</td>
					</c:if>
					<td>${timeDetail.totalPv}</td>
					<td>${timeDetail.totalUv}</td>
					<td>
						<c:if test="${timeDetail.totalPv == 0 || timeDetail.totalUv == 0}">0</c:if>
						<c:if test="${timeDetail.totalPv != 0 && timeDetail.totalUv != 0}">
							<fmt:formatNumber type="number" value="${timeDetail.totalPv / timeDetail.totalUv }" pattern="0.00" maxFractionDigits="2"/>
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
		location.href = '<%=basePath%>statistics/usage?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+
				'&did='+$("#distributorId").val()+'&cid='+$("#channelId").val()+
				'&reportType='+$("#reportType").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
				+'&dateType='+$("#dateType").val()+'&logType='+$("#logType").val();
	};
	function query(){
		location.href = '<%=basePath%>statistics/usage?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()
				+'&did='+$("#distributorId").val()+'&cid='+$("#channelId").val()+ '&reportType='+$("#reportType").val()
				+'&dateType='+$("#dateType").val()+'&logType='+$("#logType").val();
	};
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
