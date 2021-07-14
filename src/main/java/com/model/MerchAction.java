package com.model;

import java.util.List;

import com.bean.Merch;
import com.dao.MerchDao;
import com.dao.implemen.MerchDaoImp;

public class MerchAction {
    private MerchDao merchDao = new MerchDaoImp();
    
    public List<Merch> getAll() {
        return merchDao.selectAll();
    }
    
    public List<Merch> getAllByMemberId(int memberId) {
        return merchDao.selectAllByMemberId(memberId);
    }
    
    public int deleteById(int id) {
        return merchDao.delete(id);
    }
    
    public int add(Merch merch, List<byte[]> images) {
        return merchDao.insert(merch, images);
    }

    public byte[] getMerchImgById(int id) {
        return merchDao.getImage(id);
    }
    
    public List<byte[]> getMerchImgsById(int id) {
        return merchDao.getImages(id);
    }
    
    public int update(Merch merch, List<byte[]> images) {
        return merchDao.update(merch, images);
    }
}
