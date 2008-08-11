YAHOO.util.Event.addListener(window, "load", function() {
    EnhanceFromMarkup = new function() {
        var DataTable  = YAHOO.widget.DataTable,
            Paginator  = YAHOO.widget.Paginator;

        var buildQueryString = function (state,dt) {
            return "&arg0=" + state.pagination.recordOffset +
                   "&arg1=" + state.pagination.rowsPerPage+ '&time='+ new Date().getTime();
        };

        var myPaginator = new Paginator({
            containers         : ['tag_paging'],
            pageLinks          : 5,
            rowsPerPage        : 10,
            rowsPerPageOptions : [10,20,30],
            template           : "<strong>{CurrentPageReport}</strong> {PreviousPageLink} {PageLinks} {NextPageLink} {RowsPerPageDropdown}"
        });

        var myTableConfig = {
            initialRequest         : '&arg0=0&arg1=10&time='+ new Date().getTime(),  //'startIndex=0&results=25'
            generateRequest        : buildQueryString,
            paginationEventHandler : DataTable.handleDataSourcePagination,
            paginator              : myPaginator,
            sortedBy               :{key:"entrycount",dir:"desc"}
        };
        var myColumnDefs = [
            {key:"tag",label:"Tag",sortable:true},
            {key:"entrycount",sortable:true,label:"Count"},
            {key:"valid",label:"Valid",sortable:true},
            {key:"id",label:"Id",sortable:true,isPrimaryKey:true},
            {key:"delete",label:"Delete",action:'delete',formatter:function(elCell) {
                elCell.innerHTML = '<img src="/img/delete.gif" title="delete row" />';
                elCell.style.cursor = 'pointer';}},
            {key:"refreshcount",label:"Refresh",action:'refresh',formatter:function(elCell) {
                elCell.innerHTML = '<img src="/img/refresh.png" title="refresh entry count row" />';
                elCell.style.cursor = 'pointer';}}
        ];

        this.myDataSource = new YAHOO.util.DataSource('/rpc?action=GetTags');
        this.myDataSource.responseType   = YAHOO.util.DataSource.TYPE_JSON;
        this.myDataSource.responseSchema = {
            resultsList : 'records',
            fields: [{key:"tag"},{key:"entrycount"},
                {key:"valid"}, {key:"id"}, {key:"delete"}, {key:"refreshcount"}
            ],
            metaFields : {
             totalRecords:'totalRecords',
             recordStartIndex:'startIndex'
        }
        };
        this.myDataTable = new YAHOO.widget.DataTable("tagdiv", myColumnDefs, this.myDataSource,myTableConfig);

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
            if (column.action == 'delete') {
                if (confirm("Are you sure to delete the tag?")) {
                    var record = this.getRecord(target);
                    YAHOO.util.Connect.asyncRequest('POST','/rpc?action=DeleteTag' + myBuildUrl(this,record),
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
            }else if ( column.action =='refresh') {
                if (confirm("Are you sure to refresh the entry count of this tag?")) {
                    var record = this.getRecord(target);
                    YAHOO.util.Connect.asyncRequest('POST','/rpc?action=RefreshTag' + myBuildUrl(this,record),
                    {
                        success: function (o) {
                            //todo: refresh the select row.
                            //this.render();
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

        ;
    };
});
