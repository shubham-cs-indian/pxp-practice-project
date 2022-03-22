package com.cs.core.runtime.interactor.model.instance;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;

public interface IGetInstanceRequestStrategyModelForCustomTab
    extends IGetInstanceRequestStrategyModel {
  
  public static final String REFERENCED_NATURE_RELATIONSHIPS_IDS  = "referencedNatureRelationshipsIds";
  public static final String REFERENCED_VARIANT_CONTEXTS          = "referencedVariantContexts";
  public static final String CONTEXT_TAGS_FOR_TARGET_RELATIONSHIP = "contextTagsForTargetRelationship";
  public static final String REFERENCED_RELATIONSHIPS_IDS         = "referencedRelationshipsIds";
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS     = "referencedLifeCycleStatusTags";
  public static final String REFERENCED_NATURE_RELATIONSHIPS      = "referencedNatureRelationships";
  public static final String REFERENCED_ATTRIBUTES                = "referencedAttributes";
  public static final String REFERENCED_TAGS                      = "referencedTags";
  public static final String READ_ONLY_ATTRIBUTE_IDS              = "readOnlyAttributeIds";
  public static final String READ_ONLY_TAG_IDS                    = "readOnlyTagIds";
  public static final String REFERENCED_ELEMENTS                  = "referencedElements";
  
  public List<String> getReferencedNatureRelationshipsIds();
  
  public void setReferencedNatureRelationshipsIds(List<String> referencedNatureRelationshipsIds);
  
  public List<String> getReferencedRelationshipsIds();
  
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds);
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public List<String> getContextTagsForTargetRelationship();
  
  public void setContextTagsForTargetRelationship(List<String> contextTagsForTargetRelationship);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<String> getReadOnlyAttributeIds();
  
  public void setReadOnlyAttributeIds(List<String> contextTagsForTargetRelationship);
  
  public List<String> getReadOnlyTagIds();
  
  public void setReadOnlyTagIds(List<String> contextTagsForTargetRelationship);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
}
