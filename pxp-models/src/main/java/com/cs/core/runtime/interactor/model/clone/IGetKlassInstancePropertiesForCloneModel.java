package com.cs.core.runtime.interactor.model.clone;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetKlassInstancePropertiesForCloneModel extends IModel {
  
  public static final String ATTRIBUTES           = "attributes";
  public static final String TAGS                 = "tags";
  public static final String RELATIONSHIPS        = "relationships";
  public static final String PROPERTY_COLLECTIONS = "propertyCollections";
  public static final String KLASSES              = "klasses";
  public static final String TAXONOMIES           = "taxonomies";
  
  public Map<String, IConfigEntityInformationModel> getAttributes();
  
  public void setAttributes(Map<String, IConfigEntityInformationModel> attributes);
  
  public Map<String, IConfigEntityInformationModel> getTags();
  
  public void setTags(Map<String, IConfigEntityInformationModel> tags);
  
  public Map<String, IConfigEntityInformationModel> getRelationships();
  
  public void setRelationships(Map<String, IConfigEntityInformationModel> relationships);
  
  public Map<String, IReferencedPropertyCollectionModel> getPropertyCollections();
  
  public void setPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> propertyCollections);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getKlasses();
  
  public void setKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getTaxonomies();
  
  public void setTaxonomies(Map<String, IReferencedArticleTaxonomyModel> taxonomies);
}
