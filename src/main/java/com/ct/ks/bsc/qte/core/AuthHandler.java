package com.ct.ks.bsc.qte.core;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ct.ks.bsc.qte.model.User;
import com.ct.ks.bsc.qte.util.CrudResult;
import com.ct.ks.bsc.qte.util.StringUtils;

public class AuthHandler {

    /**
     * log writer for this class
     */
    private final Logger log = LoggerFactory.getLogger(AuthHandler.class);

    private static final AuthHandler inst = new AuthHandler();


    private AuthHandler() {

    }


    public static AuthHandler getInstance() {
        return inst;
    }


    public boolean authenticateUser(String loginName, String pwdMd5) {
        if (null == loginName || loginName.length() == 0 || null == pwdMd5 || pwdMd5.length() != 32) {
            return false;
        }
        CrudResult result = MasterCrudHandler.getInstance().getUser(loginName);
        if (null == result || result.getData() == null) {
            return false;
        } else {
            User user = (User) result.getData();
            try {
                // salted_md5 = upper(md5(upper(pwd_md5) + salt))
                return StringUtils.md5(pwdMd5.toUpperCase() + user.getSalt()).toUpperCase()
                        .equals(user.getSalted_md5());
            } catch (NoSuchAlgorithmException e) {
                log.error("Error occurred while authenticating user {}'s password using md5: {}", loginName,
                        e.getLocalizedMessage());
            }
            return false;
        }
    }


    public boolean isAuthenticated(String loginName) {
        CrudResult result = MasterCrudHandler.getInstance().getUser(loginName);
        if (null != result && null != result.getData() && null != ((User) result.getData())) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isAdmin(String loginName) {
        CrudResult result = MasterCrudHandler.getInstance().getUser(loginName);
        if (null != result && null != result.getData() && null != ((User) result.getData())
                && ((User) result.getData()).isAdmin()) {
            return true;
        } else {
            return false;
        }
    }
}
