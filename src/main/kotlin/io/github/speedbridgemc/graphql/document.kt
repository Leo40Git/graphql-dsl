package io.github.speedbridgemc.graphql

class Document internal constructor() {
}

class DocumentBuilder internal constructor() {
    internal fun build(): Document {
        return Document()
    }
}
