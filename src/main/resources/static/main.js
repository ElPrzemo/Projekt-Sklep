document.addEventListener('DOMContentLoaded', function () {
    updatePanels();

    var messages = [
        "Kupuj u nas, mamy kozackie promocje!",
        "Tutaj ziomek zero ściemy\nTylko u nas znajdziesz takie ceny!",
        "Nie czekaj, tylko kupuj już dziś\nI ciesz się zakupami bez zmartwień!",
        "Koniec wygłupów, tylko u nas kupuj!\nBo mamy to, czego szukasz, i dużo więcej!",
        "Nasze produkty to gwarancja jakości\nI satysfakcja dla każdego klienta!",
        "Nasze produkty to gwarancja jakości\nTo jest sklep dla najfajnijeszych gości!",
        "Bo u nas każdy znajdzie coś dla siebie\nI będzie zadowolony z zakupów!"
    ];
    var messageDiv = document.getElementById('messageDiv');
    var index = 0;
    setInterval(function () {
        messageDiv.innerHTML = messages[index];
        index = (index + 1) % messages.length;
    }, 5000);
});

function updatePanels() {
    var userLoggedIn = false;
    var userRole = '';

    fetch('/api/user/status')
        .then(response => response.json())
        .then(data => {
            userLoggedIn = data.loggedIn;
            userRole = data.role;

            var adminPanel = document.getElementById('adminPanel');
            var userPanel = document.getElementById('userPanel');

            if (userLoggedIn) {
                adminPanel.style.display = userRole === 'admin' ? 'block' : 'none';
                userPanel.style.display = userRole === 'user' ? 'block' : 'none';
            } else {
                adminPanel.style.display = 'none';
                userPanel.style.display = 'none';
            }
        })
        .catch(error => console.error('Error:', error));
}