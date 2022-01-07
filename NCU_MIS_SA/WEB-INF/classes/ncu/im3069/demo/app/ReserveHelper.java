package ncu.im3069.demo.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.json.*;

import ncu.im3069.demo.util.DBMgr;

public class ReserveHelper {
    
    private static ReserveHelper rh;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    private ReserveHelper() {
    }
    
    public static ReserveHelper getHelper() {
        if(rh == null) rh = new ReserveHelper();
        
        return rh;
    }
    
    public JSONObject create(Reserve reserve) {
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        long id = -1;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "INSERT INTO `missa`.`tb_reserve`(`user_id`, `name`, `phone_Number`, `number_of_People`, `reserve_datetime_id`, `note`)"
                    + " VALUES(?, ?, ?, ?, ?, ?)";
            
            /** 取得所需之參數 */
            int user_id = reserve.getUserID();
            String name = reserve.getName();
            String phone_number = reserve.getPhoneNumber();
            int reserve_datetime_id = reserve.getReserveDatetimeID();
            int number_of_people = reserve.getNumberOfPeople();
            String note = reserve.getNote();
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pres.setInt(1, user_id);
            pres.setString(2, name);
            pres.setString(3, phone_number);
            pres.setInt(4, number_of_people);
            pres.setInt(5, reserve_datetime_id);
            pres.setString(6, note);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            pres.executeUpdate();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }

        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("reserve_id", id);

