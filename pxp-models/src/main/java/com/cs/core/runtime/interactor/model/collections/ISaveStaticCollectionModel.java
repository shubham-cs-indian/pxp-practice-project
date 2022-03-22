package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.List;

public interface ISaveStaticCollectionModel extends IGetKlassInstanceTreeStrategyModel {
  
  public static final String ADDED_KLASS_INSTANCE_IDS   = "addedKlassInstanceIds";
  public static final String REMOVED_KLASS_INSTANCE_IDS = "removedKlassInstanceIds";
  public static final String FILTER_RESULTS_TO_SAVE     = "filterResultsToSave";
  public static final String LABEL                      = "label";
  public static final String IS_PUBLIC                  = "isPublic";
  public static final String PARENT_ID                  = "parentId";

  
  public List<IIdAndTypeModel> getAddedKlassInstanceIds();
  
  public void setAddedKlassInstanceIds(List<IIdAndTypeModel> addedKlassInstanceIds);
  
  public List<IIdAndTypeModel> getRemovedKlassInstanceIds();
  
  public void setRemovedKlassInstanceIds(List<IIdAndTypeModel> removedKlassInstanceIds);
  
  public IGetKlassInstanceTreeStrategyModel getFilterResultsToSave();
  
  public void setFilterResultsToSave(IGetKlassInstanceTreeStrategyModel filterResultsToSave);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsPublic();
  
  public void setIsPublic(Boolean isPublic);
  
  public String getParentId();

  public void setParentId(String parentId);
}
