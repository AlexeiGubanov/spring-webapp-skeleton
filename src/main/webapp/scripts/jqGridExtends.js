$.jgrid.edit.defaults = {
    ajaxEditOptions: { contentType: "application/json; charset=utf-8" }
};

$.jgrid.del.defaults = {
    ajaxDelOptions: { contentType: "application/json; charset=utf-8" }
};

$.jgrid.BuildEditOptions = function(url, serializeFunc) {
    return $.extend($.jgrid.edit.defaults, {
        url:url,
        serializeEditData: function(postData) {
            if (typeof serializeFunc === "functions")
                postData = serializeFunc(postData);
            if (postData.id == "_empty") {
                delete postData.id;
            }
            delete postData.oper;
            return $.toJSON([postData]);
        }
    });
};

$.jgrid.BuildDelOptions = function(url, serializeFunc) {
    return $.extend($.jgrid.del.defaults, {
        url:url,
        serializeDelData: function(postData) {
            if (typeof serializeFunc === "functions")
                postData = serializeFunc(postData);
            var ids = postData.id.split(",");
            return $.toJSON(ids);
        }
    });
};

$.jgrid.defaults = $.extend($.jgrid.defaults, {
    ajaxGridOptions: {
        contentType: "application/json; charset=utf-8"
    },
    datatype: 'json',
    mtype: 'GET',
    jsonReader: {
        root: "data",
        repeatitems: false,
        id:"id"
    },
    viewrecords: true,
    rowNum:20,
    rowList:[10,20,50,100,1000],
    multiselect: true,
    autowidth:true,
    height:450,
    /**
     * Event handler before passing request to server
     * @param postData
     */
    serializeGridData: function(postData) {
//        // adapt jqGrid postData format to self-api
        var data = {};
        if (this.p.rowNum > -1) {
            data.limit = postData.rows;
            data.offset = (postData.page - 1) * postData.rows;
        }
        if (typeof postData.sidx !== "undefined" && postData.sidx.length > 0) {
            data.sort = postData.sidx;
            data.dir = postData.sord;
        }
        return data;
    },
    /**
     * Event handler before processing result
     * @param data
     * @param st
     * @param xhr
     */
    beforeProcessing: function(data, st, xhr) {
        //adapt self-api to jqGrid api
        data.page = this.p.page;
        data.records = data.total;
        data.total = Math.ceil(data.total / this.p.rowNum);
    }
});