import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { view as EndpointsSelectionView } from './endpoints-selection-view';
import { view as ContextMenuViewNew } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';

const oEvents = {
  SYSTEMS_SELECTION_VIEW_SYSTEM_ADDED: "systems_selection_view_system_added"
};

const oPropTypes = {
  contextMenuViewModel: ReactPropTypes.array,
  endpointsSelectionViewModels: ReactPropTypes.array,
  context: ReactPropTypes.string,
  selectedItems: ReactPropTypes.array,
};

// @CS.SafeComponent
class SystemsSelectionView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      showDropdown: false
    };
  }

  handleDropdownVisibility = () => {
    this.setState({
      showDropdown: true
    });
  };

  systemSelectApplyClicked = (aSelectedSystemIds) => {
    EventBus.dispatch(oEvents.SYSTEMS_SELECTION_VIEW_SYSTEM_ADDED, aSelectedSystemIds, this.props.context);
  };

  getEndpointSelectionViews (aEndpointsSelectionViewModels, sContext) {
    let aEndpointSelectionViews = [];

    CS.forEach(aEndpointsSelectionViewModels, function (oEndpointsSelectionViewModel, iIndex) {
      aEndpointSelectionViews.push(
          <EndpointsSelectionView groupedSelectionViewModel={oEndpointsSelectionViewModel.groupedSelectionViewModel}
                                  key={iIndex}
                                  label={oEndpointsSelectionViewModel.label}
                                  disabled={oEndpointsSelectionViewModel.disabled}
                                  checkboxStatus={oEndpointsSelectionViewModel.checkboxStatus}
                                  systemId={oEndpointsSelectionViewModel.systemId}
                                  context={sContext}/>);
    });

    return aEndpointSelectionViews;
  };

  render () {
    let sSystemsLabel = getTranslation().SYSTEMS;
    let aEndpointsSelectionViewModels = this.props.endpointsSelectionViewModels;
    let aContextMenuViewModels = this.props.contextMenuViewModel;
    let aSelectedItems = this.props.selectedItems;
    let sContext = this.props.context;
    let aEndpointSelectionViews = this.getEndpointSelectionViews(aEndpointsSelectionViewModels, sContext);
    let sContainerClassName = sContext === 'role' ? 'systemsSelectionViewContainer role' : 'systemsSelectionViewContainer';

    return (<div className={sContainerClassName}>
      <div className="systemsSelectionViewHeaderContainer">
        <div className="systemsSelectionViewHeaderLabel">{sSystemsLabel}</div>
        <ContextMenuViewNew
            contextMenuViewModel={aContextMenuViewModels}
            context={sContext}
            disabled={false}
            onApplyHandler={this.systemSelectApplyClicked}
            anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
            targetOrigin={{horizontal: 'right', vertical: 'top'}}
            isMultiselect={true}
            showSelectedItems={true}
            selectedItems={aSelectedItems}>
          <TooltipView placement="bottom" label={"Add Systems"}>
            <div className="systemsSelectionAddSystems" onClick={this.handleDropdownVisibility}></div>
          </TooltipView>
        </ContextMenuViewNew>
      </div>
      <div className="systemsSelectionViewBodyContainer">
        {aEndpointSelectionViews}
      </div>
    </div>);
  };
}

SystemsSelectionView.propTypes = oPropTypes;

export const view = SystemsSelectionView;
export const events = oEvents;
