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

function setUser(){
    let selectUser = $(".add-product-user-name-select");
    $.each(globalUser, function(key, value) {
        selectUser.append(`
            <option value='${value.userUuid}'>${value.name}</option>
        `);
    });
}

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
    columnChangeFirst(tr);
}

function appendColumn(){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm add-product-cancel">x</button>
            </td>
            <td class="add-product-index">1</td>
            <td class="add-product-no"></td>
            <td>
                <select class="form-select select2 add-vendor-name-select" aria-label="Floating label select example">
                </select>
            </td>
            <td>
                <select class="form-select select2 add-item-name-select" aria-label="Floating label select example">
                </select>
            </td>
            <td>
                <select class="form-select select2 add-product-specification-select" aria-label="Floating label select example">
                </select>
            </td>
            <td>
                <input class="form-control form-control-sm add-product-quantity" value="1"/>
            </td>
            <td class="add-product-unit"></td>
            <td class="add-product-unit-price">0</td>
            <td class="add-product-amount">0</td>
            <td>
                <input class="form-control form-control-sm add-product-custom-unit-price red-text" />
            </td>
            <td class="add-product-custom-amount" style="color: red;">0</td>
            <td>
                <button class="btn btn-success btn-sm add-product-add">+</button>
            </td>
        </tr>
    `);
    buttonClick();
    selectChange();
    inputChange();
}

function buttonClick(){
    $('.add-product-add').on('click', function() {
        $(this).addClass('hide');
        setSelect();
    });
    $('.add-product-cancel').on('click', function() {
        let tr = $(this).closest('tr');
        let nextTr = tr.next('tr');
        let prevTr = tr.prev('tr');
        if (nextTr.length > 0 || prevTr.length > 0) {
            tr.remove();
            countTotal();
        }
        if (nextTr.length <= 0 && prevTr.length > 0) {
            prevTr.find('.add-product-add').removeClass('hide');
        }
    });
}

function selectChange(){
    $('.add-vendor-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectVendorChange(tr);
        selectItemChange(tr);
        columnChangeFirst(tr);
    });
    $('.add-item-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectItemChange(tr);
        columnChangeFirst(tr);
    });
    $('.add-product-specification-select').change(function() {
        let tr = $(this).closest('tr');
        columnChangeFirst(tr);
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

function columnChangeFirst(tr){
    let selectProduct = tr.find('.add-product-specification-select');
    let selectedProductUuid = selectProduct.val();
    let tdNo = tr.find('.add-product-no');
    let tdUnit = tr.find('.add-product-unit');
    let inputQuantity = tr.find('.add-product-quantity');
    let tdUnitPrice = tr.find('.add-product-unit-price');
    let tdAmount = tr.find('.add-product-amount');
    let inputCustomUnitPrice = tr.find('.add-product-custom-unit-price');
    let tdCustomAmount = tr.find('.add-product-custom-amount');
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = unitPrice;
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
    });
    countTotal();
}

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
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
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
    $("#quote-tbody tr").each(function() {
        let quantity = parseInt($(this).find('.add-product-quantity').val().replace(/,/g, ''));
        let tdUnitPrice = parseInt($(this).find('.add-product-unit-price').text().replace(/,/g, ''));
        let inputCustomUnitPrice = parseInt($(this).find('.add-product-custom-unit-price').val().replace(/,/g, ''));
        totalAmount += (tdUnitPrice * quantity);
        customTotalAmount += (inputCustomUnitPrice * quantity);
    });
    tax = (totalAmount * 0.05).toFixed(0);
    totalAmountWithTax = totalAmount + parseInt(tax);
    customTax = (customTotalAmount * 0.05).toFixed(0);
    customTotalAmountWithTax = customTotalAmount + parseInt(customTax);
    $("#add-quote-amount").text(totalAmount.toLocaleString());
    $("#add-quote-tax").text(Number(tax).toLocaleString());
    $("#add-quote-total-amount").text(totalAmountWithTax.toLocaleString());
    $("#add-quote-custom-amount").text(customTotalAmount.toLocaleString());
    $("#add-quote-custom-tax").text(Number(customTax).toLocaleString());
    $("#add-quote-custom-total-amount").text(customTotalAmountWithTax.toLocaleString());
}

function addQuote(){

}