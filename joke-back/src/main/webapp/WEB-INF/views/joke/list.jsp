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
    <title>内容审核</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>
    <script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script src="/ui/js/date/WdatePicker.js"></script>
    <!-- The fav icon -->
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
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 内容审核列表</h2>
                        </div>
                        <div class="box-content" style="vertical-align: middle;">
                            <div class="alert alert-info" style="vertical-align: middle;margin: auto;padding: 10px;">
                                <label style="padding-right:30px;margin: auto;vertical-align: middle;">
                                    <span>已审核量</span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>文字:<c:out value="${type0}"></c:out></span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>图片:<c:out value="${type1}"></c:out></span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>动图:<c:out value="${type2}"></c:out></span>
                                </label>

                                <label style="padding-right:30px;margin: auto;vertical-align: middle;">
                                    <span>已置顶未处理:</span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>文字:<c:out value="${type3}"></c:out></span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>图片:<c:out value="${type4}"></c:out></span>
                                </label>
                                <label style="padding-right:30px;">
                                    <span>动图:<c:out value="${type5}"></c:out></span>
                                </label>

                                <div style="float: right">
                                    <a class="btn btn-success btn-sm" href="joke/addJokePage" >
                                        <i class="glyphicon glyphicon-add icon-white"></i>新增段子
                                    </a>
                                </div>
                            </div>
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter" style="margin: 15px 5px;">
                                    <label style="padding-right:10px;">
                                        <span>开始日期</span>
                                        <c:if test="${empty startDay}">
                                            <input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value=""
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty startDay}">
                                            <input type="text" id="startDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="${startDay}"
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                    </label>
                                    <label style="padding-right:10px;">
                                        <span>结束日期</span>
                                        <c:if test="${empty endDay}">
                                            <input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="" style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty endDay}">
                                            <input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="${endDay}"
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                    </label>
                                    <div class="form-group" style="padding-right:10px;display: inline-block;">
                                        <label>格式</label>
                                        <select id="type" class="form-control input-sm">
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${!empty type && type == 0}">selected</c:if> >文字
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty type && type == 1}">selected</c:if> >图片
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty type && type == 2}">selected</c:if> >动图
                                            </option>
                                        </select>
                                    </div>
                                    <div class="form-group" style="padding-right:10px;display: inline-block;">
                                        <label>状态</label>
                                        <select id="status" class="form-control input-sm">
                                            <option value="">全部</option>
                                            <option value="0"
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >未审核
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >已通过
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty status && status == 2}">selected</c:if> >不通过
                                            </option>
                                            <option value="3"
                                                    <c:if test="${!empty status && status == 3}">selected</c:if> >已发布
                                            </option>
                                            <%--<option value="6" <c:if test="${!empty status && status == 6}">selected</c:if> >首页置顶</option>--%>
                                        </select>
                                    </div>
                                    <div style="padding-right:10px;display: inline-block;">
                                        <span>数据源</span>
                                        <select id="source" class="form-control input-sm">
                                            <c:if test="${empty source || source == 0}">
                                                <option value="" selected="selected">全部</option>
                                            </c:if>
                                            <c:if test="${!empty source && source != 0}">
                                                <option value="">全部</option>
                                            </c:if>
                                            <c:forEach items="${sourceList}" var="sources">
                                                <option value="${sources.id}"
                                                        <c:if test="${!empty source && source == sources.id}">selected</c:if> >${sources.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <label style="padding-right:10px;">
                                        <a class="btn btn-primary btn-sm" href="#" id="selectVerifyJokeList">
                                            <span class="glyphicon glyphicon-search icon-white"> 查询</span>
                                        </a>
                                    </label>
                                    <c:if test="${status == 0 || status == 2}">
                                        <label style="padding-right:10px;">
                                            <a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,'batch')">
                                                <i class="glyphicon glyphicon-ok icon-white"></i>批量通过
                                            </a>
                                        </label>
                                    </c:if>
                                    <c:if test="${status == 0}">
                                        <label style="padding-right:10px;">
                                            <a class="btn btn-primary btn-sm" href="#" onclick="verifyJoke(6,'batch')">
                                                <i class="glyphicon glyphicon-open icon-white"></i> 批量置顶
                                            </a>
                                        </label>
                                    </c:if>
                                    <c:if test="${status != 3}">
                                        <label style="padding-right:0px;">
                                            <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,'batch')">
                                                <i class="glyphicon glyphicon-remove icon-white"></i>批量不通过
                                            </a>
                                        </label>
                                    </c:if>
                                    <c:if test="${status == 3}">
                                        <label style="padding-right:10px;">
                                            <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(5,'batch')">
                                                <i class="glyphicon glyphicon-remove icon-white"></i>批量下线
                                            </a>
                                        </label>
                                    </c:if>
                                </div>

                                <thead>
                                <tr>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">全选 <input type="checkbox" id="allcheck"/></th>
                                    <th style="width: 35%;text-align: center; vertical-align: middle;">内容</th>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">格式</th>
                                    <th style="width: 12%;text-align: center; vertical-align: middle;">来源</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">抓取时间</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">分值</th>
                                    <th style="width: 6%;text-align: center; vertical-align: middle;">神评数</th>
                                    <th style="width: 6%;text-align: center; vertical-align: middle;">状态</th>
                                    <th style="width: 22%;text-align: center; vertical-align: middle;">操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="joke">
                                    <tr>
                                        <td style="text-align: center; vertical-align: middle;" onclick="ckd('${joke.id}')">
                                            <input type="checkbox" name="jokeId" id="ckd${joke.id}" value="${joke.id}"/>
                                        </td>
                                        <td>
                                            <div class="table-item" style="margin: 0px;padding: 0px;width: 100%;height: 100%;top:0px;bottom:0px;min-width: 100%;min-height: 50px;"
                                                 <c:if test="${joke.type == 1}">data-origin="${joke.img}"</c:if>
                                                 <c:if test="${joke.type == 2}">data-src="${joke.gif}"</c:if> >
                                                <c:if test="${!empty joke.title}">
                                                    <p><h5>${joke.title}</h5></p>
                                                </c:if>
                                                <c:if test="${!empty joke.content}">
                                                    <p>
                                                        <small>${joke.content}</small>
                                                    </p>
                                                </c:if>
                                            </div>
                                        </td>
                                        <td onclick="ckd('${joke.id}')">
                                            <c:if test="${joke.type == 0}">文字</c:if>
                                            <c:if test="${joke.type == 1}">图片</c:if>
                                            <c:if test="${joke.type == 2}">动图</c:if>
                                        </td>
                                        <td onclick="ckd('${joke.id}')">
                                                ${joke.sourceName}
                                        </td>
                                        <td onclick="ckd('${joke.id}')">
                                            <fmt:formatDate value="${joke.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td onclick="ckd('${joke.id}')">
                                                ${joke.weight}
                                        </td>
                                        <td onclick="ckd('${joke.id}')">
                                                ${joke.replyNum}
                                        </td>
                                        <td>
                                            <c:if test="${joke.status == 0}">未审核</c:if>
                                            <c:if test="${joke.audit == 6}">
                                                <a class="btn btn-warning btn-xs" href="#" style="margin-bottom: 2px;">
                                                    <i class="glyphicon glyphicon-open icon-white"></i> 已置顶
                                                </a>
                                            </c:if>
                                            <c:if test="${joke.status == 1}">
                                                <a class="btn btn-success btn-xs" href="#">
                                                    <i class="glyphicon glyphicon-thumbs-up icon-white"></i> 通&nbsp;&nbsp;&nbsp;过
                                                </a>
                                            </c:if>
                                            <c:if test="${joke.status == 2}">不通过</c:if>
                                            <c:if test="${joke.status == 3 || joke.status == 4}">已发布</c:if>
                                        </td>
                                        <td>
                                                <%--未审核--%>
                                            <c:if test="${joke.status == 0}">
                                                <a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,${joke.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>通过
                                                </a>
                                                <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,${joke.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i>不通过
                                                </a>
                                                <a class="btn btn-info btn-sm" href="<%=basePath%>joke/edit?id=${joke.id}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i>编辑
                                                </a>
                                            </c:if>
                                                <%--通过--%>
                                            <c:if test="${joke.status == 0}">
                                                <a class="btn btn-primary btn-sm" href="#" onclick="verifyJoke(6,${joke.id})">
                                                    <i class="glyphicon glyphicon-open icon-white"></i> 置 顶
                                                </a>
                                                <%--<a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,${joke.id})">--%>
                                                <%--<i class="glyphicon glyphicon-remove icon-white"></i> 不通过--%>
                                                <%--</a>--%>
                                            </c:if>

                                            <c:if test="${joke.status == 6}">
                                                <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(2,${joke.id})">
                                                    <i class="glyphicon glyphicon-remove icon-white"></i> 不通过
                                                </a>
                                            </c:if>
                                                <%--已发布--%>
                                            <c:if test="${joke.status == 3 || joke.status == 4}">
                                                <a class="btn btn-danger btn-sm" href="#" onclick="verifyJoke(5,${joke.id})">
                                                    <i class="glyphicon glyphicon-cloud-download icon-white"></i>下线
                                                </a>
                                            </c:if>
                                            <c:if test="${joke.status == 2}">
                                                <a class="btn btn-success btn-sm" href="#" onclick="verifyJoke(1,${joke.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i>通过
                                                </a>
                                                <a class="btn btn-info btn-sm" href="<%=basePath%>joke/edit?id=${joke.id}">
                                                    <i class="glyphicon glyphicon-edit icon-white"></i>编辑
                                                </a>
                                            </c:if>
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

            <script type="text/javascript">
                $('#table_list img').hover(function () {
                    var gif = $(this).attr('data-src');
                    if (gif) {
                        this.src = gif;
                    }
                }, function () {
                    var origin = $(this).attr('data-origin');
                    if (origin) {
                        this.src = origin;
                    }
                });

                $('.table-item').hover(function () {
                    var gif = $(this).attr('data-src');
                    var origin = $(this).attr('data-origin');
                    //console.log(gif + " ori:" + origin)
                    if (gif) {
                        $("#pic").attr("src", gif);
                        $("#showPic").css('display', 'block');
                    } else if (origin) {
                        $("#pic").attr("src", origin);
                        $("#showPic").css('display', 'block');
                    }
                }, function () {
                    $("#showPic").css('display', 'none');
                });

                $('#allcheck').on('click', function () {
                    if ($(this).prop("checked")) {
                        $(":checkbox").prop("checked", true);
                    } else {
                        $(":checkbox").prop("checked", false);
                    }
                });

                //阻止checkbox事件冒泡
                $("input[type='checkbox']").click(function(e){
                    e.stopPropagation();
                });
                function ckd(id) {
                    var isChecked = $("#" + "ckd" + id).is(":checked");
                    if (isChecked) {
                        $("#" + "ckd" + id).removeAttr("checked");
                    } else {
                        $("#" + "ckd" + id).prop("checked", "checked");
                    }
                }

                function verifyJoke(status, id) {
                    if ("batch" == id) {
                        var ids = [];
                        $('input[name="jokeId"]:checked').each(function () {
//			console.log("attr:" + this.attr + ", checked:" + this.checked + ", this:" + this + ", val:" + $(this).val());
                            ids.push($(this).val());
                        });
                        if (ids.length == 0) {
                            alert("未选中任何内容");
                            return false;
                        }
                        id = ids.toString();
                    }
                    post('joke/verify',
                            'ids=' + id + '&status=' + status + '&allStatus=' + $("#status").val(),
                            function (data) {
                                if (data.status == 1) {
                                    turnPage();
                                } else {
                                    alert('审核失败:' + data.info);
                                }
                            },
                            function () {
                                if (status == 6) {
                                    alert('请求失败，不能提交已置顶段子!');
                                } else {
                                    alert('请求失败，请检查网络环境!');
                                }
                            });
                }

                $('#selectVerifyJokeList').click(function (event) {
                    location.href = '<%=basePath%>joke/list?type=' + $("#type").val() + '&status=' + $("#status").val() + '&source=' + $("#source").val()
                            + '&startDay=' + $("#startDay").val() + '&endDay=' + $("#endDay").val();
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
                function turnPage() {
                    location.href = '<%=basePath%>joke/list?type=' + $("#type").val() + '&status=' + $("#status").val() + '&source=' + $("#source").val()
                            + '&startDay=' + $("#startDay").val() + '&endDay=' + $("#endDay").val()
                            + '&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                }
                ;
            </script>

        </div><!-- content end -->
    </div><!-- row end -->


    <!--  图片展示页面	-->
    <div id="showPic" style="display: none;text-align: center;position: fixed; _position:absolute;left:50%;top:50%;margin: -141px 0 0 -201px;border:0px;">
        <img id="pic" src=""/>
    </div>
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
