package com.openclassrooms.payMyBuddy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class FlashAttribute {

	public void successMessage(RedirectAttributes redirAttrs, String string) {
		redirAttrs.addFlashAttribute("success", string);
	}

	public void errorMessage(RedirectAttributes redirAttrs, String string) {
		redirAttrs.addFlashAttribute("error", string);
	}
}
