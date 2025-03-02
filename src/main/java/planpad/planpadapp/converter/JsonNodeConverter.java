package planpad.planpadapp.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter(autoApply = true)
@RequiredArgsConstructor
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {

        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Json → String 변환 실패", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {

        try {
            return objectMapper.readTree(dbData);
        } catch (Exception e) {
            throw new RuntimeException("String → Json 변환 실패", e);
        }
    }
}
