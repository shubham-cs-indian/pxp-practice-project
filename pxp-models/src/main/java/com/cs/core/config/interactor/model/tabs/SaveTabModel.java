package com.cs.core.config.interactor.model.tabs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveTabModel implements ISaveTabModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  id;
  protected String                                  label;
  protected String                                  icon;
  protected String                                  code;
  protected List<IModifiedTabPropertySequenceModel> modifiedPropertySequence;
  protected Integer                                 newTabSequence;
  
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
  public List<IModifiedTabPropertySequenceModel> getModifiedPropertySequence()
  {
    return modifiedPropertySequence;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedTabPropertySequenceModel.class)
  public void setModifiedPropertySequence(
      List<IModifiedTabPropertySequenceModel> modifiedPropertySequence)
  {
    this.modifiedPropertySequence = modifiedPropertySequence;
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
  public Integer getModifiedTabSequence()
  {
    return newTabSequence;
  }
  
  @Override
  public void setModifiedTabSequence(Integer newTabSequence)
  {
    this.newTabSequence = newTabSequence;
  }
}
