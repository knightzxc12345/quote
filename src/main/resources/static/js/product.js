let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';
let globalItem = '';
let globalVendor = '';
let globalVendorSelect;

window.onload = function () {
    init();
    getItems();
    offcanvasEvent();
    pageEvent();
    searchEnter();
    selectChange();
    inputChange();
    getVendors();
    $('.selectpicker').selectpicker();
};

function searchEnter(){
    $("#product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function selectChange(){
    $('#vendor-name-select').change(function() {
        let value = $(this).val();
        globalVendorSelect = 'all' == value ? null : value;
        getProducts();
    });
}

function inputChange(){
    $("#add-product-unit-price").change(function() {
        $(this).val(updateValue($(this)));
    });
    $("#add-product-cost-price").change(function() {
        $(this).val(updateValue($(this)));
    });
    $("#update-product-unit-price").change(function() {
        $(this).val(updateValue($(this)));
    });
    $("#update-product-cost-price").change(function() {
        $(this).val(updateValue($(this)));
    });
}

function updateValue(element){
    let value = $(element).val().replace(/,/g, '');
    if(!/^\d+$/.test(value)){
        return "1";
    }
    return Number(value).toLocaleString();
}

function search(){
    globalKeyword = $("#product-search-input").val();
    getProducts();
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#productList').on('click', '.get-update-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        const itemUuid = jsonData.itemUuid;
        const vendorUuids = jsonData.vendorUuids;
        $('#update-product-item').selectpicker('val', itemUuid);
        $('#update-product-vendor').selectpicker('val', vendorUuids);
        $('#update-product-uuid').val(jsonData.productUuid);
        $('#update-product-name').val(jsonData.name);
        $('#update-product-specification').val(jsonData.specification);
        $('#update-product-unit').val(jsonData.unit);
        $('#update-product-unit-price').val(jsonData.unitPrice);
        $('#update-product-cost-price').val(jsonData.costPrice);
    });
    $('#productList').on('click', '.get-delete-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-product-uuid').val(jsonData.productUuid);
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
            getProducts();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            getProducts();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        getProducts();
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
                <option value='all' selected>全部</option>
            `);
            $.each(response.data, function(key, value) {
                let selected = key == 0 ? 'selected' : '';
                $('#vendor-name-select').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
                $('#add-product-vendor').append(`
                    <option value='${value.vendorUuid}' ${selected}>${value.name}</option>
                `);
                $('#update-product-vendor').append(`
                    <option value='${value.vendorUuid}' ${selected}>${value.name}</option>
                `);
            });
            $('#vendor-name-select').selectpicker('refresh');
            $('#vendor-name-select').selectpicker('render');
            $('#add-product-vendor').selectpicker('refresh');
            $('#add-product-vendor').selectpicker('render');
            $('#update-product-vendor').selectpicker('refresh');
            $('#update-product-vendor').selectpicker('render');
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

function getItems(){
    $.ajax({
        url: `/common/item/v1`,
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
            globalItem = response.data;
            addProductItem();
            updateProductItem();
            getProducts(globalPageNow, globalPageSize);
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

function getProducts() {
    let url = `product/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
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
            $("#product-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                let itemName = findItemName(value.itemUuid);
                let unitPriceFormatted = value.unitPrice.toLocaleString();
                let costPriceFormatted = value.costPrice.toLocaleString();
                let vendorName = getVendorName(value.vendorUuids);
                $("#product-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.itemNo}</td>
                        <td>${itemName}</td>
                        <td>${value.specification}</td>
                        <td>${value.unit}</td>
                        <td>${unitPriceFormatted}</td>
                        <td>${costPriceFormatted}</td>
                        <td>${vendorName}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-product-json' data-bs-toggle='offcanvas' data-bs-target='#update-product' aria-controls='update-product'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-product-json' data-bs-toggle="modal" data-bs-target="#delete-product-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#productListPage', pageTotal, pageNow);
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

function getVendorName(vendorUuids) {
    let vendorNames = [];
    vendorUuids.forEach(function(vendorUuid) {
        let vendor = globalVendor.find(function(vendor) {
            return vendor.vendorUuid === vendorUuid;
        });
        if (vendor) {
            vendorNames.push(vendor.name);
        }
    });
    return vendorNames.join(",");
}

function addProductItem(){
    let item = $('#add-product-item');
    item.empty();
    $.each(globalItem, function(key, value) {
        item.append(`
            <option value='${value.itemUuid}'>${value.itemNo}-${value.name}</option>
        `);
    });
    $('#add-product-item').selectpicker('refresh');
    $('#add-product-item').selectpicker('render');
}

function updateProductItem(){
    let item = $('#update-product-item');
    item.empty();
    $.each(globalItem, function(key, value) {
        item.append(`
            <option value='${value.itemUuid}'>${value.itemNo}-${value.name}</option>
        `);
    });
    $('#update-product-item').selectpicker('refresh');
    $('#update-product-item').selectpicker('render');
}

function findItemName(itemUuid) {
    for (let i = 0; i < globalItem.length; i++) {
        if (globalItem[i].itemUuid === itemUuid) {
            return globalItem[i].name;
        }
    }
    return null;
}

function addProduct() {
    const itemUuid = $("#add-product-item").val();
    const specification = $("#add-product-specification").val();
    const unit = $("#add-product-unit").val();
    const unitPrice = $("#add-product-unit-price").val().replace(/,/g, '');
    const costPrice = $("#add-product-cost-price").val().replace(/,/g, '');
    const vendors = $('#add-product-vendor').val();
    // 驗證
    const specificationValid = validateInput(specification, "#add-product-specification");
    const unitValid = validateInput(unit, "#add-product-unit");
    const unitPriceValid = validateNumberInput(unitPrice, "#add-product-unit-price");
    const costPriceValid = validateNumberInput(costPrice, "#add-product-cost-price");
    const vendorValid = validateSelect(vendors, "#add-product-vendor");
    if (!specificationValid || !unitValid || !unitPriceValid || !costPriceValid || !vendorValid) {
        return;
    }
    let data = {
        itemUuid: itemUuid,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        costPrice: costPrice,
        vendors: vendors
    };
    $.ajax({
        url: '/product/v1',
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

function updateProduct() {
    const productUuid = $('#update-product-uuid').val();
    const itemUuid = $("#update-product-item").val();
    const specification = $("#update-product-specification").val();
    const unit = $("#update-product-unit").val();
    const unitPrice = $("#update-product-unit-price").val().replace(/,/g, '');
    const costPrice = $("#update-product-cost-price").val().replace(/,/g, '');
    const vendors = $('#update-product-vendor').val();
    // 驗證
    const specificationValid = validateInput(specification, "#update-product-specification");
    const unitValid = validateInput(unit, "#update-product-unit");
    const unitPriceValid = validateNumberInput(unitPrice, "#update-product-unit-price");
    const costPriceValid = validateNumberInput(costPrice, "#update-product-cost-price");
    const vendorValid = validateSelect(vendors, "#update-product-vendor");
    if (!specificationValid || !unitValid || !unitPriceValid || !costPriceValid || !vendorValid) {
        return;
    }
    let data = {
        itemUuid: itemUuid,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        costPrice: costPrice,
        vendors: vendors
    };
    $.ajax({
        url: '/product/v1/' + productUuid,
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

function deleteProduct(){
    const productUuid = $('#delete-product-uuid').val();
    $.ajax({
        url: '/product/v1/' + productUuid,
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

function validateNumberInput(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    if (!/^\d+$/.test(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    element.removeClass("is-invalid").addClass("is-valid");
    return true;
}

function validateSelect(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        alertWarning("廠商至少要選一個");
        return false;
    }
    return true;
}