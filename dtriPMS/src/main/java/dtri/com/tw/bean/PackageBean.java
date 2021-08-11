package dtri.com.tw.bean;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Basil 包裝 回傳 or 傳遞 物件
 * 
 **/
public class PackageBean {
	// 常用代號
	// danger--warning--success
	public static final String info_color_success = "success";
	public static final String info_color_warning = "warning";
	public static final String info_color_danger = "danger";
	public static final String info_message_success = "[000] The command was executed [Successfully]!!";
	public static final String info_message_warning = "[100] The command was executed [Warning]!!";
	public static final String info_message_warning1_NotFindUser = "[101] Unable to get user information [Warning]!!";
	public static final String info_message_warning2_NotFind = "[102] Did not find any results [Warning]!!";
	public static final String info_message_warning3 = "[103] The command was executed [Warning]!!";
	public static final String info_message_warning4 = "[104] The command was executed [Warning]!!";

	public static final String info_message_danger = "[502] The command was executed [ERROR]!!";
	public static final String info_administrator = " Please contact the system administrator #321";

	// "resp_content" or "req_content"
	private String type_content;// 請求 or 回傳
	private Date date;// 時間
	private String action;// 請求 動作行為
	private JSONObject header;// title 名稱表
	private JSONObject body;// 資料 內 容物
	private JSONObject body_type;// 資料 內 [容物類型群組][新增需不需要:群組/只有一般] (type:group,createOnly:(all/general))
	private Integer page_total;// 每次 總頁數 ex :10
	private Integer page_batch;// 第幾批
	private Integer page_now_nb;// 第幾分頁
	private String info; // 回傳資訊
	private String info_color; // 回傳資訊 顏色
	private String call_bk_fn;// 回傳呼叫方法
	private JSONObject call_bk_vals;// 回傳呼叫傳遞值
	private String html_body;// 切換頁面
	private String html_permission;// 權限頁面限制
	private JSONObject info_user;// 使用者資訊
	private JSONArray cell_searchs;// 查詢欄位
	private JSONArray cell_modify;// 修改欄位
	private JSONArray cell_refresh;// 修改欄位更新

	private JSONArray cell_g_modify;// 修改群組欄位

	public PackageBean() {
		this.info = info_message_success;
		this.info_color = info_color_success;
		this.body_type = new JSONObject("{'type':'general','createOnly':'all'}");//
		this.date = new Date();
	}

	/**
	 * @param type 100 : 一般錯誤<br>
	 *             101 : 沒使用者資訊<br>
	 *             102 : 沒找到東西<br>
	 * 
	 **/
	public void autoMsssage(int type) {
		switch (type) {
		case 100:
			// 一般指令錯誤
			this.info = info_message_warning;
			this.info_color = info_color_warning;
		case 101:
			// 沒使用者資訊
			this.info = info_message_warning1_NotFindUser;
			this.info_color = info_color_warning;
		case 102:
			// 沒找到東西
			this.info = info_message_warning2_NotFind;
			this.info_color = info_color_warning;
			break;
		default:
			// 不明錯誤
			this.info = info_message_danger + info_administrator;
			this.info_color = info_color_danger;
			break;
		}

	}

	public String getType_content() {
		return type_content;
	}

	public void setType_content(String type_content) {
		this.type_content = type_content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getaction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JSONObject getBody() {
		return body;
	}

	public void setBody(JSONObject body) {
		this.body = body;
	}

	public Integer getPage_total() {
		return page_total;
	}

	public void setPage_total(Integer page_total) {
		this.page_total = page_total;
	}

	public Integer getPage_batch() {
		return page_batch;
	}

	public void setPage_batch(Integer page_batch) {
		this.page_batch = page_batch;
	}

	public Integer getPage_now_nb() {
		return page_now_nb;
	}

	public void setPage_now_nb(Integer page_now_nb) {
		this.page_now_nb = page_now_nb;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public JSONObject getHeader() {
		return header;
	}

	public void setHeader(JSONObject header) {
		this.header = header;
	}

	public String getInfo_color() {
		return info_color;
	}

	public void setInfo_color(String info_color) {
		this.info_color = info_color;
	}

	public String getHtml_body() {
		return html_body;
	}

	public void setHtml_body(String html_body) {
		this.html_body = html_body;
	}

	public JSONObject getInfo_user() {
		return info_user;
	}

	public void setInfo_user(JSONObject info_user) {
		this.info_user = info_user;
	}

	public JSONArray getCell_searchs() {
		return cell_searchs;
	}

	public void setCell_searchs(JSONArray cell_searchs) {
		this.cell_searchs = cell_searchs;
	}

	public JSONArray getCell_modify() {
		return cell_modify;
	}

	public void setCell_modify(JSONArray cell_modify) {
		this.cell_modify = cell_modify;
	}

	public String getCall_bk_fn() {
		return call_bk_fn;
	}

	public void setCall_bk_fn(String call_bk_fn) {
		this.call_bk_fn = call_bk_fn;
	}

	public JSONObject getCall_bk_vals() {
		return call_bk_vals;
	}

	public void setCall_bk_vals(JSONObject call_bk_vals) {
		this.call_bk_vals = call_bk_vals;
	}

	public JSONObject getBody_type() {
		return body_type;
	}

	public void setBody_type(JSONObject body_type) {
		this.body_type = body_type;
	}

	public String getHtml_permission() {
		return html_permission;
	}

	public void setHtml_permission(String html_permission) {
		this.html_permission = html_permission;
	}

	public JSONArray getCell_g_modify() {
		return cell_g_modify;
	}

	public void setCell_g_modify(JSONArray cell_g_modify) {
		this.cell_g_modify = cell_g_modify;
	}

	public JSONArray getCell_refresh() {
		return cell_refresh;
	}

	public void setCell_refresh(JSONArray cell_refresh) {
		this.cell_refresh = cell_refresh;
	}

}
