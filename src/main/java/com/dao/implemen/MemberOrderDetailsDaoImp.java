package com.dao.implemen;

import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrderDetails;
import com.dao.MemberOrderDetailsDao;
import com.dao.common.ServiceLocator;

public class MemberOrderDetailsDaoImp implements MemberOrderDetailsDao {
    DataSource dataSource;

    public MemberOrderDetailsDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(MemberOrderDetails memberOrderDetails) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(MemberOrderDetails memberOrderDetails) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public MemberOrderDetails selectById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MemberOrderDetails> selectAllByMemberOrderDaoId(int MemberOrderDaoId) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
