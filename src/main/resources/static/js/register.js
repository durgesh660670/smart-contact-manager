

let submitObj = document.getElementById("submit");
let submitObj1 = document.getElementById("submit1");
let formObj = document.getElementById("form1");
let formObj1 = document.getElementById("form2");


submitObj.style.display = 'none';
formObj1.style.display = 'none'

const submitEmail = async () => {


    // console.log("inside submitEmail function....")



    let email = document.getElementById("email").value;

    if (email == '') {
        console.log("email is null")

    }
    else {

        let url = `http://localhost:8085/send_email`;

        await fetch(url, {
            method: "post",
            headers: {
                "content-type": "application/json; charset=utf-8"

            },
            body: JSON.stringify(



                {
                    email
                }
            )
        }).then(res => res.json())
            .then(data => {

                // console.log(data.msg);
                if (data.msg == 'ok') {

                    alert("OTP is send to your email.")
                    formObj.style.display = 'none';
                    formObj1.style.display = 'block';

                }
            })
            .catch((err) => {
                console.log(err);


            })


    }


}


const verifyOpt = async (e) => {


    // console.log("inside verifyOpt function....")



    let otp = document.getElementById("otp").value;

    if (otp == '') {
        // console.log("otp is null")

    }
    else {

        let url = `/process_verify_email_otp`;

        await fetch(url, {
            method: "post",
            headers: {
                "content-type": "application/json; charset=utf-8"

            },
            body: JSON.stringify(



                {
                    otp
                }
            )
        }).then(res => res.json())
            .then(data => {

                // console.log(data.msg);
                if (data.msg == 'success') {



                    document.createElement('form').submit.call(document.getElementById('form1'));
                    
                }
                else if (data.msg == 'OTP does not match') {
                    alert(data.msg)

                }
            })
            .catch((err) => {
                console.log(err);


            })


    }


}


