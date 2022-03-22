import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as MultiSelectSummaryView} from '../multiselectview/multi-select-summary-view';

const oPropTypes = {
  items: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.bool,
  isDisabled: ReactPropTypes.bool,
  addButtonHandler: ReactPropTypes.func,
  onApply: ReactPropTypes.func,
  cannotAdd: ReactPropTypes.bool,
  cannotRemove: ReactPropTypes.bool,
};

const oEvents = {};

// @CS.SafeComponent
class GridInstanceSummaryView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleInstanceSummaryAddBtnClicked = () => {
    if (CS.isFunction(this.props.addButtonHandler)) {
      this.props.addButtonHandler();
    }
  };

  render () {
    let bDisabledAddButton = this.props.isDisabled || this.props.cannotAdd;
    let oAddButton = bDisabledAddButton ? null : (
        <div className="gridLinkedInstanceSummaryAddButton"
             onClick={this.handleInstanceSummaryAddBtnClicked}/>);

    return (
        <div className="gridLinkedInstanceSummaryView">
          <MultiSelectSummaryView model={null}
                                  onApply={this.props.onApply}
                                  items={this.props.items}
                                  selectedItems={this.props.selectedItems}
                                  disabled={this.props.isDisabled}
                                  cannotRemove={this.props.cannotRemove}
                                  isMultiSelect={this.props.isMultiSelect}
                                  showPopover={false}
                                  showSelectedInDropdown={false}
                                  context={"gridLinkedInstanceSummaryView"}
          />
          {oAddButton}
        </div>
    );
  }
}

GridInstanceSummaryView.propTypes = oPropTypes;
GridInstanceSummaryView.defaultProps = {
  isDisabled: false
};

export const view = GridInstanceSummaryView;
export const events = oEvents;
