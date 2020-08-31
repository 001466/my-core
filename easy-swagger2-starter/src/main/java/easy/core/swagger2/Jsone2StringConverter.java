package easy.core.swagger2;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.convert.converter.Converter;
//import springfox.documentation.spring.web.json.Json;
//
//import java.text.SimpleDateFormat;
//import java.util.Locale;
//@Slf4j
//public class Jsone2StringConverter implements Converter<Json, String> {
//
//    static ObjectMapper mapper = new ObjectMapper();
//    static {
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
//    }
//
//    @Override
//    public String convert(Json source) {
//        try {
//            return mapper.writeValueAsString(source);
//        } catch (JsonProcessingException e) {
//            log.error(e.getMessage(),e);
//        }
//
//        return null;
//    }
//}