import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ChipsView } from './../chipsView/chips-view.js';
import { view as MatchAndMergeAttributeCellView } from './match-and-merge-attribute-cell-view';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import { view as MediaPreviewDialogView } from '../mediapreviewdialogview/media-preview-dialog-view';

const oEvents = {
  MATCH_MERGE_CELL_VIEW_CELL_CLICKED: "match_merge_cell_view_cell_clicked"
};

const oPropTypes = {
  property: ReactPropTypes.object,
  comparisonProperty: ReactPropTypes.object,
  rendererType: ReactPropTypes.string,
  rowData: ReactPropTypes.object,
  tableId: ReactPropTypes.string,
  tableGroupName: ReactPropTypes.string,
  isRowDisabled: ReactPropTypes.bool,
  isInstancesComparisonView: ReactPropTypes.bool,
  hideComparison:ReactPropTypes.bool,
  isGoldenRecordComparison: ReactPropTypes.bool
};
/**
 * @class MatchAndMergeCommonCellView - Use to display cell view of comparision table in product view.
 * @memberOf Views
 * @property {object} [property] - Pass properties like isDisabled, value, visibleValues.
 * @property {object} [comparisonProperty] - Pass properties of comparison properties.
 * @property {string} [rendererType] - Pass type of matchandmerge view.
 * @property {object} [rowData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {string} [tableId] - Pass Table Id.
 * @property {string} [tableGroupName] - Pass table Group Name.
 * @property {bool} [isRowDisabled] - Pass boolean for rowdisabled if true else enable.
 * @property {bool} [isInstancesComparisonView] - Pass boolean for if true for instance comparison else false.
 * @property {bool} [hideComparison] - Pass boolean for hide comparison.
 * @property {bool} [isGoldenRecordComparison] - Pass boolean for is this golden record comparison or not.
 */

