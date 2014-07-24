package com.geeekr.activerecord;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

public class SqlHandle {

	// private static final Set<Model> MODEL_CACHE = new HashSet<Model>();

	private static Class<? extends Model> handle(Model model) {

		// ADD TO CACHE
		Class<? extends Model> clazz = model.getClass();

		return clazz;
	}

	private static String tableName(Model model) {
		Class<? extends Model> clazz = handle(model);
		Table table = clazz.getAnnotation(Table.class);
		String tableName = "";
		if (table != null) {
			tableName = table.name();
		}
		return tableName;
	}

	private static List<Field> fields(Model model) {
		Class<? extends Model> clazz = handle(model);
		Field[] fields = clazz.getDeclaredFields();
		List<Field> _fields = new ArrayList<Field>();
		for (Field f : fields) {
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			_fields.add(f);
		}
		return _fields;
	}

	public static SqlMod insertSql(Model model) {
		List<Object> params = new ArrayList<Object>();
		String tableName = tableName(model);

		List<String> _fields = new ArrayList<String>();
		List<Field> fields = fields(model);
		for (Field f : fields) {
			Id id = f.getAnnotation(Id.class);
			if (id != null) {
				continue;
			}

			Column column = f.getAnnotation(Column.class);

			if (column != null) {
				_fields.add(column.name());
			} else {
				_fields.add(defaultName(f.getName()));
			}
			f.setAccessible(true);
			Object val = null;
			try {
				val = f.get(model);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			params.add(val);
		}

		StringBuilder _fiel = new StringBuilder();
		StringBuilder _params = new StringBuilder();
		for (int i = 0; i < _fields.size(); i++) {
			if (i != _fields.size() - 1) {
				_fiel.append(_fields.get(i) + ", ");
				_params.append("?, ");
			} else {
				_fiel.append(_fields.get(i));
				_params.append("?");
			}
		}

		SqlMod mod = new SqlMod();
		mod.setSql(String.format("INSERT INTO %s(%s) VALUES(%s)", tableName,
				_fiel.toString(), _params.toString()));
		mod.setParams(params);
		return mod;
	}

	public static SqlMod updateSql(Model model) {
		List<Object> params = new ArrayList<Object>();
		String tableName = tableName(model);
		List<Field> fields = fields(model);
		List<String> _fields = new ArrayList<String>();
		Serializable idVal = 0;
		String idName = null;
		for (Field f : fields) {
			Id id = f.getAnnotation(Id.class);
			if (id != null) {
				f.setAccessible(true);
				try {
					idVal = (Serializable) f.get(model);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				Column column = f.getAnnotation(Column.class);
				if (column != null) {
					idName = column.name();
				} else {
					idName = defaultName(f.getName());
				}
				continue;
			}
			Column column = f.getAnnotation(Column.class);
			if (column != null) {
				_fields.add(column.name());
			} else {
				_fields.add(defaultName(f.getName()));
			}
			f.setAccessible(true);
			Object val = null;
			try {
				val = f.get(model);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			params.add(val);
		}

		StringBuilder values = new StringBuilder();
		for (int i = 0; i < _fields.size(); i++) {
			if (i != _fields.size() - 1) {
				values.append(_fields.get(i) + "= ?, ");
			} else {
				values.append(_fields.get(i) + "= ?");
			}
		}

		SqlMod mod = new SqlMod();
		mod.setSql(String.format("UPDATE %s SET %s WHERE %s = %s", tableName,
				values.toString(), idName, idVal.toString()));
		mod.setParams(params);

		return mod;
	}

	public static SqlMod deleteSql(Model model) {
		String tableName = tableName(model);
		List<Field> fields = fields(model);
		String idName = null;
		Serializable idVal = 0;
		for (Field f : fields) {
			Id id = f.getAnnotation(Id.class);
			if (id != null) {

				f.setAccessible(true);
				try {
					idVal = (Serializable) f.get(model);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

				Column column = f.getAnnotation(Column.class);
				if (column != null) {
					idName = column.name();
				} else {
					idName = defaultName(f.getName());
				}
				break;
			}
		}

		SqlMod mod = new SqlMod();
		mod.setSql(String.format("DELETE FROM %s WHERE %s = %s", tableName,
				idName, idVal));
		return mod;
	}

	public static <T> SqlMod queryOne(Class<T> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		String tableName = "";
		if (table != null) {
			tableName = table.name();
		}
		Field[] fields = clazz.getDeclaredFields();
		String idName = null;
		for (Field f : fields) {
			Id id = f.getAnnotation(Id.class);
			if (id != null) {
				Column column = f.getAnnotation(Column.class);
				if (column != null) {
					idName = column.name();
				} else {
					idName = defaultName(f.getName());
				}
				break;
			}
		}
		SqlMod mod = new SqlMod();
		mod.setSql(String.format("SELECT * FROM %s WHERE %s = ?", tableName,
				idName));
		return mod;
	}

	private static String defaultName(String name) {
		// columnName cover to column_name
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			// 将第一个字符处理成大写
			result.append(name.substring(0, 1).toUpperCase());
			// 循环处理其余字符
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				// 在大写字母前添加下划线
				if (s.equals(s.toUpperCase())
						&& !Character.isDigit(s.charAt(0))) {
					result.append("_");
				}
				// 其他字符直接转成大写
				result.append(s.toUpperCase());
			}
		}
		return result.toString().toLowerCase();
	}
}
