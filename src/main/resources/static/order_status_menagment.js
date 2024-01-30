document.addEventListener("DOMContentLoaded", function() {
    // Kod tutaj zostanie wykonany po załadowaniu całego dokumentu
    // Możesz tutaj umieścić wszelkie inicjalizacje, które mają się wykonać po załadowaniu strony
});

function changeOrderStatus(orderId) {
    var selectElement = document.getElementById('statusSelect' + orderId);
    var newStatus = selectElement.value;

    if(newStatus) {
        fetch('/admin/changeOrderStatus/' + orderId + '?newStatus=' + newStatus, {
            method: 'POST',
            headers: {
                // Ustaw odpowiednie nagłówki, np. dla CSRF jeśli jest używany
                // 'X-CSRF-TOKEN': 'Twój_Token_CSRF_Tutaj'
            }
        }).then(response => {
            if(response.ok) {
                alert('Status zamówienia został zaktualizowany.');
                location.reload(); // Odśwież stronę, aby zobaczyć zmiany
            } else {
                alert('Wystąpił problem podczas aktualizacji statusu zamówienia.');
            }
        }).catch(error => {
            console.error('Błąd podczas zmiany statusu zamówienia:', error);
            alert('Wystąpił błąd podczas próby zmiany statusu zamówienia.');
        });
    }
}