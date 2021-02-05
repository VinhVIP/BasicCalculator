package com.vinh.basiccalculator

import java.util.*

/*
Nếu cảm thấy khó hiểu thì các bạn có thể copy code class này cho nó lẹ :D
 */

class Calculator(var expression: String) {

    var stk: Stack<Char> = Stack() // Stack dùng cho quá trình chuyển biểu thức infix -> postfix

    val operator: List<Char> = listOf('(', ')', '+', '-', '*', '/')

    /*
    Định dạng lại biểu thức bằng cách thêm các dấu cách xung quanh mỗi toán tử
     */
    private fun refine(s: String): String {
        var a: String = s
        for (c in operator) {
            a = a.replace(c.toString(), " $c ")
        }
        return a
    }

    /*
    Phương thức trả về độ ưu tiên của mỗi toán tử
     */
    private fun priority(c: Char): Int {
        when (c) {
            '*', '/' -> return 2
            '+', '-' -> return 1
            '(' -> return 0
        }
        return 0
    }

    /*
    Phương thức này xử lý thêm / xóa vào Stack
    Trả về 1 chuỗi là 1 phần của chuỗi hậu tố tương ứng
     */
    private fun process(s: String): String {
        if (s.isEmpty()) return s

        var c: Char = s[0]
        var x: Char?
        var res: String = ""

        if (c !in operator) return "$s "
        else {
            when (c) {
                '(' -> stk.push(c)
                ')' -> {
                    while (stk.isNotEmpty()) {
                        x = stk.pop()
                        if (x != '(') res += "$x "
                        else break
                    }
                }
                '+', '-', '*', '/' -> {
                    while (stk.isNotEmpty()) {
                        x = stk.pop()
                        if (priority(c) <= priority(x)) {
                            res += "$x "
                        } else {
                            stk.push(x)
                            break
                        }
                    }
                    stk.push(c)
                }
            }
        }
        return res
    }

    /*
    Trả về biểu thức hậu tố (postfix) từ dạng trung tố (infix)
     */
    fun toPostfix(): String {

        // Định dạng lại chuỗi biểu thức (trung tố) cho dễ đọc
        var infix: String = refine(expression) + " "

        var s: String = ""  // Chuỗi trung gian
        var postfix: String = ""  // Lưu trữ giá trị chuỗi hậu tố

        stk.clear()

        for (c in infix) {
            if (c != ' ') s += c
            else {
                postfix += process(s)
                s = ""
            }
        }

        while (stk.isNotEmpty()) {
            postfix += stk.pop() + " "
        }

        return postfix
    }

    /*
    Phương thức này tính toán và trả về kết quả của biểu thức
    Nếu biểu thức sai định dạng hoặc gặp lỗi tính toán như chia cho 0 thì sẽ ném ra 1 ngoại lệ
     */
    @Throws(InvalidExpressionException::class)
    fun calculate(): Double {
        /*
        try catch này bắt toàn bộ những Exception bị ném ra trong quá trình thức thi
        Và ta quy nó là lỗi do "Biểu thức sai" :))))
         */
        try {
            var postfix: String = toPostfix()     // Chuỗi biểu thức hậu tố

            var stkOperand: Stack<Double> =
                Stack()     // Stack lưu trữ các toán hạng để lấy dần ra tính toán

            var arr: List<String> = postfix.split(' ')  // Tách các toán hạng và toán tử thành mảng

            for (s in arr) {
                // Duyệt từng toán hạng và toán tử

                if (s.isEmpty()) continue

                // Nếu phần tử là toán hạng thì đẩy vào Stack
                if (s[0] !in operator) stkOperand.push(s.toDouble())
                else {
                    // Lấy 2 giá trị trên đỉnh Stack
                    // Lưu ý lấy đúng thức tự y và x
                    var y = stkOperand.pop()
                    var x = stkOperand.pop()

                    var res: Double = 0.0

                    when (s) {
                        "+" -> res = x + y
                        "-" -> res = x - y
                        "*" -> res = x * y
                        "/" -> {
                            if (y == 0.0) throw DivideZeroException()
                            else res = x / y
                        }
                    }

                    stkOperand.push(res)    // Đẩy kết quả phép tính vào lại Stack
                }
            }
            // Nếu biểu thức đúng định dạng thì lúc này Stack chỉ còn duy nhất 1 phần tử là kết quả của biểu thức
            return stkOperand.pop()

        } catch (e: DivideZeroException) {
            throw InvalidExpressionException("Không thể chia cho 0")
        } catch (e: Exception) {
            throw InvalidExpressionException("Biểu thức nhập sai định dạng hoặc chia cho 0")
        }
    }

}