package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.SectionElementPosition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(name = "attribute",
        value = ModifiedSectionAttributeModel.class),
    @JsonSubTypes.Type(name = "relationship",
        value = ModifiedSectionRelationshipModel.class),
    @JsonSubTypes.Type(name = "tag", value = ModifiedSectionTagModel.class),
    @JsonSubTypes.Type(name = "role",
        value = ModifiedSectionRoleModel.class),
    @JsonSubTypes.Type(name = "taxonomy",
        value = ModifiedSectionTaxonomyModel.class) })
public class AbstractModifiedSectionElementModel implements IModifiedSectionElementModel {
  
  private static final long                               serialVersionUID        = 1L;
  protected String                                        id;
  protected SectionElementPosition                        startPosition;
  protected Boolean                                       isMandatory             = false;
  protected Boolean                                       isShould                = false;
  protected Boolean                                       isSkipped               = false;
  protected Boolean                                       isInherited             = false;
  protected Boolean                                       isCutoff                = false;
  protected Boolean                                       isVariantAllowed        = false;
  protected String                                        type;
  protected Boolean                                       isDisabled              = false;
  protected Integer                                       numberOfVersionsAllowed = 0;
  protected Long                                          versionId;
  protected List<String>                                  tagGroups               = new ArrayList<String>();
  protected Long                                          versionTimestamp;
  protected String                                        lastModifiedBy;
  protected String                                        couplingType            = "looselyCoupled";
  protected Boolean                                       isDeleted;
  protected Boolean                                       isModified              = false;
  protected List<IModifiedSectionElementPermissionModel>  modifiedPermission;
  protected List<IModifedSectionElementNotificationModel> modifiedNotification;
  protected String                                        attributeVariantContext;
  protected String                                        code;
  protected String                                        propertyId;
  
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
  @JsonDeserialize(contentAs = ModifiedSectionElementNotificationModel.class)
  public List<IModifedSectionElementNotificationModel> getModifiedNotification()
  {
    if (modifiedNotification == null) {
      return new ArrayList<>();
    }
    return modifiedNotification;
  }
  
  @Override
  public void setModifiedNotification(
      List<IModifedSectionElementNotificationModel> modifiedNotication)
  {
    this.modifiedNotification = modifiedNotication;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedSectionElementPermissionModel.class)
  public List<IModifiedSectionElementPermissionModel> getModifiedPermission()
  {
    if (modifiedPermission == null) {
      return new ArrayList<>();
    }
    return modifiedPermission;
  }
  
  @Override
  public void setModifiedPermission(List<IModifiedSectionElementPermissionModel> modifiedPermission)
  {
    this.modifiedPermission = modifiedPermission;
  }
  
  @Override
  public Boolean getIsModified()
  {
    return isModified;
  }
  
  @Override
  public void setIsModified(Boolean isModified)
  {
    this.isModified = isModified;
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
  
  @JsonIgnore
  @Override
  public IPosition getEndPosition()
  {
    return null;
  }
  
  @Override
  public void setEndPosition(IPosition endPosition)
  {
  }
  
  @Override
  public String getDescription()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setDescription(String description)
  {
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
  
  @JsonIgnore
  @Override
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLabel()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLabel(String label)
  {
  }
  
  @JsonIgnore
  @Override
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
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
