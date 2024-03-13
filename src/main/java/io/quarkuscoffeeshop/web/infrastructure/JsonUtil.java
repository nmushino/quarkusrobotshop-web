package io.quarkuscoffeeshop.web.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RegisterForReflection
public class JsonUtil {

    static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    
    static final ObjectMapper objectMapper = new ObjectMapper();

    static String toJson(final Object object) {

        logger.debug("marshalling {} to JSON", object.toString());

        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return "{ \"error\" : \"" + e.getMessage() + "\" }";
        }
    }

    // Bug　回避
    public static String[] readNode(String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode root = mapper.readTree(toJson(json));
        String str = root.toString();
        String arrayBaristaItems[] = new String[3];

        // Jsonデータ から文字列抽出
        String itemStr = "item=";
        int itemBeginIndex = str.indexOf(itemStr);
        String itemEndStr = ",";
        int itemEndIndex = str.indexOf(itemEndStr) + itemEndStr.length();
        String itemStrSubstring = str.substring(itemBeginIndex, itemEndIndex);
        String itemResult = itemStrSubstring.replace(itemStr, "").replace(itemEndStr, "");
        arrayBaristaItems[0] = itemResult;
        
        String priceStr = str.substring(itemEndIndex, str.length());
        String priceBeginStr = "price=";
        int priceBeginIndex = priceStr.indexOf(priceBeginStr);
        String priceEndStr = ",";
        int priceEndIndex = priceStr.indexOf(priceEndStr) + priceEndStr.length();
        String priceStrSubstring = priceStr.substring(priceBeginIndex, priceEndIndex);
        String priceResult = priceStrSubstring.replace(priceBeginStr, "").replace(priceEndStr, "");
        arrayBaristaItems[1] = priceResult;

        String nameStr = str.substring(priceEndIndex, str.length());
        String nameBeginStr = "name=";
        int nameBeginIndex = nameStr.indexOf(nameBeginStr);
        String nameEndStr = "]";
        int nameEndIndex = nameStr.indexOf(nameEndStr) + nameEndStr.length();
        String nameStrSubstring = nameStr.substring(nameBeginIndex, nameEndIndex);
        String nameResult = nameStrSubstring.replace(nameBeginStr, "").replace(nameEndStr, "").replace("'", "");
        if (nameResult == null) {
            nameResult = "";
        }
        arrayBaristaItems[2] = nameResult;

        return arrayBaristaItems;
    }
   
    public static String readState(String json) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode root = mapper.readTree(toJson(json));
        String str = root.toString();

        // Jsonデータ から文字列抽出
        String stateStr = "commandType=";
        int stateBeginIndex = str.indexOf(stateStr);
        String stateEndStr = ",";
        int stateEndIndex = str.indexOf(stateEndStr) + stateEndStr.length();
        String stateStrSubstring = str.substring(stateBeginIndex, stateEndIndex);
        String stateResult = stateStrSubstring.replace(stateStr, "").replace(stateEndStr, "");
        
        return stateResult;
    }
    
}