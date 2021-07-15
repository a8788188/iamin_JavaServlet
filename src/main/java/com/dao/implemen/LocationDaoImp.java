package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.Group;
import com.bean.Location;
import com.dao.LocationDao;
import com.dao.common.ServiceLocator;

public class LocationDaoImp implements LocationDao {
    DataSource dataSource;

    public LocationDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(Location location) {
        int count = 0;
        String sql = "INSERT INTO location" + 
                "(GROUP_ID, ADDRESS, LATITUDE, LONGTITUDE, PICKUP_TIME) " + 
                "VALUES(?, ?, ?, ?, ?);";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, location.getGroupId());
            ps.setString(2, location.getAddress());
            ps.setDouble(3, location.getLatitude());
            ps.setDouble(4, location.getLongtitude());
            ps.setTimestamp(5, location.getPickup_time());
            
            count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int delete(int id) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int update(Location location) {
        int count = 0;
        String sql = "UPDATE plus_one.location SET LATITUDE = ?, LONGTITUDE = ?, PICKUP_TIME = ?, UPDATE_TIME = NOW() " 
                    +"WHERE LOCATION_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setDouble(1, location.getLatitude());
            ps.setDouble(2, location.getLongtitude());
            ps.setTimestamp(3, location.getPickup_time());
            ps.setInt(4, location.getLocationId());
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public List<Location> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int deleteByGroupId(int groupId) {
        int count = 0;
        String sql = "UPDATE plus_one.location SET DELETE_TIME = NOW() WHERE GROUP_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupId);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public List<Location> selectAllByGroupId(int GroupId) {
        String sql = "SELECT LOCATION_ID, GROUP_ID, ADDRESS, LATITUDE, LONGTITUDE, PICKUP_TIME " 
                + "FROM plus_one.location "
                + "WHERE GROUP_ID = ? "
                + "ORDER BY START_TIME DESC;";
        
        List<Location> locations = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, GroupId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
               Location location = new Location(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getDouble(5),
                        rs.getTimestamp(6)
                        );
                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

}
