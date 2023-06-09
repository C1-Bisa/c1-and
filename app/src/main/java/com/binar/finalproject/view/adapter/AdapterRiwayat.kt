package com.binar.finalproject.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.binar.finalproject.R
import com.binar.finalproject.databinding.ItemRiwayatBinding
import com.binar.finalproject.model.gethistory.Data
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdapterRiwayat(private var listRiwayat: List<Data>):
    RecyclerView.Adapter<AdapterRiwayat.ViewHolder>() {

    var itemOnClickRiwayat : ((Data)-> Unit)? = null


    class ViewHolder(var binding : ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRiwayat.ViewHolder {
        val view = ItemRiwayatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterRiwayat.ViewHolder, position: Int) {
        val listPosition = listRiwayat[position]

        if(listPosition.transaction.transactionStatus == "Issued"){
            holder.binding.labelRiwayat.setBackgroundResource(R.drawable.background_issued_riwayat)
            holder.binding.txtLabelPembayaran.text = listPosition.transaction.transactionStatus
        }


        holder.binding.cardViewHistory.setOnClickListener {
            itemOnClickRiwayat?.invoke(listPosition)
        }


        holder.binding.namaKotaKiri.text = listPosition.transaction.flights[0]?.from
        holder.binding.namaKotaKanan.text = listPosition.transaction.flights[0]?.to

        holder.binding.codeBooking.text = listPosition.transaction.transactionCode



        holder.binding.jamKeberangkatanKiri.text = timeFormate(listPosition.transaction.flights[0]?.departureTime ?: "")
        holder.binding.jamKeberangkatanKanan.text = timeFormate(listPosition.transaction.flights[0]?.arrivalTime ?: "")

        holder.binding.tanggalKiri.text = setDate(listPosition.transaction.flights[0]?.departureDate ?: "")
        holder.binding.tanggalKanan.text = setDate(listPosition.transaction.flights[0]?.arrivalDate ?: "")

        holder.binding.classPlane.text = listPosition.transaction.flights[0]?.flightClass

        val durationFlight = reformatDuration(listPosition.transaction.flights[0]?.duration.toString())
        holder.binding.jarakTempuh.text = durationFlight

        val price = "IDR ${convertToCurrencyIDR(listPosition.price.total)}"
        holder.binding.tvPriceFlightRiwayat.text = price

        if (listPosition.transaction.flights.size > 1){
            holder.binding.apply {
                layoutFlightScheduleRiwayatReturn.visibility = View.VISIBLE

                holder.binding.namaKotaKiriReturnTrip.text = listPosition.transaction.flights[1]?.from
                holder.binding.namaKotaKananReturnTrip.text = listPosition.transaction.flights[1]?.to


                holder.binding.jamKeberangkatanKiriReturnTrip.text = timeFormate(listPosition.transaction.flights[1]?.departureTime ?: "")
                holder.binding.jamKeberangkatanKananReturnTrip.text = timeFormate(listPosition.transaction.flights[1]?.arrivalTime ?: "")

                holder.binding.tanggalKiriReturnTrip.text = setDate(listPosition.transaction.flights[1]?.departureDate ?: "")
                holder.binding.tanggalKananReturnTrip.text = setDate(listPosition.transaction.flights[1]?.arrivalDate ?: "")

                val durationFlightReturn = reformatDuration(listPosition.transaction.flights[1]?.duration.toString())
                holder.binding.jarakTempuhReturnTrip.text = durationFlightReturn


            }

        }

    }

    override fun getItemCount(): Int {
        return listRiwayat.size
    }

    fun setList(list: List<Data>){
        listRiwayat = list
        notifyDataSetChanged()
    }
    // price converter
    private fun convertToCurrencyIDR(price : Int) : String{
        val currencyFormatIDR = DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale("id", "ID")))
        return currencyFormatIDR.format(price)

    }
    // date converter
    private fun setDate(date : String) : String{
        val zonedDateTimeParse = ZonedDateTime.parse(date)
        val localDateTimeAsia = zonedDateTimeParse.withZoneSameInstant(ZoneId.of("Asia/Jakarta")).toLocalDateTime()
        val dateFormatterId = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id"))
        return localDateTimeAsia.format(dateFormatterId)
    }
    //time converter
    private fun timeFormate(time : String) : String{
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val parsedTimeLocal = LocalTime.parse(time, timeFormatter)
        return parsedTimeLocal.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun reformatDuration(duration: String): String {
        val text = duration.toCharArray()
            .filter { it != '9' }
            .toCharArray()
        return "${text[0]}h ${text[1]}m"
    }

}