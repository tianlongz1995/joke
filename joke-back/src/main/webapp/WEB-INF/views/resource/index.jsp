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
        <!-- content start -->
        <div id="content" class="col-lg-10 col-sm-10">
            <div class="row">
                <div class="box col-md-6" style="margin-top: 0;">
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
                                <button onclick="modifyStatus(1)"  type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="box col-md-6" style="margin-top: 0;">
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
                                <button onclick="modifyStatus(3)" type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <div class="modal fade" id="editStatusModal" tabindex="-1" role="dialog" aria-labelledby="editStatusModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">修改渠道状态</h4>
                        </div>
                        <div class="modal-body form-inline">
                            <input id="editType" type="hidden" value=""/>
                            请输入验证码 : <input id="editCode" type="text" class="form-control input-sm" style="width: 150px;" placeholder="5分钟内有效"/>
                            &nbsp;
                            <button id="sendCode" type="button" class="btn btn-default btn-sm" onclick="sendValidationCode()">获取验证码</button>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary btn-sm" onclick="confirmEdit()">修改</button>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript">

                function modifyStatus(type) {
                    $("#sendCode").removeAttr("disabled");
                    $("#editCode").val('');
                    $("#editType").val(type);
                    $('#editStatusModal').modal('show');
                };

                function confirmEdit() {
                    var type = $("#editType").val();
                    if(type == 1){
                        indexAddSubmit();
                    } else if (type == 2){
                        indexBackAddSubmit();
                    } else if (type == 3){
                        indexTestAddSubmit();
                    }
                };

                function sendValidationCode() {
                    $("#sendCode").attr("disabled", "disabled");
                    post('resource/getValidationCode', {},
                            function (data) {
                                if (data.status == 1) {
                                    alert(data.info);
                                } else {
                                    alert('操作失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

                function indexAddSubmit() {
                    indexHide();
                    var code = $("#editCode").val();
                    var libJs = $("#libJs").val();
                    var appJs = $("#appJs").val();
                    var appCss = $("#appCss").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=1&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss +'&code=' + code,
                        function (data) {
                            if (data.status == 1) {
                                alert("更新成功!");
                                $('#editStatusModal').modal('hide');
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
                    var code = $("#editCode").val();
                    var libJs = $("#libJsBack").val();
                    var appJs = $("#appJsBack").val();
                    var appCss = $("#appCssBack").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=2&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss +'&code=' + code,
                            function (data) {
                                if (data.status == 1) {
                                    alert("更新成功!");
                                    $('#editStatusModal').modal('hide');
                                } else {
                                    alert('更新失败:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

//                function indexBackEdit() {
//                    $("#indexBackAdd").show();
//                    $("#indexBackBtnHide").show();
//                    $("#indexBackBtnEdit").hide();
//                    $("#libJsBack").removeAttr('disabled');
//                    $("#appJsBack").removeAttr('disabled');
//                    $("#appCssBack").removeAttr('disabled');
//                };

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
                    var code = $("#editCode").val();
                    var libJs = $("#libJsTest").val();
                    var appJs = $("#appJsTest").val();
                    var appCss = $("#appCssTest").val();

                    if(libJs == null || libJs.length < 1 || appJs == null || appJs.length < 1 || appCss == null || appCss.length < 1){
                        alert("内容长度不符!");
                        return false;
                    }

                    post('resource/updateIndex',
                            'type=3&libJs=' + libJs + '&appJs=' + appJs + '&appCss=' + appCss +'&code=' + code,
                            function (data) {
                                if (data.status == 1) {
                                    alert("更新成功!");
                                    $('#editStatusModal').modal('hide');
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