package com.example.projektsklep.controller;//package com.example.projectshopping.controller;
//
//import com.example.projectshopping.model.form.LoginForm;
//import com.example.projectshopping.service.LoginService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/login")
//public class LoginController {
//
//
//    private LoginService loginService;
//
//    public LoginController(LoginService loginService) {
//        this.loginService = loginService;
//    }
//
//    @PostMapping
//    public String login(LoginForm form) {
//        if (loginService.login(form)) {
//            return "redirect:/";
//        } else {
//            return "user_login";
//        }
//    }
//}