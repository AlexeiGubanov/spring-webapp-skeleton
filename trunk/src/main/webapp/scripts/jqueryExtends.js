var ua = navigator.userAgent.toLowerCase(),
        check = function (r) {
            return r.test(ua);
        },
        DOC = document,
        docMode = DOC.documentMode,
        isStrict = DOC.compatMode == "CSS1Compat",
        isOpera = check(/opera/),
        isChrome = check(/\bchrome\b/),
        isWebKit = check(/webkit/),
        isSafari = !isChrome && check(/safari/),
        isSafari2 = isSafari && check(/applewebkit\/4/), // unique to Safari 2
        isSafari3 = isSafari && check(/version\/3/),
        isSafari4 = isSafari && check(/version\/4/),
        isIE = !isOpera && check(/msie/),
        isIE7 = isIE && (check(/msie 7/) || docMode == 7),
        isIE8 = isIE && (check(/msie 8/) && docMode != 7),
        isIE6 = isIE && !isIE7 && !isIE8,
        isGecko = !isWebKit && check(/gecko/),
        isGecko2 = isGecko && check(/rv:1\.8/),
        isGecko3 = isGecko && check(/rv:1\.9/),
        isBorderBox = isIE && !isStrict,
        isWindows = check(/windows|win32/),
        isMac = check(/macintosh|mac os x/),
        isAir = check(/adobeair/),
        isLinux = check(/linux/),
        isSecure = /^https/i.test(window.location.protocol);

$.enable = function (el) {
    el.removeAttr("disabled");
};
$.disable = function (el) {
    el.attr("disabled", true);
};

$.check = function (el) {
    el.removeAttr("checked");
};
$.uncheck = function (el) {
    el.attr("checked", true);
};

$.ajaxSetup({
    cache: false
});

$.postJSON = function (url, data, callback) {
    return $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: $.toJSON(data),
        success: callback
    });
};

$.override = function (origclass, overrides) {
    if (overrides) {
        var p = origclass.prototype;
        $.extend(p, overrides);
        if (isIE && overrides.hasOwnProperty('toString')) {
            p.toString = overrides.toString;
        }
    }
};

$.inherit = function () {
    // inline overrides
    var io = function (o) {
        for (var m in o) {
            this[m] = o[m];
        }
    };
    var oc = Object.prototype.constructor;

    return function (sb, sp, overrides) {
        if (typeof sp == 'object') {
            overrides = sp;
            sp = sb;
            sb = overrides.constructor != oc ? overrides.constructor : function () {
                sp.apply(this, arguments);
            };
        }
        var F = function () {
                },
                sbp,
                spp = sp.prototype;

        F.prototype = spp;
        sbp = sb.prototype = new F();
        sbp.constructor = sb;
        sb.superclass = spp;
        if (spp.constructor == oc) {
            spp.constructor = sp;
        }
        sb.override = function (o) {
            $.override(sb, o);
        };
        sbp.superclass = sbp.supr = (function () {
            return spp;
        });
        sbp.override = io;
        $.override(sb, overrides);
        sb.inherit = function (o) {
            return $.inherit(sb, o);
        };
        return sb;
    };
}();

