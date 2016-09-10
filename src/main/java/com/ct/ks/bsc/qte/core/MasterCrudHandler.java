package com.ct.ks.bsc.qte.core;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ct.ks.bsc.qte.db.DataSourcePool;
import com.ct.ks.bsc.qte.db.SqlRunner;
import com.ct.ks.bsc.qte.model.QteDataSource;
import com.ct.ks.bsc.qte.model.User;
import com.ct.ks.bsc.qte.util.CrudResult;
import com.ct.ks.bsc.qte.util.StringUtils;

public class MasterCrudHandler {

    private Logger log = LoggerFactory.getLogger(MasterCrudHandler.class);

    private static final MasterCrudHandler inst = new MasterCrudHandler();


    private MasterCrudHandler() {

    }


    public static MasterCrudHandler getInstance() {
        return inst;
    }


    /**
     * init h2 config db if not exist
     */
    public void initMasterDbIfNotExist() {
        try {
            SqlRunner runner = SqlRunner.getMasterInstance();
            Number cnt = (Number) runner
                    .query1stCell("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'");
            if (cnt.intValue() == 0) {
                log.warn("No master database found and now initialize a new one ...");
                runner.exec("RUNSCRIPT FROM 'classpath:" + Constants.INIT_DB_SQL_FILENAME + "'");
                log.warn("Master database initialzed successfully ({}.h2.db)", Constants.CONFIG_H2_DB_FILE);
            } else {
                log.warn("Found existing master database '{}.h2.db'", Constants.CONFIG_H2_DB_FILE);
            }
        } catch (Exception e) {
            log.error("Fatal error while initializing new master database ({}.h2.db), error: {}"
                    , Constants.CONFIG_H2_DB_FILE, e.getLocalizedMessage());
        }
    }


    // ************************************************************************
    // ************************************************************************
    // ************************ CRUD methods for User *************************
    // ************************************************************************
    // ************************************************************************

    private CrudResult verfiyUserBeforeCreateOrUpdate(User user) {
        if (user == null) {
            return new CrudResult(false, "bad request: user is null!");
        } else if (user.getLogin_name().length() < 1 || user.getLogin_name().length() > 20) {
            return new CrudResult(false, "length of login name should be between 1 and 20");
        } else if (user.getUser_name() != null && user.getUser_name().length() > 100) {
            return new CrudResult(false, "max length of user name should not be over 100");
        }

        try {
            int cnt = ((Number) SqlRunner.getMasterInstance().query1stCell(
                    "select count(*) from qte_t_user where login_name=?", user.getLogin_name())).intValue();
            if (cnt > 0) {
                return new CrudResult(false, "Login name '" + user.getLogin_name()
                        + "' is already taken by some user," + " please try another name.");
            }
        } catch (Exception e1) {
            return new CrudResult(false, "error occurred while verifying existing users in master database: "
                    + e1.getLocalizedMessage());
        }
        return new CrudResult(true);
    }


    public CrudResult createUser(User user) {
        CrudResult verfityResult = verfiyUserBeforeCreateOrUpdate(user);
        if (!verfityResult.isSuccess()) {
            return verfityResult;
        }
        if (user.getPwd_md5() == null || user.getPwd_md5().length() != 32) {
            return new CrudResult(false,
                    "bad request: no valid password md5 value found, the md5 passed from server is: "
                            + user.getPwd_md5());
        }
        try {
            String newSalt = StringUtils.getRandomCapitalLetters(4);
            // salted_md5 = upper(md5(upper(pwd_md5) + salt))
            String saltedMd5 = StringUtils.md5((user.getPwd_md5() + "").toUpperCase() + newSalt);
            SqlRunner
                    .getMasterInstance()
                    .exec(
                            "insert into QTE_T_USER (LOGIN_NAME, USER_NAME, SALT, SALTED_MD5, ADMIN, DISABLED)"
                                    + " values (?,?,?,?,?,?)", user.getLogin_name(), user.getUser_name(), newSalt,
                            saltedMd5, user.isAdmin(), user.isDisabled());
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while creating new user in master database: "
                    + e.getLocalizedMessage());
        }
        return new CrudResult(true, "New user (login name=" + user.getLogin_name() + ") created successfully.");
    }


    public CrudResult getUser(long userId) {
        try {
            User user = SqlRunner.getMasterInstance().queryObj(User.class,
                    "select * from QTE_T_USER where USER_ID=?", userId);
            if (null != user) {
                return new CrudResult(true, user);
            } else {
                return new CrudResult(false, "No user with user ID " + userId + " found.");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting user from master database (USER_ID=" + userId
                    + "): "
                    + e.getLocalizedMessage());
        }
    }


    public CrudResult getUser(String loginName) {
        try {
            User user = SqlRunner.getMasterInstance().queryObj(User.class,
                    "select * from QTE_T_USER where LOGIN_NAME=?", loginName);
            if (null != user) {
                return new CrudResult(true, user);
            } else {
                return new CrudResult(false, "No user with login name '" + loginName + "' found.");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting user from master database (LOGIN_NAME="
                    + loginName + "): "
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
        CrudResult verfityResult = verfiyUserBeforeCreateOrUpdate(user);
        if (!verfityResult.isSuccess()) {
            return verfityResult;
        }
        try {
            int rows = 0;
            if (user.getPwd_md5() != null || user.getPwd_md5().equals("")) {
                rows = SqlRunner
                        .getMasterInstance()
                        .exec("update QTE_T_USER set login_name=?, user_name=?, SALTED_MD5=?, ADMIN=?, disabled=? where user_id=?",
                                user.getLogin_name(), user.getUser_name(),
                                StringUtils.md5((user.getPwd_md5() + "").toUpperCase()
                                        + user.getSalt()), user.isAdmin(), user.isDisabled(), userId);
            } else {
                rows = SqlRunner.getMasterInstance().exec(
                        "update QTE_T_USER set login_name=? user_name=?, ADMIN=?, disabled=? where user_id=?",
                        user.getLogin_name(), user.getUser_name(), user.isAdmin(), user.isDisabled(), userId);
            }
            if (rows > 0) {
                return new CrudResult(true, "User (ID=" + userId + ") updated successfully.");
            } else {
                return new CrudResult(false, "no user with userId " + userId + " found");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while getting users from master database: "
                    + e.getLocalizedMessage());
        }
    }


    public CrudResult deleteUser(long userId) {
        Connection conn = null;
        try {
            conn = DataSourcePool.getMasterConnection();
            SqlRunner.getMasterInstance().exec(conn, "delete from QTE_T_T2U where user_id=?", userId);
            int rows = SqlRunner.getMasterInstance().exec(conn, "delete from QTE_T_USER where user_id=?", userId);
            conn.commit();
            if (rows > 0) {
                return new CrudResult(true);
            } else {
                return new CrudResult(false, "no user with userId " + userId + " found");
            }
        } catch (Exception e) {
            return new CrudResult(false, "error occurred while deleting user from master database: "
                    + e.getLocalizedMessage());
        } finally {
            DbUtils.closeQuietly(conn);
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
