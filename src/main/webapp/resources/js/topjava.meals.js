const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    },
    detailsFormToSerialize: function () {
      /*  let dateTime = $('#dateTime');
        let dateTimeToSerialize = dateTime.val().replace(" ", "T");
        dateTime.val(dateTimeToSerialize);*/
        return form.serialize() + "&excess=false"
    },
    parseValue: function (key, value) {
        if (key === 'dateTime') {
            value = value.replace("T", " ");
        }
        return value;
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
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
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (!data.excess) {
                    $(row).attr("data-meal-excess", false);
                } else {
                    $(row).attr("data-meal-excess", true);
                }
            }
        })
    );
});

$('#startDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});
$('#endDate').datetimepicker({
    timepicker: false,
    format: 'Y-m-d'
});
$('#startTime').datetimepicker({
    datepicker: false,
    format: 'H:i'
});
$('#endTime').datetimepicker({
    datepicker: false,
    format: 'H:i'
});
$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});