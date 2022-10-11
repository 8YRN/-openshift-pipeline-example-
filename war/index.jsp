<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<%
  UserService userService = UserServiceFactory.getUserService();
  String signupUrl = "";

  if (userService.isUserLoggedIn()) {
    response.sendRedirect("/dashboard/index.jsp");
  } else {
    signupUrl = userService.createLoginURL("/dashboard/index.jsp");
  }
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <link rel="stylesheet" href="css/main.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/lavalamp3.css" type="text/css" media="screen">
    <link rel="stylesheet" href="css/coda-slider.css" type="text/css" media="screen" title="no title" charset="utf-8">
    
    <script src="js/jquery-1.2.6.min.js" type="text/javascript"></script>
    
    <script type="text/javascript" src="js/jquery.easing.1.3.js"></script>
    <script type="text/javascript" src="js/jquery.lavalamp-1.3.4b2.js"></script>
    
    <script src="js/jquery.scrollTo-1.4.2-min.js" type="text/javascript"></script>
    <script src="js/jquery.localscroll-1.2.7-min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/jquery.serialScroll-1.2.2-min.js" type="text/javascript" charset="utf-8"></script>
    <script src="js/coda-slider.js" type="text/javascript" charset="utf-8"></script>

    <script type="text/javascript">
      $(function() {
        $("#lavaLampMenu").lavaLamp({fx: "swing", speed: 200});
      });
    </script>
  </head>
  <body>
    <div id="main">
      <div id="header-long">
        <div id="strip"></div>
        <div id="header">
          <div id="header-top">
            <div id="logo"><div id="bg"></div><h1>Life Planning.</h1></div>
            <div id="menu">
              <ul class="lamp" id="lavaLampMenu">
                  <li><a href="#home">Home</a></li>
                  <li><a href="#help">Help</a></li>
                  <li><a href="#step1">Tour</a></li>
                  <li><a href="<%=