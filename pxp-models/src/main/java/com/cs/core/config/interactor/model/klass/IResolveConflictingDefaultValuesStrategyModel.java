package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IResolveConflictingDefaultValuesStrategyModel extends IModel {
  
  public static final String DEFAULT_VALUES_DIFF                 = "defaultValuesDiff";
  public static final String CONTENTKLASSIDS_AND_TAXONOMYIDSLIST = "contentKlassIdsAndTaxonomyIdsList";
  public static final String IS_DELETE_USECASE                   = "isDeleteUsecase";
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  public List<IContentTypeIdsInfoModel> getContentKlassIdsAndTaxonomyIdsList();
  
  public void setContentKlassIdsAndTaxonomyIdsList(
      List<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomyIdsList);
  
  public Boolean getIsDeleteUsecase();
  
  public void setIsDeleteUsecase(Boolean isDeleteUsecase);
}
