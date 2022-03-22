package com.cs.core.runtime.interactor.model.clone;

import java.util.Map;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.ReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassInstancePropertiesForCloneModel
    implements IGetKlassInstancePropertiesForCloneModel {
  
  private static final long                                  serialVersionUID = 1L;
  protected Map<String, IConfigEntityInformationModel>       attributes;
  protected Map<String, IConfigEntityInformationModel>       tags;
  protected Map<String, IConfigEntityInformationModel>       relationships;
  protected Map<String, IReferencedPropertyCollectionModel>  propertyCollections;
  protected Map<String, IReferencedKlassDetailStrategyModel> klasses;
  protected Map<String, IReferencedArticleTaxonomyModel>     taxonomies;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setAttributes(Map<String, IConfigEntityInformationModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setTags(Map<String, IConfigEntityInformationModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getRelationships()
  {
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setRelationships(Map<String, IConfigEntityInformationModel> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public Map<String, IReferencedPropertyCollectionModel> getPropertyCollections()
  {
    
    return propertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionModel.class)
  public void setPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> propertyCollections)
  {
    
    this.propertyCollections = propertyCollections;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getKlasses()
  {
    return klasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getTaxonomies()
  {
    return taxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setTaxonomies(Map<String, IReferencedArticleTaxonomyModel> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
}
