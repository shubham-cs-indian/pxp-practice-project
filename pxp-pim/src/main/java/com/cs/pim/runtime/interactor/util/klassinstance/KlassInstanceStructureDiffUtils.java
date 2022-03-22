package com.cs.pim.runtime.interactor.util.klassinstance;

import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.runtime.interactor.model.klassinstance.KlassStructureDiffModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.diff.changetype.ValueChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KlassInstanceStructureDiffUtils {
  
  public static KlassStructureDiffModel getStructureDiff(List<IStructure> workingStructures,
      List<IStructure> remoteStructures) throws Exception
  {
    List<IStructure> klassInstanceStructures = ObjectMapperUtil.readValue(
        ObjectMapperUtil.writeValueAsString(workingStructures),
        new TypeReference<List<AbstractStructure>>()
        {
          
        });
    
    List<IStructure> klassStructures = ObjectMapperUtil.readValue(
        ObjectMapperUtil.writeValueAsString(remoteStructures),
        new TypeReference<List<AbstractStructure>>()
        {
          
        });
    
    Javers javers = JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
        .build();
    
    KlassStructureDiffModel diff = new KlassStructureDiffModel();
    Map<String, List<IStructure>> addedStuff = new HashMap<>();
    List<String> removedStuff = new ArrayList<>();
    Map<String, Map<String, Object>> modifiedStuff = new HashMap<>();
    
    diff.setAdded(addedStuff);
    diff.setDeleted(removedStuff);
    diff.setModified(modifiedStuff);
    
    checkStructureForDiff("-1", klassInstanceStructures, klassStructures, diff, javers);
    
    return diff;
  }
  
  public static void checkStructureForDiff(String parentStructureId,
      List<IStructure> klassInstanceStructures, List<IStructure> klassStructures,
      KlassStructureDiffModel diff, Javers javers) throws Exception
  {
    Map<String, List<IStructure>> addedStuff = diff.getAdded();
    Map<String, Map<String, Object>> modifiedStuff = diff.getModified();
    List<String> removedStuff = (List<String>) diff.getDeleted();
    List<IStructure> parsedStructures = new ArrayList<>();
    
    for (IStructure klassInstanceStructure : klassInstanceStructures) {
      boolean isFound = false;
      String klassInstanceStructureId = klassInstanceStructure.getId();
      // TODO : Ignore : changing id property - hack for javers to make diff.
      klassInstanceStructure.setId(klassInstanceStructure.getStructureId());
      for (IStructure klassStructure : klassStructures) {
        if (klassInstanceStructure.getId()
            .equals(klassStructure.getId())) {
          isFound = true;
          
          if (!klassStructure.getIsGhost()) {
            checkStructureForDiff(klassInstanceStructure.getId(),
                klassInstanceStructure.getStructureChildren(),
                klassStructure.getStructureChildren(), diff, javers);
            
            Diff objDiff = javers.compare(klassInstanceStructure, klassStructure);
            for (ValueChange change : objDiff.getChangesByType(ValueChange.class)) {
              insertModifications(modifiedStuff, change, klassInstanceStructureId);
            }
          }
          
          parsedStructures.add(klassStructure);
          break;
          
        }
        else if (klassStructure.getIsGhost()) {
          parsedStructures.add(klassStructure);
        }
      }
      
      if (!isFound) {
        removedStuff.add(klassInstanceStructureId);
      }
      
      if (parsedStructures.size() > 0) {
        klassStructures.removeAll(parsedStructures);
      }
    }
    
    if (klassStructures.size() > 0) {
      int size = klassStructures.size();
      for (int i = size - 1; i >= 0; i--) {
        IStructure klassStructure = klassStructures.get(i);
        if (klassStructure.getIsGhost()) {
          klassStructures.remove(klassStructure);
        }
      }
      
      if (klassStructures.size() > 0) {
        addedStuff.put(parentStructureId, klassStructures);
      }
    }
  }
  
  public static void insertModifications(Map<String, Map<String, Object>> modifiedStuff,
      ValueChange change, String klassInstanceStructureId)
  {
    Map<String, Object> remoteChangesMap = modifiedStuff.get(klassInstanceStructureId);
    if (remoteChangesMap == null) {
      remoteChangesMap = new HashMap<>();
      modifiedStuff.put(klassInstanceStructureId, remoteChangesMap);
    }
    remoteChangesMap.put(change.getPropertyName(), change.getRight());
  }
}
