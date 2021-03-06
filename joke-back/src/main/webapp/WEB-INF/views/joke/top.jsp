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
    <title>首页置顶</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
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
                            <h2><i class="glyphicon glyphicon-user"></i> 首页置顶列表</h2>
                        </div>
                        <div class="box-content" style="vertical-align: middle;">
                            <table id="table_list"
                                   class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="dataTables_filter" id="DataTables_Table_0_filter" style="margin: 15px 5px;">
                                    <label style="padding-right:10px;">
                                        <span>开始</span>
                                        <c:if test="${empty startDay}">
                                            <input type="text" id="startDay"
                                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate" value=""
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty startDay}">
                                            <input type="text" id="startDay"
                                                   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" class="Wdate"
                                                   value="${startDay}"
                                                   style="max-width: 160px;"/>
                                        </c:if>
                                    </label>
                                    <label style="padding-right:10px;">
                                        <span>结束</span>
                                        <c:if test="${empty endDay}">
                                            <input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
                                                   class="Wdate" value="" style="max-width: 160px;"/>
                                        </c:if>
                                        <c:if test="${!empty endDay}">
                                            <input type="text" id="endDay" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
                                                   class="Wdate" value="${endDay}"
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
                                                    <c:if test="${!empty status && status == 0}">selected</c:if> >未处理
                                            </option>
                                            <option value="1"
                                                    <c:if test="${!empty status && status == 1}">selected</c:if> >已处理
                                            </option>
                                            <option value="2"
                                                    <c:if test="${!empty status && status == 2}">selected</c:if> >已置顶
                                            </option>
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
                                    <c:if test="${status == 0}">
                                        <label style="padding-right:10px;">
                                            <a class="btn btn-success btn-sm" href="#"
                                               onclick="process('batch', 'batch')">
                                                <i class="glyphicon glyphicon-time icon-white"></i> 批量置顶
                                            </a>
                                        </label>
                                        <label style="padding-right:10px;">
                                            <a class="btn btn-primary btn-sm" href="#" onclick="editSorts()">
                                                <i class="glyphicon glyphicon-time icon-white"></i> 批量排序
                                            </a>
                                        </label>
                                    </c:if>
                                </div>

                                <thead>
                                <tr>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">全选 <input
                                            type="checkbox" id="allcheck"/></th>
                                    <th style="width: 40%;text-align: center; vertical-align: middle;">内容</th>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">格式</th>
                                    <th style="width: 10%;text-align: center; vertical-align: middle;">来源</th>
                                    <th style="width: 15%;text-align: center; vertical-align: middle;">
                                        发布时间
                                    </th>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">权重值</th>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">状态</th>
                                    <th style="width: 5%;text-align: center; vertical-align: middle;">操作</th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach items="${list}" var="joke">
                                    <tr>
                                        <td style="text-align: center; vertical-align: middle;" onclick="ckd('${joke.id}')">
                                            <input type="checkbox" name="jokeId" id="ckd${joke.id}" value="${joke.id}"
                                                   sort="${joke.sort}"/>
                                        </td>
                                        <td>
                                            <div class="table-item"
                                                 style="margin: 0px;padding: 0px;width: 100%;height: 100%;top:0px;bottom:0px;min-width: 100%;min-height: 50px;"
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
                                            <c:if test="${!empty joke.releaseTime}">
                                                ${joke.releaseTime}
                                            </c:if>
                                        </td>
                                        <td>
                                            <input id="sort${joke.id}" type="text"
                                                   <c:if test="${joke.status != 0}">disabled</c:if>
                                                   class="form-control input-sm" style="width: 40px;padding: 5px;"
                                                   value="${joke.sort}">
                                        </td>

                                        <td>
                                                <%-- 状态 --%>
                                            <c:if test="${joke.status == 0}">
                                                <a class="btn btn-default btn-xs" href="#">
                                                    <i class="glyphicon glyphicon-question-sign"
                                                       style="color: sandybrown;"></i> 未处理
                                                </a>
                                            </c:if>
                                            <c:if test="${joke.status == 2}">
                                                <a class="btn btn-warning btn-xs" href="#">
                                                    <i class="glyphicon glyphicon-ok-circle icon-white"></i> 已置顶
                                                </a>
                                            </c:if>
                                            <c:if test="${joke.status == 1}">
                                                <a class="btn btn-success btn-xs" href="#">
                                                    <i class="glyphicon glyphicon-thumbs-up"></i> 已处理
                                                </a>
                                            </c:if>
                                        </td>
                                        <td>
                                                <%--未审核--%>
                                            <c:if test="${!empty status && status == 0}">
                                                <a class="btn btn-primary btn-xs" href="#" style="margin-bottom: 2px;"
                                                   onclick="editSort(${joke.id})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 修改排序
                                                </a>
                                                <a class="btn btn-success btn-xs" href="#"
                                                   onclick="process(${joke.id}, ${joke.sort})">
                                                    <i class="glyphicon glyphicon-ok icon-white"></i> 发布置顶
                                                </a>
                                            </c:if>
                                            <a class="btn btn-danger btn-xs" href="#" onclick="offline(${joke.id})">
                                                <i class="glyphicon glyphicon-remove icon-white"></i> 下线
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


            <div class="modal fade" id="process" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button>
                            <h4 class="modal-title" id="myModalLabel">首页置顶</h4>
                        </div>
                        <div class="modal-body">
                            <table id="orders-table" class="table table-hover">
                                <tr>
                                    <th>提示</th>
                                    <td>发布时间一定要与【推荐发布规则配置】时间一致。例如，【推荐发布规则配置】时间是每天8点整、12点整、18点整发布数据，置顶的发布时间要定在8点整、12点整、18点整，否则会出现发布失败的问题。</td>
                                </tr>
                                <tr>
                                    <th>发布时间</th>
                                    <td>
                                        <input id="ids" value="" type="hidden">
                                        <input id="sorts" value="" type="hidden">
                                        <input id="releaseTime" type="text"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})"
                                               class="form-control"
                                               value=""/>
                                    </td>
                                </tr>

                            </table>
                        </div>
                        <div class="modal-footer" style="text-align: center;">
                            <button onclick="processSubmit()" type="button" class="btn btn-sm btn-primary"
                                    data-dismiss="modal">提交
                            </button>
                        </div>
                    </div>
                </div>
            </div>


            <script type="text/javascript">
                /** 鼠标移动到图片上自动显示   */
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
                /** 鼠标移动到动图自动播放   */
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

                /** 全选   */
                $('#allcheck').on('click', function () {
                    if ($(this).prop("checked")) {
                        $(":checkbox").prop("checked", true);
                    } else {
                        $(":checkbox").prop("checked", false);
                    }
                });

                /** 修改排序值   */
                function editSort(id) {
                    var sort = $("#sort" + id).val();
                    console.log(id + ":" + sort);
                    post('joke/editTopJokeSort',
                        'id=' + id + '&sort=' + sort,
                        function (data) {
                            if (data.status == 1) {
                                alert('成功:' + data.info);
                            } else {
                                alert('处理失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                }

                /** 修改排序值   */
                function editSorts() {
                    var ids = [];
                    var sorts = [];
                    $('input[name="jokeId"]:checked').each(function () {
                        var i = $(this).val();
                        ids.push(i);
                        var sortValue = $("#sort" + i).val();
                        sorts.push(sortValue)
                    });
                    if (ids.length < 1 || sorts.length < 1) {
                        alert("未选中任何内容");
                        return false;
                    }
                    var id = ids.toString();
                    var sort = sorts.toString();
//                    alert(id + "=" + sort);
                    post('joke/editTopJokeSorts',
                        'ids=' + id + '&sorts=' + sort,
                        function (data) {
                            if (data.status == 1) {
                                alert('成功:' + data.info);
                            } else {
                                alert('处理失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                }

                /** 打开处理窗口   */
                function process(id, sort) {
                    if ("batch" == id) {
                        var ids = [];
                        var sorts = [];
                        $('input[name="jokeId"]:checked').each(function () {
                            var i = $(this).val();
                            ids.push(i);
                            var sortValue = $("#sort" + i).val();
                            sorts.push(sortValue)
                        });
                        if (ids.length == 0 || sorts.length == 0) {
                            alert("未选中任何内容");
                            return false;
                        }
                        id = ids.toString();
                        sort = sorts.toString();
                    }

                    $("#ids").val(id);
                    $("#sorts").val(sort);

                    // 显示定时发布日期和时间的设置窗口
                    $("#process").modal('show');
                };

                function offline(id) {
                    post('joke/topOffline',
                        'ids=' + id,
                        function (data) {
                            if (data.status == 1) {
                                turnPage();
                            } else {
                                alert('处理失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                };

                /** 提交处理结果   */
                function processSubmit() {
                    var id = $("#ids").val();
                    var sorts = $("#sorts").val();
                    var releaseTime = $("#releaseTime").val();
                    post('joke/releaseTopJoke',
                        'ids=' + id + '&sorts=' + sorts + '&releaseTime=' + releaseTime,
                        function (data) {
                            if (data.status == 1) {
                                turnPage();
                            } else {
                                alert('处理失败:' + data.info);
                            }
                        },
                        function () {
                            alert('请求失败，请检查网络环境');
                        });
                }
                /** 查询   */
                $('#selectVerifyJokeList').click(function (event) {
                    location.href = '<%=basePath%>joke/top?type=' + $("#type").val() + '&status=' + $("#status").val() + '&source=' + $("#source").val()
                        + '&startDay=' + $("#startDay").val() + '&endDay=' + $("#endDay").val();
                });

                function post(url, data, success, error) {
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                }
                ;
                /** 分页请求   */
                function turnPage() {
                    location.href = '<%=basePath%>joke/top?type=' + $("#type").val() + '&status=' + $("#status").val() + '&source=' + $("#source").val()
                        + '&startDay=' + $("#startDay").val() + '&endDay=' + $("#endDay").val()
                        + '&pageNumber=' + $("#pageNumber").val() + '&pageSize=' + $("#pageSize").val();
                }
                ;
            </script>

        </div><!-- content end -->
    </div><!-- row end -->


    <!--  图片展示页面	-->
    <div id="showPic"
         style="display: none;text-align: center;position: fixed; _position:absolute;left:50%;top:50%;margin: -141px 0 0 -201px;border:0px;">
        <img id="pic" src=""/>
    </div>
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html" %>
<%@ include file="../common/js.html" %>
</body>
</html>
