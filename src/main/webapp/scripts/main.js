app.Form = function (config) {
    config = config || {};
    $.extend(this, config);
    this.init();
};

app.Form.prototype = {
    recaptchaId:"recaptcha",
    needRecaptcha:false,
    beforeSubmit:false,
    baseData:{},
    init:function () {
        this.container = $("#" + this.name + "FormContainer");
        this.form = $("#" + this.name + "Form");
        this.message = $("#" + this.name + "Message");
        if (location.pathname.indexOf("activate") <= -1)
            this.fromUrl = location.pathname.substring(app.context.length, location.pathname.length);
        else
            this.fromUrl = "/";

        this.container.dialog({
            modal:true,
            autoOpen:false,
            width:this.container.width(),
            height:this.container.height(),
            resizable:true,
            draggable:false,
            zIndex:5000
        });
        $("#" + this.name + "Cancel").click(
            $.proxy(function (e) {
                this.hide();
            }, this)
        );

        this.form.submit($.proxy(this.submit, this));
    },
    submit:function (e) {
        e.preventDefault();
        var data = {
        };
        for (var k in this.fieldMap) {
            data[k] = $("#" + this.fieldMap[k] + "Input").val();
        }
        if (this.needRecaptcha && Recaptcha) {
            data.recaptchaChallenge = Recaptcha.get_challenge();
            data.recaptchaResponse = Recaptcha.get_response();
        }
        if (this.beforeSubmit) {
            if (!this.beforeSubmit(data))
                return false;
        }
        $.extend(data, this.baseData);
        $.postJSON(this.url, data, $.proxy(this.onSubmitSuccess, this));
        this.form.ajaxBusy();
    },
    onSubmitSuccess:function (data, status) {
        this.clearErrors();
        if (data.success) {
            this.message.html(data.message);
            this.message.show();
        } else {
            this.showErrors(data);

        }
    },
    show:function () {
        this.container.dialog("open");

        this.message.hide();
        this.addRecaptcha();
    },
    hide:function () {
        this.container.dialog("close");
    },
    clearErrors:function () {
        var o = $("#" + this.name + "Errors");
        o.empty();
        o.hide();
        for (var k in this.fieldMap) {
            o = $("#" + this.fieldMap[k] + "Errors");
            $("#" + this.fieldMap[k] + "Input").removeClass("errorInput");
            o.empty();
            o.hide();
        }
    },
    showErrors:function (data) {
        if (data.message != null) {
            var o = $("#" + this.name + "Errors");
            o.html(data.message);
            o.show();
        }
        for (var f in data.errors) {
            var x = this.fieldMap[f];
            o = $("#" + x + "Errors");
            if (typeof o !== "undefined") {
                $("#" + x + "Input").addClass("errorInput");
                for (var e in data.errors[f]) {
                    o.append("<p>" + data.errors[f][e] + "</p>");
                    o.show();
                }

            }
        }
        this.checkNeedRecaptcha(data);
    },
    addRecaptcha:function () {
        if (this.needRecaptcha && this.container.find("#" + this.recaptchaId).length == 0) {
            $("#" + this.recaptchaId).insertBefore(this.container.find("button[type='submit']"));
        }
        if (this.needRecaptcha && Recaptcha)
            Recaptcha.reload();
    },
    checkNeedRecaptcha:function (data) {
        // смотрим нужна ли рекапча
        if (data.data && data.data.needRecaptcha) {
            this.needRecaptcha = true;
            this.addRecaptcha();

        }
    }
};

app.SignUpForm = $.inherit(app.Form, {
    fieldMap:{email:"signUpEmail",
        password:"signUpPassword",
        passwordC:"signUpPasswordC",
        name:"signUpName"},
    init:function () {
        app.SignUpForm.superclass.init.call(this);
        $("#a_signUp").click(
            $.proxy(function (e) {
                e.preventDefault();
                this.show();
            }, this)
        );
        var me = this;
        this.tzs = true;

    },
    beforeSubmit:function (data) {
        if (this.fromUrl)
            data.fromUrl = this.fromUrl;
        return true;

    },
    show:function (fromUrl) {
        var me = this;
        if (fromUrl) {
            this.fromUrl = fromUrl;
            //подмена параметра urlFrom для redirect_url в uLogin
            var uLogin = $("#uLogin");
            var params = uLogin.attr("x-ulogin-params");
            var replace_param = params.substring(params.indexOf("urlFrom%3D"), params.length);
            var result = params.replace(replace_param, "urlFrom%3D" + this.fromUrl);
            //uLogin.attr("x-ulogin-params", result);
            var iframe = uLogin.find('iframe');
            var src = iframe.attr("src");
            var result_src = src.replace(replace_param, "urlFrom%3D" + this.fromUrl);
            iframe.attr("src", result_src);

        }
        app.SignUpForm.superclass.show.call(this);
    },
    /**
     * Обработка результата от сервера
     * @param data
     */
    onSubmitSuccess:function (data) {
        this.clearErrors();
        if (data.success) {
            // форма signUP
            // если есть сообщение, то выводим текст
            if (data.message != "") {
                this.form.hide();
                this.message.html(data.message);
                this.message.show();
            } else if (data.data != "") {
                // иначе если есть данные - то там url для перехода
                location.href = app.context + data.data;
            }
        } else {
            this.showErrors(data);
        }
    }

});

app.SignInForm = $.inherit(app.Form, {
    fieldMap:{login:"signInLogin",
        password:"signInPassword"},
    needRecaptcha:false,
    init:function () {
        app.SignUpForm.superclass.init.call(this);
        $("#a_signIn").click(
            $.proxy(function (e) {
                e.preventDefault();
                this.show();
            }, this)
        );
    },
    show:function (fromUrl) {
        if (fromUrl) {
            this.fromUrl = fromUrl;
            //подмена параметра urlFrom для redirect_url в uLogin
            var uLogin = $("#uLogin");
            var params = uLogin.attr("x-ulogin-params");
            var replace_param = params.substring(params.indexOf("urlFrom%3D"), params.length);
            var result = params.replace(replace_param, "urlFrom%3D" + this.fromUrl);
            //uLogin.attr("x-ulogin-params", result);
            var iframe = uLogin.find('iframe');
            var src = iframe.attr("src");
            var result_src = src.replace(replace_param, "urlFrom%3D" + this.fromUrl);
            iframe.attr("src", result_src);
        }

        app.SignInForm.superclass.show.call(this);
    },
    beforeSubmit:function (data) {
        if (this.fromUrl)
            data.fromUrl = this.fromUrl;
        return true;

    },
    onSubmitSuccess:function (data, status) {
        this.clearErrors();
        if (data.success) {
            // форма signIn
            location.href = app.context + data.message;
//            // если пустое сообщение, то тупо reload
//            if (data.message == "") {
//                location.reload(true);
//            } else {
//                // иначе там относительный url куда переходить
//                location.href = app.context + data.message;
//            }
        } else {
            this.showErrors(data);
        }
    }
});

app.RestorePasswordForm = $.inherit(app.Form, {
    fieldMap:{identity:"restorePasswordIdentity"}
});

