package com.backend.notariza.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.backend.notariza.entity.AgeDeclarationEntity;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;

public class AgeDeclarationPDF {
	DateFormatTH dateFormat = new DateFormatTH();

	PdfFont font;

	String content1 = "I hereby certify and attest as a duly authorized, appointed and practicing Notary Public of "
			+ "the Federal Republic of Nigeria, that the hereto attached document headed";

	String content2 = "is a copy of the original presented to me for notarization in Nigeria";

	String this1 = "on the ";

	String documentHeader = "AFFIDAVIT FOR DECLARATION OF AGE";

	public void getPDFDocument(String filename, AgeDeclarationEntity ageEntity, String notaryName) throws Exception {

		// filename = tmpDirsLocation+filename;

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename + ".pdf",
				new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
		Document document = new Document(pdfDoc, PageSize.A4);
		pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

		pdfDoc.getCatalog().setLang(new PdfString("en-IN"));
		pdfDoc.getDocumentInfo().setTitle("Age Declaration Document");

		PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

		PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

		pdfDoc.addFont(bold);

		Paragraph p1 = new Paragraph();

		p1.setFontSize(18);

		p1.setMarginBottom(20);
		p1.setMarginTop(30);

		Text title = new Text("AFFIDAVIT FOR DECLARATION OF AGE").setFont(bold);

		p1.add(title);

		p1.setTextAlignment(TextAlignment.CENTER);

		document.add(p1);

		Paragraph intro = new Paragraph();

		intro.setFontSize(13);

		Text taddress = new Text(ageEntity.getAddress()).setFont(regular);

		String fullName = ageEntity.getLastname().toUpperCase() + " " + ageEntity.getFirstname().toUpperCase();

		Text tnewName = new Text(fullName).setFont(bold);

		Text makeOath = new Text("MAKE OATH").setFont(bold);
		Text state = new Text("STATE").setFont(bold);
		Text tsex = new Text(ageEntity.getSex());

		intro.add(" I ").add(tnewName).add(" ").add(tsex).add(" , of ").add(taddress).add(" ").add(makeOath)
				.add(" and ").add(state).add(" as follows:");

		intro.setMargin(15);

		document.add(intro);

		ListItem listItem1 = new ListItem();
		ListItem listItem2 = new ListItem();
		ListItem listItem3 = new ListItem();
		ListItem listItem4 = new ListItem();
		ListItem listItem5 = new ListItem();
		ListItem listItem6 = new ListItem();
		ListItem listItem7 = new ListItem();

		Paragraph pL = new Paragraph();

		pL.setMargins(5, 5, 5, 5);

		Paragraph pL1 = new Paragraph();

		pL1.setMargins(5, 5, 5, 5);

		Paragraph pL2 = new Paragraph();

		pL2.setMargins(5, 5, 5, 5);

		Paragraph pL3 = new Paragraph();

		pL3.setMargins(5, 5, 5, 5);
		// pL3.setFontSize(13);

		Paragraph pL4 = new Paragraph();

		pL4.setMargins(5, 5, 5, 5);
		// pL4.setFontSize(13);

		Paragraph pL5 = new Paragraph();

		pL5.setMargins(5, 5, 5, 5);
		// pL5.setFontSize(13);

		Paragraph pL6 = new Paragraph();

		pL5.setMargins(5, 5, 5, 5);

		List theList = new List(ListNumberingType.DECIMAL);

		theList.setPaddings(5, 5, 5, 5);
		theList.setTextAlignment(TextAlignment.LEFT);
		theList.setMarginLeft(30);

		pL.add("I am the above named person in this affidavit.");
		listItem1.add(pL);
		theList.add(listItem1);

		String rSex = "";

		if (ageEntity.isOwner() == false) {

			String ownerName = ageEntity.getOwnerName();
			String relationship = ageEntity.getRelationshipToOwner();

			Text tOName = new Text(ownerName).setFont(bold);
			Text tRel = new Text(relationship).setFont(bold);

			pL1.add("That ").add(tOName).add(" is my ").add(tRel);
			listItem2.add(pL1);
			theList.add(listItem2);

			String oSex = ageEntity.getOwnerSex();

			if (oSex.equalsIgnoreCase("MALE")) {
				rSex = "he";
			} else {
				rSex = "she";
			}

		} else {
			rSex = "I";
		}

		String dateTrans = dateFormat.getCurrentDateInSpecificFormatDate(ageEntity.getDob()) + ".";

		pL2.add("That ").add(rSex).add(" was given birth to in ").add(ageEntity.getPlaceOfBirth()).add(" on the ")
				.add(dateTrans);

		pL3.add("That the birth was duly registerd.");
		pL4.add("That the birth certificate ").add(ageEntity.getReason()).add(".");
		pL5.add("That this sworn affidavit is now required for record and official purposes.");
		pL6.add("That I depose to this affidavit in good faith and in accordance with the oath law of Nigeria.");

		listItem3.add(pL2);
		listItem4.add(pL3);
		listItem5.add(pL4);
		listItem6.add(pL5);

		listItem7.add(pL6);

		theList.add(listItem3);
		theList.add(listItem4);
		theList.add(listItem5);
		theList.add(listItem6);
		theList.add(listItem7);

		document.add(theList);

		Paragraph depo = new Paragraph();

		depo.setMarginTop(30);

		depo.add("-------------------------------------");

		depo.setMarginLeft(370);

		document.add(depo);

		Paragraph depo1 = new Paragraph();

		Text deps = new Text("Deponent").setFont(bold);

		depo1.add(deps);

		depo1.setMarginTop(2);
		depo1.setMarginLeft(420);
		document.add(depo1);

		// Sworn before me..

		Paragraph notp = new Paragraph();

		Text notary_public = new Text("SWORN BEFORE ME: ").setFont(bold);

		Text NNAME = new Text(notaryName).setFont(bold);

		NNAME.setUnderline(2, -2);

		notp.add(notary_public).add(NNAME);
		notp.setMarginTop(10);

		document.add(notp);

		Calendar currentCalDate = Calendar.getInstance();

		String data = dateFormat.getCurrentDateInSpecificFormat(currentCalDate);

		Text day = new Text(data).setFont(bold);

		Text on = new Text("Date: ").setFont(bold);

		Paragraph dates = new Paragraph();

		dates.add(on).add(day);

		// dates.setFontSize(16);

		document.add(dates);

		String tmpDirsLocation = System.getProperty("java.io.tmpdir")+"/";

		Resource resource1 = new ClassPathResource("notariza_signature.png");
		
		RandomReference random = new RandomReference();

		File file1 = new File(tmpDirsLocation + random.getAlphaNumericString(10)+ ".png");
		Files.copy(resource1.getInputStream(), file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath1 = file1.getAbsolutePath();

		Paragraph pSig = new Paragraph();
		Image img1 = new Image(ImageDataFactory.create(filePath1));
		img1.getAccessibilityProperties().setAlternateDescription("notary signature");
		pSig.add(img1);

		img1.setMaxWidth(150);

		pSig.setTextAlignment(TextAlignment.LEFT);

		Resource resource2 = new ClassPathResource("notariza_stamp1.png");
		File file2 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource2.getInputStream(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		String filePath2 = file2.getAbsolutePath();

		Paragraph pStamp1 = new Paragraph();
		Image img2 = new Image(ImageDataFactory.create(filePath2));
		img2.getAccessibilityProperties().setAlternateDescription("notary stamp");
		pStamp1.add(img2);

		Resource resource3 = new ClassPathResource("notariza_seal_red.png");
		File file3 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
		Files.copy(resource3.getInputStream(), file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
		String filePath3 = file3.getAbsolutePath();

		Paragraph pSeal = new Paragraph();
		Image img3 = new Image(ImageDataFactory.create(filePath3));
		img3.getAccessibilityProperties().setAlternateDescription("notary seal");
		pSeal.add(img2).add(img3);

		img2.setMaxWidth(130);

		img3.setMaxWidth(130);

		pSeal.setTextAlignment(TextAlignment.LEFT);

		pSig.setMarginLeft(10);
		pSig.setMarginBottom(0);

		pSeal.setMarginTop(0);
		pSeal.setMarginLeft(10);

		document.add(pSig);
		document.add(pSeal);
		
        try {
        	
        	file1.delete();
        	file2.delete();
        	file3.delete();
        	
        }catch(Exception nn) {
        	
        }

		document.close();

	}

}
