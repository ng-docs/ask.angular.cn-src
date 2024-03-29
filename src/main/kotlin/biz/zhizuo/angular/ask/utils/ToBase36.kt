package biz.zhizuo.angular.ask.utils

import java.math.BigInteger
import java.util.*

fun UUID.toBase36(): String {
    val value = BigInteger(toString().replace("-", ""), 16)
    return value.toString(36)
}
