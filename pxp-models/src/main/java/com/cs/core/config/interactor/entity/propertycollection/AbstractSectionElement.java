package com.cs.core.config.interactor.entity.propertycollection;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({ @JsonSubTypes.Type(name = "attribute", value = SectionAttribute.class),
    @JsonSubTypes.Type(name = "relationship", value = SectionRelationship.class),
    @JsonSubTypes.Type(name = "tag", value = SectionTag.class),
    @JsonSubTypes.Type(name = "role", value = SectionRole.class),
    @JsonSubTypes.Type(name = "taxonomy", value = SectionTaxonomy.class)})
public abstract class AbstractSectionElement implements ISectionElement {
  
  private static final long        serialVersionUID        = 1L;
  
  protected String                 id;
  
  protected String                 label                   = "";
  
  protected String                 description             = "";
  
  protected String                 tooltip                 = "";
  
  protected String                 placeholder             = "";
  
  protected SectionElementPosition startPosition;
  
  protected SectionElementPosition endPosition;
  
  protected Boolean                isMandatory             = false;
  
  protected Boolean                isShould                = false;
  
  protected Boolean                isInherited             = false;
  
  protected Boolean                isCutoff                = false;
  
  protected Boolean                isVariantAllowed        = false;
  
  protected String                 type;
  
  protected Boolean                isDisabled              = false;
  
  protected Integer                numberOfVersionsAllowed = 0;
  
  protected Long                   versionId;
  
  protected List<String>           tagGroups               = new ArrayList<String>();
  
  protected Long                   versionTimestamp;
  
  protected String                 lastModifiedBy;
  
  protected String                 couplingType            = "looselyCoupled";
  
  protected Boolean                isDeleted;
  
  protected Boolean                isSkipped               = false;
  
  protected String                 attributeVariantContext;
  protected String                 code;
  protected String                 propertyId;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public IPosition getStartPosition()
  {
    return this.startPosition;
  }
  
  @JsonDeserialize(as = SectionElementPosition.class)
  @Override
  public void setStartPosition(IPosition startPosition)
  {
    this.startPosition = (SectionElementPosition) startPosition;
  }
  
  @Override
  public IPosition getEndPosition()
  {
    return this.endPosition;
  }
  
  @JsonDeserialize(as = SectionElementPosition.class)
  @Override
  public void setEndPosition(IPosition endPosition)
  {
    this.endPosition = (SectionElementPosition) endPosition;
  }
  
  @Override
  public String getDescription()
  {
    return this.description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return this.tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return this.isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public Boolean getIsShould()
  {
    return this.isShould;
  }
  
  @Override
  public void setIsShould(Boolean isShould)
  {
    this.isShould = isShould;
  }
  
  @Override
  public Boolean getIsInherited()
  {
    return this.isInherited;
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    this.isInherited = isInherited;
  }
  
  @Override
  public Boolean getIsCutoff()
  {
    return this.isCutoff;
  }
  
  @Override
  public void setIsCutoff(Boolean isCutoff)
  {
    this.isCutoff = isCutoff;
  }
  
  @Override
  public String getPlaceholder()
  {
    return this.placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
  
  @Override
  public Boolean getIsVariantAllowed()
  {
    return isVariantAllowed;
  }
  
  @Override
  public void setIsVariantAllowed(Boolean isVariantAllowed)
  {
    this.isVariantAllowed = isVariantAllowed;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Integer getNumberOfVersionsAllowed()
  {
    return numberOfVersionsAllowed;
  }
  
  @Override
  public void setNumberOfVersionsAllowed(Integer numberOfVersionsAllowed)
  {
    this.numberOfVersionsAllowed = numberOfVersionsAllowed;
  }
  
  @Override
  public Boolean getIsDeleted()
  {
    return isDeleted;
  }
  
  @Override
  public void setIsDeleted(Boolean isDeleted)
  {
    this.isDeleted = isDeleted;
  }
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    return isSkipped;
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.isSkipped = isSkipped;
  }
  
  @Override
  public String getAttributeVariantContext()
  {
    return attributeVariantContext;
  }
  
  @Override
  public void setAttributeVariantContext(String attributeVariantContext)
  {
    this.attributeVariantContext = attributeVariantContext;
  }
  
  @Override
  public String getPropertyId()
  {
    return this.propertyId;
  }
  
  @Override
  public void setPropertyId(String propertyId)
  {
    this.propertyId = propertyId;
  }
}
