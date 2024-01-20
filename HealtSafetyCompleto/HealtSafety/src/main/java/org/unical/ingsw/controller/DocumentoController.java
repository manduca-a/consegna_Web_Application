package org.unical.ingsw.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.unical.ingsw.persistenza.DBManager;
import org.unical.ingsw.persistenza.dao.DocumentoDao;
import org.unical.ingsw.persistenza.model.Documento;
import org.unical.ingsw.persistenza.model.Utente;
import org.unical.ingsw.repository.UtenteRepository;
import org.unical.ingsw.service.DateExtractor;
import org.unical.ingsw.service.OCRProcessor;
import org.unical.ingsw.service.PDFToImageConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.unical.ingsw.persistenza.model.Documento.getAvailableTipologie;

@RestController
class DocumentoController {

    private final DocumentoDao documentoDao;
    private final UtenteRepository utenteRepository;


    public DocumentoController(UtenteRepository utenteRepository) {
        this.documentoDao = DBManager.getInstance().getDocumentoDao();
        this.utenteRepository = utenteRepository;
    }

    //TROVA TUTTI I DOCUMENTI
    @GetMapping("/documenti")
    String findAll(ModelMap model) {
        List<Documento> documenti = documentoDao.findAll();
        model.addAttribute("documenti", documenti);
        return "errore";
    }

    //TROVA I DOCUMENTI DELL'UTENTE TRAMITE ID UTENTE
    @GetMapping("/utente-documenti/{id_utente}")
    ModelAndView findByUtente(@PathVariable("id_utente") Long id_utente, ModelMap model, HttpServletRequest request) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        Utente utente = utenteRepository.findById(id_utente)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utente non trovato"));

        List<Documento> documenti = documentoDao.findByUtente(utente);
        model.addAttribute("utente", utente);
        model.addAttribute("documenti", documenti);
        model.addAttribute("tipologie", getAvailableTipologie());


