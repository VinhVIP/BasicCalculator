package com.vinh.basiccalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.key_board.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var calculator: Calculator = Calculator("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)
        btn7.setOnClickListener(this)
        btn8.setOnClickListener(this)
        btn9.setOnClickListener(this)
        btnPoint.setOnClickListener(this)
        btnPlus.setOnClickListener(this)
        btnSubtract.setOnClickListener(this)
        btnMultiply.setOnClickListener(this)
        btnDivide.setOnClickListener(this)
        btnOpen.setOnClickListener(this)
        btnClose.setOnClickListener(this)
        btnDivide.setOnClickListener(this)


        // Xóa toàn bộ nội dung
        btnClear.setOnClickListener {
            tvExpression.text = ""
        }

        // Chỉ xóa 1 kí tự sau cùng
        btnDelete.setOnClickListener {
            deleteLast()
        }

        // Tính toán giá trị biểu thức
        btnCalculate.setOnClickListener {
            try {
                calculator.expression = tvExpression.text.toString()
                var result: Double = calculator.calculate()

                // Kiểm tra kết quả trả về có phải là số nguyên hay không
                // Ví dụ result = 5.0 thì ta sẽ hiển thị là 5 cho gọn
                var i: Long = result.toLong()
                if (i.toDouble() == result) tvResult.text = i.toString()
                else tvResult.text = result.toString()
            } catch (e: InvalidExpressionException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

        }

    }

    // thêm s vào cuối nội dung của tvExpression
    private fun appendToExpression(s: String) {
        tvExpression.text = tvExpression.text.toString() + s
    }

    // Xóa kí tự cuối của edExpression
    private fun deleteLast() {
        if (tvExpression.text.isNotEmpty()) {
            tvExpression.text =
                tvExpression.text.toString().substring(0, tvExpression.text.length - 1)
        }
    }

    // Xử lý sự kiện click cho các button đã đăng kí sự kiện
    override fun onClick(v: View?) {
        val btn: Button = v as Button

        var btnText = btn.text.toString()
        checkInput(btnText[0])
    }

    /*
    Hàm này kiểm tra đầu và thực hiện 1 số thay đổi đầu vào cho hợp lý thôi :)))
    Các bạn có thể thêm vào 1 số điều kiện cho phù hợp nếu muốn
     */
    private fun checkInput(op: Char) {
        if (op !in calculator.operator) {
            appendToExpression(op.toString())
            return
        }

        if (tvExpression.text.isEmpty()) {
            if (op == '(') appendToExpression(op.toString())
        } else {
            var last: Char = tvExpression.text[tvExpression.text.length - 1]
            if (last in listOf('+', '-', '*', '/', '.') && op != '(') {
                tvExpression.text =
                    tvExpression.text.toString().substring(0, tvExpression.text.length - 1) + op

            } else {
                appendToExpression(op.toString())
            }
        }
    }
}