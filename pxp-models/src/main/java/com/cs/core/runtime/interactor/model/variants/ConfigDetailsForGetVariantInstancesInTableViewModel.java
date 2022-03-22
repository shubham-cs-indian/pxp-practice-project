package com.cs.core.runtime.interactor.model.variants;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplatePermissionModel;
import com.cs.core.runtime.interactor.model.configdetails.TemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForGetVariantInstancesInTableViewModel
    implements IConfigDetailsForGetVariantInstancesInTableViewModel {
  
  private static final long                                 serialVersionUID   = 1L;
  protected Map<String, IAttribute>                         referencedAttributes;
  protected Map<String, ITag>                               referencedTags;
  protected Map<String, IRole>                              referencedRoles;
  protected IReferencedContextModel                         referencedVariantContexts;
  protected Map<String, IReferencedPropertyCollectionModel> referencedSections;
  protected Map<String, IReferencedSectionElementModel>     referencedElements;
  protected IBaseKlassTemplatePermissionModel               referencedPermissions;
  protected IGetFilterInfoModel                             filterInfo;
  protected Set<String>                                     visiblePropertyIds = new HashSet<>();
  protected Map<String, List<String>>                       typeIdIdentifierAttributeIds;
  protected Map<String, IReferencedArticleTaxonomyModel>    referencedTaxonomies;
  protected Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements;
  
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
  public Map<String, Map<String, IReferencedSectionElementModel>> getInstanceIdVsReferencedElements()
  {
    return this.instanceIdVsReferencedElements;
  }
  
  @Override
  @JsonDeserialize(contentUsing = InstanceIdVsReferencedElementsCustomDeserializer.class)
  public void setInstanceIdVsReferencedElements(
      Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements)
  {
    this.instanceIdVsReferencedElements = instanceIdVsReferencedElements;
  }

  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
}
