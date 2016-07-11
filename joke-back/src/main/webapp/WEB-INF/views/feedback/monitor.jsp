<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    String date= format.format(new Date());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>用户反馈</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
    <meta name="author" content="Muhammad Usman">
    <!-- default header name is X-CSRF-TOKEN -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>

    <base href="<%=basePath%>">
    <%@ include file="../common/css.html" %>

    <script src="<%=basePath%>ui/charisma/bower_components/jquery/jquery.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=basePath%>ui/js/ichart.1.2.min.js"></script>
    <script language="javascript" type="text/javascript" src="<%=basePath%>ui/js/date/WdatePicker.js"></script>
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
                            <h2><i class="glyphicon glyphicon-user"></i> 用户反馈</h2>
                        </div>
                        <div class="box-content">
                            <table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="bs-example" style="margin: 0px 5px;padding: 0px 5px;display: inline-block;">
                                                <div class="form-group" style="display: inline-block;margin-bottom: 0px;">
                                                    <label>
                                                        <label for="startDate" style="display: inline-block;">选择日期：</label>
                                                    </label>
                                                </div>
                                                <div class="form-group" style="display: inline-block;margin-bottom: 0px;">
                                                    <input id="startDate" class="form-control input-sm" <c:if test="${!empty startDate}">value="${startDate}"</c:if> <c:if test="${empty startDate}">value="<%=date%>"</c:if>  style="display: inline-block;" type="text"  onfocus="WdatePicker({skin:'whyGreen',minDate:'2016-01-01',maxDate:'2066-12-31'})"/>
                                                </div>
                                              <div class="form-group" style="display: inline-block;margin-bottom: 0px;">
                                                  <label>
                                                      <label for="endDate" style="display: inline-block;">至</label>
                                                  </label>
                                              </div>
                                              <div class="form-group" style="display: inline-block;margin-bottom: 0px;">
                                                  <input id="endDate" class="form-control input-sm" <c:if test="${!empty endDate}">value="${endDate}"</c:if> <c:if test="${empty endDate}">value="<%=date%>"</c:if>  style="display: inline-block;" type="text"  onfocus="WdatePicker({skin:'whyGreen',minDate:'2016-01-01',maxDate:'2066-12-31'})"/>
                                              </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12" style="margin-left: 0px;">
                                        <div class="form-group" style="display: inline-block;padding-left: 10px;">
                                            <label>
                                                <label for="distributors" style="display: inline-block;">选择渠道：</label>
                                            </label>
                                        </div>

                                        <div style="padding: 8px 0px;display: inline-block;">
                                            <select id="distributors" style="font-size: 20px;width: 150px;margin: 5px;display: inline-block;" >
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
                                        &nbsp; &nbsp;
                                        <div style="line-height: 38px;display: inline-block;">
                                            <a class="btn btn-primary btn-sm" href="#" id="query" style="text-align: center;">
                                                <span class="glyphicon glyphicon-search icon-white">查询</span>
                                            </a>
                                        </div>
                                    </div>

                                </div>
                                <thead>
                                <tr>
                                </tr>
                                </thead>
                                <tbody>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div id='pieGraph'></div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div id='lineGraph'></div>
                                    </div>
                                </div>
                                </tbody>
                            </table>
                            <div style="text-align: center;">
                                <a class="btn btn-danger btn-sm" href="feedback/list">朕已阅</a>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- box col-md-12 end -->
            </div>
            <!-- row end -->

            <script type="text/javascript">
                $(function(){
                    query();
                });
                function post(url, data, success, error) {
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    var csrfToken = $("meta[name='_csrf']").attr("content");
                    $.ajax({
                        type: 'POST', url: url, data: data, success: success, error: error,
                        headers: {'X-CSRF-TOKEN': csrfToken}
                    });
                };
                function query(){
                    post('feedback/pieGraph','startDate='+$("#startDate").val()+'&endDate='+$("#endDate").val()+ '&distributorId='+$("#distributors").val(),function(result){
                        var chart = new iChart.Donut2D({
                            render : 'pieGraph',
                            title : '各渠道报错情况',
                            center:{
                                text:'',
                                shadow:true,
                                shadow_offsetx:0,
                                shadow_offsety:2,
                                shadow_blur:2,
                                shadow_color:'#b7b7b7',
                                color:'#6f6f6f'
                            },
                            data: result,
                            offsetx:-60,
                            shadow:true,
                            background_color:'#ffffff',
                            separate_angle:0,//分离角度
                            tip:{
                                enable:true,
                                showType:'fixed'
                            },
                            legend : {
                                enable : true,
                                shadow:true,
                                background_color:null,
                                border:false,
                                legend_space:30,//图例间距
                                line_height:34,//设置行高
                                sign_space:10,//小图标与文本间距
                                sign_size:30,//小图标大小
                                color:'#6f6f6f',
                                fontsize:30//文本大小
                            },
                            sub_option:{
                                label : {
                                    background_color:null,
                                    sign:false,//设置禁用label的小图标
                                    padding:'0 4',
                                    border:{
                                        enable:false,
                                        color:'#666666'
                                    },
                                    fontsize:11,
                                    fontweight:600,
                                    color : '#4572a7'
                                },
                                border : {
                                    width : 2,
                                    color : '#ffffff'
                                }
                                /*label:false,
                                 color_factor : 0.3*/
                            },
                            showpercent:true,
                            decimalsnum:2,
                            width : 800,
                            height : 400,
                            radius:140
                        });

                        /**
                         *利用自定义组件构造左侧说明文本。
                         */
                        chart.plugin(new iChart.Custom({
                            drawFn:function(){
                                /**
                                 *计算位置
                                 */
                                var y = chart.get('originy');
                                /**
                                 *在左侧的位置，设置竖排模式渲染文字。
                                 */
                                chart.target.textAlign('center')
                                        .textBaseline('middle')
                                        .textFont('600 24px 微软雅黑')
                                        .fillText('各渠道报错情况',50,y,false,'#6d869f', 'tb',26,false,0,'middle');

                            }
                        }));

                        chart.draw();
                    },function () {
                        alert('请求失败，请检查网络环境');
                    });

                    /** ------------折线图--------------- */
                    post('feedback/lineGraph','startDate='+$("#startDate").val()+'&endDate='+$("#endDate").val()+ '&distributorId='+$("#distributors").val(),function(result){
                        var line = new iChart.LineBasic2D({
                            render : 'lineGraph',
                            /* data: linedata,*/
                            data: result.data,
                            align:'center',
                            title : '主要问题出现情况',
                            subtitle : '每种反馈问题一段时间内的统计数量',
                            footnote : '数据来源：www.oupeng.com',
                            width : 800,
                            height : 400,
                            tip:{
                                enable:true,
                                shadow:true
                            },
                            legend : {
                                enable : true,
                                row:1,//设置在一行上显示，与column配合使用
                                column : 'max',
                                valign:'top',
                                sign:'bar',
                                background_color:null,//设置透明背景
                                offsetx:-80,//设置x轴偏移，满足位置需要
                                border : true
                            },
                            crosshair:{
                                enable:true,
                                line_color:'#62bce9'
                            },
                            sub_option : {
                                label:false,
                                point_hollow : false
                            },
                            coordinate:{
                                width:640,
                                height:240,
                                axis:{
                                    color:'#9f9f9f',
                                    width:[0,0,2,2]
                                },
                                grids:{
                                    vertical:{
                                        way:'share_alike',
                                        value:5
                                    }
                                },
                                scale:[{
                                    position:'left',
                                    start_scale:0,
                                    end_scale:result.max,
                                    scale_space:result.space, // 每隔多少位显示一条数据
                                    scale_size:2,
                                    scale_color:'#9f9f9f'
                                },{
                                    position:'bottom',
                                    labels:result.labels
                                    /*labels:linelabels*/
                                }]
                            }
                        });
                        //开始画图
                        line.draw();
                    },function () {
                        alert('请求失败，请检查网络环境');
                    });
                }
                $('#query').on('click', function(){
                    query();
                });
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
