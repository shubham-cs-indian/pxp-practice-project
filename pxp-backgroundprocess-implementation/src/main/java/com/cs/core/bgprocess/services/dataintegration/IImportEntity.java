package com.cs.core.bgprocess.services.dataintegration;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface IImportEntity {

  public static final String LIST = "list";

  /**
   * Import the entity that implements this method
   *
   * @throws CSInitializationException
   * @throws CSFormatException
   * @throws RDBMSException
   */
  public void importEntity(PXONImporter pxonImporter) throws CSInitializationException, CSFormatException, RDBMSException;

}
