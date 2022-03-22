package com.cs.core.csexpress;

import java.util.regex.Pattern;

import com.cs.core.data.Text;

/**
 * Functions that convert like and regex expressions from CSE to Java
 *
 * @author vallee
 */
public class CSERegex {

  //HANDLING ONLY FOR START WITH, ENDS WITH AND CONTAINS. 
  private static String CSEToRegex(String cseRegex) {
    return Text.unescapeQuotedString(cseRegex)
            .replaceAll("(?<!\\\\)%", ".*")
            .replaceAll("(?<!\\\\)_", ".")
            .replaceAll("\\\\\\\\%", "%")
            .replaceAll("\\\\\\\\_", "_");
  }

  
  /**
   * Transform a CS literal like expression into JAVA regex
   *
   * @param likeExpression
   * @return the corresponding Java regex pattern
   */
  public static Pattern CSELikeToJavaRegex(String likeExpression) {
    return Pattern.compile(CSEToRegex(likeExpression));
  }

  public static Pattern CSERegexToJavaRegex(String regexEpression) {
    return Pattern.compile(String.format("^%s$", CSEToRegex(regexEpression)));
  }
}
