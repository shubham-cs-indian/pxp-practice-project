package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

// Node: update IReferencedSectionElementModel when new property is added
public interface ISectionElement extends IConfigEntity {
  
  public static final String START_POSITION             = "startPosition";
  public static final String END_POSITION               = "endPosition";
  public static final String DESCRIPTION                = "description";
  public static final String TOOLTIP                    = "tooltip";
  public static final String LABEL                      = "label";
  public static final String IS_MANDATORY               = "isMandatory";
  public static final String IS_SHOULD                  = "isShould";
  public static final String IS_INHERITED               = "isInherited";
  public static final String PLACEHOLDER                = "placeholder";
  public static final String IS_DISABLED                = "isDisabled";
  public static final String IS_VARIANT_ALLOWED         = "isVariantAllowed";
  public static final String NUMBER_OF_VERSIONS_ALLOWED = "numberOfVersionsAllowed";
  public static final String IS_CUT_OFF                 = "isCutoff";
  public static final String TYPE                       = "type";
  public static final String IS_DELETED                 = "isDeleted";
  public static final String IS_SKIPPED                 = "isSkipped";
  public static final String COUPLING_TYPE              = "couplingType";
  public static final String ATTRIBUTE_VARIANT_CONTEXT  = "attributeVariantContext";
  public static final String PROPERTY_ID                = "propertyId";
  
  public IPosition getStartPosition();
  
  public void setStartPosition(IPosition iPosition);
  
  public IPosition getEndPosition();
  
  public void setEndPosition(IPosition iPosition);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getTooltip();
  
  public void setTooltip(String tooltip);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsMandatory();
  
  public void setIsMandatory(Boolean isMandatory);
  
  public Boolean getIsShould();
  
  public void setIsShould(Boolean isShould);
  
  public Boolean getIsInherited();
  
  public void setIsInherited(Boolean isInherited);
  
  public String getPlaceholder();
  
  public void setPlaceholder(String placeholder);
  
  public Boolean getIsDisabled();
  
  public void setIsDisabled(Boolean isDisabled);
  
  public Boolean getIsVariantAllowed();
  
  public void setIsVariantAllowed(Boolean isVariantAllowed);
  
  public Integer getNumberOfVersionsAllowed();
  
  public void setNumberOfVersionsAllowed(Integer numberOfVersionsAllowed);
  
  public Boolean getIsCutoff();
  
  public void setIsCutoff(Boolean isCutoff);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsDeleted();
  
  public void setIsDeleted(Boolean type);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public Boolean getIsSkipped();
  
  public void setIsSkipped(Boolean isSkipped);
  
  public String getAttributeVariantContext();
  
  public void setAttributeVariantContext(String attributeVariantContext);
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
}
