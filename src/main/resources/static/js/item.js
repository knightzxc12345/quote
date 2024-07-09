let globalKeyword = '';
let globalVendor = '';
let globalVendorProduct = '';

window.onload = function () {
    init();
    getVendors();
    getItems();
    searchEnter();
    pageEvent(getItems);
    offcanvasEvent();
    selectChange();
    inputChange();
};

// 點擊搜尋
function searchEnter(){
    $("#item-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

// 搜尋
function search(){
    globalKeyword = $("#item-search-input").val();
    getItems();
}

// 關閉新增畫布
function closeAdd() {
    let offcanvasElement = document.getElementById('add-item');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#add-item input').val('');
    $('#add-item input').removeClass('is-valid');
    $('#add-item input').removeClass('is-invalid');
    $('#item-vendor-product-tbody').empty();
    setSelect();
}

// 關閉更新畫布
function closeUpdate() {
    let offcanvasElement = document.getElementById('update-item');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#update-item input').removeClass('is-valid');
    $('#update-item input').removeClass('is-invalid');
}

// 關閉刪除畫布
function closeDelete() {
    let modalElement = document.getElementById('delete-item');
    let modal = bootstrap.Modal.getInstance(modalElement);
    if (!modal) {
        modal = new bootstrap.Modal(modalElement);
    }
    modal.hide();
}

// 事件配置
function offcanvasEvent(){
    $('#itemList').on('click', '.get-update-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-item-uuid').val(jsonData.itemUuid);
        $('#update-item-no').val(jsonData.no);
        $('#update-item-name').val(jsonData.name);
    });
    $('#itemList').on('click', '.get-delete-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-item-uuid').val(jsonData.itemUuid);
    });
    $('.add-item-vendor-product-add').click(function() {
        setSelect();
    });
}

