package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetTemplateModel implements IGetTemplateModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ITemplate       entity           = new Template();
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public ITemplateHeaderVisibility getHeaderVisibility()
  {
    return entity.getHeaderVisibility();
  }
  
  @JsonDeserialize(as = TemplateHeaderVisibility.class)
  @Override
  public void setHeaderVisibility(ITemplateHeaderVisibility headerVisibility)
  {
    entity.setHeaderVisibility(headerVisibility);
  }
  
  @Override
  public List<ITemplateTab> getTabs()
  {
    return entity.getTabs();
  }
  
  @JsonDeserialize(contentAs = AbstractTemplateTab.class)
  @Override
  public void setTabs(List<ITemplateTab> tabs)
  {
    entity.setTabs(tabs);
  }
  
  /**
   * ******************* ignored properties *************************
   */
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public List<String> getContextIds()
  {
    return entity.getContextIds();
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    entity.setContextIds(contextIds);
  }
}
