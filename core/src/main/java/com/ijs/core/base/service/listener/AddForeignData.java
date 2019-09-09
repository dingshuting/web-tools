package com.ijs.core.base.service.listener;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.springframework.stereotype.Service;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.component.anno.DefaultConstructorForList;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;

/**
 *  对整个数查询的列表结果进行外键关联查询,如果在实体中有{@link DefaultConstructorForList}注解时，则此过滤器不进行外键关联赋值，
 *  同时也负责对用户的所属关联主体进行赋值，如果BaseControl.userOwner不为空，则直接调用并将调用结果赋值给用户
 *  
 * @author Dustin
 */
@Service
public class AddForeignData extends MysqlBaseServiceListener implements MysqlServiceListener {

    @Resource
    private GenericDao genericDao;

    @Override
    public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {
        switch (action) {
            case AFTER_QUERY:
            	Field[] fields;
            	if(t.getAnnotation(Entity.class)!=null) {
            		fields=t.getDeclaredFields();
            	}else {
            		fields=t.getSuperclass().getDeclaredFields();
            	}
            	
            	for(Field field:fields) {
            		if(field.getName().endsWith("Id")) {
            			try {
            				List list=((PageList)args[args.length-1]).getList();
            				for(Object obj : list) {
	            				field.setAccessible(true);
	            				Field foreign=t.getDeclaredField(field.getName().substring(0, field.getName().lastIndexOf("Id")));
	            				foreign.setAccessible(true);
	            				Object key=field.get(obj);
	            				if(key==null||foreign.get(obj)!=null) {
	            					continue;
	            				}
	            				foreign.set(obj,dao.get(foreign.getType(), key.toString()));
            				}
						} catch (NoSuchFieldException | SecurityException e) {
							// TODO Auto-generated catch block
							log.warn(e.getMessage());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
            	}
            	if(t.equals(User.class)) {
            		List list=((PageList)args[2]).getList();
            		if(BaseControl.userOwner==null) {
            			break;
            		}
            		for(Object obj : list) {
            			((User)obj).setIdentity(BaseControl.userOwner.obtain(obj));
            		}
            	}
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public ACTION[] getAction() {
        return new ACTION[]{ACTION.AFTER_QUERY};
    }

	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return ".*";
	}

}
