package com.openclassrooms.payMyBuddy.web.controller;


import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AuthenticationController {


    @GetMapping("/login")
    public String login(Model model) {
        LoginForm loginForm = new LoginForm();
        model.addAttribute("loginForm", loginForm);
        return "user/login";

    }

    @PostMapping("/login")
    public String authentication(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session) {
        log.info("attempt to authenticate user {}", loginForm.getUsername());
        // TODO 1 fetch from BD and validate credentials (cryptage pwd)
        // TODO 2 use binding result to show flash error message
        // TODO 3 try to use an interceptor to avoid duplication for checking user session
        if (("quentin".equals(loginForm.getUsername()) || "quentin@test.local".equals(loginForm.getUsername())) && "passer".equals(loginForm.getPassword())) {
            log.info("credentials match");
            session.setAttribute("username", loginForm.getUsername());
            return "redirect:/";
        } else {
            log.info("ERROR: credentials do not match");
            return "user/login";
        }
/*        if (userService.existsByEmailAndPassword(loginForm.getEmail(), loginForm.getPassword())) {
            User user = userService.getUserByEmail(loginForm.getEmail());
            int id = user.getId();
            return "redirect:/transaction/" + id;
        } else {
            // result.rejectValue("email", "error.loginForm", "Account does not exist or
            // incorrect credentials.");
            return "user/registration";
        }*/
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        log.info("attempt to logout user ");
        if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
            return "redirect:/login";
        }

        request.getSession().removeAttribute("username");
        log.info("username removed from the session. do logout");
        return "redirect:/login";
    }
}
