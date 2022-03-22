package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IRelationshipSide extends IConfigEntity {
  
  public static final String CARDINALITY          = "cardinality";
  public static final String KLASS_ID             = "klassId";
  public static final String LABEL                = "label";
  public static final String IS_VISIBLE           = "isVisible";
  public static final String ATTRIBUTES           = "attributes";
  public static final String TAGS                 = "tags";
  public static final String RELATIONSHIPS        = "relationships";
  
  public static final String CONTEXT_ID           = "contextId";
  public static final String ELEMENT_ID           = "elementId";
  public static final String TYPE                 = "type";
  
  public String getCardinality();
  
  public void setCardinality(String cardinality);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public List<IReferencedRelationshipProperty> getAttributes();
  
  public void setAttributes(List<IReferencedRelationshipProperty> attributes);
  
  public List<IReferencedRelationshipProperty> getTags();
  
  public void setTags(List<IReferencedRelationshipProperty> tags);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getElementId();
  
  public void setElementId(String elementId);
  
  public List<IReferencedRelationshipProperty> getRelationships();
  
  public void setRelationships(List<IReferencedRelationshipProperty> relationships);
  
  public void setType(String type);
  
  public String getType();
  
}
