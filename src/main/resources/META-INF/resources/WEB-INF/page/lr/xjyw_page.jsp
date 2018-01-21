<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Variety" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<%Result varietyResult = (Result)request.getAttribute("varietyResult");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>录入管理</title>
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
                var date = $("#datepicker").val();
                var varietyId = $("#selector").val();
                var num = $("#num").val();
                var price = $("#price").val();
                var carPrice = $("#car_price").val();
                var unit = $("#unit").val();
                var carFee = $("#car_fee").val();
                var total = $("#total").val();
                var carNumber = $("#car_number").val();
                var carOwner = $("#car_owner").val();
                if(customer == ""){
                    $("#add_result").text("客户名称不能为空");
                    return;
                }else if(date == ""){
                    $("#add_result").text("日期不能为空");
                    return;
                }else if(varietyId == ""){
                    $("#add_result").text("品种不能为空");
                    return;
                }else if(num == ""){
                    $("#add_result").text("数量不能为空");
                    return;
                }else if(price == ""){
                    $("#add_result").text("价格不能为空");
                    return;
                }else if(unit == ""){
                    $("#add_result").text("单位不能为空");
                    return;
                }else if(total == ""){
                    $("#add_result").text("总额不能为空");
                    return;
                }

                var obj = {
                    customer:customer,
                    date:date,
                    varietyId:varietyId,
                    num:num,
                    price:price,
                    carPrice:carPrice,
                    unit:unit,
                    carFee:carFee,
                    total:total,
                    carNumber:carNumber,
                    carOwner:carOwner
                };

                $.ajax({
                    type: "POST",
                    url: "/lr/xjyw_add",
                    contentType: "application/json",
                    data: JSON.stringify(obj),//参数列表
                    dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        $("#add_result").text(result.message);
                        $("#num").val("");
                        $("#total").val("");
                        $("#car_fee").val("");
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#pz_name").val("发送请求失败");
                    }
                });
            });


            function computeTotal(){
                var num = parseFloat($("#num").val());
                var price = parseFloat($("#price").val());
                var car_price_text = $("#car_price").val();
                var car_price = 0;
                if( car_price_text != ""){
                    car_price = parseFloat(car_price_text);
                }

                if(!isNaN(num) && !isNaN(car_price)){
                    $("#car_fee_tips").text(Math.round(num * car_price));
                }

                var car_fee_text = $("#car_fee").val();
                var car_fee = 0;
                if( car_fee_text != ""){
                    car_fee = parseFloat(car_fee_text);
                }

                if(!isNaN(num) && !isNaN(price) && !isNaN(car_fee)){
                    $("#total_tips").text(Math.round(num * price + car_fee));
                }
            }

            $(".checkNum").keyup(function(){
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
                computeTotal();
            }).bind("paste",function(){  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
                computeTotal()
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用

            $(".checkTotal").keyup(function(){
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
            }).bind("paste",function(){  //CTR+V事件处理
                $(this).val($(this).val().replace(/[^0-9.]/g,''));
            }).css("ime-mode", "disabled"); //CSS设置输入法不可用

            $(".checkNum").keypress(function(event) {
                var keyCode = event.which;
                if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))
                    return true;
                else
                    return false;
            }).focus(function() {
                this.style.imeMode='disabled';
            });

            $(".checkTotal").keypress(function(event) {
                var keyCode = event.which;
                if (keyCode == 46 || (keyCode >= 48 && keyCode <=57))
                    return true;
                else
                    return false;
            }).focus(function() {
                this.style.imeMode='disabled';
            });

            $("#datepicker").datepicker(
                {
                    changeMonth: true,
                    changeYear: true,
                    regional: "zh-TW",
                    dateFormat:"yy-mm-dd",
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
                <span class="name">录入</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">录入管理</a>
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
                                <li class="now"><span>现金业务</span></li>
                            </ul>
                            <div class="tabCont" style="display:block;">
                                <!--基本配置-->
                                <ul class="hypz">
                                    <li class="clearfix">
                                        <span class="title">客户名称：</span>
                                        <div class="li_r">
                                            <input id="customer" class="chang" name="" type="text" value="现金业务" readonly>
                                            <%--<i>*</i>--%>
                                        </div>
                                        <span id="auto_result"></span>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">日期：</span>
                                        <div class="li_r">
                                            <input id="datepicker" name="" type="text" value="<%=request.getAttribute("today_date")%>">
                                            <%--<i>*</i>--%>
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">品种：</span>
                                        <div class="li_r xial">
                                            <select id="selector">
                                                <option value ="" selected></option>
                                                <%if(varietyResult.isSuccess()){
                                                    List<Variety> varietyList = (List<Variety>)varietyResult.getResult();
                                                    for(Variety variety : varietyList){
                                                %>
                                                <option value="<%=variety.getId()%>"><%=variety.getName()%></option>
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
                                        <span class="title">数量：</span>
                                        <div class="li_r">
                                            <input id="num" name="" type="text" class="checkNum">
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">单价：</span>
                                        <div class="li_r">
                                            <input id="price" name="" type="text" class="checkNum">
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">计量单位：</span>
                                        <div class="li_r">
                                            <input id="unit" name="" type="text" value="吨" readonly>
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">运费单价：</span>
                                        <div class="li_r">
                                            <input id="car_price" name="" type="text" class="checkNum">
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">运费：</span>
                                        <div class="li_r">
                                            <input id="car_fee" name="" type="text" class="checkNum">
                                        </div>
                                        <text id="car_fee_tips" class="result"></text>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">总额：</span>
                                        <div class="li_r">
                                            <input id="total" name="" type="text" class="checkTotal">
                                        </div>
                                        <text id="total_tips" class="result"></text>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">车号：</span>
                                        <div class="li_r">
                                            <input id="car_number" name="" type="text">
                                        </div>
                                    </li>
                                    <li class="clearfix">
                                        <span class="title">承运人：</span>
                                        <div class="li_r">
                                            <input id="car_owner" name="" type="text">
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
