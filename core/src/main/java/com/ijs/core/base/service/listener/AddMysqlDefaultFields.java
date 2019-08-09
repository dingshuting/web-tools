package com.ijs.core.base.service.listener;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.SysDep;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.BeanUtils;

/**
 * 针对MDB的数据入库前的验证类，验证逻辑分为2大步：<br/>
 * 1、从关系型数据空中的表结构配置，验证数据列的有效性（判断列是否属于业务逻辑，如果不属于将验证不通过）<br/>
 * 2、根据列的其它配置对列的值进行验证的检验
 * @author Administrator
 */
@Service
@Order(0)
public class AddMysqlDefaultFields extends MysqlBaseServiceListener implements MysqlServiceListener {
	public static final String CREATE_TIME="createTime";
	public static final String USER_ID="userId";
	public static final String NAME="name";
	public static final String ID="id";
	public static final String ADD_USER="add_user";
	public static final String STATUS="status";
	public static final String UPDATE_TIME="update_time";
	public static final String UPDATE_USER="update_user";
	public static final String ISPASS="isPass";
	@Resource
	private GenericDao genericDao;

	@Override
	public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {


		switch (action) {
			case BEFOR_SAVE :
			    Object model = args[0];
                try {
                	PropertyDescriptor cTimePd=BeanUtils.getPropertyDescriptor(t, CREATE_TIME);
                	if(cTimePd!=null) {
                		if(cTimePd.getReadMethod().invoke(model)==null) {
                			cTimePd.getWriteMethod().invoke(model, new Timestamp(System.currentTimeMillis()));
                		}
                	}
                	PropertyDescriptor statusPd=BeanUtils.getPropertyDescriptor(t, STATUS);
                	if(statusPd!=null) {
                		if(statusPd.getReadMethod().invoke(model)==null) {
                			statusPd.getWriteMethod().invoke(model, 1);
                		}
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(t.equals(SysDep.class)) {
                	((SysDep)model).setOwner(getCurrentUser().getOwner());
                }
				break;
			case BEFOR_UPDATE:
				break;
			case BEFOR_QUERY:
				int ob=((StringBuffer)args[args.length-1]).indexOf("order by");
				if(t.equals(User.class)) {
					if(ob>0) {
						((StringBuffer)args[args.length-1]).insert(ob," and user.accountNo !='sa' ");
					}else {
						((StringBuffer)args[args.length-1]).append(" and user.accountNo !='sa' ");
					}
					
				}
				break;
			default:
				break;
		}
		return true;
		// TODO Auto-generated method stub
	}

	public ACTION[] getAction() {
		// TODO Auto-generated method stub
		return new ACTION[]{ACTION.BEFOR_SAVE,ACTION.BEFOR_UPDATE,ACTION.BEFOR_QUERY};
	}

}
