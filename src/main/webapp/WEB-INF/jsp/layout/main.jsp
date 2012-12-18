<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" content="text/html">
    <title>
        <spring:message code="${key}"/>
    </title>
    <link rel="stylesheet" href="${CONTEXT}/scripts/jqUI/themes/yowo/jquery-ui-1.8.16.custom.css" media="screen">
    <script src="${CONTEXT}/scripts/jquery/jquery-1.7.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jquery/jquery.query.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jquery/jquery.json-2.3.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqUI/jquery-ui-1.8.16.custom.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqUI/i18n/jquery.ui.datepicker-${pageContext.request.locale.language}.js"
            type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqueryExtends.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/app.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/main.js" type="text/javascript"></script>

    <link rel="stylesheet" href="${CONTEXT}/styles/main.css" media="screen">


    <!--[if lt IE 9]>
    <link rel="stylesheet" href="${CONTEXT}/styles/main_ie6.css" media="screen">
    <![endif]-->


    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script type="text/javascript">
        app.context = "${CONTEXT}";
    </script>


</head>
<body>
<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="content"/>
<tiles:insertAttribute name="footer"/>
</body>
</html>