        if (currentUser.isAmministratore()) {
            return new ModelAndView("documentiAmm", model);
        } else if (currentUser.isFormatore()) {
            return new ModelAndView("documentiForm", model);
        } else {
            return new ModelAndView("gestioneDocumentiDip", model);
        }

    }

    //TROVA I DOCUMENTI IN BASE ALL'ID DEL DOCUMENTO
    @GetMapping("/cerca_documento")
    ModelAndView findDocumentoById(@RequestParam("search-id") Long id,
                                   @RequestParam("userId") Long userId,
                                   HttpServletRequest request,
                                   ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        Documento documento = documentoDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento non trovato con l'ID: " + id));

        Utente utente = utenteRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        model.addAttribute("utente", utente);
        model.addAttribute("documenti", documento);

        if (currentUser.isAmministratore()) {
            return new ModelAndView("documentiAmm", model);
        } else if (currentUser.isFormatore()) {
            return new ModelAndView("documentiForm", model);
        } else {
            return new ModelAndView("gestioneDocumentiDip", model);
        }
    }

    @PostMapping("/uploadAll")
    public ModelAndView saveAll(@ModelAttribute Documento newDocumento,
                                @RequestParam("id_utenti") String id_utenti,
                                @RequestParam("file") MultipartFile file,
                                HttpServletRequest request,
                                ModelMap model) throws IOException {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);


        String[] id = id_utenti.split("/");

        long l;
        for (String i:id){
            l = Long.parseLong(i);

            Utente utente = utenteRepository.findById(l)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            newDocumento.setUtente(utente);
            newDocumento.setScaricato(false);

            if (!file.isEmpty()) {
                String baseFolderPath = "C:\\Users\\alex2\\Desktop\\documenti\\immaginiProvvisorie";
                File userDirectory = new File(baseFolderPath, "utente_" + i);

                if (!userDirectory.exists()) {
                    boolean dirCreated = userDirectory.mkdirs();
                    if (!dirCreated) {
                        throw new IOException("Impossibile creare la directory: " + userDirectory.getAbsolutePath());
                    }
                }

                String filename = file.getOriginalFilename();
                assert filename != null;
                File fileToSave = new File(userDirectory, filename);
                file.transferTo(fileToSave);

                String pdfPath = fileToSave.getAbsolutePath();
                List<String> imagePaths = PDFToImageConverter.convertPDFToImage(pdfPath);
                LocalDate dataEmissione = null;
                LocalDate dataScadenza = null;

                for (String imagePath : imagePaths) {
                    String text = OCRProcessor.extractTextFromImage(imagePath);
                    System.out.println("Testo estratto: " + text);

                    if (dataEmissione == null) {
                        dataEmissione = DateExtractor.extractDate(text, "Prima Data: (\\d{2}/\\d{2}/\\d{4})");
                    }
                    if (dataScadenza == null) {
                        dataScadenza = DateExtractor.extractDate(text, "Seconda Data .*?: (\\d{2}/\\d{2}/\\d{4})");
                    }
                }

                System.out.println(dataEmissione);
                System.out.println(dataScadenza);

                newDocumento.setDataEmissione(dataEmissione);
                newDocumento.setDataScadenza(dataScadenza);

                model.addAttribute("uploadMessage", "Il documento è stato caricato e processato correttamente");
            } else {
                model.addAttribute("uploadMessage", "Il file è vuoto e non è stato caricato.");
            }

            documentoDao.save(newDocumento);

            model.addAttribute("utente", utente);
            model.addAttribute("documenti", documentoDao.findByUtente(utente));
            model.addAttribute("tipologie", getAvailableTipologie());
        }

        return new ModelAndView("getioneDocumentiAmm", model);
    }

    //CARICA DOCUMENTI
    @PostMapping("/upload")
    public ModelAndView save(@ModelAttribute Documento newDocumento,
                             @RequestParam("userId") Long userId,
                             @RequestParam("file") MultipartFile file,
                             HttpServletRequest request,
                             ModelMap model) {

        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        try {
            Utente utente = utenteRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            newDocumento.setUtente(utente);
            newDocumento.setScaricato(false);

            System.out.println(newDocumento);

            if (!file.isEmpty()) {
                String baseFolderPath = "C:\\Users\\alex2\\Desktop\\documenti";
                String userFolderPath = "utente_" + userId;

                File userDirectory = new File(baseFolderPath, userFolderPath);

                if (!userDirectory.exists()) {
                    boolean dirCreated = userDirectory.mkdirs();
                    if (!dirCreated) {
                        throw new IOException("Impossibile creare la directory: " + userDirectory.getAbsolutePath());
                    }
                }

                String filename = file.getOriginalFilename();
                if (filename != null) {
                    File fileToSave = new File(userDirectory, filename);
                    file.transferTo(fileToSave);

                    newDocumento.setFilePath(fileToSave.getAbsolutePath());

                    System.out.println("file to save" + fileToSave);

                    String relativePath = userFolderPath + "/" + filename;
                    newDocumento.setRelativePath(relativePath);
                    newDocumento.setFileName(filename);

                    System.out.println("relative path" + relativePath);

                    String pdfPath = fileToSave.getAbsolutePath();
                    List<String> imagePaths = PDFToImageConverter.convertPDFToImage(pdfPath);
                    LocalDate dataEmissione = null;
                    LocalDate dataScadenza = null;

                    for (String imagePath : imagePaths) {
                        String text = OCRProcessor.extractTextFromImage(imagePath);
                        System.out.println("Testo estratto: " + text);

                        if (dataEmissione == null) {
                            dataEmissione = DateExtractor.extractDate(text, "Prima Data: (\\d{2}/\\d{2}/\\d{4})");
                        }
                        if (dataScadenza == null) {
                            dataScadenza = DateExtractor.extractDate(text, "Seconda Data .*?: (\\d{2}/\\d{2}/\\d{4})");
                        }
                    }

                    newDocumento.setDataEmissione(dataEmissione);
                    newDocumento.setDataScadenza(dataScadenza);

                    documentoDao.save(newDocumento);
                    model.addAttribute("uploadMessage", "Il documento è stato caricato e processato correttamente");
                } else {
                    model.addAttribute("uploadMessage", "Errore nel nome del file.");
                }
            } else {
                model.addAttribute("uploadMessage", "Il file è vuoto e non è stato caricato.");
            }

            model.addAttribute("utente", utente);
            model.addAttribute("documenti", documentoDao.findByUtente(utente));
            model.addAttribute("tipologie", getAvailableTipologie());

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("uploadMessage", "Errore durante il caricamento del documento: " + e.getMessage());
        }

        return new ModelAndView("documentiAmm", model);
    }

    private static File getFile(Long userId, String baseFolderPath) throws IOException {
        String userFolderPath = baseFolderPath + File.separator + "utente_" + userId;
        File userDirectory = new File(userFolderPath);
        if (!userDirectory.exists()){
            boolean isCreated = userDirectory.mkdirs();
            if (!isCreated) {
                throw new IOException("Impossibile creare la directory: " + userDirectory.getAbsolutePath());
            }
        }
        return userDirectory;
    }

    @PostMapping("/mark-as-downloaded/{docId}")
    public ResponseEntity<?> markAsDownloaded(@PathVariable("docId") Long docId) {
        System.out.println("sono dentro");
        documentoDao.markAsDownloaded(docId);
        System.out.println("sono fuori");
        return ResponseEntity.ok().build();
    }

    //ELIMINA DOCUMENTI
    @PostMapping("/delete_documento")
    ModelAndView deleteDocumento(@RequestParam("id") long id, @RequestParam("userId") Long userId, HttpServletRequest request, ModelMap model) {
        Utente currentUser = (Utente) request.getSession().getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        try {
            Utente utente = utenteRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
            documentoDao.deleteById(id);

            List<Documento> documenti = documentoDao.findByUtente(utente);

            List<String> tipologie = getAvailableTipologie();

            model.addAttribute("utente", utente);
            model.addAttribute("documenti", documenti);
            model.addAttribute("tipologie", tipologie);

            model.addAttribute("deleteMessage", "Documento eliminato con successo!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("deleteMessage", "Errore durante l'eliminazione del documento: " + e.getMessage());
        }
        return new ModelAndView("documentiAmm", model);
    }

    //TROVA TUTTI I DOCUMENTI TRAMITE TIPOLOGIA
    @GetMapping("/tipologia-documenti/{tipologia}")
    ModelAndView findByTipologia(@PathVariable String tipologia, ModelMap model) {
        try {
            List<Documento> documenti = documentoDao.findByTipologia(tipologia);

            model.addAttribute("documenti", documenti);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("documentiAmm", model);
    }


    //TROVA I DOCUMENTI DI QUELLA TIPOLOGIA DI QUELL'UTENTE
    @GetMapping("/documenti/{tipologia}/{id_utente}")
    ModelAndView findByTipologiaAndUtente(@PathVariable String tipologia, @PathVariable Utente id_utente, ModelMap model) {

        Utente utente = utenteRepository.findById(id_utente.getId()).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Documento> documenti =  documentoDao.findByTipologiaAndUtente(tipologia,utente);

        model.addAttribute("utente", utente);
        model.addAttribute("documenti", documenti);

        return new ModelAndView("documentiAmm", model);
    }

}