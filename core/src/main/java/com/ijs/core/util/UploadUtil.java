package com.ijs.core.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.google.gson.JsonObject;
import com.ijs.core.base.Config;

public class UploadUtil {
	protected final transient Log log = LogFactory.getLog(getClass());
	
	
	
	/**
	 * 根据  文件路径  验证  上传的临时文件加 是否存在 此文件
	 * @author Junming.li
	 * @param filePath 文件路径
	 * @return true 存在 ；false 不存在
	 * @throws Exception 验证失败
	 * time: 201508121650
	 */
	public static boolean fileExists(String filePath) throws Exception{
		try {
			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			File tempFile = new File(Config.SYS_PARAMETER_MAP.get(Config.UPLOAD_ROOT_PATH) + Config.SYS_PARAMETER_MAP.get(Config.FILE_TEMP_PATH) + "/" + fileName);
			return tempFile.exists();//判断 文件是否存在：临时文件夹下
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 根据给予的文件名重新生成一个当前时间的文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public static final int FILE = 1;
	public static final int IMAGE = 2;
	public static String getFileNameByCurrentTime(String contentType) {
		return System.currentTimeMillis()
				+ "."+contentType.substring(contentType.lastIndexOf("/")+1);
	}
	/**
	 * 根据URL链接地址删除对应的文件，尚未实现，用的时候实现了并修改此注释
	 * @param url
	 */
	public static void removeFileByUrl(String url){
		
	}
	
	public static String copyTempToSystem(String filePath, int fileType,
			String userId, boolean isAbs) throws IOException {
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		File tempFile = new File(Config.SYS_PARAMETER_MAP.get(Config.UPLOAD_ROOT_PATH)
				+ Config.SYS_PARAMETER_MAP.get(Config.FILE_TEMP_PATH) + "/" + fileName);
		String ft = "other";
		if (fileType == FILE) {
			ft = "file";
		} else if (fileType == IMAGE) {
			ft = "image";
		}
		return uploadFile(fileName, ft, tempFile, userId, isAbs);
	}

	/**
	 * 将上传上来的文件重命名后复制到图片站点
	 * 
	 * @param imgFileFileName
	 *            上传的原始文件名
	 * @param imgFileContentType
	 *            上传文件mime类型
	 * @param imgFile
	 *            上传文件的file对象
	 * @param userId
	 *            当前所上传的用于id，用于将文件进行归档该用户处理
	 * @return 一个文件路径,当isAbs 为true时返回文件的绝对路径，否则返回相对于项目的路径
	 * 
	 * @throws IOException
	 */
	public static String uploadFile(String imgFileFileName,
			String imgFileContentType, File imgFile, String userId,
			boolean isAbs) throws IOException {
		try {
			String fileName = UploadUtil
					.getFileNameByCurrentTime(imgFileFileName);
			String filePath = Config.SYS_PARAMETER_MAP
					.get(Config.UPLOAD_ROOT_PATH);
			if (imgFileContentType.toLowerCase().contains("image")) {
				filePath += Config.SYS_PARAMETER_MAP
						.get(Config.IMAGE_PATH);
			} else if (imgFileContentType.toLowerCase().contains("file")) {
				filePath += Config.SYS_PARAMETER_MAP
						.get(Config.FILE_PATH);
			} else {
				filePath += Config.SYS_PARAMETER_MAP
						.get(Config.FILE_PATH);
			}
			String fileComplatePath = filePath +"/"+ userId + "/";
			File targetDir = new File(fileComplatePath);
			if (!targetDir.exists()) {
				targetDir.mkdirs();
			}
			File newfile = new File(targetDir.getAbsolutePath() + "/"
					+ fileName);
			newfile.createNewFile();
			FileUtils.copyFile(imgFile, newfile);
			if (isAbs) {
				return newfile.getAbsolutePath();
			} else {
				return Config.SYS_PARAMETER_MAP
						.get(Config.IMAGE_DOMAIN)+Config.SYS_PARAMETER_MAP
						.get(Config.IMAGE_PATH) + "/"+userId+"/"+ fileName;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public static String uploadFileToDir(MultipartFile file, String contentType)
			throws IOException {
		String newfileName = "";
		String originalFilename = file.getOriginalFilename();
		String[] originalFilenameArr= originalFilename.split("\\.");
		if(originalFilenameArr.length>0) {//截取文件后缀
		originalFilename=originalFilenameArr[originalFilenameArr.length-1];
		newfileName = getFileNameByCurrentTime("/"+originalFilename);
		}else {
		if (contentType.contains("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.contains("application/vnd.ms-excel")){
            newfileName = getFileNameByCurrentTime("application/xlsx");
        }else if (contentType.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ){
			newfileName = getFileNameByCurrentTime("application/docx");
		}else if ( contentType.contains("application/msword")){
			newfileName = getFileNameByCurrentTime("application/doc");
		}
		}
		File fileDir=new File(Config.SYS_PARAMETER_MAP
				.get(Config.UPLOAD_ROOT_PATH)+Config.SYS_PARAMETER_MAP
				.get(Config.FILE_PATH));
		if(!fileDir.exists()){
			fileDir.mkdirs();
		}
		File newfile = new File(fileDir.getAbsolutePath()+ "/" + newfileName);
		newfile.createNewFile();
		file.transferTo(newfile);
		return Config.SYS_PARAMETER_MAP
				.get(Config.IMAGE_DOMAIN) + "/"
				+ newfileName;
	}

	/**
	 * 将结果的字符串解析，当成功是返回url，当传入失败的json将抛出异常，异常信息为解析时的错误结果
	 * 
	 * @param resultJson
	 * @return
	 * @throws Exception
	 */
	public static String getResult(JsonObject resultJson) throws Exception {
		if (resultJson != null) {
			int status = resultJson.get("error").getAsInt();
			if (status == 0) {
				return resultJson.get("url").getAsString();
			} else {
				throw new Exception(resultJson.get("message").getAsString());
			}
		} else {
			throw new Exception("resultJson can't is null");
		}
	}
	/**
	 * 只上传图片无逻辑操作
	 * 编辑器格式
	 * @param errorCode
	 * @param url
	 * @return
	 */
	public static String getResultJsonForKD(Integer errorCode, String url){
		JsonObject jso=new JsonObject();
		jso.addProperty("error", errorCode);
		jso.addProperty("url", url);
		return jso.toString();
	}
	/**
	 * base64 存储图片
	 * @param fileName
	 * @param path
	 * @param base64Str
	 */
	public static String saveImgForBase64(String base64Str){
		if ("".equals(base64Str)||base64Str.isEmpty()||null==base64Str) //数据为空
            return null;
        try{
	         String[] base64Arr = base64Str.split(",");
	         String img64 = base64Arr[1];
	         String suffix=base64Arr[0].substring(base64Arr[0].indexOf("/")+1,base64Arr[0].indexOf(";"));
	         //Base64解码
	         byte[] buffer = new BASE64Decoder().decodeBuffer(img64);
	         String fileName=getFileNameByCurrentTime(suffix);
	         System.out.println("--------------:"+Config.SYS_PARAMETER_MAP.get(Config.FILE_PATH));
//	         File path=new File(Config.SYS_PARAMETER_MAP.get(Config.UPLOAD_ROOT_PATH)+Config.SYS_PARAMETER_MAP.get(Config.FILE_PATH));
	         File path=new File(Config.SYS_PARAMETER_MAP.get(Config.UPLOAD_ROOT_PATH)+Config.SYS_PARAMETER_MAP.get(Config.FILE_PATH));
	 		if(!path.exists()){
	 			path.mkdirs();
	 		}
	         //生成文件
	         File imgFile=new File(path+"/"+fileName);
	         	if(!imgFile.exists())
	         		imgFile.createNewFile();
	            OutputStream out = new FileOutputStream(imgFile);   
	            out.write(buffer);
	            out.flush();
	            out.close();
//	            return Config.SYS_PARAMETER_MAP.get(Config.IMAGE_DOMAIN)+Config.SYS_PARAMETER_MAP.get(Config.FILE_PATH) + "/" + fileName;
	            return Config.SYS_PARAMETER_MAP.get(Config.IMAGE_DOMAIN) + "/" + fileName;
        }catch (Exception e){
            return e.getMessage();
        }
	}
	/**
	 * 根据全路径截取url
	 */
	public static String getUrlByFullUrl(String fullUrl){
		return fullUrl.replace(Config.SYS_PARAMETER_MAP
				.get(Config.IMAGE_DOMAIN).toString(),Config.SYS_PARAMETER_MAP
				.get(Config.UPLOAD_ROOT_PATH)+Config.SYS_PARAMETER_MAP
				.get(Config.FILE_PATH));
	}

}
