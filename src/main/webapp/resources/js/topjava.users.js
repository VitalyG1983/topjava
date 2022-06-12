const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function postRequest(checkbox) {
    let tr = $(checkbox).closest('tr');
    let userId = tr.prop("id");
    let tdCells = tr.find('td');
    moment('22-04-2010').format('MM/DD/YYYY');
    moment.locale()
   // $.datepicker.parseDate('dd-mm-yy', '22-04-2010');
   /* $.datepicker.parseDate('22/04/2010','dd/mm/yy');*/
    /*  let d = $.datepicker.parseDate("dd-MM-yyyy", tdCells.eq(4).text());
    let dd = $.datepicker.parseDate(tdCells.eq(4).text(),"dd-MMMM-yyyy");*/
    /*    let format = moment(tdCells.eq(4).text().format('YYYY/MM/DD'));
        let format = moment(tdCells.eq(4).text().for;*/
    /*   let values = "";
       jQuery.each($columns, function (i, item) {
           values = values + 'td' + (i + 1) + ':' + item.innerHTML + '\n';
       });
       console.log(values);*/
    $.post(ctx.ajaxUrl + userId, {
            // 'id': userId,
            'name': tdCells.eq(0).text(),
            'email': tdCells.eq(1).text(),
            'password': tdCells.eq(2).text(),
            'enabled': checkbox.checked,
            'registered': tdCells.eq(4).text(),
        },
        function () {
            updateTable();
            successNoty("Updated checkbox");
        });
}