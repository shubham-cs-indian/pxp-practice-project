import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as SelectionToggleView } from '../selectiontoggleview/selection-toggle-view';

const oEvents = {
};

const oPropTypes = {
  groups: ReactPropTypes.arrayOf(ReactPropTypes.shape({
    id: ReactPropTypes.string,
    label: ReactPropTypes.string,
    list: ReactPropTypes.array
  })),
  disabledItems: ReactPropTypes.array,
  selectedItems: ReactPropTypes.array,
  context: ReactPropTypes.string,
  contextKey: ReactPropTypes.string,
  hideEmptyGroups:ReactPropTypes.bool
};
/**
 * @class GroupedSelectionToggleView - GroupedSelectionToggleView is used to toggle(select/deselect) items from multiple groups.
 * @memberOf Views
 * @property {array} [groups] - Contains group data.
 * @property {array} [disabledItems] - Contains disabled items data of all groups.
 * @property {array} [selectedItems] - Contains selected items data of all groups.
 * @property {string} [context] - Describe the screen or which operation will be performed(for example: context:"organization")
 * @property {string} [contextKey] -
 * @property {bool} [hideEmptyGroups] - To hide empty groups.
 */

// @CS.SafeComponent
class GroupedSelectionToggleView extends React.Component {

  constructor(props) {
    super(props);
  }

  getSelectionToggleListView () {
    let __props = this.props;
    var aSystemListView = [];
    let bHideEmptyGroups = __props.hideEmptyGroups;

    CS.forEach(__props.groups, function (oGroup, iIndex) {
      let bHideGroup = bHideEmptyGroups && CS.isEmpty(oGroup.list);
      if (!bHideGroup) {
        aSystemListView.push(
            <div className="groupRow" key={iIndex}>
              <div className="groupLabel">{CS.getLabelOrCode(oGroup)}</div>
              <div className="groupValueSelector">
                <SelectionToggleView
                    selectedItems={__props.selectedItems}
                    disabledItems={__props.disabledItems}
                    items={oGroup.list}
                    context={__props.context}
                    contextKey={__props.contextKey}
                />
              </div>
            </div>
        );
      }
    });

    return aSystemListView;
  }

  render() {
    return (
        <div className="groupedSelectionToggleContainer">
          {this.getSelectionToggleListView()}
        </div>
    );
  }
}

GroupedSelectionToggleView.propTypes = oPropTypes;

export const view = GroupedSelectionToggleView;
export const events = oEvents;
