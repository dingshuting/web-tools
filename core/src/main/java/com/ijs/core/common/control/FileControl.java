package com.ijs.core.common.control;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.common.service.SaveAccessory;
import com.ijs.core.util.Result;
import com.ijs.core.util.UploadUtil;
/**
 * 文件操作的Control，所有商场文件通过本Control来实现
 * @author Dustin
 *
 */
@Controller
@RequestMapping("/file")
public class FileControl extends BaseControl {
	
/**
 * 上传文件到服务器，上传写入完成后调用SaveAccessory接口进行业务逻辑的处理
 * @param file  上传的文件
 * @param act	上传的文件动作标识，如添加用户头像为用户的服务，则传值userServ，此值为服务类的别名，同时此类需要实现SaveAccessory接口
 * @param uid	上传的文件所属的业务数据id
 * @return 返回ok代表成功
 */
	@RequestMapping("/upload")
	public @ResponseBody String upload(HttpServletResponse response,@RequestParam("file")MultipartFile file,@RequestParam("act")String act,@RequestParam(value="uid",required=false)String uid,@RequestParam(value="remark",required=false)String remark){
		try {
			log.debug("uploaded a file that name is :"+file.getContentType()+"-->"+file.getOriginalFilename());
			String url=UploadUtil.uploadFileToDir(file, file.getContentType());
			//资源文件，仅仅作为资源进行存储，主要是编辑器的上传或者其它
			if(act.equals("resource")){
				return UploadUtil.getResultJsonForKD(0, url);
			}else{
				((SaveAccessory)getApplicationContext().getBean(act)).saveAccessory(uid,url,remark);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("上传文件错误",e);
		}
		return "ok";
	}
	/**
	 * 删除指定的资源文件
	 * @param act service的实例名字
	 * @param uid 资源id,删除文件所属的对象id，然后由对应的act服务类，进行资源查找和删除操作
	 * @param remark 资源类型
	 * @return result 标准的返回对象
	 */
	@RequestMapping("/remove")
	public @ResponseBody Result remove(@RequestParam("act")String act,@RequestParam("uid")String uid,@RequestParam(value="remark",required=false)String remark){
		Result<Object> result=new Result<Object>();
		try {
			log.debug("delete a file that service is :"+act+",uid:"+uid+",remark:"+remark);
			((SaveAccessory)getApplicationContext().getBean(act)).removeAccessory(uid,remark);
			result.setCode(Result.CODE_SUCCESS);
			result.setDesc("删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("删除文件错误",e);
			result.setCode(Result.CODE_ERROR);
			result.setDesc("删除失败--"+e.getMessage());
		}
		return result;
	}
	
}
