app.EditForm = function (config) {
    config = config || {};
    $.extend(this, config);
    this.init();
};

app.EditForm.prototype = {
    containerNameTpl:"{name}_FormContainer",
    formTpl:"{name}Form",
    messageTpl:"{name}Message",
    errorsTpl:"{name}Errors",
    addBtnTpl:"{name}BtnAdd",
    saveBtnTpl:"{name}BtnSave",
    resetBtnTpl:"{name}BtnReset",
    deleteBtnTpl:"{name}BtnDelete",
    fieldValTpl:"{name}_{fieldName}_val",
    fieldErrorTpl:"{name}_{fieldName}Errors",
    fields:[],
    urlGet:"",
    urlUpdate:"",
    urlDelete:"",
    record:{},
    beforeSave:false,
    context:"",
    proxyMe:function (callback) {
        return $.proxy(callback, this);
    },
    replaceFormName:function (s) {
        return s.replace("{name}", this.name);
    },
    getFieldByName:function (name) {
        for (var i in this.fields) {
            if (name == this.fields[i].name)
                return this.fields[i];
        }
        return false;
    },
    init:function () {
        // bind basic elements
        this.container = $("#" + this.replaceFormName(this.containerNameTpl));
        this.form = $("#" + this.replaceFormName(this.formTpl));
        this.message = $("#" + this.replaceFormName(this.messageTpl));
        this.errors = $("#" + this.replaceFormName(this.errorsTpl));
        this.addBtn = $("#" + this.replaceFormName(this.addBtnTpl));
        this.resetBtn = $("#" + this.replaceFormName(this.resetBtnTpl));
        this.saveBtn = $("#" + this.replaceFormName(this.saveBtnTpl));
        this.deleteBtn = $("#" + this.replaceFormName(this.deleteBtnTpl));

        for (var i in this.fields) {
            if (typeof this.fields[i] === "string") {
                var f = {name:this.fields[i]};
                delete this.fields[i];
                this.fields[i] = f;
            }
        }

        for (var i in this.fields) {
            this.fields[i].mappedName = this.fields[i].mappedName || this.replaceFormName(this.fieldValTpl).replace("{fieldName}", this.fields[i].name);
            this.fields[i].mappedItem = $("#" + this.fields[i].mappedName);
            var en = this.fields[i].errorsName || this.replaceFormName(this.fieldErrorTpl).replace("{fieldName}", this.fields[i].name);
            this.fields[i].errorItem = $("#" + en);
        }

        this.addBtn.click(this.proxyMe(this.add));
        this.resetBtn.click(this.proxyMe(this.reset));
        this.saveBtn.click(this.proxyMe(this.save));
        this.deleteBtn.click(this.proxyMe(this.del));

    },
    updateState:function () {
        for (var i in this.fields) {
            if (!this.fields[i].skip)
                this.record[this.fields[i].name] = this.fields[i].mappedItem.val();
        }

    },
    clearMsg:function () {
        this.message.empty();
        this.message.hide();

    },
    showMsg:function (msg) {
        this.message.html(msg);
        this.message.show();

    },
    clearErrors:function () {
        this.errors.empty();
        this.errors.hide();
        for (var i in this.fields) {
            var f = this.fields[i].errorItem;
            f.empty();
            f.hide();
        }

    },
    showErrors:function (data) {
        this.clearErrors();
        var hasText = false;
        var o = this.errors;
        if (data.message != null) {

            o.html(data.message);
            hasText = true;
            o.show();
        }
        for (var n in data.errors) {

            var s = "";
            for (var e in data.errors[n]) {
                s += ("<p>" + data.errors[n][e] + "</p>");
            }
            var f = this.getFieldByName(n);
            if (f && f.errorItem[0]) {
                hasText = true;
                f.errorItem.append(s);
                f.errorItem.show();
            } else {
                hasText = true;
                o.append("<span style='font-weight: bold;'>" + n + "</span>" + s);
                o.show();
            }
        }
        if (!hasText) {
            o.html('Common error');
            o.show();

        }
    },
    showErrorsInErrors:function (data) {
        this.clearErrors();
        var o = this.errors;
        if (data.message != null) {

            o.html(data.message);
        }
        for (var n in data.errors) {
            var f = this.getFieldByName(n);
            if (f) {
//                var ei = f.errorItem;
                var lblContainer = "#" + f.mappedName.substring(0, f.mappedName.length - 4) + "_LabelContainer";
                var s = "";
                for (var e in data.errors[n]) {
                    if (s != "")
                        s += ", ";
                    s += data.errors[n][e];
                }
                o.append("<p>" + $(lblContainer).children(":first").html() + ":" + s + "</p>");


            }
        }
        o.show();
    },
    clear:function () {
        this.record = {};
        for (var i in this.fields) {
//            var defVal = "";
//            if (this.fields[i].defValue) {
//                if ($.isFunction(this.fields[i].defValue)) {
//                    defVal = this.fields[i].defValue();
//                } else {
//                    defVal = this.fields[i].defValue;
//                }
//            }
            var f = this.fields[i].mappedItem;
            f.val("");
            $.disable(f);
        }
        $.enable(this.addBtn);
        $.disable(this.resetBtn);
        $.disable(this.saveBtn);
        $.disable(this.deleteBtn);
    },
    show:function (record, isNew) {
        isNew = isNew || false;
        record = record || this.record;
        this.clearMsg();
        this.clearErrors();
        this.clear();


        if (!record.id && !isNew)
            return false;

        for (var i in this.fields) {
            if (isNew && this.fields[i].defValue) {
                var defVal = "";
                if ($.isFunction(this.fields[i].defValue)) {
                    defVal = this.fields[i].defValue();
                } else {
                    defVal = this.fields[i].defValue;
                }
                record[this.fields[i].name] = defVal;
            }
            var f = this.fields[i].mappedItem;
            f.val(record[this.fields[i].name]);
            $.enable(f);
        }
        $.enable(this.addBtn);
        $.enable(this.resetBtn);
        $.enable(this.saveBtn);
        $.enable(this.deleteBtn);
        this.record = record;


    },
    get:function (id) {
        id = id || this.record.id;
        if (!id) return false;

        $.getJSON(app.context + this.urlGet, {id:id}, this.proxyMe(this.onGetSuccess));
    },
    onGetSuccess:function (data, status) {
        this.show(data);
    },
    add:function () {
        this.show({}, true);
    },
    reset:function () {
        if (this.record.id) {
            this.get();
        } else {
            this.add()
        }
    },
    save:function () {
        this.updateState();
        if (this.beforeSave) {
            if (!this.beforeSave())
                return false;
        }
        $.postJSON(app.context + this.urlUpdate, [this.record], this.proxyMe(this.onSaveSuccess));

    },
    onSaveSuccess:function (data) {
        if (data.success) {
            var id = data.data[0];


            if (data.message) {
                this.showMsg(data.message);
                setTimeout(this.proxyMe(function () {
                    this.get(id);
                }), 600);
            } else {
                this.get(id);

            }
        } else {
            this.showErrors(data);
        }

    },
    del:function () {
        if (confirm("Подтверждаете удаление?")) {
            $.postJSON(app.context + this.urlDelete, [this.record.id], this.proxyMe(this.onDeleteSuccess));
        }

    },
    onDeleteSuccess:function (data, status) {
        if (data.success) {
            if (data.message)
                this.showMsg(data.message);
            this.clear();
        } else {
            this.showErrors(data);
        }
    }


};





