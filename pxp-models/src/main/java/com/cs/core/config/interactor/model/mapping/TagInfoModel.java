package com.cs.core.config.interactor.model.mapping;

import java.util.Collections;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TagInfoModel extends ConfigEntityInformationModel implements ITagInfoModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<IConfigEntityInformationModel> childTag         = Collections.emptyList();
  
  @Override
  public List<IConfigEntityInformationModel> getChildTag()
  {
    return childTag;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setChildTag(List<IConfigEntityInformationModel> childTag)
  {
    this.childTag = childTag;
  }
}
