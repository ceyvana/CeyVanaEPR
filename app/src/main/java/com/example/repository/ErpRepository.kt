package com.example.repository

import com.example.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ErpRepository(private val db: ErpDatabase) {

    val rawMaterials: Flow<List<RawMaterial>> = db.rawMaterialDao().getAll()
    val suppliers: Flow<List<Supplier>> = db.supplierDao().getAll()
    val products: Flow<List<Product>> = db.productDao().getAll()
    val recipes: Flow<List<Recipe>> = db.recipeDao().getAll()
    val productionOrders: Flow<List<ProductionOrder>> = db.productionOrderDao().getAll()
    val salesOrders: Flow<List<SalesOrder>> = db.salesOrderDao().getAll()
    val invoices: Flow<List<Invoice>> = db.invoiceDao().getAll()
    val cashTransactions: Flow<List<CashTransaction>> = db.cashTransactionDao().getAll()
    val distributors: Flow<List<Distributor>> = db.distributorDao().getAll()
    val reportConfig: Flow<ReportConfig?> = db.reportConfigDao().getConfigFlow()
    val sentReports: Flow<List<SentReport>> = db.sentReportDao().getAllFlow()

    suspend fun insertRawMaterial(item: RawMaterial) = db.rawMaterialDao().insert(item)
    suspend fun deleteRawMaterial(id: Int) = db.rawMaterialDao().deleteById(id)

    suspend fun insertSupplier(item: Supplier) = db.supplierDao().insert(item)
    suspend fun deleteSupplier(id: Int) = db.supplierDao().deleteById(id)

    suspend fun insertProduct(item: Product) = db.productDao().insert(item)
    suspend fun deleteProduct(sku: String) = db.productDao().deleteBySku(sku)

    suspend fun insertRecipe(item: Recipe) = db.recipeDao().insert(item)
    suspend fun deleteRecipeForProduct(sku: String) = db.recipeDao().deleteForProduct(sku)
    fun getRecipesForProduct(sku: String): Flow<List<Recipe>> = db.recipeDao().getForProduct(sku)

    suspend fun insertProductionOrder(item: ProductionOrder) = db.productionOrderDao().insert(item)
    suspend fun deleteProductionOrder(id: Int) = db.productionOrderDao().deleteById(id)

    suspend fun insertSalesOrder(item: SalesOrder) = db.salesOrderDao().insert(item)
    suspend fun deleteSalesOrder(id: Int) = db.salesOrderDao().deleteById(id)

    suspend fun insertInvoice(item: Invoice) = db.invoiceDao().insert(item)
    suspend fun deleteInvoiceByNumber(number: String) = db.invoiceDao().deleteByInvoiceNumber(number)
    suspend fun deleteInvoiceById(id: Int) = db.invoiceDao().deleteById(id)

    suspend fun insertCashTransaction(item: CashTransaction) = db.cashTransactionDao().insert(item)

    suspend fun insertDistributor(item: Distributor) = db.distributorDao().insert(item)
    suspend fun deleteDistributorByName(name: String) = db.distributorDao().deleteByName(name)
    suspend fun deleteSalesOrderByCustomerName(customerName: String) = db.salesOrderDao().deleteByCustomerName(customerName)

    suspend fun getReportConfigDirect(): ReportConfig? = db.reportConfigDao().getConfigDirect()
    suspend fun saveReportConfig(config: ReportConfig) = db.reportConfigDao().insert(config)
    suspend fun insertSentReport(report: SentReport) = db.sentReportDao().insert(report)
    suspend fun deleteSentReport(id: Int) = db.sentReportDao().deleteById(id)

    suspend fun repopulateIfEmpty() {
        // Only run if products are empty to verify first-run initialization
        val isDbEmpty = db.productDao().getAll().first().isEmpty()
        if (isDbEmpty) {
            // 1. Insert Products
            val defaultProducts = listOf(
                Product("P-001", "Premium Chili Powder", "Single Spices", 250, 450.0),
                Product("P-002", "Turmeric Powder", "Single Spices", 100, 350.0),
                Product("P-003", "Cinnamon Powder", "Single Spices", 100, 620.0),
                Product("P-003-B", "Black Pepper Powder", "Single Spices", 100, 480.0),
                Product("P-004", "Traditional Curry Powder", "Blended Products", 250, 550.0),
                Product("P-005", "Roasted Curry Powder", "Blended Products", 250, 580.0),
                Product("P-006", "Meat Curry Powder", "Blended Products", 250, 600.0)
            )
            for (p in defaultProducts) {
                db.productDao().insert(p)
            }

            // 2. Insert Raw Materials
            val defaultRaw = listOf(
                RawMaterial(name = "Chili", stockInKg = 1200.0, unitCost = 600.0, supplierName = "Matale Organic Growers", warehouseLocation = "Sec A-1", batchNo = "RM-CH-01"),
                RawMaterial(name = "Turmeric", stockInKg = 800.0, unitCost = 750.0, supplierName = "Matale Organic Growers", warehouseLocation = "Sec A-2", batchNo = "RM-TU-03"),
                RawMaterial(name = "Coriander", stockInKg = 1500.0, unitCost = 450.0, supplierName = "Kandy Trading Corp", warehouseLocation = "Sec B-1", batchNo = "RM-CO-02"),
                RawMaterial(name = "Cumin", stockInKg = 600.0, unitCost = 920.0, supplierName = "Kandy Trading Corp", warehouseLocation = "Sec B-2", batchNo = "RM-CU-05"),
                RawMaterial(name = "Fennel", stockInKg = 500.0, unitCost = 650.0, supplierName = "Kandy Trading Corp", warehouseLocation = "Sec B-3", batchNo = "RM-FE-04"),
                RawMaterial(name = "Pepper", stockInKg = 900.0, unitCost = 1100.0, supplierName = "Sabaragamuwa Spice Farms", warehouseLocation = "Sec C-1", batchNo = "RM-PE-09"),
                RawMaterial(name = "Cinnamon", stockInKg = 450.0, unitCost = 1800.0, supplierName = "Sabaragamuwa Spice Farms", warehouseLocation = "Sec C-2", batchNo = "RM-CI-10"),
                RawMaterial(name = "Cloves", stockInKg = 250.0, unitCost = 2200.0, supplierName = "Kandy Trading Corp", warehouseLocation = "Sec D-1", batchNo = "RM-CL-08")
            )
            for (r in defaultRaw) {
                db.rawMaterialDao().insert(r)
            }

            // 3. Insert Suppliers
            val defaultSuppliers = listOf(
                Supplier(name = "Matale Organic Growers", address = "Eco Hill Road, Matale", phone = "066-2244556", email = "info@mataleorganic.lk", brn = "PV-45892", paymentTerms = "Net 30", outstandingBalance = 450000.0),
                Supplier(name = "Kandy Trading Corp", address = "No 45, Peradeniya Rd, Kandy", phone = "081-4993322", email = "orders@kandytrading.lk", brn = "PV-31902", paymentTerms = "Net 15", outstandingBalance = 220000.0),
                Supplier(name = "Sabaragamuwa Spice Farms", address = "Weralupe Junction, Ratnapura", phone = "045-2288112", email = "supply@sabaraspices.lk", brn = "PV-88392", paymentTerms = "Net 30", outstandingBalance = 0.0)
            )
            for (s in defaultSuppliers) {
                db.supplierDao().insert(s)
            }

            // 4. Insert Recipes
            // Formula Traditional Curry Powder: Coriander 40%, Cumin 10%, Fennel 10%, Pepper 15%, Turmeric 10%, Curry Leaves 10%, Cinnamon 3%, Cloves 2%
            val traditionalRecipe = listOf(
                Recipe(productSku = "P-004", ingredientName = "Coriander", percentage = 40.0, unitCost = 450.0),
                Recipe(productSku = "P-004", ingredientName = "Cumin", percentage = 10.0, unitCost = 920.0),
                Recipe(productSku = "P-004", ingredientName = "Fennel", percentage = 10.0, unitCost = 650.0),
                Recipe(productSku = "P-004", ingredientName = "Pepper", percentage = 15.0, unitCost = 1100.0),
                Recipe(productSku = "P-004", ingredientName = "Turmeric", percentage = 10.0, unitCost = 750.0),
                Recipe(productSku = "P-004", ingredientName = "Cinnamon", percentage = 10.0, unitCost = 1800.0),
                Recipe(productSku = "P-004", ingredientName = "Cloves", percentage = 5.0, unitCost = 2200.0)
            )
            for (rec in traditionalRecipe) {
                db.recipeDao().insert(rec)
            }

            // Meat Curry Powder recipe (P-006): Coriander 35%, Chili 25%, Cumin 10%, Fennel 10%, Pepper 10%, Turmeric 5%, Cardamom 2%, Cloves 3%
            val meatRecipe = listOf(
                Recipe(productSku = "P-006", ingredientName = "Coriander", percentage = 35.0, unitCost = 450.0),
                Recipe(productSku = "P-006", ingredientName = "Chili", percentage = 25.0, unitCost = 600.0),
                Recipe(productSku = "P-006", ingredientName = "Cumin", percentage = 10.0, unitCost = 920.0),
                Recipe(productSku = "P-006", ingredientName = "Fennel", percentage = 10.0, unitCost = 650.0),
                Recipe(productSku = "P-006", ingredientName = "Pepper", percentage = 10.0, unitCost = 1100.0),
                Recipe(productSku = "P-006", ingredientName = "Turmeric", percentage = 5.0, unitCost = 750.0),
                Recipe(productSku = "P-006", ingredientName = "Cloves", percentage = 5.0, unitCost = 2200.0)
            )
            for (rec in meatRecipe) {
                db.recipeDao().insert(rec)
            }

            // 5. Insert Production Orders
            val orders = listOf(
                ProductionOrder(batchNumber = "B-2026-001", productSku = "P-004", quantity = 500, status = "Completed", date = "2026-06-15", operator = "Ranasinghe Banda", actualCost = 115000.0),
                ProductionOrder(batchNumber = "B-2026-002", productSku = "P-001", quantity = 800, status = "Completed", date = "2026-06-18", operator = "S. Perera", actualCost = 180000.0),
                ProductionOrder(batchNumber = "B-2026-003", productSku = "P-006", quantity = 400, status = "Pending", date = "2026-06-21", operator = "Ranasinghe Banda", actualCost = 98000.0)
            )
            for (o in orders) {
                db.productionOrderDao().insert(o)
            }

            // 6. Deposit Cash Transactions
            val transactions = listOf(
                CashTransaction(type = "CASH_IN", description = "Invoice #INV-26-001 Colombo Dist", amount = 450000.0, date = "2026-06-16", category = "SALES"),
                CashTransaction(type = "CASH_IN", description = "Invoice #INV-26-002 Gampaha Agent", amount = 380000.0, date = "2026-06-18", category = "SALES"),
                CashTransaction(type = "CASH_OUT", description = "Material purchase: Matale Growers", amount = 150000.0, date = "2026-06-12", category = "PURCHASE"),
                CashTransaction(type = "CASH_OUT", description = "Electricity June 2026", amount = 45000.0, date = "2026-06-14", category = "ELECTRICITY"),
                CashTransaction(type = "CASH_OUT", description = "Wages for production floor", amount = 120000.0, date = "2026-06-15", category = "LABOR"),
                CashTransaction(type = "CASH_OUT", description = "Factory diesel fuel", amount = 30000.0, date = "2026-06-17", category = "TRANSPORT")
            )
            for (t in transactions) {
                db.cashTransactionDao().insert(t)
            }

            // 7. Insert Distributors
            val defaultDists = listOf(
                Distributor(name = "Central Province Dealers", area = "Kandy, Matale, Nuwara Eliya", salesTarget = 2000000.0, currentAchievements = 1750000.0, phone = "0755566778")
            )
            for (d in defaultDists) {
                db.distributorDao().insert(d)
            }

            // 8. Default Sales Order & Invoices
            val sales = emptyList<SalesOrder>()
            for (s in sales) {
                db.salesOrderDao().insert(s)
            }

            val defaultInvs = emptyList<Invoice>()
            for (inv in defaultInvs) {
                db.invoiceDao().insert(inv)
            }

            // 9. Default Report Config
            db.reportConfigDao().insert(ReportConfig())
        }
    }
}
