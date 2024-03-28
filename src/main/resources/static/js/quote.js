let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalUser = '';
let globalCustomer = '';
let globalKeyword = '';
let globalUserSelect;
let globalCustomerSelect;

window.onload = function() {
    init();
    getUsers();
    getQuotes(globalPageNow, globalPageSize);
    offcanvasEvent();
    pageEvent();
    searchEnter();
    selectChange();
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

function pageEvent(){
    $(document).on("click", ".page-item", function() {
        let pageVal = $(this).find(".page-link").data('val');
        if('pre' == pageVal){
            if(0 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow -= 1;
            getQuotes();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            getQuotes();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        getQuotes();
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
                let status = findStatusName(value.status);
                $("#quote-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.createTime}</td>
                        <td>${userName}</td>
                        <td>${customerName}</td>
                        <td>${value.totalAmount.toLocaleString()}</td>
                        <td style="color: red;">${value.customTotalAmount.toLocaleString()}</td>
                        <td style="color: green;">${value.costTotalAmount.toLocaleString()}</td>
                        <td>${status}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-preview-quote-json' data-bs-toggle='offcanvas' data-bs-target='#preview-quote' aria-controls='preview-quote'>預覽</button>
                            <button type='button' class='btn btn-warning btn-sm margin-right-3 get-update-quote-json'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-quote-json' data-bs-toggle="modal" data-bs-target="#delete-quote-modal">刪除</button>
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

// 取得狀態名稱
function findStatusName(status) {
    if(1 == status){
        return "已建立";
    }
    if(3 == status){
        return "完成";
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

function addQuote(){
    location.href = "/quote/create";
}

function updateQuote(quoteUuid){
    location.href = "/quote/update/" + quoteUuid;
}