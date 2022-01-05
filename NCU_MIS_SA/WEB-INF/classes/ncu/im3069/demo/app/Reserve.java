package ncu.im3069.demo.app;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.*;

public class Reserve {
	
    /** reserve_id，預約編號 */
    private int reserve_id;

    /** user_id，會員ID */
    private int user_id;
    
    /** name，會員姓名 */
    private String name;

    /** phone，會員手機 */
    private String phone_number;
    
    private String email;
    
    /** check_in，報到 */
    private int check_in;

    /** rate，評分*/
    private int rate;
    
    /** rdt，預約時段 */
    private int reserve_datetime_id;
    
    private Date reserve_datetime;

    /** number_of_people，預約人數 */
    private int number_of_people;
    
    /** note，備註 */
    private String note;
    
    private int flag=0;
    
    private ReserveHelper rh = ReserveHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立訂單資料時，產生一個新的訂單
     * 
     * @param name 會員姓名
     * @param phone_number 會員電話
     * @param rdt 預約時段
     * @param number_of_people 預約人數
     * @param note 備註
     */
    public Reserve(int user_id, String name, String phone_number, int reserve_datetime_id, int number_of_people, String note) {
        this.user_id=user_id;
        this.name = name;
        setPhoneNumber(phone_number);
        this.reserve_datetime_id = reserve_datetime_id;
        setNumberOfPeople(number_of_people);
        setNote(note);
        setRate(0);
    }

    /**
     * 實例化（Instantiates）一個新的（new）Order 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單資料時，新改資料庫已存在的訂單
     *
     * @param name 會員姓名
     * @param phone_number 會員電話
     * @param rdt 預約時段
     * @param number_of_people 預約人數
     * @param note 備註
     */
    public Reserve(int reserve_id, int user_id, String name, String phone_number, int reserve_datetime_id, int number_of_people, String note) {
        setReserveID(reserve_id);
        this.user_id=user_id;
        this.name = name;
        setPhoneNumber(phone_number);
        this.reserve_datetime_id = reserve_datetime_id;
        setNumberOfPeople(number_of_people);
        setNote(note);
        setRate(0);
    }

    /**
     * 設定預約編號
     * 
     */
    public void setReserveID(int reserve_id) {
        this.reserve_id = reserve_id;
    }

    /**
     * 取得會員編號
     *
     * @return int 回傳會員編號
     */
    public int getUserID() {
        return this.user_id;
    }


    /**
     * 取得預約編號
     *
     * @return int 回傳預約編號
     */
    public int getReserveID() {
        return this.reserve_id;
    }

    /**
     * 取得預約會員的名
     *
     * @return String 回傳預約會員的姓名
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 設定預約對應之顧客電話
     * 
     */
    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }
    
    /**
     * 取得預約對應之顧客電話
     *
     * @return String 回傳預約對應之電話
     */
    public String getPhoneNumber() {
        return this.phone_number;
    }
    
    /**
     * 設定預約人數
     * 
     */
    public void setNumberOfPeople(int number_of_people) {
        this.number_of_people = number_of_people;
    }

    /**
     * 取得預約時段
     *
     * @return ReserveDatetime 回傳預約時段
     */
    public int getReserveDatetimeID() {
        return reserve_datetime_id;
    }
        
    /**
     * 取得預約人數
     *
     * @return int 回傳預約人數
     */
    public int getNumberOfPeople() {
        return this.number_of_people;
    }
    
    /**
     * 設定預約評比
     * 
     */
    public void setNote(String note) {
        this.note = note;
    }
    
    /**
     * 取得預約備註
     *
     * @return String 回傳預約備註
     */
    public String getNote() {
        return this.note;
    }
    
    /**
     * 設定訂單之報到
     * 
     */
    public void setCheckIn(int check_in) {
        this.check_in = check_in;
    }
    
    /**
     * 取得預約是否報到
     *
     * @return boolean 回傳預約是否報到
     */
    public int getCheckIn() {
        return this.check_in;
    }
    
    /**
     * 設定預約評比
     * 
     */
    public void setRate(int rate) {
        this.rate = rate;
    }
    
    /**
     * 取得預約評比
     *
     * @return int 回傳預約評比
     */
    public int getRate() {
        return this.rate;
    }
    
    public void setDatetime(Date reserve_datetime) {
    	this.reserve_datetime=reserve_datetime;
    }
    
    public Date getDatetime() {
    	return reserve_datetime;
    }
    
    public void setEmail(String email) {
    	this.email=email;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setFlag() {
    	flag=1;
    }
    
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.reserve_id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = rh.update(this);
        }
        
        return data;
    }

    /**
     * 取得預約基本資料
     *
     * @return JSONObject 取得訂單所有資料
     */
    public JSONObject getReserveData() {
        JSONObject jso = new JSONObject();
        jso.put("reserve_id", getReserveID());
        jso.put("user_id", getUserID());
        jso.put("name", getName());
        jso.put("phone_number", getPhoneNumber());
        jso.put("reserve_datetime_id", getReserveDatetimeID());
        if(flag==0) {
        	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            jso.put("reserve_datetime", sdFormat.format(getDatetime()));
        }
        jso.put("number_of_people", getNumberOfPeople());
        jso.put("note", getNote());
        jso.put("email",getEmail());
        jso.put("checkin",getCheckIn());
        jso.put("rate",getRate());

        return jso;
    }
}
