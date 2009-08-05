// Copyright (c) 2008, The Code Project. All rights reserved.
/// <reference path="../../JS/jquery-1.3.2-vsdoc2.js" />

function rateItem(objId, objTypeId, forceComment, allowAjaxOnLowVote) {
	if(objId > 0 && objTypeId > 0){
		var value = $("div[name=RateItem_" + objId + "]").find("input[type=radio]:checked").val();

		var comment = $("div[name=RateItem_" + objId + "] .RateComment")[0];
		comment = comment.value.replace(/^\s+|\s+$/g,'');
		
		if (value <= 0)
			ShowErrorMessage("You must select a value to vote", objId);
		else if (value <= 2 && comment == '' && forceComment && allowAjaxOnLowVote)
			ShowErrorMessage("You must provide a comment", objId);
		else if (allowAjaxOnLowVote || value > 2) {
			PrepElements(objId);
			$.get("/Script/Ratings/Ajax/RateItem.aspx?obid=" + objId + "&obtid=" + objTypeId + "&rvv=" + value + "&rvc=" + escape(comment),
					function(data) { callback(data, objId); });
		}
	}
	return false;
}

function callback(data, objId) {
	if(data.length > 0){
		var voteRes = $("div[name=RateItem_" + objId + "] .voteRes")[0];
		if(voteRes) {
			voteRes.innerHTML = data;
			voteRes.style.display = "";
		}
		voteRes = $("[name=CurRat_" + objId + "]")[0];
  		if(voteRes)voteRes.style.display = "none";
	}
	var loader = $("div[name=RateItem_" + objId + "] .loaderImg")[0];
	if(loader&&loader.style)loader.style.display = "none";
}

function PrepElements(objId){
	var loader = $("div[name=RateItem_" + objId + "] .loaderImg")[0];
	if(loader&&loader.style.display == "none")
		loader.style.display = "";

	loader = $("div[name=RateItem_" + objId + "] .voteTbl")[0];
	if(loader)loader.style.display = "none";

	loader = $("div[name=RateItem_" + objId + "] .voteRes")[0];
	if (loader) loader.style.display = "none";

	loader = $("div[name=RateItem_" + objId + "] .RateComDiv")[0];
	if(loader)loader.style.display = "none";
}

function ShowErrorMessage(msg, objId){
	var loader = $("div[name=RateItem_" + objId + "] .loaderImg")[0];
	if(loader)loader.style.display = "none";
	
	//alert(msg);
	/*
	var voteRes = $("div[name=RateItem_" + objId + "] .voteRes");
	if(voteRes) {
		voteRes.innerHTML = "An error occurred. Your vote has not been saved. Please try again later.";
		voteRes.style.display = "";
	}
	*/
}
