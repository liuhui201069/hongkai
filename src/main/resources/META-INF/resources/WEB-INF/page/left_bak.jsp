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
        <li class="now">
            <div class="nav_m">
                <span><a>品种管理</a></span>
                <i>&nbsp;</i>
            </div>
            <%--<%}%>--%>
            <ul class="erji">
                <li>
                    <span><a href="/pz/list_page" target="main">品种列表</a></span>
                </li>
                <li>
                    <span><a href="/pz/add_page" target="main">品种添加</a></span>
                </li>
                <li>
                    <span><a href="/pz/edit_page" target="main">品种编辑</a></span>
                </li>
                <%--<li>--%>
                <%--<span><a href="hymb.html" target="main">终端召集会议模板</a></span>--%>
                <%--</li>--%>
                <%--<li>--%>
                <%--<span><a href="cchy.html" target="main">已经存储的会议</a></span>--%>
                <%--</li>--%>
            </ul>
        </li>
        <li>
            <div class="nav_m">
                <span><a>现金业务管理</a></span>
                <i>&nbsp;</i>
            </div>
            <ul class="erji">
                <%--<li>--%>
                <%--<span><a href="/lr/luru_page" target="main">录入</a></span>--%>
                <%--</li>--%>
                <li>
                    <span><a href="/xjyw/lr_page" target="main">录入</a></span>
                </li>
                <li>
                    <span><a href="/xjyw/mx_page" target="main">明细</a></span>
                </li>
                <li>
                    <span><a href="/xjyw/hz_page" target="main">汇总</a></span>
                </li>
            </ul>
        </li>
        <li>
            <div class="nav_m">
                <span><a>合同业务管理</a></span>
                <i>&nbsp;</i>
            </div>
            <ul class="erji">
                <%--<li>--%>
                <%--<span><a href="/lr/luru_page" target="main">录入</a></span>--%>
                <%--</li>--%>
                <li>
                    <span><a href="/htyw/lr_page" target="main">录入</a></span>
                </li>
                <li>
                    <span><a href="/htyw/mx_page" target="main">明细</a></span>
                </li>
                <li>
                    <span><a href="/htyw/grhz_page" target="main">个人汇总</a></span>
                </li>
                <li>
                    <span><a href="/htyw/khpzhz_page" target="main">分客户分品种汇总</a></span>
                </li>
                <li>
                    <span><a href="/htyw/pzhz_page" target="main">分品种汇总</a></span>
                </li>
                <li>
                    <span><a href="/htyw/ysk_page" target="main">应收款</a></span>
                </li>
            </ul>
        </li>
        <li>
            <div class="nav_m">
                <span><a>装卸业务管理</a></span>
                <i>&nbsp;</i>
            </div>
            <ul class="erji">
                <%--<li>--%>
                <%--<span><a href="/lr/luru_page" target="main">录入</a></span>--%>
                <%--</li>--%>
                <li>
                    <span><a href="/zxyw/lr_page" target="main">录入</a></span>
                </li>
                <li>
                    <span><a href="/zxyw/mx_page" target="main">明细</a></span>
                </li>
                <li>
                    <span><a href="/zxyw/grhz_page" target="main">个人汇总</a></span>
                </li>
                <%--<li>--%>
                <%--<span><a href="/zxyw/khpzhz_page" target="main">分客户分品种汇总</a></span>--%>
                <%--</li>--%>
                <li>
                    <span><a href="/zxyw/pzhz_page" target="main">分品种汇总</a></span>
                </li>
                <li>
                    <span><a href="/zxyw/ysk_page" target="main">应收款</a></span>
                </li>
            </ul>
        </li>

        <%--<li>--%>
        <%--<div class="nav_m">--%>
        <%--<span><a>录入管理</a></span>--%>
        <%--<i>&nbsp;</i>--%>
        <%--</div>--%>
        <%--<ul class="erji">--%>
        <%--&lt;%&ndash;<li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<span><a href="/lr/luru_page" target="main">录入</a></span>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
        <%--<li>--%>
        <%--<span><a href="/lr/xjyw_page" target="main">现金业务</a></span>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<span><a href="/lr/htyw_page" target="main">合同业务</a></span>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<span><a href="/lr/zxyw_page" target="main">装卸业务</a></span>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<span><a href="/lr/yskgl_page" target="main">应收款管理</a></span>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<div class="nav_m">--%>
        <%--<span><a>汇总管理</a></span>--%>
        <%--<i>&nbsp;</i>--%>
        <%--</div>--%>
        <%--<ul class="erji">--%>
        <%--<li>--%>
        <%--<span><a href="/grhz/all_customer" target="main">个人汇总</a></span>--%>
        <%--</li>--%>
        <%--&lt;%&ndash;<li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<span><a href="/grhz/mx_page" target="main">个人明细</a></span>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<li>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<span><a href="/grhz/hz_page" target="main">按品种汇总</a></span>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
        <%--<li>--%>
        <%--<span><a href="/qbhz/hz_kh_pz_page" target="main">各客户各品种汇总</a></span>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<span><a href="/qbhz/hz_pz_page" target="main">全部汇总</a></span>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <%--<li>--%>
        <%--<div class="nav_m">--%>
        <%--<span><a>全部汇总</a></span>--%>
        <%--<i>&nbsp;</i>--%>
        <%--</div>--%>
        <%--<ul class="erji">--%>
        <%--<li>--%>
        <%--<span><a href="/qbhz/hz_kh_pz_page" target="main">各客户各品种汇总</a></span>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--<ul class="erji">--%>
        <%--<li>--%>
        <%--<span><a href="/qbhz/hz_pz_page" target="main">各品种汇总</a></span>--%>
        <%--</li>--%>
        <%--</ul>--%>
        <%--</li>--%>
        <li>
            <div class="nav_m">
                <span><a>查询管理</a></span>
                <i>&nbsp;</i>
            </div>
            <ul class="erji">
                <li>
                    <span><a href="/cx/xjyw_page" target="main">现金业务</a></span>
                </li>
                <li>
                    <span><a href="/cx/htyw_page" target="main">合同业务</a></span>
                </li>
                <li>
                    <span><a href="/cx/zxyw_page" target="main">装卸业务</a></span>
                </li>
            </ul>
        </li>
        <li>
            <div class="nav_m">
                <span><a>账号管理</a></span>
                <i>&nbsp;</i>
            </div>
            <ul class="erji">
                <li>
                    <span><a href="/account/list_page" target="main">账号列表</a></span>
                </li>
                <li>
                    <span><a href="/account/add_page" target="main">添加账号</a></span>
                </li>
                <li>
                    <span><a href="/account/right_page" target="main">权限管理</a></span>
                </li>
            </ul>
        </li>
        <li>
            <div class="nav_m">
                <span><a href="logout">退出系统</a></span>
            </div>
        </li>
    </ul>
</div>
</body>
</html>
