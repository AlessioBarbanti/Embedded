$(document).ready(function() {

    var intervalID = window.setInterval(aggiornaStatoAttualeDelSistema, 20000);

    const myURL = "http://37f130db2c40.ngrok.io";

    $("p#giorni").hide();

    $("button#statob").click(function(e) {
        e.preventDefault();
        aggiornaStatoAttualeDelSistema();
    });

    $("button#available").click(function(e) {
        e.preventDefault();
        $.post(myURL + "/api/av", JSON.stringify({}), function() {}, "json");
        alert("Dumpster disponibile!");
        aggiornaStatoAttualeDelSistema();
    });

    $("button#not-available").click(function(e) {
        e.preventDefault();
        $.post(myURL + "/api/unav", JSON.stringify({}), function() {}, "json");
        alert("Dumpster non disponibile!");
        aggiornaStatoAttualeDelSistema();
    });

    $("button#utilizzob").click(function(e) {
        e.preventDefault();
        if (document.getElementById("startDate").value != null && document.getElementById("endDate").value != null) {
            $.post(myURL + "/api/time", JSON.stringify({ inizio: document.getElementById("startDate").value, fine: document.getElementById("endDate").value }), function() {}, "json");
            $.get(myURL + "/api/data", function(data) {
                $("#numDepGiorni").text("Numero depositi: " + data["dep"]);
                $("#qtaGiorni").text("Quantità depositate: " + data["qta"]);
            });
        } else {
            alert("Specificare le date prima di cliccare su questo bottone!");
        }
    });

    function aggiornaStatoAttualeDelSistema() {
        $.get(myURL + "/api/stato", function(data) {
            if (data["disp"] == "true") {
                $("p#disponibilita").addClass("disp");
                $("p#disponibilita").text("Disponibile");
                $("p#disponibilita").css("color", "green");
            } else {
                $("p#disponibilita").remove("disp");
                $("p#disponibilita").text("Non Disponibile");
                $("p#disponibilita").css("color", "red");
            }
            $("p#numDep").text("Numero depositi fatti oggi: " + data["dep"]);
            $("p#qta").text("Quantità corrente: " + data["qta"]);
        });
    }
});