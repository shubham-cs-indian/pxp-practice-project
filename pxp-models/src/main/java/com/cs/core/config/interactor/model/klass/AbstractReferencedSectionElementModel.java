package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.model.duplicatecode.AbstractElementConflictingValuesModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "attribute",
        value = ReferencedSectionAttributeModel.class),
    @JsonSubTypes.Type(name = "relationship",
        value = ReferencedSectionRelationshipModel.class),
    @JsonSubTypes.Type(name = "tag",
        value = ReferencedSectionTagModel.class),
    @JsonSubTypes.Type(name = "role",
        value = ReferencedSectionRoleModel.class),
    @JsonSubTypes.Type(name = "taxonomy",
        value = ReferencedSectionTaxonomyModel.class)})
public abstract class AbstractReferencedSectionElementModel
    implements IReferencedSectionElementModel {
  
  private static final long                    serialVersionUID = 1L;
  
  private String                               id;
  private String                               tooltip;
  private String                               label;
  private String                               placeholder;
  private Boolean                              isDisabled       = true;
  private Boolean                              isVariantAlowed;
  private Integer                              numberOfVersionsAllowed;
  private String                               couplingType;
  private String                               type;
  private String                               description;
  private Boolean                              isMandatory      = false;
  private Boolean                              isShould         = false;
  private Boolean                              isSkipped        = false;
  private Boolean                              canRead          = false;
  private String                               attributeVariantContext;
  private String                               code;
  private String                               propertyId;
  private List<IElementConflictingValuesModel> conflictingSources;
  private Boolean                              isInherited;
  private Boolean                              isCutoff;
  private Boolean                              isDeleted;
  
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
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getTooltip()
  {
    return tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getPlaceholder()
  {
    return placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
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
    return isVariantAlowed;
  }
  
  @Override
  public void setIsVariantAllowed(Boolean isVariantAlowed)
  {
    this.isVariantAlowed = isVariantAlowed;
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  /**
   * ******************************** ignored properties
   * ********************************************
   */
  @Override
  @JsonIgnore
  public IPosition getStartPosition()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setStartPosition(IPosition iPosition)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public IPosition getEndPosition()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setEndPosition(IPosition iPosition)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsInherited()
  {
    return isInherited;
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    this.isInherited = isInherited;
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsCutoff()
  {
    return isCutoff;
  }
  
  @Override
  public void setIsCutoff(Boolean isCutoff)
  {
    this.isCutoff = isCutoff;
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsDeleted()
  {
    return isDeleted;
  }
  
  @Override
  @JsonIgnore
  public void setIsDeleted(Boolean type)
  {
    this.isDeleted = type;
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
  public Boolean getIsMandatory()
  {
    return isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public Boolean getIsShould()
  {
    return isShould;
  }
  
  @Override
  public void setIsShould(Boolean isShould)
  {
    this.isShould = isShould;
  }
  
  @Override
  public Boolean getCanRead()
  {
    return canRead;
  }
  
  @Override
  public void setCanRead(Boolean canRead)
  {
    this.canRead = canRead;
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
  
  @Override
  public List<IElementConflictingValuesModel> getConflictingSources()
  {
    return conflictingSources;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractElementConflictingValuesModel.class)
  public void setConflictingSources(List<IElementConflictingValuesModel> conflictingSources)
  {
    this.conflictingSources = conflictingSources;
  }
}
