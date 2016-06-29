<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="navbar navbar-default" role="navigation">
    <div class="navbar-inner">
        <button type="button" class="navbar-toggle pull-left animated flip">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="index.html"> <img alt="Charisma Logo" src="ui/charisma/img/logo20.png" class="hidden-xs" />
            <span>美图</span>
        </a>

        <!-- user dropdown starts -->
        <div class="btn-group pull-right">
            <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                <i class="glyphicon glyphicon-user"></i>
                <span class="hidden-sm hidden-xs"><sec:authentication property="principal.username" /></span>
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li class="divider"></li>
                <li><a href="test/mylogout">Logout</a>
                </li>
            </ul>
        </div>
        <!-- user dropdown ends -->
    </div>
</div>