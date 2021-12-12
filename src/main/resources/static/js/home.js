function verProducto(i) {
    let nroProducto = i.split("_")[1];

    location.href = "/instrumentos/verProducto/" + nroProducto
}


function buscarSoloMarca(id) {

    let marca  = id.split("_")[1].trim();

    if (location.pathname == "/") {
        location.href = "/filtro/" + marca + "/0/0";
    } else {

        let datos = location.pathname;
        let datosArray = datos.split("/");
        datosArray[2] = marca;
        let datosFin = datosArray.join("/");

        location.href = datosFin;
    }
}

function buscarConRango() {

    let desdeHasta = $("#precioDesde").val() + "-" + $("#precioHasta").val();

    if (location.pathname == "/") {
        location.href = "/filtro/0/" + desdeHasta + "/0";
    } else {
        let datos = location.pathname;
        let datosArray = datos.split("/");
        datosArray[3] = desdeHasta;
        let datosFin = datosArray.join("/");

        location.href = datosFin;
    }
}

function buscarConOrden(ascdesc) {

    if (location.pathname == "/") {
        location.href = "/filtro/0/0/" + ascdesc;
    } else {
        let datos = location.pathname;
        let datosArray = datos.split("/");
        datosArray[4] = ascdesc;
        let datosFin = datosArray.join("/");

        location.href = datosFin;
    }
}


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