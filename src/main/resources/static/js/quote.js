let globalUser = '';
let globalCustomer = '';
let globalKeyword = '';
let globalUserSelect;
let globalCustomerSelect;

window.onload = function() {
    init();
    getUsers();
    getQuotes();
    selectChange();
    searchEnter();
    pageEvent(getQuotes);
    offcanvasEvent();
};

function searchEnter(){
    $("#quote-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function search(){
    globalKeyword = $("#quote-search-input").val();
    getQuotes();
}

function selectChange(){
    $('#user-name-select').off().change(function() {
        let value = $(this).val();
        globalUserSelect = 'all' == value ? null : value;
        getQuotes();
    });
    $('#customer-name-select').off().change(function() {
        let value = $(this).val();
        globalCustomerSelect = 'all' == value ? null : value;
        getQuotes();
    });
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#quoteList').on('click', '.get-preview-quote-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        previewQuote(jsonData);
    });
    $('#quoteList').on('click', '.get-download-1-quote-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        download01Quote(row, jsonData);
    });
    $('#quoteList').on('click', '.get-download-2-quote-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        download02Quote(row, jsonData);
    });
    $('#quoteList').on('click', '.get-update-quote-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        updateQuote(jsonData.quoteUuid);
    });
    $('#quoteList').on('click', '.get-delete-quote-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-quote-uuid').val(jsonData.quoteUuid);
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
            $('#user-name-select').append(`
                <option value='all'>全部</option>
            `);
            $.each(response.data, function(key, value) {
                $('#user-name-select').append(`
                    <option value='${value.userUuid}'>${value.name}</option>
                `);
            });
            getCustomers();
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

// 取得客戶
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
            $('#customer-name-select').append(`
                <option value='all'>全部</option>
            `);
            $.each(response.data, function(key, value) {
                $('#customer-name-select').append(`
                    <option value='${value.customerUuid}'>${value.name}</option>
                `);
            });
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

