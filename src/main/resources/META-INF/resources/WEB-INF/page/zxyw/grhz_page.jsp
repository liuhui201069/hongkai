<%@ page import="java.util.List" %>
<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Variety" %>
<%@ page import="util.DateUtil" %>
<%@ page import="com.hongkai.domain.Customer" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<% Result result = (Result)request.getAttribute("result");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>装卸业务个人汇总</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css">
    <link rel="stylesheet" href="../Assets/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.structure.min.css"/>
    <link rel="stylesheet" href="../Assets/css/jquery-ui.theme.min.css"/>
    <link rel="stylesheet" href="../Assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../Assets/css/bootstrap-table.min.css"/>
    <style>
        *{padding:0px;margin:0px;}
        .pop {  display: none;  width: 950px; min-height: 470px;  max-height: 850px;  height:470px;  position: absolute;  top: 0;  left: 0;  bottom: 0;  right: 0;  margin: auto;  padding: 5px;  z-index: 130;  border-radius: 8px;  background-color: #fff;  box-shadow: 0 3px 18px rgba(100, 0, 0, .5);  }
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
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js<%="?v="+version%>"></script>
    <script type="text/javascript" src="../Assets/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="../Assets/js/datepicker-zh-TW.js"></script>
    <script type="text/javascript" src="../Assets/js/bootstrap-table.min.js"></script>
    <script type="text/javascript" src="../Assets/js/bootstrap-table-locale-all.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //自适应屏幕宽度
            // window.onresize = function () {
            //     location = location
            // };

            var main_h = $(window).height();
            $('.hy_list').css('height', main_h - 45 + 'px');

            var search_w = $(window).width() - 40;
            $('.search').css('width', search_w + 'px');
            //$('.list_hy').css('width',search_w+'px');

            $('.gb_btn').on("click",function () {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                hidePop();
            });

            function hidePop(){
                $('.bgPop,.pop').hide();
                $('#detail_table').bootstrapTable('destroy');
            }

            function showPop(){
                var _scrollHeight = $(document).scrollTop();//获取当前窗口距离页面顶部高度
                var _windowHeight = $(window).height();//获取当前窗口高度
                var _popupHeight = $('.pop').height();//获取弹出层高度
                var _posiTop = (_windowHeight-_popupHeight)/2 + _scrollHeight;
                $('.pop').css("top",_posiTop + "px");//设置position
                $('.bgPop,.pop').show();
            }

            function mxfunction(event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                // var tr = $(this).parents("tr")[0];
                $("#add_result").text("");
                // alert("提交数据成功");
                var customer = $("#customer").val();
                var customer_id = $("#customer_id").val();
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();

                if (customer_id == "") {
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
                    name:customer,
                    customer_id: customer_id,
                    start_date: start_date,
                    end_date: end_date
                };

                $.ajax({
                    type: "POST",
                    url: "/zxyw/gr_detail",
                    // contentType: "application/json",
                    data: obj,//参数列表
                    dataType: "json",
                    success: function (result) {
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
                                field: 'customer',
                                title: '客户名称'
                            }, {
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
                                field: 'carFee',
                                title: '运费',
                                sortable:true
                            },  {
                                field: 'total',
                                title: '总额',
                                sortable:true
                            },  {
                                field: 'carNumber',
                                title: '船号'
                            }, {
                                field: 'carOwner',
                                title: '起货方式'
                            },{
                                field: 'date',
                                title: '时间',
                                sortable:true
                            }]
                        });
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        $("#pz_name").val("发送请求失败");
                    }
                });
            };

            function fill_customer(elem,click_type){
                if(click_type == 1){
                    $("#pop_title").text("交易明细");
                }else if(click_type == 2){
                    $("#pop_title").text("按品种汇总");
                }
                $("#click_type").val(click_type);
                var customer_id = elem.parents("tr").find("td").eq(0).text();
                var customer = elem.parents("tr").find("td").eq(1).text();
                $("#customer").val(customer.trim());
                $("#customer_id").val(customer_id.trim());
                $("#add_result").text("");
                showPop();
            }

            $(".mx_btn").on("click", function(event){
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                fill_customer($(this),1);
            });

            $(".pzhz_btn").on("click", function (event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                fill_customer($(this),2);
            });

            function pzhzfunction(event) {
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                // var tr = $(this).parents("tr")[0];
                $("#add_result").text("");
                // alert("提交数据成功");
                var customer = $("#customer").val();
                var customer_id = $("#customer_id").val();
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();

                if (customer_id == "") {
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
                    customer: customer,
                    customer_id: customer_id,
                    start_date: start_date,
                    end_date: end_date
                };

                $.ajax({
                        type: "POST",
                        url: "/zxyw/gr_pzhz",
                        // contentType: "application/json",
                        data: obj,//参数列表
                        dataType: "json",
                        success: function (result) {
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
                                    field: 'customer',
                                    title: '客户名称'
                                }, {
                                    field: 'variety',
                                    title: '品种',
                                    sortable:true
                                }, {
                                    field: 'num',
                                    title: '数量',
                                    sortable:true
                                }, {
                                    field: 'carFee',
                                    title: '运费',
                                    sortable:true
                                },  {
                                    field: 'total',
                                    title: '总额',
                                    sortable:true
                                }]
                            });
                        },
                        error: function (result) {
                            //请求失败之后的操作
                            $("#pz_name").val("发送请求失败");
                        }
                });
                showPop();
            }

            $(".cx_btn").on("click",function(event){
                var clickType = $("#click_type").val();
                if(clickType == "1"){
                    mxfunction(event);
                }
                if(clickType == "2"){
                    pzhzfunction(event);
                }
            });

            function excelDownLoad(action){
                var customer = $("#customer").val();
                var customer_id = $("#customer_id").val();
                var start_date = $("#datepicker_start").val();
                var end_date = $("#datepicker_end").val();
                var form = $("<form>");
                form.attr('style', 'display:none');
                form.attr('target', '');
                form.attr('method', 'post');
                form.attr('action', action);
                var input0 = $('<input>');
                input0.attr('type', 'hidden');
                input0.attr('name', 'customer');
                input0.attr('value', customer);
                var input1 = $('<input>');
                input1.attr('type', 'hidden');
                input1.attr('name', 'customer_id');
                input1.attr('value', customer_id);
                var input2 = $('<input>');
                input2.attr('type', 'hidden');
                input2.attr('name', 'start_date');
                input2.attr('value', start_date);
                var input3 = $('<input>');
                input3.attr('type', 'hidden');
                input3.attr('name', 'end_date');
                input3.attr('value', end_date);
                $('body').append(form);
                form.append(input0);
                form.append(input1);
                form.append(input2);
                form.append(input3);
                form.submit();
                form.remove();
            }

            $(".dc_btn").on("click",function(event){
                event.preventDefault();//使a自带的方法失效，即无法调整到href中的URL
                var clickType = $("#click_type").val();
                if(clickType == "1"){
                    excelDownLoad("/zxyw/gr_detail_excel");
                }
                if(clickType == "2"){
                    excelDownLoad("/zxyw/gr_pzhz_excel");
                }
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
    <!--框架高度设置-->
</head>

<%--<body onLoad="Resize();">--%>
<div id="right_ctn">
    <div class="right_m">
        <!--会议列表-->
        <div class="hy_list" style="width: 1000px;height: 900px; overflow: scroll">
            <div class="box_t">
                <span class="name">装卸业务个人汇总</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">装卸业务管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">个人汇总</a>
                </div>
                <!--当前位置-->
            </div>
            <%if (!result.isSuccess()) {%>
            <div><%=result.getMessage()%>
            </div>
            <%} else {%>
            <table cellpadding="0" cellspacing="0" class="list_hy" style="width: 1000px;height: 900px">
                <tr>
                    <th scope="id">客户id</th>
                    <th scope="name">客户名称</th>
                    <th scope="create_time">首次交易时间</th>
                    <th scope="create_time">上次交易时间</th>
                    <th scope="op">操作</th>
                </tr>
                <%for (Customer customer : (List<Customer>)result.getResult()) {%>
                <tr>
                    <td><%=customer.getId()%>
                    </td>
                    <td><%=customer.getName()%>
                    </td>
                    <td><%=DateUtil.format(customer.getCreateTime())%>
                    </td>
                    <td><%=DateUtil.format(customer.getLastTime())%>
                    </td>
                    <td>
                        <%--<a href="" class="ysk_btn">应收款</a>&nbsp;&nbsp;|--%>
                        <a href="" class="mx_btn">交易明细</a>&nbsp;&nbsp;|
                        <a href="" class="pzhz_btn">按品种汇总</a>
                    </td>
                </tr>
                <%}%>
            </table>
            <%}%>
        </div>
    </div>
</div>
<!--遮罩层-->
<div class="bgPop"></div>
<!--弹出框-->
<div class="pop">
    <div class="pop-top">
        <h2 id="pop_title"></h2>
    </div>
    <!--会议列表-->
    <div >
        <!--基本配置-->
        <ul class="hypz">
            <li class="clearfix" style="height: 40px;border-bottom: 1px solid #DEDEDE;">
                <input id="click_type" name="" type="text" hidden>
                <input id="customer_id" name="" type="text" hidden>
                <input id="customer" name="" type="text" hidden>
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
                <a href="" class="cx_btn" style="text-decoration: none;display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">查询</a>
                <a href="" class="dc_btn" style="text-decoration: none;display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">导出</a>
                <a href="" class="gb_btn" style="text-decoration: none;display:inline-block;zoom: 1;width: 126px;height: 36px;text-align: center;line-height: 38px;color: rgb(255, 255, 255);margin-right: 5px;background: url(../Assets/images/btn2.png) no-repeat;">关闭</a>
                <text id="add_result" class="result" style="color:red"></text>
            </li>
            <%--<li class="tj_btn" style="border-top: none;border-bottom: 1px solid #DEDEDE;">--%>
            <%--<a href="" class="hz_btn">汇总</a>--%>
            <%--<text id="add_result" class="result"></text>--%>
            <%--</li>--%>
        </ul>
        <!--基本配置-->
    </div>

    <div id="detail_div" class="pop-content">
        <table id="detail_table">
            <thead>
            </thead>
        </table>
    </div>
</div>
</body>
</html>
