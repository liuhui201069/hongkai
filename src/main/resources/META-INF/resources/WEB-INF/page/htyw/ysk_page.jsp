<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>合同业务管理</title>
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

            $(".lr_add").on("click", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                $("#add_result").text("");
                // alert("提交数据成功");
                var customer = $("#customer").val();
                var customer_id = $("#customer_id").val();
                //还原customer_id，避免下次重复取
                var type = $("#selector").val();
                var date = $("#datepicker").val();
                var num = $("#num").val();
                var discount = $("#discount").val();

                if (customer == "") {
                    $("#add_result").text("客户名称不能为空");
                    return;
                } else if (type == "") {
                    $("#add_result").text("业务类型不能为空");
                    return;
                } else if (date == "") {
                    $("#add_result").text("日期不能为空");
                    return;
                } else if (num == "" && discount == "") {
                    $("#add_result").text("金额不能为空");
                    return;
                }

                var obj = {
                    customer: customer,
                    customerId: customer_id,
                    type: type,
                    date: date,
                    num: num,
                    discount:discount
                };

                $.ajax({
                    type: "POST",
                    url: "/htyw/ysk_add",
                    contentType: "application/json",
                    data: JSON.stringify(obj),//参数列表
                    dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        $("#add_result").text(result.message);
                        $("#customer_id").val("");
                        $("#customer").val("");
                        $("#num").val("");
                        $("#discount").val("");
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#pz_name").val("发送请求失败");
                    }
                });
            });


            function computeTotal() {
                var num = parseFloat($("#num").val());
                var price = parseFloat($("#price").val());
                var car_fee = parseFloat($("#car_fee").val());
                if (!isNaN(num) && !isNaN(price) && !isNaN(car_fee)) {
                    $("#total").val(Math.round(num * price + car_fee));
                }
            }

            $(".checkNum").keyup(function () {
                $(this).val($(this).val().replace(/[^0-9.]/g, ''));
                computeTotal();
            }).bind("paste", function () {  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9.]/g, ''));
                computeTotal()
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用

            $(".checkTotal").keyup(function () {
                $(this).val($(this).val().replace(/[^0-9.]/g, ''));
            }).bind("paste", function () {  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9.]/g, ''));
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用

            $(".checkInteger").keyup(function () {
                $(this).val($(this).val().replace(/[^\-0-9]/g, ''));
            }).bind("paste", function () {  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^\-0-9]/g, ''));
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用


            $(".checkNum").keypress(function (event) {
                var keyCode = event.which;
                if (keyCode == 46 || (keyCode >= 48 && keyCode <= 57))
                    return true;
                else
                    return false;
            }).focus(function () {
                this.style.imeMode = 'disabled';
            });

            $(".checkTotal").keypress(function (event) {
                var keyCode = event.which;
                if (keyCode == 46 || (keyCode >= 48 && keyCode <= 57))
                    return true;
                else
                    return false;
            }).focus(function () {
                this.style.imeMode = 'disabled';
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
                        $("#customer_id").val(ui.item.value);
                        return false;
                    },
                    select: function (event, ui) {
                        console.log(ui.item);
                        $("#customer").val(ui.item.label);
                        $("#customer_id").val(ui.item.value);
                        return false;
                    }
                });
            });


            $("#datepicker").datepicker(
                {
                    changeMonth: true,
                    changeYear: true,
                    regional: "zh-TW",
                    dateFormat: "yy-mm-dd",
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
                <span class="name">合同业务录入</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">合同业务管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">录入</a>
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
                                <li class="now"><span>付款</span></li>
                            </ul>
                            <div class="tabCont" style="display:block;">
                                <!--基本配置-->
                                <ul class="hypz">
                                    <li class="clearfix">
                                        <span class="title">客户名称：</span>
                                        <div class="li_r">
                                            <input id="customer" class="chang" name="" type="text">
                                            <input id="customer_id" class="chang" name="" type="text" hidden>
                                            <%--<i>*</i>--%>
                                        </div>
                                        <span id="auto_result"></span>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">日期：</span>
                                        <div class="li_r">
                                            <input id="datepicker" name="" type="text"
                                                   value="<%=request.getAttribute("today_date")%>">
                                            <%--<i>*</i>--%>
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">业务类型：</span>
                                        <div class="li_r xial">
                                            <select id="selector">
                                                <option value="2" selected>合同业务</option>
                                            </select>
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">本期付款金额：</span>
                                        <div class="li_r">
                                            <input id="num" name="" type="text" class="checkInteger">
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">优惠金额：</span>
                                        <div class="li_r">
                                            <input id="discount" name="" type="text" class="checkInteger">
                                        </div>
                                    </li>
                                    <li class="tj_btn">
                                        <a href="" class="lr_add">录入</a>
                                        <text id="add_result" class="result"></text>
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
