package com.dao.implemen;

import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrder;
import com.dao.MemberOrderDao;
import com.dao.common.ServiceLocator;

public class MemberOrderDaoImp implements MemberOrderDao {
    DataSource dataSource;

    public MemberOrderDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(MemberOrder memberOrder) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(MemberOrder memberOrder) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MemberOrder selectById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrder> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrder> selectAllByGroupId(int groupId) {
        // TODO Auto-generated method stub
        return null;
    }
}
