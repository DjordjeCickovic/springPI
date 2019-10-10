var pageNumber = 0;
var FakturaManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadPoslovneGodine: function () {
        $.ajax({
            url: this.basePath() + '/poslovna-godina',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, poslovnaGodina) {
                    $('<option>').text(poslovnaGodina.godina)
                        .attr('value', poslovnaGodina.id).appendTo($('#FakturaPoslovnaGodina'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadPoslovniPartneri: function () {
        $.ajax({
            url: this.basePath() + '/poslovni-partner',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, poslovniPartner) {
                    $('<option>').text(poslovniPartner.naziv)
                        .attr('value', poslovniPartner.id).appendTo($('#FakturaPoslovniPartner'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadPreduzece: function () {
        $.ajax({
            url: this.basePath() + '/preduzece',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, preduzece) {
                    $('<option>').text(preduzece.naziv)
                        .attr('value', preduzece.id).appendTo($('#FakturaPreduzece'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showFakturasList: function () {
        $.ajax({

            url: this.basePath() + '/faktura/pageable?page=' + pageNumber ,
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
                $('#customTable').append('<tr><th>Broj fakture</th><th>Datum fakture</th><th>Datum valute</th><th>Iznos za placanje</th><th>Osnovica</th><th>Ukupan PDV</th><th>Poslovni partner</th><th>Poslovna godina</th><th>Preduzece</th><th>Edit</th><th>Dodaj stavku</th></tr>');
                $.each(list, function (index, faktura) {
                    $('#customTable tr:last').after('<tr><td>' + faktura.id + '</td><td>' + faktura.datumFakture + '</td><td>' + faktura.datumValute + '</td><td>' + faktura.iznosZaPlacanje + '</td><td>' + faktura.osnovica  + '</td><td>' + faktura.ukupanPDV + '</td><td>' + faktura.poslovniPartner.naziv + '</td><td>' + faktura.poslovnaGodina.godina + '</td><td>' + faktura.preduzece.naziv + '</td><td><a href=\'javascript:FakturaManager.showFakturasDetail("' + faktura.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td><td>'+ '<a href="http://localhost:8080/stavkaFakture.html"><img src="http://skr.rs/hHM"style="width:25px;height:25px;"></a>' +'</td></tr>');
                });

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with Faktura details,
    showFakturasDetail: function (FakturaId) {
        if (FakturaId == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#FakturasListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/faktura/' + FakturaId,
            cache: false,
            dataType: 'json',
            success: function (faktura) {

                $('#FakturasDetailPanel').show();

                $('#FakturaId').val(faktura.id);
                $('#FakturaDatumFakture').val(faktura.datumFakture);
                $('#FakturaDatumFakture').focus();
                $('#FakturaDatumValute').val(faktura.datumValute);
                $('#FakturaIznosZaPlacanje').val(faktura.iznosZaPlacanje);
                $('#FakturaOsnovica').val(faktura.osnovica);
                $('#FakturaUkupanPdv').val(faktura.ukupanPDV);
                $('#FakturaPoslovnaGodina').val(faktura.poslovnaGodina.id);
                $('#FakturaPoslovniPartner').val(faktura.poslovniPartner.id);
                $('#FakturaPreduzece').val(faktura.preduzece.id);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#FakturasDetailPanel').hide();
        $('#FakturasListPanel').show();
        FakturaManager.showFakturasList();
        $('#FakturasListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showFakturasList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
     	   	FakturaManager.refreshList()
        }
     },
     nextPage: function () {
     	pageNumber += 1;
 	   	FakturaManager.refreshList()
      },

    // Formats a URI of a Faktura
    createFakturaURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/faktura';
        }
        return this.basePath() + '/faktura/' + encodeURIComponent($('#FakturaId').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "FakturaDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#FakturaId').val(),
            "datumFakture": $('#FakturaDatumFakture').val(),
            "datumValute": $('#FakturaDatumValute').val(),
            "iznosZaPlacanje": $('#FakturaIznosZaPlacanje').val(),
            "osnovica": $('#FakturaOsnovica').val(),
            "ukupanPDV": $('#FakturaUkupanPdv').val(),
            "poslovnaGodina": {
                id: $('#FakturaPoslovnaGodina').val()
            },
            "poslovniPartner": {
                id: $('#FakturaPoslovniPartner').val()
            },
            "preduzece": {
                id: $('#FakturaPreduzece').val()
            }

        });
    },
    // Deletes a Faktura.
    deleteFaktura: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createFakturaURL('DELETE'),
            type: 'DELETE',
            success: function () {
                FakturaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newFaktura: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#FakturasListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#FakturasDetailPanel').show();
        $('#FakturaId').attr('value', null);
        $('#FakturaDatumFakture').attr('value', null).focus().select();;
        $('#FakturaDatumValute').attr('value', null);
        $('#FakturaIznosZaPlacanje').attr('value', null);
        $('#FakturaOsnovica').attr('value', null);
        $('#FakturaUkupanPdv').attr('value', null);
        $('#FakturaPoslovnaGodina').attr('value', null);
        $('#FakturaPoslovniPartner').attr('value', null);
        $('#FakturaPreduzece').attr('value', null);


    },

    // Saves a Faktura. If there is no value in the #FakturaID hidden field then
    // a new Faktura is created by "POST" request. Otherwise an existing Faktura
    // is updated with "PUT" request.
    saveFaktura: function () {
        if (!confirm('Save?')) return
        var requestType = $('#FakturaId').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createFakturaURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                FakturaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};

//proba za dodavanje stavke fakture iz fakture

//1.dodavanje dugmeta newStavkaFakture
//
//
//
//




//proba za dodavanje stavke fakture iz fakture

$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
	
    FakturaManager.loadPoslovneGodine();
    FakturaManager.loadPreduzece();
    FakturaManager.loadPoslovniPartneri();
    FakturaManager.showFakturasList();
    $('#opseg1').hide();
    $('#opseg2').hide();
    
    $('#FakturasListPanel').show();
    $('#FakturasDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        FakturaManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        FakturaManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        FakturaManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        FakturaManager.newFaktura();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        FakturaManager.saveFaktura();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        FakturaManager.deleteFaktura();
    });


    $("#tipPretrage").change(function () {
        var selektovanaVrednost = $('#tipPretrage').val();
        $("#terminPretrage").val('');
        $('#opseg1').val('');
        $('#opseg2').val('');

        //alert( selektovanaVrednost );
        if (selektovanaVrednost == 'iznosZaPlacanje' || selektovanaVrednost == 'ukupanPdv' ||
            selektovanaVrednost == 'osnovica' || selektovanaVrednost == 'datumValute' ||
            selektovanaVrednost == 'datumFakture') {
            $('#terminPretrage').hide();
            $('#opseg1').show();
            $('#opseg2').show();

        } else {
            $('#terminPretrage').show();
            $('#opseg1').hide();
            $('#opseg2').hide();
        }


    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
        var opseg1 = $('#opseg1').val();
        var opseg2 = $('#opseg2').val();
         $('#PreviousButton').hide();
        $('#NextButton').hide();
        if (terminPretrage == '' && opseg1 !== '' && opseg2 !== '') {
            $.ajax({
                url: 'http://localhost:8080/api/faktura/' + tipPretrage + '/' + opseg1 + '/' + opseg2,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Broj fakture</th><th>Datum fakture</th><th>Datum valute</th><th>Iznos za placanje</th><th>Osnovica</th><th>Ukupan PDV</th><th>Poslovni partner</th><th>Poslovna godina</th><th>Preduzece</th><th>Edit</th></tr>');

                    $.each(list, function (index, faktura) {
                        $('#customTable tr:last').after('<tr><td>' + faktura.id + '</td><td>' + faktura.datumFakture + '</td><td>' + faktura.datumValute + '</td><td>' + faktura.iznosZaPlacanje + '</td><td>' + faktura.osnovica +  '</td><td>' + faktura.ukupanPDV + '</td><td>' + faktura.poslovniPartner.naziv + '</td><td>' + faktura.poslovnaGodina.godina + '</td><td>' + faktura.preduzece.naziv + '</td><td><a href=\'javascript:FakturaManager.showFakturasDetail("' + faktura.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '' && tipPretrage !== 'brojFakture') {
            $.ajax({
                url: 'http://localhost:8080/api/faktura/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Broj fakture</th><th>Datum fakture</th><th>Datum valute</th><th>Iznos za placanje</th><th>Osnovica</th><th>Ukupan PDV</th><th>Poslovni partner</th><th>Poslovna godina</th><th>Preduzece</th><th>Edit</th></tr>');

                    $.each(list, function (index, faktura) {
                        $('#customTable tr:last').after('<tr><td>' + faktura.id + '</td><td>' + faktura.datumFakture + '</td><td>' + faktura.datumValute + '</td><td>' + faktura.iznosZaPlacanje + '</td><td>' + faktura.osnovica  + '</td><td>' + faktura.ukupanPDV + '</td><td>' + faktura.poslovniPartner.naziv + '</td><td>' + faktura.poslovnaGodina.godina + '</td><td>' + faktura.preduzece.naziv + '</td><td><a href=\'javascript:FakturaManager.showFakturasDetail("' + faktura.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '' && tipPretrage == 'brojFakture') {
            $('#FakturasListPanel').hide();
            $('#NewButton').hide();
            $('#BackToListButton').show();
            $('#SaveButton').show();
            $('#RemoveButton').show();
            $.ajax({
                url: 'http://localhost:8080/api/faktura/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (faktura) {

                    $('#FakturasDetailPanel').show();

                    $('#FakturaId').val(faktura.id);
                    $('#FakturaDatumFakture').val(faktura.datumFakture);
                    $('#FakturaDatumFakture').focus();
                    $('#FakturaDatumValute').val(faktura.datumValute);
                    $('#FakturaIznosZaPlacanje').val(faktura.iznosZaPlacanje);
                    $('#FakturaOsnovica').val(faktura.osnovica);
                    $('#FakturaUkupanPdv').val(faktura.ukupanPDV);
                    $('#FakturaPoslovnaGodina').val(faktura.poslovnaGodina.id);
                    $('#FakturaPoslovniPartner').val(faktura.poslovniPartner.id);
                    $('#FakturaPreduzece').val(faktura.preduzece.id);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(textStatus);
                }
            });
        }
    });
});