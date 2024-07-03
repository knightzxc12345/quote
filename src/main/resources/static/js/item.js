let globalKeyword = '';

window.onload = function () {
    init();
    getItems();
    searchEnter();
    pageEvent(getItems);
    offcanvasEvent();
};

// 點擊搜尋
function searchEnter(){
    $("#item-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

// 搜尋
function search(){
    globalKeyword = $("#item-search-input").val();
    getItems();
}

// 關閉新增畫布
function closeAdd() {
    let offcanvasElement = document.getElementById('add-item');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#add-item input').val('');
    $('#add-item input').removeClass('is-valid');
    $('#add-item input').removeClass('is-invalid');
}

// 關閉更新畫布
function closeUpdate() {
    let offcanvasElement = document.getElementById('update-item');
    let offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
    if (!offcanvas) {
        offcanvas = new bootstrap.Offcanvas(offcanvasElement);
    }
    offcanvas.hide();
    $('#update-item input').removeClass('is-valid');
    $('#update-item input').removeClass('is-invalid');
}

// 關閉刪除畫布
function closeDelete() {
    let modalElement = document.getElementById('delete-item');
    let modal = bootstrap.Modal.getInstance(modalElement);
    if (!modal) {
        modal = new bootstrap.Modal(modalElement);
    }
    modal.hide();
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
    $('#itemList').on('click', '.get-update-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-item-uuid').val(jsonData.itemUuid);
        $('#update-item-no').val(jsonData.no);
        $('#update-item-name').val(jsonData.name);
    });
    $('#itemList').on('click', '.get-delete-item-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-item-uuid').val(jsonData.itemUuid);
    });
}

// 取得項目清單
function getItems() {
    let url = `/item/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`;
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
            $("#item-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                $("#item-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.no}</td>
                        <td>${value.name}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-item-json' data-bs-toggle='offcanvas' data-bs-target='#update-item' aria-controls='update-item'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-item-json' data-bs-toggle="modal" data-bs-target="#delete-item">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#itemListPage', pageTotal, pageNow);
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

// 新增項目
function addItem() {
    const no = $("#add-item-no").val();
    const name = $("#add-item-name").val();
    // 驗證
    const noValid = validateInput(no, "#add-item-no");
    const nameValid = validateInput(name, "#add-item-name");
    if (!noValid || !nameValid) {
        return;
    }
    let data = {
        no: no,
        name: name
    };
    $.ajax({
        url: '/item/v1',
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

// 更新項目
function updateItem() {
    const itemUuid = $('#update-item-uuid').val();
    const no = $("#update-item-no").val();
    const name = $("#update-item-name").val();
    // 驗證
    const noValid = validateInput(no, "#update-item-no");
    const nameValid = validateInput(name, "#update-item-name");
    if (!noValid || !nameValid) {
        return;
    }
    let data = {
        no: no,
        name: name
    };
    $.ajax({
        url: '/item/v1/' + itemUuid,
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

// 刪除項目
function deleteItem(){
    const itemUuid = $('#delete-item-uuid').val();
    $.ajax({
        url: '/item/v1/' + itemUuid,
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