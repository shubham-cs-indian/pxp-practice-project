package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.config.interactor.entity.structure.IStructure;
import java.util.List;
import java.util.Map;

public interface IKlassStructureInstance {
  
  public static final String STRUCTURE_CHILDREN          = "structureChildren";
  public static final String STRUCTURE_ATTRIBUTE_MAPPING = "structureAttributeMapping";
  
  public List<? extends IStructure> getStructureChildren();
  
  public void setStructureChildren(List<? extends IStructure> structures);
  
  public Map<String, IKlassInstanceStructure> getStructureAttributeMapping();
  
  public void setStructureAttributeMapping(
      Map<String, IKlassInstanceStructure> structureAttributeMap);
}
