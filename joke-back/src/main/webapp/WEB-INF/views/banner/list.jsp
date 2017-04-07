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
    <script src="/ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="/ui/js/date/WdatePicker.js"></script>
    <script src="/ui/js/jquery.oupeng.upload.js"></script>

    <script src="/ui/js/multiple-select.js"></script>
    <link href="/ui/css/multiple-select.css" rel="stylesheet" />
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
                                        <select class="form-control input-sm" id="statusSearch" onchange="search()">
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
                                    <div class="form-group" style="display: inline-block;padding-left:10px;">
                                        <label>
                                            <label for="cidSearch" style="display: inline-block;">频道 : </label>
                                        </label>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <select class="form-control input-sm" id="cidSearch" onchange="search()">
                                            <option value="">全部</option>
                                            <option value="1"
                                                    <c:if test="${!empty cid && cid == 1}">selected</c:if> >趣图
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty cid && cid == 2}">selected</c:if> >段子
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty cid && cid == 3}">selected</c:if> >推荐
                                            </option>
                                            <option value="4"
                                                    <c:if test="${!empty cid && cid == 4}">selected</c:if> >精选
                                            </option>
                                        </select>
                                    </label>
                                    <div class="form-group" style="display: inline-block;padding-left:10px;">
                                        <label>
                                            <label for="didSearch" style="display: inline-block;"> 渠道: </label>
                                        </label>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <select class="form-control input-sm" id="didSearch" onchange="search()">
                                            <option value="">全部</option>
                                            <c:forEach items="${distributor}" var="dis">
                                                <option value="${dis.id}"
                                                        <c:if test="${dis.id == did}">selected="selected"</c:if> >${dis.name}</option>
                                            </c:forEach>
                                        </select>
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
                                    <th>频道</th>
                                    <%--<th>渠道</th>--%>
                                    <th>类型</th>
                                    <th>状态</th>
                                    <th>描述</th>
                                    <th>创建时间</th>
                                    <th>发布时间</th>
                                    <c:if test="${!empty status && status == 3}">
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
                                            <c:if test="${banner.cid == 1}">趣图</c:if>
                                            <c:if test="${banner.cid == 2}">段子</c:if>
                                            <c:if test="${banner.cid == 3}">推荐</c:if>
                                            <c:if test="${banner.cid == 4}">精选</c:if>
                                        </td>
                                        <%--<td>--%>
                                                <%--<c:out value="${banner.dName}"/>--%>
                                        <%--</td>--%>
                                        <td>
                                            <c:if test="${banner.type == 0}">内容</c:if>
                                            <c:if test="${banner.type == 1}">广告</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${banner.status == 0}">新建</c:if>
                                            <c:if test="${banner.status == 1}">下线</c:if>
                                            <c:if test="${banner.status == 2}">上线</c:if>
                                            <c:if test="${banner.status == 3}">已发布</c:if>
                                        </td>
                                        <td><c:out value="${banner.content}"/></td>
                                        <td>
                                            <fmt:formatDate value="${banner.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${banner.publishTime}"
                                                            pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>

                                        <td>
                                            <a class="btn btn-info btn-xs" href="#" onclick="showDistibutorList(${banner.id})">
                                                <i class="glyphicon glyphicon-th-list icon-white"></i> 渠道列表
                                            </a>
                                                <%--新建状态--%>
                                            <c:if test="${banner.status == 0}">
                                                <a class="btn btn-info btn-xs"
                                                   href="banner/edit?id=${banner.id}&status=${status}&cid=${cid}&did=${did}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-xs" href="#"
                                                   onclick="delBanner(${banner.id}, ${banner.cid})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(2,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(4,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>

                                                <%--下线--%>
                                            <c:if test="${banner.status == 1}">

                                                <a class="btn btn-info btn-xs"
                                                   href="banner/edit?id=${banner.id}&status=${status}&cid=${cid}&pageSize=${pageSize}&pageNumber=${pageNumber}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                                </a>
                                                <a class="btn btn-warning btn-xs" href="#"
                                                   onclick="delBanner(${banner.id}, ${banner.cid})">
                                                    <i class="glyphicon glyphicon-trash"></i> 删除
                                                </a>
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(2,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 上线
                                                </a>
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(4,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>
                                                <%--上线 --%>
                                            <c:if test="${banner.status == 2 }">
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(1,${banner.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(4,${banner.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>立即发布
                                                </a>
                                            </c:if>

                                                <%--已发布--%>
                                            <c:if test="${banner.status == 3 }">
                                                <a class="btn btn-danger btn-xs" href="#"
                                                   onclick="offlineOnline(1,${banner.id})">
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
                                            <option value="1">趣图</option>
                                            <option value="2">段子</option>
                                            <option value="3">推荐</option>
                                            <option value="4">精选</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>渠道</th>
                                    <td>
                                        <select id="did" style="width: 100%;" multiple="multiple">
                                            <c:forEach items="${distributor}" var="dis">
                                                <option value="${dis.id}">${dis.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>

                                <tr id="imgTr">
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


            <div class="modal fade" id="dList" tabindex="-1" role="dialog" aria-labelledby="dListModal"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="dListModal">渠道列表</h4>
                        </div>
                        <div class="modal-body">
                            <table id="dListBody" class="table table-hover">

                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
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
                    var did = $("#did").multipleSelect("getSelects");
                    var img = $("#imgPriview").attr("src");
                    var cid = $('#cid').val();
                    console.log('did='+did);

                    if(did.length < 1){
                        alert("必须选择渠道!");
                        $('#addNewBanner').removeAttr("disabled");
                        return false;
                    }
                    if ($("#type").val() == 0) {
                        if (jid == "") {
                            alert("必须填写段子编号");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                        if (img == "") {
                            alert("必须上传图片");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                    } else {
                        if (adId == "") {
                            alert("必须填写广告位id");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                    }
                    var imgWidth = $("#imgWidth").val();
                    var type =  $('#type').val();
                    if (type == 0 && imgWidth < 200) {
                        alert("图片宽度必须大于200");
                        $('#addNewBanner').removeAttr("disabled");
                        return false;
                    }
                    post('banner/add',
                            'title=' + $('#addTitle').val() + '&cid=' + cid + '&type=' + type + '&jid=' + $('#jokeId').val() + '&img=' + $('#image').val() + '&content=' + $('#addContent').val() + '&adid=' + $('#adId').val() + '&width=' + $("#imgWidth").val() + '&height=' + $("#imgHeight").val() + '&publishTime=' + $("#publishTime").val() + '&did=' + did,
                            function (data) {
                                if (data['status']) {
                                    alert("添加成功");
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
                    location.href = '<%=basePath%>banner/list?status=' + $("#statusSearch").val() + '&cid=' + $("#cidSearch").val() + '&did=' + $("#didSearch").val();
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
                                        alert("操作成功");
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
                function move(id, cid, did, type, sort) {
                    post('banner/bannerMove',
                            'id=' + id + '&type=' + type + '&sort=' + sort + '&cid=' + cid+'&cid=' + cid,'&did=' + did,
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
                }
                ;
                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                /**    分页方法    */
                function turnPage() {
                    location.href = '<%=basePath%>banner/list?did=${did}&cid=${cid}&status=${status}&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                }
                ;
                //初始化新增模态框显示
                function init() {
                    $("#adIdTr").hide();
                    $('#did').multipleSelect({
                        multiple: true,
                        selectAllText: '全选',
                        multipleWidth: 400,
                        placeholder: '请选择',
                        maxHeight: 500

                    });
                }
                //控制新增模态框显示内容
                function hideOthers() {
                    var flag = $("#type").val();
                    if (flag == 1) {
//                        $("#cidTr").hide();
                        $("#imgTr").hide();
                        $("#jokeIdTr").hide();
                        $("#adIdTr").show();
                    } else {
//                        $("#cidTr").show();
                        $("#imgTr").show();
                        $("#jokeIdTr").show();
                        $("#adIdTr").hide();
                    }
                }
                ;
                function showDistibutorList(id) {
                    post('banner/distributorList',
                            'id=' + id,
                            function (data) {
                                if (data.status == 1) {
                                    console.log(data.data);
                                    $("#dListBody").html('');
                                    $("#dListBody").append('<tr><th>渠道编号</th><th>渠道名称</th></tr>');
                                    var array = data.data;
                                    var body = $("#dListBody");
                                    var html = '';
                                    for(var index in array){
                                        console.log(array[index].id + "=" + array[index].name);
                                        html += '<tr><td>';
                                        html += array[index].id;
                                        html += '</td><td>';
                                        html += array[index].name;
                                        html += '</td></tr>';
                                    }
                                    body.append(html);
                                    $("#dList").modal('show');
                                } else {
                                    alert('获取失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

                /** 修改排序值   */
                function editSort(id) {
                    var sort = $("#sort" + id).val();
                    console.log(id + ":" + sort);
                    post('banner/editSorts',
                            'ids=' + id  + '&sorts=' + sort,
                            function (data) {
                                if (data.status == 1) {
                                    alert('成功:' + data.info);
                                    location.href = '<%=basePath%>banner/list?cid=${cid}&status=${status}&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
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
