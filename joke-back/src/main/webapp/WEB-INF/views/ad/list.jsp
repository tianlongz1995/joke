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
    <title>广告管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

    <!-- The fav icon -->
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
            <div>
                <ul class="breadcrumb">
                    <li><a href="ad/list">广告管理</a></li>
                </ul>
            </div>
            <div class="row">
                <div class="col-md-1" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                    <p style="padding: 8px 0px;">投放渠道：</p>
                </div>
                <div class="col-md-11" style="margin-left: 0px;">
                    <div style="padding: 8px 0px;">
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox11" value="option1"> 全部
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox12" value="option2"> 公共
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox13" value="option3"> VIVO
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox14" value="option1"> OPPO
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox15" value="option2"> 金立
                        </label>
                        <label class="checkbox-inline">
                            <input type="checkbox" id="inlineCheckbox16" value="option3"> 魅族
                        </label>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="col-md-1" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                    <p style="padding: 8px 0px;">广告位置：</p>
                </div>
                <div class="col-md-11" style="margin-left: 0px;">
                    <p style="padding: 8px 0px;">页面位置类型</p>
                </div>
            </div>
            <div class="row">
                <div class="col-md-1" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                    <p style="padding: 8px 0px;">广告位置ID：</p>
                </div>
                <div class="col-md-11" style="margin-left: 0px;">
                    <input id="adlinkid1" type="text" style="padding: 8px 12px;" class="form-control" placeholder="输入广告链接ID"/>
                </div>
            </div>
            <div class="row">
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 广告列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info">
                                <a href="#" data-toggle="modal" data-target="#newad">添加新广告</a>

                                <label class="checkbox-inline" style="float: right;">
                                    <input type="checkbox" id="onlyViewOnline" value="option2"> 只显示上线
                                </label>
                                <label class="checkbox-inline" style="float: right;margin-left: 5px;">
                                    <input type="checkbox" id="onlyViewOffline" value="option1"> 只显示下线
                                </label>

                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter">
                                    <label style="padding-right:30px;">
                                        <span>状态</span>
                                        <select id="status">
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >下线
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >上线
                                            </option>
                                        </select>
                                    </label>
                                    <label style="padding-right:30px;">
                                        <a class="btn btn-primary" href="#" id="selectadList">
                                            <span class="glyphicon glyphicon-search icon-white">查询</span>
                                        </a>
                                    </label>
                                </div>

                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>广告位ID</th>
                                    <th>广告位置</th>
                                    <th>广告频率</th>
                                    <th>渠道名称</th>
                                    <th>广告状态</th>
                                    <th>创建时间</th>
                                    <th>更新时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="ad">
                                    <tr>
                                        <td><c:out value="${ad.id}"/></td>
                                        <td><c:out value="${ad.name}"/></td>
                                        <td>
                                            <c:if test="${ad.status == 0}">
                                                下线
                                            </c:if>
                                            <c:if test="${ad.status == 1}">
                                                上线
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:if test="${ad.status == 0}">
                                                <a class="btn btn-success" href="#" onclick="modifyStatus(1,${ad.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>上线
                                                </a>
                                            </c:if>
                                            <c:if test="${ad.status == 1}">
                                                <a class="btn btn-danger" href="#" onclick="modifyStatus(0,${ad.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                            </c:if>
                                            <a class="btn btn-info" href="ad/edit?id=${ad.id}">
                                                <i class="glyphicon glyphicon-edit icon-white"></i>编辑
                                            </a>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${ad.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${ad.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
                <!-- box col-md-12 end -->
            </div>
            <!-- row end -->

            <div class="modal fade bs-example-modal-lg" id="newad" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新建广告</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>投放渠道</th>
                                    <td>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox1" value="option1"> 全部
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox2" value="option2"> 公共
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox3" value="option3"> VIVO
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox4" value="option1"> OPPO
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox5" value="option2"> 金立
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox" id="inlineCheckbox6" value="option3"> 魅族
                                        </label>

                                    </td>
                                </tr>

                                <tr>
                                    <th>广告链接ID</th>
                                    <td><input id="adLinkId" type="text" class="form-control" placeholder="输入广告链接ID"/></td>
                                </tr>
                                <tr>
                                    <th>投放页面</th>
                                    <td>
                                        <select id="advertisingPage" class="form-control">
                                            <option value="0">频道首页</option>
                                            <option value="1">中间页</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>投放方式</th>
                                    <td>
                                        <select id="advertisingPos" class="form-control">
                                            <option value="0">页尾浮动</option>
                                            <option value="1">Banner</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>位置ID</th>
                                    <td><input id="posId" type="text" class="form-control" />DPH18976471</td>
                                </tr>
                                <tr>
                                    <th>投放频率</th>
                                    <td><input id="sloied" type="text" class="form-control" />（内容）+（广告）</td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;vertical-align: middle;">
                            <button id="addNewad" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div>
            </div>

            <script type="text/javascript">
                $('#addNewad').click(function (event) {
                    post('ad/add',
                            'name=' + $("#addname").val() + '&status=' + $('#addstatus').val(),
                            function (data) {
                                if (data['status']) {
                                    location.href = '<%=basePath%>ad/list?status=' + $("#status").val();
                                } else {
                                    alert('添加失败. info:' + data['info']);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                });

                function modifyStatus(status, id) {
                    post('ad/modifyStatus',
                            'id=' + id + '&status=' + status,
                            function (data) {
                                if (data['status']) {
                                    location.href = '<%=basePath%>ad/list?status=' + $("#status").val();
                                }
                                else {
                                    alert('操作失败. info:' + data['info']);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                }

                $('#selectadList').click(function (event) {
                    location.href = '<%=basePath%>ad/list?status=' + $("#status").val();
                });

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
            </script>

        </div>
        <!-- content end -->
    </div>
    <!-- row end -->
</div>
<!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
