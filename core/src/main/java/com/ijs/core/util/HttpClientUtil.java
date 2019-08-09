package com.ijs.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 远程访问功能封装，封装了httpClient的工具类，可以发去http的请求
 * @author Conlin
 *
 */
public class HttpClientUtil {
	
	private static Log log = LogFactory.getLog(HttpClientUtil.class);
	
	/**
	 * 使用Http Post的方法进行请求
	 * @param url 请求的url地址信息
	 * @param params 键值对的请求参数
	 * @return 返回response的body信息
	 */
    public static String post(String url, Map<String, String> params) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
        log.info("post远程调用:" + url);
        HttpPost post = postForm(url, params);  
        try {
			body = invoke(httpclient, post);
		} catch (IOException e) {
			body = "SF";
			return body;
		}  
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
      
    /**
     * 使用Get方法访问
     * @param url
     * @return
     */
    public static String get(String url) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("get远程调用:" + url);  
        HttpGet get = new HttpGet(url);  
        try {
			body = invoke(httpclient, get);
		} catch (IOException e) {
			e.printStackTrace();
			body = "error";
			return body;
		}  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
          
      
    private static String invoke(DefaultHttpClient httpclient,HttpUriRequest httpost) throws ClientProtocolException, IOException {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
          
        return body;  
    }  
  
  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient,HttpUriRequest httpost) throws ClientProtocolException, IOException {  
        HttpResponse response = null;  
          
        response = httpclient.execute(httpost);  
        return response;  
    }  
    
    private static String paseResponse(HttpResponse response) {  
        //log.info("get response from http server..");  
        HttpEntity entity = response.getEntity();  
          
        //log.info("response status: " + response.getStatusLine());  
        String charset = EntityUtils.getContentCharSet(entity);  
        //log.info(charset);  
          
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
            log.info("远程相应结果:" + body);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
    
    private static HttpPost postForm(String url, Map<String, String> params){  
          
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            //log.info("set utf-8 form entity to httppost");  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    }  
}
