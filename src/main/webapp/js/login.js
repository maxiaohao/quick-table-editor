'use strict';

if ($.messager) {
    $.messager.defaults.border = 'thin';
}

$(function() {

    $("#loginButton").linkbutton({
        onClick : function() {
            $.ajax({
                url : 'login',
                success : function(ret) {
                    if (ret.success) {
                        // redirect to index.html
                        window.location.href="index.html"; 
                    } else {
                        $.messager.alert('Error', ret.msg, 'error');
                        $("#loginForm").form("clear");
                    }
                },
                error : function() {
                    $.messager.alert('Internal Error', 'Failed get response from server!', 'error');
                }
            });
        }
    });

});
