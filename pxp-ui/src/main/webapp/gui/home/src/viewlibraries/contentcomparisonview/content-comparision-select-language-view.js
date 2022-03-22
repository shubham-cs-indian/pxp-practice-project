import React, {Component} from "react";
import PropTypes from 'prop-types';
import {view} from "../../viewlibraries/gridview/grid-view";

const GridView = view;

class ContentComparisonSelectLanguageView extends Component {

  getSelectedLanguagesView () {
    let {selectedLanguageView: oSelectedLanguageView} = this.props;
    return (
        <div className={"selectedLanguages"}>
          {oSelectedLanguageView}
        </div>
    )
  }

  getLanguageSelectionView () {
    let {contentComparisonSkeleton: oContentComparisonSkeleton, contentComparisonSelectionGridData: aContentComparisonSelectionGridData} = this.props;
    return (
        <GridView
            skeleton={oContentComparisonSkeleton}
            data={aContentComparisonSelectionGridData}
            showCheckboxColumn={true}
            hideUpperSection={true}
            hideLowerSection={true}
        />
    )
  }

  render () {
    return (
        <div className="contentComparisonSelectLanguageViewContainer">
          {this.props.matchPropertiesView}
          {this.getLanguageSelectionView()}
          {this.getSelectedLanguagesView()}
        </div>
    );
  }
}

ContentComparisonSelectLanguageView.propTypes = {
  contentComparisonSelectionGridData: PropTypes.object,
  contentComparisonSkeleton: PropTypes.object,
  selectedLanguageView: PropTypes.object,
  matchPropertiesView: PropTypes.object
};

export default ContentComparisonSelectLanguageView;