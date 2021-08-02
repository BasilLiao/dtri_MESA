package dtri.com.tw.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.WorkHours;
import dtri.com.tw.db.entity.WorkType;
import dtri.com.tw.db.pgsql.dao.WorkHoursDao;
import dtri.com.tw.db.pgsql.dao.WorkTypeDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkHoursService {
	@Autowired
	private WorkHoursDao hoursDao;
	@Autowired
	private WorkTypeDao workTypeDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size, SystemUser user) {
		PackageBean bean = new PackageBean();
		List<WorkHours> workhours = new ArrayList<WorkHours>();
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("productionRecords").descending());
		// String wh_account = null;
		String status = "0";
		String wh_s_date = "";
		String wh_e_date = "";
		String wh_do = null;
		String wh_wt_id = "0";
		String wh_pr_id = null;
		JSONArray st_val = new JSONArray();
		ArrayList<WorkType> worktypes = workTypeDao.findAll();
		worktypes.forEach(s -> {
			st_val.put((new JSONObject()).put("value", s.getWtname()).put("key", s.getWtid()));
		});
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// page_r = PageRequest.of(page, 999, Sort.by("whgid").descending());

			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_id", FFS.h_t("ID", "50px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_pr_id", FFS.h_t("製令單", "200px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_wt_id", FFS.h_t("類型", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_wt_name", FFS.h_t("類型名稱", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_do", FFS.h_t("工作內容", "350px", FFM.Wri.W_N));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_nb", FFS.h_t("已完成", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_p_quantity", FFS.h_t("需完成", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_account", FFS.h_t("作業人員", "150px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_s_date", FFS.h_t("開始時間", "180px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "wh_e_date", FFS.h_t("結束時間", "180px", FFM.Wri.W_N));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_header", FFS.h_t("群組代表", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));

			bean.setHeader(object_header);

			// 放入修改 [m__(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray s_val = new JSONArray();
			JSONArray n_val = new JSONArray();

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "wh_id", "ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_header", "群組代表"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "wh_pr_id", "製令單"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", true, st_val, "wh_wt_id", "工作類型"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-4", true, n_val, "wh_do", "工作內容"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.DATE, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "wh_s_date", "時間(始)"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.DATE, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "wh_e_date", "時間(結)"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "", "", FFM.Wri.W_Y, "col-md-1", true, n_val, "wh_nb", "完成數量"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "", "", FFM.Wri.W_N, "col-md-1", true, n_val, "pr_p_quantity", "總數"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-1", true, n_val, "wh_account", "作業人員"));

			s_val = new JSONArray();
			s_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			s_val.put((new JSONObject()).put("value", "取消").put("key", "1"));
			s_val.put((new JSONObject()).put("value", "鎖定").put("key", "2").put("dis", "disabled"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "0", FFM.Wri.W_Y, "col-md-1", true, s_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入群主指定 [(key)](modify/Create/Delete) 格式
			JSONArray obj_g_m = new JSONArray();
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "sys_status", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_s_date", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_e_date", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_do", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_account", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_nb", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_N, "col-md-1", "wh_wt_id", ""));
			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_S, "col-md-1", "wh_pr_id", ""));

			obj_g_m.put(FFS.h_g(FFM.Wri.W_N, FFM.Dno.D_S, "col-md-1", "pr_p_quantity", ""));

			bean.setCell_g_modify(obj_g_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			// object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2",
			// "wh_account", "作業員", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "wh_pr_id", "製令單", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "", "col-md-1", "wh_wt_id", "工作類型", st_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "wh_do", "事情內容", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.DATE, "", "col-md-2", "wh_s_date", "工時(起)", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.DATE, "", "col-md-2", "wh_e_date", "工時(終)", n_val));

			s_val = new JSONArray();
			s_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			s_val.put((new JSONObject()).put("value", "取消").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-1", "sys_status", "狀態", s_val));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
//			wh_account = body.getJSONObject("search").getString("wh_account");
//			wh_account = wh_account.equals("") ? null : wh_account;

			wh_e_date = body.getJSONObject("search").getString("wh_e_date");
			// wh_e_date = wh_e_date.equals("") ? null : wh_e_date;

			wh_s_date = body.getJSONObject("search").getString("wh_s_date");
			// wh_s_date = wh_s_date.equals("") ? null : wh_s_date;

			status = body.getJSONObject("search").getString("sys_status");
			status = status.equals("") ? "0" : status;

			wh_wt_id = body.getJSONObject("search").getString("wh_wt_id");
			wh_wt_id = wh_wt_id.equals("") ? "0" : wh_wt_id;

			wh_do = body.getJSONObject("search").getString("wh_do");
			wh_do = wh_do.equals("") ? null : wh_do;

			wh_pr_id = body.getJSONObject("search").getString("wh_pr_id");
			wh_pr_id = wh_pr_id.equals("") ? null : wh_pr_id;

		}

		Date s_date = wh_s_date.equals("") ? null : Fm_Time.toDateTime(wh_s_date);
		Date e_date = wh_e_date.equals("") ? null : Fm_Time.toDateTime(wh_e_date);
		// workhours = hoursDao.findAll();
		workhours = hoursDao.findAllByWorkHours(wh_pr_id/* ,wh_account */, wh_do, Integer.parseInt(wh_wt_id), Integer.parseInt(status), s_date,
				e_date, page_r);
		List<String> pr_id = new ArrayList<String>();

		// 避免重複
		String prid = "";
		for (WorkHours one : workhours) {
			if (!prid.equals(one.getProductionRecords().getPrid())) {
				pr_id.add(one.getProductionRecords().getPrid());
			}
		}
		workhours = hoursDao.findAllByWorkHours(pr_id);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		workhours.forEach(one -> {
			JSONObject object_body = new JSONObject();
			int ord = 0;

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_id", one.getWhid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_pr_id", one.getProductionRecords().getPrid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_wt_id", one.getWhwtid().getWtid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_wt_name", one.getWhwtid().getWtname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_do", one.getWhdo());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_nb", one.getWhnb());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_account", one.getWhaccount());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_s_date", one.getWhsdate() == null ? "" : Fm_Time.to_yMd_Hms(one.getWhsdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "wh_e_date", one.getWhedate() == null ? "" : Fm_Time.to_yMd_Hms(one.getWhedate()));

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_header", one.getSysheader());
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
		// 是否為群組模式? type:[group/general] || 新增時群組? createOnly:[all/general]
		bean.setBody_type(FFS.group_set(FFM.Group_type.group, FFM.Group_createOnly.general));
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");
			List<WorkHours> works_p = new ArrayList<WorkHours>();
			ProductionRecords wh_pr_id = new ProductionRecords();
			String pr_id = "";

			// 重製
			works_p = new ArrayList<WorkHours>();
			int total_tw = 0;
			for (Object one : list) {
				// 物件轉換
				WorkHours work_p = new WorkHours();
				JSONObject data = (JSONObject) one;
				WorkType type = new WorkType();
				// 驗證是否有此製令
				total_tw = 0;
				if (pr_id.equals("") || !data.getString("wh_pr_id").equals(pr_id)) {
					pr_id = data.getString("wh_pr_id");
					wh_pr_id = new ProductionRecords();
					wh_pr_id.setPrid(pr_id);
					total_tw = 0;
				} else {
					// return false;
				}

				// 檢核總數量
				List<WorkHours> check_wts = hoursDao.findAllByWorkHours(data.getString("wh_pr_id"), null, data.getInt("wh_wt_id"), 0, null, null,
						PageRequest.of(0, 9999));
				for (WorkHours workHours : check_wts) {
					total_tw += workHours.getWhnb();
				}
				total_tw += data.getInt("wh_nb");
				if (total_tw > check_wts.get(0).getProductionRecords().getPrpquantity()) {
					return false;
				}
				// 檢核數量
				if (data.getInt("wh_nb") > check_wts.get(0).getProductionRecords().getPrpquantity()) {
					return false;
				}

				// 檢核使用者
				/*
				 * String check_user =
				 * hoursDao.findAllByWhid(data.getInt("wh_id")).get(0).getSysmuser(); if
				 * (!check_user.equals("system") && !check_user.equals(user.getSuaccount())) {
				 * return false; }
				 */

				type.setWtid(data.getLong("wh_wt_id"));

				work_p.setProductionRecords(wh_pr_id);
				work_p.setWhwtid(type);
				work_p.setWhdo(data.getString("wh_do"));

				work_p.setWhnb(data.getInt("wh_nb"));
				work_p.setWhaccount(data.getString("wh_account"));
				work_p.setWhsdate(data.getString("wh_s_date").equals("") ? null : Fm_Time.toDateTime(data.getString("wh_s_date")));
				work_p.setWhedate(data.getString("wh_e_date").equals("") ? null : Fm_Time.toDateTime(data.getString("wh_e_date")));

				work_p.setSysnote("");
				work_p.setSyssort(0);
				work_p.setSysheader(data.getBoolean("sys_header"));
				work_p.setSysstatus(0);
				work_p.setSysmuser(user.getSuaccount());
				work_p.setSyscuser(user.getSuaccount());

				works_p.add(work_p);
				check = true;

			}
			hoursDao.saveAll(works_p);

			// 檢核數量 ->是否達標最低數量->達標則更新數量
			ArrayList<WorkType> w_all = workTypeDao.findAll();
			Map<Long, Integer> all_type = new HashMap<Long, Integer>();

			for (WorkType workType : w_all) {
				if (workType.getWtid() != 0) {
					works_p = hoursDao.findAllByproductionRecordsAndWorkTypeAndSysstatus(wh_pr_id, workType, 0);
					int total_nb = 0;// 暫時登記數量
					for (WorkHours workHours : works_p) {
						total_nb += workHours.getWhnb();
					}
					all_type.put(workType.getWtid(), total_nb);
				}
			}
			int last_nb = 99999;
			for (Map.Entry<Long, Integer> entry : all_type.entrySet()) {
				if (last_nb > entry.getValue()) {
					last_nb = entry.getValue();
				}
			}
			// System.out.println(last_nb);
			int save_nb = last_nb;
			works_p = hoursDao.findAllByproductionRecords(wh_pr_id);
			works_p.forEach(s -> {
				if (s.getSysheader()) {
					s.setWhnb(save_nb);
					hoursDao.save(s);
				}
			});
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
//			String sg_name = "";
//			Integer sg_g_id = 0;
//			
			if (list.length() == 0) {
				check = true;
			}
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
			String pr_id = "";
			// Integer sg_g_id = 0;
			JSONArray list = body.getJSONArray("modify");
			List<WorkHours> works_p = new ArrayList<WorkHours>();
			ProductionRecords wh_pr_id = new ProductionRecords();

			for (Object one : list) {
				// 物件轉換
				JSONObject data = (JSONObject) one;
				WorkType type = new WorkType();
				WorkHours work_p = hoursDao.findAllByWhid(data.getLong("wh_id")).get(0);
				// 驗證是否有此製令
				if (pr_id.equals("") || !data.getString("wh_pr_id").equals(pr_id)) {
					pr_id = data.getString("wh_pr_id");
					wh_pr_id = new ProductionRecords();
					wh_pr_id.setPrid(pr_id);
				}
				// 主類別不改+不是鎖定資料
				if (!data.getBoolean("sys_header") && work_p.getSysstatus() != 2) {
					// 檢核總數量
					List<WorkHours> check_wts = hoursDao.findAllByWorkHours(data.getString("wh_pr_id"), null, data.getInt("wh_wt_id"), 0, null, null,
							PageRequest.of(0, 9999));
					int total_tw = 0;
					for (WorkHours workHours : check_wts) {
						if (workHours.getWhid() == data.getInt("wh_id")) {
							total_tw += data.getInt("wh_nb");
						} else {
							total_tw += workHours.getWhnb();
						}
					}
					if (total_tw > check_wts.get(0).getProductionRecords().getPrpquantity()) {
						return false;
					}
					// 檢核數量
					if (data.getInt("wh_nb") > check_wts.get(0).getProductionRecords().getPrpquantity()) {
						return false;
					}

					// 檢核使用者
					/*
					 * String check_user =
					 * hoursDao.findAllByWhid(data.getInt("wh_id")).get(0).getSysmuser(); if
					 * (!check_user.equals("system") && !check_user.equals(user.getSuaccount())) {
					 * return false; }
					 */

					type.setWtid(data.getLong("wh_wt_id"));

					work_p.setWhid(data.getLong("wh_id"));
					work_p.setProductionRecords(wh_pr_id);
					work_p.setWhwtid(type);
					work_p.setWhdo(data.getString("wh_do"));

					work_p.setWhnb(data.getInt("wh_nb"));
					work_p.setWhaccount(data.getString("wh_account"));
					work_p.setWhsdate(data.getString("wh_s_date").equals("") ? null : Fm_Time.toDateTime(data.getString("wh_s_date")));
					work_p.setWhedate(data.getString("wh_e_date").equals("") ? null : Fm_Time.toDateTime(data.getString("wh_e_date")));

					work_p.setSysnote("");
					work_p.setSyssort(0);
					work_p.setSysheader(data.getBoolean("sys_header"));
					work_p.setSysstatus(data.getInt("sys_status"));
					work_p.setSysmuser(user.getSuaccount());
					work_p.setSysmdate(new Date());

					check = true;

					works_p.add(work_p);
				}
			}
			hoursDao.saveAll(works_p);

			// 檢核數量 ->是否達標最低數量->達標則更新數量
			ArrayList<WorkType> w_all = workTypeDao.findAll();
			Map<Long, Integer> all_type = new HashMap<Long, Integer>();

			for (WorkType workType : w_all) {
				if (workType.getWtid() != 0) {
					works_p = hoursDao.findAllByproductionRecordsAndWorkTypeAndSysstatus(wh_pr_id, workType, 0);
					int total_nb = 0;// 暫時登記數量
					for (WorkHours workHours : works_p) {
						total_nb += workHours.getWhnb();
					}
					all_type.put(workType.getWtid(), total_nb);
				}
			}
			int last_nb = 99999;
			for (Map.Entry<Long, Integer> entry : all_type.entrySet()) {
				if (last_nb > entry.getValue()) {
					last_nb = entry.getValue();
				}
			}
			// System.out.println(last_nb);
			int save_nb = last_nb;
			works_p = hoursDao.findAllByproductionRecords(wh_pr_id);
			works_p.forEach(s -> {
				if (s.getSysheader()) {
					s.setWhnb(save_nb);
					hoursDao.save(s);
				}
			});

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
				WorkHours work_p = new WorkHours();
				JSONObject data = (JSONObject) one;
				// 排除 移除群組
				if (data.getBoolean("sys_header")) {
					return false;
				} else {
					// 子類別
					// 排除鎖定
					if (data.getInt("sys_status") != 2) {
						work_p.setWhid(data.getLong("wh_id"));
						hoursDao.delete(work_p);
					}
				}
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}