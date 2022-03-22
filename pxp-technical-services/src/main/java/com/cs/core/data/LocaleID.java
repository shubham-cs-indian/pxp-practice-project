package com.cs.core.data;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Simple Locale information holder Note: it was possible to reuse java.util.Locale here but there was some questions about its behavior and
 * tolerance to codes not supported by java
 *
 * @author vallee
 */
public class LocaleID implements Comparable {

  private final String languageID;
  private final String middleID;
  private final String  regionID;
  private final Boolean isSixFigured;

  /**
   * Default constructor
   */
  public LocaleID() {
    languageID = "";
    regionID = "";
    middleID = "";
    isSixFigured = false;
  }

  /**
   * Constructor by value
   *
   * @param languageCode
   * @param regionCode
   */
  public LocaleID(String languageCode, String regionCode) {
    languageID = languageCode;
    regionID = regionCode;
    isSixFigured = false;
    middleID = "";
  }

  /**
   * Constructor from xx_XX standardized locale ID
   *
   * @param localeID
   */
  public LocaleID(String localeID) {

    if (localeID == null || localeID.isEmpty()) {
      languageID = "";
      regionID = "";
      middleID = "";
      isSixFigured = false;
    }
    else {
      String lid = localeID.trim(); // trim in order to manage properly possible
      // empty entry
      String[] localePart = lid.split("[_|-]");
      languageID = localePart.length > 0 ? localePart[0] : "";

      if (localePart.length == 3) {
        regionID = localePart[2];
        middleID = localePart[1];
        isSixFigured = true;
      }
      else if (localePart.length == 2) {
        regionID = localePart[1];
        middleID = "";
        isSixFigured = false;
      }
      else {
        regionID = "";
        middleID = "";
        isSixFigured = false;
      }
    }
  }

  /**
   * @return the language code of this localeID
   */
  public String getLanguageCode() {
    return languageID;
  }

  /**
   * @return the region code of this localeID
   */
  public String getRegionCode() {
    return regionID;
  }

  /**
   * @return true if this locale ID is empty
   */
  public boolean isEmpty() {
    return (languageID.isEmpty() && regionID.isEmpty());
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "";
    }
    if(isSixFigured){
      return languageID + "_" + middleID +"_" + regionID;
    }
    return languageID + "_" + regionID;
  }

  @Override
  public boolean equals(Object other) {
    return new EqualsBuilder().append(this.toString(),
            other).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(2, 3).append(toString())
            .build();
  }

  @Override
  public int compareTo(Object other) {
    return new CompareToBuilder().append(this.toString(),
            other.toString()).toComparison();
  }
}