(function ($) {


    $.fn.ajaxBusy = function () {
        this.append('<div class="ajaxBusy"><div class="ajaxBusyImg"></div> </div>');
        $("body").ajaxStop(function () {
            $('.ajaxBusy').remove();
        });
    };


    $.fn.closeable = function (params, existentBtn, afterClose) {
        var defParams = {
            cls: 'closeMe'
        };
        params = $.extend(defParams, params);
        var me = this;
        var el;
        if (existentBtn) {
            el = $(existentBtn);
        } else {
            el = $("<div></div>");
            el.addClass(defParams.cls);
            this.append(el);

        }

        var afterCloseCallback = afterClose;
        el.click(function () {
            me.hide();
            if (afterCloseCallback) {
                afterCloseCallback();
            }
        })

    };


    /**
     * Assign external scroll pane to some scrollable content.
     * All params are jquery selectors as strings
     * @param knobParent - knob holder (scrollbar), to retrieve max knob offset
     * @param holder - content holder, used to call scrollLeft
     * @param content - content, used to retrieve real content width
     * @author Alexei.Gubanov@gmail.com, 2011
     */
    $.fn.scrollableX = function (knobParent, holder, content) {

        return this.each(function () {
            var knobEl = $(this);
            var knobParentEl = $(knobParent);
            var holderEl = $(holder);
            var contentEl = $(content);

            $(this).draggable({
                axis: 'x',
                containment: 'parent',
                drag: function (event, ui) {
                    // % от всей длины видимого контента
                    var p = knobEl.position().left / (knobParentEl.width() - knobEl.width());
                    // умножаем на хвост, который не виден
                    p *= contentEl.width() - holderEl.width();
                    holderEl.scrollLeft(p);
                }
            });

        });


    };

    $.fn.inputDefaults = function (options) {
        var defaults = {
            cl: 'inactive',
            text: this.val()
        };
        var opts = $.extend(defaults, options);

        this.addClass(opts['cl']);
        this.val(opts['text']);
        this.focus(function () {
            if ($(this).val() == opts['text']) $(this).val('');
            $(this).removeClass(opts['cl']);
        });

        this.blur(function () {
            if ($(this).val() == '') {
                $(this).val(opts['text']);
                $(this).addClass(opts['cl']);
            }
        });
    };


    $.fn.showByPane = function (total, offset, limit) {
        return this.each(function () {
            var url;
            var s = "";
            var recsPerPage = [5, 10, 20, 50];
            for (var i = 0; i < recsPerPage.length; i++) {
                var r = recsPerPage[i];
                if (r != limit) {
                    url = $.query.set("limit", r);
                    s += '&nbsp;<a class="link" href="' + url + '">' + r + '</a>&nbsp;';
                } else {
                    s += '&nbsp;<span class="active">' + r + '</span>&nbsp;';

                }
                if (i < recsPerPage.length - 1)
                    s += '|';
            }
            $(this).append(s);
        });

    };

    $.fn.sortByPane = function (names) {
        return this.each(function () {
            var url;
            var s = "";
            var sort = $.query.get("sort");
            var order = $.query.get("dir");
            for (var i = 0; i < names.length; i++) {
                var r = names[i].field;
                if (r != sort) {
                    url = $.query.set("sort", r);
                    s += '&nbsp;<a class="link" href="' + url + '">' + names[i].name + '</a>&nbsp;';
                } else {
                    var x = "desc";
                    if (x == order) {
                        x = "asc";
                    }
                    url = $.query.set("dir", x);
                    s += '&nbsp;<a class="link" href="' + url + '">' + names[i].name + '</a>&nbsp;';

                }
                if (i < names.length - 1)
                    s += '|';
            }
            $(this).append(s);
        });

    };


    $.fn.filterPane = function (filters, baseUrl) {
        return this.each(function () {
            for (var i = 0; i < filters.length; i++) {
                var f = filters[i];
                $(this).append("<h3>" + f.title + "</h3>");
                var s = "<ul>";
                var current = $.query.get(f.paramName);
                if (current === undefined || current.length == 0)
                    current = false;
                for (var j = 0; j < f.items.length; j++) {
                    var item = f.items[j];

                    var possible = (item.value !== undefined) ? item.value : false;
                    // показываем ссылку если
                    if (current !== possible) {
                        var url = (item.value !== undefined) ? $.query.set(f.paramName, item.value) : $.query.remove(f.paramName);
                        url = url.toString();
                        if (!url || url.length == 0) url = baseUrl;
                        s += '<li><a class="link" href="' + url + '">' + item.title + '</a></li>';
                    } else {
                        s += '<li>' + item.title + '</li>';

                    }
                }
                s += "</ul>";

                $(this).append(s);

            }
        });
    };

    /**
     * Помещает пейджер внутрь переданного элемента
     * @param ajax - будет ли работать пейддже через ajax (если true, то вместо href будет вызываться onPageClick(offset)
     * @param total - сколько всего записей
     * @param offset - текущее значение смещения
     * @param limit - текущее значение лимита
     * @param pagesVisible - сколько страниц видно справа/слева от текущей, по умолчанию 3
     * @param onPageClick
     */
    $.fn.pager = function (ajax, total, offset, limit, pagesVisible, onPageClick) {
        pagesVisible = pagesVisible || 3;

        var s = "";

        var totalPages = Math.ceil(((total > limit) ? total : 0) / limit);
        var page = Math.ceil(((offset > 0) ? offset : 0) / limit) + 1;
        var PARAM = pagesVisible; // сколько страниц показывать справа/слева от текущей
        var $el = $(this);

        if (totalPages <= 1)
            return;

        var createLink = function (offset, content) {
            if (ajax) {
                var a = $('<a><div class="corners">' + content + '</div></a>');
                a.click(function () {
                    onPageClick(offset);
                });
                return a;
            } else {
                var url = $.query.set("offset", offset);
                return $('<a href="' + url + '"><div class="corners">' + content + '</div></a>');

            }
        };

        // пред.
        if (page != 1) {
            $el.append(createLink(offset - limit, '<div class="icon prev"></div>'));
        }
        // первая страница
        if ((page - PARAM) > 1) {
            $el.append(createLink(0, '1'));
            if (page - PARAM > 2)
                $el.append('<span>..</span>');
        }

        for (var i = (page - PARAM); i <= (page + PARAM); i++) {
            if (i > 0 && i <= totalPages && i != page) {
                $el.append(createLink(((i - 1) * limit), i));
            } else if (i == page) {
                $el.append('<div class="corners active">' + i + '</div>');

            }
        }


        // последняя страница
        if (page + PARAM < totalPages) {
            if (page + PARAM < (totalPages - 1))
                $el.append('<span>..</span>');
            $el.append(createLink((totalPages - 1) * limit, totalPages));
        }
        if (page != totalPages) {
            $el.append(createLink(offset + limit, '<div class="icon next"></div>'));
        }
    };

//    $.fn.checkboxesSelect = function (options) {
//        options = options || {};
//        return this.each(function () {
//            options.el = this;
//            return new yowo.CheckboxesSelect(options);
//        });
//    };

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };


})(jQuery);


