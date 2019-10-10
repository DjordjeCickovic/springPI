var pageNumber = 0;
var PoslovniPartnerManager = {
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
                        .attr('value', preduzece.id).appendTo($('#PoslovniPartnerPreduzece'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadMesta: function () {
        $.ajax({
            url: this.basePath() + '/mesto',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, mesto) {
                    $('<option>').text(mesto.grad)
                        .attr('value', mesto.id).appendTo($('#PoslovniPartnerMesto'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showPoslovniPartnersList: function () {
        $.ajax({

            url: this.basePath() + '/poslovni-partner/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Naziv</th><th>Vrsta</th><th>Preduzece</th><th>Adresa</th><th>Mesto</th><th>Edit</th></tr>');
                $.each(list, function (index, poslovniPartner) {
                    $('#customTable tr:last').after('<tr><td>' + poslovniPartner.naziv + '</td><td>' + poslovniPartner.vrsta + '</td><td>' + poslovniPartner.preduzece.naziv + '</td><td>' + poslovniPartner.adresa + '</td><td>' + poslovniPartner.mesto.grad + '</td><td><a href=\'javascript:PoslovniPartnerManager.showPoslovniPartnersDetail("' + poslovniPartner.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with PoslovniPartner details,
    showPoslovniPartnersDetail: function (PoslovniPartnerID) {
        if (PoslovniPartnerID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#PoslovniPartnersListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/poslovni-partner/' + PoslovniPartnerID,
            cache: false,
            dataType: 'json',
            success: function (poslovniPartner) {

                $('#PoslovniPartnersDetailPanel').show();

                $('#PoslovniPartnerID').val(poslovniPartner.id);
                $('#PoslovniPartnerNaziv').val(poslovniPartner.naziv);
                $('#PoslovniPartnerVrsta').val(poslovniPartner.vrsta);
                $('#PoslovniPartnerPreduzece').val(poslovniPartner.preduzece.id);
                $('#PoslovniPartnerAdresa').val(poslovniPartner.adresa);
                $('#PoslovniPartnerMesto').val(poslovniPartner.mesto.id);
                $('#PoslovniPartnerNaziv').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#PoslovniPartnersDetailPanel').hide();
        $('#PoslovniPartnersListPanel').show();
        PoslovniPartnerManager.showPoslovniPartnersList();
        $('#PoslovniPartnersListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showPoslovniPartnersList();
    },

    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            PoslovniPartnerManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	PoslovniPartnerManager.refreshList();
      },
    
    // Formats a URI of a PoslovniPartner
    createPoslovniPartnerURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/poslovni-partner';
        }
        return this.basePath() + '/poslovni-partner/' + encodeURIComponent($('#PoslovniPartnerID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "PoslovniPartnerDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#PoslovniPartnerID').val(),
            "naziv": $('#PoslovniPartnerNaziv').val(),
            "adresa": $('#PoslovniPartnerAdresa').val(),
            "vrsta": $('#PoslovniPartnerVrsta').val(),
            "mesto": {
                id: $('#PoslovniPartnerMesto').val()
            },
            "preduzece": {
                id: $('#PoslovniPartnerPreduzece').val()
            },
        });
    },
    // Deletes a PoslovniPartner.
    deletePoslovniPartner: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createPoslovniPartnerURL('DELETE'),
            type: 'DELETE',
            success: function () {
                PoslovniPartnerManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newPoslovniPartner: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#PoslovniPartnersListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#PoslovniPartnersDetailPanel').show();
        $('#PoslovniPartnerID').attr('value', null);
        $('#PoslovniPartnerNaziv').attr('value', null).focus().select();
        $('#PoslovniPartnerAdresa').attr('value', null);
        $('#PoslovniPartnerVrsta').attr('value', null);
        $('#PoslovniPartnerMesto').attr('value', null);
        $('#PoslovniPartnerPreduzece').attr('value', null);

    },

    // Saves a PoslovniPartner. If there is no value in the #PoslovniPartnerID hidden field then
    // a new PoslovniPartner is created by "POST" request. Otherwise an existing PoslovniPartner
    // is updated with "PUT" request.
    savePoslovniPartner: function () {
        if (!confirm('Save?')) return
        var requestType = $('#PoslovniPartnerID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createPoslovniPartnerURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                PoslovniPartnerManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    PoslovniPartnerManager.showPoslovniPartnersList();
    PoslovniPartnerManager.loadPreduzece();
    PoslovniPartnerManager.loadMesta();
    $('#PoslovniPartnersListPanel').show();
    $('#PoslovniPartnersDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.newPoslovniPartner();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.savePoslovniPartner();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        PoslovniPartnerManager.deletePoslovniPartner();
    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        if (tipPretrage == 'naziv') {
            $('#PoslovniPartnersListPanel').hide();
            $('#NewButton').hide();
            $('#BackToListButton').show();
            $('#SaveButton').show();
            $('#RemoveButton').show();
            $.ajax({
                url: 'http://localhost:8080/api/poslovni-partner/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (poslovniPartner) {


                    $('#PoslovniPartnersDetailPanel').show();
                    $('#PoslovniPartnerID').val(poslovniPartner.id);
                    $('#PoslovniPartnerNaziv').val(poslovniPartner.naziv);
                    $('#PoslovniPartnerAdresa').val(poslovniPartner.adresa);
                    $('#PoslovniPartnerVrsta').val(poslovniPartner.vrsta);
                    $('#PoslovniPartnerMesto').val(poslovniPartner.mesto.id);
                    $('#PoslovniPartnerPreduzece').val(poslovniPartner.preduzece.id);
                    $('#PoslovniPartnerNaziv').focus();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        }
        $.ajax({

            url: 'http://localhost:8080/api/poslovni-partner/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (list) {
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>Naziv</th><th>Vrsta</th><th>Preduzece</th><th>Adresa</th><th>Mesto</th><th>Edit</th></tr>');
                $.each(list, function (index, poslovniPartner) {
                    $('#customTable tr:last').after('<tr><td>' + poslovniPartner.naziv + '</td><td>' + poslovniPartner.vrsta + '</td><td>' + poslovniPartner.preduzece.naziv + '</td><td>' + poslovniPartner.adresa + '</td><td>' + poslovniPartner.mesto.grad + '</td><td><a href=\'javascript:PoslovniPartnerManager.showPoslovniPartnersDetail("' + poslovniPartner.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });

    });
});