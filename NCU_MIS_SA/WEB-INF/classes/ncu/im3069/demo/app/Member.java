package ncu.im3069.demo.app;

import org.json.*;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * The Class Member
 * Member類別（class）具有會員所需要之屬性與方法，並且儲存與會員相關之商業判斷邏輯<br>
 * </p>
 * 
 * @author IPLab
 * @version 1.0.0
 * @since 1.0.0
 */

public class Member {
    
    /** user_id，會員編號 */
    private int user_id;
    
    /** email，會員電子郵件信箱 */
    private String email;
    
    /** name，會員姓名 */
    private String name;
    
    /** password，會員密碼 */
    private String password;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private MemberHelper mh =  MemberHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立會員資料時，產生一名新的會員
     *
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     */
    public Member(String email, String password, String name) {
    	setEmail(email);
    	setPassword(password);
    	setName(name);
    }

    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新會員資料時，產生一名會員同時需要去資料庫檢索原有更新時間分鐘數與會員組別
     * 
     * @param user_id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param name 會員姓名
     */
    public Member(int user_id, String email, String password, String name) {
        setUserID(user_id);
        setEmail(email);
        setPassword(password);
        setName(name);
    }

    
    /**
     * 設定會員之編號
     * 
     * @param user_id
     */
    public void setUserID(int user_id) {
    	this.user_id = user_id;
    }
    
    /**
     * 取得會員之編號
     *
     * @return the user_id 回傳會員編號
     */
    public int getUserID() {
        return this.user_id;
    }

    /**
     * 設定會員之信箱
     * 
     * @param email
     */
    public void setEmail(String email) {
    	this.email = email;
    }
    
    /**
     * 取得會員之電子郵件信箱
     *
     * @return the email 回傳會員電子郵件信箱
     */
    public String getEmail() {
        return this.email;
    }
    
    /**
     * 設定會員之姓名
     * 
     * @param name
     */
    public void setName(String name) {
    	this.name = name;
    }
    
    /**
     * 取得會員之姓名
     *
     * @return the name 回傳會員姓名
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 設定會員之密碼
     * 
     * @param password
     */
    public void setPassword(String password) {
    	this.password = password;
    }

    /**
     * 取得會員之密碼
     *
     * @return the password 回傳會員密碼
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.user_id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = mh.update(this);
        }
        
        return data;
    }
    
    /**
     * 取得該名會員所有資料
     *
     * @return the data 取得該名會員之所有資料並封裝於JSONObject物件內
     */
    public JSONObject getData() {
        /** 透過JSONObject將該名會員所需之資料全部進行封裝*/ 
        JSONObject jso = new JSONObject();
        jso.put("user_id", getUserID());
        jso.put("name", getName());
        jso.put("email", getEmail());
        jso.put("password", getPassword());
        
        return jso;
    }
    
}