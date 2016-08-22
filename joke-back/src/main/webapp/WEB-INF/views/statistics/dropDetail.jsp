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
		<h2><i class="glyphicon glyphicon-user"></i> 列表页下拉刷新日统计明细</h2>
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
							<span >渠道名称</span>
							<c:if test="${empty distributorName}">
								<input id="distributorName" type="text" aria-controls="DataTables_Table_0" style="max-width: 120px;"/>
							</c:if>
							<c:if test="${!empty distributorName}">
								<input id="distributorName" type="text" aria-controls="DataTables_Table_0" value="${distributorName}" style="max-width: 120px;"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >频道名称</span>
							<c:if test="${empty channelName}">
								<input id="channelName" type="text" aria-controls="DataTables_Table_0"  style="max-width: 120px;"/>
							</c:if>
							<c:if test="${!empty channelName}">
								<input id="channelName" type="text" aria-controls="DataTables_Table_0" value="${channelName}" style="max-width: 120px;"/>
							</c:if>
						</label>
						<label style="padding-right:30px;" id="typeLabel">
							<select id="type" style="font-size: 16px;width: 100px;margin: 1px;padding: 5px;">
								<option value="0" <c:if test="${empty type || type == 0}">selected</c:if> >全部</option>
								<option value="1" <c:if test="${!empty type && type == 1}">selected</c:if> >只看渠道</option>
								<option value="2" <c:if test="${!empty type && type == 2}">selected</c:if> >只看频道</option>
								<option value="3" <c:if test="${!empty type && type == 3}">selected</c:if> >只看标签</option>
							</select>
						</label>
						<label style="padding-right:30px;">
							<input type="button" id="btnClick" class="btn btn-primary btn-sm" onclick="turnPage()" value="查询"/>
							<button id="export" type="button" class="btn btn-warning btn-sm" data-dismiss="modal">导出</button>
						</label>
					</div>
				</div>
			</div>
			<thead>
				<tr>
					<th>日期</th>
					<th>渠道</th>
					<th>频道</th>
					<th>列表页下拉刷新PV</th>
					<th>列表页下拉刷新UV</th>
					<th>列表页人均下拉刷新PV</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="timeDetail">
				<tr>
					<td>${timeDetail.time}</td>
					<td>
						<c:if test="${timeDetail.did != 'total'}">
							<c:forEach items="${distributorList}" var="distributor">
								<c:if test="${distributor.id == timeDetail.did}"><c:out value="${distributor.name}" /></c:if>
							</c:forEach>
						</c:if>
					</td>
					<td>
						<c:if test="${timeDetail.cid != 'total'}">
							<c:forEach items="${channelList}" var="channel">
								<c:if test="${timeDetail.cid == channel.id}"><c:out value="${channel.name}" /></c:if>
							</c:forEach>
						</c:if>
					</td>
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
		location.href = '<%=basePath%>statistics/dropDetail?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+
				'&dname='+$("#distributorName").val()+'&cname='+$("#channelName").val()+
				'&type='+$("#type").val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
	};
	$('#export').click(function(event) {
		var url = '<%=basePath%>statistics/dropDetailExport?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+
				'&dname='+$("#distributorName").val()+'&cname='+$("#channelName").val()+'&type='+$("#type").val();
		var $iframe,iframe_doc,iframe_html;
	    if (($iframe = $('#download_iframe')).length === 0) {
	        $iframe = $("<iframe id='download_iframe'" +
	            " style='display: none' src='about:blank'></iframe>"
	        ).appendTo("body");
	    }
	
	    iframe_doc = $iframe[0].contentWindow || $iframe[0].contentDocument;
	    if (iframe_doc.document) {
	        iframe_doc = iframe_doc.document;
	    }
	
	    iframe_html = "<html><head></head><body><form method='POST' action='" + url + "'>"
	    iframe_html += "</form></body></html>";
	    iframe_doc.open();
	    iframe_doc.write(iframe_html);
	    $(iframe_doc).find('form').submit();
	});
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
