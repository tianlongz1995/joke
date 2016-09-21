<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>内容源管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

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
                            <h2><i class="glyphicon glyphicon-user"></i> 内容源列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info" >
                                需要添加新的内容源点击:  <a href="#" data-toggle="modal" data-target="#newsource"><i class="glyphicon glyphicon-plus"></i> 新增内容源</a>
                                <%--<div style="float:right;margin-top: -5px;">
                                    <a type="button" class="btn btn-danger btn-sm" href="<%=basePath%>source/monitor" >查看监控</a>
                                </div>--%>
                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="bs-example" style="margin: 5px;padding: 5px;">
                                            <form class="form-inline">
                                                <div class="form-group" style="display: inline-block;">
                                                    <label for="sourceNane">名称：</label>
                                                    <input id="name" type="text" class="form-control input-sm" id="sourceNane" style="width: 150px;display: inline-block;" value="${name}" placeholder="输入数据源名称">
                                                </div>
                                                &nbsp;&nbsp;
                                                <div class="form-group"style="display: inline-block;">
                                                    <label for="sourceUrl">URL：</label>
                                                    <input id="url" type="email" class="form-control input-sm" id="sourceUrl" style="width: 150px;display: inline-block;" value="${url}" placeholder="输入数据源URL">
                                                </div>
                                                &nbsp;&nbsp;
                                                <div style="display: inline-block;">
                                                    <select id="status" style="font-size: 16px;width: 100px;margin: 1px;padding: 5px;" >
                                                        <c:if test="${empty status}">
                                                            <option value="" selected>全部</option>
                                                        </c:if>
                                                        <c:if test="${not empty status}">
                                                            <option value="">全部</option>
                                                        </c:if>
                                                        <option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >下线</option>
                                                        <option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >上线</option>
                                                    </select>
                                                </div>
                                                &nbsp;&nbsp;
                                                <div class="form-group"style="display: inline-block;">
                                                    <a class="btn btn-primary btn-sm" href="#" id="query" style="text-align: center;">
                                                        <span class="glyphicon glyphicon-search icon-white">查询</span>
                                                    </a>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>

                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>名称</th>
                                    <th>URL</th>
                                    <th>状态</th>
                                    <th>创建时间</th>
                                    <th>更新时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${list}" var="source">
                                    <tr>
                                        <td><c:out value="${source.id}"/></td>
                                        <td><c:out value="${source.name}"/></td>
                                        <td><c:out value="${source.url}"/></td>
                                        <td>
                                            <c:if test="${source.status == 0}">
                                                下线
                                            </c:if>
                                            <c:if test="${source.status == 1}">
                                                上线
                                            </c:if>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${source.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${source.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <a class="btn btn-danger btn-xs" href="#" onclick="delSource(${source.id})">
                                                <i class="glyphicon glyphicon-remove icon-white"></i> 删除
                                            </a>
                                            <a class="btn btn-info btn-xs" href="source/modify?id=${source.id}">
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
                </div>
                <!-- box col-md-12 end -->
            </div>
            <!-- row end -->

            <div class="modal fade bs-example-modal-lg" id="newsource" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新建内容源</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>名称</th>
                                    <td><input id="addName" type="text" class="form-control" placeholder="输入内容源名称"/></td>
                                </tr>
                                <tr>
                                    <th>URL</th>
                                    <td><input id="addUrl" type="text" class="form-control" placeholder="输入内容源URL"/></td>
                                </tr>
                                <tr>
                                    <th>状态</th>
                                    <td>
                                        <select id="addStatus" class="form-control">
                                            <option value="0">下线</option>
                                            <option value="1" selected>上线</option>
                                        </select>
                                    </td>
                                </tr>
                            </table>

                        </div>
                        <div class="modal-footer" style="text-align: center;vertical-align: middle;">
                            <button id="addNewsource" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                $('#addNewsource').click(function (event) {
                    $('#addNewsource').attr("disabled","disabled");
                    post('source/add',
                            'name=' + $("#addName").val()+ '&url=' + $('#addUrl').val()+'&status=' + $('#addStatus').val(),
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>source/list?status=' + $("#status").val()
                                    + '&pageSize=' + $("#pageSize").val() + '&pageNumber=' + $("#pageNumber").val()
                                    + '&name=' + $("#name").val() + '&url=' + $("#url").val();
                                }else if (data.status == 2){
                                    alert('添加失败:\r\n' + data.info);
                                    $('#addNewsource').attr("disabled",false);
                                } else {
                                    alert('添加失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                });

                function delSource(id) {
                    post('source/del',
                            'id=' + id,
                            function (data) {
                                if (data['status']) {
                                    location.href = '<%=basePath%>source/list?status=' + $("#status").val()
                                    +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
                                    + '&name=' + $("#name").val() + '&url=' + $("#url").val();
                                }
                                else {
                                    alert('操作失败. info:' + data['info']);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                }

                $('#query').click(function (event) {
                    location.href = '<%=basePath%>source/list?status=' + $("#status").val()
                    +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
                    + '&name=' + $("#name").val() + '&url=' + $("#url").val();
                });

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_headr']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };
                function turnPage(){
                    location.href = '<%=basePath%>source/list?status=' + $("#status").val()
                    +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
                    + '&name=' + $("#name").val() + '&url=' + $("#url").val();
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
