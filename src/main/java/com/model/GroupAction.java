package com.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.bean.Group;
import com.bean.GroupBlockade;
import com.bean.GroupCategory;
import com.bean.Location;
import com.bean.Merch;
import com.dao.GroupBlockadeDao;
import com.dao.GroupCategoryDao;
import com.dao.GroupDao;
import com.dao.GroupListDao;
import com.dao.LocationDao;
import com.dao.MerchDao;
import com.dao.implemen.GroupBlockadeDaoImp;
import com.dao.implemen.GroupCategoryDaoImp;
import com.dao.implemen.GroupDaoImp;
import com.dao.implemen.GroupListDaoImp;
import com.dao.implemen.LocationDaoImp;
import com.dao.implemen.MerchDaoImp;

public class GroupAction {
    private GroupDao groupDao = new GroupDaoImp();
    private MerchDao merchDao = new MerchDaoImp();
    private GroupCategoryDao categoryDao = new GroupCategoryDaoImp();
    private LocationDao locationDao = new LocationDaoImp();
    private GroupListDao groupListDao = new GroupListDaoImp();
    private GroupBlockadeDao groupBlockadeDao = new GroupBlockadeDaoImp();

    public List<Group> getAll() {
        return groupDao.selectAll();
    }
    
    /**
     * 使用會員ID抓取目前此會員開團購的清單
     * @param memberId
     * @return
     */
    public List<Group> getAllByMemberId(int memberId) {
        List<Group> groups = new ArrayList<>();
        groups = groupDao.selectAllByMemberId(memberId);
        
        for (Group group : groups) {
            group.setMerchs(merchDao.selectAllByGroupId(group.getGroupId()));
        }
        
        return groups;
    }
    
    /**
     * 抓取目前DB上有哪些團購種類
     * @return
     */
    public List<GroupCategory> getAllCategory() {
        return categoryDao.selectAll();
    }
    
    public int add(Group group, List<Double[]> LatLngs) {
        // 1.先存入團購資料 2.依團購的ID存入發貨地址緯經度 3.存庫團購的商品明細 4.商品LockCount增加
        
        // 1
        int insertGroupId = groupDao.insert(group);
        System.out.println("insertGroupId: " + insertGroupId);
        // 2
        if (insertGroupId > 0) {
            int count = 0;
            for (Double[] LatLng : LatLngs) {
                Location location = new Location(insertGroupId, LatLng[0], LatLng[1]);
                count += locationDao.insert(location);
                System.out.println("LatLng: "  + count);
            }
        }
        // 3 4
        for (Merch merch : group.getMerchs()) {
            System.out.println("merchId: " + merch.getMerchId());
            groupListDao.insert(insertGroupId, merch.getMerchId());
            merchDao.addLockCount(merch.getMerchId(), 1);
        }
        
        return insertGroupId;
    }
    
    public int deleteById(int id, List<Integer> merchsId) {
        if (merchsId == null) {
            // 從 group_list 抓取 Merchs ID
            merchsId = groupListDao.selectMerchIdByGroupId(id);
        }
        int result;
        // 刪除團購
        result = groupDao.delete(id);
        // 刪除團購商品清單
        groupListDao.deleteByGroupId(id);
        // 刪除商品被登記的次數
        for (int merchId : merchsId) {
            merchDao.subLockCount(merchId, 1);
        }
        // 刪除地址
        locationDao.deleteByGroupId(id);
        
        return result;
    }

    public int updateGroupStatus() {
        return groupDao.updateGroupStatus();
    }

    /**
     * 新增一筆封鎖團購資料
     */
    public int insertBlockade(int id, int memberId, String name, String reason) {
        GroupBlockade groupBlockade = new GroupBlockade(0, id, memberId, name, reason, false);
        // 更新團購狀態 4
        groupDao.updateGroupStatus(id, 4);
        // 新增封鎖
        return groupBlockadeDao.insert(groupBlockade);
    }

    /**
     * 找到此賣家有被管理員封鎖的group
     */
    public List<GroupBlockade> selectBlockadeByMemberId(int memberId) {
        List<GroupBlockade> groupBlockades;
        groupBlockades = groupBlockadeDao.selectAllByMembreId(memberId);
        // 直接更新已通知欄位
        for (GroupBlockade groupBlockade : groupBlockades) {
            groupBlockadeDao.update(groupBlockade);
        }
        
        return groupBlockades;
    }
}
