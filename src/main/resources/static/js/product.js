let globalKeyword = '';
let globalItem = '';
let globalVendor = '';
let globalVendorSelect;

window.onload = function () {
    init();
    getVendors();
    selectChange();
    inputChange();
    searchEnter();
    pageEvent(getProducts);
    offcanvasEvent();
};

// 點擊搜尋
function searchEnter(){
    $("#product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

// 搜尋變更
function selectChange(){
    $('#vendor-name-search-select').change(function() {
        let value = $(this).val();
        globalVendorSelect = 'all' == value ? null : value;
        getProducts();
    });
}

// 更新輸入數字欄位
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

// 更新輸入數字欄位值
function updateValue(element){
    let value = $(element).val().replace(/,/g, '');
    if(!/^\d+$/.test(value)){
        return "";
    }
    return Number(value).toLocaleString();
}

// 搜尋
function search(){
    globalKeyword = $("#product-search-input").val();
    getProducts();
}

// 事件配置
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

// 關閉新增畫布
function closeAdd() {
    let offcanvasElement = document.getElementById('add-product');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#add-product input').val('');
    $('#add-product input').removeClass('is-valid');
    $('#add-product input').removeClass('is-invalid');
    $('#add-product select').prop('selectedIndex', 0);
}

// 關閉更新畫布
function closeUpdate() {
    let offcanvasElement = document.getElementById('update-product');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#update-product input').removeClass('is-valid');
    $('#update-product input').removeClass('is-invalid');
}

// 關閉刪除畫布
function closeDelete() {
    let modalElement = document.getElementById('delete-product');
    let modal = bootstrap.Modal.getInstance(modalElement);
    if (!modal) {
        modal = new bootstrap.Modal(modalElement);
    }
    modal.hide();
}

// 取得廠商清單
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
            let vendorSelect = $('#vendor-name-search-select');
            let addProductVendor = $('.add-product-vendor');
            let updateProductVendor = $('.update-product-vendor');
            vendorSelect.append(`
                <option value='all' selected>全部</option>
            `);
            $.each(response.data, function(key, value) {
                let selected = key == 0 ? 'selected' : '';
                vendorSelect.append(`
                    <option value='${value.vendorUuid}'>${value.name}</option>
                `);
                addProductVendor.append(`
                    <option value='${value.vendorUuid}' ${selected}>${value.name}</option>
                `);
                updateProductVendor.append(`
                    <option value='${value.vendorUuid}' ${selected}>${value.name}</option>
                `);
            });
            vendorSelect.selectpicker('refresh');
            vendorSelect.selectpicker('render');
            addProductVendor.selectpicker('refresh');
            addProductVendor.selectpicker('render');
            updateProductVendor.selectpicker('refresh');
            updateProductVendor.selectpicker('render');
            getItems();
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

// 取得項目清單
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
            getProducts();
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

// 新增產品項目
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

// 更新產品項目
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

// 取得產品清單
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
                let vendorName = findVendorName(value.vendorUuids);
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
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-product-json' data-bs-toggle="modal" data-bs-target="#delete-product">刪除</button>
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

// 取得項目名稱
function findItemName(itemUuid) {
    for (let i = 0; i < globalItem.length; i++) {
        if (globalItem[i].itemUuid === itemUuid) {
            return globalItem[i].name;
        }
    }
    return null;
}

// 取得廠商名稱
function findVendorName(vendorUuids) {
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

// 新增產品
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
            closeAdd();
            search();
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

// 更新產品
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
            closeUpdate();
            search();
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

// 刪除產品
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
            closeDelete();
            search();
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