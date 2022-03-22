package com.cs.core.runtime.interactor.usecase.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CSLogUtil {
  
  public static String getStackTrace(Throwable throwable)
  {
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }
}
