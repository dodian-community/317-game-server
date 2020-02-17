package net.dodian.orm.converters;

import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;

@Service
public class StringToDoubleArrayConverter implements AttributeConverter<Double[], String> {

    @Override
    public String convertToDatabaseColumn(Double[] attributes) {
        if (attributes == null || attributes.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < attributes.length; i++) {
            builder.append(attributes[i]);
            if (i < (attributes.length - 1)) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    @Override
    public Double[] convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) {
            return new Double[]{};
        }

        Double[] doubleArray;

        String[] stringArray = dbData.split(",");
        doubleArray = new Double[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            doubleArray[i] = Double.parseDouble(stringArray[i]);
        }

        return doubleArray;
    }
}
