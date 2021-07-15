package com.backend.notariza.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatTH {
	
	
	public DateFormatTH() {
		
	}
	
	public String getCurrentDateInSpecificFormat(Calendar currentCalDate) {
	    String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
	    DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM yyyy");
	    return dateFormat.format(currentCalDate.getTime());
	}
	
	//date parameter..
	public String getCurrentDateInSpecificFormatDate(Date date) throws ParseException {
		
		Calendar currentCalDate = Calendar.getInstance();
		currentCalDate.setTime(date);
		
	    String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
	    DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM yyyy");
	    return dateFormat.format(currentCalDate.getTime());
	}
	
	public String getCurrentDateInSpecificFormatDate2(String date) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date dts = sdf.parse(date);
		Calendar currentCalDate = Calendar.getInstance();
		currentCalDate.setTime(dts);
		
	    String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));
	    DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "' MMMM yyyy");
	    return dateFormat.format(currentCalDate.getTime());
	}

	private String getDayNumberSuffix(int day) {
	    if (day >= 11 && day <= 13) {
	        return "th";
	    }
	    switch (day % 10) {
	    case 1:
	        return "st";
	    case 2:
	        return "nd";
	    case 3:
	        return "rd";
	    default:
	        return "th";
	    }
	}
	
}