// @CS.SafeComponent
class MatchAndMergeCommonCellView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      isPopupOpen: false,
      imageData: {},
    }
  }

  handleCellClicked =()=> {
    let oComparisonProperty = this.props.comparisonProperty;
    let oRowData = this.props.rowData;
    let sRowId = oRowData.id;
    let oProperty = this.props.property;
    let sTableId = this.props.tableId;
    let sTableGroupName = this.props.tableGroupName;

    if (!CS.isEmpty(oComparisonProperty) && !this.props.isRowDisabled && !this.props.isInstancesComparisonView) {
      if (!CS.isEqual(oComparisonProperty.value, oProperty.value)) {
        EventBus.dispatch(oEvents.MATCH_MERGE_CELL_VIEW_CELL_CLICKED, sRowId, oProperty, sTableId, sTableGroupName, this.props.isGoldenRecordComparison);
      }
    }
  }

  handleCellOnClick =(oImageData, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    if (!CS.isEmpty(oImageData.assetObjectKey)) {
      this.setState({
        isPopupOpen: true,
        imageData: oImageData
      });
    }
  }

  closeMediaPreviewPopView =()=> {
    this.setState({
      isPopupOpen: false,
      imageData: {}
    });
  }

  render() {

    let oProperty = this.props.property;
    let oComparisonProperty = this.props.comparisonProperty || {};
    let aView = [];
    let bIsEqual = false; //will indicate that this cell's value is the same as the current version's value
    let oImageData = null;
    var sChipsWrapperClassName = this.props.isInstancesComparisonView ? "chipsWrapper instancesComparison" : "chipsWrapper";

    let sTitle = this.props.title;
    let bShowPreview = this.state.isPopupOpen;
    let oStateImageData = this.state.imageData;

    switch (this.props.rendererType) {

      case "taxonomy":
        if (!CS.isEmpty(oComparisonProperty)) {
          let aSelectedTaxonomyIds = oProperty.value;
          let aCurrentVersionTaxonomyIds = oComparisonProperty.value || [];
          let aTaxonomies = CS.xor(aSelectedTaxonomyIds, aCurrentVersionTaxonomyIds);
          if (CS.isEmpty(aTaxonomies)) {
            bIsEqual = true;
          }
        }

        CS.forEach(oProperty.visibleValues, function (aValue, iIndex) {
          aView.push(
              <div className={sChipsWrapperClassName} key={iIndex}>
                <ChipsView items={aValue}/>
              </div>
          );
        });
        break;

      case "type":
        let aSelectedKlassIds = oProperty.value;
        if(!CS.isEmpty(oComparisonProperty)) {
          let aCurrentVersionKlassIds = oComparisonProperty.value || [];
          let aKlasses = CS.xor(aSelectedKlassIds, aCurrentVersionKlassIds);
          if(CS.isEmpty(aKlasses)) {
            bIsEqual = true;
          }
        }
        CS.forEach(oProperty.visibleValues, function (aValue, iIndex) {
          aView.push(
              <div className={sChipsWrapperClassName} key={iIndex}>
                <ChipsView items={aValue}/>
              </div>
          );
        });
        break;

      case "name":
        let oRow = this.props.rowData;
        let sName = oProperty.value;
        if(!CS.isEmpty(oComparisonProperty)) {
          let sCurrentVersionName = oComparisonProperty.value || null;
          if(CS.isEqual(sName, sCurrentVersionName)) {
            bIsEqual = true;
          }
        }
        aView.push(

            <MatchAndMergeAttributeCellView
                  key={oRow.id}
                  property={oProperty}
                  comparisonProperty={oComparisonProperty}
                  rendererType={this.props.rendererType}
                  masterAttribute={oProperty.nameAttribute}
                  rowId={oRow.id}
                  tableId={this.props.tableId}
                  isRowDisabled={this.props.isRowDisabled}
                  tableGroupName={this.props.tableGroupName}
                  isInstancesComparisonView={this.props.isInstancesComparisonView}
              />
        );
        break;

      case "lifeCycleStatusTag":
        let aSelectedStatusTagIds = oProperty.selectedStatusTagIds;
        if(!CS.isEmpty(oComparisonProperty)) {
          let aCurrentVersionSelectedStatusTagIds = oComparisonProperty.selectedStatusTagIds || [];
          let aTags = CS.xor(aSelectedStatusTagIds, aCurrentVersionSelectedStatusTagIds);
          if(CS.isEmpty(aTags)) {
            bIsEqual = true;
          }
        }

        aView.push(
            <div className={sChipsWrapperClassName} key={sChipsWrapperClassName}>
              <ChipsView items={oProperty.values}/>
            </div>);
        break;

      case "image":
        let sDefaultAssetInstanceId = oProperty.value;
        if(!CS.isEmpty(oComparisonProperty)) {
          let sCurrentDefaultAssetInstanceId = oComparisonProperty.value || null;
          if(CS.isEqual(sDefaultAssetInstanceId, sCurrentDefaultAssetInstanceId)) {
            bIsEqual = true;
          }
        }

        if(!CS.isEmpty(oProperty.value)) {
          var oReferencedAssets = oProperty.referencedAssetsData;
          oImageData = oReferencedAssets[oProperty.value];

          let sExtension = oImageData.extension;
          let sImageViewClassName = "mnmrImageContainer";
          let oImageView = null;

          if (ViewLibraryUtils.isXlsOrXlsxFile(sExtension)) {
            sImageViewClassName += " xlsIcon";
          } else if (ViewLibraryUtils.isObjStpFbxFile(sExtension)) {
            sImageViewClassName += sExtension=== "obj" && " objIcon";
            sImageViewClassName += sExtension=== "stp" && " stpIcon";
            sImageViewClassName += sExtension=== "fbx" && " fbxIcon";
          } else {
            oImageView = <ImageSimpleView imageSrc={oImageData.thumbKeySrc} classLabel="mnmrIcon"/>;
          }
          aView.push(
              <div className={sImageViewClassName} onClick={this.handleCellOnClick.bind(this, oImageData)} key={sImageViewClassName}>
                {oImageView}
              </div>
          );
        }
        break;

      case "eventSchedule":

        let oRepeatType = [];
        if (!CS.isEmpty(oProperty.value)) {
          CS.forEach(oProperty.value, function (sValue, sKey) {
              oRepeatType.push(<div className="creationSubDetails" key={sKey}><span className="normal">{sKey}</span><span className="highlighted">{sValue}</span></div>);
          });
        }

        return (
            <div className="creationDetails">
                {oRepeatType}
            </div>
        );
    }

    let oIsEqualIndicator = null;
    let bHideComparison = this.props.hideComparison;
    if(bIsEqual && !bHideComparison) {
      oIsEqualIndicator = (<div className="equalCell"></div>);
    }


    let oMediaPreviewDialogView = (<MediaPreviewDialogView mediaData={oStateImageData}
                               showPreview={bShowPreview}
                               title={sTitle}
                               onClose={this.closeMediaPreviewPopView}/>);

    var sClassName = "matchAndMergeCellView ";
    sClassName += this.props.rendererType;
    sClassName = !CS.isEmpty(oComparisonProperty) && oComparisonProperty.isRecommended ? sClassName + " isRecommended" : sClassName;

    return (
        <div className={sClassName}>
          <div onClick={this.handleCellClicked}>
            {oMediaPreviewDialogView}
            {oIsEqualIndicator}
            {aView}
          </div>
        </div>
    );
  }

}

MatchAndMergeCommonCellView.propTypes = oPropTypes;


export const view = MatchAndMergeCommonCellView;
export const events = oEvents;
