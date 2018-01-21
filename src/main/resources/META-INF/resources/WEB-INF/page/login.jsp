<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="version.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>登录管理</title>
    <meta name="description" content="登录界面">
    <meta name="keywords" content="登录界面">
    <link href="" rel="stylesheet">
    <style>
        body,p,div,ul,li,h1,h2,h3,h4,h5,h6{
            margin:0;
            padding: 0;
        }
        body{
            background: #E9E9E9;
        }
        #login{
            width: 400px;
            height: 250px;
            background: #FFF;
            margin:200px auto;
            position: relative;
        }
        #login h1{
            text-align:center;
            position:absolute;
            left:95px;
            top:-40px;
            font-size:21px;
        }
        #login form p{
            text-align: center;
        }
        #user{
            background:url(./Assets/images/user.png) rgba(0,0,0,.1) no-repeat;
            width: 200px;
            height: 30px;
            border:solid #ccc 1px;
            border-radius: 3px;
            padding-left: 32px;
            margin-top: 50px;
            margin-bottom: 30px;
        }
        #pwd{
            background: url(./Assets/images/pwd.png) rgba(0,0,0,.1) no-repeat;
            width: 200px;
            height: 30px;
            border:solid #ccc 1px;
            border-radius: 3px;
            padding-left: 32px;
            margin-bottom: 30px;
        }
        #submit{
            width: 232px;
            height: 30px;
            background: rgba(0,0,0,.1);
            border:solid #ccc 1px;
            border-radius: 3px;
        }
        #submit:hover{
            cursor: pointer;
            background:#D8D8D8;
        }
    </style>
</head>
<body>
<div id="login">
    <h1>湖北建强港务有限公司</h1>
    <form action="login" method="post">
        <p><input type="text" name="username" id="user" placeholder="用户名"></p>
        <p><input type="password" name="password" id="pwd" placeholder="密码"></p>
        <p><input type="submit" id="submit" value="登录"></p>
        <% String tips = (String)request.getAttribute("tips");
            if(!StringUtils.isEmpty(tips)){
        %>
        <p><text style="color:red"><%=request.getAttribute("tips")%></text></p>
        <%}%>
    </form>
</div>

</body>
</html>