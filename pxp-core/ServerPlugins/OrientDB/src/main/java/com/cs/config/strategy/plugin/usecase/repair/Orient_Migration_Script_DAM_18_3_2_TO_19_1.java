package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.smartdocument.ISmartDocument;
import com.cs.core.config.interactor.model.hidden.IGetPropertyTranslationsHiddenModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_DAM_18_3_2_TO_19_1 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_DAM_18_3_2_TO_19_1(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_DAM_18_3_2_TO_19_1/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex imageAsset = UtilClass.getVertexById(SystemLevelIds.IMAGE,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Vertex videoAsset = UtilClass.getVertexById(SystemLevelIds.VIDEO,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Vertex documentAsset = UtilClass.getVertexById(SystemLevelIds.DOCUMENT,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Vertex smartDocument = UtilClass.getVertexById(SystemLevelIds.SMART_DOCUMENT_ID,
        VertexLabelConstants.SMART_DOCUMENT);
    
    // creating extension
    createImageExtensions(imageAsset);
    createVideoExtensions(videoAsset);
    createDocumentExtensions(documentAsset);
    
    // modifying label of Video (Video ---> Multimedia)
    modifyVideoAssetLabel(videoAsset);
    
    // add Host-IP and Port in Smart Document
    addIpAndPortInSmartDocument(smartDocument);
    
    addCodeInReferences();
    return null;
  }
  
  protected void createImageExtensions(Vertex imageAsset) throws Exception
  {
    AssetUtils.createAndLinkExtension(imageAsset, "jpg", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "jpeg", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "png", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "eps", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "ai", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "ico", false, true);
    AssetUtils.createAndLinkExtension(imageAsset, "psd", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "tif", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "tiff", true, true);
    AssetUtils.createAndLinkExtension(imageAsset, "gif", true, true);
  }
  
  protected void createVideoExtensions(Vertex videoAsset) throws Exception
  {
    AssetUtils.createAndLinkExtension(videoAsset, "wmv", true, true);
    AssetUtils.createAndLinkExtension(videoAsset, "avi", true, true);
    AssetUtils.createAndLinkExtension(videoAsset, "flv", true, true);
    AssetUtils.createAndLinkExtension(videoAsset, "mpeg", true, true);
    AssetUtils.createAndLinkExtension(videoAsset, "mpg", true, true);
    AssetUtils.createAndLinkExtension(videoAsset, "mp4", true, true);
  }
  
  protected void createDocumentExtensions(Vertex documentAsset) throws Exception
  {
    AssetUtils.createAndLinkExtension(documentAsset, "pdf", true, true);
    AssetUtils.createAndLinkExtension(documentAsset, "ppt", true, true);
    AssetUtils.createAndLinkExtension(documentAsset, "pptx", true, true);
    AssetUtils.createAndLinkExtension(documentAsset, "doc", true, true);
    AssetUtils.createAndLinkExtension(documentAsset, "docx", true, true);
    AssetUtils.createAndLinkExtension(documentAsset, "xls", true, false);
    AssetUtils.createAndLinkExtension(documentAsset, "xlsx", true, false);
    AssetUtils.createAndLinkExtension(documentAsset, "obj", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "stp", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "fbx", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "zip", true, false);
    AssetUtils.createAndLinkExtension(documentAsset, "xtex", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "indd", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "idms", false, false);
    AssetUtils.createAndLinkExtension(documentAsset, "idml", true, true);
  }
  
  protected void modifyVideoAssetLabel(Vertex videoAsset)
  {
    videoAsset.setProperty(IGetPropertyTranslationsHiddenModel.LABEL__EN_US, "Multimedia");
    videoAsset.setProperty(IGetPropertyTranslationsHiddenModel.LABEL__FR_FR, "Multimedia__fr_FR");
    videoAsset.setProperty(IGetPropertyTranslationsHiddenModel.LABEL__ES_ES, "Multimedia__es_ES");
    videoAsset.setProperty(IGetPropertyTranslationsHiddenModel.LABEL__DE_DE, "Multimedia__de_DE");
    videoAsset.setProperty(IGetPropertyTranslationsHiddenModel.LABEL__EN_UK, "Multimedia");
  }
  
  protected void addIpAndPortInSmartDocument(Vertex smartDocument)
  {
    smartDocument.setProperty(ISmartDocument.RENDERER_HOST_IP, "");
    smartDocument.setProperty(ISmartDocument.RENDERER_PORT, "");
  }
  
  protected void addCodeInReferences()
  {
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVerticesOfClass("Reference")
        .iterator();
    
    while (iterator.hasNext()) {
      Vertex reference = iterator.next();
      if (reference.getProperty(CommonConstants.CODE_PROPERTY) == null) {
        reference.setProperty(CommonConstants.CODE_PROPERTY,
            reference.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
  }
}
