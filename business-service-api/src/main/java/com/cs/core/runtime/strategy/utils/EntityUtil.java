package com.cs.core.runtime.strategy.utils;

import com.cs.core.runtime.interactor.constants.application.Constants;

import java.util.List;

public class EntityUtil {
  
  public static String quoteIt(Object unquotedString)
  {
    return "\"" + unquotedString + "\"";
  }
  
  public static String quoteIt(List<String> listOfStrings)
  {
    // String unQuotedArrayOfString = listOfStrings.toString();
    StringBuilder classesArrayInString = new StringBuilder();
    classesArrayInString.append("[");
    int arraySize = listOfStrings.size();
    for (String listItem : listOfStrings) {
      arraySize--;
      classesArrayInString.append(quoteIt(listItem));
      if (arraySize != 0) {
        classesArrayInString.append(", ");
      }
    }
    classesArrayInString.append("]");
    
    return classesArrayInString.toString();
  }
  
  public static String getEntityForType(String klassType)
  {
    String entityType = null;
    
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        entityType = Constants.ARTICLE_INSTANCE_MODULE_ENTITY;
        break;
      case Constants.ASSET_KLASS_TYPE:
        entityType = Constants.ASSET_INSTANCE_MODULE_ENTITY;
        break;
      case Constants.MARKET_KLASS_TYPE:
        entityType = Constants.MARKET_INSTANCE_MODULE_ENTITY;
        break;
      case Constants.TEXT_ASSET_KLASS_TYPE:
        entityType = Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY;
        break;
      case Constants.SUPPLIER_KLASS_TYPE:
        entityType = Constants.SUPPLIER_INSTANCE_MODULE_ENTITY;
        break;
    }
    
    return entityType;
  }
  
  /**
   * if id is null or empty it return -1 else id itself simply it never retuens
   * null id insteal it return -1
   *
   * @param id
   * @return
   */
  public static String getDefaultIdIfNull(String id)
  {
    if (id == null || id.equals("undefined") || id.equals("") || id.equals("null")) {
      return "-1";
    }
    return id;
  }
}
