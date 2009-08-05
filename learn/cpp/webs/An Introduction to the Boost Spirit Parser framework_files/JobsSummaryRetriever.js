// Copyright (c) 2008, The Code Project. All rights reserved.

function RetrieveJobsSummary(divName, objectId, objectTypeId, countryId, attributesList) {
    var elm = $("div[id=" + divName + "]");
    if (elm&&elm.length > 0) {
        var queryString = "/Script/Jobs/Ajax/GetRelatedJobs.aspx?";
        queryString += "objId="    + objectId;
        queryString += "&typeId="  + objectTypeId;
        queryString += "&cntrId="  + countryId;
        queryString += "&atrList=" + attributesList;

        $(elm).load(queryString);
    }
}