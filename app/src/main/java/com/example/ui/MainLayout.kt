package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CeylonGold
import com.example.ui.theme.ForestGreen
import com.example.viewmodel.ErpScreen
import com.example.viewmodel.ErpViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(viewModel: ErpViewModel) {
    val currentActiveScreen by viewModel.currentScreen.collectAsState()
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600

    var showCopilotSheet by remember { mutableStateOf(false) }
    val aiResponseText by viewModel.aiResponse.collectAsState()
    val aiLoading by viewModel.aiLoading.collectAsState()
    var copilotQuery by remember { mutableStateOf("") }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Sidebar/Drawer items definition
    val navItems = listOf(
        Triple(ErpScreen.DASHBOARD, "Executive", Icons.Default.Home),
        Triple(ErpScreen.RAW_MATERIALS, "Procure", Icons.Default.List),
        Triple(ErpScreen.RECIPES, "Recipes", Icons.Default.Build),
        Triple(ErpScreen.MANUFACTURING, "Batch Line", Icons.Default.PlayArrow),
        Triple(ErpScreen.SALES, "Sales Hub", Icons.Default.ShoppingCart),
        Triple(ErpScreen.FINANCE, "Ledger", Icons.Default.Settings),
        Triple(ErpScreen.DISTRIBUTORS, "Distribution", Icons.Default.Place),
        Triple(ErpScreen.AI_ASSISTANT, "BI Oracle", Icons.Default.Info),
        Triple(ErpScreen.REPORTS, "Reports Admin", Icons.Default.Email)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isWideScreen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ForestGreen)
                        .padding(24.dp)
                ) {
                    Text("CEYVANA ERP", color = CeylonGold, fontSize = 20.sp, fontWeight = FontWeight.Black)
                    Text("Ceylon Spice Management", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                navItems.forEach { (screen, label, icon) ->
                    NavigationDrawerItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        selected = currentActiveScreen == screen,
                        onClick = {
                            viewModel.navigateTo(screen)
                            coroutineScope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = ForestGreen.copy(alpha = 0.15f),
                            selectedIconColor = ForestGreen,
                            selectedTextColor = ForestGreen,
                            unselectedTextColor = Color.DarkGray,
                            unselectedIconColor = Color.Gray
                        ),
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 2.dp)
                            .testTag("nav_${screen.name.lowercase()}")
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("CEYVANA Premium Ceylon Spices ERP", fontSize = 14.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CeylonGold)
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("Premium", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    },
                    navigationIcon = {
                        if (!isWideScreen) {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, "Menu", tint = ForestGreen)
                            }
                        }
                    },
                    actions = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50)) // Green indicator for Online / Auto Saving
                            )
                            Text("Offline Match Sync Active", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        // Show first 5 key destinations on mobile bottom bar
                        navItems.take(5).forEach { (screen, label, icon) ->
                            NavigationBarItem(
                                selected = currentActiveScreen == screen,
                                onClick = { viewModel.navigateTo(screen) },
                                icon = { Icon(icon, label) },
                                label = { Text(label, fontSize = 10.sp, maxLines = 1) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = ForestGreen,
                                    indicatorColor = ForestGreen,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCopilotSheet = true },
                    containerColor = ForestGreen,
                    contentColor = CeylonGold,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("ai_copilot_fab").padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "AI Copilot",
                            tint = CeylonGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "AI Copilot",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Large Screen Adaptive Sidebar menu drawer
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = Color.White,
                        header = {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 16.dp)
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ForestGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("C", color = CeylonGold, fontSize = 22.sp, fontWeight = FontWeight.Black)
                            }
                        },
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        navItems.forEach { (screen, label, icon) ->
                            NavigationRailItem(
                                selected = currentActiveScreen == screen,
                                onClick = { viewModel.navigateTo(screen) },
                                icon = { Icon(icon, label) },
                                label = { Text(label, fontSize = 10.sp) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    selectedTextColor = ForestGreen,
                                    indicatorColor = ForestGreen,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                )
                            )
                        }
                    }
                }

                // Main Adaptive Content Body Panel
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    ScreenSwitcher(currentActiveScreen, viewModel)
                }
            }
        }
    }

    if (showCopilotSheet) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showCopilotSheet = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
                    .padding(4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Copilot Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Info, "AI Logo", tint = CeylonGold, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("CEYVANA Copilot", fontSize = 14.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                                Text("Intelligent ERP Virtual Advisor", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = { showCopilotSheet = false }) {
                            Icon(Icons.Default.Close, "Close", tint = Color.Gray)
                        }
                    }
                    
                    Divider(color = Color(0xFFF1F5F9))
                    
                    // Quick analyst chips
                    Text("Ask Ceyvana AI:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CeylonGold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "What are today's sales?",
                            "Estimate spice shortages",
                            "Show product profitability",
                            "Weekly production advice",
                            "Outstanding invoices?"
                        ).forEach { queryText ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .clickable {
                                        viewModel.requestAiInsight("Direct custom question: $queryText")
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(queryText, fontSize = 10.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    // Conversation scrolling window
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (aiLoading) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(color = ForestGreen, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Consulting CEYVANA AI Oracle...", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = aiResponseText.ifEmpty { "Welcome to Ceyvana ERP System AI assistant. How can I optimize raw spice procurement or recalculate warehouse recipes today?" },
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                    
                    // Input Text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = copilotQuery,
                            onValueChange = { copilotQuery = it },
                            placeholder = { Text("Query Ceyvana advisor...", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 11.sp)
                        )
                        IconButton(
                            onClick = {
                                if (copilotQuery.isNotEmpty()) {
                                    viewModel.requestAiInsight("Direct custom question: $copilotQuery")
                                    copilotQuery = ""
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ForestGreen)
                                .size(36.dp)
                        ) {
                            Icon(Icons.Default.Send, "Send", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenSwitcher(screen: ErpScreen, viewModel: ErpViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = screen == ErpScreen.DASHBOARD,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DashboardScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.RAW_MATERIALS,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            RawMaterialScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.RECIPES,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            RecipeScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.MANUFACTURING,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ManufacturingScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.SALES,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SalesScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.FINANCE,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FinanceScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.DISTRIBUTORS,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DistributorScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.AI_ASSISTANT,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AiAssistantScreen(viewModel)
        }

        AnimatedVisibility(
            visible = screen == ErpScreen.REPORTS,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ReportsScreen(viewModel)
        }
    }
}
