package org.unical.ingsw.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.unical.ingsw.persistenza.model.Utente;

@Controller
public class PageController {

    @GetMapping("/home")
    ModelAndView home(HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        if (currentUser != null) {

            model.addAttribute("currentUser", currentUser);

            if (currentUser.isAmministratore()) {
                return new ModelAndView("homeAmm", model);
            } else if (currentUser.isFormatore()) {
                return new ModelAndView("homeForm", model);
            } else {
                return new ModelAndView("homeDip", model);
            }
        }
        return new ModelAndView("index");
    }

    @GetMapping("/dati")
    ModelAndView gestioneDati(HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            if (currentUser.isAmministratore()) {
                return new ModelAndView("gestioneDatiAmm", model);
            } else if (currentUser.isFormatore()) {
                return new ModelAndView("gestioneDatiForm", model);
            } else {
                return new ModelAndView("gestioneDatiDip", model);
            }
        }
        return new ModelAndView("index");
    }

    @GetMapping("/documenti/")
    ModelAndView gestioneDocumenti(HttpServletRequest request, ModelMap model){
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            if (currentUser.isAmministratore()) {
                return new ModelAndView("gestioneDocumentiAmm", model);
            } else if (currentUser.isFormatore()) {
                return new ModelAndView("gestioneDocumentiForm", model);
            } else {
                return new ModelAndView("gestioneDocumentiDip", model);
            }
        }
        return new ModelAndView("index");
    }

    @GetMapping("/utenti/")
    ModelAndView registraUtente(HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);

            if (currentUser.isAmministratore()) {
                return new ModelAndView("gestioneUtenti", model);
            }
        }
        return new ModelAndView("index");
    }

    @GetMapping("")
    ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/recupero")
    ModelAndView recuperoPass() {
        return new ModelAndView("recuperoPass");
    }
}
