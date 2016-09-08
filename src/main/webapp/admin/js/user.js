'use strict';

$(function() {

    $("#windowCreateUser").window({
        modal : true,
        closed : true,
        minimizable : false,
        maximizable : false,
        collapsible : false,
        border : 'thin'
    });

    $("#windowUpdateUser").window({
        modal : true,
        closed : true,
        minimizable : false,
        maximizable : false,
        collapsible : false,
        border : 'thin'
    });

    var toolbar = [ {
        text : 'Refresh',
        iconCls : 'icon-reload',
        handler : function() {

        }
    }, '-', {
        text : 'Create User',
        iconCls : 'icon-add',
        handler : function() {
            $("#windowCreateUser").window('open');
        }
    }, {
        text : 'Edit Selected User',
        iconCls : 'icon-edit',
        handler : function() {
            $("#windowModifyUser").window('open');
        }
    }, {
        text : 'Delete Selected User',
        iconCls : 'icon-cancel',
        handler : function() {

        }
    } ];

    $('#gridUsers').datagrid({
        toolbar : toolbar,
        rownumbers : false,
        singleSelect : true,
        height : '300px',
        url : '',
        method : 'get',
        columns : [ [ {
            field : 'user_id',
            title : 'User ID',
            width : 100,
            align : 'left'
        }, {
            field : 'user_name',
            title : 'User Name',
            width : 100,
            align : 'left'
        }, {
            field : 'admin',
            title : 'Is Admin',
            width : 100,
            align : 'center'
        }, {
            field : 'disabled',
            title : 'Is Disabled',
            width : 100,
            align : 'center'
        } ] ]
    });

    $('#formCreateUser').form({
        url : 'admin-crud',
        queryParams : {
            model : 'user',
            action : 'create'
        },
        onSubmit : function() {
            var fieldsValid = $('#formCreateUser').form('enableValidation').form('validate');
            if (!fieldsValid) {
                return false;
            }
            var pass1 = $("#windowCreateUser input[name='password']").val();
            var pass2 = $("#windowCreateUser input[name='retype-password']").val();
            if (!pass1 || !pass2) {
                alert('Passwords should not be empty!');
                return false;
            }
            if (pass1 !== pass2) {
                alert('The two passwords you entered don\'t match!');
                return false;
            }
            $("#inputCreateUserPwdMd5").val(md5(pass1).toUpperCase());
            return true;
        },
        success : function(data) {
            // alert(data));
        }
    });
    
    $('#formUpdateUser').form({
        url : 'admin-crud',
        queryParams : {
            model : 'user',
            action : 'update'
        },
        onSubmit : function() {
            var fieldsValid = $('#formUpdateUser').form('enableValidation').form('validate');
            if (!fieldsValid) {
                return false;
            }
            var pass1 = $("#windowUpdateUser input[name='password']").val();
            var pass2 = $("#windowUpdateUser input[name='retype-password']").val();
            if (!pass1 || !pass2) {
                alert('Passwords should not be empty!');
                return false;
            }
            if (pass1 !== pass2) {
                alert('The two passwords you entered don\'t match!');
                return false;
            }
            $("#inputUpdateUserPwdMd5").val(md5(pass1).toUpperCase());
            return true;
        },
        success : function(data) {
            // alert(data));
        }
    });

});

function submitFormCreateUser() {
    $('#formCreateUser').form('submit', {});
}

function submitFormUpdateUser() {
    $('#formUpdateUser').form('submit', {});
}
