package com.meridiane.lection3.presentation.ui.catalog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.CalendarDialogBinding
import com.meridiane.lection3.databinding.FragmentAddOrderBinding
import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.presentation.ui.MapActivity
import com.meridiane.lection3.presentation.viewModel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AddOrderFragment : Fragment() {

    private lateinit var binding: FragmentAddOrderBinding
    private var launcher: ActivityResultLauncher<Intent>? = null

    private val viewModel: ProductDetailsViewModel by activityViewModels()

    private var prise = 0

    private var time = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val text = result.data?.getStringExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY)
                    binding.textHome.setText(text)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(Constants.SEND_ID_ADD_ORDER)!!

        viewModel.getProductDetails(id)

        lifecycleScope.launch {
            viewModel.productsState.collectLatest { product ->

                if (product.title != null) {

                    viewModel.idProduct = product.id!!

                    with(binding) {
                        textOrderTitle.text =
                            if (product.title.length > 30) product.title.substring(
                                0,
                                30
                            ) else product.title
                        textOrderDepartament.text = product.department
                        imageOrderIcon.load(product.preview)
                        prise = product.price!!.toInt()
                        binding.materialButtonOrder.setText(
                            getString(
                                R.string.sum_text_order,
                                product.price.toInt()
                            )
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.successState.collectLatest { succes ->
                if(succes != ""){
                    Toast.makeText(requireContext(), succes,Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.myOrdersFragment)
                }

            }
        }

        lifecycleScope.launch{
            viewModel.errorState.collectLatest {error ->
                if(error != "") {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.stateFlow.collect { value ->
                binding.materialButtonOrder.setText(
                    getString(R.string.sum_text_order, prise * value)
                )
                binding.countOrder.text = viewModel.stateFlow.value.toString()
            }
        }

        binding.increment.setOnClickListener {
            viewModel.stateFlow.value++
        }

        binding.decrement.setOnClickListener {
            if (viewModel.stateFlow.value != 0) viewModel.stateFlow.value--
        }

        binding.textHome.setOnClickListener {
            sendMessageHelloActivity(binding.textHome.text.toString())
        }

        binding.textOrderDate.setOnClickListener {
            alertDialog()
        }

        binding.materialButtonOrder.setOnClickListener {
            check()
        }

        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun sendMessageHelloActivity(adress: String) {
        val intent: Intent = Intent(requireContext(), MapActivity::class.java)
            .putExtra(Constants.KEY_SEND_ADD_MAP_ACTIVITY, adress)
        launcher?.launch(intent)

    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(context)
        val rootDialogElement = CalendarDialogBinding.inflate(activity?.layoutInflater!!)

        val dialog = builder.setView(rootDialogElement.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        rootDialogElement.textCancelCalendar.setOnClickListener {
            dialog.dismiss()
        }

        rootDialogElement.textOk.setOnClickListener {
            if (rootDialogElement.textPickerDate.text.toString() != "")
                binding.textOrderDate.setText(rootDialogElement.textPickerDate.text.toString())
            dialog.dismiss()
        }

        rootDialogElement.calendarView.setOnDateChangeListener { _, year, month, day ->

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            val sdf = SimpleDateFormat("hh:mm:ss")
            val currentDate = sdf.format(Date())

            time = "$year-$month-${day}T${currentDate}.512Z"

            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val monthName =
                if (SimpleDateFormat(getString(R.string.simple_date_formatter)).format(calendar.time)
                        .toString().length > 2
                )
                    SimpleDateFormat(getString(R.string.simple_date_formatter)).format(calendar.time)
                        .toString().substring(0, 3)
                else SimpleDateFormat(getString(R.string.simple_date_formatter)).format(calendar.time)
                    .toString()

            rootDialogElement.textPickerDate.text = getString(
                R.string.calendar_text,
                week(dayOfWeek),
                monthName.replaceFirstChar(Char::titlecase),
                day.toString()
            )
        }
        dialog.show()
    }

    private fun week(week: Int): String {
        return when (week) {
            1 -> getString(R.string.sunday)
            2 -> getString(R.string.monday)
            3 -> getString(R.string.tuesday)
            4 -> getString(R.string.wednesday)
            5 -> getString(R.string.thursday)
            6 -> getString(R.string.friday)
            else -> getString(R.string.saturday)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun check() {

        when {
            viewModel.stateFlow.value != 0 &&
                    binding.textHome.text!!.contains(",") &&
                    binding.textRoom.text!!.all { it.isDigit() } &&
                    binding.textOrderDate.text!!.contains(",") -> {
                // добавление заказа на сервер
                val order = AddOrder(
                    ProductId = viewModel.idProduct,
                    Quantity = viewModel.stateFlow.value,
                    Size = viewModel.sizeProduct,
                    House = binding.textRoom.text.toString(),
                    Apartment = binding.textHome.text.toString(),
                    Etd = time
                )

                viewModel.addOrder(order)
            }

            else -> Log.d("MyTag", "Данные не все заполнены, проверяем условие")
        }
    }

}