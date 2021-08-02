package com.dao;

import java.util.List;

import com.bean.Merch;

public interface MerchDao {
    int insert(Merch merch, List<byte[]> images);

    int update(Merch merch, List<byte[]> images);

    int delete(int id);

    Merch selectById(int id);

    List<Merch> selectAll();
    
    List<Merch> selectAllByMemberId(int memberId);
    
    List<Merch> selectAllByMerchsId(List<Integer> merchsId);

    byte[] getImage(int id);
    
    byte[] getImage(int id, int number);
    
    List<byte[]> getImages(int id);
    
    List<Merch> selectAllByGroupId(int groupId);
    
    int addLockCount(int id, int add);
    
    int subLockCount(int id, int sub);
}
