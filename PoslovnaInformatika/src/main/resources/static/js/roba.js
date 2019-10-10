var pageNumber = 0;
var RobaManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    loadGrupaRobe: function () {
        $.ajax({
            url: this.basePath() + '/grupa-robe',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, grupaRobe) {
                    $('<option>').text(grupaRobe.naziv)
                        .attr('value', grupaRobe.id).appendTo($('#RobaGrupaRobe'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    loadJediniceMere: function () {
        $.ajax({
            url: this.basePath() + '/jedinica-mere',
            dataType: 'json',
            cache: false,
            success: function (data) {
                $.each(data, function (index, jedinicaMere) {
                    $('<option>').text(jedinicaMere.naziv)
                        .attr('value', jedinicaMere.id).appendTo($('#RobaJedinicaMere'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showRobasList: function () {
        $.ajax({

            url: this.basePath() + '/roba/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Naziv</th><th>Grupa robe</th><th>Jedinica mere</th><th>Edit</th></tr>');
                $.each(list, function (index, roba) {
                    $('#customTable tr:last').after('<tr><td>' + roba.naziv + '</td><td>' + roba.grupaRobe.naziv + '</td><td>' + roba.jedinicaMere.naziv + '</td><td><a href=\'javascript:RobaManager.showRobasDetail("' + roba.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with Roba details,
    showRobasDetail: function (RobaId) {
        if (RobaId == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#RobasListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/roba/' + RobaId,
            cache: false,
            dataType: 'json',
            success: function (roba) {

                $('#RobasDetailPanel').show();

                $('#RobaID').val(roba.id);
                $('#RobaNaziv').val(roba.naziv);
                $('#RobaNaziv').focus();
                $('#RobaGrupaRobe').val(roba.grupaRobe.id);
                $('#RobaJedinicaMere').val(roba.jedinicaMere.id);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#RobasDetailPanel').hide();
        $('#RobasListPanel').show();
        RobaManager.showRobasList();
        $('#RobasListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showRobasList();
    },

    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            RobaManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	RobaManager.refreshList();
      },
    
    // Formats a URI of a Roba
    createRobaURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/roba';
        }
        return this.basePath() + '/roba/' + encodeURIComponent($('#RobaID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "RobaDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#RobaID').val(),

            "naziv": $('#RobaNaziv').val(),
            "grupaRobe": {
                id: $('#RobaGrupaRobe').val()
            },
            "jedinicaMere": {
                id: $('#RobaJedinicaMere').val()
            },
        });
    },
    // Deletes a Roba.
    deleteRoba: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createRobaURL('DELETE'),
            type: 'DELETE',
            success: function () {
                RobaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newRoba: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#RobasListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#RobasDetailPanel').show();
        $('#RobaID').attr('value', null);
        $('#RobaNaziv').attr('value', null);
        $('#RobaGrupaRobe').attr('value', null);
        $('#RobaJedinicaMere').attr('value', null);

    },

    // Saves a Roba. If there is no value in the #RobaID hidden field then
    // a new Roba is created by "POST" request. Otherwise an existing Roba
    // is updated with "PUT" request.
    saveRoba: function () {
        if (!confirm('Save?')) return
        var requestType = $('#RobaID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createRobaURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                RobaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    RobaManager.showRobasList();
    RobaManager.loadGrupaRobe();
    RobaManager.loadJediniceMere();
    $('#RobasListPanel').show();
    $('#RobasDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        RobaManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        RobaManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        RobaManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        RobaManager.newRoba();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        RobaManager.saveRoba();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        RobaManager.deleteRoba();
    });
    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $.ajax({

            url: 'http://localhost:8080/api/roba/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (list) {
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>Naziv</th><th>Grupa robe</th><th>Jedinica mere</th><th>Edit</th></tr>');
                if (tipPretrage == 'grupaRobe') {

                    $.each(list, function (index, roba) {
                        $('#customTable tr:last').after('<tr><td>' + roba.naziv + '</td><td>' + roba.grupaRobe.naziv + '</td><td>' + roba.jedinicaMere.naziv + '</td><td><a href=\'javascript:RobaManager.showRobasDetail("' + roba.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });

                }
                if (tipPretrage == 'jedinicaMere') {
                    $.each(list, function (index, roba) {
                        $('#customTable tr:last').after('<tr><td>' + roba.naziv + '</td><td>' + roba.grupaRobe.naziv + '</td><td>' + roba.jedinicaMere.naziv + '</td><td><a href=\'javascript:RobaManager.showRobasDetail("' + roba.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                }

                if (tipPretrage == 'naziv') {
                    $.each(list, function (index, roba) {
                        $('#customTable tr:last').after('<tr><td>' + roba.naziv + '</td><td>' + roba.grupaRobe.naziv + '</td><td>' + roba.jedinicaMere.naziv + '</td><td><a href=\'javascript:RobaManager.showRobasDetail("' + roba.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });

                }


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });

    });
});