// 取得項目清單
function getItems() {
    let url = `/item/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
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
            let vendors;
            let vendorProducts;
            let qty;
            let unitPrice;
            let amount;
            let totalAmount;
            $.each(response.data.responses, function (key, value) {
                vendors = formatVendor(value.vendorProducts);
                vendorProducts = formatVendorProduct(value.vendorProducts);
                qty = formatQty(value.vendorProducts);
                unitPrice = formatUnitPrice(value.vendorProducts);
                amount = formatAmount(value.vendorProducts);
                totalAmount = formatTotalAmount(value.vendorProducts);
                $("#item-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td style='vertical-align: middle;'>${value.no}</td>
                        <td style='vertical-align: middle;'>${value.name}</td>
                        <td style='vertical-align: middle;'>${value.spec}</td>
                        <td>${vendors}</td>
                        <td>${vendorProducts}</td>
                        <td>${qty}</td>
                        <td>${unitPrice}</td>
                        <td>${amount}</td>
                        <td style='vertical-align: middle;'>${totalAmount}</td>
                        <td style='vertical-align: middle;'>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-item-json' data-bs-toggle='offcanvas' data-bs-target='#update-item' aria-controls='update-item'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-item-json' data-bs-toggle="modal" data-bs-target="#delete-item">刪除</button>
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

// 轉換廠商清單
function formatVendor(value){
    let result = '';
    let amount;
    for(let i = 0; i < value.length; i ++){
        result += `
            <div class='row'>
                <div class='col'>${value[i].vendorName}</div>
            </div>
        `;
    }
    return result;
}

// 轉換廠商產品清單
function formatVendorProduct(value){
    let result = '';
    let amount;
    for(let i = 0; i < value.length; i ++){
        result += `
            <div class='row'>
                <div class='col'>${value[i].vendorProductName}</div>
            </div>
        `;
    }
    return result;
}

// 轉換數量清單
function formatQty(value){
    let result = '';
    let amount;
    for(let i = 0; i < value.length; i ++){
        result += `
            <div class='row'>
                <div class='col'>${value[i].qty}</div>
            </div>
        `;
    }
    return result;
}

// 轉換數量清單
function formatUnitPrice(value){
    let result = '';
    let amount;
    for(let i = 0; i < value.length; i ++){
        result += `
            <div class='row'>
                <div class='col'>${value[i].unitPrice.toLocaleString()}</div>
            </div>
        `;
    }
    return result;
}

// 轉換數量清單
function formatAmount(value){
    let result = '';
    let amount;
    for(let i = 0; i < value.length; i ++){
        amount = parseInt(value[i].qty) * parseInt(value[i].unitPrice);
        result += `
            <div class='row'>
                <div class='col'>${amount.toLocaleString()}</div>
            </div>
        `;
    }
    return result;
}

// 轉換總計
function formatTotalAmount(value){
    let totalAmount = 0;
    let amount;
    for(let i = 0; i < value.length; i ++){
        amount = parseInt(value[i].qty) * parseInt(value[i].unitPrice);
        totalAmount += amount;
    }
    return totalAmount.toLocaleString();
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

// 取得廠商產品清單
function getVendorProducts() {
    $.ajax({
        url: `/common/vendor-product/v1`,
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
            globalVendorProduct = response.data;
            setSelect();
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

// 設定下拉選單
function setSelect(){
    appendColumn();
    let selectVendor = $('.add-item-vendor-select:last');
    $.each(globalVendor, function(key, value) {
        selectVendor.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
    });
    selectVendor.selectpicker('render');
    let tr = selectVendor.closest('tr');
    addItemVendorSelect(tr);
    updateItemQtyInput(tr);
}

// 加入欄位
function appendColumn(){
    $('#item-vendor-product-tbody').append(`
        <tr>
            <td>
                <select class="selectpicker add-item-vendor-select" data-live-search="true">

                </select>
            </td>
            <td class="add-item-vendor-product-select-td">

            </td>
            <td>
                <input type="text" class="form-control add-item-vendor-product-qty" style="margin-left: 1px;"/>
            </td>
            <td>
                <input type="text" class="form-control add-item-vendor-product-unit-price" style="margin-left: 1px;" disabled/>
            </td>
            <td>
                <div class="align-self-center" style="margin-left: 1px;">
                    <button class="btn btn-sm btn-danger add-item-vendor-product-cancel">x</button>
                </div>
            </td>
        </tr>
    `);
    selectChange();
    inputChange();
    cancelClick();
}

// 廠商選項調整
function selectChange(){
    $('.add-item-vendor-select').change(function() {
        let tr = $(this).closest('tr');
        addItemVendorSelect(tr);
    });
    $('.add-item-vendor-product-select').change(function() {
        let tr = $(this).closest('tr');
        updateItemQtyInput(tr);
    });
}

// 廠商產品數量調整
function inputChange(){
    $('.add-item-vendor-product-qty').change(function() {
        let tr = $(this).closest('tr');
        updateItemQtyInput(tr);
    });
}

// 點擊取消事件
function cancelClick(){
    $('.add-item-vendor-product-cancel').click(function() {
        let length = $('#item-vendor-product-tbody tr').length;
        if(length <= 1){
            return;
        }
        let tr = $(this).closest('tr');
        tr.remove();
        countAmount();
    });
}

// 新增項目廠商產品選單
function addItemVendorSelect(tr){
    let selectVendor = tr.find('.add-item-vendor-select select');
    let selectedVendorUuid = selectVendor.val();
    let selectVendorProductTd = tr.find('.add-item-vendor-product-select-td');
    selectVendorProductTd.empty();
    selectVendorProductTd.append(`
        <select class="selectpicker add-item-vendor-product-select" data-live-search="true">

        </select>
    `);
    let selectVendorProduct = tr.find('.add-item-vendor-product-select');
    $.each(globalVendorProduct, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        selectVendorProduct.append(`
            <option value='${value.vendorProductUuid}'>${value.name}</option>
        `);
    });
    selectVendorProduct.selectpicker('render');
    updateItemQtyInput(tr);
    selectChange();
}

// 新增項目廠商產品數量及成本及總金額
function updateItemQtyInput(tr){
    let selectVendorProduct = tr.find('.add-item-vendor-product-select select');
    let selectedVendorProductUuid = selectVendorProduct.val();
    let qty = tr.find('.add-item-vendor-product-qty');
    let unitPrice = tr.find('.add-item-vendor-product-unit-price');
    if(isEmpty(qty.val())){
        qty.val(1);
    }
    let price = 0;
    $.each(globalVendorProduct, function(key, value) {
        if(value.vendorProductUuid != selectedVendorProductUuid){
            return;
        }
        price = parseInt(value.unitPrice) * parseInt(qty.val());
        unitPrice.val(price.toLocaleString());
    });
    countAmount();
}

// 計算總計
function countAmount(){
    let totalAmount = $('#add-item-total-amount');
    let amount = 0;
    $.each($('.add-item-vendor-product-unit-price'), function(key, value){
        amount += parseInt($(this).val().replace(/,/g, ''));
    });
    totalAmount.text(amount.toLocaleString());
}

// 新增項目
function addItem() {
    const no = $("#add-item-no").val();
    const name = $("#add-item-name").val();
    const spec = $("#add-item-spec").val();
    const vendorProducts = getItemVendorProducts($("#item-vendor-product-tbody tr"));
    // 驗證
    const noValid = validateInput(no, "#add-item-no");
    const nameValid = validateInput(name, "#add-item-name");
    const specValid = validateInput(name, "#add-item-spec");
    if (!noValid || !nameValid || !specValid) {
        return;
    }
    let data = {
        no: no,
        name: name,
        spec: spec,
        vendorProducts : vendorProducts
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

// 取得廠商產品清單
function getItemVendorProducts(vendorProductTrs){
    let vendorProducts = [];
    let vendorProduct;
    $.each(vendorProductTrs, function(key, value){
        vendorProduct = {
            "vendorProductUuid" : $(value).find('.add-item-vendor-product-select select').val(),
            "qty" : $(value).find('.add-item-vendor-product-qty').val()
        };
        vendorProducts.push(vendorProduct);
    });
    return vendorProducts;
}

// 更新項目
function updateItem() {
    const itemUuid = $('#update-item-uuid').val();
    const no = $("#update-item-no").val();
    const name = $("#update-item-name").val();
    // 驗證
    const noValid = validateInput(no, "#update-item-no");
    const nameValid = validateInput(name, "#update-item-name");
    if (!noValid || !nameValid) {
        return;
    }
    let data = {
        no: no,
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

// 刪除項目
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