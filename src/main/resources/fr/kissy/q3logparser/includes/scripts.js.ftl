$.extend(true, $.fn.dataTable.defaults, {
    "sDom": "<'row-fluid'<'span12'f>r>t"
});
$.extend($.fn.dataTableExt.oStdClasses, {
    "sWrapper": "dataTables_wrapper form-inline"
});
$.extend($.fn.dataTableExt.oSort, {
    "html-badge-asc": function(x, y) {
        return parseFloat($(x).text()) - parseFloat($(y).text());
    },
    "html-badge-desc": function(x, y) {
        return parseFloat($(y).text()) - parseFloat($(x).text());
    }
});
$.extend($.fn.dataTableExt.ofnSearch, {
    "html-badge": function (sData) {
        console.log(sData);
        return $(sData).text();
    }
});
$(document).ready(function() {
    $('#game-results').dataTable({
        "bPaginate": false,
        "bFilter": true,
        "bSort": true,
        "aoColumnDefs": [
            {"sType": "html-badge", "aTargets": [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]}
        ],
        "aaSorting": [[2, "desc"]]
    });
});