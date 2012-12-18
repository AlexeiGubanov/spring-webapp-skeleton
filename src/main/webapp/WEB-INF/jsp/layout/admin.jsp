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


    <link href="${CONTEXT}/images/icons/favicon.gif" rel="shortcut icon" type="image/x-icon"/>
    <link rel="stylesheet" href="${CONTEXT}/styles/style_admin.css" media="screen">
    <link rel="stylesheet" href="${CONTEXT}/scripts/jqUI/themes/admin/jquery-ui-1.8.16.custom.css" media="screen">
    <script src="${CONTEXT}/scripts/jquery/jquery-1.7.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jquery/jquery.json-2.3.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqUI/jquery-ui-1.8.16.custom.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqueryExtends.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/app.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/mainAdmin.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" media="screen" href="${CONTEXT}/scripts/jqGrid/css/ui.jqgrid.css"/>
    <script src="${CONTEXT}/scripts/jqGrid/i18n/grid.locale-ru.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqGrid/jquery.jqGrid.src.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqGridExtends.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqSplitter/splitter.js" type="text/javascript"></script>
    <script src="${CONTEXT}/scripts/jqForm/jquery.form.js" type="text/javascript"></script>


    <script type="text/javascript">
        app.context = "${CONTEXT}";

        $(function () {

            $("ul.menu li").hover(function () {

                $(this).addClass("hover");
                $('ul:first', this).css('visibility', 'visible');

            }, function () {

                $(this).removeClass("hover");
                $('ul:first', this).css('visibility', 'hidden');

            });

            $("ul.menu li ul li:has(ul)").find("a:first").append(" &raquo; ");

        });
    </script>


</head>

<body>


<div id="header">
    <div id="headerLogo">
        <a href="${CONTEXT}/admin">
            <h1 id="logo"></h1>
        </a>
    </div>
</div>
<div id="menuContainer" class="ui-widget">
    <ul class="menu">
        <li><a href="${CONTEXT}/admin/users"><spring:message code="admin.menu.users"/></a></li>


    </ul>

</div>
<div id="container">
    <tiles:insertAttribute name="content"/>
</div>


</body>
</html>


