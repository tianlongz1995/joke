<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date= format.format(new Date());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>抓取统计 - 内容源管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>

    <script src="<%=basePath%>ui/charisma/bower_components/jquery/jquery.min.js"></script>
<%--    <link href='<%=basePath%>ui/js/date/skin/whyGreen/datepicker.css ' rel='stylesheet'>
    <script language="javascript" type="text/javascript" src="<%=basePath%>ui/js/date/lang/zh-cn.js"></script>
    <script language="javascript" type="text/javascript" src="<%=basePath%>ui/js/date/calendar.js"></script>--%>
    <script language="javascript" type="text/javascript" src="<%=basePath%>ui/js/date/WdatePicker.js"></script>
<script language="javascript" type="text/javascript">
String.prototype.replaceAll = function (FindText, RepText) {
    regExp = new RegExp(FindText, "g");
    return this.replace(regExp, RepText);
}
</script>
    <!-- The fav icon -->
    <link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
<jsp:include page="../common/topbar.jsp"/>
<div class="ch-container">
    <div class="row">
        <jsp:include page="../common/leftmenu.jsp"/>
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
                    <li><a href="source/list">内容源管理</a></li>
                </ul>
            </div>

            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 抓取统计</h2>
                        </div>
                        <div class="box-content">
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="bs-example" style="margin: 5px;padding: 5px;display: inline-block;">
                                          <%--  <form class="form-inline" id="monitorCrawlForm" action="source/monitor" method="POST">--%>
                                                <div class="form-group" style="display: inline-block;">
                                                    <label>
                                                        <label for="startTime" style="display: inline-block;">开始日期：</label>
                                                    </label>
                                                </div>
                                                <div class="form-group" style="display: inline-block;">
                                                    <input id="startTime" class="form-control input-sm" <c:if test="${!empty startTime}">value="${startTime}"</c:if> <c:if test="${empty startTime}">value="<%=date%>"</c:if>  style="display: inline-block;width: 85px;" type="text"  onfocus="WdatePicker({skin:'whyGreen',minDate:'2016-01-01',maxDate:'2066-12-31'})"/>
                                                </div>
                                              &nbsp;&nbsp;
                                                <div class="form-group" style="display: inline-block;">
                                                  <label>
                                                      <label for="endTime" style="display: inline-block;">结束日期：</label>
                                                  </label>
                                                </div>
                                                <div class="form-group" style="display: inline-block;">
                                                  <input id="endTime" class="form-control input-sm" <c:if test="${!empty endTime}">value="${endTime}"</c:if> <c:if test="${empty endTime}">value="<%=date%>"</c:if>  style="display: inline-block;width: 85px;" type="text"  onfocus="WdatePicker({skin:'whyGreen',minDate:'2016-01-01',maxDate:'2066-12-31'})"/>
                                                </div>
                                              &nbsp;&nbsp;
                                              <div class="form-group" style="display: inline-block;">
                                                  <label>
                                                      <select id="sourceType" class="form-control input-sm" onchange="checkSource()">
                                                          <option value="0" <c:if test="${!empty sourceType && sourceType == 0}">selected='selected'</c:if> >格式:</option>
                                                          <option value="1" <c:if test="${empty sourceType || sourceType == 1}">selected='selected'</c:if>>数据源:</option>
                                                      </select>
                                                     <%-- <label for="endTime" style="display: inline-block;">格式:</label>--%>
                                                  </label>
                                              </div>
                                              <div class="checkbox" style="display: inline-block; " >
                                                  <label style="padding-left: 3px;">
                                                      <div id="typeGroup" class="btn-group">
                                                          <button <c:if test="${empty sourceType || sourceType == 1}">disabled='disabled'</c:if> onclick="checkType(this)" type="button" value="0" class="btn btn-default <c:if test="${type == 0}">btn-primary</c:if> btn-sm" <c:if test="${!empty type && type == 0}">id='type'</c:if> >纯文</button>
                                                          <button <c:if test="${empty sourceType || sourceType == 1}">disabled='disabled'</c:if> onclick="checkType(this)" type="button" value="1" class="btn btn-default <c:if test="${type == 1}">btn-primary</c:if> btn-sm" <c:if test="${!empty type && type == 1}">id='type'</c:if> >图片</button>
                                                          <button <c:if test="${empty sourceType || sourceType == 1}">disabled='disabled'</c:if> onclick="checkType(this)" type="button" value="2" class="btn btn-default <c:if test="${type == 2}">btn-primary</c:if> btn-sm" <c:if test="${!empty type && type == 2}">id='type'</c:if> >动图</button>
                                                      </div>
                                                  </label>
                                              </div>
                                                &nbsp;&nbsp;
                                                <div class="form-group" style="display: inline-block;">
                                                  <label>
                                                      <label for="name" style="display: inline-block;">名称:</label>
                                                  </label>
                                                </div>
                                                <div class="checkbox" style="display: inline-block;">
                                                  <label style="padding-left: 3px;">
                                                     <input id="name" type="text" value="${name}"/>
                                                  </label>
                                                </div>
                                                &nbsp;&nbsp;
                                                <div class="form-group" style="display: inline-block;">
                                                    <a class="btn btn-primary btn-sm" href="#" id="query" style="text-align: center;">
                                                        <i class="glyphicon glyphicon-search icon-white"></i> 查询
                                                    </a>
                                                </div>
                                              &nbsp;&nbsp;
                                              <div class="form-group" style="display: inline-block;">
                                                  <a class="btn btn-primary btn-sm" href="#" id="export" style="text-align: center;">
                                                      <i class="glyphicon glyphicon-share icon-white"></i> 导出
                                                  </a>
                                              </div>
                                        </div>
                                    </div>
                                </div>

                                <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>URL</th>
                                    <c:if test="${!empty sourceType && sourceType == 0}">
                                        <th>格式</th>
                                    </c:if>
                                    <th>统计时间</th>
                                    <th>抓取数量</th>
                                    <th>状态</th>
                                    <th>抓取次数</th>
                                    <th>上次抓取时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="sourceCrawl">
                                    <tr>
                                        <td><c:out value="${sourceCrawl.sourceName}"/></td>
                                        <td><c:out value="${sourceCrawl.url}"/></td>
                                        <c:if test="${!empty sourceType && sourceType == 0}">
                                            <td>
                                                <c:if test="${sourceCrawl.type == 0}">纯文</c:if>
                                                <c:if test="${sourceCrawl.type == 1}">图片</c:if>
                                                <c:if test="${sourceCrawl.type == 2}">动图</c:if>
                                            </td>
                                        </c:if>
                                        <td>
                                            <c:if test="${sourceCrawl.day != null}">
                                                ${sourceCrawl.day}
                                            </c:if>
                                        </td>
                                        <td><c:out value="${sourceCrawl.grabTotal}"/></td>
                                        <td>
                                            <c:if test="${sourceCrawl.status == 0}">
                                                不可用
                                            </c:if>
                                            <c:if test="${sourceCrawl.status == 1}">
                                                可用
                                            </c:if>
                                        </td>
                                        <td><c:out value="${sourceCrawl.grabCount}"/></td>
                                        <td>
                                            <fmt:formatDate value="${sourceCrawl.lastGrabTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
                </div>
                <!-- box col-md-12 end -->
            </div>
            <!-- row end -->

            <script type="text/javascript">
                $('#query').click(function(event) {
                    var type = $("#type").val();
                    if(type == null){
                        type = '';
                    }
                    var startTime = $("#startTime").val();
                    var endTime = $("#endTime").val();
                    var startDate = startTime.replaceAll("-","/");
                    var endDate = endTime.replaceAll('-',"/");
                    var start  = new Date(startDate).getTime();
                    var end = new Date(endDate).getTime();
                    if((end - start) > 2678400000){
                        alert("时间范围必须是一个月以内");
                        return false;
                    }
                    location.href = '<%=basePath%>source/crawl?startTime=' + startTime +'&endTime='+endTime
                            + '&name=' + $("#name").val() + '&type=' + type + '&sourceType='+$("#sourceType").val();
                });
                $('#export').click(function(event) {
                    var startTime = $("#startTime").val();
                    var endTime = $("#endTime").val();
                    if(startTime == null || endTime == null){
                        alert("必须选择查询时间范围");
                        return false;
                    }else{
                        var startDate = startTime.replaceAll("-","/");
                        var endDate = endTime.replaceAll('-',"/");
                        startTime = startTime.replaceAll('-','');
                        endTime = endTime.replaceAll('-','');
                        var start  = new Date(startDate).getTime();
                        var end = new Date(endDate).getTime();
                        if((end - start) > 2678400000){
                            alert("时间范围必须是一个月以内");
                            return false;
                        }
                    }
                    var type = $("#type").val();
                    if(type == null){
                        type = '';
                    }
                    var url = '<%=basePath%>source/crawlExport?startTime=' + startTime +'&endTime='+endTime
                            + '&name=' + $("#name").val() + '&type=' + type + '&sourceType='+$("#sourceType").val();
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
                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };
                function checkType(node) {
//                    删除兄弟节点的选中状态
                    $(node).siblings().removeClass("btn-primary").addClass("btn-default").removeAttr('id')
//                    切换本节点的选中状态
                    if($(node).hasClass("btn-default")){
                        $(node).removeClass("btn-default").addClass("btn-primary").attr('id','type');
                    }else{
                        $(node).removeClass("btn-primary").addClass("btn-default").removeAttr('id');
                    }
                };
                function turnPage(){
                    var type = $("#type").val();
                    if(type == null){
                        type = '';
                    }
                    location.href = '<%=basePath%>source/crawl?startTime=' + $("#startTime").val() +'&endTime='+$("#endTime").val()
                            +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()+ '&sourceType='+$("#sourceType").val()
                            + '&name=' + $("#name").val() + '&type=' + type;
                };
                function checkSource() {
                    var type = $("#sourceType").val();
                    if(type == 0){
                        $("#typeGroup").children().removeAttr('disabled');
                    }else if(type == 1){
                        $("#typeGroup").children().removeClass("btn-primary").addClass("btn-default").removeAttr('id').attr('disabled', 'disabled');
                    }
                }
            </script>

        </div>
        <!-- content end -->
    </div>
    <!-- row end -->
</div>
<!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
