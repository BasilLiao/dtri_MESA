package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionSN;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionSNDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionSnService {
	@Autowired
	private ProductionSNDao snDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<ProductionSN> productinoSns = new ArrayList<ProductionSN>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("psid").descending());
		String ps_name = null;
		String ps_g_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_id", FFS.h_t("ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_g_id", FFS.h_t("群組ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_g_name", FFS.h_t("群組名稱", "200px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_name", FFS.h_t("規則名稱", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_value", FFS.h_t("規則參數", "100px", FFM.Wri.W_N));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "500px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_header", FFS.h_t("群組代表?", "100px", FFM.Wri.W_N));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "ps_id", "ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "ps_g_id", "群組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "ps_g_name", "規則群組名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "ps_name", "規則名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "ps_value", "規則參數"));

//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sys_c_date", "建立時間"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sys_c_user", "建立人"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_date", "修改時間"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_user", "修改人"));

			obj_m.put(FFS.h_m(FFM.Dno.D_N,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-10", false, n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-2", true, n_val, "sys_sort", "排序"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_N, "col-md-1", true, a_val, "sys_status", "狀態"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_header", "群組代表"));
			bean.setCell_modify(obj_m);

			// 放入群主指定 [(key)](modify/Create/Delete) 格式
			JSONArray obj_g_m = new JSONArray();
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-2", "ps_name",""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-2", "ps_value",""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-2", "ps_g_name",""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-10", "sys_note",""));
			
			bean.setCell_g_modify(obj_g_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "ps_g_name", "規則群組名稱", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "ps_name", "規則名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			ps_name = body.getJSONObject("search").getString("ps_name");
			ps_name = ps_name.equals("") ? null : ps_name;
			ps_g_name = body.getJSONObject("search").getString("ps_g_name");
			ps_g_name = ps_g_name.equals("") ? null : ps_g_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		productinoSns = snDao.findAllByProductionSN(ps_name, ps_g_name, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productinoSns.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_id", one.getPsid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_g_id", one.getPsgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_g_name", one.getPsgname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_name", one.getPsname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_value", one.getPsvalue());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_user", one.getSyscuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_user", one.getSysmuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_note", one.getSysnote());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_sort", one.getSyssort());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_ver", one.getSysver());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_status", one.getSysstatus());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_header", one.getSysheader());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		// bean.setBody_type("fatherSon");
		// 是否為群組模式? type:[group/general] || 新增時群組? createOnly:[all/general]
		bean.setBody_type(new JSONObject("{'type':'group','createOnly':'all'}"));

		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");
			List<ProductionSN> sys_cs = new ArrayList<ProductionSN>();
			for (Object one : list) {
				// 物件轉換
				ProductionSN sys_c = new ProductionSN();
				JSONObject data = (JSONObject) one;
				// 檢查是否在特定屬性下(此處限制)
				ArrayList<ProductionSN> sn = snDao.findAllBySysheaderAndPsgid(true, data.getLong("ps_g_id"));
				if (sn.size() == 0) {
					return false;
				}
				// 檢查重複
				sn = snDao.findAllBySysheaderOrderByPsgidAsc(false);
				for (ProductionSN sn_one : sn) {
					if (sn_one.getPsname().equals(data.getString("ps_name")) || sn_one.getPsvalue().equals(data.getString("ps_value"))) {
						return false;
					}
				}
				sys_c.setPsgid(sn.get(0).getPsgid());
				sys_c.setPsgname(sn.get(0).getPsgname());
				sys_c.setPsname(data.getString("ps_name"));
				sys_c.setPsvalue(data.getString("ps_value"));
				sys_c.setSysnote("");
				sys_c.setSyssort(0);
				sys_c.setSysstatus(0);
				sys_c.setSysheader(false);
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSyscuser(user.getSuaccount());
				sys_cs.add(sys_c);
			}
			snDao.saveAll(sys_cs);
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
			if (list.length() == 0) {
				return true;
			}
//			for (Object one : list) {
//				// 物件轉換
//				ProductionSN sys_c = new ProductionSN();
//				JSONObject data = (JSONObject) one;
//				// 檢查是否在特定屬性下
//				ArrayList<ProductionSN> sn = snDao.findAllBySysheaderAndPsgid(true, data.getInt("ps_g_id"));
//				if (sn.size() == 0) {
//					return false;
//				}
//				//不存新群組
//				if(data.getBoolean("sys_header")) {
//					continue;
//				}
//				sys_c.setPsgid(sn.get(0).getPsgid());
//				sys_c.setPsgname(sn.get(0).getPsgname());
//				sys_c.setPsname(data.getString("ps_name"));
//				sys_c.setPsvalue(data.getString("ps_value"));
//				sys_c.setSysnote("");
//				sys_c.setSyssort(0);
//				sys_c.setSysstatus(0);
//				sys_c.setSysheader(false);
//				sys_c.setSysmuser(user.getSuaccount());
//				sys_c.setSyscuser(user.getSuaccount());
//
//				snDao.save(sys_c);
//			}
//			check = true;
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
			String ps_g_name = "";
			for (Object one : list) {
				// 物件轉換
				ProductionSN sys_p = new ProductionSN();
				JSONObject data = (JSONObject) one;
				sys_p.setPsid(data.getLong("ps_id"));
				sys_p.setPsgid(data.getLong("ps_g_id"));
				sys_p.setPsname(data.getString("ps_name"));
				sys_p.setPsgname(data.getString("ps_g_name"));
				sys_p.setPsvalue(data.getString("ps_value"));

				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(0);
				sys_p.setSysstatus(0);

				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSysmdate(new Date());
				// 父類別
				if (data.getBoolean("sys_header")) {
					ps_g_name = data.getString("ps_g_name");
					sys_p.setPsgname(ps_g_name);
					sys_p.setSysheader(true);
					sys_p.setPsname("");
					snDao.save(sys_p);
				} else {
					// 子類別
					sys_p.setPsgname(ps_g_name);
					sys_p.setSysheader(false);
					snDao.save(sys_p);
				}

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
				ProductionSN sys_p = new ProductionSN();
				JSONObject data = (JSONObject) one;
				// 父類別除外
				if (!data.getBoolean("sys_header")) {
					// 必須要保持兩筆資料以上
					if (snDao.findAllBySysheaderAndPsgid(false, data.getLong("ps_g_id")).size() > 1) {
						sys_p.setPsid(data.getLong("ps_id"));
						snDao.deleteByPsidAndSysheader(sys_p.getPsid(), false);
					}
					check = true;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
