<%@include file="../xava/imports.jsp"%>

<%@page import="org.openxava.application.meta.MetaApplications"%>
<%@page import="org.openxava.application.meta.MetaApplication"%>
<%@page import="org.openxava.util.Locales"%>
<%@page import="com.openxava.naviox.web.NaviOXStyle"%>

<%-- To put your own text add entries in the i18n messages files of your project 
In MyApplication-labels_en.properties:
MyApplication=My application
MyApplication[description]=My application does this and that

In MyApplication-messages_en.properties:
welcome_point1=This is a additional explanatory line
--%>

<%

String applicationName = request.getContextPath().substring(1);
MetaApplication metaApplication = MetaApplications.getMetaApplication(applicationName);
Locales.setCurrent(request);
String oxVersion = org.openxava.controller.ModuleManager.getVersion();
String title = (String) request.getAttribute("naviox.pageTitle");
if (title == null) title = metaApplication.getLabel();
%>

<!DOCTYPE html>

<head>
	<title><%=title%></title>
	<meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1'>
	<link href="<%=request.getContextPath()%>/naviox/style/naviox.css?ox=<%=oxVersion%>" rel="stylesheet" type="text/css"> 
</head>

<body id="welcome" <%=NaviOXStyle.getBodyClass(request)%>>

<h1><%=metaApplication.getLabel()%></h1>
<p><%=metaApplication.getDescription()%></p>


<p id="welcome_point1"><xava:message key="welcome_point1"/></p> 
<p id="signin_tip"><xava:message key="signin_tip"/></p> 



</body>

