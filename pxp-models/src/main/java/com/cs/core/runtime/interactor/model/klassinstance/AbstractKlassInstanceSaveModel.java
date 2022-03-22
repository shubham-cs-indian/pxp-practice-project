package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.role.ModifiedRoleInstanceModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.contentidentifier.IContentIdentifier;
import com.cs.core.runtime.interactor.entity.datarule.ContentRelationshipInstance;
import com.cs.core.runtime.interactor.entity.datarule.IRuleViolation;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.language.ILanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.language.LanguageAndVersionId;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.summary.IKlassInstanceVersionSummary;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInstanceSaveModel;
import com.cs.core.runtime.interactor.model.attribute.AbstractModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.cs.core.runtime.interactor.model.supplierinstance.SupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.targetinstance.MarketInstanceSaveModel;
import com.cs.core.runtime.interactor.model.textassetinstance.TextAssetInstanceSaveModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "baseType",
    visible = true)
@JsonSubTypes({
    @Type(value = ArticleInstanceSaveModel.class, name = Constants.ARTICLE_INSTANCE_BASE_TYPE),
    @Type(value = AssetInstanceSaveModel.class, name = Constants.ASSET_INSTANCE_BASE_TYPE),
    @Type(value = MarketInstanceSaveModel.class, name = Constants.MARKET_INSTANCE_BASE_TYPE),
    @Type(value = SupplierInstanceSaveModel.class, name = Constants.SUPPLIER_INSTANCE_BASE_TYPE),
    @Type(value = TextAssetInstanceSaveModel.class, name = Constants.TEXTASSET_INSTANCE_BASE_TYPE) })
public abstract class AbstractKlassInstanceSaveModel implements IKlassInstanceSaveModel {
  
  private static final long                              serialVersionUID            = 1L;
  protected IKlassInstance                               entity;
  protected List<IContentAttributeInstance>              addedAttributes;
  protected List<String>                                 deletedAttributes;
  protected List<IModifiedContentAttributeInstanceModel> modifiedAttributes;
  protected List<ITagInstance>                           addedTags;
  protected List<String>                                 deletedTags;
  protected List<IModifiedContentTagInstanceModel>       modifiedTags;
  protected List<IRoleInstance>                          addedRoles;
  protected List<String>                                 deletedRoles;
  protected List<IModifiedRoleInstanceModel>             modifiedRoles;
  protected List<IContentRelationshipInstance>           addedRelationships          = new ArrayList<>();
  protected List<IContentRelationshipInstance>           deletedRelationships        = new ArrayList<>();
  protected List<IContentRelationshipInstance>           modifiedRelationships       = new ArrayList<>();
  protected IGetKlassInstanceTreeStrategyModel           getKlassInstanceTreeInfo    = null;
  protected String                                       saveComment;
  protected List<IContextInstance>                       contexts;
  protected String                                       variantInstanceId;
  protected String                                       contextId;
  protected List<String>                                 taxonomyIds                 = new ArrayList<>();
  protected List<IContentRelationshipInstance>           addedNatureRelationships    = new ArrayList<>();
  protected List<IContentRelationshipInstance>           deletedNatureRelationships  = new ArrayList<>();
  protected List<IContentRelationshipInstance>           modifiedNatureRelationships = new ArrayList<>();
  protected Boolean                                      isLinked                    = true;
  protected String                                       templateId;
  protected String                                       tabType;
  protected Boolean                                      isGetAll                    = false;
  protected IInstanceTimeRange                           timeRange;
  protected String                                       childContextId;
  protected List<IIdAndBaseType>                         addedLinkedInstances;
  protected List<String>                                 deletedLinkedInstances;
  protected String                                       componentId;
  protected IMessageInformation                          messages;
  protected String                                       tabId;
  protected String                                       typeId;
  /* protected List<String>                              addedTaxonomyIds              = new ArrayList<>();
  protected List<String>                                 addedKlassIds                 = new ArrayList<>();*/
  protected String                                       languageId;
  protected Boolean                                      isCreateTanslatableInstance;
  protected List<ILanguageAndVersionId>                  languageInstances;
  protected List<IContentAttributeInstance>              languageDependentAttributes;
  protected Boolean                                      isEmbedded;
  protected Boolean                                      isSaveAndPublish            = false;
  protected Boolean                                      isGridEdit                  = false;
  protected List<String>                                 addedTaxonomyIds = new ArrayList<>();
  protected List<String>                                 deletedTaxonomyIds = new ArrayList<>();
  
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
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
  public String getName()
  {
    return entity.getName();
  }
  
