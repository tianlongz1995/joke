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
    <title>拉黑管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="/ui/js/date/WdatePicker.js"></script>
    <!-- The fav icon -->
    <link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
<jsp:include page="../common/topbar.jsp"/>
<div class="ch-container">
    <div class="row">
        <jsp:include page="../common/leftmenu.jsp"/>
        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div class="row">
                <div class="box col-md-12" style="margin-top: 0;">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 黑人列表</h2>
                        </div>
                        <div class="box-content" style="vertical-align: middle;">
                            <table id="table_list"
                                   class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter" style="margin: 15px 5px;">
                                    <label style="padding-right:10px;">
                                        <span>UID:</span>
                                        <c:if test="${empty uid}">
                                            <input type="text" id="uid" value=""
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty uid}">
                                            <input type="text" id="uid" value="${uid}"
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                    </label>

                                    <label style="padding-right:10px;">
                                        <a class="btn btn-primary btn-sm" href="#" id="selectBlackMan">
                                            <span class="glyphicon glyphicon-search icon-white"> 查&nbsp;&nbsp;询</span>
                                        </a>
                                    </label>
                                    <%--&nbsp;&nbsp;--%>
                                    <%--<label style="padding-right:10px;">--%>
                                    <%--<span>添加敏感词</span>--%>
                                    <%--<input type="text" id="word" value=""--%>
                                    <%--style="max-width: 160px;"/>--%>
                                    <%--</label>--%>
                                    &nbsp;&nbsp;
                                    <label style="padding-right:10px;">
                                        <a class="btn btn-primary btn-sm" href="#" id="selectAll"
                                           onclick="addSensitive()">
                                            <span class="glyphicon glyphicon-search icon-white"> 查询全部</span>
                                        </a>
                                    </label>
                                </div>

                                <thead>
                                <tr>
                                    <th style="width: 50%;text-align: center; vertical-align: middle;">UID</th>
                                    <th style="width: 50%;text-align: center; vertical-align: middle;">用户名</th>
                                    <th style="width: 50%;text-align: center; vertical-align: middle;">拉黑时间</th>
                                    <th style="width: 50%;text-align: center; vertical-align: middle;">拉黑人</th>
                                    <th style="width: 50%;text-align: center; vertical-align: middle;">操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="black_man">
                                    <tr>
                                        <td>
                                                ${black_man.id}
                                        </td>
                                        <td>
                                                ${black_man.nick}
                                        </td>
                                        <td>
                                                ${black_man.updateTime}
                                        </td>
                                        <td>
                                                ${black_man.create_by}
                                        </td>
                                        <td>
                                            <a class="btn btn-danger btn-sm" href="#"
                                               onclick="retrieveBlackMan(${black_man.id})">
                                                <i class="glyphicon glyphicon-remove icon-white"></i> 恢&nbsp;&nbsp;复
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <div class="row">
                                <div class="col-md-12 center-block">
                                    <div class="dataTables_paginate paging_bootstrap pagination">
                                        <jsp:include page="../common/page.jsp"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">

                //                function delSensitive(id) {
                //                    post('sensitive/del', 'id=' + id,
                //                        function (data) {
                //                            if (data.status == 1) {
                //                                alert("操作成功!");
                //                            } else {
                //                                alert('添加失败:' + data.info);
                //                            }
                //                        },
                //                        function () {
                //                            alert('请求失败，请检查网络环境!');
                //                        });
                //                }

                function retrieveBlackMan(uid) {
                    var uid = $("#uid").val();
                    post('joke/retrieve',
                        'uid=' + uid,
                        function (data) {
                            if (data.status == 1) {
                                alert("恢复成功!");
                            } else {
                                alert('恢复失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境!');
                        });
                }


                $('#selectBlackMan').click(function (event) {
                    location.href = '<%=basePath%>joke/getBlackMan?uid=' + $("#uid").val();
                });

                $('#selectAll').click(function (event) {
                    location.href = '<%=basePath%>comment/listBlackMan';
                });


                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }

                function turnPage() {
                    location.href = '<%=basePath%>sensitive/list?keyWord=' + $("#keyWord").val()
                        + '&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                }
                ;
            </script>


        </div><!-- content end -->
    </div><!-- row end -->

</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
