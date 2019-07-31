package org.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

/**
 * Created by geely
 *
 * Json 工具类 - jackson
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //序列化所有字段
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //禁用序列化时空Bean报错
        objectMapper.disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
        //禁用反序列化时未知属性报错
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        //禁用日期类型使用timestamps序列化格式
        objectMapper.disable(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS);

        //设置日期类型序列化和反序列化格式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

    }

    /**
     * 对象 转 字符串 （适用集合）
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    /**
     * 对象 转 字符串并格式化 （适用集合）
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }


    /**
     * 字符串 转 对象 （不适用集合，会得到 List<LinkedHashMap> ）
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 字符串 转 对象 （适用集合）
     *
     * <pre>
     *     List<User> userListObj = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {});
     * </pre>
     *
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }


    /**
     * 字符串 转 对象 （适用集合）
     *
     * <pre>
     *     List<User> userListObj = JsonUtil.string2Obj(userListStr, List.class, User.class);
     * </pre>
     *
     * @param str
     * @param collectionClass
     * @param elementClasses
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }


    public static void main(String[] args) {
//        TestPojo testPojo = new TestPojo();
//        testPojo.setName("Geely");
//        testPojo.setId(666);
//
//        //{"name":"Geely","id":666}
//        String json = "{\"name\":\"Geely\",\"color\":\"blue\",\"id\":666}";
//        TestPojo testPojoObject = JsonUtil.string2Obj(json, TestPojo.class);
////        String testPojoJson = JsonUtil.obj2String(testPojo);
////        log.info("testPojoJson:{}",testPojoJson);
//
//        log.info("end");

//        User user = new User();
//        user.setId(2);
//        user.setEmail("geely@happymmall.com");
//        user.setCreateTime(new Date());
//        String userJsonPretty = JsonUtil.obj2StringPretty(user);
//        log.info("userJson:{}",userJsonPretty);


//        User u2 = new User();
//        u2.setId(2);
//        u2.setEmail("geelyu2@happymmall.com");
//
//
//
//        String user1Json = JsonUtil.obj2String(u1);
//
//        String user1JsonPretty = JsonUtil.obj2StringPretty(u1);
//
//        log.info("user1Json:{}",user1Json);
//
//        log.info("user1JsonPretty:{}",user1JsonPretty);
//
//
//        User user = JsonUtil.string2Obj(user1Json,User.class);
//
//
//        List<User> userList = Lists.newArrayList();
//        userList.add(u1);
//        userList.add(u2);
//
//        String userListStr = JsonUtil.obj2StringPretty(userList);
//
//        log.info("==================");
//
//        log.info(userListStr);
//
//
//        List<User> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<User>>() {
//        });
//
//
//        List<User> userListObj2 = JsonUtil.string2Obj(userListStr,List.class,User.class);

//        System.out.println("end");

    }


}
