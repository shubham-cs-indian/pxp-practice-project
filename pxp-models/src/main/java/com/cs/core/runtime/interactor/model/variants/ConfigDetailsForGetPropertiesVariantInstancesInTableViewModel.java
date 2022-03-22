package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplatePermissionModel;
import com.cs.core.runtime.interactor.model.configdetails.TemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class ConfigDetailsForGetPropertiesVariantInstancesInTableViewModel
    implements IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel {
  
  private static final long                                 serialVersionUID   = 1L;
  protected Map<String, IAttribute>                         referencedAttributes;
  protected Map<String, ITag>                               referencedTags;
  protected IReferencedContextModel                         referencedVariantContexts;
  protected Map<String, IReferencedPropertyCollectionModel> referencedSections;
  protected Map<String, IReferencedSectionElementModel>     referencedElements;
  protected IBaseKlassTemplatePermissionModel               referencedPermissions;
  protected IGetFilterInfoModel                             filterInfo;
  protected Map<String, IRole>                              referencedRoles;
  protected Set<String>                                     visiblePropertyIds = new HashSet<>();
  protected Map<String, List<String>>                       typeIdIdentifierAttributeIds;
  protected List<String>                                    versionableAttributes;
  protected List<String>                                    versionableTags;
  protected List<String>                                    mandatoryAttributeIds;
  protected List<String>                                    shouldAttributeIds;
  protected List<String>                                    mandatoryTagIds;
  protected List<String>                                    shouldTagIds;
  
  @Override
  public Set<String> getVisiblePropertyIds()
  {
    return visiblePropertyIds;
  }
  
  @Override
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds)
  {
    this.visiblePropertyIds = visiblePropertyIds;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
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
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections()
  {
    return referencedSections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections)
  {
    this.referencedSections = referencedSections;
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
  
  @Override
  public IBaseKlassTemplatePermissionModel getReferencedPermissions()
  {
    return referencedPermissions;
  }
  
  @Override
  @JsonDeserialize(as = TemplatePermissionForGetVariantInstancesModel.class)
  public void setReferencedPermissions(IBaseKlassTemplatePermissionModel referencedPermissions)
  {
    this.referencedPermissions = referencedPermissions;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  @JsonDeserialize(as = GetFilterInfoModel.class)
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
  }
  
  @Override
  public List<String> getVersionableAttributes()
  {
    if (versionableAttributes == null) {
      versionableAttributes = new ArrayList<>();
    }
    return versionableAttributes;
  }
  
  @Override
  public void setVersionableAttributes(List<String> versionableAttributes)
  {
    this.versionableAttributes = versionableAttributes;
  }
  
  @Override
  public List<String> getVersionableTags()
  {
    if (versionableTags == null) {
      versionableTags = new ArrayList<>();
    }
    return versionableTags;
  }
  
  @Override
  public void setVersionableTags(List<String> versionableTags)
  {
    this.versionableTags = versionableTags;
  }
  
  @Override
  public List<String> getMandatoryAttributeIds()
  {
    if (mandatoryAttributeIds == null) {
      mandatoryAttributeIds = new ArrayList<>();
    }
    return mandatoryAttributeIds;
  }
  
  @Override
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds)
  {
    this.mandatoryAttributeIds = mandatoryAttributeIds;
  }
  
  @Override
  public List<String> getMandatoryTagIds()
  {
    if (mandatoryTagIds == null) {
      mandatoryTagIds = new ArrayList<>();
    }
    return mandatoryTagIds;
  }
  
  @Override
  public void setMandatoryTagIds(List<String> mandatoryTagIds)
  {
    this.mandatoryTagIds = mandatoryTagIds;
  }
  
  @Override
  public List<String> getShouldAttributeIds()
  {
    if (shouldAttributeIds == null) {
      shouldAttributeIds = new ArrayList<>();
    }
    return shouldAttributeIds;
  }
  
  @Override
  public void setShouldAttributeIds(List<String> shouldAttributeIds)
  {
    this.shouldAttributeIds = shouldAttributeIds;
  }
  
  @Override
  public List<String> getShouldTagIds()
  {
    if (shouldTagIds == null) {
      shouldTagIds = new ArrayList<>();
    }
    return shouldTagIds;
  }
  
  @Override
  public void setShouldTagIds(List<String> shouldTagIds)
  {
    this.shouldTagIds = shouldTagIds;
  }
}