  @Override
  public void setName(String name)
  {
    entity.setName(name);
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    entity.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  /*@Override
  public String getOwner()
  {
    return entity.getOwner();
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    entity.setOwner(owner);
  }*/
  
  @Override
  public String getBaseType()
  {
    return entity.getBaseType();
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    entity.setBaseType(baseType);
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
  public Long getLastModified()
  {
    return entity.getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    entity.setLastModified(lastModified);
  }
  
  @Override
  public List<IContentAttributeInstance> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new ArrayList<IContentAttributeInstance>();
    }
    return addedAttributes;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @Override
  public void setAddedAttributes(List<IContentAttributeInstance> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributes()
  {
    if (deletedAttributes == null) {
      deletedAttributes = new ArrayList<String>();
    }
    return deletedAttributes;
  }
  
  @Override
  public void setDeletedAttributes(List<String> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @Override
  public List<IModifiedContentAttributeInstanceModel> getModifiedAttributes()
  {
    if (modifiedAttributes == null) {
      modifiedAttributes = new ArrayList<IModifiedContentAttributeInstanceModel>();
    }
    return modifiedAttributes;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentAttributeInstanceModel.class)
  @Override
  public void setModifiedAttributes(List<IModifiedContentAttributeInstanceModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public List<ITagInstance> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<ITagInstance>();
    }
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setAddedTags(List<ITagInstance> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<String>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<IModifiedContentTagInstanceModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<IModifiedContentTagInstanceModel>();
    }
    return modifiedTags;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  @Override
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<IRoleInstance> getAddedRoles()
  {
    if (addedRoles == null) {
      addedRoles = new ArrayList<IRoleInstance>();
    }
    return addedRoles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setAddedRoles(List<IRoleInstance> addedRoles)
  {
    this.addedRoles = addedRoles;
  }
  
  @Override
  public List<String> getDeletedRoles()
  {
    if (deletedRoles == null) {
      deletedRoles = new ArrayList<String>();
    }
    return deletedRoles;
  }
  
  @Override
  public void setDeletedRoles(List<String> deletedRoles)
  {
    this.deletedRoles = deletedRoles;
  }
  
  @Override
  public List<IModifiedRoleInstanceModel> getModifiedRoles()
  {
    if (modifiedRoles == null) {
      modifiedRoles = new ArrayList<IModifiedRoleInstanceModel>();
    }
    return modifiedRoles;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = ModifiedRoleInstanceModel.class)
  @Override
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles)
  {
    this.modifiedRoles = modifiedRoles;
  }
  
  @Override
  public List<ILanguageAndVersionId> getLanguageInstances()
  {
    if (languageInstances == null) {
      languageInstances = new ArrayList<>();
    }
    return languageInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = LanguageAndVersionId.class)
  public void setLanguageInstances(List<ILanguageAndVersionId> languageInstances)
  {
    this.languageInstances = languageInstances;
  }
  
  @JsonIgnore
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return (List<? extends ITagInstance>) this.entity.getTags();
    // throw new RuntimeException("Not to be used for save Model");
  }
  
  @JsonIgnore
  @Override
  public void setTags(List<? extends IContentTagInstance> tags)
  {
    // klassInstance.setTags(tags);
    throw new RuntimeException("Not to be used for save Model");
  }
  
  @JsonIgnore
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return this.entity.getAttributes();
    // throw new RuntimeException("Not to be used for save Model");
  }
  
  @JsonIgnore
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributeInstances)
  {
    // klassInstance.setAttributes(attributeInstances);
    throw new RuntimeException("Not to be used for save Model");
  }
  
  @JsonIgnore
  @Override
  public List<? extends IRoleInstance> getRoles()
  {
    return this.entity.getRoles();
    // throw new RuntimeException("Not to be used for save Model");
  }
  
  @JsonIgnore
  @Override
  public void setRoles(List<? extends IRoleInstance> roles)
  {
    // klassInstance.setRoles(roles);
    throw new RuntimeException("Not to be used for save Model");
  }
  
