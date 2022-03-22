package com.cs.core.config.interactor.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Template implements ITemplate {
  
  private static final long           serialVersionUID = 1L;
  
  protected String                    id;
  protected String                    label;
  protected String                    icon;
  protected String                    iconKey;
  protected String                    type;
  protected ITemplateHeaderVisibility headerVisibility;
  protected List<ITemplateTab>        tabs             = new ArrayList<>();
  protected Long                      versionId;
  protected String                    code;
  protected List<String>              contextIds       = new ArrayList<>();
  
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
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public ITemplateHeaderVisibility getHeaderVisibility()
  {
    return headerVisibility;
  }
  
  @JsonDeserialize(as = TemplateHeaderVisibility.class)
  @Override
  public void setHeaderVisibility(ITemplateHeaderVisibility headerVisibility)
  {
    this.headerVisibility = headerVisibility;
  }
  
  @Override
  public List<ITemplateTab> getTabs()
  {
    return tabs;
  }
  
  @JsonDeserialize(contentAs = AbstractTemplateTab.class)
  @Override
  public void setTabs(List<ITemplateTab> tabs)
  {
    this.tabs = tabs;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  /**
   * ******************** ignored properties *********************
   */
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public List<String> getContextIds()
  {
    return contextIds;
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    this.contextIds = contextIds;
  }
}
