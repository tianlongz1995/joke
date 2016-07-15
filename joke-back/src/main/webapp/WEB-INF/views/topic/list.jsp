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
	<title>专题管理</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Charisma, a fully featured, responsive, HTML5, Bootstrap admin template.">
	<meta name="author" content="Muhammad Usman">
	<!-- default header name is X-CSRF-TOKEN -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>

	<base href="<%=basePath%>">
	<%@ include file="../common/css.html"%>
	<script src="ui/charisma/bower_components/jquery/jquery.min.js"></script>
	<script src="/ui/js/date/WdatePicker.js"></script>
	<script src="ui/js/jquery.oupeng.upload.js"></script>

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
		<li><a href="channel/list">专题管理</a></li>
	</ul>
</div>

<div class="row">
<div class="box col-md-12">

<div class="box-inner">
	<div class="box-header well" data-original-title="">
		<h2><i class="glyphicon glyphicon-user"></i> 专题列表</h2>
	</div>
	<div class="box-content">
		<div class="alert alert-info">
			需要添加新的专题点击: 
					<a href="#" data-toggle="modal" data-target="#newTopic">新增专题</a>
		</div>
		<table id="table_list" class="table table-striped table-bordered bootstrap-datatable responsive">
			<div class="dataTables_filter" id="DataTables_Table_0_filter">
				<label style="padding-right:30px;">
					<span >状态</span>
					<select id="status">
						<option value="">全部</option>
						<option value="0" <c:if test="${!empty status && status == 0}">selected</c:if> >新建</option>
						<option value="1" <c:if test="${!empty status && status == 1}">selected</c:if> >下线</option>
						<option value="2" <c:if test="${!empty status && status == 2}">selected</c:if> >上线</option>
						<option value="3" <c:if test="${!empty status && status == 3}">selected</c:if> >已发布</option>
					</select>
				</label>
				<label style="padding-right:30px;">
					<a class="btn btn-primary" href="#" id="selectTopicList">
						<span class="glyphicon glyphicon-search icon-white" >查询</span>
					</a>
				</label>
			</div>
		
			<thead>
				<tr>
					<th>期数</th>
					<th>主题</th>
					<th>发布渠道</th>
					<th>状态</th>
					<th>创建时间</th>
					<th>发布时间</th>
					<th>操作</th>
				</tr>
			</thead>
	
			<tbody>
				<c:forEach items="${list}" var="topic">
					<tr>
						<td>[<c:out value="${topic.id}"/>]</td>
						<td><c:out value="${topic.title}"/></td>
						<td>
							<c:if test="${!empty topic.dids}">
								<c:forTokens items="${topic.dids}" delims="," var="did">
									<c:forEach items="${distributorList}" var="distributor">
										<c:if test="${distributor.id == did}">${distributor.name}<br/></c:if>
									</c:forEach>
								</c:forTokens>
							</c:if>
						</td>
						<td>
							<c:if test="${topic.status == 0}">新建</c:if>
							<c:if test="${topic.status == 1}">下线</c:if>
							<c:if test="${topic.status == 2}">上线</c:if>
							<c:if test="${topic.status == 3}">已发布</c:if>
						</td>
						<td>
							<fmt:formatDate value="${topic.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<fmt:formatDate value="${topic.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
						<td>
							<c:if test="${topic.status == 0}">
								<a class="btn btn-info" href="topic/edit?id=${topic.id}">
						        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
						        </a>
						        <a class="btn btn-warning" href="topic/joke?topicId=${topic.id}">
						        	<i class="glyphicon glyphicon-arrow-right"></i>添加内容
						        </a>
								<a class="btn btn-danger" href="#" onclick="verifyTopic(1,${topic.id})">
					        	 	<i class="glyphicon glyphicon-remove icon-white"></i>下线
					        	</a>
					        	<a class="btn btn-success" href="#" onclick="verifyTopic(2,${topic.id})">
						       		<i class="glyphicon glyphicon-ok icon-white"></i>上线
						        </a>
							</c:if>
							<c:if test="${topic.status == 1}">
								<a class="btn btn-info" href="topic/edit?id=${topic.id}">
						        	<i class="glyphicon glyphicon-edit icon-white"></i>编辑
						        </a>
						        <a class="btn btn-warning" href="topic/joke?topicId=${topic.id}">
						        	<i class="glyphicon glyphicon-arrow-right"></i>添加内容
						        </a>
								<a class="btn btn-success" href="#" onclick="verifyTopic(2,${topic.id})">
						       		<i class="glyphicon glyphicon-ok icon-white"></i>上线
						        </a>
							</c:if>
							<c:if test="${topic.status == 2 || topic.status == 3}">
								<a class="btn btn-danger" href="#" onclick="verifyTopic(1,${topic.id})">
						        	 <i class="glyphicon glyphicon-remove icon-white"></i>下线
						        </a>
							</c:if>
					    </td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
</div>
</div><!-- box col-md-12 end -->
</div><!-- row end -->

<div class="modal fade" id="newTopic" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title" id="myModalLabel">新建专题</h4>
			</div>
			<div class="modal-body">
				<table id="orders-table" class="table table-hover">
					<tr>
						<th>发布时间</th>
						<td>
							<input id="addPublishTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:00:00'})" class="Wdate" value=""/>
						</td>
					</tr>
					<tr>
						<th>发布渠道</th>
						<td>
							<input type="checkbox" id="allcheck" >全选</input>
							<c:forEach items="${distributorList}" var="distributor">
								<input type="checkbox" name="distributorId" value="${distributor.id}" >${distributor.name}</input>
							</c:forEach>
						</td>
					</tr>
					<tr>
						<th>主题</th>
						<td>
							<input id="addTitle" type="text" maxlength="30" style="width:500px;" value=""/>
						</td>
					</tr>
					<tr>
						<th>图片</th>
						<td>
				  			<input id="img" name ="img" type="file" accept=".jpg,.jpeg,.png"/>
				  			<input id="image" type="hidden"/>
				  			<img id="imgPriview" style="display: none;width:500px;height:300px;" src=""/>
				  			<input id="imgDelButton" type="button" class="btn btn-default" style="display: none" value="删除" />
				  		</td>
					</tr>
					<tr>
						<th>简介</th>
						<td>
				  			<textarea id="addContent" type="text" class="form-control" ROWS="10"  COLS="10"></textarea>
				  		</td>
					</tr>
		  		</table>
			</div>
			<div class="modal-footer">
				<button id="addNewTopic" type="button" class="btn btn-default" data-dismiss="modal">提交</button>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$('#allcheck').on('click', function(){
	if ($(this).prop("checked")) {
        $(":checkbox").prop("checked", true);
    }else {
        $(":checkbox").prop("checked", false);
    }
});


$('#addNewTopic').click(function(event) {
	var dids = [];
	$('input[name="distributorId"]:checked').each(function(){
		dids.push($(this).val());
		});
	
	post('topic/add',
		'publishTime='+$("#addPublishTime").val()+'&title='+$('#addTitle').val()+'&img='+$('#image').val()+'&content='
			+$('#addContent').val()+'&dids='+dids.toString(), 
		function (data) {
			if(data['status']) {
				location.href = '<%=basePath%>topic/list?status='+$("#status").val();
			}else {
				alert('添加专题失败. info:'+data['info']);
			}
		},
		function () {
			alert('请求失败，请检查网络环境');
		});
});

function verifyTopic(status,id) {
	post('topic/verify',
			'id='+id+'&status='+status, 
			function (data) {
				if(data['status']) {
					location.href = '<%=basePath%>topic/list?status='+$("#status").val();
				}else {
					alert('操作失败. info:'+data['info']);
				}
			},
			function () {
				alert('请求失败，请检查网络环境');
			});
}

$('#selectTopicList').click(function(event) {
	location.href = '<%=basePath%>topic/list?status='+$("#status").val();
});

$('#imgDelButton').click(function () {
	$('#img').val('');
	$('#image').val('');
	$("#imgPriview").hide();
});

$('#img').change(function () {
    var file = $(this)[0].files[0];
    $(this).OupengUpload(file, {
        url: 'upload/img?${_csrf.parameterName}=${_csrf.token}',
        acceptFileTypes: 'image/*',
        maxFileSize: 1024*1024*5,
        minFileSize: 0,
        onUploadSuccess: function (data) {
        	$("#image").val(data);
        	$("#imgPriview").attr('src',data).show();
        	$("#imgDelButton").show();
        },
        onUploadError: function (data) {
            alert(data);
        }
    });
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

</div><!-- content end -->
</div><!-- row end -->
</div><!-- ch-container end -->

<hr>
<%@ include file="../common/footer.html"%>
<%@ include file="../common/js.html"%>
</body>
</html>
