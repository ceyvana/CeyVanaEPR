package com.example.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RawMaterialDao {
    @Query("SELECT * FROM raw_materials ORDER BY name ASC")
    fun getAll(): Flow<List<RawMaterial>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(material: RawMaterial)

    @Query("DELETE FROM raw_materials WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface SupplierDao {
    @Query("SELECT * FROM suppliers ORDER BY name ASC")
    fun getAll(): Flow<List<Supplier>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supplier: Supplier)

    @Query("DELETE FROM suppliers WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Product)

    @Query("DELETE FROM products WHERE sku = :sku")
    suspend fun deleteBySku(sku: String)
}

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAll(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE productSku = :productSku")
    fun getForProduct(productSku: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Query("DELETE FROM recipes WHERE productSku = :productSku")
    suspend fun deleteForProduct(productSku: String)
}

@Dao
interface ProductionOrderDao {
    @Query("SELECT * FROM production_orders ORDER BY id DESC")
    fun getAll(): Flow<List<ProductionOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: ProductionOrder)

    @Query("DELETE FROM production_orders WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface SalesOrderDao {
    @Query("SELECT * FROM sales_orders ORDER BY id DESC")
    fun getAll(): Flow<List<SalesOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: SalesOrder)

    @Query("DELETE FROM sales_orders WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM sales_orders WHERE customerName = :customerName")
    suspend fun deleteByCustomerName(customerName: String)
}

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY id DESC")
    fun getAll(): Flow<List<Invoice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(invoice: Invoice)

    @Query("DELETE FROM invoices WHERE invoiceNumber = :invoiceNumber")
    suspend fun deleteByInvoiceNumber(invoiceNumber: String)

    @Query("DELETE FROM invoices WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface CashTransactionDao {
    @Query("SELECT * FROM cash_transactions ORDER BY id DESC")
    fun getAll(): Flow<List<CashTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: CashTransaction)
}

@Dao
interface DistributorDao {
    @Query("SELECT * FROM distributors ORDER BY name ASC")
    fun getAll(): Flow<List<Distributor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(distributor: Distributor)

    @Query("DELETE FROM distributors WHERE name = :name")
    suspend fun deleteByName(name: String)
}

@Dao
interface ReportConfigDao {
    @Query("SELECT * FROM report_configurations WHERE id = 1 LIMIT 1")
    fun getConfigFlow(): Flow<ReportConfig?>

    @Query("SELECT * FROM report_configurations WHERE id = 1 LIMIT 1")
    suspend fun getConfigDirect(): ReportConfig?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: ReportConfig)
}

@Dao
interface SentReportDao {
    @Query("SELECT * FROM sent_reports_history ORDER BY id DESC")
    fun getAllFlow(): Flow<List<SentReport>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: SentReport)

    @Query("DELETE FROM sent_reports_history WHERE id = :id")
    suspend fun deleteById(id: Int)
}

