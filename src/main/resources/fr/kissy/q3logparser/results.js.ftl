$.extend(true, $.fn.dataTable.defaults, {
    "sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>"
});
$.extend($.fn.dataTableExt.oStdClasses, {
    "sWrapper": "dataTables_wrapper form-inline"
});
$.extend($.fn.dataTableExt.oSort, {
    "html-badge-asc": function(x, y) {
        return $(x).text() - $(y).text();
    },
    "html-badge-desc": function(x, y) {
        return $(y).text() - $(x).text();
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
            {"sType": "html", "aTargets": [0, 1]},
            {"sType": "html-badge", "aTargets": [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]}
        ],
        "aaSorting": [[ 3, "desc" ]]
    });
} );