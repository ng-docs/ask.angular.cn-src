package biz.zhizuo.angular.ask.utils

fun Iterable<Double>.toFloatArray(): FloatArray {
    return this.map { it.toFloat() }.toFloatArray()
}
