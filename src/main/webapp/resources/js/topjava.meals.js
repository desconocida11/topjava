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
                    "defaultContent": "Update",
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

function filter() {
    const form = $("#filterForm");
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: form.serialize()
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Data filtered");
    });
}

function updateTable() {
    let isFiltered = false;
    $('#filterForm').find(":input").each(function() {
        if($(this).val() !== "")
            isFiltered = true;
    });
    if (isFiltered === true) {
        filter();
    }
    else {
        $.get(ctx.ajaxUrl, function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        });
    }
}

function cancelFilter() {
    $('#filterForm').find(":input").val("");
    updateTable();
}