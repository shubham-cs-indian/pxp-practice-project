package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.businessapi.base.Validations;
import com.cs.config.dto.ConfigUserDTO;
import com.cs.config.idto.IConfigUserDTO;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.config.user.UserValidation;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.runtime.strategy.utils.AuthUtils;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/** @author vallee */
public class ImportUsers implements IImportEntity {

  private static final String IMPORT_USERS                    = "ImportUsers";


  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> userBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_USER);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigUserDTO> userDTOList = new ArrayList<>();
      for (Entry userBlock : userBlocks.entrySet()) {
        try {
        IConfigUserDTO configUserDTO = new ConfigUserDTO();
        configUserDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) userBlock.getValue()));
        Validations.validateCode(configUserDTO.getCode());
        String userName = configUserDTO.getUserName();
        if(StringUtils.isAnyEmpty(userName, configUserDTO.getFirstName(), configUserDTO.getLastName(), configUserDTO.getEmail())) {
          importer.incrementNumberOfException();
          importer.getJobData().getLog().error( "UserName , FirstName, LastName and Email all fields are mandatory" + configUserDTO.toPXON());
        }else {
          UserValidation.validateContactNumber(configUserDTO.getContact());
          String gender = UserValidation.validateGender(configUserDTO.getGender());
          configUserDTO.setGender(gender);
          IUserDTO userDTO = configurationDAO.createUser(userName);
          configUserDTO.setUserIID(userDTO.getUserIID());
          String defaultUserPassword = (String) BGProcessApplication.getApplicationContext().getBean("defaultUserPassword");
          configUserDTO.setPassword(AuthUtils.getSaltedHash(defaultUserPassword));
          userDTOList.add(configUserDTO);
        }
        }catch (Exception e) {
          importer.incrementNumberOfException();
          importer.getJobData().getLog().exception(e);
          importer.getJobData().getLog().error("Inavlid User info" + userBlock.getKey());
        }
      }
      // create users in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), userDTOList.toArray(new IConfigUserDTO[0]));
      importer.logIds(responseMap);
    } catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigUserDTO... users) throws CSInitializationException, CSFormatException
  {
    List<IConfigUserDTO> usersList = Arrays.asList(users);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, usersList);

    return CSConfigServer.instance().request(requestModel, IMPORT_USERS, localeID);
  }
}
