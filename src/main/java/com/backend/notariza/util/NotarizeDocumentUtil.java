package com.backend.notariza.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class NotarizeDocumentUtil {
	
	PDDocument doc;
	DateFormatTH dateFormat  = new DateFormatTH();
	String tmpDirsLocation = System.getProperty("java.io.tmpdir")+"/";
	
	public NotarizeDocumentUtil() {
		
	}
	
	public int numberOfPages(File file) throws InvalidPasswordException, IOException {
		doc = PDDocument.load(file);
		int numberOfPages = doc.getNumberOfPages();
		
		doc.close();
		
		return numberOfPages;
	}

	public void loadDocument(File file, String output_name, boolean ctc) throws Exception, IOException {

		doc = PDDocument.load(file);

		int numberOfPages = doc.getNumberOfPages();

		for (int i = 0; i <= numberOfPages - 1; i++) {

			addSeal(i, output_name, ctc);
		}
	}

	// Method

	public void addSeal(int i, String output_name, boolean ctc) throws IOException {

		PDPage page = doc.getPage(i);
		PDRectangle pageSize = page.getMediaBox();
		
		PDPageContentStream image = new PDPageContentStream(doc, page, AppendMode.APPEND, true, true);

		float height = pageSize.getHeight();
		float width = pageSize.getWidth();
		
		//Signature...
		
		RandomReference random = new RandomReference();
		
    	Resource resource1 = new ClassPathResource("notariza_signature.png");
    	File file1 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath1 = file1.getAbsolutePath();
		PDImageXObject pdfSig = PDImageXObject.createFromFile(filePath1, doc);
		
		float newHeight1 = height - 80;
		image.drawImage(pdfSig, width - 120, height - newHeight1, 100, 100);
		System.out.println("Signature Inserted");
		
		//insert Stamp..
		Resource resource2 = new ClassPathResource("notariza_stamp1.png");
		File file2 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath2 = file2.getAbsolutePath();
		PDImageXObject pdfStamp = PDImageXObject.createFromFile(filePath2, doc);
		
		//CTC Stamp..
		
		Resource resource21 = new ClassPathResource("notariza_stamp_ctc.png");
		File file21 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource21.getInputStream(), file21.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath21 = file21.getAbsolutePath();
		PDImageXObject pdfCTC = PDImageXObject.createFromFile(filePath21, doc);
		
		float newHeight3 = height - 20;
		
		System.out.println("Signature Inserted");
		
		if(!ctc) {
			image.drawImage(pdfStamp, width - 220, height - newHeight3, 120, 120);
		}else {
			image.drawImage(pdfCTC, width - 220, height - newHeight3, 120, 120);
		}

		Resource resource11 = new ClassPathResource("notariza_seal.png");
		File file11 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource11.getInputStream(), file11.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath11 = file11.getAbsolutePath();

		PDImageXObject pdfimg = PDImageXObject.createFromFile(filePath11, doc);

		// set the Image inside the Page

		float newHeight = height - 20;
		image.drawImage(pdfimg, width -120, height - newHeight, 130, 130);
		System.out.println("Seal Inserted");
		
		
		image.beginText();
		 
		// Set the X and Y corodinates for the text to be positioned
		image.newLineAtOffset(width -150,  20);

		// Set a Font and its Size
		image.setFont(PDType1Font.COURIER_BOLD, 14);
		
        Calendar currentCalDate = Calendar.getInstance();
        
        String data =  dateFormat.getCurrentDateInSpecificFormat(currentCalDate);
		
		image.showText(data);

		// End the Stream
		image.endText();
		

		// Closing the page of PDF by closing
		// PDPageContentStream Object
		// && Saving the Document
		image.close();
		
		file1.delete();
		file2.delete();
		file21.delete();
		file11.delete();
		
		doc.save(output_name);

	}

}
