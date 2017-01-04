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
                <li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-link"></i><span> 内容源管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="source/list"><span> 内容源列表</span></a></li>
						<li><a href="source/crawl?sourceType=1"><span> 抓取统计</span></a></li>
						<li><a href="source/quality?sourceType=1"><span> 审核质量统计</span></a></li>
					</ul>
                </li>
				<li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-link"></i><span> 资源管理</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="resource/index"><span> 首页升级</span></a></li>
						<li><a href="resource/banner"><span> 横幅管理</span></a></li>
						<li><a href="resource/select"><span> 精选管理</span></a></li>
					</ul>
				</li>
                <li>
                	<a class="ajax-link" href="feedback/monitor">
                		<i class="glyphicon glyphicon-envelope"></i>
                		<span> 用户反馈</span>
                	</a>
                </li>
                <li class="accordion">
					<a href="#"><i class="glyphicon glyphicon-plus"></i><span> 数据统计</span></a>
					<ul class="nav nav-pills nav-stacked">
						<li><a href="statistics/dayTotal"><span> 总日报</span></a></li>
						<li><a href="statistics/dayDetail"><span> 渠道日报</span></a></li>
						<li><a href="statistics/weekTotal"><span> 总周报</span></a></li>
						<li><a href="statistics/weekDetail"><span> 渠道周报</span></a></li>
						<li><a href="statistics/monthTotal"><span> 总月报</span></a></li>
						<li><a href="statistics/monthDetail"><span> 渠道月报</span></a></li>
						<!--	新增下拉刷新统计	-->
						<li><a href="statistics/dropTotal"><span> 刷新统计</span></a></li>
						<li><a href="statistics/dropDetail"><span> 刷新明细统计</span></a></li>
						<!--	列表页长图展开点击统计	-->
						<%--<li><a href="statistics/usage"><span> 长图展开统计</span></a></li>--%>
						<!-- 使用统计 -->
						<li><a href="statistics/usage"><span> 使用统计</span></a></li>
					</ul>
				</li>
			</ul>
			<label id="for-is-ajax" for="is-ajax" style="display: none;"><input id="is-ajax" type="checkbox"> Ajax on menu</label>
        </div>
    </div>
</div>