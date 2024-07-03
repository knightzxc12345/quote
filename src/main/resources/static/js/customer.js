let globalKeyword = '';

window.onload = function () {
    init();
    getCustomers(globalPageNow, globalPageSize);
    pageEvent(getCustomers);
    searchEnter();
    offcanvasEvent();
};

// 點擊搜尋
function searchEnter(){
    $("#customer-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

// 搜尋
function search(){
    globalKeyword = $("#customer-search-input").val();
    getCustomers();
}

// 事件配置
function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#customerList').on('click', '.get-update-customer-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-customer-uuid').val(jsonData.customerUuid);
        $('#update-customer-name').val(jsonData.name);
        $('#update-customer-address').val(jsonData.address);
        $('#update-customer-vat-number').val(jsonData.vatNumber);
        $('#update-customer-deputy-manager-name').val(jsonData.deputyManagerName);
        $('#update-customer-deputy-manager-mobile').val(jsonData.deputyManagerMobile);
        $('#update-customer-deputy-manager-email').val(jsonData.deputyManagerEmail);
        $('#update-customer-manager-name').val(jsonData.managerName);
        $('#update-customer-manager-mobile').val(jsonData.managerMobile);
        $('#update-customer-manager-email').val(jsonData.managerEmail);
        $('#update-customer-general-affairs-manager-name').val(jsonData.generalAffairsManagerName);
        $('#update-customer-general-affairs-manager-mobile').val(jsonData.generalAffairsManagerMobile);
        $('#update-customer-general-affairs-manager-email').val(jsonData.generalAffairsManagerEmail);
    });
    $('#customerList').on('click', '.get-delete-customer-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-customer-uuid').val(jsonData.customerUuid);
    });
}

// 關閉新增畫布
function closeAdd() {
    let offcanvasElement = document.getElementById('add-customer');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#add-customer input').val('');
    $('#add-customer input').removeClass('is-valid');
    $('#add-customer input').removeClass('is-invalid');
}

// 關閉更新畫布
function closeUpdate() {
    let offcanvasElement = document.getElementById('update-customer');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#update-customer input').removeClass('is-valid');
    $('#update-customer input').removeClass('is-invalid');
}

// 關閉刪除畫布
function closeDelete() {
    let modalElement = document.getElementById('delete-customer');
    let modal = bootstrap.Modal.getInstance(modalElement);
    if (!modal) {
        modal = new bootstrap.Modal(modalElement);
    }
    modal.hide();
}

// 取得客戶清單
function getCustomers() {
    $.ajax({
        url: `/customer/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`,
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
            $("#customer-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                $("#customer-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.name}</td>
                        <td>${value.address}</td>
                        <td>${value.vatNumber}</td>
                        <td>
                            ${value.deputyManagerName}<br/>
                            ${value.deputyManagerMobile}<br/>
                            ${value.deputyManagerEmail}
                        </td>
                        <td>
                            ${value.managerName}<br/>
                            ${value.managerMobile}<br/>
                            ${value.managerEmail}
                        </td>
                        <td>
                            ${value.generalAffairsManagerName}<br/>
                            ${value.generalAffairsManagerMobile}<br/>
                            ${value.generalAffairsManagerEmail}
                        </td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-customer-json' data-bs-toggle='offcanvas' data-bs-target='#update-customer' aria-controls='update-customer'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-customer-json' data-bs-toggle="modal" data-bs-target="#delete-customer">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#customerListPage', pageTotal, pageNow);
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

// 新增客戶
function addCustomer() {
    const name = $("#add-customer-name").val();
    const address = $("#add-customer-address").val();
    const vatNumber = $("#add-customer-vat-number").val();
    const deputyManagerName = $('#add-customer-deputy-manager-name').val();
    const deputyManagerMobile = $('#add-customer-deputy-manager-mobile').val();
    const deputyManagerEmail = $('#add-customer-deputy-manager-email').val();
    const managerName = $('#add-customer-manager-name').val();
    const managerMobile = $('#add-customer-manager-mobile').val();
    const managerEmail = $('#add-customer-manager-email').val();
    const generalAffairsManagerName = $('#add-customer-general-affairs-manager-name').val();
    const generalAffairsManagerMobile = $('#add-customer-general-affairs-manager-mobile').val();
    const generalAffairsManagerEmail = $('#add-customer-general-affairs-manager-email').val();
    // 驗證姓名是否為空
    const nameValid = validateInput(name, "#add-customer-name");
    if (!nameValid) {
        return;
    }
    let data = {
        name: name,
        address: address,
        vatNumber: vatNumber,
        deputyManagerName: deputyManagerName,
        deputyManagerMobile: deputyManagerMobile,
        deputyManagerEmail: deputyManagerEmail,
        managerName: managerName,
        managerMobile: managerMobile,
        managerEmail: managerEmail,
        generalAffairsManagerName: generalAffairsManagerName,
        generalAffairsManagerMobile: generalAffairsManagerMobile,
        generalAffairsManagerEmail: generalAffairsManagerEmail
    };
    $.ajax({
        url: '/customer/v1',
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'POST',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00003') {
                alertError('系統錯誤');
                return;
            }
            closeAdd();
            search();
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

// 更新客戶
function updateCustomer() {
    const customerUuid = $('#update-customer-uuid').val();
    const name = $("#update-customer-name").val();
    const address = $("#update-customer-address").val();
    const vatNumber = $("#update-customer-vat-number").val();
    const deputyManagerName = $('#update-customer-deputy-manager-name').val();
    const deputyManagerMobile = $('#update-customer-deputy-manager-mobile').val();
    const deputyManagerEmail = $('#update-customer-deputy-manager-email').val();
    const managerName = $('#update-customer-manager-name').val();
    const managerMobile = $('#update-customer-manager-mobile').val();
    const managerEmail = $('#update-customer-manager-email').val();
    const generalAffairsManagerName = $('#update-customer-general-affairs-manager-name').val();
    const generalAffairsManagerMobile = $('#update-customer-general-affairs-manager-mobile').val();
    const generalAffairsManagerEmail = $('#update-customer-general-affairs-manager-email').val();
    // 驗證姓名是否為空
    const nameValid = validateInput(name, "#update-customer-name");
    if (!nameValid) {
        return;
    }
    let data = {
        name: name,
        address: address,
        vatNumber: vatNumber,
        deputyManagerName: deputyManagerName,
        deputyManagerMobile: deputyManagerMobile,
        deputyManagerEmail: deputyManagerEmail,
        managerName: managerName,
        managerMobile: managerMobile,
        managerEmail: managerEmail,
        generalAffairsManagerName: generalAffairsManagerName,
        generalAffairsManagerMobile: generalAffairsManagerMobile,
        generalAffairsManagerEmail: generalAffairsManagerEmail
    };
    $.ajax({
        url: '/customer/v1/' + customerUuid,
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'PUT',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00004') {
                alertError('系統錯誤');
                return;
            }
            closeUpdate();
            search();
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

// 刪除客戶
function deleteCustomer(){
    const customerUuid = $('#delete-customer-uuid').val();
    $.ajax({
        url: '/customer/v1/' + customerUuid,
        contentType: 'application/json',
        type: 'DELETE',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00005') {
                alertError('系統錯誤');
                return;
            }
            closeDelete();
            search();
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