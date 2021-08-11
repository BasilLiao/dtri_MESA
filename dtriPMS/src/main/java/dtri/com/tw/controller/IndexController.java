package dtri.com.tw.controller;

import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.ModelAndView;

import dtri.com.tw.bean.ConfigBean;
import dtri.com.tw.bean.PackageBean;
import dtri.com.tw.db.entity.SystemGroup;
import dtri.com.tw.db.entity.SystemPermission;
import dtri.com.tw.db.entity.SystemUser;
import dtri.com.tw.login.LoginUserDetails;
import dtri.com.tw.service.IndexService;
import dtri.com.tw.service.PackageService;

@Controller
public class IndexController {
	// 功能
	final static String SYS_F = "index.basil";

	@Autowired
	PackageService packageService;
	@Autowired
	IndexService indexService;

	@Autowired
	ConfigBean configBean;

	/**
	 * 登入 and 登出-畫面
	 */
	@RequestMapping(value = { "/", "/login.basil", "/index.basil", "/logout.basil" }, method = { RequestMethod.GET })
	public ModelAndView loginCheck(HttpServletRequest request) {
		System.out.println("---controller -login(logout) " + SYS_F + " Check");
		// 可能有錯誤碼
		String error = request.getParameter("status");

		// 測試網路列印
		String printerName = "";
		PrintService service = null;
		// Get array of all print services - sort order NOT GUARANTEED!
		PrintService[] services = PrinterJob.lookupPrintServices();
		// Retrieve specified print service from the array
		for (int index = 0; service == null && index < services.length; index++) {
			System.out.println(services[index].getName());
			if (services[index].getName().equalsIgnoreCase(printerName)) {
				service = services[index];
			}
		}
		configBean.init();
		// 回傳-模板
		return new ModelAndView("./html/login.html", "status", error);
	}

	/**
	 * (初始化)主頁
	 * 
	 */
	@RequestMapping(value = { "/index.basil" }, method = { RequestMethod.POST })
	public ModelAndView indexCheck() {
		System.out.println("---controller -index(init) " + SYS_F + " Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
		// 取得-當前用戶資料
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step1.查詢資料
			SystemUser user = userDetails.getSystemUser();
			List<SystemGroup> nav = userDetails.getSystemGroup();
			resp = indexService.getNav(nav);
			resp.setInfo_user(indexService.getUserInfo(user));
			// Step1.查詢資料權限
			systemGroup = userDetails.getSystemGroup();
		} else {
			// 取得用戶失敗
			resp.setInfo(PackageBean.info_message_warning1_NotFindUser);
			resp.setInfo_color(PackageBean.info_color_warning);
		}
		// UI限制功能
		SystemPermission one = new SystemPermission();
		systemGroup.forEach(p -> {
			if (p.getSystemPermission().getSpcontrol().equals(SYS_F)) {
				one.setSppermission(p.getSystemPermission().getSppermission());
			}
		});
		// Step2.包裝回傳
		resp = packageService.setObjResp(resp, req, "");

		// 回傳-模板
		return new ModelAndView("./html/main.html", "initMain", packageService.objToJson(resp));
	}

	/**
	 * (再次讀取)主頁
	 */
	@ResponseBody
	@RequestMapping(value = { "ajax/index.basil" }, method = { RequestMethod.POST })
	public String index(@RequestBody String json_object) {
		System.out.println("---controller -index(again) " + SYS_F + " Check");
		PackageBean req = new PackageBean();
		PackageBean resp = new PackageBean();
		// Step1.包裝解析
		req = packageService.jsonToObj(new JSONObject(json_object));
		// 取得-當前用戶資料
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			// Step2.查詢資料
			SystemUser user = userDetails.getSystemUser();
			List<SystemGroup> nav = userDetails.getSystemGroup();
			resp = indexService.getNav(nav);
			resp.setInfo_user(indexService.getUserInfo(user));
		} else {
			// 取得用戶失敗
			resp.setInfo(PackageBean.info_message_warning1_NotFindUser);
			resp.setInfo_color(PackageBean.info_color_warning);
		}
		// Step2.包裝回傳
		resp = packageService.setObjResp(resp, req, "");

		// 回傳-模板
		return packageService.objToJson(resp);
	}
}
