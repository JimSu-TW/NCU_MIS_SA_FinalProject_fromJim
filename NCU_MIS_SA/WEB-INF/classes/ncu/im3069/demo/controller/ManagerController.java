package ncu.im3069.demo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import ncu.im3069.demo.app.Manager;
import ncu.im3069.demo.app.ManagerHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class ManagerController
 */
@WebServlet("/api/manager.do")
public class ManagerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	/** mh，ManagerHelper之物件與Manager相關之資料庫方法（Sigleton） */
    private ManagerHelper mh =  ManagerHelper.getHelper();
    
    public ManagerController() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            JSONObject jso = jsr.getObject();
            
            /** 取出經解析到JSONObject之Request參數 */
            String password = jso.getString("password");
            int root =jso.getInt("root");
            
            /** 建立一個新的會員物件 */
            Manager m = new Manager(password, root);
            
            /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
            if(password.isEmpty()) {
                /** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
            }
            /** 透過ManagerHelper物件的checkDuplicate()檢查該會員電子郵件信箱是否有重複 */
            else if (!mh.checkDuplicate(m)) {
                /** 透過ManagerHelper物件的create()方法新建一個會員至資料庫 */
                JSONObject data = mh.create(m);
                
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

        /**
         * 處理Http Method請求GET方法（取得資料）
         *
         * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
         * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
         * @throws ServletException the servlet exception
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
            String manager_id = jsr.getParameter("manager_id");
            
            /** 判斷該字串是否存在，若存在代表要取回個別會員之資料，否則代表要取回全部資料庫內會員之資料 */
            if (manager_id.isEmpty()) {
                /** 透過ManagerHelper物件之getAll()方法取回所有會員之資料，回傳之資料為JSONObject物件 */
                JSONObject query = mh.getAll();
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "所有會員資料取得成功");
                resp.put("response", query);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
            else {
                /** 透過ManagerHelper物件的getByID()方法自資料庫取回該名會員之資料，回傳之資料為JSONObject物件 */
                JSONObject query = mh.getByID(manager_id);
                
                /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "會員資料取得成功");
                resp.put("response", query);
        
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
        }

        /**
         * 處理Http Method請求DELETE方法（刪除）
         *
         * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
         * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
         * @throws ServletException the servlet exception
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
            JsonReader jsr = new JsonReader(request);
            JSONObject jso = jsr.getObject();
            
            /** 取出經解析到JSONObject之Request參數 */
            int manager_id = jso.getInt("manager_id");
            
            /** 透過ManagerHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
            JSONObject query = mh.deleteByID(manager_id);
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "會員移除成功！");
            resp.put("response", query);

            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
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
            int manager_id = jso.getInt("manager_id");
            String password = jso.getString("password");
            int root = jso.getInt("root");

            /** 透過傳入之參數，新建一個以這些參數之會員Manager物件 */
            Manager m = new Manager(manager_id, password, root);
            
            /** 透過Manager物件的update()方法至資料庫更新該名會員資料，回傳之資料為JSONObject物件 */
            JSONObject data = m.update();
            
            /** 新建一個JSONObject用於將回傳之資料進行封裝 */
            JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 更新會員資料...");
            resp.put("response", data);
            
            /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
            jsr.response(resp, response);
        }
    }
