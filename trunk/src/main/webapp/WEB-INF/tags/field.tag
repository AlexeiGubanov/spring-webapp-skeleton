<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" %>
<%@ attribute name="type" %>
<%@ attribute name="value" %>

<%@ attribute name="required" type="java.lang.Boolean" %>
<c:if test="${type == null}"><c:set var="type" value="text"/></c:if>


<div id="${name}Container" class="fieldContainer">
    <div id="${name}Field" class="field <c:if test="${required eq true}">required</c:if>">
        <label for="${name}Input">
            <c:if test="${fn:startsWith(label,'=')}">${fn:substringAfter(label, "=")}</c:if>
            <c:if test="${not fn:startsWith(label,'=')}"><spring:message code="${label}"/></c:if>

            <c:if test="${required eq true}">
                <em>*</em>
            </c:if>
        </label>

        <c:choose>
            <c:when test="${type eq 'text'}">
                <input id="${name}Input" type="text" name="${name}Input" value="${value}"/>
            </c:when>
            <c:when test="${type eq 'secret'}">
                <input id="${name}Input" type="password" name="${name}Input"/>
            </c:when>
            <c:when test="${type eq 'combo'}">
                <select id="${name}Input" name="${name}Input">
                    <jsp:doBody/>
                </select>
            </c:when>
        </c:choose>

    </div>
    <div id="${name}Errors" class="error"></div>
</div>

