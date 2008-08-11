YAHOO.util.Event.addListener(window, "load", function() {
    EnhanceFromMarkup = new function() {
        var DataTable  = YAHOO.widget.DataTable,
            Paginator  = YAHOO.widget.Paginator;

        var buildQueryString = function (state,dt) {
            return "&arg0=" + state.pagination.recordOffset +
                   "&arg1=" + state.pagination.rowsPerPage+ '&time='+ new Date().getTime();
        };

        var myPaginator = new Paginator({
            containers         : ['paging'],
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
            sortedBy               :{key:"date",dir:"desc"}
        };        

        var myColumnDefs = [
            {key:"image",label:"Image",sortable:true,formatter:function(elCell,oRecord) {
                var imgUrl = oRecord.getData("image")
                elCell.innerHTML = '<img src="'+imgUrl+'" width=36px />';
                elCell.style.cursor = 'pointer';}},
            {key:"url",label:"URL", formatter:YAHOO.widget.DataTable.formatLink},
            {key:"date",label:"Date",sortable:true,formatter:YAHOO.widget.DataTable.formatDate},
            {key:"id",label:"Id",sortable:true,isPrimaryKey:true},
            {key:"delete",label:"Delete",action:'delete',formatter:function(elCell) {
                elCell.innerHTML = '<img src="/img/delete.gif" title="delete row" />';
                elCell.style.cursor = 'pointer';}}
        ];

        this.myDataSource = new YAHOO.util.DataSource('/rpc?action=GetImages');
        this.myDataSource.responseType   = YAHOO.util.DataSource.TYPE_JSON;
        this.myDataSource.responseSchema = {
            resultsList : 'records',
            fields: [{key:"image"},{key:"url"},
                {key:"date"}, {key:"id"}, {key:"delete"}
            ],
            metaFields : {
             totalRecords:'totalRecords',
             recordStartIndex:'startIndex'
        }
        };

        this.myDataTable = new YAHOO.widget.DataTable("imagediv", myColumnDefs, this.myDataSource,myTableConfig);

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
                if (confirm('Are you sure to delete the image?')) {
                    var record = this.getRecord(target);
                    YAHOO.util.Connect.asyncRequest('POST','/rpc?action=DeleteImage' + myBuildUrl(this,record),
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

        ;
    };
});
