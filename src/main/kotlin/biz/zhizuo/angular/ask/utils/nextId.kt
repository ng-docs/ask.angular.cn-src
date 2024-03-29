package biz.zhizuo.angular.ask.utils

import com.fasterxml.uuid.Generators

fun nextId(): String {
    return Generators.timeBasedReorderedGenerator().generate().toBase36()
}
