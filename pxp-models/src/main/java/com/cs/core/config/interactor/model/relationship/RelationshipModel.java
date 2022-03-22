package com.cs.core.config.interactor.model.relationship;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.entity.relationship.RelationshipSide;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, defaultImpl = RelationshipModel.class,
    include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
public class RelationshipModel implements IRelationshipModel {
  
  private static final long                          serialVersionUID         = 1L;
  
  protected IRelationship                            entity;
  protected List<IModifiedRelationshipPropertyModel> addedAttributes          = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> addedTags                = new ArrayList<>();

  protected Boolean                                  isLite                   = false;

  public RelationshipModel()
  {
    entity = new Relationship();
  }
  
  public RelationshipModel(IRelationship relationship)
  {
    entity = relationship;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public String getTooltip()
  {
    return entity.getTooltip();
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    entity.setTooltip(tooltip);
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return entity.getIsMandatory();
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    entity.setIsMandatory(isMandatory);
  }
  
  @Override
  public String getPlaceholder()
  {
    return entity.getPlaceholder();
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    entity.setPlaceholder(placeholder);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }

  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }

  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public IRelationshipSide getSide1()
  {
    return entity.getSide1();
  }
  
  @JsonDeserialize(as = RelationshipSide.class)
  @Override
  public void setSide1(IRelationshipSide side1)
  {
    entity.setSide1(side1);
  }
  
  @Override
  public IRelationshipSide getSide2()
  {
    return entity.getSide2();
  }
  
  @JsonDeserialize(as = RelationshipSide.class)
  @Override
  public void setSide2(IRelationshipSide side2)
  {
    entity.setSide2(side2);
  }
  
  @Override
  public Boolean getAutoUpdate()
  {
    
    return entity.getAutoUpdate();
  }
  
  @Override
  public void setAutoUpdate(Boolean autoUpdate)
  {
    entity.setAutoUpdate(autoUpdate);
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return entity.getSections();
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    entity.setSections(sections);
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getAddedAttributes()
  {
    return addedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setAddedAttributes(List<IModifiedRelationshipPropertyModel> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getAddedTags()
  {
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setAddedTags(List<IModifiedRelationshipPropertyModel> addedTags)
  {
    this.addedTags = addedTags;
  }

  @Override
  public String getRhythm()
  {
    return entity.getRhythm();
  }
  
  @Override
  public void setRhythm(String rhythm)
  {
    entity.setRhythm(rhythm);
  }
  
  @Override
  public String getTabId()
  {
    return entity.getTabId();
  }
  
  @Override
  public void setTabId(String tabId)
  {
    entity.setTabId(tabId);
  }
  
  @Override
  public Boolean getIsNature()
  {
    return entity.getIsNature();
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.entity.setIsNature(isNature);
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.entity.getPropertyIID();
  }
  
  @Override
  public void setPropertyIID(long relationshipIID)
  {
    this.entity.setPropertyIID(relationshipIID);
  }
  
  @Override
  public Boolean getEnableAfterSave()
  {
    return this.entity.getEnableAfterSave();
  }

  @Override
  public void setEnableAfterSave(Boolean enableAfterSave)
  {
    this.entity.setEnableAfterSave(enableAfterSave);
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
