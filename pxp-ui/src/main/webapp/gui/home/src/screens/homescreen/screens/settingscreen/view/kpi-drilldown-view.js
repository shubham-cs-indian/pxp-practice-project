import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as LazyMSSView } from './../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  HANDLE_ADD_NEW_KPI_DRILLDOWN_LEVEL_CLICKED: "handle_add_new_kpi_drilldown_level_clicked",
  HANDLE_REMOVE_KPI_DRILLDOWN_LEVEL_CLICKED: "handle_remove_kpi_drilldown_level_clicked",
  HANDLE_KPI_DRILLDOWN_LEVEL_MSS_VALUE_CHANGED: "handle_kpi_drilldown_level_mss_value_changed"
};

const oPropTypes = {
  drillDowns: ReactPropTypes.array,
  tagMap: ReactPropTypes.object,
  selectedDrillDownTags: ReactPropTypes.array,
  selectedTaxonomyLabels: ReactPropTypes.array,
  referencedTags: ReactPropTypes.object,
};

// @CS.SafeComponent
class KPIDrilldownView extends React.Component {

  constructor (props) {
    super(props);

    this.getLevelView = this.getLevelView.bind(this);
  }

  static propTypes = oPropTypes;

  handleAddLevel = (sLevelType) => {
    EventBus.dispatch(oEvents.HANDLE_ADD_NEW_KPI_DRILLDOWN_LEVEL_CLICKED, sLevelType);
  };

  handleRemoveLevel = (sDrilldownLevelId) => {
    EventBus.dispatch(oEvents.HANDLE_REMOVE_KPI_DRILLDOWN_LEVEL_CLICKED, sDrilldownLevelId);
  };

  handleTagSelection = (sDrilldownLevelId, aSelectedItems, oReferencedTag) => {
    EventBus.dispatch(oEvents.HANDLE_KPI_DRILLDOWN_LEVEL_MSS_VALUE_CHANGED, sDrilldownLevelId, aSelectedItems, oReferencedTag);
  };

  getLazyMSSView = (oModel, fApplyHandler) => {
    var oAnchorOrigin = this.props.anchorOrigin || {horizontal: 'left', vertical: 'top'};
    var oTargetOrigin = this.props.targetOrigin || {horizontal: 'left', vertical: 'bottom'};
    return (
        <LazyMSSView {...oModel} anchorOrigin={oAnchorOrigin} targetOrigin={oTargetOrigin} onApply={fApplyHandler}/>
    )
  };

  getTaxonomyLabelView = (sTaxonomyLabel, iLevelCount) => {
    let sTaxonomyLabelToShow = sTaxonomyLabel+"  "+getTranslation().LEVEL+"-"+iLevelCount;
    return (
        <div className="kpiDrillDownTaxonomyLabel" title={sTaxonomyLabelToShow} key={iLevelCount}>
          {sTaxonomyLabelToShow}
        </div>
    );
  };

  getLevelView = () => {
    let _this = this;
    let __props = _this.props;
    let aTagMap = __props.tagMap;
    let aSelectedDrillDownTags = __props.selectedDrillDownTags;
    let aSelectedTaxonomyLabels = __props.selectedTaxonomyLabels;
    let oReferencedTags = __props.referencedTags;
    let aViews = [];
    let iLevelCount = 0;
    let oMSSProcessRequestResponseObj = {
      requestType: "configData",
      entityName: "tags",
    };

    CS.forEach(__props.drillDowns, function (oDrillDownLevel, iIndex) {

      let oSelectionView = null;
      let aTaxonomyLabelsView = null;
      if (oDrillDownLevel.type === "tag") {
        let fOnApply = _this.handleTagSelection.bind(_this, oDrillDownLevel.id);
        let oTagLevelLazyMSSModel = ViewUtils.getLazyMSSViewModel(oDrillDownLevel.typeIds, oReferencedTags, "", oMSSProcessRequestResponseObj, false, aSelectedDrillDownTags);
        oTagLevelLazyMSSModel.menuListHeight = "500px";
        oSelectionView = _this.getLazyMSSView(oTagLevelLazyMSSModel, fOnApply);
      } else if (oDrillDownLevel.type === "taxonomy") {
        aTaxonomyLabelsView = [];
        ++iLevelCount;
        CS.forEach(aSelectedTaxonomyLabels, function (sLabel) {
          aTaxonomyLabelsView.push(_this.getTaxonomyLabelView(sLabel, iLevelCount));
        });
      }

      aViews.push(
          <div className="drillDownLevelRow" key={oDrillDownLevel.id}>
            <div className="drillDownLevelLabel">{getTranslation()["LEVEL"] + " " + (iIndex + 1)}</div>
            <div className="drillDownLevelTypeContainer">{getTranslation()[CS.upperCase(oDrillDownLevel.type)]}</div>
            <div className="drillDownMSSContainer">{oSelectionView}</div>
            <div className="drillTaxonomyLabelsContainer">{aTaxonomyLabelsView}</div>
            <TooltipView label={getTranslation()["REMOVE"]}>
              <div className="drillDownRemoveLevel" onClick={_this.handleRemoveLevel.bind(_this, oDrillDownLevel.id)}></div>
            </TooltipView>
          </div>
      );
    });

    return aViews;
  };

  getNextLevelAdditionOptionView = () => {
    var aButtonView = [];
    aButtonView.push(<CustomMaterialButtonView label={"+ " + getTranslation()["ADD_TAXONOMY"]}
                                               key="taxonomy"
                                               isRaisedButton={true}
                                               isDisabled={false}
                                               onButtonClick={this.handleAddLevel.bind(this, "taxonomy")}/>);

    aButtonView.push(<CustomMaterialButtonView label={"+ " + getTranslation()["ADD_TAG"]}
                                               key="tag"
                                               isRaisedButton={true}
                                               isDisabled={false}
                                               onButtonClick={this.handleAddLevel.bind(this, "tag")}/>);

    return (
        <div className="addMoreOptionContainer">
          {aButtonView}
        </div>
    )
  };

  render() {
    return (
        <div className="kpiDrillDownContainer">
          {this.getLevelView()}
          {this.getNextLevelAdditionOptionView()}
        </div>
    );
  }
}

export const view = KPIDrilldownView;
export const events = oEvents;
