package io.github.speedbridgemc.graphql

fun main() {
    ObjectValue.build {
        putList("test") {
            addInt(21)
            addBoolean(false)
            addBoolean(true)
            addObject {
                putEnum("type", "AXE")
                putInt("damage", 10)
            }
            addList {
                addString("a")
                addString("b")
                addString("c")
            }
        }
    }
}