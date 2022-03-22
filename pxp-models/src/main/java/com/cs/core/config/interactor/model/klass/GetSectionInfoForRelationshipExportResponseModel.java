package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.tag.ITagBasicInfoModel;
import com.cs.core.config.interactor.model.tag.TagBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetSectionInfoForRelationshipExportResponseModel implements IGetSectionInfoForRelationshipExportResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<IConfigEntityInformationModel> attributes       = new ArrayList<>();
  protected List<IConfigEntityInformationModel> relationships    = new ArrayList<>();
  protected List<ITagBasicInfoModel>            tags             = new ArrayList<>();
  
  @Override
  public List<IConfigEntityInformationModel> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setAttributes(List<IConfigEntityInformationModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setRelationships(List<IConfigEntityInformationModel> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<ITagBasicInfoModel> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagBasicInfoModel.class)
  public void setTags(List<ITagBasicInfoModel> tags)
  {
    this.tags = tags;
  }
}
