package com.cs.core.runtime.strategy.utils;

import java.util.Collection;

public class DbUtils {
  
  public static String quoteIt(Collection<String> listOfStrings)
  {
    StringBuilder classesArrayInString = new StringBuilder();
    classesArrayInString.append("(");
    int arraySize = listOfStrings.size();
    for (String listItem : listOfStrings) {
      arraySize--;
      classesArrayInString.append("'" + listItem + "'");
      if (arraySize != 0) {
        classesArrayInString.append(", ");
      }
    }
    classesArrayInString.append(")");
    
    return classesArrayInString.toString();
  }
  
}
