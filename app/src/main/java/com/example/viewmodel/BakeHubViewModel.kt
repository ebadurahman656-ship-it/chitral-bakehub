package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class BakeHubUiState(
    val currentRole: AppRole = AppRole.CUSTOMER,
    val selectedLocation: String = SampleData.deliveryLocations[0],
    val currentCustomerTab: CustomerNavTab = CustomerNavTab.HOME,
    val selectedCategory: BakeryCategory = BakeryCategory.ALL,
    val searchQuery: String = "",
    val bakeries: List<Bakery> = SampleData.bakeries,
    val products: List<BakeryProduct> = SampleData.products,
    val cartItems: List<CartItem> = emptyList(),
    val orders: List<Order> = SampleData.initialOrders,
    val isCheckoutOpen: Boolean = false,
    val selectedOrderType: OrderType = OrderType.PREPARE_NOW,
    val scheduledDate: String = "Tomorrow, Oct 18",
    val scheduledSlot: String = "10:00 AM - 12:00 PM",
    val selectedBakeryForDetail: Bakery? = null,
    val selectedProductForDetail: BakeryProduct? = null,
    val trackedOrder: Order? = null,
    val isLocationPickerOpen: Boolean = false,
    val bakeryOvenStatus: OvenStatus = OvenStatus.ACTIVE,
    val riderPinInput: String = "",
    val riderStatusFeedback: String? = null,
    val deliveryZones: List<DeliveryZone> = SampleData.deliveryZones,
    val commissionPercent: Int = 12,
    val pendingBakeryApprovals: List<String> = listOf(
        "Kafirstan Heritage Honey & Bread",
        "Garam Chashma Walnut Oven"
    ),
    val snackbarMessage: String? = null
)

class BakeHubViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BakeHubUiState())
    val uiState: StateFlow<BakeHubUiState> = _uiState.asStateFlow()

    fun setRole(role: AppRole) {
        _uiState.update { it.copy(currentRole = role) }
    }

    fun setCustomerTab(tab: CustomerNavTab) {
        _uiState.update { it.copy(currentCustomerTab = tab) }
    }

    fun setCategory(category: BakeryCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setLocation(location: String) {
        _uiState.update { it.copy(selectedLocation = location, isLocationPickerOpen = false) }
    }

    fun toggleLocationPicker(open: Boolean) {
        _uiState.update { it.copy(isLocationPickerOpen = open) }
    }

    fun setSelectedBakery(bakery: Bakery?) {
        _uiState.update { it.copy(selectedBakeryForDetail = bakery) }
    }

    fun setSelectedProduct(product: BakeryProduct?) {
        _uiState.update { it.copy(selectedProductForDetail = product) }
    }

    fun setTrackedOrder(order: Order?) {
        _uiState.update { it.copy(trackedOrder = order) }
    }

    fun setCheckoutOpen(open: Boolean) {
        _uiState.update { it.copy(isCheckoutOpen = open) }
    }

    fun setOrderType(orderType: OrderType) {
        _uiState.update { it.copy(selectedOrderType = orderType) }
    }

    fun setScheduleDetails(date: String, slot: String) {
        _uiState.update { it.copy(scheduledDate = date, scheduledSlot = slot) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    // --- CART ACTIONS ---
    fun addToCart(product: BakeryProduct) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.product.id == product.id }
            val updated = if (existing != null) {
                state.cartItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                state.cartItems + CartItem(product = product, quantity = 1)
            }
            state.copy(
                cartItems = updated,
                snackbarMessage = "Added ${product.name} to cart"
            )
        }
    }

    fun incrementCartItem(product: BakeryProduct) {
        _uiState.update { state ->
            val updated = state.cartItems.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
            state.copy(cartItems = updated)
        }
    }

    fun decrementCartItem(product: BakeryProduct) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.product.id == product.id }
            val updated = if (existing != null && existing.quantity > 1) {
                state.cartItems.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity - 1) else it
                }
            } else {
                state.cartItems.filterNot { it.product.id == product.id }
            }
            state.copy(cartItems = updated)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyList()) }
    }

    // --- ORDER PLACEMENT ---
    fun placeOrder(paymentMethod: String, customerNotes: String) {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        val firstProduct = state.cartItems.first().product
        val targetBakery = state.bakeries.find { it.id == firstProduct.bakeryId } ?: state.bakeries.first()

        val subtotal = state.cartItems.sumOf { it.product.pricePkr * it.quantity }
        val deliveryFee = targetBakery.deliveryFeePkr
        val total = subtotal + deliveryFee

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val isScheduled = state.selectedOrderType == OrderType.SCHEDULE_FOR_LATER
        val orderNum = "#${(4103..4199).random()}"

        val newOrder = Order(
            id = "order_${System.currentTimeMillis()}",
            orderNumber = orderNum,
            bakeryId = targetBakery.id,
            bakeryName = targetBakery.name,
            customerName = "Ali Rehman (You)",
            customerPhoneMasked = "+92 345 ***8812",
            deliveryAddress = state.selectedLocation,
            items = state.cartItems,
            orderType = state.selectedOrderType,
            scheduledDate = if (isScheduled) state.scheduledDate else null,
            scheduledSlot = if (isScheduled) state.scheduledSlot else null,
            status = if (isScheduled) OrderStatus.ACCEPTED else OrderStatus.PLACED,
            placedAtTime = currentTime,
            estimatedDeliveryTime = if (isScheduled) {
                "Scheduled for ${state.scheduledDate} (${state.scheduledSlot})"
            } else {
                "Est. 35-45 mins (Oven Live)"
            },
            subtotalPkr = subtotal,
            deliveryFeePkr = deliveryFee,
            totalPkr = total,
            paymentMethod = paymentMethod,
            riderName = "Assigned upon dispatch",
            riderPhone = null,
            handoverPin = "${(1000..9999).random()}",
            specialNotes = customerNotes.ifBlank { null }
        )

        _uiState.update {
            it.copy(
                orders = listOf(newOrder) + it.orders,
                cartItems = emptyList(),
                isCheckoutOpen = false,
                trackedOrder = newOrder,
                currentCustomerTab = CustomerNavTab.ORDERS,
                snackbarMessage = "Order $orderNum confirmed with ${targetBakery.name}!"
            )
        }
    }

    // --- BAKERY MERCHANT ACTIONS ---
    fun acceptBakeryOrder(orderId: String) {
        _uiState.update { state ->
            val updated = state.orders.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        status = OrderStatus.IN_OVEN,
                        estimatedDeliveryTime = "In the Oven (20 mins remaining)"
                    )
                } else order
            }
            state.copy(
                orders = updated,
                snackbarMessage = "Order accepted and queued into oven!"
            )
        }
    }

    fun rejectBakeryOrder(orderId: String) {
        _uiState.update { state ->
            val updated = state.orders.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        status = OrderStatus.REJECTED,
                        estimatedDeliveryTime = "Cancelled by Bakery"
                    )
                } else order
            }
            state.copy(orders = updated, snackbarMessage = "Order declined.")
        }
    }

    fun markOrderReadyInOven(orderId: String) {
        _uiState.update { state ->
            val updated = state.orders.map { order ->
                if (order.id == orderId) {
                    order.copy(
                        status = OrderStatus.READY_FOR_PICKUP,
                        estimatedDeliveryTime = "Ready at counter for Rider Handover"
                    )
                } else order
            }
            state.copy(orders = updated, snackbarMessage = "Order marked ready for dispatch!")
        }
    }

    fun setOvenThrottle(status: OvenStatus) {
        _uiState.update { it.copy(bakeryOvenStatus = status) }
    }

    fun toggleProductStock(productId: String) {
        _uiState.update { state ->
            val updated = state.products.map {
                if (it.id == productId) it.copy(isLiveInStock = !it.isLiveInStock) else it
            }
            state.copy(products = updated)
        }
    }

    // --- RIDER ACTIONS ---
    fun setRiderPinInput(pin: String) {
        _uiState.update { it.copy(riderPinInput = pin) }
    }

    fun confirmRiderPickup(orderId: String) {
        val state = _uiState.value
        val order = state.orders.find { it.id == orderId } ?: return

        // Verify PIN or allow slide
        if (state.riderPinInput.isNotBlank() && state.riderPinInput != order.handoverPin) {
            _uiState.update { it.copy(riderStatusFeedback = "Invalid 4-digit PIN! Merchant code is required.") }
            return
        }

        _uiState.update { s ->
            val updated = s.orders.map {
                if (it.id == orderId) {
                    it.copy(
                        status = OrderStatus.OUT_FOR_DELIVERY,
                        riderName = "Karim (Active)",
                        estimatedDeliveryTime = "Out for Delivery (12 mins away)"
                    )
                } else it
            }
            s.copy(
                orders = updated,
                riderPinInput = "",
                riderStatusFeedback = null,
                snackbarMessage = "Pickup verified! Out for delivery to customer."
            )
        }
    }

    fun markOrderDelivered(orderId: String) {
        _uiState.update { state ->
            val updated = state.orders.map {
                if (it.id == orderId) {
                    it.copy(
                        status = OrderStatus.DELIVERED,
                        estimatedDeliveryTime = "Delivered successfully"
                    )
                } else it
            }
            state.copy(
                orders = updated,
                snackbarMessage = "Order marked as Delivered!"
            )
        }
    }

    // --- ADMIN ACTIONS ---
    fun toggleZone(zoneId: String) {
        _uiState.update { state ->
            val updated = state.deliveryZones.map {
                if (it.id == zoneId) it.copy(isEnabled = !it.isEnabled) else it
            }
            state.copy(deliveryZones = updated)
        }
    }

    fun updateWinterSurcharge(zoneId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.deliveryZones.map {
                if (it.id == zoneId) {
                    val newVal = (it.winterSurchargePkr + delta).coerceAtLeast(0)
                    it.copy(winterSurchargePkr = newVal)
                } else it
            }
            state.copy(deliveryZones = updated)
        }
    }

    fun setCommission(percent: Int) {
        _uiState.update { it.copy(commissionPercent = percent) }
    }

    fun approveBakery(name: String) {
        _uiState.update { state ->
            state.copy(
                pendingBakeryApprovals = state.pendingBakeryApprovals.filterNot { it == name },
                snackbarMessage = "Approved $name for Chitral BakeHub Marketplace!"
            )
        }
    }
}
