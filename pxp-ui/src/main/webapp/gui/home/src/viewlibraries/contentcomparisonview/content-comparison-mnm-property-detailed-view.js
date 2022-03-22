import CS from '../../libraries/cs';
import React, {Component} from 'react';
import {withStyles} from '@material-ui/core/styles';
import PropTypes from 'prop-types';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import ContentComparisonMNMPropertyItemViewObj from './content-comparison-mnm-property-item-view';
import {view as NothingFoundViewObj} from '../../viewlibraries/nothingfoundview/nothing-found-view';
import {getTranslations} from '../../commonmodule/store/helper/translation-manager.js';
import GRConstants from '../../screens/homescreen/screens/contentscreen/tack/golden-record-view-constants';

const ContentComparisonMNMPropertyItemView = ContentComparisonMNMPropertyItemViewObj.view;
const NothingFoundView = NothingFoundViewObj;


const styles = theme => ({
  radioRoot: {
     padding: "0",
    "alignItems": "start",
    width: "24px",
    height: "24px"
  }
});

const oEvents = {};

class ContentComparisonMnmPropertyDetailedView extends Component {

  handlePropertyValueClicked = (oElement) => {
    let oProperty = oElement.property;
    let oPropertyToSave = {};
    if (oProperty.propertyType === "tag") {
      let oTagGroupModel = oProperty.tagGroupModel.tagGroupModel;
      oPropertyToSave = {
        tagId: oTagGroupModel.tagId,
        tagValueRelevanceData: oTagGroupModel.entityTag.tagValues
      };
    } else if (oProperty.propertyType === "attribute") {
      oPropertyToSave.valueAsHtml = oProperty.valueAsHtml;
      oPropertyToSave.value = oProperty.value;
    } else if (oProperty.propertyType === "relationship" || oProperty.propertyType === "natureRelationship") {
      oPropertyToSave.sourceKlassInstanceId = oProperty.sourceKlassInstanceId;
      oPropertyToSave.referencedAssetsData = oProperty.referencedAssetsData;
      oPropertyToSave.paginationData = oProperty.paginationData;
    }
    this.props.onChangeHandler(oPropertyToSave);
  };

  getRightSectionPropertyLabel = (sLabel) => {
    return (
        <div className={"propertyHeader"}>
          {(sLabel || "").toUpperCase()}
        </div>
    );
  };

  getPropertyValue = (oElement) => {
    let oProperty = oElement.property;
    if (oElement.isValueEmpty) {
      return <div className="valueNotPresent">{getTranslations().NO_VALUE}</div>
    }
    let sClassName = "propertyValue";

    return (
        <div className={sClassName}>
          <ContentComparisonMNMPropertyItemView
              oElement={oProperty}
              sViewContext={GRConstants.PROPERTY_DETAIL_VIEW}
              sTableId={this.props.selectedTableId}/>
        </div>
    );
  };

  getSourcesDetails = (aRecordLabels) => {
    let aViews = [];
    CS.forEach(aRecordLabels, function (oRecordLabel) {
      let sSubLabel = CS.isEmpty(oRecordLabel.subLabel) ? "" : " - " + oRecordLabel.subLabel;
      aViews.push(
        <div className="sourceInfo">
          {/*<div className="organizationName">{}</div>*/}
          <div className="sourceName">{oRecordLabel.label + sSubLabel}</div>
        </div>
      );
    });
    return aViews;
  };

  getPropertyDetailedView = (oPropertyDetailedData) => {
    let _this = this;
    let {classes, emptyElementsMsgKey} = _this.props;
    let aView = [];
    if(CS.isEmpty(oPropertyDetailedData.elements)) {
      return (
          <NothingFoundView
              message={getTranslations()[emptyElementsMsgKey]}
          />
      )
    }

    CS.forEach(oPropertyDetailedData.elements, function (oElement, sKey) {

      let oRadioButtonView = oElement.suppressSelection
                             ? <div className="suppressSelection"/>
                             : <Radio checked={oElement.isChecked || false}
                                      color="primary"
                                      classes={{root: classes.radioRoot}}
                                      onChange={_this.handlePropertyValueClicked.bind(this, oElement)}/>
      aView.push(
          <div className={oElement.suppressSelection ? "suppressPropertyDetailedViewWrapper" : "propertyDetailedViewWrapper"}>
            <div className={"propertyValueWrapper"}>
              {oRadioButtonView}
              {_this.getPropertyValue(oElement)}
            </div>
            {<div className={"propertyDetailedViewSourceDetail"}>{_this.getSourcesDetails(oElement.recordLabels)}</div>}
          </div>
      );
    });

    return (
        <div className={"contentComparisonMatchAndMergePropertyDetailedView"}>
            <RadioGroup
                aria-label="contentComparisonDataSelection"
                name="contentComparisonData"
                value={""}>
              {aView}
            </RadioGroup>
        </div>
    );
  };


  render () {
    let {propertyDetailedData: oPropertyDetailedData, selectedPropertyId: sSelectedPropertyId} = this.props;
    let oSelectedPropertyDetailedData = oPropertyDetailedData[sSelectedPropertyId];
    return (
        <div className={"contentComparisonMatchAndMergePropertyDetailedViewWrapper"}>
          {this.getRightSectionPropertyLabel(oSelectedPropertyDetailedData.label)}
          {this.getPropertyDetailedView(oSelectedPropertyDetailedData)}
        </div>
    );
  }
}

ContentComparisonMnmPropertyDetailedView.propTypes = {
  propertyDetailedData: PropTypes.object,
  selectedPropertyId: PropTypes.string,
  context: PropTypes.string, //TODO: #Refact20 unused
  selectedTableId: PropTypes.string,
  onChangeHandler: PropTypes.func,
};

export default withStyles(styles)(ContentComparisonMnmPropertyDetailedView);
export const events = oEvents;
