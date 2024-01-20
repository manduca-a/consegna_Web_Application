document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById('registrationDoc');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Previene l'invio automatico del form

        // Recupero dei valori dal form
        const file = document.getElementById('file').value;
        const descrizione = document.getElementById('descrizione').value;
        const tipologia = document.getElementById('tipologia').value;
        const dataEmissione = document.getElementById('data_emissione').value;
        const dataScadenza = document.getElementById('data_scadenza').value;

        // Controllo se tutti i campi sono stati compilati
        if (!file || !descrizione || !tipologia) {
            alert('Si prega di compilare tutti i campi.');
            return;
        }

        // Validazione delle date
        // const oggi = new Date();
        // oggi.setHours(0, 0, 0, 0); // Reset dell'orario per il confronto solo sulle date
        // const emissione = new Date(dataEmissione);
        // const scadenza = new Date(dataScadenza);
        //
        // if (emissione > oggi) {
        //     alert('La data di emissione deve essere minore o uguale alla data odierna.');
        //     return;
        // }
        // if (scadenza <= oggi) {
        //     alert('La data di scadenza deve essere maggiore della data odierna.');
        //     return;
        // }

        // Riepilogo e conferma dell'invio
        const conferma = `Riepilogo:\nFile: ${file}\nDescrizione: ${descrizione}\nTipologia: ${tipologia}\nConfermi l'invio dei dati?`;
        if (confirm(conferma)) {
            form.submit(); // Invio del form se l'utente conferma
        }
    });
});
