package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.ValueInheritancePropagationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetInstanceInfoForBulkPropagationResponseModel
    implements IGetInstanceInfoForBulkPropagationResponseModel {
  
  private static final long                   serialVersionUID = 1L;
  protected List<IContentTypeIdsInfoModel>    contentTypeIdsInfoModelList;
  protected IValueInheritancePropagationModel valuePropagationDataForRelationship;
  
  @Override
  public List<IContentTypeIdsInfoModel> getContentTypeIdsInfoModelList()
  {
    if (contentTypeIdsInfoModelList == null) {
      contentTypeIdsInfoModelList = new ArrayList<>();
    }
    return contentTypeIdsInfoModelList;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentTypeIdsInfoModel.class)
  public void setContentTypeIdsInfoModelList(
      List<IContentTypeIdsInfoModel> contentTypeIdsInfoModelList)
  {
    this.contentTypeIdsInfoModelList = contentTypeIdsInfoModelList;
  }
  
  @Override
  public IValueInheritancePropagationModel getValuePropagationDataForRelationship()
  {
    return valuePropagationDataForRelationship;
  }
  
  @Override
  @JsonDeserialize(as = ValueInheritancePropagationModel.class)
  public void setValuePropagationDataForRelationships(
      IValueInheritancePropagationModel valuePropagationDataForRelationship)
  {
    this.valuePropagationDataForRelationship = valuePropagationDataForRelationship;
  }
}
