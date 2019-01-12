package com.geral.springboot01.dao;



import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BaseDao {

	@Autowired
	private DataSource dataSource;
	
	
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
	
	public void closeAll(Connection conn,Statement stmt,ResultSet rs) throws SQLException 
	{
		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * 执行查询的统一方法
	 * 
	 * @param sql
	 * @param refect
	 * @return
	 * @throws SQLException 
	 */
	public  List<Object> executeGetClass(String sql,Class refect) throws SQLException {
		List<Object> list = new ArrayList<Object>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = (Statement) conn.createStatement();
			rs = (ResultSet) stmt.executeQuery(sql);
			while (rs.next()) {
				Object obj = refect.newInstance();
				int count = rs.getMetaData().getColumnCount();
				for (int i = 1; i <= count; i++) {

					String columnName = rs.getMetaData().getColumnName(i);
					String methodName = "set"
							+ columnName.substring(0, 1).toUpperCase()
							+ columnName.substring(1);
					Field field = refect.getDeclaredField(columnName);
					Method method = refect.getDeclaredMethod(methodName, field.getType());
					if (field.getType().getName().equals("java.lang.Integer")) {
						method.invoke(obj, rs.getInt(columnName));
					} else if (field.getType().getName().equals("java.lang.String")) {
						method.invoke(obj, rs.getString(columnName));
					} else if (field.getType().getName().equals("java.lang.Double")) {
						method.invoke(obj, rs.getDouble(columnName));
					} else if (field.getType().getName().equals("double")) {
						method.invoke(obj, rs.getDouble(columnName));
					}else if (field.getType().getName().equals("long")) {
						method.invoke(obj, rs.getLong(columnName));
					}else if (field.getType().getName().equals("java.lang.Long")) {
						method.invoke(obj, rs.getLong(columnName));
					} else if (field.getType().getName().equals("java.util.Date")) {
						method.invoke(obj, rs.getDate(columnName));
					}
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, stmt, rs);
		}
		return list;
	}
	
}
