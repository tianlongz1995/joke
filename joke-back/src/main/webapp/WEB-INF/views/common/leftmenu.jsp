<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="col-sm-2 col-lg-2">
    <div class="sidebar-nav">
        <div class="nav-canvas">
            <ul class="nav nav-pills nav-stacked main-menu">
                <li class="nav-header">Main</li>
                <li>
                	<a class="ajax-link" href="home">
                		<i class="glyphicon glyphicon-home"></i>
                		<span> 首页</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="user">
                		<i class="glyphicon glyphicon-user"></i>
                		<span> 用户管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="joke/search">
                		<i class="glyphicon glyphicon-search"></i>
                		<span> 快捷查找</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="joke/list?status=0">
                		<i class="glyphicon glyphicon-list-alt"></i>
                		<span> 内容审核</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="distributor/list?status=1">
                		<i class="glyphicon glyphicon-road"></i>
                		<span> 渠道管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="channel/list">
                		<i class="glyphicon glyphicon-th-large"></i>
                		<span> 频道管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="topic/list">
                		<i class="glyphicon glyphicon-th"></i>
                		<span> 专题管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="ad/list">
                		<i class="glyphicon glyphicon-picture"></i>
                		<span> 广告管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="source/list">
                		<i class="glyphicon glyphicon-signal"></i>
                		<span> 内容源管理</span>
                	</a>
                </li>
                <li>
                	<a class="ajax-link" href="feedback/monitor">
                		<i class="glyphicon glyphicon-envelope"></i>
                		<span> 用户反馈</span>
                	</a>
                </li>
			</ul>
			<label id="for-is-ajax" for="is-ajax" style="display: none;"><input id="is-ajax" type="checkbox"> Ajax on menu</label>
        </div>
    </div>
</div>