package com.example.foodbar.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.foodbar.controller.RetrofitClient
import com.example.foodbar.databinding.FragmentMyReservationBinding
import com.example.foodbar.model.Reservation.Reservation
import com.example.foodbar.utils.HideKeyboard
import com.example.foodbar.utils.SharedPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.EOFException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MyReservationFragment : Fragment() {
    // Gunakan lateinit untuk inisialisasi binding karena kita akan menginisialisasinya di onCreateView.
    // lateinit berarti "late initialization".
    private var _binding: FragmentMyReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan DataBindingUtil untuk inflating layout dan memperoleh instance dari Binding class.
        _binding = FragmentMyReservationBinding.inflate(inflater, container, false)

        // Set onClickListener kepada editTextReservation dari binding
        binding.editTextReservationDate.setOnClickListener {
            showDateTimeDialog(binding.editTextReservationDate)
        }

        binding.buttonSubmit.setOnClickListener {
            HideKeyboard.hideKeyboard(requireActivity())
            // Implementasi kode untuk submit reservation
            val tableType = binding.spinnerTableType.selectedItem.toString()
            val deskripsi = binding.editTextDescription.text.toString()
            val reservationDate = binding.editTextReservationDate.text.toString()

            // convert reservationDate to Date
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val date = formatter.parse(reservationDate)


            createReservation(tableType, date, deskripsi)
        }
        return binding.root
    }



    private fun showDateTimeDialog(editTextReservation: EditText) {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(context, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // set date time to editTextReservation
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                editTextReservation.setText(dateFormat.format(calendar.time))

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        DatePickerDialog(requireContext(), dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun createReservation(tableType: String, reservationDate: Date, deskripsi: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = SharedPreferences.getToken(requireContext())
            val reservation = Reservation(
                tableType = tableType,
                reservationDate = reservationDate,
                deskripsi = deskripsi
            )

            try {
                val response = RetrofitClient.reservationController.createReservation("Bearer $token", reservation)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Snackbar.make(binding.root, "Reservation created successfully", Snackbar.LENGTH_LONG).show()
                        resetview()
                    } else {
                        Snackbar.make(binding.root, "Failed to create reservation", Snackbar.LENGTH_LONG).show()
                    }
                }
            } catch (e: EOFException) {
                withContext(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Failed to create reservation", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun resetview() {
        binding.spinnerTableType.setSelection(0)
        binding.editTextReservationDate.setText("")
        binding.editTextDescription.setText("")
    }


    // Tambahkan lifecycle callback onDestroyView untuk membersihkan reference ke binding.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
