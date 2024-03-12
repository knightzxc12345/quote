const globalUserName = localStorage.getItem('name');
const globalToken = localStorage.getItem('token');

window.onload = function() {
    if(window.location.pathname === '/'){
        return;
    }
    if(isEmpty(globalUserName) || isEmpty(globalToken)){
        goBack();
        return;
    }
    $("#headerUserName").text(globalUserName);
};

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