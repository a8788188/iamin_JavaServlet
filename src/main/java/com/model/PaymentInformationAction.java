package com.model;

import java.util.List;

import com.bean.MemberOrder;
import com.bean.MemberOrderDetails;
import com.dao.MemberOrderDao;
import com.dao.MemberOrderDetailsDao;
import com.dao.implemen.MemberOrderDaoImp;
import com.dao.implemen.MemberOrderDetailsDaoImp;

public class PaymentInformationAction {
    private MemberOrderDao memberOrderDao = new MemberOrderDaoImp();
    private MemberOrderDetailsDao memberOrderDetailsDao = new MemberOrderDetailsDaoImp();
    
    public List<MemberOrder> getAllByGroupId(int groupId) {
        return memberOrderDao.selectAllByGroupId(groupId);
    }
    
    public List<MemberOrderDetails> getAllByMemberOrders(List<MemberOrder> memberOrders) {
        // 抽取所有memberOrderId
        int[] MemberOrderIds = new int[memberOrders.size()];
        for (int i = 0; i < memberOrders.size(); i++) {
            int id = memberOrders.get(i).getMemberOrderId();
            MemberOrderIds[i] = id;
        }
        // 
        return memberOrderDetailsDao.selectAllByMemberOrderIds(MemberOrderIds);
    }
    
    public int updates(List<MemberOrder> memberOrders) {
        int result = -1;
        for (MemberOrder memberOrder : memberOrders) {
            result = memberOrderDao.update(memberOrder);
        }
        return result;
    }
    
    public int updateDeliverStatus(int memberOrderId, boolean status) {
        return memberOrderDao.updateDeliverStatus(memberOrderId, status);
    }
}
