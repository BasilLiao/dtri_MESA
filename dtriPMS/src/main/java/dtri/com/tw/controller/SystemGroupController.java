package dtri.com.tw.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.login.LoginUserDetails;
import dtri.com.tw.service.PackageService;
import dtri.com.tw.service.SystemGroupService;

@Controller
public class SystemGroupController {
	// 功能
	final static String SYS_F = "system_group.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	SystemGroupService groupService;

	/**
	 * 訪問
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String access(@RequestBody String json_object) {
		System.out.println("---controller -access " + SYS_F + " Check");
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		 
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = groupService.getData(req.getBody(), req.getPage_batch(), req.getPage_total(), user);
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, "");
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 查詢
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AR" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String search(@RequestBody String json_object) {
		System.out.println("---controller -search " + SYS_F + " Check");
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		 
		System.out.println(json_object);
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行查詢
		resp = groupService.getData(req.getBody(), req.getPage_batch(), req.getPage_total(), user);
		// Step3.包裝回傳
		resp = packageService.setObjResp(resp, req, "");
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AC" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
	public String create(@RequestBody String json_object) {
		System.out.println("---controller -create " + SYS_F + " Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		 
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.createData(req.getBody(), user);
		if (check) {
			check = groupService.save_asData(req.getBody(), user);
		}
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, "");
		} else {
			// Step4.包裝回傳
			resp.autoMsssage(100);
			resp = packageService.setObjResp(resp, req, "");
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AU" }, method = { RequestMethod.PUT }, produces = "application/json;charset=UTF-8")
	public String modify(@RequestBody String json_object) {
		System.out.println("---controller -modify " + SYS_F + " Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		 
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.updateData(req.getBody(), user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, "");
		} else {
			// Step4.包裝回傳
			resp.autoMsssage(100);
			resp = packageService.setObjResp(resp, req, "");
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}

	/**
	 * 移除
	 */
	@ResponseBody
	@RequestMapping(value = { "/ajax/system_group.basil.AD" }, method = { RequestMethod.DELETE }, produces = "application/json;charset=UTF-8")
	public String delete(@RequestBody String json_object) {
		System.out.println("---controller -delete " + SYS_F + " Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		boolean check = false;
		 
		System.out.println(json_object);
		// 取得-當前用戶資料
		SystemUser user = new SystemUser();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			user = userDetails.getSystemUser();
		}
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// Step2.進行新增
		check = groupService.deleteData(req.getBody(),user);
		// Step3.進行判定
		if (check) {
			// Step4.包裝回傳
			resp = packageService.setObjResp(resp, req, "");
		} else {
			// Step4.包裝回傳
			resp.autoMsssage(100);
			resp = packageService.setObjResp(resp, req, "");
		}
		// 回傳-資料
		return packageService.objToJson(resp);
	}
}