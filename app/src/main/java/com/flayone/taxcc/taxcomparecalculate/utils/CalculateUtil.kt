package com.flayone.taxcc.taxcomparecalculate.utils

import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import java.math.BigDecimal


/**
 * Created by liyayu on 2018/6/20.
 * 金额计算相关
 */
//设置金额输入小数点后2位有效
fun keepEditTwoPoint(editText: EditText) {
    editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
    editText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
        // 在无内容或者现有内容全选，输入.时自动添加0
        if (source == "." && (dest.toString().isEmpty() || dstart == 0 && dend == dest.length)) {
            return@InputFilter "0."
        }
        // 获取新的string
        val newString = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
        // 如果新的string超过1个.就认为不合法
        if (newString.indexOf(".") != newString.lastIndexOf(".")) {
            return@InputFilter ""
        }
        // 如果新的string有.且小数超过2位就认为不合法
        if (newString.contains(".")) {
            val index = newString.indexOf(".")
            if (newString.length - index - 1 > 2) {
                return@InputFilter ""
            }
        }
        //相当于maxLength
//        if (newString.length > 10) {
//            ""
//        } else null
        return@InputFilter null
    })
}

// + 高精度加法，避免出现0.2+0.1二进制转换失准情况
fun add(v1: String, v2: String): String {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        //            return b1.add(b2).toString();
        b1.add(b2).stripTrailingZeros().toPlainString()//去掉无用的小数点以及末尾的0
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }

}

// + 多个相加
fun add(vararg v1: String): String {
    return try {
        var a1 = BigDecimal("0")
        var b1: BigDecimal
        for (s in v1) {
            b1 = BigDecimal(if (s.isEmpty()) "0" else s)
            a1 = a1.add(b1)
        }
        a1.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }
}

fun subtract(vararg ss: String): String {
    return try {
        var a1 = BigDecimal(if (ss[0].isEmpty()) "0" else ss[0])
        var b1: BigDecimal
        for (i in 1 until ss.size) {
            b1 = BigDecimal(if (ss[i].isEmpty()) "0" else ss[i])
            a1 = a1.subtract(b1)
        }
        a1.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }
}

// - 减法
fun subtract(v1: String, v2: String): String {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        b1.subtract(b2).stripTrailingZeros().toPlainString()//去掉无用的小数点以及末尾的0
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }

}

/**
 * -
 *
 * @param v1
 * @param v2
 * @param scale 保留小数位数，采用四舍五入方式
 * @return
 */
fun subtract(v1: String, v2: String, scale: Int): String {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }

}

// *
fun multiply(v1: String, v2: String): String {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        b1.multiply(b2).toString()
    } catch (e: Exception) {
        e.printStackTrace()

        "0"
    }

}

/**
 * @param v1
 * @param v2
 * @param scale 保留小数位数，采用四舍五入方式
 * @return
 */
fun multiply(v1: String, v2: String, scale: Int): String {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString()
    } catch (e: Exception) {
        e.printStackTrace()

        "0"
    }

}

// 比大小，-1：小于 0：等于 1：大于
fun compareTo(v1: String, v2: String): Int {
    return try {
        val b1 = BigDecimal(if (v1.isEmpty()) "0" else v1)
        val b2 = BigDecimal(if (v2.isEmpty()) "0" else v2)
        b1.compareTo(b2)
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }

}

/**
 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
 *
 * @param v1    被除数
 * @param v2    除数
 * @param scale 表示表示需要精确到小数点以后几位。
 * @return 两个参数的商
 */

fun div(v1: String, v2: String, scale: Int): String {
    if (scale < 0) {
        throw IllegalArgumentException(
                "The scale must be a positive integer or zero")
    }
    return try {
        val b1 = BigDecimal(v1)
        val b2 = BigDecimal(v2)
        b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        "0"
    }
}

fun shortMoney(ori: String): String {
    if (ori.toDouble() <= 999.99) {
        return ori
    }
    if (ori.toDouble() <= 999999.99) {
        return "${div(ori, "1000", 1)}K"
    }
    return "${div(ori, "1000000", 1)}M"
}
fun shortYearMoney(ori: String): String{
    if (ori.toDouble() <= 9999.99) {
        return ori
    }
    if (ori.toDouble() <= 999999.99) {
        return "${div(ori, "10000", 1)}W"
    }
    return "${div(ori, "1000000", 1)}M"
}
fun calculateYearTaxRate(s: String): String {
    val result = when (s.toDouble()) {
        in yearLevelList[0] until yearLevelList[1] -> taxRateList[0]
        in yearLevelList[1] until yearLevelList[2] -> taxRateList[1]
        in yearLevelList[2] until yearLevelList[3] -> taxRateList[2]
        in yearLevelList[3] until yearLevelList[4] -> taxRateList[3]
        in yearLevelList[4] until yearLevelList[5] -> taxRateList[4]
        in yearLevelList[5] until yearLevelList[6] -> taxRateList[5]
        in yearLevelList[6] until Int.MAX_VALUE -> taxRateList[6]
        else -> {
            ""
        }
    }
    return "${multiply(result, "100")}%"
}