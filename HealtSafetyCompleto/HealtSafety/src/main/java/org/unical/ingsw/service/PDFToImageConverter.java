package org.unical.ingsw.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFToImageConverter {
    public static List<String> convertPDFToImage(String pdfPath) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        File pdfFile = new File(pdfPath);
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                String imagePath = "C:\\Users\\alex2\\Desktop\\documenti\\immaginiProvvisorie" + pdfFile.getName() + "-" + i + ".png";
                ImageIO.write(image, "PNG", new File(imagePath));
                imagePaths.add(imagePath);
            }
        }
        return imagePaths;
    }
}
