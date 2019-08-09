package com.ijs.core.conf;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.ijs.core.base.filter.ControlAllInterceptor;
import com.ijs.core.util.DateUtil;
@Configuration
public class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Autowired 
	private Environment evn;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		String no_verify_urls=evn.getProperty("app.config.no-verify-urls");
		registry.addInterceptor(new ControlAllInterceptor(no_verify_urls)).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
	public class DateSerializer implements JsonSerializer<Date>,JsonDeserializer<Date>{

		@Override
		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			String dataVal=json.getAsString();
			return DateUtil.stringToDate(dataVal, null);
		}

		@Override
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			// TODO Auto-generated method stub
			String val=DateUtil.dateFormat(src, null);
			return context.serialize(val);
		}
	}
	public class NumberSerializer implements JsonDeserializer<Integer>{

		@Override
		public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			String dataVal=json.getAsString();
			if(dataVal==null||dataVal.isEmpty()) {
				return null;
			}
			return Integer.parseInt(dataVal);
		}

	}
	public class DoubleSerializer implements JsonDeserializer<Double>{

		@Override
		public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			// TODO Auto-generated method stub
			String dataVal=json.getAsString();
			if(dataVal==null||dataVal.isEmpty()) {
				return null;
			}
			return Double.parseDouble(dataVal);
		}

	}
	@Bean
	GsonHttpMessageConverter fastJsonHttpMessageConverter(){
		GsonHttpMessageConverter gsonHttpMessageConverter=new GsonHttpMessageConverter();
		List<MediaType> types=new ArrayList<MediaType>();
		types.add(MediaType.APPLICATION_JSON);
		types.add(MediaType.APPLICATION_JSON_UTF8);
		Gson gson=new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).registerTypeAdapter(Integer.class, new NumberSerializer())
				.registerTypeAdapter(Double.class, new DoubleSerializer()).create();
		gsonHttpMessageConverter.setGson(gson);
		gsonHttpMessageConverter.setSupportedMediaTypes(types);
		
		return gsonHttpMessageConverter;
	}
}
