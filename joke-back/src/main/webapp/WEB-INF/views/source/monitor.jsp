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
    <title>内容源监控</title>
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
                            <h2><i class="glyphicon glyphicon-user"></i> 内容源监控</h2>
                        </div>
                        <div class="box-content">
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="bs-example" style="margin: 5px;padding: 5px;display: inline-block;">
                                          <%--  <form class="form-inline" id="monitorCrawlForm" action="source/monitor" method="POST">--%>
                                                <div class="form-group" style="display: inline-block;">
                                                    <label>
                                                        <label for="crawlDate" style="display: inline-block;">日期：</label>
                                                    </label>
                                                </div>
                                                <div class="form-group" style="display: inline-block;">
                                                    <input id="crawlDate" class="form-control input-sm" <c:if test="${!empty crawlDate}">value="${crawlDate}"</c:if> <c:if test="${empty crawlDate}">value="<%=date%>"</c:if>  style="display: inline-block;" type="text"  onfocus="WdatePicker({skin:'whyGreen',minDate:'2016-01-01',maxDate:'2066-12-31'})"/>
                                                </div>
                                                &nbsp;&nbsp;
                                                <div class="checkbox" style="display: inline-block;">
                                                    <label>
                                                        <input id="status" type="checkbox" <c:if test="${!empty status && status == 1}">checked</c:if>> 只展示可用通道
                                                    </label>
                                                </div>
                                                &nbsp;&nbsp;
                                                <div class="form-group" style="display: inline-block;">
                                                    <%--<button type="submit" class="btn btn-primary btn-sm" >查询</button>--%>
                                                    <a class="btn btn-primary btn-sm" href="#" id="query" style="text-align: center;">
                                                        <span class="glyphicon glyphicon-search icon-white">查询</span>
                                                    </a>

                                                </div>
                                        </div>
                                    </div>
                                </div>

                                <thead>
                                <tr>
                                    <th>内容源名称</th>
                                    <th>网址</th>
                                    <th>可用状态</th>
                                    <th>抓取次数（日）</th>
                                    <th>审核通过率（日）</th>
                                    <th>上次抓取时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="sourceCrawl">
                                    <tr>
                                        <td><c:out value="${sourceCrawl.sourceName}"/></td>
                                        <td><c:out value="${sourceCrawl.url}"/></td>
                                        <td>
                                            <c:if test="${sourceCrawl.status == 0}">
                                                不可用
                                            </c:if>
                                            <c:if test="${sourceCrawl.status == 1}">
                                                可用
                                            </c:if>
                                        </td>
                                        <td><c:out value="${sourceCrawl.grabCount}"/></td>
                                        <td><c:out value="${sourceCrawl.verifyRate}"/>%</td>
                                        <td>
                                            <fmt:formatDate value="${sourceCrawl.lastGrabTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div style="text-align: center;">
                                <a class="btn btn-danger btn-sm" href="source/list">朕已阅</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- box col-md-12 end -->
            </div>
            <!-- row end -->

            <script type="text/javascript">
                $('#query').click(function(event) {
                    var status = "";
                    if($("#status")[0].checked == true){
                        status = "1";
                    }
                    location.href = '<%=basePath%>source/monitor?status=' + status
                    + '&crawlDate=' + $("#crawlDate").val();
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
