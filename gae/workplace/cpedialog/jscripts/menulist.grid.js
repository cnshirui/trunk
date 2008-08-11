        YAHOO.util.Event.addListener(window, "load", function() {
            EnhanceFromMarkup = new function() {
                var myColumnDefs = [
                    {key:"title",label:"Title",sortable:true,editor:"textbox"},
                    {key:"permalink",label:"Permalink",sortable:true,editor:"textbox"},
                    {key:"target",label:"Target",sortable:true,editor:"dropdown",editorOptions:{dropdownOptions:["_self","_blank","_parent","_top"]}},
                    {key:"order",label:"Order",formatter:YAHOO.widget.DataTable.formatNumber,sortable:true,editor:"textbox",editorOptions:{validator:YAHOO.widget.DataTable.validateNumber}},
                    {key:"valid",label:"Valid",sortable:true,editor:"radio",editorOptions:{radioOptions:[true,false],disableBtns:true}},
                    {key:"id",label:"Id",sortable:true,isPrimaryKey:true},
                    {key:"delete",label:"Delete",action:'delete',formatter:function(elCell) {
                        elCell.innerHTML = '<img src="/img/delete.gif" title="delete row" />';
                        elCell.style.cursor = 'pointer';}},
                    {key:"insert",label:"Add",action:'insert',formatter:function(elCell) {
                        elCell.innerHTML = '<img src="/img/insert.png" title="insert new row" />';
                        elCell.style.cursor = 'pointer';}}
                ];

                this.myDataSource = new YAHOO.util.DataSource(YAHOO.util.Dom.get("menutable"));
                this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_HTMLTABLE;
                this.myDataSource.responseSchema = {
                    fields: [{key:"title"},{key:"permalink"},{key:"target"},
                        {key:"order", parser:YAHOO.util.DataSource.parseNumber},
                        {key:"valid"}, {key:"id"}, {key:"delete"}, {key:"insert"}
                    ]
                };
                this.myDataTable = new YAHOO.widget.DataTable("menudiv", myColumnDefs, this.myDataSource,
                   { sortedBy:{key:"order",dir:"asc"}});

                // Set up editing flow
                this.highlightEditableCell = function(oArgs) {
                    var elCell = oArgs.target;
                    if (YAHOO.util.Dom.hasClass(elCell, "yui-dt-editable")) {
                        this.highlightCell(elCell);
                    }
                };
                this.myDataTable.subscribe("cellMouseoverEvent", this.highlightEditableCell);
                this.myDataTable.subscribe("cellMouseoutEvent", this.myDataTable.onEventUnhighlightCell);
                //this.myDataTable.subscribe("cellClickEvent", this.myDataTable.onEventShowCellEditor);

                // Hook into custom event to customize save-flow of "radio" editor
                this.myDataTable.subscribe("editorUpdateEvent", function(oArgs) {
                    if (oArgs.editor.column.key === "valid") {
                        this.saveCellEditor();
                    }
                });
                this.myDataTable.subscribe("editorBlurEvent", function(oArgs) {
                    this.cancelCellEditor();
                });

                var myBuildUrl = function(datatable,record) {
                    var url = '';
                    var cols = datatable.getColumnSet().keys;
                    for (var i = 0; i < cols.length; i++) {
                        if (cols[i].isPrimaryKey) {
                            url += '&' + cols[i].key + '=' + encodeURIComponent(record.getData(cols[i].key));
                        }
                    }
                    return url;
                };

                this.myDataTable.subscribe('cellClickEvent', function(ev) {
                    var target = YAHOO.util.Event.getTarget(ev);
                    var column = this.getColumn(target);
                    if (column.action == 'insert') {
                       if (confirm('Are you sure to add a new menu?')) {
                           YAHOO.util.Connect.asyncRequest('POST', '/rpc?action=AddMenu',
                           {
                               success: function (o) {
                                       var record = YAHOO.lang.JSON.parse(o.responseText);
                                       this.addRow(record ,this.getRecordIndex(target));
                                },
                               failure: function (o) {
                                   alert(o.statusText);
                               },
                               scope:this
                           }
                                   );
                       }
                    } else {
                        this.onEventShowCellEditor(ev);
                    }
                });

                this.myDataTable.subscribe('cellClickEvent', function(ev) {
                    var target = YAHOO.util.Event.getTarget(ev);
                    var column = this.getColumn(target);
                    if (column.action == 'delete') {
                        if (confirm('Are you sure to delete the menu?')) {
                            var record = this.getRecord(target);
                            YAHOO.util.Connect.asyncRequest('POST','/rpc?action=DeleteMenu' + myBuildUrl(this,record),
                            {
                                success: function (o) {
                                    if (o.responseText == 'true') {
                                        this.deleteRow(target);
                                    } else {
                                        alert(o.responseText);
                                    }
                                },
                                failure: function (o) {
                                    alert(o.statusText);
                                },
                                scope:this
                            }
                                    );
                        }
                    } else {
                        this.onEventShowCellEditor(ev);
                    }
                });

                YAHOO.widget.DataTable.prototype.saveCellEditor = function() {
                    // ++++ this is the inner function to handle the several possible failure conditions
                    var onFailure = function (msg) {
                        alert(msg);
                    };

                   // +++ this comes from the original except for the part I cut to place in the function above.
                    if (this._oCellEditor.isActive) {
                        var newData = this._oCellEditor.value;
                        // Copy the data to pass to the event
                        var oldData = YAHOO.widget.DataTable._cloneObject(this._oCellEditor.record.getData(this._oCellEditor.column.key));

                        // Validate input data
                        if (this._oCellEditor.validator) {
                            newData = this._oCellEditor.value = this._oCellEditor.validator.call(this, newData, oldData, this._oCellEditor);
                            if (newData === null) {
                                this.resetCellEditor();
                                this.fireEvent("editorRevertEvent",
                                {editor:this._oCellEditor, oldData:oldData, newData:newData});
                                YAHOO.log("Could not save Cell Editor input due to invalid data " +
                                          lang.dump(newData), "warn", this.toString());
                                return;
                            }
                        }

                        var editColumn = this._oCellEditor.column.key;
                        YAHOO.util.Connect.asyncRequest(
                                'POST',
                                '/rpc?action=UpdateMenu&editColumn='+editColumn+'&newData=' + encodeURIComponent(newData) +
                                '&oldData=' + encodeURIComponent(oldData) + myBuildUrl(this, this._oCellEditor.record),
                        {
                            success: function (o) {
                                // Update the Record
                                this._oRecordSet.updateRecordValue(this._oCellEditor.record, this._oCellEditor.column.key, this._oCellEditor.value);
                                // Update the UI
                                this.formatCell(this._oCellEditor.cell.firstChild);

                                // Bug fix 1764044
                                this._oChainRender.add({
                                    method: function() {
                                        this._syncColWidths();
                                    },
                                    scope: this
                                });
                                this._oChainRender.run();
                                // Clear out the Cell Editor
                                this.resetCellEditor();

                                this.fireEvent("editorSaveEvent",
                                {editor:this._oCellEditor, oldData:oldData, newData:newData});
                                YAHOO.log("Cell Editor input saved", "info", this.toString());
                            },
                            failure: function(o) {
                                onFailure(o.statusText);
                            },
                            scope: this
                        }
                                );
                    }
                    else {
                        YAHOO.log("Cell Editor not active to save input", "warn", this.toString());
                    }
                };
                ;
            };
        });
