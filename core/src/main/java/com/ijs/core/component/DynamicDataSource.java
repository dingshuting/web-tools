package com.ijs.core.component;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
/**
 * 配置动态数据源
 * @author Dustin
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{
	@Override
	protected Object determineCurrentLookupKey() {
		return DynamicDataSourceContextHolder.getDataSourceType();
	}
}
