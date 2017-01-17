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
    <title>Banner管理</title>
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
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled
                    to use this site.
                </p>
            </div>
        </noscript>

        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div>
                <ul class="breadcrumb">
                    <li><a href="banner/list">Banner管理</a></li>
                </ul>
            </div>

            <div class="row">
                <div class="box col-md-12">

                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> Banner列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info">
                                需要添加新的Banner点击:
                                <a href="#" data-toggle="modal" data-target="#newBanner" onclick="init()">新增Banner</a>
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
                                            <%--<option value="">全部</option>--%>
                                            <option value="0"
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >下线
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >上线
                                            </option>
                                        </select>
                                    </label>
                                    <div class="form-group" style="display: inline-block;padding-left:10px;">
                                        <label>
                                            <label for="cidSearch" style="display: inline-block;">频道 : </label>
                                        </label>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <select class="form-control input" id="cidSearch" onchange="search()">
                                            <%--<option value="">全部</option>--%>
                                            <option value="1"
                                                    <c:if test="${!empty cid && cid == 1}">selected</c:if> >段子
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty cid && cid == 2}">selected</c:if> >趣图
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty cid && cid == 3}">selected</c:if> >推荐
                                            </option>
                                            <option value="4"
                                                    <c:if test="${!empty cid && cid == 4}">selected</c:if> >精选
                                            </option>
                                        </select>
                                    </label>
                                </div>

                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>标题</th>
                                    <th>图片</th>
                                    <th>广告位id</th>
                                    <th>段子id</th>
                                    <th>频道类型</th>
                                    <th>内容类型</th>
                                    <th>状态</th>
                                    <th>描述</th>
                                    <c:if test="${ not empty cid}">
                                        <th>排序值</th>
                                    </c:if>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="banner">
                                    <tr>
                                        <td><c:out value="${banner.id}"/></td>
                                        <td><c:out value="${banner.title}"/></td>
                                        <td>
                                            <img src="${banner.img}" style="width: 176px;height: 100px;">
                                        </td>
                                        <td><c:out value="${banner.slot}"/></td>
                                        <td><c:out value="${banner.jid}"/></td>
                                        <td>
                                            <c:if test="${banner.cid == 1}">段子</c:if>
                                            <c:if test="${banner.cid == 2}">趣图</c:if>
                                            <c:if test="${banner.cid == 3}">推荐</c:if>
                                            <c:if test="${banner.cid == 4}">精选</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${banner.type == 0}">内容</c:if>
                                            <c:if test="${banner.type == 1}">广告</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${banner.status == 0}">下线</c:if>
                                            <c:if test="${banner.status == 1}">上线</c:if>
                                        </td>
                                        <td><c:out value="${banner.content}"/></td>
                                        <c:if test="${ not empty cid}">
                                            <td><c:out value="${banner.sort}"/></td>
                                        </c:if>
                                        <td>
                                                <%--下线--%>
                                            <c:if test="${banner.status == 0}">
                                                <a class="btn btn-info btn-sm"
                                                   href="banner/edit?id=${banner.id}&status=${status}&cid=${cid}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-sm" href="#"
                                                   onclick="delBanner(${banner.id},${banner.cid})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(1,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                            </c:if>
                                                <%--上线--%>
                                            <c:if test="${banner.status == 1}">
                                                <a class="btn btn-danger btn-sm" href="#"
                                                   onclick="offlineOnline(0,${banner.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                                <c:if test="${ not empty cid}">
                                                    <c:if test="${firstElement != banner.id}">
                                                        <a class="btn btn-success btn-sm" href="#"
                                                           onclick="move('${banner.id}','${banner.cid}', 1, ${banner.sort})">
                                                            <i class="glyphicon glyphicon-arrow-up"></i> 上移
                                                        </a>
                                                    </c:if>

                                                    <a class="btn btn-success btn-sm" href="#"
                                                       onclick="move('${banner.id}','${banner.cid}', 2, ${banner.sort})">
                                                        <i class="glyphicon glyphicon-arrow-down"></i> 下移
                                                    </a>
                                                </c:if>
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

            <div class="modal fade" id="newBanner" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新增Banner列表内容</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>内容类型</th>
                                    <td>
                                        <select class="form-control" id="type" onchange="hideOthers()">
                                            <option value="0">内容</option>
                                            <option value="1">广告</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <input id="addTitle" class="form-control" type="text"
                                               style="width:100%;" value=""/>
                                    </td>
                                </tr>
                                <tr id="jokeIdTr">
                                    <th>段子id</th>
                                    <td>
                                        <input id="jokeId" class="form-control" type="number" style="width:100%;"
                                               value=""/>
                                    </td>
                                </tr>
                                <tr id="adIdTr">
                                    <th>广告位id</th>
                                    <td>
                                        <input id="adId" class="form-control" type="number" style="width:100%;"
                                               value=""/>
                                    </td>
                                </tr>
                                <tr id="cidTr">
                                    <th>频道类型</th>
                                    <td>
                                        <select class="form-control" id="cid">
                                            <option value="1">段子</option>
                                            <option value="2">趣图</option>
                                            <option value="3">推荐</option>
                                            <option value="4">精选</option>
                                        </select>
                                    </td>
                                </tr>

                                <tr id="imgTr">
                                    <th>图片</th>
                                    <td>
                                        <input id="img" name="img" type="file" accept=".jpg,.jpeg,.png"
                                               class="form-control"/>
                                        <input id="image" type="hidden"/>
                                        <img id="imgPriview" style="display: none;width:60%;height:300px;" src=""/>
                                        <input type="hidden" id="imgWidth" value="">
                                        <input type="hidden" id="imgHeight" value="">
                                        <input id="imgDelButton" type="button" class="btn btn-default"
                                               style="display: none" value="删除"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>描述</th>
                                    <td>
                                        <input id="addContent" type="text" class="form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="addNewBanner" type="button" class="btn btn-primary" data-dismiss="modal">提交
                            </button>
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

                //新增banner
                $('#addNewBanner').click(function (event) {
                    $('#addNewBanner').attr("disabled", "disabled");
                    var jid = $("#jokeId").val();
                    var adId = $("#adId").val();
                    var img = $("#imgPriview").attr("src");

                    if ($("#type").val() == 0) {
                        if (jid == "") {
                            alert("必须填写段子编号");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                        if(img == ""){
                            alert("必须上传图片");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                    }else{
                        if (adId == "") {
                            alert("必须填写广告位id");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                    }

                    post('banner/add',
                            'title=' + $('#addTitle').val() + '&cid=' + $('#cid').val() + '&type=' + $('#type').val() + '&jid=' + $('#jokeId').val() + '&img=' + $('#image').val() + '&content=' + $('#addContent').val() + '&adid=' + $('#adId').val()+'&width='+$("#imgWidth").val()+'&height='+$("#imgHeight").val(),
                            function (data) {
                                if (data['status']) {
                                    location.reload();
                                } else {
                                    $('#addNewBanner').removeAttr("disabled");
                                    alert('添加Banner失败. info:' + data['info']);
                                }
                            },
                            function () {
                                $('#addNewBanner').removeAttr("disabled");
                                alert('请求失败，请检查网络环境');
                            });
                });
                //banner 查询
                function search() {
                    location.href = '<%=basePath%>banner/list?status=' + $("#statusSearch").val() + '&cid=' + $("#cidSearch").val();
                }

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

                function delBanner(id, cid) {
                    if (confirm("确认删除么？")) {
                        post('banner/del',
                                'id=' + id + '&cid=' + cid,
                                function (data) {
                                    if (data['status']) {
                                        location.href = '<%=basePath%>banner/list?cid=${cid}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';
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
                }
                ;
                //移动
                function move(id, cid, type, sort) {
                    post('banner/bannerMove',
                            'id=' + id + '&type=' + type + '&sort=' + sort + '&cid=' + cid,
                            function (data) {
                                if (data['status']) {
                                    //刷新页面
                                    location.reload();
                                } else {
                                    alert('banner位置失败:' + data.info);
                                }
                            },
                            function () {
                                alert('移动请求失败，请检查网络环境');
                            });
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
                    location.href = '<%=basePath%>banner/list?cid=${cid}&status=${status}&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                };
                //初始化新增模态框显示
                function init(){
                    $("#adIdTr").hide();
                }
                //控制新增模态框显示内容
                function hideOthers(){
                    var flag = $("#type").val();
                    if(flag == 1){
                        $("#cidTr").hide();
                        $("#imgTr").hide();
                        $("#jokeIdTr").hide();
                        $("#adIdTr").show();
                    }else{
                        $("#cidTr").show();
                        $("#imgTr").show();
                        $("#jokeIdTr").show();
                        $("#adIdTr").hide();
                    }
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
