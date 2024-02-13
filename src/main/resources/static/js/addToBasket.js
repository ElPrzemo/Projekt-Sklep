function addToBasket(productId) {
    const formData = new FormData();
    formData.append('quantity', 1); // Zakładając, że zawsze dodajesz 1 sztukę produktu

    fetch(`/basket/add/${productId}`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert("Produkt dodany do koszyka!");
                // Tutaj możesz dodać logikę odświeżania widoku koszyka lub strony
            } else {
                alert("Nie udało się dodać produktu do koszyka.");
            }
        })
        .catch(error => console.error('Błąd:', error));
}