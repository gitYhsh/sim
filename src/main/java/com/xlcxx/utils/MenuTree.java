package com.xlcxx.utils;

import java.util.ArrayList;
import java.util.List;

public class MenuTree<T> {
	/**
	 * 节点ID
	 */
	private String id;
	/**
	 * 图标
	 */
	private String icon;
	/**
	 * url
	 */
	private String path;

	private String  component;
	/**
	 * 显示节点文本
	 */
	private String label;

	/**
	 * 节点的子节点
	 */
	private List<MenuTree<T>> children = new ArrayList<>();

	/**是否需要展开**/
	private int head ;

	/**
	 * 父ID
	 */
	private String parentId;

	public int getHead() {
		return head;
	}

	public void setHead(int head) {
		this.head = head;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<MenuTree<T>> getChildren() {
		return children;
	}

	public void setChildren(List<MenuTree<T>> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
