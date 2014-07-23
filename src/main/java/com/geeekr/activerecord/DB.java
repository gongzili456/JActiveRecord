package com.geeekr.activerecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DB {
	private static final String JDBC_DRIVER = "driverClass";
	private static final String JDBC_URL = "jdbcUrl";
	private static final String JDBC_USER = "user";
	private static final String JDBC_PASSWORD = "password";

	private static DataSource ds;

	private DB() {
	}

	static {
		initDataSource();
	}

	private static final void initDataSource() {
		Properties c3p0pro = new Properties();
		String path = DB.class.getClassLoader().getResource("c3p0.properties")
				.getPath();
		try {
			c3p0pro.load(new FileReader(path));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String drverClass = c3p0pro.getProperty(JDBC_DRIVER);
		if (drverClass != null) {
			try {
				// 加载驱动类
				Class.forName(drverClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		Map<Object, Object> overrides = new HashMap<Object, Object>();

		for (Object key : c3p0pro.keySet()) {
			if (((String) key).startsWith("c3p0")) {
				overrides.put(key, c3p0pro.get(key));
			}
		}

		try {
			DataSource ds_unpooled = DataSources.unpooledDataSource(
					c3p0pro.getProperty(JDBC_URL),
					c3p0pro.getProperty(JDBC_USER),
					c3p0pro.getProperty(JDBC_PASSWORD));

			ds = DataSources.pooledDataSource(ds_unpooled, overrides);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static synchronized DataSource getDataSource() {
		return ds;
	}

	public static synchronized Connection getConnection() throws SQLException {
		final Connection con = ds.getConnection();
		return con;
	}

}
