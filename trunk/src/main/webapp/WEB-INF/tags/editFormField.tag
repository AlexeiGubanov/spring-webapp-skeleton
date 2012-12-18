<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" %>
<%@ attribute name="label" %>
<%@ attribute name="type" %>
<%@ attribute name="multiple" %>
<%@ attribute name="afterInput"%>


<c:if test="${type == null}"><c:set var="type" value="text"/></c:if>

<div id="${name}_FieldContainer" class="formField">

    <div id="${name}_LabelContainer" class="formFieldLabelContainer">
        <span class="formFieldLabel"><spring:message code="${label}"/></span>
    </div>

    <div id="${name}_ValContainer" class="formFieldValContainer">
        <c:choose>
            <c:when test="${type eq 'text'}">
                <input id="${name}_val" class="formFieldVal" type="text"/>
                ${afterInput}
            </c:when>
            <c:when test="${type eq 'combo'}">
                <select id="${name}_val" class="formFieldVal"
                        <c:if test="${multiple eq true}">
                            multiple="multiple"
                        </c:if>
                        >
                    <jsp:doBody/>
                </select>
            </c:when>
        </c:choose>
    </div>

</div>

<div id="${name}Errors" class="fieldError"></div>