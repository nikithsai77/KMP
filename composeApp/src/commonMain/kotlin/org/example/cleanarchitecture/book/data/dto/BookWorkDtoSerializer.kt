package org.example.cleanarchitecture.book.data.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer

object BookWorkDtoSerializer: KSerializer<BookWorkDto> {

    //descriptor simply says that we are informing to serialisation these fields we considered while parsing.
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        BookWorkDto::class.simpleName!!
    ) {
        element<String?>(elementName = "description")
    }

    //decoder will gives the access of JSON Response here we can decode the response(convert the json response into Kotlin/Java obj).

    override fun deserialize(decoder: Decoder): BookWorkDto = decoder.decodeStructure(descriptor) {
        var description: String? = null
        while (true) {
            when(decodeElementIndex(descriptor = descriptor)) {
                0 -> {
                    val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("This decoder only works with JSON")
                    val element = jsonDecoder.decodeJsonElement()
                    description = if(element is JsonObject) {
                        decoder.json.decodeFromJsonElement(
                            element = element,
                            deserializer = DescriptionDto.serializer()
                        ).value
                    }
                    else if (element is JsonPrimitive && element.isString) element.content
                    else null
                }
                CompositeDecoder.DECODE_DONE -> break
            }
        }
        return@decodeStructure BookWorkDto(description = description)
    }

    //used to send the data to api by using json.
    override fun serialize(encoder: Encoder, value: BookWorkDto) = encoder.encodeStructure(descriptor) {
        value.description?.let {
            encodeStringElement(descriptor, index = 0, it)
        }
    }
}