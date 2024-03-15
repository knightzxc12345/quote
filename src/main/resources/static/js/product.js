let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';

window.onload = function () {
    init();
    getProducts(globalPageNow, globalPageSize);
    offcanvasEvent();
    pageEvent();
    searchEnter();
};

function searchEnter(){
    $("#product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            globalKeyword = $("#product-search-input").val();
            getProducts();
        }
    });
}

function search(){
    globalKeyword = $("#product-search-input").val();
    getProducts();
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('#add-product-no').val('');
            $('#add-product-name').val('');
            $('#add-product-specification').val('');
            $('#add-product-unit').val('');
            $('#add-product-unit-price').val('');
            $('#add-product-origin-price').val('');
            $('#update-product-no').val('');
            $('#update-product-name').val('');
            $('#update-product-specification').val('');
            $('#update-product-unit').val('');
            $('#update-product-unit-price').val('');
            $('#update-product-origin-price').val('');
        }
    });
    $('#productList').on('click', '.get-update-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-product-uuid').val(jsonData.productUuid);
        $('#update-product-no').val(jsonData.no);
        $('#update-product-name').val(jsonData.name);
        $('#update-product-specification').val(jsonData.specification);
        $('#update-product-unit').val(jsonData.unit);
        $('#update-product-unit-price').val(jsonData.unitPrice);
        $('#update-product-origin-price').val(jsonData.originPrice);
    });
    $('#productList').on('click', '.get-delete-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-product-uuid').val(jsonData.productUuid);
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
            getProducts();
            return;
        }
        if('next' == pageVal){
            if(globalPageTotal - 1 == globalPageNow){
                return;
            }
            // 設定全域變數
            globalPageNow += 1;
            getProducts();
            return;
        }
        let numberPageText = parseInt(pageVal, 10);
        // 設定全域變數
        globalPageNow = numberPageText - 1;
        getProducts();
    });
}

function getProducts() {
    let name = '';
    let token = '';
    $.ajax({
        url: `product/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`,
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
            $("#product-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                $("#product-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td class='hide'>${value.productUuid}</th>
                        <td>${value.no}</td>
                        <td>${value.name}</td>
                        <td>${value.specification}</td>
                        <td>${value.unit}</td>
                        <td>${value.unitPrice}</td>
                        <td>${value.originPrice}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-product-json' data-bs-toggle='offcanvas' data-bs-target='#update-product' aria-controls='update-product'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-product-json' data-bs-toggle="modal" data-bs-target="#delete-product-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#productListPage', pageTotal, pageNow);
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

function addProduct() {
    const no = $("#add-product-no").val();
    const name = $("#add-product-name").val();
    const specification = $("#add-product-specification").val();
    const unit = $("#add-product-unit").val();
    const unitPrice = $("#add-product-unit-price").val();
    const originPrice = $("#add-product-origin-price").val();
    // 驗證
    const noValid = validateInput(name, "#add-product-no");
    const nameValid = validateInput(name, "#add-product-name");
    const specificationValid = validateInput(name, "#add-product-specification");
    const unitValid = validateInput(name, "#add-product-unit");
    const unitPriceValid = validateInput(name, "#add-product-unit-price");
    const originPriceValid = validateInput(name, "#add-product-origin-price");
    if (!noValid || !nameValid || !specificationValid || !unitValid || !unitPriceValid || !originPriceValid) {
        return;
    }
    var data = {
        no: no,
        name: name,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        originPrice: originPrice
    };
    $.ajax({
        url: 'product/v1',
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

function updateProduct() {
    const productUuid = $('#update-product-uuid').val();
    const no = $("#add-product-no").val();
    const name = $("#add-product-name").val();
    const specification = $("#add-product-specification").val();
    const unit = $("#add-product-unit").val();
    const unitPrice = $("#add-product-unit-price").val();
    const originPrice = $("#add-product-origin-price").val();
    // 驗證
    const noValid = validateInput(name, "#add-product-no");
    const nameValid = validateInput(name, "#add-product-name");
    const specificationValid = validateInput(name, "#add-product-specification");
    const unitValid = validateInput(name, "#add-product-unit");
    const unitPriceValid = validateInput(name, "#add-product-unit-price");
    const originPriceValid = validateInput(name, "#add-product-origin-price");
    if (!noValid || !nameValid || !specificationValid || !unitValid || !unitPriceValid || !originPriceValid) {
        return;
    }
    var data = {
        no: no,
        name: name,
        specification: specification,
        unit: unit,
        unitPrice: unitPrice,
        originPrice: originPrice
    };
    $.ajax({
        url: 'product/v1/' + productUuid,
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

function deleteProduct(){
    const productUuid = $('#delete-product-uuid').val();
    $.ajax({
        url: 'product/v1/' + productUuid,
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