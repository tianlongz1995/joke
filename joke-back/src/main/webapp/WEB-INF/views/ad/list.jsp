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
    <%--<%@ include file="../common/css.html" %>--%>
    <jsp:include page="../common/css.jsp"/>
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
                <div class="box col-md-12">
                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 广告列表</h2>
                        </div>
                        <div class="box-content">
                            <div class="alert alert-info" >
                                需要添加新的广告点击:  <a href="#" data-toggle="modal" data-target="#newad"><i class="glyphicon glyphicon-plus"></i> 新增广告</a>
                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row" style="padding: 0px 5px;">
                                    <div class="col-md-2" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                                        <p style="padding: 8px 0px;margin: 0px;">投放渠道：</p>
                                    </div>
                                    <div class="col-md-10" style="margin-left: 0px;">
                                        <div style="padding: 8px 0px;">
                                            <select id="distributors" style="font-size: 20px;width: 150px;margin: 5px;" >
                                                <c:if test="${empty distributorId}">
                                                    <option value="" selected>全部</option>
                                                </c:if>
                                                <c:if test="${not empty distributorId}">
                                                    <option value="">全部</option>
                                                </c:if>
                                                <c:forEach items="${dList}" var="distributor" varStatus="status">
                                                    <option value='<c:out value="${distributor.id}"/>' <c:if test="${!empty distributorId && distributorId == distributor.id}">selected</c:if> ><c:out value="${distributor.name}"/></option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="row" style="padding:  0px 5px;">
                                    <div class="col-md-2" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                                        <p style="padding: 8px 0px;margin: 0px;">广告位置 ：</p>
                                    </div>
                                    <div class="col-md-10" style="margin-left: 0px;">
                                        <select id="pagePos" style="font-size: 20px;width: 150px;margin: 5px;" >
                                            <c:if test="${empty pos}">
                                                <option value="" selected>全部</option>
                                            </c:if>
                                            <c:if test="${not empty pos}">
                                                <option value="">全部</option>
                                            </c:if>
                                            <option value="1" <c:if test="${!empty pos && pos == 1}">selected</c:if> >列表页中间</option>
                                            <option value="2" <c:if test="${!empty pos && pos == 2}">selected</c:if> >详情页插屏</option>
                                            <option value="3" <c:if test="${!empty pos && pos == 3}">selected</c:if> >详情页上方</option>
                                            <option value="4" <c:if test="${!empty pos && pos == 4}">selected</c:if> >详情页中部</option>
                                            <option value="5" <c:if test="${!empty pos && pos == 5}">selected</c:if> >详情页底部</option>
                                        </select>

                                    </div>
                                </div>
                                <div class="row" style="padding:  0px 5px;">
                                    <div class="col-md-2" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                                        <p style="padding: 8px 0px;margin: 0px;">广告状态 ：</p>
                                    </div>
                                    <div class="col-md-10" style="margin-left: 0px;">
                                        <select id="status" style="font-size: 20px;width: 150px;margin: 5px;" >
                                            <c:if test="${empty status}">
                                                <option value="" selected>全部</option>
                                            </c:if>
                                            <c:if test="${not empty status}">
                                                <option value="">全部</option>
                                            </c:if>
                                            <option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >下线
                                            </option>
                                            <option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >上线
                                            </option>
                                        </select>

                                    </div>
                                </div>
                                <div class="row" style="padding: 0px 5px;margin-bottom: 15px;">
                                    <div class="col-md-2" style="text-align: left;height: 38px;margin-right: 0px;padding-right: 0px;">
                                        <p style="padding: 8px 0px;margin: 0px;">广告位ID ：</p>
                                    </div>
                                    <div class="col-md-3" style="margin-left: 0px;line-height: 38px;">
                                        <input id="myslotId" type="text" style="padding: 8px 0px;margin:5px;width: 100%;" class=" input-sm col-xs-4" placeholder="输入广告链接ID" value="${slotId}"/>
                                    </div>
                                    <div class="col-md-7" style="text-align: left;line-height: 38px;">
                                        <a class="btn btn-primary btn-sm" href="#" id="selectadList" style="text-align: center;">
                                            <span class="glyphicon glyphicon-search icon-white">查询</span>
                                        </a>
                                    </div>
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
                                        <td><c:out value="${ad.slotId}"/></td>
                                        <td>
                                            <c:if test="${ad.pos == 1}">列表页中间</c:if>
                                            <c:if test="${ad.pos == 2}">详情页插屏</c:if>
                                            <c:if test="${ad.pos == 3}">详情页上方</c:if>
                                            <c:if test="${ad.pos == 4}">详情页中部</c:if>
                                            <c:if test="${ad.pos == 5}">详情页底部</c:if>
                                        </td>
                                        <td>
                                            <c:if test="${ad.pos == 1}">
                                                <c:out value="${ad.slide}"/>
                                            </c:if>
                                        </td>
                                        <td><c:out value="${ad.dName}"/></td>
                                        <td>
                                            <c:if test="${ad.status == 0}">
                                                下线
                                            </c:if>
                                            <c:if test="${ad.status == 1}">
                                                上线
                                            </c:if>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${ad.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${ad.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <c:if test="${ad.status == 0}">
                                                <a class="btn btn-success btn-xs" href="#" onclick="modifyStatus(1,${ad.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>上线
                                                </a>
                                            </c:if>
                                            <c:if test="${ad.status == 1}">
                                                <a class="btn btn-danger btn-xs" href="#" onclick="modifyStatus(0,${ad.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>下线
                                                </a>
                                            </c:if>
                                            <a class="btn btn-info btn-xs" href="ad/modify?id=${ad.id}">
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
                                        <jsp:include page="../common/page.jsp" />
                                    </div>
                                </div>
                            </div>
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
                                        <select id="did" style="font-size: 16px;width: 150px;margin: 2px;" class="form-control">
                                            <c:forEach items="${dList}" var="distributor" varStatus="status">
                                                <option value='<c:out value="${distributor.id}"/>'><c:out value="${distributor.name}"/></option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>广告位置ID</th>
                                    <td><input id="slotId" type="text" class="form-control" placeholder="输入广告位置ID"/></td>
                                </tr>
                                <tr>
                                    <th>广告状态</th>
                                    <td>
                                        <select id="addstatus" class="form-control">
                                            <option value="0">下线</option>
                                            <option value="1" selected>上线</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr id="adStatusTr">
                                    <th>投放位置</th>
                                    <td>
                                        <select id="pos" class="form-control">
                                            <option value="1" selected>列表页中间</option>
                                            <option value="2">详情页插屏</option>
                                            <option value="3">详情页上方</option>
                                            <option value="4">详情页中部</option>
                                            <option value="5">详情页底部</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>投放频率</th>
                                    <td><input id="slide" type="text" class="input-sm" style="width:50px;" />（内容）+ 1（广告）</td>
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
                $('#addNewad').click(function(event) {
                    $('#addNewad').attr("disabled","disabled");
                    var slotId = $('#slotId').val();
                    if(slotId != null && slotId.trim().length > 0 && !isInteger(slotId)){
                        alert("广告编号不是Int类型!");
                        $('#addNewad').removeAttr("disabled");
                        return false;
                    }
                    var slide = $('#slide').val();
                    if(slide == null || slide == undefined ){
                        slide = '';
                    }
                    var pos = $('#pos').val();
                    if(pos == 1){
                         if(!isInteger(slide) || slide < 1 || slide > 99){
                             alert("投放频率必须是大于0或者小于100的正整数");
                             $('#addNewad').removeAttr("disabled");
                             return false;
                         }
                    }
                    post('ad/addCheck','did='+ $("#did").val()+'&pos='+$('#pos').val(),function(result){
                        if(result.status == 1){
                            post('ad/add',
                                    'did=' + $("#did").val()+'&pos='+pos+'&slide='+slide+'&status='+$('#addstatus').val()+'&slotId='+$('#slotId').val(),
                                    function (data) {
                                        if(data.status == 1) {
                                            location.href = '<%=basePath%>ad/list?status='+$("#status").val();
                                        }else {
                                            $('#addNewad').removeAttr("disabled");
                                            alert('添加失败. info:'+data.info);
                                        }
                                    },
                                    function () {
                                        $('#addNewad').removeAttr("disabled");
                                        alert('请求失败，请检查网络环境');
                                    });
                        }else{
                            $('#addNewad').removeAttr("disabled");
                            alert('添加失败. info:'+result.info);
                        }
                    }, function(){
                        $('#addNewad').removeAttr("disabled");
                        alert('添加失败. info:'+data['info']);
                    });

                });


                function modifyStatus(status, id) {
                    post('<%=basePath%>ad/modifyStatus',
                            'id=' + id + '&status=' + status,
                            function (data) {
                                if (data.status == 1) {
                                    var slotId = $("#myslotId").val();
                                    var param = '';
                                    if($.isNumeric(slotId)){
                                        param += "&slotId="+slotId;
                                    }
                                    var did = $('#distributors').val();
                                    location.href = '<%=basePath%>ad/list?status=' + $("#status").val()
                                    + '&distributorId=' + did +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
                                    + '&pos=' + $("#pagePos").val() + param;
                                }
                                else {
                                    alert('操作失败. info:' + data.info);
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                            });
                };

                $('#selectadList').click(function (event) {
                    var slotId = $("#myslotId").val();
                    var param = '';
                    if(isInteger(slotId)){
                        param += "&slotId="+slotId;
                    }
                    if(slotId != null && slotId.trim().length > 0 && !isInteger(slotId)){
                        alert("广告编号不是Int类型!");
                        return false;
                    }
                    var did = $('#distributors').val();
                    location.href = '<%=basePath%>ad/list?status=' + $("#status").val()
                        + '&distributorId=' + did +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val()
                        + '&pos=' + $("#pagePos").val() + param;
                });

                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };
                function turnPage(){
                    var slotId = $("#myslotId").val();
                    var param = '';
                    if(isInteger(slotId)){
                        param += "&slotId="+slotId;
                    }
                    if(slotId != null && slotId.trim().length > 0 && !isInteger(slotId)){
                        alert("广告编号不是Int类型!");
                        return false;
                    }
                    location.href = '<%=basePath%>ad/list?status='+$("#status").val()+'&distributorId='+$("#distributors").val()+'&pos='+$("#pagePos").val()
                    +'&pageSize='+$("#pageSize").val()+'&pageNumber='+$("#pageNumber").val() + param;
                };
                $('#pos').change(function (event) {
                    var pos = $("#pos").val();
                    if(pos == 1){
                        $("#adStatusTr").next().remove();
                        $("#adStatusTr").after('<tr><th>投放频率</th><td><input id="slide" type="text" class="input-sm" style="width:50px;" />（内容）+ 1（广告）</td></tr>');
                    }else{
                        $("#adStatusTr").next().remove();
                    }
                });
                function isInteger(x) {
                	if(x == null || x.trim().length < 1){
                        return false;
                     }
                     var n = Number(x);
                     return n >= -2147483648 && n <= 2147483647;
                }
            </script>

        </div>
        <!-- content end -->
    </div>
    <!-- row end -->
</div>
<!-- ch-container end -->

<hr>
<%--<%@ include file="../common/footer.html" %>--%>
<%--<%@ include file="../common/js.html" %>--%>
<jsp:include page="../common/footer.jsp"/>
<jsp:include page="../common/js.jsp"/>
</body>
</html>
