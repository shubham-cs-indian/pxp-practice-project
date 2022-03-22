package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IDeleteContentsDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteContentsDataPreparationModel implements IDeleteContentsDataPreparationModel {
  
  private static final long                       serialVersionUID = 1L;
  protected List<String>                          deletedContentIds;
  protected List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate;
  
  @Override
  public List<String> getDeletedContentIds()
  {
    if (deletedContentIds == null) {
      deletedContentIds = new ArrayList<>();
    }
    return deletedContentIds;
  }
  
  @Override
  public void setDeletedContentIds(List<String> deletedContentIds)
  {
    this.deletedContentIds = deletedContentIds;
  }
  
  @Override
  public List<IDeletedContentTypeIdsInfoModel> getKlassInstancesToUpdate()
  {
    if (klassInstancesToUpdate == null) {
      klassInstancesToUpdate = new ArrayList<>();
    }
    return klassInstancesToUpdate;
  }
  
  @Override
  public void setKlassInstancesToUpdate(
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate)
  {
    this.klassInstancesToUpdate = klassInstancesToUpdate;
  }
}
