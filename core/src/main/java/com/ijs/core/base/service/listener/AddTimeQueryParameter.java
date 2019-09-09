package com.ijs.core.base.service.listener;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.BaseModel;
import com.ijs.core.exception.ServiceException;
import com.ijs.core.util.QueryParameters;
import com.ijs.core.util.QueryParameters.Section;
import com.ijs.core.util.model.JpqlModel;

/**
 * 对所有的高级查询进行反向JPQL解析，此查询主要用于时间段，或者数字区间的查询条件生成
 * @author Dustin
 *
 */
@Service
@Order(1)
public class AddTimeQueryParameter extends MysqlBaseServiceListener implements MysqlServiceListener {

	@Override
	public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {

		switch (action) {
			case BEFOR_QUERY:
				JpqlModel jpql=(JpqlModel) args[args.length-1];
				Object queryEntity=args[1];
				if(queryEntity instanceof BaseModel) {
					QueryParameters qp=((BaseModel)queryEntity).getQp();
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
						jpql.addWhere(timeQp.toString());
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

	@Override
	public String getClassExOfService() {
		// TODO Auto-generated method stub
		return ".*";
	}
	public static void main(String[] args) {
		System.out.println(AddTimeQueryParameter.class.getName().matches(".*"));
	}
}
