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
    <title>Banner编辑</title>
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
                            <h2><i class="glyphicon glyphicon-user"></i> Banner编辑</h2>
                        </div>
                        <div class="box-content" style="text-align: center;">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <td><input id="bannerId" type="text" class="form-control" disabled="disabled" value="${banner.id}"/></td>
                                </tr>
                                <tr>
                                    <th>状态</th>
                                    <td>
                                        <c:if test="${banner.status == 0}">
                                            <input id="bannerStatus" type="text" class="form-control" disabled="disabled" value="新建"/>
                                        </c:if>
                                        <c:if test="${banner.status == 1}">
                                            <input id="bannerStatus" type="text" class="form-control" disabled="disabled" value="下线"/>
                                        </c:if>
                                        <c:if test="${banner.status == 2}">
                                            <input id="bannerStatus" type="text" class="form-control" disabled="disabled" value="上线"/>
                                        </c:if>
                                        <c:if test="${banner.status == 3}">
                                            <input id="bannerStatus" type="text" class="form-control" disabled="disabled" value="已发布"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <th width="207px">内容类型</th>
                                    <td>
                                        <select class="form-control input" style="width: 100%;" id="type" onclick="hideOthers()">
                                            <option style="width: 90%;" value="0"
                                                    <c:if test="${!empty banner.type && banner.type == 0}">selected</c:if> >
                                                内容
                                            </option>
                                            <option style="width: 90%;" value="1"
                                                    <c:if test="${!empty banner.type && banner.type == 1}">selected</c:if> >
                                                广告
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>发布时间</th>
                                    <td>
                                        <input id="publishTime" type="text"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})"
                                               class="form-control"
                                               value="${banner.publishTimeString}"/>
                                    </td>
                                <tr/>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <c:if test="${empty banner.title}">
                                            <input id="title" type="text" value="" style="width:100%;"
                                                   class="form-control"/>
                                        </c:if>
                                        <c:if test="${!empty banner.title}">
                                            <input id="title" type="text" style="width:100%;" value="${banner.title}"
                                                   class="form-control"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr id="jokeIdTr">
                                    <th>段子id</th>
                                    <td>
                                        <input id="jokeId" type="number" style="width:100%;" value="${banner.jid}"
                                               class="form-control"/>
                                    </td>
                                </tr>
                                <tr id="adIdTr">
                                    <th>广告位id</th>
                                    <td>
                                        <input id="adId" type="number" style="width:100%;" value="${banner.slot}"
                                               class="form-control"/>
                                    </td>
                                </tr>
                                <tr id="cidTr">
                                    <th>频道类型</th>
                                    <td>
                                        <select class="form-control input" id="cid">
                                            <option value="1"
                                                    <c:if test="${!empty banner.cid && banner.cid == 1}">selected</c:if> >
                                                趣图
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty banner.cid && banner.cid == 2}">selected</c:if> >
                                                段子
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty banner.cid && banner.cid == 3}">selected</c:if> >
                                                推荐
                                            </option>
                                            <option value="4"
                                                    <c:if test="${!empty banner.cid && banner.cid == 4}">selected</c:if> >
                                                精选
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>渠道类型</th>
                                    <td>
                                        <select id="did" style="max-width: 80%;min-width: 50%;" multiple="multiple">
                                            <c:forEach items="${distributor}" var="dis">
                                                <option value="${dis.id}" >${dis.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>

                                <tr id="imgTr">
                                    <th>图片</th>
                                    <td>
                                        <input id="img" name="img" type="file" accept=".jpg,.jpeg,.png"
                                               class="form-control"/>
                                        <c:if test="${empty banner.img}">
                                            <input id="image" type="hidden"/>
                                            <img id="imgPriview" style="display: none;width:60%;height:300px;" src="">
                                        </c:if>
                                        <c:if test="${!empty banner.img}">
                                            <input id="image" type="hidden" value="${banner.img}"/>
                                            <img id="imgPriview" style=width:60%;" src="${banner.img}">
                                            <input id="imgDelButton" type="button" class="btn btn-danger btn"
                                                   value="删除"/>
                                        </c:if>

                                        <input type="hidden" value="${banner.width}" id="imgWidth">
                                        <input type="hidden" value="${banner.height}" id="imgHeight">

                                    </td>
                                </tr>
                                <tr>
                                    <th>描述</th>
                                    <td>
                                        <c:if test="${empty banner.content}">
                                            <input id="bannerContent" type="text" class="form-control"
                                                   value="${banner.content}"/>
                                        </c:if>
                                        <c:if test="${!empty banner.content}">
                                            <input id="bannerContent" type="text" class="form-control"
                                                   value="${banner.content}"/>
                                        </c:if>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <button id="updateBanner" type="button" class="btn btn-primary btn-lg" data-dismiss="modal">
                                &nbsp;&nbsp;提交&nbsp;&nbsp;</button>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                $(document).ready(function () {
                    var flag = $("#type").val();
                    if (flag == 1) {
                        $("#cidTr").hide();
                        $("#imgTr").hide();
                        $("#jokeIdTr").hide();
                    } else {
                        $("#adIdTr").hide();
                    }
                    if ('${banner.img}' != '') {
                        $("#imgPriview").css('display', 'block');
                        $("#imgDelButton").css('display', 'block');
                    }
                    $('#did').multipleSelect({
                        multiple: true,
                        selectAllText: '全选',
                        multipleWidth: 180,
                        placeholder: '请选择',
                        maxHeight: 500

                    });
                    $("#did").multipleSelect("setSelects", ${did});
                });

                $('#imgDelButton').click(function () {
                    $('#img').val('');
                    $('#image').val('');
                    $("#imgPriview").hide();
                });
                //文件上传
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
                //更新Banner
                $('#updateBanner').click(function (event) {
                    var jid = $("#jokeId").val();
                    var adId = $("#adId").val();
                    var img = $("#imgPriview").attr("src");
                    var did = $("#did").multipleSelect("getSelects");
                    //内容 段子id不能为空
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
                    } else { //广告 广告位id不为空
                        if (adId == "") {
                            alert("必须填写广告位id");
                            $('#addNewBanner').removeAttr("disabled");
                            return false;
                        }
                    }
                    var imgWidth = $("#imgWidth").val();
                    var type = $('#type').val();
                    if (type == 0 && imgWidth < 200) {
                        alert("图片宽度必须大于200");
                        return false;
                    }
                    $('#updateBanner').attr("disabled", "disabled");

                    post('banner/update',
                            'id=' + $("#bannerId").val() + '&title=' + $("#title").val() + '&jid=' + $("#jokeId").val() + '&cid=' + $("#cid").val() + '&content=' + $('#bannerContent').val() + '&type=' + type
                            + '&img=' + $('#image').val() + '&adId=' + $('#adId').val() + '&publishTime=' + $("#publishTime").val() + '&width=' + $("#imgWidth").val() + '&height=' + $("#imgHeight").val() + '&did=' + did,
                            function (data) {
                                if (data['status']) {
                                    alert("更新成功");
                                    location.href = '<%=basePath%>banner/list?cid=${cid}&status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';
                                } else {
                                    alert('更新失败. info:' + data['info']);
                                    $('#updateBanner').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('更新请求失败，请检查网络环境');
                                $('#updateBanner').removeAttr("disabled");
                            });
                });

                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                //控制新增模态框显示内容
                function hideOthers() {
                    var flag = $("#type").val();
                    if (flag == 1) {
                        $("#cidTr").hide();
                        $("#imgTr").hide();
                        $("#jokeIdTr").hide();
                        $("#adIdTr").show();
                    } else {
                        $("#cidTr").show();
                        $("#imgTr").show();
                        $("#jokeIdTr").show();
                        $("#adIdTr").hide();
                    }
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