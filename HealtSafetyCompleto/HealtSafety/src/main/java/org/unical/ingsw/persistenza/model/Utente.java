package org.unical.ingsw.persistenza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Utente {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)

    private Long id;
    private String password, nome, cognome, email;
    private boolean archiviato, amministratore, formatore;
    private LocalDate data_di_nascita;
    private String token;

    public static List<String> getAvailableRoles(){
        return Arrays.asList(
                "DIPENDENTE",
                "AMMINISTRATORE",
                "FORMATORE");

    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setArchiviato(boolean archiviato) {
        this.archiviato = archiviato;
    }
    public void setAmministratore(boolean amministratore) {
        this.amministratore = amministratore;
    }
    public void setFormatore(boolean formatore) {
        this.formatore = formatore;
    }
    public void setData_di_nascita(LocalDate data_di_nascita) {
        this.data_di_nascita = data_di_nascita;
    }
    public Object getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
