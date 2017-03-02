<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>频道编辑</title>
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
        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div class="row">
                <div class="box col-md-12" style="margin-top: 0;">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 频道编辑</h2>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <td><input id="channelid" type="text" class="form-control" disabled="disabled" value="${channel.id}"/></td>
                                </tr>
                                <tr>
                                    <th>名称</th>
                                    <td><input id="name" type="text" class="form-control" value="${channel.name}"/></td>
                                </tr>
                                <tr>
                                    <th>发布数量</th>
                                    <td><input id="size" type="text" class="form-control" placeholder="发布数量" value="${channel.size}"/></td>
                                </tr>
                                <tr>
                                    <th>类别</th>
                                    <td>
                                        <select id="type" class="form-control">
                                            <option value="0" <c:if test="${0 == channel.type}">selected</c:if>>普通</option>
                                            <option value="1" <c:if test="${1 == channel.type}">selected</c:if>>专题</option>
                                            <option value="2" <c:if test="${2 == channel.type}">selected</c:if>>推荐</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>内容属性</th>
                                    <td>
                                        <label style="padding-right:30px;">
                                            <span>文字</span>
                                            <input name="addcontentType" type="checkbox" value="0"
                                                    <c:if test="${fn:contains(channel.contentType, '0')}"> checked</c:if>
                                            />
                                        </label>
                                        <label style="padding-right:30px;">
                                            <span>图片</span>
                                            <input name="addcontentType" type="checkbox" value="1"
                                                    <c:if test="${fn:contains(channel.contentType, '1')}"> checked</c:if>
                                            />
                                        </label>
                                        <label style="padding-right:30px;">
                                            <span>动图</span>
                                            <input name="addcontentType" type="checkbox" value="2"
                                                    <c:if test="${fn:contains(channel.contentType, '2')}"> checked</c:if>
                                            />
                                        </label>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <button id="updateChannel" type="button" class="btn btn-default btn-sm" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                $('#updateChannel').click(function (event) {
                    $('#updateChannel').attr("disabled", "disabled");
                    var contentType = [];
                    $('input[name="addcontentType"]:checked').each(function () {
                        contentType.push($(this).val());
                    });
                    if (contentType.length == 0) {
                        alert("未选中任何内容属性");
                        $('#updateChannel').removeAttr("disabled");
                        return false;
                    }
                    var size = $('#size').val();
                    if (size < 1 || size > 1000) {
                        alert("发布数据数量必须在1~1000之内!");
                        $('#updateChannel').removeAttr("disabled");
                        return false;
                    }
                    post('channel/update',
                            'id=' + $("#channelid").val() + '&name=' + $("#name").val() + '&type=' + $('#type').val() + '&contentType=' + contentType.toString() + '&size=' + size,
                            function (data) {
                                if (data['status']) {
                                    alert("更新成功");
                                    location.href = '<%=basePath%>channel/list';
                                } else {
                                    alert('更新失败. info:' + data['info']);
                                    $('#updateChannel').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $('#updateChannel').removeAttr("disabled");
                            });
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

        </div><!-- content end -->
    </div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>