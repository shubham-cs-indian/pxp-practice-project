package com.cs.ui.config.controller.usecase.icons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.UploadAssetModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.config.interactor.usecase.assetserver.IUploadMultipleAssetsToServer;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@RestController
public class UploadIcons {
  
  @Autowired
  protected String              orientDBHost;
  
  @Autowired
  protected String              orientDBPort;
  
  @Autowired
  protected String              orientDBdatabase;
  
  @Autowired
  protected Authenticator       getAuthenticator;
  
  @Autowired
  IUploadMultipleAssetsToServer uploadMultipleAssetsToServer;
  
  @RequestMapping(value = "/upload/link/icons", method = RequestMethod.GET)
  public String uploadMultipleFileHandler() throws Exception
  {
    Map<String, String> iconIdsJson = new HashMap<>();
    UploadAssetModel uploadAssetModel = new UploadAssetModel();
    File iconsSheet = new File("IconsImport/iconssheet.xlsx");
    FileInputStream fileInputStream = new FileInputStream(iconsSheet);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
    
    // Map<String,String> taxonomyIcons = new HashMap<>();
    // iconIdsJson.put("taxonomy", taxonomyIcons);
    XSSFSheet taxSheet = workbook.getSheetAt(0);
    readFileAndUploadImages(uploadAssetModel, iconIdsJson, taxSheet);
    String response = executePlugin("Orient_Linking_Icons", iconIdsJson);
    // Map<String,String> tagsIcons = new HashMap<>();
    // iconIdsJson.put("tags", tagsIcons);
    /*XSSFSheet tagsSheet = workbook.getSheet("tags");
    readFileAndUploadImages(uploadAssetModel, iconIdsJson, tagsSheet);
    
    //  Map<String,String> pcIcons = new HashMap<>();
    //    iconIdsJson.put("pc", pcIcons);
    XSSFSheet pcSheet = workbook.getSheet("pc");
    readFileAndUploadImages(uploadAssetModel, iconIdsJson, pcSheet);*/
    
    return response;
  }
  
  private void readFileAndUploadImages(UploadAssetModel uploadAssetModel,
      Map<String, String> taxonomyIcons, XSSFSheet sheet)
      throws FileNotFoundException, IOException, Exception
  {
    Iterator<Row> rowIterator = sheet.iterator();
    List<String> logs = new ArrayList<>();
    while (rowIterator.hasNext()) {
      Row row = rowIterator.next();
      String entityId = row.getCell(0)
          .getStringCellValue();
      String fileName = row.getCell(1)
          .getStringCellValue();
      
      File iconFile = new File("IconsImport/icons/" + fileName);
      try {
        FileInputStream is = new FileInputStream(iconFile);
        List<IMultiPartFileInfoModel> multiPartFileInfoModelList = new ArrayList<>();
        MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
        multiPartFileInfoModel.setKey(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix()));
        multiPartFileInfoModel.setBytes(IOUtils.toByteArray(is));
        multiPartFileInfoModel.setOriginalFilename(fileName);
        multiPartFileInfoModelList.add(multiPartFileInfoModel);
        uploadAssetModel.setMode(CommonConstants.MODE_CONFIG);
        uploadAssetModel.setMultiPartFileInfoList(multiPartFileInfoModelList);
        IBulkUploadResponseAssetModel uploadResponse = uploadMultipleAssetsToServer
            .execute(uploadAssetModel);
        IAssetKeysModel assetKeysModel = uploadResponse.getSuccess().getAssetKeysModelList()
            .get(0);
        String imageKey = assetKeysModel.getImageKey();
        taxonomyIcons.put(entityId, imageKey);
      }
      catch (FileNotFoundException e) {
        logs.add(fileName);
      }
    }
    System.out.println("Icons not found :\n" + logs);
  }
  
  public String executePlugin(String useCase, Object requestMap) throws Exception
  {
    
    URL URI = new URL("http://" + orientDBHost + ":" + orientDBPort + "/" + useCase + "/"
        + orientDBdatabase + "/");
    System.out.println("UPLOADING ICONS!@!");
    System.out.println("\nURI : " + URI);
    // System.out.println("\nParam : " + contentJson);
    Authenticator.setDefault(getAuthenticator);
    HttpURLConnection connection = prepareConnection(URI, "POST");
    String contentJson = ObjectMapperUtil.writeValueAsString(requestMap);
    if (contentJson != null) {
      DataOutputStream output = new DataOutputStream(connection.getOutputStream());
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
      writer.write(contentJson);
      writer.close();
    }
    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
      return getResponseString(connection.getInputStream());
      
    }
    else if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
      return null;
    }
    else {
      DataInputStream errorDataStream = new DataInputStream(connection.getErrorStream());
      StringBuilder content = new StringBuilder("");
      BufferedReader d = new BufferedReader(new InputStreamReader(errorDataStream));
      String str;
      while ((str = d.readLine()) != null) {
        content.append(str);
      }
      throw new Exception(
          "Failed Plugin : " + URI + " reason : " + getResponseString(connection.getInputStream()));
      // return null;
    }
  }
  
  protected String getResponseString(InputStream in) throws IOException
  {
    StringBuilder content = new StringBuilder();
    DataInputStream input = new DataInputStream(in);
    BufferedReader d = new BufferedReader(new InputStreamReader(input, "UTF-8"));
    String str;
    while ((str = d.readLine()) != null) {
      content.append(str);
    }
    
    return content.toString();
  }
  
  protected HttpURLConnection prepareConnection(URL URI, String methodType) throws Exception
  {
    HttpURLConnection connection = (HttpURLConnection) URI.openConnection();
    connection.setRequestMethod(methodType);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.connect();
    
    return connection;
  }
}
