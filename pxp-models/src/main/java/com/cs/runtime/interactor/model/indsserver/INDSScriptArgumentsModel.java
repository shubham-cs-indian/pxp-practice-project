package com.cs.runtime.interactor.model.indsserver;

public class INDSScriptArgumentsModel implements IINDSScriptArgumentsModel {

  private static final long serialVersionUID = 1L;
  
  private String name;
	private String value;
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
}