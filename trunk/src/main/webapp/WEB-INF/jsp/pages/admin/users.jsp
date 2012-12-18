<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="yowo" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<script type="text/javascript">

    var ShowUserSearch = {
        params:{},
        needRefresh:false,

        updateParams:function () {

            var email = $("#showUserSearchLogin").val();
            if (email.length < 3) {
                email = null;
            }
            var status = $("#showUserSearchState").val();
            if (status == "") {
                status = null;
            }

            var createOnFrom = $("#showUserSearchCreateDateFrom").val();
            if (createOnFrom == "") {
                createOnFrom = null;
            }

            var createOnTo = $("#showUserSearchCreateDateTo").val();
            if (createOnTo == "") {
                createOnTo = null;
            }

            var params = {};

            if (email != null) {
                params.email = email;
            }
            if (status != null) {
                params.status = status;
            }
            if (createOnFrom != null) {
                params.createOnFrom = createOnFrom;
            }
            if (createOnTo != null) {
                params.createOnTo = createOnTo;
            }

            ShowUserSearch.needRefresh = false;
            ShowUserSearch.needRefresh |= (ShowUserSearch.params.email != params.email);
            ShowUserSearch.needRefresh |= (ShowUserSearch.params.status != params.status);
            ShowUserSearch.needRefresh |= (ShowUserSearch.params.createOnFrom != params.createOnFrom);
            ShowUserSearch.needRefresh |= (ShowUserSearch.params.createOnTo != params.createOnTo);
            ShowUserSearch.params = params;
        },
        doSearch:function () {
            if (ShowUserSearch.needRefresh)
                $("#list").trigger("reloadGrid");
        }

    };


    var lastsel2;

    var edo = $.jgrid.BuildEditOptions('${CONTEXT}/admin/user/update');

    $(function () {
        $("#list").jqGrid({
            multiselect:false,
            url:'${CONTEXT}/admin/user/list',
            //editurl:'${CONTEXT}/admin/user/update',
            colNames:['ID', 'E-mail', 'Состояние'],
            colModel:[
                {name:'id', width:20},
                {name:'email', width:200, editrules:{required:true}},
                {name:'status', width:200, formatter:'select', edittype:"select",
                    editoptions:{ value:"0:Новый;1:Активный;2:Заблокирован" },
                    editable:true
                }
            ],
            ondblClickRow:function (id, row, col, e) {
                $(this).jqGrid('editGridRow', id, edo);
            },
            serializeGridData:function (postData) {
                // adapt jqGrid postData format to self-api
                var data = {};
                if (this.p.rowNum > -1) {
                    data.limit = postData.rows;
                    data.offset = (postData.page - 1) * postData.rows;
                }
                if (typeof postData.sidx !== "undefined" && postData.sidx.length > 0) {
                    data.sort = postData.sidx;
                    data.dir = postData.sord;
                }
                var params = ShowUserSearch.params;
                data = $.extend(data, params);
                return data;
            },

            pager:'#pager'

        });
        $("#list").jqGrid('navGrid', '#pager', {edit:true, add:false, del:false, search:true}, edo, {}, {}, {}, {});

        $("#showSearchUserContainer input, #showSearchUserContainer select").blur(function () {
            ShowUserSearch.updateParams();
            ShowUserSearch.doSearch();
        });

        $("#showSearchUserContainer #showUserSearchCreateDateFrom, #showSearchUserContainer #showUserSearchCreateDateTo").datepicker({
            onSelect:function (dateText, inst) {
                ShowUserSearch.updateParams();
                ShowUserSearch.doSearch();
                $(this).datepicker("hide");

            }
        });

    });
</script>

<div id="showUser">

    <div id="showSearchUserContainer">
        <label for="showUserSearchLogin"><spring:message code="showUser.search.login.name"/></label>
        <input id="showUserSearchLogin" type="text"/>
        <label for="showUserSearchCreateDate"><spring:message code="showUser.search.createDateFrom.name"/></label>
        <input id="showUserSearchCreateDateFrom" type="text"/>
        <label for="showUserSearchCreateDate"><spring:message code="showUser.search.createDateTo.name"/></label>
        <input id="showUserSearchCreateDateTo" type="text"/>
        <label for="showUserSearchState"><spring:message code="showUser.search.state.name"/></label>
        <select id="showUserSearchState">
            <option value=""></option>
            <c:forTokens items="0,1,2" delims="," var="i">
                <option value="${i}"><spring:message code="showUser.search.state.${i}"/></option>
            </c:forTokens>
        </select>
    </div>

    <table id="list">
    </table>

    <div id="pager"></div>
</div>
