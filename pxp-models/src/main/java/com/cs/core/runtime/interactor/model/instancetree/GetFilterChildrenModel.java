package com.cs.core.runtime.interactor.model.instancetree;

public class GetFilterChildrenModel implements IGetFilterChildrenModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          code;
  protected String          label;
  protected String          id;
  protected Long            count;
  protected Double          from;
  protected Double          to;
  protected String          icon;
  protected String          iconKey;
  
  public GetFilterChildrenModel(String id, String label, long count)
  {
    super();
    this.id=id;
    this.label=label;
    this.count=count;
  }
  public GetFilterChildrenModel()
  {
    
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
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public Double getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Double from)
  {
    this.from = from;
  }
  
  @Override
  public Double getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(Double to)
  {
    this.to = to;
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
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
}
