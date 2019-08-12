package com.ijs.core.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 乱码问题处理：
 * 1、GBK编码提交的
 * 首先urlencode短信内容（content），然后在API请求时，带入encode=gbk
 * <p>
 * 2、UTF-8编码的
 * <p>
 * 将content 做urlencode编码后，带入encode=utf8或utf-8
 * http://m.5c.com.cn/api/send/index.php?username=XXX&password_md5=XXX&apikey=XXX&mobile=XXX&content=%E4%BD%A0%E5%A5%BD%E6%89%8D%E6%94%B6%E7%9B%8A%E9%9F%A6&encode=utf8
 * <p>
 * 示例
 */

public class SendSMSUtil {

    public static enum SMSType { 
        SHENFEN_YANZHENG, DENGLU_QUEREN, DENGLU_YICHANG, YONGHU_ZHUCE, XIUGAI_MIMA, XINXI_BIANGENG, NOTIFICATION, RE_NOTIFICATION,NOSELF_SING,PUBLIC_YANZHENG,PAIJIAN_NOTI
    }

    public static String sendByAliyun(SMSType type, String mobile, Map<String, String> paras) {
        /*//设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        //初始化ascClient需要的几个参数
        //短信API产品名称（短信产品名固定，无需修改）
        final String product = "Dysmsapi";
        //短信API产品域名（接口地址固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";

        //替换成你的AK
        //你的accessKeyId,参考本文档步骤2
        final String accessKeyId = "xxxx";
        //你的accessKeySecret，参考本文档步骤2
        final String accessKeySecret = "xxxx";

        // 短信签名
        final String signName = "亿达企服";

        try {
            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);

            if (SMSType.SHENFEN_YANZHENG.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805095");

            } else if (SMSType.DENGLU_QUEREN.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805094");

            } else if (SMSType.DENGLU_YICHANG.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805093");

            } else if (SMSType.YONGHU_ZHUCE.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805092");

            } else if (SMSType.XIUGAI_MIMA.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805091");

            } else if (SMSType.XINXI_BIANGENG.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_139805090");

            } else if (SMSType.NOTIFICATION.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                //request.setTemplateCode("SMS_142025166");
                request.setTemplateCode("SMS_157278911");
            } else if (SMSType.RE_NOTIFICATION.equals(type)) {
                //必填:短信模板-可在短信控制台中找到
                request.setTemplateCode("SMS_142025168");

            }else if(SMSType.NOSELF_SING.equals(type)) {
            	request.setTemplateCode("SMS_142615001");
            }else if(SMSType.PUBLIC_YANZHENG.equals(type)) {
                request.setTemplateCode("SMS_145597691");
            }else if(SMSType.PAIJIAN_NOTI.equals(type)){
            	 //request.setTemplateCode("SMS_150170232");
            	request.setTemplateCode("SMS_157283918");
            }else {
                throw new RuntimeException("不支持的短信类型");
            }

            if (!CollectionUtils.isEmpty(paras)) {
                //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
                Gson gson = new GsonBuilder().serializeNulls().create();
                request.setTemplateParam(gson.toJson(paras));
            }

            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                return sendSmsResponse.getCode();

            } else {
                throw new RuntimeException("发送短信失败：错误码" + sendSmsResponse.getCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return null;
    }

    /**
     * 发送短信
     *
     * @param mobile  手机号,只发一个号码：13800000001。发多个号码：13800000001,13800000002,...N 。使用半角逗号分隔。
     * @param content 长度1-500位  特别注意：签名必须设置，网页验证码应用需要加添加【图形识别码】。例如：【易肥网】吴斌，您的验证码是：12345
     * @return 返回结果
     */
    public static String send(String mobile, String content) {

        //连接超时及读取超时设置
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); //连接超时：30秒
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");    //读取超时：30秒

        //新建一个StringBuffer链接
        StringBuffer buffer = new StringBuffer();


        //String encode = "GBK"; //页面编码和短信内容编码为GBK。重要说明：如提交短信后收到乱码，请将GBK改为UTF-8测试。如本程序页面为编码格式为：ASCII/GB2312/GBK则该处为GBK。如本页面编码为UTF-8或需要支持繁体，阿拉伯文等Unicode，请将此处写为：UTF-8

        String encode = "UTF-8";
        String username = "xxxx";  //用户名

        //String password_md5 = "2A542532C9BE37132D0EFE26A03BEACB";  //密码 dongli888
        String password_md5 = "xxxx";  //密码 


        //String mobile = "18211178672";  //手机号,只发一个号码：13800000001。发多个号码：13800000001,13800000002,...N 。使用半角逗号分隔。
        //String apikey = "7571f347a2ff9a3d0116db912c0040cc";  //apikey秘钥（请登录 http://m.5c.com.cn 短信平台-->账号管理-->我的信息 中复制apikey）
        String apikey = "8c016479dceae3d0bbd7af6777c91f74";  //apikey秘钥（请登录 http://m.5c.com.cn 短信平台-->账号管理-->我的信息 中复制apikey）

        try {


            String contentUrlEncode = URLEncoder.encode(content, encode);  //对短信内容做Urlencode编码操作。注意：如

            //把发送链接存入buffer中，如连接超时，可能是您服务器不支持域名解析，请将下面连接中的：【m.5c.com.cn】修改为IP：【115.28.23.78】
            buffer.append("http://m.5c.com.cn/api/send/index.php?username=" + username + "&password_md5=" + password_md5 + "&mobile=" + mobile + "&apikey=" + apikey + "&content=" + contentUrlEncode + "&encode=" + encode);

            System.out.println(buffer); //调试功能，输入完整的请求URL地址

            //把buffer链接存入新建的URL中
            URL url = new URL(buffer.toString());

            //打开URL链接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //使用POST方式发送
            connection.setRequestMethod("POST");

            //使用长链接方式
            connection.setRequestProperty("Connection", "Keep-Alive");

            //发送短信内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            //获取返回值
            String result = reader.readLine();

            //输出result内容，查看返回值，成功为success，错误为error，详见该文档起始注释
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机生成6位验证码
     *
     * @return
     */
    public static String createRandomVcode() {
        String vcode = "";
        for (int i = 0; i < 6; i++) {
            vcode = vcode + (int) (Math.random() * 9);
        }
        return vcode;
    }


}
