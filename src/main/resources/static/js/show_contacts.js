// active link



document.getElementById('view-contacts-link').classList.add('active')

// search

const search = () => {

    let query = document.getElementById("search-input").value;
    let searchObj = document.getElementById("search-result");

    if (query == '') {

        searchObj.style.display = "none";
    }
    else {

        let url = `http://localhost:8085/user/search/${query}`;

        fetch(url).then((res) => {

            return res.json();

        })
            .then((data) => {

                console.log(data)

                let text = `<div class='list-group'>`;

                data.forEach(e => {

                    text += `<a href='/user/show_contacts/contact/${e.cid}' class='list-group-item list-group-action'>${e.name}</a>`
                });
                text += `</div>`;

                searchObj.innerHTML = text;

                searchObj.style.display = "block";


            }).catch((err) => {
                 console.log(err);


            })


    }
}
