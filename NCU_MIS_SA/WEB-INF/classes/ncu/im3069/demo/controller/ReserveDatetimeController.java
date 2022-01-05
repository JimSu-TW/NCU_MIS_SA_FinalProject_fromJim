package ncu.im3069.demo.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.ReserveDatetime;
import ncu.im3069.demo.app.ReserveDatetimeHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ReserveDatetimeController
 */
@WebServlet("/api/reservedatetime.do")
public class ReserveDatetimeController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private ReserveDatetimeHelper rdh =  ReserveDatetimeHelper.getHelper();

    public ReserveDatetimeController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("reserve_datetime_id");

        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回購物車內產品之資料，否則代表要取回全部資料庫內產品之資料 */
        if (!id.isEmpty()) {
          JSONObject query = rdh.getById(Integer.parseInt(id));
          resp.put("status", "200");
          resp.put("message", "所有購物車之商品資料取得成功");
          resp.put("response", query);
        }
        else {
          JSONObject query = rdh.getAll();

          resp.put("status", "200");
          resp.put("message", "所有商品資料取得成功");
          resp.put("response", query);
        }

        jsr.response(resp, response);
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("reserve_datetime_id");
        String dateStr = jso.getString("reserve_datetime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        int seat = jso.getInt("seat");
		try {
			Date datetime = sdf.parse(dateStr);
			/** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
            ReserveDatetime r = new ReserveDatetime(id,datetime,seat);
            
            /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
            JSONObject data = r.update();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 更新時間資料...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
		} catch (ParseException e) {

			e.printStackTrace();
		}

    }
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            JSONObject jso = jsr.getObject();
            
            /** 取出經解析到JSONObject之Request參數 */
            int id = jso.getInt("reserve_datetime_id");
            
            /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
            JSONObject query = rdh.deleteByID(id);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "時間移除成功！");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            JSONObject jso = jsr.getObject();
            Date now = new Date();

            long datenum = jso.getLong("reserve_datetime");
            String seat = jso.getString("seat");
			Date datetime = new Date(datenum-8*60*60*1000);
			if(datetime.before(now)) {
				String resp = "{\"status\": \'400\', \"message\": \'日期錯誤\', \'response\': \'\'}";
	            /** 透過JsonReader物件回傳到前端（以字串方式） */
	            jsr.response(resp, response);
			}
			else {
				/** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
	            ReserveDatetime rd = new ReserveDatetime(datetime,Integer.parseInt(seat));
	            
	            /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
	            if(datenum==0 || seat.isEmpty()) {
	                /** 以字串組出JSON格式之資料 */
	                String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
	                /** 透過JsonReader物件回傳到前端（以字串方式） */
	                jsr.response(resp, response);
	            }
	            /** 透過MemberHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
	            else if (!rdh.checkDuplicate(rd)) {
	                /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
	                JSONObject data = rdh.create(rd);
	                
	                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
	                JSONObject resp = new JSONObject();
	                resp.put("status", "200");
	                resp.put("message", "成功! 註冊會員資料...");
	                resp.put("response", data);
	                
	                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
	                jsr.response(resp, response);
	            }
	            else {
	                /** 以字串組出JSON格式之資料 */
	                String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail帳號重複！\', \'response\': \'\'}";
	                /** 透過JsonReader物件回傳到前端（以字串方式） */
	                jsr.response(resp, response);
	            }
			}
        }

}
