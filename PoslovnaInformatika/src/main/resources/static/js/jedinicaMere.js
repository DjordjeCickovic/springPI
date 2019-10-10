var pageNumber = 0;
var JedinicaMereManager = {
    // Returns the url of the application server
    basePath: function () {
        return 'http://localhost:8080/api';
    },
    showJedinicaMeresList: function () {
        $.ajax({

            url: this.basePath() + '/jedinica-mere/pageable?page=' + pageNumber,
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
                $('#customTable').append('<tr><th>Naziv</th><th>Edit</th></tr>');
                $.each(list, function (index, jedinicaMere) {
                    $('#customTable tr:last').after('<tr><td>' + jedinicaMere.naziv + '</td><td><a href=\'javascript:JedinicaMereManager.showJedinicaMeresDetail("' + jedinicaMere.id + '")\'><img src="http://skr.rs/hxu"style="width:25px;height:25px;"></a></td></tr>');
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows a form with JedinicaMere details,
    showJedinicaMeresDetail: function (JedinicaMereID) {
        if (JedinicaMereID == null) return;
        $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#JedinicaMeresListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: this.basePath() + '/jedinica-mere/' + JedinicaMereID,
            cache: false,
            dataType: 'json',
            success: function (jedinicaMere) {

                $('#JedinicaMeresDetailPanel').show();
                $('#JedinicaMereID').val(jedinicaMere.id);
                $('#JedinicaMereNaziv').val(jedinicaMere.naziv);
                $('#JedinicaMereNaziv').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Returns back to the search mode.
    backToList: function () {
        $('#JedinicaMeresDetailPanel').hide();
        $('#JedinicaMeresListPanel').show();
        JedinicaMereManager.showJedinicaMeresList();
        $('#JedinicaMeresListPanel').focus();
    },

    // Refreshes the list.
    refreshList: function () {
        this.backToList();
        this.showJedinicaMeresList();
    },
    
    previousPage: function () {
        if(pageNumber > 0){
     	   pageNumber -= 1;
            JedinicaMereManager.refreshList();
        }
     },
     nextPage: function () {
     	pageNumber += 1;
     	JedinicaMereManager.refreshList();
      },

    // Formats a URI of a JedinicaMere
    createJedinicaMereURL: function (requestType) {
        if (requestType == 'POST') {
            return this.basePath() + '/jedinica-mere';
        }
        return this.basePath() + '/jedinica-mere/' + encodeURIComponent($('#JedinicaMereID').val());
    },

    // Creates an object with the properties retrieved from the input fields 
    // of the "JedinicaMereDetails" form.
    collectFieldValues: function () {
        return JSON.stringify({
            "id": $('#JedinicaMereID').val(),
            "naziv": $('#JedinicaMereNaziv').val(),

        });
    },
    // Deletes a JedinicaMere.
    deleteJedinicaMere: function () {
        if (!confirm('Delete?')) return;
        $.ajax({
            url: this.createJedinicaMereURL('DELETE'),
            type: 'DELETE',
            success: function () {
                JedinicaMereManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
    // Shows the professor form with blank values.
    newJedinicaMere: function () {
    	$('#PreviousButton').hide();
    	$('#NextButton').hide();
        $('#NewButton').hide();
        $('#SaveButton').show();
        $('#BackToListButton').show();
        $('#JedinicaMeresListPanel').slideUp("slow");
        $('#RemoveButton').hide();
        $('#JedinicaMeresDetailPanel').show();
        $('#JedinicaMereID').attr('value', null);
        $('#JedinicaMereNaziv').attr('value', 'Naziv').focus().select();


    },

    // Saves a JedinicaMere. If there is no value in the #JedinicaMereID hidden field then
    // a new JedinicaMere is created by "POST" request. Otherwise an existing JedinicaMere
    // is updated with "PUT" request.
    saveJedinicaMere: function () {
        if (!confirm('Save?')) return
        var requestType = $('#JedinicaMereID').val() != '' ? 'PUT' : 'POST';
        $.ajax({
            url: this.createJedinicaMereURL(requestType),
            dataType: 'json',
            type: requestType,
            contentType: "application/json",
            data: this.collectFieldValues(),
            success: function (result) {
                JedinicaMereManager.refreshList();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(textStatus);
            }
        });
    },
};
$(document).ready(function () {
    // pre-populate the drop down lists of parent customTable
    JedinicaMereManager.showJedinicaMeresList();
    $('#JedinicaMeresListPanel').show();
    $('#JedinicaMeresDetailPanel').hide();
    // attach event handlers to buttons
    $('#BackToListButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.backToList();
    });
    $('#PreviousButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.previousPage();
    });
    $('#NextButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.nextPage();
    });
    $('#NewButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.newJedinicaMere();
    });
    $('#SaveButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.saveJedinicaMere();
    });
    $('#RemoveButton').click(function (e) {
        e.preventDefault();
        JedinicaMereManager.deleteJedinicaMere();
    });

    $("#pretraziButton").click(function () {
        var tipPretrage = $("#tipPretrage").val();
        var terminPretrage = $("#terminPretrage").val();
         $('#PreviousButton').hide();
        $('#NextButton').hide();
        $('#JedinicaMeresListPanel').hide();
        $('#NewButton').hide();
        $('#BackToListButton').show();
        $('#SaveButton').show();
        $('#RemoveButton').show();
        $.ajax({
            url: 'http://localhost:8080/api/jedinica-mere/' + tipPretrage + '/' + terminPretrage,
            cache: false,
            dataType: 'json',
            success: function (jedinicaMere) {


                $('#JedinicaMeresDetailPanel').show();
                $('#JedinicaMereID').val(jedinicaMere.id);
                $('#JedinicaMereNaziv').val(jedinicaMere.naziv);
                $('#JedinicaMereNaziv').focus();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Nije pronadjeno!');
            }
        });
    });
});