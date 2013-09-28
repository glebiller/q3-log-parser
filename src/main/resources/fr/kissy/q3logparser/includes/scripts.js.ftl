$.extend(true, $.fn.dataTable.defaults, {
    "sDom": "<'row'<'col-md-12'f>r>t"
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
    },
    "short-date-asc": function(x, y) {
        return parseInt(x.split('/').reverse().join(''), 10) - parseInt(y.split('/').reverse().join(''), 10);
    },
    "short-date-desc": function(x, y) {
        return parseInt(y.split('/').reverse().join(''), 10) - parseInt(x.split('/').reverse().join(''), 10);
    }
});
$.extend($.fn.dataTableExt.ofnSearch, {
    "html-badge": function (sData) {
        console.log(sData);
        return $(sData).text();
    }
});
$(document).ready(function() {
    $('#match-results').dataTable({
        "bPaginate": false,
        "bFilter": true,
        "bSort": true,
        "aoColumnDefs": [
            {"sType": "html-badge", "aTargets": [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]}
        ],
        "aaSorting": [[2, "desc"]]
    });
    $('#matches-list').dataTable({
        "bPaginate": false,
        "bFilter": true,
        "bSort": true,
        "aoColumnDefs": [
            {"sType": "short-date", "aTargets": [0]}
        ],
        "aaSorting": [[0, "desc"]]
    });
    $('#stats-list').dataTable({
        "bPaginate": false,
        "bFilter": true,
        "bSort": true,
        "aoColumnDefs": [
            {"sType": "html-badge", "aTargets": [1, 2, 3, 4, 5, 6, 7]}
        ],
        "aaSorting": [[0, "desc"]]
    });
});