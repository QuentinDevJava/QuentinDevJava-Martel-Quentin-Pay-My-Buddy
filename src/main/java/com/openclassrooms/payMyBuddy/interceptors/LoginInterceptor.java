package com.openclassrooms.payMyBuddy.interceptors;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.openclassrooms.payMyBuddy.constants.AppConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

	   /**
     * Pre-handle method to check if the user session exists. If not, redirects to the login page.
     * 
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the handler to execute
     * @return true if the user is authenticated, false otherwise (redirects to login page)
     * @throws Exception if an error occurs during the process
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("preHandle : URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute(AppConstants.SESSION_ATTRIBUTE) == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect("/login");
			return false;
		} else {
			String username = session.getAttribute(AppConstants.SESSION_ATTRIBUTE).toString();
			log.debug("username retrieved from current session: {}", username);
			return true;
		}
	}

    /**
     * Post-handle method to check if the user session exists after handling the request.
     * If the session is invalid, redirects to the login page.
     * 
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param handler the handler that was executed
     * @param modelAndView the model and view to be rendered
     * @throws Exception if an error occurs during the process
     */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		log.debug("postHandle URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute(AppConstants.SESSION_ATTRIBUTE) == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect("/login");
		} else {
			String username = session.getAttribute(AppConstants.SESSION_ATTRIBUTE).toString();
			log.debug("username retrieved from current session: {}", username);
		}
	}

}
