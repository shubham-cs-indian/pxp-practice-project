package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.structure.IStructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassStructureDiffModel implements IKlassStructureDiffModel {
  
  private static final long        serialVersionUID = 1L;
  
  Map<String, List<IStructure>>    added            = new HashMap<>();
  
  List<String>                     deleted          = new ArrayList<>();
  
  Map<String, Map<String, Object>> modified         = new HashMap<>();
  
  @Override
  public Map<String, List<IStructure>> getAdded()
  {
    return this.added;
  }
  
  @Override
  public void setAdded(Map<String, List<IStructure>> added)
  {
    this.added = added;
  }
  
  @Override
  public List<String> getDeleted()
  {
    return this.deleted;
  }
  
  @Override
  public void setDeleted(List<String> deleted)
  {
    this.deleted = deleted;
  }
  
  @Override
  public Map<String, Map<String, Object>> getModified()
  {
    return this.modified;
  }
  
  @Override
  public void setModified(Map<String, Map<String, Object>> modified)
  {
    this.modified = modified;
  }
}
