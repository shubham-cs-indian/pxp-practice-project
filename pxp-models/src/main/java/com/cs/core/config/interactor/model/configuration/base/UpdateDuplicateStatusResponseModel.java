package com.cs.core.config.interactor.model.configuration.base;

import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UpdateDuplicateStatusResponseModel extends IdAndTypeModel
    implements IUpdateDuplicateStatusResponseModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected IUpdateSearchableInstanceModel updateSearchableDocumentData;
  
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
