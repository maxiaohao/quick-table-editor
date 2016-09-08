package com.ct.ks.bsc.qte.core;

import java.util.List;

import com.ct.ks.bsc.qte.db.SqlRunner;
import com.ct.ks.bsc.qte.model.QteDataSource;
import com.ct.ks.bsc.qte.model.User;
import com.ct.ks.bsc.qte.util.CrudResult;
import com.ct.ks.bsc.qte.util.StringUtils;

public class MasterCrudHandler {

    private static final MasterCrudHandler inst = new MasterCrudHandler();


    private MasterCrudHandler() {

    }


    public static MasterCrudHandler getInstance() {
        return inst;
    }


    // ************************************************************************
    // ************************************************************************
    // ************************ CRUD methods for User *************************
    // ************************************************************************
    // ************************************************************************

    public CrudResult createUser(User user) {
        try {
            String newSalt = StringUtils.getRandomCapitalLetters(4);
            // salted_md5 = upper(md5(user_name + upper(pwd_md5) + salt))
            String saltedMd5 = StringUtils.md5(user.getLogin_name() + (user.getPwd_md5() + "").toUpperCase() + newSalt);
            SqlRunner.getMasterInstance().exec(
                    "insert into QTE_T_USER (LOGIN_NAME, SALT, SALTED_MD5, IS_ADMIN, IS_DISABLED) values (?,?,?,?,?)",
                    user.getLogin_name(), newSalt, saltedMd5, user.isAdmin(), user.isDisabled());
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while creating new user: " + e.getLocalizedMessage());
        }
        return new CrudResult(true);
    }


    public CrudResult getUser(String userName) {
        try {
            User user = SqlRunner.getMasterInstance().queryObj(User.class,
                    "select * from QTE_T_USER where USER_NAME=?",
                    userName);
            return new CrudResult(true, user);
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting user (" + userName + "): "
                    + e.getLocalizedMessage());
        }
    }


    public CrudResult getUsers() {
        try {
            List<User> users = SqlRunner.getMasterInstance().queryObjs(User.class,
                    "select * from QTE_T_USER order by user_id");
            return new CrudResult(true, users);
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting users: " + e.getLocalizedMessage());
        }
    }


    public CrudResult updateUser(long userId, User user) {
        try {
            int rows = 0;
            if (user.getPwd_md5() != null || user.getPwd_md5().equals("")) {
                rows = SqlRunner.getMasterInstance()
                        .exec(
                                "update QTE_T_USER set user_name=?, SALTED_MD5=?, ADMIN=?, disabled=? where user_id=?",
                                user.getLogin_name(),
                                StringUtils.md5(user.getLogin_name() + (user.getPwd_md5() + "").toUpperCase()
                                        + user.getSalt()), user.isAdmin(), user.isDisabled(), userId);
            } else {
                rows = SqlRunner.getMasterInstance().exec(
                        "update QTE_T_USER set user_name=?, ADMIN=?, disabled=? where user_id=?", user.getLogin_name(),
                        user.isAdmin(), user.isDisabled(), userId);
            }
            if (rows > 0) {
                return new CrudResult(true);
            } else {
                return new CrudResult(false, "no user with userId " + userId + " found");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting users: " + e.getLocalizedMessage());
        }
    }


    public CrudResult deleteUser(long userId) {
        try {
            int rows = SqlRunner.getMasterInstance().exec("delete from QTE_T_USER where user_id=?", userId);
            if (rows > 0) {
                return new CrudResult(true);
            } else {
                return new CrudResult(false, "no user with userId " + userId + " found");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting users: " + e.getLocalizedMessage());
        }
    }


    // ************************************************************************
    // ************************************************************************
    // ******************** CRUD methods for QteDataSource ********************
    // ************************************************************************
    // ************************************************************************

    /**
     *
     * @param dsName
     * @return
     * @throws Exception
     */
    public QteDataSource getQteDataSource(String dsName) throws Exception {
        return SqlRunner.getMasterInstance().queryObj(QteDataSource.class,
                "select * from QTE_T_DATASOURCE where DS_NAME=?", dsName);
    }

    // ************************************************************************
    // ************************************************************************
    // ************************ CRUD methods for Table ************************
    // ************************************************************************
    // ************************************************************************
}
