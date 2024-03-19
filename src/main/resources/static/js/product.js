let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';
let globalVendor = '';

window.onload = function () {
    init();
    getVendors();
    offcanvasEvent();
    pageEvent();
    searchEnter();
};

function searchEnter(){
    $("#product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
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
        const vendorUuid = jsonData.vendorUuid;
        $('#update-product-vendor option').each(function() {
            console.log($(this).val());
            if($(this).val() == vendorUuid) {
                $(this).prop('selected', true);
            }
        });
        $('#update-product-uuid').val(jsonData.productUuid);
        $('#update-product-no').val(jsonData.no);
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
        url: `vendor/v1`,
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
            $.each(response.data, function(key, value) {
                $('#add-product-vendor').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
                $('#update-product-vendor').append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
            });
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
    $.ajax({
        url: `product/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`,
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
                let vendorName = findVendorName(value.vendorUuid);
                let unitPriceFormatted = value.unitPrice.toLocaleString();
                let costPriceFormatted = value.costPrice.toLocaleString();
                $("#product-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${vendorName}</td>
                        <td>${value.no}</td>
                        <td>${value.name}</td>
                        <td>${value.specification}</td>
                        <td>${value.unit}</td>
                        <td>${unitPriceFormatted}</td>
                        <td>${costPriceFormatted}</td>
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

function findVendorName(vendorUuid) {
    for (var i = 0; i < globalVendor.length; i++) {
        if (globalVendor[i].vendorUuid === vendorUuid) {
            return globalVendor[i].name;
        }
    }
    return null;
}

function addProduct() {
    const vendorUuid = $("#add-product-vendor").val();
    const no = $("#add-product-no").val();
    const name = $("#add-product-name").val();
    const specification = $("#add-product-specification").val();
    const unit = $("#add-product-unit").val();
    const unitPrice = $("#add-product-unit-price").val();
    const costPrice = $("#add-product-cost-price").val();
    // 驗證
    const vendorUuidValid = validateInput(vendorUuid, "#add-product-vendor-uuid");
    const noValid = validateInput(no, "#add-product-no");
    const nameValid = validateInput(name, "#add-product-name");
    const specificationValid = validateInput(specification, "#add-product-specification");
    const unitValid = validateInput(unit, "#add-product-unit");
    const unitPriceValid = validateNumberInput(unitPrice, "#add-product-unit-price");
    const costPriceValid = validateNumberInput(costPrice, "#add-product-cost-price");
    if (!vendorUuidValid || !noValid || !nameValid || !specificationValid || !unitValid || !unitPriceValid || !costPriceValid) {
        return;
    }
    var data = {
        vendorUuid: vendorUuid,
        no: no,
        name: name,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        costPrice: costPrice
    };
    $.ajax({
        url: 'product/v1',
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
    const vendorUuid = $("#update-product-vendor").val();
    const no = $("#update-product-no").val();
    const name = $("#update-product-name").val();
    const specification = $("#update-product-specification").val();
    const unit = $("#update-product-unit").val();
    const unitPrice = $("#update-product-unit-price").val().replace(/,/g, '');
    const costPrice = $("#update-product-cost-price").val().replace(/,/g, '');
    // 驗證
    const vendorUuidValid = validateInput(vendorUuid, "#update-product-vendor-uuid");
    const noValid = validateInput(no, "#update-product-no");
    const nameValid = validateInput(name, "#update-product-name");
    const specificationValid = validateInput(specification, "#update-product-specification");
    const unitValid = validateInput(unit, "#update-product-unit");
    const unitPriceValid = validateNumberInput(unitPrice, "#update-product-unit-price");
    const costPriceValid = validateNumberInput(costPrice, "#update-product-cost-price");
    if (!vendorUuidValid || !noValid || !nameValid || !specificationValid || !unitValid || !unitPriceValid || !costPriceValid) {
        return;
    }
    var data = {
        vendorUuid: vendorUuid,
        no: no,
        name: name,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        costPrice: costPrice
    };
    $.ajax({
        url: 'product/v1/' + productUuid,
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
        url: 'product/v1/' + productUuid,
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