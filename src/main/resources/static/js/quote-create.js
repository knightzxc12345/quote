let globalVendor = '';
let globalProduct = '';

window.onload = function () {
    init();
    select2Init();
    getVendors();
    selectChange();
};

function select2Init(){
    $(".select2").select2();
}

function backQuote(){
    location.href = "/quote"
}

function selectChange(){
    $('.add-vendor-name-select').off().change(function() {
        let tr = $(this).closest('tr');
        let selectProductName = tr.find('.add-product-name-select');
        selectProductName.empty();
        let selectedVendor = $(this).val();
        $.each(globalProduct, function(key, value) {
            if(value.vendorUuid != selectedVendor){
                return;
            }
            selectProductName.append(`
                <option value="${value.productUuid}">${value.name}</option>
            `);
        });
    });
    $('.add-product-name-select').off().change(function() {
        let tr = $(this).closest('tr');
        let selectProductSpecification = tr.find('.add-product-specification-select');
        selectProductSpecification.empty();
        let selectedVendor = tr.find('.add-vendor-name-select').val();
        let selectedProduct = $(this).val();
        $.each(globalProduct, function(key, value) {
            if(value.vendorUuid != selectedVendor){
                return;
            }
            if(value.productUuid != selectedProduct){
                return;
            }
            selectProductSpecification.append(`
                <option value="${value.productUuid}">${value.specification}</option>
            `);
        });
    });
}

function getVendors(){
    $.ajax({
        url: `/common/vendor/v1`,
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
            globalVendor = response.data;
            getProducts();
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

function getProducts(){
    $.ajax({
        url: `/common/product/v1`,
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
            globalProduct = response.data;
            setSelect();
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

function setSelect(){
    let selectVendor = $('.add-vendor-name-select:last');
    $.each(globalVendor, function(key, value) {
        selectVendor.append(`
            <option value='${value.vendorUuid}'>${value.name}</option>
        `);
    });
    let selectedVendorUuid = selectVendor.val();
    let selectProductName = $('.add-product-name-select:last');
    selectProductName.empty();
    $.each(globalProduct, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        selectProductName.append(`
            <option value='${value.productUuid}'>${value.name}</option>
        `);
    });
    let selectedProductUuid = selectProductName.val();
    let selectProductSpecification = $('.add-product-specification-select:last');
    selectProductSpecification.empty();
    $.each(globalProduct, function(key, value) {
        if(value.vendorUuid != selectedVendorUuid){
            return;
        }
        if(value.productUuid != selectedProductUuid){
            return;
        }
        selectProductSpecification.append(`
            <option value='${value.productUuid}'>${value.specification}</option>
        `);
    });
}