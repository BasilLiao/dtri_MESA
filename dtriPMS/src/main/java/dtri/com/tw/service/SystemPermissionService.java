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
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemPermissionService {
	@Autowired
	private SystemPermissionDao permissionDao;
	@Autowired
	private SystemGroupDao groupDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size, String user) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemPermission> systemPermissions = new ArrayList<SystemPermission>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		String sp_name = null;
		String sp_g_name = null;
		String status = "0";
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("spid").descending());
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_id", FFS.h_t("功能ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_g_id", FFS.h_t("功能組ID", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_g_name", FFS.h_t("功能組名稱", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_name", FFS.h_t("功能名稱", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_control", FFS.h_t("功能控制", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sp_permission", FFS.h_t("權限範圍", "150px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_N));
			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray values = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, values, "sp_id", "功能ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, values, "sp_g_id", "功能組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "功能組名稱", "", FFM.Wri.W_Y, "col-md-2", true, values, "sp_g_name", "功能組名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "功能單元名稱", "", FFM.Wri.W_Y, "col-md-2", true, values, "sp_name", "功能名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "例:index.basil", "", FFM.Wri.W_Y, "col-md-2", true, values, "sp_control", "功能控制"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "例:0001001101", "", FFM.Wri.W_Y, "col-md-2", true, values, "sp_permission", "權限範圍"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N,
			// "col-md-2", false, values, "sys_c_date", "建立時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N,
			// "col-md-2", false, values, "sys_c_user", "建立人"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N,
			// "col-md-2", false, values, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N,
			// "col-md-2", false, values, "sys_m_user", "修改人"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", true, values, "sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-1", false, values, "sys_ver", "版本"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-10", false, values, "sys_note", "備註"));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			values.put((new JSONObject()).put("value", "Admin(專用)").put("key", "2"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_Y, "col-md-2", true, values, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sp_g_name", "功能組名稱", new JSONArray()));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sp_name", "功能名稱", new JSONArray()));
			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "", "col-md-2", "sys_status", "狀態", values));
			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			sp_name = body.getJSONObject("search").getString("sp_name");
			sp_name = sp_name.equals("") ? null : sp_name;
			sp_g_name = body.getJSONObject("search").getString("sp_g_name");
			sp_g_name = sp_g_name.equals("") ? null : sp_g_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;

		}
		systemPermissions = permissionDao.findAllByPermission(sp_name, sp_g_name, Integer.parseInt(status), user, page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemPermissions.forEach(one -> {
			int ord = 0;
			JSONObject object_body = new JSONObject();
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_id", one.getSpid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_g_id", one.getSpgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_g_name", one.getSpgname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_name", one.getSpname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_control", one.getSpcontrol());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sp_permission", one.getSppermission());
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
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSyscuser(user.getSuaccount());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				SystemGroup sys_g = new SystemGroup();
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
					// 同步添加到ADMIN
					sys_g.setSggid(1l);
					sys_g.setSystemPermission(sys_p);
					sys_g.setSyssort(sys_p.getSyssort());
					sys_g.setSgpermission("1111111111");
				} else {
					// 取得最新G_ID

					sys_p.setSpgid((permissionDao.getSystem_config_g_seq()));
					// 同步添加到ADMIN
					sys_g.setSggid(1l);
					sys_g.setSystemPermission(sys_p);
					sys_g.setSyssort(sys_p.getSyssort());
					sys_g.setSgpermission("1111111111");

				}
				permissionDao.save(sys_p);

				// 同步添加到ADMIN
				sys_g = groupDao.findBySgidOrderBySgidAscSyssortAsc(1l).get(0);
				SystemGroup sys_g_new = new SystemGroup();
				sys_g_new.setSystemPermission(sys_p);
				sys_g_new.setSggid(sys_g.getSgid());
				sys_g_new.setSgname(sys_g.getSgname());
				sys_g_new.setSgpermission("1111111111");
				sys_g_new.setSyssort(sys_p.getSyssort());
				groupDao.save(sys_g_new);
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
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSyscuser(user.getSuaccount());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
					sys_p.setSysheader(false);
				} else {
					// 取得最新G_ID
					sys_p.setSpgid((permissionDao.getSystem_config_g_seq()));
					sys_p.setSysheader(false);
				}
				permissionDao.save(sys_p);

				// 同步添加到ADMIN
				SystemGroup sys_g = groupDao.findBySgidOrderBySgidAscSyssortAsc(1l).get(0);
				SystemGroup sys_g_new = new SystemGroup();
				sys_g_new.setSystemPermission(sys_p);
				sys_g_new.setSggid(sys_g.getSgid());
				sys_g_new.setSgname(sys_g.getSgname());
				sys_g_new.setSgpermission("1111111111");
				sys_g_new.setSyssort(sys_p.getSyssort());
				groupDao.save(sys_g_new);
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
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpid(data.getLong("sp_id"));
				sys_p.setSpname(data.getString("sp_name"));
				sys_p.setSpgid(data.getLong("sp_g_id"));
				sys_p.setSpgname(data.getString("sp_g_name"));
				sys_p.setSpcontrol(data.getString("sp_control"));
				sys_p.setSppermission(data.getString("sp_permission"));
				sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmdate(new Date());

				// 檢查群組名稱重複
				ArrayList<SystemPermission> sys_p_g = permissionDao.findAllByPermissionGroupTop1(sys_p.getSpgname(), PageRequest.of(0, 1));
				if (sys_p_g != null && sys_p_g.size() > 0) {
					// 重複 則取同樣G_ID
					sys_p.setSpgid(sys_p_g.get(0).getSpgid());
					sys_p.setSysheader(false);
				} else {
					// 取得最新G_ID
					sys_p.setSpgid((permissionDao.getSystem_config_g_seq()));
					sys_p.setSysheader(true);
				}
				permissionDao.save(sys_p);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
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
				SystemPermission sys_p = new SystemPermission();
				JSONObject data = (JSONObject) one;
				sys_p.setSpid(data.getLong("sp_id"));

				permissionDao.deleteBySpidAndSysheader(sys_p.getSpid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