// 取得報價單
function getQuotes() {
    let url = `quote/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
    if(!isEmpty(globalUserSelect)){
        url += `&userUuid=${globalUserSelect}`;
    }
    if(!isEmpty(globalCustomerSelect)){
        url += `&customerUuid=${globalCustomerSelect}`;
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
            $("#quote-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                let userName = findUserName(value.userUuid);
                let customerName = findCustomerName(value.customerUuid);
                $("#quote-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.createTime}</td>
                        <td>${userName}</td>
                        <td>${customerName}</td>
                        <td>${value.totalAmount.toLocaleString()}</td>
                        <td style="color: red;">${value.customTotalAmount.toLocaleString()}</td>
                        <td style="color: green;">${value.costTotalAmount.toLocaleString()}</td>
                        <td>${value.status}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-preview-quote-json' data-bs-toggle='offcanvas' data-bs-target='#preview-quote' aria-controls='preview-quote'>預覽</button>
                            <button type='button' class='btn btn-dark btn-sm margin-right-3 get-download-1-quote-json'>
                                <span class="spinner-border spinner-border-sm hide download-quote-loading" aria-hidden="true"></span>
                                <span class="download-1-quote-text" role="status">詠安-下載</span>
                            </button>
                            <button type='button' class='btn btn-dark btn-sm margin-right-3 get-download-2-quote-json'>
                                <span class="spinner-border spinner-border-sm hide download-quote-loading" aria-hidden="true"></span>
                                <span class="download-2-quote-text" role="status">創豐-下載</span>
                            </button>
                            <button type='button' class='btn btn-warning btn-sm margin-right-3 get-update-quote-json'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-quote-json' data-bs-toggle="modal" data-bs-target="#delete-quote">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#quoteListPage', pageTotal, pageNow);
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

// 取得使用者名稱
function findUserName(userUuid) {
    for (let i = 0; i < globalUser.length; i++) {
        if (globalUser[i].userUuid === userUuid) {
            return globalUser[i].name;
        }
    }
    return null;
}

// 取得客戶名稱
function findCustomerName(customerUuid) {
    for (let i = 0; i < globalCustomer.length; i++) {
        if (globalCustomer[i].customerUuid === customerUuid) {
            return globalCustomer[i].name;
        }
    }
    return null;
}

// 預覽報價單
function previewQuote(data){
    $('.preview-quote-inner').addClass('hide');
    $('.preview-quote').append(`
        <div class="d-flex text-secondary justify-content-center preview-quote-loading">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    `);
    const quoteUuid = data.quoteUuid;
    $.ajax({
        url: '/quote/v1/preview/' + quoteUuid,
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00001') {
                alertError('查無資料');
                return;
            }
            let data = response.data;
            $('#preview-quote-customer-name').text(data.customerName);
            $('#preview-quote-user-name').text(data.userName);
            $('#preview-quote-customer-address').text(data.customerAddress);
            $('#preview-quote-undertaker-name').text(data.underTakerName);
            $('#preview-quote-undertaker-tel').text(data.underTakerTel);
            $('#preview-quote-tbody').empty();
            $.each(data.items, function(index, value) {
                $('#preview-quote-tbody').append(`
                    <tr>
                        <td class="preview-quote-index">${value.index}</td>
                        <td class="preview-quote-item-no">${value.itemNo}</td>
                        <td class="preview-quote-item-name">${value.itemName}</td>
                        <td class="preview-quote-item-spec">${value.itemSpec}</td>
                        <td class="preview-quote-item-quantity">${value.quantity}</td>
                        <td class="preview-quote-item-unit">${value.itemUnit}</td>
                        <td class="preview-quote-item-unit-price">${value.itemVendorProductPrice.toLocaleString()}</td>
                        <td class="preview-quote-item-amount">${value.itemVendorProductAmount.toLocaleString()}</td>
                        <td class="preview-quote-item-custom-unit-price" style="color: red;">${value.itemVendorProductCustomPrice.toLocaleString()}</td>
                        <td class="preview-quote-item-custom-amount" style="color: red;">${value.itemVendorProductCustomAmount.toLocaleString()}</td>
                        <td class="preview-quote-item-cost-price" style="color: green;">${value.itemVendorProductCostPrice.toLocaleString()}</td>
                        <td class="preview-quote-item-cost-amount" style="color: green;">${value.itemVendorProductCostAmount.toLocaleString()}</td>
                    </tr>
                `);
            });
            $('#preview-quote-amount').text(data.amount.toLocaleString());
            $('#preview-quote-tax').text(data.tax.toLocaleString());
            $('#preview-quote-total-amount').text(data.totalAmount.toLocaleString());
            $('#preview-quote-custom-amount').text(data.customAmount.toLocaleString());
            $('#preview-quote-custom-tax').text(data.customTax.toLocaleString());
            $('#preview-quote-custom-total-amount').text(data.customTotalAmount.toLocaleString());
            $('#preview-quote-cost-amount').text(data.costAmount.toLocaleString());
            $('#preview-quote-cost-tax').text(data.costTax.toLocaleString());
            $('#preview-quote-cost-total-amount').text(data.costTotalAmount.toLocaleString());
            $('.preview-quote-loading').remove();
            $('.preview-quote-inner').removeClass();
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

// 下載報價單
function download01Quote(row, data){
    let loading = row.find('.download-quote-loading');
    loading.removeClass('hide');
    let loadingText = row.find('.download-1-quote-text');
    loadingText.text('下載中...');
    const quoteUuid = data.quoteUuid;
    const customerName = findCustomerName(data.customerUuid);
    $.ajax({
        url: '/quote/v1/download/' + quoteUuid + '/1',
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        xhrFields: {
            responseType: 'blob'
        },
        success: function (data) {
            let a = document.createElement('a');
            a.href = window.URL.createObjectURL(data);
            a.download = getDate() + customerName + "-詠安設計有限公司報價單" + '.xlsx';
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            loading.addClass('hide');
            loadingText.text('詠安-下載');
        },
        error: function (xhr, status, error) {
            alertError(message);
        }
    });
}

// 下載報價單
function download02Quote(row, data){
    let loading = row.find('.download-quote-loading');
    loading.removeClass('hide');
    let loadingText = row.find('.download-2-quote-text');
    loadingText.text('下載中...');
    const quoteUuid = data.quoteUuid;
    const customerName = findCustomerName(data.customerUuid);
    $.ajax({
        url: '/quote/v1/download/' + quoteUuid + '/2',
        contentType: 'application/json',
        type: 'GET',
        headers: headers,
        xhrFields: {
            responseType: 'blob'
        },
        success: function (data) {
            let a = document.createElement('a');
            a.href = window.URL.createObjectURL(data);
            a.download = getDate() + customerName + "-創豐有限公司報價單" + '.xlsx';
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            loading.addClass('hide');
            loadingText.text('創豐-下載');
        },
        error: function (xhr, status, error) {
            alertError(message);
        }
    });
}

// 刪除報價單
function deleteQuote(){
    const quoteUuid = $('#delete-quote-uuid').val();
    $.ajax({
        url: '/quote/v1/' + quoteUuid,
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

// 新增報價單跳頁
function addQuote(){
    location.href = "/quote/create";
}

// 更新報價單跳頁
function updateQuote(quoteUuid){
    location.href = "/quote/update/" + quoteUuid;
}