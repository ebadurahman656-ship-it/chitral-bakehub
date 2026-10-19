package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun RiderDispatchScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    val activeDeliveries = uiState.orders.filter {
        it.status == OrderStatus.READY_FOR_PICKUP || it.status == OrderStatus.OUT_FOR_DELIVERY
    }
    val completedDeliveries = uiState.orders.filter { it.status == OrderStatus.DELIVERED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // Rider Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = RoastedCoffee,
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "RIDER DISPATCH NETWORK",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldenHoney,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Karim (Rider #14) • On Shift",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = DarkGreenSuccess
                    ) {
                        Text(
                            text = "ONLINE (CHITRAL TOWN)",
                            style = MaterialTheme.typography.labelSmall,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "ACTIVE DELIVERIES (${activeDeliveries.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BakeryOrange
                )
            }

            if (activeDeliveries.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🛵", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No active pickup requests right now",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown
                            )
                            Text(
                                text = "Standing by for orders from Shahi Bazar and Chew Bazar ovens.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedBrownText
                            )
                        }
                    }
                }
            } else {
                items(activeDeliveries) { order ->
                    RiderOrderCard(
                        order = order,
                        enteredPin = uiState.riderPinInput,
                        feedbackError = uiState.riderStatusFeedback,
                        onPinChange = { viewModel.setRiderPinInput(it) },
                        onConfirmPickup = { viewModel.confirmRiderPickup(order.id) },
                        onMarkDelivered = { viewModel.markOrderDelivered(order.id) }
                    )
                }
            }

            if (completedDeliveries.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "COMPLETED TODAY (${completedDeliveries.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = RoastedCoffee
                    )
                }

                items(completedDeliveries) { order ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${order.orderNumber} • ${order.bakeryName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBrownText
                                )
                                Text(
                                    text = "Delivered to ${order.deliveryAddress}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedBrownText
                                )
                            }
                            Text(
                                text = "Rs. ${order.deliveryFeePkr} earned",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreenSuccess
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiderOrderCard(
    order: Order,
    enteredPin: String,
    feedbackError: String?,
    onPinChange: (String) -> Unit,
    onConfirmPickup: () -> Unit,
    onMarkDelivered: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(2.dp, BakeryOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.testTag("rider_order_${order.orderNumber}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORDER ${order.orderNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (order.status == OrderStatus.READY_FOR_PICKUP) DarkAmberWarning else DarkGreenSuccess
                ) {
                    Text(
                        text = if (order.status == OrderStatus.READY_FOR_PICKUP) "PICKUP STAGE" else "TRANSIT STAGE",
                        style = MaterialTheme.typography.labelSmall,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step 1: Bakery Pickup Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SoftBakeryBeige,
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "1. PICKUP AT BAKERY",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoastedCoffee,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = order.bakeryName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrownText
                    )
                    Text(
                        text = "Packages: ${order.items.size} (Fragile Warm Bakery Box)",
                        style = MaterialTheme.typography.bodySmall,
                        color = RoastedCoffee
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Step 2: Customer Delivery Card
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = WarmCream,
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "2. DELIVER TO CUSTOMER",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoastedCoffee,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${order.customerName} (${order.customerPhoneMasked})",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepBrownText
                    )
                    Text(
                        text = order.deliveryAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = DeepBrownText
                    )
                    Text(
                        text = "Collect: Rs. ${order.totalPkr} (${order.paymentMethod})",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = BakeryOrange,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action based on status
            if (order.status == OrderStatus.READY_FOR_PICKUP) {
                Text(
                    text = "Verify 4-Digit Handover PIN from Baker:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = onPinChange,
                        placeholder = { Text("e.g. ${order.handoverPin}", fontSize = 12.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("rider_pin_input"),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = onConfirmPickup,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("rider_confirm_pickup_button")
                    ) {
                        Text("Confirm Pickup", color = PureWhite, fontWeight = FontWeight.Bold)
                    }
                }

                if (!feedbackError.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = feedbackError, color = DarkRedError, style = MaterialTheme.typography.labelSmall)
                }
            } else {
                // Out for delivery -> Big Delivered Button
                Button(
                    onClick = onMarkDelivered,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGreenSuccess),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("rider_mark_delivered_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = PureWhite)
                        Text(
                            text = "Slide/Tap to Confirm Handover to Customer",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }
                }
            }
        }
    }
}
