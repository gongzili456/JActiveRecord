package com.geeekr.activerecord;

import java.io.Serializable;

public class Model {

	public static DbServer db() {
		return DbServer.instance();
	}

	/**
	 * insert or an update based on that.
	 */
	public void save() {
		db().save(this);
	}

	/**
	 * Update this entity.
	 */
	public void update() {
		db().update(this);
	}

	/**
	 * Insert this entity.
	 */
	public void insert() {
		db().insert(this);
	}

	/**
	 * Delete this entity.
	 */
	public void delete() {
		db().delete(this);
	}

	/**
	 * @param <T>
	 * @param <T>
	 * 
	 */
	public static <T> T query(Class<T> clazz, Serializable id) {
		return (T) db().query(clazz, id);
	}
}
