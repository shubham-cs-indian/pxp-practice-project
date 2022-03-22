package com.cs.core.config.interactor.model.klass;


import java.util.ArrayList;
import java.util.List;

public class SaveRelationshipToExportModel implements ISaveRelationshipToExportModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String> addedRelationshipsToExport;
  protected List<String> deletedRelationshipsToExport;
  protected List<String> addedAttibutesToExport;
  protected List<String> deletdAttibutesToExport;
  protected List<String> addedTagsToExport;
  protected List<String> deletedTagsToExport;
  
  @Override
  public List<String> getAddedRelationshipsToExport()
  {
    if (addedRelationshipsToExport == null) {
      addedRelationshipsToExport = new ArrayList<String>();
    }
    return addedRelationshipsToExport;
  }
  
  @Override
  public void setAddedRelationshipsToExport(List<String> addedRelationshipsToExport)
  {
    this.addedRelationshipsToExport = addedRelationshipsToExport;
  }
  
  @Override
  public List<String> getDeletedRelationshipsToExport()
  {
    if (deletedRelationshipsToExport == null) {
      deletedRelationshipsToExport = new ArrayList<String>();
    }
    return deletedRelationshipsToExport;
  }
  
  @Override
  public void setDeletedRelationshipsToExport(List<String> deletedRelationshipsToExport)
  {
    this.deletedRelationshipsToExport = deletedRelationshipsToExport;
  }
  
  @Override
  public List<String> getAddedAttibutesToExport()
  {
    if (addedAttibutesToExport == null) {
      addedAttibutesToExport = new ArrayList<String>();
    }
    return addedAttibutesToExport;
  }
  
  @Override
  public void setAddedAttibutesToExport(List<String> addedAttibutesToExport)
  {
    this.addedAttibutesToExport = addedAttibutesToExport;
  }
  
  @Override
  public List<String> getDeletdAttibutesToExport()
  {
    if (deletdAttibutesToExport == null) {
      deletdAttibutesToExport = new ArrayList<String>();
    }
    return deletdAttibutesToExport;
  }
  
  @Override
  public void setDeletdAttibutesToExport(List<String> deletdAttibutesToExport)
  {
    this.deletdAttibutesToExport = deletdAttibutesToExport;
  }
  
  @Override
  public List<String> getAddedTagsToExport()
  {
    if (addedTagsToExport == null) {
      addedTagsToExport = new ArrayList<String>();
    }
    return addedTagsToExport;
  }
  
  @Override
  public void setAddedTagsToExport(List<String> addedTagsToExport)
  {
    this.addedTagsToExport = addedTagsToExport;
  }
  
  @Override
  public List<String> getDeletedTagsToExport()
  {
    if (deletedTagsToExport == null) {
      deletedTagsToExport = new ArrayList<String>();
    }
    return deletedTagsToExport;
  }
  
  @Override
  public void setDeletedTagsToExport(List<String> deletedTagsToExport)
  {
    this.deletedTagsToExport = deletedTagsToExport;
  }
  
}
