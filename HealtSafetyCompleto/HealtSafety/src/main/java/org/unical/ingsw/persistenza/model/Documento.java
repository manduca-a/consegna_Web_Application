package org.unical.ingsw.persistenza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)

    private Long id;
    private String filePath;     // Percorso assoluto
    private String relativePath; // Percorso relativo
    private String fileName;     // Nome del file
    private boolean scaricato;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data_emissione, data_scadenza;
    private String tipologia, descrizione;



    @ManyToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;


    public static List<String> getAvailableTipologie(){
        return Arrays.asList(
                "DOC. ID.",
                "ATTESTATO FORMAZIONE",
                "NOMINE",
                "MANSIONARIO",
                "VISITA MEDICA"
                );
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setDataEmissione(LocalDate dataEmissione) {
        this.data_emissione = dataEmissione;
    }
    public void setDataScadenza(LocalDate dataScadenza) {
        this.data_scadenza = dataScadenza;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public void setScaricato(boolean scaricato) {
        this.scaricato = scaricato;
    }
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    public Object getDataEmissione() {
        return data_emissione;
    }
    public Object getDataScadenza() {
        return data_scadenza;
    }
}