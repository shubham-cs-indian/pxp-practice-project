package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.model.tag.ITagBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetSectionInfoForRelationshipExportResponseModel extends IModel {
  
  public static final String ATTRIBUTES    = "attributes";
  public static final String RELATIONSHIPS = "relationships";
  public static final String TAGS          = "tags";
  
  public List<IConfigEntityInformationModel> getAttributes();
  public void setAttributes(List<IConfigEntityInformationModel> attributes);
  
  public List<IConfigEntityInformationModel> getRelationships();
  public void setRelationships(List<IConfigEntityInformationModel> relationships);
  
  public List<ITagBasicInfoModel> getTags();
  public void setTags(List<ITagBasicInfoModel> tags);
  
}