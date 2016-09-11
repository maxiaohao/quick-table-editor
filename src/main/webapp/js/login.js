'use strict';

if ($.messager) {
    $.messager.defaults.border = 'thin';
}

$(function() {

    function submitLogin() {
        $.ajax({
            url : 'login',
            data : {
                login_name : $("#loginName").val(),
                pwd_md5 : md5($("#password").val()).toUpperCase(),
            },
            success : function(ret) {
                if (ret.success) {
                    // redirect to index.html
                    window.location.href = "authenticated/main.html";
                } else {
                    // $.messager.alert('Error', "Incorrect login name or password.", 'error');
                    // $("#loginForm").form("clear");
                    $('#loginButton').linkbutton('disable');
                    $("#errMsg").css('visibility', 'visible');
                    setTimeout(function() {
                        $("#errMsg").css('visibility', 'hidden');
                        $('#loginButton').linkbutton('enable');
                    }, 3000);
                }
            },
            error : function() {
                $.messager.alert('Internal Error', 'Failed get response from server!', 'error');
            }
        });
    }

    $("#loginName").textbox('textbox').bind('keydown', function(e) {
        if (e.keyCode === 13) {
            submitLogin();
        }
    });

    $("#password").textbox('textbox').bind('keydown', function(e) {
        if (e.keyCode === 13) {
            submitLogin();
        }
    });

    $("#loginButton").linkbutton({
        onClick : function() {
            submitLogin();
        }
    });

});
