/**
 * 
 */
package com.ijs.core.util.security;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;



public final class BASE64Encoder {

	public static String decodeURL(String url){
		try {
			return new String(Base64.decode(url.replaceAll("[*]", "=").getBytes()));
		} catch (Base64DecodingException e) {
			return null;
		}
	}
	
	public static String encodeURL(String url){
			return new String(Base64.encode(url.getBytes())).replace("=", "*").replace("\n", "");
	}
}
