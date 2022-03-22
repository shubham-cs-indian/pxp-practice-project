package com.cs.core.config.strategy.model.attribute;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForSaveConcatenatedAttributeModel extends IModel {

  public static final String REFERENCED_ELEMENTS                                     = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES                                   = "referencedAttributes";
  public static final String REFERENCED_TAGS                                         = "referencedTags";
  public static final String REFERENCED_ATTRIBUTES_FOR_CALCULATED                    = "referencedAttributesForCalculated";

  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<Long, IAttribute> getReferencedAttributesForCalculated();
  
  public void setReferencedAttributesForCalculated(Map<Long, IAttribute> referencedAttributesForCalculated);
  
}
