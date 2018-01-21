package com.samwoo.slot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.samwoo.slot.greendao.DaoMaster;
import com.samwoo.slot.greendao.DaoSession;
import com.samwoo.slot.greendao.RankDao;

import java.util.List;

/**
 * Created by Administrator on 2018/1/21.
 */

public class DatabaseManager {
    private static final String TAG = "DatabaseManager";
    private static final String DATABASE_NAME = "History_rank";

    private static DatabaseManager sInstance;
    private DaoSession mDaoSession;

    //单例模式
    public static DatabaseManager getsInstance() {
        if (null == sInstance) {
            synchronized (DatabaseManager.class) {
                if (null == sInstance) {
                    sInstance = new DatabaseManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化数据库
     *
     * @param context
     */
    public void initDatabase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * @param rank
     */
    public void addRank(Rank rank) {
        mDaoSession.getRankDao().save(rank);
    }

    /**
     * 根据Id来删除某条数据
     *
     * @param id
     */
    public void deleteRank(Long id) {
        Rank rank = mDaoSession.getRankDao()
                .queryBuilder()
                .where(RankDao.Properties.Id.eq(id))
                .build()
                .unique();
        if (null != rank) {
            mDaoSession.getRankDao().delete(rank);
        }
    }

    /**
     * 删除所有数据记录
     */
    public void deleteAllRank() {
        mDaoSession.getRankDao().deleteAll();
    }

    /**
     * 按得分降序查询所有数据记录
     *
     * @return
     */
    public List<Rank> queryAllRank() {
        List<Rank> mList = mDaoSession.getRankDao()
                .queryBuilder()
                .orderDesc(RankDao.Properties.Score)
                .list();
        return mList;
    }
}
