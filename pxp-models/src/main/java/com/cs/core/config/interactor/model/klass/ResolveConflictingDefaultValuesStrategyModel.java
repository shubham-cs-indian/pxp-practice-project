package com.cs.core.config.interactor.model.klass;

import java.util.List;

public class ResolveConflictingDefaultValuesStrategyModel
    implements IResolveConflictingDefaultValuesStrategyModel {
  
  protected List<IDefaultValueChangeModel> defaultValuesDiff;
  protected List<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomyIdsList;
  protected Boolean                        isDeleteUsecase = false;
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    return defaultValuesDiff;
  }
  
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public List<IContentTypeIdsInfoModel> getContentKlassIdsAndTaxonomyIdsList()
  {
    return contentKlassIdsAndTaxonomyIdsList;
  }
  
  @Override
  public void setContentKlassIdsAndTaxonomyIdsList(
      List<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomyIdsList)
  {
    this.contentKlassIdsAndTaxonomyIdsList = contentKlassIdsAndTaxonomyIdsList;
  }
  
  @Override
  public Boolean getIsDeleteUsecase()
  {
    return isDeleteUsecase;
  }
  
  @Override
  public void setIsDeleteUsecase(Boolean isDeleteUsecase)
  {
    this.isDeleteUsecase = isDeleteUsecase;
  }
}
