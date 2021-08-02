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
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.SystemGroupDao;
import dtri.com.tw.db.pgsql.dao.SystemPermissionDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class SystemGroupService {
	@Autowired
	private SystemGroupDao groupDao;
	@Autowired
	private SystemPermissionDao permissionDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size, SystemUser user) {
		PackageBean bean = new PackageBean();
		List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
		ArrayList<SystemPermission> permissions = new ArrayList<SystemPermission>();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("sggid").descending());
		String sg_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			page_r = PageRequest.of(page, 999, Sort.by("spgid").descending());

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_id", FFS.h_t("ID", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_g_id", FFS.h_t("群組ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_name", FFS.h_t("群組名稱", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_sp_name", FFS.h_t("單元名稱", "150px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_header", FFS.h_t("群組代表", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_sp_id", FFS.h_t("關聯ID", "100px", FFM.Wri.W_N));

			// (sg_permission[特殊3(512),特殊2(256),特殊1(128),訪問(64),下載(32),上傳(16),新增(8),修改(4),刪除(2),查詢(1)])
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_512", FFS.h_t("S3", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_256", FFS.h_t("S2", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_128", FFS.h_t("S1", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_64", FFS.h_t("訪", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_32", FFS.h_t("下", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_16", FFS.h_t("上", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_8", FFS.h_t("增", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_4", FFS.h_t("改", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_2", FFS.h_t("刪", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sg_permission_1", FFS.h_t("查", "50px", FFM.Wri.W_N));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));

			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray s_val = new JSONArray();
			JSONArray n_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sg_id", "群組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "sg_g_id", "群組類別ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_ver", "版本"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", true, n_val, "sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "sg_name", "群組名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "sg_sp_name", "單元名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_header", "群組代表"));

			s_val = new JSONArray();
			s_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			s_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_N, "col-md-1", true, s_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			JSONArray st_val = new JSONArray();
			permissions = permissionDao.findAllByPermission(null, null, 0, user.getSuaccount(), page_r);
			Long old_p_g_id = 0l;
			String old_p_g_name = "";
			for (SystemPermission s : permissions) {
				// 排除群組代表
				old_p_g_id = s.getSpgid();
				if (old_p_g_id != 0) {
					if (old_p_g_name.equals(s.getSpgname())) {
						st_val.put((new JSONObject()).put("value", s.getSpname()).put("key", s.getSpid()));
					} else {
						old_p_g_name = s.getSpgname();
						st_val.put((new JSONObject()).put("value", "====" + old_p_g_name + "====").put("key", "dis" + old_p_g_id).put("dis",
								"disabled"));
						st_val.put((new JSONObject()).put("value", s.getSpname()).put("key", s.getSpid()));
					}
				}
			}
			//
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, st_val, "sg_sp_id", "權限關聯ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_512", "S3"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_256", "S2"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_128", "S1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_64", "訪問"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_32", "下載"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_16", "上傳"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_8", "新增"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_4", "修改"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_2", "刪除"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.CHE, FFM.Type.CHE, "", "", FFM.Wri.W_Y, "col-md-1", false, n_val, "sg_permission_1", "查詢"));

			// 放入群主指定 [(key)](modify/Create/Delete) 格式
			JSONArray obj_g_m = new JSONArray();
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sys_sort", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_id", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_g_id", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_sp_id", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_sp_name", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-2", "sg_name", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_512", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_256", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_128", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_64", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_32", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_16", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_8", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_4", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_2", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sg_permission_1", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-1", "sys_status", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sys_header", ""));

			bean.setCell_g_modify(obj_g_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "sg_name", "群組名稱", n_val));

			s_val = new JSONArray();
			s_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			s_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", s_val));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			sg_name = body.getJSONObject("search").getString("sg_name");
			sg_name = sg_name.equals("") ? null : sg_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}

		// 全查
		page_r = PageRequest.of(page, 999, Sort.by("sggid").descending());
		List<Long> sggid = new ArrayList<Long>();
		// 是否=為系統使用者?
		if (user.getSuid() == 1) {
			groupDao.findAllBySysheader(true, page_r).forEach(s -> {
				sggid.add(s.getSggid());
			});
		} else {
			groupDao.findAllBySysheaderAndSgidNot(true, 1l, page_r).forEach(s -> {
				sggid.add(s.getSggid());
			});
		}

		systemGroup = groupDao.findAllBySystemGroup(sg_name, Integer.parseInt(status), sggid);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		systemGroup.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_id", one.getSgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_g_id", one.getSggid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_name", one.getSgname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_sp_name", one.getSystemPermission().getSpname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_header", one.getSysheader());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_sp_id", one.getSystemPermission().getSpid());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_512",
					Character.toString(one.getSgpermission().charAt(0)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_256",
					Character.toString(one.getSgpermission().charAt(1)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_128",
					Character.toString(one.getSgpermission().charAt(2)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_64",
					Character.toString(one.getSgpermission().charAt(3)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_32",
					Character.toString(one.getSgpermission().charAt(4)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_16",
					Character.toString(one.getSgpermission().charAt(5)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_8",
					Character.toString(one.getSgpermission().charAt(6)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_4",
					Character.toString(one.getSgpermission().charAt(7)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_2",
					Character.toString(one.getSgpermission().charAt(8)).equals("0") ? false : true);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sg_permission_1",
					Character.toString(one.getSgpermission().charAt(9)).equals("0") ? false : true);

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_user", one.getSyscuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_user", one.getSysmuser());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_sort", one.getSyssort());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_ver", one.getSysver());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_status", one.getSysstatus());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_note", one.getSysnote());
			object_bodys.put(object_body);
		});
		bean.setBody(new JSONObject().put("search", object_bodys));
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
			String sg_name = "";
			Long sg_g_id = 0l;
			for (Object one : list) {
				// 物件轉換
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();

				// 群組判定代表
				p.setSpid(data.getString("sg_sp_id").equals("") ? 1l : data.getLong("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission((data.getBoolean("sg_permission_512") ? "1" : "0") + (data.getBoolean("sg_permission_256") ? "1" : "0")
						+ (data.getBoolean("sg_permission_128") ? "1" : "0") + (data.getBoolean("sg_permission_64") ? "1" : "0")
						+ (data.getBoolean("sg_permission_32") ? "1" : "0") + (data.getBoolean("sg_permission_16") ? "1" : "0")
						+ (data.getBoolean("sg_permission_8") ? "1" : "0") + (data.getBoolean("sg_permission_4") ? "1" : "0")
						+ (data.getBoolean("sg_permission_2") ? "1" : "0") + (data.getBoolean("sg_permission_1") ? "1" : "0"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSyscuser(user.getSuaccount());
				sys_p.setSysnote("");
				// 如果是特定的子類別
				if (data.getString("sg_g_id") != null && !data.getString("sg_g_id").equals("")) {
					sg_g_id = data.getLong("sg_g_id");
					sg_name = data.getString("sg_name");
				}

				// 新建 群組代表名稱-父類別
				// SystemGroup sys_p_h = new SystemGroup();
				if (sg_g_id == 0) {
					sys_p.setSggid(groupDao.getSystem_group_g_seq());
					sg_g_id = sys_p.getSggid();
					sg_name = sys_p.getSgname();

					SystemGroup sys_p_h = new SystemGroup();
					sys_p_h.setSgname(sg_name);
					sys_p_h.setSggid(sg_g_id);
					sys_p_h.setSgpermission("0000000000");
					sys_p_h.setSysheader(true);
					sys_p_h.setSystemPermission(new SystemPermission(1l));
					sys_p_h.setSyssort(0);
					sys_p_h.setSysstatus(0);
					sys_p_h.setSysmuser(user.getSuaccount());
					sys_p_h.setSyscuser(user.getSuaccount());
					groupDao.save(sys_p_h);
				} else {
					// 登記子類別
					sys_p.setSysheader(false);
					sys_p.setSgname(sg_name);
					sys_p.setSggid(sg_g_id);
					groupDao.save(sys_p);
				}

			}
			check = true;
		} catch (Exception e) {
			System.out.println(e);
			return check;
		}
		return check;
	}

	// 另存檔 資料清單
	@Transactional
	public boolean save_asData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("save_as");
			String sg_name = "";
			Long sg_g_id = 0l;
			// 如果沒資料則不做事
			if (list.length() == 0) {
				return true;
			}

			// 檢查群組名稱重複(沒重複 則定義 group_name)
			for (Object one : list) {
				JSONObject data = (JSONObject) one;
				if (data.getBoolean("sys_header")) {
					ArrayList<SystemGroup> sys_p_g = groupDao.findAllByGroupTop1(data.getString("sg_name"), PageRequest.of(0, 1));
					if (sys_p_g != null && sys_p_g.size() > 0) {
						return false;
					} else {
						sg_name = data.getString("sg_name");
					}
				}
			}
			// 如果沒定義到 sg_name 則排除
			if (sg_name.equals("")) {
				return false;
			}
			for (Object one : list) {
				// 物件轉換
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();
				p.setSpid(data.isNull("sg_sp_id") ? 1l : data.getLong("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission((data.getBoolean("sg_permission_512") ? "1" : "0") + (data.getBoolean("sg_permission_256") ? "1" : "0")
						+ (data.getBoolean("sg_permission_128") ? "1" : "0") + (data.getBoolean("sg_permission_64") ? "1" : "0")
						+ (data.getBoolean("sg_permission_32") ? "1" : "0") + (data.getBoolean("sg_permission_16") ? "1" : "0")
						+ (data.getBoolean("sg_permission_8") ? "1" : "0") + (data.getBoolean("sg_permission_4") ? "1" : "0")
						+ (data.getBoolean("sg_permission_2") ? "1" : "0") + (data.getBoolean("sg_permission_1") ? "1" : "0"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmuser(user.getSuaccount());
				sys_p.setSyscuser(user.getSuaccount());

				// 新建 群組代表名稱-父類別
				if (data.getBoolean("sys_header")) {
					// 查詢最新 群組ID
					// sg_g_id = groupDao.findAllByTop1(PageRequest.of(0, 1)).get(0).getSggid() + 1;
					// 登記父類 群組代表名稱
					sg_name = data.getString("sg_name");
					SystemGroup sys_p_h = new SystemGroup();
					sys_p_h.setSggid(groupDao.getSystem_group_g_seq());
					sys_p_h.setSgname(sg_name);
					sys_p_h.setSgpermission("0000000000");
					sys_p_h.setSysheader(true);
					sys_p_h.setSystemPermission(new SystemPermission(1l));

					sys_p_h.setSysnote("");
					sys_p_h.setSyssort(0);
					sys_p_h.setSysstatus(0);
					sys_p_h.setSysmuser(user.getSuaccount());
					sys_p_h.setSyscuser(user.getSuaccount());
					sg_g_id = sys_p_h.getSggid();
					groupDao.save(sys_p_h);
				} else {
					// 登記子類別
					sys_p.setSgname(sg_name == "" ? null : sg_name);
					sys_p.setSggid(sg_g_id);
					groupDao.save(sys_p);

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
			String sg_name = "";
			// Integer sg_g_id = 0;
			JSONArray list = body.getJSONArray("modify");
			for (Object one : list) {
				// 物件轉換
				SystemGroup sys_p = new SystemGroup();
				JSONObject data = (JSONObject) one;
				sys_p.setSgid(data.getLong("sg_id"));
				sys_p.setSggid(data.getLong("sg_g_id"));
				sys_p.setSgname(data.getString("sg_name"));
				SystemPermission p = new SystemPermission();
				p.setSpid(data.isNull("sg_sp_id") ? 1l : data.getLong("sg_sp_id"));
				sys_p.setSystemPermission(p);
				sys_p.setSgpermission((data.getBoolean("sg_permission_512") ? "1" : "0") + (data.getBoolean("sg_permission_256") ? "1" : "0")
						+ (data.getBoolean("sg_permission_128") ? "1" : "0") + (data.getBoolean("sg_permission_64") ? "1" : "0")
						+ (data.getBoolean("sg_permission_32") ? "1" : "0") + (data.getBoolean("sg_permission_16") ? "1" : "0")
						+ (data.getBoolean("sg_permission_8") ? "1" : "0") + (data.getBoolean("sg_permission_4") ? "1" : "0")
						+ (data.getBoolean("sg_permission_2") ? "1" : "0") + (data.getBoolean("sg_permission_1") ? "1" : "0"));
				// sys_p.setSysnote(data.getString("sys_note"));
				sys_p.setSyssort(data.getInt("sys_sort"));
				sys_p.setSysstatus(data.getInt("sys_status"));
				sys_p.setSysmdate(new Date());

				// 檢查[群組名稱]重複
				ArrayList<SystemGroup> sys_p_g = groupDao.findAllByGroupTop1(data.getString("sg_name"), PageRequest.of(0, 1));
				if (sys_p_g != null || data.getBoolean("sys_header")) {
					// 如果是 父類別(限定修改)+(子類別全數修改)
					if (data.getBoolean("sys_header")) {
						List<SystemGroup> sys_p_g_old = groupDao.findBySggidOrderBySggid(sys_p.getSggid());
						sys_p_g_old.forEach(d -> {
							d.setSgname(data.getString("sg_name"));
							d.setSysstatus(sys_p.getSysstatus());
							d.setSgpermission(sys_p.getSgpermission());
							d.setSysmuser(user.getSuaccount());
							groupDao.save(d);
						});
						sg_name = sys_p.getSgname();
						// 父類別(限定修改) 還原
						sys_p.setSgpermission("0000000000");
						sys_p.setSysheader(true);
						sys_p.setSystemPermission(new SystemPermission(1l));
						sys_p.setSyssort(0);
						sys_p.setSysmuser(user.getSuaccount());
						groupDao.save(sys_p);
					} else {
						// 子類別
						if (!sg_name.equals("")) {
							sys_p.setSgname(sg_name);
						}
						groupDao.save(sys_p);
					}
					check = true;
				} else {
					// 如果 不是修改類 則
					check = false;
				}

			}
		} catch (Exception e) {
			System.out.println(e);
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
				SystemGroup sys_p = new SystemGroup();

				JSONObject data = (JSONObject) one;
				sys_p.setSgid(data.getLong("sg_id"));

				// 不得刪除自己
				if (data.getInt("sg_id") != user.getSusggid()) {
					if (data.getInt("sg_sp_id") == 0) {
						// 父類別 關聯全清除
						groupDao.deleteBySggid(data.getLong("sg_g_id"));
					}
				} else {
					return false;
				}

				groupDao.delete(sys_p);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
