package com.onehuddle.leaderboard.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;


public class StringUtil {
	  private static final Random RANDOM = new SecureRandom();
	  /** Length of password. @see #generateRandomPassword() */
	  public static final int PASSWORD_LENGTH = 6;
	  
	static Logger logger = Logger.getLogger(StringUtil.class);
    public static String escape(String s) {
        if (s != null) {
            s = s.replace("\'", "\\\'");
        }
        return s;
    }

    public static String escape(Date d) {
        String s = null;
        if (d != null) {
            s = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(d);
        }
        return escape(s);
    }

    public static String quote(String s) {
        if (isNull(s)) {
            s = "null";
        } else {
            s = "'" + escape(s) + "'";
        }
        return s;
    }

    public static String quote(Date d) {
        String s = null;
        if (d != null) {
            s = escape(d);
        }
        return quote(s);
    }

    public static String quote(Integer d) {
        String s = null;
        if (d != null) {
            s = escape(d.toString());
        }
        return quote(s);
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }

    public static boolean isNull(String s) {
    	/*
    	if(s.equalsIgnoreCase("null")){
    		return false;
    	}else{
    		return (isEmpty(s) || "null".equalsIgnoreCase(s.trim()) || "(null)".equalsIgnoreCase(s.trim()));
    	}
    	*/
    	return (isEmpty(s) || "null".equalsIgnoreCase(s.trim()) || "(null)".equalsIgnoreCase(s.trim()));
        //return (isEmpty(s) || "(null)".equalsIgnoreCase(s.trim()));//FOR LO-4734
    }

    public static String notNull(String s) {
        if (isNull(s)) {
            return "";
        } else {
            return s;
        }
    }

    public static String addLineBraek(String s, int numberofchars) {
        StringBuilder sb = new StringBuilder(s);
        int i = 0;
        while ((i = sb.indexOf(" ", i + numberofchars)) != -1) {
            sb.replace(i, i + 1, "</br>");
        }
    	//s = s.replaceAll("(.{"+numberofchars+"})", "$1</br>");
        //return s;
        return sb.toString();
    }

    /**
     * Will take a url such as http://www.google.com and return www.google.com
     *
     * @param url get Host from full URL.
     * @return HostName.
     */
    public static String getHost(String url) {
        if (url == null || url.length() == 0) {
            return "";
        }

        int doubleslash = url.indexOf("//");
        if (doubleslash == -1) {
            doubleslash = 0;
        } else {
            doubleslash += 2;
        }

        int end = url.indexOf('/', doubleslash);
        end = end >= 0 ? end : url.length();

        return url.substring(doubleslash, end);
    }


    /*
     * Get the base domain for a given host or url. E.g. mail.google.com will return google.com
     * @param host 
     * @return 
     */
    public static String getBaseDomain(String url) {
        String host = getHost(url);

        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            return host.substring(startIndex);
        } else {
            return host;
        }
    }
    
    
    public static String generateRandomAlphaNumiricCode()
    {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ0123456789+@";

        String pw = "";
        for (int i=0; i<PASSWORD_LENGTH; i++)
        {
            int index = (int)(RANDOM.nextDouble()*letters.length());
            pw += letters.substring(index, index+1);
        }
        return pw;
    }
    
    public static String generateRandomNumiricCode(int code_length)
    {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "1234567890";

        String pw = "";
        for (int i=0; i<code_length; i++)
        {
            int index = (int)(RANDOM.nextDouble()*letters.length());
            pw += letters.substring(index, index+1);
        }
        return pw;
    }
    
    /*
     * author: ashokhegde
     * 
     * Converts a string into an ArrayList of Integers
     * String should be comma separated
     * 
     */
    public static ArrayList<Integer> convertStringToIntegerArrayList(String stringToConvert) {
    	ArrayList<Integer> convertedArrayList = new ArrayList<Integer>();
    	try {
    		String[]stringArray  = stringToConvert.split(",");
    		for (int i = 0; i < stringArray.length; i++) {
    			convertedArrayList.add(Integer.parseInt(stringArray[i]));
    		}
    		
    	} catch (Exception e) {
            
    	}
    	return convertedArrayList;
    }
    
    /*
     * author: ashokhegde
     * 
     * Converts an ArrayList of integers to a comma separated string
     * 
     */
    
    public static String convertArrayListToStringWithBrackets(ArrayList<Integer> arrayToConvert) {
    	return "(" + convertArrayListToString(arrayToConvert, ',') + ")";
    }
    
    public static String convertArrayListToString(ArrayList<Integer> arrayToConvert) {
    	return convertArrayListToString(arrayToConvert, ',');
    }
    
    private static String convertArrayListToString(ArrayList<Integer> arrayToConvert, char seperator) {
    	StringBuilder convertedString = new StringBuilder();
    	for (int i = 0; i < arrayToConvert.size(); i++) {
    		if (i != 0) {
    			convertedString.append(seperator);
    		}
    		convertedString.append(arrayToConvert.get(i));
    	}
    	return convertedString.toString();
    }
    
    /*
     * author: ashokhegde
     * 
     * Replace all instances of the characters in charsToReplace with charToReplaceWith
     * 
     */
    public static String replaceAllSpecialCharachters(String originalString, char[] charsToReplace, 
    																			char charToReplaceWith) {
    	if (StringUtil.isEmpty(originalString) || (charsToReplace == null || charsToReplace.length == 0)) {
    		return originalString;
    	}
    	for (int i = 0; i < charsToReplace.length; i++) {
    		originalString = originalString.replace(charsToReplace[i], charToReplaceWith);
    	}
    	return originalString;
    }
}
