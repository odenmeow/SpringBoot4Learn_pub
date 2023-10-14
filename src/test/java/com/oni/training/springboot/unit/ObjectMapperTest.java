package com.oni.training.springboot.unit;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

// 這邊歸這邊 八、區隔回應主體與資料庫文件 寫到Product資料夾下
public class ObjectMapperTest {
    // 下面是本來就有被提供的 不是我建立的喔!
    private ObjectMapper mapper=new ObjectMapper();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    // null的對象將不會輸出
    private static class Book{
        private String id;
        private String name;
        private int price;
        @JsonIgnore  //序列化輸出 會忽略此項
        private String isbn;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        使日期可以轉換成指定格式輸出
        private Date createdTime; //我使用util
        @JsonUnwrapped
        private Publisher publisher;
//        加入 @JsonUnwrapped 之前
//{
//    "id": "B0001",
//    "name": "Computer Science",
//    "price": 350,
//    "isbn": "978-986-123-456-7",
//    "createdTime": 1585493050168,
//    "publisher": {
//        "companyName": "Taipei company",
//        "address": "Taipei",
//        "tel": "02-1234-5678"
//    }
//}
//        =================================
//        加入之後
//{
//    "id": "B0001",
//    "name": "Computer Science",
//    "price": 350,
//    "isbn": "978-986-123-456-7",
//    "createdTime": 1585493050168,
//    "companyName": "Taipei company",
//    "address": "Taipei",
//    "tel": "02-1234-5678"
//}     小心將物件展開後，發生欄位名稱相同
//        @JsonProperty  使用這個來調整

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Date getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(Date createdTime) {
            this.createdTime = createdTime;
        }

        public Publisher getPublisher() {
            return publisher;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
        }
    }
    private static class Publisher{
        private String companyName;
        private String address;
        @JsonProperty("telephone")
        private String tel;
//  由於         @JsonProperty("telephone")    所以序列化會輸出
//{
//    "companyName": "Taipei Company"
//    "address": "Taipei",
//    "telephone": "02-1234-5678"
//}
        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

    @Test
    public void testSerializeBookToJSON() throws Exception {
        Book book=new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        // getter setter 有就可以傳進去 也就是JAVABean 對象可以進去
        String bookJSONStr=mapper.writeValueAsString(book);
        JSONObject bookJSON=new JSONObject(bookJSONStr);

        Assert.assertEquals(book.getId(),bookJSON.getString("id"));
        Assert.assertEquals(book.getName(),bookJSON.getString("name"));
        Assert.assertEquals(book.getPrice(),bookJSON.getInt("price"));
        Assert.assertEquals(book.getIsbn(),bookJSON.getString("isbn"));
        Assert.assertEquals(book.getCreatedTime().getTime(),bookJSON.getLong("createdTime"));
        // Date 物件 getTime 得到long
        //Expected :Sun Sep 24 19:59:31 CST 2023
        //Actual   :1695556771744

    }
    @Test
    public void testDeserializeJSONtoPublisher() throws JSONException, JsonProcessingException {
        JSONObject publisherJSON =new JSONObject()
                // 使用 put 要幫她丟exception  (JSONException)
                .put("companyName","Taipei Company")
                .put("address","Taipei")
                .put("tel","02-1234-5678");

        String publisherJSONStr=publisherJSON.toString();
        // 下面又有 (JsonProcessingException)
        Publisher publisher=mapper.readValue(publisherJSONStr,Publisher.class);
        // 上面這個 很方便就能把字串轉換成對應物件，只是
        Assert.assertEquals(publisherJSON.getString("companyName"),publisher.getCompanyName());
        Assert.assertEquals(publisherJSON.getString("address"),publisher.getAddress());
        Assert.assertEquals(publisherJSON.getString("tel"),publisher.getTel());

    }




}
