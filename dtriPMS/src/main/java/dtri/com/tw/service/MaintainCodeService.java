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
import dtri.com.tw.db.entity.MaintainCode;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.MaintainCodeDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class MaintainCodeService {
	@Autowired
	private MaintainCodeDao mcDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<MaintainCode> maintainCodes = new ArrayList<MaintainCode>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("mcid").descending());
		String mc_value = null;
		String mc_g_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "mc_id", FFS.h_t("ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "mc_g_id", FFS.h_t("群組ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "mc_g_name", FFS.h_t("故障(總項目)名稱", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "mc_name", FFS.h_t("故障(支項目)名稱", "180px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "mc_value", FFS.h_t("故障編碼", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_header", FFS.h_t("群組代表?", "100px", FFM.Wri.W_Y));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "mc_id", "ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "mc_g_id", "群組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "mc_g_name", "故障(總項目)名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "mc_name", "故障(支項目)名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "mc_value", "故障編碼"));

//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFS.See.DIS, "col-md-2", false, n_val, "sys_c_date", "建立時間"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFS.See.DIS, "col-md-2", false, n_val, "sys_c_user", "建立人"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFS.See.DIS, "col-md-2", false, n_val, "sys_m_date", "修改時間"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFS.See.DIS, "col-md-2", false, n_val, "sys_m_user", "修改人"));

