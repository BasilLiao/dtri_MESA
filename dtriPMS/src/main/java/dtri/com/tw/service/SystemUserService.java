package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.SystemUserDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemUserService {
	@Autowired
	private SystemUserDao userDao;
	@Autowired
	private SystemGroupDao groupDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size, SystemUser user) {
		PackageBean bean = new PackageBean();
		ArrayList<SystemUser> systemUsers = new ArrayList<SystemUser>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		String su_account = null;
		String su_name = null;
		String status = "0";
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("suid").descending());
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_id", FFS.h_t("ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_sg_g_id", FFS.h_t("群組ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_name", FFS.h_t("姓名", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_e_name", FFS.h_t("英文姓名", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_position", FFS.h_t("職位", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_account", FFS.h_t("帳號", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "su_email", FFS.h_t("Email", "200px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式

			JSONArray obj_m = new JSONArray();
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, new JSONArray(), "su_id", "ID"));

			JSONArray groups = new JSONArray();
			// (u些事admin專用)
			if (user.getSusggid() == 1) {
				groupDao.findAllBySysheader(true, PageRequest.of(0, 999)).forEach(s -> {
					groups.put((new JSONObject()).put("value", s.getSgname()).put("key", s.getSggid()));
				});
			} else {
				groupDao.findAllBySysheaderAndSgidNot(true, 1l, PageRequest.of(0, 999)).forEach(s -> {
					groups.put((new JSONObject()).put("value", s.getSgname()).put("key", s.getSggid()));
				});
			}

			JSONArray value = new JSONArray();
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, groups, "su_sg_g_id", "群組名稱"));
			JSONArray values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, value, "su_name", "姓名"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, value, "su_e_name", "英文姓名"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, value, "su_account", "帳號"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.PASS, "", "", FFM.Wri.W_Y, "col-md-2", false, value, "su_password", "密碼"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, value, "su_position", "職位"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-4", true, value, "su_email", "Email"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, value, "sys_c_date", "建立時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, value, "sys_c_user", "建立人"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, value, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N,
			// "col-md-2", false, value, "sys_m_user", "修改人"));

			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "0", "0", FFM.Wri.W_N,
			// "col-md-1",
			// false, value, "sys_ver", "版本"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "0", "0", FFM.Wri.W_Y,
			// "col-md-1",
			// true, value, "sys_sort", "排序"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-6", false, value, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "0", "0", FFM.Wri.W_Y, "col-md-2", true, values, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "su_account", "帳號", value));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "su_name", "姓名", value));

			values = new JSONArray();
			values.put((new JSONObject()).put("value", "正常").put("key", "0"));
			values.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", values));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			su_account = body.getJSONObject("search").getString("su_account");
			su_account = su_account.equals("") ? null : su_account;
			su_name = body.getJSONObject("search").getString("su_name");
			su_name = su_name.equals("") ? null : su_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		// 全查
		if (user.getSusggid() == 1) {
			systemUsers = userDao.findAllBySystemUser(su_name, su_account, Integer.parseInt(status), page_r);
		} else {
			systemUsers = userDao.findAllBySystemUserNotAdmin(su_name, su_account, Integer.parseInt(status), page_r);
		}

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemUsers.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_id", one.getSuid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_sg_g_id", one.getSusggid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_name", one.getSuname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_e_name", one.getSuename());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_position", one.getSuposition());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_account", one.getSuaccount());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "su_email", one.getSuemail());

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
		PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		try {
			JSONArray list = body.getJSONArray("create");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_c = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_c.setSusggid(data.getLong("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));
				// 密碼空不存
				if (data.getString("su_password").equals("")) {
					return false;
				}
				sys_c.setSupassword(pwdEncoder.encode((data.getString("su_password"))));
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(0);
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSyscuser(user.getSuaccount());

				// 帳號重複
				if (userDao.findBySuaccount(sys_c.getSuaccount()) != null) {
					check = false;
					return check;
				} else {
					userDao.save(sys_c);
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
		PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		try {
			JSONArray list = body.getJSONArray("save_as");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_c = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_c.setSusggid(data.getLong("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));

				// 密碼空不存
				if (data.getString("su_password").equals("")) {
					return false;
				}
				sys_c.setSupassword(pwdEncoder.encode(data.getString("su_password")));
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(0);
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSyscuser(user.getSuaccount());

				// 帳號重複
				if (userDao.findBySuaccount(sys_c.getSuaccount()) != null) {
					check = false;
					return check;
				} else {
					userDao.save(sys_c);
				}
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
			PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
			JSONArray list = body.getJSONArray("modify");
			for (Object one : list) {
				// 物件轉換
				JSONObject data = (JSONObject) one;
				SystemUser sys_c = userDao.findAllBySuid(data.getLong("su_id")).get(0);
				// sys_c.setSuid(data.getInt("su_id"));
				sys_c.setSusggid(data.getLong("su_sg_g_id"));
				sys_c.setSuname(data.getString("su_name"));
				sys_c.setSuename(data.getString("su_e_name"));
				sys_c.setSuposition(data.getString("su_position"));
				sys_c.setSuaccount(data.getString("su_account"));
				// 密碼空不存
				if (!data.getString("su_password").equals("")) {
					sys_c.setSupassword(pwdEncoder.encode(data.getString("su_password")));
				}
				sys_c.setSuemail(data.getString("su_email"));

				sys_c.setSysnote(data.getString("sys_note"));
				sys_c.setSyssort(0);
				sys_c.setSysstatus(data.getInt("sys_status"));
				sys_c.setSysmuser(user.getSuaccount());
				sys_c.setSysmdate(new Date());

				userDao.save(sys_c);
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
	public boolean deleteData(JSONObject body, SystemUser user) {

		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("delete");
			for (Object one : list) {
				// 物件轉換
				SystemUser sys_p = new SystemUser();
				JSONObject data = (JSONObject) one;
				sys_p.setSuid(data.getLong("su_id"));
				// 不得刪除自己
				if (!data.getString("su_account").equals(user.getSuaccount())) {
					if (userDao.deleteBySuid(sys_p.getSuid()) > 0) {
						check = true;
					}
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
