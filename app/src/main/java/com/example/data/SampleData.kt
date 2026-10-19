package com.example.data

import com.example.R
import com.example.model.*

object SampleData {

    val deliveryLocations = listOf(
        "Shahi Bazar, Chitral",
        "Chew Bazar, Chitral",
        "Denin Chowk, Chitral",
        "Atalique Bazar, Chitral",
        "Drosh Main Bazar"
    )

    val bakeries = listOf(
        Bakery(
            id = "bakery_1",
            name = "Hindukush Artisanal Bakers",
            location = "Shahi Bazar",
            address = "Shop #14, Old Fort Road, Shahi Bazar, Chitral",
            rating = 4.9,
            reviewCount = 142,
            prepTimeMin = 20,
            prepTimeMax = 35,
            deliveryFeePkr = 80,
            minOrderPkr = 300,
            isOpen = true,
            ovenStatus = OvenStatus.ACTIVE,
            imageRes = R.drawable.img_bakery_hindukush,
            heroImageRes = R.drawable.img_hero_banner,
            description = "Centuries-old stone oven traditions in the heart of Shahi Bazar. Famous for hand-kneaded wild walnut sourdough loaves, hot morning Roghani naans, and mountain honey pastries.",
            specialties = listOf("Walnut Loaves", "Stone Oven", "Wild Honey")
        ),
        Bakery(
            id = "bakery_2",
            name = "Tirich Mir Hearth & Sweets",
            location = "Chew Bazar",
            address = "Main Bypass Road, Near River Bridge, Chew Bazar, Chitral",
            rating = 4.8,
            reviewCount = 98,
            prepTimeMin = 30,
            prepTimeMax = 45,
            deliveryFeePkr = 100,
            minOrderPkr = 350,
            isOpen = true,
            ovenStatus = OvenStatus.ACTIVE,
            imageRes = R.drawable.img_cake_pastry,
            heroImageRes = R.drawable.img_hero_banner,
            description = "Artisan confectionery and bespoke celebration oven bakes. Specializes in multi-tier celebration cakes, rich Belgian dark chocolate fudge, and crispy Chitrali almond tea biscuits.",
            specialties = listOf("Fudge Cakes", "Celebration Bakes", "Biscuits")
        ),
        Bakery(
            id = "bakery_3",
            name = "Pamir Sweets & Pastry Works",
            location = "Denin Chowk",
            address = "Airport Road, Denin Sector 2, Chitral",
            rating = 4.7,
            reviewCount = 64,
            prepTimeMin = 35,
            prepTimeMax = 50,
            deliveryFeePkr = 120,
            minOrderPkr = 400,
            isOpen = true,
            ovenStatus = OvenStatus.BUSY,
            imageRes = R.drawable.img_bakery_hindukush,
            heroImageRes = R.drawable.img_hero_banner,
            description = "Denin's beloved local bakehouse, renowned for slow-cooked cardamom Shahi Halwa puff pastries, sesame crust rolls, and freshly pulled breakfast milk loaves.",
            specialties = listOf("Shahi Halwa Pastries", "Roghani Naan")
        ),
        Bakery(
            id = "bakery_4",
            name = "Drosh Valley Mountain Bakes",
            location = "Drosh Main",
            address = "Bazaar Chowk, Drosh Valley",
            rating = 4.9,
            reviewCount = 82,
            prepTimeMin = 40,
            prepTimeMax = 55,
            deliveryFeePkr = 150,
            minOrderPkr = 500,
            isOpen = true,
            ovenStatus = OvenStatus.ACTIVE,
            imageRes = R.drawable.img_cake_pastry,
            heroImageRes = R.drawable.img_hero_banner,
            description = "Rustic Lower Chitral bakehouse utilizing stone-milled organic whole wheat, sun-dried mountain mulberries, and roasted walnut tea cakes baked fresh every morning.",
            specialties = listOf("Organic Wheat", "Dry Fruit Cakes")
        ),
        Bakery(
            id = "bakery_5",
            name = "Kalash Valley Hearth & Bakes",
            location = "Bumburet Link",
            address = "Ayun-Bumburet Junction Road, Chitral",
            rating = 4.8,
            reviewCount = 57,
            prepTimeMin = 35,
            prepTimeMax = 50,
            deliveryFeePkr = 140,
            minOrderPkr = 450,
            isOpen = true,
            ovenStatus = OvenStatus.ACTIVE,
            imageRes = R.drawable.img_bakery_hindukush,
            heroImageRes = R.drawable.img_hero_banner,
            description = "Preserving authentic valley recipes with wood-smoked Kalash walnut flatbreads, pure butter shortbreads, and seasonal alpine apricot tarts.",
            specialties = listOf("Wood Smoked", "Butter Shortbread", "Apricot Tarts")
        )
    )

