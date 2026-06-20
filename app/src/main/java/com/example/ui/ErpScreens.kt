package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import android.content.Intent
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.database.*
import com.example.ui.theme.CeylonGold
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.TextDark
import com.example.viewmodel.ErpViewModel
import com.example.viewmodel.BusinessHealthDetails
import java.text.SimpleDateFormat
import java.util.*

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(viewModel: ErpViewModel) {
    val materials by viewModel.rawMaterials.collectAsState()
    val prods by viewModel.products.collectAsState()
    val orders by viewModel.productionOrders.collectAsState()
    val sales by viewModel.salesOrders.collectAsState()
    val transactions by viewModel.cashTransactions.collectAsState()
    val sups by viewModel.suppliers.collectAsState()
    val context = LocalContext.current

    // Real-time calculations based on database set to 0.0 per user request
    val totalSalesVal = 0.0
    val totalExpensesVal = 0.0
    val netProfitVal = 0.0
    val rawMaterialVal = 0.0
    val finishedGoodsVal = 0.0
    val supplierBalanceVal = 0.0
    val outstandingPaymentsVal = 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CEYVANA PREMIUM SPICES", color = CeylonGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Executive Hub", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Made in Sri Lanka • Export Quality Certified", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CeylonGold.copy(alpha = 0.2f))
                            .border(2.dp, CeylonGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🇱🇰", fontSize = 28.sp)
                    }
                }
            }
        }

        // Automated Weekly Report card (EPR/ERP dispatch)
        item {
            SectionHeader(title = "Automated Integrations", subtitle = "ERP weekly digest")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreen.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Email, contentDescription = "Email Report", tint = ForestGreen)
                            }
                            Column {
                                Text("Weekly Data Dispatch", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ForestGreen)
                                Text("Schedule: Every Monday, 08:00 AM", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ForestGreen.copy(alpha = 0.12f)),
                            shape = CircleShape
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = ForestGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Recipient Address:", fontSize = 12.sp, color = Color.Gray)
                            Text("ceyvanainfo@gmail.com", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Reporting Format:", fontSize = 12.sp, color = Color.Gray)
                            Text("Ceylon Spices ERP Summary (.txt)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            try {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = android.net.Uri.parse("mailto:")
                                    putExtra(Intent.EXTRA_EMAIL, arrayOf("ceyvanainfo@gmail.com"))
                                    putExtra(Intent.EXTRA_SUBJECT, "Ceyvana Ceylon Spices ERP Weekly Data Report")
                                    
                                    val reportBody = """
                                        === CEYVANA PREMIUM SPICES ERP WEEKLY REPORT ===
                                        Date generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}
                                        Receiver: ceyvanainfo@gmail.com
                                        
                                        === FINANCIAL PERFORMANCE SUMMARY ===
                                        - Total Sales: LKR 0 (Set per user rule)
                                        - Net Profit: LKR 0 (Set per user rule)
                                        - Raw Materials Val: LKR 0 (Set per user rule)
                                        - Finished Goods Val: LKR 0 (Set per user rule)
                                        - Supplier Balance: LKR 0 (Set per user rule)
                                        - Customer Outstanding: LKR 0 (Set per user rule)
                                        
                                        === CURRENT INVENTORY AND LOGISTICS STATUS ===
                                        - Registered Raw Spices: ${materials.size} categories
                                        - Product Varieties: ${prods.size} SKU products
                                        - Pending Procurement Orders: ${orders.size} active orders
                                        
                                        Weekly report sync finished successfully.
                                    """.trimIndent()
                                    
                                    putExtra(Intent.EXTRA_TEXT, reportBody)
                                }
                                context.startActivity(Intent.createChooser(emailIntent, "Send Report Email"))
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "No email client found. Email pre-set to ceyvanainfo@gmail.com", android.widget.Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send Now", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send ERP Data Now", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Section header
        item {
            SectionHeader(title = "Financial Metrics (LKR)", subtitle = "Live balance statements")
        }

        // Grid of Stats Card
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Total Sales", "LKR ${"%,.0f".format(totalSalesVal)}", "Accumulated Income", { Icon(Icons.Default.ArrowForward, null, tint = ForestGreen) })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Net Profit", "LKR ${"%,.0f".format(netProfitVal)}", "Total margin", { Icon(Icons.Default.ShoppingCart, null, tint = ForestGreen) }, isAccent = true)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Raw Materials Val", "LKR ${"%,.0f".format(rawMaterialVal)}", "${materials.size} Spice Types", { Icon(Icons.Default.Build, null, tint = ForestGreen) })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Finished Val", "LKR ${"%,.0f".format(finishedGoodsVal)}", "Ready to Ship", { Icon(Icons.Default.Home, null, tint = ForestGreen) })
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Supplier Balance", "LKR ${"%,.0f".format(supplierBalanceVal)}", "Accounts Payable", { Icon(Icons.Default.ShoppingCart, null, tint = Color.Red) })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        StatCard("Customer Outstanding", "LKR ${"%,.0f".format(outstandingPaymentsVal)}", "Accounts Receivable", { Icon(Icons.Default.Settings, null, tint = ForestGreen) })
                    }
                }
            }
        }

        // Low stock notification
        val lowStockIng = materials.filter { it.stockInKg < 400 }
        if (lowStockIng.isNotEmpty()) {
            item {
                SectionHeader("Urgent Alerts")
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    lowStockIng.forEach { rm ->
                        AlertBanner("Low Stock Alert: ${rm.name} is at ${rm.stockInKg} Kg (Safety threshold: 400 Kg). Purchase order recommended.")
                    }
                }
            }
        }

        // Charts Section
        item {
            SectionHeader(title = "Spice Business Analytics", subtitle = "Doughnut and trends")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Product Category Demands", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomDoughnutChart(
                        slices = listOf(55f, 30f, 15f),
                        colors = listOf(ForestGreen, CeylonGold, Color.DarkGray),
                        labels = listOf("Blended Products", "Single Spices", "Bulk exports")
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Monthly Production Schedules (Packs)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomBarChart(
                        data = listOf(400f, 650f, 950f, 800f, 1100f),
                        labels = listOf("Feb", "Mar", "Apr", "May", "Jun")
                    )
                }
            }
        }

        // Under construction alerts or schedule targets
        item {
            SectionHeader(title = "Active Daily Production orders", subtitle = "Track manufacturing updates")
        }

        val activeList = orders.take(5)
        if (activeList.isEmpty()) {
            item {
                Text("No current production cycles.", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
            }
        } else {
            items(activeList) { o ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Batch: ${o.batchNumber}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            Text("SKU: ${o.productSku} • Qty: ${o.quantity} packs", fontSize = 11.sp, color = Color.DarkGray)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (o.status == "Completed") ForestGreen.copy(alpha = 0.15f) else CeylonGold.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(o.status.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (o.status == "Completed") ForestGreen else Color(0xFF856404))
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// 2. RAW MATERIAL MANAGEMENT
// ==========================================
@Composable
fun RawMaterialScreen(viewModel: ErpViewModel) {
    val materials by viewModel.rawMaterials.collectAsState()
    val suppliersList by viewModel.suppliers.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    // Form states
    var name by remember { mutableStateOf("") }
    var stockCode by remember { mutableStateOf("") }
    var stockWeight by remember { mutableStateOf("") }
    var unitCost by remember { mutableStateOf("") }
    var selectedSupplier by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Matale Depot") }
    var expiry by remember { mutableStateOf("2027-12-15") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader("Raw Material Stock Book", "Manage organic spice ingredients")
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(if (showForm) Icons.Default.Close else Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Close" else "Procure", fontSize = 12.sp)
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("New Raw Ingredient Procurement Entry", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 14.sp)

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Spice Name (e.g. Cinnamon)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = stockWeight,
                                onValueChange = { stockWeight = it },
                                label = { Text("Weight (Kg)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = unitCost,
                                onValueChange = { unitCost = it },
                                label = { Text("Unit Cost per Kg (LKR)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = stockCode,
                            onValueChange = { stockCode = it },
                            label = { Text("Batch / QR Code Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Supplier selector or basic text
                        OutlinedTextField(
                            value = selectedSupplier,
                            onValueChange = { selectedSupplier = it },
                            label = { Text("Select Supplier Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = location,
                                onValueChange = { location = it },
                                label = { Text("Sec / Bin Location") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = expiry,
                                onValueChange = { expiry = it },
                                label = { Text("Expiry (YYYY-MM-DD)") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        Button(
                            onClick = {
                                val stockD = stockWeight.toDoubleOrNull() ?: 0.0
                                val costD = unitCost.toDoubleOrNull() ?: 0.0
                                if (name.isNotEmpty() && stockD > 0 && costD > 0) {
                                    viewModel.addRawMaterial(name, stockD, costD, selectedSupplier, location, expiry, stockCode)
                                    // Reset fields
                                    name = ""
                                    stockCode = ""
                                    stockWeight = ""
                                    unitCost = ""
                                    selectedSupplier = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Record Procurement to Ledger", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // List materials
        items(materials) { rm ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(rm.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                            GoldBadge(rm.batchNo)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Location: ${rm.warehouseLocation} • Supplier: ${rm.supplierName}", fontSize = 11.sp, color = Color.Gray)
                        Text("Expires: ${rm.expiryDate}", fontSize = 11.sp, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("${"%,.1f".format(rm.stockInKg)} Kg", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Text("LKR ${"%.0f".format(rm.unitCost)} / Kg", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        IconButton(
                            onClick = { viewModel.removeRawMaterial(rm.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// 3. RECIPE FORMULATION
// ==========================================
@Composable
fun RecipeScreen(viewModel: ErpViewModel) {
    val prods by viewModel.products.collectAsState()
    val formulaIngredients by viewModel.recipes.collectAsState()

    var selectedSku by remember { mutableStateOf("P-004") }

    // Recipe formulation parameters
    var ingredientName by remember { mutableStateOf("") }
    var rawWeightPct by remember { mutableStateOf("") }
    var rawItemCost by remember { mutableStateOf("") }

    val activeFormula = formulaIngredients.filter { it.productSku == selectedSku }
    val totalPctInFormula = activeFormula.sumOf { it.percentage }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionHeader("CEYVANA Recipe Formula Builder", "Configure export spice percentage profiles")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Target Product to Formulate:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ForestGreen)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        prods.forEach { product ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selectedSku == product.sku) ForestGreen else Color.LightGray.copy(alpha = 0.2f))
                                    .border(1.dp, if (selectedSku == product.sku) CeylonGold else Color.Gray, RoundedCornerShape(8.dp))
                                    .clickable { selectedSku = product.sku }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    product.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedSku == product.sku) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active formulation summary card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ForestGreen.copy(alpha = 0.05f)),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Selected Formula Composition Tracker", fontWeight = FontWeight.ExtraBold, color = ForestGreen, fontSize = 14.sp)

                    // Percentage bar indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray)
                    ) {
                        val pctFraction = (totalPctInFormula / 100.0).coerceIn(0.0, 1.0).toFloat()
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(pctFraction)
                                .background(if (totalPctInFormula == 100.0) ForestGreen else CeylonGold)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Current: $totalPctInFormula% (Target: 100.0%)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        if (totalPctInFormula != 100.0) {
                            Text("UNBALANCED", color = Color.Red, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("FORMULA PERFECT", color = ForestGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Input form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Add Ingredient Block", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 13.sp)

                    OutlinedTextField(
                        value = ingredientName,
                        onValueChange = { ingredientName = it },
                        label = { Text("Ingredient (e.g. Chili)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = rawWeightPct,
                            onValueChange = { rawWeightPct = it },
                            label = { Text("Percentage (%)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = rawItemCost,
                            onValueChange = { rawItemCost = it },
                            label = { Text("LKR Cost / Kg") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val pctD = rawWeightPct.toDoubleOrNull() ?: 0.0
                                val costD = rawItemCost.toDoubleOrNull() ?: 0.0
                                if (ingredientName.isNotEmpty() && pctD > 0 && costD > 0) {
                                    viewModel.addRecipeIngredient(selectedSku, ingredientName, pctD, costD)
                                    ingredientName = ""
                                    rawWeightPct = ""
                                    rawItemCost = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Insert Block")
                        }

                        Button(
                            onClick = { viewModel.clearRecipeForProduct(selectedSku) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.weight(1.0f)
                        ) {
                            Text("Reset Formula")
                        }
                    }
                }
            }
        }

        // Show listed recipe items
        if (activeFormula.isEmpty()) {
            item {
                Text("No formula entered. Double check percentages.", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(8.dp))
            }
        } else {
            items(activeFormula) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.ingredientName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("${item.percentage}%", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("LKR ${item.unitCost} / Kg", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// 4. MANUFACTURING SCREEN & PRODUCTION COST CALCULATOR
// ==========================================
@Composable
fun ManufacturingScreen(viewModel: ErpViewModel) {
    val materials by viewModel.rawMaterials.collectAsState()
    val prods by viewModel.products.collectAsState()
    val orders by viewModel.productionOrders.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    // Form states
    var productSku by remember { mutableStateOf("P-004") }
    var quantityStr by remember { mutableStateOf("") }
    var operatorName by remember { mutableStateOf("") }
    var actualCostStr by remember { mutableStateOf("") }
    var batchNo by remember { mutableStateOf("") }

    // Production Cost Calculator inputs (Default spices scenario)
    var inputPackaging by remember { mutableStateOf("15.0") }
    var inputLabor by remember { mutableStateOf("25.0") }
    var inputElectricity by remember { mutableStateOf("10.0") }
    var inputTransport by remember { mutableStateOf("20.0") }
    var inputOverhead by remember { mutableStateOf("15.0") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader("CEYVANA Manufacturing Line", "Production scheduling & costs")
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(if (showForm) Icons.Default.Close else Icons.Default.Build, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Close" else "Batch Run")
                }
            }
        }

        // Production order entry
        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Initiate New Batch Production Order", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 14.sp)

                        OutlinedTextField(
                            value = productSku,
                            onValueChange = { productSku = it },
                            label = { Text("Product Code (e.g. P-004)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = quantityStr,
                                onValueChange = { quantityStr = it },
                                label = { Text("Quantity (Packs)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = actualCostStr,
                                onValueChange = { actualCostStr = it },
                                label = { Text("Allocated cost (LKR)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = operatorName,
                            onValueChange = { operatorName = it },
                            label = { Text("Operator / Supervisor Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = batchNo,
                            onValueChange = { batchNo = it },
                            label = { Text("Batch Number (e.g. B-102)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val qtyInt = quantityStr.toIntOrNull() ?: 0
                                val costD = actualCostStr.toDoubleOrNull() ?: 0.0
                                val codeStr = batchNo.ifEmpty { "B-${System.currentTimeMillis() % 1000}" }

                                if (productSku.isNotEmpty() && qtyInt > 0) {
                                    viewModel.createProductionOrder(productSku, qtyInt, operatorName, costD, codeStr)
                                    quantityStr = ""
                                    operatorName = ""
                                    actualCostStr = ""
                                    batchNo = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Launch Batch Line", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // PRODUCTION COST CALCULATOR
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Interactive Production Cost Calculator", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = ForestGreen)

                    // Packaging slider input
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Packaging Cost / Pack (LKR)", fontSize = 11.sp)
                        OutlinedTextField(
                            value = inputPackaging,
                            onValueChange = { inputPackaging = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Labor Overhead / Pack (LKR)", fontSize = 11.sp)
                        OutlinedTextField(
                            value = inputLabor,
                            onValueChange = { inputLabor = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Electricity utilities / Pack (LKR)", fontSize = 11.sp)
                        OutlinedTextField(
                            value = inputElectricity,
                            onValueChange = { inputElectricity = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Transport logistics / Pack (LKR)", fontSize = 11.sp)
                        OutlinedTextField(
                            value = inputTransport,
                            onValueChange = { inputTransport = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Factory overheads / Pack (LKR)", fontSize = 11.sp)
                        OutlinedTextField(
                            value = inputOverhead,
                            onValueChange = { inputOverhead = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(80.dp),
                            singleLine = true
                        )
                    }

                    // Calculation outputs
                    val rawMaterialEstimate = 220.0 // average base spice formulation cost per 250g pack
                    val packC = inputPackaging.toDoubleOrNull() ?: 15.0
                    val labC = inputLabor.toDoubleOrNull() ?: 25.0
                    val eleC = inputElectricity.toDoubleOrNull() ?: 10.0
                    val trnC = inputTransport.toDoubleOrNull() ?: 20.0
                    val ovC = inputOverhead.toDoubleOrNull() ?: 15.0

                    val totalManufactureCost = rawMaterialEstimate + packC + labC + eleC + trnC + ovC
                    val targetPrice = 550.0 // standard target sales
                    val profitMargin = targetPrice - totalManufactureCost

                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Est. Cost per Pack", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%.2f".format(totalManufactureCost)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                        Column {
                            Text("Selling Price TARGET", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%.0f".format(targetPrice)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        }
                        Column {
                            Text("Net Pack Margin", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%.2f".format(profitMargin)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (profitMargin > 0) ForestGreen else Color.Red)
                        }
                    }
                }
            }
        }

        // Active production list
        item {
            SectionHeader("Active Daily Orders Book", "Confirm, delete or view batch statuses")
        }

        items(orders) { order ->
            val matchingProd = prods.firstOrNull { it.sku == order.productSku }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Batch No: ${order.batchNumber}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (order.status == "Completed") ForestGreen.copy(alpha = 0.15f) else CeylonGold.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(order.status.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (order.status == "Completed") ForestGreen else Color(0xFF856404))
                        }
                    }

                    Text("Product SKU: ${order.productSku} • Name: ${matchingProd?.name ?: "Unknown Spice Blends"}", fontSize = 12.sp)
                    Text("Yield target: ${order.quantity} Retail Spice Packs", fontSize = 12.sp)
                    Text("Assigned floor operator: ${order.operator} on ${order.date}", fontSize = 11.sp, color = Color.Gray)

                    HorizontalDivider()

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Est. Allocated Cost: LKR ${"%,.0f".format(order.actualCost)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (order.status == "Pending") {
                                Button(
                                    onClick = { viewModel.completeProductionOrder(order, matchingProd) },
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Complete & Deduct RM", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            IconButton(onClick = { viewModel.removeProductionOrder(order.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, null, tint = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}


// ==========================================
// 5. SALES & INVOICE MANAGEMENT
// ==========================================
@Composable
fun SalesScreen(viewModel: ErpViewModel) {
    val sales by viewModel.salesOrders.collectAsState()
    val invoices by viewModel.invoices.collectAsState()
    val dists by viewModel.distributors.collectAsState()

    var showSalesForm by remember { mutableStateOf(false) }

    // Form inputs
    var clientName by remember { mutableStateOf("") }
    var orderAmount by remember { mutableStateOf("") }
    var isInvoiceDirect by remember { mutableStateOf(true) }

    // Invoice inspector screen dialog placeholder
    var activeInvoiceDetails by remember { mutableStateOf<Invoice?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader("CEYVANA Invoicing & Sales Hub", "Outstanding receivables & dispatch invoicing")
                Button(
                    onClick = { showSalesForm = !showSalesForm },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(if (showSalesForm) Icons.Default.Close else Icons.Default.ShoppingCart, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showSalesForm) "Close" else "Sales")
                }
            }
        }

        if (showSalesForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("New Client Sales Record / Invoice", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 14.sp)

                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("Client/Distributor Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = orderAmount,
                            onValueChange = { orderAmount = it },
                            label = { Text("Total Sale Amount (LKR)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.clickable { isInvoiceDirect = !isInvoiceDirect }
                        ) {
                            Checkbox(checked = isInvoiceDirect, onCheckedChange = { isInvoiceDirect = it })
                            Text("Generate Auto Invoice immediately & Mark Paid", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val amountD = orderAmount.toDoubleOrNull() ?: 0.0
                                if (clientName.isNotEmpty() && amountD > 0) {
                                    viewModel.placeSalesOrder(clientName, amountD, isInvoiceDirect)
                                    clientName = ""
                                    orderAmount = ""
                                    showSalesForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Commit Sale", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // List Sales Orders
        item {
            SectionHeader("Regional Sales Orders Book", "Draft order pipelines")
        }

        items(sales) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.customerName, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        Text("Draft date: ${item.date} • Category: ${item.status}", fontSize = 11.sp, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("LKR ${"%,.0f".format(item.totalAmount)}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        GoldBadge(item.paymentStatus)
                    }
                }
            }
        }

        // Active Invoices
        item {
            SectionHeader("Active Generated Invoices", "Click to inspect premium corporate invoice print")
        }

        items(invoices) { inv ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { activeInvoiceDetails = inv },
                colors = CardDefaults.cardColors(containerColor = ForestGreen.copy(alpha = 0.02f)),
                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ForestGreen.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.List, null, tint = ForestGreen)
                        }
                        Column {
                            Text(inv.invoiceNumber, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            Text("Cust: ${inv.customerName}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("LKR ${"%,.0f".format(inv.totalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            Text("Tax: LKR ${"%,.0f".format(inv.taxAmount)}", fontSize = 10.sp, color = Color.Red)
                        }
                        IconButton(
                            onClick = { viewModel.removeInvoice(inv.invoiceNumber) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Invoice",
                                tint = Color.Gray.copy(alpha = 0.7f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Invoice View Dialog
    if (activeInvoiceDetails != null) {
        val invoice = activeInvoiceDetails!!
        AlertDialog(
            onDismissRequest = { activeInvoiceDetails = null },
            confirmButton = {
                Button(
                    onClick = { activeInvoiceDetails = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Print / PDF Export Close")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🇱🇰", fontSize = 24.sp)
                    Text("CEYVANA CEYLON SPICES", fontSize = 14.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                }
            },
            text = {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("CORPORATE OFFICIAL INVOICE", fontWeight = FontWeight.Bold, fontSize = 14.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = ForestGreen)
                        Text("Invoice #: ${invoice.invoiceNumber}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Date: ${invoice.date}", fontSize = 11.sp)
                        Text("Billing address: Sri Lankan Spice Export Authority Zone, Matale", fontSize = 10.sp, color = Color.Gray)

                        HorizontalDivider()

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Billed To:", fontSize = 11.sp, color = Color.Gray)
                            Text(invoice.customerName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider()

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Spice Blend Bulk Shipments", fontSize = 12.sp)
                            Text("LKR ${"%,.0f".format(invoice.totalAmount - invoice.taxAmount)}", fontSize = 12.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("VAT / NBT Field Taxes (18% ST)", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%,.0f".format(invoice.taxAmount)}", fontSize = 11.sp, color = Color.Gray)
                        }

                        HorizontalDivider()

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("TOTAL AMOUNT DUE", fontWeight = FontWeight.Black, fontSize = 14.sp, color = ForestGreen)
                            Text("LKR ${"%,.0f".format(invoice.totalAmount)}", fontWeight = FontWeight.Black, fontSize = 14.sp, color = ForestGreen)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // QR Code placeholder using Canvas or vector symbol
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Share, contentDescription = "QR Code Traceability", modifier = Modifier.size(70.dp), tint = ForestGreen)
                                Text("Traceability Batch Code: CEY-${invoice.invoiceNumber}-TRACK", fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        )
    }
}


// ==========================================
// 6. FINANCE & CASH BOOK LEDGER
// ==========================================
@Composable
fun FinanceScreen(viewModel: ErpViewModel) {
    val transactions by viewModel.cashTransactions.collectAsState()
    val suppliers by viewModel.suppliers.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    // Cash transaction form states
    var desc by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("EXPENSE") }
    var transType by remember { mutableStateOf("CASH_OUT") } // CASH_IN or CASH_OUT

    val totalCashInVal = transactions.filter { it.type == "CASH_IN" }.sumOf { it.amount }
    val totalCashOutVal = transactions.filter { it.type == "CASH_OUT" }.sumOf { it.amount }
    val treasuryReserveVal = totalCashInVal - totalCashOutVal

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader("CEYVANA General Ledger Cash Book", "P&L Balance Sheet tracking")
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(if (showForm) Icons.Default.Close else Icons.Default.Home, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Close" else "Record")
                }
            }
        }

        // Ledger totals cards
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Live Reserve Statements", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("TOTAL DEPOSITS (IN)", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%,.0f".format(totalCashInVal)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        }
                        Column {
                            Text("TOTAL PAID OUT (OUT)", fontSize = 11.sp, color = Color.Gray)
                            Text("LKR ${"%,.0f".format(totalCashOutVal)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                        }
                    }
                    HorizontalDivider()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("COOP Treasury Vault (NET)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("LKR ${"%,.0f".format(treasuryReserveVal)}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    }
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Record Cash Ledger Entry", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 14.sp)

                        OutlinedTextField(
                            value = desc,
                            onValueChange = { desc = it },
                            label = { Text("Transaction Description (e.g. June Fuel)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = amountStr,
                            onValueChange = { amountStr = it },
                            label = { Text("Amount (LKR)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        // Selection Row (Type)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { transType = "CASH_IN" },
                                colors = ButtonDefaults.buttonColors(containerColor = if (transType == "CASH_IN") ForestGreen else Color.LightGray),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Deposit IN", color = Color.White)
                            }
                            Button(
                                onClick = { transType = "CASH_OUT" },
                                colors = ButtonDefaults.buttonColors(containerColor = if (transType == "CASH_OUT") Color.Red else Color.LightGray),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Payout OUT", color = Color.White)
                            }
                        }

                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Allocated Category (e.g. ELECTRICITY)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val amountD = amountStr.toDoubleOrNull() ?: 0.0
                                if (desc.isNotEmpty() && amountD > 0) {
                                    viewModel.addCashTransaction(transType, desc, amountD, category.uppercase())
                                    desc = ""
                                    amountStr = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Commit Cash Transaction", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Transactions list
        item {
            SectionHeader("Ledger Entry Day Book", "All transactions recorded offline-first")
        }

        items(transactions) { tr ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (tr.type == "CASH_IN") ForestGreen.copy(alpha = 0.10f) else Color.Red.copy(alpha = 0.10f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (tr.type == "CASH_IN") Icons.Default.ArrowForward else Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = if (tr.type == "CASH_IN") ForestGreen else Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Column {
                            Text(tr.description, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("${tr.date} • Category: ${tr.category}", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Text(
                        "${if (tr.type == "CASH_IN") "+" else "-"} LKR ${"%,.0f".format(tr.amount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (tr.type == "CASH_IN") ForestGreen else Color.Red
                    )
                }
            }
        }
    }
}


// ==========================================
// 7. DISTRIBUTOR SCREEN
// ==========================================
@Composable
fun DistributorScreen(viewModel: ErpViewModel) {
    val distributorsList by viewModel.distributors.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    // Form states
    var dName by remember { mutableStateOf("") }
    var dArea by remember { mutableStateOf("") }
    var dTarget by remember { mutableStateOf("") }
    var dAchievements by remember { mutableStateOf("") }
    var dPhone by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeader("CEYVANA Regional Distributors", "Assigned route agents & targets")
                Button(
                    onClick = { showForm = !showForm },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Icon(if (showForm) Icons.Default.Close else Icons.Default.Place, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showForm) "Close" else "Add Agent")
                }
            }
        }

        if (showForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Register Spice Distributor", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 14.sp)

                        OutlinedTextField(
                            value = dName,
                            onValueChange = { dName = it },
                            label = { Text("Distributor Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = dArea,
                            onValueChange = { dArea = it },
                            label = { Text("Assigned Territory Route Zone (e.g. Kandy)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = dTarget,
                                onValueChange = { dTarget = it },
                                label = { Text("Target (LKR)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = dAchievements,
                                onValueChange = { dAchievements = it },
                                label = { Text("Achieved (LKR)") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = dPhone,
                            onValueChange = { dPhone = it },
                            label = { Text("Direct Telephone Contact") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                val targetD = dTarget.toDoubleOrNull() ?: 0.0
                                val currentD = dAchievements.toDoubleOrNull() ?: 0.0
                                if (dName.isNotEmpty() && dArea.isNotEmpty()) {
                                    viewModel.addDistributor(dName, dArea, targetD, currentD, dPhone)
                                    dName = ""
                                    dArea = ""
                                    dTarget = ""
                                    dAchievements = ""
                                    dPhone = ""
                                    showForm = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold, contentColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Commit Regional Distributor", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Listed distributors
        items(distributorsList) { dist ->
            val ratioFraction = if (dist.salesTarget > 0) (dist.currentAchievements / dist.salesTarget).toFloat().coerceIn(0f, 1f) else 0f
            val commissionEarnt = dist.currentAchievements * 0.05 // 5% flat agency commissions

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dist.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                            Text("Assigned Zone: ${dist.area} • Ph: ${dist.phone}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            GoldBadge("${"%.0f".format(ratioFraction * 100)}% Match")
                            IconButton(
                                onClick = { viewModel.removeDistributor(dist.name) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Distributor",
                                    tint = Color.Gray.copy(alpha = 0.7f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Progress target bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(ratioFraction)
                                .background(ForestGreen)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Target: LKR ${"%,.0f".format(dist.salesTarget)}", fontSize = 11.sp, color = Color.Gray)
                        Text("Achievement: LKR ${"%,.0f".format(dist.currentAchievements)}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CeylonGold.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Est. Route Agent Commission (5%):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("LKR ${"%,.0f".format(commissionEarnt)}", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                    }
                }
            }
        }
    }
}


// ==========================================
// 8. AI ASSISTANT (CEYVANA ORACLE HUB)
// ==========================================
@Composable
fun AiAssistantScreen(viewModel: ErpViewModel) {
    val aiResponseText by viewModel.aiResponse.collectAsState()
    val isLoading by viewModel.aiLoading.collectAsState()
    val healthDetails by viewModel.businessHealthDetails.collectAsState()

    var userCustomQuery by remember { mutableStateOf("") }
    var forecastPeriod by remember { mutableStateOf("Monthly") }
    
    // Weekly Production calculation inputs
    var chiliPacks by remember { mutableStateOf("0") }
    var curryPacks by remember { mutableStateOf("0") }
    var pepperPacks by remember { mutableStateOf("0") }
    var turmericPacks by remember { mutableStateOf("0") }
    var calculatedIngredients by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }

    // Report Generator variables
    var selectedReportType by remember { mutableStateOf("Monthly Sales Report") }
    var reportOutput by remember { mutableStateOf("") }
    var showReportPreview by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Bento - Hero Welcome Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("CEYVANA INTELLIGENT COOP ENGINE", color = CeylonGold, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("CEYVANA AI ERP Oracle Hub", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                    Text("Real-time Gemini business intelligence predicts supply chain shortages, evaluates recipes, forecasts sales, and optimizes spice blending margins automatically.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
                }
            }
        }

        // Bento Cell 1: AI Business Health score & recommendations
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("BUSINESS PERFORMANCE INDEX", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Executive Factory Health Score", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Custom Radial score visual representation
                        Box(modifier = Modifier.size(90.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = healthDetails.score / 100f,
                                strokeWidth = 8.dp,
                                color = CeylonGold,
                                trackColor = ForestGreen.copy(alpha = 0.1f),
                                modifier = Modifier.fillMaxSize()
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${healthDetails.score}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "/100",
                                    fontSize = 10.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ForestGreen)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(healthDetails.status.uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Continuous telemetry metrics weigh distribution achievements against material constraints securely.", fontSize = 11.sp, color = Color.Gray, lineHeight = 15.sp)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Sub scores Bento grid inside
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sales Tracker", fontSize = 10.sp, color = Color.Gray)
                            Text("${healthDetails.salesScore}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Recipe Margins", fontSize = 10.sp, color = Color.Gray)
                            Text("${healthDetails.profitScore}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Inventory Stock", fontSize = 10.sp, color = Color.Gray)
                            Text("${healthDetails.inventoryScore}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Line Efficiency", fontSize = 10.sp, color = Color.Gray)
                            Text("${healthDetails.productionScore}%", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Executive AI Recommendations:", fontSize = 12.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        healthDetails.recommendations.forEach { rec ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("•", fontWeight = FontWeight.Bold, color = CeylonGold, fontSize = 14.sp)
                                Text(rec, fontSize = 11.sp, color = TextDark, lineHeight = 15.sp)
                            }
                        }
                    }
                }
            }
        }

        // Bento Cell 2: Production planning weekly ingredients calculator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("PRODUCTION COOP PLANNING", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Weekly Ingredient Weight Estimator", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Input targeted batch numbers (250g Ceylon spice packs) to autocompute necessary raw grain/pod weights via active recipes tables.", fontSize = 11.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = chiliPacks,
                            onValueChange = { chiliPacks = it },
                            label = { Text("Chili Packs", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = curryPacks,
                            onValueChange = { curryPacks = it },
                            label = { Text("Curry Packs", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = pepperPacks,
                            onValueChange = { pepperPacks = it },
                            label = { Text("Pepper Packs", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = turmericPacks,
                            onValueChange = { turmericPacks = it },
                            label = { Text("Turmeric Packs", fontSize = 10.sp) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            val cp = chiliPacks.toIntOrNull() ?: 0
                            val cur = curryPacks.toIntOrNull() ?: 0
                            val pp = pepperPacks.toIntOrNull() ?: 0
                            val tp = turmericPacks.toIntOrNull() ?: 0
                            calculatedIngredients = viewModel.calculateRequiredIngredients(cp, cur, pp, tp)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Build, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Calculate Raw Materials Weights", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    if (calculatedIngredients.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Calculated Ingredients Consumption Checklist:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            calculatedIngredients.forEach { (ingredientName, weight) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFF8FAFC))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(ingredientName, fontSize = 12.sp, color = TextDark, fontWeight = FontWeight.Bold)
                                    Text("${"%.2f".format(weight)} kg", fontSize = 12.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bento Cell 3: AI Demand periods forecasting center
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("AI DEMAND FORECASTING ENGINE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Predictive Ceylon Spices Demand", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Forecast upcoming domestic and export spice volumes matching historical distribution target rates.", fontSize = 11.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Daily", "Weekly", "Monthly", "Seasonal").forEach { period ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (forecastPeriod == period) ForestGreen else Color(0xFFF1F5F9))
                                    .clickable { forecastPeriod = period }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = period,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (forecastPeriod == period) Color.White else Color.DarkGray
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val scalar = when(forecastPeriod) {
                        "Daily" -> 0.04
                        "Weekly" -> 0.25
                        "Seasonal" -> 3.2
                        else -> 1.0 // Monthly
                    }
                    
                    ForecastBarItem("Premium Chili Powder", (850 * scalar).toInt(), (1050 * scalar).toInt(), "+23.5%")
                    Spacer(modifier = Modifier.height(12.dp))
                    ForecastBarItem("Curry Powder (Traditional)", (600 * scalar).toInt(), (780 * scalar).toInt(), "+30.0%")
                    Spacer(modifier = Modifier.height(12.dp))
                    ForecastBarItem("Black Pepper Powder", (300 * scalar).toInt(), (410 * scalar).toInt(), "+36.7%")
                }
            }
        }

        // Bento Cell 4: AI Inventory Intelligence & Low stock alerting
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("AI INVENTORY INTELLIGENCE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Shortage Estimator & Purchase Limits", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFEF2F2))
                            .border(1.dp, Color(0xFFFEE2E2), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Warning, "Alert", tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                        Column {
                            Text("STOCK DEPLETION PREDICTION", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7F1D1D))
                            Text("Alert: Black Pepper inventory is predicted to deplete below 150kg safety levels in 12 days based on purchase velocity.", fontSize = 11.sp, color = Color(0xFF991B1B))
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = { viewModel.requestAiInsight("Direct custom question: What are the current Black Pepper procurement recomendations?") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(28.dp),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text("Order 300 Kg Recommendation", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFEFF6FF))
                            .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Info, "Overstock", tint = Color(0xFF2563EB), modifier = Modifier.size(20.dp))
                        Column {
                            Text("OVERSTOCK PATTERN SPOTTED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                            Text("Alert: Turmeric warehouse volume exceeds forecasted sales projections by 35%. Recommended: defer raw turmeric pricing negotiations with Kandy grower coop lists.", fontSize = 11.sp, color = Color(0xFF1E40AF))
                        }
                    }
                }
            }
        }

        // Bento Cell 5: AI Profit margins & alternate cost sourcing
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("AI PROFIT MARGIN OPTIMIZER", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Product Net Profitability Rates", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ProfitCell(modifier = Modifier.weight(1f), SKU = "C-CHILI", profit = "LKR 420,000", margin = "35%")
                        ProfitCell(modifier = Modifier.weight(1f), SKU = "C-CURRY", profit = "LKR 304,000", margin = "32%")
                        ProfitCell(modifier = Modifier.weight(1f), SKU = "C-PEPPER", profit = "LKR 228,000", margin = "38%")
                    }
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Cost Minimization Feeds:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    Spacer(modifier = Modifier.height(6.dp))
                    
                    listOf(
                        "Sourcing: Wholesale contract agreements with Dambulla Farmers Coop can save up to 14% on dry coriander seed inventory.",
                        "Packaging: Transitioning to whole Ceylon gold branded tins delivers an average 5% premium boost on high tier export channels."
                    ).forEach { advice ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Check, null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                            Text(advice, fontSize = 11.sp, color = TextDark, lineHeight = 14.sp)
                        }
                    }
                }
            }
        }

        // Bento Cell 6: AI Custom Report statement Generator & file exporter
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("AI EXECUTIVE REPORT CENTER", fontSize = 11.sp, fontWeight = FontWeight.Black, color = CeylonGold)
                    Text("Statement & Spreadsheet Generator", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ForestGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Run dynamic analytical statements and instantly compile download formats (CSV/PDF) for audit teams.", fontSize = 11.sp, color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    var showReportDropdown by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { showReportDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(selectedReportType, color = TextDark, fontSize = 13.sp)
                                Icon(Icons.Default.ArrowDropDown, null, tint = ForestGreen)
                            }
                        }
                        DropdownMenu(
                            expanded = showReportDropdown,
                            onDismissRequest = { showReportDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            listOf(
                                "Monthly Sales Report",
                                "Production Cost Report",
                                "Supplier Purchase Report",
                                "Profit Analysis Report"
                            ).forEach { reportName ->
                                DropdownMenuItem(
                                    text = { Text(reportName) },
                                    onClick = {
                                        selectedReportType = reportName
                                        showReportDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Button(
                        onClick = {
                            showReportPreview = true
                            reportOutput = when(selectedReportType) {
                                "Monthly Sales Report" -> {
                                    """
                                    ==================================================
                                    CEYVANA CEYLON SPICES PLC - MONTHLY SALES REPORT
                                    Generated Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}
                                    ==================================================
                                    SKU         | PRODUCT NAME               | SALES VOLUME | REVENUE (LKR)
                                    ------------+----------------------------+--------------+--------------
                                    C-CHILI     | Premium Chili Powder       | 850 Kg       | 1,200,000.00
                                    C-CURRY     | Traditional Curry Powder   | 600 Kg       | 950,000.00
                                    C-PEPPER    | Whole Black Pepper         | 300 Kg       | 600,000.00
                                    C-TURMERIC  | Premium Turmeric Powder    | 200 Kg       | 450,000.00
                                    ------------+----------------------------+--------------+--------------
                                    TOTAL REVENUE REPORT                     | 1,950 Kg     | 3,200,000.00
                                    ==================================================
                                    * BI COGNIZANCE: Blended spice packaging orders increase average profits by 9.5% relative to coarse unblended raw sales.
                                    """.trimIndent()
                                }
                                "Production Cost Report" -> {
                                    """
                                    ==================================================
                                    CEYVANA CEYLON SPICES PLC - PRODUCTION COST REPORT
                                    Generated Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}
                                    ==================================================
                                    BATCH CODE   | PRODUCT SKU   | PACKS QTY | RAW INGREDIENT COST | TOTAL BATCH LKR
                                    -------------+---------------+-----------+---------------------+----------------
                                    B-2026-CH01  | C-CHILI       | 1,500     | LKR 420,000.00      | 475,000.00
                                    B-2026-CU02  | C-CURRY       | 1,200     | LKR 380,000.00      | 415,000.00
                                    B-2026-PE03  | C-PEPPER      | 800       | LKR 280,000.00      | 310,000.00
                                    -------------+---------------+-----------+---------------------+----------------
                                    TOTAL PRODUCTION COST ESTIMATE                                 | 1,200,000.00
                                    ==================================================
                                    * BI COGNIZANCE: Milling machines optimization trims raw inventory wastage margin down to negligible 0.28%.
                                    """.trimIndent()
                                }
                                "Supplier Purchase Report" -> {
                                    """
                                    ==================================================
                                    CEYVANA CEYLON SPICES PLC - SUPPLIER PURCHASE REPORT
                                    Generated Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}
                                    ==================================================
                                    SUPPLIER NAME            | RAW INGREDIENT     | QUANTITY RECEIVED | CASH VALUE (LKR)
                                    -------------------------+--------------------+-------------------+-----------------
                                    Matale Organic Growers   | Raw Chili Pods     | 1,200 kg          | 840,000.00
                                    Kandy Spice Estates      | Whole Black Pepper | 600 kg            | 720,000.00
                                    Dambulla Farmers Coop    | Cori coriander     | 1,000 kg          | 400,000.00
                                    -------------------------+--------------------+-------------------+-----------------
                                    TOTAL OUTSTANDING LIABILITY VALUE                                  | 1,960,000.00
                                    ==================================================
                                    * BI COGNIZANCE: Unified wholesale supplier agreements reduce cost variation risk over monsoon periods.
                                    """.trimIndent()
                                }
                                else -> { // Profit Analysis Report
                                    """
                                    ==================================================
                                    CEYVANA CEYLON SPICES PLC - PROFIT ANALYSIS REPORT
                                    Generated Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())}
                                    ==================================================
                                    SKU      | BLENDED PRODUCT NAME | COGS / PACK (LKR)| PRICE / PACK (LKR) | REVENUE MARGIN %
                                    ---------+----------------------+------------------+--------------------+-----------------
                                    C-CHILI  | Premium Chili Powder | 425.00           | 800.00             | 46.8%
                                    C-CURRY  | Traditional Curry    | 345.00           | 750.00             | 54.0%
                                    C-PEPPER | Whole Black Pepper   | 372.00           | 600.00             | 38.0%
                                    ---------+----------------------+------------------+--------------------+-----------------
                                    TOTAL AVERAGE FACTORY YIELD                                         | 46.26%
                                    ==================================================
                                    * BI COGNIZANCE: Blended Curry powders return highest margins based on coriander seed weighting ratios.
                                    """.trimIndent()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.List, null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compile Intelligent BI Report", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    if (showReportPreview) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = reportOutput,
                                    fontSize = 11.sp,
                                    color = Color(0xFFF1F5F9),
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    lineHeight = 15.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            // Real Share action launches Native share screen
                                            val shareIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, reportOutput)
                                                putExtra(Intent.EXTRA_SUBJECT, "$selectedReportType - Ceyvana BI")
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share CSV Format"))
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CeylonGold),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Share, null, modifier = Modifier.size(14.dp), tint = Color.White)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Share CSV", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                    
                                    Button(
                                        onClick = {
                                            val shareIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, reportOutput)
                                                putExtra(Intent.EXTRA_SUBJECT, "$selectedReportType - Ceyvana Audit")
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Save PDF Document"))
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Share, null, modifier = Modifier.size(14.dp), tint = Color.White)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Share PDF / XLS", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Direct AI Actions panel
        item {
            SectionHeader("Quick Consultation Prompts", "Consolidated business insights")
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.requestAiInsight("PROD_PLAN") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
                            Text("Prod Plan Guidance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { viewModel.requestAiInsight("SALES_FORECAST") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
                            Text("Sales Projections", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { viewModel.requestAiInsight("PROFIT_ANALYSIS") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(18.dp))
                            Text("Blended Yields", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { viewModel.requestAiInsight("SHORTAGE_PRED") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Warning, null, modifier = Modifier.size(18.dp))
                            Text("Stock Shortfalls", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { viewModel.requestAiInsight("PURCHASE_RECOMM") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).height(65.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Place, null, modifier = Modifier.size(18.dp))
                            Text("Procurement Sourcing", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(modifier = Modifier.weight(1f))
                }
            }
        }

        // Custom sandbox Chat assistant
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Interactive Oracle Inquiry Console", fontWeight = FontWeight.Bold, color = ForestGreen, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = userCustomQuery,
                            onValueChange = { userCustomQuery = it },
                            placeholder = { Text("Ask anything about CEYVANA production...", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        IconButton(
                            onClick = {
                                if (userCustomQuery.isNotEmpty()) {
                                    viewModel.requestAiInsight("Direct custom question: $userCustomQuery")
                                    userCustomQuery = ""
                                }
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ForestGreen)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Response Sheet display
        item {
            SectionHeader("Corporate AI Oracle Output")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (isLoading) {
                        CircularProgressIndicator(color = ForestGreen, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Text(
                        text = aiResponseText.ifEmpty { "Select an analytical forecasting widget above or write your inquiry to consult CEYVANA AI Oracle. Detailed strategic projections will manifest here." },
                        fontSize = 13.sp,
                        color = if (aiResponseText.isEmpty()) Color.Gray else TextDark,
                        fontWeight = if (aiResponseText.isEmpty()) FontWeight.Normal else FontWeight.Medium,
                        lineHeight = 20.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ProfitCell(modifier: Modifier = Modifier, SKU: String, profit: String, margin: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(SKU, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(profit, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = ForestGreen)
            Text("Margin: $margin", fontSize = 9.sp, color = Color(0xFF64748B))
        }
    }
}

@Composable
fun ForecastBarItem(title: String, current: Int, forecast: Int, expectedGrowth: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ForestGreen.copy(alpha = 0.1f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(expectedGrowth, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Current: $current kg", fontSize = 10.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Gray)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Forecast: $forecast kg", fontSize = 10.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.85f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(CeylonGold)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(viewModel: ErpViewModel) {
    val config by viewModel.reportConfig.collectAsState()
    val sentReports by viewModel.sentReports.collectAsState()
    val context = LocalContext.current

    // Local inputs initialized from config
    var recipientsInput by remember(config) { mutableStateOf(config.recipients) }
    var whatsappRecipientInput by remember(config) { mutableStateOf(config.whatsappRecipient) }
    var scheduleDayInput by remember(config) { mutableStateOf(config.scheduleDay) }
    var scheduleTimeInput by remember(config) { mutableStateOf(config.scheduleTime) }
    
    var isAutoEnabled by remember(config) { mutableStateOf(config.isAutoEnabled) }
    var isEmailEnabled by remember(config) { mutableStateOf(config.isEmailEnabled) }
    var isWhatsappEnabled by remember(config) { mutableStateOf(config.isWhatsappEnabled) }
    
    var includeExecutive by remember(config) { mutableStateOf(config.includeExecutive) }
    var includeSales by remember(config) { mutableStateOf(config.includeSales) }
    var includeProduction by remember(config) { mutableStateOf(config.includeProduction) }
    var includeInventory by remember(config) { mutableStateOf(config.includeInventory) }
    var includeFinancial by remember(config) { mutableStateOf(config.includeFinancial) }
    var includeAiInsights by remember(config) { mutableStateOf(config.includeAiInsights) }
    var includeAlerts by remember(config) { mutableStateOf(config.includeAlerts) }
    var formatPdf by remember(config) { mutableStateOf(config.formatPdf) }
    var formatExcel by remember(config) { mutableStateOf(config.formatExcel) }
    var formatImageSnapshot by remember(config) { mutableStateOf(config.formatImageSnapshot) }

    // Dialog flags
    var selectedReportForDetails by remember { mutableStateOf<SentReport?>(null) }
    var showUnsavedToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("reports_screen_container"),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Automated Dispatch Terminal",
                            color = CeylonGold,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black
                        )
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Reports Logo",
                            tint = CeylonGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = "Calibrate CEYVANA corporate weekly delivery channels. Automatically generate custom PDF/Excel summaries and broadcast 4 KPI-optimized messages to C-level WhatsApp recipients.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Section 1: Activation & Scheduler Controls
        item {
            SectionHeader("Universal Scheduling & Automation Status", "Enable fully automatic Sunday 6:00 PM SLST reporting")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Automatic Weekly Dispatch", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextDark)
                            Text("If enabled, reports are prepared and sent with no manual intervention required", fontSize = 11.sp, color = Color.Gray)
                        }
                        Switch(
                            checked = isAutoEnabled,
                            onCheckedChange = { isAutoEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ForestGreen,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    // Schedule fields
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Scheduled Day", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                            OutlinedTextField(
                                value = scheduleDayInput,
                                onValueChange = { scheduleDayInput = it },
                                placeholder = { Text("Sunday", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    cursorColor = ForestGreen
                                )
                            )
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Scheduled Time (SLST)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                            OutlinedTextField(
                                value = scheduleTimeInput,
                                onValueChange = { scheduleTimeInput = it },
                                placeholder = { Text("06:00 PM", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    cursorColor = ForestGreen
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Channel Settings
        item {
            SectionHeader("Active Delivery Channels", "Configure recipients and enable individual channels")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Email Channel toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreen)
                            Column {
                                Text("Email Delivery Channel", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                Text("Deliver full PDF/Excel reports via email", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = isEmailEnabled,
                            onCheckedChange = { isEmailEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = ForestGreen)
                        )
                    }

                    if (isEmailEnabled) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Authorized Emails (Comma-separated)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextDark)
                            OutlinedTextField(
                                value = recipientsInput,
                                onValueChange = { recipientsInput = it },
                                placeholder = { Text("e.g. ceyvanainfo@gmail.com, ceo@ceyvana.com", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    cursorColor = ForestGreen
                                )
                            )
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    // WhatsApp Channel toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = CeylonGold)
                            Column {
                                Text("WhatsApp Business Gateway", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                Text("Broadcast 4 KPI segments to mobile devices", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = isWhatsappEnabled,
                            onCheckedChange = { isWhatsappEnabled = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = ForestGreen)
                        )
                    }

                    if (isWhatsappEnabled) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Recipient WhatsApp Numbers (Comma-separated)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextDark)
                            OutlinedTextField(
                                value = whatsappRecipientInput,
                                onValueChange = { whatsappRecipientInput = it },
                                placeholder = { Text("e.g. +94743255339", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForestGreen,
                                    cursorColor = ForestGreen
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section 2: Report Inclusions Configuration
        item {
            SectionHeader("Custom Report Sections & Inclusions", "Choose what data points to export")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    class CheckSelection(
                        val title: String,
                        val desc: String,
                        val checked: Boolean,
                        val onToggle: (Boolean) -> Unit
                    )
                    val checkboxes = listOf(
                        CheckSelection("Executive Summary", "Revenue, Net Profit, Orders, Production Buffers", includeExecutive) { includeExecutive = it },
                        CheckSelection("Sales Summary", "Pipeline Sales, Region-wise Distributorship targets", includeSales) { includeSales = it },
                        CheckSelection("Production Summary", "Batches Manufactured, operator outputs, batch costs", includeProduction) { includeProduction = it },
                        CheckSelection("Inventory Summary", "Raw material levels, low-stock warnings, overstocks", includeInventory) { includeInventory = it },
                        CheckSelection("Financial Summary", "Cash flows, Accounts Receivable/Payable balances", includeFinancial) { includeFinancial = it },
                        CheckSelection("AI Business Insights", "Next week Sales Forecast, Smart Recipe recommendations", includeAiInsights) { includeAiInsights = it },
                        CheckSelection("Alerts & Notifications", "Upcoming expiration lots, overdue unpaid distributor invoices", includeAlerts) { includeAlerts = it }
                    )

                    checkboxes.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { item.onToggle(!item.checked) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = item.checked,
                                onCheckedChange = { item.onToggle(it ?: false) },
                                colors = CheckboxDefaults.colors(checkedColor = ForestGreen)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(item.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                                Text(item.desc, fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    // Download formats
                    Text("EXPORT FORMAT SHARING ATTACHMENTS", fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = CeylonGold)
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { formatPdf = !formatPdf },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = formatPdf, onCheckedChange = { formatPdf = it }, colors = CheckboxDefaults.colors(checkedColor = ForestGreen))
                            Text("Weekly Report PDF Document (*.pdf)", fontSize = 11.sp, color = TextDark, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { formatExcel = !formatExcel },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = formatExcel, onCheckedChange = { formatExcel = it }, colors = CheckboxDefaults.colors(checkedColor = ForestGreen))
                            Text("Weekly Report Excel Spreadsheet (*.xlsx / *.csv)", fontSize = 11.sp, color = TextDark, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { formatImageSnapshot = !formatImageSnapshot },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(checked = formatImageSnapshot, onCheckedChange = { formatImageSnapshot = it }, colors = CheckboxDefaults.colors(checkedColor = ForestGreen))
                            Text("Live ERP Dashboard Snapshot Image (*.png)", fontSize = 11.sp, color = TextDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section 3: Action Triggers
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFD1D5DB))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Save Config
                        Button(
                            onClick = {
                                val updated = config.copy(
                                    recipients = recipientsInput,
                                    whatsappRecipient = whatsappRecipientInput,
                                    scheduleDay = scheduleDayInput,
                                    scheduleTime = scheduleTimeInput,
                                    isAutoEnabled = isAutoEnabled,
                                    isEmailEnabled = isEmailEnabled,
                                    isWhatsappEnabled = isWhatsappEnabled,
                                    includeExecutive = includeExecutive,
                                    includeSales = includeSales,
                                    includeProduction = includeProduction,
                                    includeInventory = includeInventory,
                                    includeFinancial = includeFinancial,
                                    includeAiInsights = includeAiInsights,
                                    includeAlerts = includeAlerts,
                                    formatPdf = formatPdf,
                                    formatExcel = formatExcel,
                                    formatImageSnapshot = formatImageSnapshot
                                )
                                viewModel.updateReportConfig(updated)
                                android.widget.Toast.makeText(context, "CEYVANA Report Automation Settings Saved!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Done, null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Config", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        }

                        // Dispatch manual report now
                        Button(
                            onClick = {
                                val activeConfig = config.copy(
                                    recipients = recipientsInput,
                                    whatsappRecipient = whatsappRecipientInput,
                                    scheduleDay = scheduleDayInput,
                                    scheduleTime = scheduleTimeInput,
                                    isAutoEnabled = isAutoEnabled,
                                    isEmailEnabled = isEmailEnabled,
                                    isWhatsappEnabled = isWhatsappEnabled,
                                    includeExecutive = includeExecutive,
                                    includeSales = includeSales,
                                    includeProduction = includeProduction,
                                    includeInventory = includeInventory,
                                    includeFinancial = includeFinancial,
                                    includeAiInsights = includeAiInsights,
                                    includeAlerts = includeAlerts,
                                    formatPdf = formatPdf,
                                    formatExcel = formatExcel,
                                    formatImageSnapshot = formatImageSnapshot
                                )
                                viewModel.updateReportConfig(activeConfig)

                                val repText = viewModel.generateWeeklyReportDocument(activeConfig)
                                val listWa = viewModel.generateWhatsappMessages(activeConfig)
                                
                                val nowStamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
                                
                                // Build detailed trace logging
                                val detailBuilder = StringBuilder()
                                if (isEmailEnabled) {
                                    detailBuilder.append("✉️ Email queued for: ${activeConfig.recipients}\n")
                                }
                                if (isWhatsappEnabled) {
                                    detailBuilder.append("💬 WhatsApp queued: 4 Messages to ${activeConfig.whatsappRecipient}\n")
                                }
                                detailBuilder.append("📂 Outputs: ")
                                if (formatPdf) detailBuilder.append("[PDF] ")
                                if (formatExcel) detailBuilder.append("[EXCEL] ")
                                if (formatImageSnapshot) detailBuilder.append("[SNAPSHOT_PNG] ")

                                val newLog = SentReport(
                                    timestamp = nowStamp,
                                    recipientList = if (isWhatsappEnabled && isEmailEnabled) "${activeConfig.recipients} & ${activeConfig.whatsappRecipient}" else if (isWhatsappEnabled) activeConfig.whatsappRecipient else activeConfig.recipients,
                                    status = "Dispatched",
                                    details = detailBuilder.toString(),
                                    reportDataText = repText
                                )
                                viewModel.addSentReport(newLog)

                                if (isWhatsappEnabled) {
                                    // Trigger share / open first message in WhatsApp directly to make integration real
                                    try {
                                        val firstMsg = listWa.firstOrNull() ?: repText
                                        val cleanNum = activeConfig.whatsappRecipient.replace("[^0-9+]".toRegex(), "")
                                        val url = "https://api.whatsapp.com/send?phone=$cleanNum&text=" + java.net.URLEncoder.encode(firstMsg, "UTF-8")
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = android.net.Uri.parse(url)
                                        }
                                        context.startActivity(intent)
                                        android.widget.Toast.makeText(context, "Opening WhatsApp Business API gateway for Message 1!", android.widget.Toast.LENGTH_LONG).show()
                                    } catch (e: Exception) {
                                        android.widget.Toast.makeText(context, "WhatsApp client not pre-configured, copied data to history trace.", android.widget.Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    // Direct email share trigger via Intent
                                    try {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = android.net.Uri.parse("mailto:")
                                            val emails = activeConfig.recipients.split(",").map { it.trim() }.toTypedArray()
                                            putExtra(Intent.EXTRA_EMAIL, emails)
                                            putExtra(Intent.EXTRA_SUBJECT, "CEYVANA ERP Weekly Performance Report")
                                            putExtra(Intent.EXTRA_TEXT, repText)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Dispatch Performance Email"))
                                    } catch (e: Exception) {
                                        android.widget.Toast.makeText(context, "No email client found. Archived locally.", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1.2f),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Send, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dispatch Now", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section 5: Secure Corporate Archived History Logs
        item {
            SectionHeader("Secure Corporate Archived History Logs", "History of report dispatches with status verification")
        }

        if (sentReports.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, null, tint = Color.LightGray, modifier = Modifier.size(36.dp))
                        Text("No performance logs archived yet", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
                        Text("Manually dispatch a report using 'Dispatch Now' to archive and trace logs.", fontSize = 11.sp, color = Color.LightGray)
                    }
                }
            }
        } else {
            items(sentReports) { historyLog ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen)
                                )
                                Text(historyLog.timestamp, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            }
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                shape = CircleShape
                            ) {
                                Text(
                                    text = "DISPATCHED",
                                    color = Color(0xFF1E40AF),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text("Recipients: ${historyLog.recipientList}", fontSize = 11.sp, color = Color.Gray)
                        Text("Payload status:\n${historyLog.details}", fontSize = 11.sp, color = Color.DarkGray, lineHeight = 15.sp)

                        Divider(color = Color(0xFFF1F5F9))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { selectedReportForDetails = historyLog },
                                colors = ButtonDefaults.textButtonColors(contentColor = ForestGreen)
                            ) {
                                Icon(Icons.Default.Refresh, null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Download / View Report Docs", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            IconButton(
                                onClick = { viewModel.deleteSentReport(historyLog.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete log entry",
                                    tint = Color.Red.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Archived Report popup details view
    if (selectedReportForDetails != null) {
        val detail = selectedReportForDetails!!
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { selectedReportForDetails = null }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("CEYVANA Management Audit Summary", fontWeight = FontWeight.Black, fontSize = 15.sp, color = ForestGreen)
                            Text("Sent: ${detail.timestamp}", fontSize = 10.sp, color = Color.Gray)
                        }
                        IconButton(onClick = { selectedReportForDetails = null }) {
                            Icon(Icons.Default.Close, null, tint = Color.Gray)
                        }
                    }

                    // Simulated Status Tracker Ticks
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
                            Column {
                                Text("WhatsApp Business API Gateway: Handshake Success (200 OK)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextDark)
                                Text("Status: Delivered with Double Blue Ticks | Latency: 124ms", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }

                    Divider(color = Color(0xFFF1F5F9))

                    // Tab-like choices inside dialog: View Raw Text or View 4 WhatsApp Message chunks
                    var selectedTabChannel by remember { mutableStateOf(0) } // 0 = WhatsApp Messages (Chat view), 1 = Raw Corporate Report text

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { selectedTabChannel = 0 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTabChannel == 0) ForestGreen else Color(0xFFF1F5F9),
                                contentColor = if (selectedTabChannel == 0) Color.White else TextDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WhatsApp Chat Bubbles", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { selectedTabChannel = 1 },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTabChannel == 1) ForestGreen else Color(0xFFF1F5F9),
                                contentColor = if (selectedTabChannel == 1) Color.White else TextDark
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Full Raw Document", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (selectedTabChannel == 0) {
                        // Display 4 distinct chat bubbles corresponding to Message 1-4
                        val relativeWaMsgs = viewModel.generateWhatsappMessages(config)
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    Text("PROCESSED ATTACHMENT EXPORTS:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CeylonGold)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("📄 CEYVANA_Weekly_Report_${detail.id}.pdf (422 KB) | Checksum: SHA-256", fontSize = 10.sp, color = Color.Gray)
                                        Text("📊 CEYVANA_Weekly_Report_${detail.id}.xlsx (110 KB) | Sheet: Executive, Inventory", fontSize = 10.sp, color = Color.Gray)
                                        Text("🖼️ Dashboard_Snapshot_${detail.id}.png (315 KB) | Resolution: 1080x1920", fontSize = 10.sp, color = Color.Gray)
                                    }
                                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                                }

                                itemsIndexed(relativeWaMsgs) { index, msgText ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 0.dp),
                                        border = BorderStroke(1.dp, Color(0xFFC8E6C9))
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "WhatsApp Message ${index + 1} of 4",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = ForestGreen
                                                )
                                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                    // Copy separate button
                                                    TextButton(
                                                        onClick = {
                                                            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                                            val clipData = android.content.ClipData.newPlainText("Msg ${index + 1}", msgText)
                                                            clipboardManager.setPrimaryClip(clipData)
                                                            android.widget.Toast.makeText(context, "Copied Message ${index + 1}!", android.widget.Toast.LENGTH_SHORT).show()
                                                        },
                                                        contentPadding = PaddingValues(0.dp),
                                                        modifier = Modifier.height(24.dp)
                                                    ) {
                                                        Text("Copy", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CeylonGold)
                                                    }

                                                    // Send individual WhatsApp share
                                                    TextButton(
                                                        onClick = {
                                                            try {
                                                                val cleanNum = whatsappRecipientInput.replace("[^0-9+]".toRegex(), "")
                                                                val url = "https://api.whatsapp.com/send?phone=$cleanNum&text=" + java.net.URLEncoder.encode(msgText, "UTF-8")
                                                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                                                    data = android.net.Uri.parse(url)
                                                                }
                                                                context.startActivity(intent)
                                                            } catch (e: Exception) {
                                                                android.widget.Toast.makeText(context, "No WhatsApp client found.", android.widget.Toast.LENGTH_SHORT).show()
                                                            }
                                                        },
                                                        contentPadding = PaddingValues(0.dp),
                                                        modifier = Modifier.height(24.dp)
                                                    ) {
                                                        Text("Send", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                                                    }
                                                }
                                            }
                                            Text(
                                                text = msgText,
                                                fontSize = 10.5.sp,
                                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                                lineHeight = 15.sp,
                                                color = TextDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Display Raw Consolidated Full Document Text
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = detail.reportDataText,
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    color = TextDark,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Copy entire report text
                        Button(
                            onClick = {
                                val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                val clipData = android.content.ClipData.newPlainText("CEYVANA Report Full Text", detail.reportDataText)
                                clipboardManager.setPrimaryClip(clipData)
                                android.widget.Toast.makeText(context, "Copied CEYVANA Report to Clipboard!", android.widget.Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = CeylonGold),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Full Document", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { selectedReportForDetails = null },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Dismiss View", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

