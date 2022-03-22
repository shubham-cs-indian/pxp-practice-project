/**
 * Created by CS56 on 10/4/2016.
 */

import EntityViewProps from './entity-view-props';

var ArticleViewProps = function () {
  this.inheritsFrom(EntityViewProps);
  this.className = "ArticleViewProps";

  var oArticleFolderVisibility = {
    showAll: true,
    showFolders: true,
    showArticles: true
  };

  this.getArticleFolderVisibility = function () {
    return oArticleFolderVisibility;
  };

};

export default new ArticleViewProps();