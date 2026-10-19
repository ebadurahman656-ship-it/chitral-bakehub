package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppRole
import com.example.model.CustomerNavTab
import com.example.ui.components.BakeryDetailBottomSheet
import com.example.ui.components.CheckoutBottomSheet
import com.example.ui.components.LocationSelectionDialog
import com.example.ui.components.OrderTrackingSheet
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.BakeHubViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BakeHubApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BakeHubApp(
    viewModel: BakeHubViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showRoleModal by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button on secondary roles or sheets
    BackHandler(enabled = uiState.currentRole != AppRole.CUSTOMER || uiState.trackedOrder != null || uiState.isCheckoutOpen || uiState.selectedBakeryForDetail != null) {
        when {
            uiState.selectedBakeryForDetail != null -> viewModel.setSelectedBakery(null)
            uiState.isCheckoutOpen -> viewModel.setCheckoutOpen(false)
            uiState.trackedOrder != null -> viewModel.setTrackedOrder(null)
            uiState.currentRole != AppRole.CUSTOMER -> viewModel.setRole(AppRole.CUSTOMER)
            uiState.currentCustomerTab != CustomerNavTab.HOME -> viewModel.setCustomerTab(CustomerNavTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WarmCream,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = ChocolateBrown,
                    contentColor = PureWhite,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = data.visuals.message, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        },
        bottomBar = {
            if (uiState.currentRole == AppRole.CUSTOMER) {
                Surface(
                    color = PureWhite,
                    shadowElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarmBeigeBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    NavigationBar(
                        containerColor = PureWhite,
                        contentColor = ChocolateBrown,
                        tonalElevation = 0.dp
                    ) {
                        CustomerNavTab.values().forEach { tab ->
                            val isSelected = tab == uiState.currentCustomerTab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.setCustomerTab(tab) },
                                icon = {
                                    Text(
                                        text = tab.iconEmoji,
                                        fontSize = if (isSelected) 22.sp else 18.sp
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) BakeryOrange else MutedBrownText
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BakeryOrange,
                                    selectedTextColor = BakeryOrange,
                                    indicatorColor = SoftBakeryBeige,
                                    unselectedIconColor = MutedBrownText,
                                    unselectedTextColor = MutedBrownText
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            } else {
                // Secondary Roles Floating Return Bar
                Surface(
                    color = ChocolateBrown,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = uiState.currentRole.iconEmoji, fontSize = 20.sp)
                            Column {
                                Text(
                                    text = uiState.currentRole.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GoldenHoney,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Demo simulation active",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoftBakeryBeige,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.setRole(AppRole.CUSTOMER) },
                            colors = ButtonDefaults.buttonColors(containerColor = BakeryOrange),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Customer View →", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentRole) {
                AppRole.CUSTOMER -> {
                    when (uiState.currentCustomerTab) {
                        CustomerNavTab.HOME -> CustomerHomeScreen(
                            uiState = uiState,
                            viewModel = viewModel,
                            onRoleClick = { showRoleModal = true }
                        )
                        CustomerNavTab.SEARCH -> CustomerSearchScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                        CustomerNavTab.ORDERS -> CustomerOrdersScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                        CustomerNavTab.PROFILE -> CustomerProfileScreen(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }
                AppRole.BAKERY_MERCHANT -> BakeryDashboardScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                AppRole.RIDER -> RiderDispatchScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                AppRole.ADMIN -> AdminConsoleScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                AppRole.PWA_WEB -> PwaWebViewScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    // Role Switcher Modal
    if (showRoleModal) {
        ModalBottomSheet(
            onDismissRequest = { showRoleModal = false },
            containerColor = PureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Chitral BakeHub Ecosystem",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ChocolateBrown
                )
                Text(
                    text = "Select a perspective to test:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedBrownText
                )

                Spacer(modifier = Modifier.height(14.dp))

                AppRole.values().forEach { role ->
                    val isSelected = role == uiState.currentRole
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) SoftBakeryBeige else WarmCream,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) BakeryOrange else WarmBeigeBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                viewModel.setRole(role)
                                showRoleModal = false
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = role.iconEmoji, fontSize = 22.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = role.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) BakeryOrange else DeepBrownText
                                )
                                Text(
                                    text = role.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedBrownText
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Location Picker Dialog
    if (uiState.isLocationPickerOpen) {
        LocationSelectionDialog(
            currentLocation = uiState.selectedLocation,
            onSelectLocation = { viewModel.setLocation(it) },
            onDismiss = { viewModel.toggleLocationPicker(false) }
        )
    }

    // Checkout Bottom Sheet
    if (uiState.isCheckoutOpen) {
        CheckoutBottomSheet(
            cartItems = uiState.cartItems,
            selectedOrderType = uiState.selectedOrderType,
            selectedDate = uiState.scheduledDate,
            selectedSlot = uiState.scheduledSlot,
            deliveryLocation = uiState.selectedLocation,
            onOrderTypeChange = { viewModel.setOrderType(it) },
            onScheduleChange = { d, s -> viewModel.setScheduleDetails(d, s) },
            onPlaceOrder = { method, notes -> viewModel.placeOrder(method, notes) },
            onDismiss = { viewModel.setCheckoutOpen(false) }
        )
    }

    // Order Tracking Sheet
    uiState.trackedOrder?.let { order ->
        OrderTrackingSheet(
            order = order,
            onDismiss = { viewModel.setTrackedOrder(null) }
        )
    }

    // Bakery Detail Bottom Sheet
    uiState.selectedBakeryForDetail?.let { bakery ->
        BakeryDetailBottomSheet(
            bakery = bakery,
            products = uiState.products,
            cartItems = uiState.cartItems,
            onAddToCart = { viewModel.addToCart(it) },
            onIncrement = { viewModel.incrementCartItem(it) },
            onDecrement = { viewModel.decrementCartItem(it) },
            onDismiss = { viewModel.setSelectedBakery(null) }
        )
    }
}
