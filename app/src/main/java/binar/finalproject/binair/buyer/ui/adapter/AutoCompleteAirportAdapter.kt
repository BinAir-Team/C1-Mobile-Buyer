package binar.finalproject.binair.buyer.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.response.CityAirport
import binar.finalproject.binair.buyer.databinding.ItemAutocompleteBinding


class AutoCompleteAirportAdapter(context: Context, private val airportList: ArrayList<CityAirport?>) :
    ArrayAdapter<CityAirport?>(context, R.layout.item_autocomplete, airportList) {
    private var filteredAirport : ArrayList<CityAirport> = ArrayList()

    fun getDataAirport(post : Int) : CityAirport = filteredAirport[post]

    override fun getCount(): Int = filteredAirport.size

    override fun getFilter(): Filter {
        return AirportFilter(this, airportList)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val airport = filteredAirport[position]
        val view = LayoutInflater.from(context).inflate(R.layout.item_autocomplete, parent, false)
        val binding = ItemAutocompleteBinding.bind(view)
        binding.tvKota.text = airport.city
        binding.tvKodeAirport.text = airport.code
        binding.tvAirport.text = airport.airport
        return view
    }

    class AirportFilter(
        private var adapter: AutoCompleteAirportAdapter,
        private var airportList: ArrayList<CityAirport?>
    ) : Filter() {
        private var filteredAirport: ArrayList<CityAirport> = ArrayList()

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            filteredAirport.clear()
            val results = FilterResults()
            if (constraint != null) {
                for (airport in airportList) {
                    if (airport?.city?.lowercase()?.contains(constraint.toString().lowercase().trim())!!) {
                        filteredAirport.add(airport)
                    }
                }
                results.values = filteredAirport
                results.count = filteredAirport.size
            }else{
                filteredAirport.addAll(airportList as ArrayList<CityAirport>)
            }
            results.values = filteredAirport
            results.count = filteredAirport.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            if (results != null && results.count > 0) {
//                adapter.filteredAirport = results.values as List<CityAirport>
//                adapter.notifyDataSetChanged()
//            } else {
//                adapter.notifyDataSetInvalidated()
//            }
            adapter.filteredAirport.clear()
            adapter.filteredAirport.addAll(results?.values as ArrayList<CityAirport>)
            adapter.notifyDataSetChanged()
        }
    }
}