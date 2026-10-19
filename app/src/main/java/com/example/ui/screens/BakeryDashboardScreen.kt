package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OvenStatus
import com.example.model.OrderStatus
import com.example.model.OrderType
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubUiState
import com.example.viewmodel.BakeHubViewModel

@Composable
fun BakeryDashboardScreen(
    uiState: BakeHubUiState,
    viewModel: BakeHubViewModel,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) } // 0: KDS Orders, 1: Product Stock & Oven Capacity
    val incomingOrders = uiState.orders.filter { it.status == OrderStatus.PLACED }
    val bakingOrders = uiState.orders.filter { it.status == OrderStatus.ACCEPTED || it.status == OrderStatus.IN_OVEN }
    val readyOrders = uiState.orders.filter { it.status == OrderStatus.READY_FOR_PICKUP }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
    ) {
        // Top Merchant App Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChocolateBrown,
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
                            text = "CHITRAL BAKEHUB • KITCHEN OS",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldenHoney,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Hindukush Artisanal Bakers",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    // Global Oven Capacity Selector
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when (uiState.bakeryOvenStatus) {
                            OvenStatus.ACTIVE -> DarkGreenSuccess
                            OvenStatus.BUSY -> DarkAmberWarning
                            OvenStatus.PAUSED -> DarkRedError
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val next = when (uiState.bakeryOvenStatus) {
                                    OvenStatus.ACTIVE -> OvenStatus.BUSY
                                    OvenStatus.BUSY -> OvenStatus.PAUSED
                                    OvenStatus.PAUSED -> OvenStatus.ACTIVE
                                }
                                viewModel.setOvenThrottle(next)
                            }
                            .testTag("oven_capacity_toggle")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🔥", fontSize = 12.sp)
                            Text(
                                text = uiState.bakeryOvenStatus.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // KPI Overview Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoastedCoffee,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "TODAY'S ORDERS", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            Text(text = "${uiState.orders.size} Tickets", style = MaterialTheme.typography.titleSmall, color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoastedCoffee,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "TODAY'S REVENUE", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            val revenue = uiState.orders.sumOf { it.totalPkr }
                            Text(text = "Rs. $revenue", style = MaterialTheme.typography.titleSmall, color = GoldenHoney, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoastedCoffee,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = "AVG PREP SPEED", style = MaterialTheme.typography.labelSmall, color = SoftBakeryBeige, fontSize = 9.sp)
                            Text(text = "22 mins", style = MaterialTheme.typography.titleSmall, color = PureWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = PureWhite,
            contentColor = BakeryOrange,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                    color = BakeryOrange
                )
            }
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = {
                    Text(
                        text = "Orders (${incomingOrders.size + bakingOrders.size + readyOrders.size})",
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 0) BakeryOrange else DeepBrownText
                    )
                }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = {
                    Text(
                        text = "Stock & Ovens",
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 1) BakeryOrange else DeepBrownText
                    )
                }
            )
        }

        if (activeTab == 0) {
            // Kitchen Display System Orders Flow
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Incoming Orders Section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "⚠️", fontSize = 16.sp)
                            Text(
                                text = "NEW INCOMING TICKETS (${incomingOrders.size})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = DarkAmberWarning
                            )
                        }
                        if (incomingOrders.isNotEmpty()) {
                            Text(text = "Action Required", style = MaterialTheme.typography.labelSmall, color = DarkAmberWarning, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                if (incomingOrders.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PureWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "No pending incoming orders at this moment. Kitchen is up to date.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedBrownText,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                } else {
                    items(incomingOrders) { order ->
                        IncomingOrderKdsCard(
                            order = order,
                            onAccept = { viewModel.acceptBakeryOrder(order.id) },
                            onReject = { viewModel.rejectBakeryOrder(order.id) }
                        )
                    }
                }

                // In Preparation Section
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "IN THE OVEN / PREPARATION (${bakingOrders.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = BakeryOrange
                    )
                }

                items(bakingOrders) { order ->
                    BakingOrderKdsCard(
                        order = order,
                        onMarkReady = { viewModel.markOrderReadyInOven(order.id) }
                    )
                }

                // Ready For Rider Handover
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "READY FOR RIDER DISPATCH (${readyOrders.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreenSuccess
                    )
                }

                items(readyOrders) { order ->
                    ReadyOrderKdsCard(order = order)
                }
            }
        } else {
            // Product Stock Management
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Stone Oven Throttle & Store Hours",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = ChocolateBrown
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tap to simulate peak rush hours or dough fermentation pauses:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedBrownText
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OvenStatus.values().forEach { st ->
                                    val isSelected = st == uiState.bakeryOvenStatus
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) BakeryOrange else SoftBakeryBeige,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BakeryOrange else WarmBeigeBorder),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { viewModel.setOvenThrottle(st) }
                                    ) {
                                        Text(
                                            text = st.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isSelected) PureWhite else DeepBrownText,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Item Availability & Live In-Stock Toggle",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ChocolateBrown
                    )
                }

                items(uiState.products) { prod ->
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = prod.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepBrownText
                                )
                                Text(
                                    text = "Rs. ${prod.pricePkr} • ${prod.category.title}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedBrownText
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (prod.isLiveInStock) "Available" else "Sold Out",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (prod.isLiveInStock) DarkGreenSuccess else DarkRedError
                                )
                                Switch(
                                    checked = prod.isLiveInStock,
                                    onCheckedChange = { viewModel.toggleProductStock(prod.id) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = PureWhite,
                                        checkedTrackColor = DarkGreenSuccess,
                                        uncheckedThumbColor = PureWhite,
                                        uncheckedTrackColor = WarmBeigeBorder
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncomingOrderKdsCard(
    order: com.example.model.Order,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(2.dp, BakeryOrange),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.testTag("kds_incoming_${order.orderNumber}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(text = order.orderNumber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                    Surface(shape = RoundedCornerShape(4.dp), color = BakeryOrange) {
                        Text(text = order.orderType.displayName, color = PureWhite, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                Text(text = "Rs. ${order.totalPkr}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = BakeryOrange)
            }

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Text(
                    text = "• ${item.quantity}x ${item.product.name} (${item.product.weightOrPortion})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepBrownText
                )
            }

            if (!order.specialNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = RoundedCornerShape(6.dp), color = SoftBakeryBeige) {
                    Text(
                        text = "Customer Note: ${order.specialNotes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = RoastedCoffee,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkRedError),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkRedError),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text("Reject", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                    modifier = Modifier
                        .weight(2f)
                        .height(44.dp)
                        .testTag("accept_order_${order.orderNumber}")
                ) {
                    Text("Accept & Start Oven", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BakingOrderKdsCard(
    order: com.example.model.Order,
    onMarkReady: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${order.orderNumber} • ${order.customerName}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ChocolateBrown)
                Text(text = "🔥 In Oven", style = MaterialTheme.typography.labelSmall, color = BakeryOrange, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            order.items.forEach { item ->
                Text(text = "${item.quantity}x ${item.product.name}", style = MaterialTheme.typography.bodySmall, color = DeepBrownText)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onMarkReady,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreenSuccess),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text("Mark as Ready in Oven (Pass to Counter)", color = PureWhite, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReadyOrderKdsCard(order: com.example.model.Order) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SuccessBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkGreenSuccess)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "${order.orderNumber} Ready at Counter", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = DarkGreenSuccess)
                Text(text = "Rider: ${order.riderName ?: "Assigned"}", style = MaterialTheme.typography.bodySmall, color = DeepBrownText)
                Text(text = "Verification PIN: ${order.handoverPin}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = ChocolateBrown)
            }

            Surface(shape = RoundedCornerShape(6.dp), color = DarkGreenSuccess) {
                Text(text = "Awaiting Rider", color = PureWhite, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}
