let globalVendor = '';
let globalItem = '';
let globalProduct = '';
let globalCustomer = '';
let globalUser = '';
let productIndex = 1;

window.onload = function () {
    init();
    select2Init();
    getVendors();
    getCustomers();
    getUsers();
    selectChange();
    inputChange();
};

function select2Init(){
    $(".select2").select2();
}

function backQuote(){
    location.href = "/quote"
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

// 取得品項
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

// 取得產品
function getProducts(){
    $.ajax({
        url: `/common/product/v1`,
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
            globalProduct = response.data;
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

// 取得使用者
function getCustomers(){
    $.ajax({
        url: `/common/customer/v1`,
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
            globalCustomer = response.data;
            setCustomer();
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

// 設定使用者
function setCustomer(){
    $.each(globalCustomer, function(key, value) {
        let selectCustomer = $(".add-product-customer-name-select");
        selectCustomer.append(`
            <option value='${value.customerUuid}'>${value.name}</option>
        `);
        if(key == 0){
            $(".add-product-customer-address").text(value.address);
        }
    });
    customerChange();
}

// 客戶變化時
function customerChange(){
    $(".add-product-customer-name-select").on("select2:select", function() {
        let selectedCustomer = $(".add-product-customer-name-select").val();
        $.each(globalCustomer, function(key, value) {
            if(value.customerUuid != selectedCustomer){
                return;
            }
            $(".add-product-customer-address").text(value.address);
        });
    });
}

// 取得使用者
function getUsers(){
    $.ajax({
        url: `/common/user/v1/business`,
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
            globalUser = response.data;
            setUser();
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

// 設定使用者
function setUser(){
    let selectUser = $(".add-product-user-name-select");
    $.each(globalUser, function(key, value) {
        selectUser.append(`
            <option value='${value.userUuid}'>${value.name}</option>
        `);
    });
}

// 設定下拉選單
function setSelect(){
    appendColumn();
    let selectVendor = $('.add-vendor-name-select:last');
    $.each(globalVendor, function(key, value) {
        selectVendor.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
    });
    let selectedVendorUuid = selectVendor.val();
    let selectItem = $('.add-item-name-select:last');
    $.each(globalItem, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        selectItem.append(`
            <option value='${value.itemUuid}'>${value.name}</option>
        `);
    });
    let selectedItemUuid = selectItem.val();
    let selectProduct = $('.add-product-specification-select:last');
    $.each(globalProduct, function(key, value) {
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        selectProduct.append(`
            <option value='${value.productUuid}'>${value.specification}</option>
        `);
    });
    let tr = selectProduct.closest('tr');
    addColumnChange(tr);
}

// 寫入row
function appendColumn(){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm add-product-cancel">x</button>
            </td>
            <td class="add-product-uuid hide"></td>
            <td class="add-product-index">${productIndex}</td>
            <td class="add-product-no"></td>
            <td>
                <select class="form-select select2 add-vendor-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 add-item-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 add-product-specification-select">
                </select>
            </td>
            <td>
                <input class="form-control form-control-sm add-product-quantity" value="1"/>
            </td>
            <td class="add-product-unit"></td>
            <td class="add-product-unit-price">0</td>
            <td class="add-product-amount">0</td>
            <td>
                <input class="form-control form-control-sm add-product-custom-unit-price red-text"/>
            </td>
            <td class="add-product-custom-amount" style="color: red;">0</td>
            <td class="add-product-cost-unit-price"  style="color: green;">0</td>
            <td class="add-product-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm add-product-add">+</button>
            </td>
        </tr>
    `);
    productIndex++;
    buttonClick();
    selectChange();
    inputChange();
}

// 按鈕事件
function buttonClick(){
    $('.add-product-add').off('click').on('click', function() {
        $(this).addClass('hide');
        setSelect();
    });
    $('.add-product-cancel').off('click').on('click', function() {
        let tr = $(this).closest('tr');
        let nextTr = tr.next('tr');
        let prevTr = tr.prev('tr');
        if (nextTr.length > 0 || prevTr.length > 0) {
            tr.remove();
            countTotal();
            resetIndex();
            productIndex--;
        }
        if (nextTr.length <= 0 && prevTr.length > 0) {
            prevTr.find('.add-product-add').removeClass('hide');
        }
    });
}

// 重設index
function resetIndex(){
    let index = 1;
    $('#quote-tbody tr').each(function() {
        $(this).find('td.add-product-index').text(index++);
    });
}

// 選項變化時
function selectChange(){
    $('.add-vendor-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectVendorChange(tr);
        selectItemChange(tr);
        addColumnChange(tr);
    });
    $('.add-item-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectItemChange(tr);
        addColumnChange(tr);
    });
    $('.add-product-specification-select').change(function() {
        let tr = $(this).closest('tr');
        addColumnChange(tr);
    });
}

// 修改數量或客製單價
function inputChange(){
    $(".add-product-quantity").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val();
        if(!/^\d+$/.test(inputValue)){
            $(this).val(1);
        }
        columnChange(tr);
    });
    $(".add-product-custom-unit-price").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val().replace(/,/g, '');
        if(!/^\d+$/.test(inputValue)){
            let unitPrice = tr.find('.add-product-unit-price');
            $(this).text(unitPrice);
        }
        columnChange(tr);
    });
}

// 廠商變化時品項變動
function selectVendorChange(tr){
    let selectVendor = tr.find('.add-vendor-name-select');
    let selectedVendorUuid = selectVendor.val();
    let selectItem = tr.find('.add-item-name-select');
    selectItem.empty();
    $.each(globalItem, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        selectItem.append(`
            <option value='${value.itemUuid}'>${value.name}</option>
        `);
    });
}

// 品項變化時產品變動
function selectItemChange(tr){
    let selectItem = tr.find('.add-item-name-select');
    let selectedItemUuid = selectItem.val();
    let selectProduct = tr.find('.add-product-specification-select');
    selectProduct.empty();
    $.each(globalProduct, function(key, value) {
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        selectProduct.append(`
            <option value='${value.productUuid}'>${value.specification}</option>
        `);
    });
}

// 新增新的row
function addColumnChange(tr){
    let selectProduct = tr.find('.add-product-specification-select');
    let selectedProductUuid = selectProduct.val();
    let tdNo = tr.find('.add-product-no');
    let tdUnit = tr.find('.add-product-unit');
    let inputQuantity = tr.find('.add-product-quantity');
    let tdUnitPrice = tr.find('.add-product-unit-price');
    let tdAmount = tr.find('.add-product-amount');
    let inputCustomUnitPrice = tr.find('.add-product-custom-unit-price');
    let tdCustomAmount = tr.find('.add-product-custom-amount');
    let tdCostPrice = tr.find('.add-product-cost-price');
    let tdCostAmount = tr.find('.add-product-cost-amount');
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = unitPrice;
        let costPrice = parseInt(value.costPrice);
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostPrice.text(costPrice.toLocaleString());
        tdCostAmount.text((quantity * costPrice).toLocaleString());
    });
    countTotal();
}

// 欄位變化時
function columnChange(tr){
    let selectProduct = tr.find('.add-product-specification-select');
    let selectedProductUuid = selectProduct.val();
    let tdNo = tr.find('.add-product-no');
    let tdUnit = tr.find('.add-product-unit');
    let inputQuantity = tr.find('.add-product-quantity');
    let tdUnitPrice = tr.find('.add-product-unit-price');
    let tdAmount = tr.find('.add-product-amount');
    let inputCustomUnitPrice = tr.find('.add-product-custom-unit-price');
    let tdCustomAmount = tr.find('.add-product-custom-amount');
    let tdCostPrice = tr.find('.add-product-cost-price');
    let tdCostAmount = tr.find('.add-product-cost-amount');
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        let costPrice = parseInt(value.costPrice);
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostPrice.text(costPrice.toLocaleString());
        tdCostAmount.text((quantity * costPrice).toLocaleString());
    });
    countTotal();
}

function countTotal(){
    let totalAmount = 0;
    let tax = 0;
    let totalAmountWithTax = 0;
    let customTotalAmount = 0;
    let customTax = 0;
    let customTotalAmountWithTax = 0;
    let costTotalAmount = 0;
    let costTax = 0;
    let costTotalAmountWithTax = 0;
    $("#quote-tbody tr").each(function() {
        let quantity = parseInt($(this).find('.add-product-quantity').val().replace(/,/g, ''));
        let tdUnitPrice = parseInt($(this).find('.add-product-unit-price').text().replace(/,/g, ''));
        let inputCustomUnitPrice = parseInt($(this).find('.add-product-custom-unit-price').val().replace(/,/g, ''));
        let tdCustomUnitPrice = parseInt($(this).find('.add-product-cost-unit-price').text().replace(/,/g, ''));
        totalAmount += (tdUnitPrice * quantity);
        customTotalAmount += (inputCustomUnitPrice * quantity);
        costTotalAmount += (tdCustomUnitPrice * quantity);
    });
    tax = (totalAmount * 0.05).toFixed(0);
    totalAmountWithTax = totalAmount + parseInt(tax);
    customTax = (customTotalAmount * 0.05).toFixed(0);
    customTotalAmountWithTax = customTotalAmount + parseInt(customTax);
    costTax = (costTotalAmount * 0.05).toFixed(0);
    costTotalAmountWithTax = costTotalAmount + parseInt(costTax);
    $("#add-quote-amount").text(totalAmount.toLocaleString());
    $("#add-quote-tax").text(Number(tax).toLocaleString());
    $("#add-quote-total-amount").text(totalAmountWithTax.toLocaleString());
    $("#add-quote-custom-amount").text(customTotalAmount.toLocaleString());
    $("#add-quote-custom-tax").text(Number(customTax).toLocaleString());
    $("#add-quote-custom-total-amount").text(customTotalAmountWithTax.toLocaleString());
    $("#add-quote-cost-amount").text(costTotalAmount.toLocaleString());
    $("#add-quote-cost-tax").text(Number(costTax).toLocaleString());
    $("#add-quote-cost-total-amount").text(costTotalAmountWithTax.toLocaleString());
}

function addQuote(){
    $('#add-quote-back').prop('disabled', true);
    $('#add-quote').prop('disabled', true);
    $('#add-quote-loading').removeClass('hide');
    $('#add-quote-text').text('建立中...');
    const userUuid = $('.add-product-user-name-select').val();
    const customerUuid = $('.add-product-customer-name-select').val();
    const underTakerName = $('.add-product-under-taker-name').val();
    const underTakerTel = $('.add-product-under-taker-tel').val();
    let products = [];
    let product;
    $('#quote-tbody tr').each(function() {
        product = {
            productUuid: $(this).find('.add-product-specification-select').val(),
            quantity: parseInt($(this).find('.add-product-quantity').val().replace(/,/g, '')),
            customUnitPrice: parseInt($(this).find('.add-product-custom-unit-price').val().replace(/,/g, ''))
        };
        products.push(product);
    });
    let data = {
        userUuid: userUuid,
        customerUuid: customerUuid,
        underTakerName: underTakerName,
        underTakerTel: underTakerTel,
        products: products
    };
    $.ajax({
        url: `/quote/v1`,
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'POST',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00003') {
                alertError('系統錯誤');
                $('#add-quote-back').prop('disabled', false);
                $('#add-quote').prop('disabled', false);
                $('#add-quote-loading').addClass('hide');
                $('#add-quote-text').text('送出');
                return;
            }
            $('.add-product-success-modal').modal('show');
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

function goBack(){
    location.href = "/quote";
}