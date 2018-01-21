<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>个人汇总</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.structure.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.theme.min.css"/>
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="../Assets/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../Assets/js/datepicker-zh-TW.js"></script>

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
            $('#pz_name').keydown(function (e) {
                if (e.keyCode == 13) {
                    event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                    $(".pz_add").click(); //处理事件
                }
            });

            $(".hz_btn").on("click", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                $("#add_result").text("");
                // alert("提交数据成功");
                var customer = $("#customer").val();
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();

                if (customer == "") {
                    $("#add_result").text("客户名称不能为空");
                    return;
                } else if (start_date == "") {
                    $("#add_result").text("开始日期不能为空");
                    return;
                } else if (end_date == "") {
                    $("#add_result").text("结束日期不能为空");
                    return;
                }

                var obj = {
                    name: customer,
                    start_date: start_date,
                    end_date: end_date
                };

                $.ajax({
                    type: "POST",
                    url: "/grhz/sum",
                    // contentType: "application/json",
                    data: obj,//参数列表
                    dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        $("#add_result").text(result.message);
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#pz_name").val("发送请求失败");
                    }
                });
            });

            $(function () {
                $("#customer").autocomplete({
                    minLength: 1,
                    source: function (request, response) {
                        $.ajax({
                            url: "/lr/search_customer",
                            type: "post",
                            dataType: "json",
                            data: {"name": $("#customer").val()},

                            success: function (data) {
                                console.log(data);
                                if (data.success) {
                                    response($.map(data.result, function (item) {
                                        return {
                                            label: item.name,
                                            value: item.id
                                        }
                                    }));
                                } else {
                                    $("#auto_result").text(data.message);
                                }
                            }
                        });
                    },
                    focus: function (event, ui) {
                        console.log(ui.item);
                        $("#customer").val(ui.item.label);
                        return false;
                    },
                    select: function (event, ui) {
                        console.log(ui.item);
                        $("#customer").val(ui.item.label);
                        return false;
                    }
                });
            });


            $("#datepicker_start").datepicker(
                {
                    changeMonth: true,
                    changeYear: true,
                    regional: "zh-TW",
                    dateFormat: "yy-mm-dd",
                }
            );


            $("#datepicker_end").datepicker(
                {
                    changeMonth: true,
                    changeYear: true,
                    regional: "zh-TW",
                    dateFormat: "yy-mm-dd",
                }
            );
        });


    </script>
</head>

<%--<body onLoad="Resize();">--%>
<div id="right_ctn">
    <div class="right_m">
        <!--会议列表-->
        <div class="hy_list">
            <div class="box_t">
                <span class="name">录入</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">个人汇总</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">个人汇总</a>
                </div>
                <!--当前位置-->
            </div>
            <div class="space_hx">&nbsp;</div>
            <!--新建会议-->
            <form action="" method="post">
                <div class="xjhy" style="padding:0px;">
                    <div class="tabBox_t">
                        <div class="tabBox">
                            <div class="tabCont" style="display:block;">
                                <!--基本配置-->
                                <ul class="hypz">
                                    <li class="clearfix" style="height: 40px;border-bottom: 1px solid #DEDEDE;">
                                        <span class="title" style="width:60px;vertical-align: baseline;">客户名称：</span>
                                        <div class="li_r" style="width:100px;margin-left:0px;">
                                            <input id="customer" style="width: 100px;margin-left:0px;" name="" type="text">
                                            <%--<i>*</i>--%>
                                        </div>
                                        <span id="auto_result"></span>
                                        <span class="title" style="width:60px;margin-left:10px;vertical-align: baseline;">开始日期：</span>
                                        <div class="li_r" style="width:100px;margin-left:0px;">
                                            <input id="datepicker_start" name="" type="text" style="width:100px;margin-left:0px;"
                                                   value="<%=request.getAttribute("start_date")%>">
                                            <%--<i>*</i>--%>
                                        </div>
                                        <span class="title" style="width:60px;margin-left:10px;vertical-align: baseline;">截止日期：</span>
                                        <div class="li_r" style="width:100px;margin-left:0px;">
                                            <input id="datepicker_end" name="" type="text" style="width:100px;margin-left:0px;"
                                                   value="<%=request.getAttribute("end_date")%>">
                                            <%--<i>*</i>--%>
                                        </div>
                                        <a href="" class="hz_btn" style="display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">汇总</a>
                                        <text id="add_result" class="result" style="color:red"></text>
                                    </li>
                                    <%--<li class="tj_btn" style="border-top: none;border-bottom: 1px solid #DEDEDE;">--%>
                                        <%--<a href="" class="hz_btn">汇总</a>--%>
                                        <%--<text id="add_result" class="result"></text>--%>
                                    <%--</li>--%>
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
