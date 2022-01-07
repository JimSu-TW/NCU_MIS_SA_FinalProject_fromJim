package ncu.im3069.demo.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.*;

public class ReserveDatetime {

    /** reserve_datetime_id，時段編號 */
    private int reserve_datetime_id;

    /** available_datetime，可預約時段*/
    private Date available_datetime;
    
    /** available_seat_number，可預約座位數*/
    private int available_seat_number;
    
    private ReserveDatetimeHelper rdh =  ReserveDatetimeHelper.getHelper();
    /**
     * 實例化（Instantiates）一個新的（new）ReserveDatetime 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     *
     * @param reserve_datetime_id 時段編號
     * @param available_datetime 時段
     * @param available_seat_number 座位數
     */
	public ReserveDatetime(int reserve_datetime_id, Date available_datetime, int available_seat_number) {
		this.reserve_datetime_id = reserve_datetime_id;
		this.available_datetime=new Date(available_datetime.getTime()-1000*60*60*8);
		this.available_seat_number=available_seat_number;
	}
	
	public ReserveDatetime(Date available_datetime, int available_seat_number) {
		this.available_datetime=new Date(available_datetime.getTime()-1000*60*60*8);
		this.available_seat_number=available_seat_number;
	}
    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	
	public int getReserveDatetimeId() {
		return this.reserve_datetime_id;
	}
	
    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public Date getAvailableDatetime() {
		return this.available_datetime;
	}
	
    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
	public int getAvailableSeatNumber() {
		return this.available_seat_number;
	}

	public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.reserve_datetime_id != 0) {
            /** 透過MemberHelper物件，更新目前之會員資料置資料庫中 */
            data = rdh.update(this);
        }
        
        return data;
    }

    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
	public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("reserve_datetime_id", getReserveDatetimeId());
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        jso.put("available_datetime",sdFormat.format(getAvailableDatetime()));
        jso.put("available_seat_number", getAvailableSeatNumber());
        return jso;
    }
}
