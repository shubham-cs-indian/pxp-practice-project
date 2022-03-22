import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ImageCoverflowProps from './../model/image-coverflow-view-props';
import ImageCoverflowUtils from './../helper/image-coverflow-utils';
import ContentUtils from './content-utils';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import LogFactory from '../../../../../../libraries/logger/log-factory';

import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ScreenModeUtils from './screen-mode-utils';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import AttributeTypeDictionary from '../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import StandardAttributeIdDictionary from '../../tack/mock/standard-attribute-id-dictionary';
import TaskProps from './../model/content-task-props';
import AssetUploadContextDictionary from "../../../../../../commonmodule/tack/asset-upload-context-dictionary";

var logger = LogFactory.getLogger('image-coverflow-store');
var trackMe = MethodTracker.getTracker('image-coverflow-store');
var getTranslation = ScreenModeUtils.getTranslationDictionary;
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var ImageCoverflowStore = (function () {

  var _triggerChange = function () {
    ImageCoverflowStore.trigger('image-change');
  };

  //************************************* Private API's **********************************************//

  var _getActiveEntity = function () {
    return ContentUtils.getActiveContent();
  };

  var _makeActiveEntityDirty = function () {
    var oActiveEntity = _getActiveEntity();
    if(!CS.isEmpty(oActiveEntity)) {
      oActiveEntity =  ContentUtils.makeContentDirty(oActiveEntity);
      oActiveEntity.addedAssets = [];
      oActiveEntity.deletedAssets = [];
    }

    return oActiveEntity;
  };

  var _setImageCoverflowActiveIndex = function (sId, iSelectedImageIndex, oCallbackData) {
    trackMe('_setCoverflowSelectedKey');
    logger.debug('_setImageCoverflowActiveIndex: Setting coverflow active index',
        {'selectedImageIndex': iSelectedImageIndex});
    ImageCoverflowUtils.setImageCoverflowActiveIndex(iSelectedImageIndex);
    var oActiveEntity = _getActiveEntity();
    // selectedIndex is sent as -1 when there are no images in the content;
    if (iSelectedImageIndex != -1) {

      ContentUtils.removeAllSelectionInContentScreen();
      ContentUtils.changeItemSelectionMode('image');
      var oSelectedImage;

      oActiveEntity = oActiveEntity.contentClone? oActiveEntity.contentClone: oActiveEntity;

      var aList = oActiveEntity.referencedAssets;
      if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
        aList = oActiveEntity.attributes;
      }

      var aSelectedCoverflowAssets = CS.filter(aList, {attributeId: sId});
      oSelectedImage = aSelectedCoverflowAssets[iSelectedImageIndex];

      ImageCoverflowUtils.setSelectedImage(oSelectedImage.assetObjectKey, oSelectedImage);
      ContentUtils.fillTagValuesListWithObjectTags(oSelectedImage.tags);
    }

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  var successPostToAssetBulk = function (oAttributeProps, oCallbackData, oResponse) {
    var oSuccessResponse = oResponse.success;
    let aAssetKeysModel = oSuccessResponse && oSuccessResponse.assetKeysModelList;
    var bIsAssetUploadSubmitedToBGP = oResponse.isAssetUploadSubmitedToBGP;
    var aAssetSaveInBulk = [];
    var oActiveEntity = ContentUtils.getActiveEntity();
    var bIsForTask = oAttributeProps.isForTask;
    let sContext = oCallbackData.context;

    CS.forEach(aAssetKeysModel, function (oImageValue) {
      var oNewImage = {
        "assetObjectKey": UniqueIdentifierGenerator.generateUUID(),
        "fileName": '',
        "properties": {},
      };

      oNewImage.thumbKey = oImageValue.thumbKey;
      oNewImage.assetObjectKey = oImageValue.imageKey;
      oNewImage.previewImageKey = oImageValue.previewKey;
      oNewImage.hash = oImageValue.hash;
      oNewImage.properties.status = 0;

      var oMetadata = oImageValue.metadata;

      if (oImageValue.width && oImageValue.height) {
        oNewImage.properties.width = oImageValue.width;
        oNewImage.properties.height = oImageValue.height;
      }

      if (oMetadata && oMetadata['name']) {
        oNewImage.fileName = oMetadata['name']
      }
      else if (oMetadata && oMetadata['File:FileName']) {
        oNewImage.fileName = oMetadata['File:FileName']
      }

      if (oMetadata && oMetadata['File:FileTypeExtension']) {
        var sExtensionFromMetadata = oMetadata['File:FileTypeExtension'].toLocaleLowerCase();
        // if (CS.includes(CS.keys(oAssetklassTypeDictionary), sExtensionFromMetadata)) {
          oNewImage.properties.extension = sExtensionFromMetadata;
        // }
      } else if(oImageValue.fileName) {
        oNewImage.properties.extension = oImageValue.fileName.split(".")[1];
        oNewImage.fileName = oImageValue.fileName;
      }

      var sImageType = '';
      var sType = '';
      var oFile = {name: oNewImage.fileName};
      if (_getIsValidAssetFileTypes(oFile, "imageAsset")) {
        sImageType = 'Image';
        sType = 'attachment_asset';
      } else if (_getIsValidAssetFileTypes(oFile, "documentAsset")) {
        sImageType = 'Document';
        sType = 'attachment_asset';
      } else if (_getIsValidAssetFileTypes(oFile, "videoAsset")) {
        sImageType = 'Video';
        sType = 'attachment_asset'
      }
      oNewImage.type = sImageType;

      if (oNewImage.type == 'Video') {
        oNewImage.properties.mp4 = oImageValue.mp4Key;
      }

      if (bIsForTask) {
        oNewImage.type = 'Attachment';
      }

      if (oActiveEntity && oActiveEntity.baseType == BaseTypesDictionary.assetBaseType && !bIsForTask) {
        var oComponentProps = ContentUtils.getComponentProps();
        var oKlass = {};
        let sDuplicateId = oSuccessResponse.duplicateId;
        let sThumbnailPath = oImageValue.thumbnailPath;
        let sFilePath = oImageValue.filePath;
        oKlass.sections = oComponentProps.screen.getActiveSections();
        // _createAssetInstanceWithAttribute(sExtensionFromMetadata, oNewImage, oKlass);
        _assignAssetInfoIntoAsset(oMetadata, oNewImage, oAttributeProps, sDuplicateId, sThumbnailPath, sFilePath);
      } else {
        sType = oImageValue.klassId;
        var oNewArticle = ContentUtils.createDummyArticle('', sType, false);
        oNewArticle.metadata = oMetadata;
        oNewArticle.name = oMetadata.name;
        oNewArticle.isFileUploaded = true;
        oNewArticle.assetInformation = oNewImage;
        aAssetSaveInBulk.push(oNewArticle);
      }
    });

    if (!CS.isEmpty(oResponse.failure)) {
      var aResponseData = oResponse.failure.exceptionDetails;
      if (aResponseData.length !== 0) {
        failurePostToAssetBulk(oResponse);
      }
    }
    if (oActiveEntity && oActiveEntity.baseType == BaseTypesDictionary.assetBaseType && !bIsForTask) {
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();
    } else if (sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION
        || sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP) {
      if (oCallbackData.functionToExecute){
        oCallbackData.functionToExecute(oSuccessResponse);
      } else {
        alertify.success(getTranslation().ASSETS_SUCCESSFULLY_UPLOADED);
      }
      _triggerChange();
    } else if (bIsAssetUploadSubmitedToBGP || ((sContext === AssetUploadContextDictionary.TASK_FILE_ATTACHMENT || sContext === AssetUploadContextDictionary.COMMENT_FILE_ATTACHMENT) && bIsForTask)) {
      _createNewAssets(aAssetSaveInBulk, oCallbackData, bIsForTask);
    }
  };

  var _createNewAssets = function (aAssetsList, oImageUploadCallback, bIsForTask) {
    var oBulkUploadData = {};
    oBulkUploadData.assetInstances = aAssetsList;
    var oBulkUploadProps = ContentUtils.getComponentProps().screen.getBulkUploadProps();

    oBulkUploadData.label = oBulkUploadProps.label || null;
    oBulkUploadData.collectionIds = oBulkUploadProps.collectionIds || [];

    var sUrl = getRequestMapping().BulkCreateAsset;

    if (bIsForTask) {
      sUrl = getRequestMapping().BulkCreateAttachment;
    }

    CS.putRequest(sUrl, {}, oBulkUploadData, successBulkCreateAssetCallback.bind(this, aAssetsList, oImageUploadCallback, bIsForTask),
        failureBulkCreateAssetCallback.bind(this, oImageUploadCallback));
  };

  let _isUploadedAssetProcessed = function (sException) {
    let aAssetExceptions = ["ExifToolException", "ImageMagickException", "FFMPEGException", "ApachePOIXWPFException"];
    return CS.includes(aAssetExceptions, sException) ?  false : true;
  };

  var successBulkCreateAssetCallback = function (aAssetsList, oCallback, bIsForTask, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
          var aResponseData = oResponse.failure.exceptionDetails;
          if (aResponseData.length !== 0) {
            failureBulkCreateAssetCallback(oCallback, oResponse);
          }
    }

    var oScreenProps = ContentUtils.getComponentProps().screen;
    oScreenProps.setBulkUploadProps({});
    oScreenProps.setBulkUploadCollectionView(false);

    if(!bIsForTask) {
      alertify.success(getTranslation().ASSETS_SUCCESSFULLY_UPLOADED);
    }

    if(oCallback && oCallback.functionToExecute) {
      oResponse.filterContext = oCallback.filterContext;
      oCallback.functionToExecute(oResponse, aAssetsList);
    }

    return {oResponse, aAssetsList};
  };

  var failureBulkCreateAssetCallback = function (oCallback, oResponse) {
    let aResponseData = oResponse.failure.exceptionDetails;
    let oTranslations = getTranslation();
    let aRenditionNotProcessedWarnings = [];
    let aErrors = [];
    let sRenditionNotProcessedException = "RenditionNotProcessedException";

    ContentUtils.getComponentProps().screen.setBulkUploadProps({});

    CS.forEach(aResponseData, function (oFailure) {
      if(oFailure.key === sRenditionNotProcessedException){
        aRenditionNotProcessedWarnings.push(oFailure.itemName);
      } else {
        aErrors.push(oFailure);
      }
    });

    if (CS.isNotEmpty(aRenditionNotProcessedWarnings)) {
      alertify.warning(`${oTranslations[sRenditionNotProcessedException]} ${aRenditionNotProcessedWarnings.join(", ")}`);
    }
    if (CS.isNotEmpty(aErrors)) {
      ContentUtils.failureCallback(oResponse,'failureBulkCreateAssetCallback',getTranslation());
    }
  };

  var failurePostToAssetBulk = function (oResponse) {
    let aResponseData = oResponse.failure.exceptionDetails;
    let aWarnings = [];
    let aErrors = [];
    let oTranslations = getTranslation();
    let sAssetAlreadyUploadedExceptionKey = "AssetAlreadyUploadedException";
    let sExtractedFileNotProcessedException = "ExtractedFileNotProcessedException";
    let sAssetFileTypeNotSupportedException = "AssetFileTypeNotSupportedException";
    let aExtractedFileNotProcessed = [];
    let aFileTypeNotSupportedAssetNames = [];
    let aFailures = [];
    let oRelationshipProps = ContentUtils.getComponentProps().relationshipView;
    oRelationshipProps.setIsBulkUploadDialogOpen(false);

    CS.forEach(aResponseData, function (oFailure) {
      if (oFailure.key === sAssetAlreadyUploadedExceptionKey) {
        aWarnings.push(oFailure.itemName);
      } else if( !_isUploadedAssetProcessed(oFailure.key)){
        !CS.includes(aFailures, oFailure.itemName) && aFailures.push(oFailure.itemName);
      }  else if(oFailure.key === sExtractedFileNotProcessedException){
    	  aExtractedFileNotProcessed.push(oFailure.itemName);
      } else if (oFailure.key === sAssetFileTypeNotSupportedException){
        aFileTypeNotSupportedAssetNames.push(oFailure.itemName);
      }
      else {
        aErrors.push(oFailure);
      }
    });

    if (CS.isNotEmpty(aWarnings)) {
      alertify.warning(`${oTranslations[sAssetAlreadyUploadedExceptionKey]} ${aWarnings.join(", ")}`);
    }
    if(CS.isNotEmpty(aFailures)){
      alertify.warning(`${oTranslations.UploadedAssetsCouldNotBeProcessed}  ${aFailures.join(", ")}`);
    }
    if(CS.isNotEmpty(aExtractedFileNotProcessed)){
      alertify.warning(`${oTranslations[sExtractedFileNotProcessedException]}  ${aExtractedFileNotProcessed.join(", ")}`);
    }
    if (CS.isNotEmpty(aFileTypeNotSupportedAssetNames)) {
      alertify.error(`${ContentUtils.getDecodedTranslation(getTranslation().AssetFileTypeNotSupportedException, {assetName: aFileTypeNotSupportedAssetNames})}`);
    }
    if (CS.isNotEmpty(aErrors)) {
      oResponse.failure.exceptionDetails = aErrors;
      ContentUtils.failureCallback(oResponse, 'failurePostToAssetBulk', getTranslation());
    }
    _triggerChange();
  };

  var failureGetAssetStatus = function (oResponse) {
    ContentUtils.failureCallback(oResponse,'failureGetAssetStatus',getTranslation());
  };

  /******************************* Private ****************************/
  var _getAssetStatus = function (oNewImage, oResponse) {
    if (!oResponse) {
      oResponse = {};
    }
    if (!CS.has(oNewImage, 'properties'))
      return;
    var oNewImageClone = CS.cloneDeep(oNewImage);
    var count = 0;

    if (oResponse.status /*&& oNewImage.properties.status*/) {
      oNewImage.properties.status = oResponse.status;
      count += parseInt(oNewImage.properties.status);
    }
    if (count == 100 || oResponse.status == 100) {
      if (oNewImage.properties.mp4) {
        oNewImage.properties.mp4 = oNewImage.properties.mp4.replace('#', '');
      } else {
        oNewImage.assetObjectKey = oNewImage.assetObjectKey.replace('#', '');
      }
      count = 100;
      ImageCoverflowUtils.removeFromActiveVideoStatusRequests(oNewImage.assetObjectKey);
    }
    if (count != 100) {
      var id = oNewImage.assetObjectKey.replace('#', '');
      if (oNewImage.properties.mp4) {
        id = oNewImage.properties.mp4.replace('#', '');
      }

      setTimeout(function () {
        CS.customPostRequest((RequestMapping.getRequestUrl(getRequestMapping().GetAssetStatus, {
          type: oNewImage.type,
          id: id
        }) + "/")).then(_getAssetStatus.bind(this, oNewImage), failureGetAssetStatus);
      }, 2000);
    }
    if (!CS.isEqual(oNewImageClone, oNewImage))
      _triggerChange();
  };

  var _assignAssetInfoIntoAsset = function (oMetadata, oNewImage, oAttributeProps, sDuplicateId, sThumbnailPath, sFilePath) {
    var oContentClone = ContentUtils.makeActiveContentDirty();
    let oScreenProps = ContentUtils.getComponentProps().screen;

    oContentClone.assetInformation = oNewImage;

    oContentClone.metadata = oMetadata;
    oContentClone.isFileUploaded = true;
    oContentClone.duplicateId = sDuplicateId;
    oContentClone.thumbnailPath = sThumbnailPath;
    oContentClone.filePath = sFilePath;
    ContentUtils.setVariantVersionProps(oContentClone);

    if(oAttributeProps.functionToExecute) {
        oAttributeProps.functionToExecute();
    }

    var aList = oContentClone.referencedAssets;
    if(oContentClone.baseType == BaseTypesDictionary.assetBaseType) {
      aList = oContentClone.attributes;
      oScreenProps.setSelectedImageId(oNewImage.thumbKey);
    }
    var aCoverFlowAttributes = CS.filter(aList, {attributeId: oAttributeProps.id});
    if(!ImageCoverflowUtils.getPropsForContext()) {
      ImageCoverflowStore.setDefaultPropsForContext();
    }

    var bIsDefault = true;
    var bSetDefaultIndex = false;
    if(aCoverFlowAttributes.length == 0) {
      bIsDefault= true;
      bSetDefaultIndex = true;
    }
    var sActiveImageIndex = bSetDefaultIndex? CS.findIndex(aCoverFlowAttributes, {isDefault: true}): aCoverFlowAttributes.length-1;
    _setImageCoverflowActiveIndex(oAttributeProps.id, sActiveImageIndex, {});
    oNewImage.isDefault = bIsDefault;

    var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(oNewImage.attributeId, 'attribute');
    if(!CS.isEmpty(oSection)) {
      ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
    }
    ImageCoverflowUtils.setIsAttributeImageDialogOpen(false);
  };

  var _removeMetaDataInfoFromAsset = function (oEntity) {
    if(oEntity.baseType == BaseTypesDictionary.assetBaseType) {
      CS.forEach(oEntity.attributes, function (oAttribute) {
        if(oAttribute.attributeId == StandardAttributeIdDictionary[AttributeTypeDictionary.STANDARD_EXIF]) {
          //Exif
          oAttribute.value = '';
        }
        else if (oAttribute.attributeId == StandardAttributeIdDictionary[AttributeTypeDictionary.STANDARD_IPTC]) {
          //Iptc
          oAttribute.value = '';
        }
        else if (oAttribute.attributeId == StandardAttributeIdDictionary[AttributeTypeDictionary.STANDARD_XMP]) {
          //Xmp
          oAttribute.value = '';
        }
        else if (oAttribute.attributeId == StandardAttributeIdDictionary[AttributeTypeDictionary.STANDARD_OTHER]) {
          //Other
          oAttribute.value = '';
        }
      });
    }
  };

  let getModeForAssetUpload = function (bIsUploadedFromAsset, sContext) {
    let sMode = "";
    if (bIsUploadedFromAsset) {
      sMode = "singleUpload";
    } else {
      if (sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION
          || sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP) {
        sMode = "relationshipBulkUpload";
      } else {
        sMode = "bulkUpload";
      }
    }

    return sMode;
  };

  var postToAssetBulk = function (data, oAttributeProps, oCallbackData, bIsUploadedFromAsset, sKlassId, sContext = "") {
    oCallbackData.context = sContext;
    let sMode = getModeForAssetUpload(bIsUploadedFromAsset, sContext);
    var sUrl = RequestMapping.getRequestUrl(getRequestMapping().AssetBulkUpload, {mode: sMode, isUploadedFromInstance: bIsUploadedFromAsset, klassId: sKlassId});

    if (oAttributeProps.isForTask) {
      sUrl = RequestMapping.getRequestUrl(getRequestMapping().AssetBulkUpload, {mode: 'Attachment', isUploadedFromInstance: bIsUploadedFromAsset, klassId: "attachment_asset"});
    }
    if (oCallbackData.hasOwnProperty('isForegroundUpload') && oCallbackData.isForegroundUpload) {
      sUrl = RequestMapping.getRequestUrl(getRequestMapping().AssetsForegroundUpload, {mode: "relationshipUpload", klassId: sKlassId});
    }

    return CS.customPostRequest(sUrl, data, successPostToAssetBulk.bind(this, oAttributeProps, oCallbackData), failurePostToAssetBulk, false);
  };

  let _getIsValidAssetFileTypes = function (oFile, sNatureType) {
    let oScreenProps = ContentUtils.getComponentProps().screen;
    let oAssetExtensions = oScreenProps.getAssetExtensions();
    let aActiveAssetExtension = oAssetExtensions[sNatureType] ? oAssetExtensions[sNatureType] : oAssetExtensions.allAssets;
    let sTypeRegex = aActiveAssetExtension.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  }

  var _changedCoverflowDialogStatus = (sId, sContext, bStatus) => {
    if(sContext === "contentHeader") {
      ImageCoverflowUtils.setIsHeaderImageDialogOpen(bStatus);
    } else if (sContext === "attribute") {
      ImageCoverflowUtils.setIsAttributeImageDialogOpen(bStatus);
    }
  };

  var _handleAssetUploadEvent = function (aFiles, oAttributeProps, oCallbackData) {
    trackMe('_handleCoverflowImageUploadEvent');

    var oScreenProps = ContentUtils.getComponentProps().screen;

    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    let bIsUploadedFromAsset = true;
    let sActiveContentNatureType = oScreenProps.getActiveContentClass().natureType;
    let aInvalidAssets = [];
    if (aFiles.length) {
      var iFilesInProcessCount = 0;

      //TODO: Test Properly
      if(aFiles.length >= 2) {
        var sAlertMessage = getTranslation().MAXIMUM + " 1 " + getTranslation().ITEMS_CAN_BE_ADDED;
        alertify.message(sAlertMessage);
        return;
      }

      //TODO: Handling for zip remaining
      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {

        if (_getIsValidAssetFileTypes(file, sActiveContentNatureType)) {

          bIsAnyValidImage = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {

            var iDotIndex = theFile.name.lastIndexOf(".");
            var sExtension = theFile.name.substr(iDotIndex + 1).toLowerCase();
            return function (event) {
              var filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              if(!bIsAnyInvalidImage) {
                let sklassId = oScreenProps.getActiveContent().__NATURE_KLASS_ID;
                postToAssetBulk(data, oAttributeProps, oCallbackData, bIsUploadedFromAsset, sklassId);
              }
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
          aInvalidAssets.push(file.name);
        }
      });
    }

    if (bIsAnyInvalidImage) {
      alertify.error(`${ContentUtils.getDecodedTranslation(getTranslation().AssetFileTypeNotSupportedException, {assetName: aInvalidAssets})}`);
      ContentUtils.hideLoadingScreen();
    }

    if(!bIsAnyValidImage){
      ContentUtils.hideLoadingScreen();
    }
  };

  let getAssetTypeById = function (sKlassId) {
    switch(sKlassId){
      case "image_asset":
        return "imageAsset";

      case "video_asset":
        return "videoAsset";

      case "document_asset":
        return "documentAsset";

      default:
           return "allAssets";

    }
  };

  let _fillRequestModelFromCallback = function (oRequestData, oCallback) {
    if (oCallback.hasOwnProperty('requestData') && oCallback.requestData) {
      let aKeys = oCallback.requestData.keys;
      let aValues = oCallback.requestData.values;
      CS.forEach(aKeys, function (sKey, iIndex) {
        if (typeof aValues[iIndex] === 'object') {
          oRequestData.append(sKey, JSON.stringify(aValues[iIndex]));
        } else {
          oRequestData.append(sKey, aValues[iIndex]);
        }
      });
      delete oCallback.requestData;
    }
  };

  var _handleContentAssetBulkUploadButtonClicked =  function (sId, oFiles, oCallbackData, bIsForTask, sContext) {
    trackMe('handleContentAssetBulkUploadButtonClicked');
    let oBulkUploadProps = ContentUtils.getComponentProps().screen.getBulkUploadProps();
    let sAssetNatureType = CS.isEmpty(oBulkUploadProps.klassId) ? getAssetTypeById("") : getAssetTypeById(oBulkUploadProps.klassId[0]);
    sContext = CS.isEmpty(oCallbackData) && CS.isEmpty(oCallbackData.context) ? sContext : oCallbackData.context;
    var aFiles = oFiles.files;
    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    let bIsUploadedFromAsset = false;
    let aInvalidAssets = [];
    if (aFiles.length) {

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {

        if (_getIsValidAssetFileTypes(file, sAssetNatureType)) {
          bIsAnyValidImage = true;

          var sFilekey = UniqueIdentifierGenerator.generateUUID();//oNewImage.assetObjectKey;
          data.append(sFilekey, file);
        } else {
          bIsAnyInvalidImage = true;
          aInvalidAssets.push(file.name);
        }
      });
    }

    if(bIsAnyInvalidImage){
      alertify.error(`${ContentUtils.getDecodedTranslation(getTranslation().AssetFileTypeNotSupportedException, {assetName: aInvalidAssets})}`);
      ContentUtils.hideLoadingScreen();
    }

    if(!bIsAnyValidImage){
      ContentUtils.hideLoadingScreen();
    }

    if(bIsAnyValidImage) {
      var oAttributeProps = {
        isForTask : bIsForTask
      };
      let aCollectionIds = oBulkUploadProps.collectionIds || [];
      data.append("collectionIds", aCollectionIds.toString());
      _fillRequestModelFromCallback(data, oCallbackData);
      return postToAssetBulk(data, oAttributeProps, oCallbackData, bIsUploadedFromAsset, oBulkUploadProps.klassId, sContext);
    }

    return Promise.resolve();
  };

  var _updateImageCoverActiveIndexData = function (sContext) {
    var oImageCoverflowProps = ImageCoverflowProps.getImageCoverflowMapProps();
      if(oImageCoverflowProps.prevImageCoverflowActiveIndex){
        if(sContext == "discard"){
          oImageCoverflowProps.imageCoverflowActiveIndex = oImageCoverflowProps.prevImageCoverflowActiveIndex;
        }
        delete oImageCoverflowProps.prevImageCoverflowActiveIndex;
      }
  };

  /************************************** Public API's **********************************************/
  return {

    handleAssetUploadClick: function (sActionName, oProperties, oCallback) {

      let oCallbackData = {};
      switch (sActionName.toLowerCase()) {
        case "upload_image" :
          let oAttributeProps = {};
          oAttributeProps.id = "assetcoverflowattribute";
          oAttributeProps.functionToExecute = oCallback.functionToExecute;
          oCallbackData.functionToExecute = function () {
            ImageCoverflowUtils.setIsHeaderImageDialogOpen(false);
          };
          _handleAssetUploadEvent(oProperties.files, oAttributeProps, oCallbackData);
          break;
      }
    },

    handleCoverflowDetailViewClosedButtonClicked: function(sId, sContext) {
      trackMe('handleCoverflowDetailViewClosedButtonClicked');
      var oActiveEntity = _getActiveEntity();
      if(oActiveEntity.contentClone) {
        var aList = oActiveEntity.contentClone.referencedAssets;
        var bIsAssetType = oActiveEntity.baseType == BaseTypesDictionary.assetBaseType;
        if(bIsAssetType) {
          aList = oActiveEntity.contentClone.attributes;
        }
        var aRemovedAssets = CS.remove(aList, {isNewlyUploaded: true});
        if(aRemovedAssets.length){
          var aImageTypeAttributes = aList;
          if(bIsAssetType) {
            aImageTypeAttributes = CS.filter(aList, {baseType: BaseTypesDictionary.imageAttributeInstanceBaseType});
          }
          ImageCoverflowStore.setImageCoverflowActiveIndex(aImageTypeAttributes.length-1);
        }

        var aOriginalAssetObjectList = ImageCoverflowUtils.getOriginalAssetObjectList();
        CS.forEach(aOriginalAssetObjectList, function(oOriginalAssetObject){
          var oAssetObject = CS.find(aList, {id: oOriginalAssetObject.id});
          CS.assign(oAssetObject, oOriginalAssetObject);
        });

        _removeMetaDataInfoFromAsset(oActiveEntity.contentClone);
      }
      ImageCoverflowUtils.setOriginalAssetObjectList([]);

      _changedCoverflowDialogStatus(sId, sContext, false);

      ImageCoverflowUtils.resetActiveVideoStatusRequests();
      var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(sId, 'attribute');
      if(!CS.isEmpty(oSection)) {
        ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
      }
      let oTaskData = TaskProps.getTaskData();
      oTaskData.activeProperty = {};
      TaskProps.setShowAnnotations(false);
    },

    setCoverflowImageAsDefault: function (oModel, iImageCoverflowItemIndex, sId) {
      trackMe('setCoverflowImageAsDefault');
      logger.debug('setCoverflowImageAsDefault: setting coverflow image as default',
                   {'imageIndexToSelect': iImageCoverflowItemIndex,
                     'imageCoverflowModel': oModel
                   });

      var oActiveEntity = _makeActiveEntityDirty();

      //DATAMIGRATION: change to mappingId
      var aList = oActiveEntity.referencedAssets;
      if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
        aList = oActiveEntity.attributes;
      }

      var aCoverflowAssets = CS.filter(aList, {attributeId: sId});
      var oSelectedImage = aCoverflowAssets[iImageCoverflowItemIndex];
      var isDefault = !oSelectedImage.isDefault;

      //set all the coverflow images as default false
      CS.forEach(aCoverflowAssets, function(oImage) {
        oImage.isDefault = false;
      });

      oSelectedImage.isDefault = isDefault;

      // push this image to the selectionImagesList
      ImageCoverflowUtils.emptySelectedImages();
      ImageCoverflowUtils.setSelectedImage(oModel.coverflowImageKey, oSelectedImage);

      // fill the tagValuesList with the tags of the selected Image from the active, opened content
      ContentUtils.fillTagValuesListWithObjectTags(oSelectedImage.tags);

      ImageCoverflowUtils.setImageCoverflowActiveIndex(iImageCoverflowItemIndex);

      var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(sId, 'attribute');
      if(!CS.isEmpty(oSection)) {
        ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
      }
    },

    handleOpenCoverflowViewerButtonClicked: function (oModel, iImageCoverflowItemIndex, sId, sContext) {
      trackMe('handleOpenCoverflowViewerButtonClicked');
      var oActiveEntity = _getActiveEntity();

      ContentUtils.removeAllSelectionInContentScreen();
      ContentUtils.changeItemSelectionMode('image');
      oActiveEntity = oActiveEntity.contentClone? oActiveEntity.contentClone: oActiveEntity;

      var aList = oActiveEntity.referencedAssets;
      if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
        aList = oActiveEntity.attributes;
      }
      var aCoverflowAssets = CS.filter(aList, {attributeId: sId});
      var oSelectedImage = oActiveEntity.assetInformation;

      // push this image to the selectionImagesList
      ImageCoverflowUtils.emptySelectedImages();
      ImageCoverflowUtils.setSelectedImage(oModel.coverflowImageKey, oSelectedImage);

      _changedCoverflowDialogStatus(sId, sContext, true);

      // fill the tagValuesList with the tags of the selected Image from the active, opened content
      ContentUtils.fillTagValuesListWithObjectTags(oSelectedImage.tags);

      ImageCoverflowUtils.setImageCoverflowActiveIndex(iImageCoverflowItemIndex);
      var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(sId, 'attribute');
      if(!CS.isEmpty(oSection)) {
        ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
      }
    },

    handleCoverflowDetailViewAssetItemClicked: function (oModel, assetObjectKey, sId) {
      trackMe('handleCoverflowDetailViewAssetItemClicked');

      var oActiveEntity = _getActiveEntity();

      oActiveEntity = oActiveEntity.contentClone? oActiveEntity.contentClone: oActiveEntity;

      var aList = oActiveEntity.referencedAssets;
      if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
        aList = oActiveEntity.attributes;
      }
      var sAssetId = oModel.id;
      var aCoverflowAssets = CS.filter(aList, {attributeId: sId});
      var iIndex = CS.findIndex(aCoverflowAssets, {"id": sAssetId});

      ImageCoverflowUtils.setImageCoverflowActiveIndex(iIndex);
      var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(sId, 'attribute');
      if(!CS.isEmpty(oSection)) {
        ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
      }
    },

    handleCoverflowDetailViewDataChanged: function (oModel, sContext, sId, assetObjectKey, sNewValue) {
      trackMe('handleCoverflowDetailViewDataChanged');
      var oActiveEntity = _makeActiveEntityDirty();

      var aList = oActiveEntity.referencedAssets;
      if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
        aList = oActiveEntity.attributes;
      }

      var aCoverflowAssets = CS.filter(aList, {attributeId: sId});
      var oSelectedAsset = CS.find(aCoverflowAssets, {"assetObjectKey": assetObjectKey});

      var oContentAttribute = CS.find(aList, {id: oSelectedAsset.id});
      var aOriginalAssetObjectList = ImageCoverflowProps.getOriginalAssetObjectList();
      if(!CS.find(aOriginalAssetObjectList, {id: oSelectedAsset.id})){
        aOriginalAssetObjectList.push(CS.cloneDeep(oContentAttribute));
      }
      oContentAttribute[sContext] = sNewValue;
      var oSection = ContentUtils.getSectionFromActiveEntityClassByEntityId(sId, 'attribute');
      if(!CS.isEmpty(oSection)) {
        ContentUtils.getComponentProps().contentSectionViewProps.setSectionsToUpdate(oSection.id);
      }
    },

    getSelectedImages: function () {
      return ImageCoverflowUtils.getSelectedImages();
    },

    setImageCoverflowActiveIndex: function (iIndex) {
      ImageCoverflowUtils.setImageCoverflowActiveIndex(iIndex);
    },

    setMaxCoverflowItemAllowed: function (iMaxCoverflowItemAllowed) {
      ImageCoverflowUtils.setMaxCoverflowItemAllowed(iMaxCoverflowItemAllowed);
    },

    setDefaultPropsForContext: function () {
      ImageCoverflowUtils.setDefaultPropsForContext();
    },

    getAssetStatus: function (oAsset) {
      _getAssetStatus(oAsset)
    },

    uploadFileAttachment: function (sContext, oFiles, oCallbackData){
      return _handleContentAssetBulkUploadButtonClicked(sContext, oFiles, oCallbackData, true);
    },

    handleContentAssetBulkUploadButtonClicked: function (sId, oFiles, oCallbackData, sContext) {
      return _handleContentAssetBulkUploadButtonClicked(sId, oFiles, oCallbackData, false, sContext);
    },

    updateImageCoverActiveIndexData: function (sContext) {
      _updateImageCoverActiveIndexData(sContext);
    },

    handleDashboardBulkUpload: function (aFiles) {
      ImageCoverflowProps.setBulkUploadFiles(aFiles);
    },

    postToAssetBulk: function (data, oAttributeProps, oCallbackData, bIsUploadedFromAsset, sKlassId) {
      postToAssetBulk(data, oAttributeProps, oCallbackData, bIsUploadedFromAsset, sKlassId)
    }
  }
})();

MicroEvent.mixin(ImageCoverflowStore);

export default ImageCoverflowStore;
