package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

public interface ISaveStrategyInstanceResponseModel extends IModel {
  
  public static final String KLASS_INSTANCE_INFO             = "klassInstanceInfo";
  public static final String BULK_PROPAGATION_INFO           = "bulkPropagationInfo";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA = "updateSearchableDocumentData";
  
  public IKlassInstanceInfoModel getKlassInstanceInfo();
  
  public void setKlassInstanceInfo(IKlassInstanceInfoModel klassInstanceInfo);
  
  public IBulkPropagationInfoModel getBulkPropagationInfo();
  
  public void setBulkPropagationInfo(IBulkPropagationInfoModel bulkPropagationInfo);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
}
