package com.cs.core.config.interactor.model.mapping;

import java.util.List;

import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPropertyGroupInfoResponseModel extends IModel {
  
  public static final String ATTRIBUTE_LIST = "attributeList";
  public static final String TAG_LIST       = "tagList";
  
  public List<IConfigEntityInformationModel> getAttributeList();
  
  public void setAttributeList(List<IConfigEntityInformationModel> attributeList);
  
  public List<ITagInfoModel> getTagList();
  
  public void setTagList(List<ITagInfoModel> tagList);
  
  public void setAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings);
  
  public List<IConfigRuleAttributeOutBoundMappingModel> getAttributeMappings();
  
  public void setTagMappings(List<IConfigRuleTagOutBoundMappingModel> tagMappings);
  
  public List<IConfigRuleTagOutBoundMappingModel> getTagMappings();
  
  public IConfigModelForBoarding getConfigDetails();
  
  public void setConfigDetails(IConfigModelForBoarding configDetails);
}
