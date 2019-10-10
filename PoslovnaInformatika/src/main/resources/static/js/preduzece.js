var pageNumber = 0;
var PreduzeceManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadMesta: function () {
        $.ajax({
            url: this.basePath() + '/mesto',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, mesto) {
                    $('<option>').text(mesto.grad)
                        .attr('value', mesto.id).appendTo($('#PreduzeceMesto'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showPreduzecesList: function () {
        $.ajax({

            url: this.basePath() + '/preduzece/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Naziv</th><th>PIB</th><th>Email</th><th>Telefon</th><th>Adresa</th><th>Mesto</th><th>Edit</th></tr>');
                $.each(list, function (index, preduzece) {
                    $('#customTable tr:last').after('<tr><td>' + preduzece.naziv + '</td><td>' + preduzece.pib + '</td><td>' + preduzece.email + '</td><td>' + preduzece.telefon + '</td><td>' + preduzece.adresa + '</td><td>' + preduzece.mesto.grad + '</td><td><a href=\'javascript:PreduzeceManager.showPreduzecesDetail("' + preduzece.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with Preduzece details,
    showPreduzecesDetail: function (PreduzeceID) {
        if (PreduzeceID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#PreduzecesListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/preduzece/' + PreduzeceID,
            cache: false,
            dataType: 'json',
            success: function (preduzece) {

                $('#PreduzecesDetailPanel').show();
                $('#PreduzeceID').val(preduzece.id);
                $('#PreduzeceNaziv').val(preduzece.naziv);
                $('#PreduzecePib').val(preduzece.pib);
                $('#PreduzeceAdresa').val(preduzece.adresa);
                $('#PreduzeceEmail').val(preduzece.email);
                $('#PreduzeceTelefon').val(preduzece.telefon);
                $('#PreduzeceMesto').val(preduzece.mesto.id);
                $('#PreduzeceAdresa').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#PreduzecesDetailPanel').hide();
        $('#PreduzecesListPanel').show();
        PreduzeceManager.showPreduzecesList();
        $('#PreduzecesListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showPreduzecesList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            PreduzeceManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	PreduzeceManager.refreshList();
      },

    // Formats a URI of a Preduzece
    createPreduzeceURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/preduzece';
        }
        return this.basePath() + '/preduzece/' + encodeURIComponent($('#PreduzeceID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "PreduzeceDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#PreduzeceID').val(),
            "naziv": $('#PreduzeceNaziv').val(),
            "pib": $('#PreduzecePib').val(),
            "adresa": $('#PreduzeceAdresa').val(),
            "email": $('#PreduzeceEmail').val(),
            "telefon": $('#PreduzeceTelefon').val(),
            "mesto": {
                id: $('#PreduzeceMesto').val()
            },

        });

    },
    // Deletes a Preduzece.
    deletePreduzece: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createPreduzeceURL('DELETE'),
            type: 'DELETE',
            success: function () {
                PreduzeceManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newPreduzece: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#PreduzecesListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#PreduzecesDetailPanel').show();
        $('#PreduzeceID').attr('value', null);
        $('#PreduzeceAdresa').attr('value', null).focus().select();
        $('#PreduzeceEmail').attr('value', null);
        $('#PreduzeceNaziv').attr('value', null);
        $('#PreduzecePib').attr('value', null);
        $('#PreduzeceTelefon').attr('value', null);
        $('#PreduzeceMesto').attr('value', null);


    },

    // Saves a Preduzece. If there is no value in the #PreduzeceID hidden field then
    // a new Preduzece is created by "POST" request. Otherwise an existing Preduzece
    // is updated with "PUT" request.
    savePreduzece: function () {
        if (!confirm('Save?')) return
        var requestType = $('#PreduzeceID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createPreduzeceURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                PreduzeceManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    PreduzeceManager.showPreduzecesList();
    PreduzeceManager.loadMesta();
    $('#PreduzecesListPanel').show();
    $('#PreduzecesDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.newPreduzece();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.savePreduzece();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        PreduzeceManager.deletePreduzece();
    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
        $('#PreviousButton').hide();
        $('#NextButton').hide();

        if (tipPretrage == 'adresa' || tipPretrage == 'telefon' || tipPretrage == 'mesto') {
            $.ajax({
                url: 'http://localhost:8080/api/preduzece/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Naziv</th><th>PIB</th><th>Email</th><th>Telefon</th><th>Adresa</th><th>Mesto</th><th>Edit</th></tr>');
                    $.each(list, function (index, preduzece) {
                        $('#customTable tr:last').after('<tr><td>' + preduzece.naziv + '</td><td>' + preduzece.pib + '</td><td>' + preduzece.email + '</td><td>' + preduzece.telefon + '</td><td>' + preduzece.adresa + '</td><td>' + preduzece.mesto.grad + '</td><td><a href=\'javascript:PreduzeceManager.showPreduzecesDetail("' + preduzece.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (tipPretrage == 'naziv' || tipPretrage == 'pib') {
            $('#PreduzecesListPanel').hide();
            $('#NewButton').hide();
            $('#BackToListButton').show();
            $('#SaveButton').show();
            $('#RemoveButton').show();
            $.ajax({
                url: 'http://localhost:8080/api/preduzece/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (preduzece) {
                    $('#PreduzecesDetailPanel').show();
                    $('#PreduzeceID').val(preduzece.id);
                    $('#PreduzeceAdresa').val(preduzece.adresa);
                    $('#PreduzeceEmail').val(preduzece.email);
                    $('#PreduzeceNaziv').val(preduzece.naziv);
                    $('#PreduzecePib').val(preduzece.pib);
                    $('#PreduzeceTelefon').val(preduzece.telefon);
                    $('#PreduzeceMesto').val(preduzece.mesto.id);
                    $('#PreduzeceAdresa').focus();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });


        }
    });
});