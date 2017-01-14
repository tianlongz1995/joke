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
                            <h2><i class="glyphicon glyphicon-cog"></i> 纯文发布规则配置</h2>
                            <div id="textBtnEdit" style="float: right;display: block;" title="修改" onclick="textEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="textBtnHide" style="float: right;display: none;" title="返回" onclick="textHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>发布时间：</th>
                                    <td><input id="jTime" type="text" class="form-control" value="${trole}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>发布数量:</th>
                                    <td><input id="jNum" type="number" class="form-control" value="${textNum}" disabled="disabled"/></td>
                                </tr>
                                </thead>
                            </table>
                            <div id="textAdd" style="text-align: center;display: none;">
                                <button onclick="modifyStatus(1)"  type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->

                <div class="box col-md-4">
                    <div class="box-inner">
                        <div class="box-header well bg-info" data-original-title="">
                            <h2><i class="glyphicon glyphicon-cog"></i> 趣图发布规则配置</h2>
                            <div id="qutuBtnEdit" style="float: right;display: block;" title="修改" onclick="qutuEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="qutuBtnHide" style="float: right;display: none;" title="返回" onclick="qutuHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>

                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>发布时间:</th>
                                    <td><input id="qTime" type="text" class="form-control" value="${qrole}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>静图发布数量:</th>
                                    <td><input id="qImageNum" type="number" class="form-control" value="${qImageNum}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>动图发布数量:</th>
                                    <td>
                                        <input id="qGiftNum" type="number" class="form-control" value="${qGiftNum}" disabled="disabled"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>静图发布权重:</th>
                                    <td><input id="qImageWeight" type="number" class="form-control" value="${qImageWeight}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>动图发布数量:</th>
                                    <td>
                                        <input id="qGiftWeight" type="number" class="form-control" value="${qGiftWeight}" disabled="disabled"/>
                                    </td>
                                </tr>
                                </thead>
                            </table>
                            <div id="qutuAdd" style="text-align: center;display: none;">
                                <button onclick="modifyStatus(2)" type="button" class="btn btn-info" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->

                <div class="box col-md-4">
                    <div class="box-inner">
                        <div class="box-header well bg-info" data-original-title="">
                            <h2><i class="glyphicon glyphicon-cog"></i> 推荐发布规则配置</h2>
                            <div id="recommendBtnEdit" style="float: right;display: block;" title="修改" onclick="recommendEdit()"><i class="glyphicon glyphicon-edit text-primary"></i></div>
                            <div id="recommendBtnHide" style="float: right;display: none;" title="返回" onclick="recommendHide()"><i class="glyphicon glyphicon-log-out text-primary"></i></div>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th>发布时间：</th>
                                    <td><input id="tTime" type="text" class="form-control" value="${rrole}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>纯文发布数量:</th>
                                    <td><input id="tTextNum" type="number" class="form-control" value="${rTextNum}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>静图发布数量:</th>
                                    <td>
                                        <input id="tImageNum" type="number" class="form-control" value="${rImageNum}" disabled="disabled"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>动图发布数量:</th>
                                    <td>
                                        <input id="tGiftNum" type="number" class="form-control" value="${rGiftNum}" disabled="disabled"/>
                                    </td>
                                </tr>

                                <tr>
                                    <th>纯文发布权重:</th>
                                    <td><input id="tTextWeight" type="number" class="form-control" value="${rTextWeight}" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>静图发布权重:</th>
                                    <td>
                                        <input id="tImageWeight" type="number" class="form-control" value="${rImageWeight}" disabled="disabled"/>
                                    </td>
                                </tr>
                                <tr>
                                    <th>动图发布权重:</th>
                                    <td>
                                        <input id="tGiftWeight" type="number" class="form-control" value="${rGiftWeight}" disabled="disabled"/>
                                    </td>
                                </tr>

                                </thead>
                            </table>
                            <div id="recommendAdd" style="text-align: center;display: none;">
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
                        addTextPublishRole();
                    } else if (type == 2){
                        addQuTuPublishRole();
                    } else if (type == 3){
                        addRecommendPublishRole();
                    }
                };
                
                /** --------------------纯文发布规则配置-------------------- **/
                function addTextPublishRole() {
                    var code = $("#editCode").val();
                    textHide();
                    var jTime = $("#jTime").val();
                    var jNum = $("#jNum").val();


                    if( jTime == ""){
                        alert("纯文发布时间不能为空");
                        return false;
                    }
                    if(jNum == "" || jNum < 0){
                        alert("纯文发布数量不能为空,或者小于0");
                        return false;
                    }

                    post('joke/addPublishRole',
                            'type=1&role=' + jTime + '&textNum=' + jNum +'&code=' + code,
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

                //纯文修改
                function textEdit() {
                    $("#textAdd").show();
                    $("#textBtnHide").show();
                    $("#textBtnEdit").hide();
                    $("#jTime").removeAttr('disabled');
                    $("#jNum").removeAttr('disabled');
                };
                //纯文隐藏
                function textHide() {
                    $("#textAdd").hide();
                    $("#textBtnEdit").show();
                    $("#textBtnHide").hide();
                    $("#jTime").attr('disabled','disabled');
                    $("#jNum").attr('disabled','disabled');
                };
                /** --------------------趣图发布规则配置-------------------- **/
                function addQuTuPublishRole() {
                    qutuHide();
                    var code = $("#editCode").val();
                    var qTime = $("#qTime").val();
                    var qImageNum = $("#qImageNum").val();
                    var qGiftNum = $("#qGiftNum").val();
                    var qImageWeight = $("#qImageWeight").val();
                    var qGiftWeight = $("#qGiftWeight").val();

                    if (qTime == "") {
                        alert("趣图发布时间不能为空");
                        return false;
                    }
                    if (qImageNum == "" || qImageNum < 0) {
                        alert("静图发布数量不能为空,或者小于0");
                        return false;
                    }
                    if (qGiftNum == "" || qGiftNum < 0) {
                        alert("动图发布数量不能为空,或者小于0");
                        return false;
                    }
                    if(qImageWeight == "" || qImageNum < 0){
                        alert("静图发布权重不能为空,或者小于0");
                        return false;
                    }
                    if(qGiftWeight == "" || qGiftWeight < 0){
                        alert("动图发布权重不能为空,或者小于0");
                        return false;
                    }

                    post('joke/addPublishRole',
                            'type=2&role=' + qTime + '&imageNum=' + qImageNum + '&giftNum=' + qGiftNum +'&imageWeight=' +qImageWeight +'&giftWeight='+ qGiftWeight +'&code=' + code,
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

                function sendValidationCode() {
                    $("#sendCode").attr("disabled", "disabled");
                    post('joke/getValidationCode', {},
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

                function qutuEdit() {
                    $("#qutuAdd").show();
                    $("#qutuBtnHide").show();
                    $("#qutuBtnEdit").hide();

                    $("#qTime").removeAttr('disabled');
                    $("#qImageNum").removeAttr('disabled');
                    $("#qGiftNum").removeAttr('disabled');
                    $("#qGiftWeight").removeAttr('disabled');
                    $("#qImageWeight").removeAttr('disabled');
                };

                function qutuHide() {
                    $("#qutuAdd").hide();
                    $("#qutuBtnEdit").show();
                    $("#qutuBtnHide").hide();

                    $("#qTime").attr('disabled','disabled');
                    $("#qImageNum").attr('disabled','disabled');
                    $("#qImageWeight").attr('disabled','disabled');
                    $("#qGiftNum").attr('disabled','disabled');
                    $("#qGiftWeight").attr('disabled','disabled');
                };

                /** --------------------推荐发布规则------------------ **/
                function addRecommendPublishRole() {
                    recommendHide();
                    var code = $("#editCode").val();
                    var tTime = $("#tTime").val();
                    var tTextNum = $("#tTextNum").val();
                    var tImageNum = $("#tImageNum").val();
                    var tGiftNum = $("#tGiftNum").val();

                    var tTextWeight = $("#tTextWeight").val();
                    var tImageWeight = $("#tImageWeight").val();
                    var tGiftWeight = $("#tGiftWeight").val();

                    if(tTime == ""){
                        alert("推荐发布时间不能为空");
                        return false;
                    }
                    if(tTextNum =="" || tTextNum < 0){
                        alert("纯文发布数量不能为空,或者小于0");
                        return false;
                    }
                    if(tImageNum =="" || tImageNum < 0){
                        alert("静图发布数量不能为空,或者小于0");
                        return false;
                    }
                    if(tGiftNum =="" || tGiftNum < 0){
                        alert("动图发布数量不能为空,或者小于0");
                        return false;
                    }
                    if(tTextWeight == "" || tTextWeight < 0){
                        alert("纯文发布权重不能为空，或者小于0");
                        return false;
                    }
                    if(tImageWeight == "" || tImageWeight < 0){
                        alert("静图发布权重不能为空，或者小于0");
                        return false;
                    }
                    if(tGiftNum == "" || tGiftWeight < 0){
                        alert("动图发布权重不能为空，或者小于0")
                        return false;
                    }

                    post('joke/addPublishRole',
                            'type=3&role=' + tTime + '&textNum=' + tTextNum + '&imageNum=' + tImageNum +'&giftNum=' +tGiftNum + "&imageWeight="+tImageWeight +'&textWeight='+tTextWeight+'&giftWeight='+tGiftWeight +'&code=' + code,
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

                function recommendEdit() {
                    $("#recommendAdd").show();
                    $("#recommendBtnHide").show();
                    $("#recommendBtnEdit").hide();

                    $("#tTime").removeAttr('disabled');
                    $("#tTextNum").removeAttr('disabled');
                    $("#tTextWeight").removeAttr('disabled');
                    $("#tImageNum").removeAttr('disabled');
                    $("#tImageWeight").removeAttr('disabled');
                    $("#tGiftNum").removeAttr('disabled');
                    $("#tGiftWeight").removeAttr('disabled');
                };

                function recommendHide() {
                    $("#recommendAdd").hide();
                    $("#recommendBtnEdit").show();
                    $("#recommendBtnHide").hide();

                    $("#tTime").attr('disabled','disabled');
                    $("#tTextNum").attr('disabled','disabled');
                    $("#tTextWeight").attr('disabled','disabled');
                    $("#tImageNum").attr('disabled','disabled');
                    $("#tImageWeight").attr('disabled','disabled');
                    $("#tGiftNum").attr('disabled','disabled');
                    $("#tGiftWeight").attr('disabled','disabled');
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