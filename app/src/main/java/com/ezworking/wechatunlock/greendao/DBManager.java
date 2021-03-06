package com.ezworking.wechatunlock.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ezworking.wechatunlock.domain.ContactResult;
import com.ezworking.wechatunlock.domain.ContactResultDao;
import com.ezworking.wechatunlock.domain.DaoMaster;
import com.ezworking.wechatunlock.domain.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by wangchao on 2017/6/4 0004.
 */

public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param contactResult
     */
    public void insertContact(ContactResult contactResult) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        userDao.insert(contactResult);
    }

    /**
     * 插入一组信息
     */
    public void insertContactsInfo(List<ContactResult> contactResult) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao goodsInfoDao = daoSession.getContactResultDao();
        goodsInfoDao.insertOrReplaceInTx(contactResult);
    }


    /**
     * 删除一条记录
     *
     * @param contactResult
     */
    public void deleteUser(ContactResult contactResult) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        userDao.delete(contactResult);
    }

    /**
     * 更新一条记录
     *
     * @param contactResult
     */
    public void updateUser(ContactResult contactResult) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        userDao.update(contactResult);
    }

    /**
     * 查询用户列表
     */
    public List<ContactResult> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        QueryBuilder<ContactResult> qb = userDao.queryBuilder();
        List<ContactResult> list = qb.list();
        return list;
    }




    /**
     * 查询用户列表
     */
    public List<ContactResult> queryUserList(String identifier) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        QueryBuilder<ContactResult> qb = userDao.queryBuilder();
        qb.where(ContactResultDao.Properties.Identifier.eq(identifier));
        List<ContactResult> list = qb.list();
        return list;
    }

    /**
     * 查询扫描的所有商品信息
     *
     * @return
     */
    public List<ContactResult> queryScanGoodsInfo(String identifier) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactResultDao userDao = daoSession.getContactResultDao();
        QueryBuilder<ContactResult> queryBuilder = userDao.queryBuilder()
                .where(ContactResultDao.Properties.Identifier.eq(identifier));
        return queryBuilder.list();
    }
}