    val products = listOf(
        BakeryProduct(
            id = "prod_1",
            bakeryId = "bakery_1",
            name = "Chitrali Walnut Loaf",
            category = BakeryCategory.BREAD,
            pricePkr = 380,
            description = "Stone-hearth artisan sourdough crafted with crushed wild Chitral walnuts and freshly milled flour.",
            weightOrPortion = "550g loaf",
            isLiveInStock = true,
            prepTimeMinutes = 20,
            imageRes = R.drawable.img_hero_banner,
            isPopular = true,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_2",
            bakeryId = "bakery_1",
            name = "Honey Almond Pastry Slice",
            category = BakeryCategory.PASTRIES,
            pricePkr = 220,
            description = "Light butter sponge infused with raw valley blossom honey and topped with toasted almond flakes.",
            weightOrPortion = "1 slice",
            isLiveInStock = true,
            prepTimeMinutes = 15,
            imageRes = R.drawable.img_cake_pastry,
            isPopular = true,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_3",
            bakeryId = "bakery_2",
            name = "Chocolate Walnut Fudge Cake",
            category = BakeryCategory.CAKES,
            pricePkr = 1450,
            description = "Decadent Dutch cocoa cake with dark chocolate ganache and roasted mountain walnuts.",
            weightOrPortion = "2 lbs (Serves 6-8)",
            isLiveInStock = true,
            prepTimeMinutes = 45,
            imageRes = R.drawable.img_cake_pastry,
            isPopular = true,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_4",
            bakeryId = "bakery_3",
            name = "Royal Shahi Halwa Puff Pastry",
            category = BakeryCategory.PASTRIES,
            pricePkr = 190,
            description = "Flaky French puff pastry folded around slow-cooked cardamom halwa and crushed pistachios.",
            weightOrPortion = "2 pieces",
            isLiveInStock = true,
            prepTimeMinutes = 20,
            imageRes = R.drawable.img_bakery_hindukush,
            isPopular = false,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_5",
            bakeryId = "bakery_1",
            name = "Traditional Roghani Naan Package",
            category = BakeryCategory.BREAD,
            pricePkr = 240,
            description = "Crispy clay oven flatbreads enriched with milk, butter, and golden sesame seeds.",
            weightOrPortion = "3 pack",
            isLiveInStock = true,
            prepTimeMinutes = 15,
            imageRes = R.drawable.img_hero_banner,
            isPopular = true,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_6",
            bakeryId = "bakery_2",
            name = "Chitral Almond Butter Biscuits",
            category = BakeryCategory.BISCUITS,
            pricePkr = 320,
            description = "Crisp, crumbly tea biscuits made with pure desi ghee and ground Chitral almonds.",
            weightOrPortion = "Box of 12",
            isLiveInStock = true,
            prepTimeMinutes = 10,
            imageRes = R.drawable.img_cake_pastry,
            isPopular = false,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_7",
            bakeryId = "bakery_1",
            name = "Dark Chocolate Hazelnut Cookies",
            category = BakeryCategory.COOKIES,
            pricePkr = 180,
            description = "Giant soft-baked chocolate chunk cookie with melted chocolate chips.",
            weightOrPortion = "Pack of 2",
            isLiveInStock = true,
            prepTimeMinutes = 10,
            imageRes = R.drawable.img_cake_pastry,
            isPopular = false,
            isRapidOven = true
        ),
        BakeryProduct(
            id = "prod_8",
            bakeryId = "bakery_2",
            name = "Custom 2-Tier Celebration Cake",
            category = BakeryCategory.CUSTOM_CAKES,
            pricePkr = 2850,
            description = "Hand-piped celebratory tiered sponge cake with vanilla buttercream and customized message.",
            weightOrPortion = "4 lbs (Custom text)",
            isLiveInStock = true,
            prepTimeMinutes = 180,
            imageRes = R.drawable.img_cake_pastry,
            isPopular = true,
            isRapidOven = false // Schedule only
        )
    )

