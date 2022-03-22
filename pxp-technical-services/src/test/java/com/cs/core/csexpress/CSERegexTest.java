package com.cs.core.csexpress;

import org.junit.Test;

public class CSERegexTest {
 
  @Test
  public void cseToRegex()
  {
   System.out.println(CSERegex.CSELikeToJavaRegex("abc%")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("%ab\\\\%cd")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("%abc")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("%abc%")); 
   
   System.out.println(CSERegex.CSELikeToJavaRegex("abc_")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("_ab\\\\_cd")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("_abc")); 
   System.out.println(CSERegex.CSELikeToJavaRegex("_abc_")); 
   
   System.out.println(CSERegex.CSELikeToJavaRegex("_abc%")); 
  }
  
}
