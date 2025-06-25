// Wait for the DOM to be fully loaded
document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);

    // Enable tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function(tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Enable popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function(popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Product quantity increment/decrement
    const quantityInputs = document.querySelectorAll('.quantity-input');
    quantityInputs.forEach(function(input) {
        const decrementBtn = input.parentElement.querySelector('.decrement-btn');
        const incrementBtn = input.parentElement.querySelector('.increment-btn');

        if (decrementBtn) {
            decrementBtn.addEventListener('click', function() {
                if (input.value > 1) {
                    input.value = parseInt(input.value) - 1;
                    // Trigger change event to update any dependent elements
                    input.dispatchEvent(new Event('change'));
                }
            });
        }

        if (incrementBtn) {
            incrementBtn.addEventListener('click', function() {
                const max = input.getAttribute('max') || 100;
                if (parseInt(input.value) < parseInt(max)) {
                    input.value = parseInt(input.value) + 1;
                    // Trigger change event to update any dependent elements
                    input.dispatchEvent(new Event('change'));
                }
            });
        }
    });

    // Form validation
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Dynamic price calculation for product quantity changes
    const updateTotalPrice = function() {
        const quantityInput = document.getElementById('quantity');
        const priceElement = document.getElementById('product-price');
        const totalPriceElement = document.getElementById('total-price');
        
        if (quantityInput && priceElement && totalPriceElement) {
            const quantity = parseInt(quantityInput.value);
            const price = parseFloat(priceElement.getAttribute('data-price'));
            const total = (quantity * price).toFixed(2);
            totalPriceElement.textContent = total;
        }
    };

    // Add event listener to quantity input if it exists
    const quantityInput = document.getElementById('quantity');
    if (quantityInput) {
        quantityInput.addEventListener('change', updateTotalPrice);
        // Initial calculation
        updateTotalPrice();
    }
});