package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetBucketInstancesResponseModel implements IGetBucketInstancesResponseModel {
  
  private static final long                                      serialVersionUID = 1L;
  protected List<IBucketKlassInstanceInfoModel>                  klassInstancesList;
  protected Map<String, IAssetAttributeInstanceInformationModel> referencedAssets;
  protected Map<String, IReferencedArticleTaxonomyModel>         referencedTaxonomies;
  protected Map<String, IReferencedKlassDetailStrategyModel>     referencedKlasses;
  protected Map<String, IIdLabelCodeModel>                       referencedOrganizations;
  protected Long                                                 count;
  
  @Override
  public List<IBucketKlassInstanceInfoModel> getKlassInstancesList()
  {
    return klassInstancesList;
  }
  
  @JsonDeserialize(contentAs = BucketKlassInstanceInfoModel.class)
  @Override
  public void setKlassInstancesList(List<IBucketKlassInstanceInfoModel> klassInstancesList)
  {
    this.klassInstancesList = klassInstancesList;
  }
  
  @Override
  public Map<String, IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  @Override
  public void setReferencedAssets(
      Map<String, IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  @Override
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations()
  {
    return referencedOrganizations;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations)
  {
    this.referencedOrganizations = referencedOrganizations;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
