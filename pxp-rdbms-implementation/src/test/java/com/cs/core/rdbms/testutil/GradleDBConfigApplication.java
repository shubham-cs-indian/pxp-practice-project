package com.cs.core.rdbms.testutil;

import com.cs.core.technical.rdbms.exception.RDBMSException;

public class GradleDBConfigApplication {
  
  public static void main(String[] args) throws RDBMSException, ClassNotFoundException
  {
    
    AbstractRDBMSDriverTests.registerDriver();
  }
}
