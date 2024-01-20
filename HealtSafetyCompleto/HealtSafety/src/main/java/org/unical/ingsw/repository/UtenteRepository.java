package org.unical.ingsw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.ingsw.persistenza.model.Utente;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {


    Optional<Utente> findByEmailAndPassword(String email, String password);
    List<Utente> findByNome(String nome);
    List<Utente> findByCognome(String cognome);
    List<Utente> findByNomeAndCognome(String nome, String cognome);
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
}
