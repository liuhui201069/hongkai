<%@ page import="java.util.List" %>
<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Account" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<% Result result = (Result)request.getAttribute("result");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>账号列表</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css">
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //自适应屏幕宽度
            window.onresize = function () {
                location = location
            };

            var main_h = $(window).height();
            $('.hy_list').css('height', main_h - 45 + 'px');

            var search_w = $(window).width() - 40;
            $('.search').css('width', search_w + 'px');
            //$('.list_hy').css('width',search_w+'px');

            $("body").on('click',".disable",function(event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                var button = $(this);
                var resultText = button.next();
                var username = button.val();
                $.ajax({
                    type: "POST",
                    url: "/account/disable",
                    data: {username:username},//参数列表
                    success: function(result){
                        //请求正确之后的操作
                        resultText.text(result.message);
                        button.text("启用");
                        button.attr("class","enable");
                    },
                    error: function(result){
                        //请求失败之后的操作
                        resultText.text("发送请求失败");
                    }
                });
            });


            $("body").on('click',".enable",function(event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                var button = $(this);
                var resultText = $(this).next();
                var username = $(this).val();
                $.ajax({
                    type: "POST",
                    url: "/account/enable",
                    data: {username:username},//参数列表
                    success: function(result){
                        //请求正确之后的操作
                        resultText.text(result.message);
                        button.text("禁用");
                        button.attr("class","disable");
                    },
                    error: function(result){
                        //请求失败之后的操作
                        resultText.text("发送请求失败");
                    }
                });
            });
        });
    </script>
    <!--框架高度设置-->
</head>

<%--<body onLoad="Resize();">--%>
<div id="right_ctn">
    <div class="right_m">
        <!--会议列表-->
        <div class="hy_list">
            <div class="box_t">
                <span class="name">账号列表</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">账号管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">账号列表</a>
                </div>
                <!--当前位置-->
            </div>
            <%if (!result.isSuccess()) {%>
            <div><%=result.getMessage()%>
            </div>
            <%} else {%>
            <table cellpadding="0" cellspacing="0" class="list_hy">
                <tr>
                    <th scope="id">账号id</th>
                    <th scope="name">用户名</th>
                    <th scope="create_time">添加时间</th>
                    <th scope="last_time">最后登录时间</th>
                    <th scope="operate">操作</th>
                </tr>
                <%for (Account account : (List<Account>)result.getResult()) {%>
                <tr>
                    <td><%=account.getId()%>
                    </td>
                    <td><%=account.getUsername()%>
                    </td>
                    <td><%=account.getCreateTime()%>
                    </td>
                    <td><%=account.getLastTime()%>
                    </td>
                    <td>
                        <%if(account.getDisable() == 0){%>
                        <button class="disable" value="<%=account.getUsername()%>">禁用</button>
                        <%}else{%>
                        <button class="enable" value="<%=account.getUsername()%>">启用</button>
                        <%}%>
                        <text class="result"></text>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>
        </div>
    </div>
</div>
</body>
</html>
