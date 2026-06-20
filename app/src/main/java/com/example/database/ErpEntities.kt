package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "raw_materials")
data class RawMaterial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val stockInKg: Double,
    val unitCost: Double,
    val supplierName: String,
    val warehouseLocation: String = "Main Warehouse",
    val expiryDate: String = "2027-12-31",
    val batchNo: String = "B-001"
)

@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val phone: String,
    val email: String,
    val brn: String = "PV-123456",
    val paymentTerms: String = "Net 30",
    val outstandingBalance: Double = 0.0
)

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val sku: String,
    val name: String,
    val category: String, // "Single Spices" or "Blended Products"
    val weightGrams: Int,
    val sellingPrice: Double,
    val imageRes: String = "ic_launcher_foreground"
)

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productSku: String,
    val ingredientName: String,
    val percentage: Double, // percentage e.g. 40.0 for 40%
    val unitCost: Double // Raw material unit cost per kg
)

@Entity(tableName = "production_orders")
data class ProductionOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchNumber: String,
    val productSku: String,
    val quantity: Int, // Number of packs
    val status: String, // "Pending", "Completed"
    val date: String,
    val operator: String,
    val actualCost: Double
)

@Entity(tableName = "sales_orders")
data class SalesOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val totalAmount: Double,
    val date: String,
    val status: String, // "Quotations", "Sales Orders", "Invoices", "Delivery Notes", "Completed"
    val paymentStatus: String, // "Paid", "Pending", "Unpaid"
    val currentBalance: Double = 0.0
)

@Entity(tableName = "invoices")
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val invoiceNumber: String,
    val salesOrderId: Int,
    val customerName: String,
    val totalAmount: Double,
    val taxAmount: Double, // VAT/NBT
    val date: String,
    val paymentStatus: String // "Paid", "Pending"
)

@Entity(tableName = "cash_transactions")
data class CashTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "CASH_IN", "CASH_OUT"
    val description: String,
    val amount: Double,
    val date: String,
    val category: String // "SALES", "PURCHASE", "ELECTRICITY", "LABOR", "TRANSPORT", "OVERHEAD", "INCOME", "EXPENSE"
)

@Entity(tableName = "distributors")
data class Distributor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val area: String,
    val salesTarget: Double,
    val currentAchievements: Double,
    val phone: String
)

@Entity(tableName = "report_configurations")
data class ReportConfig(
    @PrimaryKey val id: Int = 1,
    val recipients: String = "ceyvanainfo@gmail.com",
    val isAutoEnabled: Boolean = true,
    val scheduleDay: String = "Sunday",
    val scheduleTime: String = "06:00 PM",
    val includeExecutive: Boolean = true,
    val includeSales: Boolean = true,
    val includeProduction: Boolean = true,
    val includeInventory: Boolean = true,
    val includeFinancial: Boolean = true,
    val includeAiInsights: Boolean = true,
    val includeAlerts: Boolean = true,
    val formatPdf: Boolean = true,
    val formatExcel: Boolean = true,
    val whatsappRecipient: String = "+94 743 255 339",
    val isWhatsappEnabled: Boolean = true,
    val isEmailEnabled: Boolean = true,
    val formatImageSnapshot: Boolean = true
)

@Entity(tableName = "sent_reports_history")
data class SentReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: String,
    val recipientList: String,
    val status: String,
    val details: String,
    val reportDataText: String
)

