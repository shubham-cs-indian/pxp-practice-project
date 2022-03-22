package com.cs.core.runtime.interactor.model.clone;

import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;

public interface ICloneInstanceStrategyResponseModel extends IModel {
  
  public static final String CONTENT_INFO                    = "contentInfo";
  public static final String BULK_PROPAGATION_INFO           = "bulkPropagationInfo";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA = "updateSearchableDocumentData";
  public static final String DATA_TRANSFER_INFO              = "dataTransferInfo";
  
  public List<IRelationshipDataTransferInfoModel> getDataTransferInfo();
  
  public void setDataTransferInfo(List<IRelationshipDataTransferInfoModel> dataTransferInfo);
  
  public IBulkPropagationInfoModel getBulkPropagationInfo();
  
  public void setBulkPropagationInfo(IBulkPropagationInfoModel bulkPropagationInfo);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
  
  public ITypeInfoWithContentIdentifiersModel getContentInfo();
  
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo);
}
