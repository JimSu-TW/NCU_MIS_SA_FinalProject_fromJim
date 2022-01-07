package ncu.im3069.demo.app;

import org.json.JSONObject;

public class Manager {

	/** manager_id，管理員編號 */
    private int manager_id;
    
    /** password，管理員密碼 */
    private String password;
    
    /** root ，管理員權限 */
    private int root;
    
    /** mh，MemberHelper之物件與Member相關之資料庫方法（Sigleton） */
    private ManagerHelper mh =  ManagerHelper.getHelper();
    
    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於建立會員資料時，產生一名新的會員
     *
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param root 會員姓名
     */
    public Manager(String password, int root) {
    	setPassword(password);
    	setRoot(root);
    }

    /**
     * 實例化（Instantiates）一個新的（new）Member物件<br>
     * 採用多載（overload）方法進行，此建構子用於更新會員資料時，產生一名會員同時需要去資料庫檢索原有更新時間分鐘數與會員組別
     * 
     * @param manager_id 會員編號
     * @param email 會員電子信箱
     * @param password 會員密碼
     * @param root 會員姓名
     */
    public Manager(int manager_id, String password, int root) {
        setManagerID(manager_id);
        setPassword(password);
        setRoot(root);
    }
    
    /**
     * 設定會員之編號
     * 
     * @param manager_id
     */
    public void setManagerID(int manager_id) {
    	this.manager_id = manager_id;
    }
    
    /**
     * 取得會員之編號
     *
     * @return the manager_id 回傳會員編號
     */
    public int getManagerID() {
        return this.manager_id;
    }
    
    /**
     * 設定會員之姓名
     * 
     * @param root
     */
    public void setRoot(int root) {
    	this.root = root;
    }
    
    /**
     * 取得會員之姓名
     *
     * @return the root 回傳會員姓名
     */
    public int getRoot() {
        return this.root;
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
        if(this.manager_id != 0) {
            /** 透過ManagerHelper物件，更新目前之會員資料置資料庫中 */
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
        jso.put("manager_id", getManagerID());
        jso.put("password", getPassword());
        jso.put("root", getRoot());
        
        return jso;
    }
    
}
