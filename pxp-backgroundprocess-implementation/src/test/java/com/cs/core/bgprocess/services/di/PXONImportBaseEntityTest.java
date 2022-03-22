package com.cs.core.bgprocess.services.di;

import com.cs.core.services.CSProperties;
import org.junit.Test;

public class PXONImportBaseEntityTest extends AbstractPXONImporterTest {

  private static final String TEST_FOLDER          = "/entityPxon";
  private static final String IMPORT_BASE_ENTITIES = TEST_FOLDER + "/importEntities.pxon";

  @Test
  public void importBaseEntities() throws Exception
  {
    String directory = CSProperties.instance().getString("nfs.file.path");
    String fileName = prepareImportData(directory, getPxonAsString(IMPORT_BASE_ENTITIES));
    submitImportProcess(fileName);
  }

}
