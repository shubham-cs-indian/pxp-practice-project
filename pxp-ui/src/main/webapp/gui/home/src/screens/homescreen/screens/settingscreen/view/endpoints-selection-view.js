import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { view as GroupedSelectionToggleView } from '../../../../../viewlibraries/groupedselectiontoggleview/grouped-selection-toggle-view';

const oEvents = {
  ENDPOINTS_SELECTION_VIEW_SELECT_ALL_CHECKED: "endpoints_selection_view_select_all_checked",
  ENDPOINTS_SELECTION_VIEW_DELETE_CLICKED: "endpoints_selection_view_delete_clicked"
};

const oPropTypes = {
  groupedSelectionViewModel: ReactPropTypes.object,
  label: ReactPropTypes.string,
  disabled: ReactPropTypes.bool,
  checkboxStatus: ReactPropTypes.number,
  systemId: ReactPropTypes.string,
  context: ReactPropTypes.string,
};

// @CS.SafeComponent
class EndpointsSelectionView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleDeleteSystemClicked = (sSystemId) => {
    EventBus.dispatch(oEvents.ENDPOINTS_SELECTION_VIEW_DELETE_CLICKED, sSystemId, this.props.context);
  }

  handleSelectAllEndpointsChecked = (sSystemId) => {
    if (!this.props.disabled) {
      let oProps = this.props;
      EventBus.dispatch(oEvents.ENDPOINTS_SELECTION_VIEW_SELECT_ALL_CHECKED, sSystemId, oProps.checkboxStatus, oProps.context);
    }
  }

  getGroupedSelectionToggleView(oGroupedSelectionToggleViewModel) {

    return (
        <GroupedSelectionToggleView groups={oGroupedSelectionToggleViewModel.endpointGroups}
                                    selectedItems={oGroupedSelectionToggleViewModel.selectedEndpoints}
                                    disabledItems={oGroupedSelectionToggleViewModel.disabledEndpoints}
                                    contextKey={oGroupedSelectionToggleViewModel.contextKey}
                                    context={oGroupedSelectionToggleViewModel.context}
                                    hideEmptyGroups={true}/>
    )
  }

  getCheckboxStatusClassName(iCheckboxStatus) {
    switch (iCheckboxStatus) {
      case 0:
        return "zero";
      case 1:
        return "one";
      case 2:
        return "two";
    }
  }

  render () {
    let oProps = this.props;
    let oGroupedSelectionToggleViewModel = oProps.groupedSelectionViewModel;
    let oGroupedSelectionToggleView = this.getGroupedSelectionToggleView(oGroupedSelectionToggleViewModel);
    let sLabel = oProps.label;
    let sSystemId = oProps.systemId;
    let oCheckboxDOM = null;
    if (oProps.disabled !== true) {
      let sCheckboxClassName = "button endpointsSelectionHeaderCheckbox " + this.getCheckboxStatusClassName(oProps.checkboxStatus);
      sCheckboxClassName += " notDisabled";
      oCheckboxDOM = (<div className={sCheckboxClassName} onClick={this.handleSelectAllEndpointsChecked.bind(this, sSystemId)}></div>);
    } else {
      oGroupedSelectionToggleView = (<div className="disabledSelectionToggle">{oGroupedSelectionToggleView}</div>);
    }
    let oDeleteButtonDOM = (<div className="button endpointsSelectionHeaderCrossButton" onClick={this.handleDeleteSystemClicked.bind(this, sSystemId)}></div>);


    return (
        <div className="endpointsSelectionViewContainer">
          <div className="endpointsSelectionHeaderContainer">
            {oCheckboxDOM}
            <div className="endpointsSelectionHeaderLabel">{sLabel}</div>
            {oDeleteButtonDOM}
          </div>
          <div className="endpointsSelectionBodyContainer">
            {oGroupedSelectionToggleView}
          </div>
        </div>
    );
  }
}

EndpointsSelectionView.propTypes = oPropTypes;

export const view = EndpointsSelectionView;
export const events = oEvents;
