package com.example.model

enum class BakeryCategory(val title: String, val iconEmoji: String) {
    ALL("All", "⭐"),
    CAKES("Cakes", "🎂"),
    BREAD("Bread", "🍞"),
    PASTRIES("Pastries", "🥐"),
    BISCUITS("Biscuits", "🍪"),
    COOKIES("Cookies", "🥠"),
    CUSTOM_CAKES("Custom Cakes", "✨")
}

enum class OvenStatus(val label: String) {
    ACTIVE("Oven Active"),
    BUSY("Busy Oven (+20m)"),
    PAUSED("Orders Paused")
}

data class Bakery(
    val id: String,
    val name: String,
    val location: String,
    val address: String,
    val rating: Double,
    val reviewCount: Int,
    val prepTimeMin: Int,
    val prepTimeMax: Int,
    val deliveryFeePkr: Int,
    val minOrderPkr: Int,
    val isOpen: Boolean = true,
    val ovenStatus: OvenStatus = OvenStatus.ACTIVE,
    val imageRes: Int,
    val heroImageRes: Int? = null,
    val description: String = "",
    val specialties: List<String> = emptyList()
)

data class BakeryProduct(
    val id: String,
    val bakeryId: String,
    val name: String,
    val category: BakeryCategory,
    val pricePkr: Int,
    val description: String,
    val weightOrPortion: String,
    val isLiveInStock: Boolean = true,
    val prepTimeMinutes: Int = 25,
    val imageRes: Int,
    val isPopular: Boolean = false,
    val isRapidOven: Boolean = true // Eligible for Prepare Now
)

data class CartItem(
    val product: BakeryProduct,
    val quantity: Int = 1,
    val specialNotes: String = ""
)

enum class OrderType(val displayName: String) {
    PREPARE_NOW("Prepare Now"),
    SCHEDULE_FOR_LATER("Schedule for Later")
}

enum class OrderStatus(val title: String, val stepIndex: Int) {
    PLACED("Order Placed", 0),
    ACCEPTED("Bakery Accepted", 1),
    IN_OVEN("In the Oven", 2),
    READY_FOR_PICKUP("Ready for Pickup", 3),
    OUT_FOR_DELIVERY("Out for Delivery", 4),
    DELIVERED("Delivered", 5),
    REJECTED("Declined", -1)
}

data class Order(
    val id: String,
    val orderNumber: String,
    val bakeryId: String,
    val bakeryName: String,
    val customerName: String,
    val customerPhoneMasked: String,
    val deliveryAddress: String,
    val items: List<CartItem>,
    val orderType: OrderType,
    val scheduledDate: String? = null,
    val scheduledSlot: String? = null,
    val status: OrderStatus = OrderStatus.PLACED,
    val placedAtTime: String,
    val estimatedDeliveryTime: String,
    val subtotalPkr: Int,
    val deliveryFeePkr: Int,
    val totalPkr: Int,
    val paymentMethod: String = "Cash on Delivery",
    val riderName: String? = "Karim (Rider #14)",
    val riderPhone: String? = "+92 345 ***8812",
    val handoverPin: String = "8842",
    val specialNotes: String? = null
)

enum class AppRole(val title: String, val subtitle: String, val iconEmoji: String) {
    CUSTOMER("Customer App", "Browse, order & track", "🛍️"),
    BAKERY_MERCHANT("Bakery KDS", "Kitchen Display & Oven", "🧑‍🍳"),
    RIDER("Rider Dispatch", "Pickup & delivery PIN", "🛵"),
    ADMIN("Admin Console", "Fleet, zones & metrics", "⚙️"),
    PWA_WEB("PWA Web View", "Installable Android PWA", "🌐")
}

enum class CustomerNavTab(val label: String, val iconEmoji: String) {
    HOME("Home", "🏠"),
    SEARCH("Search", "🔍"),
    ORDERS("Orders", "📋"),
    PROFILE("Profile", "👤")
}

data class DeliveryZone(
    val id: String,
    val name: String,
    val baseFeePkr: Int,
    val winterSurchargePkr: Int,
    val avgDeliveryMins: Int,
    val activeRiders: Int,
    val isEnabled: Boolean = true
)
