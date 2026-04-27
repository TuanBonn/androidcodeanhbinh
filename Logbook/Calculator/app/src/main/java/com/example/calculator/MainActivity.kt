package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private lateinit var edtNumber1: EditText
    private lateinit var edtNumber2: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnSubtract: Button
    private lateinit var btnMultiply: Button
    private lateinit var btnDivide: Button
    private lateinit var btnClear: Button // Thêm mới
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo các View
        edtNumber1 = findViewById(R.id.edtNumber1)
        edtNumber2 = findViewById(R.id.edtNumber2)
        tvResult = findViewById(R.id.tvResult)
        btnAdd = findViewById(R.id.btnAdd)
        btnSubtract = findViewById(R.id.btnSubtract)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)
        btnClear = findViewById(R.id.btnClear)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnAdd.setOnClickListener { performCalculation { a, b -> a + b } }
        btnSubtract.setOnClickListener { performCalculation { a, b -> a - b } }
        btnMultiply.setOnClickListener { performCalculation { a, b -> a * b } }

        btnDivide.setOnClickListener {
            if (validateInput()) {
                val num1 = edtNumber1.text.toString().toDouble()
                val num2 = edtNumber2.text.toString().toDouble()
                if (num2 == 0.0) {
                    tvResult.text = "Kết quả: Không thể chia cho 0"
                } else {
                    val result = num1 / num2
                    tvResult.text = "Kết quả: ${formatResult(result)}"
                }
            }
        }

        // Xử lý nút Clear
        btnClear.setOnClickListener {
            edtNumber1.text.clear()
            edtNumber2.text.clear()
            tvResult.text = "Kết quả: "
        }
    }

    // Hàm rút gọn logic tính toán
    private fun performCalculation(operation: (Double, Double) -> Double) {
        if (validateInput()) {
            val num1 = edtNumber1.text.toString().toDouble()
            val num2 = edtNumber2.text.toString().toDouble()
            val result = operation(num1, num2)
            tvResult.text = "Kết quả: ${formatResult(result)}"
        }
    }

    private fun validateInput(): Boolean {
        val strNum1 = edtNumber1.text.toString()
        val strNum2 = edtNumber2.text.toString()

        if (strNum1.isEmpty() || strNum2.isEmpty()) {
            tvResult.text = "Lỗi: Vui lòng nhập đủ 2 số"
            return false
        }

        return try {
            strNum1.toDouble()
            strNum2.toDouble()
            true
        } catch (e: NumberFormatException) {
            tvResult.text = "Lỗi: Định dạng số không hợp lệ"
            return false
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) {
            value.toInt().toString()
        } else {
            String.format("%.6f", value).trimEnd('0').trimEnd('.')
        }
    }
}
