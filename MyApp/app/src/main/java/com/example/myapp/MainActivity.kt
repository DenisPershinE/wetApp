package com.example.myapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.example.myapp.databinding.ActivityMainBinding
import com.example.myapp.retrofit.ProdactAPI
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    lateinit var newClass: ActivityMainBinding
    var histor = ArrayList<String>()
    var pref: SharedPreferences? = null
var newNumberOfBin: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(newClass.root)
        pref = getSharedPreferences("TABL", Context.MODE_PRIVATE)


        val interseptor = HttpLoggingInterceptor()
        interseptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interseptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://lookup.binlist.net").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val prodactAPI = retrofit.create(ProdactAPI::class.java)

        newClass.button.setOnClickListener {
            var numberOfBin = newClass.editTextNumber.text.toString()
            newNumberOfBin = numberOfBin
            saveData(newNumberOfBin)
            histor.add(numberOfBin)
            newClass.listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, histor)

            val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
                throwable.printStackTrace()
            }

            CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
                val checkNumberOfBin: Int
                val testCard = 45717360
val check = numberOfBin.isNullOrEmpty()
                if (check == true) {
                    checkNumberOfBin = testCard
                } else {
                    checkNumberOfBin = numberOfBin.toInt()
                }
                val product = prodactAPI.getInfoById(checkNumberOfBin)


                    runOnUiThread {

                            newClass.textView3.text = product.scheme
                            newClass.textView5.text = product.brand
                            newClass.textView10.text = product.number.length.toString()
                            newClass.textView11.text = product.number.luhn.toString()
                            newClass.textView13.text = product.type
                            newClass.textView29.text = product.prepaid.toString()
                            newClass.textView17.text = product.country.numeric.toString()
                            newClass.textView16.text = product.country.alpha2
                            newClass.textView18.text = product.country.name
                            newClass.textView19.text = product.country.emoji
                            newClass.textView20.text = product.country.currency
                            newClass.textView22.text = product.country.latitude.toString()
                            newClass.textView24.text = product.country.longitude.toString()
                            newClass.textView25.text = product.bank.name
                            newClass.textView30.text = product.bank.city
                            newClass.textView28.text = product.bank.phone
                            newClass.textView27.text = product.bank.url

                    }
                }

            }

        newNumberOfBin = pref?.getString("info", "0")!!
        newClass.textView31.text = newNumberOfBin

            }

    fun saveData(inf: String?) {
        val editor = pref?.edit()
        editor?.putString("info", inf)
        editor?.apply()
    }

        }


