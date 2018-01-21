<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" >
<title>账号管理</title>
<link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
<link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
<link rel="stylesheet" type="text/css" href="../Assets/css/thems.css">
<script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript">
$(function(){
	//自适应屏幕宽度
	window.onresize=function(){ location=location };

	var main_h = $(window).height();
	$('.hy_list').css('height',main_h-45+'px');

	var main_w = $(window).width();
	$('.xjhy').css('width',main_w-40+'px');

	//响应回车事件
    $('#pz_name').keydown(function(e){
        if(e.keyCode==13){
            event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
            $(".pz_add").click(); //处理事件
        }
    });

    $(".pz_add").on("click",function(event){
        event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
        $("#add_result").text("");
        // alert("提交数据成功");
        var username = $("#username").val();
        var password = $("#password").val();
        var password2 = $("#password2").val();
        if(password != password2){
            $("#add_result").text("两次输入密码不一致");
            return;
        }

        var obj = {
            username : username,
            password : password
        };

        $.ajax({
            type: "POST",
            url: "/account/add",
            contentType:"application/json",
            data: JSON.stringify(obj),//参数列表
            dataType:"json",
            success: function(result){
                //请求正确之后的操作
                $("#add_result").text(result.message);
            },
            error: function(result){
                //请求失败之后的操作
                $("#pz_name").val("发送请求失败");
            }
        });
    });

});
</script>
</head>

<%--<body onLoad="Resize();">--%>
<div id="right_ctn">
	<div class="right_m">
		<!--会议列表-->
        <div class="hy_list">
        	<div class="box_t">
            	<span class="name"></span>
                <!--当前位置-->
                <div class="position">
                	<a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">账号管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">添加账号</a>
                </div>
                <!--当前位置-->
            </div>
            <div class="space_hx">&nbsp;</div>
            <!--终端列表-->
            <form action="" method="post">
            <div class="xjhy">
            <!--基本配置-->
                <ul class="hypz">
                    <li class="clearfix">
                        <span class="title">用户名：</span>
                        <div class="li_r">
                            <input  name="" type="text" id="username">
                            <%--<i>*</i>--%>
                        </div>
                    </li>
                    <li class="clearfix">
                        <span class="title">密码：</span>
                        <div class="li_r">
                            <input type="password" name="" id="password">
                            <%--<i>*</i>--%>
                        </div>
                    </li>
                    <li class="clearfix">
                        <span class="title">再次确认密码：</span>
                        <div class="li_r">
                            <input type="password" name="" id="password2">
                            <%--<i>*</i>--%>
                        </div>
                    </li>
                    <li class="tj_btn">
                        <a href="" class="pz_add">添加</a>
                        <text id="add_result" class="result"></text>
                    </li>
                </ul>
            <!--基本配置-->
            </div>
            </form>
            <!--终端列表-->
        </div>
        <!--会议列表-->
    </div>
</div>
</body>
</html>
