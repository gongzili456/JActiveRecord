package com.geeekr.activerecord;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
		try {
			PreparedStatement ps = conn().prepareStatement(sql.getSql());
			List<Object> params = sql.getParams();
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert this entity.
	 */
	public void insert(Model model) {
		SqlMod sql = SqlHandle.insertSql(model);
		try {
			PreparedStatement ps = conn().prepareStatement(sql.getSql());
			List<Object> params = sql.getParams();
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete this entity.
	 */
	public void delete(Model model) {
		SqlMod sql = SqlHandle.deleteSql(model);
		try {
			Statement ps = conn().createStatement();
			ps.executeUpdate(sql.getSql());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public <T> T query(Class<T> clazz, Serializable id) {
		SqlMod sql = SqlHandle.queryOne(clazz);
		QueryRunner runner = new QueryRunner(DB.getDataSource());
		T t = null;
		try {
			t = runner.query(sql.getSql(), new BeanHandler<T>(clazz,
					new BasicRowProcessor(new CusBeanProcessor())), id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
}
