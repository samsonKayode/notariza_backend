package com.backend.notariza.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.backend.notariza.entity.GiftDeedEntity;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfViewerPreferences;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;

public class GiftDeedPDF {

	static DateFormatTH dateFormat = new DateFormatTH();

	PdfFont font;

	static PdfFont regular, bold, bold1;

	static int textFontSize = 11;

	int fontSize = 11;
	
	String tmpDirsLocation = System.getProperty("java.io.tmpdir")+"/";

	public GiftDeedPDF() {

	}

	public void getPDFDocument(String filename, GiftDeedEntity giftDeedEntity, String notaryName)
			throws IOException, ParseException {

		PdfDocument pdfDoc = new PdfDocument(
				new PdfWriter(filename, new WriterProperties().addUAXmpMetadata().setPdfVersion(PdfVersion.PDF_1_7)));
		Document document = new Document(pdfDoc, PageSize.A4);
		pdfDoc.getCatalog().setViewerPreferences(new PdfViewerPreferences().setDisplayDocTitle(true));

		document.setFontSize(12);

		regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		bold1 = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

		document.setFont(font);

		Text giftDeed = new Text("DEED OF GIFT");
		giftDeed.setFont(bold1);
		giftDeed.setTextAlignment(TextAlignment.CENTER);
		giftDeed.setFontSize(25);

		Paragraph GD = new Paragraph();
		GD.add(giftDeed);
		GD.setTextAlignment(TextAlignment.CENTER);
		GD.setMarginTop(5);
		GD.setFontSize(20);

		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		Text tr1 = new Text("DEED OF GIFT");
		tr1.setFont(bold);
		tr1.setFontSize(fontSize);

		Text contentText4 = new Text(String.valueOf(year));
		contentText4.setFontSize(textFontSize);
		contentText4.setFont(bold);
		// contentText4.setUnderline(1, -1);

		Paragraph dateP = new Paragraph();
		dateP.add("This GIFT ").add(tr1).add(" is made on the .............. day of .............. ").add(contentText4);
		dateP.setTextAlignment(TextAlignment.LEFT);
		dateP.setMargins(10, 10, 0, 10);
		dateP.setFontSize(fontSize);

		Paragraph btw = new Paragraph();
		btw.add("BETWEEN ").setFont(bold1);
		btw.setTextAlignment(TextAlignment.LEFT);
		btw.setMargins(10, 10, 0, 10);
		btw.setFontSize(fontSize);

		Text dName = new Text(giftDeedEntity.getDonorName().toUpperCase());
		dName.setFontSize(12);
		dName.setFont(bold);
		// dName.setUnderline(1, -1);

		Text dAdd = new Text(giftDeedEntity.getDonorAddress());
		dAdd.setFontSize(textFontSize);
		dAdd.setFont(bold);
		// dAdd.setUnderline(1, -1);

		Text dnName = new Text(giftDeedEntity.getDoneeName().toUpperCase());
		dnName.setFontSize(12);
		dnName.setFont(bold);
		// dnName.setUnderline(1, -1);

		Text dnAdd = new Text(giftDeedEntity.getDoneeAddress());
		dnAdd.setFontSize(textFontSize);
		dnAdd.setFont(bold);
		// dnAdd.setUnderline(1, -1);

		Paragraph donorP = new Paragraph();
		donorP.add(dName).add("   Of   ").add(dAdd).add(" (hereinafter called the DONOR)");
		donorP.setTextAlignment(TextAlignment.LEFT);
		donorP.setMargins(10, 10, 0, 10);
		donorP.setFontSize(textFontSize);

		Paragraph and = new Paragraph();
		and.add("AND ").setFont(bold1);
		and.setTextAlignment(TextAlignment.LEFT);
		and.setMargins(10, 10, 0, 10);
		// and.setFontSize(fontSize);

		Paragraph doneeP = new Paragraph();
		doneeP.add(dnName).add("   Of   ").add(dnAdd).add(" (hereinafter called the DONEE)");
		doneeP.setTextAlignment(TextAlignment.LEFT);
		doneeP.setMargins(10, 10, 0, 10);
		doneeP.setFontSize(fontSize);

		Paragraph whereas = new Paragraph();
		whereas.add("WHEREAS: ").setFont(bold1);
		whereas.setTextAlignment(TextAlignment.LEFT);
		whereas.setMargins(10, 10, 0, 10);
		whereas.setFontSize(fontSize);

		Text t1 = new Text("The DONOR is the ");
		// t1.setFontSize(11);

		Text t2 = new Text(giftDeedEntity.getRelationshipToDonee());
		// t2.setUnderline(1, -1);
		t2.setFont(bold);
		t2.setFontSize(textFontSize);

		Text t3 = new Text("The DONEE is the ");
		t3.setFontSize(fontSize);

		Text t4 = new Text(giftDeedEntity.getRelationshipToDonor());
		// t4.setUnderline(1, -1);
		t4.setFont(bold);
		t4.setFontSize(textFontSize);

		Paragraph p1 = new Paragraph();
		p1.add(t1).add(t2).add(" of the DONEE");

		p1.setFontSize(fontSize);

		Paragraph p2 = new Paragraph();
		p2.add(t3).add(t4).add(" of the DONOR");
		p2.setFontSize(fontSize);

		List theList = new List(ListNumberingType.ENGLISH_UPPER);
		ListItem item1 = new ListItem();

		item1.add(p1);

		ListItem item2 = new ListItem();
		item2.add(p2);

		DecimalFormat df = new DecimalFormat("###,###,###.##");

		Text t5 = new Text(giftDeedEntity.getDenomination() + df.format(giftDeedEntity.getAmountGifted()));
		// t5.setUnderline(1, -1);
		t5.setFont(bold);
		t5.setFontSize(textFontSize);

		Text t6 = new Text(giftDeedEntity.getDonorBankName());
		// t6.setUnderline(1, -1);
		t6.setFont(bold);
		t6.setFontSize(textFontSize);

		Text t7 = new Text(giftDeedEntity.getDonorAccountNumber());
		// t7.setUnderline(1, -1);
		t7.setFont(bold);
		t7.setFontSize(textFontSize);

		Text t8 = new Text(giftDeedEntity.getDonorAccountName());
		// t8.setUnderline(1, -1);
		t8.setFont(bold1);
		t8.setFontSize(textFontSize);

		Paragraph p3 = new Paragraph();
		p3.add("The DONOR is the rightful owner of the sum of ").add(t5)
				.add("  domiciled in the DONOR's bank account with ").add(t6).add(" with account number ").add(t7)
				.add(" and account name ").add(t8).add(" (hereinafter referred to as Donor’s Bank Account)");

		p3.setFontSize(fontSize);

		ListItem item3 = new ListItem();
		item3.add(p3);

		Text t9 = new Text(giftDeedEntity.getDenomination() + df.format(giftDeedEntity.getAmountGifted()));

		// t9.setUnderline(1, -1);
		t9.setFont(bold1);
		t9.setFontSize(textFontSize);

		String longText = " domiciled in the Donor’s Bank Account to the DONEE as an unconditional and irrevocable financial gift and the DONEE is willing to accept the said gift.";

		Paragraph p4 = new Paragraph();
		p4.add("The DONOR wishes to give the said sum of ").add(t9).add(longText);

		p4.setFontSize(fontSize);

		ListItem item4 = new ListItem();
		item4.add(p4);

		theList.add(item1);
		theList.add(item2);
		theList.add(item3);
		theList.add(item4);

		theList.setPaddings(5, 5, 5, 5);
		theList.setTextAlignment(TextAlignment.LEFT);
		theList.setMarginLeft(20);
		theList.setFontSize(textFontSize);

		Paragraph witness = new Paragraph();
		witness.add("NOW THIS DEED WITNESSES AS FOLLOWS: ").setFont(bold);
		witness.setTextAlignment(TextAlignment.LEFT);
		witness.setMargins(10, 10, 0, 10);
		witness.setFontSize(fontSize);

		// second List...

		List theList2 = new List(ListNumberingType.DECIMAL);

		theList2.setPaddings(5, 5, 5, 5);
		theList2.setTextAlignment(TextAlignment.LEFT);
		theList2.setMarginLeft(20);
		// theList2.setFontSize(textFontSize);

		ListItem listItem1 = new ListItem();

		String longText2 = "IN CONSIDERATION of the love and affection for the DONEE, the DONOR hereby grants an unconditional and irrevocable GIFT in the amount of ";

		Text t10 = new Text(giftDeedEntity.getTransferMode());
		t10.setFont(bold1);
		t10.setFontSize(textFontSize);

		Text t11 = new Text(giftDeedEntity.getDoneeBankName());
		t11.setFont(bold1);
		t11.setFontSize(textFontSize);

		Text t12 = new Text(giftDeedEntity.getDoneeAccountNumber());
		t12.setFont(bold1);
		t12.setFontSize(textFontSize);

		Text t13 = new Text(giftDeedEntity.getDoneeAccountName());
		t13.setFont(bold1);
		t13.setFontSize(textFontSize);

		String dateTrans = dateFormat.getCurrentDateInSpecificFormatDate(giftDeedEntity.getDate());

		Text DT = new Text(dateTrans);
		DT.setFont(bold1);
		DT.setFontSize(textFontSize);

		Paragraph p5 = new Paragraph();

		p5.add(longText2).add(t5).add(" transferred via ").add(t10).add(" on the ").add(DT)
				.add(" from DONOR’s BANK ACCOUNT to the DONEE’s BANK ACCOUNT domiciled with ").add(t11)
				.add(" having the Account Number: ").add(t12).add(" and the Account Name ").add(t13)
				.add(" (the receipt of which the DONEE hereby acknowledges)");

		p5.setFontSize(fontSize);

		listItem1.add(p5);

		Paragraph p6 = new Paragraph();
		p6.add("The DONOR hereby certifies that THE GIFT is a genuine gift and is made wholly out of the DONOR’s acquired funds.");

		Paragraph p7 = new Paragraph();
		p7.add("That no other persons shall have any right whatsoever over the above-mentioned GIFT during the lifetime of the DONOR or thereafter.");

		Paragraph p8 = new Paragraph();
		p8.add("The DONEE shall stand possessed of the said GIFT and may utilize it in any manner whatsoever and for whatsoever purpose the DONEE desires.");

		Paragraph p9 = new Paragraph();
		p9.add("This GIFT shall be an irrevocable gift which is acceptable by the DONEE");

		p6.setFontSize(fontSize);
		p7.setFontSize(fontSize);
		p8.setFontSize(fontSize);
		p9.setFontSize(fontSize);

		ListItem listItem2 = new ListItem();
		listItem2.add(p6);
		ListItem listItem3 = new ListItem();
		listItem3.add(p7);
		ListItem listItem4 = new ListItem();
		listItem4.add(p8);
		ListItem listItem5 = new ListItem();
		listItem5.add(p9);

		theList2.add(listItem1);
		theList2.add(listItem2);
		theList2.add(listItem3);
		theList2.add(listItem4);
		theList2.add(listItem5);

		Text t15 = new Text("IN WITNESS WHEREOF").setBold();
		t15.setFontSize(fontSize);
		// t15.setFont(bold);

		Paragraph p10 = new Paragraph();
		p10.add(t15).add(", the parties have set their hands and seals on the day and year first above written.");

		Text t16 = new Text("SIGNED, SEALED AND DELIVERED ").setBold();
		t16.setFontSize(fontSize);

		Text t17 = new Text("DONOR ").setBold();
		t17.setFontSize(fontSize);

		Text t18 = new Text("DONEE ").setBold();
		t18.setFontSize(fontSize);

		Paragraph p11 = new Paragraph();
		p11.add(t16).add(", by the within named ").add(t17);

		Paragraph p12 = new Paragraph();
		p12.add("..............................................................................................................................");
		p12.setMarginTop(20);

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

		pSig.setTextAlignment(TextAlignment.CENTER);

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

		img2.setMaxWidth(120);

		// img1.setFixedPosition(420, 100);

		img3.setMaxWidth(120);
		// img2.setFixedPosition(450, 170);
		// img3.setFixedPosition(480, 10);

		pSeal.setTextAlignment(TextAlignment.CENTER);

		Paragraph p13 = new Paragraph();
		p13.add(t16).add(", by the within named ").add(t18);

		p13.setMarginTop(10);

		Paragraph p14 = new Paragraph();
		p14.add("..............................................................................................................................");
		p14.setMarginTop(20);

		// Donor witness..

		Paragraph p15 = new Paragraph();
		p15.setFontSize(fontSize);
		p15.setFont(bold);
		p15.add("In the presence of:");

		Text name = new Text("NAME: ");
		name.setFontSize(fontSize);

		Text address = new Text("ADDRESS: ");
		address.setFontSize(fontSize);

		Text occupation = new Text("OCCUPATION: ");
		occupation.setFontSize(fontSize);

		Text signature = new Text("SIGNATURE & DATE: ");
		signature.setFontSize(fontSize);

		Text wName = new Text(giftDeedEntity.getFirstWitnessName());
		wName.setFont(bold1);
		wName.setFontSize(textFontSize);

		Text wAddress = new Text(giftDeedEntity.getFirstWitnessAddress());
		wAddress.setFont(bold1);
		wAddress.setFontSize(textFontSize);

		Text wOccupation = new Text(giftDeedEntity.getFirstWitnessOccupation());
		wOccupation.setFont(bold1);
		wOccupation.setFontSize(textFontSize);

		Paragraph p16 = new Paragraph();
		p16.add(name).add(wName);

		Paragraph p17 = new Paragraph();
		p17.add(address).add(wAddress);

		Paragraph p18 = new Paragraph();
		p18.add(occupation).add(wOccupation);

		Paragraph p22 = new Paragraph();
		p22.add(signature).add("................................................................................");
		p22.setMarginTop(10);
		p15.setMarginBottom(10);
		p15.setMarginTop(10);

		// Donee witness..

		Text wName2 = new Text(giftDeedEntity.getSecondWitnessName());
		wName2.setFont(bold1);
		wName2.setFontSize(textFontSize);

		Text wAddress2 = new Text(giftDeedEntity.getSecondWitnessAddress());
		wAddress2.setFont(bold1);
		wAddress2.setFontSize(textFontSize);

		Text wOccupation2 = new Text(giftDeedEntity.getSecondWitnessOccupation());
		wOccupation2.setFont(bold1);
		wOccupation2.setFontSize(textFontSize);

		Paragraph p19 = new Paragraph();
		p19.add(name).add(wName2);

		Paragraph p20 = new Paragraph();
		p20.add(address).add(wAddress2);

		Paragraph p21 = new Paragraph();
		p21.add(occupation).add(wOccupation2);

		Paragraph attestedBy = new Paragraph();
		attestedBy.add("Duly Attested By:");
		attestedBy.setTextAlignment(TextAlignment.CENTER);
		attestedBy.setMarginTop(20);

		Text contentText5 = new Text(notaryName);
		contentText5.setFontSize(14);
		contentText5.setFont(font);
		contentText5.setUnderline(2, -2);

		Paragraph NotaryName = new Paragraph();
		NotaryName.add(contentText5);
		NotaryName.setTextAlignment(TextAlignment.CENTER);
		// NotaryName.setMargins(0, 40, 0, 40);

		Text contentText6 = new Text("(Name of Notary Public and Seal)");
		contentText6.setFontSize(fontSize);
		contentText6.setFont(font);

		Paragraph notaryDesc = new Paragraph();
		notaryDesc.add(contentText6);
		notaryDesc.setTextAlignment(TextAlignment.CENTER);
		notaryDesc.setMargins(-5, 40, 0, 40);
		
		Calendar currentDate = Calendar.getInstance();
		String currDate = dateFormat.getCurrentDateInSpecificFormat(currentDate);
		
		Paragraph datePara = new Paragraph();
		datePara.add(currDate);
		datePara.setMargins(0, 0, 0, 0);
		datePara.setTextAlignment(TextAlignment.CENTER);
		datePara.setFont(bold1);

		document.add(GD);
		document.add(dateP);
		document.add(btw);
		document.add(donorP);
		document.add(and);
		document.add(doneeP);
		document.add(whereas);
		document.add(theList);
		document.add(witness);

		document.add(theList2);

		// document.add(pSig);
		// document.add(pSeal);

		document.add(p10);
		document.add(p11);
		document.add(p12);

		AreaBreak areaBreak = new AreaBreak();

		document.add(areaBreak);

		document.add(p15);

		document.add(p16);
		document.add(p17);
		document.add(p18);
		document.add(p22);

		document.add(p13);
		document.add(p14);

		document.add(p15);
		document.add(p19);
		document.add(p20);
		document.add(p21);
		document.add(p22);

		document.add(attestedBy);
		document.add(pSig);
		document.add(pSeal);
		
		document.add(datePara);
		
		document.add(NotaryName);
		document.add(notaryDesc);
		
        try {
        	
        	file1.delete();
        	file2.delete();
        	file3.delete();
        	
        }catch(Exception nn) {
        	
        }

		document.close();
	}

}
