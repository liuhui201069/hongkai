<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Variety" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<%Result varietyResult = (Result)request.getAttribute("result");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>品种编辑</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css"/>
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>

    <script type="text/javascript">
        $(function () {
            //自适应屏幕宽度
            // window.onresize = function () {
            //     location = location
            // };

            var main_h = $(window).height();
            $('.hy_list').css('height', main_h - 45 + 'px');

            var main_w = $(window).width();
            $('.xjhy').css('width', main_w - 40 + 'px');

            //响应回车事件
            $('#new_variery').keydown(function (e) {
                if (e.keyCode == 13) {
                    event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                    $(".lr_edit").click(); //处理事件
                }
            });

            $(".lr_edit").on("click", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                $("#edit_result").text("");
                // alert("提交数据成功");

                var variety = $("#selector").val();
                var newVariety = $("#new_variery").val();

                if(variety == ""){
                    $("#edit_result").text("品种不能为空");
                    return;
                }else if(newVariety == ""){
                    $("#edit_result").text("新名称不能为空");
                    return;
                }else if(variety == newVariety){
                    $("#edit_result").text("新品种名称和旧品种名称相同");
                    return;
                }

                var obj = {
                    variety:variety,
                    newVariety:newVariety
                };

                $.ajax({
                    type: "POST",
                    url: "/pz/edit",
                    // contentType: "application/json",
                    data: obj,//参数列表
                    // dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        $("#edit_result").text(result.message);
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#edit_result").val("发送请求失败");
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
                <span class="name">品种编辑</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">品种管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">品种编辑</a>
                </div>
                <!--当前位置-->
            </div>
            <div class="space_hx">&nbsp;</div>
            <!--新建会议-->
            <form action="" method="post">
                <div class="xjhy" style="padding:0px;">
                    <div class="tabBox_t">
                        <div class="tabBox">
                            <ul class="tabNav">
                                <li class="now"><span>品种编辑</span></li>
                            </ul>
                            <div class="tabCont" style="display:block;">
                                <!--基本配置-->
                                <ul class="hypz">
                                    <li class="clearfix">
                                        <span class="title">品种：</span>
                                        <div class="li_r xial">
                                            <select id="selector">
                                                <option value ="" selected></option>
                                                <%if(varietyResult.isSuccess()){
                                                    List<Variety> varietyList = (List<Variety>)varietyResult.getResult();
                                                    for(Variety variety : varietyList){
                                                %>
                                                <option value="<%=variety.getName()%>"><%=variety.getName()%></option>
                                                <%
                                                    }
                                                }
                                                %>
                                            </select>
                                        </div>
                                        <%if(!varietyResult.isSuccess()){%>
                                            <span style="color:red;text-align: left;" class="title"><%=varietyResult.getMessage()%></span>
                                        <%}%>

                                    </li>
                                    <li class="clearfix">
                                        <span class="title">更名：</span>
                                        <div class="li_r">
                                            <input id="new_variery" name="" type="text">
                                        </div>
                                    </li>
                                    <li class="tj_btn">
                                        <a href="" class="lr_edit">修改</a>
                                        <text id="edit_result" class="result"></text>
                                    </li>
                                </ul>
                                <!--基本配置-->
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <!--新建会议-->
        </div>
        <!--会议列表-->
    </div>
</div>
</body>
</html>
