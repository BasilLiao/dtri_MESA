package dtri.com.tw.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.service.ProductionHeaderService;
import dtri.com.tw.service.SystemApiService;

@Controller
public class SystemApiController {
	// 功能
	final static String SYS_F = "api.basil";

	@Autowired
	ProductionHeaderService headerService;
	@Autowired
	SystemApiService apiService;

	/**
	 * API-與延展系統串接
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/api.basil" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String access(@RequestBody String json_object) {
		System.out.println("---controller -access " + SYS_F + " Check");
		System.out.println(json_object);
		JSONObject obj = new JSONObject(json_object);
		String obj_re = "";
		SystemUser user = new SystemUser();
		user.setSuaccount("system");

		// 解析行為
		switch (obj.getString("action")) {
		case "production_create":
			headerService.createData(obj, user);
			break;
		case "get_work_program":
			obj_re = apiService.getWorkstationProgramList().toString();
			break;

		default:
			break;
		}

		// 回傳-資料
		return obj_re;
	}

}
