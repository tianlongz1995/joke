<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>

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
    <link href="/ui/richtext/css/wangEditor.css" rel="stylesheet" type="text/css">
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
        height: 200px;
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
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >新建
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >下线
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty status && status == 2}">selected</c:if> >上线
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty status && status == 3}">selected</c:if> >已发布
                                            </option>
                                        </select>
                                    </label>
                                </div>
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>标题</th>
                                    <th>图片</th>
                                    <th>状态</th>
                                    <th>创建时间</th>
                                    <th>更新时间</th>
                                    <th>发布时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="choice">
                                    <tr>
                                        <td><c:out value="${choice.id}"/></td>
                                        <td><c:out value="${choice.title}"/></td>
                                        <td>
                                            <img src="${choice.img}" style="width: 176px;height: 100px;">
                                        </td>
                                        <td>
                                            <c:if test="${choice.status == 0}">新建</c:if>
                                            <c:if test="${choice.status == 1}">下线</c:if>
                                            <c:if test="${choice.status == 2}">上线</c:if>
                                            <c:if test="${choice.status == 3}">已发布</c:if>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${choice.createTime}"
                                                            pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${choice.updateTime}"
                                                            pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${choice.publishTime}"
                                                            pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                                <%--新建--%>
                                            <c:if test="${choice.status == 0}">

                                                <a class="btn btn-info btn-sm"
                                                   href="choice/edit?id=${choice.id}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-sm" href="#"
                                                   onclick="delChoice(${choice.id})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(2,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                                <%--<a class="btn btn-danger btn-sm" href="#"--%>
                                                   <%--onclick="offlineOnline(1,${choice.id})">--%>
                                                    <%--<i class="glyphicon glyphicon-remove icon-white"></i>下线--%>
                                                <%--</a>--%>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(4,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>
                                                <%--下线--%>
                                            <c:if test="${choice.status == 1}">
                                                <a class="btn btn-info btn-sm"
                                                   href="choice/edit?id=${choice.id}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-sm" href="#"
                                                   onclick="delChoice(${choice.id})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(2,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(4,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>
                                                <%--上线--%>
                                            <c:if test="${choice.status == 2}">
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(1,${choice.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(4,${choice.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>
                                                <%--已发布--%>
                                            <c:if test="${choice.status == 3 }">
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(1,${choice.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                            </c:if>

                                            <a class="btn btn-primary btn-sm" href="#" data-toggle="modal"
                                               data-target="#reviewContent"
                                               onclick="review(${choice.id})">
                                                <i class="glyphicon glyphicon-zoom-in"></i> 预览
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
            <%--新增choice 模态框--%>
            <div class="modal fade bs-example-modal-lg" id="newBanner" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel"
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
                                    <th>发布时间</th>
                                    <td>
                                        <input id="publishTime" type="text"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})"
                                               class="form-control"
                                               value=""/>
                                    </td>
                                <tr/>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <input id="cTitle" class="form-control" type="text"
                                               style="width:100%;" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>图片</th>
                                    <td>
                                        <input id="img" name="img" type="file" accept=".jpg,.jpeg,.png"
                                               class="form-control"/>
                                        <input id="image" type="hidden"/>
                                        <img id="imgPriview" style="display: none;width:60%;" src=""/>
                                        <input type="hidden" id="imgWidth" value="">
                                        <input type="hidden" id="imgHeight" value="">

                                        <input id="imgDelButton" type="button" class="btn btn-default"
                                               style="display: none" value="删除"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>内容(图片宽度需超过200)</th>
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
            <%--预览--%>
            <div class="modal fade bs-example-modal-lg" id="reviewContent" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="choiceContentId"></h4>
                        </div>
                        <div class="modal-body" id="reviewModalContent" style="height: 400px;overflow-y: scroll">

                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </div>
            </div>
            <script type="text/javascript">
                $(document).ready(function () {
                    if ('${banner.img}' != '') {
                        $("#imgPriview").css('display', 'block');
                        $("#imgDelButton").css('display', 'block');
                    }
                });
                var editor = new wangEditor('editor-container');

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
                    // 获取编辑器纯文本内容
                    var onlyText = editor.$txt.text();

                    var img = $("#imgPriview").attr("src");
                    //去除空格
                    var content = $("#editor-container").html();
                    content = $.trim(content);
                    console.log(content);
                    if (cTitle == "") {
                        alert("请填写标题");
                        $('#addNewChoice').removeAttr("disabled");
                        return false;
                    }
                    onlyText = $.trim(onlyText);
                    //不排除多个换行内容为空的情况
                    if (onlyText == "") {
                        alert("请编辑内容,添加文字");
                        $('#addNewChoice').removeAttr("disabled");
                        return false;
                    }
                    if (img == "") {
                        alert("必须上传图片");
                        $('#addNewChoice').removeAttr("disabled");
                        return false;
                    }
                    var imgWidth = $("#imgWidth").val();
                    if(imgWidth<200){
                        alert("图片宽度必须大于200");
                        return false;
                    }
                    post('choice/add',
                            'title=' + cTitle + '&content=' + encodeURIComponent(content) + '&image=' + img + '&width=' + $("#imgWidth").val() + '&height=' + $("#imgHeight").val() + '&publishTime=' + $("#publishTime").val(),
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
                }
                ;
                //上线 下线
                function offlineOnline(status, id) {
                    if (confirm("确认操作?")) {
                        post('choice/offlineOnline',
                                'id=' + id + '&status=' + status,
                                function (data) {
                                    if (data['status']) {
                                        alert("操作成功");
                                        location.href = '<%=basePath%>choice/list?status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';
                                    } else {
                                        alert('操作失败. info:' + data['info']);
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                });
                    }
                }
                ;

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'GET', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                /**    分页方法    */
                function turnPage() {
                    location.href = '<%=basePath%>choice/list?status=${status}&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                }
                ;

                <%--wangEditor 富文本编辑器--%>
                $(function () {
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
                //预览
                function review(cid) {
                    $("#reviewModalContent").html("");
                    $("#choiceContentId").html("精选内容预览，ID:" + cid);
                    post('choice/review',
                            'id=' + cid,
                            function (data) {
                                $("#reviewModalContent").html(data.info);
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                }
                ;
                $('#imgDelButton').click(function () {
                    $('#img').val('');
                    $('#image').val('');
                    $("#imgPriview").hide();
                });

                $('#img').change(function () {
                    var file = $(this)[0].files[0];
                    $(this).OupengUpload(file, {
                        url: 'upload/cbImg?${_csrf.parameterName}=${_csrf.token}',
                        acceptFileTypes: 'image/*',
                        maxFileSize: 1024 * 1024 * 5,
                        minFileSize: 0,
                        onUploadSuccess: function (data) {
                            var result = eval("(" + data + ")");
                            $("#image").val(result.url);
                            $("#imgPriview").attr('src', result.url).show();
                            $("#imgWidth").val(result.width);
                            $("#imgHeight").val(result.height);
                            $("#imgDelButton").show();
                        },
                        onUploadError: function (data) {
                            alert(data);
                        }
                    });
                });
            </script>

        </div><!-- content end -->
    </div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