//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.TTA, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-12", false, n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-2", true, n_val, "sys_sort", "排序"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "", "", FFS.See.DIS, "col-md-2", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_N, "col-md-1", true, a_val, "sys_status", "狀態"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_header", "群組代表"));
			bean.setCell_modify(obj_m);

			// 放入群主指定 [(key)](modify/Create/Delete) 格式
			JSONArray obj_g_m = new JSONArray();
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-2", "mc_name", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-2", "mc_g_name", ""));

			bean.setCell_g_modify(obj_g_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "mc_g_name", "維修群組名稱", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "mc_value", "維修群組代號", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			mc_value = body.getJSONObject("search").getString("mc_value");
			mc_value = mc_value.equals("") ? null : mc_value;
			mc_g_name = body.getJSONObject("search").getString("mc_g_name");
			mc_g_name = mc_g_name.equals("") ? null : mc_g_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		maintainCodes = mcDao.findAllByMaintainCode(mc_value, mc_g_name, Integer.parseInt(status), page_r);
		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		maintainCodes.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "mc_id", one.getMcid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "mc_g_id", one.getMcgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "mc_g_name", one.getMcgname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "mc_name", one.getMcname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "mc_value", one.getMcvalue());

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
		// 是否為群組模式? type:[group/general] || 新增時群組? createOnly:[all/general]
		bean.setBody_type(new JSONObject("{'type':'group','createOnly':'all'}"));
		// 沒查到東西
		if (maintainCodes.size() == 0) {
			bean.autoMsssage(102);
			return bean;
		}
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");
			String mc_g_name = "";
			Long mc_g_id = 0l;
			for (Object one : list) {
				// 物件轉換
				MaintainCode sys_c = new MaintainCode();
				JSONObject data = (JSONObject) one;
				// 檢查代碼重複/名稱?
				if (mcDao.findAllByMcvalueAndSysheader(data.getString("mc_value"), false).size() > 0) {
					return false;
				}

				// 檢查是否在特定屬性下
				ArrayList<MaintainCode> mc = mcDao.findAllBySysheaderAndMcgname(true, data.getString("mc_g_name"));
				if (mc.size() == 0 && mc_g_id == 0) {
					// 父類別
					mc_g_id = mcDao.getMaintain_code_g_seq();
					mc_g_name = data.getString("mc_g_name");
					sys_c.setMcgid(mc_g_id);
					sys_c.setMcgname(data.getString("mc_g_name"));
					sys_c.setMcname("");
					sys_c.setMcvalue(data.getString("mc_value"));
					sys_c.setSysnote("");
					sys_c.setSyssort(0);
					sys_c.setSysstatus(0);
					sys_c.setSysheader(true);
					sys_c.setSysmuser(user.getSuaccount());
					sys_c.setSyscuser(user.getSuaccount());
					mcDao.save(sys_c);

				} else {
					// 子類別
					sys_c.setMcgid(mc_g_id == 0 ? mc.get(0).getMcgid() : mc_g_id);
					sys_c.setMcgname(mc_g_name.equals("") ? mc.get(0).getMcgname() : mc_g_name);
					sys_c.setMcname(data.getString("mc_name"));
					sys_c.setMcvalue(data.getString("mc_value"));
					sys_c.setSysnote("");
					sys_c.setSyssort(0);
					sys_c.setSysstatus(0);
					sys_c.setSysheader(false);
					sys_c.setSysmuser(user.getSuaccount());
					sys_c.setSyscuser(user.getSuaccount());
					mcDao.save(sys_c);

				}
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
			String mc_g_name = "";
			Long mc_g_id = 0l;
			List<MaintainCode> sys_c_s = new ArrayList<MaintainCode>();
			for (Object one : list) {
				// 物件轉換
				MaintainCode sys_c = new MaintainCode();
				JSONObject data = (JSONObject) one;
				sys_c.setMcgid(data.getLong("mc_g_id"));
				sys_c.setMcgname(data.getString("mc_g_name"));
				// 檢查代碼重複/名稱?
				if (mcDao.findAllByMcvalue(data.getString("mc_value")).size() > 0) {
					return false;
				}
				if (mc_g_id == 0) {
					// 檢查是否在特定屬性下
					ArrayList<MaintainCode> mc = mcDao.findAllBySysheaderAndMcgname(true, data.getString("mc_g_name"));
					if (mc.size() > 0) {
						return false;
					}
					// 父類別
					mc_g_id = mcDao.getMaintain_code_g_seq();
					mc_g_name = data.getString("mc_g_name");
					sys_c.setMcgid(mc_g_id);
					sys_c.setMcgname(mc_g_name);
					sys_c.setMcname("");
					sys_c.setMcvalue(data.getString("mc_value"));
					sys_c.setSysnote("");
					sys_c.setSyssort(0);
					sys_c.setSysstatus(0);
					sys_c.setSysheader(true);
					sys_c.setSysmuser(user.getSuaccount());
					sys_c.setSyscuser(user.getSuaccount());
					// mcDao.save(sys_c);
				} else {
					// 子類別
					sys_c.setMcgid(mc_g_id);
					sys_c.setMcgname(mc_g_name);
					sys_c.setMcname(data.getString("mc_name"));
					sys_c.setMcvalue(data.getString("mc_value"));
					sys_c.setSysnote("");
					sys_c.setSyssort(0);
					sys_c.setSysstatus(0);
					sys_c.setSysheader(false);
					sys_c.setSysmuser(user.getSuaccount());
					sys_c.setSyscuser(user.getSuaccount());
					// mcDao.save(sys_c);
				}
				sys_c_s.add(sys_c);
			}
			mcDao.saveAll(sys_c_s);
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
			List<MaintainCode> sys_p_s = new ArrayList<MaintainCode>();
			JSONArray list = body.getJSONArray("modify");
			String mc_g_name = "";
			for (Object one : list) {
				// 物件轉換
				MaintainCode sys_p = new MaintainCode();
				JSONObject data = (JSONObject) one;
				if (data.getBoolean("sys_header")) {
					// 父類別
					mc_g_name = data.getString("mc_g_name");
					sys_p.setMcid(data.getLong("mc_id"));
					sys_p.setMcgid(data.getLong("mc_g_id"));
					sys_p.setMcname("");
					sys_p.setMcgname(mc_g_name);
					sys_p.setMcvalue(data.getString("mc_value"));
					sys_p.setSysnote("");
					sys_p.setSyssort(0);
					sys_p.setSysstatus(0);
					sys_p.setSysheader(true);
					sys_p.setSysmuser(user.getSuaccount());
					sys_p.setSysmdate(new Date());
					sys_p_s.add(sys_p);
					// mcDao.save(sys_p);
				} else {
					// 子
					sys_p.setMcid(data.getLong("mc_id"));
					sys_p.setMcgid(data.getLong("mc_g_id"));
					sys_p.setMcname(data.getString("mc_name"));
					sys_p.setMcgname(mc_g_name);
					sys_p.setMcvalue(data.getString("mc_value"));
					sys_p.setSysnote("");
					sys_p.setSyssort(0);
					sys_p.setSysstatus(0);
					sys_p.setSysheader(false);
					sys_p.setSysmuser(user.getSuaccount());
					sys_p.setSysmdate(new Date());
					sys_p_s.add(sys_p);
					// mcDao.save(sys_p);
				}
			}
			mcDao.saveAll(sys_p_s);
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
				MaintainCode sys_p = new MaintainCode();
				JSONObject data = (JSONObject) one;
				// 父類別除外
				if (data.getBoolean("sys_header")) {
					sys_p.setMcgid(data.getLong("mc_g_id"));
					mcDao.deleteByMcgid(sys_p.getMcgid());
				} else {
					sys_p.setMcid(data.getLong("mc_id"));
					mcDao.deleteByMcidAndSysheader(sys_p.getMcid(), false);
				}
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
