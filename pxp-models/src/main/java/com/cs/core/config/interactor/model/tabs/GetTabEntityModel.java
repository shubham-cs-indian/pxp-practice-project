package com.cs.core.config.interactor.model.tabs;

import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.tabs.Tab;

import java.util.List;

public class GetTabEntityModel implements IGetTabEntityModel {
  
  private static final long serialVersionUID = 1L;
  protected ITab            tab;
  protected Integer         sequence;
  
  public GetTabEntityModel()
  {
    tab = new Tab();
  }
  
  @Override
  public String getLabel()
  {
    return tab.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    tab.setLabel(label);
  }
  
  @Override
  public String getId()
  {
    return tab.getId();
  }
  
  @Override
  public void setId(String id)
  {
    tab.setId(id);
  }
  
  @Override
  public List<String> getPropertySequenceList()
  {
    return tab.getPropertySequenceList();
  }
  
  @Override
  public void setPropertySequenceList(List<String> propertySequenceList)
  {
    tab.setPropertySequenceList(propertySequenceList);
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
    return tab.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    tab.setIcon(icon);
  }
  
  @Override
  public String getCode()
  {
    return tab.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    tab.setCode(code);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return tab.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    tab.setIsStandard(isStandard);
  }

  @Override
  public String getIconKey()
  {
    return tab.getIconKey();
  }

  @Override
  public void setIconKey(String iconKey)
  {
    tab.setIconKey(iconKey);
  }
}
