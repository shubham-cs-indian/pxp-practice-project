package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagTaxonomy;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.template.IModifiedSequenceModel;

import java.util.List;

public interface ISaveTagTaxonomyModel extends ITagTaxonomy, IConfigModel {
  
  public static final String ADDED_APPLIED_KLASSES    = "addedAppliedKlasses";
  public static final String DELETED_APPLIED_KLASSES  = "deletedAppliedKlasses";
  
  public static final String ADDED_SECTIONS           = "addedSections";
  public static final String DELETED_SECTIONS         = "deletedSections";
  public static final String MODIFIED_SECTIONS        = "modifiedSections";
  public static final String MODIFIED_ELEMENTS        = "modifiedElements";
  public static final String DELETED_ELEMENTS         = "deletedElements";
  public static final String ADDED_ELEMENTS           = "addedElements";
  public static final String ADDED_DATA_RULES         = "addedDataRules";
  public static final String DELETED_DATA_RULES       = "deletedDataRules";
  public static final String ADDED_TASKS              = "addedTasks";
  public static final String DELETED_TASKS            = "deletedTasks";
  public static final String ADDED_CONTEXT_KLASSES    = "addedContextKlasses";
  public static final String DELETED_CONTEXT_KLASSES  = "deletedContextKlasses";
  public static final String MODIFIED_CONTEXT_KLASSES = "modifiedContextKlasses";
  public static final String MODIFIED_SEQUENCE        = "modifiedSequence";
  public static final String ADDED_LEVEL              = "addedLevel";
  public static final String DELETED_LEVEL            = "deletedLevel";
  
  public List<String> getAddedAppliedKlasses();
  
  public void setAddedAppliedKlasses(List<String> addedKlasses);
  
  public List<String> getDeletedAppliedKlasses();
  
  public void setDeletedAppliedKlasses(List<String> deletedKlasses);
  
  public List<? extends IModifiedSectionElementModel> getModifiedElements();
  
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> updatedElements);
  
  public List<? extends ISection> getAddedSections();
  
  public void setAddedSections(List<? extends ISection> addedSections);
  
  public List<String> getDeletedSections();
  
  public void setDeletedSections(List<String> deletedSectionIds);
  
  public List<IModifiedSectionModel> getModifiedSections();
  
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections);
  
  public List<IDeletedSectionElementModel> getDeletedElements();
  
  public void setDeletedElements(List<IDeletedSectionElementModel> deletedElements);
  
  public List<IAddedSectionElementModel> getAddedElements();
  
  public void setAddedElements(List<IAddedSectionElementModel> addedElements);
  
  public List<String> getAddedDataRules();
  
  public void setAddedDataRules(List<String> addedDataRules);
  
  public List<String> getDeletedDataRules();
  
  public void setDeletedDataRules(List<String> deletedDataRules);
  
  public List<String> getAddedTasks();
  
  public void setAddedTasks(List<String> addedTasks);
  
  public List<String> getDeletedTasks();
  
  public void setDeletedTasks(List<String> deletedTasks);
  
  public List<IContextKlassModel> getAddedContextKlasses();
  
  public void setAddedContextKlasses(List<IContextKlassModel> addedContextKlasses);
  
  public List<String> getDeletedContextKlasses();
  
  public void setDeletedContextKlasses(List<String> deletedContextKlasses);
  
  public IModifiedSequenceModel getModifiedSequence();
  
  public void setModifiedSequence(IModifiedSequenceModel modifiedSequence);
  
  public IAddedTaxonomyLevelModel getAddedLevel();
  
  public void setAddedLevel(IAddedTaxonomyLevelModel addedLevel);
  
  public String getDeletedLevel();
  
  public void setDeletedLevel(String deletedLevel);
  
  public List<IModifiedContextKlassModel> getModifiedContextKlasses();
  
  public void setModifiedContextKlasses(List<IModifiedContextKlassModel> modifiedContextKlasses);
}
