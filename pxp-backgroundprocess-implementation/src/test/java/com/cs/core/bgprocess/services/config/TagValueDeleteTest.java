package com.cs.core.bgprocess.services.config;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.TagValueDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class TagValueDeleteTest extends AbstractBGProcessTests {
  
  private static final String SERVICE = "TAG_VALUE_DELETE";
  
  @Before
  @Override
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void deleteTagValueBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    
    TagValueDeleteDTO entryData = new TagValueDeleteDTO();
    
    Set<String> deletedTagValues = new HashSet<>();
    deletedTagValues.add("64MB");

    entryData.setTagValues(deletedTagValues);
    
    println(entryData.toJSON());
    
    BGPDriverDAO.instance()
        .submitBGPProcess("Admin", SERVICE, getTestCallbackTemplateURL(), userPriority,
            new JSONContent(entryData.toJSON()));
    
    this.runJobSample(5);
  }
}
