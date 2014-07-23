package com.geeekr.activerecord;

import java.util.List;

public class SqlMod {

	private String sql;

	private List<Object> params;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "SqlMod [sql=" + sql + ", params=" + params + "]";
	}

}
