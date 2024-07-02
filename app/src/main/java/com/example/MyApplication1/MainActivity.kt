package com.example.MyApplication1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication1.databinding.ActivityMainBinding
import java.util.Locale
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data: ArrayList<Modal>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data=ArrayList<Modal>()
        apiData
        rvAdapter=RvAdapter(this, this.data)
        binding.RecyclerViewID.layoutManager=LinearLayoutManager(this)
        binding.RecyclerViewID.adapter=rvAdapter

//        binding.search.addTextChangedListener(object:TextMatcher)
//        binding.searchBox.isVisible=false
        binding.searchBox.clearFocus()
        binding.searchBox.setOnQueryTextListener(object:SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchBox.clearFocus()
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
//                searchBox.clear


                val filteredData=ArrayList<Modal>()
                for(eachItem in data){
                    if (p0 != null) {
                        if(eachItem.name.lowercase(Locale.getDefault()).contains(p0.lowercase(Locale.getDefault()))){
                            filteredData.add(eachItem)
                        }
                    }
                }
                if(filteredData.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No Data Found!!!", Toast.LENGTH_SHORT).show()
                }else{

                }
                rvAdapter.setFilteredData(filteredData)

                return true
            }


        } )
    }
     private val apiData:Unit
    get(){
        val queue = Volley.newRequestQueue(this)
        val url="https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"
        val jsonObjectRequest:JsonObjectRequest= @SuppressLint("NotifyDataSetChanged")
        object :JsonObjectRequest(Method.GET,url,null,Response.Listener
        {

            response ->
            binding.progressBar.isVisible=false
            try{
                val dataArray=response.getJSONArray("data")
                val k=dataArray.length()
                for (i in 0 until k){
                    val dataObject =dataArray.getJSONObject(i)
                    val symbol=dataObject.getString("symbol")
                    val  name=dataObject.getString("name")
                    val quote=dataObject.getJSONObject("quote")
                    val USD=quote.getJSONObject("USD")
                    val price=String.format("%.3f",USD.getDouble("price"))
                    data.add(Modal(name,symbol, "$$price"))

                }
                rvAdapter.notifyDataSetChanged()

            }
            catch (e:Exception) {
                Toast.makeText(this, "Error Occurred in fetching data!!!", Toast.LENGTH_LONG).show()
            }
        },
            Response.ErrorListener {Toast.makeText(this,"Error Occurred!!! Make sure you are connected to Internet.",Toast.LENGTH_LONG).show() }
        )
        {
            override fun getHeaders(): Map<String, String> {
                val headers=HashMap<String,String>()
                headers["X-CMC_PRO_API_KEY"]="7d47fab8-8d11-4eeb-85eb-e0308759c460"
                return headers
            }
        }
        queue.add(jsonObjectRequest)
    }
}