package com.cs.plugin.utility;

import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import java.util.Comparator;
import java.util.Map;

public class PropertyCollectionPropertyComparator implements Comparator<Map<String, Object>> {
  
  @Override
  public int compare(Map<String, Object> property1, Map<String, Object> property2)
  {
    /*Map<String, Object> position1 = (Map<String, Object>) property1
        .get(IPropertyCollectionElement.POSITION);
    Map<String, Object> position2 = (Map<String, Object>) property2
        .get(IPropertyCollectionElement.POSITION);
    
    Integer x1 = (Integer) position1.get(IPosition.X);
    Integer x2 = (Integer) position2.get(IPosition.X);
    
    Integer y1 = (Integer) position1.get(IPosition.Y);
    Integer y2 = (Integer) position2.get(IPosition.Y);
    
    if (x1 > x2) {
      return 1;
    }
    
    if (x1 < x2) {
      return -1;
    }
    
    *//** ****** x1 == x2 ***** *//*
                                   if (y1 > y2) {
                                   return 1;
                                   }
                                   
                                   if (y1 < y2) {
                                   return -1;
                                   }
                                   */
    return 0;
  }
}
