package com.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bean.Member;
import com.bean.MemberOrder;
import com.dao.MemberOrderDao;
import com.dao.implemen.MemberOrderDaoImp;

public class FcmAction {
    private MemberOrderDao memberOrderDao = new MemberOrderDaoImp();
    private Set<String> tokens = Collections.synchronizedSet(new HashSet<>());

    public Set<String> getTokensByGroupId(int groupId, int paymentSelect) {
        // 抓取有下這筆訂單的MemberOrder
        Map<MemberOrder, String> map = new HashMap<>();
        map = memberOrderDao.selectAllAndTokenByGroupId(groupId);
        // 選擇要推送訊息的類別，並且取出TokenId
        for (Map.Entry<MemberOrder, String> entry : map.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                continue;
            }
            switch (paymentSelect) {
                // all
                case 0:
                    tokens.add(entry.getValue());
                    break;
                // 已付款
                case 1:
                    if (entry.getKey().getPayentMethod() == 1) {
                        tokens.add(entry.getValue());
                    }
                    break;
                // 未付款
                case 2:
                    if (entry.getKey().getPayentMethod() == 2) {
                        tokens.add(entry.getValue());
                    }
                    break;
                default:
                    break;
            }
        }
        
        
        return tokens;
    }
}
