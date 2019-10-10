var pageNumber = 0;
var GrupaRobeManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadPreduzece: function () {
        $.ajax({
            url: this.basePath() + '/preduzece',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, preduzece) {
                    $('<option>').text(preduzece.naziv)
                        .attr('value', preduzece.id).appendTo($('#GrupaRobePreduzece'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadPdv: function () {
        $.ajax({
            url: this.basePath() + '/pdv',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, pdv) {
                    $('<option>').text(pdv.naziv)
                        .attr('value', pdv.id).appendTo($('#GrupaRobePdv'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showGrupaRobesList: function () {
        $.ajax({

            url: this.basePath() + '/grupa-robe/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Naziv</th><th>PDV</th><th>Preduzece</th><th>Edit</th></tr>');
                $.each(list, function (index, grupaRobe) {
                    $('#customTable tr:last').after('<tr><td>' + grupaRobe.naziv + '</td><td>' + grupaRobe.pdv.naziv + '</td><td>' + grupaRobe.preduzece.naziv + '</td><td><a href=\'javascript:GrupaRobeManager.showGrupaRobesDetail("' + grupaRobe.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with GrupaRobe details,
    showGrupaRobesDetail: function (GrupaRobeId) {
        if (GrupaRobeId == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#GrupaRobesListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/grupa-robe/' + GrupaRobeId,
            cache: false,
            dataType: 'json',
            success: function (grupaRobe) {

                $('#GrupaRobesDetailPanel').show();

                $('#GrupaRobeID').val(grupaRobe.id);
                $('#GrupaRobeNaziv').val(grupaRobe.naziv);
                $('#GrupaRobePreduzece').val(grupaRobe.preduzece.id);
                $('#GrupaRobePdv').val(grupaRobe.pdv.id);

                $('#GrupaRobeNaziv').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#GrupaRobesDetailPanel').hide();
        $('#GrupaRobesListPanel').show();
        GrupaRobeManager.showGrupaRobesList();
        $('#GrupaRobesListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showGrupaRobesList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            GrupaRobeManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	GrupaRobeManager.refreshList();
      },

    // Formats a URI of a GrupaRobe
    createGrupaRobeURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/grupa-robe';
        }
        return this.basePath() + '/grupa-robe/' + encodeURIComponent($('#GrupaRobeID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "GrupaRobeDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#GrupaRobeID').val(),
            "naziv": $('#GrupaRobeNaziv').val(),
            "preduzece": {
                id: $('#GrupaRobePreduzece').val()
            },
            "pdv": {
                id: $('#GrupaRobePdv').val()
            },

        });
    },
    // Deletes a GrupaRobe.
    deleteGrupaRobe: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createGrupaRobeURL('DELETE'),
            type: 'DELETE',
            success: function () {
                GrupaRobeManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newGrupaRobe: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#GrupaRobesListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#GrupaRobesDetailPanel').show();
        $('#GrupaRobeID').attr('value', null);
        $('#GrupaRobeNaziv').attr('value', 'Naziv').focus().select();
        $('#GrupaRobePreduzece').attr('value', null);
        $('#GrupaRobePdv').attr('value', null);


    },

    // Saves a GrupaRobe. If there is no value in the #GrupaRobeID hidden field then
    // a new GrupaRobe is created by "POST" request. Otherwise an existing GrupaRobe
    // is updated with "PUT" request.
    saveGrupaRobe: function () {
        if (!confirm('Save?')) return
        var requestType = $('#GrupaRobeID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createGrupaRobeURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                GrupaRobeManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    GrupaRobeManager.showGrupaRobesList();
    GrupaRobeManager.loadPreduzece();
    GrupaRobeManager.loadPdv();
    $('#GrupaRobesListPanel').show();
    $('#GrupaRobesDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.newGrupaRobe();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.saveGrupaRobe();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        GrupaRobeManager.deleteGrupaRobe();
    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
         $('#PreviousButton').hide();
        $('#NextButton').hide();
        $.ajax({

            url: 'http://localhost:8080/api/grupa-robe/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (list) {
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>Naziv</th><th>PDV</th><th>Preduzece</th><th>Edit</th></tr>');
                if (tipPretrage == 'preduzece') {

                    $.each(list, function (index, grupaRobe) {
                        $('#customTable tr:last').after('<tr><td>' + grupaRobe.naziv + '</td><td>' + grupaRobe.pdv.naziv + '</td><td>' + grupaRobe.preduzece.naziv + '</td><td><a href=\'javascript:GrupaRobeManager.showGrupaRobesDetail("' + grupaRobe.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });

                }
                if (tipPretrage == 'pdv') {
                    $.each(list, function (index, grupaRobe) {
                        $('#customTable tr:last').after('<tr><td>' + grupaRobe.naziv + '</td><td>' + grupaRobe.pdv.naziv + '</td><td>' + grupaRobe.preduzece.naziv + '</td><td><a href=\'javascript:GrupaRobeManager.showGrupaRobesDetail("' + grupaRobe.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                }

                if (tipPretrage == 'naziv') {
                    $('#GrupaRobesListPanel').hide();
                    $('#NewButton').hide();
                    $('#BackToListButton').show();
                    $('#SaveButton').show();
                    $('#RemoveButton').show();
                    $.ajax({
                        url: 'http://localhost:8080/api/grupa-robe/' + tipPretrage + '/' + terminPretrage,
                        cache: false,
                        dataType: 'json',
                        success: function (grupaRobe) {

                            $('#GrupaRobesDetailPanel').show();

                            $('#GrupaRobeID').val(grupaRobe.id);
                            $('#GrupaRobeNaziv').val(grupaRobe.naziv);
                            $('#GrupaRobePreduzece').val(grupaRobe.preduzece.id);
                            $('#GrupaRobePdv').val(grupaRobe.pdv.id);

                            $('#GrupaRobeNaziv').focus();
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert(textStatus);
                        }
                    });
                }


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });

    });
});