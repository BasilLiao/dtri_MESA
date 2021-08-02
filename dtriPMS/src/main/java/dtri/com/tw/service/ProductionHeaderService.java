package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.ProductionHeader;
import dtri.com.tw.db.entity.ProductionRecords;
import dtri.com.tw.db.entity.ProductionSN;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.entity.WorkHours;
import dtri.com.tw.db.entity.WorkType;
import dtri.com.tw.db.entity.Workstation;
import dtri.com.tw.db.entity.WorkstationProgram;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.db.pgsql.dao.ProductionHeaderDao;
import dtri.com.tw.db.pgsql.dao.ProductionSNDao;
import dtri.com.tw.db.pgsql.dao.WorkHoursDao;
import dtri.com.tw.db.pgsql.dao.WorkTypeDao;
import dtri.com.tw.db.pgsql.dao.WorkstationDao;
import dtri.com.tw.db.pgsql.dao.WorkstationProgramDao;
import dtri.com.tw.tools.Fm_SN;
import dtri.com.tw.tools.Fm_Time;

@Service
public class ProductionHeaderService {
	@Autowired
	private ProductionHeaderDao productionHeaderDao;

	@Autowired
	private WorkstationProgramDao programDao;

	@Autowired
	private WorkstationDao workDao;

	@Autowired
	private ProductionBodyDao productionBodyDao;

	@Autowired
	private ProductionSNDao snDao;

	@Autowired
	private WorkTypeDao typeDao;

	@Autowired
	private WorkHoursDao hoursDao;

	@Autowired
	EntityManager em;

