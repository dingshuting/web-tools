/**
 * 
 */
package com.ijs.core.base.unit;

import java.util.List;

import com.ijs.core.base.model.Func;

/**
 * @author Tairong
 * 将一个List转成树型结构
 */
public class ListToTreeMap {

	public void converFunc(List<Func> newlist, List<Func> list, String parent){		
		for(Func func : list){
			if(func.getParentId().equals(parent)){
				if(!newlist.contains(func)){
					newlist.add(func);
				}
				converFunc(func.getSubFuncs(), list, func.getId());
			}
		}
	}
}
