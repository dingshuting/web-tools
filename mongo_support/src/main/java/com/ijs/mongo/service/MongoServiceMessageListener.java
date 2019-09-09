package com.ijs.mongo.service;

import com.mongodb.DBObject;

public interface MongoServiceMessageListener {
	/**
	 * 数据对象保存时根据字段所触发的相关消息提醒
	 * @param args 一般为DBObject对象
	 * @param content 渲染后的筒子信息串
	 */
	public void sendMessage(Object args,String content);
}
