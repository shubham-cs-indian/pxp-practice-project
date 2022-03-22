package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.configdetails.IAddedStructureModel;

import java.util.List;

public interface IProjectKlassSaveModel extends IProjectKlassModel, IKlassSaveModel {
  
  public static final String ADDED_GTIN_KLASS    = "addedGtinKlass";
  public static final String DELETED_GTIN_KLASS  = "deletedGtinKlass";
  public static final String MODIFIED_GTIN_KLASS = "modifiedGtinKlass";
  
  public List<? extends IAddedStructureModel> getAddedStructures();
  
  public void setAddedStructures(List<? extends IAddedStructureModel> addedStructures);
  
  public List<? extends IStructure> getModifiedStructures();
  
  public void setModifiedStructures(List<? extends IStructure> modifiedStructures);
  
  public List<String> getDeletedStructures();
  
  public void setDeletedStructures(List<String> deletedStructureIds);
  
  public IContextKlassModel getAddedGtinKlass();
  
  public void setAddedGtinKlass(IContextKlassModel addedGtinKlass);
  
  public String getDeletedGtinKlass();
  
  public void setDeletedGtinKlass(String deletedGtinKlass);
  
  public IModifiedContextKlassModel getModifiedGtinKlass();
  
  public void setModifiedGtinKlass(IModifiedContextKlassModel modifiedGtinKlass);

}
