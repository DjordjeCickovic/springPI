var pageNumber = 0;
var PDVManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    showPDVsList: function () {
        $.ajax({

            url: this.basePath() + '/pdv/pageable?page=' + pageNumber,
            cache: false,
            dataType: 'json',
            success: function (list) {
            	$('#PreviousButton').show();
            	$('#NextButton').show();
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>Naziv</th><th>Edit</th></tr>');
                $.each(list, function (index, pdv) {
                    $('#customTable tr:last').after('<tr><td>' + pdv.naziv + '</td><td><a href=\'javascript:PDVManager.showPDVsDetail("' + pdv.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with PDV details,
    showPDVsDetail: function (PDVID) {
        if (PDVID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#PDVListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/pdv/' + PDVID,
            cache: false,
            dataType: 'json',
            success: function (pdv) {

                $('#PDVDetailPanel').show();

                $('#PDVID').val(pdv.id);
                $('#PDVNaziv').val(pdv.naziv);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#PDVDetailPanel').hide();
        $('#PDVListPanel').show();
        PDVManager.showPDVsList();
        $('#PDVListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showPDVsList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            PDVManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	PDVManager.refreshList();
      },

    // Formats a URI of a PDV
    createPDVURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/pdv';
        }
        return this.basePath() + '/pdv/' + encodeURIComponent($('#PDVID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "PDVDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#PDVID').val(),
            "naziv": $('#PDVNaziv').val(),

        });
    },
    // Deletes a PDV.
    deletePDV: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createPDVURL('DELETE'),
            type: 'DELETE',
            success: function () {
                PDVManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newPDV: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#PDVListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#PDVDetailPanel').show();
        $('#PDVID').attr('value', null);
        $('#PDVNaziv').attr('value', null);


    },

    // Saves a PDV. If there is no value in the #PDVID hidden field then
    // a new PDV is created by "POST" request. Otherwise an existing PDV
    // is updated with "PUT" request.
    savePDV: function () {
        if (!confirm('Save?')) return
        var requestType = $('#PDVID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createPDVURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                PDVManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    PDVManager.showPDVsList();
    $('#PDVListPanel').show();
    $('#PDVDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        PDVManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        PDVManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        PDVManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        PDVManager.newPDV();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        PDVManager.savePDV();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        PDVManager.deletePDV();
    });
    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
         $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#PDVListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: 'http://localhost:8080/api/pdv/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (pdv) {

                $('#PDVDetailPanel').show();

                $('#PDVID').val(pdv.id);
                $('#PDVNaziv').val(pdv.naziv);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    });
});