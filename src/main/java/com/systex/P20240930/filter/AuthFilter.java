package com.systex.P20240930.filter;

import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();

        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        
        // 定義不需要認證的 URI
        String loginURI = httpRequest.getContextPath() + "/login";
        String registerURI = httpRequest.getContextPath() + "/register";
        String h2ConsoleURI = httpRequest.getContextPath() + "/h2-console";

        // 檢查是否已登入且不在不需認證的頁面
//        if (!loggedIn && !(requestURI.equals(loginURI) || requestURI.equals(registerURI) || requestURI.startsWith(h2ConsoleURI))) {
//            httpResponse.sendRedirect(loginURI); // 導向登入頁面
//            return;
//        }
        
        boolean isProtectedResource = 
                requestURI.startsWith(httpRequest.getContextPath() + "/lottery") ||
                requestURI.startsWith(httpRequest.getContextPath() + "/main") ||
                requestURI.equals(httpRequest.getContextPath() + "/") ||
                requestURI.equals(httpRequest.getContextPath() + "/index") ||
                requestURI.equals(httpRequest.getContextPath() + "/index.jsp");

		if (isProtectedResource && (session == null ||session.getAttribute("user") == null)) {
		// 如果未登入且訪問受保護資源，重定向到登入頁面
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
		} else {
		// 其他情況（例如，登入後訪問受保護資源），繼續處理請求
			chain.doFilter(request, response);
		}
	}
}

