package org.unical.ingsw.persistenza.dao.postgres;

import org.unical.ingsw.persistenza.dao.DocumentoDao;
import org.unical.ingsw.persistenza.model.Documento;
import org.unical.ingsw.persistenza.model.Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentoDaoPostgres implements DocumentoDao {
    Connection connection;
    public DocumentoDaoPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Documento> findAll() {
        List<Documento> documenti = new ArrayList<>();
        String query = "SELECT d.*, u.* FROM documenti d INNER JOIN utente u ON d.utente_id = u.id";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Documento documento = new Documento();
                documento.setId(rs.getLong("d.id"));
                documento.setFilePath(rs.getString("d.file_path"));
                documento.setRelativePath(rs.getString("d.relative_path"));
                documento.setFileName(rs.getString("d.file_name"));
                documento.setScaricato(rs.getBoolean("d.scaricato"));
                documento.setDataEmissione(rs.getObject("d.data_emissione", LocalDate.class));
                documento.setDataScadenza(rs.getObject("d.data_scadenza", LocalDate.class));
                documento.setTipologia(rs.getString("d.tipologia"));
                documento.setDescrizione(rs.getString("d.descrizione"));

                // Caricamento dell'utente
                Utente utente = new Utente();
                utente.setId(rs.getLong("u.id"));
                utente.setCognome(rs.getString("u.cognome"));
                utente.setPassword(rs.getString("u.password"));
                utente.setArchiviato(Boolean.parseBoolean(rs.getString("u.archiviato")));
                utente.setAmministratore(Boolean.parseBoolean(rs.getString("u.amministratore")));
                utente.setFormatore(Boolean.parseBoolean(rs.getString("u.formatore")));
                utente.setNome(rs.getString("u.nome"));
                utente.setCognome(rs.getString("u.cognome"));
                utente.setData_di_nascita(rs.getDate("u.data_nascita") != null ? rs.getDate("u.data_nascita").toLocalDate() : null);

                documento.setUtente(utente);
                documenti.add(documento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documenti;
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM documenti WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'eliminazione del documento", e);
        }
    }

    @Override
    public void save(Documento newDocumento) {
        String query = "INSERT INTO documenti (file_path, relative_path, file_name, scaricato, data_emissione, data_scadenza, tipologia, descrizione, utente_id) VALUES (? , ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, newDocumento.getFilePath());
            pst.setString(2, newDocumento.getRelativePath());
            pst.setString(3, newDocumento.getFileName());
            pst.setBoolean(4, newDocumento.isScaricato());
            pst.setObject(5, newDocumento.getDataEmissione());
            pst.setObject(6, newDocumento.getDataScadenza());
            pst.setString(7, newDocumento.getTipologia());
            pst.setString(8, newDocumento.getDescrizione());
            pst.setLong(9, newDocumento.getUtente().getId());
            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long newId = generatedKeys.getLong(1);
                    newDocumento.setId(newId);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante il salvataggio del documento", e);
        }
    }

    @Override
    public void markAsDownloaded(Long docId) {
        String query = "UPDATE documenti SET scaricato = TRUE WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, docId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Documento> findByUtente(Utente utente) {
        List<Documento> documenti = new ArrayList<>();
        String query = "SELECT * FROM documenti WHERE utente_id = ?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, utente.getId());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Documento documento = new Documento();

                    documento.setId(rs.getLong("id"));
                    documento.setFilePath(rs.getString("file_path"));
                    documento.setRelativePath(rs.getString("relative_path"));
                    documento.setFileName(rs.getString("file_name"));
                    documento.setScaricato(rs.getBoolean("scaricato"));
                    documento.setDataEmissione(rs.getObject("data_emissione", LocalDate.class));
                    documento.setDataScadenza(rs.getObject("data_scadenza", LocalDate.class));
                    documento.setTipologia(rs.getString("tipologia"));
                    documento.setDescrizione(rs.getString("descrizione"));
                    documento.setUtente(utente);

                    documenti.add(documento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documenti;
    }

    @Override
    public Optional<Documento> findById(Long id) {
        String query = "SELECT * FROM documenti WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setLong(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Documento documento = new Documento();

                    documento.setId(rs.getLong("id"));
                    documento.setFilePath(rs.getString("file_path"));
                    documento.setRelativePath(rs.getString("relative_path"));
                    documento.setFileName(rs.getString("file_name"));
                    documento.setScaricato(rs.getBoolean("scaricato"));
                    documento.setDataEmissione(rs.getObject("data_emissione", LocalDate.class));
                    documento.setDataScadenza(rs.getObject("data_scadenza", LocalDate.class));
                    documento.setTipologia(rs.getString("tipologia"));
                    documento.setDescrizione(rs.getString("descrizione"));

                    return Optional.of(documento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public List<Documento> findByTipologia(String tipologia) {
        List<Documento> documenti = new ArrayList<>();
        String query = "SELECT * FROM documenti WHERE tipologia = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, tipologia);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Documento documento = new Documento();

                    documento.setId(rs.getLong("id"));
                    documento.setFilePath(rs.getString("file_path"));
                    documento.setRelativePath(rs.getString("relative_path"));
                    documento.setFileName(rs.getString("file_name"));
                    documento.setScaricato(rs.getBoolean("scaricato"));
                    documento.setDataEmissione(rs.getObject("data_emissione", LocalDate.class));
                    documento.setDataScadenza(rs.getObject("data_scadenza", LocalDate.class));
                    documento.setTipologia(rs.getString("tipologia"));
                    documento.setDescrizione(rs.getString("descrizione"));

                    documenti.add(documento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la ricerca dei documenti per tipologia", e);
        }
        return documenti;
    }

    @Override
    public List<Documento> findByTipologiaAndUtente(String tipologia, Utente utente) {
        List<Documento> documenti = new ArrayList<>();
        String query = "SELECT * FROM documenti WHERE tipologia = ? AND utente_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, tipologia);
            pst.setLong(2, utente.getId());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Documento documento = new Documento();
                    // Imposta le proprietà del documento in base ai risultati della query
                    documento.setId(rs.getLong("id"));
                    documento.setFilePath(rs.getString("file_path"));
                    documento.setRelativePath(rs.getString("relative_path"));
                    documento.setFileName(rs.getString("file_name"));
                    documento.setScaricato(rs.getBoolean("scaricato"));
                    documento.setDataEmissione(rs.getObject("data_emissione", LocalDate.class));
                    documento.setDataScadenza(rs.getObject("data_scadenza", LocalDate.class));
                    documento.setTipologia(rs.getString("tipologia"));
                    documento.setDescrizione(rs.getString("descrizione"));
                    documento.setUtente(utente);

                    documenti.add(documento);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la ricerca dei documenti per tipologia e utente", e);
        }
        return documenti;
    }


}
