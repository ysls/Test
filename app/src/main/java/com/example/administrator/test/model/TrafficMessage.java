package com.example.administrator.test.model;

public class TrafficMessage {
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApplyed() {
		return applyed;
	}
	public void setApplyed(String applyed) {
		this.applyed = applyed;
	}
	public String getSurplus() {
		return surplus;
	}
	public void setSurplus(String surplus) {
		this.surplus = surplus;
	}
	public String getAll() {
		return all;
	}
	public void setAll(String all) {
		this.all = all;
	}
	
	
	public String getTypeContext() {
		return typeContext;
	}
	public void setTypeContext(String typeContext) {
		this.typeContext = typeContext;
	}

       /**
        * ����˵��
        */
	private String typeContext;
	private Integer id;
	/**
	 * ʹ��
	 */
	private String applyed;//ʹ����
	/**
	 * ʣ��
	 */
	private String surplus;//ʣ��
	/**
	 * һ��
	 */
	private String all;//һ��
	

}
