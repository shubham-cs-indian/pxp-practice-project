package com.cs.core.runtime.interactor.entity.datarule;

import java.util.List;

import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict.Setting;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITaxonomyInheritanceRequestModel extends IModel{
  
  public static final String CONTENT_ID                      = "contentId";
  public static final String CONTENT_BASE_TYPE               = "contentBaseType";
  public static final String SOURCE_CONTENT_ID               = "sourceContentId";
  public static final String SOURCE_CONTENT_BASE_TYPE        = "sourceContentBaseType";
  public static final String RELATIONSHIP_ID                 = "relationshipId";
  public static final String RELATIONHSIP_SIDE_ID            = "relationshipSideId";
  public static final String TAXONOMY_INHERITANCE_SETTING    = "taxonomyInheritanceSetting";
  public static final String ADDED_ELEMENTS                  = "addedElements";
  public static final String ADDED_TAXONOMY_IDS              = "addedTaxonomyIds";
  public static final String REMOVED_TAXONOMY_IDS            = "removedTaxonomyIds";

  public String getContentId();
  public void setContentId(String contentId);
  
  public String getContentBaseType();
  public void setContentBaseType(String contentBaseType);
  
  public String getSourceContentId();
  public void setSourceContentId(String sourceContentId);
  
  public String getSourceContentBaseType();
  public void setSourceContentBaseType(String sourceContentBaseType);
  
  public String getRelationshipId();
  public void setRelationshipId(String relationshipId);
  
  public String getRelationshipSideId();
  public void setRelationshipSideId(String relationshipSideId);
  
  public Setting getTaxonomyInheritanceSetting();
  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting);
  
  public List<IIdAndBaseType> getAddedElements();
  public void setAddedElements(List<IIdAndBaseType> addedElements);
  
  public List<String> getAddedTaxonomyIds();
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);

  public List<String> getRemovedTaxonomyIds();
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds);
}
