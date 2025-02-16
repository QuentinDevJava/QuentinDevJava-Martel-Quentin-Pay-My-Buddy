package com.openclassrooms.paymybuddy.interceptors;

import static com.openclassrooms.paymybuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.LOGIN;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * Intercepts HTTP requests to check if the user is authenticated. If the user
 * is not authenticated, it redirects them to the login page. It ensures that
 * only authenticated users can access certain resources.
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

	/**
	 * Pre-handle method to check if the user session exists. If not, redirects to
	 * the login page.
	 * 
	 * @param request  the current HTTP request
	 * @param response the current HTTP response
	 * @param handler  the handler to execute
	 * @return true if the user is authenticated, false otherwise (redirects to
	 *         login page)
	 * @throws Exception if an error occurs during the process
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("preHandle : URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute(SESSION_ATTRIBUTE) == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect(LOGIN);
			return false;
		} else {
			String username = session.getAttribute(SESSION_ATTRIBUTE).toString();
			log.debug("username retrieved from current session: {}", username);
			return true;
		}
	}

	/**
	 * Post-handle method to check if the user session exists after handling the
	 * request. If the session is invalid, redirects to the login page.
	 * 
	 * @param request      the current HTTP request
	 * @param response     the current HTTP response
	 * @param handler      the handler that was executed
	 * @param modelAndView the model and view to be rendered
	 * @throws Exception if an error occurs during the process
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		log.debug("postHandle URL = " + request.getRequestURL() + " Methode = " + request.getMethod());
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute(SESSION_ATTRIBUTE) == null) {
			log.debug("Session is null redirect to authentication");
			response.sendRedirect(LOGIN);
		} else {
			String username = session.getAttribute(SESSION_ATTRIBUTE).toString();
			log.debug("username retrieved from current session: {}", username);
		}
	}

}
