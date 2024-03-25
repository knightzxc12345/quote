let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';
let globalVendor = '';
let globalVendorSelect;

window.onload = function () {
    init();
    searchEnter();
    selectChange();
    getVendors();
    offcanvasEvent();
    pageEvent();
    selectChange();
};

function searchEnter(){
    $("#item-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function selectChange(){
    $('#vendor-name-select').off().change(function() {
        let value = $(this).val();
        globalVendorSelect = 'all' == value ? null : value;
        getItems();
    });
}

function search(){
    globalKeyword = $("#item-search-input").val();
    getItems();
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#itemList').on('click', '.get-update-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        const vendorUuid = jsonData.vendorUuid;
        $('#update-item-vendor option').each(function() {
            console.log($(this).val());
            if($(this).val() == vendorUuid) {
                $(this).prop('selected', true);
            }
        });
        $('#update-item-uuid').val(jsonData.itemUuid);
        $('#update-item-name').val(jsonData.name);
    });
    $('#itemList').on('click', '.get-delete-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-item-uuid').val(jsonData.itemUuid);
    });
}

function pageEvent(){
    $(document).on("click", ".page-item", function() {
        let pageVal = $(this).find(".page-link").data('val');
        if('pre' == pageVal){
            if(0 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow -= 1;
            getItems();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            getItems();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        getItems();
    });
}

function getVendors(){
    $.ajax({
        url: `/common/vendor/v1`,
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00002') {
                alertError('系統錯誤');
                return;
            }
            // 空陣列
            if ($.isEmptyObject(response.data)) {
                return;
            }
            globalVendor = response.data;
            $('#vendor-name-select').append(`
                <option value='all'>全部</option>
            `);
            $.each(response.data, function(key, value) {
                $('#vendor-name-select').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
                $('#add-item-vendor').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
                $('#update-item-vendor').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
            });
            getItems(globalPageNow, globalPageSize);
        },
        error: function (xhr, status, error) {
            let code = xhr.responseJSON.code;
            if (code == 'A00006') {
                goBack();
                return;
            }
            console.log(jsonResponse);
        }
    });
}

function getItems() {
    let url = `/item/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
    if(!isEmpty(globalVendorSelect)){
        url += `&vendorUuid=${globalVendorSelect}`;
    }
    $.ajax({
        url: url,
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00002') {
                alertError('系統錯誤');
                return;
            }
            // 空陣列
            if ($.isEmptyObject(response.data)) {
                return;
            }
            $("#item-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                let vendorName = findVendorName(value.vendorUuid);
                $("#item-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${vendorName}</td>
                        <td>${value.name}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-item-json' data-bs-toggle='offcanvas' data-bs-target='#update-item' aria-controls='update-item'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-item-json' data-bs-toggle="modal" data-bs-target="#delete-item-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#itemListPage', pageTotal, pageNow);
        },
        error: function (xhr, status, error) {
            let code = xhr.responseJSON.code;
            if (code == 'A00006') {
                goBack();
                return;
            }
            console.log(jsonResponse);
        }
    });
}

function findVendorName(vendorUuid) {
    for (let i = 0; i < globalVendor.length; i++) {
        if (globalVendor[i].vendorUuid === vendorUuid) {
            return globalVendor[i].name;
        }
    }
    return null;
}

function addItem() {
    const vendorUuid = $("#add-item-vendor").val();
    const itemUuid = $("#add-item-vendor").val();
    const name = $("#add-item-name").val();
    // 驗證
    const vendorUuidValid = validateInput(vendorUuid, "#add-item-vendor-uuid");
    const nameValid = validateInput(name, "#add-item-name");
    if (!vendorUuidValid || !nameValid) {
        return;
    }
    let data = {
        vendorUuid: vendorUuid,
        name: name
    };
    $.ajax({
        url: '/item/v1',
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'POST',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00003') {
                alertError('系統錯誤');
                return;
            }
            location.reload();
        },
        error: function (xhr, status, error) {
            let code = xhr.responseJSON.code;
            let message = xhr.responseJSON.message;
            if (code == 'A00006') {
                goBack();
                return;
            }
            alertError(message);
        }
    });
}

function updateItem() {
    const itemUuid = $('#update-item-uuid').val();
    const vendorUuid = $("#update-item-vendor").val();
    const name = $("#update-item-name").val();
    // 驗證
    const vendorUuidValid = validateInput(vendorUuid, "#update-item-vendor-uuid");
    const nameValid = validateInput(name, "#update-item-name");
    if (!vendorUuidValid || !nameValid) {
        return;
    }
    let data = {
        vendorUuid: vendorUuid,
        name: name
    };
    $.ajax({
        url: '/item/v1/' + itemUuid,
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'PUT',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00004') {
                alertError('系統錯誤');
                return;
            }
            location.reload();
        },
        error: function (xhr, status, error) {
            let code = xhr.responseJSON.code;
            let message = xhr.responseJSON.message;
            if (code == 'A00006') {
                goBack();
                return;
            }
            alertError(message);
        }
    });
}

function deleteItem(){
    const itemUuid = $('#delete-item-uuid').val();
    $.ajax({
        url: '/item/v1/' + itemUuid,
        contentType: 'application/json',
        type: 'DELETE',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00005') {
                alertError('系統錯誤');
                return;
            }
            location.reload();
        },
        error: function (xhr, status, error) {
            let code = xhr.responseJSON.code;
            let message = xhr.responseJSON.message;
            if (code == 'A00006') {
                goBack();
                return;
            }
            alertError(message);
        }
    });
}

function validateInput(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    element.removeClass("is-invalid").addClass("is-valid");
    return true;
}