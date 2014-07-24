package com.geeekr.activerecord;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

public class DbServer {
	private static final DbServer db = new DbServer();

	private DbServer() {
	}

	public static DbServer instance() {
		return db;
	}

	private Connection conn() throws SQLException {
		return DB.getConnection();
	}

	/**
	 * insert or an update based on that.
	 */
	public void save(Model model) {

		
		
	}

	/**
	 * Update this entity.
	 */
	public void update(Model model) {
		SqlMod sql = SqlHandle.updateSql(model);
		QueryRunner runner = new QueryRunner();
		Connection connection = null;
		try {
			connection = conn();
			runner.update(connection, sql.getSql(), sql.getParams().toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Insert this entity.
	 */
	public void insert(Model model) {
		SqlMod sql = SqlHandle.insertSql(model);
		System.out.println(sql.getSql());

		QueryRunner runner = new QueryRunner();
		Connection connection = null;
		try {
			connection = conn();
			runner.update(conn(), sql.getSql(), sql.getParams().toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete this entity.
	 */
	public void delete(Model model) {
		SqlMod sql = SqlHandle.deleteSql(model);
		QueryRunner runner = new QueryRunner();
		Connection connection = null;
		try {
			connection = conn();
			runner.update(conn(), sql.getSql(), sql.getParams().toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public <T> T query(Class<T> clazz, Serializable id) {
		SqlMod sql = SqlHandle.queryOne(clazz);
		QueryRunner runner = new QueryRunner(DB.getDataSource());
		T t = null;
		Connection connection = null;
		try {
			connection = conn();
			t = runner.query(sql.getSql(), new BeanHandler<T>(clazz,
					new BasicRowProcessor(new CusBeanProcessor())), id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return t;
	}
}
