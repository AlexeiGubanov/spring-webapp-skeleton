app = {
    context:""

};

/**
 * Базовый класс
 * @param config
 */
app.Base = function (config) {
    config = config || {};
    $.extend(this, config);
    this.init();
};

app.Base.prototype = {
    init:function () {

    },
    proxyMe:function (callback) {
        return $.proxy(callback, this);
    }
};


/**
 * Интерфейс наблюдаемого объекта
 */
app.Observable = $.inherit(app.Base, {
    init:function () {
        this.observers = [];
    },
    bind:function (eventType, callback) {
        if (!(callback instanceof Object)) {
            return;
        }
        if (!this.observers[eventType]) {
            this.observers[eventType] = [];
        }
        this.observers[eventType].push(callback);
    },
    unbind:function (eventType, callback) {

        if (this.observers[eventType] && this.observers[eventType].contains(callback)) {
            this.observers[eventType].remove(callback);
        }
    },

    fire:function () {
        var eventType = arguments[0];
        var argsArray = Array.prototype.slice.call(arguments, 1);
        if (!this.observers[eventType]) {
            return;
        }
        var os = this.observers[eventType];

        for (var i = 0; i < os.length; i++) {
            if (os[i] instanceof Function) {
                os[i].apply(this, argsArray);
            }
        }

    }

});

var localTz = new Date().getTimezoneOffset() * 60 * 1000;
var toLocalDate = function (ms) {
    return ms + localTz;
};
var toServerDate = function (ms) {
    return ms - localTz;
};


/**
 * Мультивыбор в виде чекбоксов с возможностью группировки.
 * options:
 *  el - элемент, к которому привязать
 *  items: массив данных, древовидная структура
 *   каждый элемент может иметь поле childs - группа  (optgroup)
 *   если нету - значение (option),
 *
 */
app.CheckboxesSelect = $.inherit(app.Base, {
    initialSelectedAll:true,
    data:[],
    levelPaddingStep:5,
    init:function () {
        this.selectToggler = true;
        // рисуем данные
        this.draw();
        this.checkAll(this.initialSelectedAll);

    },
    reset:function () {
        this.selectToggler = !this.selectToggler;
        this.checkAll(this.selectToggler);
    },
    getItemHtmlText:function (item, prevText, level) {
        var result = prevText || "";
        level = level || 0;
        var paddingLeft = level * this.levelPaddingStep;
        var paddingLeft2 = (level + 1) * this.levelPaddingStep;
        // смотрим тип
        if (item.childs) {
            if (item.childs.length > 0) {
                // группа
                item.elId = "chkItemGroup_" + item.id;
                result += "<p class='chkGroup lvl_" + level + "' style='padding-left: " + paddingLeft + "px'><input id='" + item.elId + "'  type='checkbox'>" + item.name + "</input></p>";
                for (var i = 0; i < item.childs.length; i++) {
                    result += this.getItemHtmlText(item.childs[i]);
                }
            }
        } else {
            // значение
            this.total++;
            item.elId = "chkItem_" + item.id;
            result += "<p class='chkItem lvl_" + level + "' style='padding-left: " + paddingLeft2 + "px'><input id='" + item.elId + "'  type='checkbox' value='" + item.id + "'> " + item.name + " </input></p>";
        }
        return result;

    },
    clear:function () {
        $(this.el).empty();
        this.total = 0;
        this.data = [];
    },
    draw:function (data) {
        data = data || this.data;
        this.total = 0;
        var t = "";
        // проходим по данным
        for (var i = 0; i < data.length; i++) {
            var item = data[i];
            t += this.getItemHtmlText(item);
        }
        $(this.el).html(t);
        $(this.el).find("input[type='checkbox']").click(this.proxyMe(this.onChkClick));
        this.data = data;


    },
    findByElId:function (elId, items) {
        items = items || this.data;
        for (var i = 0; i < items.length; i++) {
            var item = items[i];
            if (item.elId == elId)
                return item;
            if (item.childs) {
                item = this.findByElId(elId, item.childs);
                if (item != null)
                    return item;
            }

        }
        return null;
    },
    onChkClick:function (e) {
        var item = this.findByElId($(e.target).attr('id'));
        var c = $(e.target).attr('checked');
        c = (c == 'checked');
        if (item != null) {
            this.checkItem(item, c);
        }
    },
    checkItem:function (item, checked) {
        if (checked)
            this.el.find("#" + item.elId).attr('checked', 'checked');
        else
            this.el.find("#" + item.elId).removeAttr('checked');
        item.checked = checked;
        if (item.childs) {
            for (var i = 0; i < item.childs.length; i++) {
                this.checkItem(item.childs[i], checked);
            }
        }
    },
    checkAll:function (checked) {
        for (var i = 0; i < this.data.length; i++) {
            this.checkItem(this.data[i], checked)
        }
    },
    getValues:function (items) {
        items = items || this.data;
        var vals = [];
        for (var i = 0; i < items.length; i++) {
            if (items[i].childs) {
                vals = vals.concat(this.getValues(items[i].childs))
            } else if (items[i].checked) {
                vals.push(items[i].id);
            }
        }
        return vals;
    },
    isAllSelected:function () {
        return this.getValues().length == this.total;
    }

});


var getDurationText = function (d) {
    d = Math.ceil(d / 60 / 60 / 1000);
    if (d > 24) {
        d = Math.ceil(d / 24);
        return d + " " + getDurationTextConfig.days[getNumberSuffixType(d)];

    } else {
        return d + " " + getDurationTextConfig.hours[getNumberSuffixType(d)];

    }
};
getDurationTextConfig = {
    hours:['hour', 'hours', 'hours'],
    days:['day', 'days', 'days']
};

var getNumberSuffixType = function (x) {
    x = x % 100;
    if (x > 4 && x < 21) {
        return 2;
    } else {
        x = x % 10;
        if (x == 1) {
            return 0;
        } else if (x > 1 && x < 5) {
            return 1
        } else {
            return 2;
        }
    }
};


