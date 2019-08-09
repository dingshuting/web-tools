/**
 * 
 */
package com.ijs.core.util.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;


public final class MD5PasswordEncoder {

	public final static String encode(String rawPass, String salt) {   
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		md5.setEncodeHashAsBase64(false);
						
        return md5.encodePassword(rawPass, salt);
    }   
		
	public final static boolean valid(String encPass, String rawPass, String salt) {   
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		md5.setEncodeHashAsBase64(false);
				
        return md5.isPasswordValid(encPass, rawPass, salt);
    } 
	
	
	public static void main(String[] args){
		List<String> list = new ArrayList<String>();
		list.add("lisilisi");
		
		
		for(String username : list){
			System.out.println(username + " " + MD5PasswordEncoder.encode("123456", ""));
		}

	}
}
