package com.cs.core.services;

import com.cs.core.technical.exception.CSInitializationException;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Singleton to access properties system
 *
 * @author Pankaj Gajjar
 */
public final class CSProperties {

  private static File instanceFile = null;
  private static CSProperties instance = null;
  private Configuration config;

  private CSProperties() throws CSInitializationException {
    reload();
  }

  /**
   * Must be call once before any reference to instance() It can be called 2 times, once initialized the CSProperties instance cannot be
   * overwritten anymore.
   *
   * @param propertyFilePath defines the file path of property file
   * @throws CSInitializationException
   */
  public static void init(String propertyFilePath) throws CSInitializationException {
    if (instance != null) {
      return;
    }
    instanceFile = new File(propertyFilePath);
    if (!instanceFile.exists()) {
      throw new CSInitializationException("Properties file not found at " + instanceFile.getPath());
    }
    instance = new CSProperties();
  }

  /**
   * @return unique instance of properties
   * @throws CSInitializationException
   */
  public static CSProperties instance() throws CSInitializationException {
    if (instance == null) {
      throw new CSInitializationException(
              "Properties not initialized - call to CSProperties.init() is mandatory");
    }
    return instance;
  }

  /**
   * (re)load the declared property file to take changes into account
   *
   * @throws CSInitializationException
   */
  public void reload() throws CSInitializationException {
    try {
      config = (new Configurations()).properties(instanceFile);
    } catch (ConfigurationException ex) {
      throw new CSInitializationException("Properties not loaded", ex);
    }
  }

  /**
   * @return all defined property names
   */
  public Set<String> getPropertyNames() {
    Set<String> keys = new TreeSet<>();
    Iterator<String> keyItr = config.getKeys();
    while (keyItr.hasNext()) {
      keys.add(keyItr.next());
    }
    return keys;
  }

  /**
   * @param propertyName property name
   * @return String property from the property file or empty string by default
   */
  public String getString(String propertyName) {
    return config.getString(propertyName, "");
  }

  /**
   * @param propertyName property name
   * @return an array of property values from the property file or an empty array by default
   */
  public String[] getStringArray(String propertyName) {
    try {
      return config.getStringArray(propertyName);
    } catch (Exception ex) {
      return new String[0];
    }
  }

  /**
   * @param propertyName property name
   * @return long property from the property file or 0 by default
   */
  public long getLong(String propertyName) {
    return config.getLong(propertyName, 0L);
  }

  /**
   * @param propertyName property name
   * @return integer property from the property file or 0 by default
   */
  public int getInt(String propertyName) {
    return config.getInt(propertyName, 0);
  }

  /**
   * @param propertyName property name
   * @return boolean property from the property file or false by default
   */
  public boolean getBoolean(String propertyName) {
    return config.getBoolean(propertyName, Boolean.FALSE);
  }
}
