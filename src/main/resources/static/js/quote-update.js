let globalVendor = '';
let globalItem = '';
let globalProduct = '';
let globalCustomer = '';
let globalUser = '';
let productIndex = 1;

window.onload = function () {
    init();
    select2Init();
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
            getQuotes();
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
            getUsers();
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
        let selectCustomer = $(".update-product-customer-name-select");
        selectCustomer.append(`
            <option value='${value.customerUuid}'>${value.name}</option>
        `);
        if(key == 0){
            $(".update-product-customer-address").text(value.address);
        }
    });
    customerChange();
}

function customerChange(){
    $(".update-product-customer-name-select").on("select2:select", function() {
        let selectedCustomer = $(".update-product-customer-name-select").val();
        $.each(globalCustomer, function(key, value) {
            if(value.customerUuid != selectedCustomer){
                return;
            }
            $(".update-product-customer-address").text(value.address);
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
            getVendors();
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
    let selectUser = $(".update-product-user-name-select");
    $.each(globalUser, function(key, value) {
        selectUser.append(`
            <option value='${value.userUuid}'>${value.name}</option>
        `);
    });
}

function getQuotes(){
    let quoteUuid = $('.update-quote-uuid').text();
    $.ajax({
        url: `/quote/v1/` + quoteUuid,
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00001') {
                alertError('系統錯誤');
                return;
            }
            // 空陣列
            if ($.isEmptyObject(response.data)) {
                return;
            }
            $.each(response.data.products, function(index, value) {
                setFirstSelect(value);
            });
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

function setFirstSelect(product){
    appendColumnFirst(product);
    let vendorUuid = product.vendorUuid;
    let itemUuid = product.itemUuid;
    let productUuid = product.productUuid;
    let selectVendor = $('.update-vendor-name-select:last');
    $.each(globalVendor, function(key, value) {
        let isSelected = value.vendorUuid == product.vendorUuid ? 'selected' : '';
        selectVendor.append(`
            <option value='${value.vendorUuid}' ${isSelected}>${value.name}</option>
        `);
    });
    let selectedVendorUuid = selectVendor.val();
    let selectItem = $('.update-item-name-select:last');
    $.each(globalItem, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        let isSelected = value.itemUuid == product.itemUuid ? 'selected' : '';
        selectItem.append(`
            <option value='${value.itemUuid}' ${isSelected}>${value.name}</option>
        `);
    });
    let selectedItemUuid = selectItem.val();
    let selectProduct = $('.update-product-specification-select:last');
    $.each(globalProduct, function(key, value) {
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        let isSelected = value.productUuid == product.productUuid ? 'selected' : '';
        selectProduct.append(`
            <option value='${value.productUuid}' ${isSelected}>${value.specification}</option>
        `);
    });
    let tr = selectProduct.closest('tr');
    columnChangeFirst(tr);
}

function setSelect(){
    appendColumn();
    let selectVendor = $('.update-vendor-name-select:last');
    $.each(globalVendor, function(key, value) {
        selectVendor.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
    });
    let selectedVendorUuid = selectVendor.val();
    let selectItem = $('.update-item-name-select:last');
    $.each(globalItem, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        selectItem.append(`
            <option value='${value.itemUuid}'>${value.name}</option>
        `);
    });
    let selectedItemUuid = selectItem.val();
    let selectProduct = $('.update-product-specification-select:last');
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

function appendColumnFirst(product){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm update-product-cancel">x</button>
            </td>
            <td class="update-product-uuid hide"></td>
            <td class="update-product-index">${productIndex++}</td>
            <td class="update-product-no"></td>
            <td>
                <select class="form-select select2 update-vendor-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 update-item-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 update-product-specification-select">
                </select>
            </td>
            <td>
                <input class="form-control form-control-sm update-product-quantity" value="${product.quantity}"/>
            </td>
            <td class="update-product-unit"></td>
            <td class="update-product-unit-price">0</td>
            <td class="update-product-amount">0</td>
            <td>
                <input class="form-control form-control-sm update-product-custom-unit-price red-text" value="${product.customUnitPrice.toLocaleString()}"/>
            </td>
            <td class="update-product-custom-amount" style="color: red;">0</td>
            <td class="update-product-cost-unit-price"  style="color: green;">0</td>
            <td class="update-product-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm update-product-add">+</button>
            </td>
        </tr>
    `);
    buttonClick();
    selectChange();
    inputChange();
}

function appendColumn(){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm update-product-cancel">x</button>
            </td>
            <td class="update-product-uuid hide"></td>
            <td class="update-product-index">${productIndex}</td>
            <td class="update-product-no"></td>
            <td>
                <select class="form-select select2 update-vendor-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 update-item-name-select">
                </select>
            </td>
            <td>
                <select class="form-select select2 update-product-specification-select">
                </select>
            </td>
            <td>
                <input class="form-control form-control-sm update-product-quantity" value="1"/>
            </td>
            <td class="update-product-unit"></td>
            <td class="update-product-unit-price">0</td>
            <td class="update-product-amount">0</td>
            <td>
                <input class="form-control form-control-sm update-product-custom-unit-price red-text"/>
            </td>
            <td class="update-product-custom-amount" style="color: red;">0</td>
            <td class="update-product-cost-unit-price"  style="color: green;">0</td>
            <td class="update-product-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm update-product-add">+</button>
            </td>
        </tr>
    `);
    productIndex++;
    buttonClick();
    selectChange();
    inputChange();
}

function buttonClick(){
    $('.update-product-add').off('click').on('click', function() {
        $(this).addClass('hide');
        setSelect();
    });
    $('.update-product-cancel').off('click').on('click', function() {
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
            prevTr.find('.update-product-add').removeClass('hide');
        }
    });
}

function resetIndex(){
    let index = 1;
    $('#quote-tbody tr').each(function() {
        $(this).find('td.update-product-index').text(index++);
    });
}

function selectChange(){
    $('.update-vendor-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectVendorChange(tr);
        selectItemChange(tr);
        columnChange(tr);
    });
    $('.update-item-name-select').change(function() {
        let tr = $(this).closest('tr');
        selectItemChange(tr);
        columnChange(tr);
    });
    $('.update-product-specification-select').change(function() {
        let tr = $(this).closest('tr');
        columnChange(tr);
    });
}

// 修改數量或客製單價
function inputChange(){
    $(".update-product-quantity").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val();
        if(!/^\d+$/.test(inputValue)){
            $(this).val(1);
        }
        columnChange(tr);
    });
    $(".update-product-custom-unit-price").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val().replace(/,/g, '');
        if(!/^\d+$/.test(inputValue)){
            let unitPrice = tr.find('.update-product-unit-price');
            $(this).text(unitPrice);
        }
        columnChange(tr);
    });
}

