<%@ page import="org.swas.util.SystemParameters" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="yowo" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="signedIn" value="${not empty sessionScope.PROFILE}"/>
<%--<jsp:useBean id="systemParams" class="com.app.util.SystemParameters" scope="application"/>--%>

<header id="header">


    <nav id="headerUserMenu">

        <c:if test="${!signedIn}">


        <a id="a_signUp">
            <span class="link"><spring:message code="menu.signUp"/></span>
        </a>
        <span class="text"><spring:message code="menu.or"/></span>
        <a id="a_signIn">
            <span class="link"><spring:message code="menu.signIn"/></span>
        </a>

        <script src="http://ulogin.ru/js/ulogin.js"></script>
        <div id="signUpFormContainer">
            <form id="signUpForm" class="form">
                <h1><spring:message code="signUp.title"/></h1>


                <div id="uLogin1"
                     x-ulogin-params="display=small&fields=first_name,last_name,photo,email,city,country&providers=facebook,google,vkontakte,odnoklassniki,mailru,yandex,livejournal,openid&hidden=&redirect_uri=http%3A%2F%2F<%= request.getServerName()%>%3A<%= request.getServerPort()%>${CONTEXT}%2Fauth%3FurlFrom%3D<%= request.getAttribute("javax.servlet.forward.servlet_path") %>"></div>

                <div class="field">
                    <a id="a_signInFromUp" class="link">
                        <spring:message code="signUp.signIn"/>
                    </a>
                </div>

                <div id="signUpErrors" class="error"></div>
                <yowo:field label="signUp.email.label" name="signUpEmail" required="true"/>
                <yowo:field label="signUp.password.label" name="signUpPassword" type="secret" required="true"/>
                <yowo:field label="signUp.passwordC.label" name="signUpPasswordC" type="secret" required="true"/>
                <yowo:field label="signUp.name.label" name="signUpName"/>


                <button type="submit" id="signUpSubmit"><spring:message code="signUp.submit"/></button>
                <button type="button" id="signUpCancel"><spring:message code="signUp.cancel"/></button>


            </form>
            <div id="signUpMessage" class="message"></div>
        </div>

        <div id="signInFormContainer">
            <form id="signInForm" class="form">
                <h1><spring:message code="signIn.title"/></h1>

                <div id="uLogin"
                     x-ulogin-params="display=small&fields=first_name,last_name,photo,email,city,country&providers=facebook,google,vkontakte,odnoklassniki,mailru,yandex,livejournal,openid&hidden=&redirect_uri=http%3A%2F%2F<%= request.getServerName()%>%3A<%= request.getServerPort()%>${CONTEXT}%2Fauth%3FurlFrom%3D<%= request.getAttribute("javax.servlet.forward.servlet_path") %>"></div>


                <div class="field">
                    <a id="a_signUpFromIn" class="link">
                        <spring:message code="signIn.signUp"/>
                    </a>
                </div>

                <div id="signInErrors" class="error"></div>
                <yowo:field label="signIn.login.label" name="signInLogin" required="true"/>
                <yowo:field label="signIn.password.label" name="signInPassword" type="secret" required="true"/>
                <div class="field">
                    <a id="a_forgotPassword" class="link">
                        <spring:message code="signIn.forgotPassword"/>
                    </a>
                </div>


                <button type="submit" id="signInSubmit"><spring:message code="signIn.submit"/></button>
                <button type="button" id="signInCancel"><spring:message code="signIn.cancel"/></button>


            </form>
            <div id="signInMessage" class="message"></div>
        </div>

        <div id="restorePasswordFormContainer">
            <form id="restorePasswordForm" class="form">
                <h1><spring:message code="restorePassword.title"/></h1>

                <div id="restorePasswordErrors" class="error"></div>
                <yowo:field label="restorePassword.identity.label" name="restorePasswordIdentity" required="true"/>
                <button type="submit" id="restorePasswordSubmit"><spring:message
                        code="restorePassword.submit"/></button>
                <button type="button" id="restorePasswordCancel"><spring:message
                        code="restorePassword.cancel"/></button>

            </form>
            <div id="restorePasswordMessage" class="message"></div>
        </div>

        <div style="display: none;">
            <div class="field" id="recaptcha">

            </div>
        </div>

        <script type="text/javascript" src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>

        <script type="text/javascript">
            $(function () {
                //GLOBAL singleton instances
                app.signInForm = new app.SignInForm({name: "signIn", url: app.context + "/signIn"});

                app.signUpForm = new app.SignUpForm({name: "signUp", url: app.context + "/signUp"});

                app.passwordRestoreForm = new app.RestorePasswordForm({name: "restorePassword", url: app.context + "/restorePassword"});

                $("#a_forgotPassword").click(
                        function (e) {
                            e.preventDefault();
                            app.signInForm.hide();
                            app.passwordRestoreForm.show();
                        }
                );
                $("#a_signInFromUp").click(
                        function (e) {
                            e.preventDefault();
                            app.signUpForm.hide();
                            app.signInForm.show();
                        }
                );

                $("#a_signUpFromIn").click(
                        function (e) {
                            e.preventDefault();
                            app.signInForm.hide();
                            app.signUpForm.show();
                        }
                );


                if (location.hash.indexOf("signUp") > -1) {
                    app.signUpForm.show();
                } else if (location.hash.indexOf("signIn") > -1) {
                    app.signInForm.show();
                }

                if (Recaptcha) {
                    Recaptcha.create("<%=SystemParameters.getParam("recaptcha.key.public") %>",
                            "recaptcha",
                            {
                                theme: "white",
                                callback: Recaptcha.focus_response_field
                            }
                    );
                }
            });
        </script>

        </c:if>


</header>

