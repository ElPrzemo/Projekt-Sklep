document.addEventListener('DOMContentLoaded', function () {
    updatePanels();


    var messages = [
        "Kupuj u nas, bo to jest jedyna okazja, żeby zdobyć wymarzone produkty w tak niskiej cenie!",
        "Tylko u nas znajdziesz produkty, które są tak dobre, że nie będziesz mógł się ich oprzeć!",
        "Nie czekaj, bo oferta wygasa już za chwilę!",
        "Koniec czekania, czas na zakupy! Tylko u nas znajdziesz to, czego szukasz!",
        "Nasze produkty to gwarancja jakości i satysfakcji, więc możesz być pewien, że nie będziesz zawiedziony!",
        "To jest sklep dla prawdziwych pasjonatów! Tylko u nas znajdziesz to, co kochasz!",
        "Bo u nas każdy znajdzie coś dla siebie, bez względu na to, jakie są jego potrzeby i gusta!",
        "Nie czekaj, tylko kupuj już dziś! To jest oferta, której nie możesz przegapić!",
        "Kupuj u nas, bo to jest najlepszy wybór! Nie znajdziesz lepszej oferty w całym mieście!",
        "To jest oferta, która może zmienić Twoje życie! Nie przegap jej!",
        "Tylko u nas znajdziesz produkty, które są tak wyjątkowe, że każdy będzie o nich mówił!",
        "Bo u nas każdy klient jest traktowany wyjątkowo! Jesteśmy tu dla Ciebie!",
        "Kupuj u nas, bo to jest inwestycja w przyszłość! Nasze produkty będą Ci służyć przez wiele lat!",
        "Nie czekaj, tylko kupuj już dziś! To jest oferta, która nie powtórzy się!",
        "Kupuj u nas, bo to jest gwarancja sukcesu! Nasze produkty pomogą Ci osiągnąć Twoje cele!",
        "To jest oferta, która spełni Twoje najskrytsze marzenia! Nie przegap jej!",
        "Bo u nas każdy może zdobyć wszystko, co chce!",
    ]
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