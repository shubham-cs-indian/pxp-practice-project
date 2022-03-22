import CS from '../../../../../libraries/cs';

var assetTypes = {
  imageTypes: ['.jpg', '.jpeg', '.png', '.ico', '.eps', '.ai', '.psd', '.tif', '.tiff', '.gif'],
  videoTypes: ['.wmv', '.avi', '.mov', '.flv', '.mpeg', '.mpg', '.mp4'],
  documentTypes: ['.pdf', '.ppt', '.pptx', '.indd', '.doc', '.docx', '.xls', '.xlsx', '.obj', '.stp', '.fbx', '.xtex'],
  zipTypes: ['.zip'],
  onBoardingImportTypes: [".xlsx", ".xls", ".json"]
};

assetTypes['allTypes'] = CS.reduce(assetTypes, function(all, type) {
  return all.concat(type);
}, []);

export default assetTypes;
