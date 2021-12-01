package io.github.speedbridgemc.graphql

object GraphQL {
    fun document(block: DocumentBuilder.() -> Unit): Document {
        return DocumentBuilder().also(block).build()
    }
}