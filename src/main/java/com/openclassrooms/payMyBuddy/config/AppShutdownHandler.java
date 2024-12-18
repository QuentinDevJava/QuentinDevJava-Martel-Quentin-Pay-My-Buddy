package com.openclassrooms.payMyBuddy.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppShutdownHandler {

	private final HttpServletRequest request;

	public AppShutdownHandler(HttpServletRequest request) {
		this.request = request;
	}

	@PreDestroy
	public void onShutdown() {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute("identifier");
			log.info("Session terminated, identifier removed from the session during shutdown.");
		}
	}
}