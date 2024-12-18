package com.openclassrooms.payMyBuddy.interceptors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("username") == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect("/login");
			return false;
		} else {
			String username = session.getAttribute("username").toString();
			log.debug("username retrieved from current session: {}", username);
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		log.debug("URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("username") == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect("/login");
		} else {
			String username = session.getAttribute("username").toString();
			log.debug("username retrieved from current session: {}", username);
		}
	}

}
