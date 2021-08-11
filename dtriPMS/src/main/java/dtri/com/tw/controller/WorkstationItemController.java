package dtri.com.tw.controller;

import org.springframework.stereotype.Controller;

@Controller
public class WorkstationItemController {
	// 功能
	final static String SYS_F = "workstation_item.basil";
// 遺棄功能獏組
//	@Autowired
//	PackageService packageService;
//	@Autowired
//	WorkstationItemService itemService;
//
//	/**
//	 * 訪問
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/ajax/workstation_item.basil" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	public String access(@RequestBody String json_object) {
//		System.out.println("---controller -access " + SYS_F + " Check");
//		PackageBean req = new PackageBean();
//		PackageBean resp = new PackageBean();
//		 
//		System.out.println(json_object);
//		// 取得-當前用戶資料
//		List<SystemGroup> systemGroup = new ArrayList<SystemGroup>();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (!(authentication instanceof AnonymousAuthenticationToken)) {
//			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			// Step1.查詢資料權限
//			systemGroup = userDetails.getSystemGroup();
//		}
//		// UI限制功能
//		SystemPermission one = new SystemPermission();
//		systemGroup.forEach(p -> {
//			if (p.getSystemPermission().getSpcontrol().equals(SYS_F)) {
//				one.setSppermission(p.getSystemPermission().getSppermission());
//			}
//		});
//		// Step1.包裝解析
//		req = packageService.jsonToObj(new JSONObject(json_object));
//		// Step2.進行查詢
//		resp = itemService.getData(req.getBody(), req.getPage_batch(), req.getPage_total());
//		// Step3.包裝回傳
//		resp = packageService.setObjResp(resp, req, info, info_color, one.getSppermission());
//		// 回傳-資料
//		return packageService.objToJson(resp);
//	}
//
//	/**
//	 * 查詢
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/ajax/workstation_item.basil.AR" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	public String search(@RequestBody String json_object) {
//		System.out.println("---controller -search " + SYS_F + " Check");
//		PackageBean req = new PackageBean();
//		PackageBean resp = new PackageBean();
//		 
//		System.out.println(json_object);
//		// Step1.包裝解析
//		req = packageService.jsonToObj(new JSONObject(json_object));
//		// Step2.進行查詢
//		resp = itemService.getData(req.getBody(), req.getPage_batch(), req.getPage_total());
//		// Step3.包裝回傳
//		resp = packageService.setObjResp(resp, req, "");
//		// 回傳-資料
//		return packageService.objToJson(resp);
//	}
//
//	/**
//	 * 新增
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/ajax/workstation_item.basil.AC" }, method = { RequestMethod.POST }, produces = "application/json;charset=UTF-8")
//	public String create(@RequestBody String json_object) {
//		System.out.println("---controller -create " + SYS_F + " Check");
//		PackageBean req = new PackageBean();
//		PackageBean resp = new PackageBean();
//		boolean check = false;
//		 
//		System.out.println(json_object);
//		// 取得-當前用戶資料
//		SystemUser user = new SystemUser();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (!(authentication instanceof AnonymousAuthenticationToken)) {
//			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			// Step1.查詢資料
//			user = userDetails.getSystemUser();
//		}
//		// Step1.包裝解析
//		req = packageService.jsonToObj(new JSONObject(json_object));
//		// Step2.進行新增
//		check = itemService.createData(req.getBody(), user);
//		if (check) {
//			check = itemService.save_asData(req.getBody(), user);
//		}
//		// Step3.進行判定
//		if (check) {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, "");
//		} else {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning, "");
//		}
//		// 回傳-資料
//		return packageService.objToJson(resp);
//	}
//
//	/**
//	 * 修改
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/ajax/workstation_item.basil.AU" }, method = { RequestMethod.PUT }, produces = "application/json;charset=UTF-8")
//	public String modify(@RequestBody String json_object) {
//		System.out.println("---controller - -modify " + SYS_F + " Check");
//		PackageBean req = new PackageBean();
//		PackageBean resp = new PackageBean();
//		boolean check = false;
//		 
//		System.out.println(json_object);
//		// 取得-當前用戶資料
//		SystemUser user = new SystemUser();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (!(authentication instanceof AnonymousAuthenticationToken)) {
//			LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			// Step1.查詢資料
//			user = userDetails.getSystemUser();
//		}
//		// Step1.包裝解析
//		req = packageService.jsonToObj(new JSONObject(json_object));
//		// Step2.進行新增
//		check = itemService.updateData(req.getBody(), user);
//		// Step3.進行判定
//		if (check) {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, "");
//		} else {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning, "");
//		}
//		// 回傳-資料
//		return packageService.objToJson(resp);
//	}
//
//	/**
//	 * 移除
//	 */
//	@ResponseBody
//	@RequestMapping(value = { "/ajax/workstation_item.basil.AD" }, method = { RequestMethod.DELETE }, produces = "application/json;charset=UTF-8")
//	public String delete(@RequestBody String json_object) {
//		System.out.println("---controller -delete " + SYS_F + " Check");
//		PackageBean req = new PackageBean();
//		PackageBean resp = new PackageBean();
//		boolean check = false;
//		 
//		System.out.println(json_object);
//
//		// Step1.包裝解析
//		req = packageService.jsonToObj(new JSONObject(json_object));
//		// Step2.進行新增
//		check = itemService.deleteData(req.getBody());
//		// Step3.進行判定
//		if (check) {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, "");
//		} else {
//			// Step4.包裝回傳
//			resp = packageService.setObjResp(resp, req, PackageBean.info_message_warning, PackageBean.info_color_warning, "");
//		}
//		// 回傳-資料
//		return packageService.objToJson(resp);
//	}
}
