package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

import com.cs.core.config.interactor.model.klass.IAttributeIdValueModel;
import com.cs.core.config.interactor.model.klass.ITagIdValueModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;

public interface IBulkApplyValueRequestModel extends IModel {
  
  public static String ATTRIBUTES           = "attributes";
  public static String TAGS                 = "tags";
  public static String KLASSINSTANCES       = "klassInstances";
  public static String ADDED_TAXONOMY_IDS   = "addedTaxonomyIds";
  public static String DELETED_TAXONOMY_IDS = "deletedTaxonomyIds";
  public static String ADDED_KLASS_IDS      = "addedKlassIds";
  public static String DELETED_KLASS_IDS    = "deletedKlassIds";
  
  public List<IAttributeIdValueModel> getAttributes();
  public void setAttributes(List<IAttributeIdValueModel> attributes);
  
  public List<ITagIdValueModel> getTags();
  public void setTags(List<ITagIdValueModel> tags);
  
  public List<IIdAndBaseType> getKlassInstances();
  public void setKlassInstances(List<IIdAndBaseType> klassInstances);
  
  public List<String> getAddedTaxonomyIds();
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);

  public List<String> getAddedKlassIds();
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedKlassIds();
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
}
