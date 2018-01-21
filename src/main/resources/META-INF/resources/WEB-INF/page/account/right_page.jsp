<%@ page import="java.util.List" %>
<%@ page import="com.hongkai.code.Result" %>
<%@ page import="com.hongkai.domain.Account" %>
<%@ page import="com.hongkai.domain.Menu" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../version.jsp" %>
<% Result accountResult = (Result)request.getAttribute("accounts");%>
<% Result menuResult = (Result)request.getAttribute("menus");%>
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

            $('.ok_btn').on('click', function (event) {
                event.preventDefault();
                var accountId = $('#selector').val();
                var ids = [];
                $.each($('input:checkbox'), function () {
                    if (this.checked) {
                        ids.push($(this).val());
                    }
                });

                if(ids.length == 0){
                    return;
                }

                $.ajax({
                    type: "POST",
                    url: "/account/set_right",
                    // contentType: "application/json",
                    data: {accountId:accountId, ids:ids.join(",")},//参数列表
                    dataType: "json",
                    success: function (result) {
                        //请求正确之后的操作
                        // $("#add_result").text(result.message);
                        if(result.success){
                            alert("权限设定成功");
                        }else{
                            alert(result.message);
                        }
                    },
                    error: function (result) {
                        //请求失败之后的操作
                        alert("权限权限请求失败.");
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
                <span class="name">权限管理</span>
                <!--当前位置-->
                <div class="position">
                    <a href=""><img src="../Assets/images/icon5.png" alt=""/></a>
                    <a href="">首页</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">账号管理</a>
                    <span><img src="../Assets/images/icon3.png" alt=""/></span>
                    <a href="">权限管理</a>
                </div>
                <!--当前位置-->
            </div>
            <div>
                <li class="clearfix">
                    <span class="title">账号：</span>
                    <div class="li_r xial">
                        <select id="selector">
                            <option value="" selected></option>
                            <%
                                if (accountResult.isSuccess()) {
                                    List<Account> accounts = (List<Account>)accountResult.getResult();
                                    for (Account account : accounts) {
                            %>
                            <option value="<%=account.getId()%>"><%=account.getUsername()%>
                            </option>
                            <%
                                    }
                                }
                            %>
                        </select>
                        <a href="" class="ok_btn">确认</a>
                    </div>
                    <%if (!accountResult.isSuccess()) {%>
                    <span style="color:red;text-align: left;" class="title"><%=accountResult.getMessage()%></span>
                    <%}%>

                </li>
            </div>
            <%if (!accountResult.isSuccess()) {%>
            <div><%=accountResult.getMessage()%>
            </div>
            <%} else {%>
            <table id="right_table" cellpadding="0" cellspacing="0" class="list_hy">
                <tr>
                    <th scope=""></th>
                    <th scope="id">ID</th>
                    <th scope="pname">主菜单名称</th>
                    <th scope="name">子菜单名称</th>
                </tr>
                <%for (Menu menu : (List<Menu>)menuResult.getResult()) {%>
                <tr>
                    <td>
                        <input type="checkbox" value="<%=menu.getId()%>"/>
                    </td>
                    <td><%=menu.getId()%>
                    </td>
                    <td><%=menu.getPname()%>
                    </td>
                    <td><%=menu.getName()%>
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
