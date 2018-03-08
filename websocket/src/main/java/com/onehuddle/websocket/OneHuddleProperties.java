package com.onehuddle.websocket;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Set;

public class OneHuddleProperties {
    
	private static final String DEFAULT_PROP_FILE = "onehuddle.properties";
	private static final String OVERRIDE_PROP_FILE = "/etc/onehuddle.properties";
    
	private static OneHuddleProperties instance = null;
	private LinkedHashMap<String, String> props;
	
	private OneHuddleProperties() {
		props = new LinkedHashMap<String, String>();
    }
	
	public static OneHuddleProperties getInstance() {
		if (instance == null) {
			synchronized(OneHuddleProperties.class) {
				if (instance == null) {
					instance = new OneHuddleProperties();
                    instance.init();
				}
			}
		}
		return instance;
	}
    
	public String getProperty(String key) {
		return props.get(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		String retval = defaultValue;
		String value = getProperty(key);
		if (value != null) {
			retval = value;
		}
		return retval;
	}
	
	public boolean setProperty(String key, String value) {
		if (props.containsKey(key)) {
			props.remove(key);
			props.put(key, value);
			// AH
			// We should also save this value into the file
			return true;
		} else {
			return false;
		}
	}
    
	public Set<String> getPropertyNames() {
		return props.keySet();
	}
	
	public String toString() {
		return props.toString();
	}
	
    public void init() {
		if (props.isEmpty()) {
			synchronized(props) {
				if (props.isEmpty()) {
					try {
						loadDefaultPropertiesFile();
						loadOverridePropertiesFile();
					} catch (Throwable caught) {
						props.clear();
						caught.printStackTrace(System.err);
					}
					//System.err.println("Final Properties");
					//System.err.println(toString());
				}
			}
		}
	}
    

    
    private void loadDefaultPropertiesFile() throws Throwable {
		InputStream in = null;
		try {
			Properties dp = new Properties();
			in = OneHuddleProperties.class.getResourceAsStream(DEFAULT_PROP_FILE);
			dp.load(in);
			in.close();
			//System.err.println(DEFAULT_PROP_FILE);
			//System.err.println(dp);
			in = null;
			Iterator<String> i1 = dp.stringPropertyNames().iterator();
			while (i1.hasNext()) {
				String propertyName = i1.next();
				props.put(propertyName, dp.getProperty(propertyName));
			}
		} finally {
			try {
				if (in != null) in.close();
			} catch (Throwable caught) {
				// ignore
			}
		}
	}
    
	
	private void loadOverridePropertiesFile() throws Throwable {
		FileReader reader = null;
		try {
			File of = new File(OVERRIDE_PROP_FILE);
			if (of.exists()) {
				Properties op = new Properties();
				reader = new FileReader(of);
				op.load(reader);
				reader.close();
				reader = null;
				//System.err.println(OVERRIDE_PROP_FILE);
				//System.err.println(op);
				Iterator<String> i2 = op.stringPropertyNames().iterator();
				while (i2.hasNext()) {
					String propertyName = i2.next();
					props.put(propertyName, op.getProperty(propertyName));
				}
			}
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Throwable caught) {
				// ignore
			}
		}
	}
	
}

