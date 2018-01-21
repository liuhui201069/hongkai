<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="version.jsp" %>
<%
    Map<String, Map<String,String>> menus = (Map<String, Map<String,String>>)request.getAttribute("account_menus");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>左边导航</title>
    <link rel="stylesheet" type="text/css" href="Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="Assets/css/common.css"/>
    <script type="text/javascript" src="Assets/js/jquery-3.2.1.min.js"></script>
    <!--框架高度设置-->
    <script type="text/javascript">
        $(function () {
            $('.sidenav li').click(function () {
                $(this).siblings('li').removeClass('now');
                $(this).addClass('now');
            });

            $('.erji li').click(function () {
                $(this).siblings('li').removeClass('now_li');
                $(this).addClass('now_li');
            });

            var main_h = $(window).height();
            $('.sidenav').css('height', main_h + 'px');
        })
    </script>
    <!--框架高度设置-->
</head>

<body>
<div id="left_ctn">
    <ul class="sidenav">

            <%int count = 0;for(Map.Entry<String,Map<String,String>> entry : menus.entrySet()){ count++;%>
                <%if(count == 1){%>
                <li class="now">
                        <%}else{%>
                <li>
                    <%}%>
                    <div class="nav_m">
                        <span><a><%=entry.getKey()%></a></span>
                        <i>&nbsp;</i>
                    </div>
                    <ul class="erji">
                <%for(Map.Entry<String,String> en : entry.getValue().entrySet()){%>
                        <li>
                            <span><a href="<%=en.getValue()%>" target="main"><%=en.getKey()%></a></span>
                        </li>
                <%}%>
                    </ul>
                </li>
            <%}%>
        <li>
            <div class="nav_m">
                <span><a href="logout">退出系统</a></span>
            </div>
        </li>
    </ul>
</div>
</body>
</html>
