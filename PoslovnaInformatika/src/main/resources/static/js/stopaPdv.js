var pageNumber = 0;
var StopaPDVManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadPdv: function () {
        $.ajax({
            url: this.basePath() + '/pdv',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, pdv) {
                    $('<option>').text(pdv.naziv)
                        .attr('value', pdv.id).appendTo($('#StopaPDVPDV'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showStopaPDVsList: function () {
        $.ajax({

            url: this.basePath() + '/stopa-pdv/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>DatumVazenja</th><th>Procenat</th><th>PDV</th><th>Edit</th></tr>');
                $.each(list, function (index, stopaPDV) {
                    $('#customTable tr:last').after('<tr><td>' + stopaPDV.datumVazenja + '</td><td>' + stopaPDV.procenat + '</td><td>' + stopaPDV.pdv.naziv + '</td><td><a href=\'javascript:StopaPDVManager.showStopaPDVsDetail("' + stopaPDV.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with StopaPDV details,
    showStopaPDVsDetail: function (StopaPDVId) {
        if (StopaPDVId == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#StopaPDVsListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/stopa-pdv/' + StopaPDVId,
            cache: false,
            dataType: 'json',
            success: function (stopaPDV) {

                $('#StopaPDVsDetailPanel').show();

                $('#StopaPDVID').val(stopaPDV.id);
                $('#StopaPDVDatumVazenja').val(stopaPDV.datumVazenja);
                $('#StopaPDVProcenat').val(stopaPDV.procenat);
                $('#StopaPDVPDV').val(stopaPDV.pdv.id);
                $('#StopaPDVDatumVazenja').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#StopaPDVsDetailPanel').hide();
        $('#StopaPDVsListPanel').show();
        StopaPDVManager.showStopaPDVsList();
        $('#StopaPDVsListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showStopaPDVsList();
    },

    // Formats a URI of a StopaPDV
    createStopaPDVURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/stopa-pdv';
        }
        return this.basePath() + '/stopa-pdv/' + encodeURIComponent($('#StopaPDVID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "StopaPDVDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#StopaPDVID').val(),
            "datumVazenja": $('#StopaPDVDatumVazenja').val(),
            "procenat": $('#StopaPDVProcenat').val(),
            "pdv": {
                id: $('#StopaPDVPDV').val()
            },
        });
    },
    // Deletes a StopaPDV.
    deleteStopaPDV: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createStopaPDVURL('DELETE'),
            type: 'DELETE',
            success: function () {
                StopaPDVManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newStopaPDV: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#StopaPDVsListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#StopaPDVsDetailPanel').show();
        $('#StopaPDVID').attr('value', null);
        $('#StopaPDVDatumVazenja').attr('value', null).focus().select();
        $('#StopaPDVProcenat').attr('value', null);
        $('#StopaPDVPDV').attr('value', null);

    },
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            StopaPDVManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	StopaPDVManager.refreshList();
      },

    // Saves a StopaPDV. If there is no value in the #StopaPDVID hidden field then
    // a new StopaPDV is created by "POST" request. Otherwise an existing StopaPDV
    // is updated with "PUT" request.
    saveStopaPDV: function () {
        if (!confirm('Save?')) return
        var requestType = $('#StopaPDVID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createStopaPDVURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                StopaPDVManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable

    StopaPDVManager.showStopaPDVsList();
    StopaPDVManager.loadPdv();
    $('#StopaPDVsListPanel').show();
    $('#StopaPDVsDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.newStopaPDV();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.saveStopaPDV();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        StopaPDVManager.deleteStopaPDV();
    });

    $("#pretraziButton").click(function () {
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
        $.ajax({

            url: 'http://localhost:8080/api/stopa-pdv/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (list) {
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>DatumVazenja</th><th>Procenat</th><th>PDV</th><th>Edit</th></tr>');
                $.each(list, function (index, stopaPDV) {
                    $('#customTable tr:last').after('<tr><td>' + stopaPDV.datumVazenja + '</td><td>' + stopaPDV.procenat + '</td><td>' + stopaPDV.pdv.naziv + '</td><td><a href=\'javascript:StopaPDVManager.showStopaPDVsDetail("' + stopaPDV.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });

    });
});