package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.bulkpropagation.BulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkPropagationInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author CS23
 */
public class SaveStrategyInstanceResponseModel implements ISaveStrategyInstanceResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected IKlassInstanceInfoModel        klassInstanceInfo;
  protected IBulkPropagationInfoModel      bulkPropagationInfo;
  protected IUpdateSearchableInstanceModel updateSearchableDocumentData;
  
  @Override
  public IKlassInstanceInfoModel getKlassInstanceInfo()
  {
    return this.klassInstanceInfo;
  }
  
  @Override
  @JsonDeserialize(as = KlassInstanceInfoModel.class)
  public void setKlassInstanceInfo(IKlassInstanceInfoModel klassInstanceInfo)
  {
    this.klassInstanceInfo = klassInstanceInfo;
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
  
  @Override
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
}
