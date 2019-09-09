package com.ijs.core.base.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Administrator on 2018/10/19.
 */
@MappedSuperclass
public abstract class AbstractExtraDataColsAuthorization extends BaseModel  implements java.io.Serializable{

    private String id;
    private String roleId;
    private String extraDataColsId;
    private Integer isRead;
    private Integer isWrite;
    private Integer isEdit;
    private Integer isDel;
	private Integer isSeach;
	private Integer isShowInList;
	private Integer isShowread;
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "role_id", nullable = true,length = 64)
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    @Column(name = "extra_data_cols_id", nullable = true,length = 64)
    public String getExtraDataColsId() {
        return extraDataColsId;
    }

    public void setExtraDataColsId(String extraDataColsId) {
        this.extraDataColsId = extraDataColsId;
    }
    @Column(name = "is_read", nullable = true)
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
    @Column(name = "is_write", nullable = true)
    public Integer getIsWrite() {
        return isWrite;
    }

    public void setIsWrite(Integer isWrite) {
        this.isWrite = isWrite;
    }

    @Column(name = "is_edit", nullable = true)
    public Integer getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Integer isEdit) {
		this.isEdit = isEdit;
	}
	 @Column(name = "is_del", nullable = true)
	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	
	@Column(name = "is_seach")
	public Integer getIsSeach() {
		return this.isSeach;
	}

	public void setIsSeach(Integer isSeach) {
		this.isSeach = isSeach;
	}

	@Column(name = "is_show_in_list")
	public Integer getIsShowInList() {
		return isShowInList;
	}

	public void setIsShowInList(Integer isShowInList) {
		this.isShowInList = isShowInList;
	}

	public AbstractExtraDataColsAuthorization(String id) {
        this.id = id;
    }

    public AbstractExtraDataColsAuthorization() {
    }
    @Column(name = "is_showread")
    public Integer getIsShowread() {
		return isShowread;
	}

	public void setIsShowread(Integer isShowread) {
		this.isShowread = isShowread;
	}

	public AbstractExtraDataColsAuthorization(String id, String roleId, String extraDataColsId, Integer isRead, Integer isWrite,Integer isEdit,Integer isDel,Integer isSeach,Integer isShowInList,Integer isShowread) {
        this.id = id;
        this.roleId = roleId;
        this.extraDataColsId = extraDataColsId;
        this.isRead = isRead;
        this.isWrite = isWrite;
        this.isEdit = isEdit;
        this.isDel = isDel;
        this.isSeach=isSeach;
        this.isShowInList=isShowInList;
        this.isShowread=isShowread;
    }
}
