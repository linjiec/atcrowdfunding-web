package com.atguigu.atcrowdfunding.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.AJAXResult;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.service.UserService;

@Controller
public class DispatcherController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping("/main")
	public String main() {
		return "main";
	}
	
	/**
	 * 退出时从session会话中清除loginUser信息
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		
//		session.removeAttribute("loginUser");
		session.invalidate();//session失效
		return "redirect:login";
	}
	
	@ResponseBody
	@RequestMapping("/doAJAXLogin")
	public Object doAJAXLogin(User user,HttpSession session) {
	 
		AJAXResult result = new AJAXResult();
		
		User dbUser = userService.query4Login(user);
		
		//三、判断用户信息是否存在
		if(dbUser!=null) {
			session.setAttribute("loginUser", dbUser);
			result.setSuccess(true);
		}else {
			//登录失败，跳转回登录界面，并提示错误信息
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(User user, Model model) {
		
		//一。获取表单元素
		//1）通过HttpServletRequest获取
		//2)在方法参数列表中增加表单对应的参数，名称相同 String loginacct,String userpswd
		//3)将表单数据封装为实体类对象User user
		
		//二、查询用户信息
		User dbUser = userService.query4Login(user);
		
		//三、判断用户信息是否存在
		if(dbUser!=null) {
			//登录成功，跳转到主页面
			return "main";
		}else {
			//登录失败，跳转回登录界面，并提示错误信息
			String errorMsg ="登录账号或密码不正确，请重新输入！";
			model.addAttribute("errorMsg", errorMsg);
		return "redirect:login";//重定向的方式跳转到登录界面
		}
	}
}
