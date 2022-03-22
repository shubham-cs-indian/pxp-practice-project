import CS from '../../../../../libraries/cs/index';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as MultiSelectSearchView } from '../../../../../viewlibraries/multiselectview/multi-select-search-view';
import ViewUtils from '../view/utils/view-utils';

const oEvents = {
  HANDLE_UNIQUE_SELECTION_CHANGED: "handle_unique_selection_changed",
  HANDLE_DELETE_UNIQUE_SELECTION: "handle_delete_unique_selection",
  HANDLE_UNIQUE_SELECTION_OK: "handle_delete_unique_selection_ok"
};

const oPropTypes = {
  attribute: ReactPropTypes.object,
  viewData: ReactPropTypes.object,
  closeDialog: ReactPropTypes.func,
  isDisabled: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  activeUniqueSelector: ReactPropTypes.string
};

// @CS.SafeComponent
class ContextualAttributeEditSectionView extends React.Component {
  static propTypes = oPropTypes;

  handleEditButtonClicked = (sUniqueSelectorId) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.HANDLE_UNIQUE_SELECTION_CHANGED, sUniqueSelectorId, sContext);
  };

  handleEditCombinationMasterOk = (sUniqueSelectorId) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.HANDLE_UNIQUE_SELECTION_OK, sUniqueSelectorId, sContext);

  };

  handleDeleteSelection = (sUniqueSelectorId) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.HANDLE_DELETE_UNIQUE_SELECTION, sUniqueSelectorId, sContext);
  };

  getMasterTagMap = (aTags) => {
    var oMasterTagMap = {};
    var oTagMap = {};
    var oTagValuesMap = {};

    CS.forEach(aTags, function (oTag) {
      var aNewTagValues = [];
      CS.forEach(oTag.tagValues, function (oTagValue) {
        var oNewTagValue = {
          id: oTagValue.tagValueId,
          label: CS.getLabelOrCode(oTagValue),
          isSelected: oTagValue.isSelected
        };
        oTagValuesMap[oTagValue.tagValueId] = oNewTagValue;
        aNewTagValues.push(oNewTagValue);
      });
      var oNewTag = {
        id: oTag.tagId,
        label: CS.getLabelOrCode(oTag),
        isMultiselect: oTag.isMultiselect,
        tagValues: aNewTagValues
      };
      oTagMap[oTag.tagId] = oNewTag;
    });
    oMasterTagMap.tagMap = oTagMap;
    oMasterTagMap.tagValuesMap = oTagValuesMap;
    return oMasterTagMap;
  };

  getEditCombinationView = (oUniqueSelector, oTagMap) => {
    var aEditCombinationViewRows = [];
    var _this = this;
    var sSplitter = ViewUtils.getSplitter();

    CS.forEach(oUniqueSelector.selectionValues, function (oSelectionValue) {
      var aSelectedTagValues = oSelectionValue.tagValues;
      var aFilteredTagValues = [];
      var sContextKey = _this.props.context + sSplitter + oUniqueSelector.id + sSplitter + oSelectionValue.tagId;
      var aSelectedTagMap = [];

      CS.forEach(aSelectedTagValues, function (oTagValue) {
        aSelectedTagMap.push(oTagValue.tagValueId);
      });

      var oTagGroup = oTagMap[oSelectionValue.tagId];
      CS.forEach(oTagGroup.tagValues, function (oTag) {
        if (oTag.isSelected) {
          aFilteredTagValues.push(oTag);
        }
      });

      if (!CS.isEmpty(aFilteredTagValues)) {
        aEditCombinationViewRows.push(
            <div className="editCombinationElementWrapper">
              <div className="editCombinationViewHeader">
                {CS.getLabelOrCode(oTagGroup)}
              </div>
              <div className="editCombinationMSS">
                <MultiSelectSearchView items={aFilteredTagValues}
                                       selectedItems={CS.cloneDeep(aSelectedTagMap)}
                                       context={sContextKey}
                                       isMultiSelect={oTagGroup.isMultiselect}
                                       disabled={false}/>
              </div>
            </div>
        );
      }
    });
    var oEditCombinationView = (
        <div className="editCombinationViewContainer">
          <div className="editCombinations">{aEditCombinationViewRows}</div>
          <div className="editCombinationViewMasterControls">
            <div className="editCombinationViewMasterOk"
                 onClick={_this.handleEditCombinationMasterOk.bind(_this, oUniqueSelector.id)}></div>
          </div>
        </div>
    );
    return oEditCombinationView;
  };

  getView = () => {
    var _this = this;
    var oViewData = _this.props.viewData;
    var aTags = oViewData.tags;
    var aUniqueSelectors = oViewData.uniqueSelectors;
    var aViewRows = [];

    var oMasterTagMap = this.getMasterTagMap(aTags);
    var oTagMap = oMasterTagMap.tagMap;
    var oTagValuesMap = oMasterTagMap.tagValuesMap;


    CS.forEach(aUniqueSelectors, function (oUniqueSelector) {
      var aCombinations = [];
      if (oUniqueSelector.id == oViewData.activeUniqueSelector) {
        //Expanded View
        var oEditCombinationView = _this.getEditCombinationView(oUniqueSelector, oTagMap, oTagValuesMap);
        aViewRows.push(<div className="editSection"> {oEditCombinationView}</div>);
      } else {
        //Collapsed View
        CS.forEach(oUniqueSelector.selectionValues, function (oSelectionValue) {
          var sTagId = oSelectionValue.tagId;
          var sTagLabel = CS.getLabelOrCode(oTagMap[sTagId]);
          var aSelectionTagValues = oSelectionValue.tagValues;
          var sSelectedTagValues = "";
          var aSelectedTagValues = [];
          if (aSelectionTagValues.length > 0) {
            CS.forEach(aSelectionTagValues, function (sTagValue) {
              aSelectedTagValues.push(CS.getLabelOrCode(oTagValuesMap[sTagValue.tagValueId]));
            });
            sSelectedTagValues = aSelectedTagValues.join(', ');

            aCombinations.push(<div className="combination">
              <div className="combinationKey">{sTagLabel + " :"}</div>
              <div className="combinationValues">{sSelectedTagValues}
              </div>
            </div>);
          }
        });
        aViewRows.push(<div className="combinationRow" >
          <div className="rowWrapper">
            <div className="combinationsWrapper">
              <div className="combinations"> {aCombinations}</div>
            </div>
            <div className="controls">
              <div className="editCombination" onClick={_this.handleEditButtonClicked.bind(this, oUniqueSelector.id)}>
              </div>
              <div className="deleteCombination" onClick={_this.handleDeleteSelection.bind(this, oUniqueSelector.id)}>
              </div>
            </div>
          </div>
        </div>)
      }
    });
    return aViewRows;
  };

  render() {

    return (
        <div className="contextualAttributeEditSectionContainer">
          <div className="contextualAttributeEditSectionSelect">
            {this.getView()}
          </div>
        </div>
    );
  }
}


export const view = ContextualAttributeEditSectionView;
export const events = oEvents;
