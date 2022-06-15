const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    updateTable: function () {
        $.get(userAjaxUrl, updateDataTable
        )
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
        //type: "POST",
        // url: ctx.ajaxUrl + userId,
        //data: {'enabled': checked}
        type: "PATCH",
        url: ctx.ajaxUrl + userId + "?enabled=" + checked
    }).done(function () {
        $(checkbox).closest('tr').attr("data-user-enable", checked);
        successNoty(!checked ? "Record is deactivated" : "Record is activated");
    }).fail(function () {
        $(checkbox).prop('checked', !checked);
    });
}