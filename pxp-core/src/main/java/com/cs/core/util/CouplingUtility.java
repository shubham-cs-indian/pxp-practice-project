package com.cs.core.util;

import com.cs.constants.CommonConstants;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;

public class CouplingUtility {
  
  public static CouplingType getCouplingType(String couplingTypeString)
  {
    CouplingType couplingType = CouplingType.UNDEFINED;
    
    if (couplingTypeString == null) {
      return couplingType;
    }
    
    switch (couplingTypeString) {
      case CommonConstants.TIGHTLY_COUPLED:
        // couplingType = CouplingType.TIGHT;
        break;
      
      case CommonConstants.LOOSELY_COUPLED:
        // couplingType = CouplingType.LOOSE;
        break;
      
      case CommonConstants.DYNAMIC_COUPLED:
        // assign ENUM
        break;
      
      case CommonConstants.READ_ONLY_COUPLED:
        // assign ENUM
        break;
      
      default:
        // TODO throw exception for unknown type
        break;
    }
    return couplingType;
  }
}
