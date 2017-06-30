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
    <title>发布规则</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
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
                        <div class="box-header well bg-primary" data-original-title="">
                            <h2><i class="glyphicon glyphicon-cog"></i> 缓存管理</h2>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <tr >
                                    <th>
                                        精选切图：
                                    </th>
                                    <td>
                                        <input id="choiceCrop" type="text" class="input-sm" value=""/>
                                        <a id="choiceCropCode" onclick="getValidationCode(1)" type="button" class="btn btn-default btn-sm">获取验证码</a>
                                        <a id="c" onclick="choiceCrop()" type="button" class="btn btn-success btn-sm">精选切图</a>
                                    </td>
                                </tr>
                                <tr >
                                    <th>
                                        段子头像补全：
                                    </th>
                                    <td>
                                        <input id="jokeAvatar" type="text" class="input-sm" value=""/>
                                        <a id="jokeAvatarCode" onclick="getValidationCode(2)" type="button" class="btn btn-default btn-sm">获取验证码</a>
                                        <a id="a" onclick="jokeAvatar()" type="button" class="btn btn-success btn-sm">段子头像补全</a>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        清除全部joke缓存：
                                    </th>
                                    <td>
                                        <input id="cacheAvatar" type="text" class="input-sm" value=""/>
                                        <a id="jokeCacheCode" onclick="getValidationCode(3)" type="button"
                                           class="btn btn-default btn-sm">获取验证码</a>
                                        <a id="cleanCache" onclick="delJokeAndCommentCache()" type="button"
                                           class="btn btn-success btn-sm">清除joke缓存</a>
                                    </td>
                                </tr>

                            </table>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->

                <div id="cachediv" style="margin: 50px auto;">
                    <p>正在处理...</p>
                </div>


                <script type="text/javascript">

                    $(function () {
                        $("#cachediv").hide();
                    });
                    /**
                     * 获取验证码
                     */
                    function getValidationCode(type) {
                        if(type == 1){
                            $("#choiceCropCode").attr('disabled', 'disabled');
                        } else if (type == 2) {
                            $("#jokeAvatarCode").attr('disabled', 'disabled');
                        } else {
                            $("#jokeCacheCode").attr('disabled', 'disabled');
                        }
                        post('admin/getValidationCode', 'type=' + type,
                            function (data) {
                                if (data.status == 1) {
                                    alert("验证码已发送到邮箱, 请查收!");
                                } else {
                                    alert('更新失败:' + data.info);
                                    if (type == 1) {
                                        $("#choiceCropCode").removeAttr('disabled');
                                    } else if (type == 2) {
                                        $("#jokeAvatarCode").removeAttr('disabled');
                                    } else {
                                        $("#jokeCacheCode").removeAttr('disabled');
                                    }
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                if (type == 1) {
                                    $("#choiceCropCode").removeAttr('disabled');
                                } else if (type == 2) {
                                    $("#jokeAvatarCode").removeAttr('disabled');
                                } else {
                                    $("#jokeCacheCode").removeAttr('disabled');
                                }
                            });
                    }
                    ;

                    /** --------------------精选切图-------------------- **/
                    function choiceCrop() {
                        $("#c").attr('disabled', 'disabled');
                        var code = $("#choiceCrop").val();
                        if (code == null || code == '') {
                            alert("请输入验证码!");
                            return false;
                        }
                        post('admin/choiceCrop', 'code=' + code,
                                function (data) {
                                    if (data.status == 1) {
                                        alert("处理完成!");
                                        $("#choiceCrop").val('');
                                    } else {
                                        alert('更新失败:' + data.info);
                                    }
                                    $("#c").removeAttr('disabled');
                                    $("#choiceCropCode").removeAttr('disabled');

                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                    $("#c").removeAttr('disabled');
                                    $("#choiceCropCode").removeAttr('disabled');
                                });
                    }
                    ;

                    /** --------------------删除joke及其comment缓存-------------------- **/
                    function delJokeAndCommentCache() {

                        $("#cleanCache").attr('disabled', 'disabled');
                        var code = $("#cacheAvatar").val();
                        if (code == null || code == '') {
                            alert("请输入验证码!");
                            return false;
                        }

                        $("#cachediv").show();
                        post('admin/delJokeAndCommentCache', 'code=' + code,
                            function (data) {
                                if (data.status == 1) {
                                    alert("处理完成!");
                                    $("#cacheAvatar").val('');
                                } else {
                                    alert('更新失败:' + data.info);
                                }
                                $("#cachediv").hide();
                                $("#cleanCache").removeAttr('disabled');
                                $("#jokeCacheCode").removeAttr('disabled');
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $("#cachediv").hide();
                                $("#cleanCache").removeAttr('disabled');
                                $("#jokeCacheCode").removeAttr('disabled');
                            });
                    }
                    ;

                    /** --------------------段子头像补全-------------------- **/
                    function jokeAvatar() {
                        $("#a").attr('disabled', 'disabled');
                        var code = $("#jokeAvatar").val();
                        if (code == null || code == '') {
                            alert("请输入验证码!");
                            return false;
                        }
                        post('admin/jokeAvatar', 'code=' + code,
                                function (data) {
                                    if (data.status == 1) {
                                        alert("处理完成!");
                                        $("#jokeAvatar").val('');
                                    } else {
                                        alert('更新失败:' + data.info);
                                    }
                                    $("#a").removeAttr('disabled');
                                    $("#jokeAvatarCode").removeAttr('disabled');
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                    $("#a").removeAttr('disabled');
                                    $("#jokeAvatarCode").removeAttr('disabled');
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
                </script>

            </div><!-- content end -->
        </div><!-- row end -->
    </div><!-- ch-container end -->

    <hr>
    <%@ include file="../common/footer.html" %>
    <%@ include file="../common/js.html" %>
</body>
</html>