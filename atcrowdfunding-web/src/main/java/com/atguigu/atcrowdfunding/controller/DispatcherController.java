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
	 * �˳�ʱ��session�Ự�����loginUser��Ϣ
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		
//		session.removeAttribute("loginUser");
		session.invalidate();//sessionʧЧ
		return "redirect:login";
	}
	
	@ResponseBody
	@RequestMapping("/doAJAXLogin")
	public Object doAJAXLogin(User user,HttpSession session) {
	 
		AJAXResult result = new AJAXResult();
		
		User dbUser = userService.query4Login(user);
		
		//�����ж��û���Ϣ�Ƿ����
		if(dbUser!=null) {
			session.setAttribute("loginUser", dbUser);
			result.setSuccess(true);
		}else {
			//��¼ʧ�ܣ���ת�ص�¼���棬����ʾ������Ϣ
			result.setSuccess(false);
		}
		
		return result;
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(User user, Model model) {
		
		//һ����ȡ��Ԫ��
		//1��ͨ��HttpServletRequest��ȡ
		//2)�ڷ��������б������ӱ���Ӧ�Ĳ�����������ͬ String loginacct,String userpswd
		//3)�������ݷ�װΪʵ�������User user
		
		//������ѯ�û���Ϣ
		User dbUser = userService.query4Login(user);
		
		//�����ж��û���Ϣ�Ƿ����
		if(dbUser!=null) {
			//��¼�ɹ�����ת����ҳ��
			return "main";
		}else {
			//��¼ʧ�ܣ���ת�ص�¼���棬����ʾ������Ϣ
			String errorMsg ="��¼�˺Ż����벻��ȷ�����������룡";
			model.addAttribute("errorMsg", errorMsg);
		return "redirect:login";//�ض���ķ�ʽ��ת����¼����
		}
	}
}
