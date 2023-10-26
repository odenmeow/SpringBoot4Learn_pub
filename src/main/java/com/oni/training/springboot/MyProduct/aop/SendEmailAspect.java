package com.oni.training.springboot.MyProduct.aop;


import com.google.api.services.gmail.model.Message;
import com.oni.training.springboot.MyProduct.auth.JwtService;
import com.oni.training.springboot.MyProduct.auth.auth_user.SpringUser;
import com.oni.training.springboot.MyProduct.auth.auth_user.UserIdentity;
import com.oni.training.springboot.MyProduct.entity.mail.SendMailRequest;
import com.oni.training.springboot.MyProduct.service.MailService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Component
@Aspect

//@EnableAspectJAutoProxy
public class SendEmailAspect {

    @Autowired
    private UserIdentity userIdentity;
    @Autowired
    private MailService mailService;
    @Autowired
    private JwtService jwtService;

    private static final Map<ActionType,String> SUBJECT_TEMPLATE_MAP;
    private static final Map<ActionType,String> MESSAGE_TEMPLATE_MAP;
    static{
        SUBJECT_TEMPLATE_MAP=new EnumMap<>(ActionType.class);
        SUBJECT_TEMPLATE_MAP.put(ActionType.CREATE,"New %s");
        SUBJECT_TEMPLATE_MAP.put(ActionType.UPDATE,"Update %s");
        SUBJECT_TEMPLATE_MAP.put(ActionType.DELETE,"Delete %s");

        MESSAGE_TEMPLATE_MAP=new EnumMap<>(ActionType.class);
        MESSAGE_TEMPLATE_MAP.put(ActionType.CREATE,"Hi, %s. There's a new %s (%s) created.");
        MESSAGE_TEMPLATE_MAP.put(ActionType.UPDATE,"Hi, %s. There's a %s (%s) updated.");
        MESSAGE_TEMPLATE_MAP.put(ActionType.DELETE,"Hi, %s. A %s (%s) is just deleted.");
    }

    @Pointcut("@annotation(com.oni.training.springboot.MyProduct.aop.SendEmail)")
    public void pointcut(){
    }
//     擁有注解的方法如果發生例外 則不會觸發這邊 而應該使用@AfterThrowing
//     或者不管錯誤 就是要做 那也能考慮 @After
    @AfterReturning(pointcut = "pointcut()",returning = "result")
    public void sendEmail(JoinPoint joinPoint,Object result) throws Exception {

        SendEmail annotation=getAnnotation(joinPoint);
        if(userIdentity.isAnonymous()){

            // 使用者為未紀錄
            System.out.println("無登入，產品操作，不寄信");
            // 同一個server只會有一個enum的實例
            if(annotation.entity().equals(EntityType.APP_USER)){
                String subject=composeSubject(annotation);
                String message=composeMessage(annotation,joinPoint,result);
                SendMailRequest sendMailRequest=
                        new SendMailRequest().builder()
                                .subject(subject)
                                .content(message)
                                .receivers(
                                        Collections.singletonList(getEntityEmail(result))
                                                .toArray(new String[0]))
                                .build();
                System.out.println("即將送出信件!!!!!!!!!!");
                mailService.sendMail(sendMailRequest);
            }
            return;

        }
        String subject=composeSubject(annotation);
        String message=composeMessage(annotation,joinPoint,result);
        SendMailRequest sendMailRequest=
                new SendMailRequest().builder()
                        .subject(subject)
                        .content(message)
                        .receivers(
                                Collections.singletonList(userIdentity.getEmail())
                                .toArray(new String[0]))
                        .build();
        System.out.println("即將送出信件!!!!!!!!!!");
        mailService.sendMail(sendMailRequest);

    }
    private SendEmail getAnnotation(JoinPoint joinPoint){
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        return signature.getMethod().getAnnotation(SendEmail.class);
    }

    private String composeSubject(SendEmail annotation){
        String template =SUBJECT_TEMPLATE_MAP.get(annotation.action());
        return String.format(template,annotation.entity());
    }
    // entity是 result 也就是回傳的結果中會去找有沒有 id
    private String composeMessage(SendEmail annotation,JoinPoint joinPoint,Object entity){
        String template=MESSAGE_TEMPLATE_MAP.get(annotation.action());
        int idParamIndex= annotation.idParamIndex();

//                String entityId=idParamIndex==-1
//                                ?getEntityId(entity)
//                                :(String) joinPoint.getArgs()[idParamIndex];      原本的寫法 改成下面讓email建立時可以通用
        String entityId="strange";
        switch (idParamIndex){
            case -2:
                entityId=getEntityEmail(entity); //entity=原始方法回傳值
                break;
            case -1:
                entityId=getEntityId(entity);
                break;
            default:
                entityId=(String) joinPoint.getArgs()[idParamIndex];
                break;
        }

        // entity是 result 會從回傳的結果中會去找有沒有 id
        // inParamIndex -1  > 從回傳中的ResponseEntity<Product>之類找 id
        //               0  > 從參數列找 id  (例如delete(String id))
        return String.format(template,
                userIdentity.getName()==null?getEntityName(entity):userIdentity.getName(),
                annotation.entity(),entityId);
    }

    private String getEntityName(Object obj) {
        try {
            Field field=obj.getClass().getDeclaredField("name");
            field.setAccessible(true);
            return (String)field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getEntityEmail(Object obj){
        try {
            Field field=obj.getClass().getDeclaredField("token");
            field.setAccessible(true);
            return jwtService.extractUsername((String)field.get(obj));

        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
            return "";
        }
    }
    private String getEntityId(Object obj){
        try{
            Field field =obj.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (String) field.get(obj);
            // |  可以捕捉多個異常 || 若A成立B就捉不到 <catch>只能 | 不能 || ，||其他地方可以
        }catch (NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
            return "";
        }
    }
}
