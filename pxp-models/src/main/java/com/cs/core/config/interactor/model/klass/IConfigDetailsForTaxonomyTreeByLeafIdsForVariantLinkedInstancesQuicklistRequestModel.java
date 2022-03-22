package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel
    extends IModel {
  
  public static final String USER_ID            = "userId";
  public static final String ENTITY_ID          = "entityId";
  public static final String PARENT_TAXONOMY_ID = "parentTaxonomyId";
  public static final String LEAF_IDS           = "leafIds";
  public static final String IS_KLASS_TAXONOMY  = "isKlassTaxonomy";
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public List<String> getLeafIds();
  
  public void setLeafIds(List<String> leafIds);
  
  public Boolean getIsKlassTaxonomy();
  
  public void setIsKlassTaxonomy(Boolean isKlassTaxonomy);
}
