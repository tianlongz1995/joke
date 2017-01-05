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
    <title>精选管理</title>
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
    <script src="ui/js/jquery.oupeng.upload.js"></script>
    <%--富文本编辑器--%>
    <link  href="/ui/richtext/css/wangEditor.css"rel="stylesheet" type="text/css">
    <script src="/ui/richtext/js/wangEditor.js"></script>


    <!-- The fav icon -->
    <link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
<style type="text/css">
    #editor-trigger {
        height: 500px;
        /*max-height: 500px;*/
    }
    .container {
        width: 100%;
        height: 400px;
        margin: 0 auto;
        position: relative;
    }
</style>
<jsp:include page="../common/topbar.jsp"/>
<div class="ch-container">
    <div class="row">
        <jsp:include page="../common/leftmenu.jsp"/>
        <noscript>
            <div class="alert alert-block col-md-12">
                <h4 class="alert-heading">Warning!</h4>
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled
                    to use this site.
                </p>
            </div>
        </noscript>

        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div>
                <ul class="breadcrumb">
                    <li><a href="choice/list">精选管理</a></li>
                </ul>
            </div>

            <div class="row">
                <div class="box col-md-12">

                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 精选列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info">
                                需要添加新的精选点击:
                                <a href="#" data-toggle="modal" data-target="#newBanner">新增精选</a>
                            </div>

                            <table id="table_list"
                                   class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter">
                                    <div class="form-group" style="display: inline-block;padding-left:10px;">
                                        <label>
                                            <label for="statusSearch" style="display: inline-block;">状态: </label>
                                        </label>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <select class="form-control input" id="statusSearch" onchange="search()">
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >下线
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >上线
                                            </option>
                                        </select>
                                    </label>
                                </div>
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>标题</th>
                                    <th>状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="choice">
                                    <tr>
                                        <td><c:out value="${choice.id}"/></td>
                                        <td><c:out value="${choice.title}"/></td>
                                        <td>
                                            <c:if test="${choice.status == 0}">下线</c:if>
                                            <c:if test="${choice.status == 1}">上线</c:if>
                                        </td>
                                        <td>
                                                <%--下线--%>
                                            <c:if test="${choice.status == 0}">
                                                <button class="btn btn-warning btn-sm" href="#"
                                                   onclick="$('#reviewModalContent').html(${choice.content})">
                                                    <i class="glyphicon glyphicon-zoom-in"></i> 预览
                                                </button>
                                                <a class="btn btn-info btn-sm"
                                                   href="choice/edit?id=${choice.id}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-sm" href="#"
                                                   onclick="delChoice(${choice.id})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(1,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                            </c:if>
                                                <%--上线--%>
                                            <c:if test="${choice.status == 1}">
                                                <a class="btn btn-warning btn-sm" href="#"
                                                   onclick="content()">
                                                    <i class="glyphicon glyphicon-zoom-in"></i> 预览
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(0,${choice.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
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
       <%--新增choice 模态框--%>
            <div class="modal fade bs-example-modal-lg" id="newBanner" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新增精选内容</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <input id="cTitle" class="form-control" type="text" maxlength="8"
                                               style="width:100%;" value=""/>
                                    </td>
                                </tr>

                                <tr>
                                    <th>内容</th>
                                    <td>
                                        <!--富文本编辑器-->
                                        <div id="editor-container" class="container">

                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="addNewChoice" type="button" class="btn btn-primary" data-dismiss="modal">提交
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <%--预览模态框--%>
            <div class="modal fade bs-example-modal-lg" id="review" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" >预览精选内容</h4>
                        </div>
                        <div class="modal-body" id="reviewModalContent">

                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $('#allcheck').on('click', function () {
                    if ($(this).prop("checked")) {
                        $(":checkbox").prop("checked", true);
                    } else {
                        $(":checkbox").prop("checked", false);
                    }
                });
                //新增Choice
                $('#addNewChoice').click(function (event) {
                    $('#addNewChoice').attr("disabled", "disabled");
                    var cTitle = $("#cTitle").val();
                    var content = $("#editor-container").html();
                    //去除空格
                    content = $.trim(content);
                    if (cTitle == "") {
                        alert("请填写标题");
                        $('#addNewChoice').removeAttr("disabled");
                        return false;
                    }
                    //不排除多个换行内容为空的情况
                    if (content=="<p><br></p>") {
                        alert("请编辑内容");
                        $('#addNewChoice').removeAttr("disabled");
                        return false;
                    }
                    post('choice/add',
                            'title=' + cTitle + '&content=' + content,
                            function (data) {
                                if (data['status']) {
                                    location.reload();
                                } else {
                                    $('#addNewChoice').removeAttr("disabled");
                                    alert('添加精选失败. info:' + data['info']);
                                }
                            },
                            function () {
                                $('#addNewChoice').removeAttr("disabled");
                                alert('添加请求失败，请检查网络环境');
                            });
                });
                //choice 查询
                function search() {
                    location.href = '<%=basePath%>choice/list?status=' + $("#statusSearch").val();
                }
                function delChoice(id) {
                    if (confirm("确认删除么？")) {
                        post('choice/del',
                                'id=' + id,
                                function (data) {
                                    if (data['status']) {
                                        location.reload();
                                        <%--location.href = '<%=basePath%>choice/list?status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';--%>
                                    } else {
                                        alert('删除失败. info:' + data['info']);
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                });
                    }
                };
                //上线 下线
                function offlineOnline(status, id) {
                    if (confirm("确认操作?")) {
                        post('banner/offlineOnline',
                                'id=' + id + '&status=' + status,
                                function (data) {
                                    if (data['status']) {
                                        location.href = '<%=basePath%>banner/list?cid=${cid}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';
                                    } else {
                                        alert('操作失败. info:' + data['info']);
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                });
                    }
                };

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };
                /**    分页方法    */
                function turnPage() {
                    location.href = '<%=basePath%>choice/list?status=${status}&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                };

             <%--wangEditor 富文本编辑器--%>
                $(function () {
                    var editor = new wangEditor('editor-container');
                    //图片上传地址
                    editor.config.uploadImgUrl = "upload/richText?${_csrf.parameterName}=${_csrf.token}";
                    // 自定义load事件
                    editor.config.uploadImgFns.onload = function (resultText) {
                        var data = eval('(' + resultText + ')');
                        var imgUrl = data.info;
                        if (data.status == 0) {
                            alert("图片上传失败")
                        } else {
                            var originalName = editor.uploadImgOriginalName || '';
                            // 如果 resultText 是图片的url地址，可以这样插入图片：
                            editor.command(null, 'insertHtml', '<img src="' + imgUrl + '" alt="' + originalName + '" style="max-width:100%;"/>');
                        }

                    };
//                    // 普通的自定义菜单
//                    editor.config.menus = [
//                        'source',
//                        '|',     // '|' 是菜单组的分割线
//                        'bold',
//                        'underline',
//                        'italic',
//                        'strikethrough',
//                        'eraser',
//                        'forecolor',
//                        'bgcolor',
//                        'alignleft',
//                        'img',
//                    ];

                    editor.create();
                });


                function review(content){
                    alert("ddddd");
//                    $("#reviewModalContent").html("");
//                    $("#reviewModalContent").html(content);
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
