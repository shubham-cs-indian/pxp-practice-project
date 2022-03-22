package com.cs.core.config.interactor.model.propertycollection;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.PropertyCollectionElement;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.entity.role.AbstractRole;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.attributiontaxonomy.IMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.MasterTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPropertyCollectionModel extends ConfigResponseWithAuditLogModel
    implements IGetPropertyCollectionModel {
  
  private static final long            serialVersionUID = 1L;
  
  protected IPropertyCollection        entity;
  protected List<ITag>                 referencedTags;
  protected List<IAttribute>           referencedAttributes;
  protected List<IRole>                referencedRoles;
  protected List<String>               complexAttributeIds;
  protected List<IRelationship>        referencedRelationships;
  protected List<IMasterTaxonomyModel> referencedTaxonomies;
  protected IIdLabelCodeModel          tab;
  
  public GetPropertyCollectionModel()
  {
    this.entity = new PropertyCollection();
  }
  
  public GetPropertyCollectionModel(IPropertyCollection entity)
  {
    this.entity = entity;
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
  public List<IPropertyCollectionElement> getElements()
  {
    return entity.getElements();
  }
  
  @JsonDeserialize(contentAs = PropertyCollectionElement.class)
  @Override
  public void setElements(List<IPropertyCollectionElement> elements)
  {
    entity.setElements(elements);
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
  public List<ITag> getReferencedTags()
  {
    if (referencedTags == null) {
      referencedTags = new ArrayList<>();
    }
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(List<ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<IAttribute> getReferencedAttributes()
  {
    if (referencedAttributes == null) {
      referencedAttributes = new ArrayList<>();
    }
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(List<IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public List<IRole> getReferencedRoles()
  {
    if (referencedRoles == null) {
      referencedRoles = new ArrayList<>();
    }
    return referencedRoles;
  }
  
  @JsonDeserialize(contentAs = AbstractRole.class)
  @Override
  public void setReferencedRoles(List<IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public List<String> getComplexAttributeIds()
  {
    return complexAttributeIds;
  }
  
  @Override
  public void setComplexAttributeIds(List<String> complexAttributeIds)
  {
    this.complexAttributeIds = complexAttributeIds;
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
  public List<IRelationship> getReferencedRelationships()
  {
    
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = Relationship.class)
  @Override
  public void setReferencedRelationships(List<IRelationship> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Boolean getIsForXRay()
  {
    return entity.getIsForXRay();
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    entity.setIsForXRay(isForXRay);
  }
  
  @Override
  public Boolean getIsDefaultForXRay()
  {
    return entity.getIsDefaultForXRay();
  }
  
  @Override
  public void setIsDefaultForXRay(Boolean isDefaultForXRay)
  {
    entity.setIsDefaultForXRay(isDefaultForXRay);
  }
  
  @Override
  public List<IMasterTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = MasterTaxonomyModel.class)
  public void setReferencedTaxonomies(List<IMasterTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public IIdLabelCodeModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = IdLabelCodeModel.class)
  public void setTab(IIdLabelCodeModel tab)
  {
    this.tab = tab;
  }
  
  /**
   * *********************************************** Ignore properties
   * ****************************************************
   */
  @JsonIgnore
  @Override
  public List<String> getAttributeIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getTagIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTagIds(List<String> tagIds)
  {
  }
  
  @JsonIgnore
  @Override
  public List<String> getPropertySequenceIds()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPropertySequenceIds(List<String> propertySequenceIds)
  {
    
  }
}
