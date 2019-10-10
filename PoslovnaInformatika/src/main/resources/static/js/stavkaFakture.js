var pageNumber = 0;
var StavkaFaktureManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadRoba: function () {
        $.ajax({
            url: this.basePath() + '/roba',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, roba) {
                    $('<option>').text(roba.naziv)
                        .attr('value', roba.id).appendTo($('#StavkaFaktureRoba'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadFaktura: function () {
        $.ajax({
            url: this.basePath() + '/faktura',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, faktura) {
                    $('<option>').text(faktura.id)
                        .attr('value', faktura.id).appendTo($('#StavkaFaktureFaktura'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showStavkaFakturesList: function () {
        $.ajax({

            url: this.basePath() + '/stavka-fakture/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Kolicina</th><th>Jedinicna cena</th><th>Rabat</th><th>Osnovica za PDV</th><th>PDV %</th><th>Iznos PDV</th><th>Iznos stavke</th><th>Roba</th><th>Broj fakture</th><th>Edit</th></tr>');
                $.each(list, function (index, stavkaFakture) {
                    $('#customTable tr:last').after('<tr><td>' + stavkaFakture.kolicina + '</td><td>' + stavkaFakture.jedinicnaCena + '</td><td>' + stavkaFakture.rabat +
                        '</td><td>' + stavkaFakture.osnovicaZaPDV +
                        '</td><td>' + stavkaFakture.procenatPDV +
                        '</td><td>' + stavkaFakture.iznosPDV +
                        '</td><td>' + stavkaFakture.iznosStavke +
                        '</td><td>' + stavkaFakture.roba.naziv +
                        '</td><td>' + stavkaFakture.faktura.id +
                        '</td><td><a href=\'javascript:StavkaFaktureManager.showStavkaFakturesDetail("' + stavkaFakture.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with StavkaFakture details,
    showStavkaFakturesDetail: function (StavkaFaktureID) {
        if (StavkaFaktureID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#StavkaFakturesListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/stavka-fakture/' + StavkaFaktureID,
            cache: false,
            dataType: 'json',
            success: function (stavkaFakture) {

                $('#StavkaFakturesDetailPanel').show();
                $('#SaveButton').show();
                $('#StavkaFaktureID').val(stavkaFakture.id);
                $('#StavkaFaktureKolicina').val(stavkaFakture.kolicina);
                $('#StavkaFaktureKolicina').focus();
                $('#StavkaFaktureRoba').val(stavkaFakture.roba.id);
                $('#StavkaFaktureFaktura').val(stavkaFakture.faktura.id);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#StavkaFakturesDetailPanel').hide();
        $('#StavkaFakturesListPanel').show();
        StavkaFaktureManager.showStavkaFakturesList();
        $('#StavkaFakturesListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showStavkaFakturesList();
    },

    // Formats a URI of a StavkaFakture
    createStavkaFaktureURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/stavka-fakture';
        }
        return this.basePath() + '/stavka-fakture/' + encodeURIComponent($('#StavkaFaktureID').val());
    },

    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            StavkaFaktureManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
            StavkaFaktureManager.refreshList();
      },
    
    // Creates an object with the properties retrieved from the input fields 
    // of the "StavkaFaktureDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#StavkaFaktureID').val(),

            "kolicina": $('#StavkaFaktureKolicina').val(),
            "roba": {
                id: $('#StavkaFaktureRoba').val()
            },
            "faktura": {
                id: $('#StavkaFaktureFaktura').val()
            }

        });
    },
    // Deletes a StavkaFakture.
    deleteStavkaFakture: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createStavkaFaktureURL('DELETE'),
            type: 'DELETE',
            success: function () {
                StavkaFaktureManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newStavkaFakture: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#StavkaFakturesListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#StavkaFakturesDetailPanel').show();
        $('#StavkaFaktureID').attr('value', null);
        $('#StavkaFaktureKolicina').attr('value', null);
        $('#StavkaFaktureRoba').attr('value', null);
        $('#StavkaFaktureFaktura').attr('value', null);

    },

    // Saves a StavkaFakture. If there is no value in the #StavkaFaktureID hidden field then
    // a new StavkaFakture is created by "POST" request. Otherwise an existing StavkaFakture
    // is updated with "PUT" request.
    saveStavkaFakture: function () {
        if (!confirm('Save?')) return
        var requestType = $('#StavkaFaktureID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createStavkaFaktureURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                StavkaFaktureManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    StavkaFaktureManager.showStavkaFakturesList();
    StavkaFaktureManager.loadRoba();
    StavkaFaktureManager.loadFaktura();
    $('#opseg1').hide();
    $('#opseg2').hide();

    $('#StavkaFakturesListPanel').show();
    $('#StavkaFakturesDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.newStavkaFakture();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.saveStavkaFakture();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        StavkaFaktureManager.deleteStavkaFakture();
    });
    $("#tipPretrage").change(function () {
        var selektovanaVrednost = $('#tipPretrage').val();
        $("#terminPretrage").val('');
        $('#opseg1').val('');
        $('#opseg2').val('');

        if (selektovanaVrednost == 'kolicina' || selektovanaVrednost == 'cena' ||
            selektovanaVrednost == 'rabat' || selektovanaVrednost == 'osnovica' ||
            selektovanaVrednost == 'pdvProcenat' || selektovanaVrednost == 'pdv' || selektovanaVrednost == 'iznosStavke') {
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
                url: 'http://localhost:8080/api/stavka-fakture/' + tipPretrage + '/' + opseg1 + '/' + opseg2,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Kolicina</th><th>Jedinicna cena</th><th>Rabat</th><th>Osnovica za PDV</th><th>PDV %</th><th>Iznos PDV</th><th>Iznos stavke</th><th>Roba</th><th>Broj fakture</th><th>Edit</th></tr>');
                    $.each(list, function (index, stavkaFakture) {
                        $('#customTable tr:last').after('<tr><td>' + stavkaFakture.kolicina + '</td><td>' + stavkaFakture.jedinicnaCena + '</td><td>' + stavkaFakture.rabat +
                            '</td><td>' + stavkaFakture.osnovicaZaPDV +
                            '</td><td>' + stavkaFakture.procenatPDV +
                            '</td><td>' + stavkaFakture.iznosPDV +
                            '</td><td>' + stavkaFakture.iznosStavke +
                            '</td><td>' + stavkaFakture.roba.naziv +
                            '</td><td>' + stavkaFakture.faktura.id +
                            '</td><td><a href=\'javascript:StavkaFaktureManager.showStavkaFakturesDetail("' + stavkaFakture.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '') {
            $.ajax({
                url: 'http://localhost:8080/api/stavka-fakture/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                $('#customTable').append('<tr><th>Kolicina</th><th>Jedinicna cena</th><th>Rabat</th><th>Osnovica za PDV</th><th>PDV %</th><th>Iznos PDV</th><th>Iznos stavke</th><th>Roba</th><th>Broj fakture</th><th>Edit</th></tr>');

                    
                    $.each(list, function (index, stavkaFakture) {
                        $('#customTable tr:last').after('<tr><td>' + stavkaFakture.kolicina + '</td><td>' + stavkaFakture.jedinicnaCena + '</td><td>' + stavkaFakture.rabat +
                            '</td><td>' + stavkaFakture.osnovicaZaPDV +
                            '</td><td>' + stavkaFakture.procenatPDV +
                            '</td><td>' + stavkaFakture.iznosPDV +
                            '</td><td>' + stavkaFakture.iznosStavke +
                            '</td><td>' + stavkaFakture.roba.naziv +
                            '</td><td>' + stavkaFakture.faktura.id +
                            '</td><td><a href=\'javascript:StavkaFaktureManager.showStavkaFakturesDetail("' + stavkaFakture.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
       
    });
});