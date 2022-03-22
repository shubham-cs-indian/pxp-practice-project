import CS from '../../libraries/cs';
var assetTypes = {
  imageTypes: ['.jpg', '.jpeg', '.png', '.ico', '.eps', '.ai', '.psd', '.tif', '.tiff', '.gif', '.zip'],
  videoTypes: ['.wmv', '.avi', '.mov', '.flv', '.mpeg', '.mpg', '.mp4'],
  documentTypes: ['.pdf', '.ppt', '.pptx', '.indd', '.doc', '.docx', '.xls', '.xlsx', '.obj', '.stp', '.fbx'],
  fileImportTypes: [".xlsx", ".xls"]
};

assetTypes['allTypes'] = CS.reduce(assetTypes, function(all, type) {
  return all.concat(type);
}, []);

export default assetTypes;
