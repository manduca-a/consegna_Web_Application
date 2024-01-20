//package org.unical.ingsw.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.unical.ingsw.persistenza.model.Documento;
//import org.unical.ingsw.persistenza.model.Utente;
//
//import java.util.List;
//
//public interface DocumentoRepository extends JpaRepository<Documento, Long> {
//
//    List<Documento> findByUtente(Utente id_utente);
//
//    List<Documento> findByTipologia(String tipologia);
//
//    List<Documento> findByTipologiaAndUtente(String tipologia, Utente utente);
//}
