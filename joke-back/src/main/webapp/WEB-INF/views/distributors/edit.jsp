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
    <title>渠道编辑</title>
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
            <div class="row">
                <div class="box col-md-12">

                    <div class="box-inner">
                        <div class="box-header well" data-original-title="">
                            <h2><i class="glyphicon glyphicon-user"></i> 渠道编辑</h2>
                        </div>
                        <div class="box-content">
                            <table class="table table-hover">
                                <tr>
                                    <div class="row" style="padding: 8px;vertical-align: middle;">
                                        <div class="col-lg-1 col-sm-1"  style="padding: 5px;text-align: right;">
                                            ID
                                        </div>
                                        <div class="col-lg-3 col-sm-3">
                                            <input id="distributorid" type="text" class="form-control input-sm" disabled="disabled" value="${distributors.id}"/>
                                        </div>
                                        <div class="col-lg-1 col-sm-1"  style="padding: 5px;text-align: right;">
                                            名称
                                        </div>
                                        <div class="col-lg-3 col-sm-3">
                                            <input id="name" type="text" class="form-control input-sm" value="${distributors.name}"/>
                                        </div>
                                        <div class="col-lg-1 col-sm-1"  style="padding: 5px;text-align: right;">
                                            状态
                                        </div>
                                        <div class="col-lg-3 col-sm-3">
                                            <select id="status" class="form-control input-sm">
                                                <option value="0" <c:if test="${0 == distributors.status}">selected</c:if>>下线</option>
                                                <option value="1" <c:if test="${1 == distributors.status}">selected</c:if>>上线</option>
                                            </select>
                                        </div>
                                    </div>
                                </tr>
                                <tr>
                                    <th style="vertical-align: middle;">频道</th>
                                    <td>
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
                                                        <input type="checkbox" name="channelIds" value="${channel.id}"
                                                               <c:if test="${!empty channel.status && 1 == channel.status}">checked="checked"</c:if> >
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
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.lc}">checked="checked"</c:if> ></th>
                                                <th>列表页中间</th>
                                                <th><input id="listCenter" type="text" name="adsValue" class="form-control input-sm" value="${ads.lc}"/></th>
                                                <th><input id="interval" type="text" class="form-control input-sm" value="${ads.s}"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.lb}">checked="checked"</c:if> ></th>
                                                <th>列表页底部</th>
                                                <th colspan="2"><input id="listBottom" name="adsValue" type="text" class="form-control input-sm" value="${ads.lb}"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.dt}">checked="checked"</c:if> ></th>
                                                <th>详情页上方</th>
                                                <th colspan="2"><input id="detailsTop" name="adsValue" type="text" class="form-control input-sm" value="${ads.dt}"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.dc}">checked="checked"</c:if> ></th>
                                                <th>详情页中部</th>
                                                <th colspan="2"><input id="detailsCentor" name="adsValue" type="text" class="form-control input-sm" value="${ads.dc}"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.db}">checked="checked"</c:if> ></th>
                                                <th>详情页底部</th>
                                                <th colspan="2"><input id="detailsBottom" name="adsValue" type="text" class="form-control input-sm" value="${ads.db}"/></th>
                                            </tr>
                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.di}">checked="checked"</c:if> ></th>
                                                <th>详情页插屏</th>
                                                <th colspan="2"><input id="detailsInterstitial" name="adsValue" type="text" class="form-control input-sm" value="${ads.di}"/></th>
                                            </tr>

                                            <tr>
                                                <th><input type="checkbox" name="ads" <c:if test="${!empty ads.dr}">checked="checked"</c:if>></th>
                                                <th>详情页推荐广告</th>
                                                <th colspan="2"><input id="detailsRecommendAd" name="adsValue" type="text" class="form-control input-sm" value="${ads.dr}"/></th>

                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                            <div style="width: 100%;text-align: center;">
                                <button type="button" class="btn btn-default" onclick="javascript:window.history.go(-1)" >返回</button>
                                &nbsp;&nbsp;
                                <button id="updateDistributor" type="button" class="btn btn-primary" data-dismiss="modal">提交</button>
                            </div>
                        </div>
                    </div>
                </div><!-- box col-md-12 end -->
            </div><!-- row end -->

            <script type="text/javascript">
                $('#updateDistributor').click(function (event) {
                    $('#updateDistributor').attr("disabled", "disabled");
                    var id = document.getElementsByName('channelIds');
                    var value = new Array();
                    for (var i = 0; i < id.length; i++) {
                        if (id[i].checked){
                            value.push(id[i].value);
                        }
                    }
                    if (value.length == 0) {
                        alert("未选中任何频道");
                        $('#updateDistributor').removeAttr("disabled");
                        return false;
                    }
                    var name = $("#name").val();
                    var status = $('#status').val();
                    if(name == null || name.length < 1 || status == null || status.length < 1){
                        alert("名称、状态不能为空!");
                        $('#updateDistributor').removeAttr("disabled");
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
                                $('#updateDistributor').removeAttr("disabled");
                                return false;
                            }
                            if(i == 0){
                                var interval = $("#interval").val();
                                if(interval == null || interval.length < 1){
                                    alert("列表页中间广告间隔必须设置!");
                                    $('#updateDistributor').removeAttr("disabled");
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
                            }else if(i == 6){
                                ads = ads + '&dr=' + adsValue[i].value;
                            }
                        }
                    }
                    if(index < 1){
                        alert("必须设置广告默认值!");
                        $('#updateDistributor').removeAttr("disabled");
                        return false;
                    }

                    post('distributors/edit',
                            'id=' + $("#distributorid").val() + '&name=' + name + '&status=' + status + '&channelIds=' + value.toString() + ads,
                            function (data) {
                                if (data.status == 1) {
                                    location.href = '<%=basePath%>distributors/list';
                                } else {
                                    alert('更新失败:' + data.info);
                                    $('#updateDistributor').removeAttr("disabled");
                                }
                            },
                            function () {
                                alert('请求失败，请检查网络环境');
                                $('#updateDistributor').removeAttr("disabled");
                            });
                });
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
                }
                ;
                function post(url, data, success, error) {
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