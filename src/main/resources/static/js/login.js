document.addEventListener("keydown", function(event) {
    if (event.key === "Enter") {
        login();
    }
});

function login() {
    const account = $("#account").val();
    const password = $("#password").val();
    const accountValid = validateInput(account, "#account");
    const passwordValid = validateInput(password, "#password");
    if (accountValid && passwordValid) {
        valid(account, password);
    }
}

function validateInput(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    element.removeClass("is-invalid").addClass("is-valid");
    return true;
}

function valid(account, password){
    const data = {
        "account" : account,
        "password" : password
    }
    let name = '';
    let token = '';
    $.ajax({
        url: 'common/login/v1',
        contentType: 'application/json',
        type: 'POST',
        data: JSON.stringify(data),
        success: function(response) {
            if(response.code != 'C00001'){
                alertError('系統錯誤');
                return;
            }
            name = response.data.name;
            token = response.data.token;
            if(isEmpty(name) || isEmpty(token)){
                alertError('查無使用者');
                return;
            }
            localStorage.setItem('name', name);
            localStorage.setItem('token', token);
            location.href = "/quote";
        },
        error: function(xhr, status, error) {
            console.error('Error:', error);
        }
    });
}