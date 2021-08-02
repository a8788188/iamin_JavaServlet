package com.dao.implemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bean.MemberOrderDetails;
import com.bean.Merch;
import com.dao.MerchDao;
import com.dao.common.ServiceLocator;


public class MerchDaoImp implements MerchDao{
    DataSource dataSource;

    public MerchDaoImp() {
        dataSource = ServiceLocator.getInstance().getDataSource();
    }

    @Override
    public int insert(Merch merch, List<byte[]> images) {
        int count = 0;
        int imageNum = images.size();
        System.out.println("imageNum = " + imageNum);
        if (merch.getMerchDesc() == "") {
            merch.setMerchDesc("NULL");
        }
        String sql = "";
        switch (imageNum) {
        case 0:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC) " + 
                    "VALUES(?, ?, ?, ?);";
            break;
        case 1:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC, IMG_1) " + 
                    "VALUES(?, ?, ?, ?, ?);";
            break;
        case 2:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC, IMG_1, IMG_2) " + 
                    "VALUES(?, ?, ?, ?, ?, ?);";
            break;
        case 3:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC, IMG_1, IMG_2, IMG_3) " + 
                    "VALUES(?, ?, ?, ?, ?, ?, ?);";
            break;
        case 4:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC, IMG_1, IMG_2, IMG_3, IMG_4) " + 
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
            break;
        case 5:
            sql = "INSERT INTO merch" + 
                    "(MEMBER_ID, NAME, PRICE, MERCH_DESC, IMG_1, IMG_2, IMG_3, IMG_4, IMG_5) " + 
                    "VALUES(?, ?, ?, ?, ?, ?, ? , ?, ?);";
            break;

        default:
            return -1;
        }
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, merch.getMemberId());
            ps.setString(2, merch.getName());
            ps.setInt(3, merch.getPrice());
            ps.setString(4, merch.getMerchDesc());
            switch (imageNum) {
            case 0:
                break;
            case 1:
                ps.setBytes(5, images.get(0));
                break;
            case 2:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                break;
            case 3:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                break;
            case 4:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                ps.setBytes(8, images.get(3));
                break;
            case 5:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                ps.setBytes(8, images.get(3));
                ps.setBytes(9, images.get(4));
                break;

            default:
                return -1;
            }
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public int update(Merch merch, List<byte[]> images) {
        int count = 0;
        int imageNum = images.size();
        System.out.println("imageNum = " + imageNum);
        if (merch.getMerchDesc() == "") {
            merch.setMerchDesc("NULL");
        }
        String sql = "";
        switch (imageNum) {
        case 0:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "WHERE MERCH_ID = ?;";
            break;
        case 1:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "IMG_1 = ? WHERE MERCH_ID = ?;";
            break;
        case 2:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "IMG_1 = ?, IMG_2 = ? WHERE MERCH_ID = ?;";
            break;
        case 3:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "IMG_1 = ?, IMG_2 = ?, IMG_3 = ? WHERE MERCH_ID = ?;";
            break;
        case 4:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "IMG_1 = ?, IMG_2 = ?, IMG_3 = ?, IMG_4 = ? WHERE MERCH_ID = ?;";
            break;
        case 5:
            sql = "UPDATE merch SET NAME = ?, PRICE = ?, MERCH_DESC = ?, UPDATE_TIME = ?, "
                    + "IMG_1 = ?, IMG_2 = ?, IMG_3 = ?, IMG_4 = ?, IMG_5 = ? WHERE MERCH_ID = ?;";
            break;

        default:
            return -1;
        }
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            //ps.setInt(1, merch.getMemberId());
            ps.setString(1, merch.getName());
            ps.setInt(2, merch.getPrice());
            ps.setString(3, merch.getMerchDesc());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            switch (imageNum) {
            case 0:
                ps.setInt(5, merch.getMerchId());
                break;
            case 1:
                ps.setBytes(5, images.get(0));
                ps.setInt(6, merch.getMerchId());
                break;
            case 2:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setInt(7, merch.getMerchId());
                break;
            case 3:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                ps.setInt(8, merch.getMerchId());
                break;
            case 4:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                ps.setBytes(8, images.get(3));
                ps.setInt(9, merch.getMerchId());
                break;
            case 5:
                ps.setBytes(5, images.get(0));
                ps.setBytes(6, images.get(1));
                ps.setBytes(7, images.get(2));
                ps.setBytes(8, images.get(3));
                ps.setBytes(9, images.get(4));
                ps.setInt(10, merch.getMerchId());
                break;

            default:
                return -1;
            }
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public int delete(int id) {
        int count = 0;
        String sql = "UPDATE plus_one.merch SET DELETE_TIME = NOW() WHERE MERCH_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public Merch selectById(int id) {
    	Merch merch = null;
    	String sql = "SELECT * FROM merch WHERE merch_id = ?";
    	try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
            	merch = new Merch(rs.getInt(1),
            			rs.getInt(2),
            			rs.getString(3),
            			rs.getInt(4),
            			rs.getString(5),
            			rs.getInt("LOCK_COUNT"));
            }
            return merch;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Merch> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<Merch> selectAllByMemberId(int memberId) {
        String sql = "SELECT MERCH_ID, NAME, PRICE, MERCH_DESC, LOCK_COUNT " 
                + "FROM merch WHERE MEMBER_ID = ? AND DELETE_TIME IS null "
                + "ORDER BY START_TIME DESC;";
        
        List<Merch> merchList = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int price = rs.getInt(3);
                String merchDesc = rs.getString(4);
                int lockCount = rs.getInt(5);
                Merch merch = new Merch(id, memberId, name, price, merchDesc, lockCount);
                merchList.add(merch);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return merchList;
    }
    
    @Override
    public List<Merch> selectAllByMerchsId(List<Integer> merchsId) {
        System.out.println("merchsId: " + merchsId);
        
        String sql = "SELECT MERCH_ID, MEMBER_ID, NAME, PRICE, MERCH_DESC, LOCK_COUNT " 
                + "FROM plus_one.merch WHERE MERCH_ID IN (?) "
                + "ORDER BY START_TIME DESC;";
        
        String in = "";
        for (int i = 0; i < merchsId.size(); i++) {
            in = in + merchsId.get(i);
            if(i < merchsId.size() - 1) {
                in = in + ", ";
            }
        }
        sql = sql.replace("(?)", "(" + in +")");
        System.out.println(sql);
        
        List<Merch> merchs = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Merch merch = new Merch(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6)
                        );
                
                merchs.add(merch);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return merchs;
    }
    
    @Override
    public byte[] getImage(int id) {
        String sql = "SELECT IMG_1 " 
                + "FROM merch WHERE MERCH_ID = ?;";
        
        byte[] image = null;
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                if (rs.getBytes(1) != null) {
                    image = rs.getBytes(1);
                }
            }
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        return image;
    }
    
    @Override
    public byte[] getImage(int id, int number) {
        String sql = "SELECT IMG_? " 
                + "FROM merch WHERE MERCH_ID = ?;";
        
        byte[] image = null;
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, number);
            ps.setInt(2, id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                if (rs.getBytes(1) != null) {
                    image = rs.getBytes(1);
                }
            }
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
        
        return image;
    }

    @Override
    public List<byte[]> getImages(int id) {
        String sql = "SELECT IMG_1, IMG_2, IMG_3, IMG_4, IMG_5 " 
                + "FROM merch WHERE MERCH_ID = ?;";
        
        List<byte[]> images = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                if (rs.getBytes(1) != null) {
                    images.add(rs.getBytes(1));
                }
                if (rs.getBytes(2) != null) {
                    images.add(rs.getBytes(2));
                }
                if (rs.getBytes(3) != null) {
                    images.add(rs.getBytes(3));
                }
                if (rs.getBytes(4) != null) {
                    images.add(rs.getBytes(4));
                }
                if (rs.getBytes(5) != null) {
                    images.add(rs.getBytes(5));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return images;
    }

    @Override
    public List<Merch> selectAllByGroupId(int groupId) {
        String sql =  "SELECT m.MERCH_ID, m.MEMBER_ID, NAME, PRICE, MERCH_DESC, LOCK_COUNT "
                    + "FROM merch m JOIN group_list g on m.MERCH_ID = g.MERCH_ID "
                    + "WHERE g.GROUP_ID = ?;";
        List<Merch> merchs = new ArrayList<>();
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt(1);
                int memberId = rs.getInt(2);
                String name = rs.getString(3);
                int price = rs.getInt(4);
                String merchDesc = rs.getString(5);
                int lockCount = rs.getInt(6);
                Merch merch = new Merch(id, memberId, name, price, merchDesc, lockCount);
                merchs.add(merch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return merchs;
    }

    @Override
    public int addLockCount(int id, int add) {
        int count = 0;
        String sql = "UPDATE plus_one.merch SET LOCK_COUNT = LOCK_COUNT + ? WHERE MERCH_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, add);
            ps.setInt(2, id);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }

    @Override
    public int subLockCount(int id, int sub) {
        int count = 0;
        String sql = "UPDATE plus_one.merch SET LOCK_COUNT = LOCK_COUNT - ? WHERE MERCH_ID = ?;";
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setInt(1, sub);
            ps.setInt(2, id);
            count = ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return count;
    }
}
