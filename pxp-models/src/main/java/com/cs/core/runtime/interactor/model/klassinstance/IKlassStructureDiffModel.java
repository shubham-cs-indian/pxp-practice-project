package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IKlassStructureDiffModel extends IModel {
  
  public static final String ADDED    = "added";
  public static final String DELETED  = "deleted";
  public static final String MODIFIED = "modified";
  
  public Map<String, List<IStructure>> getAdded();
  
  public void setAdded(Map<String, List<IStructure>> added);
  
  public List<String> getDeleted();
  
  public void setDeleted(List<String> deleted);
  
  public Map<String, Map<String, Object>> getModified();
  
  public void setModified(Map<String, Map<String, Object>> modified);
}
