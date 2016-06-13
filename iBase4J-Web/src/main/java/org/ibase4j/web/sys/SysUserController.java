/**
 * 
 */
package org.ibase4j.web.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ibase4j.core.support.BaseController;
import org.ibase4j.core.util.Request2ModelUtil;
import org.ibase4j.core.util.WebUtil;
import org.ibase4j.model.generator.SysUser;
import org.ibase4j.model.sys.SysMenuBean;
import org.ibase4j.service.sys.SysAuthorizeService;
import org.ibase4j.service.sys.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * 用户管理控制器
 * 
 * @author ShenHuaJie
 * @version 2016年5月20日 下午3:12:12
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysAuthorizeService authorizeService;

	// 修改用户信息
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Object update(HttpServletRequest request, ModelMap modelMap) {
		SysUser sysUser = Request2ModelUtil.covert(SysUser.class, request);
		sysUserService.updateUserInfo(sysUser);
		return setSuccessModelMap(modelMap);
	}

	// 修改密码
	@RequestMapping(value = "/update/password", method = RequestMethod.POST)
	public Object updatePassword(ModelMap modelMap, @RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "password", required = false) String password) {
		sysUserService.updatePassword(id, password);
		return setSuccessModelMap(modelMap);
	}

	// 查询用户
	@RequestMapping(value = "/read/list")
	public Object get(HttpServletRequest request, ModelMap modelMap) {
		Map<String, Object> params = WebUtil.getParameterMap(request);
		PageInfo<?> list = sysUserService.queryBeans(params);
		return setSuccessModelMap(modelMap, list);
	}

	// 用户详细信息
	@RequestMapping(value = "/read/detail")
	public Object detail(ModelMap modelMap, @RequestParam(value = "id", required = false) Integer id) {
		SysUser sysUser = sysUserService.queryById(id);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		return setSuccessModelMap(modelMap, sysUser);
	}

	// 当前用户
	@RequestMapping(value = "/read/current")
	public Object current(ModelMap modelMap) {
		Integer id = getCurrUser();
		SysUser sysUser = sysUserService.queryById(id);
		if (sysUser != null) {
			sysUser.setPassword(null);
		}
		List<SysMenuBean> menus = authorizeService.queryAuthorizeByUserId(id);
		modelMap.put("user", sysUser);
		modelMap.put("menus", menus);
		return setSuccessModelMap(modelMap);
	}
}