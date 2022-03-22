import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as AvailableFiltersView } from '../../../../../viewlibraries/filterview/fltr-available-filters-view';
import { view as AppliedFiltersView } from '../../../../../viewlibraries/filterview/fltr-applied-filter-view';

const oEvents = {};

const oPropTypes = {
  filterData: ReactPropTypes.object,
  onViewUpdate: ReactPropTypes.func,
  context: ReactPropTypes.string,
  extraData: ReactPropTypes.object
};

// @CS.SafeComponent
class VariatingAttributeFilterWrapperView extends React.Component {
  static propTypes = oPropTypes;

  getFilterDetailsSectionView = () => {
    let __props = this.props;
    let oFilterData = __props.filterData;
    let oAvailableFilterItemViewDetails = {
      showDefaultIcon: true
    };

    return (
        <div className="filterDetailsSectionContainer">
            <AvailableFiltersView availableFilterData={oFilterData.availableFilterData}
                                  appliedFilterDataClone={oFilterData.appliedFilterDataClone}
                                  onViewUpdate={__props.onViewUpdate}
                                  appliedFilterData={oFilterData.appliedFilterData}
                                  selectedHierarchyContext={""}
                                  selectedFilterHierarchyFilterGroups={[]}
                                  availableEntityViewStatus={null}
                                  isAdvancedFilterApplied={false}
                                  showGroupBy={false}
                                  showAdvancedSearchOptions={false}
                                  context={__props.context}
                                  extraData={__props.extraData}
                                  filterContext={__props.filterContext}
                                  availableFilterItemViewDetails = {oAvailableFilterItemViewDetails}
            />

            <AppliedFiltersView availableFilterData={oFilterData.availableFilterData}
                               appliedFilterData={oFilterData.appliedFilterData}
                               appliedFilterDataClone={null}
                               masterAttributeList={oFilterData.masterAttributeList}
                               selectedFilterHierarchyFilterGroups={[]}
                               isAdvancedFilterApplied={false}
                               showClearFilterButton={false}
                               context={__props.context}
                               extraData={__props.extraData}
                               showDefaultIcon={true}
                               filterContext={__props.filterContext}/>
        </div>
    );
  };

  render() {
    let oStyle = {
      marginBottom: "10px"
    };

    let fGetFilterDetailsSectionView = this.getFilterDetailsSectionView();

    return (
        <div style={oStyle} className="uomTableFilterBody">
          {fGetFilterDetailsSectionView}
        </div>
    );
  }
}


export const view = VariatingAttributeFilterWrapperView;
export const events = oEvents;
