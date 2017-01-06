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
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>上线
                                                </a>
                                            </c:if>
                                            <c:if test="${distributors.status == 1}">
                                                <a class="btn btn-danger btn-xs" href="#" onclick="modifyStatus(0,${distributors.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                            </c:if>
                                            <a class="btn btn-info btn-xs" href="distributors/editPage?id=${distributors.id}">
                                                <i class="glyphicon glyphicon-edit icon-white"></i>编辑
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
                                                <tr>
                                                    <th><input type="checkbox" name="channelIds" value="1"></th>
                                                    <th>趣图</th>
                                                    <th>
                                                        <button class="btn btn-primary btn-xs" onclick="up(this)"><i class="glyphicon glyphicon-arrow-up icon-white"></i> 上移
                                                        </button>
                                                        <button class="btn btn-primary btn-xs" onclick="down(this)"><i class="glyphicon glyphicon-arrow-down icon-white"></i> 下移
                                                        </button>
                                                    </th>
                                                </tr>
                                                <tr>
                                                    <th><input type="checkbox" name="channelIds" value="2"></th>
                                                    <th>段子</th>
                                                    <th>
                                                        <button class="btn btn-primary btn-sm" onclick="up(this)"><i class="glyphicon glyphicon-arrow-up icon-white"></i> 上移
                                                        </button>
                                                        <button class="btn btn-primary btn-sm" onclick="down(this)"><i class="glyphicon glyphicon-arrow-down icon-white"></i> 下移
                                                        </button>
                                                    </th>
                                                </tr>
                                                <tr>
                                                    <th><input type="checkbox" name="channelIds" value="3"></th>
                                                    <th>推荐</th>
                                                    <th>
                                                        <button class="btn btn-primary btn-sm" onclick="up(this)"><i class="glyphicon glyphicon-arrow-up icon-white"></i> 上移
                                                        </button>
                                                        <button class="btn btn-primary btn-sm" onclick="down(this)"><i class="glyphicon glyphicon-arrow-down icon-white"></i> 下移
                                                        </button>
                                                    </th>
                                                </tr>
                                                <tr>
                                                    <th><input type="checkbox" name="channelIds" value="4"></th>
                                                    <th>精选</th>
                                                    <th>
                                                        <button class="btn btn-primary btn-sm" onclick="up(this)"><i class="glyphicon glyphicon-arrow-up icon-white"></i> 上移
                                                        </button>
                                                        <button class="btn btn-primary btn-sm" onclick="down(this)"><i class="glyphicon glyphicon-arrow-down icon-white"></i> 下移
                                                        </button>
                                                    </th>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="add" type="button" class="btn btn-success" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div>
            </div>


            <div class="modal fade bs-example-modal-sm" id="dataManagerModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">进入数据管理界面</h4>
                        </div>
                        <div class="modal-body">
                            请输入密码:<input id="managerId" type="text" class="form-control" placeholder="数据管理中心密码"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary" id="dataManager">GO!</button>
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
                    post('distributors/add',
                            'name=' + name + '&status=' + status + '&channelIds=' + contentType.toString(),
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
                    post('distributors/edit',
                            'id=' + id + '&status=' + status,
                            function (data) {
                                if (data['status']) {
                                    location.href = '<%=basePath%>distributors/list?status=' + $("#status").val();
                                } else {
                                    alert('操作失败. info:' + data['info']);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                }
                ;

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
                            + '&pageSize=' + $("#pageSize").val() + '&pageNumber=' + $("#pageNumber").val();
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
