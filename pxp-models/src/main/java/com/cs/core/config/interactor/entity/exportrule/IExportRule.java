package com.cs.core.config.interactor.entity.exportrule;

import java.util.List;

public interface IExportRule {
  
  public String getName();
  
  public void setName(String name);
  
  public String getId();
  
  public void setId(String id);
  
  public String getModule();
  
  public void setModule(String module);
  
  public String getSubModule();
  
  public void setSubModule(String subModule);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<IAttributeRule> getAttributes();
  
  public void setAttributes(List<IAttributeRule> attributes);
  
  public List<ITagRule> getTags();
  
  public void setTags(List<ITagRule> tags);
  
  public Boolean getIsDisabled();
  
  public void setIsDisbaled(Boolean isDisabled);
}
