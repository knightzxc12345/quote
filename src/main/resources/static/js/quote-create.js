let globalItem = '';
let globalCustomer = '';
let globalUser = '';
let productIndex = 1;

window.onload = function () {
    init();
    getItems();
    getCustomers();
    getUsers();
    selectChange();
    inputChange();
};

function backQuote(){
    location.href = "/quote"
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
    let customerSelect = $('#customer-select');
    customerSelect.empty();
    let selected;
    $.each(globalCustomer, function(key, value) {
        selected = key == 0 ? 'selected' : '';
        customerSelect.append(`
            <option value='${value.customerUuid}' ${selected}>${value.name}</option>
        `);
    });
    customerSelect.selectpicker('refresh');
    customerSelect.selectpicker('render');
    customerChange();
    setAddress();
}

// 客戶變化時
function customerChange(){
    $("#customer-select").change(function() {
        setAddress();
    });
}

// 設定地址
function setAddress(){
    let customerSelect = $("#customer-select").val();
    $.each(globalCustomer, function(key, value) {
        if(value.customerUuid != customerSelect){
            return;
        }
        $("#customer-address").text(value.address);
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
    let selectUser = $("#user-select");
    $.each(globalUser, function(key, value) {
        selectUser.append(`
            <option value='${value.userUuid}'>${value.name}</option>
        `);
    });
    selectUser.selectpicker('refresh');
    selectUser.selectpicker('render');
}

// 設定下拉選單
function setSelect(){
    appendColumn();
    let selectItem = $('.add-item-name-select:last');
    $.each(globalItem, function(key, value) {
        selectItem.append(`
            <option value='${value.itemUuid}'>${value.itemNo}</option>
        `);
    });
    let tr = selectItem.closest('tr');
    addColumnChange(tr);
}

// 寫入row
function appendColumn(){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm add-item-cancel">x</button>
            </td>
            <td class="add-item-uuid hide"></td>
            <td class="add-item-index">${productIndex}</td>
            <td>
                <select class="form-select select2 add-item-name-select">
                </select>
            </td>
            <td class="add-item-name"></td>
            <td class="add-item-spec"></td>
            <td>
                <input class="form-control form-control-sm add-item-quantity" value="1"/>
            </td>
            <td class="add-item-unit"></td>
            <td class="add-item-unit-price">0</td>
            <td class="add-item-amount">0</td>
            <td>
                <input class="form-control form-control-sm add-item-custom-unit-price red-text"/>
            </td>
            <td class="add-item-custom-amount" style="color: red;">0</td>
            <td class="add-item-cost-price"  style="color: green;">0</td>
            <td class="add-item-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm add-item-add">+</button>
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
    $('.add-item-add').off('click').on('click', function() {
        $(this).addClass('hide');
        setSelect();
    });
    $('.add-item-cancel').off('click').on('click', function() {
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
            prevTr.find('.add-item-add').removeClass('hide');
        }
    });
}

// 重設index
function resetIndex(){
    let index = 1;
    $('#quote-tbody tr').each(function() {
        $(this).find('td.add-item-index').text(index++);
    });
}

// 選項變化時
function selectChange(){
    $('.add-item-name-select').change(function() {
        let tr = $(this).closest('tr');
        addColumnChange(tr);
    });
}

// 修改數量或客製單價
function inputChange(){
    $(".add-item-quantity").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val();
        if(!/^\d+$/.test(inputValue)){
            $(this).val(1);
        }
        columnChange(tr);
    });
    $(".add-item-custom-unit-price").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val().replace(/,/g, '');
        if(!/^\d+$/.test(inputValue)){
            let unitPrice = tr.find('.add-item-unit-price');
            $(this).text(unitPrice);
        }
        columnChange(tr);
    });
}

// 新增新的row
function addColumnChange(tr){
    let selectItem = tr.find('.add-item-name-select');
    let selectedItemUuid = selectItem.val();
    let itemName = tr.find('.add-item-name');
    let itemSpec = tr.find('.add-item-spec');
    let itemUnit = tr.find('.add-item-unit');
    let inputQuantity = tr.find('.add-item-quantity');
    let tdUnitPrice = tr.find('.add-item-unit-price');
    let tdAmount = tr.find('.add-item-amount');
    let inputCustomUnitPrice = tr.find('.add-item-custom-unit-price');
    let tdCustomAmount = tr.find('.add-item-custom-amount');
    let tdCostPrice = tr.find('.add-item-cost-price');
    let tdCostAmount = tr.find('.add-item-cost-amount');
    $.each(globalItem, function(key, value){
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        itemName.text(value.name);
        itemSpec.text(value.spec);
        itemUnit.text(value.unit);
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.amount);
        let customUnitPrice = unitPrice;
        let costPrice = parseInt(value.unitPrice);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostPrice.text(costPrice.toLocaleString());
        tdCostAmount.text((quantity * costPrice).toLocaleString());
        countTotal();
    });
}

// 欄位變化時
function columnChange(tr){
    let selectItem = tr.find('.add-item-name-select');
    let selectedItemUuid = selectItem.val();
    let tdNo = tr.find('.add-item-no');
    let tdUnit = tr.find('.add-item-unit');
    let inputQuantity = tr.find('.add-item-quantity');
    let tdUnitPrice = tr.find('.add-item-unit-price');
    let tdAmount = tr.find('.add-item-amount');
    let inputCustomUnitPrice = tr.find('.add-item-custom-unit-price');
    let tdCustomAmount = tr.find('.add-item-custom-amount');
    let tdCostPrice = tr.find('.add-item-cost-price');
    let tdCostAmount = tr.find('.add-item-cost-amount');
    $.each(globalItem, function(key, value) {
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.amount);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        let costPrice = parseInt(value.unitPrice);
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
        let quantity = parseInt($(this).find('.add-item-quantity').val().replace(/,/g, ''));
        let tdUnitPrice = parseInt($(this).find('.add-item-unit-price').text().replace(/,/g, ''));
        let inputCustomUnitPrice = parseInt($(this).find('.add-item-custom-unit-price').val().replace(/,/g, ''));
        let tdCustomUnitPrice = parseInt($(this).find('.add-item-cost-price').text().replace(/,/g, ''));
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
    const userUuid = $('#user-select').val();
    const customerUuid = $('#customerair-select').val();
    const underTakerName = $('#under-taker-name').val();
    const underTakerTel = $('#under-taker-tel').val();
    let items = [];
    let item;
    $('#quote-tbody tr').each(function() {
        item = {
            itemUuid: $(this).find('.add-item-name-select').val(),
            quantity: parseInt($(this).find('.add-item-quantity').val().replace(/,/g, '')),
            customUnitPrice: parseInt($(this).find('.add-item-custom-unit-price').val().replace(/,/g, ''))
        };
        items.push(item);
    });
    let data = {
        userUuid: userUuid,
        customerUuid: customerUuid,
        underTakerName: underTakerName,
        underTakerTel: underTakerTel,
        items: items
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