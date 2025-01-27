package com.openclassrooms.paymybuddy.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles application shutdown by removing the 'username' attribute from the session.
 */
@Slf4j
@Component
@Profile("!test")
public class AppShutdownHandler {

    /** The HTTP request. */
    private final HttpServletRequest request;

    /**
     * Instantiates a new app shutdown handler.
     * 
     * @param request The HTTP request.
     */
    public AppShutdownHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Removes the 'username' attribute from the session during application shutdown.
     * This method is called before the bean is destroyed.
     */
    @PreDestroy
    public void onShutdown() {

        HttpSession session = request.getSession(false);
        if (session != null) {
            if (session.getAttribute("username") != null) {
                session.removeAttribute("username");
                log.debug("Session terminated, identifier removed from the session during shutdown.");
            } else {
                log.debug("Session exists, but no 'username' attribute found to remove.");
            }
        } else {
            log.debug("No active session found during shutdown.");
        }
    }
}