package com.ijs.mongo.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.json.JSONArray;
import org.springframework.context.annotation.Bean;

import com.google.gson.JsonArray;

public abstract class ValidateRules implements Validate.IValidateRule {
	protected String para;

	public ValidateRules() {
		// TODO Auto-generated constructor stub
		super();
	}

	public ValidateRules(String para) {
		this.para = para;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		// TODO Auto-generated method stub
		if(para!=null)
		this.para = para;
	}

	public String getFailedHint() {
		// TODO Auto-generated method stub
		return "failed";
	}

	/**
	 * Email格式验证
	 * 
	 * @author 周继光
	 * 
	 */
	public static class EmailValidateRule extends ValidateRules {
		public EmailValidateRule() {
			// TODO Auto-generated constructor stub
			super("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "email格式有误";
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			// TODO 自动生成的方法存根
			if (data != null && !data.toString().isEmpty()  && data instanceof String) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				if (!matcher.matches()) {
					throw new ValidationException(getFailedHint());
				}
			}

		}
	}

	public static class MinLengthValidateRule extends ValidateRules {
		public MinLengthValidateRule(Integer minLength) {
			// TODO Auto-generated constructor stub
			super(minLength.toString());
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if (data != null   && data instanceof String&&!data.toString().isEmpty()) {
				Integer len = Integer.parseInt(para);
				if (data.toString().length() < len) {
					throw new ValidationException(getFailedHint());
				}
			}
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "最小长度不能小于" + para;
		}

	}

	public static class MaxLengthValidateRule extends ValidateRules {
		public MaxLengthValidateRule(Integer maxLength) {
			// TODO Auto-generated constructor stub
			super(maxLength.toString());
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if (data != null &&  data instanceof String&&!data.toString().isEmpty()) {
				Integer len = Integer.parseInt(para);
				if (data.toString().length() > len) {
					throw new ValidationException(getFailedHint());
				}
			}
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "最大长度不能大于" + para;
		}

	}

	public static class IllegalCharacterValidateRule extends ValidateRules {
		String regex = "^[\\w\\d\\_\\-\\@\\.]*$";

		
		@Override
		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "含有\"a-z\",\"A-Z\",\"0–9\",\"_\",\"-\",\"@\",\".\"之外的字符";
		}


		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if (data != null && !data.toString().isEmpty() && data instanceof String) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				matcher.matches();
			}
		}
	}
	public static class NotNullValidateRule extends ValidateRules {

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if(data==null || !data.toString().isEmpty()){
				if(data instanceof String&&data.toString().isEmpty())
					throw new ValidationException(getFailedHint());
				if(data instanceof JSONArray){
					if(((JSONArray)data).length()<1){
						throw new ValidationException(getFailedHint());
					}
				}
			}
		}
		@Override
		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "字段不能为空";
		}
		
	}
	public static class MaxSizeValidateRule extends ValidateRules {

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if(data!=null&&data instanceof JSONArray){
				if(((JSONArray)data).length()<=Integer.parseInt(this.getPara()))
				throw new ValidationException(getFailedHint());
			}
		}
		@Override
		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "字段大小超出限制"+this.getPara();
		}
	}
	public static class MinSizeValidateRule extends ValidateRules {

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			if(data!=null&&data instanceof JSONArray){
				if(((JSONArray)data).length()<=Integer.parseInt(this.getPara()))
				throw new ValidationException(getFailedHint());
			}
		}
		@Override
		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "字段大小小于最小限制"+this.getPara();
		}
	}
	
	/**
	 * 手机及办公电话格式验证
	 * 
	 * @author wb
	 * 
	 */
	public static class TelephoneValidateRule extends ValidateRules {
		public TelephoneValidateRule() {
			// TODO Auto-generated constructor stub
			super("((13|15|17|18)[0-9]{9})");
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "联系方式格式有误";
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			// TODO 自动生成的方法存根
			if (data != null && data instanceof String && !data.toString().isEmpty()) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				if (!matcher.matches()) {
					throw new ValidationException(getFailedHint());
				}
			}

		}
	}
	
	/**
	 * 传真格式验证
	 * 
	 * @author wb
	 * 
	 */
	public static class FaxValidateRule extends ValidateRules {
		public FaxValidateRule() {
			// TODO Auto-generated constructor stub
			super("(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?");
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "传真格式有误";
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			// TODO 自动生成的方法存根
			if (data != null && data instanceof String && !data.toString().isEmpty()) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				if (!matcher.matches()) {
					throw new ValidationException(getFailedHint());
				}
			}

		}
	}
	
	/**
	 * 整数格式验证
	 * 
	 * @author wb
	 * 
	 */
	public static class IntegerValidateRule extends ValidateRules {
		public IntegerValidateRule() {
			// TODO Auto-generated constructor stub
			super("[\\+]?\\d+");
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "整数格式有误";
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			// TODO 自动生成的方法存根
			if (data != null && data instanceof String && !data.toString().isEmpty()) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				if (!matcher.matches()) {
					throw new ValidationException(getFailedHint());
				}
			}

		}
	}
	
	/**
	 * 保留两位小数的正数格式验证
	 * 
	 * @author wb
	 * 
	 */
	public static class NumberValidateRule extends ValidateRules {
		public NumberValidateRule() {
			// TODO Auto-generated constructor stub
			super("[0-9]{1}\\d*(\\.\\d{1,2})?");
		}

		public String getFailedHint() {
			// TODO Auto-generated method stub
			return "请输入最多保留两位小数的正数";
		}

		public void validate(Object data) throws ValidationException {
			// TODO Auto-generated method stub
			// TODO 自动生成的方法存根
			if (data != null &&  !data.toString().isEmpty() && data instanceof String) {
				Pattern pattern = Pattern.compile(para);
				Matcher matcher = pattern.matcher(data.toString());
				if (!matcher.matches()) {
					throw new ValidationException(getFailedHint());
				}
			}

		}
	}
	
	public static void main(String[] args) {
		String reg= "";
		
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher("159-1111111");
		System.out.println(matcher.matches());
		
	}
}