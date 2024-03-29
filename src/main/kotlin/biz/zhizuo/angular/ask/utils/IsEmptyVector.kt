package biz.zhizuo.angular.ask.utils

fun FloatArray?.isEmptyVector(): Boolean {
    return this == null || this.all { it == 0.0f }
}
