package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAddedOrDeletedVariantsDataPreparationModel extends IModel {
  
  public static final String ADDED_VARIANTS_IDS       = "addedVariantsIds";
  public static final String DELETED_VARIANTS_IDS     = "deletedVariantsIds";
  public static final String KLASS_INSTANCE_TO_UPDATE = "klassInstanceToUpdate";
  
  public List<String> getAddedVariantsIds();
  
  public void setAddedVariantsIds(List<String> addedVariantsIds);
  
  public List<String> getDeletedVariantsIds();
  
  public void setDeletedVariantsIds(List<String> deletedVariantsIds);
  
  public IContentTypeIdsInfoModel getKlassInstanceToUpdate();
  
  public void setKlassInstanceToUpdate(IContentTypeIdsInfoModel klassInstanceToUpdate);
}
