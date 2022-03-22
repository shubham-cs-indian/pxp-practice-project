import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as MultiSelectSearchView } from './../multiselectview/multi-select-search-view';
import { view as LazyMSSView } from '../lazy-mss-view/lazy-mss-view';
import { view as LazyContextMenuView } from '../lazycontextmenuview/lazy-context-menu-view';

const oEvents = {
  GRID_MSS_ADDITIONAL_LIST_ITEM_ADDED: "grid_mss_additional_list_item_added"
};
const oPropTypes = {
  property: ReactPropTypes.object,
  onApply: ReactPropTypes.func,
  context: ReactPropTypes.string,
};
/**
 * @class GridCalculatedAttributePropertyView
 * @memberOf Views
 * @property {object} [property]- Contains cell data(ex. {cannotRemove: false contentId: "264ac6a9-6c34-4a8c-8d08-89910db521b7"
 * context: "gridTagLinkedMasterTag" disabled: false, items: Array[5], label: "",  rejectedList: Array[0], requestResponseInfo: {…}, selectedItems: Array[1], singleSelect: true}
 * @property {func} [onApply] - Executes when apply button is clicked.
 */

// @CS.SafeComponent
class GridCalculatedAttributePropertyView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showPopover: false
    }
  }

  openPopover =()=> {
    this.setState({
      showPopover: true
    });
  }

  closePopover =()=> {
    this.setState({
      showPopover: false
    });
  }

  addAdditionalListValue =(aCheckedItems, oReferencedData)=> {
    var oProperty = this.props.property;
    var sContext = oProperty.context;
    var sContentId = oProperty.contentId;
    EventBus.dispatch(oEvents.GRID_MSS_ADDITIONAL_LIST_ITEM_ADDED, sContentId, aCheckedItems, sContext, oReferencedData, this.props.context);
    this.setState({
      showPopover: false
    });
  }

/*
  handleNativeDropdownChanged =(sValue)=> {
    this.props.onApply([sValue]);
  }
*/

/*
  getNativeDropdownView =(aItems, aSelectedItems, bIsDisabled)=> {
    var oProperty = this.props.property;
    var bDisableCross = oProperty.disableCross;

    return <NativeDropdownView items={aItems}
                        value={aSelectedItems[0]}
                        disabled={bIsDisabled}
                        mandatory={bDisableCross}
                        onValueChange={this.handleNativeDropdownChanged}/>
  }
*/

  getMSSViewBasedOnParentOrChild =()=>{
    var oProperty = this.props.property;
    var aItems = oProperty.items || [];
    var aSelectedItems = oProperty.value;
    var bSingleSelect = oProperty.singleSelect;
    var bWithCheckbox = oProperty.checkBox;
    var bDisableCross = oProperty.disableCross;
    var bIsDisabled = oProperty.disabled || false;
    var sMSSContext = oProperty.context + ViewUtils.getSplitter() + oProperty.contentId;

    var oMSSView = null;
    if(oProperty.requestResponseInfo){
      oMSSView = <LazyMSSView
          key={oProperty.contentId}
          isMultiSelect={!bSingleSelect}
          disabled={bIsDisabled}
          selectedItems={aSelectedItems}
          excludedItems={oProperty.rejectedList}
          context={""}
          cannotRemove={bDisableCross}
          showColor={true}
          onApply={this.props.onApply}
          showCreateButton={false}
          isLoadMoreEnabled={CS.noop}
          searchHandler={CS.noop}
          searchText={""}
          loadMoreHandler={CS.noop}
          referencedData={oProperty.referencedData}
          requestResponseInfo={oProperty.requestResponseInfo}
      />
    }else {
      oMSSView = <MultiSelectSearchView
          key={oProperty.contentId}
          model={null}
          onApply={this.props.onApply}
          items={aItems}
          selectedItems={aSelectedItems}
          isMultiSelect={!bSingleSelect}
          checkbox={bWithCheckbox}
          disabled={bIsDisabled}
          disableCross={bDisableCross}
          context={sMSSContext}/>
    }

    return oMSSView;
  };

  getAdditionalViewButton = () => {
    var oProperty = this.props.property;
    var aSelectedItems = oProperty.value;
    var aExcludedItems = oProperty.excludedMasterChildren;
    var bSingleSelect = oProperty.singleSelect;
    var bDisableCross = oProperty.disableCross;
    var bIsDisabled = oProperty.disabled || false;
    var sMSSContext = oProperty.context + ViewUtils.getSplitter() + oProperty.contentId;
    var sClassName = "gridContextMenuViewAdditionalList";
    var bHideAddButton = !oProperty.additionalBtnReqResInfo || CS.isEmpty(aSelectedItems);


    if(!bHideAddButton){
      return  <LazyContextMenuView
          key={oProperty.additionalBtnReqResInfo.customRequestModel.tagGroupId}
          className={sClassName}
          isMultiSelect={!bSingleSelect}
          disabled={bIsDisabled}
          selectedItems={[]}
          excludedItems={aExcludedItems}
          context={""}
          cannotRemove={bDisableCross}
          showColor={true}
          onApply={this.addAdditionalListValue}
          showCreateButton={false}
          isLoadMoreEnabled={CS.noop}
          searchHandler={CS.noop}
          searchText={""}
          loadMoreHandler={CS.noop}
          referencedData={oProperty.referencedData}
          requestResponseInfo={oProperty.additionalBtnReqResInfo}
          isMultiselect={true}
          onApplyHandler={this.addAdditionalListValue}
      >
        <div className="mssAddButton" onClick={this.openPopover}></div>
      </LazyContextMenuView>
    }

    return null;
  };

  render() {
    let oMSSViewBasedOnParentOrChild = this.getMSSViewBasedOnParentOrChild();
    let oAdditionalButton = this.getAdditionalViewButton();
    return (
        <div className="gridMSSWithAdditionalList">
          {oMSSViewBasedOnParentOrChild}
          {oAdditionalButton}
        </div>
    );
  }
}



GridCalculatedAttributePropertyView.propTyepes = oPropTypes;

export const view = GridCalculatedAttributePropertyView;
export const events = oEvents;
