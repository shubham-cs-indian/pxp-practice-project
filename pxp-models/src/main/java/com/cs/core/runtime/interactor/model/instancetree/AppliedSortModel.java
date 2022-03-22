package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.runtime.interactor.model.filter.SortModel;

public class AppliedSortModel extends SortModel implements IAppliedSortModel {
  
  private static final long serialVersionUID = 1L;
  protected String          code;
  protected String          label            = "";
  protected Boolean         isNumeric        = false;
  protected String 			icon;
  protected String 			iconKey;

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
  public Boolean getIsNumeric()
  {
    return isNumeric;
  }
  
  @Override
  public void setIsNumeric(Boolean isNumeric)
  {
    this.isNumeric = isNumeric;
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
