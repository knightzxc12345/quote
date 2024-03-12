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
    const url = 'common/login/v1';
    const data = {
        "account" : account,
        "password" : password
    }
    let name = '';
    let token = '';
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
    .then(response => response.json())
    .then(data => {
        if(data.code == 'C00001'){
            name = data.data.name;
            token = data.data.token;
        }
        if(isEmpty(name) || isEmpty(token)){
            alertError('查無使用者');
            return;
        }
        localStorage.setItem('name', data.data.name);
        localStorage.setItem('token', data.data.token);
        goQuote();
    })
    .catch(error => console.error('Error:', error));
}

function goQuote(){
    location.href = "/quote";
}