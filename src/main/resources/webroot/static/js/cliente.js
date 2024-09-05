let courseList = []
let courseConsult = ''

function getCourses(type) {
    let course = document.getElementById(type).value;
    const xhttp = new XMLHttpRequest();
    courseConsult = type;
    xhttp.onload = function() {
        courseList = JSON.parse(this.responseText);
        var previous = document.getElementById("previous");
        var next = document.getElementById("next");
        previous.setAttribute('data-page',0);
        next.setAttribute('data-page', 1);
        nextCourses("previous");
    }
    xhttp.open("GET", "/electivas?carrer="+type) ;
    xhttp.send();
}

function createTable(limit,start){
    let table = document.getElementById("courseTable").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
    for (let i = start; i < limit; i++) {
        let course = courseList[i];
        let row = table.insertRow();
        let cell1 = row.insertCell(0);
        let cell2 = row.insertCell(1);
        let cell3 = row.insertCell(2);
        let cell4 = row.insertCell(3);
        let cell5 = row.insertCell(4);
        let cell6 = row.insertCell(5);
        let cell7 = row.insertCell(6)
        cell1.innerHTML = course.nombre;
        cell2.innerHTML = course.codigo;
        cell3.innerHTML = course.creditos;
        cell4.innerHTML = course.tipo;
        cell5.innerHTML = course.prerrequisitos;
        cell6.innerHTML = course.area;
        cell7.innerHTML = course.departamento;
    }
}

function nextCourses(button) {
    var page = parseInt(document.getElementById(button).getAttribute('data-page'));
    var course = document.getElementById('text');
    course.textContent = courseConsult+":Pagina-"+ String(page+1);
    var parar = 0;
    var previous = document.getElementById("previous");
    var next = document.getElementById("next");
    if ((page+1) * 10 > courseList.length) {
        previous.setAttribute('aria-disabled', false);
        next.setAttribute('aria-disabled', true);
        next.setAttribute('tabindex', -1);
        previous.setAttribute('tabindex', 1);
        previous.setAttribute('data-page', page - 1);
        next.setAttribute('data-page', page);
        parar = courseList.length;
    } else if (page == 0) {
        previous.setAttribute('aria-disabled', true);
        next.setAttribute('aria-disabled', false);
        previous.setAttribute('tabindex', -1);
        next.setAttribute('tabindex', 1);
        previous.setAttribute('data-page', 0);
        next.setAttribute('data-page', 1);
        parar = 10;
    } else {
        previous.setAttribute('aria-disabled', false);
        next.setAttribute('aria-disabled', false);
        previous.setAttribute('tabindex', 1);
        next.setAttribute('tabindex', 2);
        previous.setAttribute('data-page', page - 1);
        next.setAttribute('data-page', page + 1);
        parar = (page*10) + 10;
    }
    createTable(parar, page*10);
}

