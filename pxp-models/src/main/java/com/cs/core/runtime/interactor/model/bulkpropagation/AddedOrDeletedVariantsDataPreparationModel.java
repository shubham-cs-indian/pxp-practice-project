package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IAddedOrDeletedVariantsDataPreparationModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;

import java.util.ArrayList;
import java.util.List;

public class AddedOrDeletedVariantsDataPreparationModel
    implements IAddedOrDeletedVariantsDataPreparationModel {
  
  private static final long          serialVersionUID   = 1L;
  protected List<String>             addedVariantsIds   = new ArrayList<>();
  protected List<String>             deletedVariantsIds = new ArrayList<>();
  protected IContentTypeIdsInfoModel klassInstanceToUpdate;
  
  @Override
  public List<String> getAddedVariantsIds()
  {
    return addedVariantsIds;
  }
  
  @Override
  public void setAddedVariantsIds(List<String> addedVariantsIds)
  {
    this.addedVariantsIds = addedVariantsIds;
  }
  
  @Override
  public List<String> getDeletedVariantsIds()
  {
    return deletedVariantsIds;
  }
  
  @Override
  public void setDeletedVariantsIds(List<String> deletedVariantsIds)
  {
    this.deletedVariantsIds = deletedVariantsIds;
  }
  
  @Override
  public IContentTypeIdsInfoModel getKlassInstanceToUpdate()
  {
    return klassInstanceToUpdate;
  }
  
  @Override
  public void setKlassInstanceToUpdate(IContentTypeIdsInfoModel klassInstanceToUpdate)
  {
    this.klassInstanceToUpdate = klassInstanceToUpdate;
  }
}
