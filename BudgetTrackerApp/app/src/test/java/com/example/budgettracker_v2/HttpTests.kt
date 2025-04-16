package com.example.budgettracker_v2

import com.example.budgettracker_v2.models.Balans
import com.example.budgettracker_v2.models.Categorie
import com.example.budgettracker_v2.models.Datum
import com.example.budgettracker_v2.models.Klant
import com.example.budgettracker_v2.models.Transaction
import com.example.budgettracker_v2.repositories.balans.apiBalans
import com.example.budgettracker_v2.repositories.categorie.apiCategory
import com.example.budgettracker_v2.repositories.datum.apiDatum
import com.example.budgettracker_v2.repositories.klant.apiKlant
import com.example.budgettracker_v2.repositories.transaction.apiTransaction
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class HttpTests {

    //transactie
    @Test
    fun getTransactieStatus200Test(){
        runBlocking {
            val response = apiTransaction.getTransacties()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun getTransactiesByUserStatus200Test(){
        runBlocking {
            val response = apiTransaction.geTransactiesByUser("2")
            assertEquals(200, response.code())
        }
    }

    @Test
    fun postTransactieStatus200Test(){
        runBlocking {
            val transaction = Transaction(tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
            val response = apiTransaction.postTransacties(transaction)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun updateTransactieStatus200Test(){
        runBlocking {
            val transaction = Transaction(tr_id = 8 ,tr_bedrag = 999.00, tr_mededeling = "testing post", tr_begunstigde = "testing post", tr_dt_id = 1, tr_bl_id = 1, tr_ct_id = 1)
            val response = apiTransaction.updateTransacties(transaction)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun deleteTransactieStatus200Test(){
        runBlocking {
            val response = apiTransaction.deleteTransacties("24")
            assertEquals(200, response.code())
        }
    }

    //categorie
    @Test
    fun CategorieStatus200Test(){
        runBlocking {
            val response = apiCategory.getCategorieen()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun CategorieenNotNullTest(){
        runBlocking {
            val response = apiCategory.getCategorieen()
            assertNotNull(response.data)
        }
    }

    @Test
    fun postCategorieTest(){
        runBlocking {
            val category = Categorie(ct_naam = "Sport")
            val response = apiCategory.postCategorieen(category)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun updateCategorieStatus200Test(){
        runBlocking {
            val category = Categorie(ct_id = 13, ct_naam = "Sportt")
            val response = apiCategory.updateCategorieen(category)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun deleteCategorieStatus200Test(){
        runBlocking {
            val response = apiCategory.deleteCategorieen("13")
            assertEquals(200, response.code())
        }
    }

    //klant
    @Test
    fun getKlantStatus200Test(){
        runBlocking {
            val response = apiKlant.getKlanten()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun postKlantStatus200Test(){
        runBlocking {
            val klant = Klant(kl_naam = "testNaam", kl_voornaam = "testVoornaam", kl_email = "testEmail", kl_wachtwoord = "testWachtwoord", kl_isAdmin = false)
            val response = apiKlant.postKlanten(klant)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun updateKlantStatus200Test(){
        runBlocking {
            val klant = Klant(kl_naam = "updated", kl_voornaam = "updated", kl_email = "testEmail", kl_wachtwoord = "testWachtwoord", kl_isAdmin = false)
            val response = apiKlant.postKlanten(klant)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun deleteKlantStatus200Test(){
        runBlocking {
            val response = apiKlant.deleteKlanten("12")
            assertEquals(200, response.code())
        }
    }

    //balans
    @Test
    fun getBalansStatus200Test(){
        runBlocking {
            val response = apiBalans.getBalansen()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun postBalansStatus200Test(){
        runBlocking {
            val balans = Balans(bl_kl_id = 3)
            val response = apiBalans.postBalans(balans)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun deleteBalansStatus200Test(){
        runBlocking {
            val response = apiBalans.deleteBalansen("10")
            assertEquals(200, response.code())
        }
    }

    //datum
    @Test
    fun getDatumStatus200Test(){
        runBlocking {
            val response = apiDatum.getDatums()
            assertEquals("200", response.status)
        }
    }

    @Test
    fun postDatumStatus200Test(){
        runBlocking {
            val datum = Datum(dt_datum = "2025-04-16", dt_jaar = 2025, dt_maand = "April", dt_maand_num = 4, dt_dag = 16)
            val response = apiDatum.postDatums(datum)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun updateDatumStatus200Test(){
        runBlocking {
            val datum = Datum(dt_id = 520 ,dt_datum = "2025-04-10", dt_jaar = 2025, dt_maand = "April", dt_maand_num = 4, dt_dag = 10)
            val response = apiDatum.updateDatums(datum)
            assertEquals(200, response.code())
        }
    }

    @Test
    fun deleteDatumStatus200Test(){
        runBlocking {
            val response = apiDatum.deleteDatums("520")
            assertEquals(200, response.code())
        }
    }
}