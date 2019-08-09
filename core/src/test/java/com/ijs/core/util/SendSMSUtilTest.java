package com.ijs.core.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class SendSMSUtilTest {

    @Test
    @Ignore
    public void sendByAliyun() {
        String mobile = "18601941900"; // 短信接收人的手机号，必填

        Assert.assertNotNull(mobile, "接收短信的手机号不能为空，否则无法发送短信");

        Map<String, String> paras = new LinkedHashMap<>();

        // 身份验证
        paras.clear();
        paras.put("code", SendSMSUtil.createRandomVcode());
        //SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.SHENFEN_YANZHENG, mobile, paras);

//        // 登录确认
//        paras.clear();
//        paras.put("code", SendSMSUtil.createRandomVcode());
//        SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.DENGLU_QUEREN, mobile, paras);
//
//        // 登录异常
//        paras.clear();
//        paras.put("code", SendSMSUtil.createRandomVcode());
//        SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.DENGLU_YICHANG, mobile, paras);
//
//        // 用户注册
//        paras.clear();
//        paras.put("code", SendSMSUtil.createRandomVcode());
//        SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.YONGHU_ZHUCE, mobile, paras);
//
//        // 修改密码
//        paras.clear();
//        paras.put("code", SendSMSUtil.createRandomVcode());
//        SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.XIUGAI_MIMA, mobile, paras);
//
//        // 信息变更
//        paras.clear();
//        paras.put("code", SendSMSUtil.createRandomVcode());
//        SendSMSUtil.sendByAliyun(SendSMSUtil.SMSType.XINXI_BIANGENG, mobile, paras);
    }

}