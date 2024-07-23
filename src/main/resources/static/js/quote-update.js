let globalItem = '';
let globalCustomer = '';
let globalUser = '';
let itemIndex = 1;

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
            getQuote();
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

// 取得報價單
function getQuote(){
    let quoteUuid = $('#update-quote-uuid').text();
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
            $.each(response.data.items, function(index, value) {
                setSelectFirst(value);
            });
            setCustomer(response.data.customerUuid);
            setUser(response.data.userUuid);
            $("#quote-tbody tr:last .update-item-add").removeClass('hide');
            $("#under-taker-name").val(response.data.underTakerName);
            $("#under-taker-tel").val(response.data.underTakerTel);
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
function setCustomer(customerUuid){
    let customerSelect = $('#customer-select');
    customerSelect.empty();
    let selected;
    $.each(globalCustomer, function(key, value) {
        selected = value.customerUuid == customerUuid ? 'selected' : '';
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

// 設定使用者
function setUser(userUuid){
    let selectUser = $("#user-select");
    let selected;
    $.each(globalUser, function(key, value) {
        selected = value.userUuid == userUuid ? 'selected' : '';
        selectUser.append(`
            <option value='${value.userUuid}' ${selected}>${value.name}</option>
        `);
    });
    selectUser.selectpicker('refresh');
    selectUser.selectpicker('render');
}

// 設定下拉選單
function setSelectFirst(item){
    appendColumnFirst(item);
    let selectItem = $('.update-item-name-select:last');
    let selected;
    $.each(globalItem, function(key, value) {
        selected = item.itemUuid == value.itemUuid ? 'selected' : '';
        selectItem.append(`
            <option value='${value.itemUuid}' ${selected}>${value.itemNo}</option>
        `);
    });
    let tr = selectItem.closest('tr');
    addColumnChangeFirst(tr);
}

// 設定下拉選單
function setSelect(){
    appendColumn();
    let selectItem = $('.update-item-name-select:last');
    $.each(globalItem, function(key, value) {
        selectItem.append(`
            <option value='${value.itemUuid}'>${value.itemNo}</option>
        `);
    });
    let tr = selectItem.closest('tr');
    addColumnChange(tr);
}

// 寫入row
function appendColumnFirst(item){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm update-item-cancel">x</button>
            </td>
            <td class="update-item-uuid hide"></td>
            <td class="update-item-index">${itemIndex++}</td>
            <td>
                <select class="form-select select2 update-item-name-select">
                </select>
            </td>
            <td class="update-item-name"></td>
            <td class="update-item-spec"></td>
            <td>
                <input class="form-control form-control-sm update-item-quantity" value="${item.quantity}"/>
            </td>
            <td class="update-item-unit"></td>
            <td class="update-item-unit-price">0</td>
            <td class="update-item-amount">0</td>
            <td>
                <input class="form-control form-control-sm update-item-custom-unit-price red-text" value="${item.itemVendorProductCustomPrice.toLocaleString()}"/>
            </td>
            <td class="update-item-custom-amount" style="color: red;">0</td>
            <td class="update-item-cost-price"  style="color: green;">0</td>
            <td class="update-item-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm update-item-add hide">+</button>
            </td>
        </tr>
    `);
    buttonClick();
    selectChange();
    inputChange();
}

// 寫入row
function appendColumn(){
    $('#quote-tbody').append(`
        <tr>
            <td>
                <button class="btn btn-danger btn-sm add-item-cancel">x</button>
            </td>
            <td class="update-item-uuid hide"></td>
            <td class="update-item-index">${itemIndex++}</td>
            <td>
                <select class="form-select select2 update-item-name-select">
                </select>
            </td>
            <td class="update-item-name"></td>
            <td class="update-item-spec"></td>
            <td>
                <input class="form-control form-control-sm update-item-quantity" value="1"/>
            </td>
            <td class="update-item-unit"></td>
            <td class="update-item-unit-price">0</td>
            <td class="update-item-amount">0</td>
            <td>
                <input class="form-control form-control-sm update-item-custom-unit-price red-text"/>
            </td>
            <td class="update-item-custom-amount" style="color: red;">0</td>
            <td class="update-item-cost-price"  style="color: green;">0</td>
            <td class="update-item-cost-amount"  style="color: green;">0</td>
            <td>
                <button class="btn btn-success btn-sm update-item-add">+</button>
            </td>
        </tr>
    `);
    buttonClick();
    selectChange();
    inputChange();
}

// 按鈕事件
function buttonClick(){
    $('.update-item-add').off('click').on('click', function() {
        $(this).addClass('hide');
        setSelect();
    });
    $('.update-item-cancel').off('click').on('click', function() {
        let tr = $(this).closest('tr');
        let nextTr = tr.next('tr');
        let prevTr = tr.prev('tr');
        if (nextTr.length > 0 || prevTr.length > 0) {
            tr.remove();
            countTotal();
            resetIndex();
            itemIndex--;
        }
        if (nextTr.length <= 0 && prevTr.length > 0) {
            prevTr.find('.update-item-add').removeClass('hide');
        }
    });
}

// 重設index
function resetIndex(){
    let index = 1;
    $('#quote-tbody tr').each(function() {
        $(this).find('td.update-item-index').text(index++);
    });
}

// 選項變化時
function selectChange(){
    $('.update-item-name-select').change(function() {
        let tr = $(this).closest('tr');
        addColumnChange(tr);
    });
}

// 修改數量或客製單價
function inputChange(){
    $(".update-item-quantity").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val();
        if(!/^\d+$/.test(inputValue)){
            $(this).val(1);
        }
        columnChange(tr);
    });
    $(".update-item-custom-unit-price").change(function() {
        let tr = $(this).closest('tr');
        let inputValue = $(this).val().replace(/,/g, '');
        if(!/^\d+$/.test(inputValue)){
            let unitPrice = tr.find('.update-item-unit-price');
            $(this).text(unitPrice);
        }
        columnChange(tr);
    });
}

// 進入頁面時選項變化時調整row資料
function addColumnChangeFirst(tr){
    let selectItem = tr.find('.update-item-name-select');
    let selectedItemUuid = selectItem.val();
    let itemName = tr.find('.update-item-name');
    let itemSpec = tr.find('.update-item-spec');
    let itemUnit = tr.find('.update-item-unit');
    let inputQuantity = tr.find('.update-item-quantity');
    let tdUnitPrice = tr.find('.update-item-unit-price');
    let tdAmount = tr.find('.update-item-amount');
    let inputCustomUnitPrice = tr.find('.update-item-custom-unit-price');
    let tdCustomAmount = tr.find('.update-item-custom-amount');
    let tdCostPrice = tr.find('.update-item-cost-price');
    let tdCostAmount = tr.find('.update-item-cost-amount');
    $.each(globalItem, function(key, value){
        if(value.itemUuid != selectedItemUuid){
            return;
        }
        itemName.text(value.name);
        itemSpec.text(value.spec);
        itemUnit.text(value.unit);
        let quantity = parseInt(inputQuantity.val());
        let unitPrice = parseInt(value.amount);
        let customUnitPrice = parseInt(inputCustomUnitPrice.val().replace(/,/g, ''));
        let costPrice = parseInt(value.unitPrice);
        tdUnitPrice.text(unitPrice.toLocaleString());
        tdAmount.text((quantity * unitPrice).toLocaleString());
        inputCustomUnitPrice.val(customUnitPrice.toLocaleString());
        tdCustomAmount.text((quantity * customUnitPrice).toLocaleString());
        tdCostPrice.text(costPrice.toLocaleString());
        tdCostAmount.text((quantity * costPrice).toLocaleString());
    });
    countTotal();
}

// 新增新的row
function addColumnChange(tr){
    let selectItem = tr.find('.update-item-name-select');
    let selectedItemUuid = selectItem.val();
    let itemName = tr.find('.update-item-name');
    let itemSpec = tr.find('.update-item-spec');
    let itemUnit = tr.find('.update-item-unit');
    let inputQuantity = tr.find('.update-item-quantity');
    let tdUnitPrice = tr.find('.update-item-unit-price');
    let tdAmount = tr.find('.update-item-amount');
    let inputCustomUnitPrice = tr.find('.update-item-custom-unit-price');
    let tdCustomAmount = tr.find('.update-item-custom-amount');
    let tdCostPrice = tr.find('.update-item-cost-price');
    let tdCostAmount = tr.find('.update-item-cost-amount');
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
    });
    countTotal();
}

// 欄位變化時
function columnChange(tr){
    let selectItem = tr.find('.update-item-name-select');
    let selectedItemUuid = selectItem.val();
    let tdNo = tr.find('.update-item-no');
    let tdUnit = tr.find('.update-item-unit');
    let inputQuantity = tr.find('.update-item-quantity');
    let tdUnitPrice = tr.find('.update-item-unit-price');
    let tdAmount = tr.find('.update-item-amount');
    let inputCustomUnitPrice = tr.find('.update-item-custom-unit-price');
    let tdCustomAmount = tr.find('.update-item-custom-amount');
    let tdCostPrice = tr.find('.update-item-cost-price');
    let tdCostAmount = tr.find('.update-item-cost-amount');
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
        let quantity = parseInt($(this).find('.update-item-quantity').val().replace(/,/g, ''));
        let tdUnitPrice = parseInt($(this).find('.update-item-unit-price').text().replace(/,/g, ''));
        let inputCustomUnitPrice = parseInt($(this).find('.update-item-custom-unit-price').val().replace(/,/g, ''));
        let tdCustomUnitPrice = parseInt($(this).find('.update-item-cost-price').text().replace(/,/g, ''));
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

function updateQuote(){
    $('#update-quote-back').prop('disabled', true);
    $('#update-quote').prop('disabled', true);
    $('#update-quote-loading').removeClass('hide');
    $('#update-quote-text').text('建立中...');
    const quoteUuid = $('#update-quote-uuid').text();
    const userUuid = $('#user-select').val();
    const customerUuid = $('#customer-select').val();
    const underTakerName = $('#under-taker-name').val();
    const underTakerTel = $('#under-taker-tel').val();
    let items = [];
    let item;
    $('#quote-tbody tr').each(function() {
        item = {
            itemUuid: $(this).find('.update-item-name-select').val(),
            quantity: parseInt($(this).find('.update-item-quantity').val().replace(/,/g, '')),
            customUnitPrice: parseInt($(this).find('.update-item-custom-unit-price').val().replace(/,/g, ''))
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
        url: `/quote/v1/` + quoteUuid,
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'PUT',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00004') {
                alertError('系統錯誤');
                $('#update-quote-back').prop('disabled', false);
                $('#update-quote').prop('disabled', false);
                $('#update-quote-loading').addClass('hide');
                $('#update-quote-text').text('送出');
                return;
            }
            $('.update-item-success-modal').modal('show');
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