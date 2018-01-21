<%@ page import="java.util.List" %>
<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Variety" %>
<%@ page import="util.DateUtil" %>
<%@ page import="com.hongkai.domain.Record" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<% Result result = (Result)request.getAttribute("result");%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <title>会议列表</title>
    <link rel="stylesheet" type="text/css" href="../Assets/css/reset.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../Assets/css/thems.css">
    <script type="text/javascript" src="../Assets/js/jquery-3.2.1.min.js"></script>
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
                <span class="name">品种列表</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">品种管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">品种列表</a>
                </div>
                <!--当前位置-->
            </div>
            <%if (!result.isSuccess()) {%>
            <div><%=result.getMessage()%>
            </div>
            <%} else {%>
            <table cellpadding="0" cellspacing="0" class="list_hy">
                <tr>
                    <%--<th scope="id">记录id</th>--%>
                    <th scope="customer">客户名称</th>
                    <th scope="variety">品种</th>
                    <th scope="num">数量</th>
                    <th scope="price">单价</th>
                    <th scope="unit">单位</th>
                    <th scope="car_fee">运费</th>
                    <th scope="total">总额</th>
                    <th scope="car_number">车号</th>
                    <th scope="car_owner">承运人</th>
                    <th scope="time">时间</th>
                </tr>
                <%for (Record record : (List<Record>)result.getResult()) {%>
                <tr>
                    <td><%=record.getCustomer()%></td>
                    <td><%=record.getVariety()%></td>
                    <td><%=record.getNum()%></td>
                    <td><%=record.getPrice()%></td>
                    <td>吨</td>
                    <td><%=record.getCarFee()%></td>
                    <td><%=record.getTotal()%></td>
                    <td><%=record.getCarNumber()%></td>
                    <td><%=record.getCarOwner()%></td>
                    <td><%=record.getDate()%>
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
