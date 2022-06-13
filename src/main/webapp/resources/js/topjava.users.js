const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, function (data) {
            updateDataTable(data);
        })
    }
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

function enable(checkbox) {
    let userId = $(checkbox).closest('tr').prop("id");
    let checked = checkbox.checked;
    $.ajax({
        type: "POST",
        url: ctx.ajaxUrl + userId,
        data: {'enabled': checked}
    }).done(function () {
        if (!checked) {
            $(checkbox).closest('tr').css('opacity', "30%");
            successNoty("Record is deactivated");
        } else {
            $(checkbox).closest('tr').css('opacity', "100%");
            successNoty("Record is activated");
        }
    }).fail(function () {
        $(checkbox).prop('checked', !checked);
    });
}