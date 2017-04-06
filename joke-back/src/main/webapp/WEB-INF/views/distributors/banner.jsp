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
    <title>Banner列表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="/ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="/ui/js/date/WdatePicker.js"></script>
    <script src="/ui/js/jquery.oupeng.upload.js"></script>

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
                            <h2><i class="glyphicon glyphicon-user"></i> Banner列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info">
                                <a href="<%=basePath%>distributors/list?status=${status}&limit=${limit}&pageNo=${pageNo}" class="btn btn-success btn-sm" >返回</a>
                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>标题</th>
                                    <th>频道</th>
                                    <th>排序值</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="banner">
                                    <tr>
                                        <td><c:out value="${banner.id}"/></td>
                                        <td><c:out value="${banner.title}"/></td>
                                        <td>
                                            <c:if test="${banner.cid == 1}">趣图</c:if>
                                            <c:if test="${banner.cid == 2}">段子</c:if>
                                            <c:if test="${banner.cid == 3}">推荐</c:if>
                                            <c:if test="${banner.cid == 4}">精选</c:if>
                                        </td>
                                        <td>
                                            <input id="sort${banner.dbId}" type="text" class="form-control input-sm" style="width: 20px;padding: 1px;text-align: center;" value="${banner.sort}">
                                        </td>
                                        <td>
                                            <a class="btn btn-primary btn-xs" href="#" style="margin-bottom: 2px;" onclick="editSort(${banner.dbId}, ${did})">
                                                <i class="glyphicon glyphicon-ok icon-white"></i> 排序
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };

                /** 修改排序值   */
                function editSort(id, did) {
                    var sort = $("#sort" + id).val();
                    console.log(id + ":" + sort);
                    post('banner/editSorts',
                            'ids=' + id  + '&sorts=' + sort + '&did=' + did,
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>distributors/banner?did=${did}&status=${status}&limit=${limit}&pageNo=${pageNo}';
                                } else {
                                    alert('处理失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };
            </script>

        </div><!-- content end -->
    </div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
