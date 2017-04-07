<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>笑料百出</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="renderer" content="webkit">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-title" content="笑料百出">
    <meta http-equiv="Cache-Control" content="no-siteapp">
    <meta name="HandheldFriendly" content="true">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="format-detection" content="telephone=no">
    <script>
        var navInfo = ${config};
        var CLIENTTIME = new Date();
        var SERVERTIME = new Date(${systemUtc});
    </script>
    <link href="http://s.opfed.com/joke_new/prd/${index.appCss}" rel='stylesheet' type='text/css'>
</head>
<body>
<div id="app"></div>
<script src="http://s.opfed.com/lib/zepto/1.2.0/zepto.min.js"></script>
<script src="http://s.opfed.com/joke_new/prd/${index.libJs}"></script>
<script src="http://s.opfed.com/joke_new/prd/${index.appJs}"></script>
<div style="display: none;">
    <script type="text/javascript">
        var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
        document.write(unescape("%3Cspan id='cnzz_stat_icon_1261515641'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1261515641' type='text/javascript'%3E%3C/script%3E"));</script>
</div>
</body>
</html>