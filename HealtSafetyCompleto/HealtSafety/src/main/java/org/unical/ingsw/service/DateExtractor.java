package org.unical.ingsw.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DateExtractor {
    public static LocalDate extractDate(String text, String datePattern) {
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            // Converti la stringa di testo in un oggetto LocalDate
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Usa il formato appropriato
            return LocalDate.parse(matcher.group(1), formatter);
        }
        return null;
    }
}
