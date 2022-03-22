package com.cs.core.initialize;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.cs.core.config.iconlibrary.IUploadStandardIconsToServerService;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.iconlibrary.IUploadMultipleIconsRequestModel;
import com.cs.core.config.interactor.model.iconlibrary.UploadMultipleIconsRequestModel;

@Component
public class InitializeIconForStandardEntitiesService implements IInitializeIconForStandardEntitesService {

  @Autowired
  IUploadStandardIconsToServerService uploadStandardIconsToServerService;
  
  @Autowired
  ResourceLoader                      resourceLoader;
 
  @Override
  public void execute() throws Exception
  {
    uploadStandardIconsToServerService.execute(getUploadIconFileModel());
  }
  
   private IUploadMultipleIconsRequestModel getUploadIconFileModel() throws Exception
  {
    IUploadMultipleIconsRequestModel uploadMultipleIconsRequestModel = new UploadMultipleIconsRequestModel();
    Resource resource = null;
    URL inputUrl = getClass().getResource("/standardicons");
    final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    if (jarFile.isFile()) {
      resource = resourceLoader.getResource(inputUrl.toString());
    }
    else {
      resource = resourceLoader.getResource("classpath:standardicons");
    }
     
     File file = resource.getFile();
   
     List<IMultiPartFileInfoModel> multiPartFileInfoList = new ArrayList<>();
     Map<String, Object> fileKeyVsCodeMap = new HashMap<String, Object>();
     Map<String, Object> fileKeyVsNameMap =new HashMap<>();
       int count  = 0;
       for (final File f : file.listFiles()) {
           try {
               if (StringUtils.isNotBlank(f.getPath())) {
                 IMultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
                 File imageFile = new File(f.getPath());
                 Path path = Paths.get(f.getPath());
                 byte[] bytes = Files.readAllBytes(path);
                 String filekey = "file"+count;
                 multiPartFileInfoModel.setKey(filekey);
                 multiPartFileInfoModel.setBytes(bytes);
                 multiPartFileInfoModel.setOriginalFilename(imageFile.getName());
                 multiPartFileInfoModel.setAdditionalProperty("path", path);
                 multiPartFileInfoList.add(multiPartFileInfoModel);
                 fileKeyVsCodeMap.put(filekey, imageFile.getName().split("\\.")[0]);
                 fileKeyVsNameMap.put(filekey, imageFile.getName());
               }
               count++;
             
           } catch (final IOException e) {
               // handle errors here
           }
     }
     uploadMultipleIconsRequestModel.setFileKeyVsCodeMap(fileKeyVsCodeMap);
     uploadMultipleIconsRequestModel.setMultiPartFileInfoList(multiPartFileInfoList);
     uploadMultipleIconsRequestModel.setFileKeyVsNameMap(fileKeyVsNameMap);
    return uploadMultipleIconsRequestModel;
  }
}
