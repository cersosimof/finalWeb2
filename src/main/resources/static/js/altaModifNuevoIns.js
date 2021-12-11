
function soloNumerosYPuntos(e) {
    var key = window.event ? e.which : e.keyCode;
    console.log(key)
    if (!(key == 46 || key >= 48 && key <= 57))
        e.preventDefault();
}

function solonumeros(e) {
    var key = window.event ? e.which : e.keyCode;
    if (key < 48 || key > 57)
        e.preventDefault();
}


