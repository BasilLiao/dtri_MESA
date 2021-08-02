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
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationProgram;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkstationProgramService {
	@Autowired
	private WorkstationProgramDao programDao;
	@Autowired
	private WorkstationDao workstationDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
		ArrayList<Workstation> workstations = new ArrayList<Workstation>();
		JSONArray a_vals = new JSONArray();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("wpid").descending());
		String wp_name = null;
		String wp_c_name = null;
		String status = "0";
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wp_id", FFS.h_t("ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wp_g_id", FFS.h_t("群組[ID]", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wp_name", FFS.h_t("流程序[名稱]", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wp_c_name", FFS.h_t("流程序[代碼]", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wp_w_g_id", FFS.h_t("工作站[ID]", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "w_pb_name", FFS.h_t("工作站[名稱]", "150px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "w_c_name", FFS.h_t("工作站[代碼]", "150px", FFM.Wri.W_N));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "280px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_header", FFS.h_t("群組", "100px", FFM.Wri.W_N));

			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "wp_id", "ID"));

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "wp_g_id", "群組ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "wp_name", "流程序[名稱]"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", true, n_val, "wp_c_name", "流程序[代碼]"));

			workstations = workstationDao.findAllBySysheader(true, PageRequest.of(0, 999));
			workstations.forEach(w -> {
				if (w.getWgid() != 0)
					a_vals.put((new JSONObject()).put("value", w.getWpbname()).put("key", w.getWgid()));
			});

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "0", "0", FFM.Wri.W_Y, "col-md-2", true, a_vals, "wp_w_g_id", "工作站[ID]"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, " ", " ", FFM.Wri.W_N, "col-md-2", false, n_val, "w_pb_name", "工作站[名稱]"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, " ", " ", FFM.Wri.W_N, "col-md-2", false, n_val, "w_c_name", "工作站[代碼]"));

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.TTA, FFM.Type.TEXT, " ", " ", FFM.Wri.W_Y, "col-md-12", false, n_val, "sys_note", "備註"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", true, n_val, "sys_sort", "排序"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_N, "col-md-1", true, a_val, "sys_status", "狀態"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "false", "false", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_header", "群組"));
			bean.setCell_modify(obj_m);

			// 放入群主指定 [(key)](modify/Create/Delete) 格式
			JSONArray obj_g_m = new JSONArray();

			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_S, "col-md-1", "sys_sort", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-2", "wp_w_g_id", ""));

			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-2", "wp_name", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-1", "wp_c_name", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_N, "col-md-1", "w_c_name", ""));
			
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_N, "col-md-1", "sys_sort", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_N, "col-md-1", "sys_ver", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_Y, FFM.Dno.D_S, "col-md-1", "sys_status", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sys_header", "true"));

			bean.setCell_g_modify(obj_g_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "wp_c_name", "工作程序代號", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "wp_name", "工作程序名稱", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			wp_name = body.getJSONObject("search").getString("wp_name");
			wp_name = wp_name.equals("") ? null : wp_name;
			wp_c_name = body.getJSONObject("search").getString("wp_c_name");
			wp_c_name = wp_c_name.equals("") ? null : wp_c_name;
			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;
		}
		workstationPrograms = programDao.findAllByProgram(wp_name, wp_c_name, Integer.parseInt(status), page_r);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		workstationPrograms.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wp_id", one.getWpid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wp_g_id", one.getWpgid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wp_name", one.getWpname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wp_c_name", one.getWpcname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wp_w_g_id", one.getWpwgid());
			Workstation work = workstationDao.findAllByWgidOrderBySyssortAsc(one.getWpwgid()).get(0);
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "w_pb_name", work.getWpbname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "w_c_name", work.getWcname());

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
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		JSONArray list = body.getJSONArray("create");
		ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
		WorkstationProgram sys_wp_f = new WorkstationProgram();
		WorkstationProgram sys_wp = new WorkstationProgram();
		try {
			for (Object one : list) {
				// Step1. 物件_轉換
				JSONObject data = (JSONObject) one;

				// Step2.檢查
				// 檢查名稱重複?
				workstationPrograms = programDao.findAllByWpcnameOrWpname(data.getString("wp_c_name"), data.getString("wp_name"));
				if (workstationPrograms.size() > 0 && data.getBoolean("sys_header")) {
					return false;
				}

				// Step3.資料分類建置
				if (data.getBoolean("sys_header")) {
					// 父
					sys_wp_f = new WorkstationProgram();
					Long wpgid = programDao.getWorkstation_program_g_seq();
					sys_wp_f.setWpgid(wpgid);
					sys_wp_f.setWpcname(data.getString("wp_c_name"));
					sys_wp_f.setWpname(data.getString("wp_name"));
					sys_wp_f.setWpwgid(0l);
					sys_wp_f.setSyssort(0);
					sys_wp_f.setSysnote("");
					sys_wp_f.setSysstatus(0);
					sys_wp_f.setSysheader(true);
					sys_wp_f.setSysmuser(user.getSuaccount());
					sys_wp_f.setSyscuser(user.getSuaccount());
					programDao.save(sys_wp_f);
				} else {
					// 如果為子目錄 非群組
					if (!sys_wp_f.getSysheader()) {
						sys_wp_f = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getLong("wp_g_id"), true).get(0);
					}
					// 子
					sys_wp = new WorkstationProgram();
					sys_wp.setWpcname(sys_wp_f.getWpcname());
					sys_wp.setWpname(sys_wp_f.getWpname());
					sys_wp.setWpgid(sys_wp_f.getWpgid());
					sys_wp.setSysnote("");
					sys_wp.setSyssort(data.getInt("sys_sort"));
					sys_wp.setSysstatus(data.getInt("sys_status"));
					sys_wp.setWpwgid(data.getLong("wp_w_g_id"));
					sys_wp.setSysheader(false);
					sys_wp.setSysmuser(user.getSuaccount());
					sys_wp.setSyscuser(user.getSuaccount());
					programDao.save(sys_wp);
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
		JSONArray list = body.getJSONArray("save_as");
		ArrayList<WorkstationProgram> workstationPrograms = new ArrayList<WorkstationProgram>();
		WorkstationProgram sys_wp_f = new WorkstationProgram();
		WorkstationProgram sys_wp = new WorkstationProgram();
		try {
			for (Object one : list) {
				// Step1. 物件_轉換
				JSONObject data = (JSONObject) one;

				// Step2.檢查
				// 檢查名稱重複?
				workstationPrograms = programDao.findAllByWpcnameOrWpname(data.getString("wp_c_name"), data.getString("wp_name"));
				if (workstationPrograms.size() > 0 && data.getBoolean("sys_header")) {
					return false;
				}

				// Step3.資料分類建置
				if (data.getBoolean("sys_header")) {
					// 父
					sys_wp_f = new WorkstationProgram();
					Long wpgid = programDao.getWorkstation_program_g_seq();
					sys_wp_f.setWpgid(wpgid);
					sys_wp_f.setWpcname(data.getString("wp_c_name"));
					sys_wp_f.setWpname(data.getString("wp_name"));
					sys_wp_f.setWpwgid(0l);
					sys_wp_f.setSyssort(0);
					sys_wp_f.setSysnote("");
					sys_wp_f.setSysstatus(0);
					sys_wp_f.setSysheader(true);
					sys_wp_f.setSysmuser(user.getSuaccount());
					sys_wp_f.setSyscuser(user.getSuaccount());
					programDao.save(sys_wp_f);
				} else {
					// 子
					sys_wp = new WorkstationProgram();
					sys_wp.setWpcname(sys_wp_f.getWpcname());
					sys_wp.setWpname(sys_wp_f.getWpname());
					sys_wp.setWpgid(sys_wp_f.getWpgid());
					sys_wp.setSysnote("");
					sys_wp.setSyssort(data.getInt("sys_sort"));
					sys_wp.setSysstatus(data.getInt("sys_status"));
					sys_wp.setWpwgid(data.getLong("wp_w_g_id"));
					sys_wp.setSysheader(false);
					sys_wp.setSysmuser(user.getSuaccount());
					sys_wp.setSyscuser(user.getSuaccount());
					programDao.save(sys_wp);
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
			JSONArray list = body.getJSONArray("modify");
			WorkstationProgram sys_p_f = new WorkstationProgram();
			WorkstationProgram sys_p = new WorkstationProgram();

			for (Object one : list) {
				// 物件轉換
				JSONObject data = (JSONObject) one;
				// 父類別
				if (data.getBoolean("sys_header")) {
					// 檢查名稱重複?
					if (programDao.findAllByWpcnameAndWpcnameNot(data.getString("wp_c_name"), data.getString("wp_c_name")).size() > 0 || //
							programDao.findAllByWpnameAndWpnameNot(data.getString("wp_name"), data.getString("wp_name")).size() > 0) {
						return false;
					}
					sys_p_f = new WorkstationProgram();
					sys_p_f.setWpid(data.getLong("wp_id"));
					sys_p_f.setWpname(data.getString("wp_name"));
					sys_p_f.setWpgid(data.getLong("wp_g_id"));
					sys_p_f.setWpcname(data.getString("wp_c_name"));
					sys_p_f.setWpwgid(0l);
					sys_p_f.setSysnote("");
					sys_p_f.setSyssort(0);
					sys_p_f.setSysstatus(data.getInt("sys_status"));
					sys_p_f.setSysmuser(user.getSuaccount());
					sys_p_f.setSysmdate(new Date());
					sys_p_f.setSysheader(true);
					programDao.save(sys_p_f);

					// 更新子類別
					ArrayList<WorkstationProgram> sys_p_s = programDao.findAllByWpgidOrderBySyssortAsc(data.getLong("wp_g_id"));
					sys_p_s.forEach(wp -> {
						wp.setWpname(data.getString("wp_name"));
						wp.setWpcname(data.getString("wp_c_name"));
					});
					programDao.saveAll(sys_p_s);
				} else {
					sys_p = new WorkstationProgram();
					sys_p.setWpid(data.getLong("wp_id"));
					sys_p.setWpgid(sys_p_f.getWpgid());
					sys_p.setWpname(sys_p_f.getWpname());
					sys_p.setWpcname(sys_p_f.getWpcname());
					sys_p.setWpwgid(data.getLong("wp_w_g_id"));
					sys_p.setSysnote("");
					sys_p.setSyssort(data.getInt("sys_sort"));
					sys_p.setSysstatus(data.getInt("sys_status"));
					sys_p.setSysmuser(user.getSuaccount());
					sys_p.setSysmdate(new Date());
					programDao.save(sys_p);
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
				WorkstationProgram sys_p = new WorkstationProgram();
				JSONObject data = (JSONObject) one;
				// 群組移除
				if (data.getBoolean("sys_header")) {
					programDao.deleteByWpgid(data.getLong("wp_g_id"));
					continue;
				}
				sys_p.setWpid(data.getLong("wp_id"));
				programDao.deleteByWpidAndSysheader(sys_p.getWpid(), false);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
