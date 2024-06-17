let globalKeyword = '';
let globalVendor = '';
let globalVendorSelect;

window.onload = function () {
    init();
    getVendors();
    selectChange();
    inputChange();
    searchEnter();
    pageEvent(getVendorProducts);
    offcanvasEvent();
};

// 事件配置
function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#vendorProductList').on('click', '.get-update-vendor-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-vendor-product-uuid').val(jsonData.vendorProductUuid);
        $('#update-vendor-product-vendor').val(jsonData.vendorUuid);
        $('#update-vendor-product-name').val(jsonData.name);
        $('#update-vendor-product-unit-price').val(Number(jsonData.unitPrice).toLocaleString());
    });
    $('#vendorProductList').on('click', '.get-delete-vendor-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-vendor-product-uuid').val(jsonData.vendorProductUuid);
    });
}

function searchEnter(){
    $("#vendor-product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function search(){
    globalKeyword = $("#vendor-product-search-input").val();
    getVendorProducts();
}

// 搜尋變更
function selectChange(){
    $('#vendor-name-search-select').change(function() {
        let value = $(this).val();
        globalVendorSelect = 'all' == value ? null : value;
        getVendorProducts();
    });
}

// 輸入框改變
function inputChange(){
    $("#add-vendor-product-unit-price").change(function() {
        $(this).val(updateValue($(this)));
    });
    $("#update-vendor-product-unit-price").change(function() {
        $(this).val(updateValue($(this)));
    });
}

// 更新輸入框數字
function updateValue(element){
    let value = $(element).val().replace(/,/g, '');
    if(!/^\d+$/.test(value)){
        return "";
    }
    return Number(value).toLocaleString();
}

// 取得廠商
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
            updateVendorSelect();
            getVendorProducts();
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

// 更新廠商選單
function updateVendorSelect(){
    let vendorSelect = $('#vendor-name-search-select');
    let addItem = $('#add-vendor-product-vendor');
    let updateItem = $('#update-vendor-product-vendor');
    addItem.empty();
    updateItem.empty();
    vendorSelect.append(`
        <option value='all' selected>全部</option>
    `);
    $.each(globalVendor, function(key, value) {
        vendorSelect.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
        addItem.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
        updateItem.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
    });
    vendorSelect.selectpicker('refresh');
    vendorSelect.selectpicker('render');
    addItem.selectpicker('refresh');
    addItem.selectpicker('render');
    updateItem.selectpicker('refresh');
    updateItem.selectpicker('render');
}

// 取得廠商產品清單
function getVendorProducts() {
    let url = `/vendor-product/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
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
            $("#vendor-product-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                let vendorName = findVendorName(value.vendorUuid);
                let unitPriceFormatted = value.unitPrice.toLocaleString();
                $("#vendor-product-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${vendorName}</td>
                        <td>${value.name}</td>
                        <td>${unitPriceFormatted}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-vendor-product-json' data-bs-toggle='offcanvas' data-bs-target='#update-vendor-product' aria-controls='update-vendor-product'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-vendor-product-json' data-bs-toggle="modal" data-bs-target="#delete-vendor-product-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#vendorProductListPage', pageTotal, pageNow);
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

// 取得廠商名稱
function findVendorName(vendorUuid) {
    for (let i = 0; i < globalVendor.length; i++) {
        if (globalVendor[i].vendorUuid === vendorUuid) {
            return globalVendor[i].name;
        }
    }
    return null;
}

// 新增廠商產品
function addVendorProduct() {
    const vendorUuid = $("#add-vendor-product-vendor").val();
    const name = $("#add-vendor-product-name").val();
    const unitPrice = $("#add-vendor-product-unit-price").val().replace(/,/g, '');
    // 驗證
    const nameValid = validateInput(name, "#add-vendor-product-name");
    const unitPriceValid = validateNumberInput(unitPrice, "#add-vendor-product-unit-price");
    if (!nameValid && !unitPriceValid) {
        return;
    }
    let data = {
        vendorUuid: vendorUuid,
        name: name,
        unitPrice: unitPrice,
    };
    $.ajax({
        url: '/vendor-product/v1',
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

// 更新廠商產品
function updateVendorProduct() {
    const vendorProductUuid = $('#update-vendor-product-uuid').val();
    const vendorUuid = $("#update-vendor-product-vendor").val();
    const name = $("#update-vendor-product-name").val();
    const unitPrice = $("#update-vendor-product-unit-price").val().replace(/,/g, '');
    // 驗證
    const nameValid = validateInput(name, "#add-vendor-product-name");
    const unitPriceValid = validateNumberInput(unitPrice, "#add-vendor-product-unit-price");
    if (!nameValid && !unitPriceValid) {
        return;
    }
    let data = {
        vendorUuid: vendorUuid,
        name: name,
        unitPrice: unitPrice,
    };
    $.ajax({
        url: '/vendor-product/v1/' + vendorProductUuid,
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

// 刪除廠商產品
function deleteVendorProduct(){
    const vendorUuid = $('#delete-vendor-product-uuid').val();
    $.ajax({
        url: '/vendor-product/v1/' + vendorUuid,
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