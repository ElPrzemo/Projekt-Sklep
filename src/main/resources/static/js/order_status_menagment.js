// orderStatusManagement.js

document.addEventListener("DOMContentLoaded", function() {
    // Tutaj możesz umieścić wszelkie inicjalizacje
});

function changeOrderStatus(orderId) {
    var newStatus = document.getElementById('statusSelect' + orderId).value;
    if(newStatus) {
        fetch('/admin/changeOrderStatus/' + orderId + '?newStatus=' + newStatus, {
            method: 'POST',
            headers: {
                // 'X-CSRF-TOKEN': 'Twój_Token_CSRF_Tutaj' // Jeśli używasz CSRF
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