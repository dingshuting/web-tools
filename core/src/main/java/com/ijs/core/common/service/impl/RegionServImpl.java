package com.ijs.core.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ijs.core.base.model.Region;
import com.ijs.core.base.service.impl.GenericServImpl;
import com.ijs.core.common.service.RegionServ;

@Service("regionServ")
public class RegionServImpl extends GenericServImpl implements
RegionServ {
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Region> findRegByPid(String pid,String rname) {
		// TODO Auto-generated method stub
		String jpql="from Region where parentId=?";
		if(rname!=null&&!rname.trim().equals("")){
			jpql+=" and name like '%"+rname+"%'";
		}
		List list=dao.find(jpql, pid);
		return list;
	}
	@Override
	public Region getDetail(String id) {
		// TODO Auto-generated method stub
		try {
			if(!id.equals("0")){
				Region reg=(Region) dao.findOne("from Region where id=?",id);
				List<Region> tc=this.findRegByPid(id,null);
				List<Region> children=new ArrayList<Region>();
					for(Region child:tc){
						children.add(getDetail(child.getId()));
					}
					reg.setChildRegs(children);
//					reg.setParentReg(getDetail(reg.getParentId()));
//					reg.setBrotherRegs(findRegByPid(reg.getParentId(),null));
					return reg;
			}else{
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(id);
			return null;
		}
	}
	@Override
	public Region getDetailContainChild(String id) {
		// TODO Auto-generated method stub
		Region reg;
		try {
			reg = (Region) dao.findOne("from Region where id=?",id);
			reg.setChildRegs(findRegByPid(reg.getId(), null));
			return reg;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	}
	/**
	 * 区域详情  liuyanyan
	 */
	public Region getRegionDetail(String id) {
		try {
			Region region = (Region)dao.findOne("from Region where id="+id);
			Region regionName = (Region) dao
					.findOne("from Region where id=?",
							new Object[] { region.getParentId() });
			return region;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	
}	
