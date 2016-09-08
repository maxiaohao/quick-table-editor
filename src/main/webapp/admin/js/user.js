'use strict';

if ($.fn.window) {
    $.fn.window.defaults.border = 'thin';
}

if ($.messager) {
    $.messager.defaults.border = 'thin';
}

$(function() {

    $("#windowUserDetails").dialog({
        modal : true,
        closed : true,
        minimizable : false,
        maximizable : false,
        collapsible : false,
        // border : 'thin',
        buttons : [ {
            text : 'Submit',
            iconCls : 'icon-ok',
            handler : function() {
                $('#formUser').form('submit', {});
            }
        }, {
            text : 'Cancel',
            iconCls : 'icon-cancel',
            handler : function() {
                $("#windowUserDetails").window('close');
            }
        } ]
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
            openWindowUserDetails('create');

        }
    }, {
        id : 'btnEdit',
        text : 'Edit Selected User',
        iconCls : 'icon-edit',
        handler : function() {
            var selUser = $('#gridUsers').datagrid('getSelected');
            $('#formUser').form('load', selUser);
            openWindowUserDetails('update');
        },
        disabled : true
    }, {
        text : 'Delete Selected User',
        iconCls : 'icon-remove',
        handler : function() {

        },
        style : 'text-align:right'
    } ];

    $('#gridUsers').datagrid({
        toolbar : toolbar,
        rownumbers : false,
        singleSelect : true,
        height : '300px',
        url : 'admin-crud?model=user&action=read', //$.evalJSON( encoded )
        method : 'post',
        columns : [ [ {
            field : 'user_id',
            title : 'User ID',
            width : 70,
            align : 'left'
        }, {
            field : 'login_name',
            title : 'Login Name',
            width : 170,
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
        } ] ],
        onSelect : function(index, row) {
            $("#btnEdit").prop({
                disabled : false
            });
        }
    });

    $('#formUser').form({});

});

function openWindowUserDetails(action) {
    $("#windowUserDetails").window({
        title : 'create' === action ? 'Create New User' : 'Edit User'
    });
    $('#formUser').form({
        url : 'admin-crud',
        queryParams : {
            model : 'user',
            action : action
        },
        onSubmit : function() {
            var fieldsValid = $(this).form('enableValidation').form('validate');
            if (!fieldsValid) {
                return false;
            }
            var pass1 = $("#windowUserDetails input[name='password']").val();
            var pass2 = $("#windowUserDetails input[name='retype-password']").val();
            if ('create' === action || ('update' === action && (pass1 || pass2))) {
                if ('create' === action && (!pass1 && !pass2)) {
                    $.messager.alert('Invalid Password', 'Password should not be empty!', 'error');
                    return false;
                }
                if (pass1 !== pass2) {
                    $.messager.alert('Invalid Password', 'The two passwords you entered don\'t match!', 'error');
                    return false;
                }
                $("#inputHiddenPwdMd5").val(md5(pass1).toUpperCase());
            } else {
                $("#inputHiddenPwdMd5").val(null);
            }
            return true;
        },
        success : function(data) {
            // alert(data));
        }
    });
    $("#windowUserDetails").window('open');
}
