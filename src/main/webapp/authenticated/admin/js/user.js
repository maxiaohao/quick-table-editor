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
        } ],
        onClose : function() {
            $('#formUser').form('clear');
            $("#password").passwordbox('clear');
            $("#retype-password").passwordbox('clear');
        }
    });

    var toolbar = [
            {
                text : 'Refresh',
                iconCls : 'icon-reload',
                handler : function() {
                    refreshAll();
                }
            },
            '-',
            {
                text : 'Create User',
                iconCls : 'icon-add',
                handler : function() {
                    openWindowUserDetails('create', null);
                }
            },
            {
                id : 'btnEdit',
                text : 'Edit Selected User',
                iconCls : 'icon-edit',
                handler : function() {
                    var selUser = $('#gridUsers').datagrid('getSelected');
                    openWindowUserDetails('update', selUser);
                },
                disabled : true
            },
            {
                id : 'btnDelete',
                text : 'Delete Selected User',
                iconCls : 'icon-remove',
                handler : function() {
                    var selUser = $('#gridUsers').datagrid('getSelected')
                    $.messager.confirm('Confirm', 'Are you sure to delete user "' + selUser.login_name + '"?',
                            function(r) {
                                if (r) {
                                    $.ajax({
                                        url : 'admin-crud',
                                        data : {
                                            model : 'user',
                                            action : 'delete',
                                            user_id : selUser.user_id
                                        },
                                        success : function(ret) {
                                            if (ret.success) {
                                                $.messager.alert('Success', 'User "' + selUser.login_name
                                                        + '" has been deleted successfully. ', 'info');
                                                refreshAll();
                                            } else {
                                                $.messager.alert('Error', ret.msg, 'error');
                                            }
                                        },
                                        error : function() {
                                            $.messager.alert('Internal Error', 'Failed get response from server!',
                                                    'error');
                                        }
                                    });
                                }
                            });
                },
                disabled : true
            } ];

    $('#gridUsers').datagrid({
        toolbar : toolbar,
        rownumbers : false,
        singleSelect : true,
        height : '300px',
        idField : 'user_id',
        columns : [ [ {
            field : 'user_id',
            title : 'User ID',
            width : 50,
            align : 'center'
        }, {
            field : 'login_name',
            title : 'Login Name',
            width : 90,
            align : 'left'
        }, {
            field : 'user_name',
            title : 'User Name',
            width : 180,
            align : 'left'
        }, {
            field : 'admin',
            title : 'Is Admin',
            width : 70,
            align : 'center'
        }, {
            field : 'disabled',
            title : 'Is Disabled',
            width : 70,
            align : 'center'
        } ] ],
        onSelect : function(index, row) {
            $('#btnEdit').linkbutton('enable');
            $('#btnDelete').linkbutton('enable');
        },
        onBeforeUnselect : function(index, row) {
            $('#btnEdit').linkbutton('disable');
            $('#btnDelete').linkbutton('disable');
        },
        loader : function(param, success, error) {
            $.ajax({
                url : 'admin-crud?model=user&action=read',
                success : function(data) {
                    success(data.data);
                },
                error : function() {
                    $.messager.alert('Internal Error', 'Failed to get response from server!', 'error');
                }
            });
        }
    });

    $('#formUser').form({});

    function openWindowUserDetails(action, selUser) {
        if ('create' === action) {
            $("#password").passwordbox({
                prompt : 'Password',
                required : true
            });
            $("#retype-password").passwordbox({
                prompt : 'Re-type password',
                required : true
            });
        } else {
            if (selUser) {
                $('#formUser').form('load', {
                    user_id : selUser.user_id,
                    login_name : selUser.login_name,
                    user_name : selUser.user_name,
                    admin : selUser.admin ? 'on' : '',
                    disabled : selUser.disabled ? 'on' : ''
                });
                $("#password").passwordbox({
                    prompt : 'Leave empty for no change',
                    required : false
                });
                $("#retype-password").passwordbox({
                    prompt : 'Leave empty for no change',
                    required : false
                });
            } else {
                $.messager.alert('Error', 'No selected user', 'error');
                return;
            }
        }
        var newWindowTitle = 'create' === action ? 'Create New User' : 'Edit User ( User ID=' + selUser.user_id + " )";
        $("#windowUserDetails").window({
            title : newWindowTitle
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
                var pass1 = $("#password").val();
                var pass2 = $("#retype-password").val();
                if ('create' === action || ('update' === action && (pass1 || pass2))) {
                    if ('create' === action && (!pass1 && !pass2)) {
                        $.messager.alert('Invalid Password', 'Password should not be empty!', 'error');
                        return false;
                    }
                    if (pass1 !== pass2) {
                        $.messager.alert('Invalid Password', 'The two passwords you entered don\'t match!', 'error');
                        return false;
                    }
                    $("#formUser input[name=pwd_md5]").val(md5(pass1).toUpperCase());
                } else {
                    $("#formUser input[name=pwd_md5]").val(null);
                }
                var data = {
                    user_id : $("#formUser input[name=user_id]").val(),
                    login_name : $("#formUser input[name=login_name]").val(),
                    user_name : $("#formUser input[name=user_name]").val(),
                    pwd_md5 : $("#formUser input[name=pwd_md5]").val(),
                    admin : $("#formUser input[name=admin]:checked").val() ? true : false,
                    disabled : $("#formUser input[name=disabled]:checked").val() ? true : false
                };
                $("#formUser input[name=json_data]").val(JSON.stringify(data));
                return true;
            },
            success : function(data) {
                var ret = JSON.parse(data);
                if (ret.success) {
                    $.messager.alert('Success', ret.msg, 'info');
                    refreshAll();
                } else {
                    $.messager.alert('Error', ret.msg, 'error');
                }
            }
        });
        $("#windowUserDetails").window('open');
    }

    function refreshAll() {
        $('#btnEdit').linkbutton('disable');
        $('#btnDelete').linkbutton('disable');
        $("#windowUserDetails").window('close');
        $('#formUser').form('clear');
        $("#gridUsers").datagrid('clearSelections');
        $("#gridUsers").datagrid('reload');
    }

});
