package com.ijs.core.base.service.listener;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.BaseModel;
import com.ijs.core.base.model.User;
import com.ijs.core.common.dao.GenericDao;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.QueryParameters.Section;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 针对MDB的数据入库前的验证类，验证逻辑分为2大步：<br/>
 * 1、从关系型数据空中的表结构配置，验证数据列的有效性（判断列是否属于业务逻辑，如果不属于将验证不通过）<br/>
 * 2、根据列的其它配置对列的值进行验证的检验
 * @author Administrator
 */
@Service
@Order(1)
public class AddTimeQueryParameter extends MysqlBaseServiceListener implements MysqlServiceListener {

	@Override
	public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {

		switch (action) {
			case BEFOR_QUERY:
				StringBuffer jpql=(StringBuffer) args[args.length-1];
				Object queryEntity=args[1];
				if(queryEntity instanceof BaseModel) {
					QueryParameters qp=((BaseModel)queryEntity).getQp();
					int ob=jpql.indexOf("order by");
					if(qp.getSections()!=null) {
						StringBuffer timeQp=new StringBuffer();
						for(Section section:qp.getSections()) {
							if(section.getColName()==null||section.getColName().isEmpty()) {
								continue;
							}
							if(section.getType()==null||section.getType()==2) {
								if (section.getStart() != null && !section.getStart().isEmpty()) {
									timeQp.append(" and ").append(section.getColName()).append(">='").append(section.getStart()).append("' ");
								}
								if (section.getEnd() != null &&! section.getEnd().isEmpty()) {
									timeQp.append(" and ").append(section.getColName()).append("<='").append(section.getEnd()).append("' ");
								}
							}else if(section.getType()==1) {
								if (section.getStart() != null && !section.getStart().isEmpty()) {
									timeQp.append(" and ").append(section.getColName()).append("='").append(section.getStart()).append("' ");
								}
							}
						}
						if(ob==-1) {
							jpql.append(timeQp.toString());
						}else {
							jpql.insert(ob, timeQp.toString());
						}
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
		return new ACTION[]{ACTION.BEFOR_QUERY};
	}
}
