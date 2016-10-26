<%--
  Created by IntelliJ IDEA.
  User: hushuang
  Date: 16/9/7
  Time: 上午10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>频道管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html"%>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>

    <!-- The fav icon -->
    <link rel="shortcut icon" href="ui/charisma/img/favicon.ico">
</head>

<body>
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
                    <li><a href="channel/list">频道管理</a></li>
                </ul>
            </div>

            <div class="row">
                <div class="box col-md-12">

                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 推荐频道权重列表</h2></div>
                        <div class="box-content">
                            <div class="alert alert-info">
                                需要添加新的权重点击:
                                <a href="#" data-toggle="modal" data-target="#newItem">新增权重</a>
                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>权重</th>
                                    <th>推荐频道发布数量</th>
                                    <th>父级编码</th>
                                    <th>描述</th>
                                    <th>更新时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="dictionary">
                                    <tr>
                                        <td><c:out value="${dictionary.seq}"/> </td>
                                        <td><c:out value="${dictionary.code}"/></td>
                                        <td><c:out value="${dictionary.value}"/></td>
                                        <td><c:out value="${dictionary.parentCode}"/></td>
                                        <td><c:out value="${dictionary.describe}"/></td>
                                        <td>
                                            <fmt:formatDate value="${dictionary.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <a class="btn btn-info btn-sm" onclick="weightEidt('${dictionary.id}')" >
                                                <i class="glyphicon glyphicon-edit icon-white"></i> 编辑
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                            <!--  分页  -->
                            <div class="row">
                                <div class="col-md-12 center-block">
                                    <div class="dataTables_paginate paging_bootstrap pagination">
                                        <jsp:include page="../common/page.jsp" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <!-- 新增弹出框 -->
            <div class="modal fade" id="newItem" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="myModalLabel">新建频道权重</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>序号</th>
                                    <td><input id="seq" type="text" class="form-control" placeholder="序号" /></td>
                                </tr>
                                <tr>
                                    <th>父级编码</th>
                                    <td><input id="parentCode" type="text" class="form-control" placeholder="父级编码" disabled="disabled" value="10001"/></td>
                                </tr>
                                <tr>
                                    <th>权重</th>
                                    <td><input id="code" type="text" class="form-control" placeholder="权重" /></td>
                                </tr>
                                <tr>
                                    <th>发布数量</th>
                                    <td><input id="value" type="text" class="form-control" placeholder="发布数量" /></td>
                                </tr>
                                <tr>
                                    <th>描述</th>
                                    <td><input id="describe" type="text" class="form-control" placeholder="描述" /></td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="addNewItem" type="button" class="btn btn-primary btn-sm" data-dismiss="modal">提交</button>
                        </div>
                    </div>
                </div>
            </div>


            <!-- 修改弹出框 -->
            <div class="modal fade" id="editItem" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                            <h4 class="modal-title" id="editModalLabel">修改频道权重</h4>
                        </div>
                        <div class="modal-body">
                            <table id="edit-table" class="table table-hover">
                                <input id="editId" type="hidden" />
                                <tr>
                                    <th>序号</th>
                                    <td><input id="editSeq" type="text" class="form-control" placeholder="序号" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>父级编码</th>
                                    <td><input id="editParentCode" type="text" class="form-control" placeholder="父级编码" disabled="disabled"/></td>
                                </tr>
                                <tr>
                                    <th>权重</th>
                                    <td><input id="editCode" type="text" class="form-control" placeholder="权重" /></td>
                                </tr>
                                <tr>
                                    <th>发布数量</th>
                                    <td><input id="editValue" type="text" class="form-control" placeholder="发布数量" required="true"/></td>
                                </tr>
                                <tr>
                                    <th>描述</th>
                                    <td><input id="editDescribe" type="text" class="form-control" placeholder="描述" required="true"/></td>
                                </tr>
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button id="editNewItem" type="button" class="btn btn-primary btn-sm" onclick="editConfirm()" >提交</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade bs-example-modal-sm" id="dataManagerModal" tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title">删除记录</h4>
                        </div>
                        <div class="modal-body">
                            确定要删除这条记录吗?
                            <input id="deleteId" type="hidden"/>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btn-danger btn-sm" onclick="delConfirm()">确定</button>
                        </div>
                    </div>
                </div>
            </div>


            <script type="text/javascript">
                $('#addNewItem').click(function(event) {
                    var code = $("#code").val();
                    var parentCode = $("#parentCode").val();
                    var describe = $("#describe").val();
                    var value = $("#value").val();
                    var seq = $("#seq").val();
                    if(code && code.length > 0 && parentCode && parentCode.length > 0 && describe && describe.length > 0 && seq && seq.length > 0 && value && value.length > 0){
                        $('#addNewItem').attr("disabled","disabled");
                        post('channel/weightAdd',
                                'code='+code+'&parentCode='+parentCode+'&value='+value+'&describe='+describe+'&seq='+seq,
                                function (data) {
                                    if(data.status == 1) {
                                        cleanAddInput();
                                        location.href = '<%=basePath%>channel/weight?code=10001&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
                                    }else {
                                        alert('添加频道失败:'+data['info']);
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                });
                    }else{
                        alert("参数不能为空!");
                        return false;
                    }
                });
                function weightDel(id) {
                    $("#deleteId").val(id);
                    $("#dataManagerModal").modal('show');
                };
                function delConfirm() {
                    $("#dataManagerModal").modal('hide');
                    var id = $("#deleteId").val();
                    post('channel/weightDel', 'id='+id,
                        function (data) {
                            if(data.status == 1) {
                                location.href = '<%=basePath%>channel/weight?code=10001&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
                            }else {
                                alert('删除失败:'+data.info);
                            }
                            $("#deleteId").val(""); // 置空
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                            $("#deleteId").val(""); // 置空
                        });
                };
                function weightEidt(id) {
                    post('channel/weightGet', 'id=' + id,
                        function (data) {
                            if(data.status == 1) {
                                $("#editId").val(data.data.id);
                                $("#editCode").val(data.data.code);
                                $("#editParentCode").val(data.data.parentCode);
                                $("#editDescribe").val(data.data.describe);
                                $("#editValue").val(data.data.value);
                                $("#editSeq").val(data.data.seq);
                                $("#editItem").modal('show');
                            }else {
                                alert('获取权重信息失败:'+data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                };
                function editConfirm() {
                    var id = $("#editId").val();
                    var code = $("#editCode").val();
                    var parentCode = $("#editParentCode").val();
                    var describe = $("#editDescribe").val();
                    var value = $("#editValue").val();
                    var seq = $("#editSeq").val();
                    if(id && id.length > 0 && code && code.length > 0 && parentCode && parentCode.length > 0 && describe && describe.length > 0 && seq && seq.length > 0 && value && value.length > 0){
                        post('channel/weightEdit',
                                'id='+id+'&code='+code+'&parentCode='+parentCode+'&describe='+describe+'&value='+value+'&seq='+seq,
                                function (data) {
                                    if(data.status == 1) {
                                        $("#editId").val('');
                                        $("#editCode").val('');
                                        $("#editParentCode").val('');
                                        $("#editDescribe").val('');
                                        $("#editValue").val('');
                                        $("#editSeq").val('');
                                        $("#editItem").modal('hide');
                                        location.href = '<%=basePath%>channel/weight?code=10001&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val();
                                    }else {
                                        alert('修改失败:'+data.info);
                                    }
                                },
                                function () {
                                    alert('请求失败，请检查网络环境');
                                    $("#deleteId").val(""); // 置空
                                });
                    }else{
                        alert("参数不能为空!");
//                        $("#editItem").modal('show');
                        return false;
                    }
                };
                function cleanAddInput() {
                    $("#code").val('');
                    $("#parentCode").val('');
                    $("#describe").val('');
                    $("#value").val('');
                    $("#seq").val('');
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
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>

