package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;

public interface IKlassSaveModel extends IKlassModel {
  
  public static final String MODIFIED_ELEMENTS             = "modifiedElements";
  public static final String ADDED_SECTIONS                = "addedSections";
  public static final String DELETED_SECTIONS              = "deletedSections";
  public static final String MODIFIED_SECTIONS             = "modifiedSections";
  public static final String ADDED_DATA_RULES              = "addedDataRules";
  public static final String DELETED_DATA_RULES            = "deletedDataRules";
  public static final String ADDED_CONTEXTS                = "addedContexts";
  public static final String DELETED_CONTEXTS              = "deletedContexts";
  public static final String ADDED_RELATIONSHIPS           = "addedRelationships";
  public static final String MODIFIED_RELATIONSHIPS        = "modifiedRelationships";
  public static final String DELETED_RELATIONSHIPS         = "deletedRelationships";
  public static final String ADDED_LIFECYCLE_STATUS_TAGS   = "addedLifecycleStatusTags";
  public static final String DELETED_LIFECYCLE_STATUS_TAGS = "deletedLifecycleStatusTags";
  public static final String REFERENCED_TAGS               = "referencedTags";
  public static final String ADDED_TASKS                   = "addedTasks";
  public static final String DELETED_TASKS                 = "deletedTasks";
  public static final String ADDED_CONTEXT_KLASSES         = "addedContextKlasses";
  public static final String DELETED_CONTEXT_KLASSES       = "deletedContextKlasses";
  public static final String MODIFIED_CONTEXT_KLASSES      = "modifiedContextKlasses";
  public static final String ADDED_LANGUAGE_KLASS          = "addedLanguageKlass";
  public static final String DELETED_LANGUAGE_KLASS        = "deletedLanguageKlass";
  public static final String MODIFIED_LANGUAGE_KLASS       = "modifiedLanguageKlass";
  public static final String RELATIONSHIP_EXPORT           = "relationshipExport";

  
  public List<? extends ISection> getAddedSections();
  
  public void setAddedSections(List<? extends ISection> addedSections);
  
  public List<String> getDeletedSections();
  
  public void setDeletedSections(List<String> deletedSectionIds);
  
  public List<String> getAddedDataRules();
  
  public void setAddedDataRules(List<String> addedDataRules);
  
  public List<String> getDeletedDataRules();
  
  public void setDeletedDataRules(List<String> deletedDataRules);
  
  public List<? extends IModifiedSectionElementModel> getModifiedElements();
  
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> updatedElements);
  
  public List<IModifiedSectionModel> getModifiedSections();
  
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections);
  
  public IKlassContextModel getAddedContexts();
  
  public void setAddedContexts(IKlassContextModel addedContexts);
  
  public IKlassContextModel getDeletedContexts();
  
  public void setDeletedContexts(IKlassContextModel deletedContexts);
  
  public List<IAddedNatureRelationshipModel> getAddedRelationships();
  
  public void setAddedRelationships(List<IAddedNatureRelationshipModel> addedRelationships);
  
  public List<IModifiedNatureRelationshipModel> getModifiedRelationships();
  
  public void setModifiedRelationships(
      List<IModifiedNatureRelationshipModel> modifiedRelationships);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
  
  public List<String> getAddedLifecycleStatusTags();
  
  public void setAddedLifecycleStatusTags(List<String> addedLifecycleStatusTags);
  
  public List<String> getDeletedLifecycleStatusTags();
  
  public void setDeletedLifecycleStatusTags(List<String> deletedLifecycleStatusTags);
  
  public List<? extends ITag> getReferencedTags();
  
  public void setReferencedTags(List<? extends ITag> referencedTags);
  
  public List<String> getAddedTasks();
  
  public void setAddedTasks(List<String> addedTasks);
  
  public List<String> getDeletedTasks();
  
  public void setDeletedTasks(List<String> deletedTasks);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public List<IContextKlassModel> getAddedContextKlasses();
  
  public void setAddedContextKlasses(List<IContextKlassModel> addedContextKlasses);
  
  public List<String> getDeletedContextKlasses();
  
  public void setDeletedContextKlasses(List<String> deletedContextKlasses);
  
  public List<IModifiedContextKlassModel> getModifiedContextKlasses();
  
  public void setModifiedContextKlasses(List<IModifiedContextKlassModel> modifiedContextKlasses);
  
  public IContextKlassModel getAddedLanguageKlass();
  
  public void setAddedLanguageKlass(IContextKlassModel addedLanguageKlass);
  
  public String getDeletedLanguageKlass();
  
  public void setDeletedLanguageKlass(String deletedLanguageKlass);
  
  public IModifiedContextKlassModel getModifiedLanguageKlass();
  
  public void setModifiedLanguageKlass(IModifiedContextKlassModel modifiedLanguageKlass);
  
  public ISaveRelationshipToExportModel getRelationshipExport();
  
  public void setRelationshipExport(ISaveRelationshipToExportModel relationshipExport);
  
}
