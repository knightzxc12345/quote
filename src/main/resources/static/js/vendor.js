let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';

window.onload = function () {
    init();
    getVendors(globalPageNow, globalPageSize);
    offcanvasEvent();
    pageEvent();
    searchEnter();
};

function searchEnter(){
    $("#vendor-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function search(){
    globalKeyword = $("#vendor-search-input").val();
    getVendors();
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#vendorList').on('click', '.get-update-vendor-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-vendor-uuid').val(jsonData.vendorUuid);
        $('#update-vendor-name').val(jsonData.name);
        $('#update-vendor-address').val(jsonData.address);
        $('#update-vendor-mobile').val(jsonData.mobile);
        $('#update-vendor-tel').val(jsonData.tel);
        $('#update-vendor-fax').val(jsonData.fax);
    });
    $('#vendorList').on('click', '.get-delete-vendor-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-vendor-uuid').val(jsonData.vendorUuid);
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
            getVendors();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            getVendors();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        getVendors();
    });
}

function getVendors() {
    $.ajax({
        url: `/vendor/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`,
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
            $("#vendor-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                $("#vendor-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.name}</td>
                        <td>${value.address}</td>
                        <td>${value.mobile}</td>
                        <td>${value.tel}</td>
                        <td>${value.fax}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-vendor-json' data-bs-toggle='offcanvas' data-bs-target='#update-vendor' aria-controls='update-vendor'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-vendor-json' data-bs-toggle="modal" data-bs-target="#delete-vendor-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#vendorListPage', pageTotal, pageNow);
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

function addVendor() {
    const name = $("#add-vendor-name").val();
    const address = $("#add-vendor-address").val();
    const mobile = $("#add-vendor-mobile").val();
    const tel = $("#add-vendor-tel").val();
    const fax = $("#add-vendor-fax").val();
    // 驗證
    const nameValid = validateInput(name, "#add-vendor-name");
    if (!nameValid) {
        return;
    }
    let data = {
        name: name,
        address: address,
        mobile: mobile,
        tel: tel,
        fax: fax
    };
    $.ajax({
        url: '/vendor/v1',
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'POST',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00003') {
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

function updateVendor() {
    const vendorUuid = $('#update-vendor-uuid').val();
    const name = $("#update-vendor-name").val();
    const address = $("#update-vendor-address").val();
    const mobile = $("#update-vendor-mobile").val();
    const tel = $("#update-vendor-tel").val();
    const fax = $("#update-vendor-fax").val();
    // 驗證
    const nameValid = validateInput(name, "#add-vendor-name");
    if (!nameValid) {
        return;
    }
    let data = {
        name: name,
        address: address,
        mobile: mobile,
        tel: tel,
        fax: fax
    };
    $.ajax({
        url: '/vendor/v1/' + vendorUuid,
        contentType: 'application/json',
        data: JSON.stringify(data),
        type: 'PUT',
        headers: headers,
        success: function (response) {
            if (response.code != 'C00004') {
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

function deleteVendor(){
    const vendorUuid = $('#delete-vendor-uuid').val();
    $.ajax({
        url: '/vendor/v1/' + vendorUuid,
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

function validateInput(value, elementId) {
    const element = $(elementId);
    if (isEmpty(value)) {
        element.removeClass("is-valid").addClass("is-invalid");
        return false;
    }
    element.removeClass("is-invalid").addClass("is-valid");
    return true;
}