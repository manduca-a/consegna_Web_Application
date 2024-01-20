package org.unical.ingsw.persistenza.dao;

import org.unical.ingsw.persistenza.model.Documento;
import org.unical.ingsw.persistenza.model.Utente;

import java.util.List;
import java.util.Optional;

public interface DocumentoDao {
    List<Documento> findByUtente(Utente id_utente);
    Optional<Documento> findById(Long id);
    List<Documento> findByTipologia(String tipologia);
    List<Documento> findByTipologiaAndUtente(String tipologia, Utente utente);
    List<Documento> findAll();
    void deleteById(long id);
    void save(Documento newDocumento);
    void markAsDownloaded(Long docId);
}