        return response;
    }
    
    public JSONObject getAll() {
        Reserve r = null;
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM (`missa`.`tb_reserve` INNER JOIN `missa`.`tb_reservedatetime` ON `tb_reserve`.`reserve_datetime_id`=`tb_reservedatetime`.`reserve_datetime_id`) INNER JOIN `missa`.`tb_member` ON `tb_reserve`.`user_id`=`tb_member`.`user_id` WHERE `tb_reserve`.`number_of_People`>0  ORDER BY `tb_reservedatetime`.`available_datetime`";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int reserve_id = rs.getInt("reserve_id");
                int user_id = rs.getInt("user_id");
                String name = rs.getString("name");
                String phone_number = rs.getString("phone_number");
                int reserve_datetime_id = rs.getInt("reserve_datetime_id");
                int number_of_people = rs.getInt("number_of_people");
                String note = rs.getString("note");

                
                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Reserve(reserve_id, user_id, name, phone_number, reserve_datetime_id, number_of_people, note);
                r.setDatetime(new Date(rs.getTimestamp("available_datetime").getTime()-1000*60*60*8));
                r.setEmail(rs.getString("email"));
                r.setCheckIn(rs.getInt("checkin"));
                
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(r.getReserveData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    
    public JSONObject getByUserId(String id) {
        JSONArray jsa = new JSONArray();
        Reserve r = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM (`missa`.`tb_reserve` INNER JOIN `missa`.`tb_reservedatetime` ON `tb_reserve`.`reserve_datetime_id`=`tb_reservedatetime`.`reserve_datetime_id`) INNER JOIN `missa`.`tb_member` ON `tb_reserve`.`user_id`=`tb_member`.`user_id` WHERE `tb_reserve`.`user_id` = ? AND `tb_reserve`.`number_of_People`>0";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int reserve_id = rs.getInt("reserve_id");
                int user_id = rs.getInt("user_id");
                String name = rs.getString("name");
                String phone_number = rs.getString("phone_Number");
                int reserve_datetime_id = rs.getInt("reserve_datetime_id");
                int number_of_people = rs.getInt("number_of_People");
                String note = rs.getString("note");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Reserve(reserve_id, user_id, name, phone_number, reserve_datetime_id, number_of_people, note);
                r.setDatetime(new Date(rs.getTimestamp("available_datetime").getTime()-1000*60*60*8));
                r.setEmail(rs.getString("email"));
                r.setCheckIn(rs.getInt("checkin"));
                
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                jsa.put(r.getReserveData());
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
    public JSONObject getByReserveId(String id) {
        JSONObject data = new JSONObject();
        Reserve r = null;
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        /** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
        ResultSet rs = null;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM (`missa`.`tb_reserve` INNER JOIN `missa`.`tb_reservedatetime` ON `tb_reserve`.`reserve_datetime_id`=`tb_reservedatetime`.`reserve_datetime_id`) INNER JOIN `missa`.`tb_member` ON `tb_reserve`.`user_id`=`tb_member`.`user_id` WHERE `tb_reserve`.`reserve_id` = ?";
            
            /** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
            pres = conn.prepareStatement(sql);
            pres.setString(1, id);
            /** 執行查詢之SQL指令並記錄其回傳之資料 */
            rs = pres.executeQuery();

            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            /** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                row += 1;
                
                /** 將 ResultSet 之資料取出 */
                int reserve_id = rs.getInt("reserve_id");
                int user_id = rs.getInt("user_id");
                String name = rs.getString("name");
                String phone_number = rs.getString("phone_Number");
                int reserve_datetime_id = rs.getInt("reserve_datetime_id");
                int number_of_people = rs.getInt("number_of_People");
                String note = rs.getString("note");
                
                /** 將每一筆商品資料產生一名新Product物件 */
                r = new Reserve(reserve_id, user_id, name, phone_number, reserve_datetime_id, number_of_people, note);
                r.setDatetime(new Date(rs.getTimestamp("available_datetime").getTime()-1000*60*60*8));
                r.setEmail(rs.getString("email"));
                r.setRate(rs.getInt("rate"));
                r.setCheckIn(rs.getInt("checkin"));
                
                /** 取出該項商品之資料並封裝至 JSONsonArray 內 */
                data = r.getReserveData();
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(rs, pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
        
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", data);

        return response;
    }
    
    public JSONObject update(Reserve r) {
        /** 紀錄回傳之資料 */
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        /** 紀錄程式開始執行時間 */
        long start_time = System.nanoTime();
        /** 紀錄SQL總行數 */
        int row = 0;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql1 = "Update `missa`.`tb_reserve` SET `phone_Number` = ? ,`note` = ?, `modified_time`=? WHERE `reserve_id` = ?";
            String sql2 = "Update `missa`.`tb_reserve` SET `user_id`=?, `phone_Number` = ? ,`note` = ?, `modified_time`=?, `number_of_People`=? WHERE `reserve_id` = ?";
            String sql3 = "Update `missa`.`tb_reserve` SET `rate` = ? ,`modified_time`=? WHERE `reserve_id` = ?";
            String sql4 = "Update `missa`.`tb_reserve` SET `checkin` = ? ,`modified_time`=? WHERE `reserve_id` = ?";
            /** 取得所需之參數 */
            int user_id = r.getUserID();
            String phone = r.getPhoneNumber();
            String note = r.getNote();
            int num_of_people = r.getNumberOfPeople();
            Date now = new Date();
            Timestamp nowtime = new Timestamp(now.getTime()+8*60*60*1000);
            int rate = r.getRate();
            int checkin = r.getCheckIn();
            if(num_of_people==0) {
            	/** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql2);
	            pres.setInt(1, user_id);
	            pres.setString(2, phone);
	            pres.setString(3, note);
	            pres.setTimestamp(4, nowtime);
	            pres.setInt(5, num_of_people);
	            pres.setInt(6, r.getReserveID());
	            /** 執行更新之SQL指令並記錄影響之行數 */
	            row = pres.executeUpdate();
	
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
            }
            else if(rate>0){
            	/** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql3);
	            pres.setInt(1, rate);
	            pres.setTimestamp(2, nowtime);
	            pres.setInt(3, r.getReserveID());
	            /** 執行更新之SQL指令並記錄影響之行數 */
	            row = pres.executeUpdate();
	
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
            }
            else if(checkin==1) {
            	/** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql4);
	            pres.setInt(1, checkin);
	            pres.setTimestamp(2, nowtime);
	            pres.setInt(3, r.getReserveID());
	            /** 執行更新之SQL指令並記錄影響之行數 */
	            row = pres.executeUpdate();
	
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
            }
            else {
	            /** 將參數回填至SQL指令當中 */
	            pres = conn.prepareStatement(sql1);
	            pres.setString(1, phone);
	            pres.setString(2, note);
	            pres.setTimestamp(3, nowtime);
	            pres.setInt(4, r.getReserveID());
	            /** 執行更新之SQL指令並記錄影響之行數 */
	            row = pres.executeUpdate();
	
	            /** 紀錄真實執行的SQL指令，並印出 **/
	            exexcute_sql = pres.toString();
	            System.out.println(exexcute_sql);
            }

        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        /** 紀錄程式結束執行時間 */
        long end_time = System.nanoTime();
        /** 紀錄程式執行時間 */
        long duration = (end_time - start_time);
        
        /** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
        JSONObject response = new JSONObject();
        response.put("sql", exexcute_sql);
        response.put("row", row);
        response.put("time", duration);
        response.put("data", jsa);

        return response;
    }
}
