AJS.Labels = (function () {
    var $ = AJS.$;
    return {

        // Variable to enforce one label operation at a time
        operationInProgress: false,

        // Updates and displays the status message if any. Use empty string to clear it out.
        updateStatus: function(statusMessage) {
            $("#labelOperationStatus").html(statusMessage);
            if(statusMessage != "")
                $("#waitImageAndStatus").addClass("open");
            else
                $("#waitImageAndStatus").removeClass("open");
        },
        // Use before any new label operation calls. It clears out all previous error messages and updates the status.
        startOperation: function(statusMessage) {
            AJS.Labels.operationInProgress = true;
            $("#errorSpan").html("");
            AJS.Labels.labelOperationError("");
            AJS.Labels.updateStatus(statusMessage);
        },
        // User after any label operation calls have finished. It clears out the status message.
        finishOperation: function() {
            AJS.Labels.updateStatus("");
            AJS.Labels.operationInProgress = false;
        },
        // Updates and displays an error message. Mainly for server and dwr errors.
        handleError: function (htmlMessage) {
            AJS.Labels.operationInProgress = false;
            AJS.Labels.updateStatus("");
            $("#errorSpan").html(htmlMessage);
        },
        // Updates and displays label operation error messages. Mainly for errors when ajax response is not success.
        labelOperationError: function(htmlMessage) {
            $("#labelOperationErrorMessage").html(htmlMessage);
            if(htmlMessage != "")
                AJS.setVisible("#labelOperationErrorContainer", true);
            else
                AJS.setVisible("#labelOperationErrorContainer", false);
        },

        addLabel: function() {
            if (!AJS.Labels.operationInProgress) {
                AJS.Labels.startOperation("Adding label...");
                AddLabelToEntity.addLabel(AJS.params.pageId, $("#labelsString").val(), {
                    callback: AJS.Labels.addLabelCallback,
                    errorHandler: AJS.Labels.addLabelErrorHander
                });
            }
            return false;
        },
        addLabelCallback: function (response) {
            if (response.success) {
                $("#labelsList").html($("#labelsList").html() + response.response);
                // rebind the remove links for the newly added labels
                $(".labels-editor .remove-label").unbind('click');
                $(".labels-editor .remove-label").click(AJS.Labels.removeLabel);
                $("#labelsString").val("");
            }
            else {
                AJS.Labels.labelOperationError(response.response);
            }
            // clear the text box and focus on it should the user want to add another label
            $("#labelsString").focus();
            SuggestedLabelsForEntity.viewLabels(AJS.params.pageId, AJS.Labels.suggestedLabelsCallback);
            AJS.Labels.finishOperation();
        },
        addLabelErrorHander: function () {
            AJS.Labels.handleError("[41a] Error connecting to the server. The labels have not been updated.");
        },
        removeLabel: function () {
            if (!AJS.Labels.operationInProgress) {
                AJS.Labels.startOperation('Removing label ...');
                var labelId = AJS.$(this).parent().attr("id").replace(/^label-/, "");
                RemoveLabelFromEntity.removeLabel(AJS.params.pageId, labelId, {
                    callback: AJS.Labels.removeLabelCallback(labelId),
                    errorHandler: AJS.Labels.removeLabelErrorHandler}
                );
            }
            return false;
        },
        removeLabelCallback : function(labelId) {
            return function(response) {
                if (response.success) {
                    $("#label-" + labelId).fadeOut("slow", function () {
                        $(this).remove();
                    });
                }
                else {
                    AJS.Labels.labelOperationError(response.response);
                }
                AJS.Labels.finishOperation();
            }
        },
        removeLabelErrorHandler: function (response) {
            var message = "Error connecting to the server. The labels have not been updated";
            if(response) message += ": " + response;

            AJS.Labels.handleError(message);
        },
        suggestedLabelsCallback: function (response) {
            if (!response.success) return;
            $("#suggestedLabelsSpan").html(response.response);
            $("#suggestedLabelsSpan .suggested-label").click(function () {
                var val = $('#labelsString').val();
                if (val.length > 0) val += " ";
                val += $(this).text();
                $('#labelsString').val(val);
                var toRemove = this;
                if ($(this).parent().find("a").length == 1) { // if we're the last suggestion
                    toRemove = $(this).parent();
                }
                $(toRemove).fadeOut(function () { $(this).remove(); });
                return false;
            });
        }
    };
})();

AJS.toInit(function ($) {

    var toggleLabels = function (e) {
        $('#labels_div').toggleClass("hidden");
        $("#labels_info").toggleClass("hidden");

        if ($('#labels_div').hasClass("hidden")) {
            $("#labels_info").html($("#labelsString").val().toLowerCase());
            $("#labels_edit_link").html(AJS.params.editLabel);
        }
        else {
            SuggestedLabelsForEntity.viewLabels(AJS.params.pageId, AJS.Labels.suggestedLabelsCallback);

            $("#labels_edit_link").html(AJS.params.doneLabel);
        }

        if (e) return false;
    };

    var labelsShowing = $("#labelsShowing");
    if (labelsShowing && labelsShowing.val() == "true") {
        toggleLabels();
    }

    $("#labels_edit_link").click(toggleLabels);

    if ($("#labelsString").length > 0) {
        new AJS.Autocompleter("labelsString", "labelsAutocompleteList", AJS.params.pageId, {
            tokens: [",", " "],
            dwrFunction: GenerateAutocompleteLabelsListForEntity.autocompleteLabels
        });
    }

    $("#add-labels-form").submit(AJS.Labels.addLabel);
    $(".labels-editor .add-labels").click(AJS.Labels.addLabel);
    $(".labels-editor .remove-label").click(AJS.Labels.removeLabel);

    $(".show-labels-editor").click(function () {
        SuggestedLabelsForEntity.viewLabels(AJS.params.pageId, AJS.Labels.suggestedLabelsCallback);
        // reset the value of this field, just in case the browser wants to become helpful and insert the old value
        $("#labelsString").val("");
        $("#labels-section").addClass("open");

        // update the links
        AJS.setVisible(".show-labels-editor", false);
        AJS.setVisible("a.hide-labels-editor", true);
        AJS.setVisible("#labels-section-title", true);

        $("#labelsString").get(0).focus();
        return false;
    });
    $(".hide-labels-editor").click(function () {
        // clear out any error messages
        AJS.Labels.labelOperationError("");
        $("#errorSpan").html("");

        $("#labels-section").removeClass("open");

        // update the links
        AJS.setVisible("a.hide-labels-editor", false);
        AJS.setVisible(".show-labels-editor", true);
        if ($("#labelsList").children().length == 0 && $("#labelsString").val() == "") { // no labels
            $(".show-labels-editor").addClass("add").text(AJS.params.addLabel);
            AJS.setVisible("#labels-section-title", false);
        }
        else {
            $(".show-labels-editor").removeClass("add").text(AJS.params.editLabel);
            AJS.setVisible("#labels-section-title", true);
        }

        // add label if any user input
        if($("#labelsString").val() != "")
            AJS.Labels.addLabel();
        
        return false;
    });
});

