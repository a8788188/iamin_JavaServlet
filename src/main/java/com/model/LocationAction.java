package com.model;

import java.util.List;

import com.bean.Location;
import com.dao.LocationDao;
import com.dao.implemen.LocationDaoImp;

public class LocationAction {
    private LocationDao locationDao = new LocationDaoImp();
    
    public List<Location> getAllByGroupId(int GroupId) {
        return locationDao.selectAllByGroupId(GroupId);
    }
    
    public int update(Location location) {
        return locationDao.update(location);
    }
}
