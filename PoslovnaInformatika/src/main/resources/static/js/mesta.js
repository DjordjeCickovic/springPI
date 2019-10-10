var pageNumber = 0;
var MestoManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    showMestosList: function () {
        $.ajax({

        	url: this.basePath() + '/mesto/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Drzava</th><th>Grad</th><th>Edit</th></tr>');
                $.each(list, function (index, mesto) {
                    $('#customTable tr:last').after('<tr><td>' + mesto.drzava + '</td><td>' + mesto.grad + '</td><td><a href=\'javascript:MestoManager.showMestosDetail("' + mesto.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },

    showMestosListPretraga: function () {
        $.ajax({

            url: this.basePath() + '/mesto' + '/' + tipPretrage + '/' + terminPretrage,
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
                $('#customTable').append('<tr><th>Drzava</th><th>Grad</th><th>Edit</th></tr>');
                $.each(list, function (index, mesto) {
                    $('#customTable tr:last').after('<tr><td>' + mesto.drzava + '</td><td>' + mesto.grad + '</td><td><a href=\'javascript:MestoManager.showMestosDetail("' + mesto.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with Mesto details,
    showMestosDetail: function (MestoId) {
        if (MestoId == null) return;
        $('#MestosListPanel').hide();
        $('#PreviousButton').hide();
        $('#ReportLink').hide();
        $('#NextButton').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/mesto/' + MestoId,
            cache: false,
            dataType: 'json',
            success: function (mesto) {

                $('#MestosDetailPanel').show();

                $('#MestoID').val(mesto.id);
                $('#MestoDrzava').val(mesto.drzava);
                $('#MestoGrad').val(mesto.grad);
                $('#MestoGrad').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#MestosDetailPanel').hide();
        $('#MestosListPanel').show();
        $('#ReportLink').show();
        MestoManager.showMestosList();
        $('#MestosListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showMestosList();
    },

    // Formats a URI of a Mesto
    createMestoURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/mesto';
        }
        return this.basePath() + '/mesto/' + encodeURIComponent($('#MestoID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "MestoDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#MestoID').val(),
            "drzava": $('#MestoDrzava').val(),
            "grad": $('#MestoGrad').val(),
        });
    },
    // Deletes a Mesto.
    deleteMesto: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createMestoURL('DELETE'),
            type: 'DELETE',
            success: function () {
                MestoManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            MestoManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
            MestoManager.refreshList();
      },
    // Shows the professor form with blank values.
    newMesto: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#ReportLink').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#MestosListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#MestosDetailPanel').show();
        $('#MestoID').attr('value', null);
        $('#MestoDrzava').attr('value', null);
        $('#MestoGrad').attr('value', null).focus().select();

    },

    // Saves a Mesto. If there is no value in the #MestoID hidden field then
    // a new Mesto is created by "POST" request. Otherwise an existing Mesto
    // is updated with "PUT" request.
    saveMesto: function () {
        if (!confirm('Save?')) return
        var requestType = $('#MestoID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createMestoURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                MestoManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    MestoManager.showMestosList();
    $('#MestosListPanel').show();
    $('#MestosDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        MestoManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        MestoManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        MestoManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        MestoManager.newMesto();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        MestoManager.saveMesto();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        MestoManager.deleteMesto();
    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();

         $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#ReportLink').hide();
        $.ajax({

            url: 'http://localhost:8080/api/mesto/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (list) {
                $('#NewButton').show();
                $('#RemoveButton').hide();
                $('#SaveButton').hide();
                $('#BackToListButton').hide();
                $('#customTable').empty();
                $('#customTable').append('<tr><th>Drzava</th><th>Grad</th><th>Edit</th></tr>');
                if (tipPretrage == 'drzava') {
                    $.each(list, function (index, mesto) {
                        $('#customTable tr:last').after('<tr><td>' + mesto.drzava + '</td><td>' + mesto.grad + '</td><td><a href=\'javascript:MestoManager.showMestosDetail("' + mesto.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                    });

                }
                if (tipPretrage == 'grad') {


                    $('#MestosListPanel').hide();
                    $('#PreviousButton').hide();
                	$('#NextButton').hide();
                    $('#NewButton').hide();
                    $('#BackToListButton').show();
                    $('#SaveButton').show();
                    $('#RemoveButton').show();
                    $.ajax({
                        url: 'http://localhost:8080/api/mesto/' + tipPretrage + '/' + terminPretrage,
                        cache: false,
                        dataType: 'json',
                        success: function (mesto) {

                            $('#MestosDetailPanel').show();

                            $('#MestoID').val(mesto.id);
                            $('#MestoDrzava').val(mesto.drzava);
                            $('#MestoGrad').val(mesto.grad);
                            $('#MestoGrad').focus();
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert(textStatus);
                        }
                    });
                };


            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });

    });
});