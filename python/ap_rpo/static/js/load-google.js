google.load("search", "1");

function OnLoad() {
      // Create a search control
      var searchControl = new google.search.SearchControl();

      // Add in a full set of searchers
      var localSearch = new google.search.LocalSearch();
      searchControl.addSearcher(localSearch);
      searchControl.addSearcher(new google.search.BlogSearch());
      searchControl.addSearcher(new google.search.WebSearch());
//      searchControl.addSearcher(new google.search.VideoSearch());
      

      // Set the Local Search center point
      localSearch.setCenterPoint("New York, NY");

      //var drawOptions = new GdrawOptionsgoogle.search.DrawOptions();
      //drawOptions.setDrawMode(GSearchControlgoogle.search.SearchControl.DRAW_MODE_TABBED);
            
      // Tell the searcher to draw itself and tell it where to attach
      searchControl.draw(document.getElementById("searchcontrol"));

      // Execute an inital search
      //searchControl.execute("");
}
google.setOnLoadCallback(OnLoad);