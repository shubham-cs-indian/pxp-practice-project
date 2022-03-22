package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public interface IKlassInstanceSaveModel extends IContentInstance, IRuntimeModel {
  
  public static final String ADDED_RELATIONSHIPS            = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS          = "deletedRelationships";
  public static final String MODIFIED_RELATIONSHIPS         = "modifiedRelationships";
  public static final String ADDED_ATTRIBUTES               = "addedAttributes";
  public static final String DELETED_ATTRIBUTES             = "deletedAttributes";
  public static final String MODIFIED_ATTRIBUTES            = "modifiedAttributes";
  public static final String ADDED_TAGS                     = "addedTags";
  public static final String DELETED_TAGS                   = "deletedTags";
  public static final String MODIFIED_TAGS                  = "modifiedTags";
  public static final String ADDED_ROLES                    = "addedRoles";
  public static final String DELETED_ROLES                  = "deletedRoles";
  public static final String MODIFIED_ROLES                 = "modifiedRoles";
  public static final String ADDED_ASSETS                   = "addedAssets";
  public static final String DELETED_ASSETS                 = "deletedAssets";
  public static final String GET_KLASS_INSTANCE_TREE_INFO   = "getKlassInstanceTreeInfo";
  public static final String SAVE_COMMENT                   = "saveComment";
  public static final String VARIANT_INSTANCE_ID            = "variantInstanceId";
  public static final String CHILD_CONTEXT_ID               = "childContextId";
  public static final String TAXONOMY_IDS                   = "taxonomyIds";
  public static final String ADDED_NATURE_RELATIONSHIPS     = "addedNatureRelationships";
  public static final String DELETED_NATURE_RELATIONSHIPS   = "deletedNatureRelationships";
  public static final String MODIFIED_NATURE_RELATIONSHIPS  = "modifiedNatureRelationships";
  public static final String IS_LINKED                      = "isLinked";
  public static final String TEMPLATE_ID                    = "templateId";
  public static final String TAB_TYPE                       = "tabType";
  public static final String IS_GET_ALL                     = "isGetAll";
  public static final String CONTEXT_ID                     = "contextId";
  public static final String TIME_RANGE                     = "timeRange";
  public static final String ADDED_LINKED_INSTANCES         = "addedLinkedInstances";
  public static final String DELETED_LINKED_INSTANCES       = "deletedLinkedInstances";
  public static final String TAB_ID                         = "tabId";
  public static final String TYPE_ID                        = "typeId";
  /* public static final String ADDED_KLASSIDS             = "addedKlassIds";
  public static final String ADDED_TAXONOMYIDS             = "addedTaxonomyIds";*/
  public static final String LANGUAGE_ID                    = "languageId";
  public static final String IS_CREATE_TANSLATABLE_INSTANCE = "isCreateTanslatableInstance";
  public static final String LANGUAGE_DEPENDENT_ATTRIBUTES  = "languageDependentAttributes";
  public static final String LANGUAGES_TO_COMPARE           = "languagesToCompare";
  public static final String IS_SAVE_AND_PUBLISH            = "isSaveAndPublish";
  public static final String IS_GRID_EDIT                   = "isGridEdit";
  public static final String ADDED_TAXONOMYIDS             = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMYIDS             = "deletedTaxonomyIds";
  
  
  public List<IContentRelationshipInstance> getAddedRelationships();
  
  public void setAddedRelationships(List<IContentRelationshipInstance> addedRelationships);
  
  public List<IContentRelationshipInstance> getDeletedRelationships();
  
  public void setDeletedRelationships(List<IContentRelationshipInstance> deletedRelationships);
  
  public List<IContentRelationshipInstance> getModifiedRelationships();
  
  public void setModifiedRelationships(List<IContentRelationshipInstance> modifiedRelationships);
  
  public List<IContentAttributeInstance> getAddedAttributes();
  
  public void setAddedAttributes(List<IContentAttributeInstance> addedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
  
  public List<IModifiedContentAttributeInstanceModel> getModifiedAttributes();
  
  public void setModifiedAttributes(
      List<IModifiedContentAttributeInstanceModel> modifiedAttributes);
  
  public List<ITagInstance> getAddedTags();
  
  public void setAddedTags(List<ITagInstance> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags);
  
  public List<IRoleInstance> getAddedRoles();
  
  public void setAddedRoles(List<IRoleInstance> addedRoles);
  
  public List<String> getDeletedRoles();
  
  public void setDeletedRoles(List<String> deletedRoles);
  
  public List<IModifiedRoleInstanceModel> getModifiedRoles();
  
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles);
  
  public IGetKlassInstanceTreeStrategyModel getGetKlassInstanceTreeInfo();
  
  public void setGetKlassInstanceTreeInfo(
      IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeInfo);
  
  public String getSaveComment();
  
  public void setSaveComment(String saveComment);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public String getChildContextId();
  
  public void setChildContextId(String childContextId);
  
  public List<IContentRelationshipInstance> getAddedNatureRelationships();
  
  public void setAddedNatureRelationships(
      List<IContentRelationshipInstance> addedNatureRelationships);
  
  public List<IContentRelationshipInstance> getDeletedNatureRelationships();
  
  public void setDeletedNatureRelationships(
      List<IContentRelationshipInstance> deletedNatureRelationships);
  
  public List<IContentRelationshipInstance> getModifiedNatureRelationships();
  
  public void setModifiedNatureRelationships(
      List<IContentRelationshipInstance> modifiedRelationships);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getTabType();
  
  public void setTabType(String tabType);
  
  public Boolean getIsGetAll();
  
  public void setIsGetAll(Boolean isGetAll);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public List<IIdAndBaseType> getAddedLinkedInstances();
  
  public void setAddedLinkedInstances(List<IIdAndBaseType> addedLinkedInstances);
  
  public List<String> getDeletedLinkedInstances();
  
  public void setDeletedLinkedInstances(List<String> deletedLinkedInstances);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  /*public List<String> getAddedKlassIds();
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);*/
  
  public String getLanguageId();
  
  public void setLanguageId(String languageId);
  
  public Boolean getIsCreateTanslatableInstance();
  
  public void setIsCreateTanslatableInstance(Boolean isCreateTanslatableInstance);
  
  public List<IContentAttributeInstance> getLanguageDependentAttributes();
  
  public void setLanguageDependentAttributes(
      List<IContentAttributeInstance> languageDependentAttributes);
  
  public List<String> getLanguagesToCompare();
  
  public void setLanguagesToCompare(List<String> languagesToCompare);
  
  public Boolean getIsSaveAndPublish();  
  public void setIsSaveAndPublish(Boolean isSaveAndPublish);
  
  public Boolean getIsGridEdit();
  
  public void setIsGridEdit(Boolean isGridEdit);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
}