function selectVendorChange(tr){
    let selectVendor = tr.find('.update-vendor-name-select');
    let selectedVendorUuid = selectVendor.val();
    let selectItem = tr.find('.update-item-name-select');
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
    let selectItem = tr.find('.update-item-name-select');
    let selectedItemUuid = selectItem.val();
    let selectProduct = tr.find('.update-product-specification-select');
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
    let selectProduct = tr.find('.update-product-specification-select');
    let selectedProductUuid = selectProduct.val();
    let tdNo = tr.find('.update-product-no');
    let tdUnit = tr.find('.update-product-unit');
    let inputQuantity = tr.find('.update-product-quantity');
    let tdUnitPrice = tr.find('.update-product-unit-price');
    let tdAmount = tr.find('.update-product-amount');
    let inputCustomUnitPrice = tr.find('.update-product-custom-unit-price');
    let tdCustomAmount = tr.find('.update-product-custom-amount');
    let tdCostUnitPrice = tr.find('.update-product-cost-unit-price');
    let tdCostAmount = tr.find('.update-product-cost-amount');
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        let costUnitPrice = parseInt(value.costPrice);
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostUnitPrice.text(costUnitPrice.toLocaleString());
        tdCostAmount.text((quantity * costUnitPrice).toLocaleString());
    });
    countTotal();
}

function columnChange(tr){
    let selectProduct = tr.find('.update-product-specification-select');
    let selectedProductUuid = selectProduct.val();
    let tdNo = tr.find('.update-product-no');
    let tdUnit = tr.find('.update-product-unit');
    let inputQuantity = tr.find('.update-product-quantity');
    let tdUnitPrice = tr.find('.update-product-unit-price');
    let tdAmount = tr.find('.update-product-amount');
    let inputCustomUnitPrice = tr.find('.update-product-custom-unit-price');
    let tdCustomAmount = tr.find('.update-product-custom-amount');
    let tdCostUnitPrice = tr.find('.update-product-cost-unit-price');
    let tdCostAmount = tr.find('.update-product-cost-amount');
    $.each(globalProduct, function(key, value) {
        if(value.productUuid != selectedProductUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.unitPrice);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        let costUnitPrice = parseInt(value.costPrice);
        tdNo.text(value.no);
        tdUnit.text(value.unit);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostUnitPrice.text(costUnitPrice.toLocaleString());
        tdCostAmount.text((quantity * costUnitPrice).toLocaleString());
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
        let quantity = parseInt($(this).find('.update-product-quantity').val().replace(/,/g, ''));
        let tdUnitPrice = parseInt($(this).find('.update-product-unit-price').text().replace(/,/g, ''));
        let inputCustomUnitPrice = parseInt($(this).find('.update-product-custom-unit-price').val().replace(/,/g, ''));
        let tdCustomUnitPrice = parseInt($(this).find('.update-product-cost-unit-price').text().replace(/,/g, ''));
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
    $("#update-quote-amount").text(totalAmount.toLocaleString());
    $("#update-quote-tax").text(Number(tax).toLocaleString());
    $("#update-quote-total-amount").text(totalAmountWithTax.toLocaleString());
    $("#update-quote-custom-amount").text(customTotalAmount.toLocaleString());
    $("#update-quote-custom-tax").text(Number(customTax).toLocaleString());
    $("#update-quote-custom-total-amount").text(customTotalAmountWithTax.toLocaleString());
    $("#update-quote-cost-amount").text(costTotalAmount.toLocaleString());
    $("#update-quote-cost-tax").text(Number(costTax).toLocaleString());
    $("#update-quote-cost-total-amount").text(costTotalAmountWithTax.toLocaleString());
}

function addQuote(){
    const userUuid = $('.update-product-user-name-select').val();
    const customerUuid = $('.update-product-customer-name-select').val();
    const underTakerName = $('.update-product-under-taker-name').val();
    const underTakerTel = $('.update-product-under-taker-tel').val();
    let products = [];
    let product;
    $('#quote-tbody tr').each(function() {
        product = {
            productUuid: $(this).find('.update-product-specification-select').val(),
            quantity: parseInt($(this).find('.update-product-quantity').val().replace(/,/g, '')),
            customUnitPrice: parseInt($(this).find('.update-product-custom-unit-price').val().replace(/,/g, ''))
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
                return;
            }
            $('.update-product-success-modal').modal('show');
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