package com.binar.finalproject.view.riwayat

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.binar.finalproject.R
import com.binar.finalproject.databinding.DateDialogRiwayatBinding
import com.binar.finalproject.databinding.FragmentRiwayatBinding
import com.binar.finalproject.databinding.SearchDialogRiwayatBinding
import com.binar.finalproject.local.DataStoreUser
import com.binar.finalproject.view.adapter.AdapterRiwayat
import com.binar.finalproject.viewmodel.TransactionHistoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.timessquare.CalendarPickerView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class RiwayatFragment : Fragment() {

    lateinit var binding : FragmentRiwayatBinding

    private lateinit var riwayatAdapter : AdapterRiwayat

    private lateinit var dataSotreUser : DataStoreUser

    private val transactionHistoryVM : TransactionHistoryViewModel by viewModels()

    private  var tokenUser : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRiwayatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE

        //untuk meload data
        binding.layoutLoadingData.visibility = View.VISIBLE

        dataSotreUser = DataStoreUser(requireContext().applicationContext)

        dataSotreUser.getToken.asLiveData().observe(viewLifecycleOwner){
            if (it != null){
                tokenUser = it
                setLayoutListData(tokenUser)

            }

        }


        binding.btnSearchRiwayat.setOnClickListener {
            showDialogSearch()
        }

        binding.btnFilterRiwayatAfterData.setOnClickListener {
            showDialogFilterDateRiwayat()
        }



        if (dataSotreUser.isAlreadyLogin()) {
            binding.layoutNonLogin.visibility = View.GONE
            binding.linearLayoutLogin.visibility = View.VISIBLE


        } else {
            binding.layoutNonLogin.visibility = View.VISIBLE
            binding.linearLayoutLogin.visibility = View.GONE



        }

        binding.btnMasukRiwayat.setOnClickListener {
            findNavController().navigate(R.id.action_riwayatFragment_to_loginFragment)
        }
    }

    private fun showDialogFilterDateRiwayat() {
        val dialogFilterDate = BottomSheetDialog(requireContext())

        dialogFilterDate.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogFilterDate.setContentView(R.layout.date_dialog_riwayat)
        val bindingDialogFilter = DateDialogRiwayatBinding.inflate(layoutInflater)
        dialogFilterDate.setContentView(bindingDialogFilter.root)

        setInitialDatePicker(bindingDialogFilter)
        bindingDialogFilter.btnClose.setOnClickListener {
            dialogFilterDate.dismiss()
        }

        dialogFilterDate.show()
        dialogFilterDate.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogFilterDate.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogFilterDate.window?.attributes?.windowAnimations = R.style.DialogAnimation;
        dialogFilterDate.window?.setGravity(Gravity.BOTTOM);
    }



    private fun setInitialDatePicker(bindingDialogFilter : DateDialogRiwayatBinding) {
        val startDate = Date()

        val nextYear = Calendar.getInstance()

        nextYear.add(Calendar.YEAR, 1)

        bindingDialogFilter.dateRiwayat.init(startDate,nextYear.time)
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(startDate)
    }

    private fun showDialogSearch() {
        val dialogSearchRiwayat = Dialog(requireContext())

        dialogSearchRiwayat.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSearchRiwayat.setContentView(R.layout.search_dialog_riwayat)
        val bindingSearchRiwayat = SearchDialogRiwayatBinding.inflate(layoutInflater)
        dialogSearchRiwayat.setContentView(bindingSearchRiwayat.root)


        //menutup dialog
        bindingSearchRiwayat.btnCloseRiwayat.setOnClickListener {
            dialogSearchRiwayat.dismiss()
        }

        dialogSearchRiwayat.show()
        dialogSearchRiwayat.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialogSearchRiwayat.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialogSearchRiwayat.window?.attributes?.windowAnimations = R.style.DialogAnimation;
        dialogSearchRiwayat.window?.setGravity(Gravity.BOTTOM);
    }

    private fun setLayoutListData(token: String) {
        riwayatAdapter = AdapterRiwayat(ArrayList())
        transactionHistoryVM.getHistoryTransaction(token)
        transactionHistoryVM.historyTransaction.observe(viewLifecycleOwner){ list ->
            if (list != null && list.isNotEmpty()){
                val listSorted = list.sortedByDescending { it.transaction.flights[0].transactionFlight.transactionId }

                binding.rvRiwayatAfterData.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = riwayatAdapter
                }

                riwayatAdapter.setList(listSorted)
                Log.i("DATA HISTORY", list.toString())

                binding.layoutLoadingData.visibility = View.GONE
                binding.rvRiwayatAfterData.visibility = View.VISIBLE

            }else{
                binding.rvRiwayatAfterData.visibility = View.GONE
                binding.layoutRiwayatEmpty.visibility = View.VISIBLE

                binding.layoutLoadingData.visibility = View.GONE

            }

        }
        riwayatAdapter.itemOnClickRiwayat = {
            if (it.transaction.flights[0].transactionFlight.transactionId.toString().isNotEmpty()){
                val bundleIdTransaction = Bundle().apply {
                    putInt("ID_TRANSACTION", it.transaction.flights[0].transactionFlight.transactionId)
                }
                binding.layoutLoadingData.visibility = View.GONE

                findNavController().navigate(R.id.action_riwayatFragment_to_detailRiwayatFragment, bundleIdTransaction)
                Log.i("BUNDLE_ID_TRANSACTION", it.transaction.flights[0]?.transactionFlight?.transactionId.toString())

            }
        }

    }


}