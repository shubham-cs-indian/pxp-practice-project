package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteContentsDataPreparationModel extends IModel {
  
  String DELETED_CONTENT_IDS       = "deletedContentIds";
  String KLASS_INSTANCES_TO_UPDATE = "klassInstancesToUpdate";
  
  public List<String> getDeletedContentIds();
  
  public void setDeletedContentIds(List<String> deletedContentIds);
  
  public List<IDeletedContentTypeIdsInfoModel> getKlassInstancesToUpdate();
  
  public void setKlassInstancesToUpdate(
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate);
}
