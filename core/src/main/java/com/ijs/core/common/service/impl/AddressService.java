package com.ijs.core.common.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ijs.core.base.listener.MysqlBaseServiceListener;
import com.ijs.core.base.listener.MysqlServiceListener;
import com.ijs.core.base.model.Address;
import com.ijs.core.exception.ServiceException;

@Service
public class AddressService extends MysqlBaseServiceListener implements MysqlServiceListener {

	@Override
	public boolean doAction(ACTION action, Class t, Object... args) throws ServiceException {
		// TODO Auto-generated method stub
		switch (action) {
		case BEFOR_UPDATE:
			if (t.equals(Address.class)) {
				if (isLogin()) {
					if (((Address) args[0]).getIsDefault() == 1) {
						dao.executeJPQL("update Address set isDefault=2 where owner=?", getCurrentUser().getOwner());
					}
					String region = ((Address) args[0]).getRegion();
					((Address) args[0]).setRegion(region.substring(region.lastIndexOf(",") + 1));
					return true;
				} else {
					log.error("the info of address is not allowed to modefy without logined");
					return false;
				}
			}
			break;
		case BEFOR_SAVE:
			if (t.equals(Address.class)) {
				if (isLogin()) {
					String region = ((Address) args[0]).getRegion();
					if (((Address) args[0]).getIsDefault() == null) {
						((Address) args[0]).setIsDefault(2);
					}
					((Address) args[0]).setCreateTime(new Date());
					((Address) args[0]).setRegion(region.substring(region.lastIndexOf(",") + 1));
					((Address) args[0]).setOwner(getCurrentUser().getOwner());
					((Address) args[0]).setStatus(1);
					if (((Address) args[0]).getIsDefault() == 1) {
						dao.executeJPQL("update Address set isDefault=2 where owner=?", getCurrentUser().getOwner());
					}
					return true;
				} else {
					log.error("the info of address is not allowed to modefy without logined");
					return false;
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
		// TODO Auto-generated method stub
		return new ACTION[] { ACTION.BEFOR_SAVE,ACTION.BEFOR_UPDATE };
	}

}
