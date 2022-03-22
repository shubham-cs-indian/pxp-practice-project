package com.cs.core.runtime.interactor.model.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionGetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstanceRequestStrategyModelForCustomTab
    extends AbstractGetInstanceRequestStrategyModel
    implements IGetInstanceRequestStrategyModelForCustomTab {
  
  private static final long                                  serialVersionUID = 1L;
  protected Map<String, IReferencedSectionElementModel>      referencedElements;
  private Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships;
  private IReferencedContextModel                            referencedVariantContexts;
  private List<String>                                       contextTagsForTargetRelationship;
  private List<String>                                       referencedNatureRelationshipsIds;
  private List<String>                                       referencedRelationshipsIds;
  private List<String>                                       referencedLifeCycleStatusTags;
  private Map<String, IAttribute>                            referencedAttributes;
  private Map<String, ITag>                                  referencedTags;
  private List<String>                                       readOnlyAttributeIds;
  private List<String>                                       readOnlyTagIds;
  
  public GetInstanceRequestStrategyModelForCustomTab(IGetInstanceRequestModel model)
  {
    this.id = model.getId();
    this.moduleId = model.getId();
    this.templateId = model.getTemplateId();
    this.contextId = model.getContextId();
    this.sortField = model.getSortField();
    this.sortOrder = model.getSortOrder();
    this.from = model.getFrom();
    this.size = model.getSize();
  }
  
  public GetInstanceRequestStrategyModelForCustomTab(
      IKlassInstanceVersionGetModel klassInstanceVersionGetModel)
  {
    this.id = klassInstanceVersionGetModel.getId();
    this.templateId = klassInstanceVersionGetModel.getTemplateId();
    this.size = klassInstanceVersionGetModel.getSize();
    this.from = klassInstanceVersionGetModel.getFrom();
  }
  
  public GetInstanceRequestStrategyModelForCustomTab()
  {
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    if (referencedNatureRelationships == null) {
      referencedNatureRelationships = new HashMap<>();
    }
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
  }
  
  @Override
  public List<String> getContextTagsForTargetRelationship()
  {
    if (contextTagsForTargetRelationship == null) {
      contextTagsForTargetRelationship = new ArrayList<>();
    }
    return contextTagsForTargetRelationship;
  }
  
  @Override
  public void setContextTagsForTargetRelationship(List<String> contextTagsForTargetRelationship)
  {
    this.contextTagsForTargetRelationship = contextTagsForTargetRelationship;
  }
  
  @Override
  public List<String> getReferencedNatureRelationshipsIds()
  {
    if (referencedNatureRelationshipsIds == null) {
      referencedNatureRelationshipsIds = new ArrayList<>();
    }
    return referencedNatureRelationshipsIds;
  }
  
  @Override
  public void setReferencedNatureRelationshipsIds(List<String> referencedNatureRelationshipsIds)
  {
    this.referencedNatureRelationshipsIds = referencedNatureRelationshipsIds;
  }
  
  @Override
  public List<String> getReferencedRelationshipsIds()
  {
    if (referencedRelationshipsIds == null) {
      referencedRelationshipsIds = new ArrayList<>();
    }
    return referencedRelationshipsIds;
  }
  
  @Override
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds)
  {
    this.referencedRelationshipsIds = referencedRelationshipsIds;
  }
  
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    if (referencedLifeCycleStatusTags == null) {
      referencedLifeCycleStatusTags = new ArrayList<>();
    }
    return referencedLifeCycleStatusTags;
  }
  
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    this.referencedLifeCycleStatusTags = referencedLifeCycleStatusTags;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
    }
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
    }
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<String> getReadOnlyAttributeIds()
  {
    if (readOnlyAttributeIds == null) {
      readOnlyAttributeIds = new ArrayList<>();
    }
    return readOnlyAttributeIds;
  }
  
  @Override
  public void setReadOnlyAttributeIds(List<String> readOnlyAttributeIds)
  {
    this.readOnlyAttributeIds = readOnlyAttributeIds;
  }
  
  @Override
  public List<String> getReadOnlyTagIds()
  {
    if (readOnlyTagIds == null) {
      readOnlyTagIds = new ArrayList<>();
    }
    return readOnlyTagIds;
  }
  
  @Override
  public void setReadOnlyTagIds(List<String> readOnlyTagIds)
  {
    this.readOnlyTagIds = readOnlyTagIds;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
}
