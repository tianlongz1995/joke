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
    <title>新增段子</title>
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
        <div class="col-lg-10 col-sm-10">
            <div class="row">
                <div class="box col-md-12" style="margin-top: 0;">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 新增段子</h2>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <input id="title" type="text" class="form-control" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>类型</th>
                                    <td>
                                        <label class="radio-inline">
                                            <input onchange="checkMyRadio(0)" type="radio" name="type" id="type1" value="0"> 段子
                                        </label>
                                        <label class="radio-inline">
                                            <input onchange="checkMyRadio(1)" type="radio" name="type" id="type2" value="1"> 图片
                                        </label>
                                        <label class="radio-inline">
                                            <input onchange="checkMyRadio(1)" type="radio" name="type" id="type3" value="2"> 动图
                                        </label>
                                    </td>
                                </tr>
                                <tr>
                                    <th>图片</th>
                                    <td>
                                        <input id="img" name="img" type="file" accept=".jpg,.jpeg,.png" class="form-control"/>
                                        <input id="image" type="hidden" value=""/>
                                        <img id="imgPriview" style="display: none"   >
                                        <input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>内容</th>
                                    <td>
                                        <textarea id="content" type="text" class="form-control" ROWS="10" COLS="10"></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <th>权重分值</th>
                                    <td>
                                        <input type="text" id="weight" class="form-control" value="">
                                    </td>
                                </thead>
                            </table>
                            <div style="width: 100%;text-align: center;">
                                <a id="addJoke" onclick="addJoke()" class="btn btn-primary" style="vertical-align: middle;" >
                                    <i class="glyphicon glyphicon-plus-sign icon-white"></i> 新增段子
                                </a>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <%--预览--%>
            <div class="modal fade bs-example-modal-lg" id="reviewContent" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-body"  style="max-width: 400px;overflow-x: scroll">
                            <img id="imageView" src="">
                            <input id="gifImage" type="hidden" value=""/>
                            <input id="imageId" type="hidden" value=""/>
                            <input id="imageIndex" type="hidden" value="0"/>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="nextFrame" onclick="nextFrame()" type="button" class="btn btn-default">下一张</button>
                            <button id="submitImage" onclick="submitImage()" type="button" class="btn btn-default">确认</button>
                        </div>
                    </div>
                </div>
            </div>


            <script type="text/javascript">

                function checkMyRadio(type) {
                    if (type == 1) {
                        $("#img").css('display', 'block');
                        $("#imgDelButton").css('display', 'block');
                    } else {
                        $("#img").css('display', 'none');
                        $("#imgDelButton").css('display', 'none');
                    }
                };

                $('#imgDelButton').click(function () {
                    $('#img').val('');
                    $('#image').val('');
                    $("#imgPriview").hide();
                    $("#imgDelButton").css('display', 'none');
                });

                $('#img').change(function () {
                    var type = $('input:radio[name="type"]:checked').val();
                    var file = $(this)[0].files[0];
                    if (type == null) {
                        alert("没有选中类型!");
                        return false;
                    } else {
                        if(type == 1){
                            var point = file.name.lastIndexOf(".");
                            var suffix = file.name.substr(point);
                            if(suffix != '.jpg' && suffix != '.jpeg' && suffix != '.png' && suffix != '.JPG' && suffix != '.JPEG' && suffix != '.PNG'){
                                alert("图片类型只支持:jpg、jpeg、png格式的图片!");
                                return false;
                            }
                        } else if(type == 2){
                            var point = file.name.lastIndexOf(".");
                            var suffix = file.name.substr(point);
                            if(suffix != '.gif'){
                                alert("动图类型只支持:gif格式的图片!");
                                return false;
                            }
                        }

                    }

                    $(this).OupengUpload(file, {
                        url: 'upload/img?${_csrf.parameterName}=${_csrf.token}',
                        acceptFileTypes: 'image/*',
                        maxFileSize: 1024 * 1024 * 5,
                        minFileSize: 0,
                        onUploadSuccess: function (data) {
//                            console.log(data);
                            $('#image').val('' + data);
                            $("#imgPriview").attr('src', data).show();
                            $("#imgDelButton").show();
                        },
                        onUploadError: function (data) {
                            alert(data);
                        }
                    });
                });

                function addJoke() {
                    var type = $('input:radio[name="type"]:checked').val();
                    var weight = $("#weight").val();
                    var content = $("#content").val();
                    var image = $("#image").val();
                    if(weight == null || weight.length < 1 || isNaN(weight)){
                        alert("权重必须是数字!");
                        return false;
                    }
                    if (type == null) {
                        alert("没有选中类型!");
                        return false;
                    } else {
                        if(type > 0 && image.length < 10){
                            alert("您还没有上传图片!");
                            return false;
                        }
                    }
                    $('#addJoke').attr("disabled", "disabled");
                    post('joke/addJoke',
                            'title=' + $("#title").val() + '&type=' + type + '&image=' + image
                            + '&content=' + content + '&weight=' + weight,
                            function (data) {
                                if (data.status == 1) {
                                    if(type == 2){
                                        $("#gifImage").val(data.info);
                                        $("#imageView").attr('src', data.info);
                                        $("#imageId").val(data.data);
                                        $("#reviewContent").modal('show');
                                    } else {
                                        location.href = '<%=basePath%>joke/list';
                                    }
                                } else {
                                    alert('更新失败:' + data['info']);
                                    $('#addJoke').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $('#addJoke').removeAttr("disabled");
                            });
                };

                function nextFrame(){
                    $('#nextFrame').removeAttr("disabled");
                    var img = $("#gifImage").val();
                    var index = parseInt($("#imageIndex").val()) + 1;
                    console.log('nextImage' + img);
                    post('joke/nextFrame',
                            'index=' + index + '&img=' + img,
                            function (data) {
                                if (data.status == 1) {
                                    $("#imageIndex").val(index)
                                    $("#imageView").attr('src', data.info);
                                } else {
                                    alert('更新失败:' + data['info']);
                                    $('#nextFrame').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $('#nextFrame').removeAttr("disabled");
                            });


                };

                function submitImage(){
                    $('#submitImage').removeAttr("disabled");
                    var img = $("#imageView").attr('src');
                    console.log('submitImage' + img);
                    var index = parseInt($("#imageIndex").val());
                    if(index == 0){
                        location.href = '<%=basePath%>joke/list';
                    } else {
                        var id = $("#imageId").val();
                        post('joke/submitImage',
                                'id=' + id + '&img=' + img,
                                function (data) {
                                    if (data.status == 1) {
                                        location.href = '<%=basePath%>joke/list';
                                    } else {
                                        alert('更新失败:' + data['info']);
                                        $('#submitImage').removeAttr("disabled");
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                    $('#submitImage').removeAttr("disabled");
                                });
                    }
                };

                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
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