let globalPageNow = 0;
let globalPageSize = 12;
let globalPageTotal = 0;
let globalKeyword = '';

window.onload = function () {
    init();
    getVendorProducts(globalPageNow, globalPageSize);
    offcanvasEvent();
    pageEvent();
    searchEnter();
};

function searchEnter(){
    $("#vendor-product-search-input").on("keyup", function(event) {
        if (event.keyCode === 13) {
            search();
        }
    });
}

function search(){
    globalKeyword = $("#vendor-product-search-input").val();
    getVendorProducts();
}

function offcanvasEvent(){
    document.addEventListener('click', function(event) {
        if (event.target.matches('[data-bs-dismiss="offcanvas"]')) {
            $('.offcanvas-body .form-control').val('');
            $('.offcanvas-body .form-control').removeClass('is-valid');
            $('.offcanvas-body .form-control').removeClass('is-invalid');
        }
    });
    $('#vendorProductList').on('click', '.get-update-vendor-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#update-vendor-product-vendor').val(jsonData.vendorUuid);
        $('#update-vendor-product-name').val(jsonData.name);
        $('#update-vendor-product-unit-price').val(jsonData.unitPrice);
    });
    $('#vendorProductList').on('click', '.get-delete-vendor-product-json', function() {
        const row = $(this).closest('tr');
        const jsonData = row.data('json');
        $('#delete-vendor-product-uuid').val(jsonData.vendorProductUuid);
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

function getVendorProducts() {
    $.ajax({
        url: `/vendor-product/v1?page=${globalPageNow}&size=${globalPageSize}&keyword=${globalKeyword}`,
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
            $("#vendor-product-tbody").empty();
            $.each(response.data.responses, function (key, value) {
                $("#vendor-product-tbody").append(`
                    <tr data-json='${JSON.stringify(value)}'>
                        <td>${value.vendorUuid}</td>
                        <td>${value.name}</td>
                        <td>${value.unitPrice}</td>
                        <td>
                            <button type='button' class='btn btn-secondary btn-sm margin-right-3 get-update-vendor-product-json' data-bs-toggle='offcanvas' data-bs-target='#update-vendor-product' aria-controls='update-vendor-product'>編輯</button>
                            <button type='button' class='btn btn-danger btn-sm margin-right-3 get-delete-vendor-product-json' data-bs-toggle="modal" data-bs-target="#delete-vendor-product-modal">刪除</button>
                        </td>
                    </tr>
                `);
            });
            let pageTotal = response.data.pageTotal;
            let pageNow = response.data.pageNow;
            // 設定全域變數
            globalPageTotal = pageTotal;
            globalPageNow = pageNow;
            setPage('#vendorProductListPage', pageTotal, pageNow);
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