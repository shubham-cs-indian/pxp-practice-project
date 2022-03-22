import CS from '../../../../../../libraries/cs';
import ContentScreenProps from './../model/content-screen-props';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import DownloadDialogListViewModel from "../../view/model/download-dialog-list-view-model";
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import DraggableListViewModel from "../../../../../../viewlibraries/draggablelistview/model/draggable-list-view-model";
import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import ContentUtils from './content-utils';

const sFileNameRegex = /[\\\/:*?"<>|“.]/;

const BulkAssetDownloadStore = (function () {

  var _triggerChange = function () {
    BulkAssetDownloadStore.trigger('bulkAssetDownload-change');
  };

  //Returns invalid message and along wd value if name is invalid;
  let _getAssetNameWithValidation = function (oData, sKey, aInvalidRowIds, iId = 0, bCanDownload = true, bIsChecked = true) {
    let sValue = "";
    let oModel = {};

    switch (sKey) {
      case "assetInstanceName":
        sValue = oData[sKey];
        break;
      case "assetFileName":
        sValue = CS.isNotEmpty(oData[sKey]) ? oData[sKey] : oData["assetInstanceName"];
        break;
    }

    if (CS.isEmpty(sValue) && bCanDownload) {
      oModel.name = sValue;
      oModel.isInvalid = true;
      oModel.invalidMessage = getTranslation().FILENAME_CANNOT_BE_BLANK;
    } else if (sValue.match(sFileNameRegex) && bCanDownload) {
      oModel.name = sValue;
      oModel.isInvalid = true;
      oModel.invalidMessage = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME + " \\ / : * ? \" < > | \"";
    } else {
      oModel.name = sValue;
      oModel.isInvalid = false;
      oModel.invalidMessage = "";
    }

    if (bIsChecked && oModel.isInvalid && bCanDownload) {
      if (oData.hasOwnProperty('assetInstanceId')) {
        aInvalidRowIds.push(oData.assetInstanceId);
      } else {
        if (iId > 0) {
          aInvalidRowIds.push(iId);
        }
      }
    }

    return oModel;
  };

  //Process back end response for dialog view and set props;
  let _preProcessDownloadDialogDataForView = function (bShouldDownloadAssetWithOriginalFilename) {
    let sNameToPick = "";
    let oIdVsKeyObject = {};
    let bIsNoAssetHaveFileName = false;
    let iTotalSelectedAssetsSize = 0;
    let iTotalSelectedAssetsCount = 0;
    let sAssetInstanceIdKey = "assetInstanceId";
    let oBulkDownloadProps = ContentScreenProps.bulkDownloadAssetProps;
    let aDownloadDialogInfo = oBulkDownloadProps.getDownloadInfo();
    let aInvalidRowIds = oBulkDownloadProps.getInvalidRowIds();
    if (bShouldDownloadAssetWithOriginalFilename) {
      sNameToPick = "assetFileName";
    } else {
      sNameToPick = "assetInstanceName";
    }

    let aDialogData = CS.map(aDownloadDialogInfo, function (oAssetInfo) {
      let bIsTIVClassChildDisabled = false;
      let bIsMainAssetDisabled = false;
      let bIsMainAssetClassChildDisabled = false;
      let oTivInfo = oAssetInfo.tivdownloadInformation;
      let bIsParentInvalid = false;
      let sParentInvalidMessage = "";
      let oMainAssetNameValidationModel = _getAssetNameWithValidation(oAssetInfo, sNameToPick, aInvalidRowIds, 0, oAssetInfo.canDownload);
      if (!bIsNoAssetHaveFileName) {
        bIsNoAssetHaveFileName = CS.isNotEmpty(oAssetInfo.assetFileName);
      }
      let oMainAssetNameModel = {
        "assetFileName": oAssetInfo.assetFileName,
        "assetInstanceName": oAssetInfo.assetInstanceName,
      };
      let sMainAssetId = oAssetInfo.assetInstanceId;
      oIdVsKeyObject[sMainAssetId] = oMainAssetNameModel;
      if (!bIsMainAssetDisabled) {
        bIsMainAssetDisabled = oAssetInfo.size === 0;
        bIsMainAssetClassChildDisabled = true;
      }

      let aChildren = [{
        id: getTranslation().MAIN_ASSET,
        label: getTranslation().MAIN_ASSET,
        isExpanded: true,
        totalSize: oAssetInfo.size,
        totalCount: 1,
        isChecked: (oAssetInfo.size !== 0 && oAssetInfo.canDownload),
        isPartialChecked: false,
        isCheckboxDisabled: oAssetInfo.size === 0 && oAssetInfo.canDownload,
        children: [new DownloadDialogListViewModel(oAssetInfo[sAssetInstanceIdKey], oMainAssetNameValidationModel.name, oAssetInfo.size,
            oAssetInfo.extension, (oAssetInfo.size !== 0), (oAssetInfo.size === 0 || !oAssetInfo.canDownload),
            oMainAssetNameValidationModel.isInvalid, oMainAssetNameValidationModel.invalidMessage)],
      }];
      if (CS.isNotEmpty(oTivInfo)) {
        let aTivClasses = Object.keys(oTivInfo);
        CS.forEach(aTivClasses, function (sCode) {
          let aTivChildren = [];
          let sTIVClassName = "";
          let iTivChildrenSize = 0;
          let iNumberOfTIVHavingSizeZero = 0;
          let bIsTIVChildDisabled = false;
          let bIsTIVHaveDownloadPermission = false;
          let iTIVChildrenCount = oTivInfo[sCode].length;
          CS.forEach(oTivInfo[sCode], function (oTivInfo) {
            let sId = oTivInfo[sAssetInstanceIdKey];
            let oTIVNameModel = {
              "assetFileName": oTivInfo.assetFileName,
              "assetInstanceName": oTivInfo.assetInstanceName,
            };
            oIdVsKeyObject[sId] = oTIVNameModel;
            sTIVClassName = oTivInfo.assetKlassName || "[" + sCode + "]";
            if (!bIsNoAssetHaveFileName) {
              bIsNoAssetHaveFileName = CS.isNotEmpty(oTivInfo.assetFileName);
            }
            if (!bIsTIVHaveDownloadPermission) {
              bIsTIVHaveDownloadPermission = oTivInfo.canDownload;
            }
            let bIsChecked = (oTivInfo.size !== 0);
            let oTIVChildrenNameValidationModel = _getAssetNameWithValidation(oTivInfo, sNameToPick, aInvalidRowIds, 0, oTivInfo.canDownload);
            if (oTIVChildrenNameValidationModel.isInvalid && !bIsParentInvalid) {
              bIsParentInvalid = true;
              sParentInvalidMessage = oTIVChildrenNameValidationModel.invalidMessage;
            }
            if (bIsChecked && oTIVChildrenNameValidationModel.isInvalid) {
              if (!CS.includes(aInvalidRowIds, sMainAssetId)) {
                aInvalidRowIds.push(sMainAssetId);
              }
            }
            iTivChildrenSize += oTivInfo.size;
            if (oTivInfo.size === 0) {
              iTIVChildrenCount -= 1;
              iNumberOfTIVHavingSizeZero ++;
              bIsTIVClassChildDisabled = true;
              bIsTIVChildDisabled = true;
            }
            aTivChildren.push(new DownloadDialogListViewModel(sId, oTIVChildrenNameValidationModel.name, oTivInfo.size, oTivInfo.extension,
                bIsChecked, (oTivInfo.size === 0 || !oTivInfo.canDownload), oTIVChildrenNameValidationModel.isInvalid,
                oTIVChildrenNameValidationModel.invalidMessage));
          });

          if (CS.isNotEmpty(aTivChildren)) {
            let oChildModel = {
              id: sCode,
              label: sTIVClassName,
              isExpanded: true,
              totalSize: iTivChildrenSize,
              totalCount: iTIVChildrenCount,
              isTIVChildDisabled: bIsTIVChildDisabled,
              isChecked: (iTivChildrenSize !== 0 && !bIsTIVChildDisabled && bIsTIVHaveDownloadPermission),
              isPartialChecked: iTivChildrenSize !== 0 && oTivInfo[sCode].length > iNumberOfTIVHavingSizeZero && bIsTIVChildDisabled,
              isCheckboxDisabled: (iTivChildrenSize === 0),
              children: aTivChildren,
            };
            aChildren.push(oChildModel);
          }
        });
      }

      let iCount = oAssetInfo.totalContent;
      iTotalSelectedAssetsSize += oAssetInfo.totalSize;
      iTotalSelectedAssetsCount += iCount;
      let iTotalSize = oAssetInfo.totalSize;

      let oProperties = {
        id: sMainAssetId,
        totalSize: iTotalSize,
        totalCount: iCount,
        selectedContentSize: iTotalSize,
        selectedContentCount: iCount,
        isExpanded: false,
        disableExpandButton: (iCount === 0),
        isCheckboxDisabled: (iCount === 0),
        isClassChildDisabled: bIsTIVClassChildDisabled || bIsMainAssetClassChildDisabled,
        isPartialChecked: !oAssetInfo.allDownloadPermission || (iCount >= 1 && bIsTIVClassChildDisabled) || bIsMainAssetDisabled,
        children: aChildren,
      };

      if (bIsParentInvalid || oMainAssetNameValidationModel.isInvalid) {
        oProperties.isInvalid = true;
        oProperties.invalidMessage = oMainAssetNameValidationModel.isInvalid
                                     ? oMainAssetNameValidationModel.invalidMessage : sParentInvalidMessage;
      }

      return new DraggableListViewModel(
          oAssetInfo.assetKlassName,
          oMainAssetNameValidationModel.name,
          (!oProperties.isPartialChecked && oAssetInfo.allDownloadPermission && iCount !== 0),
          true,
          false,
          "asset/" + oAssetInfo.container + "/" + oAssetInfo.thumbKey,
          true,
          true,
          "activeNodeIcon",
          true,
          "assetDownload",
          "",
          oProperties
      );
    });

    //Setting updated props based on response.
    oBulkDownloadProps.setIdVsNameModel(oIdVsKeyObject);
    oBulkDownloadProps.setTotalSelectedAssetsSize(iTotalSelectedAssetsSize);
    oBulkDownloadProps.setTotalSelectedAssetsCount(iTotalSelectedAssetsCount);
    CS.isNotEmpty(aInvalidRowIds) && oBulkDownloadProps.setInvalidRowIds(aInvalidRowIds);
    oBulkDownloadProps.setIsToggleButtonDisabled((iTotalSelectedAssetsCount === 0 && bShouldDownloadAssetWithOriginalFilename)
        || !bIsNoAssetHaveFileName);
    oBulkDownloadProps.setActiveDownloadDialogData({downloadModel: aDialogData, isDirty: false, context: sNameToPick});
  };

  /*If parent (i.e TIV class or asset instance) checked/unchecked then check/uncheck its children.
    iParentLevel: 1: Top most parent updated (i.e asset instance).
    iParentLevel: 2: TIV class.
    sPropertyKey: Which key in model updated.
  */
  let _updateDownloadDialogChildElementOnParentUpdate = function (oUpdatedParentElement, sPropertyKey, iParentLevel) {
    let value = oUpdatedParentElement[sPropertyKey];

    switch (iParentLevel) {
      case 1:
        let oProperties = oUpdatedParentElement.properties;
        if (oProperties.isClassChildDisabled && oProperties.isPartialChecked) {
          value = true;
        }
        CS.forEach(oProperties.children, function (oClassChild) {
          let bIsCheckboxDisabled = oClassChild.isCheckboxDisabled;
          if (!bIsCheckboxDisabled) {
            oClassChild[sPropertyKey] = value;
            CS.forEach(oClassChild.children, function (oChildElement) {
              let bIsChildDisabled = oChildElement.isDisabled;
              if (!bIsChildDisabled) {
                oChildElement[sPropertyKey] = value;
              }
            });
          }
        });
        break;
      case 2:
        CS.forEach(oUpdatedParentElement.children, function (oChild) {
          let bIsChildDisabled = oChild.isDisabled;
          if (oUpdatedParentElement.isTIVChildDisabled && oUpdatedParentElement.isPartialChecked) {
            value = true;
          }
          if (!bIsChildDisabled) {
            oChild[sPropertyKey] = value;
          }
        });
        break;
    }
  };

  let _handleBulkDownloadDialogCheckboxClicked = function (sId, iIndex, sClassId = "") {
    let oBulkAssetDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let oActiveDialogData = oBulkAssetDownloadDialogProps.getActiveDownloadDialogData();
    let aActiveDialogData = oActiveDialogData.isDirty ? oActiveDialogData.clonedObject : oActiveDialogData.downloadModel;
    let bIsFolderByAsset = oBulkAssetDownloadDialogProps.getIsFolderByAsset();
    let aInvalidRowIds = oBulkAssetDownloadDialogProps.getInvalidRowIds();

    if (sId == "folderByAssetCheckbox") {
      oBulkAssetDownloadDialogProps.setIsFolderByAsset(!bIsFolderByAsset);
    } else {
      let oUpdatedParentElement = aActiveDialogData[iIndex];
      let oProperties = oUpdatedParentElement.properties;
      let bIsClassChildDisabled = oProperties.isClassChildDisabled;
      if (oProperties.id == sId && sClassId !== getTranslation().MAIN_ASSET) {
        oUpdatedParentElement.isChecked = !oUpdatedParentElement.isChecked;
        if (oUpdatedParentElement.isChecked && bIsClassChildDisabled) {
          if (oProperties.isPartialChecked) {
            oProperties.isPartialChecked = !oUpdatedParentElement.isChecked;
          } else {
            oProperties.isPartialChecked = oUpdatedParentElement.isChecked;
          }
          oUpdatedParentElement.isChecked = false;
        } else if (!oUpdatedParentElement.isChecked && !oProperties.isPartialChecked && !bIsClassChildDisabled) {
          CS.remove(aInvalidRowIds, function (sId) {
            return sId === oProperties.id;
          });
        } else if (oUpdatedParentElement.isChecked || !oProperties.isPartialChecked) {
          if (oProperties.isInvalid) {
            aInvalidRowIds.push(oProperties.id);
          }
        }
        _updateDownloadDialogChildElementOnParentUpdate(oUpdatedParentElement, "isChecked", 1);
      } else {
        CS.forEach(oProperties.children, function (oChild) {
          if (oChild.id == sId) {
            oChild.isChecked = !oChild.isChecked;
            let bIsParentTIVChildDisabled = oChild.isTIVChildDisabled;
            if (oChild.isChecked && bIsParentTIVChildDisabled) {
              if (oChild.isPartialChecked) {
                oChild.isPartialChecked = !oChild.isChecked;
              } else {
                oChild.isPartialChecked = oChild.isChecked;
              }
              oChild.isChecked = false;
            }
            _updateDownloadDialogChildElementOnParentUpdate(oChild, "isChecked", 2);
          } else if (oChild.id == sClassId) {
            let oFoundChild = CS.find(oChild.children, {id: sId});
            oFoundChild.isChecked = !oFoundChild.isChecked;
          }
        });
      }
      _updateSelectedSizeAndCountOfParent(oUpdatedParentElement, aInvalidRowIds);
      oBulkAssetDownloadDialogProps.setActiveDownloadDialogData(oActiveDialogData);
      oBulkAssetDownloadDialogProps.setInvalidRowIds(aInvalidRowIds);
      _calculateTotalCountAndSize();
    }
  };

  let _calculateTotalCountAndSize = function () {
    let iTotalSelectedAssetsSize = 0;
    let iTotalSelectedAssetsCount = 0;
    let oBulkAssetDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let oActiveDialogData = oBulkAssetDownloadDialogProps.getActiveDownloadDialogData();
    let aActiveDialogData = oActiveDialogData.isDirty ? oActiveDialogData.clonedObject : oActiveDialogData.downloadModel;

    CS.forEach(aActiveDialogData, function (oParentElementData) {
      let oProperties = oParentElementData.properties;
      let bIsChecked = oParentElementData.isChecked;
      let bIsHalfChecked = oProperties.isPartialChecked;
      let bIsCheckboxDisabled = oProperties.isCheckboxDisabled;

      if (bIsChecked && !bIsCheckboxDisabled) {
        iTotalSelectedAssetsSize += oProperties.totalSize;
        iTotalSelectedAssetsCount += oProperties.totalCount;
      } else if (bIsHalfChecked) {
        CS.forEach(oProperties.children, function (oClassChild) {
          let bIsClassChecked = oClassChild.isChecked;
          let bIsClassHalfChecked = oClassChild.isPartialChecked;
          let bIsClassCheckboxDisabled = oClassChild.isCheckboxDisabled;

          if (bIsClassChecked && !bIsClassCheckboxDisabled) {
            iTotalSelectedAssetsSize += oClassChild.totalSize;
            iTotalSelectedAssetsCount += oClassChild.totalCount;
          } else if (bIsClassHalfChecked) {
            CS.forEach(oClassChild.children, function (oChild) {
              let bIsChildChecked = oChild.isChecked;
              let bIsDisabled = oChild.isDisabled;

              if (bIsChildChecked && !bIsDisabled) {
                iTotalSelectedAssetsSize += oChild.size;
                iTotalSelectedAssetsCount += 1;
              }
            });
          }
        });
      }
    });
    oBulkAssetDownloadDialogProps.setTotalSelectedAssetsCount(iTotalSelectedAssetsCount);
    oBulkAssetDownloadDialogProps.setTotalSelectedAssetsSize(iTotalSelectedAssetsSize < 0 ? 0 : iTotalSelectedAssetsSize);
  };

  /*After selecting/deselecting update selected asset count and size on parent level and based on count make parent
    partial checked, if child is invalid and checked then make parent invalid ;*/
  let _updateSelectedSizeAndCountOfParent = function (oUpdatedParentElement, aInvalidRowIds) {
    let iNumberOfCheckedClasses = 0;
    let iNumberOfUncheckedClasses = 0;
    let iTotalSelectedCount = 0;
    let iTotalSelectedSize = 0;
    let bIsParentInvalid = false;
    let oProperties = oUpdatedParentElement.properties;
    let iClassChildrenLength = oProperties.children.length;

    CS.forEach(oProperties.children, function (oClassChild) {
      let iNumberOfCheckedChild = 0;
      let iNumberOfUncheckedChild = 0;
      let iChildElementsLength = oClassChild.children.length;
      CS.forEach(oClassChild.children, function (oChildElement) {
        if (!oChildElement.isChecked) {
          iNumberOfUncheckedChild++;
          CS.remove(aInvalidRowIds, function (sId) {
            return sId === oChildElement.id;
          });
        } else {
          iTotalSelectedSize += oChildElement.size;
          iNumberOfCheckedChild++;
          if (oChildElement.isInvalid) {
            aInvalidRowIds.push(oChildElement.id);
            if (!CS.includes(aInvalidRowIds, oProperties.id)) {
              aInvalidRowIds.push(oProperties.id);
            }
          }
          if (!bIsParentInvalid) {
            bIsParentInvalid = oChildElement.isInvalid;
          }
        }
      });
      iTotalSelectedCount += iNumberOfCheckedChild;
      if (iNumberOfUncheckedChild == iChildElementsLength) {
        oClassChild.isChecked = false;
        oClassChild.isPartialChecked = false;
        iNumberOfUncheckedClasses++;
      } else if (iNumberOfCheckedChild == iChildElementsLength) {
        oClassChild.isChecked = true;
        oClassChild.isPartialChecked = false;
        iNumberOfCheckedClasses++;
      } else {
        oClassChild.isChecked = false;
        oClassChild.isPartialChecked = true;
      }
    });
    if (iNumberOfCheckedClasses == iClassChildrenLength) {
      oUpdatedParentElement.isChecked = true;
      oProperties.isPartialChecked = false;
    } else if (iNumberOfUncheckedClasses == iClassChildrenLength) {
      oUpdatedParentElement.isChecked = false;
      oProperties.isPartialChecked = false;
    } else {
      oUpdatedParentElement.isChecked = false;
      oProperties.isPartialChecked = true;
    }
    if (!bIsParentInvalid) {
      CS.remove(aInvalidRowIds, function (sId) {
        return sId === oProperties.id;
      });
    }
    oProperties.isInvalid = bIsParentInvalid;
    oProperties.selectedContentCount = iTotalSelectedCount;
    oProperties.selectedContentSize = iTotalSelectedSize;
  };

  //Expand parent and collapse previously active parent as only one parent can be expanded.
  let _handleBulkDownloadDialogExpandButtonClicked = function (sId, iIndex) {
    let oBulkAssetDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let iExpandedParentIndex = oBulkAssetDownloadDialogProps.getExpandedParentIndex();
    let oActiveDialogData = oBulkAssetDownloadDialogProps.getActiveDownloadDialogData();
    let aActiveDialogData = oActiveDialogData.isDirty ? oActiveDialogData.clonedObject : oActiveDialogData.downloadModel;
    let oUpdatedParentElement = aActiveDialogData[iIndex];
    let oProperties = oUpdatedParentElement.properties;

    if (oProperties.id == sId) {
      oProperties.isExpanded = !oProperties.isExpanded;
      if (iExpandedParentIndex >= 0 && iExpandedParentIndex !== iIndex) {
        if (oProperties.isExpanded) {
          let oFoundExpandedParentElement = aActiveDialogData[iExpandedParentIndex];
          let oParentProperties = oFoundExpandedParentElement.properties;
          oParentProperties.isExpanded = false;
          oBulkAssetDownloadDialogProps.setExpandedParentIndex(iIndex);
        }
      } else if (iExpandedParentIndex >= 0 && iExpandedParentIndex === iIndex) {
        !oProperties.isExpanded && oBulkAssetDownloadDialogProps.setExpandedParentIndex(-1);
      } else {
        oBulkAssetDownloadDialogProps.setExpandedParentIndex(iIndex);
      }
    } else {
      let aClassChildren = oProperties.children;
      let iLength = aClassChildren.length;
      let oFoundChild = CS.find(aClassChildren, {id: sId});
      let iIndex = aClassChildren.indexOf(oFoundChild);
      oFoundChild.isExpanded = !oFoundChild.isExpanded;
      if (oFoundChild.isExpanded) {
        if (oFoundChild.hasOwnProperty('showBorderBottom')) {
          oFoundChild.showBorderBottom = false;
        }
      } else {
        if (iIndex + 1 !== iLength) {
          oFoundChild.showBorderBottom = true;
        }
      }
    }
  };

  //Reset all props on cancel action.
  let _handleBulkDownloadDialogCancelButtonClicked = function () {
    let oBulkDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    oBulkDownloadDialogProps.reset();
  };

  /*Shift to asset instance or file name based on toggle button position.
    If new name is invalid then push its id in invalidRowIds array */
  let _updateValuesBasedOnToggleButton = function (oActiveDialogData) {
    let aInvalidRowIds = [];
    let sActiveContext = oActiveDialogData.context;
    let oBulkDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let oIdVsNameObject = oBulkDownloadDialogProps.getIdVsNameModel();
    let sNameToPick = sActiveContext === "assetFileName" ? "assetInstanceName" : "assetFileName";
    let aActiveDialogData = oActiveDialogData.isDirty ? oActiveDialogData.clonedObject : oActiveDialogData.downloadModel;

    CS.forEach(aActiveDialogData, function (oParentElement) {
      let oProperties = oParentElement.properties;
      let oIdVsNameModel = oIdVsNameObject[oProperties.id];
      let sNewParentLabel = "";
      let bIsParentInvalid = false;
      let sParenInvalidMessage = "";
      let oTIVNameWithValidationModel = {};

      CS.forEach(oProperties.children, function (oClassChild) {
        let sClassId = oClassChild.id;
        if (sClassId === getTranslation().MAIN_ASSET) {
          let oNameWithValidationModel = _getAssetNameWithValidation(oIdVsNameModel, sNameToPick, aInvalidRowIds,
                                            oClassChild.children[0].id, oClassChild.children[0].isChecked);
          sNewParentLabel = oNameWithValidationModel.name;
          oClassChild.children[0].label = sNewParentLabel;
          if (!bIsParentInvalid) {
            bIsParentInvalid = oNameWithValidationModel.isInvalid;
            sParenInvalidMessage = oNameWithValidationModel.invalidMessage;
          }
          oClassChild.children[0].isInvalid = bIsParentInvalid;
          oClassChild.children[0].invalidMessage = sParenInvalidMessage;
        } else {
          CS.forEach(oClassChild.children, function (oTIVChild) {
            let oTIVChildIdVsNameModel = oIdVsNameObject[oTIVChild.id];
            oTIVNameWithValidationModel = _getAssetNameWithValidation(oTIVChildIdVsNameModel, sNameToPick, aInvalidRowIds,
                                              oTIVChild.id, true, oTIVChild.isChecked);
            oTIVChild.label = oTIVNameWithValidationModel.name;

            if (!bIsParentInvalid) {
              bIsParentInvalid = oTIVNameWithValidationModel.isInvalid;
              sParenInvalidMessage = oTIVNameWithValidationModel.invalidMessage;
            }
            if (oTIVNameWithValidationModel.isInvalid && oTIVChild.isChecked) {
              if (!CS.includes(aInvalidRowIds, oProperties.id)) {
                aInvalidRowIds.push(oProperties.id);
              }
            }

            oTIVChild.isInvalid = oTIVNameWithValidationModel.isInvalid;
            oTIVChild.invalidMessage = oTIVNameWithValidationModel.invalidMessage;
          });
        }
      });
      oParentElement.label = sNewParentLabel;
      oProperties.isInvalid = bIsParentInvalid;
      oProperties.invalidMessage = sParenInvalidMessage;
    });

    if (oActiveDialogData.isDirty) {
      oActiveDialogData.isDirty = false;
      delete oActiveDialogData.clonedObject;
    }
    oActiveDialogData.context = sNameToPick;
    oActiveDialogData.downloadModel = aActiveDialogData;
    let bIsDownloadAsInvalid = CS.includes(oBulkDownloadDialogProps.getInvalidRowIds(), "downloadAs");
    bIsDownloadAsInvalid && aInvalidRowIds.push("downloadAs");
    oBulkDownloadDialogProps.setInvalidRowIds(aInvalidRowIds);
    oBulkDownloadDialogProps.setActiveDownloadDialogData(oActiveDialogData);
  };

  /* Check if download file name contains invalid value den make it invalid and push downloadAS as id in invalidRowIds
   array. */
  let _handleBulkDownloadDialogFixedSectionValueChanged = function (sButtonId, oData) {
    let oBulkDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let oActiveDialogData = oBulkDownloadDialogProps.getActiveDownloadDialogData();
    let bIsToggleButtonOn = oBulkDownloadDialogProps.getIsDownloadDialogToggleButtonOn();

    switch (sButtonId) {
      case "toggle":
        _updateValuesBasedOnToggleButton(oActiveDialogData);
        oBulkDownloadDialogProps.setIsDownloadDialogToggleButtonOn(!bIsToggleButtonOn);
        break;
      case "text":
        let sUpdatedDialogId = oData.id;
        let aInvalidRowIds = oBulkDownloadDialogProps.getInvalidRowIds();
        switch (sUpdatedDialogId) {
          case "download":
            let sDownloadFileName = oData.downloadFileName.trim();
            let oDownloadAsExtraData = {
              "downloadAsErrorMessage": "",
              "isDownloadAsError": false
            };
            let bIsDownloadAsInvalid = CS.includes(aInvalidRowIds, "downloadAs");
            if (CS.isEmpty(sDownloadFileName)) {
              bIsDownloadAsInvalid || aInvalidRowIds.push("downloadAs");
              oDownloadAsExtraData.downloadFileNameErrorMsg = getTranslation().FILENAME_CANNOT_BE_BLANK;
              oDownloadAsExtraData.isDownloadAsError = true;
            } else if (sDownloadFileName.match(sFileNameRegex)) {
              bIsDownloadAsInvalid || aInvalidRowIds.push("downloadAs");
              oDownloadAsExtraData.downloadFileNameErrorMsg = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME + " \\ / : * ? \" < > | \"";
              oDownloadAsExtraData.isDownloadAsError = true;
            } else {
              CS.remove(aInvalidRowIds, function (sAssetId) {
                return sAssetId === "downloadAs";
              });
            }
            oBulkDownloadDialogProps.setInvalidRowIds(aInvalidRowIds);
            oBulkDownloadDialogProps.setDownloadFileName(sDownloadFileName);
            oBulkDownloadDialogProps.setDownloadAsExtraData(oDownloadAsExtraData);
            break;
          case "comment":
            oBulkDownloadDialogProps.setDownloadComments(oData.downloadComments.trim());
            break;
        }
        break;
    }
  };

  /*Check for invalid value entered in TIV class child.
    Make parent and TIV child instance as invalid */
  let _handleBulkDownloadDialogChildElementValueChanged = function (sId, sValue, sClassId, iParentIndex) {
    sValue = sValue.trim();
    let oBulkDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let oActiveDialogData = oBulkDownloadDialogProps.getActiveDownloadDialogData();
    let aInvalidRowIds = oBulkDownloadDialogProps.getInvalidRowIds();
    if (!oActiveDialogData.isDirty) {
      oActiveDialogData.isDirty = true;
      oActiveDialogData.clonedObject = CS.cloneDeep(oActiveDialogData.downloadModel);
    }
    let aClonedObject = oActiveDialogData.clonedObject;
    let oActiveAssetData = aClonedObject[iParentIndex];
    let oProperties = oActiveAssetData.properties;
    let aClassChildren = oProperties.children;
    let oActiveClassChild = CS.find(aClassChildren, {id: sClassId});
    let oActiveTIVChild = CS.find(oActiveClassChild.children, {id: sId});
    oActiveTIVChild.label = sValue;
    //Delete invalid message and isInvalid flag only if new value is not empty and does not contain any invalid characters.
    if (oActiveTIVChild.hasOwnProperty('isInvalid') && oActiveTIVChild.isInvalid
        && !sValue.match(sFileNameRegex) && !CS.isEmpty(sValue)) {
      delete oActiveTIVChild.isInvalid;
      delete oActiveTIVChild.invalidMessage;
      delete oProperties.isInvalid;
      delete oProperties.invalidMessage;
    }
    if (CS.isEmpty(sValue)){
      if (!CS.includes(aInvalidRowIds, sId)) {
        aInvalidRowIds.push(sId);
      }
      if (!CS.includes(aInvalidRowIds, oProperties.id)) {
        aInvalidRowIds.push(oProperties.id);
      }
      oActiveTIVChild.isInvalid = true;
      oActiveTIVChild.invalidMessage = getTranslation().FILENAME_CANNOT_BE_BLANK;
      oProperties.isInvalid = true;
      oProperties.invalidMessage = getTranslation().FILENAME_CANNOT_BE_BLANK;
    } else if (sValue.match(sFileNameRegex)) {
      if (!CS.includes(aInvalidRowIds, sId)) {
        aInvalidRowIds.push(sId);
      }
      if (!CS.includes(aInvalidRowIds, oProperties.id)) {
        aInvalidRowIds.push(oProperties.id);
      }
      oActiveTIVChild.isInvalid = true;
      oActiveTIVChild.invalidMessage = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME + " \\ / : * ? \" < > | \"";
      oProperties.isInvalid = true;
      oProperties.invalidMessage = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME + " \\ / : * ? \" < > | \"";
    } else {
      CS.remove(aInvalidRowIds, function (sAssetId) {
        return sAssetId === sId;
      });
    }
    if (!_checkForInvalidTIVChild(aClassChildren)) {
      CS.remove(aInvalidRowIds, function (sId) {
        return oProperties.id === sId;
      });
    }
    oBulkDownloadDialogProps.setInvalidRowIds(aInvalidRowIds);
    oBulkDownloadDialogProps.setActiveDownloadDialogData(oActiveDialogData);
  };

  let _checkForInvalidTIVChild = function (aClassChildren) {
    let bIsParentInvalid = false;
    CS.forEach(aClassChildren, function (oTIVChild) {
      CS.forEach(oTIVChild.children, function (oTIVChild) {
        if (oTIVChild.isInvalid) {
          bIsParentInvalid = true;
        }
      })
    });

    return bIsParentInvalid;
  };

  //Process downloadInfo based on checked elements on dialog.
  let _handleBulkDownloadDialogDownloadButtonClicked = function () {
    let oBulkDownloadDialogProps = ContentScreenProps.bulkDownloadAssetProps;
    let aOriginalDownloadData = oBulkDownloadDialogProps.getDownloadInfo();
    let oProcessedDownloadData = oBulkDownloadDialogProps.getActiveDownloadDialogData();
    let aActiveDialogData = oProcessedDownloadData.isDirty ? oProcessedDownloadData.clonedObject : oProcessedDownloadData.downloadModel;

    CS.forEach(aActiveDialogData, function (oActiveParentElement) {
      let oActiveParentProperties = oActiveParentElement.properties;
      let bIsParentChecked = oActiveParentElement.isChecked;
      let bIsParentCheckboxDisabled = oActiveParentProperties.isCheckboxDisabled;
      let bIsActiveParentPartialChecked = oActiveParentProperties.isPartialChecked;

      if ((!bIsParentChecked && !bIsActiveParentPartialChecked) || bIsParentCheckboxDisabled) {
        CS.remove(aOriginalDownloadData, function (oOriginalInstanceData) {
          return oOriginalInstanceData.assetInstanceId === oActiveParentProperties.id;
        });
      } else {
        CS.forEach(oActiveParentProperties.children, function (oClassChild) {
          let oOriginalParentData = CS.find(aOriginalDownloadData, {assetInstanceId: oActiveParentProperties.id});
          let sClassId = oClassChild.id;
          let oOriginalParentTIVInfo = oOriginalParentData.tivdownloadInformation;
          let aClassChildrenList = oOriginalParentTIVInfo[sClassId];
          let bIsClassChildChecked = oClassChild.isChecked;
          let bIsPartialChecked = oClassChild.isPartialChecked;
          let bIsClassCheckboxDisabled = oClassChild.isCheckboxDisabled;
          if ((!bIsClassChildChecked && !bIsPartialChecked && oClassChild.id !== getTranslation().MAIN_ASSET) || bIsClassCheckboxDisabled) {
            delete oOriginalParentTIVInfo[sClassId];
          } else {
            if (oClassChild.id === getTranslation().MAIN_ASSET) {
              if (bIsClassChildChecked) {
                oOriginalParentData.fileNameToDownload = oClassChild.children[0].label;
              } else {
                oOriginalParentData.assetObjectKey = "";
              }
            } else {
              CS.forEach(oClassChild.children, function (oTIVChild) {
                let bIsTIVChecked = oTIVChild.isChecked;
                let bIsDisabled = oTIVChild.isDisabled;
                let oOriginalTIVChild = CS.find(aClassChildrenList, {assetInstanceId: oTIVChild.id});
                if (!bIsTIVChecked || bIsDisabled) {
                  CS.remove(aClassChildrenList, function (oOriginalTIVChild) {
                    return oOriginalTIVChild.assetInstanceId === oTIVChild.id;
                  });
                } else {
                  oOriginalTIVChild.fileNameToDownload = oTIVChild.label;
                }
              });
            }
          }
        });
      }
    });

    let sSelectedTabId = ContentUtils.getSelectedTabId();
    let oDownloadRange = ContentScreenProps.assetDownloadTrackerProps.getDownloadRange();
    let bTrackDownloads = ContentScreenProps.screen.getActiveContentClass().trackDownloads;

    let oRequestData = {
      downloadInformation: aOriginalDownloadData,
      downloadFileName: oBulkDownloadDialogProps.getDownloadFileName(),
      comments: oBulkDownloadDialogProps.getDownloadComments(),
      shouldCreateSeparateFolders: oBulkDownloadDialogProps.getIsFolderByAsset(),
      isDownloadedFromInstance: (sSelectedTabId == "overview_tab"),
      downloadWithinTimeRange: oDownloadRange,
      shouldTrackDownloads: bTrackDownloads
    };

    CS.postRequest(ContentScreenRequestMapping.BulkDownload, {}, oRequestData, successBulkAssetsDownloadCallback, failureBulkAssetsDownloadCallback);
  };

  let successBulkAssetsDownloadCallback = function (oResponse) {
    ContentScreenProps.bulkDownloadAssetProps.reset();
    if (!CS.isEmpty(oResponse.success)) {
      let oResponseData = oResponse.success;
      let sId = oResponseData.id;
      let sHref = RequestMapping.getRequestUrl(ContentScreenRequestMapping.BulkDownloadSrc, {
        id: sId
      });
      let oPopUp = window.open(sHref);
      if (oPopUp === null || typeof oPopUp === "undefined") {
        alertify.error(getTranslation().PLEASE_DISABLE_POP_UP_BLOCKER);
      }
      let oActiveEntity = ContentUtils.getActiveEntity();
      if (CS.isNotEmpty(oActiveEntity)) {
        let oDownloadTrackerProps = ContentScreenProps.assetDownloadTrackerProps;
        oDownloadTrackerProps.setDownloadCountWithinRange(oResponseData.downloadCountWithinRange);
        oDownloadTrackerProps.setTotalDownloadCount(oResponseData.totalDownloadCount);
      }
    }
    _triggerChange();
  };

  let failureBulkAssetsDownloadCallback = function (oResponse) {
    ContentScreenProps.bulkDownloadAssetProps.reset();
    ContentUtils.failureCallback(oResponse, 'failureBulkAssetsDownloadCallback', getTranslation());
  };

  //--------------------------------------------  Public API's  -----------------------------------------------------//

  return {
    preProcessDownloadDialogDataForView: function(bShouldDownloadAssetWithOriginalFilename) {
      _preProcessDownloadDialogDataForView(bShouldDownloadAssetWithOriginalFilename);
    },
    handleBulkDownloadDialogCheckboxClicked: function (sId, iIndex, sClassId) {
      _handleBulkDownloadDialogCheckboxClicked(sId, iIndex, sClassId);
      _triggerChange();
    },
    handleBulkDownloadDialogExpandButtonClicked: function (sId, iIndex) {
      _handleBulkDownloadDialogExpandButtonClicked(sId, iIndex);
      _triggerChange();
    },
    handleBulkDownloadDialogCancelButtonClicked: function () {
      _handleBulkDownloadDialogCancelButtonClicked();
      _triggerChange();
    },
    handleBulkDownloadDialogFixedSectionValueChanged: function (sButtonId, oData) {
      _handleBulkDownloadDialogFixedSectionValueChanged(sButtonId, oData);
      _triggerChange();
    },
    handleBulkDownloadDialogChildElementValueChanged: function (sId, sValue, sClassId, iParentIndex) {
      _handleBulkDownloadDialogChildElementValueChanged(sId, sValue, sClassId, iParentIndex);
      _triggerChange();
    },
    handleBulkDownloadDialogDownloadButtonClicked: function () {
      _handleBulkDownloadDialogDownloadButtonClicked();
    }
  }


})();

MicroEvent.mixin(BulkAssetDownloadStore);

export default BulkAssetDownloadStore;



