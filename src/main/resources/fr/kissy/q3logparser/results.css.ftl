.force-center,
.force-center td,
.force-center th,
 td.dataTables_empty {
    text-align: center !important;
}

div.dataTables_filter label {
	float: right;
}

table.table {
	clear: both;
	margin-bottom: 6px !important;
	max-width: none !important;
}

table.table thead .sorting,
table.table thead .sorting_asc,
table.table thead .sorting_desc,
table.table thead .sorting_asc_disabled,
table.table thead .sorting_desc_disabled {
	cursor: pointer;
	*cursor: hand;
}

table.table thead .sorting { background: url('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/images/sort_both.png') no-repeat center right; }
table.table thead .sorting_asc { background: url('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/images/sort_asc.png') no-repeat center right; }
table.table thead .sorting_desc { background: url('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/images/sort_desc.png') no-repeat center right; }

table.table thead .sorting_asc_disabled { background: url('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/images/sort_asc_disabled.png') no-repeat center right; }
table.table thead .sorting_desc_disabled { background: url('http://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.4/images/sort_desc_disabled.png') no-repeat center right; }

table.dataTable th:active {
	outline: none;
}
