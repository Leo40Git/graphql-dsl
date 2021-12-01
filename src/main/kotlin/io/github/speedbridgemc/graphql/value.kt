package io.github.speedbridgemc.graphql

sealed interface Value

data class VariableRef(val name: String) : Value

data class IntValue(val value: Int) : Value

data class FloatValue(val value: Float) : Value

data class StringValue(val value: String) : Value

sealed class BooleanValue : Value {
    companion object {
        @JvmStatic
        fun from(value: Boolean) = if (value) TrueValue else FalseValue
    }

    abstract val value: Boolean
}

object TrueValue : BooleanValue() {
    override val value = true
}

object FalseValue : BooleanValue() {
    override val value = false
}

object NullValue : Value

data class EnumValue(val name: String) : Value {
    init {
        if ((name == "null") or (name == "true") or (name == "false")) {
            throw IllegalArgumentException("Invalid enum name \"${name}\"")
        }
    }
}

class ListValue : Value, MutableList<Value> {
    companion object {
        @JvmStatic
        fun build(block: ListValueBuilder.() -> Unit): ListValue {
            return ListValueBuilder().also(block).build()
        }
    }
    
    private val delegate: MutableList<Value> = ArrayList()
    
    constructor()
    
    constructor(from: Collection<Value>) {
        addAll(from)
    }
    
    override val size: Int
        get() = delegate.size

    override fun contains(element: Value): Boolean = delegate.contains(element)

    override fun containsAll(elements: Collection<Value>): Boolean = delegate.containsAll(elements)

    override fun get(index: Int): Value = delegate.get(index)

    override fun indexOf(element: Value): Int = delegate.indexOf(element)

    override fun isEmpty(): Boolean = delegate.isEmpty()

    override fun iterator(): MutableIterator<Value> = delegate.iterator()

    override fun lastIndexOf(element: Value): Int = delegate.lastIndexOf(element)

    override fun add(element: Value): Boolean = delegate.add(element)

    override fun add(index: Int, element: Value) = delegate.add(index, element)

    override fun addAll(index: Int, elements: Collection<Value>): Boolean = delegate.addAll(index, elements)

    override fun addAll(elements: Collection<Value>): Boolean = delegate.addAll(elements)

    override fun clear() = delegate.clear()

    override fun listIterator(): MutableListIterator<Value> = delegate.listIterator()

    override fun listIterator(index: Int): MutableListIterator<Value> = delegate.listIterator(index)

    override fun remove(element: Value): Boolean = delegate.remove(element)

    override fun removeAll(elements: Collection<Value>): Boolean = delegate.removeAll(elements)

    override fun removeAt(index: Int): Value = delegate.removeAt(index)

    override fun retainAll(elements: Collection<Value>): Boolean = delegate.retainAll(elements)

    override fun set(index: Int, element: Value): Value = delegate.set(index, element)

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<Value> = delegate.subList(fromIndex, toIndex)
}

@GQLDslMarker
class ListValueBuilder {
    private val list: MutableList<Value> = ArrayList()
    
    fun add(value: Value) {
        list.add(value)
    }

    fun addVariableRef(varName: String) = add(VariableRef(varName))

    fun addInt(value: Int) = add(IntValue(value))

    fun addFloat(value: Float) = add(FloatValue(value))

    fun addString(value: String) = add(StringValue(value))

    fun addBoolean(value: Boolean) = add(BooleanValue.from(value))

    fun addNull(key: String) = add(NullValue)

    fun addEnum(name: String) = add(EnumValue(name))

    fun addList(block: ListValueBuilder.() -> Unit) = add(ListValue.build(block))

    fun addObject(block: ObjectValueBuilder.() -> Unit) = add(ObjectValue.build(block))

    internal fun build() = ListValue(list)
}

class ObjectValue : Value, MutableMap<String, Value> {
    companion object {
        @JvmStatic
        fun build(block: ObjectValueBuilder.() -> Unit): ObjectValue {
            return ObjectValueBuilder().also(block).build()
        }
    }

    private val delegate: MutableMap<String, Value> = HashMap()

    constructor()

    constructor(from: Map<out String, Value>) {
        putAll(from)
    }

    override val size: Int
        get() = delegate.size

    override fun containsKey(key: String): Boolean = delegate.containsKey(key)

    override fun containsValue(value: Value): Boolean = delegate.containsValue(value)

    override fun get(key: String): Value? = delegate[key]

    override fun isEmpty(): Boolean = delegate.isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<String, Value>>
        get() = delegate.entries
    override val keys: MutableSet<String>
        get() = delegate.keys
    override val values: MutableCollection<Value>
        get() = delegate.values

    override fun clear() = delegate.clear()

    override fun put(key: String, value: Value): Value? = delegate.put(key, value)

    override fun putAll(from: Map<out String, Value>) = delegate.putAll(from)

    override fun remove(key: String): Value? = delegate.remove(key)
}

@GQLDslMarker
class ObjectValueBuilder internal constructor() {
    private val map: MutableMap<String, Value> = HashMap()

    fun put(key: String, value: Value) {
        map[key] = value
    }

    fun putVariableRef(key: String, varName: String) = put(key, VariableRef(varName))

    fun putInt(key: String, value: Int) = put(key, IntValue(value))

    fun putFloat(key: String, value: Float) = put(key, FloatValue(value))

    fun putString(key: String, value: String) = put(key, StringValue(value))

    fun putBoolean(key: String, value: Boolean) = put(key, BooleanValue.from(value))

    fun putNull(key: String) = put(key, NullValue)

    fun putEnum(key: String, name: String) = put(key, EnumValue(name))

    fun putList(key: String, block: ListValueBuilder.() -> Unit) = put(key, ListValue.build(block))

    fun putObject(key: String, block: ObjectValueBuilder.() -> Unit) = put(key, ObjectValue.build(block))

    internal fun build() = ObjectValue(map)
}
