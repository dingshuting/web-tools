package com.ijs.core.base.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/10/19.
 */
@Entity
@Table(name = "extra_data_cols_authorization")
@Cacheable
public class ExtraDataColsAuthorization extends AbstractExtraDataColsAuthorization {

    public ExtraDataColsAuthorization(String id) {
        super(id);
    }

    public ExtraDataColsAuthorization() {
    }

    public ExtraDataColsAuthorization(String id, String roleId, String extraDataColsId, Integer isRead, Integer isWrite,Integer isEdit,Integer isDel,Integer isSeach,Integer isShowInList,Integer isShowread) {
        super(id, roleId, extraDataColsId, isRead, isWrite, isEdit, isDel, isSeach, isShowInList,isShowread);
    }

}
