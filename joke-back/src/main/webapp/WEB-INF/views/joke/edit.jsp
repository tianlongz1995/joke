<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>内容编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="ui/js/jquery.oupeng.upload.js"></script>
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
                            <h2><i class="glyphicon glyphicon-user"></i> 内容编辑</h2>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <c:if test="${empty joke.title}">
                                            <input id="title" type="text" class="form-control" value=""/>
                                        </c:if>
                                        <c:if test="${!empty joke.title}">
                                            <input id="title" type="text" class="form-control" value="${joke.title}"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <th>图片 /动图</th>
                                    <td>
                                        <%--<input id="img" name="img" type="file" accept=".jpg,.jpeg,.png,.gif"/>--%>
                                        <img id="imgPriview" style="display: none"
                                        <c:if test="${joke.type == 2}"> src="${joke.gif}" </c:if>
                                        <c:if test="${joke.type == 1}"> src="${joke.img}" </c:if> >
                                        <%--<input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除"/>--%>
                                    </td>
                                </tr>
                                <tr>
                                    <th>内容</th>
                                    <td>
                                        <textarea id="content2" type="text" class="form-control" ROWS="10" COLS="10"><c:out value="${joke.content}"/></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <th>分值</th>
                                    <td>
                                        <input type="text" id="weight" class="form-control" value="${joke.weight}">
                                    </td>
                                </thead>
                            </table>
                            <input id="id" type="hidden" value="${joke.id}"/>
                            <button id="updateJoke" type="button" class="btn btn-primary btn-sm" data-dismiss="modal">通过</button>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                $(document).ready(function () {
                    if (('${joke.type}' == '2' || '${joke.type}' == '1')) {
                        $("#imgPriview").css('display', 'block');
                        $("#imgDelButton").css('display', 'block');
                    }
                });


                $('#updateJoke').click(function () {
                    $('#updateJoke').attr("disabled", "disabled");
                    var id = $("#id").val();
                    var title = $("#title").val();
                    var content = $("#content2").val();
                    var weight = $("#weight").val();

                    if(weight == null || weight.length < 1 || isNaN(weight)){
                        alert("权重必须是数字!");
                        return false;
                    }

                    post('joke/update',
                            'id=' + id + '&title=' + title + '&content=' + content + '&weight=' + weight,
                            function (data) {
                                if (data['status']) {
                                    location.href = '<%=basePath%>joke/list';
                                } else {
                                    alert('更新失败. info:' + data['info']);
                                    $('#updateJoke').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $('#updateJoke').removeAttr("disabled");
                            });
                });

                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
            </script>

        </div><!-- content end -->
    </div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>