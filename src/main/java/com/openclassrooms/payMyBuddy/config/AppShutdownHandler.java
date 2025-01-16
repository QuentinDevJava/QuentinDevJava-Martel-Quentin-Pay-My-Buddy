package com.openclassrooms.payMyBuddy.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
public class AppShutdownHandler {

	private final HttpServletRequest request;

	public AppShutdownHandler(HttpServletRequest request) {
		this.request = request;
	}

	@PreDestroy
	public void onShutdown() {

		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("username") != null) {
				session.removeAttribute("username");
				log.debug("Session terminated, identifier removed from the session during shutdown.");
			} else {
				log.info("Session exists, but no 'username' attribute found to remove.");
			}
		} else {
			log.info("No active session found during shutdown.");
		}
	}
}