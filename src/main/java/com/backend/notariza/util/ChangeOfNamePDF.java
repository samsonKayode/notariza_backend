package com.backend.notariza.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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


public class ChangeOfNamePDF {
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	DateFormatTH dateFormat  = new DateFormatTH();
	
	PdfFont font;

	String content1 = "I hereby certify and attest as a duly authorized, appointed and practicing Notary Public of "
			+ "the Federal Republic of Nigeria, that the hereto attached document headed";

	String content2 = "is a copy of the original presented to me for notarization in Nigeria";

	String this1 = "on the ";

	String documentHeader = "SWORN AFFIDAVIT OF CHANGE OF NAME";
	
	public void getPDFDocument(String filename, String oldName, String newName, String address, String sex, String notaryName) throws Exception {
		
		//filename = tmpDirsLocation+filename;
		
		
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename+".pdf",
                new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
        Document document = new Document(pdfDoc, PageSize.A4);
        pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
        
        pdfDoc.getCatalog().setLang(new PdfString("en-IN"));
        pdfDoc.getDocumentInfo().setTitle("Change Of Name Document");
        
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
        
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        pdfDoc.addFont(bold);
        
        Paragraph p1 = new Paragraph();
        
        p1.setFontSize(18);
        p1.setRelativePosition(80, 10, 80, 10);
        
        p1.setMarginBottom(20);
        p1.setMarginTop(30);
        
        Text title = new Text("SWORN AFFIDAVIT OF CHANGE OF NAME").setFont(bold);
        
        p1.add(title);
        
        document.add(p1);
        
        Paragraph intro = new Paragraph();
        
        intro.setFontSize(13);
        
        Text taddress = new Text(address).setFont(regular);
        
        Text tnewName = new Text(newName.toUpperCase()).setFont(bold);
        Text toldName = new Text(oldName.toUpperCase()).setFont(bold);
        
        Text makeOath = new Text("MAKE OATH").setFont(bold);
        Text state = new Text("STATE").setFont(bold);
        Text tsex = new Text(sex);

        
        intro.add(" I ").add(tnewName).add(" ").add(tsex).add(" , of ").add(taddress).add(" ").add(makeOath) .add(" and ").add(state).add(" as follows:");
        
        intro.setMargin(15);
        
        document.add(intro);

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
        //pL5.setFontSize(13);
        
        List theList = new List(ListNumberingType.DECIMAL);
        
        theList.setPaddings(5, 5, 5, 5);
		theList.setTextAlignment(TextAlignment.LEFT);
		theList.setMarginLeft(30);
        
        pL.add("I am the above named person in this affidavit.");
        pL1.add("That I was formerly known and addressed as ").add(toldName);
        pL2.add("That I now wish to be known, called and addressed as ").add(tnewName).add(" hence, this affidavit.");
        pL3.add("That all previous documents bearing my former name remains valid.");
        pL4.add("That this sworn affidavit is now required for record and official purposes.");
        pL5.add("That I depose to this affidavit in good faith and in accordance with the oath law of Nigeria.");
        
        ListItem listItem1 = new ListItem();
        ListItem listItem2 = new ListItem();
        ListItem listItem3 = new ListItem();
        ListItem listItem4 = new ListItem();
        ListItem listItem5 = new ListItem();
        ListItem listItem6 = new ListItem();
        
        listItem1.add(pL);
        listItem2.add(pL1);
        listItem3.add(pL2);
        listItem4.add(pL3);
        listItem5.add(pL4);
        listItem6.add(pL5);
        
        
        
        theList.add(listItem1);
        theList.add(listItem2);
        theList.add(listItem3);
        theList.add(listItem4);
        theList.add(listItem5);
        theList.add(listItem6);
        
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
       
        
        //Sworn before me..
        
        Paragraph notp = new Paragraph();
        
        Text notary_public = new Text("SWORN BEFORE ME: ").setFont(bold);
        
        Text NNAME = new Text(notaryName).setFont(bold);
        
        NNAME.setUnderline(2,-2);
        
        notp.add(notary_public).add(NNAME);
        notp.setMarginTop(10);
        
        document.add(notp);
        
        Calendar currentCalDate = Calendar.getInstance();
        
        String data =  dateFormat.getCurrentDateInSpecificFormat(currentCalDate);
        
        Text day = new Text(data).setFont(bold);
        
        Text on = new Text("Date: ").setFont(bold);
        
        Paragraph dates = new Paragraph();
        
        dates.add(on).add(day);
        
       // dates.setFontSize(16);
        
        document.add(dates);

        

		String tmpDirsLocation = System.getProperty("java.io.tmpdir")+"/";
		RandomReference random = new RandomReference();
        
		Resource resource1 = new ClassPathResource("notariza_signature.png");
        
		File file1 = new File(tmpDirsLocation+random.getAlphaNumericString(10)+".png");
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
        
        //delete temp files...
        
        try {
        	
        	file1.delete();
        	file2.delete();
        	file3.delete();
        	
        }catch(Exception nn) {
        	
        }
        
        /*
        AreaBreak aB = new AreaBreak();
        
        document.add(aB);
        
        
        //Adding certificate..
        
        font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

		Text titleText = new Text("NOTARY CERTIFICATE");
		titleText.setFont(font);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFontSize(50);
		
		Resource resource1 = new ClassPathResource("notary_certificate.png");
		File file1 = resource1.getFile();
		String filePath1 = file1.getAbsolutePath();

		Paragraph pImg = new Paragraph();
		Image img1 = new Image(ImageDataFactory.create(filePath1));
		img1.getAccessibilityProperties().setAlternateDescription("notary_certificate");
		pImg.add(img1);

		img1.setFixedPosition(30, 100);

		Paragraph title1 = new Paragraph();
		title1.add(titleText);
		title1.setTextAlignment(TextAlignment.CENTER);
		title1.setMarginTop(80);

		// content..

		Text contentText1 = new Text(content1);
		contentText1.setFontSize(14);
		contentText1.setFont(font);

		Paragraph content_1 = new Paragraph();
		content_1.add(contentText1);
		content_1.setTextAlignment(TextAlignment.CENTER);
		content_1.setMargins(30, 40, 0, 40);

		Text contentText3 = new Text(documentHeader);

		contentText3.setFontSize(16);
		contentText3.setFont(font);
		contentText3.setUnderline(3, -3);

		Paragraph documentHead = new Paragraph();
		documentHead.add(contentText3);
		documentHead.setTextAlignment(TextAlignment.CENTER);
		documentHead.setMargins(10, 40, 0, 40);

		Text contentText2 = new Text(content2);
		contentText2.setFontSize(14);
		contentText2.setFont(font);

		Paragraph content_2 = new Paragraph();
		content_2.add(contentText2);
		content_2.setTextAlignment(TextAlignment.CENTER);
		content_2.setMargins(10, 40, 0, 40);

		Calendar currentCalDate1 = Calendar.getInstance();
		String date = dateFormat.getCurrentDateInSpecificFormat(currentCalDate1);

		Text OD = new Text("On the ");
		OD.setFontSize(14);
		OD.setFont(font);

		Text contentText4 = new Text(date);
		contentText4.setFontSize(14);
		contentText4.setFont(font);
		contentText4.setUnderline(3, -3);

		Paragraph dateP = new Paragraph();
		dateP.add(OD).add(contentText4);
		dateP.setTextAlignment(TextAlignment.LEFT);
		dateP.setMargins(10, 40, 0, 40);

		// name and seal of notary...

		Text contentText5 = new Text(notaryName);
		contentText5.setFontSize(14);
		contentText5.setFont(font);
		contentText5.setUnderline(3, -3);

		Paragraph NotaryName = new Paragraph();
		NotaryName.add(contentText5);
		NotaryName.setTextAlignment(TextAlignment.CENTER);
		NotaryName.setMargins(40, 40, 0, 40);

		Text contentText6 = new Text("(Name of Notary and Seal)");
		contentText6.setFontSize(14);
		contentText6.setFont(font);

		Paragraph notaryDesc = new Paragraph();
		notaryDesc.add(contentText6);
		notaryDesc.setTextAlignment(TextAlignment.CENTER);
		notaryDesc.setMargins(0, 40, 0, 40);

		// add the seal..
		Resource resource11 = new ClassPathResource("notary_seal.jpg");
		File file11 = resource11.getFile();
		String filePath11 = file11.getAbsolutePath();

		Paragraph seal = new Paragraph();
		Image sealImg = new Image(ImageDataFactory.create(filePath11));
		sealImg.getAccessibilityProperties().setAlternateDescription("notary_seal");
		seal.add(sealImg);
		sealImg.setWidth(100);
		seal.setTextAlignment(TextAlignment.CENTER);

		document.add(pImg);
		document.add(title1);
		document.add(content_1);
		document.add(documentHead);
		document.add(content_2);
		document.add(dateP);
		document.add(NotaryName);
		document.add(notaryDesc);
		document.add(seal);
		
		*/
       
        
        document.close();
		
	}

}
