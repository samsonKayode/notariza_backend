package com.backend.notariza.util;

import java.io.File;
import java.util.Calendar;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.itextpdf.io.font.FontConstants;
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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

@SuppressWarnings("deprecation")
public class NotaryCertificate {

	DateFormatTH dateFormat = new DateFormatTH();

	PdfFont font;

	String content1 = "I hereby certify and attest as a duly authorized, appointed and practicing Notary Public of "
			+ "the Federal Republic of Nigeria, that the hereto attached document headed";

	String content2 = "is a copy of the original presented to me for notarization in Nigeria";

	String this1 = "on the ";

	String documentHeader = "SWORN AFFIDAVIT OF CHANGE OF NAME";

	String notaryName = "OLUSHOLA OKUYIGA";

	public NotaryCertificate() {

		//getCert(documentHeader, notaryName);

	}

	public void getCert(String filename, String documentHeader, String notaryName) throws Exception {

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename,
				new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
		Document document2 = new Document(pdfDoc, PageSize.A4);
		pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));
		pdfDoc.getCatalog().setLang(new PdfString("en-IN"));
		pdfDoc.getDocumentInfo().setTitle("Notorial Certificate");

		font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

		Text titleText = new Text("NOTARY CERTIFICATE");
		titleText.setFont(font);
		titleText.setTextAlignment(TextAlignment.CENTER);
		titleText.setFontSize(50);
		
		Resource resource = new ClassPathResource("notary_certificate.png");
		File file = resource.getFile();
		String filePath = file.getAbsolutePath();

		Paragraph pImg = new Paragraph();
		Image img = new Image(ImageDataFactory.create(filePath));
		img.getAccessibilityProperties().setAlternateDescription("notary_certificate");
		pImg.add(img);

		img.setFixedPosition(30, 100);

		Paragraph title = new Paragraph();
		title.add(titleText);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setMarginTop(80);

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

		Calendar currentCalDate = Calendar.getInstance();
		String date = dateFormat.getCurrentDateInSpecificFormat(currentCalDate);

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
		Resource resource1 = new ClassPathResource("notary_seal.jpg");
		File file1 = resource1.getFile();
		String filePath1 = file1.getAbsolutePath();

		Paragraph seal = new Paragraph();
		Image sealImg = new Image(ImageDataFactory.create(filePath1));
		sealImg.getAccessibilityProperties().setAlternateDescription("notary_seal");
		seal.add(sealImg);
		sealImg.setWidth(100);
		seal.setTextAlignment(TextAlignment.CENTER);

		document2.add(pImg);
		document2.add(title);
		document2.add(content_1);
		document2.add(documentHead);
		document2.add(content_2);
		document2.add(dateP);
		document2.add(NotaryName);
		document2.add(notaryDesc);
		document2.add(seal);

		document2.close();

	}

	public static void main(String[] args) throws Exception {

		new NotaryCertificate();
	}

}
