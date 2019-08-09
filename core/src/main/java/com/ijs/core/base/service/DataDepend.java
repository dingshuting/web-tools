package com.ijs.core.base.service;

import java.io.Serializable;

/**
 * 数据依赖接口，有此接口，说明数据可以被外部或者其它业务包依赖，通过调用接口获取到被依赖的对象实例
 * @author Administrator
 *
 */
public interface DataDepend {
	/**
	 * 获取可依赖的实例对象，具体被谁依赖由使用该接口者确认，
	 * 此结构使用场景是当2个对象不相关，并且无逻辑主外键关系时，却又需要依赖，可通过此接口进行实现
	 * 如：用户的归属者，其归属者，可能是一个公司、一个人或者某个实体，并无明确的确认，因此可以使用此接口，在最后服务启动时确认提供具体的归属者
	 * @param key 根据不同的业务场景，可选择性的值
	 * @return 返回具体依赖的对象值
	 */
	public Object obtain(Object key);
}
