package com.cs.core.config.interactor.entity.relationship;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class Relationship implements IRelationship {
  
  private static final long          serialVersionUID = 1L;
  protected String                   id;
  protected String                   description;
  protected String                   tooltip;
  protected boolean                  isMandatory;
  protected boolean                  isStandard;
  protected boolean                  autoUpdate;
  protected String                   placeholder;
  protected String                   label;
  protected String                   icon;
  protected String                   iconKey;
  protected String                   type             = this.getClass()
      .getName();
  protected Long                     versionId;
  protected Long                     versionTimestamp;
  protected String                   lastModifiedBy;
  protected IRelationshipSide        side1;
  protected IRelationshipSide        side2;
  protected List<? extends ISection> sections         = new ArrayList<>();
  protected String                   code;
  protected String                   rhythm;
  protected String                   tabId;
  protected Boolean                  isNature;
  protected long                     propertyIID;
  protected Boolean                  enableAfterSave  = false;
  protected Boolean                  isLite = false;

  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
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
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
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
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
  public Boolean getIsStandard()
  {
    return this.isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public IRelationshipSide getSide1()
  {
    return side1;
  }
  
  @JsonDeserialize(as = RelationshipSide.class)
  @Override
  public void setSide1(IRelationshipSide side1)
  {
    this.side1 = side1;
  }
  
  @Override
  public IRelationshipSide getSide2()
  {
    return side2;
  }
  
  @JsonDeserialize(as = RelationshipSide.class)
  @Override
  public void setSide2(IRelationshipSide side2)
  {
    this.side2 = side2;
  }
  
  @Override
  public Boolean getAutoUpdate()
  {
    
    return autoUpdate;
  }
  
  @Override
  public void setAutoUpdate(Boolean autoUpdate)
  {
    this.autoUpdate = autoUpdate;
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return sections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    this.sections = sections;
  }
  
  @Override
  public String getRhythm()
  {
    return rhythm;
  }
  
  @Override
  public void setRhythm(String rhythm)
  {
    this.rhythm = rhythm;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.propertyIID;
  }
  
  @Override
  public void setPropertyIID(long relationshipIID)
  {
    this.propertyIID = relationshipIID;
  }
  
  @Override
  public Boolean getEnableAfterSave()
  {
    return enableAfterSave;
  }

  @Override
  public void setEnableAfterSave(Boolean enableAfterSave)
  {
    this.enableAfterSave = enableAfterSave;
  }

  @Override
  public Boolean getIsLite()
  {
    return isLite;
  }

  @Override
  public void setIsLite(Boolean isLite)
  {
    this.isLite = isLite;
  }
}
