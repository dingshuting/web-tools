package com.ijs.core.base.service.listener;

import java.beans.PropertyDescriptor;
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
import com.ijs.core.util.model.JpqlModel;

/**
 * 对所有的业务进行监听，当业务数据进行保存时，添加默认的创建时间、状态2个字段，前提是拥有这2个字段。同时添加过滤，当为用户数据时排除掉‘sa’账户
 * @author Dustin
 *
 */
@Service
@Order(0)
public class AddMysqlDefaultFields extends MysqlBaseServiceListener implements MysqlServiceListener {
	public static final String CREATE_TIME="createTime";
	public static final String STATUS="status";
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
				if(t.equals(User.class)) {
					JpqlModel jpql=((JpqlModel)args[args.length-1]);
					jpql.addWhere(" and user.accountNo !='sa' ");
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

	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return ".*";
	}

}
