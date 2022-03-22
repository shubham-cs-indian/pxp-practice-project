package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Set;

public interface IConfigDetailsForQuickListModel extends IModel {
  
  public static final String DISPLAY_TAG_IDS             = "displayTagIds";
  public static final String RELEVANCE_TAG_IDS           = "relevanceTagIds";
  public static final String ENTITIES                    = "entities";
  public static final String KLASS_IDS_HAVING_RP         = "klassIdsHavingRP";
  public static final String ALLOWED_TYPES               = "allowedTypes";
  public static final String KLASS_TYPE                  = "klassType";
  public static final String X_RAY_CONFIG_DETAILS        = "xrayConfigDetails";
  public static final String TAXONOMY_IDS_HAVING_RP      = "taxonomyIdsHavingRP";
  public static final String KLASS_IDS_HAVING_CP         = "klassIdsHavingCP";
  public static final String KLASS_RELATIONSHIP_IDS      = "klassRelationshipIds";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS = "side2LinkedVariantKrIds";
  
  public List<String> getDisplayTagIds();
  
  public void setDisplayTagIds(List<String> displayTagIds);
  
  public List<String> getRelevanceTagIds();
  
  public void setRelevanceTagIds(List<String> relevanceTagIds);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> allowedTypes);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getKlassIdsHavingCP();
  
  public void setKlassIdsHavingCP(Set<String> klassIdsHavingCP);
  
  public IRelationship getRelationshipConfig();
  
  public void setRelationshipConfig(IRelationship relationshipConfig);
  
  public List<String> getKlassRelationshipIds();
  
  public void setKlassRelationshipIds(List<String> klassRelationshipIds);
  
  public List<String> getSide2LinkedVariantKrIds();
  
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
}
