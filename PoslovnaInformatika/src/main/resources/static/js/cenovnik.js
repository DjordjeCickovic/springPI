var pageNumber = 0;
var CenovnikManager = {
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
                        .attr('value', preduzece.id).appendTo($('#CenovnikPreduzece'));
                })
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    showCenovniksList: function () {
        $.ajax({

            url: this.basePath() + '/cenovnik/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Datum vazenja</th><th>Preduzece</th><th>Edit</th></tr>');
                $.each(list, function (index, cenovnik) {
                    $('#customTable tr:last').after('<tr><td>' + cenovnik.datumVazenja + '</td><td>' + cenovnik.preduzece.naziv + '</td><td><a href=\'javascript:CenovnikManager.showCenovniksDetail("' + cenovnik.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with Cenovnik details,
    showCenovniksDetail: function (id) {
        if (id == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#CenovniksListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/cenovnik/' + id,
            cache: false,
            dataType: 'json',
            success: function (cenovnik) {

                $('#CenovniksDetailPanel').show();

                $('#CenovnikID').val(cenovnik.id);
                $('#CenovnikDatumVazenja').val(cenovnik.datumVazenja);
                $('#CenovnikDatumVazenja').focus();
                $('#CenovnikPreduzece').val(cenovnik.preduzece.id);


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },

    // Returns back to the search mode.
    backToList: function () {
        $('#CenovniksDetailPanel').hide();
        $('#CenovniksListPanel').show();
        CenovnikManager.showCenovniksList();
        $('#CenovniksListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showCenovniksList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            CenovnikManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	CenovnikManager.refreshList();
      },

    // Formats a URI of a Cenovnik
    createCenovnikURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/cenovnik';
        }
        return this.basePath() + '/cenovnik/' + encodeURIComponent($('#CenovnikID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "CenovnikDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#CenovnikID').val(),
            "datumVazenja": $('#CenovnikDatumVazenja').val(),
            "preduzece": {
                id: $('#CenovnikPreduzece').val()
            }

        });
    },
    // Deletes a Cenovnik.
    deleteCenovnik: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createCenovnikURL('DELETE'),
            type: 'DELETE',
            success: function () {
                CenovnikManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newCenovnik: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#CenovniksListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#CenovniksDetailPanel').show();
        $('#CenovnikID').attr('value', null);
        $('#CenovnikDatumVazenja').attr('value', null);
        $('#CenovnikPreduzece').attr('value', null);

    },

    // Saves a Cenovnik. If there is no value in the #CenovnikID hidden field then
    // a new Cenovnik is created by "POST" request. Otherwise an existing Cenovnik
    // is updated with "PUT" request.
    saveCenovnik: function () {
        if (!confirm('Save?')) return
        var requestType = $('#CenovnikID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createCenovnikURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                CenovnikManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    CenovnikManager.showCenovniksList();
    CenovnikManager.loadPreduzece();
    $('#CenovniksListPanel').show();
    $('#CenovniksDetailPanel').hide();
    $('#opseg1').hide();
    $('#opseg2').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.newCenovnik();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.saveCenovnik();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        CenovnikManager.deleteCenovnik();
    });
    $("#tipPretrage").change(function () {
        var selektovanaVrednost = $('#tipPretrage').val();
        $("#terminPretrage").val('');
        $('#opseg1').val('');
        $('#opseg2').val('');

        if (selektovanaVrednost == 'datumVazenja') {
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
                url: 'http://localhost:8080/api/cenovnik/' + tipPretrage + '/' + opseg1 + '/' + opseg2,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();
                    $('#customTable').append('<tr><th>Datum vazenja</th><th>Preduzece</th><th>Edit</th></tr>');
                    $.each(list, function (index, cenovnik) {
                        $('#customTable tr:last').after('<tr><td>' + cenovnik.datumVazenja + '</td><td>' + cenovnik.preduzece.naziv + '</td><td><a href=\'javascript:CenovnikManager.showCenovniksDetail("' + cenovnik.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
        if (terminPretrage !== '' && opseg1 == '' && opseg2 == '') {
            $.ajax({
                url: 'http://localhost:8080/api/cenovnik/' + tipPretrage + '/' + terminPretrage,
                cache: false,
                dataType: 'json',
                success: function (list) {
                    $('#NewButton').show();
                    $('#RemoveButton').hide();
                    $('#SaveButton').hide();
                    $('#BackToListButton').hide();
                    $('#customTable').empty();

                    $('#customTable').append('<tr><th>Datum vazenja</th><th>Preduzece</th><th>Edit</th></tr>');
                    $.each(list, function (index, cenovnik) {
                        $('#customTable tr:last').after('<tr><td>' + cenovnik.datumVazenja + '</td><td>' + cenovnik.preduzece.naziv + '</td><td><a href=\'javascript:CenovnikManager.showCenovniksDetail("' + cenovnik.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });


                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Nije pronadjeno!');
                }
            });
        }
    });
});