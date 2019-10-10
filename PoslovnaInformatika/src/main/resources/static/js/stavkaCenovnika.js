var pageNumber = 0;
var StavkaCenovnikaManager = {
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
                        .attr('value', roba.id).appendTo($('#StavkaCenovnikaRoba'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadCenovnik: function () {
        $.ajax({
            url: this.basePath() + '/cenovnik',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, cenovnik) {
                    $('<option>').text(cenovnik.datumVazenja)
                        .attr('value', cenovnik.id).appendTo($('#StavkaCenovnikaCenovnik'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showStavkaCenovnikasList: function () {
        $.ajax({

            url: this.basePath() + '/stavka-cenovnika/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Roba</th><th>Cena</th><th>Cenovnik-Datum vazenja</th><th>Edit</th></tr>');
                $.each(list, function (index, stavkaCenovnika) {
                    $('#customTable tr:last').after('<tr><td>' + stavkaCenovnika.roba.naziv + '</td><td>' + stavkaCenovnika.cena + '</td><td>' + stavkaCenovnika.cenovnik.datumVazenja

                        +
                        '</td><td><a href=\'javascript:StavkaCenovnikaManager.showStavkaCenovnikasDetail("' + stavkaCenovnika.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with StavkaCenovnika details,
    showStavkaCenovnikasDetail: function (StavkaCenovnikaID) {
        if (StavkaCenovnikaID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#StavkaCenovnikasListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/stavka-cenovnika/' + StavkaCenovnikaID,
            cache: false,
            dataType: 'json',
            success: function (stavkaCenovnika) {

                $('#StavkaCenovnikasDetailPanel').show();
                $('#StavkaCenovnikaID').val(stavkaCenovnika.id);
                $('#StavkaCenovnikaRoba').val(stavkaCenovnika.roba.id);
                $('#StavkaCenovnikaCena').val(stavkaCenovnika.cena);
                $('#StavkaCenovnikaCenovnik').val(stavkaCenovnika.cenovnik.id);

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#StavkaCenovnikasDetailPanel').hide();
        $('#StavkaCenovnikasListPanel').show();
        StavkaCenovnikaManager.showStavkaCenovnikasList();
        $('#StavkaCenovnikasListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showStavkaCenovnikasList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            StavkaCenovnikaManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
            StavkaCenovnikaManager.refreshList();
      },

    // Formats a URI of a StavkaCenovnika
    createStavkaCenovnikaURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/stavka-cenovnika';
        }
        return this.basePath() + '/stavka-cenovnika/' + encodeURIComponent($('#StavkaCenovnikaID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "StavkaCenovnikaDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#StavkaCenovnikaID').val(),

            "cena": $('#StavkaCenovnikaCena').val(),

            "roba": {
                id: $('#StavkaCenovnikaRoba').val()
            },
            "cenovnik": {
                id: $('#StavkaCenovnikaCenovnik').val()
            }

        });
    },
    // Deletes a StavkaCenovnika.
    deleteStavkaCenovnika: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createStavkaCenovnikaURL('DELETE'),
            type: 'DELETE',
            success: function () {
                StavkaCenovnikaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newStavkaCenovnika: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#StavkaCenovnikasListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#StavkaCenovnikasDetailPanel').show();
        $('#StavkaCenovnikaID').attr('value', null);
        $('#StavkaCenovnikaCena').attr('value', null);
        $('#StavkaCenovnikaRoba').attr('value', null);
        $('#StavkaCenovnikaCenovnik').attr('value', null);

    },

    // Saves a StavkaCenovnika. If there is no value in the #StavkaCenovnikaID hidden field then
    // a new StavkaCenovnika is created by "POST" request. Otherwise an existing StavkaCenovnika
    // is updated with "PUT" request.
    saveStavkaCenovnika: function () {
        if (!confirm('Save?')) return
        var requestType = $('#StavkaCenovnikaID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createStavkaCenovnikaURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                StavkaCenovnikaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    StavkaCenovnikaManager.showStavkaCenovnikasList();
    StavkaCenovnikaManager.loadRoba();
    StavkaCenovnikaManager.loadCenovnik();
    $('#StavkaCenovnikasListPanel').show();
    $('#StavkaCenovnikasDetailPanel').hide();
    $('#opseg1').hide();
    $('#opseg2').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.newStavkaCenovnika();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.saveStavkaCenovnika();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        StavkaCenovnikaManager.deleteStavkaCenovnika();
    });
  $("#tipPretrage").change(function () {
        var selektovanaVrednost = $('#tipPretrage').val();
        $("#terminPretrage").val('');
        $('#opseg1').val('');
        $('#opseg2').val('');

        if (selektovanaVrednost == 'cena') {
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
                url: 'http://localhost:8080/api/stavka-cenovnika/' + tipPretrage + '/' + opseg1 + '/' + opseg2,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                                    $('#customTable').append('<tr><th>Roba</th><th>Cena</th><th>Cenovnik-Datum vazenja</th><th>Edit</th></tr>');
                $.each(list, function (index, stavkaCenovnika) {
                    $('#customTable tr:last').after('<tr><td>' + stavkaCenovnika.roba.naziv + '</td><td>' + stavkaCenovnika.cena + '</td><td>' + stavkaCenovnika.cenovnik.datumVazenja

                        +
                        '</td><td><a href=\'javascript:StavkaCenovnikaManager.showStavkaCenovnikasDetail("' + stavkaCenovnika.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '') {
            $.ajax({
                url: 'http://localhost:8080/api/stavka-cenovnika/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                $('#customTable').append('<tr><th>Roba</th><th>Cena</th><th>Cenovnik-Datum vazenja</th><th>Edit</th></tr>');
                $.each(list, function (index, stavkaCenovnika) {
                    $('#customTable tr:last').after('<tr><td>' + stavkaCenovnika.roba.naziv + '</td><td>' + stavkaCenovnika.cena + '</td><td>' + stavkaCenovnika.cenovnik.datumVazenja

                        +
                        '</td><td><a href=\'javascript:StavkaCenovnikaManager.showStavkaCenovnikasDetail("' + stavkaCenovnika.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
    });
});