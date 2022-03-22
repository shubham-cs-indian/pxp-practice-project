package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForSaveRelationshipInstancesResponseModel extends IModel {
  
  public static final String REFERENCED_NATURE_RELATIONSHIPS     = "referencedNatureRelationships";
  public static final String REFERENCED_RELATIONSHIPS            = "referencedRelationships";
  public static final String REFERENCED_VARIANT_CONTEXTS         = "referencedVariantContexts";
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String REFERENCED_TAGS                     = "referencedTags";
  public static final String REFERENCED_ELEMENTS                 = "referencedElements";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS         = "side2LinkedVariantKrIds";
  public static final String LINKED_VARIANT_PROPERTY_IIDS        = "linkedVariantPropertyIids";
  
  public List<String> getSide2LinkedVariantKrIds();
  
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  
  public List<Long> getLinkedVariantPropertyIids();
  
  public void setLinkedVariantPropertyIids(List<Long> linkedVariantPropertyIids);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships();
  
  void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
