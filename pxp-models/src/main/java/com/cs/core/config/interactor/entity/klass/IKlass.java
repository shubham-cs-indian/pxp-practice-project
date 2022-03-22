package com.cs.core.config.interactor.entity.klass;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public interface IKlass extends IKlassBasic {
  
  public static final String SECTIONS                       = "sections";
  public static final String PERMISSIONS                    = "permission";
  public static final String IS_STANDARD                    = "isStandard";
  public static final String IS_ENFORCED_TAXONOMY           = "isEnforcedTaxonomy";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN = "numberOfVersionsToMaintain";
  public static final String NOTIFICATION_SETTINGS          = "notificationSettings";
  public static final String TREE_TYPE_OPTION               = "treeTypeOption";
  public static final String IS_DEFAULT_CHILD               = "isDefaultChild";
  public static final String IS_DEFAULT_FOLDER              = "isDefaultFolder";
  public static final String IS_ALLOWED_AT_TOP_LEVEL        = "isAllowedAtTopLevel";
  public static final String IS_ABSTRACT                    = "isAbstract";
  public static final String ALLOWED_TYPES                  = "allowedTypes";
  public static final String DATA_RULES                     = "dataRules";
  public static final String CONTEXTS                       = "contexts";
  public static final String NATURE_TYPE                    = "natureType";
  public static final String RELATIONSHIPS                  = "relationships";
  public static final String PREVIEW_IMAGE                  = "previewImage";
  public static final String IS_NATURE                      = "isNature";
  public static final String LIFE_CYCLE_STATUS_TAGS         = "lifeCycleStatusTags";
  public static final String TASKS                          = "tasks";
  public static final String EMBEDDED_KLASS_IDS             = "embeddedKlassIds";
  public static final String LANGUAGE_KLASS_ID              = "languageKlassId";
  public static final String CLASSIFIER_IID                 = "classifierIID";
  public static final String CONTEXT_ID                     = "contextID";
  public static final String CONTEXT_INTERNAL_ID            = "contextIID";
  
  public List<? extends ISection> getSections();
  
  public void setSections(List<? extends ISection> sections);
  
  public IKlassPermission getPermission();
  
  public void setPermission(IKlassPermission permission);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public Boolean getIsEnforcedTaxonomy();
  
  public void setIsEnforcedTaxonomy(Boolean isEnforcedTaxanomy);
  
  public Long getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Long numberOfVersions);
  
  public Map<String, Map<String, Boolean>> getNotificationSettings();
  
  public void setNotificationSettings(Map<String, Map<String, Boolean>> notificationSettings);
  
  public String getTreeTypeOption();
  
  public void setTreeTypeOption(String treeTypeOption);
  
  public Boolean getIsDefaultChild();
  
  public void setIsDefaultChild(Boolean isDefaultChild);
  
  public Boolean getIsDefaultFolder();
  
  public void setIsDefaultFolder(Boolean isDefaultFolder);
  
  public Boolean getIsAllowedAtTopLevel();
  
  public void setIsAllowedAtTopLevel(Boolean isAllowedAtTopLevel);
  
  public Boolean getIsAbstract();
  
  public void setIsAbstract(Boolean issAbstract);
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> allowedTypes);
  
  public List<String> getDataRules();
  
  public void setDataRules(List<String> dataRules);
  
  public IKlassContext getContexts();
  
  public void setContexts(IKlassContext variantContexts);
  
  public String getNatureType();
  
  public void setNatureType(String natureType);
  
  public List<IKlassNatureRelationship> getRelationships();
  
  public void setRelationships(List<IKlassNatureRelationship> relationships);
  
  // public void setSectionRelationships(List<? extends ISectionRelationship>
  // sectionRelationships);
  
  // public List<? extends ISectionRelationship> getSectionRelationships();
  
  /*
   * public List<String> getImages();
   *
   * public void setImages(List<String> images);
   */
  
  public String getPreviewImage();
  
  public void setPreviewImage(String previewImage);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public List<String> getLifeCycleStatusTags();
  
  public void setLifeCycleStatusTags(List<String> lifeCycleStatusTags);
  
  public List<String> getTasks();
  
  public void setTasks(List<String> tasks);
  
  public List<String> getEmbeddedKlassIds();
  
  public void setEmbeddedKlassIds(List<String> embeddedKlassIds);
  
  public String getLanguageKlassId();
  
  public void setLanguageKlassId(String languageKlassId);
  
  public Long getClassifierIID();
  
  public void setClassifierIID(Long classifierIID);
  
  public String getContextID();
  
  public void setContextID(String contextID);
  
  public Long getContextIID();
  
  public void setContextIID(Long contextIID);
}
