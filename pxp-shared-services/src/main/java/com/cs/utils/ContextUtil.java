package com.cs.utils;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;

public class ContextUtil {
  
  public static ContextType getContextTypeByType(String type)
  {
    ContextType contextType = ContextType.UNDEFINED;
    
    switch (type) {
      case CommonConstants.ATTRIBUTE_VARIANT_CONTEXT:
        contextType = ContextType.ATTRIBUTE_CONTEXT;
        break;
      
      case CommonConstants.PRODUCT_VARIANT:
        contextType = ContextType.LINKED_VARIANT;
        break;
      
      case CommonConstants.RELATIONSHIP_VARIANT:
        contextType = ContextType.RELATIONSHIP_VARIANT;
        break;
      
      case CommonConstants.CONTEXTUAL_VARIANT:
        contextType = ContextType.EMBEDDED_VARIANT;
        break;
      
      case CommonConstants.GTIN_VARIANT:
        contextType = ContextType.GTIN_VARIANT;
        break;
      
      case CommonConstants.IMAGE_VARIANT : 
        contextType = ContextType.IMAGE_VARIANT;
        break;
        
      default:
        contextType = ContextType.UNDEFINED;
    }
    return contextType;
  }
  
  public static ContextType getContextTypeByClassNatureType(String type)
  {
    ContextType contextType = ContextType.UNDEFINED;
    
    switch (type) {
      case CommonConstants.EMBEDDED_KLASS_TYPE:
        contextType = ContextType.EMBEDDED_VARIANT;
        break;
      
      case CommonConstants.GTIN_KLASS_TYPE:
        contextType = ContextType.GTIN_VARIANT;
        break;
        
      case CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE:
        contextType = ContextType.IMAGE_VARIANT;
        break;  
      
      default:
        contextType = ContextType.UNDEFINED;
    }
    return contextType;
  }
}
