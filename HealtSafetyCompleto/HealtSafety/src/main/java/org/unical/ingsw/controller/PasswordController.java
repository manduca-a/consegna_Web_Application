package org.unical.ingsw.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.unical.ingsw.persistenza.model.Utente;
import org.unical.ingsw.repository.UtenteRepository;
import org.unical.ingsw.service.EmailService;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UtenteRepository utenteRepository;

    PasswordController(@Autowired UtenteRepository repository) {
        this.utenteRepository = repository;
    }

    @PostMapping("/recuperoPassServices")
    ModelAndView processForgotPassword(@RequestParam("email") String email) throws UnsupportedEncodingException {
        String token = UUID.randomUUID().toString();
        Optional<Utente> utenteOpt = utenteRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            utente.setToken(token);
            utenteRepository.save(utente);
            emailService.sendPasswordResetEmail(email, token);
        }

        ModelAndView modelAndView = new ModelAndView("recuperoPass");
        modelAndView.addObject("message", "Riceverai un'email con le istruzioni per reimpostare la tua password.");
        return modelAndView;
    }

    @GetMapping("/resetPassword")
    public ModelAndView showResetPasswordForm(@RequestParam("token") String token, @RequestParam("email") String email, ModelMap model) {
        ModelAndView modelAndView = new ModelAndView("resetPassword");
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return modelAndView;
    }

    @PostMapping("/resetPassword")
    ModelAndView resetPassword(@RequestParam("token") String token,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return new ModelAndView("errore");
        }

        Optional<Utente> utenteOpt = utenteRepository.findByEmail(email);

        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();


            if (utente.getToken() != null && utente.getToken().equals(token)) {
                utente.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
                utente.setToken(null);
                utenteRepository.save(utente);
                return new ModelAndView("index");

            }
        }
        return new ModelAndView("recuperoPass");
    }
}
