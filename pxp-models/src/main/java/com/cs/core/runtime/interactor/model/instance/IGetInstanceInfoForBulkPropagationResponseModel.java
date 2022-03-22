package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetInstanceInfoForBulkPropagationResponseModel extends IModel {
  
  public static final String CONTENT_TYPE_IDS_INFO_LIST               = "contentTypeIdsInfoModelList";
  public static final String VALUE_PROPAGATION_DATA_FOR_RELATIONSHIPS = "valuePropagationDataForRelationships";
  
  public List<IContentTypeIdsInfoModel> getContentTypeIdsInfoModelList();
  
  public void setContentTypeIdsInfoModelList(
      List<IContentTypeIdsInfoModel> contentTypeIdsInfoModelList);
  
  public IValueInheritancePropagationModel getValuePropagationDataForRelationship();
  
  public void setValuePropagationDataForRelationships(
      IValueInheritancePropagationModel valuePropagationDataForRelationship);
}
