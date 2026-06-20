package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        RawMaterial::class,
        Supplier::class,
        Product::class,
        Recipe::class,
        ProductionOrder::class,
        SalesOrder::class,
        Invoice::class,
        CashTransaction::class,
        Distributor::class,
        ReportConfig::class,
        SentReport::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ErpDatabase : RoomDatabase() {
    abstract fun rawMaterialDao(): RawMaterialDao
    abstract fun supplierDao(): SupplierDao
    abstract fun productDao(): ProductDao
    abstract fun recipeDao(): RecipeDao
    abstract fun productionOrderDao(): ProductionOrderDao
    abstract fun salesOrderDao(): SalesOrderDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun cashTransactionDao(): CashTransactionDao
    abstract fun distributorDao(): DistributorDao
    abstract fun reportConfigDao(): ReportConfigDao
    abstract fun sentReportDao(): SentReportDao

    companion object {
        @Volatile
        private var INSTANCE: ErpDatabase? = null

        fun getDatabase(context: Context): ErpDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ErpDatabase::class.java,
                    "ceyvana_erp_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
