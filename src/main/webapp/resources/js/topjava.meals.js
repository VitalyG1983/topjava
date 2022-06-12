const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
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
                    "asc"
                ]
            ]
        })
    );
});

function mealsFilter() {
    let filterform = $('#filterform');
    $.get(ctx.ajaxUrl + "filter", {
            'startDate': filterform.prop('startDate').value,
            'endDate': filterform.prop('endDate').value,
            'startTime': filterform.prop('startTime').value,
            'endTime': filterform.prop('endTime').value
        },
        function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
            successNoty("Filtered");
        });
}

function clearFilter() {
    document.getElementById("filterform").reset();
}