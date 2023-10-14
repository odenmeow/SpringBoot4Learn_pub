package com.oni.training.springboot.integration.MyProduct;

import com.oni.training.springboot.ConverterTest.ChildOrigin;
import com.oni.training.springboot.ConverterTest.Child_2;
import com.oni.training.springboot.ConverterTest.MyConverter;
import com.oni.training.springboot.ConverterTest.ancestor;
import jakarta.validation.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;


public class man {
        @Test
        public  void mai() {

            ApplicationContext ac=new AnnotationConfigApplicationContext(MyConverter.class);

            ((ConfigurableApplicationContext)ac).close();
            System.out.println("========anno搞完換別人搞========");
            ChildOrigin co=new ChildOrigin(null,0,0);

//            hi(@Valid co);
//          上面無效 必須要在controller才行
//            手動驗證
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();


            // 手动验证对象
            Set<ConstraintViolation<ChildOrigin>> violations = validator.validate(co);

            if (!violations.isEmpty()) {
                for (ConstraintViolation<ChildOrigin> violation : violations) {
                    System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
                }
            } else {
                System.out.println("对象验证通过");
                //失敗(如果沒自定義)則會 name: 不得是空的
            }
        }
        // 無論如何，只能夠訪問 父類的屬性跟方法而已 沒辦法透過注入 訪問子類的屬性
        public static void hi(ancestor a){
            System.out.println("你好"+a.name);
            Child_2 c=new Child_2();
            System.out.println(c.name);
            c.setName(null);
        }

    }