	// 取得當前 資料清單

	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		List<ProductionHeader> productionHeaders = new ArrayList<ProductionHeader>();
		ProductionBody body_one = productionBodyDao.findAllByPbid(0l).get(0);
		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		PageRequest page_r = PageRequest.of(page, p_size, Sort.by("phid").descending());
		String phmodel = "";
		String phprid = "";
		String sysstatus = "0";
		String pb_sn_value = "";
		String pb_sn_name = "";
		String pb_sn = "";
		String pr_order_id = "";
		String pr_c_name = "";
		String pr_bom_id = "";
		String pr_b_item = "";
		String pr_s_item = "";
		List<Long> pbid = new ArrayList<Long>();
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			// header
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_id", FFS.h_t("ID", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_pb_g_id", FFS.h_t("SN_G_ID", "130px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_type", FFS.h_t("製令類", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_pr_id", FFS.h_t("製令單號", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_p_number", FFS.h_t("驗證號[Part No]", "160px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_p_name", FFS.h_t("產品號[Product Name]", "200px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_wp_id", FFS.h_t("工作程序", "150px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_s_date", FFS.h_t("開始時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_e_date", FFS.h_t("結束時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ph_schedule", FFS.h_t("進度(X／X)", "130px", FFM.Wri.W_Y));
			// 規格
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_order_id", FFS.h_t("訂單編號", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_c_name", FFS.h_t("客戶名稱", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_bom_id", FFS.h_t("BOM料號", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_c_from", FFS.h_t("單據來源", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_p_model", FFS.h_t("產品型號", "150px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_b_item", FFS.h_t("規格定義", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_s_item", FFS.h_t("軟體定義", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_p_quantity", FFS.h_t("需產數", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_h_ok_quantity", FFS.h_t("加工完成數", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_p_ok_quantity", FFS.h_t("完成數", "150px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_s_sn", FFS.h_t("產品序號(開始)", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_e_sn", FFS.h_t("產品序號(結束)", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_s_b_sn", FFS.h_t("燒錄序號(開始)", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_e_b_sn", FFS.h_t("燒錄序號(結束)", "150px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pr_w_years", FFS.h_t("保固", "100px", FFM.Wri.W_Y));

			// sn 設定
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_1", FFS.h_t("P(SN)機種", "110px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_2", FFS.h_t("P(SN)廠別", "110px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_3", FFS.h_t("P(SN)保固", "110px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_4", FFS.h_t("P(SN)週期", "110px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_5", FFS.h_t("P(SN)面板", "110px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "ps_sn_6", FFS.h_t("P(SN)流水", "110px", FFM.Wri.W_Y));

			// sys
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "180px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px", FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px", FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px", FFM.Wri.W_N));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px", FFM.Wri.W_Y));

			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();
			// header
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "ph_id", "ID"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-1", false, n_val, "ph_pb_g_id", "S_GN_ID"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "一般製令_No(sn)").put("key", "A511_no_sn"));
			a_val.put((new JSONObject()).put("value", "一般製令_Both(sn)").put("key", "A511_no_and_has_sn"));
			a_val.put((new JSONObject()).put("value", "一般製令_Create(sn)").put("key", "A511_has_sn"));

			a_val.put((new JSONObject()).put("value", "重工製令_No(sn)").put("key", "A521_no_sn"));
			a_val.put((new JSONObject()).put("value", "重工製令_Both(sn)").put("key", "A521_no_and_has_sn"));
			a_val.put((new JSONObject()).put("value", "重工製令_Create(sn)").put("key", "A521_has_sn"));

			a_val.put((new JSONObject()).put("value", "維護製令_No(sn)").put("key", "A522_service"));
			a_val.put((new JSONObject()).put("value", "拆解製令_No(sn)").put("key", "A431_disassemble"));
			a_val.put((new JSONObject()).put("value", "委外製令_No(sn)").put("key", "A512_outside"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "A511", "A511", FFM.Wri.W_Y, "col-md-2", true, a_val, "ph_type", "製令類"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "ph_pr_id", "製令單號"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", false, n_val, "ph_p_number", "驗證號[Part No]"));

			ArrayList<WorkstationProgram> list_p = programDao.findAllBySysheader(true);
			JSONArray a_val_p = new JSONArray();
			list_p.forEach(p -> {
				a_val_p.put((new JSONObject()).put("value", p.getWpname()).put("key", p.getWpgid()));
			});
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, a_val_p, "ph_wp_id", "工作程序"));

			// 規格-ProductionRecords
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "pr_p_model", "產品型號"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "pr_bom_id", "BOM料號"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "pr_order_id", "訂單編號"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", true, n_val, "pr_c_name", "客戶名稱"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "13碼", "", FFM.Wri.W_N, "col-md-2", false, n_val, "pr_s_sn", "產品SN_開始"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "13碼", "", FFM.Wri.W_N, "col-md-2", false, n_val, "pr_e_sn", "產品SN_結束"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "pr_s_b_sn", "燒錄SN_開始"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "pr_e_b_sn", "燒錄SN_結束"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-6", true, n_val, "pr_b_item", "規格定義"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-6", true, n_val, "pr_s_item", "軟體定義"));
			// sys
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", true, n_val, "pr_p_quantity", "需產數"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-1", true, n_val, "pr_p_ok_quantity", "完成產品數"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0",
			// FFM.Wri.W_N, "col-md-1", true, n_val, "pr_h_ok_quantity", "完成加工數"));

			// sn規格定義(固定範圍)
			JSONArray a_val_1 = new JSONArray();
			JSONArray a_val_2 = new JSONArray();
			JSONArray a_val_3 = new JSONArray();
			String a_val_4 = "0000";
			JSONArray a_val_5 = new JSONArray();
			String a_val_6 = "000";
			ArrayList<ProductionSN> p_sn = snDao.findAllBySysheaderOrderByPsgidAsc(false);
			for (ProductionSN productionSN : p_sn) {
				switch (productionSN.getPsgid().toString()) {
				case "1":
					a_val_1.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case "2":
					a_val_2.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case "3":
					a_val_3.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case "4":
					a_val_4 = productionSN.getPsvalue();
					break;
				case "5":
					a_val_5.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
					break;
				case "6":
					a_val_6 = productionSN.getPsvalue();
					break;
				default:
					break;
				}
			}
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-1", true, a_val_1, "ps_sn_1", "SN_機種"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-1", true, a_val_2, "ps_sn_2", "SN_廠別"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-1", true, a_val_3, "ps_sn_3", "SN_保固"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, a_val_4, a_val_4, FFM.Wri.W_N, "col-md-1", true, n_val, "ps_sn_4", "SN_週期"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-1", true, a_val_5, "ps_sn_5", "SN_面板"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, a_val_6, a_val_6, FFM.Wri.W_Y, "col-md-1", true, n_val, "ps_sn_6", "SN_流水號"));

			String psf = "無客製化SN,請空白";
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, psf, "", FFM.Wri.W_Y, "col-md-2", false, n_val, "ps_b_f_sn", "SN_燒錄序號(固定)"));//
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-2", false, n_val, "ps_b_sn", "SN_燒錄序號(流水)"));//

			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-1", false, n_val, "sys_ver", "版本"));
			obj_m.put(FFS.h_m(FFM.Dno.D_N, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_N, "col-md-1", true, n_val, "sys_sort", "排序"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.TTA, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-12", false, n_val, "sys_note", "備註"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "ph_schedule", "進度"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", false, n_val, "pr_c_from", "單據來源"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "ph_s_date", "開始(時)"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_N, "col-md-2", true, n_val, "ph_e_date", "結束(時)"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "", FFM.Wri.W_Y, "col-md-2", false, n_val, "ph_p_name", "產品號[Product Name]"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, "0", "0", FFM.Wri.W_Y, "col-md-1", false, n_val, "pr_w_years", "保固年"));
			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "待命中").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "生產中").put("key", "1"));
			a_val.put((new JSONObject()).put("value", "已完成").put("key", "2"));
			a_val.put((new JSONObject()).put("value", "暫停中").put("key", "8"));
			a_val.put((new JSONObject()).put("value", "已終止").put("key", "9"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.SEL, FFM.Type.TEXT, "0", "0", FFM.Wri.W_Y, "col-md-1", true, a_val, "sys_status", "狀態"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_date", "修改時間"));
			// obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.TEXT, "", "",
			// FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_user", "修改人"));

			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			// 製令查詢
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "ph_pr_id", "製令單號", n_val));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "sys_status", "狀態", a_val));
			// 規格查詢
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_p_model", "產品型號", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_order_id", "訂單號", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_c_name", "客戶名稱", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_bom_id", "BOM料號", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_b_item", "軟體定義", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pr_s_item", "規格定義", n_val));

			// 項目查詢(選單)

			a_val = new JSONArray();
			for (int j = 0; j < 50; j++) {
				String m_name = "getPbvalue" + String.format("%02d", j + 1);
				try {
					Method method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String name = String.format("pb_value" + String.format("%02d", j + 1));
					if (value != null && !value.equals("")) {
						a_val.put((new JSONObject()).put("value", value).put("key", name));
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			object_searchs.put(FFS.h_s(FFM.Tag.SEL, FFM.Type.TEXT, "0", "col-md-2", "pb_sn_name", "SN_料件類型", a_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "0", "col-md-2", "pb_sn_value", "SN_料件序號", n_val));
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pb_sn", "SN_產品序號", n_val));

			bean.setCell_searchs(object_searchs);
		} else {
			// 進行-特定查詢
			phmodel = body.getJSONObject("search").getString("pr_p_model");
			phmodel = (phmodel.equals("")) ? null : phmodel;

			phprid = body.getJSONObject("search").getString("ph_pr_id");
			phprid = (phprid.equals("")) ? null : phprid;

			sysstatus = body.getJSONObject("search").getString("sys_status");
			sysstatus = (sysstatus.equals("")) ? "0" : sysstatus;

			pb_sn = body.getJSONObject("search").getString("pb_sn");
			pb_sn = (pb_sn == null) ? "" : pb_sn;

			pb_sn_name = body.getJSONObject("search").getString("pb_sn_name");
			pb_sn_name = pb_sn_name == null ? "" : pb_sn_name;

			pb_sn_value = body.getJSONObject("search").getString("pb_sn_value");
			pb_sn_value = pb_sn_value == null ? "" : pb_sn_value;

			if (pb_sn_name.equals("") || pb_sn_value.equals("")) {
				pb_sn_value = "";
				pb_sn_name = "";
			}
			pr_order_id = body.getJSONObject("search").getString("pr_order_id");
			pr_order_id = (pr_order_id.equals("")) ? null : pr_order_id;

			pr_c_name = body.getJSONObject("search").getString("pr_c_name");
			pr_c_name = (pr_c_name.equals("")) ? null : pr_c_name;

			pr_bom_id = body.getJSONObject("search").getString("pr_bom_id");
			pr_bom_id = (pr_bom_id.equals("")) ? null : pr_bom_id;

			pr_b_item = body.getJSONObject("search").getString("pr_b_item");
			pr_b_item = (pr_b_item.equals("")) ? null : pr_b_item;

			pr_s_item = body.getJSONObject("search").getString("pr_s_item");
			pr_s_item = (pr_s_item.equals("")) ? null : pr_s_item;
		}
		// 產品型號+製令單號
		String nativeQuery = "SELECT b.pb_g_id FROM production_body b join production_header h on b.pb_g_id = h.ph_pb_g_id  WHERE ";
		if (!pb_sn_value.equals("")) {
			nativeQuery += " (:pb_sn_value='' or " + pb_sn_name + " LIKE :pb_sn_value) and ";
		}
		nativeQuery += " (:pb_sn='' or b.pb_sn LIKE :pb_sn) and ";
		nativeQuery += " (b.pb_g_id != 0) group by b.pb_g_id ";
		// nativeQuery += " order by b.pb_g_id limit " + p_size + " offset " + page + "
		// ";
		Query query = em.createNativeQuery(nativeQuery);
		if (!pb_sn_value.equals("")) {
			query.setParameter("pb_sn_value", "%" + pb_sn_value + "%");
		}
		query.setParameter("pb_sn", "%" + pb_sn + "%");
		// 轉換LONG
		List<BigInteger> pbid_obj = query.getResultList();
		for (BigInteger obj : pbid_obj) {
			String one = obj.toString();
			pbid.add(Long.parseLong(one));
		}
		// 如果有查SN 則要有值/沒查則pass
		if ((!pb_sn_value.equals("") && pbid.size() > 0) || pb_sn_value.equals("")) {
			productionHeaders = productionHeaderDao.findAllByProductionHeader(phmodel, phprid, Integer.parseInt(sysstatus), pbid, pr_order_id,
					pr_c_name, pr_bom_id, pr_b_item, pr_s_item, page_r);
		}
		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		JSONObject object_bodys_all = new JSONObject();

		productionHeaders.forEach(one -> {
			JSONObject object_body = new JSONObject();
			// header
			int ord = 0;
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_id", one.getPhid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_pb_g_id", one.getPhpbgid());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_type", one.getPhtype());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_pr_id", one.getProductionRecords().getPrid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_p_number", one.getPhpnumber() == null ? "" : one.getPhpnumber());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_p_name", one.getPhpname() == null ? "" : one.getPhpname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_wp_id", one.getPhwpid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_s_date", one.getPhsdate() == null ? "" : Fm_Time.to_yMd_Hms(one.getPhsdate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_e_date", one.getPhedate() == null ? "" : Fm_Time.to_yMd_Hms(one.getPhedate()));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ph_schedule", one.getPhschedule());

			// 規格-ProductionRecords
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_order_id", one.getProductionRecords().getProrderid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_c_name", one.getProductionRecords().getPrcname());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_bom_id", one.getProductionRecords().getPrbomid());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_c_from", one.getProductionRecords().getPrcfrom());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_p_model", one.getProductionRecords().getPrpmodel());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_b_item", one.getProductionRecords().getPrbitem());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_s_item", one.getProductionRecords().getPrsitem());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_p_quantity", one.getProductionRecords().getPrpquantity());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_h_ok_quantity", one.getProductionRecords().getPrhokquantity());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_p_ok_quantity", one.getProductionRecords().getPrpokquantity());

			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_s_sn", one.getProductionRecords().getPrssn());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_e_sn", one.getProductionRecords().getPresn());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_s_b_sn", one.getProductionRecords().getPrsbsn());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_e_b_sn", one.getProductionRecords().getPrebsn());
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pr_w_years", one.getProductionRecords().getPrwyears());
			// SN設定
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_1", one.getProductionRecords().getPrssn().subSequence(0, 3));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_2", one.getProductionRecords().getPrssn().subSequence(3, 4));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_3", one.getProductionRecords().getPrssn().subSequence(4, 5));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_4", one.getProductionRecords().getPrssn().subSequence(5, 9));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_5", one.getProductionRecords().getPrssn().subSequence(9, 10));
			object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "ps_sn_6", one.getProductionRecords().getPrssn().subSequence(10, 13));

			// sys
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
		object_bodys_all.put("search", object_bodys);

		// 刷新修改畫面 [(key)](modify/Create/Delete) 格式
		JSONArray obj_m = new JSONArray();
		JSONArray n_val = new JSONArray();

		// sn規格定義(固定範圍)
		JSONArray a_val_1 = new JSONArray();
		JSONArray a_val_2 = new JSONArray();
		JSONArray a_val_3 = new JSONArray();
		String a_val_4 = "0000";
		JSONArray a_val_5 = new JSONArray();
		String a_val_6 = "000";
		ArrayList<ProductionSN> p_sn = snDao.findAllBySysheaderOrderByPsgidAsc(false);
		for (ProductionSN productionSN : p_sn) {
			switch (productionSN.getPsgid().toString()) {
			case "1":
				a_val_1.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case "2":
				a_val_2.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case "3":
				a_val_3.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case "4":
				a_val_4 = productionSN.getPsvalue();
				break;
			case "5":
				a_val_5.put((new JSONObject()).put("value", productionSN.getPsname()).put("key", productionSN.getPsvalue()));
				break;
			case "6":
				a_val_6 = productionSN.getPsvalue();
				break;
			default:
				break;
			}
		}
		// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "",
		// FFM.Wri.W_Y,
		// "col-md-1", true, a_val_1, "ps_sn_1", "P_SN_機種"));
		// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "",
		// FFM.Wri.W_Y,
		// "col-md-1", true, a_val_2, "ps_sn_2", "P_SN_廠別"));
		// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "",
		// FFM.Wri.W_Y,
		// "col-md-1", true, a_val_3, "ps_sn_3", "P_SN_保固"));
		obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, a_val_4, a_val_4, FFM.Wri.W_N, "col-md-1", true, n_val, "ps_sn_4", "P_SN_週期"));
		// obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "",
		// FFM.Wri.W_Y,
		// "col-md-1", true, a_val_5, "ps_sn_5", "P_SN_面板"));
		obj_m.put(FFS.h_m(FFM.Dno.D_S, FFM.Tag.INP, FFM.Type.NUMB, a_val_6, a_val_6, FFM.Wri.W_N, "col-md-1", true, n_val, "ps_sn_6", "P_SN_流水"));
		bean.setCell_refresh(obj_m);

		bean.setBody(object_bodys_all);

		// 是否為群組模式? type:[group/general] || 新增時群組? createOnly:[all/general]
		// bean.setBody_type(new JSONObject("{'type':'group','createOnly':'general'}"));
		return bean;
	}

	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
			JSONArray list = body.getJSONArray("create");

			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				// ProductionBody pro_b = new ProductionBody();
				ProductionRecords pro_r = new ProductionRecords();
				ArrayList<ProductionSN> pro_sn = new ArrayList<ProductionSN>();
				JSONObject data = (JSONObject) one;

				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByProductionRecords(search);

				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					// header
					// int id_h = productionHeaderDao.getProductionHeaderSeq();
					// int id_b = productionBodyDao.getProductionBodySeq();
					Long id_b_g2 = 1l;
					// 工作站資訊
					JSONObject json_work = new JSONObject();
					ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getLong("ph_wp_id"), false);
					for (WorkstationProgram p_one : programs) {
						ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
						JSONObject json_one = new JSONObject();
						json_one.put("name", works.get(0).getWpbname());
						json_one.put("type", "N");
						json_one.put("id", works.get(0).getWid());
						json_one.put("w_pb_cell", works.get(0).getWpbcell());
						json_one.put("sort", p_one.getSyssort());
						json_work.put(works.get(0).getWcname(), json_one);
					}
					// 無SN不需要指定新建
					JSONArray sn_lists = new JSONArray();
					if (data.getString("ph_type").equals("A511_no_and_has_sn") || data.getString("ph_type").equals("A511_has_sn")) {

						// 檢核SN選項
						if (data.getString("ps_sn_1").equals("no_") || //
								data.getString("ps_sn_2").equals("s") || //
								data.getString("ps_sn_3").equals("n") || //
								data.getString("ps_sn_5").equals("n")) {
							return false;
						}

						// sn
						pro_sn = new ArrayList<ProductionSN>();
						ProductionSN pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_1"));
						pro_sn_one.setPsgid(1L);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_2"));
						pro_sn_one.setPsgid(2l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_3"));
						pro_sn_one.setPsgid(3l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_4"));
						pro_sn_one.setPsgid(4l);
						pro_sn_one.setPsname("[YYWW]");
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_5"));
						pro_sn_one.setPsgid(5l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_6"));
						pro_sn_one.setPsgid(6l);
						pro_sn_one.setPsname("[000]");
						pro_sn.add(pro_sn_one);
						JSONObject sn_list = Fm_SN.analyze_Sn(pro_sn, true, data.getInt("pr_p_quantity"));

						// 檢核_區間是否用過
						String sn_f = sn_list.getJSONArray("sn_list").get(0).toString();
						String sn_e = sn_list.getJSONArray("sn_list").get(sn_list.getJSONArray("sn_list").length() - 1).toString();
						if (productionBodyDao.findAllByPbsn(sn_f).size() > 0 || productionBodyDao.findAllByPbsn(sn_e).size() > 0) {
							return false;
						}

						// 更新SN區段
						ProductionSN pro_sn_YYMM = snDao.findAllByPsid(11L).get(0);
						pro_sn_YYMM.setPsvalue(sn_list.getString("sn_YYWW"));
						ProductionSN pro_sn_SN = snDao.findAllByPsid(16L).get(0);
						pro_sn_SN.setPsvalue(sn_list.getString("sn_000"));
						snDao.save(pro_sn_SN);
						snDao.save(pro_sn_YYMM);

						sn_lists = sn_list.getJSONArray("sn_list");

						Long id_b_g = productionBodyDao.getProductionBodyGSeq();
						List<ProductionBody> pro_bs = new ArrayList<ProductionBody>();

						for (int i = 0; i < sn_lists.length(); i++) {
							// body
							ProductionBody pro_b = new ProductionBody();
							pro_b.setSysver(0);
							pro_b.setPbgid(id_b_g);
							pro_b.setSysheader(false);
							pro_b.setPbsn(sn_lists.getString(i));
							if (data.getString("ps_b_f_sn") != null && !data.getString("ps_b_f_sn").equals("") && data.getInt("ps_b_sn") >= 0) {
								String pr_e_b_sn = data.getString("ps_b_f_sn")
										+ String.format("%0" + data.getString("ps_b_sn").length() + "d", (data.getInt("ps_b_sn") + i));
								pro_b.setPbbsn(pr_e_b_sn);
							} else {
								pro_b.setPbbsn(sn_lists.getString(i));
							}
							pro_b.setPbcheck(false);
							pro_b.setPbusefulsn(0);
							pro_b.setPbwyears(data.getInt("pr_w_years"));
							pro_b.setSysstatus(0);
							pro_b.setSyssort(data.getInt("sys_sort"));
							pro_b.setPblpath("");
							pro_b.setPblsize("");
							pro_b.setPbltext("");
							pro_b.setPbschedule(json_work.toString());
							pro_b.setSysmuser(user.getSuaccount());
							pro_b.setSyscuser(user.getSuaccount());
							pro_bs.add(pro_b);

						}
						productionBodyDao.saveAll(pro_bs);
						id_b_g2 = id_b_g;
					}

					// 規格
					pro_r.setPrid(data.getString("ph_pr_id").toUpperCase());
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrcfrom(data.getString("pr_c_from").equals("") ? "MES" : data.getString("pr_c_from"));
					pro_r.setPrcname(data.getString("pr_c_name"));
					pro_r.setProrderid(data.getString("pr_order_id"));
					pro_r.setPrpmodel(data.getString("pr_p_model"));
					pro_r.setPrbitem(data.get("pr_b_item").toString());
					pro_r.setPrsitem(data.get("pr_s_item").toString());
					pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
					pro_r.setPrpokquantity(0);
					pro_r.setPrhokquantity(0);
					pro_r.setPrwyears(data.getInt("pr_w_years"));

					if (!data.getString("ph_type").equals("A511_no_sn")) {
						pro_r.setPrssn(sn_lists.get(0).toString());
						pro_r.setPresn(sn_lists.get(sn_lists.length() - 1).toString());
						// 燒錄序號
						if (data.getString("ps_b_f_sn") != null && !data.getString("ps_b_f_sn").equals("") && data.getInt("ps_b_sn") >= 0) {
							int ps_b_sn = data.getInt("ps_b_sn");
							int ps_b_size = data.getString("ps_b_sn").length();
							ps_b_sn += data.getInt("pr_p_quantity");
							String pr_e_b_sn = String.format("%0" + ps_b_size + "d", ps_b_sn);

							pro_r.setPrsbsn(data.getString("ps_b_f_sn") + data.getString("ps_b_sn"));
							pro_r.setPrebsn(data.getString("ps_b_f_sn") + pr_e_b_sn);
						} else {
							pro_r.setPrsbsn(sn_lists.get(0).toString());
							pro_r.setPrebsn(sn_lists.get(sn_lists.length() - 1).toString());
						}
					} else {
						// 沒SN不須登記
						pro_r.setPrssn("no_sn0000n000");
						pro_r.setPresn("no_sn0000n000");
						pro_r.setPrsbsn("no_sn0000n000");
						pro_r.setPrebsn("no_sn0000n000");
					}

					pro_r.setSysmuser(user.getSuaccount());
					pro_r.setSyscuser(user.getSuaccount());

					// productionRecordsDao.save(pro_r);

					// header
					pro_h = new ProductionHeader();
					// pro_h.setPhid(id_h + 1);
					pro_h.setPhwpid(data.getLong("ph_wp_id"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhpnumber(data.getString("ph_p_number"));
					pro_h.setPhpname(data.getString("ph_p_name"));
					pro_h.setPhschedule(0 + "／" + data.getInt("pr_p_quantity"));
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuaccount());
					pro_h.setSyscuser(user.getSuaccount());
					pro_h.setPhtype(data.getString("ph_type"));
					pro_h.setPhpbgid(id_b_g2);
					productionHeaderDao.save(pro_h);

					// typeDao+hoursDao
					List<WorkHours> works_p = new ArrayList<WorkHours>();
					ArrayList<WorkType> types = typeDao.findAll();
					types.forEach(t -> {
						WorkHours work_p = new WorkHours();
						if (t.getWtid() == 0) {
							// 父類別
							work_p.setSysheader(true);
						} else {
							work_p.setSysheader(false);
							// 子類別
						}
						work_p.setProductionRecords(pro_r);
						work_p.setWhwtid(t);
						work_p.setWhdo("複製:範例樣本,不列入計算");
						work_p.setWhaccount("");
						work_p.setWhnb(0);
						work_p.setSysnote("");
						work_p.setSyssort(0);
						work_p.setSysstatus(2);

						// 製令單類型
						switch (data.getString("ph_type")) {
						case "A511_no_sn":
							// A511_no_sn 一般製令_No(sn)
							works_p.add(work_p);
							break;
						case "A511_no_and_has_sn":
							// A511_no_and_has_sn 一般製令_Both(sn)
							works_p.add(work_p);
							break;
						case "A511_has_sn":
							// A511_has_sn 一般製令_Create(sn)

							break;
						case "A521_no_sn":
							// A511_no_sn 重工製令_No(sn)

							break;
						case "A521_has_sn":
							// A521_has_sn 重工製令_Re(sn)

							break;
						case "A522_service":
							// A522_service 維護製令

							break;
						case "A431_disassemble":
							// A522_service 拆解製令

							break;
						case "A512_outside":
							// A522_service 委外製令

							break;

						default:
							break;
						}

					});
					hoursDao.saveAll(works_p);

				} else {
					return check;
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
			for (Object one : list) {
				// 物件轉換
				ProductionHeader pro_h = new ProductionHeader();
				// ProductionBody pro_b = new ProductionBody();
				ProductionRecords pro_r = new ProductionRecords();
				ArrayList<ProductionSN> pro_sn = new ArrayList<ProductionSN>();
				JSONObject data = (JSONObject) one;

				// 查詢重複
				ProductionRecords search = new ProductionRecords();
				search.setPrid(data.getString("ph_pr_id"));
				List<ProductionHeader> headers = productionHeaderDao.findAllByProductionRecords(search);
				if (headers.size() > 0) {
					pro_h = headers.get(0);
				}
				// 無重複->新建
				if (headers.size() < 1) {
					// header
					// int id_h = productionHeaderDao.getProductionHeaderSeq();
					// int id_b = productionBodyDao.getProductionBodySeq();
					Long id_b_g2 = 1l;
					// 工作站資訊
					JSONObject json_work = new JSONObject();
					ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getLong("ph_wp_id"), false);
					for (WorkstationProgram p_one : programs) {
						ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
						JSONObject json_one = new JSONObject();
						json_one.put("name", works.get(0).getWpbname());
						json_one.put("type", "N");
						json_one.put("id", works.get(0).getWid());
						json_one.put("w_pb_cell", works.get(0).getWpbcell());
						json_one.put("sort", p_one.getSyssort());
						json_work.put(works.get(0).getWcname(), json_one);
					}
					// 無SN不需要指定新建
					JSONArray sn_lists = new JSONArray();
					if (data.getString("ph_type").equals("A511_no_and_has_sn") || data.getString("ph_type").equals("A511_has_sn")) {

						// 檢核SN選項
						if (data.getString("ps_sn_1").equals("no_") || //
								data.getString("ps_sn_2").equals("s") || //
								data.getString("ps_sn_3").equals("n") || //
								data.getString("ps_sn_5").equals("n")) {
							return false;
						}

						// sn
						pro_sn = new ArrayList<ProductionSN>();
						ProductionSN pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_1"));
						pro_sn_one.setPsgid(1l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_2"));
						pro_sn_one.setPsgid(2l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_3"));
						pro_sn_one.setPsgid(3l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_4"));
						pro_sn_one.setPsgid(4l);
						pro_sn_one.setPsname("[YYWW]");
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_5"));
						pro_sn_one.setPsgid(5l);
						pro_sn.add(pro_sn_one);

						pro_sn_one = new ProductionSN();
						pro_sn_one.setPsvalue(data.getString("ps_sn_6"));
						pro_sn_one.setPsgid(6l);
						pro_sn_one.setPsname("[000]");
						pro_sn.add(pro_sn_one);
						JSONObject sn_list = Fm_SN.analyze_Sn(pro_sn, true, data.getInt("pr_p_quantity"));

						// 檢核_區間是否用過
						String sn_f = sn_list.getJSONArray("sn_list").get(0).toString();
						String sn_e = sn_list.getJSONArray("sn_list").get(sn_list.getJSONArray("sn_list").length() - 1).toString();
						if (productionBodyDao.findAllByPbsn(sn_f).size() > 0 || productionBodyDao.findAllByPbsn(sn_e).size() > 0) {
							return false;
						}

						// 更新SN區段
						ProductionSN pro_sn_YYMM = snDao.findAllByPsid(11L).get(0);
						pro_sn_YYMM.setPsvalue(sn_list.getString("sn_YYWW"));
						ProductionSN pro_sn_SN = snDao.findAllByPsid(16L).get(0);
						pro_sn_SN.setPsvalue(sn_list.getString("sn_000"));
						snDao.save(pro_sn_SN);
						snDao.save(pro_sn_YYMM);

						sn_lists = sn_list.getJSONArray("sn_list");

						Long id_b_g = productionBodyDao.getProductionBodyGSeq();
						List<ProductionBody> pro_bs = new ArrayList<ProductionBody>();
						for (int i = 0; i < sn_lists.length(); i++) {
							// body
							ProductionBody pro_b = new ProductionBody();
							pro_b.setSysver(0);
							pro_b.setPbgid(id_b_g);
							pro_b.setSysheader(false);
							pro_b.setPbsn(sn_lists.getString(i));
							if (data.getString("ps_b_f_sn") != null && !data.getString("ps_b_f_sn").equals("") && data.getInt("ps_b_sn") >= 0) {
								String pr_e_b_sn = data.getString("ps_b_f_sn")
										+ String.format("%0" + data.getString("ps_b_sn").length() + "d", (data.getInt("ps_b_sn") + i));
								pro_b.setPbbsn(pr_e_b_sn);
							} else {
								pro_b.setPbbsn(sn_lists.getString(i));
							}
							pro_b.setPbcheck(false);
							pro_b.setPbusefulsn(0);
							pro_b.setPbwyears(data.getInt("pr_w_years"));
							pro_b.setSyssort(data.getInt("sys_sort"));
							pro_b.setPblpath("");
							pro_b.setPblsize("");
							pro_b.setPbltext("");
							pro_b.setPbschedule(json_work.toString());
							pro_b.setSysmuser(user.getSuaccount());
							pro_b.setSyscuser(user.getSuaccount());
							pro_b.setSysstatus(0);
							pro_bs.add(pro_b);
						}
						productionBodyDao.saveAll(pro_bs);
						id_b_g2 = id_b_g;
					}

					// 規格
					pro_r.setPrid(data.getString("ph_pr_id").toUpperCase());
					pro_r.setPrbomid(data.getString("pr_bom_id"));
					pro_r.setPrcfrom("MES");
					pro_r.setPrcname(data.getString("pr_c_name"));
					pro_r.setProrderid(data.getString("pr_order_id"));
					pro_r.setPrpmodel(data.getString("pr_p_model"));
					pro_r.setPrbitem("");
					pro_r.setPrsitem("");
					pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
					pro_r.setPrpokquantity(0);
					pro_r.setPrhokquantity(0);
					pro_r.setPrwyears(data.getInt("pr_w_years"));

					if (!data.getString("ph_type").equals("A511_no_sn")) {
						pro_r.setPrssn(sn_lists.get(0).toString());
						pro_r.setPresn(sn_lists.get(sn_lists.length() - 1).toString());
						// 燒錄序號
						if (data.getString("ps_b_f_sn") != null && !data.getString("ps_b_f_sn").equals("") && data.getInt("ps_b_sn") >= 0) {
							int ps_b_sn = data.getInt("ps_b_sn");
							int ps_b_size = data.getString("ps_b_sn").length();
							ps_b_sn += data.getInt("pr_p_quantity");
							String pr_e_b_sn = String.format("%0" + ps_b_size + "d", ps_b_sn);

							pro_r.setPrsbsn(data.getString("ps_b_f_sn") + data.getString("ps_b_sn"));
							pro_r.setPrebsn(data.getString("ps_b_f_sn") + pr_e_b_sn);
						} else {
							pro_r.setPrsbsn(sn_lists.get(0).toString());
							pro_r.setPrebsn(sn_lists.get(sn_lists.length() - 1).toString());
						}
					} else {
						// 沒SN不須登記
						pro_r.setPrssn("no_sn0000n000");
						pro_r.setPresn("no_sn0000n000");
						pro_r.setPrsbsn("no_sn0000n000");
						pro_r.setPrebsn("no_sn0000n000");
					}

					pro_r.setSysmuser(user.getSuaccount());
					pro_r.setSyscuser(user.getSuaccount());
					// header
					pro_h = new ProductionHeader();
					// pro_h.setPhid(id_h + 1);
					pro_h.setPhwpid(data.getLong("ph_wp_id"));
					pro_h.setProductionRecords(pro_r);
					pro_h.setPhpnumber(data.getString("ph_p_number"));
					pro_h.setPhpname(data.getString("ph_p_name"));
					pro_h.setPhschedule(0 + "／" + data.getInt("pr_p_quantity"));
					pro_h.setSysheader(true);
					pro_h.setSysnote(data.getString("sys_note"));
					pro_h.setSyssort(data.getInt("sys_sort"));
					pro_h.setSysver(0);
					pro_h.setSysstatus(data.getInt("sys_status"));
					pro_h.setSysmuser(user.getSuaccount());
					pro_h.setSyscuser(user.getSuaccount());
					pro_h.setPhtype(data.getString("ph_type"));
					pro_h.setPhpbgid(id_b_g2);
					productionHeaderDao.save(pro_h);

					// typeDao+hoursDao
					List<WorkHours> works_p = new ArrayList<WorkHours>();
					ArrayList<WorkType> types = typeDao.findAll();
					types.forEach(t -> {
						WorkHours work_p = new WorkHours();
						if (t.getWtid() == 0) {
							// 父類別
							work_p.setSysheader(true);
						} else {
							work_p.setSysheader(false);
							// 子類別
						}
						work_p.setProductionRecords(pro_r);
						work_p.setWhwtid(t);
						work_p.setWhdo("複製:範例樣本,不列入計算");
						work_p.setWhaccount("");
						work_p.setWhnb(0);
						work_p.setSysnote("");
						work_p.setSyssort(0);
						work_p.setSysstatus(2);

						// 製令單類型
						switch (data.getString("ph_type")) {
						case "A511_no_sn":
							// A511_no_sn 一般製令_No(sn)
							works_p.add(work_p);
							break;
						case "A511_no_and_has_sn":
							// A511_no_and_has_sn 一般製令_Both(sn)
							works_p.add(work_p);
							break;
						case "A511_has_sn":
							// A511_has_sn 一般製令_Create(sn)

							break;
						case "A521_no_sn":
							// A511_no_sn 重工製令_No(sn)

							break;
						case "A521_has_sn":
							// A521_has_sn 重工製令_Re(sn)

							break;
						case "A522_service":
							// A522_service 維護製令

							break;
						case "A431_disassemble":
							// A522_service 拆解製令

							break;
						case "A512_outside":
							// A522_service 委外製令

							break;

						default:
							break;
						}

					});
					hoursDao.saveAll(works_p);

				} else {
					return check;
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
			for (Object one : list) {
				// 物件轉換
				JSONObject data = (JSONObject) one;

				// 查詢重複
				List<ProductionHeader> headers = productionHeaderDao.findAllByPhid(data.getLong("ph_id"));
				// 查詢資料
				if (headers.size() != 1) {
					return check;
				}
				// 查詢是否 為已啟動(不可修改)
				if (headers.get(0).getSysstatus() != 0) {
					// 但是 如果是 緊急停止 or 暫停 只修改狀態
					if (headers.get(0).getSysstatus() != 0 && //
							(data.getInt("sys_status") == 8 || //
									data.getInt("sys_status") == 9 || //
									data.getInt("sys_status") == 1 || //
									data.getInt("sys_status") == 2)) {
						ProductionHeader one_header = headers.get(0);
						one_header.setSysstatus(data.getInt("sys_status"));
						productionHeaderDao.save(one_header);
						return true;
					}
					return check;
				}

				ProductionHeader one_h = headers.get(0);
				// 工作站資訊
				JSONObject json_work = new JSONObject();
				ArrayList<WorkstationProgram> programs = programDao.findAllByWpgidAndSysheaderOrderBySyssortAsc(data.getLong("ph_wp_id"), false);
				for (WorkstationProgram p_one : programs) {
					ArrayList<Workstation> works = workDao.findAllByWgidAndSysheaderOrderBySyssortAsc(p_one.getWpwgid(), true);
					JSONObject json_one = new JSONObject();
					json_one.put("name", works.get(0).getWpbname());
					json_one.put("type", "N");
					json_one.put("id", works.get(0).getWid());
					json_one.put("w_pb_cell", works.get(0).getWpbcell());
					json_one.put("sort", p_one.getSyssort());
					json_work.put(works.get(0).getWcname(), json_one);
				}
				// 更新sn 過站設定
				JSONObject json_work_d = json_work;
				List<ProductionBody> pb_s = productionBodyDao.findAllByPbgidOrderByPbsnAsc(one_h.getPhpbgid());
				pb_s.forEach(s -> {
					s.setPbschedule(json_work_d.toString());
				});
				productionBodyDao.saveAll(pb_s);

				// 規格
				ProductionRecords pro_r = one_h.getProductionRecords();
				// pro_r.setPrid(data.getString("ph_pr_id"));
				pro_r.setPrbomid(data.getString("pr_bom_id"));
				pro_r.setPrcfrom("MES");
				pro_r.setPrcname(data.getString("pr_c_name"));
				pro_r.setPrwyears(data.getInt("pr_w_years"));
				pro_r.setProrderid(data.getString("pr_order_id"));
				pro_r.setPrpmodel(data.getString("pr_p_model"));
				pro_r.setPrbomid(data.getString("pr_bom_id"));
				// pro_r.setPrpquantity(data.getInt("pr_p_quantity"));
				pro_r.setPrpokquantity(data.getInt("pr_p_ok_quantity"));
				pro_r.setSysmuser(user.getSuaccount());
				pro_r.setSysmdate(new Date());

				// header
				one_h.setPhtype(data.getString("ph_type"));
				one_h.setPhwpid(data.getLong("ph_wp_id"));
				one_h.setPhpnumber(data.getString("ph_p_number"));
				one_h.setPhpname(data.getString("ph_p_name"));
				one_h.setProductionRecords(pro_r);
				one_h.setPhschedule(data.getInt("pr_p_ok_quantity") + "／" + pro_r.getPrpquantity());
				one_h.setSysheader(true);
				one_h.setSysnote(data.getString("sys_note"));
				one_h.setSyssort(data.getInt("sys_sort"));
				one_h.setSysver(0);
				// 數量不為0 不可改回/待命狀態[0]
				if (data.getInt("sys_status") == 0) {
					if (data.getInt("pr_p_ok_quantity") == 0) {
						one_h.setSysstatus(data.getInt("sys_status"));
					}
				} else {
					one_h.setSysstatus(data.getInt("sys_status"));
				}
				one_h.setSysmuser(user.getSuaccount());
				one_h.setSysmdate(new Date());
				productionHeaderDao.save(one_h);

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
				JSONObject data = (JSONObject) one;
				ProductionHeader pheader = productionHeaderDao.findAllByPhid(data.getLong("ph_id")).get(0);
				hoursDao.deleteByproductionRecords(pheader.getProductionRecords());
				productionHeaderDao.deleteByPhidAndSysheader(data.getLong("ph_id"), true);
				check = true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return check;
	}
}
