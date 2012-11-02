package org.obeonetwork.pim.uml2.gen.java.services;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonServices {
	public String reqDate() {
		Date date = new Date(); // to get the date
		Locale locale = Locale.getDefault();// to get the language of the system
		DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.LONG, locale);
		return dateFormatShort.format(date);
	}
	
	public String reqTime() {
		Date date = new Date();
		return DateFormat.getTimeInstance(DateFormat.LONG).format(date);
	}
}
