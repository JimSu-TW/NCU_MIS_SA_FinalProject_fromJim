package ncu.im3069.demo.controller;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.Reserve;
import ncu.im3069.demo.app.ReserveHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ReserveController
 */
@WebServlet("/api/reserve.do")
public class ReserveController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
    /** rh，ReserveHelper 之物件與 Reserve 相關之資料庫方法（Sigleton） */
    private ReserveHelper rh =  ReserveHelper.getHelper();


    public ReserveController() {
        super();
    }

    /**
     * 處理 Http Method 請求 GET 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);

        /** 取出經解析到 JsonReader 之 Request 參數 */
        String user_id = jsr.getParameter("user_id");
        String reserve_id = jsr.getParameter("reserve_id");

        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();

        /** 判斷該字串是否存在，若存在代表要取回個別訂單之資料，否則代表要取回全部資料庫內訂單之資料 */
        if (!user_id.isEmpty()) {
          /** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = rh.getByUserId(user_id);
          resp.put("status", "200");
          resp.put("message", "單筆訂單資料取得成功");
          resp.put("response", query);
        }
        else if(!reserve_id.isEmpty()){
        	/** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆訂單之資料，回傳之資料為 JSONObject 物件 */
            JSONObject query = rh.getByReserveId(reserve_id);
            resp.put("status", "200");
            resp.put("message", "單筆訂單資料取得成功");
            resp.put("response", query);
        }
        else {
          /** 透過 orderHelper 物件之 getAll() 方法取回所有訂單之資料，回傳之資料為 JSONObject 物件 */
          JSONObject query = rh.getAll();
          resp.put("status", "200");
          resp.put("message", "所有訂單資料取得成功");
          resp.put("response", query);
        }

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}

    /**
     * 處理 Http Method 請求 POST 方法（新增資料）
     *
     * @param request Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
     * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    /** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        /** 取出經解析到 JSONObject 之 Request 參數 */
        int user_id = jso.getInt("user_id");
        String name = jso.getString("name");
        String phone_number = jso.getString("phone_number");
        int reserve_datetime_id = jso.getInt("reserve_datetime_id");
        int number_of_people = jso.getInt("number_of_people");
        String note = jso.getString("note");

        
        /** 建立一個新的訂單物件 */
		if(reserve_datetime_id!=0) {
	        Reserve rd = new Reserve(user_id, name, phone_number, reserve_datetime_id, number_of_people, note);
	        rd.setFlag();
	        /** 透過 orderHelper 物件的 create() 方法新建一筆訂單至資料庫 */
	        JSONObject result = rh.create(rd);

	        /** 設定回傳回來的訂單編號與訂單細項編號 */
	        rd.setReserveID((int) result.getLong("reserve_id"));

	        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
	        JSONObject resp = new JSONObject();
	        resp.put("status", "200");
	        resp.put("message", "訂單新增成功！");
	        resp.put("response", rd.getReserveData());

	        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
	        jsr.response(resp, response);
		}
	}
	
	/**
     * 處理Http Method請求PUT方法（更新）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int reserve_id = jso.getInt("reserve_id");
        int user_id = jso.getInt("user_id");
        String name = jso.getString("name");
        String phone_number = jso.getString("phone_number");
        int reserve_datetime_id = jso.getInt("reserve_datetime_id");
        int number_of_people = jso.getInt("number_of_people");
        String note = jso.getString("note");
        int rate = jso.getInt("rate");
        int checkin =jso.getInt("checkin");

        /** 透過傳入之參數，新建一個以這些參數之會員Member物件 */
        Reserve rd = new Reserve(reserve_id,user_id, name, phone_number, reserve_datetime_id, number_of_people, note);
        if(rate>0) {
        	rd.setRate(rate);
        }
        rd.setCheckIn(checkin);
        /** 透過Member物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
        JSONObject data = rd.update();
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新預約資料...");
        resp.put("response", data);
        
        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
