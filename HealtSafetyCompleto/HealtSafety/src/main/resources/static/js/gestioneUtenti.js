document.addEventListener('DOMContentLoaded', function() {
    var registrationForm = document.getElementById('registrationForm');
    var deleteForm = document.getElementById('deleteForm');

    registrationForm.onsubmit = function (event) {
        event.preventDefault();
        // Controlla se i campi sono compilati
        var nome = document.getElementById('nome').value;
        var cognome = document.getElementById('cognome').value;
        var data_di_nascita = document.getElementById('data_di_nascita').value;
        var email = document.getElementById('email').value;
        var password = document.getElementById('password').value;
        var confPassword = document.getElementById('confPassword').value;

        // Controlla se almeno una delle checkbox è selezionata
        var amministratore = document.getElementById('amministratore').checked;
        var formatore = document.getElementById('formatore').checked;
        var dipendente = document.getElementById('dip').checked;

        var ruolo = amministratore ? "Amministratore" : formatore ? "Formatore" : dipendente ? "Dipendente" : "Nessun ruolo selezionato";

        if (!isUserAdult(data_di_nascita)) {
            alert('Devi essere maggiorenne per registrarti.');
            return false;
        }

        if (!nome || !cognome || !data_di_nascita || !email || !password || !confPassword) {
            alert('Per favore, compila tutti i campi richiesti.');
            return false;
        }

        if (!amministratore && !formatore && !dipendente) {
            alert('Per favore, seleziona almeno un ruolo.');
            return false;
        }

        // Controlla se la password e la conferma della password coincidono
        if (password !== confPassword) {
            alert('Le password non coincidono. Riprova.');
            return false;
        }

        var riepilogo = "Confermi i seguenti dati?\n";
        riepilogo += "Nome: " + nome + "\n";
        riepilogo += "Cognome: " + cognome + "\n";
        riepilogo += "Data di nascita: " + data_di_nascita + "\n";
        riepilogo += "Email: " + email + "\n";
        riepilogo += "Password: " + password + "\n"; // Includere la password nel riepilogo può essere rischioso per la sicurezza
        riepilogo += "Ruolo: " + ruolo + "\n"; // Aggiungi il ruolo al riepilogo

        // Se l'utente clicca "OK", invia il modulo con AJAX
        if (confirm(riepilogo)) {
            var formData = new FormData(registrationForm);
            fetch('/save_utente', {
                method: 'POST',
                body: formData
            })
                .then(function (response) {
                    return response.json();
                })
                .then(function (data) {
                    if (data.utente_aggiunto) {
                        alert('Utente aggiunto con successo!');
                        registrationForm.reset();
                    } else {
                        // Gestisci il messaggio di errore restituito dal server
                        alert(data.errore || "Si è verificato un errore durante l'aggiunta dell'utente.");
                    }
                })
                .catch(function (error) {
                    alert('Si è verificato un errore nella richiesta.');
                });
        } else {
            alert('Registrazione annullata.');
        }

        deleteForm.onsubmit = function(event) {
            event.preventDefault();
            var userId = document.getElementById('id').value;
            var isConfirmed = confirm("Sicuro di voler eliminare l'utente con ID: " + userId + "?");
            if (!isConfirmed) {
                return;
            }

            fetch('/delete_utente/' + userId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
                .then(function(response) {
                    if (!response.ok) {
                        throw new Error('Errore nella risposta del server');
                    }
                    return response.json();
                })
                .then(function(data) {
                    // ... handle response ...
                })
                .catch(function(error) {
                    alert('Si è verificato un errore: ' + error.message);
                });
        };
    };
});

function isUserAdult(birthDateString) {
    var today = new Date();
    var birthDate = new Date(birthDateString);
    var age = today.getFullYear() - birthDate.getFullYear();
    var m = today.getMonth() - birthDate.getMonth();
    if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }
    return age >= 18;
}