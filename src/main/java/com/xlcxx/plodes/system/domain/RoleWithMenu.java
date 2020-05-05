package com.xlcxx.plodes.system.domain;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class RoleWithMenu extends Role{

	private static final long serialVersionUID = 2013847071068967187L;
	
	private Long menuId;
	
	private List<Long> menuIds;

	private JSONArray myUsers;

	public JSONArray getMyUsers() {
		return myUsers;
	}

	public void setMyUsers(JSONArray myUsers) {
		this.myUsers = myUsers;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public List<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<Long> menuIds) {
		this.menuIds = menuIds;
	}
	
	

}
