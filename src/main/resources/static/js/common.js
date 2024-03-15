const globalUserName = localStorage.getItem('name');
const globalToken = localStorage.getItem('token');
const headers = {
    'Authorization': `Bearer ${globalToken}`,
};

function init(){
    valid();
    // 初始化人名
    $("#header-username").text(globalUserName);
    // 初始化menu
    if(window.location.pathname === '/customer'){
        $("#menu-customer").addClass('menu-active');
    }
    if(window.location.pathname === '/product'){
        $("#menu-product").addClass('menu-active');
    }
    if(window.location.pathname === '/quote'){
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
    return value === null || value === undefined || value === '';
}

function alertSuccess(message){
    $("#alert-success-message").val(message);
    $("#alert-success").css("display", "");
    window.setTimeout(function(){
        $("#alert-success").css("display", "none");
    }, 5000);
}

function alertError(message){
    $("#alert-danger-message").text(message);
    $("#alert-danger").css("display", "block");
    window.setTimeout(function(){
        $("#alert-danger").css("display", "none");
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