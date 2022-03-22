package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveStaticCollectionModel extends GetKlassInstanceTreeStrategyModel
    implements ISaveStaticCollectionModel {
  
  private static final long                    serialVersionUID = 1L;
  protected List<IIdAndTypeModel>              addedKlassInstanceIds;
  protected List<IIdAndTypeModel>              removedKlassInstanceIds;
  protected IGetKlassInstanceTreeStrategyModel filterResultsToSave;
  protected String                             label;
  protected Boolean                            isPublic;
  protected String                             parentId;

  
  @Override
  public Boolean getIsPublic()
  {
    return isPublic;
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    this.isPublic = isPublic;
  }
  
  @Override
  public List<IIdAndTypeModel> getAddedKlassInstanceIds()
  {
    if (addedKlassInstanceIds == null) {
      addedKlassInstanceIds = new ArrayList<>();
    }
    return addedKlassInstanceIds;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setAddedKlassInstanceIds(List<IIdAndTypeModel> addedKlassInstanceIds)
  {
    this.addedKlassInstanceIds = addedKlassInstanceIds;
  }
  
  @Override
  public List<IIdAndTypeModel> getRemovedKlassInstanceIds()
  {
    if (removedKlassInstanceIds == null) {
      removedKlassInstanceIds = new ArrayList<>();
    }
    return removedKlassInstanceIds;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setRemovedKlassInstanceIds(List<IIdAndTypeModel> removedKlassInstanceIds)
  {
    this.removedKlassInstanceIds = removedKlassInstanceIds;
  }
  
  @Override
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave()
  {
    return filterResultsToSave;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  @Override
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave)
  {
    this.filterResultsToSave = filterResultsToSave;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }

  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }

}
