const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        mealsFilter()
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#mealtable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    );
});

function mealsFilter() {
    let filterform = $('#filterform');
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: filterform.serialize()
    }).done(function (data) {
        updateDataTable(data);
        successNoty("Filtered");
    });
}

function clearFilter() {
    $('#filterform').get(0).reset();
    mealsFilter();
}