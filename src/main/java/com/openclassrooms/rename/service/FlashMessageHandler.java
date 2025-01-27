package com.openclassrooms.rename.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Service to add flash messages to redirects.
 * 
 * This service allows adding success or error messages to redirect attributes
 * to be displayed on the next page.
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li><b>{@link #successMessage} :</b> Adds a success message to the redirect attributes.</li>
 *   <li><b>{@link #errorMessage} :</b> Adds an error message to the redirect attributes.</li>
 * </ul>
 */
@Service
public class FlashMessageHandler {

	/**
	 * Adds a success message to the redirect attributes.
	 * 
	 * @param redirAttrs The redirect attributes.
	 * @param message The success message to display.
	 */
	public void successMessage(RedirectAttributes redirAttrs, String message) {
		redirAttrs.addFlashAttribute("success", message);
	}

	/**
	 * Adds an error message to the redirect attributes.
	 * 
	 * @param redirAttrs The redirect attributes.
	 * @param message The error message to display.
	 */
	public void errorMessage(RedirectAttributes redirAttrs, String message) {
		redirAttrs.addFlashAttribute("error", message);
	}
}
