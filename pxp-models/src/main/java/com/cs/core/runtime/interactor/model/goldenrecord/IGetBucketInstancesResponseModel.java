package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetBucketInstancesResponseModel extends IModel {
  
  public static final String KLASS_INSTANCES_LIST     = "klassInstancesList";
  public static final String REFERENCED_ASSETS        = "referencedAssets";
  public static final String REFERENCED_KLASSES       = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES    = "referencedTaxonomies";
  public static final String REFERENCED_ORGANIZATIONS = "referencedOrganizations";
  public static final String COUNT                    = "count";
  
  public List<IBucketKlassInstanceInfoModel> getKlassInstancesList();
  
  public void setKlassInstancesList(List<IBucketKlassInstanceInfoModel> klassInstanceList);
  
  public Map<String, IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedKlasses);
  
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations();
  
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations);
  
  public Long getCount();
  
  public void setCount(Long count);
}
