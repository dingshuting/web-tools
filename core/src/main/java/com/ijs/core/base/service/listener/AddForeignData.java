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
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;

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
            				List list=((PageList)args[2]).getList();
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

}
