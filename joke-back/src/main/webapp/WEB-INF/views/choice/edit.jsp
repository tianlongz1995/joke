<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>精选编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html"%>
    <script src="/ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="/ui/js/date/WdatePicker.js"></script>
    <script src="/ui/js/jquery.oupeng.upload.js"></script>

    <%--富文本编辑器--%>
    <link  href="/ui/richtext/css/wangEditor.css"rel="stylesheet" type="text/css">
    <script src="/ui/richtext/js/wangEditor.js"></script>
    <!-- The fav icon -->
    <link rel="shortcut icon" href="/ui/charisma/img/favicon.ico">
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
<jsp:include page="../common/topbar.jsp" />
<div class="ch-container">
    <div class="row">
        <jsp:include page="../common/leftmenu.jsp" />
        <noscript>
            <div class="alert alert-block col-md-12">
                <h4 class="alert-heading">Warning!</h4>
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled to use this site.
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
                            <h2><i class="glyphicon glyphicon-user"></i> 精选编辑</h2>
                        </div>
                        <div class="box-content"  style="text-align: center;" >
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <td><input id="choiceId" type="text" class="form-control" disabled="disabled" value="${choice.id}"/></td>
                                </tr>
                                <tr>
                                    <th>发布时间</th>
                                    <td>
                                        <input id="publishTime" type="text"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})"
                                               class="form-control"
                                               value="${choice.publishTimeString}"/>
                                    </td>
                                <tr/>
                                <tr>
                                    <th>标题</th>
                                    <td>
                                        <c:if test="${empty choice.title}">
                                            <input id="title" type="text" value="" style="width:100%;"   class="form-control"/>
                                        </c:if>
                                        <c:if test="${!empty choice.title}">
                                            <input id="title" type="text" style="width:100%;"  value="${choice.title}"  class="form-control"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <th>图片</th>
                                    <td>
                                        <input id="img" name="img" type="file" accept=".jpg,.jpeg,.png"
                                               class="form-control"/>
                                        <c:if test="${empty choice.img}">
                                            <input id="image" type="hidden"/>
                                            <img id="imgPriview" style="display: none;width:60%;height:300px;" src="">
                                        </c:if>
                                        <c:if test="${!empty choice.img}">
                                            <input id="image" type="hidden" value="${choice.img}"/>
                                            <img id="imgPriview" style="width:60%;"src="${choice.img}">

                                            <input id="imgDelButton" type="button" class="btn btn-danger btn"
                                                   value="删除"/>
                                        </c:if>

                                        <input type="hidden" value="${choice.width}" id="imgWidth">
                                        <input type="hidden" value="${choice.height}" id="imgHeight">
                                    </td>
                                </tr>
                                <tr>
                                    <th>状态</th>
                                    <td>
                                        <c:if test="${choice.status == 0}">
                                            <input id="status" type="text" class="form-control" disabled="disabled" value="新建"/>
                                        </c:if>
                                        <c:if test="${choice.status == 1}">
                                            <input id="status" type="text" class="form-control" disabled="disabled" value="下线"/>
                                        </c:if>
                                        <c:if test="${choice.status == 2}">
                                            <input id="status" type="text" class="form-control" disabled="disabled" value="上线"/>
                                        </c:if>
                                        <c:if test="${choice.status == 3}">
                                            <input id="status" type="text" class="form-control" disabled="disabled" value="已发布"/>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <th>内容(图片宽度需超过200)</th>
                                    <td>
                                        <!--富文本编辑器-->
                                        <div id="editor-container" class="container">
                                           ${choice.content}
                                        </div>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <button id="updateChoice" type="button" class="btn btn-primary btn-lg" data-dismiss="modal">&nbsp;&nbsp;提交&nbsp;&nbsp;</button>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                var editor = new wangEditor('editor-container');
                //更新Banner
                $('#updateChoice').click(function(event) {
                    // 获取编辑器纯文本内容
                    var onlyText = editor.$txt.text();
                    $('#updateChoice').attr("disabled","disabled");
                    var title = $("#title").val();
                    var content = $("#editor-container").html();
                    var img = $("#imgPriview").attr("src");
                    //去除空格
                    content = $.trim(content);
                    onlyText = $.trim(onlyText);
                    if (title == "") {
                        alert("请填写标题");
                        $('#updateChoice').removeAttr("disabled");
                        return false;
                    }
                    //不排除多个换行内容为空的情况
                    if (onlyText == "") {
                        alert("请编辑内容,填写文字");
                        $('#updateChoice').removeAttr("disabled");
                        return false;
                    }
                    if(img == ""){
                        alert("必须上传图片");
                        $('#addNewBanner').removeAttr("disabled");
                        return false;
                    }
                    var imgWidth = $("#imgWidth").val();
                    if(imgWidth<200){
                        alert("图片宽度必须大于200");
                        return false;
                    }
                    post('choice/update',
                            'id='+$("#choiceId").val()+'&title='+title+'&content='+ encodeURIComponent(content)+ '&image='+img+'&width='+$("#imgWidth").val()+'&height='+$("#imgHeight").val()+'&publishTime='+$("#publishTime").val(),
                            function (data) {
                                if(data['status']) {
                                    location.href = '<%=basePath%>choice/list?status=${status}&pageSize=${pageSize}&pageNumber=${pageNumber}';
                                } else {
                                    alert('更新失败. info:'+data['info']);
                                    $('#updateChoice').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('更新请求失败，请检查网络环境');
                                $('#updateChoice').removeAttr("disabled");
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



//图片上传处理
                $(document).ready(function () {
                    if('${banner.img}' != ''){
                        $("#imgPriview").css('display','block');
                        $("#imgDelButton").css('display','block');
                    }
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
                        maxFileSize: 1024*1024*5,
                        minFileSize: 0,
                        onUploadSuccess: function (data) {
                            var result = eval("(" + data + ")");
                            $("#image").val(data);
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
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>