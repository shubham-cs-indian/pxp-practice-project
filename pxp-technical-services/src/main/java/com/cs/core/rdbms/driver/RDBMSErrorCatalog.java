package com.cs.core.rdbms.driver;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Store all RDBMS Error Information as a catalog with code, type and message text. The catalog supports i18n internationalization. This is
 * done through bundle property files - RDBMSErrorCatalog_en.properties for English, - RDBMSErrorCatalog_de.properties for German, -
 * RDBMSErrorCatalog_fr.properties for French, ...
 *
 * @author PankajGajjar
 */
public class RDBMSErrorCatalog {

  // Singleton implementation
  private static RDBMSErrorCatalog INSTANCE = null;
  private static ResourceBundle errorBundle = null;

  private RDBMSErrorCatalog() throws RDBMSException {
    Locale currentLocale = Locale.ENGLISH; // default locale, will change later
    // stage how to use it well by
    // application
    try {
      URL[] urls = {new File("bundles").toURI()
        .toURL()};
      ClassLoader loader = new URLClassLoader(urls);
      errorBundle = ResourceBundle.getBundle("ErrorCataLogBundle", currentLocale, loader);
    } catch (IOException e) {
      throw new RDBMSException(0, "Initialization", e);
    }
  }

  /**
   * @return singleton implementation
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public RDBMSErrorCatalog instance() throws RDBMSException {
    if (INSTANCE == null) {
      INSTANCE = new RDBMSErrorCatalog();
    }
    return INSTANCE;
  }

  /**
   * @param errorCode the error code
   * @param defType is the default type returned in case of unknown error code
   * @param defMessage is the default message returned in case of unknown error code
   * @return internationalized error information
   */
  public Information getInformation(int errorCode, String defType, String defMessage) {
    String catalogCode = String.format("%04d", errorCode);
    try {
      String desc[] = errorBundle.getString(catalogCode)
              .split(":");
      return new Information(desc[0], (desc.length > 1 ? desc[1] : defMessage));
    } catch (NullPointerException | MissingResourceException | ClassCastException e) {
      return new Information(defType, defMessage);
    }
  }

  /**
   * @param errorCode the error code
   * @param defType is the default type returned in case of unknown error code
   * @param defMessage is the default message returned in case of unknown error code
   * @return an exception ready to be thrown with internationalized error information
   */
  public RDBMSException getException(int errorCode, String defType, String defMessage) {
    Information info = new Information(defType, defMessage);
    return new RDBMSException(errorCode, info.getType(), info.getMessage());
  }

  /**
   * A simple POJO class containing error information
   */
  public static class Information {

    private final String type;
    private final String message;

    public Information(String type, String message) {
      this.type = type;
      this.message = message;
    }

    public String getType() {
      return type;
    }

    public String getMessage() {
      return message;
    }
  }
}
