// side bar

const openSidebar = () => {



    let sidebarObj = document.getElementById('side-bar')
    let contentObj = document.getElementById('content')
    let checkObj = document.getElementById('check')

        sidebarObj.style.display = 'none'

        // alert(contentObj);

        contentObj.style.marginLeft = '1%'
    

}

const closeSidebar = () => {



    let sidebarObj = document.getElementById('side-bar')
    let contentObj = document.getElementById('content')


    sidebarObj.style.display = 'block'

    // alert(contentObj);

    contentObj.style.marginLeft = '20%'
}