  @Override
  public List<IContentRelationshipInstance> getAddedRelationships()
  {
    if (addedRelationships == null) {
      addedRelationships = new ArrayList<>();
    }
    return addedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setAddedRelationships(List<IContentRelationshipInstance> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getDeletedRelationships()
  {
    if (deletedRelationships == null) {
      deletedRelationships = new ArrayList<>();
    }
    return deletedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setDeletedRelationships(List<IContentRelationshipInstance> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getModifiedRelationships()
  {
    return modifiedRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setModifiedRelationships(List<IContentRelationshipInstance> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public IGetKlassInstanceTreeStrategyModel getGetKlassInstanceTreeInfo()
  {
    return getKlassInstanceTreeInfo;
  }
  
  @JsonDeserialize(as = GetKlassInstanceTreeStrategyModel.class)
  @Override
  public void setGetKlassInstanceTreeInfo(
      IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeInfo)
  {
    this.getKlassInstanceTreeInfo = getKlassInstanceTreeInfo;
  }
  
  @Override
  public String getSaveComment()
  {
    return saveComment;
  }
  
  @Override
  public void setSaveComment(String saveComment)
  {
    this.saveComment = saveComment;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
  
  @Override
  public String getChildContextId()
  {
    
    return childContextId;
  }
  
  @Override
  public void setChildContextId(String childContextId)
  {
    this.childContextId = childContextId;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return entity.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyId)
  {
    this.entity.setTaxonomyIds(taxonomyId);
  }
  
  @Override
  public List<IContentRelationshipInstance> getAddedNatureRelationships()
  {
    if (addedNatureRelationships == null) {
      addedNatureRelationships = new ArrayList<>();
    }
    return addedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setAddedNatureRelationships(
      List<IContentRelationshipInstance> addedNatureRelationships)
  {
    this.addedNatureRelationships = addedNatureRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getDeletedNatureRelationships()
  {
    if (deletedNatureRelationships == null) {
      deletedNatureRelationships = new ArrayList<>();
    }
    return deletedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setDeletedNatureRelationships(
      List<IContentRelationshipInstance> deletedNatureRelationships)
  {
    this.deletedNatureRelationships = deletedNatureRelationships;
  }
  
  @Override
  public List<IContentRelationshipInstance> getModifiedNatureRelationships()
  {
    return modifiedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setModifiedNatureRelationships(
      List<IContentRelationshipInstance> modifiedNatureRelationships)
  {
    this.modifiedNatureRelationships = modifiedNatureRelationships;
  }
  
  @Override
  public String getJobId()
  {
    return this.entity.getJobId();
  }
  
  @Override
  public void setJobId(String jobId)
  {
    this.entity.setJobId(jobId);
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return this.isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return entity.getSelectedTaxonomyIds();
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    entity.setSelectedTaxonomyIds(selectedTaxonomyIds);
  }
  
  @Override
  public String getTabType()
  {
    return tabType;
  }
  
  @Override
  public void setTabType(String tabType)
  {
    this.tabType = tabType;
  }
  
  @Override
  public Boolean getIsGetAll()
  {
    return isGetAll;
  }
  
  @Override
  public void setIsGetAll(Boolean isGetAll)
  {
    this.isGetAll = isGetAll;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @JsonDeserialize(as = InstanceTimeRange.class)
  @Override
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public List<IIdAndBaseType> getAddedLinkedInstances()
  {
    if (addedLinkedInstances == null) {
      addedLinkedInstances = new ArrayList<>();
    }
    return addedLinkedInstances;
  }
  
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  @Override
  public void setAddedLinkedInstances(List<IIdAndBaseType> addedLinkedInstances)
  {
    this.addedLinkedInstances = addedLinkedInstances;
  }
  
  @Override
  public List<String> getDeletedLinkedInstances()
  {
    if (deletedLinkedInstances == null) {
      deletedLinkedInstances = new ArrayList<String>();
    }
    return deletedLinkedInstances;
  }
  
  @Override
  public void setDeletedLinkedInstances(List<String> deletedLinkedInstances)
  {
    this.deletedLinkedInstances = deletedLinkedInstances;
  }
  
  @Override
  public String getComponentId()
  {
    return entity.getComponentId();
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.entity.setComponentId(componentId);
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return ((IContentInstance) entity).getDefaultAssetInstanceId();
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    ((IContentInstance) entity).setDefaultAssetInstanceId(defaultAssetInstanceId);
  }
  
  @Override
  public List<String> getTypes()
  {
    return entity.getTypes();
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    entity.setTypes(types);
  }
  
  @Override
  public IMessageInformation getMessages()
  {
    if (messages == null) {
      messages = new MessageInformation();
    }
    return messages;
  }
  
  @JsonDeserialize(as = MessageInformation.class)
  @Override
  public void setMessages(IMessageInformation messages)
  {
    this.messages = messages;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return this.entity.getPhysicalCatalogId();
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatelogId)
  {
    this.entity.setPhysicalCatalogId(physicalCatelogId);
  }
  
  @Override
  public String getPortalId()
  {
    return this.entity.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.entity.setPortalId(portalId);
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return this.entity.getLogicalCatalogId();
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatelogId)
  {
    this.entity.setLogicalCatalogId(logicalCatelogId);
  }
  
  @Override
  public String getSystemId()
  {
    return this.entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.entity.setSystemId(systemId);
  }
  
  @Override
  public String getOrganizationId()
  {
    return this.entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getEndpointId()
  {
    return this.entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.entity.setEndpointId(endpointId);
  }
  
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
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
  }
  
  /**
   * ******************* Ignored Properties **********************
   */
  @Override
  @JsonIgnore
  public List<IRuleViolation> getRuleViolation()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setRuleViolation(List<IRuleViolation> ruleViolation)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getBranchOf()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setBranchOf(String branchOf)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getVersionOf()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionOf(String versionOf)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public IContextInstance getContext()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setContext(IContextInstance context)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public IKlassInstanceVersionSummary getSummary()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setSummary(IKlassInstanceVersionSummary summary)
  {
  }
  
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setKlassInstanceId(String masterContentId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getParentId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setParentId(String parentVariantId)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@Override
  public Boolean getIsFromExternalSource()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  public void setIsFromExternalSource(Boolean isFromExternalSource)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  /*@Override
    public Boolean getIsSkipped()
    {
      // TODO Auto-generated method stub
      return null;
    }
  */
  /*@Override
  public void setIsSkipped(Boolean isSkipped)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  @Override
  public List<String> getPath()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPath(List<String> path)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getOriginalInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  /*  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }*/
  
  @JsonIgnore
  @Override
  public List<IContentIdentifier> getPartnerSources()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPartnerSources(List<IContentIdentifier> partnerSources)
  {
  }
  
  @Override
  public String getLanguageId()
  {
    return languageId;
  }
  
  @Override
  public void setLanguageId(String languageId)
  {
    this.languageId = languageId;
  }
  
  @Override
  public Boolean getIsCreateTanslatableInstance()
  {
    if (isCreateTanslatableInstance == null) {
      isCreateTanslatableInstance = false;
    }
    return isCreateTanslatableInstance;
  }
  
  @Override
  public void setIsCreateTanslatableInstance(Boolean isCreateTanslatableInstance)
  {
    this.isCreateTanslatableInstance = isCreateTanslatableInstance;
  }
  
  @Override
  public List<IContentAttributeInstance> getLanguageDependentAttributes()
  {
    if (languageDependentAttributes == null) {
      languageDependentAttributes = new ArrayList<>();
    }
    return languageDependentAttributes;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @Override
  public void setLanguageDependentAttributes(
      List<IContentAttributeInstance> languageDependentAttributes)
  {
    this.languageDependentAttributes = languageDependentAttributes;
  }
  
  @Override
  public Boolean getIsEmbedded()
  {
    return isEmbedded;
  }
  
  @Override
  public void setIsEmbedded(Boolean isEmbedded)
  {
    this.isEmbedded = isEmbedded;
  }
  
  @Override
  public Boolean getIsSaveAndPublish()
  {
    return isSaveAndPublish;
  }

  @Override
  public void setIsSaveAndPublish(Boolean isSaveAndPublish)
  {
    this.isSaveAndPublish = isSaveAndPublish;
  }
  
  @Override
  public Boolean getIsGridEdit()
  {
    return isGridEdit;
  }

  @Override
  public void setIsGridEdit(Boolean isGridEdit)
  {
    this.isGridEdit = isGridEdit;
  }}
