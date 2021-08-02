package dtri.com.tw.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.ProductionBody;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.db.pgsql.dao.ProductionBodyDao;
import dtri.com.tw.tools.Fm_Time;

@Service
public class WorkstationConfigService {
	@Autowired
	private ProductionBodyDao bodyDao;

	// 取得當前 資料清單
	public PackageBean getData(JSONObject body, int page, int p_size) {
		PackageBean bean = new PackageBean();
		List<ProductionBody> productionBodys = new ArrayList<ProductionBody>();

		// 查詢的頁數，page=從0起算/size=查詢的每頁筆數
		if (p_size < 1) {
			page = 0;
			p_size = 100;
		}
		String pb_value = null;
		// 初次載入需要標頭 / 之後就不用
		if (body == null || body.isNull("search")) {
			// 放入包裝(header) [01 是排序][_h__ 是分割直][資料庫欄位名稱]
			JSONObject object_header = new JSONObject();
			int ord = 0;
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pb_cell", FFS.h_t("系統欄位", "150px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "pb_value", FFS.h_t("工作站欄位名稱", "150px",FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_date", FFS.h_t("建立時間", "150px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_c_user", FFS.h_t("建立人", "100px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_date", FFS.h_t("修改時間", "150px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_m_user", FFS.h_t("修改人", "100px",FFM.Wri.W_Y));

			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_note", FFS.h_t("備註", "100px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_sort", FFS.h_t("排序", "100px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_ver", FFS.h_t("版本", "100px",FFM.Wri.W_Y));
			object_header.put(FFS.ord((ord += 1), FFM.Hmb.H) + "sys_status", FFS.h_t("狀態", "100px",FFM.Wri.W_Y));
			bean.setHeader(object_header);

			// 放入修改 [(key)](modify/Create/Delete) 格式
			JSONArray obj_m = new JSONArray();
			JSONArray n_val = new JSONArray();
			JSONArray a_val = new JSONArray();

			JSONArray a_val_body = new JSONArray();
			ProductionBody body_one = bodyDao.findAllByPbid(0l).get(0);
			// sn關聯表
			int j = 0;
			Method method;
			for (j = 0; j < 20; j++) {
				String m_name = "getPbwname" + String.format("%02d", j + 1);
				try {
					method = body_one.getClass().getMethod(m_name);
					String value = (String) method.invoke(body_one);
					String key = "pb_w_name" + String.format("%02d", j + 1);

					a_val_body.put((new JSONObject()).put("value", value == null ? key : value).put("key", key));

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
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.SEL, FFM.Type.TEXT, "", "",FFM.Wri.W_N, "col-md-2", false, a_val_body, "pb_cell", "系統欄位"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",FFM.Wri.W_Y, "col-md-2", true, n_val, "pb_value", "工作站欄位名稱"));

			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",FFM.Wri.W_N, "col-md-2", false, n_val, "sys_c_date", "建立時間"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",FFM.Wri.W_N, "col-md-2", false, n_val, "sys_c_user", "建立人"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_date", "修改時間"));
			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFM.Type.TEXT, "", "",FFM.Wri.W_N, "col-md-2", false, n_val, "sys_m_user", "修改人"));
//
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.TTA, FFM.Type.TEXT, "", "",FFM.Wri.W_Y, "col-md-12", false, n_val, "sys_note", "備註"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "0", "0",FFM.Wri.W_Y, "col-md-2", true, n_val, "sys_sort", "排序"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFM.Tag.INP, FFS.NUMB, "", "",FFM.Wri.W_N, "col-md-2", false, n_val, "sys_ver", "版本"));

			a_val = new JSONArray();
			a_val.put((new JSONObject()).put("value", "正常").put("key", "0"));
			a_val.put((new JSONObject()).put("value", "異常").put("key", "1"));
//			obj_m.put(FFS.h_m(FFM.Dno.D_S,FFS.SEL, FFM.Type.TEXT, "", "0",FFM.Wri.W_Y, "col-md-2", true, a_val, "sys_status", "狀態"));
			bean.setCell_modify(obj_m);

			// 放入包裝(search)
			JSONArray object_searchs = new JSONArray();
			object_searchs.put(FFS.h_s(FFM.Tag.INP, FFM.Type.TEXT, "", "col-md-2", "pb_value", "SN_過站名稱", n_val));
			bean.setCell_searchs(object_searchs);
		} else {

			// 進行-特定查詢
			pb_value = body.getJSONObject("search").getString("pb_value");
			pb_value = pb_value.equals("") ? null : pb_value;
		}
		productionBodys = bodyDao.findAllByPbid(0l);

		// 放入包裝(body) [01 是排序][_b__ 是分割直][資料庫欄位名稱]
		JSONArray object_bodys = new JSONArray();
		productionBodys.forEach(one -> {
			// sn關聯表
			Method method;
			for (int j = 0; j < 20; j++) {
				int ord = 0;
				String m_name = "getPbwname" + String.format("%02d", j + 1);
				try {
					method = one.getClass().getMethod(m_name);
					String value = (String) method.invoke(one);
					String name = "pb_w_name" + String.format("%02d", j + 1);

					JSONObject object_body = new JSONObject();
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pb_cell", name);
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "pb_value", value == null ? "" : value);
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_date", Fm_Time.to_yMd_Hms(one.getSyscdate()));
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_c_user", one.getSyscuser());
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_date", Fm_Time.to_yMd_Hms(one.getSysmdate()));
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_m_user", one.getSysmuser());
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_note", one.getSysnote());
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_sort", one.getSyssort());
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_ver", one.getSysver());
					object_body.put(FFS.ord((ord += 1), FFM.Hmb.B) + "sys_status", one.getSysstatus());
					object_bodys.put(object_body);

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

		});
		bean.setBody(new JSONObject().put("search", object_bodys));
		return bean;
	}

	// 存檔 資料清單
	@Transactional
	public boolean createData(JSONObject body, SystemUser user) {
		boolean check = false;
		try {
//			JSONArray list = body.getJSONArray("create");
//			for (Object one : list) {
//				// 物件轉換
//			}
//			check = true;
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
//			JSONArray list = body.getJSONArray("save_as");
//			for (Object one : list) {
//				// 物件轉換
//				//bodyDao.save(sys_c);
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
			for (Object one : list) {
				// 物件轉換
				ProductionBody p_body = bodyDao.findAllByPbid(0l).get(0);
				JSONObject data = (JSONObject) one;
				// SN類別

				// 欄位名稱/值
				String pb_cell = data.getString("pb_cell");
				String pb_value = data.getString("pb_value");
				// 欄位有值
				if (pb_cell != null && !pb_cell.equals("")) {
					String in_name = "setPbwname" + pb_cell.substring(pb_cell.length() - 2);
					Method in_method = p_body.getClass().getMethod(in_name, String.class);
					in_method.invoke(p_body, pb_value);

					p_body.setSysmuser(user.getSuaccount());
					p_body.setSysmdate(new Date());

					bodyDao.save(p_body);
				}
				check = true;
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
//			JSONArray list = body.getJSONArray("delete");
//			for (Object one : list) {
//				// 物件轉換
//				check = true;
//			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return check;
	}
}
