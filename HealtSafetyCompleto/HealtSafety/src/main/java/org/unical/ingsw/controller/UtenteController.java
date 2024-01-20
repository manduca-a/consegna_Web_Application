package org.unical.ingsw.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.unical.ingsw.persistenza.model.Utente;
import org.unical.ingsw.repository.UtenteRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
class UtenteController {

    private final UtenteRepository utenteRepository;

    UtenteController(@Autowired  UtenteRepository repository) {
        this.utenteRepository = repository;
    }

    //TROVA TUTTI GLI UTENTI
    @GetMapping("/utenti")
    ModelAndView all(ModelMap model) {
        model.addAttribute("lista_utenti", utenteRepository.findAll());
        return new ModelAndView("utenti",model);
    }

    //CONTROLLA SE LA PASSWORD E L'EMAIL COINCIDONO
    @PostMapping("/home")
    ModelAndView login_utente(ModelMap model, HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Optional<Utente> userOptional = utenteRepository.findByEmail(email);

        if (userOptional.isPresent()){
            Utente utente = userOptional.get();

            String enpassword = utente.getPassword();

            if (BCrypt.checkpw(password, enpassword)){
                request.getSession().setAttribute("currentUser", utente);
                model.addAttribute("currentUser", utente);
            }
            else {
                System.out.println("Password errata");
            }

            if(utente.isAmministratore()) {
                return new ModelAndView("homeAmm", model);
            } else if(utente.isArchiviato()) {
                return new ModelAndView("index", model);
            } else if(utente.isFormatore()) {
                return new ModelAndView("homeForm", model);
            } else {
                return new ModelAndView("homeDip", model);
            }
        } else {
            model.addAttribute("errore", "Credenziali non valide");
            return new ModelAndView("errore", model);
        }
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
        return new ModelAndView("index");
    }


    //TROVA L'UTENTE TRAMITE: ID, NOME, COGNOME, NOMEeCOGNOME, EMAIL
    @GetMapping("/search_utente")
    ModelAndView findUtente(HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        try {
            String searchInput = request.getParameter("search");
            List<Utente> foundUtenti = new ArrayList<>();

            if (searchInput != null && !searchInput.isEmpty()) {
                if (searchInput.equalsIgnoreCase("all")){
                    foundUtenti.addAll(utenteRepository.findAll());
                }
                else if (searchInput.matches("\\d+")) {
                    Optional<Utente> utente = utenteRepository.findById(Long.valueOf(searchInput));
                    utente.ifPresent(foundUtenti::add);
                } else if (searchInput.contains("@")) {
                    Optional<Utente> utenteOpt = utenteRepository.findByEmail(searchInput);
                    utenteOpt.ifPresent(foundUtenti::add);
                } else {
                    String[] names = searchInput.split("\\s+");
                    if (names.length == 2) {
                        foundUtenti.addAll(utenteRepository.findByNomeAndCognome(names[0], names[1]));
                    } else {
                        foundUtenti.addAll(utenteRepository.findByNome(searchInput));
                        foundUtenti.addAll(utenteRepository.findByCognome(searchInput));
                    }
                }
                if (foundUtenti.isEmpty()) {
                    model.addAttribute("errore_cerca_utente", "Utente non trovato.");
                } else {
                    model.addAttribute("searchInput", searchInput );
                    model.addAttribute("utenti", foundUtenti);
                }
            } else {
                model.addAttribute("errore_cerca_utente", "Inserisci almeno un criterio di ricerca.");
            }
        } catch (NumberFormatException e) {
            model.addAttribute("errore_cerca_utente", "L'ID fornito non è valido.");
        }
        return new ModelAndView("gestioneDocumentiAmm", model);
    }

    //SALVA IL NUOVO UTENTE
    @PostMapping("/save_utente")
    ResponseEntity<?> newUtente(@ModelAttribute Utente newUtente) {
        if (utenteRepository.existsByEmail(newUtente.getEmail())) {
            // L'utente con questa email esiste già
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("errore", "Un utente con questa email esiste già."));
        }

        try {
            newUtente.setPassword(BCrypt.hashpw(newUtente.getPassword(), BCrypt.gensalt(12)));
            utenteRepository.save(newUtente);
            return ResponseEntity.ok(Collections.singletonMap("utente_aggiunto", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("errore", "Errore durante la registrazione dell'utente."));
        }
    }


    //ELIMINA UTENTI
    @PostMapping("/delete_utente")
    ModelAndView deleteUtente(@RequestParam("id") Long id,  HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        try{
            utenteRepository.deleteById(id);
            model.addAttribute("deleteMessage", "Documento eliminato con successo!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("deleteMessage", "Errore durante l'eliminazione del documento: " + e.getMessage());
        }
        return new ModelAndView("gestioneUtenti", model);
    }










    @PutMapping("/utente/{id}")
    Utente replaceUtente(@RequestBody Utente newUtente, @PathVariable Long id) {

        return utenteRepository.findById(id)
                .map(u -> {
                    u.setNome(newUtente.getNome());
                    u.setPassword(newUtente.getPassword());
                    u.setCognome(newUtente.getCognome());
                    u.setEmail(newUtente.getEmail());
                    u.setArchiviato(newUtente.isArchiviato());
                    u.setAmministratore(newUtente.isAmministratore());
                    u.setFormatore(newUtente.isFormatore());
                    u.setData_di_nascita(newUtente.getData_di_nascita());
                    return utenteRepository.save(u);
                })
                .orElseGet(() -> utenteRepository.save(newUtente));
    }
}