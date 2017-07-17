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
    <title>评论审核</title>
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
                            <h2><i class="glyphicon glyphicon-user"></i> 评论审核列表</h2>
                        </div>
                        <div class="box-content" style="vertical-align: middle;">
                            <table id="table_list"
                                   class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter" style="margin: 15px 5px;">
                                    <label style="padding-right:10px;">
                                        <span>关键字:</span>
                                        <c:if test="${empty keyWord}">
                                            <input type="text" id="keyWord" value=""
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty keyWord}">
                                            <input type="text" id="keyWord" value="${keyWord}"
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                    </label>
                                    <div class="form-group" style="padding-right:10px;display: inline-block;">
                                        <label>状态</label>
                                        <select id="state" class="form-control input-sm">
                                            <option value="">全部</option>
                                            <option value="1"
                                                    <c:if test="${!empty state && state == 1}">selected</c:if> >未审核
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty state && state == 2}">selected</c:if> >已通过
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty state && state == 3}">selected</c:if> >拉黑
                                            </option>
                                            <option value="4"
                                                    <c:if test="${!empty state && state == 4}">selected</c:if> >删除
                                            </option>
                                        </select>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <a class="btn btn-primary btn-sm" href="#" id="selectCommentList">
                                            <span class="glyphicon glyphicon-search icon-white"> 查询</span>
                                        </a>
                                    </label>
                                    <%--<c:if test="${state == 1 }">--%>
                                    <%--<label style="padding-right:10px;">--%>
                                    <%--<a class="btn btn-success btn-sm" href="#"--%>
                                    <%--onclick="verifyComment(2,'batch',null,null)">--%>
                                    <%--<i class="glyphicon glyphicon-ok icon-white"></i>批量通过--%>
                                    <%--</a>--%>
                                    <%--</label>--%>
                                    <%--</c:if>--%>
                                    <c:if test="${state != 3}">
                                        <label style="padding-right:0px;">
                                            <a class="btn btn-danger btn-sm" href="#"
                                               onclick="verifyComment(3,'batch',null,null)">
                                                <i class="glyphicon glyphicon-remove icon-white"></i>批量拉黑
                                            </a>
                                        </label>
                                    </c:if>
                                    <c:if test="${state != 4}">
                                        <label style="padding-right:0px;">
                                            <a class="btn btn-danger btn-sm" href="#"
                                               onclick="verifyComment(4,'batch',null,null)">
                                                <i class="glyphicon glyphicon-remove icon-white"></i>批量删除
                                            </a>
                                        </label>
                                    </c:if>
                                    <c:if test="${state != 1}">
                                        <label style="padding-right:0px;">
                                            <a class="btn btn-danger btn-sm" href="#"
                                               onclick="verifyComment(1,'batch',null,null)">
                                                <i class="glyphicon glyphicon-remove icon-white"></i>批量恢复
                                            </a>
                                        </label>
                                    </c:if>
                                </div>

                                <thead>
                                <tr>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">全选 <input
                                            type="checkbox" id="allcheck"/></th>
                                    <th style="width: 35%;text-align: center; vertical-align: middle;">评论内容</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">时间</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">UID</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">用户名</th>
                                    <th style="width: 22%;text-align: center; vertical-align: middle;">操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="comment">
                                    <tr>
                                        <td style="text-align: center; vertical-align: middle;">
                                            <input type="checkbox" name="commentId" value="${comment.id}"/>
                                            <%--<input type="hidden" name="userId" value="${comment.uid}">--%>
                                            <%--<input type="hidden" name="username" value="${comment.nick}">--%>
                                        </td>
                                        <td>
                                                ${comment.bc}
                                        </td>
                                        <td>
                                                ${comment.createTime}
                                        </td>
                                        <td>
                                                ${comment.uid}
                                        </td>
                                        <td>
                                                ${comment.nick}
                                        </td>
                                        <td>
                                                <%--未审核--%>
                                            <c:if test="${comment.state == 1}">
                                                <%--<a class="btn btn-success btn-sm" href="#"--%>
                                                <%--onclick="verifyComment(2,${comment.id},null,null)">--%>
                                                <%--<i class="glyphicon glyphicon-ok icon-white"></i>通过--%>
                                                <%--</a>--%>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="verifyComment(3,${comment.id},${comment.uid},'${comment.nick}')">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>拉黑
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="verifyComment(4,${comment.id},null,null)">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>删除
                                                </a>
                                            </c:if>
                                                <%--通过--%>
                                            <c:if test="${comment.state != 1}">
                                                <a class="btn btn-primary btn-sm" href="#"
                                                   onclick="verifyComment(1,${comment.id},null,null)">
                                                    <i class="glyphicon glyphicon-open icon-white"></i> 恢复
                                                </a>
                                            </c:if>

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

                $('#allcheck').on('click', function () {
                    if ($(this).prop("checked")) {
                        $(":checkbox").prop("checked", true);
                    } else {
                        $(":checkbox").prop("checked", false);
                    }
                });

                function verifyComment(state, id, uid, nick) {
                    if ("batch" == id) {
                        var ids = [];
                        var uids = [];
                        var nicks = [];
                        $('input[name="commentId"]:checked').each(function () {
                            ids.push($(this).val());
                            uids.push($(this).parent().next().next().next().html().trim());
                            nicks.push($(this).parent().next().next().next().next().html().trim());
                        });
                        if (ids.length == 0) {
                            alert("未选中任何内容");
                            return false;
                        }
                        id = ids.toString();
                        uid = uids.toString();
                        nick = nicks.toString();
                    }
                    post('comment/verify',
                        'ids=' + id + '&uids=' + uid + '&nicks=' + nick + '&state=' + state + '&allState=' + $("#state").val(),
                        function (data) {
                            if (data.status == 1) {
                                alert("操作成功!");
                                turnPage();
                            } else {
                                alert('审核失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境!');
                        });
                }

                $('#selectCommentList').click(function (event) {
                    location.href = '<%=basePath%>comment/list?keyWord=' + $("#keyWord").val() + '&state=' + $("#state").val();
                });

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                function turnPage() {
                    location.href = '<%=basePath%>comment/list?keyWord=' + $("#keyWord").val() + '&state=' + $("#state").val()
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
