package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.ConfigModelForBoarding;
import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPropertyGroupInfoResponseModel implements IGetPropertyGroupInfoResponseModel {
  
  private static final long                                serialVersionUID  = 1L;
  
  protected List<IConfigEntityInformationModel>            attributeList;
  protected List<ITagInfoModel>                            tagList;
  protected List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings = new ArrayList<>();
  protected List<IConfigRuleTagOutBoundMappingModel>       tagMappings       = new ArrayList<>();
  protected IConfigModelForBoarding                        configDetails;
  
  @Override
  public List<IConfigEntityInformationModel> getAttributeList()
  {
    return attributeList;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setAttributeList(List<IConfigEntityInformationModel> attributeList)
  {
    this.attributeList = attributeList;
  }
  
  @Override
  public List<ITagInfoModel> getTagList()
  {
    return tagList;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagInfoModel.class)
  public void setTagList(List<ITagInfoModel> tagList)
  {
    this.tagList = tagList;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleAttributeOutBoundMappingModel.class)
  public void setAttributeMappings(List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings)
  {
    this.attributeMappings = attributeMappings;
  }
  
  @Override
  public List<IConfigRuleAttributeOutBoundMappingModel> getAttributeMappings()
  {
    
    return attributeMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigRuleTagOutBoundMappingModel.class)
  public void setTagMappings(List<IConfigRuleTagOutBoundMappingModel> tagMappings)
  {
    this.tagMappings = tagMappings;
  }
  
  @Override
  public List<IConfigRuleTagOutBoundMappingModel> getTagMappings()
  {
    
    return tagMappings;
  }
  
  @Override
  public IConfigModelForBoarding getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ConfigModelForBoarding.class)
  public void setConfigDetails(IConfigModelForBoarding configDetails)
  {
    this.configDetails = configDetails;
  }
}
