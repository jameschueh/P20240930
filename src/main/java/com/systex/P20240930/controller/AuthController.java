package com.systex.P20240930.controller;

import com.systex.P20240930.model.Member;
import com.systex.P20240930.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.LinkedList;

@Controller
public class AuthController {

    @Autowired
    private MemberService memberService;

    // 設定首頁重定向到 login
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login"; // 訪問 / 時自動重定向到 /login
    }

    // 註冊頁面
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "auth/register";
    }

    // 處理註冊請求
    @PostMapping("/checkRegister")
    public String processRegister(
            @RequestParam("account") String account,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {

        LinkedList<String> errors = new LinkedList<>();

        // 驗證帳號和密碼是否為空
        if (account == null || account.isEmpty()) {
            errors.add("請輸入帳號。");
        }
        if (password == null || password.isEmpty()) {
            errors.add("請輸入密碼。");
        }

        // 檢查是否有錯誤
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "auth/register"; // 返回註冊頁面並顯示錯誤訊息
        }

        // 嘗試註冊新帳號
        Member member = new Member();
        member.setAccount(account);
        member.setPassword(password);

        if (memberService.register(member)) {
            redirectAttributes.addFlashAttribute("successMessage", "註冊成功，請登入！");
            return "redirect:/login"; // 註冊成功，跳轉到登入頁面
        } else {
            errors.add("該帳號已經存在！");
            model.addAttribute("errors", errors);
            return "auth/register"; // 註冊失敗，返回註冊頁面
        }
    }

    // 登入頁面
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    // 處理登入請求
    @PostMapping("/checkLogin")
    public String processLogin(
            @RequestParam("account") String account,
            @RequestParam("password") String password,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        LinkedList<String> errors = new LinkedList<>();

        // 驗證帳號和密碼是否為空
        if (account == null || account.isEmpty()) {
            errors.add("請輸入帳號。");
        }
        if (password == null || password.isEmpty()) {
            errors.add("請輸入密碼。");
        }

        // 如果有錯誤，返回登入頁面
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "auth/login";
        }

        // 驗證帳號和密碼
        Member member = memberService.login(account, password);
        if (member != null) {
            session.setAttribute("user", member); // 記錄登入用戶信息到 session
            return "redirect:index.jsp"; // 登入成功，跳轉到主頁面
        } else {
            errors.add("帳號或密碼錯誤！");
            model.addAttribute("errors", errors);
            return "auth/login"; // 登入失敗，返回登入頁面
        }
    }

    // 登出功能
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 清除 session
        return "redirect:/login"; // 登出後重定向到登入頁面
    }
}
