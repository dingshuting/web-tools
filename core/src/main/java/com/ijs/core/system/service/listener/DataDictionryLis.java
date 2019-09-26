package com.ijs.core.system.service.listener;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import org.springframework.stereotype.Service;

import com.ijs.core.base.control.BaseControl;
import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.DataDictionary;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.component.anno.DefaultConstructorForList;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.PageList;
import com.ijs.core.util.model.JpqlModel;

/**
 *  对整个数查询的列表结果进行外键关联查询,如果在实体中有{@link DefaultConstructorForList}注解时，则此过滤器不进行外键关联赋值，
 *  同时也负责对用户的所属关联主体进行赋值，如果BaseControl.userOwner不为空，则直接调用并将调用结果赋值给用户
 *  
 * @author Dustin
 */
@Service
public class DataDictionryLis extends MysqlBaseServiceListener implements MysqlServiceListener {


    @Override
    public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {
        switch (action) {
        	case BEFOR_QUERY:
        		JpqlModel jm=(JpqlModel) args[args.length-1];
        		jm.addWhere(" and status=1 ");
        		if(!getCurrentUser().isSa()) {
        			jm.addWhere(" and locked<>1 ");
        		}
        		break;
        	case BEFOR_DELETE:
        	case BEFOR_SAVE:
        	case BEFOR_UPDATE:
        		DataDictionary dd=(DataDictionary) args[0];
        		if(!getCurrentUser().isSa()&&dd.getLocked()==1) {
        			throw new ServiceException("数据字典已锁定");
        		}
            default:
                break;
        }
        return true;
    }

    @Override
    public ACTION[] getAction() {
        return new ACTION[]{ACTION.BEFOR_QUERY,ACTION.BEFOR_SAVE,ACTION.BEFOR_UPDATE,ACTION.BEFOR_DELETE};
    }

	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return DataDictionary.class.getName();
	}

}
