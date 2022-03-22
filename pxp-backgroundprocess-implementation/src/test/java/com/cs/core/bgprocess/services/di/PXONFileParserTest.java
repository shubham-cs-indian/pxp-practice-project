package com.cs.core.bgprocess.services.di;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;

import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;

/**
 * @author tauseef
 */
public class PXONFileParserTest {
  
  private static final String TEST_PROPERTIES_PATH = "./src/test/bgprocess.properties";
  
  @Before
  public void init() throws CSInitializationException
  {
    CSProperties.init(TEST_PROPERTIES_PATH);
  }
  
  @Test
  public void parseFileBlock() throws CSInitializationException, CSFormatException
  {
    printTestTitle("runSamples parseFileBlock");
    String directory = CSProperties.instance().getString("httpServer.downloadDir");
    Path filePath = FileSystems.getDefault().getPath(directory, "importEntities.pxon");
    
    PXONFileParser pxonFileParser = new PXONFileParser(filePath.toString());
    PXONFileParser.PXONBlock blockInfo = null;
    while ((blockInfo = pxonFileParser.getNextBlock()) != null) {
      println("Start Position : " + blockInfo.getStart());
      println("End Position : " + blockInfo.getEnd());
      println("Data : " + blockInfo.getData());
    }
  }
}
