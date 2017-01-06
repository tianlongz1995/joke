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
    <title>渠道管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="ui/js/jquery.form.js"></script>
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
                <div class="box col-md-12">

                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><a href="#" data-toggle="modal" data-target="#dataManagerModal"><i class="glyphicon glyphicon-user"></i></a> 渠道列表</h2>

                            <button onclick="addShow()" style="float: right;" class="btn btn-primary btn-xs"><i class="glyphicon glyphicon-plus"></i> 新增渠道</button>
                        </div>
                        <div class="box-content">
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row">
                                    <div class="col-md-12">
                                        <form class="form-inline">
                                            <div class="form-group" style="display: inline-block;">
                                                <label>
                                                    <label for="status" style="display: inline-block;">状态：</label>
                                                </label>
                                            </div>
                                            <div class="form-group" style="display: inline-block;">
                                                <select id="status" class="form-control input-sm" onchange="queryList()">
                                                    <option value="0"
                                                            <c:if test="${!empty status && status == 0}">selected</c:if> >下线
                                                    </option>
                                                    <option value="1"
                                                            <c:if test="${!empty status && status == 1}">selected</c:if> >上线
                                                    </option>
                                                </select>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>渠道名称</th>
                                    <th>渠道状态</th>
                                    <th>创建时间</th>
                                    <th>更新时间</th>
                                    <th>更新人</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="distributors">
                                    <tr>
                                        <td><c:out value="${distributors.id}"/></td>
                                        <td><c:out value="${distributors.name}"/></td>
                                        <td>
                                            <c:if test="${distributors.status == 0}">
                                                下线
                                            </c:if>
                                            <c:if test="${distributors.status == 1}">
                                                上线
                                            </c:if>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${distributors.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${distributors.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <c:out value="${distributors.updateBy}"/>
                                        </td>
                                        <td>
                                            <c:if test="${distributors.status == 0}">
                                                <a class="btn btn-success btn-xs" href="#" onclick="modifyStatus(1,${distributors.id})">
                                                    <i class="glyphicon glyphicon-cloud-upload icon-white"></i>上线
                                                </a>
                                            </c:if>
                                            <c:if test="${distributors.status == 1}">
                                                <a class="btn btn-warning btn-xs" href="#" onclick="modifyStatus(0,${distributors.id})">
                                                    <i class="glyphicon glyphicon-cloud-download icon-white"></i>下线
                                                </a>
                                            </c:if>
                                            <a class="btn btn-info btn-xs" href="distributors/editPage?id=${distributors.id}">
                                                <i class="glyphicon glyphicon-edit icon-white"></i>编辑
                                            </a>
                                            <a class="btn btn-danger btn-xs" href="#" onclick="deleteStatus(${distributors.id})">
                                                <i class="glyphicon glyphicon-trash icon-white"></i>删除
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

            <div class="modal fade bs-example-modal-lg" id="newDistributor" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新建渠道</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>名称</th>
                                    <td><input id="addName" name="addName" type="text" class="form-control" placeholder="渠道名称"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    </td>
                                </tr>
                                <tr>
                                    <th>状态</th>
                                    <td>
                                        <select id="addStatus" name="addStatus" class="form-control">
                                            <option value="1">上线</option>
                                            <option value="0">下线</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>频道</th>
                                    <td colspan="1">
                                        <table>
                                            <thead>
                                            <tr>
                                                <th>选中</th>
                                                <th>频道名称</th>
                                                <th>移动</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${channels}" var="channel">
                                                <tr>
                                                    <th>
                                                        <input type="checkbox" name="channelIds" value="${channel.id}"  >
                                                    </th>
                                                    <th>
                                                        <c:out value="${channel.name}"/>
                                                    </th>
                                                    <th>
                                                        <button class="btn btn-info btn-xs" onclick="up(this)"><i class="glyphicon glyphicon-arrow-up icon-white"></i> 上移</button>
                                                        <button class="btn btn-info btn-xs" onclick="down(this)"><i class="glyphicon glyphicon-arrow-down icon-white"></i> 下移
                                                        </button>
                                                    </th>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <th style="vertical-align: middle;">广告</th>
                                    <td>
                                        <table>
                                            <tr>
                                                <th>状态</th>
                                                <th>广告位置</th>
                                                <th>广告编号</th>
                                                <th>投放频率 n（内容）+ 1（广告）</th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" checked="checked" ></th>
                                                <th>列表页中间</th>
                                                <th><input id="listCenter" type="text" name="adsValue" class="form-control input-sm" value="0"/></th>
                                                <th><input id="interval" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads"  checked="checked" ></th>
                                                <th>列表页底部</th>
                                                <th colspan="2"><input id="listBottom" name="adsValue" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" checked="checked" ></th>
                                                <th>详情页上方</th>
                                                <th colspan="2"><input id="detailsTop" name="adsValue" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" checked="checked" ></th>
                                                <th>详情页中部</th>
                                                <th colspan="2"><input id="detailsCentor" name="adsValue" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" checked="checked" ></th>
                                                <th>详情页底部</th>
                                                <th colspan="2"><input id="detailsBottom" name="adsValue" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" checked="checked" ></th>
                                                <th>详情页插屏</th>
                                                <th colspan="2"><input id="detailsInterstitial" name="adsValue" type="text" class="form-control input-sm" value="0"/></th>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default" data-dismiss="modal" >取消</button>
                            &nbsp;&nbsp;
                            <button id="add" type="button" class="btn btn-primary" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="editStatusModal" tabindex="-1" role="dialog" aria-labelledby="editStatusModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">修改渠道状态</h4>
                        </div>
                        <div class="modal-body form-inline">
                            <input id="editDid" type="hidden" class="form-control" value=""/>
                            <input id="editStatus" type="hidden" class="form-control" value=""/>
                            请输入验证码 : <input id="editCode" type="text" class="form-control input-sm" style="width: 150px;" placeholder="5分钟内有效"/>
                            &nbsp;
                            <button id="sendCode" type="button" class="btn btn-default btn-sm" onclick="sendValidationCode()">获取验证码</button>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary btn-sm" onclick="editStatus()">修改</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="delStatusModal" tabindex="-1" role="dialog" aria-labelledby="delStatusModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">删除渠道</h4>
                        </div>
                        <div class="modal-body form-inline">
                            <input id="delDid" type="hidden" class="form-control" value=""/>
                            请输入验证码 : <input id="delCode" type="text" class="form-control input-sm" style="width: 150px;" placeholder="5分钟内有效"/>
                            &nbsp;
                            <button id="delSendCode" type="button" class="btn btn-default btn-sm" onclick="sendValidationCode()">获取验证码</button>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                            <button type="button" class="btn btn-primary btn-sm" onclick="delStatus()">修改</button>
                        </div>
                    </div>
                </div>
            </div>


            <script type="text/javascript">
                $('#add').click(function (event) {
                    $('#add').attr("disabled", "disabled");
                    var contentType = [];
                    $('input[name="channelIds"]:checked').each(function () {
                        contentType.push($(this).val());
                    });
                    if (contentType.length == 0) {
                        alert("未选中任何频道");
                        $('#add').removeAttr("disabled");
                        return false;
                    }
                    var name = $("#addName").val();
                    var status = $('#addStatus').val();
                    if(name == null || name.length < 1 || status == null || status.length < 1){
                        alert("名称、状态不能为空!");
                        $('#add').removeAttr("disabled");
                        return false;
                    }
                    var id = document.getElementsByName('ads');
                    var adsValue = document.getElementsByName('adsValue');
                    var ads = '';
                    var index = 0;
                    for (var i = 0; i < id.length; i++) {
                        if (id[i].checked){
                            index++;
                            if(adsValue[i].value == null || adsValue[i].value.length < 1){
                                alert("选中的广告编号不能为空!");
                                $('#add').removeAttr("disabled");
                                return false;
                            }
                            if(i == 0){
                                var interval = $("#interval").val();
                                if(interval == null || interval.length < 1){
                                    alert("列表页中间广告间隔必须设置!");
                                    $('#add').removeAttr("disabled");
                                    return false;
                                }
                                ads = '&s=' + interval;
                            }
                            if(i == 0){
                                ads = ads + '&lc=' + adsValue[i].value;
                            } else if(i == 1){
                                ads = ads + '&lb=' + adsValue[i].value;
                            } else if(i == 2){
                                ads = ads + '&dt=' + adsValue[i].value;
                            } else if(i == 3){
                                ads = ads + '&dc=' + adsValue[i].value;
                            } else if(i == 4){
                                ads = ads + '&db=' + adsValue[i].value;
                            } else if(i == 5){
                                ads = ads + '&di=' + adsValue[i].value;
                            }
                        }
                    }
                    if(index < 1){
                        alert("必须设置广告默认值!");
                        $('#add').removeAttr("disabled");
                        return false;
                    }
                    post('distributors/add',
                            'name=' + name + '&status=' + status + '&channelIds=' + contentType.toString() + ads,
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val();
                                } else {
                                    $('#add').removeAttr("disabled");
                                    alert('添加频道失败:' + data.info);
                                }
                            },
                            function () {
                                $('#add').removeAttr("disabled");
                                alert('请求失败，请检查网络环境');
                            });
                });

                function addShow() {
                    $('#newDistributor').modal('show');
                };

                function modifyStatus(status, id) {
                    $("#sendCode").removeAttr("disabled");
                    $("#editCode").val('');
                    $('#editDid').val(id);
                    $('#editStatus').val(status);
                    $('#editStatusModal').modal('show');
                };

                function deleteStatus(id) {
                    $("#delSendCode").removeAttr("disabled");
                    $("#delCode").val('');
                    $('#delDid').val(id);
                    $('#delStatusModal').modal('show');
                };

                function sendValidationCode() {
                    $("#sendCode").attr("disabled", "disabled");
                    $("#delSendCode").attr("disabled", "disabled");
                    post('distributors/getValidationCode', {},
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
                }

                function editStatus() {
                    var code = $("#editCode").val();
                    var did = $("#editDid").val();
                    var status = $("#editStatus").val();

                    if(code == null || code.length < 4 || did == null || did.length < 1 || status == null || status.length < 1){
                        alert("参数格式异常!");
                        return false;
                    }
                    post('distributors/editStatus',
                            'did=' + did + '&status=' + status + '&code=' + code,
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val()
                                            + '&limit=' + $("#pageSize").val() + '&pageNo=' + $("#pageNumber").val();
                                } else {
//                                    $('#editStatusModal').modal('hide');
                                    alert('操作失败:' + data['info']);
                                    return false;
                                }
                            },
                            function () {
//                                $('#editStatusModal').modal('hide');
                                alert('请求失败，请检查网络环境');
                                return false;
                            });
                };

                function delStatus() {
                    var code = $("#delCode").val();
                    var did = $("#delDid").val();

                    if(code == null || code.length < 4 || did == null || did.length < 1){
                        alert("参数格式异常!");
                        return false;
                    }
                    post('distributors/del',
                            'did=' + did + '&code=' + code,
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val()
                                            + '&limit=' + $("#pageSize").val() + '&pageNo=' + $("#pageNumber").val();
                                } else {
//                                    $('#editStatusModal').modal('hide');
                                    alert('操作失败:' + data['info']);
                                    return false;
                                }
                            },
                            function () {
//                                $('#editStatusModal').modal('hide');
                                alert('请求失败，请检查网络环境');
                                return false;
                            });
                };

                $('#selectDistributorList').click(function (event) {
                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val();
                });

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                function up(obj) {
                    var objParentTR = $(obj).parent().parent();
                    var prevTR = objParentTR.prev();
                    if (prevTR.length > 0) {
                        prevTR.insertAfter(objParentTR);
                    }
                }
                ;
                function down(obj) {
                    var objParentTR = $(obj).parent().parent();
                    var nextTR = objParentTR.next();
                    if (nextTR.length > 0) {
                        nextTR.insertBefore(objParentTR);
                    }
                };

                function queryList() {
                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val();
                };

                function turnPage() {
                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val()
                            + '&limit=' + $("#pageSize").val() + '&pageNo=' + $("#pageNumber").val();
                };

                $('#dataManager').click(function (event) {
                    var managerId = $("#managerId").val();
                    if (managerId == null && managerId.length() < 6) {
                        alert("密码错误！");
                        return false;
                    }
                    location.href = '<%=basePath%>distributors/dataManager?managerKey=' + managerId;
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
