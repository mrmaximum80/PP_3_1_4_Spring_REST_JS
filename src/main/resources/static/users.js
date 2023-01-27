// Add user begin---------------------------------------------------------------------

async function sendData(data) {
    let url = "http://localhost:8080/admin/users";
    return await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: data,
    })
}

function formToJOSN(formNode) {

    let data = new Object();
    data.name = formNode.elements.name.value;
    data.surname = formNode.elements.surname.value;
    data.age = formNode.elements.age.value;
    data.username = formNode.elements.username.value;
    data.password = formNode.elements.password.value;
    data.strRoles = Array.from(formNode.elements.role.options)
        .filter(option => option.selected)
        .map(option => option.value);

    let json = JSON.stringify(data)
    return json;
}

const applicantForm = document.querySelector('#add-user');
applicantForm.addEventListener('submit', handleFormSubmit, false);

function handleFormSubmit(event) {
    // Просим форму не отправлять данные самостоятельно
    event.preventDefault();
    console.log('Отправка!');

    const data = formToJOSN(event.target);
    console.log('handleFormSubmit: ')
    console.log(data);
    const response = sendData(data);

    deleteTable();
    sleep(250);
    loadUsers();

    $('#label1').tab('show');
}

// Add user end---------------------------------------------------------------------

function deleteTable() {
    let table = document.querySelector('#users_table');
    console.log('Столбцы')
    console.log(table.rows.length);
    let size = table.rows.length
    for (let i = 0; i < size - 1; i++) {
        table.deleteRow(1);
    }
}


loadUsers();
function loadUsers() {
    let url = "http://localhost:8080/admin/all_users";

    fetch(url)
        .then(response => response.json())
        .then(data => {
            console.log(data)
            createTable(data);
        });
}

// Create table begin---------------------------------------------------------------------

function createTable(users) {
    let table = document.querySelector('#users_table');

    console.log(users.length);
    for (let i = 0; i < users.length; i++) {
        createRow(users[i], table, i);
    }
}

function createRow(user, table, i) {
    table.insertRow(i + 1);
    table.rows[i + 1].insertCell(0).innerHTML = "<span style='font-weight: bold'>" + user.id + "</span>";
    table.rows[i + 1].insertCell(1).innerHTML = user.name;
    table.rows[i + 1].insertCell(2).innerHTML = user.surname;
    table.rows[i + 1].insertCell(3).innerHTML = user.age;
    table.rows[i + 1].insertCell(4).innerHTML = user.username;
    table.rows[i + 1].insertCell(5).innerHTML = user.rolesAsString;

    let e_button = document.createElement("button");
    e_button.id = 'e_button' + user.id;
    e_button.value = 'Edit';
    e_button.type = "button";
    e_button.className = "btn btn-info";
    e_button.name = "Edit";
    e_button.textContent = 'Edit';
    table.rows[i + 1].insertCell(6).appendChild(e_button);
    e_button.addEventListener("click", () => openEditWindow(user));

    let d_button = document.createElement("button");
    d_button.id = 'd_button' + user.id;
    d_button.value = 'Edit';
    d_button.type = "button";
    d_button.className = "btn btn-danger";
    d_button.name = "Delete";
    d_button.textContent = 'Delete';
    table.rows[i + 1].insertCell(7).appendChild(d_button);

    d_button.addEventListener("click", () => openDeleteWindow(user));
}

// Create table end---------------------------------------------------------------------


// User edit begin---------------------------------------------------------------------

function openEditWindow(user) {

    let form = document.forms.formedit;
    console.log(form.elements.name);
    form.elements.id.value = user.id;
    form.elements.name.value = user.name;
    form.elements.surname.value = user.surname;
    form.elements.age.value = user.age;
    form.elements.username.value = user.username;

    $('#modaledit').modal('show');

    let edit_button = document.querySelector('#edit_btn');
    edit_button.addEventListener("click", handleEdit, false);

    function handleEdit(event) {
        // Просим форму не отправлять данные самостоятельно
        event.preventDefault();
        console.log('Edit');
        let table = document.querySelector('#users_table')
        let form = document.forms.formedit;

        // let data = new Object();
        user.id = form.elements.id.value;
        user.name = form.elements.name.value;
        user.surname = form.elements.surname.value;
        user.age = form.elements.age.value;
        user.username = form.elements.username.value;
        user.password = form.elements.password.value;
        user.strRoles = Array.from(form.elements.role.options)
            .filter(option => option.selected)
            .map(option => option.value);
        user.rolesAsString = "";
        for (let i = 0; i < user.strRoles.length; i++) {
            user.rolesAsString = user.rolesAsString
                + user.strRoles[i].slice(5, user.strRoles[i].length) + " ";
        }

        for (let i = 1; i < table.rows.length; i++) {
            if (table.rows[i].cells[0].textContent == form.elements.id.value) {
                editRow(user, i);
                break;
            }
        }

        let json = JSON.stringify(user);
        let url = "http://localhost:8080/admin/users/" + form.elements.id.value;

        fetch(url, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: json
        })

        $('#modaledit').modal('hide');

    }

    function editRow(user, i) {
        let table = document.querySelector('#users_table');
        table.rows[i].cells[1].innerHTML = user.name;
        table.rows[i].cells[2].innerHTML = user.surname;
        table.rows[i].cells[3].innerHTML = user.age;
        table.rows[i].cells[4].innerHTML = user.username;
        table.rows[i].cells[5].innerHTML = user.rolesAsString;
    }
}

// User edit end---------------------------------------------------------------------


// User delete begin---------------------------------------------------------------------

function openDeleteWindow(user) {
    console.log('openDeleteWindow');
    console.log(user.id)

    let form = document.forms.formdelete;
    console.log(form.elements.name);
    form.elements.userid.value = user.id;
    form.elements.name.value = user.name;
    form.elements.surname.value = user.surname;
    form.elements.age.value = user.age;
    form.elements.username.value = user.username;

    $('#modaldelete').modal('show');

    let del_button = document.querySelector('#del_btn');
    del_button.addEventListener("click", handleDelete, false);
}

function handleDelete(event) {
    // Просим форму не отправлять данные самостоятельно
    event.preventDefault();
    console.log('Delete!');
    let table = document.querySelector('#users_table')
    let form = document.forms.formdelete;
    for (let i = 1; i < table.rows.length; i++) {
        if (table.rows[i].cells[0].textContent == form.elements.userid.value) {
            table.deleteRow(i);
            break;
        }
    }

    let url = "http://localhost:8080/admin/users/" + form.elements.userid.value;
    console.log(url);
    fetch(url, { method: 'DELETE' });
    $('#modaldelete').modal('hide');

}
// User delete end---------------------------------------------------------------------


function sleep(milliseconds) {
    const date = Date.now();
    let currentDate = null;
    do {
        currentDate = Date.now();
    } while (currentDate - date < milliseconds);
}