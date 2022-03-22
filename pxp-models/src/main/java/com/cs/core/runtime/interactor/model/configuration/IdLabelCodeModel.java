package com.cs.core.runtime.interactor.model.configuration;

public class IdLabelCodeModel implements IIdLabelCodeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          label;
  protected String          code;

  public IdLabelCodeModel(String code){
    this.code = code;
  }

  public IdLabelCodeModel(){

  }

  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
}
