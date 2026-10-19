package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.OrderType
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    cartItems: List<CartItem>,
    selectedOrderType: OrderType,
    selectedDate: String,
    selectedSlot: String,
    deliveryLocation: String,
    onOrderTypeChange: (OrderType) -> Unit,
    onScheduleChange: (String, String) -> Unit,
    onPlaceOrder: (paymentMethod: String, customerNotes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var customerNotes by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Cash on Delivery (PKR)") }

    val subtotal = cartItems.sumOf { it.product.pricePkr * it.quantity }
    val deliveryFee = 80
    val total = subtotal + deliveryFee

    val dates = listOf("Today, Evening", "Tomorrow, Oct 18", "Sunday, Oct 19", "Monday, Oct 20")
    val slots = listOf("09:00 AM - 11:00 AM", "11:00 AM - 01:00 PM", "02:00 PM - 04:00 PM", "05:00 PM - 07:00 PM")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Review & Place Order",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                    Text(
                        text = "Chitral BakeHub Artisanal Kitchens",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedBrownText
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = ChocolateBrown)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- SECTION 1: DUAL ORDER FULFILLMENT SELECTOR ---
            Text(
                text = "1. Choose Preparation & Delivery Mode",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Prepare Now Mode
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedOrderType == OrderType.PREPARE_NOW) SoftBakeryBeige else PureWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (selectedOrderType == OrderType.PREPARE_NOW) 2.dp else 1.dp,
                        color = if (selectedOrderType == OrderType.PREPARE_NOW) BakeryOrange else WarmBeigeBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOrderTypeChange(OrderType.PREPARE_NOW) }
                        .testTag("mode_prepare_now")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "⚡", fontSize = 16.sp)
                            Text(
                                text = "Prepare Now",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOrderType == OrderType.PREPARE_NOW) BakeryOrange else DeepBrownText
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fresh stone bake. Countdown starts after bakery accepts.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MutedBrownText,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (selectedOrderType == OrderType.PREPARE_NOW) BakeryOrange else WarmBeigeBorder
                        ) {
                            Text(
                                text = "Est. 30-45 mins",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (selectedOrderType == OrderType.PREPARE_NOW) PureWhite else ChocolateBrown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Schedule for Later Mode
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) SoftBakeryBeige else PureWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) 2.dp else 1.dp,
                        color = if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) GoldenHoney else WarmBeigeBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOrderTypeChange(OrderType.SCHEDULE_FOR_LATER) }
                        .testTag("mode_schedule_later")
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🗓️", fontSize = 16.sp)
                            Text(
                                text = "Schedule Later",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) ChocolateBrown else DeepBrownText
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Future celebrations & fresh morning batches.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MutedBrownText,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) GoldenHoney else WarmBeigeBorder
                        ) {
                            Text(
                                text = "Pick Date & Slot",
                                style = MaterialTheme.typography.labelSmall,
                                color = ChocolateBrown,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Schedule Options when Schedule for Later is selected
            if (selectedOrderType == OrderType.SCHEDULE_FOR_LATER) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmCream),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldenHoney)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Select Delivery Date & 2-Hour Slot:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ChocolateBrown
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Date chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            dates.take(3).forEach { d ->
                                val isSelected = d == selectedDate
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) BakeryOrange else PureWhite,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onScheduleChange(d, selectedSlot) }
                                ) {
                                    Text(
                                        text = d.split(",").firstOrNull() ?: d,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) PureWhite else DeepBrownText,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Slot chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            slots.take(2).forEach { s ->
                                val isSelected = s == selectedSlot
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) ChocolateBrown else PureWhite,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onScheduleChange(selectedDate, s) }
                                ) {
                                    Text(
                                        text = s,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) PureWhite else DeepBrownText,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Notice: The bakery will review & confirm your scheduled slot within 60 minutes.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = RoastedCoffee
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WarningBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkAmberWarning.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = DarkAmberWarning, modifier = Modifier.size(16.dp))
                        Text(
                            text = "Live prep promise: Order countdown begins only after the bakery oven confirms your ticket.",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = DarkAmberWarning
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- SECTION 2: DELIVERY ADDRESS ---
            Text(
                text = "2. Delivery Location",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = WarmCream,
                border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BakeryOrange)
                    Column {
                        Text(
                            text = deliveryLocation,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepBrownText
                        )
                        Text(
                            text = "Chitral Valley Rider Network (Dispatched via local hub)",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MutedBrownText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- SECTION 3: ORDER ITEMS BREAKDOWN ---
            Text(
                text = "3. Bakery Basket Items (${cartItems.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )
            Spacer(modifier = Modifier.height(6.dp))
            cartItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.quantity}x ${item.product.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepBrownText
                    )
                    Text(
                        text = "Rs. ${item.product.pricePkr * item.quantity}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer Special Instructions
            OutlinedTextField(
                value = customerNotes,
                onValueChange = { customerNotes = it },
                label = { Text("Special notes for bakery (e.g. Slice loaf, greeting text)", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BakeryOrange,
                    unfocusedBorderColor = WarmBeigeBorder
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // --- SECTION 4: PAYMENT METHOD ---
            Text(
                text = "4. Payment Method",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DeepBrownText
            )
            Spacer(modifier = Modifier.height(6.dp))

            val methods = listOf("Cash on Delivery (PKR)", "Local Digital Wallet (Easypaisa/JazzCash)", "Direct Bank Transfer")
            methods.forEach { method ->
                val isSelected = method == selectedPaymentMethod
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) SoftBakeryBeige else PureWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BakeryOrange else WarmBeigeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedPaymentMethod = method }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedPaymentMethod = method },
                            colors = RadioButtonDefaults.colors(selectedColor = BakeryOrange)
                        )
                        Text(
                            text = method,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepBrownText,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // --- BILL TOTAL ---
            HorizontalDivider(color = WarmBeigeBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Item Subtotal", style = MaterialTheme.typography.bodyMedium, color = MutedBrownText)
                Text(text = "Rs. $subtotal", style = MaterialTheme.typography.bodyMedium, color = DeepBrownText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Local Delivery Fee", style = MaterialTheme.typography.bodyMedium, color = MutedBrownText)
                Text(text = "Rs. $deliveryFee", style = MaterialTheme.typography.bodyMedium, color = DeepBrownText)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Total Amount", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                Text(text = "Rs. $total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BakeryOrange)
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Primary Action Button
            Button(
                onClick = { onPlaceOrder(selectedPaymentMethod, customerNotes) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_order_button")
            ) {
                Text(
                    text = if (selectedOrderType == OrderType.PREPARE_NOW) "Confirm & Request Oven Prep (Rs. $total)" else "Schedule Bakery Order (Rs. $total)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            }
        }
    }
}
