
// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (GenerateAutocompleteLabelsListForEntity == null) var GenerateAutocompleteLabelsListForEntity = {};
GenerateAutocompleteLabelsListForEntity._path = '/wiki/dwr';
GenerateAutocompleteLabelsListForEntity.setLabelManager = function(p0, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'setLabelManager', p0, callback);
}
GenerateAutocompleteLabelsListForEntity.setPageManager = function(p0, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'setPageManager', p0, callback);
}
GenerateAutocompleteLabelsListForEntity.autocompleteLabels = function(p0, p1, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'autocompleteLabels', p0, p1, callback);
}
GenerateAutocompleteLabelsListForEntity.getText = function(p0, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'getText', p0, callback);
}
GenerateAutocompleteLabelsListForEntity.getText = function(p0, p1, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'getText', p0, p1, callback);
}
GenerateAutocompleteLabelsListForEntity.getText = function(p0, p1, callback) {
  dwr.engine._execute(GenerateAutocompleteLabelsListForEntity._path, 'GenerateAutocompleteLabelsListForEntity', 'getText', p0, p1, callback);
}
