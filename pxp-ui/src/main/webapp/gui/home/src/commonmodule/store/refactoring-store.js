import alertify from "./custom-alertify-store";
import { getTranslations as oTranslations } from "./helper/translation-manager";
import ViewLibraryUtils from "../../viewlibraries/utils/view-library-utils";
import UniqueIdentifierGenerator
  from "../../libraries/uniqueidentifiergenerator/unique-identifier-generator";
import CS from "../../libraries/cs";
import RequestMapping from "../../libraries/requestmappingparser/request-mapping-parser";
import { UploadRequestMapping as oUploadRequestMapping } from "../../screens/homescreen/screens/settingscreen/tack/setting-screen-request-mapping";
import SettingScreenProps
  from "../../screens/homescreen/screens/settingscreen/store/model/setting-screen-props";
import MicroEvent from "../../libraries/microevent/MicroEvent";
import ExceptionLogger from "../../libraries/exceptionhandling/exception-logger";
import CommonUtils from "../util/common-utils";

var RefactoringStore = (function () {

  const _triggerChange = function () {
    RefactoringStore.trigger('store-changed');
  };

  let _isUploadedAssetProcessed = function (sException) {
    let aAssetExceptions = ["ExifToolException", "ImageMagickException", "FFMPEGException", "ApachePOIXWPFException"];
    return CS.includes(aAssetExceptions, sException) ?  false : true;
  };

  const successPostIconToAsset = function (sUniqueId, oCallback, oResponse) {
    const oAssetInfo = oResponse.success.assetKeysModelList[0];
    if (oCallback && oCallback.functionToExecute) {
      oCallback.getAssetInfo ? oCallback.functionToExecute(oAssetInfo) : oCallback.functionToExecute(oAssetInfo.thumbKey);
    }
    _triggerChange();

    return oAssetInfo;
  };

  const failureCallback = (oResponse, sFunctionName, oTranslations, oCallback) => {
    let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
      if (error.key === "EntityConfigurationDependencyException") {
        isConfigError = true;
      }
      return isConfigError;
    }, false);
    if (configError) {
      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
        return;
      }
    }
    CommonUtils.failureCallback(oResponse, sFunctionName, oTranslations);
  };

  const failurePostIconToAsset = function (oResponse) {
    ExceptionLogger.log(oResponse);
    let aErrors = [];
    let aProcessedAssetErrors = [];
    let oFailure = oResponse.failure;
    let aExceptionDetails = oFailure.devExceptionDetails;
    CS.forEach(aExceptionDetails, function (oException) {
      if(!_isUploadedAssetProcessed(oException.key)){
        !CS.includes(aProcessedAssetErrors, oException.itemName) && aProcessedAssetErrors.push(oException.itemName);
      } else {
        aErrors.push(oException)
      }
    });
    if(CS.isNotEmpty(aProcessedAssetErrors)){
      alertify.error(`${oTranslations().UploadedAssetsCouldNotBeProcessed} ${aProcessedAssetErrors.join(", ")}`);
    }
    if (CS.isNotEmpty(aErrors)) {
      oResponse.failure.exceptionDetails = aErrors;
      failureCallback(oResponse, "failurePostIconToAsset", oTranslations());
    }
  };

  const _getIsValidAssetFileTypes = function (oFile, sNatureType) {
    let oScreenProps = SettingScreenProps.screen;
    let oAssetExtensions = oScreenProps.getAssetExtensions();
    let aActiveAssetExtension = oAssetExtensions[sNatureType] ? oAssetExtensions[sNatureType] : oAssetExtensions.allAssets;
    let sTypeRegex = aActiveAssetExtension.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  const _postIconToAsset = function (file, oCallback) {
    var sUniqueId = UniqueIdentifierGenerator.generateUUID();
    var data = new FormData();
    data.append('fileUpload', file);
    data.append('fileUpload', sUniqueId);
    return CS.customPostRequest(RequestMapping.getRequestUrl(oUploadRequestMapping.UploadImage) + "?mode=config&size=20&klassId=asset_asset", data,
      successPostIconToAsset.bind(this, sUniqueId, oCallback), failurePostIconToAsset, false);
  };

  const _uploadIconToAsset = async function (aFiles, bLimitImageSize, oCallback) {
    let dummyPromise = Promise.resolve();
    if (aFiles.length) {
      var oFile = aFiles[0];
      if(_getIsValidAssetFileTypes(oFile)) {
        if (bLimitImageSize && oFile.size > 50000) {
          alertify.error(oTranslations().FILE_SIZE_EXCEEDS_MAXIMUM);
          return dummyPromise;
        }
        return _postIconToAsset(oFile, oCallback);
      } else {
        alertify.error(`${ViewLibraryUtils.getDecodedTranslation(oTranslations().AssetFileTypeNotSupportedException, {assetName: oFile.name})}`);
      }
    } else {
      if (oCallback.functionToExecute) {
        oCallback.functionToExecute("");
      }
    }

    return dummyPromise;
  };

  const _getIconUrl = function (sKey) {
    if (CS.startsWith(sKey, "data:")) {
      return sKey;
    }
    return RequestMapping.getRequestUrl(oUploadRequestMapping.GetAssetImage, {type: "Icons", id: sKey}) + "/";
  };

  const _makeObjectDirty = function (oObjectToMakeDirty) {
    if (!oObjectToMakeDirty.clonedObject) {
      oObjectToMakeDirty.clonedObject = CS.cloneDeep(oObjectToMakeDirty);
      oObjectToMakeDirty.isDirty = true;
    }
  };

  return {

    uploadIconToAsset: function (aFiles, bLimitImageSize, oCallback) {
      _uploadIconToAsset(aFiles, bLimitImageSize, oCallback);
    },

    getIconUrl: function (sAssetKey) {
      return _getIconUrl(sAssetKey);
    },

    makeObjectDirty: function (oObjectToMakeDirty) {
      _makeObjectDirty(oObjectToMakeDirty);
    },

  }

})();

MicroEvent.mixin(RefactoringStore);

export default RefactoringStore;
