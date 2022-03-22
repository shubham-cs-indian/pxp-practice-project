package com.cs.core.config.interactor.model.configuration.base;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

public interface IUpdateDuplicateStatusResponseModel extends IIdAndTypeModel {
  
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA = "updateSearchableDocumentData";
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
}
