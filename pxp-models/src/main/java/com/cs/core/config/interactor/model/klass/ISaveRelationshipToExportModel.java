package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveRelationshipToExportModel extends IModel {
  
  public static final String ADDED_RELATIONSHIP_TO_EXPORT   = "addedRelationshipsToExport";
  public static final String DELETED_RELATIONSHIP_TO_EXPORT = "deletedRelationshipsToExport";
  public static final String ADDED_ATTRIBUTES_TO_EXPORT     = "addedAttibutesToExport";
  public static final String DELETED_ATTRIBUTES_TO_EXPORT   = "deletdAttibutesToExport";
  public static final String ADDED_TAGS_TO_EXPORT           = "addedTagsToExport";
  public static final String DELETED_TAGS_TO_EXPORT         = "deletedTagsToExport";
  
  public List<String> getAddedRelationshipsToExport();
  public void setAddedRelationshipsToExport(List<String> addedRelationshipsToExport);
  
  public List<String> getDeletedRelationshipsToExport();
  public void setDeletedRelationshipsToExport(List<String> deletedRelationshipsToExport);
  
  public List<String> getAddedAttibutesToExport();
  public void setAddedAttibutesToExport(List<String> addedAttibutesToExport);
  
  public List<String> getDeletdAttibutesToExport();
  public void setDeletdAttibutesToExport(List<String> deletdAttibutesToExport);
  
  public List<String> getAddedTagsToExport();
  public void setAddedTagsToExport(List<String> addedTagsToExport);
  
  public List<String> getDeletedTagsToExport();
  public void setDeletedTagsToExport(List<String> deletedTagsToExport);
}
