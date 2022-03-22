package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.klass.TypeInfoWithContentIdentifiersModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.BulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.clone.ICloneInstanceStrategyResponseModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class CloneInstanceStrategyResponseModel implements ICloneInstanceStrategyResponseModel {
  
  private static final long                          serialVersionUID = 1L;
  protected IBulkPropagationInfoModel                bulkPropagationInfo;
  protected IUpdateSearchableInstanceModel           updateSearchableDocumentData;
  protected List<IRelationshipDataTransferInfoModel> dataTransferInfo;
  protected ITypeInfoWithContentIdentifiersModel     contentInfo;
  
  @Override
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo()
  {
    return dataTransferInfo;
  }
  
  @JsonDeserialize(contentAs = RelationshipDataTransferInfoModel.class)
  @Override
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo)
  {
    this.dataTransferInfo = dataTransferInfo;
  }
  
  @Override
  public IBulkPropagationInfoModel getBulkPropagationInfo()
  {
    return bulkPropagationInfo;
  }
  
  @Override
  @JsonDeserialize(as = BulkPropagationInfoModel.class)
  public void setBulkPropagationInfo(IBulkPropagationInfoModel bulkPropagationInfo)
  {
    this.bulkPropagationInfo = bulkPropagationInfo;
  }
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(as = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
  
  @Override
  public ITypeInfoWithContentIdentifiersModel getContentInfo()
  {
    return contentInfo;
  }
  
  @JsonDeserialize(as = TypeInfoWithContentIdentifiersModel.class)
  @Override
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo)
  {
    this.contentInfo = contentInfo;
  }
}
