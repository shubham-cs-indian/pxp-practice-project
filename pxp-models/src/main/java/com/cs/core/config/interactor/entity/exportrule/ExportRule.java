package com.cs.core.config.interactor.entity.exportrule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ExportRule implements IExportRule {
  
  protected String               id;
  protected String               module;
  protected String               subModule;
  protected List<String>         klassIds;
  protected List<IAttributeRule> attributes;
  protected List<ITagRule>       tags;
  protected String               name;
  protected Boolean              isDisabled;
  
  @Override
  public String getModule()
  {
    return module;
  }
  
  @Override
  public void setModule(String module)
  {
    this.module = module;
  }
  
  @Override
  public String getSubModule()
  {
    return subModule;
  }
  
  @Override
  public void setSubModule(String subModule)
  {
    this.subModule = subModule;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<IAttributeRule> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeRule.class)
  @Override
  public void setAttributes(List<IAttributeRule> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ITagRule> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagRule.class)
  @Override
  public void setTags(List<ITagRule> tags)
  {
    this.tags = tags;
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
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisbaled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
}
