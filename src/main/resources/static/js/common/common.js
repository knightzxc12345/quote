const globalUserName = localStorage.getItem('name');
const globalToken = localStorage.getItem('token');
const headers = {
    'Authorization': `Bearer ${globalToken}`,
};
let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;

function init(){
    valid();
    // 初始化人名
    $("#header-username").text(globalUserName);
    // 初始化menu
    if(window.location.pathname === '/customer'){
        $("#menu-customer").addClass('menu-active');
    }
    if(window.location.pathname === '/vendor'){
        $("#menu-vendor").addClass('menu-active');
    }
    if(window.location.pathname === '/vendor-product'){
        $("#menu-vendor-product").addClass('menu-active');
    }
    if(window.location.pathname === '/item'){
        $("#menu-item").addClass('menu-active');
    }
    if(window.location.pathname === '/quote'){
        $("#menu-quote").addClass('menu-active');
    }
    if(window.location.pathname === '/quote/create'){
        $("#menu-quote").addClass('menu-active');
    }
    if(window.location.pathname.includes('/quote/update/')){
        $("#menu-quote").addClass('menu-active');
    }
}

function valid(){
    if(window.location.pathname === '/'){
        return;
    }
    if(isEmpty(globalUserName) || isEmpty(globalToken)){
        goBack();
        return;
    }
}

function isEmpty(value) {
    return value === null || value === undefined || value === '' || value.length == 0;
}

function alertSuccess(message){
    $(".alert-success-message").val(message);
    $(".alert-success").css("display", "");
    window.setTimeout(function(){
        $(".alert-success").css("display", "none");
    }, 5000);
}

function alertError(message){
    $(".alert-danger-message").text(message);
    $(".alert-danger").css("display", "block");
    window.setTimeout(function(){
        $(".alert-danger").css("display", "none");
    }, 5000);
}

function alertWarning(message){
    $(".alert-warning-message").text(message);
    $(".alert-warning").css("display", "block");
    window.setTimeout(function(){
        $(".alert-warning").css("display", "none");
    }, 5000);
}

function goBack(){
    location.href = "/";
}

function logout(){
    localStorage.setItem('name', null);
    localStorage.setItem('token', null);
    location.href = "/";
}

function setPage(elementId, pageTotal, pageNow){
    $(elementId).empty();
    if(0 == pageTotal){
        return;
    }
    if(0 == pageNow){
        $(elementId).append(`
            <li class="page-item disabled">
                <span class="page-link" data-val="pre">上一頁</span>
            </li>
        `);
    }
    if(0 != pageNow){
        $(elementId).append(`
            <li class="page-item">
                <span class="page-link" data-val="pre">上一頁</span>
            </li>
        `);
    }
    for(let i = 0; i < pageTotal; i++){
        if(i == pageNow){
            $(elementId).append(`
                <li class="page-item active">
                    <span class="page-link" data-val="${i + 1}">${i + 1}</span>
                </li>
            `);
        }
        if(i != pageNow){
            $(elementId).append(`
                <li class="page-item">
                    <span class="page-link" data-val="${i + 1}">${i + 1}</span>
                </li>
            `);
        }

    }
    if(pageTotal == pageNow + 1){
        $(elementId).append(`
            <li class="page-item disabled">
                <span class="page-link" data-val="next">下一頁</span>
            </li>
        `);
    }
    if(pageTotal != pageNow + 1){
        $(elementId).append(`
            <li class="page-item">
                <span class="page-link" data-val="next">下一頁</span>
            </li>
        `);
    }
}

// 驗證輸入框
function validateInput(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    element.removeClass("is-invalid").addClass("is-valid");
    return true;
}

// 驗證數字
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

// 驗證下拉選單
function validateSelect(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        alertWarning("廠商至少要選一個");
        return false;
    }
    return true;
}

// 分頁配置
function pageEvent(action){
    $(document).on("click", ".page-item", function() {
        let pageVal = $(this).find(".page-link").data('val');
        if('pre' == pageVal){
            if(0 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow -= 1;
            action();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            action();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        action();
    });
}

function generateUUID() {
    let chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    let result = '';
    for (var i = 0; i < 10; i++) {
        var randomIndex = Math.floor(Math.random() * chars.length);
        result += chars.charAt(randomIndex);
    }
    return result;
}

function getDate(){
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}