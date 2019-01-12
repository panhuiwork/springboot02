package com.geral.springboot01.contral;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.geral.springboot01.dao.BaseDao;
import com.geral.springboot01.dao.Userinfo2;

@RestController
public class Contral {

	@Autowired
	private BaseDao baseDao;
	
	@GetMapping("/getin")
	public List<Object> getin() 
	{
		List<Object> ulis=null;
		try {
			ulis = baseDao.executeGetClass("select * from userinfo2", Userinfo2.class);
		} catch (SQLException e) {
		}
		return ulis;
	}
}