    val initialOrders = listOf(
        Order(
            id = "order_4102",
            orderNumber = "#4102",
            bakeryId = "bakery_1",
            bakeryName = "Hindukush Artisanal Bakers",
            customerName = "Ali Rehman",
            customerPhoneMasked = "+92 345 ***8812",
            deliveryAddress = "House 12, Chew Bazar, Near Bridge, Chitral",
            items = listOf(
                CartItem(products[0], 2), // Walnut Loaf
                CartItem(products[1], 1)  // Honey Almond Pastry
            ),
            orderType = OrderType.PREPARE_NOW,
            status = OrderStatus.IN_OVEN,
            placedAtTime = "10:14 AM",
            estimatedDeliveryTime = "10:55 AM (In Oven)",
            subtotalPkr = 980,
            deliveryFeePkr = 80,
            totalPkr = 1060,
            paymentMethod = "Cash on Delivery",
            riderName = "Karim (Rider #14)",
            riderPhone = "+92 345 ***8812",
            handoverPin = "8842",
            specialNotes = "Please slice the walnut loaf."
        ),
        Order(
            id = "order_4098",
            orderNumber = "#4098",
            bakeryId = "bakery_2",
            bakeryName = "Tirich Mir Hearth & Sweets",
            customerName = "Fatima Bibi",
            customerPhoneMasked = "+92 333 ***2291",
            deliveryAddress = "Denin Officers Colony, Block B, Chitral",
            items = listOf(
                CartItem(products[2], 1) // Chocolate Fudge Cake
            ),
            orderType = OrderType.SCHEDULE_FOR_LATER,
            scheduledDate = "Tomorrow, Oct 18",
            scheduledSlot = "10:00 AM - 12:00 PM",
            status = OrderStatus.ACCEPTED,
            placedAtTime = "09:30 AM",
            estimatedDeliveryTime = "Scheduled for Tomorrow 10:00 AM",
            subtotalPkr = 1450,
            deliveryFeePkr = 100,
            totalPkr = 1550,
            paymentMethod = "Bank Transfer",
            riderName = "Assigned Tomorrow",
            riderPhone = null,
            handoverPin = "1923",
            specialNotes = "Write 'Happy 10th Birthday Ayla' on plaque"
        ),
        Order(
            id = "order_4085",
            orderNumber = "#4085",
            bakeryId = "bakery_3",
            bakeryName = "Pamir Sweets & Pastry Works",
            customerName = "Shahid Khan",
            customerPhoneMasked = "+92 312 ***7710",
            deliveryAddress = "Shahi Bazar Market Gate #2",
            items = listOf(
                CartItem(products[3], 3) // Shahi Halwa Pastry
            ),
            orderType = OrderType.PREPARE_NOW,
            status = OrderStatus.DELIVERED,
            placedAtTime = "08:15 AM",
            estimatedDeliveryTime = "Delivered at 09:02 AM",
            subtotalPkr = 570,
            deliveryFeePkr = 120,
            totalPkr = 690,
            paymentMethod = "Cash on Delivery",
            riderName = "Farhan (Rider #8)",
            handoverPin = "5501"
        )
    )

    val deliveryZones = listOf(
        DeliveryZone("zone_1", "Shahi Bazar Central", 80, 0, 25, 4, true),
        DeliveryZone("zone_2", "Chew Bazar & Riverbanks", 100, 20, 35, 3, true),
        DeliveryZone("zone_3", "Denin & Airport Sector", 120, 30, 40, 2, true),
        DeliveryZone("zone_4", "Drosh Valley Highway", 180, 50, 60, 2, true)
    )
}
