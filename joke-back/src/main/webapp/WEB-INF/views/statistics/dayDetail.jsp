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
		<h2><i class="glyphicon glyphicon-user"></i> 渠道日报</h2>
	</div>
	<div class="box-content">
		<table  class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="row">
				<div class="col-md-15">
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
						<label style="padding-right:30px;">
							<span >渠道名称</span>
							<c:if test="${empty distributorName}">
								<input id="distributorName" type="text" aria-controls="DataTables_Table_0" />
							</c:if>
							<c:if test="${!empty distributorName}">
								<input id="distributorName" type="text" aria-controls="DataTables_Table_0" value="${distributorName}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >频道名称</span>
							<c:if test="${empty channelName}">
								<input id="channelName" type="text" aria-controls="DataTables_Table_0" />
							</c:if>
							<c:if test="${!empty channelName}">
								<input id="channelName" type="text" aria-controls="DataTables_Table_0" value="${channelName}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<span >标签名称</span>
							<c:if test="${empty labelName}">
								<input id="labelName" type="text" aria-controls="DataTables_Table_0" />
							</c:if>
							<c:if test="${!empty labelName}">
								<input id="labelName" type="text" aria-controls="DataTables_Table_0" value="${labelName}"/>
							</c:if>
						</label>
						<label style="padding-right:30px;">
							<input type="radio" name="type" value="0" <c:if test="${type == 0 }">checked</c:if>>全部
							<input type="radio" name="type" value="1" <c:if test="${type == 1 }">checked</c:if>>只看渠道
							<input type="radio" name="type" value="2" <c:if test="${type == 2 }">checked</c:if>>只看频道
							<input type="radio" name="type" value="3" <c:if test="${type == 3 }">checked</c:if>>只看标签
						</label>
						<label style="padding-right:30px;">
							<input type="button" id="btnClick" class="btn btn-submit" onclick="turnPage()" value="查询"/>
							<button id="export" type="button" class="btn btn-default" data-dismiss="modal">导出</button>
						</label>
					</div>
				</div>
			</div>
			<thead>
				<tr>
					<th>日期</th>
					<th>渠道</th>
					<th>频道</th>
					<th>标签</th>
					<th>总pv</th>
					<th>总uv</th>
					<th>人均pv</th>
					<th>入口pv</th>
					<th>入口uv</th>
					<th>入口人均pv</th>
					<th>列表页pv</th>
					<th>列表页uv</th>
					<th>列表页人均pv</th>
					<th>详情页pv</th>
					<th>详情页uv</th>
					<th>详情页人均pv</th>
					<th>新用户pv</th>
					<th>新用户uv</th>
					<th>新用户留存数</th>
					<th>新用户留存率</th>
					<th>老用户pv</th>
					<th>老用户uv</th>
					<th>老用户留存数</th>
					<th>老用户留存率</th>
					<th>活跃用户留存数</th>
					<th>活跃用户留存率</th>
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
						<c:if test="${timeDetail.mid != 'total'}">
							<c:if test="${fn:substring(timeDetail.mid, 0, 1) == 'c' }" >
								<c:forEach items="${channelList}" var="channel">
									<c:if test="${fn:substring(timeDetail.mid, 1, fn:length(timeDetail.mid)) == channel.id}"><c:out value="${channel.name}" /></c:if>
								</c:forEach>
							</c:if>
						</c:if>
					</td>
					<td>
						<c:if test="${timeDetail.mid != 'total'}">
							<c:if test="${fn:substring(timeDetail.mid, 0, 1) == 'l' }" >
								<c:forEach items="${labelList}" var="label">
									<c:if test="${fn:substring(timeDetail.mid, 1, fn:length(timeDetail.mid)) == label.id}"><c:out value="${label.name}" /></c:if>
								</c:forEach>
							</c:if>
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
					<td>${timeDetail.enterPv}</td>
					<td>${timeDetail.enterUv}</td>
					<td>
						<c:if test="${timeDetail.enterPv == 0 || timeDetail.enterUv == 0}">0</c:if>
						<c:if test="${timeDetail.enterPv != 0 && timeDetail.enterUv != 0}">
							<fmt:formatNumber type="number" value="${timeDetail.enterPv / timeDetail.enterUv }" pattern="0.00" maxFractionDigits="2"/>
						</c:if>
					</td>
					<td>${timeDetail.listPv}</td>
					<td>${timeDetail.listUv}</td>
					<td>
						<c:if test="${timeDetail.listPv == 0 || timeDetail.listUv == 0}">0</c:if>
						<c:if test="${timeDetail.listPv != 0 && timeDetail.listUv != 0}">
							<fmt:formatNumber type="number" value="${timeDetail.listPv / timeDetail.listUv }" pattern="0.00" maxFractionDigits="2"/>
						</c:if>
					</td>
					<td>${timeDetail.detailPv}</td>
					<td>${timeDetail.detailUv}</td>
					<td>
						<c:if test="${timeDetail.detailPv == 0 || timeDetail.detailUv == 0}">0</c:if>
						<c:if test="${timeDetail.detailPv != 0 && timeDetail.detailUv != 0}">
							<fmt:formatNumber type="number" value="${timeDetail.detailPv / timeDetail.detailUv }" pattern="0.00" maxFractionDigits="2"/>
						</c:if>
					</td>
					<td>${timeDetail.newUserPv}</td>
					<td>${timeDetail.newUserUv}</td>
					<td>${timeDetail.newUserKeep}</td>
					<td>
						<c:if test="${timeDetail.lastNewUserUv == 0 }">0%</c:if>
						<c:if test="${timeDetail.lastNewUserUv != 0 }">
							<fmt:formatNumber type="percent" value="${timeDetail.newUserKeep / timeDetail.lastNewUserUv }" />
						</c:if>
					</td>
					<td>${timeDetail.oldUserPv}</td>
					<td>${timeDetail.oldUserUv}</td>
					<td>${timeDetail.oldUserKeep}</td>
					<td>
						<c:if test="${timeDetail.lastOldUserUv == 0 }">0%</c:if>
						<c:if test="${timeDetail.lastOldUserUv != 0 }">
							<fmt:formatNumber type="percent" value="${timeDetail.oldUserKeep / timeDetail.lastOldUserUv }" />
						</c:if>
					</td>
					<td>${timeDetail.actionUserKeep}</td>
					<td>
						<c:if test="${timeDetail.lastActionUserUv == 0 }">0%</c:if>
						<c:if test="${timeDetail.lastActionUserUv != 0 }">
							<fmt:formatNumber type="percent" value="${timeDetail.actionUserKeep / timeDetail.lastActionUserUv }" />
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
		location.href = '<%=basePath%>statistics/dayDetail?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+
				'&dname='+$("#distributorName").val()+'&cname='+$("#channelName").val()+'&lname='+$("#labelName").val()+
				'&type='+$('input[name="type"]:checked ').val()+'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
	}
	$('#export').click(function(event) {
		var url = '<%=basePath%>statistics/dayDetailExport?startDay='+$("#startDay").val()+'&endDay='+$("#endDay").val()+
		'&dname='+$("#distributorName").val()+'&cname='+$("#channelName").val()+'&lname='+$("#labelName").val()
		+'&type='+$('input[name="type"]:checked ').val();
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
