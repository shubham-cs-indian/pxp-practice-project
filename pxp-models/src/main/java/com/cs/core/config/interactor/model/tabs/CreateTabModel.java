package com.cs.core.config.interactor.model.tabs;

public class CreateTabModel implements ICreateTabModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          code;
  protected Boolean         isStandard       = false;
  protected Integer         sequence;
  protected String          icon;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
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
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public Integer getSequence()
  {
    return sequence;
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    this.sequence = sequence;
  }

  @Override
  public String getIcon()
  {
    return icon;
  }

  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
}
