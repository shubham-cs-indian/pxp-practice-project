package com.cs.core.config.interactor.model.datarule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;

public interface IDataRulesHelperModel {
  
  public static final String PRODUCT_IDENTIFIERS = "productIdentifiers";
  public static final String REMOVED_MANDATORY_IDENTIFIERS = "removedMandatoryIdentifiers";
  public static final String TRANSLATABLE_PROPERTY_IIDS    = "translatablePropertyIIDs";
  public static final String MUST                          = "must";
  public static final String SHOULD                        = "should";
  public static final String REFERENCED_ELEMENTS           = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES         = "referencedAttributes";
  public static final String REFERENCED_TAGS               = "referencedTags";
  
  public Map<Long,List<Long>> getProductIdentifiers();
  
  public void setProductIdentifiers(Map<Long,List<Long>> productIdentifiers);
  
  public List<Long> getRemovedMandatoryIdentifiers();
  
  public void setRemovedMandatoryIdentifiers(List<Long> removedMandatoryIdentifiers);
  
  public List<Long> getTranslatablePropertyIIDs();
  
  public void setTranslatablePropertyIIDs(List<Long> translatablePropertyIIDs);
  
  public List<Long> getMust();
  
  public void setMust(List<Long> must);
  
  public List<Long> getShould();
  
  public void setShould(List<Long> should);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
