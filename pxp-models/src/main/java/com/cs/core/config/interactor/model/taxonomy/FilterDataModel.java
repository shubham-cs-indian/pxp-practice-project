package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class FilterDataModel implements IFilterDataModel {
  
  protected List<IConfigEntityTreeInformationModel> tags;
  protected List<IConfigEntityTreeInformationModel> attributes;
  
  @Override
  public List<IConfigEntityTreeInformationModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  public void setTags(List<IConfigEntityTreeInformationModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IConfigEntityTreeInformationModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  @Override
  public void setAttributes(List<IConfigEntityTreeInformationModel> attributes)
  {
    this.attributes = attributes;
  }
}
