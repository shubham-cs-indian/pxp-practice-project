/*package com.cs.core.runtime.strategy.db;

import java.util.Properties;

public class XProperties extends Properties {

  private static final long serialVersionUID = 1L;
  private static final String START_CONST     = "{";
  private static final String END_CONST       = "}";
  private static final int    MAX_SUBST_DEPTH = 5;

  public XProperties()
  {
    super();
  }

  public XProperties(Properties defaults)
  {
    super(defaults);
  }

  public String getProperty(String key)
  {
    return getProperty(key, 0);
  }

  private String getProperty(String key, int level)
  {

    String value = super.getProperty(key);
    if (value != null) {

      int beginIndex = 0;
      int startName = value.indexOf(START_CONST, beginIndex);

      while (startName != -1) {
        if (level + 1 > MAX_SUBST_DEPTH) {
          return value;
        }

        int endName = value.indexOf(END_CONST, startName);
        if (endName == -1) {
          return value;
        }

        String constName = value.substring(startName + 1, endName);
        String constValue = getProperty(constName, level + 1);

        if (constValue == null) {
          return value;
        }

        String newValue = (startName > 0) ? value.substring(0, startName) : "";
        newValue += constValue;

        beginIndex = newValue.length();
        newValue += value.substring(endName + 1);
        value = newValue;

        startName = value.indexOf(START_CONST, beginIndex);
      }
    }

    return value;
  }

}
*/
