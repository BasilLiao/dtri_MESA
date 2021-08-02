package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemConfig;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemConfigDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemConfigService {
	@Autowired
	private SystemConfigDao configDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemConfig> systemConfigs = new ArrayList<SystemConfig>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("scgid").descending());
		String sc_name = null;
		String sc_g_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sc_id", FFS.h_t("ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sc_g_id", FFS.h_t("群組ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sc_g_name", FFS.h_t("群組名稱", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sc_name", FFS.h_t("名稱", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sc_value", FFS.h_t("參數", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sc_id", "ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sc_g_id", "群組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "sc_g_name", "群組名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "sc_name", "名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-3", true, n_val, "sc_value", "參數"));

			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, n_val, "sys_c_date", "建立時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, n_val, "sys_c_user", "建立人"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, n_val, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, n_val, "sys_m_user", "修改人"));

			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.TTA, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_Y, "col-md-12",
			// false, n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", true, n_val, "sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_Y, "col-md-1", true, a_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sc_g_name", "群組名稱", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sc_name", "名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			sc_name = body.getJSONObject("search").getString("sc_name");
			sc_name = sc_name.equals("") ? null : sc_name;
			sc_g_name = body.getJSONObject("search").getString("sc_g_name");
			sc_g_name = sc_g_name.equals("") ? null : sc_g_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		systemConfigs = configDao.findAllByConfig(sc_name, sc_g_name, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemConfigs.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sc_id", one.getScid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sc_g_id", one.getScgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sc_g_name", one.getScgname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sc_name", one.getScname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sc_value", one.getScvalue());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_user", one.getSyscuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_user", one.getSysmuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_note", one.getSysnote());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_sort", one.getSyssort());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_ver", one.getSysver());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_status", one.getSysstatus());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");
			for (Object one : list) {
				// 物件轉換
				SystemConfig sys_c = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_c.setScname(data.getString("sc_name"));
				sys_c.setScgname(data.getString("sc_g_name"));
				sys_c.setScvalue(data.getString("sc_value"));
				sys_c.setSysnote(data.has("sys_note") ? data.getString("sys_note") : "");
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSyscuser(user.getSuaccount());

				// 檢查群組名稱重複
				ArrayList<SystemConfig> sys_p_g = configDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_c.setScgid(sys_p_g.get(0).getScgid());
				} else {
					// 取得最新G_ID
					sys_c.setScgid(configDao.getSystem_config_g_seq());
				}
				configDao.save(sys_c);
			}
			check = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 存檔 資料清單
	@Transactional
	public boolean save_asData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				SystemConfig sys_c = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_c.setScname(data.getString("sc_name"));
				sys_c.setScgname(data.getString("sc_g_name"));
				sys_c.setScvalue(data.getString("sc_value"));
				sys_c.setSysnote(data.has("sys_note") ? data.getString("sys_note") : "");
				sys_c.setSyssort(data.getInt("sys_sort"));
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSyscuser(user.getSuaccount());

				// 檢查群組名稱重複
				ArrayList<SystemConfig> sys_c_g = configDao.findAllByConfigGroupTop1(sys_c.getScgname(), PageRequest.of(0, 1));
				if (sys_c_g != null && sys_c_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_c.setScgid(sys_c_g.get(0).getScgid());
				} else {
					// 取得最新G_ID
					sys_c.setScgid(configDao.getSystem_config_g_seq());
				}
				configDao.save(sys_c);
			}
			check = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}

	// 更新 資料清單
	@Transactional
	public boolean updateData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("modify");
			for (Object one : list) {
				// 物件轉換
				SystemConfig sys_p = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_p.setScid(data.getLong("sc_id"));
				sys_p.setScname(data.getString("sc_name"));
				sys_p.setScgid(data.getLong("sc_g_id"));
				sys_p.setScgname(data.getString("sc_g_name"));
				sys_p.setScvalue(data.getString("sc_value"));
				sys_p.setSysnote(data.has("sys_note") ? data.getString("sys_note") : "");
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSysmdate(new Date());

				// 檢查群組名稱重複
				ArrayList<SystemConfig> sys_p_g = configDao.findAllByConfigGroupTop1(sys_p.getScgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setScgid(sys_p_g.get(0).getScgid());
				} else {
					// 取得最新G_ID
					sys_p.setScgid(configDao.getSystem_config_g_seq());
				}
				configDao.save(sys_p);
			}
			// 有更新才正確
			if (list.length() > 0) {
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}

	// 移除 資料清單
	@Transactional
	public boolean deleteData(JSONObject body) {

		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("delete");
			for (Object one : list) {
				// 物件轉換
				SystemConfig sys_p = new SystemConfig();
				JSONObject data = (JSONObject) one;
				sys_p.setScid(data.getLong("sc_id"));

				configDao.deleteByScidAndSysheader(sys_p.getScid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
