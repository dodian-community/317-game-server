package net.dodian.orm.converters;

import org.springframework.stereotype.Service;

import javax.persistence.AttributeConverter;

@Service
public class StringToIntArrayConverter implements AttributeConverter<Integer[], String> {

    @Override
    public String convertToDatabaseColumn(Integer[] attributes) {
        if (attributes == null || attributes.length == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < attributes.length; i++) {
            builder.append(attributes[i]);
            if(i < (attributes.length - 1)) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    @Override
    public Integer[] convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) {
            return new Integer[]{};
        }

        Integer[] intArray;

        String[] stringArray = dbData.split(",");
        intArray = new Integer[stringArray.length];

        for(int i = 0; i < stringArray.length; i++) {
            intArray[i] = Integer.parseInt(stringArray[i]);
        }

        return intArray;
    }
}
