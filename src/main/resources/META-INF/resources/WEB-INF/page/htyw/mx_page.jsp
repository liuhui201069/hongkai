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
    <title>合同业务明细</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.structure.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.theme.min.css"/>
    <link rel="stylesheet" href="../Assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../Assets/css/bootstrap-table.min.css"/>
    <style>
        *{padding:0px;margin:0px;}
        .pop {  display: none;  width: 600px; min-height: 670px;  max-height: 850px;  height:670px;  position: absolute;  top: 0;  left: 0;  bottom: 0;  right: 0;  margin: auto;  padding: 5px;  z-index: 130;  border-radius: 8px;  background-color: #fff;  box-shadow: 0 3px 18px rgba(100, 0, 0, .5);  }
        .pop-top{ width:100%;  border-bottom: 1px #E5E5E5 solid; padding-left: 10px; padding-bottom: 10px;  }
        .pop-top h2{  float: left;  display:black}
        .pop-top span{  float: right;  cursor: pointer;  font-weight: bold; display:black}
        .pop-foot{  height:50px;  line-height:50px;  width:100%;  border-top: 1px #E5E5E5 solid;  text-align: right;  }
        .pop-cancel, .pop-ok {  padding:8px 15px;  margin:15px 5px;  border: none;  border-radius: 5px;  background-color: #337AB7;  color: #fff;  cursor:pointer;  }
        .pop-cancel {  background-color: #FFF;  border:1px #CECECE solid;  color: #000;  }
        .pop-content{  height: 380px;  }
        .pop-content-left{  float: left;  }
        .pop-content-right{  width:310px;  float: left;  padding-top:20px;  padding-left:20px;  font-size: 16px;  line-height:35px;  }
        .bgPop{  display: none;  position: absolute;  z-index: 129;  left: 0;  top: 0;  width: 100%;  height: 850px;  background: rgba(0,0,0,.2);  }
    </style>
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="../Assets/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../Assets/js/datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="../Assets/js/bootstrap-table.min.js"></script>
    <script type="text/javascript" src="../Assets/js/bootstrap-table-locale-all.min.js"></script>

    <script type="text/javascript">
        var datas,edit_td,edit_idx;
        $(function () {
            //自适应屏幕宽度
            // window.onresize = function () {
            //     location = location
            // };

            var main_h = $(window).height();
            // $('.hy_list').css('height', main_h - 45 + 'px');

            var main_w = $(window).width();
            $('.xjhy').css('width', main_w - 40 + 'px');

            //响应回车事件
            $('#pz_name').keydown(function (e) {
                if (e.keyCode == 13) {
                    event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                    $(".pz_add").click(); //处理事件
                }
            });

            function hidePop(){
                $('.bgPop,.pop').hide();
            }

            function showPop(){
                var _scrollHeight = $(document).scrollTop();//获取当前窗口距离页面顶部高度
                var _windowHeight = $(window).height();//获取当前窗口高度
                var _popupHeight = $('.pop').height();//获取弹出层高度
                var _posiTop = (_windowHeight-_popupHeight)/2 + _scrollHeight;
                $('.pop').css("top",_posiTop + "px");//设置position
                $('.bgPop,.pop').show();
            }

            function operateFormatter() {
                return [
                    '<a href="" class="edit_btn">编辑</a>'
                ].join('');
            }
            $("body").on('click',".gb_btn", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                hidePop();
            });

            $("body").on('click',".edit_btn", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                var td = $(this).parents("tr").find("td");
                edit_td = td;
                var record_id = td.eq(0).text();
                for(var idx in datas){
                    var data = datas[idx];
                    if(data.id == parseInt(record_id)){
                        edit_idx = idx;
                        $("#record_id").val(record_id);
                        $("#customer_id").val(data.customerId);
                        $("#customer").val(data.customer);
                        $("#selector").val(data.varietyId);
                        $("#num").val(data.num);
                        $("#price").val(data.price);
                        $("#unit").val(data.unit);
                        $("#car_price").val(data.carPrice);
                        $("#car_fee").val(data.carFee);
                        $("#total").val(data.total);
                        $("#car_number").val(data.carNumber);
                        $("#car_owner").val(data.carOwner);
                        $("#datepicker").val(data.date);
                    }
                }
                showPop();
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

            $("body").on('click',".edit_ok_btn", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                var record_id = $("#record_id").val();
                var customer = $("#customer").val();
                var customer_id = $("#customer_id").val();
                //还原customer_id，避免下次重复取
                $("#customer_id").val("");
                var date = $("#datepicker").val();
                var variety_id = $("#selector").val();
                var variety = $("#selector").find("option:selected").text();
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
                }else if(variety_id == ""){
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
                    id:record_id,
                    customerId:customer_id,
                    customer:customer,
                    date:date,
                    varietyId:variety_id,
                    variety:variety,
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
                    url: "/record/edit",
                    contentType: "application/json",
                    data: JSON.stringify(obj),//参数列表
                    dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        $("#add_result").text(result.message);
                        if (result.success) {
                            var data = result.result;
                            alert("修改成功");
                            edit_td.eq(1).text(data.customer);
                            edit_td.eq(2).text(data.variety);
                            edit_td.eq(3).text(data.num);
                            edit_td.eq(4).text(data.price);
                            edit_td.eq(5).text(data.unit);
                            edit_td.eq(6).text(data.carPrice);
                            edit_td.eq(7).text(data.carFee);
                            edit_td.eq(8).text(data.total);
                            edit_td.eq(9).text(data.carNumber);
                            edit_td.eq(10).text(data.carOwner);
                            edit_td.eq(11).text(data.date);
                            datas[edit_idx] = data;
                        } else {
                            alert(result.message);
                        }
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#pz_name").val("发送请求失败");
                    }
                });
            });

            $(".hz_btn").on("click", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                $("#add_result").text("");
                // alert("提交数据成功");
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();

                if (start_date == "") {
                    $("#add_result").text("开始日期不能为空");
                    return;
                } else if (end_date == "") {
                    $("#add_result").text("结束日期不能为空");
                    return;
                }

                var obj = {
                    start_date: start_date,
                    end_date: end_date
                };

                $.ajax({
                    type: "POST",
                    url: "/htyw/detail",
                    // contentType: "application/json",
                    data: obj,//参数列表
                    dataType: "json",
                    success: function (result) {
                        datas = result.result;
                        //请求正确之后的操作
                        $("#add_result").text(result.message);
                        console.log(result.result);
                        $('#detail_table').bootstrapTable('destroy');
                        $('#detail_table').bootstrapTable({
                            data: result.result,
                            toolbar: '#toolbar',                //工具按钮用哪个容器
                            striped: true,                      //是否显示行间隔色
                            sortable: true,                     //是否启用排序
                            sortOrder: "asc",                   //排序方式
                            columns: [{
                                field: 'id',
                                title: 'ID',
                                sortable:true
                            },{
                                field: 'customer',
                                title: '客户名称',
                                sortable:true
                            },{
                                field: 'variety',
                                title: '品种',
                                sortable:true
                            }, {
                                field: 'num',
                                title: '数量',
                                sortable:true
                            }, {
                                field: 'price',
                                title: '单价',
                                sortable:true
                            }, {
                                field: 'unit',
                                title: '单位'
                            }, {
                                field: 'carPrice',
                                title: '运费单价',
                                sortable:true
                            }, {
                                field: 'carFee',
                                title: '运费',
                                sortable:true
                            },  {
                                field: 'total',
                                title: '总额',
                                sortable:true
                            },  {
                                field: 'carNumber',
                                title: '车号'
                            }, {
                                field: 'carOwner',
                                title: '承运人'
                            },{
                                field: 'date',
                                title: '时间',
                                sortable:true
                            }, {
                                field: 'operate',
                                title: '操作',
                                formatter: operateFormatter
                            }]
                        });
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

            function excelDownLoad(action){
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();
                var form = $("<form>");
                form.attr('style', 'display:none');
                form.attr('target', '');
                form.attr('method', 'post');
                form.attr('action', action);
                var input2 = $('<input>');
                input2.attr('type', 'hidden');
                input2.attr('name', 'start_date');
                input2.attr('value', start_date);
                var input3 = $('<input>');
                input3.attr('type', 'hidden');
                input3.attr('name', 'end_date');
                input3.attr('value', end_date);
                $('body').append(form);
                form.append(input2);
                form.append(input3);
                form.submit();
                form.remove();
            }

            $(".dc_btn").on("click",function(event){
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                excelDownLoad("/htyw/detail_excel");
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
                <span class="name">合同业务明细</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">合同业务管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">明细</a>
                </div>
                <!--当前位置-->
            </div>
            <div class="space_hx">&nbsp;</div>
            <!--新建会议-->
            <div class="tabCont" style="display:block;">
                <!--基本配置-->
                <ul class="hypz">
                    <li class="clearfix" style="height: 40px;border-bottom: 1px solid #DEDEDE;">
                        <span style="margin-left:10px;vertical-align: baseline;">开始日期：</span>
                        <div class="li_r" style="width:100px;margin-left:0px;">
                            <input id="datepicker_start" name="" type="text" style="width:100px;margin-left:0px;"
                                   value="<%=request.getAttribute("start_date")%>">
                            <%--<i>*</i>--%>
                        </div>
                        <span style="margin-left:10px;vertical-align: baseline;">截止日期：</span>
                        <div class="li_r" style="width:100px;margin-left:0px;">
                            <input id="datepicker_end" name="" type="text" style="width:100px;margin-left:0px;"
                                   value="<%=request.getAttribute("end_date")%>">
                            <%--<i>*</i>--%>
                        </div>
                        <a href="" class="hz_btn" style="text-decoration: none;display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">查询</a>
                        <a href="" class="dc_btn" style="text-decoration: none;display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">导出</a>
                        <text id="add_result" class="result" style="color:red"></text>
                    </li>
                    <%--<li class="tj_btn" style="border-top: none;border-bottom: 1px solid #DEDEDE;">--%>
                    <%--<a href="" class="hz_btn">汇总</a>--%>
                    <%--<text id="add_result" class="result"></text>--%>
                    <%--</li>--%>
                </ul>
                <!--基本配置-->
            </div>
            <!--新建会议-->
        </div>
        <!--会议列表-->
        <div id="detail_div" style="padding-left: 20px;padding-right: 20px;padding-top: 10px;background: white;">
            <table id="detail_table">
                <thead>
                </thead>
            </table>
        </div>
    </div>
</div>

<div class="bgPop"></div>
<!--弹出框-->
<div class="pop">
<div class="pop-content">
    <div class="tabBox_t">
        <div class="tabBox">
            <ul class="tabNav">
                <li class="now"><span>编辑</span></li>
            </ul>
            <div class="tabCont" style="display:block;">
                <!--基本配置-->
                <ul class="hypz">
                    <li class="clearfix">
                        <span class="title">客户名称：</span>
                        <div class="li_r">
                            <input id="customer" class="chang" name="" type="text">
                            <input id="customer_id" class="chang" name="" type="text" hidden>
                            <input id="record_id" class="chang" name="" type="text" hidden>
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
                        <a href="" class="edit_ok_btn">确定</a>
                        <a href="" class="gb_btn">关闭</a>
                        <text id="add_result" class="result"></text>
                    </li>
                </ul>
                <!--基本配置-->
            </div>
        </div>
    </div>
</div>
</div>
</body>
</html>
