var pageNumber = 0;
var PoslovnaGodinaManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    showPoslovnaGodinasList: function () {
        $.ajax({

            url: this.basePath() + '/poslovna-godina/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Godina</th><th>Zakljucena</th><th>Edit</th></tr>');
                $.each(list, function (index, poslovnaGodina) {
                    $('#customTable tr:last').after('<tr><td>' + poslovnaGodina.godina + '</td><td>' + poslovnaGodina.zakljucena + '</td><td><a href=\'javascript:PoslovnaGodinaManager.showPoslovnaGodinasDetail("' + poslovnaGodina.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with PoslovnaGodina details,
    showPoslovnaGodinasDetail: function (PoslovnaGodinaId) {
        if (PoslovnaGodinaId == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#PoslovnaGodinasListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/poslovna-godina/' + PoslovnaGodinaId,
            cache: false,
            dataType: 'json',
            success: function (poslovnaGodina) {

                $('#PoslovnaGodinasDetailPanel').show();

                $('#PoslovnaGodinaID').val(poslovnaGodina.id);
                $('#PoslovnaGodinaGodina').val(poslovnaGodina.godina);
                $('#PoslovnaGodinaZakljucena').select(poslovnaGodina.zakljucena);
                $('#PoslovnaGodinaGodina').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#PoslovnaGodinasDetailPanel').hide();
        $('#PoslovnaGodinasListPanel').show();
        PoslovnaGodinaManager.showPoslovnaGodinasList();
        $('#PoslovnaGodinasListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showPoslovnaGodinasList();
    },

    // Formats a URI of a PoslovnaGodina
    createPoslovnaGodinaURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/poslovna-godina';
        }
        return this.basePath() + '/poslovna-godina/' + encodeURIComponent($('#PoslovnaGodinaID').val());
    },
    
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            PoslovnaGodinaManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	PoslovnaGodinaManager.refreshList();
      },

    // Creates an object with the properties retrieved from the input fields 
    // of the "PoslovnaGodinaDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#PoslovnaGodinaID').val(),
            "godina": $('#PoslovnaGodinaGodina').val(),
            "zakljucena": $('#PoslovnaGodinaZakljucena').val(),
        });
    },
    // Deletes a PoslovnaGodina.
    deletePoslovnaGodina: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createPoslovnaGodinaURL('DELETE'),
            type: 'DELETE',
            success: function () {
                PoslovnaGodinaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newPoslovnaGodina: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#PoslovnaGodinasListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#PoslovnaGodinasDetailPanel').show();
        $('#PoslovnaGodinaID').attr('value', null);
        $('#PoslovnaGodinaGodina').attr('value', 'Godina').focus().select();
        $('#PoslovnaGodinaZakljucena').attr('value', 'Zakljucena');

    },

    // Saves a PoslovnaGodina. If there is no value in the #PoslovnaGodinaID hidden field then
    // a new PoslovnaGodina is created by "POST" request. Otherwise an existing PoslovnaGodina
    // is updated with "PUT" request.
    savePoslovnaGodina: function () {
        if (!confirm('Save?')) return
        var requestType = $('#PoslovnaGodinaID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createPoslovnaGodinaURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                PoslovnaGodinaManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    PoslovnaGodinaManager.showPoslovnaGodinasList();
    $('#PoslovnaGodinasListPanel').show();
    $('#PoslovnaGodinasDetailPanel').hide();
    $('#opseg1').hide();
    $('#opseg2').hide();

    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.newPoslovnaGodina();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.savePoslovnaGodina();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        PoslovnaGodinaManager.deletePoslovnaGodina();
    });

    $("#tipPretrage").change(function () {
        var selektovanaVrednost = $('#tipPretrage').val();
        $("#terminPretrage").val('');
        $('#opseg1').val('');
        $('#opseg2').val('');

        if (selektovanaVrednost == 'godina') {
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
        if (opseg1 !== '' && opseg2 !== '') {
            $.ajax({
                url: 'http://localhost:8080/api/poslovna-godina/' + tipPretrage + '/' + opseg1 + '/' + opseg2,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Godina</th><th>Zakljucena</th><th>Edit</th></tr>');
                    $.each(list, function (index, poslovnaGodina) {
                        $('#customTable tr:last').after('<tr><td>' + poslovnaGodina.godina + '</td><td>' + poslovnaGodina.zakljucena + '</td><td><a href=\'javascript:PoslovnaGodinaManager.showPoslovnaGodinasDetail("' + poslovnaGodina.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '') {
            $.ajax({
                url: 'http://localhost:8080/api/poslovna-godina/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Godina</th><th>Zakljucena</th><th>Edit</th></tr>');
                    $.each(list, function (index, poslovnaGodina) {
                        $('#customTable tr:last').after('<tr><td>' + poslovnaGodina.godina + '</td><td>' + poslovnaGodina.zakljucena + '</td><td><a href=\'javascript:PoslovnaGodinaManager.showPoslovnaGodinasDetail("' + poslovnaGodina.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
    });
});