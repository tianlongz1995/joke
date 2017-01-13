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
    <title>内容源编辑</title>
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
        <noscript>
            <div class="alert alert-block col-md-12">
                <h4 class="alert-heading">Warning!</h4>
                <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a>enabled to use this site.
                </p>
            </div>
        </noscript>

        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div class="row">
                <div class="box col-md-4">
                    <div class="box-inner">
                        <div class="box-header well bg-primary" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 首页资源配置</h2>
                            <div id="indexBtnEdit" style="float: right;display: block;" title="修改" onclick="indexEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="indexBtnHide" style="float: right;display: none;" title="返回" onclick="indexHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>lib.js</th>
                                    <td><input id="libJs" type="text" class="form-control" value="${index.libJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.js</th>
                                    <td><input id="appJs" type="text" class="form-control" value="${index.appJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.css</th>
                                    <td>
                                        <input id="appCss" type="text" class="form-control" value="${index.appCss}" disabled="disabled"/>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <div id="indexAdd" style="text-align: center;display: none;">
                                <button onclick="indexAddSubmit()"  type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->

                <div class="box col-md-4">
                    <div class="box-inner">
                        <div class="box-header well bg-info" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 备用资源配置</h2>
                            <div id="indexBackBtnEdit" style="float: right;display: block;" title="修改" onclick="indexBackEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="indexBackBtnHide" style="float: right;display: none;" title="返回" onclick="indexBackHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>

                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>lib.js</th>
                                    <td><input id="libJsBack" type="text" class="form-control" value="${back.libJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.js</th>
                                    <td><input id="appJsBack" type="text" class="form-control" value="${back.appJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.css</th>
                                    <td>
                                        <input id="appCssBack" type="text" class="form-control" value="${back.appCss}" disabled="disabled"/>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <div id="indexBackAdd" style="text-align: center;display: none;">
                                <button onclick="indexBackAddSubmit()" type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->

                <div class="box col-md-4">
                    <div class="box-inner">
                        <div class="box-header well bg-info" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 测试资源配置</h2>
                            <div id="indexTestBtnEdit" style="float: right;display: block;" title="修改" onclick="indexTestEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="indexTestBtnHide" style="float: right;display: none;" title="返回" onclick="indexTestHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>

                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>lib.js</th>
                                    <td><input id="libJsTest" type="text" class="form-control" value="${test.libJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.js</th>
                                    <td><input id="appJsTest" type="text" class="form-control" value="${test.appJs}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>app.css</th>
                                    <td>
                                        <input id="appCssTest" type="text" class="form-control" value="${test.appCss}" disabled="disabled"/>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <div id="indexTestAdd" style="text-align: center;display: none;">
                                <button onclick="indexTestAddSubmit()" type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                function indexAddSubmit() {
                    indexHide();
                    var libJs = $("#libJs").val();
                    var appJs = $("#appJs").val();
                    var appCss = $("#appCss").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=1&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss,
                        function (data) {
                            if (data.status == 1) {
                                alert("更新成功!");
                            } else {
                                alert('更新失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                };

                function indexEdit() {
                    $("#indexAdd").show();
                    $("#indexBtnHide").show();
                    $("#indexBtnEdit").hide();
                    $("#libJs").removeAttr('disabled');
                    $("#appJs").removeAttr('disabled');
                    $("#appCss").removeAttr('disabled');
                };

                function indexHide() {
                    $("#indexAdd").hide();
                    $("#indexBtnEdit").show();
                    $("#indexBtnHide").hide();
                    $("#libJs").attr('disabled','disabled');
                    $("#appJs").attr('disabled','disabled');
                    $("#appCss").attr('disabled','disabled');
                };
                /** --------------------备份配置-2-------------------- **/
                function indexBackAddSubmit() {
                    indexBackHide();
                    var libJs = $("#libJsBack").val();
                    var appJs = $("#appJsBack").val();
                    var appCss = $("#appCssBack").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=2&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss,
                            function (data) {
                                if (data.status == 1) {
                                    alert("更新成功!");
                                } else {
                                    alert('更新失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

                function indexBackEdit() {
                    $("#indexBackAdd").show();
                    $("#indexBackBtnHide").show();
                    $("#indexBackBtnEdit").hide();
                    $("#libJsBack").removeAttr('disabled');
                    $("#appJsBack").removeAttr('disabled');
                    $("#appCssBack").removeAttr('disabled');
                };

                function indexBackHide() {
                    $("#indexBackAdd").hide();
                    $("#indexBackBtnEdit").show();
                    $("#indexBackBtnHide").hide();
                    $("#libJsBack").attr('disabled','disabled');
                    $("#appJsBack").attr('disabled','disabled');
                    $("#appCssBack").attr('disabled','disabled');
                };

                /** --------------------测试配置--3------------------- **/
                function indexTestAddSubmit() {
                    indexTestHide();
                    var libJs = $("#libJsTest").val();
                    var appJs = $("#appJsTest").val();
                    var appCss = $("#appCssTest").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=3&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss,
                            function (data) {
                                if (data.status == 1) {
                                    alert("更新成功!");
                                } else {
                                    alert('更新失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

                function indexTestEdit() {
                    $("#indexTestAdd").show();
                    $("#indexTestBtnHide").show();
                    $("#indexTestBtnEdit").hide();
                    $("#libJsTest").removeAttr('disabled');
                    $("#appJsTest").removeAttr('disabled');
                    $("#appCssTest").removeAttr('disabled');
                };

                function indexTestHide() {
                    $("#indexTestAdd").hide();
                    $("#indexTestBtnEdit").show();
                    $("#indexTestBtnHide").hide();
                    $("#libJsTest").attr('disabled','disabled');
                    $("#appJsTest").attr('disabled','disabled');
                    $("#appCssTest").attr('disabled','disabled');
                };

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